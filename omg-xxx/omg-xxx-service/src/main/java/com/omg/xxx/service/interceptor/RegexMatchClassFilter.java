package com.omg.xxx.service.interceptor;

import org.springframework.aop.ClassFilter;

/**
 * Created by wenjing on 2016-7-8.
 */
public class RegexMatchClassFilter implements ClassFilter {

    private String patten;

    @Override
    public boolean matches(Class<?> clazz) {

        if (patten == null) {
            throw new IllegalArgumentException("Argument not defined: patten!");
        }
        return clazz.getName().matches(this.patten);
    }

    public void setPatten(String patten) {
        this.patten = patten;
    }

}
