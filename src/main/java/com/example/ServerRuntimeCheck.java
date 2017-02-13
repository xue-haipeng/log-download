package com.example;

import weblogic.health.HealthState;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by Xue on 01/22/17.
 */
public class ServerRuntimeCheck {

    private static MBeanServerConnection connection;
    private static JMXConnector connector;
    private static final ObjectName service;

    static {
        try {
            service = new ObjectName(
                    "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
        } catch (MalformedObjectNameException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    public static void initConnection(String hostname, String portString,
                                      String username, String password) throws IOException {
        String protocol = "t3";
        Integer portInteger = Integer.valueOf(portString);
        int port = portInteger.intValue();
        String jndiroot = "/jndi/";
        String mserver = "weblogic.management.mbeanservers.runtime";
        JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname,
                port, jndiroot + mserver);
        Hashtable h = new Hashtable();
        h.put(Context.SECURITY_PRINCIPAL, username);
        h.put(Context.SECURITY_CREDENTIALS, password);
        h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
                "weblogic.management.remote");
        connector = JMXConnectorFactory.connect(serviceURL, h);
        connection = connector.getMBeanServerConnection();
    }

    public static ObjectName getServerRuntime() throws Exception {
        return (ObjectName) connection.getAttribute(service, "ServerRuntime");
    }

    // Get ApplicationRuntimeMBeans
    public static ObjectName[] getApplicationRuntime() throws Exception {
        ObjectName serverrt = (ObjectName) connection.getAttribute(service, "ServerRuntime");
        return  (ObjectName[]) connection.getAttribute(serverrt, "ApplicationRuntimes");
    }

    public void printNameAndState() throws Exception {
        ObjectName serverRT = getServerRuntime();
        System.out.println("got server runtimes");
        String name = (String) connection.getAttribute(serverRT,"Name");
        String state = (String) connection.getAttribute(serverRT,"State");
        HealthState healthState = (HealthState) connection.getAttribute(serverRT, "HealthState");
        int stateCode = healthState.getState();
        String health = HealthState.mapToString(stateCode);
        System.out.println("Server Name: " + name + ". Server State: " + state + ", Server Health: " + health);
        System.out.println();
    }

    public void printAppState() throws Exception {
        ObjectName[] appRT = getApplicationRuntime();
        Arrays.stream(appRT).forEach(objectName -> {
            try {
                System.out.println("Application name: " + (String)connection.getAttribute(objectName,"Name"));

                HealthState healthState = (HealthState) connection.getAttribute(objectName,"HealthState");
                System.out.println("HealthState: " + HealthState.mapToString(healthState.getState()));

                int stateCode = (int) connection.getAttribute(objectName,"ActiveVersionState");
                String activeVersionState;
                switch (stateCode) {
                    case 0: activeVersionState = "UNPREPARED"; break;
                    case 1: activeVersionState = "PREPARED"; break;
                    case 2: activeVersionState = "ACTIVATED"; break;
                    case 3: activeVersionState = "NEW"; break;
                    default: activeVersionState = "N/A"; break;
                }
                System.out.println("ActiveVersionState: " + activeVersionState);
                ObjectName[] compRT = (ObjectName[]) connection.getAttribute(objectName,"ComponentRuntimes");
                Arrays.stream(compRT).forEach(comp -> {

                    try {
                        System.out.println("Component Type: " + connection.getAttribute(comp,"Type"));
                        if ("WebAppComponentRuntime".equals(connection.getAttribute(comp,"Type"))) {
                            System.out.println("OpenSessionsCurrentCount: " + connection.getAttribute(comp,"OpenSessionsCurrentCount"));
                        }
                    } catch (MBeanException e) {
                        e.printStackTrace();
                    } catch (AttributeNotFoundException e) {
                        System.out.println("___________");
//                        e.printStackTrace();
                    } catch (InstanceNotFoundException e) {
                        e.printStackTrace();
                    } catch (ReflectionException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println();
            } catch (MBeanException e) {
                e.printStackTrace();
            } catch (AttributeNotFoundException e) {
                e.printStackTrace();
            } catch (InstanceNotFoundException e) {
                e.printStackTrace();
            } catch (ReflectionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
/*
    public static void main(String[] args) throws Exception {
        String hostname = "192.168.147.133";
        String portString = "7001";
        String username = "weblogic";
        String password = "welcome1";

        ServerRuntimeCheck s = new ServerRuntimeCheck();
        initConnection(hostname, portString, username, password);
        s.printNameAndState();
        s.printAppState();
        connector.close();
    }*/
}

