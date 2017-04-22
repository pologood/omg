package com.omg.framework.data.common;

import com.omg.framework.data.loadbalance.LoadBalance;

/**
 * Created by wenjing on 2017-4-17.
 */
public class ReadWriteKey {
    private LoadBalance<String> loadBalance;
    private String writeKey;

    public ReadWriteKey() {
    }

    public void setWriteKey() {
        ShardingUtil.setReadWriteKey(this.writeKey);
    }

    public void setReadKey() {
        if(this.loadBalance == null) {
            ShardingUtil.setReadWriteKey(this.writeKey);
        } else {
            ShardingUtil.setReadWriteKey((String)this.loadBalance.elect());
        }

    }

    public String getKey() {
        return ShardingUtil.getReadWriteKey() == null?this.writeKey:ShardingUtil.getReadWriteKey();
    }

    public LoadBalance<String> getLoadBalance() {
        return this.loadBalance;
    }

    public void setLoadBalance(LoadBalance<String> loadBalance) {
        this.loadBalance = loadBalance;
    }

    public String getWriteKey() {
        return this.writeKey;
    }

    public void setWriteKey(String writeKey) {
        this.writeKey = writeKey;
    }
}

