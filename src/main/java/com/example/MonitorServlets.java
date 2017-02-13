package com.example;

/**
 * Created by Xue on 01/22/17.
 */

import weblogic.health.HealthState;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.util.Hashtable;
public class MonitorServlets {
    private static MBeanServerConnection connection;
    private static JMXConnector connector;
    private static final ObjectName service;
    // Initializing the object name for DomainRuntimeServiceMBean
    // so it can be used throughout the class.
    static {
        try {
            service = new ObjectName(
                    "com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
        }catch (MalformedObjectNameException e) {
            throw new AssertionError(e.getMessage());
        }
    }
    /*
    * Initialize connection to the Domain Runtime MBean Server
    */
    public static void initConnection(String hostname, String portString, String username, String password) throws IOException {
        String protocol = "t3";
        Integer portInteger = Integer.valueOf(portString);
        int port = portInteger.intValue();
        String jndiroot = "/jndi/";
        String mserver = "weblogic.management.mbeanservers.domainruntime";
        JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname, port,jndiroot + mserver);
        Hashtable h = new Hashtable();
        h.put(Context.SECURITY_PRINCIPAL, username);
        h.put(Context.SECURITY_CREDENTIALS, password);
        h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
        connector = JMXConnectorFactory.connect(serviceURL, h);
        connection = connector.getMBeanServerConnection();
    }
    /*
    * Get an array of ServerRuntimeMBeans
    */
    public static ObjectName[] getServerRuntimes() throws Exception {
        return (ObjectName[]) connection.getAttribute(service,"ServerRuntimes");
    }
    /*
    * Get an array of WebApplicationComponentRuntimeMBeans
    */
    public void getServletData() throws Exception {
        ObjectName[] serverRT = getServerRuntimes();
        int length = (int) serverRT.length;
        for (int i = 0; i < length; i++) {
            ObjectName[] appRT = (ObjectName[]) connection.getAttribute(serverRT[i],"ApplicationRuntimes");
            System.out.println("Application name: " + (String)connection.getAttribute(appRT[i],"Name"));
            HealthState healthState = (HealthState) connection.getAttribute(appRT[i],"HealthState");
            System.out.println(HealthState.mapToString(healthState.getState()));
            System.out.println(connection.getAttribute(appRT[i],"ActiveVersionState"));

            ObjectName[] compRT = (ObjectName[]) connection.getAttribute(appRT[i],"ComponentRuntimes");
            int compLength = (int) compRT.length;
            for (int y = 0; y < compLength; y++) {
                String componentType = (String) connection.getAttribute(compRT[y],"Type");
                System.out.println(connection.getAttribute(compRT[y],"DeploymentState"));

                System.out.println("OpenSessionsCurrentCount: " + connection.getAttribute(compRT[y],"OpenSessionsCurrentCount"));

                connection.getAttribute(compRT[y],"DeploymentState");
                System.out.println(componentType);
            }
        }
    }
/*    public static void main(String[] args) throws Exception {
        String hostname = "192.168.147.133";
        String portString = "7001";
        String username = "weblogic";
        String password = "welcome1";
        MonitorServlets s = new MonitorServlets();
        initConnection(hostname, portString, username, password);
        s.getServletData();
        connector.close();
    }*/
}