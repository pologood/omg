package com.omg.framework.data.transaction;

import com.omg.framework.data.common.ReadWriteKey;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * Created by wenjing on 2017-4-19.
 */
public class ReadWriteTransactionManager extends DataSourceTransactionManager{

    private ReadWriteKey readWriteKey;

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {

        if (definition != null && definition.isReadOnly()) {
            this.readWriteKey.setReadKey();
        } else {
            this.readWriteKey.setWriteKey();
        }

        super.doBegin(transaction, definition);
    }

    public ReadWriteKey getReadWriteKey() {
        return readWriteKey;
    }

    public void setReadWriteKey(ReadWriteKey readWriteKey) {
        this.readWriteKey = readWriteKey;
    }
}
