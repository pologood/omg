package com.omg.framework.data.loadbalance;

/**
 * Created by wenjing on 2017-4-17.
 */
public interface LoadBalance<T> {
    T elect();
}
