package com.omg.framework.data.loadbalance;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Created by wenjing on 2017-4-17.
 */
public class RoundRobinLoadBalance implements LoadBalance<String> {
    private static final int MIN_LB_FACTOR = 1;
    private List<String> targets;
    private int currentPos = 0;

    public RoundRobinLoadBalance(Map<String, Integer> lbFactors) {
        Assert.notEmpty(lbFactors);
        this.targets = this.initTargets(lbFactors);
    }

    public List<String> initTargets(Map<String, Integer> lbFactors) {
        if(MapUtils.isEmpty(lbFactors)) {
            return null;
        } else {
            this.fixFactor(lbFactors);
            Collection factors = lbFactors.values();
            int min = ((Integer) Collections.min(factors)).intValue();
            return min > 1 && this.canModAll(min, factors)?this.buildBalanceTargets(lbFactors, min):this.buildBalanceTargets(lbFactors, 1);
        }
    }

    private void fixFactor(Map<String, Integer> lbFactors) {
        Iterator i$ = lbFactors.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry entry = (Map.Entry)i$.next();
            if(((Integer)entry.getValue()).intValue() < 1) {
                entry.setValue(Integer.valueOf(1));
            }
        }

    }

    private boolean canModAll(int baseFactor, Collection<Integer> factors) {
        Iterator i$ = factors.iterator();

        Integer factor;
        do {
            if(!i$.hasNext()) {
                return true;
            }

            factor = (Integer)i$.next();
        } while(factor.intValue() % baseFactor == 0);

        return false;
    }

    private List<String> buildBalanceTargets(Map<String, Integer> lbFactors, int baseFactor) {
        ArrayList targets = new ArrayList();
        Iterator i$ = lbFactors.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry entry = (Map.Entry)i$.next();
            int count = ((Integer)entry.getValue()).intValue() / baseFactor;

            for(int i = 0; i < count; ++i) {
                targets.add(entry.getKey());
            }
        }

        return targets;
    }

    public synchronized String elect() {
        if(CollectionUtils.isEmpty(this.targets)) {
            return null;
        } else {
            if(this.currentPos >= this.targets.size()) {
                this.currentPos = 0;
            }

            return (String)this.targets.get(this.currentPos++);
        }
    }
}