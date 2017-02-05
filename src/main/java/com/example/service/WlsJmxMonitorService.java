package com.example.service;

import com.example.utils.WlsJmxMonitorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Xue on 11/30/16.
 */
@Component
@ConfigurationProperties(prefix = "jmx.connection")
public class WlsJmxMonitorService {
    private static final Logger logger = LoggerFactory.getLogger(WlsJmxMonitorService.class);
    private List<String> host;
    private String username;
    private String passwd;

    public List<String> getHost() {
        return host;
    }

    public void setHost(List<String> host) {
        this.host = host;
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

    public List<Map<String, String>> pollingWlsVieJmx() {
        List<Map<String, String>> list = new ArrayList<>();
        host.forEach(h -> {
            try {
                Map<String, String> server8001 = WlsJmxMonitorUtils.serverStatePolling(h,"8001", username, passwd);
                Map<String, String> server8002 = WlsJmxMonitorUtils.serverStatePolling(h,"8002", username, passwd);
                list.add(server8001);
                list.add(server8002);
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return list;
    }
}
