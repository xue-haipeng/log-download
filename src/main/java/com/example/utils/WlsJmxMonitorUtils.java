package com.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weblogic.health.HealthState;

import javax.management.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


/**
 * Created by Xue on 02/05/17.
 */
public class WlsJmxMonitorUtils {

    private static final Logger logger = LoggerFactory.getLogger(WlsJmxMonitorUtils.class);

    public static List<Map<String, String>> domainStatePolling(String hostname, String portString, String username, String passwd, Executor executor) throws Exception {
        MBeanServerConnection connection = WlsJmxConnUtils.getJmxConnection(hostname, portString, username, passwd, "weblogic.management.mbeanservers.domainruntime");
        ObjectName[] domainRT = WlsJmxConnUtils.getDomainRuntimes(connection);
        List<CompletableFuture<Map<String, String>>> futureList = Arrays.stream(domainRT).map(server -> CompletableFuture.supplyAsync(
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

    }

    public static Map<String, String> serverStatePolling(String hostname, String portString, String username, String passwd) throws Exception {
        Map<String, String> map = new HashMap<>();
        MBeanServerConnection connection = WlsJmxConnUtils.getJmxConnection(hostname, portString, username, passwd, "weblogic.management.mbeanservers.runtime");
        ObjectName serverRT = WlsJmxConnUtils.getServerRuntime(connection);
        logger.debug("Got server runtimes");
        String name = (String) connection.getAttribute(serverRT,"Name");
        String state = (String) connection.getAttribute(serverRT,"State");
        HealthState healthState = (HealthState) connection.getAttribute(serverRT, "HealthState");
        int stateCode = healthState.getState();
        String health = HealthState.mapToString(stateCode);
        map.put("serverName", name);
        map.put("serverState", state);
        map.put("serverHealth", health);
        logger.info("Server Name: {}. Running Status: {}, Health Status: {}", name, state, health);

        ObjectName jvmRT = (ObjectName) connection.getAttribute(serverRT,"JVMRuntime");
        long heapSizeCurrent = (long) connection.getAttribute(jvmRT, "HeapSizeCurrent");
        long heapFreeCurrent = (long) connection.getAttribute(jvmRT, "HeapFreeCurrent");
        int heapFreePercent = (int) connection.getAttribute(jvmRT, "HeapFreePercent");
/*
        String threadStackDump = (String) connection.getAttribute(jvmRT, "ThreadStackDump");
        String[] stringArray = threadStackDump.split("\\r?\\n\\n");
        List<String> threads = Arrays.asList(stringArray);
        List<String> stuckThread = threads.stream().filter(s -> s.contains("[STUCK]")).map(s -> s.concat(System.getProperty("line.separator"))).collect(Collectors.toList());
        stuckThread.forEach(System.out::println);
*/
        logger.info("Server name: " + name + ". HeapSizeCurrent: " + heapSizeCurrent/1024/1024 + "MB, HeapFreeCurrent: "
                + heapFreeCurrent/1024/1024 + "MB, HeapFreePercent: " + heapFreePercent + "%");
        map.put("heapSizeCurrent", heapSizeCurrent/1024/1024 + "M");
        map.put("heapFreeCurrent", heapFreeCurrent/1024/1024 + "M");
        map.put("heapFreePercent", heapFreePercent + "%");

        ObjectName[] appRT = (ObjectName[]) connection.getAttribute(serverRT,"ApplicationRuntimes");
        Optional<ObjectName> optAppRT = Arrays.stream(appRT).filter(app -> {
            try {
                return "CurrentApps".equals((String) connection.getAttribute(app,"Name"));
            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            return false;
        }).findAny();
        optAppRT.ifPresent(app -> {
            try {
                String appName = (String) connection.getAttribute(app,"Name");
                map.put("appName", appName);
                HealthState hs = (HealthState) connection.getAttribute(app,"HealthState");
                String appHealthState = HealthState.mapToString(hs.getState());
                map.put("appHealthState", appHealthState);
                int sc = (int) connection.getAttribute(app,"ActiveVersionState");
                String appActiveState = activeStateMapToString(sc);
                map.put("appActiveState", appActiveState);
                logger.info("appName: {}, appHealthState: {}, appActiveState: {}", appName, appHealthState, appActiveState);

                ObjectName[] compRT = (ObjectName[]) connection.getAttribute(app,"ComponentRuntimes");
                if ("WebAppComponentRuntime".equals(connection.getAttribute(compRT[1],"Type"))) {
                    int openSessionsCurrentCount = (int) connection.getAttribute(compRT[1],"OpenSessionsCurrentCount");
                    String openSessions = Integer.toString(openSessionsCurrentCount);
                    map.put("openSessions", openSessions);
                    logger.info("openSessions: {}", openSessions);
                }
            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        });

        Optional<ObjectName> optJdbcRT = Arrays.stream(appRT).filter(app -> {
            try {
                return "CNPCHRConnDS".equals((String) connection.getAttribute(app,"Name"));
            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                logger.error(e.getMessage());
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
                map.put("dsState", activeStateMapToString(dsState));
                int dsLeakedConn = (int) connection.getAttribute(compRT[0],"LeakedConnectionCount");
                map.put("dsLeakedConn", Integer.toString(dsLeakedConn));
            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        });

        return map;
    }

    public static String activeStateMapToString (int code) {
        switch (code) {
            case 0: return "UNPREPARED";
            case 1: return "PREPARED";
            case 2: return "ACTIVATED";
            case 3: return "NEW";
            default: return "N/A";
        }
    }
}
