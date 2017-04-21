/*    */ package com.omg.framework.data.mybatis.pagination.orderbyhelper.sqlsource;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashMap;
/*    */ import org.apache.ibatis.builder.BuilderException;
/*    */ import org.apache.ibatis.builder.SqlSourceBuilder;
/*    */ import org.apache.ibatis.builder.StaticSqlSource;
/*    */ import org.apache.ibatis.builder.annotation.ProviderSqlSource;
/*    */ import org.apache.ibatis.mapping.BoundSql;
/*    */ import org.apache.ibatis.mapping.SqlSource;
/*    */ import org.apache.ibatis.reflection.MetaObject;
/*    */ import org.apache.ibatis.reflection.SystemMetaObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */
public class OrderByProviderSqlSource implements SqlSource, OrderBySqlSource {
    private SqlSourceBuilder sqlSourceParser;
    private Class<?> providerType;
    private Method providerMethod;
    private Boolean providerTakesParameterObject;
    private SqlSource original;

    public OrderByProviderSqlSource(ProviderSqlSource provider) {
        MetaObject metaObject = SystemMetaObject.forObject(provider);
        this.sqlSourceParser = (SqlSourceBuilder)metaObject.getValue("sqlSourceParser");
        this.providerType = (Class)metaObject.getValue("providerType");
        this.providerMethod = (Method)metaObject.getValue("providerMethod");
        this.providerTakesParameterObject = (Boolean)metaObject.getValue("providerTakesParameterObject");
        this.original = provider;
    }

    public BoundSql getBoundSql(Object parameterObject) {
        SqlSource sqlSource = this.createSqlSource(parameterObject);
        return sqlSource.getBoundSql(parameterObject);
    }

    private SqlSource createSqlSource(Object parameterObject) {
        try {
            String e;
            if(this.providerTakesParameterObject.booleanValue()) {
                e = (String)this.providerMethod.invoke(this.providerType.newInstance(), new Object[]{parameterObject});
            } else {
                e = (String)this.providerMethod.invoke(this.providerType.newInstance(), new Object[0]);
            }

            Class parameterType = parameterObject == null?Object.class:parameterObject.getClass();
            StaticSqlSource sqlSource = (StaticSqlSource)this.sqlSourceParser.parse(e, parameterType, new HashMap());
            return new OrderByStaticSqlSource(sqlSource);
        } catch (Exception var5) {
            throw new BuilderException("Error invoking SqlProvider method (" + this.providerType.getName() + "." + this.providerMethod.getName() + ").  Cause: " + var5, var5);
        }
    }

    public SqlSource getOriginal() {
        return this.original;
    }
}
