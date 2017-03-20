package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Xue on 11/30/16.
 */
@Component
@ConfigurationProperties(prefix = "sftp")
public class SftpInvokeService {
    private static final Logger logger = LoggerFactory.getLogger(SftpInvokeService.class);
    private String username;
    private String passwd;
    private Map<String, String> logfiles;
    private Map<String, String>  xoaps;

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

    public Map<String, String> getXoaps() {
        return xoaps;
    }

    public void setXoaps(Map<String, String> xoaps) {
        this.xoaps = xoaps;
    }

    public void logFileDownload(String ip, String type, String logName) {
        String username = getUsername();
        String passwd = getPasswd();
        String logPath;
        if ("log".equals(type) || "out".equals(type)) {
            logPath = getLogfiles().get(ip).replace("{}", logName).replace("[]", type);
        } else {
            String tmp = getLogfiles().get(ip).replace("{}", logName).replace("[]", "log");
            logPath = "/oracle/admin/ZHAP5_DOMAIN/mserver/ZHAP5_DOMAIN/gc_" + tmp.substring(tmp.length() - 16);
        }
        logger.info("Connecting {}, fetching {} ...", ip, logPath);
        try {
            SFTPGetTest.sftpDownload(ip, username, passwd, logPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logFileDownloadXoaps(String ip, String logName) {
        String username = this.getUsername();
        String passwd = this.getPasswd();
        String logPath = this.getXoaps().get(ip).replace("{}", logName);
        logger.info("Connecting {}, fetching {} ...", ip, logPath);
        try {
            SFTPGetTest.sftpDownload(ip, username, passwd, logPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
