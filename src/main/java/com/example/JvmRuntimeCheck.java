package com.example;

import com.example.utils.WlsJmxMonitorUtils;

import java.util.Map;

/**
 * Created by Xue on 01/22/17.
 */
public class JvmRuntimeCheck {

    public static void main(String[] args) throws Exception {
//        String hostname = "10.30.41.80";
        String hostname = "192.168.147.133";
        String portString = "7001";
        String username = "weblogic";
        String password = "welcome1";

        Map<String, String> map = WlsJmxMonitorUtils.serverStatePolling(hostname, portString, username, password);
        map.forEach((k, v) -> System.out.println(k + " : " + v));

    }
}

