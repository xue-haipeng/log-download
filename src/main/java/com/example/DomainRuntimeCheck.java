package com.example;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by Xue on 01/22/17.
 */
public class DomainRuntimeCheck {

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
        JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname, port, jndiroot + mserver);
        Hashtable h = new Hashtable();
        h.put(Context.SECURITY_PRINCIPAL, username);
        h.put(Context.SECURITY_CREDENTIALS, password);
        h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
        connector = JMXConnectorFactory.connect(serviceURL, h);
        connection = connector.getMBeanServerConnection();
    }

    /*
    * Print an array of ServerRuntimeMBeans.
    * This MBean is the root of the runtime MBean hierarchy, and
    * each server in the domain hosts its own instance.
    */
    public static ObjectName[] getServerRuntimes() throws Exception {
        return (ObjectName[]) connection.getAttribute(service,"ServerRuntimes");
    }

    /*
    * Iterate through ServerRuntimeMBeans and get the name and state
    */
    public void printNameAndState() throws Exception {
        ObjectName[] serverRT = getServerRuntimes();
        System.out.println("got server runtimes");
        int length = (int) serverRT.length;
        for (int i = 0; i < length; i++) {
            String name = (String) connection.getAttribute(serverRT[i],"Name");
            String state = (String) connection.getAttribute(serverRT[i],"State");
            System.out.println("Server name: " + name + ".   Server state: " + state);
        }
    }

    public static void main(String[] args) throws Exception {
        String hostname = "10.30.41.80";
//        String hostname = "10.30.41.79";
        String portString = "8001";
        String username = "xuehaipeng";
        String password = "xue112486";

        DomainRuntimeCheck s = new DomainRuntimeCheck();
        initConnection(hostname, portString, username, password);
        s.printNameAndState();
        connector.close();
    }
}

