package com.omg.framework.data.common;

/**
 * Created by wenjing on 2017-4-17.
 */
public class ShardingHolder {
    private String readWriteKey;

    public ShardingHolder() {
    }

    public String getReadWriteKey() {
        return this.readWriteKey;
    }

    public void setReadWriteKey(String readWriteKey) {
        this.readWriteKey = readWriteKey;
    }
}

