package com.example;

import com.example.utils.JschSshUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Xue on 01/19/17.
 */

public class TestSsh1 {
/*
    private static List<String> ips = Arrays.asList("10.30.148.121", "10.30.150.166", "10.30.150.168", "10.30.150.169", "10.30.150.181", "10.30.150.251",
            "10.30.47.16", "10.30.47.17", "10.30.47.31", "10.30.47.32", "10.30.49.69", "10.30.49.70", "11.11.4.54", "11.11.4.59", "11.11.49.131", "11.11.49.132",
            "11.11.49.133", "11.11.49.134", "11.11.8.34", "11.11.8.56", "11.11.8.57", "11.11.8.58", "11.11.8.59");
    private static String username = "itmuser";
    private static String passwd = "itmuser";*/

    private static List<String> ips = Arrays.asList("10.30.41.81", "10.30.41.82", "10.30.41.79", "10.30.41.78", "10.30.41.69",
            "10.30.41.68", "10.30.41.67", "10.30.41.66", "10.30.41.101", "10.30.41.75", "10.30.41.76", "10.30.41.77");
    private static String username = "wladm";
    private static String passwd = "lsrAtA!2";
    private static List<String> cmd = Arrays.asList("hostname", "uptime", "ps -ef | grep ZHAP5_Srv | grep -v grep");
//    private static List<String> cmd = Arrays.asList("sudo passwd wladm", "itmuser", "qkfqIa!7", "qkfqIa!7");
    public static String out = null;

    public static void sshService() {
        ips.forEach(ip -> {
            try {
                JschSshUtil.sshConn(ip, username, passwd, cmd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            out = JschSshUtil.out;
            JschSshUtil.sshDisconn();
        });
    }

    public static void main(String[] args) {
        sshService();
    }

}
