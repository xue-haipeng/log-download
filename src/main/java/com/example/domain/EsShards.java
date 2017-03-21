package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Xue on 03/21/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsShards {
    private Long total;
    private Long successful;
    private Long failed;

    public EsShards() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getSuccessful() {
        return successful;
    }

    public void setSuccessful(Long successful) {
        this.successful = successful;
    }

    public Long getFailed() {
        return failed;
    }

    public void setFailed(Long failed) {
        this.failed = failed;
    }

    @Override
    public String toString() {
        return "EsShards{" +
                "total=" + total +
                ", successful=" + successful +
                ", failed=" + failed +
                '}';
    }
}
