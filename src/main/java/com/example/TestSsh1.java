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
    //    private static List<String> ips = Arrays.asList("192.168.147.139");
    private static List<String> ips = Arrays.asList("11.11.49.148");
    private static String username = "itmuser";
    private static String passwd = "itmuser";
    private static List<String> cmd = Arrays.asList("uptime");
//    private static List<String> cmd = Arrays.asList("df -h");
    public static String out = null;

    public static void sshService(List<String> ips, List<String> cmd) {
        ips.forEach(ip -> {
            try {
                JschSshUtil.sshConn(ip, username, passwd, cmd, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JschSshUtil.sshDisconn();
        });
    }

/*    public static void main(String[] args) {
        List<String> ips = IntStream.rangeClosed(2, 9).mapToObj(item -> Integer.toString(item)).map(item -> "exoaps00" + item + ".erp.petrochina").collect(Collectors.toList());
        //.mapToObj(item - > return {"exoaps00" + item + "erp.petrochina"})
        sshService(ips, cmd);
//        System.out.println(System.getProperty("line.separator"));
    }*/

}
