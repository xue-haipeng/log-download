package com.example;

import com.example.utils.WlsJmxMonitorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weblogic.health.HealthState;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

/**
 * Created by Xue on 01/22/17.
 */
public class DomainRuntimeCheck {

    private static final Logger logger = LoggerFactory.getLogger(DomainRuntimeCheck.class);
    private static MBeanServerConnection connection;
    private static JMXConnector connector;
    private static final ObjectName service;

    private final Executor executor = Executors.newFixedThreadPool(25, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    });

    static {
        try {
            service = new ObjectName(
                    "com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
        }catch (MalformedObjectNameException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    public static void initConnection(String hostname, String portString, String username, String password) throws IOException {
        String protocol = "t3";
        Integer portInteger = Integer.valueOf(portString);
        int port = portInteger.intValue();
        String jndiroot = "/jndi/";
        String mserver = "weblogic.management.mbeanservers.domainruntime";
        JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname, port, jndiroot + mserver);
        Hashtable h = new Hashtable();
        h.put(Context.SECURITY_PRINCIPAL, username);
        h.put(Context.SECURITY_CREDENTIALS, password);
        h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
        h.put("jmx.remote.x.request.waiting.timeout", new Long(10000));
        connector = JMXConnectorFactory.connect(serviceURL, h);
        connection = connector.getMBeanServerConnection();
    }

    public static ObjectName[] getServerRuntimes() throws Exception {
        return (ObjectName[]) connection.getAttribute(service,"ServerRuntimes");
    }

    public List<Map<String, String>> printNameAndState() throws Exception {
        ObjectName[] serverRT = getServerRuntimes();
        List<CompletableFuture<Map<String, String>>> futureList = Arrays.stream(serverRT).map(server -> CompletableFuture.supplyAsync(
                () -> {
                    Map<String, String> map = new HashMap<>();
                    try {
                        String name = (String) connection.getAttribute(server,"Name");
                        String state = (String) connection.getAttribute(server,"State");
                        HealthState healthState = (HealthState) connection.getAttribute(server, "HealthState");
                        String serverHealthState = HealthState.mapToString(healthState.getState());
                        logger.debug("serverName: {}, serverState, {}, serverHealth, {}");
                        map.put("serverName", name);
                        map.put("serverState", state);
                        map.put("serverHealth", serverHealthState);
                        ObjectName jvmRT = (ObjectName) connection.getAttribute(server,"JVMRuntime");
                        long heapSizeCurrent = (long) connection.getAttribute(jvmRT,"HeapSizeCurrent");
                        long heapFreeCurrent = (long) connection.getAttribute(jvmRT,"HeapFreeCurrent");
                        int heapFreePercent = (int) connection.getAttribute(jvmRT,"HeapFreePercent");
                        logger.debug("HeapSizeCurrent: " + heapSizeCurrent + ", HeapFreeCurrent: " + heapFreeCurrent + ", HeapFreePercent: " + heapFreePercent);
                        map.put("heapSizeCurrent", heapSizeCurrent/1024/1024 + "M");
                        map.put("heapFreeCurrent", heapFreeCurrent/1024/1024 + "M");
                        map.put("heapFreePercent", heapFreePercent + "%");
                        ObjectName[] appRT = (ObjectName[]) connection.getAttribute(server,"ApplicationRuntimes");
                        Optional<ObjectName> optAppRT = Arrays.stream(appRT).filter(app -> {
                            try {
                                return "CurrentApps".equals((String) connection.getAttribute(app,"Name"));
                            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }).findAny();
                        optAppRT.ifPresent(app -> {
                            try {
                                String appName = (String) connection.getAttribute(app,"Name");
                                HealthState hs = (HealthState) connection.getAttribute(app,"HealthState");
                                String appHealthState = HealthState.mapToString(hs.getState());
                                int sc = (int) connection.getAttribute(app,"ActiveVersionState");
                                String appActiveState = WlsJmxMonitorUtils.activeStateMapToString(sc);
                                logger.debug("appName: {}, appHealthState: {}, appActiveState: {}", appName, appHealthState, appActiveState);
                                map.put("appName", appName);
                                map.put("appHealthState", appHealthState);
                                map.put("appActiveState", appActiveState);
                                ObjectName[] compRT = (ObjectName[]) connection.getAttribute(app,"ComponentRuntimes");
                                if (compRT.length == 2) {
                                    if ("WebAppComponentRuntime".equals(connection.getAttribute(compRT[1],"Type"))) {
                                        int openSessionsCurrentCount = (int) connection.getAttribute(compRT[1],"OpenSessionsCurrentCount");
                                        String openSessions = Integer.toString(openSessionsCurrentCount);
                                        logger.debug("OpenSessionsCurrentCount: " + openSessions);
                                        map.put("openSessions", openSessions);
                                    }
                                } else if (compRT.length == 1) {
                                    if ("WebAppComponentRuntime".equals(connection.getAttribute(compRT[0],"Type"))) {
                                        int openSessionsCurrentCount = (int) connection.getAttribute(compRT[1],"OpenSessionsCurrentCount");
                                        String openSessions = Integer.toString(openSessionsCurrentCount);
                                        logger.debug("OpenSessionsCurrentCount: " + openSessions);
                                        map.put("openSessions", openSessions);
                                    }
                                }
                            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                                e.printStackTrace();
                            }
                        });

                        Optional<ObjectName> optJdbcRT = Arrays.stream(appRT).filter(app -> {
                            try {
                                return "CNPCHRConnDS".equals((String) connection.getAttribute(app,"Name"));
                            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }).findAny();
                        optJdbcRT.ifPresent(app -> {
                            try {
                                ObjectName[] compRT = (ObjectName[]) connection.getAttribute(app,"ComponentRuntimes");
                                int dsActiveConn = (int) connection.getAttribute(compRT[0],"ActiveConnectionsCurrentCount");
                                map.put("dsActiveConn", Integer.toString(dsActiveConn));
                                int dsCapacity = (int) connection.getAttribute(compRT[0],"CurrCapacity");
                                map.put("dsCapacity", Integer.toString(dsCapacity));
                                int dsState = (int) connection.getAttribute(compRT[0],"DeploymentState");
                                map.put("dsState", WlsJmxMonitorUtils.activeStateMapToString(dsState));
                                int dsLeakedConn = (int) connection.getAttribute(compRT[0],"LeakedConnectionCount");
                                map.put("dsLeakedConn", Integer.toString(dsLeakedConn));
                            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                        e.printStackTrace();
                    }
                    return map;
                }, executor
        )).collect(Collectors.toList());

        return futureList.stream().map(CompletableFuture :: join).collect(Collectors.toList());
/*
        List<Map<String, String>> list = new ArrayList<>();
        long start = System.nanoTime();
        Arrays.stream(serverRT).forEach(server -> {
            Map<String, String> map = new HashMap<>();
            try {
                String name = (String) connection.getAttribute(server,"Name");
                String state = (String) connection.getAttribute(server,"State");
                HealthState healthState = (HealthState) connection.getAttribute(server, "HealthState");
                String serverHealthState = HealthState.mapToString(healthState.getState());
                logger.debug("serverName: {}, serverState, {}, serverHealth, {}");
                map.put("serverName", name);
                map.put("serverState", state);
                map.put("serverHealth", serverHealthState);
                ObjectName jvmRT = (ObjectName) connection.getAttribute(server,"JVMRuntime");
                long heapSizeCurrent = (long) connection.getAttribute(jvmRT,"HeapSizeCurrent");
                long heapFreeCurrent = (long) connection.getAttribute(jvmRT,"HeapFreeCurrent");
                int heapFreePercent = (int) connection.getAttribute(jvmRT,"HeapFreePercent");
                logger.debug("HeapSizeCurrent: " + heapSizeCurrent + ", HeapFreeCurrent: " + heapFreeCurrent + ", HeapFreePercent: " + heapFreePercent);
                map.put("heapSizeCurrent", heapSizeCurrent/1024/1024 + "M");
                map.put("heapFreeCurrent", heapFreeCurrent/1024/1024 + "M");
                map.put("heapFreePercent", heapFreePercent + "%");
                ObjectName[] appRT = (ObjectName[]) connection.getAttribute(server,"ApplicationRuntimes");
                Optional<ObjectName> optAppRT = Arrays.stream(appRT).filter(app -> {
                    try {
                        return "CurrentApps".equals((String) connection.getAttribute(app,"Name"));
                    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).findAny();
                optAppRT.ifPresent(app -> {
                    try {
                        String appName = (String) connection.getAttribute(app,"Name");
                        HealthState hs = (HealthState) connection.getAttribute(app,"HealthState");
                        String appHealthState = HealthState.mapToString(hs.getState());
                        int sc = (int) connection.getAttribute(app,"ActiveVersionState");
                        String appActiveState = WlsJmxMonitorUtils.activeStateMapToString(sc);
                        logger.debug("appName: {}, appHealthState: {}, appActiveState: {}", appName, appHealthState, appActiveState);
                        map.put("appName", appName);
                        map.put("appHealthState", appHealthState);
                        map.put("appActiveState", appActiveState);
                        ObjectName[] compRT = (ObjectName[]) connection.getAttribute(app,"ComponentRuntimes");
                        if (compRT.length == 2) {
                            if ("WebAppComponentRuntime".equals(connection.getAttribute(compRT[1],"Type"))) {
                                int openSessionsCurrentCount = (int) connection.getAttribute(compRT[1],"OpenSessionsCurrentCount");
                                String openSessions = Integer.toString(openSessionsCurrentCount);
                                logger.debug("OpenSessionsCurrentCount: " + openSessions);
                                map.put("openSessions", openSessions);
                            }
                        } else if (compRT.length == 1) {
                            if ("WebAppComponentRuntime".equals(connection.getAttribute(compRT[0],"Type"))) {
                                int openSessionsCurrentCount = (int) connection.getAttribute(compRT[1],"OpenSessionsCurrentCount");
                                String openSessions = Integer.toString(openSessionsCurrentCount);
                                logger.debug("OpenSessionsCurrentCount: " + openSessions);
                                map.put("openSessions", openSessions);
                            }
                        }
                    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                        e.printStackTrace();
                    }
                });

                Optional<ObjectName> optJdbcRT = Arrays.stream(appRT).filter(app -> {
                    try {
                        return "CNPCHRConnDS".equals((String) connection.getAttribute(app,"Name"));
                    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).findAny();
                optJdbcRT.ifPresent(app -> {
                    try {
                        ObjectName[] compRT = (ObjectName[]) connection.getAttribute(app,"ComponentRuntimes");
                        int dsActiveConn = (int) connection.getAttribute(compRT[0],"ActiveConnectionsCurrentCount");
                        map.put("dsActiveConn", Integer.toString(dsActiveConn));
                        int dsCapacity = (int) connection.getAttribute(compRT[0],"CurrCapacity");
                        map.put("dsCapacity", Integer.toString(dsCapacity));
                        int dsState = (int) connection.getAttribute(compRT[0],"DeploymentState");
                        map.put("dsState", WlsJmxMonitorUtils.activeStateMapToString(dsState));
                        int dsLeakedConn = (int) connection.getAttribute(compRT[0],"LeakedConnectionCount");
                        map.put("dsLeakedConn", Integer.toString(dsLeakedConn));
                    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                e.printStackTrace();
            }
            System.out.println("*************************");
            list.add(map);
        });
        long end = System.nanoTime();
        list.forEach(System.out :: println);
        System.out.println("********** " + (end - start) + " *************");*/
    }

    public static void main(String[] args) throws Exception {
        String hostname = "10.30.41.80";
        String portString = "7001";
        String username = "xuehaipeng";
        String password = "xue112486";

        DomainRuntimeCheck s = new DomainRuntimeCheck();
        initConnection(hostname, portString, username, password);
        long start = System.nanoTime();
        s.printNameAndState();
        long end = System.nanoTime();
        System.out.println("*********** " + (end - start));
        connector.close();
    }
}

