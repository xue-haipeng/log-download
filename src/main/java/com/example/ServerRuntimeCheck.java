package com.example;

import weblogic.health.HealthState;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.net.MalformedURLException;
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
                                      String username, String password) throws IOException, MalformedURLException {
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
        return (ObjectName) connection.getAttribute(service,
                "ServerRuntime");
    }

    public void printNameAndState() throws Exception {
        ObjectName serverRT = getServerRuntime();
        System.out.println("got server runtimes");
        String name = (String) connection.getAttribute(serverRT,"Name");
        String state = (String) connection.getAttribute(serverRT,"State");
        HealthState healthState = (HealthState) connection.getAttribute(serverRT, "HealthState");
        int stateCode = healthState.getState();
        String health = HealthState.mapToString(stateCode);
        System.out.println("Server name: " + name + ".   Server state: " + state + ", Server Health: " + health);
    }

    public static void main(String[] args) throws Exception {
//        String hostname = "10.30.41.80";
        String hostname = "10.30.41.79";
        String portString = "8001";
        String username = "xuehaipeng";
        String password = "xue112486";

        ServerRuntimeCheck s = new ServerRuntimeCheck();
        initConnection(hostname, portString, username, password);
        s.printNameAndState();
        connector.close();
    }
}

