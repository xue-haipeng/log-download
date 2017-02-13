package com.example.domain;

import org.wuwz.poi.ExportConfig;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Xue on 02/12/17.
 */
@Entity
@Table(name = "wls_checklist")
public class WlsMonitor {
    @Id
    @GeneratedValue
    private Long id;
    @ExportConfig(value = "记录时间")
    private Date recordTime;
    @ExportConfig(value = "系统名称")
    private String system_name;
    @ExportConfig(value = "Server名称")
    private String serverName;
    @ExportConfig(value = "Server状态")
    private String serverState;
    @ExportConfig(value = "Server健康状态")
    private String serverHealth;
    @ExportConfig(value = "当前堆大小")
    private String heapSizeCurrent;
    @ExportConfig(value = "当前堆剩余空间")
    private String heapFreeCurrent;
    @ExportConfig(value = "堆剩余空间百分比")
    private String heapFreePercent;
    @ExportConfig(value = "应用名称")
    private String appName;
    @ExportConfig(value = "激活状态")
    private String appActiveState;
    @ExportConfig(value = "应用健康状况")
    private String appHealthState;
    @ExportConfig(value = "当前会话数")
    private String openSessionsCurrentCount;
    @ExportConfig(value = "所有会话数")
    private String sessionsOpenedTotalCount;
    @ExportConfig(value = "数据源名称")
    private String datasourceName;
    @ExportConfig(value = "数据源状态")
    private String datasourceState;
    @ExportConfig(value = "数据源容量")
    private String datasourceCapacity;
    @ExportConfig(value = "当前活动连接数")
    private String activeConnectionsCurrentCount;
    @ExportConfig(value = "最大活动连接数")
    private String activeConnectionsHighCount;
    @ExportConfig(value = "泄露连接数")
    private String datasourceLeakedConnections;
    @ExportConfig(value = "等待连接数")
    private String waitingForConnections;
    @ExportConfig(value = "线程池健康状态")
    private String threadPoolHealth;
    @ExportConfig(value = "执行线程总数")
    private String executeThreadTotalCount;
    @ExportConfig(value = "备用线程总数")
    private String standbyThreadCount;
    @ExportConfig(value = "队列长度")
    private String queueLength;
    @ExportConfig(value = "暂挂用户请求数")
    private String pendingUserRequestCount;
    @ExportConfig(value = "等待失败连接总数")
    private String waitingForConnectionFailureTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getSystem_name() {
        return system_name;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerState() {
        return serverState;
    }

    public void setServerState(String serverState) {
        this.serverState = serverState;
    }

    public String getServerHealth() {
        return serverHealth;
    }

    public void setServerHealth(String serverHealth) {
        this.serverHealth = serverHealth;
    }

    public String getHeapSizeCurrent() {
        return heapSizeCurrent;
    }

    public void setHeapSizeCurrent(String heapSizeCurrent) {
        this.heapSizeCurrent = heapSizeCurrent;
    }

    public String getHeapFreeCurrent() {
        return heapFreeCurrent;
    }

    public void setHeapFreeCurrent(String heapFreeCurrent) {
        this.heapFreeCurrent = heapFreeCurrent;
    }

    public String getHeapFreePercent() {
        return heapFreePercent;
    }

    public void setHeapFreePercent(String heapFreePercent) {
        this.heapFreePercent = heapFreePercent;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppActiveState() {
        return appActiveState;
    }

    public void setAppActiveState(String appActiveState) {
        this.appActiveState = appActiveState;
    }

    public String getAppHealthState() {
        return appHealthState;
    }

    public void setAppHealthState(String appHealthState) {
        this.appHealthState = appHealthState;
    }

    public String getOpenSessionsCurrentCount() {
        return openSessionsCurrentCount;
    }

    public void setOpenSessionsCurrentCount(String openSessionsCurrentCount) {
        this.openSessionsCurrentCount = openSessionsCurrentCount;
    }

    public String getSessionsOpenedTotalCount() {
        return sessionsOpenedTotalCount;
    }

    public void setSessionsOpenedTotalCount(String sessionsOpenedTotalCount) {
        this.sessionsOpenedTotalCount = sessionsOpenedTotalCount;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getDatasourceState() {
        return datasourceState;
    }

    public void setDatasourceState(String datasourceState) {
        this.datasourceState = datasourceState;
    }

    public String getDatasourceCapacity() {
        return datasourceCapacity;
    }

    public void setDatasourceCapacity(String datasourceCapacity) {
        this.datasourceCapacity = datasourceCapacity;
    }

    public String getActiveConnectionsCurrentCount() {
        return activeConnectionsCurrentCount;
    }

    public void setActiveConnectionsCurrentCount(String activeConnectionsCurrentCount) {
        this.activeConnectionsCurrentCount = activeConnectionsCurrentCount;
    }

    public String getActiveConnectionsHighCount() {
        return activeConnectionsHighCount;
    }

    public void setActiveConnectionsHighCount(String activeConnectionsHighCount) {
        this.activeConnectionsHighCount = activeConnectionsHighCount;
    }

    public String getDatasourceLeakedConnections() {
        return datasourceLeakedConnections;
    }

    public void setDatasourceLeakedConnections(String datasourceLeakedConnections) {
        this.datasourceLeakedConnections = datasourceLeakedConnections;
    }

    public String getWaitingForConnections() {
        return waitingForConnections;
    }

    public void setWaitingForConnections(String waitingForConnections) {
        this.waitingForConnections = waitingForConnections;
    }

    public String getThreadPoolHealth() {
        return threadPoolHealth;
    }

    public void setThreadPoolHealth(String threadPoolHealth) {
        this.threadPoolHealth = threadPoolHealth;
    }

    public String getExecuteThreadTotalCount() {
        return executeThreadTotalCount;
    }

    public void setExecuteThreadTotalCount(String executeThreadTotalCount) {
        this.executeThreadTotalCount = executeThreadTotalCount;
    }

    public String getStandbyThreadCount() {
        return standbyThreadCount;
    }

    public void setStandbyThreadCount(String standbyThreadCount) {
        this.standbyThreadCount = standbyThreadCount;
    }

    public String getQueueLength() {
        return queueLength;
    }

    public void setQueueLength(String queueLength) {
        this.queueLength = queueLength;
    }

    public String getPendingUserRequestCount() {
        return pendingUserRequestCount;
    }

    public void setPendingUserRequestCount(String pendingUserRequestCount) {
        this.pendingUserRequestCount = pendingUserRequestCount;
    }

    public String getWaitingForConnectionFailureTotal() {
        return waitingForConnectionFailureTotal;
    }

    public void setWaitingForConnectionFailureTotal(String waitingForConnectionFailureTotal) {
        this.waitingForConnectionFailureTotal = waitingForConnectionFailureTotal;
    }
}
