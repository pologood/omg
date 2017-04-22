package com.omg.framework.data.interceptor;


import com.omg.framework.data.common.Constants;
import com.omg.framework.data.common.ReadWriteKey;
import com.omg.framework.data.common.ShardingUtil;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wenjing on 2017-4-14.
 */
public class ReadWriteInterceptor implements MethodInterceptor, Constants {
    private ReadWriteKey readWriteKey;

    public ReadWriteInterceptor() {
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        Method method = invocation.getThis().getClass().getMethod(methodName, invocation.getMethod().getParameterTypes());
        Transactional tx = (Transactional)method.getAnnotation(Transactional.class);
        if(tx == null) {
            tx = (Transactional)invocation.getThis().getClass().getAnnotation(Transactional.class);
        }

        if(tx != null && tx.readOnly()) {
            this.readWriteKey.setReadKey();
        } else {
            this.readWriteKey.setWriteKey();
        }

        Object e;
        try {
            e = invocation.proceed();
        } catch (Throwable var9) {
            throw var9;
        } finally {
            ShardingUtil.removeCurrent();
        }

        return e;
    }

    public ReadWriteKey getReadWriteKey() {
        return this.readWriteKey;
    }

    public void setReadWriteKey(ReadWriteKey readWriteKey) {
        this.readWriteKey = readWriteKey;
    }
}