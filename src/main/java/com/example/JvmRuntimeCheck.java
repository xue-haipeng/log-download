package com.example;

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
public class JvmRuntimeCheck {

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
        JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname, port, jndiroot + mserver);
        Hashtable h = new Hashtable();
        h.put(Context.SECURITY_PRINCIPAL, username);
        h.put(Context.SECURITY_CREDENTIALS, password);
        h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
        connector = JMXConnectorFactory.connect(serviceURL, h);
        connection = connector.getMBeanServerConnection();
    }

    public static ObjectName getJvmRuntime() throws Exception {
        ObjectName serverrt = (ObjectName) connection.getAttribute(service,"ServerRuntime");
        return (ObjectName) connection.getAttribute(serverrt, "JVMRuntime");
    }

    public void printNameAndState() throws Exception {
        ObjectName jvmRuntime = getJvmRuntime();
        System.out.println("got jvm runtimes");
        String name = (String) connection.getAttribute(jvmRuntime,"Name");
        long heapSizeCurrent = (long) connection.getAttribute(jvmRuntime, "HeapSizeCurrent");
        long heapFreeCurrent = (long) connection.getAttribute(jvmRuntime, "HeapFreeCurrent");
        int heapFreePercent = (int) connection.getAttribute(jvmRuntime, "HeapFreePercent");
        String threadStackDump = (String) connection.getAttribute(jvmRuntime, "ThreadStackDump");
        String[] stringArray = threadStackDump.split("\\r?\\n\\n");
        System.out.println("Server name: " + name + ". HeapSizeCurrent: " + heapSizeCurrent/1024/1024 + "MB, HeapFreeCurrent: "
                + heapFreeCurrent/1024/1024 + "MB, HeapFreePercent: " + heapFreePercent + "%");
//        System.out.println("ThreadStackDump: " + threadStackDump);
        System.out.println("+++++++++++++++++++++++++" + System.getProperty("line.separator"));
        System.out.println(stringArray.length);
        System.out.println("+++++++++++++++++++++++++" + System.getProperty("line.separator"));
        System.out.println(stringArray[100]);
        System.out.println("+++++++++++++++++++++++++");
        System.out.println(stringArray[101]);

    }

    public static void main(String[] args) throws Exception {
//        String hostname = "10.30.41.80";
        String hostname = "10.30.41.79";
        String portString = "8001";
        String username = "xuehaipeng";
        String password = "xue112486";

        JvmRuntimeCheck s = new JvmRuntimeCheck();
        initConnection(hostname, portString, username, password);
        s.printNameAndState();
        connector.close();
    }
}

