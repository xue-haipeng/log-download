package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Xue on 03/21/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsHits {
    private Long total;

    public EsHits() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "EsHits{" +
                "total=" + total +
                '}';
    }
}
