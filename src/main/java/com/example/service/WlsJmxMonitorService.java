package com.example.service;

import com.example.utils.WlsJmxMonitorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

/**
 * Created by Xue on 11/30/16.
 */
@Component
@ConfigurationProperties(prefix = "jmx.connection")
public class WlsJmxMonitorService {
    private static final Logger logger = LoggerFactory.getLogger(WlsJmxMonitorService.class);
    private List<String> host;
    private String aserverUrl;
    private String aserverPort;
    private String username;
    private String passwd;

    private final Executor serverExecutor = Executors.newFixedThreadPool(24, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    });

    private final Executor domainExecutor = Executors.newFixedThreadPool(25, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    });

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

    public String getAserverUrl() {
        return aserverUrl;
    }

    public void setAserverUrl(String aserverUrl) {
        this.aserverUrl = aserverUrl;
    }

    public String getAserverPort() {
        return aserverPort;
    }

    public void setAserverPort(String aserverPort) {
        this.aserverPort = aserverPort;
    }

    public List<Map<String, String>> pollingWlsVieJmx() {
        List<CompletableFuture<Map<String, String>>> futureList =
                host.stream().map(h -> CompletableFuture.supplyAsync(
                        () -> {
                            Map<String, String> server = null;
                            try {
                                server = WlsJmxMonitorUtils.serverStatePolling(h.split("#")[0], h.split("#")[1], username, passwd);
                            } catch (Exception e) {
                                logger.error(e.getMessage());
                                if (server == null) {
                                    server = new HashMap<>();
                                    server.put("serverName", h.replace("#", ":"));
                                }
                            }
                            return server;
                        }, serverExecutor
                )).collect(Collectors.toList());

        return futureList.stream().map(CompletableFuture :: join).collect(Collectors.toList());
    }

    public List<Map<String, String>> pollingDomainVieJmx() throws Exception {
        return WlsJmxMonitorUtils.domainStatePolling(aserverUrl, aserverPort, username, passwd, domainExecutor);
    }
}
