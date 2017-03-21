package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Xue on 03/21/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ESQueryResponse {

    private int took;
    private boolean timed_out;
    private EsShards _shards;
    private EsHits hits;

    public ESQueryResponse() {
    }

    public int getTook() {
        return took;
    }

    public void setTook(int took) {
        this.took = took;
    }

    public boolean isTimed_out() {
        return timed_out;
    }

    public void setTimed_out(boolean timed_out) {
        this.timed_out = timed_out;
    }

    public EsShards get_shards() {
        return _shards;
    }

    public void set_shards(EsShards _shards) {
        this._shards = _shards;
    }

    public EsHits getHits() {
        return hits;
    }

    public void setHits(EsHits hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "ESQueryResponse{" +
                "took=" + took +
                ", timed_out=" + timed_out +
                ", _shards=" + _shards +
                ", hits=" + hits +
                '}';
    }
}
