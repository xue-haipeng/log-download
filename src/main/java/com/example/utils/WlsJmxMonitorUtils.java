package com.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weblogic.health.HealthState;

import javax.management.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Created by Xue on 02/05/17.
 */
public class WlsJmxMonitorUtils {

    private static final Logger logger = LoggerFactory.getLogger(WlsJmxMonitorUtils.class);

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
                e.getCause();
            }
            return false;
        }).findAny();
        optAppRT.ifPresent(app -> {
            try {
                String appName = (String)connection.getAttribute(app,"Name");
                map.put("appName", appName);
                HealthState hs = (HealthState) connection.getAttribute(app,"HealthState");
                String appHealthState = HealthState.mapToString(hs.getState());
                map.put("appHealthState", appHealthState);
                int sc = (int) connection.getAttribute(app,"ActiveVersionState");
                String appActiveState = activeStateMapToString(sc);
                map.put("appActiveState", appActiveState);
                logger.info("appName: {}, appHealthState: {}, appActiveState: {}", appName, appHealthState, appActiveState);

                ObjectName[] compRT = (ObjectName[]) connection.getAttribute(app,"ComponentRuntimes");
                System.out.println(" ********* " + compRT.length + "   compRT[0]: " + connection.getAttribute(compRT[0],"Type")
                        + " , compRT[1]: " + connection.getAttribute(compRT[1],"Type"));
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
