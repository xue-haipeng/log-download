package com.example.service;

import com.example.utils.JschSshUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Xue on 01/20/17.
 */
@Component
@ConfigurationProperties(prefix = "ssh")
public class SshCmdService {
    private String ip;
    private String username;
    private String passwd;
    private List<String> cmd;
    public String out = null;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public List<String> getCmd() {
        return cmd;
    }

    public void setCmd(List<String> cmd) {
        this.cmd = cmd;
    }

    public void sshService() {
        try {
            JschSshUtil.sshConn(ip, username, passwd, cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out = JschSshUtil.out;
        JschSshUtil.sshDisconn();
    }
}
