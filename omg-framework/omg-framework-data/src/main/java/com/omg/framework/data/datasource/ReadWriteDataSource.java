package com.omg.framework.data.datasource;

import com.omg.framework.data.common.Constants;
import com.omg.framework.data.common.ReadWriteKey;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Created by wenjing on 2017-4-17.
 */
public class ReadWriteDataSource extends AbstractRoutingDataSource implements Constants {
    private ReadWriteKey readWriteKey;

    public ReadWriteDataSource() {
    }

    protected Object determineCurrentLookupKey() {
        String key = this.readWriteKey.getKey();
        TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        return key;
    }

    public ReadWriteKey getReadWriteKey() {
        return this.readWriteKey;
    }

    public void setReadWriteKey(ReadWriteKey readWriteKey) {
        this.readWriteKey = readWriteKey;
    }
}
