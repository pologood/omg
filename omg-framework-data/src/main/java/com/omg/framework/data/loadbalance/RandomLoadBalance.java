package com.omg.framework.data.loadbalance;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Random;

/**
 * Created by wenjing on 2017-4-17.
 */
public class RandomLoadBalance implements LoadBalance<String> {
    private List<String> targets;
    private final Random random = new Random();

    public RandomLoadBalance(List<String> targets) {
        Assert.notEmpty(targets);
        this.targets = targets;
    }

    public synchronized String elect() {
        return CollectionUtils.isEmpty(this.targets)?null:(String)this.targets.get(this.random.nextInt(this.targets.size()));
    }
}
