package com.example.service;

import com.example.utils.JschSshUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Xue on 01/20/17.
 */
@Component
@ConfigurationProperties(prefix = "ssh")
public class SshCmdService {
    private static final Logger logger = LoggerFactory.getLogger(SshCmdService.class);
    private List<String> ips;
    private String username;
    private String passwd;
    private List<String> cmd;
    public String out = null;

    public List<String> getIp() {
        return ips;
    }

    public void setIp(List<String> ips) {
        this.ips = ips;
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

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public void sshService() {
        ips.forEach(ip -> {
            try {
                JschSshUtil.sshConn(ip, username, passwd, cmd, simpMessagingTemplate);
            } catch (Exception e) {
                e.printStackTrace();
            }
/*            out = JschSshUtil.out;
            logger.info(out);*/
            JschSshUtil.sshDisconn();
        });
    }
}
