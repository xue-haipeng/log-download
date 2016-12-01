package com.example.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Xue on 11/30/16.
 */
@Component
@ConfigurationProperties(prefix = "sftp")
public class SftpInvokeService {

    private String username;
    private String passwd;
    private Map<String, String> logfiles;

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

    public Map<String, String> getLogfiles() {
        return logfiles;
    }

    public void setLogfiles(Map<String, String> logfiles) {
        this.logfiles = logfiles;
    }

    public void logFileDownload(String ip, String logName) {
        String username = getUsername();
        String passwd = getPasswd();

        String logPath = getLogfiles().get(ip).replace("{}", logName);

        try {
            SFTPGetTest.sftpDownload(ip, username, passwd, logPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
