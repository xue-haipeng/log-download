package com.example;

import com.example.utils.WlsJmxMonitorUtils;

import java.util.Map;

/**
 * Created by Xue on 01/22/17.
 */
public class JvmRuntimeCheck {

    public static void main(String[] args) throws Exception {
//        String hostname = "10.30.41.80";
        String hostname = "10.30.41.101";
        String portString = "8002";
        String username = "weblogic";
        String password = "xfvenc&7";

        Map<String, String> map = WlsJmxMonitorUtils.serverStatePolling(hostname, portString, username, password);
        map.forEach((k, v) -> System.out.println(k + " : " + v));

    }
}

