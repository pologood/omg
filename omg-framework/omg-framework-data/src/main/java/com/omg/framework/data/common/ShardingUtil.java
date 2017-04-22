package com.omg.framework.data.common;

import org.apache.commons.collections.CollectionUtils;

import java.util.Stack;

/**
 * Created by wenjing on 2017-4-17.
 */
public class ShardingUtil {
    private static final ThreadLocal<Stack<ShardingHolder>> REPOSITORY_HOLDER_STACK = new ThreadLocal();

    public ShardingUtil() {
    }

    public static void removeCurrent() {
        Stack stack = (Stack)REPOSITORY_HOLDER_STACK.get();
        if(CollectionUtils.isEmpty(stack)) {
            REPOSITORY_HOLDER_STACK.remove();
        } else {
            stack.pop();
        }
    }

    public static void setReadWriteKey(String key) {
        Stack stack = (Stack)REPOSITORY_HOLDER_STACK.get();
        if(stack == null) {
            stack = new Stack();
            REPOSITORY_HOLDER_STACK.set(stack);
        }

        ShardingHolder holder = new ShardingHolder();
        holder.setReadWriteKey(key);
        stack.push(holder);
    }

    public static String getReadWriteKey() {
        Stack stack = (Stack)REPOSITORY_HOLDER_STACK.get();
        return CollectionUtils.isEmpty(stack)?null:((ShardingHolder)stack.peek()).getReadWriteKey();
    }
}

