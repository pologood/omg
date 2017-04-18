package com.omg.xxx.service.bean.mapper;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * @author bohan
 *
 * Bean拷贝基础工厂，可直接在Service中Autowired MapperFacade使用拷贝功能
 * 
 */
@Component
public class MapperFacadeFactory implements FactoryBean<MapperFacade> {
    public MapperFacade getObject() throws Exception {
        return new DefaultMapperFactory.Builder().build().getMapperFacade();
    }
    
    public Class<?> getObjectType() {
        return MapperFacade.class;
    }
 
    public boolean isSingleton() {
        return true;
    }
}