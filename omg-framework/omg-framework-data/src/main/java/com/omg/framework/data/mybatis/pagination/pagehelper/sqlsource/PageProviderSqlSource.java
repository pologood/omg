/*     */ package com.omg.framework.data.mybatis.pagination.pagehelper.sqlsource;
/*     */ 
/*     */ import com.omg.framework.data.mybatis.pagination.orderbyhelper.sqlsource.OrderBySqlSource;
/*     */ import com.omg.framework.data.mybatis.pagination.orderbyhelper.sqlsource.OrderByStaticSqlSource;
/*     */ import com.omg.framework.data.mybatis.pagination.pagehelper.Constant;
/*     */ import com.omg.framework.data.mybatis.pagination.pagehelper.parser.Parser;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.ibatis.builder.BuilderException;
/*     */ import org.apache.ibatis.builder.SqlSourceBuilder;
/*     */ import org.apache.ibatis.builder.StaticSqlSource;
/*     */ import org.apache.ibatis.builder.annotation.ProviderSqlSource;
/*     */ import org.apache.ibatis.mapping.BoundSql;
/*     */ import org.apache.ibatis.mapping.SqlSource;
/*     */ import org.apache.ibatis.reflection.MetaObject;
/*     */ import org.apache.ibatis.reflection.SystemMetaObject;
/*     */ import org.apache.ibatis.session.Configuration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PageProviderSqlSource
/*     */   extends PageSqlSource
/*     */   implements OrderBySqlSource, Constant
/*     */ {
/*     */   private SqlSourceBuilder sqlSourceParser;
/*     */   private Class<?> providerType;
/*     */   private Method providerMethod;
/*     */   private Boolean providerTakesParameterObject;
/*     */   private SqlSource original;
/*     */   private Configuration configuration;
/*     */   private Parser parser;
/*     */   
/*     */   public PageProviderSqlSource(ProviderSqlSource provider, Parser parser)
/*     */   {
/*  60 */     MetaObject metaObject = SystemMetaObject.forObject(provider);
/*  61 */     this.sqlSourceParser = ((SqlSourceBuilder)metaObject.getValue("sqlSourceParser"));
/*  62 */     this.providerType = ((Class)metaObject.getValue("providerType"));
/*  63 */     this.providerMethod = ((Method)metaObject.getValue("providerMethod"));
/*  64 */     this.providerTakesParameterObject = ((Boolean)metaObject.getValue("providerTakesParameterObject"));
/*  65 */     this.configuration = ((Configuration)metaObject.getValue("sqlSourceParser.configuration"));
/*  66 */     this.original = provider;
/*  67 */     this.parser = parser;
/*     */   }
/*     */   
/*     */   private SqlSource createSqlSource(Object parameterObject) {
/*     */     try {
/*     */       String sql;
/*  73 */       if (this.providerTakesParameterObject.booleanValue()) {
/*  74 */         sql = (String)this.providerMethod.invoke(this.providerType.newInstance(), new Object[] { parameterObject });
/*     */       } else {
/*  76 */         sql = (String)this.providerMethod.invoke(this.providerType.newInstance(), new Object[0]);
/*     */       }
/*  78 */       Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
/*  79 */       StaticSqlSource sqlSource = (StaticSqlSource)this.sqlSourceParser.parse(sql, parameterType, new HashMap());
/*  80 */       return new OrderByStaticSqlSource(sqlSource);
/*     */     } catch (Exception e) {
/*  82 */       throw new BuilderException("Error invoking SqlProvider method (" + this.providerType.getName() + "." + this.providerMethod.getName() + ").  Cause: " + e, e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected BoundSql getDefaultBoundSql(Object parameterObject)
/*     */   {
/*  90 */     SqlSource sqlSource = createSqlSource(parameterObject);
/*  91 */     return sqlSource.getBoundSql(parameterObject);
/*     */   }
/*     */   
/*     */ 
/*     */   protected BoundSql getCountBoundSql(Object parameterObject)
/*     */   {
/*  97 */     SqlSource sqlSource = createSqlSource(parameterObject);
/*  98 */     BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
/*  99 */     return new BoundSql(this.configuration, this.parser.getCountSql(boundSql.getSql()), boundSql.getParameterMappings(), parameterObject);
/*     */   }
/*     */   
/*     */ 
/*     */   protected BoundSql getPageBoundSql(Object parameterObject)
/*     */   {
/*     */
/*     */     BoundSql boundSql;
/*     */     
/* 109 */     if (((parameterObject instanceof Map)) && (((Map)parameterObject).containsKey("_provider_object"))) {
/* 110 */       SqlSource sqlSource = createSqlSource(((Map)parameterObject).get("_provider_object"));
/* 111 */       boundSql = sqlSource.getBoundSql(((Map)parameterObject).get("_provider_object"));
/*     */     } else {
/* 113 */       SqlSource sqlSource = createSqlSource(parameterObject);
/* 114 */       boundSql = sqlSource.getBoundSql(parameterObject);
/*     */     }
/* 116 */     return new BoundSql(this.configuration, this.parser.getPageSql(boundSql.getSql()), this.parser.getPageParameterMapping(this.configuration, boundSql), parameterObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SqlSource getOriginal()
/*     */   {
/* 124 */     return this.original;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\omg.framework.data.mybatis\omg.framework.data.mybatis-mybatis-pagination\1.4.2-SNAPSHOT\omg.framework.data.mybatis-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\omg.framework.data.mybatis\pagination\pagehelper\sqlsource\PageProviderSqlSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */