/*     */ package com.omg.framework.data.mybatis.pagination.pagehelper.parser.impl;
/*     */ 
/*     */ import com.omg.framework.data.mybatis.pagination.pagehelper.Constant;
/*     */ import com.omg.framework.data.mybatis.pagination.pagehelper.Dialect;
/*     */ import com.omg.framework.data.mybatis.pagination.pagehelper.Page;
/*     */ import com.omg.framework.data.mybatis.pagination.pagehelper.parser.Parser;
/*     */ import com.omg.framework.data.mybatis.pagination.pagehelper.parser.SqlParser;
/*     */ import com.omg.framework.data.mybatis.pagination.pagehelper.sqlsource.PageProviderSqlSource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.ibatis.mapping.BoundSql;
/*     */ import org.apache.ibatis.mapping.MappedStatement;
/*     */ import org.apache.ibatis.mapping.ParameterMapping;
/*     */ import org.apache.ibatis.mapping.ParameterMapping.Builder;
/*     */ import org.apache.ibatis.reflection.MetaObject;
/*     */ import org.apache.ibatis.reflection.SystemMetaObject;
/*     */ import org.apache.ibatis.session.Configuration;
/*     */ import org.apache.ibatis.type.TypeHandlerRegistry;
/*     */
/*     */ public abstract class AbstractParser
/*     */   implements Parser, Constant
/*     */ {
/*  51 */   public static final SqlParser sqlParser = new SqlParser();
/*     */
/*     */   public static Parser newParser(Dialect dialect) {
/*  54 */     Parser parser = null;
/*  55 */     switch (dialect) {
/*     */     case mysql:
/*     */     case mariadb:
/*     */     case sqlite:
/*  59 */       parser = new MysqlParser();
/*  60 */       break;
/*     */     default:
/*  62 */       throw new RuntimeException("分页插件" + dialect + "方言错误!");
/*     */     }
/*  64 */     return parser;
/*     */   }
/*     */
/*     */   public static Map<String, Object> processParameter(MappedStatement ms, Object parameterObject, BoundSql boundSql) {
/*  68 */     Map paramMap = null;
/*  69 */     boolean hasTypeHandler; if (parameterObject == null) {
/*  70 */       paramMap = new HashMap();
/*  71 */     } else if ((parameterObject instanceof Map))
/*     */     {
/*  73 */       paramMap = new HashMap();
/*  74 */       paramMap.putAll((Map)parameterObject);
/*     */     } else {
/*  76 */       paramMap = new HashMap();
/*     */
/*     */
/*  79 */       hasTypeHandler = ms.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass());
/*  80 */       MetaObject metaObject = SystemMetaObject.forObject(parameterObject);
/*     */
/*  82 */       if ((ms.getSqlSource() instanceof PageProviderSqlSource)) {
/*  83 */         paramMap.put("_provider_object", parameterObject);
/*     */       }
/*  85 */       if (!hasTypeHandler) {
/*  86 */         for (String name : metaObject.getGetterNames()) {
/*  87 */           paramMap.put(name, metaObject.getValue(name));
/*     */         }
/*     */       }
/*     */
/*  91 */       if ((boundSql.getParameterMappings() != null) && (boundSql.getParameterMappings().size() > 0)) {
/*  92 */         for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
/*  93 */           String name = parameterMapping.getProperty();
/*  94 */           if ((!name.equals("First_PageHelper")) && (!name.equals("Second_PageHelper")) && (paramMap.get(name) == null))
/*     */           {
/*     */
/*  97 */             if ((hasTypeHandler) || (parameterMapping.getJavaType().equals(parameterObject.getClass())))
/*     */             {
/*  99 */               paramMap.put(name, parameterObject);
/* 100 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */
/* 107 */     paramMap.put("_ORIGINAL_PARAMETER_OBJECT", parameterObject);
/* 108 */     return paramMap;
/*     */   }
/*     */
/*     */   public boolean isSupportedMappedStatementCache() {
/* 112 */     return true;
/*     */   }
/*     */
/*     */   public String getCountSql(String sql) {
/* 116 */     return sqlParser.getSmartCountSql(sql);
/*     */   }
/*     */
/*     */   public abstract String getPageSql(String paramString);
/*     */
/*     */   public List<ParameterMapping> getPageParameterMapping(Configuration configuration, BoundSql boundSql) {
/* 122 */     List<ParameterMapping> newParameterMappings = new ArrayList();
/* 123 */     if ((boundSql != null) && (boundSql.getParameterMappings() != null)) {
/* 124 */       newParameterMappings.addAll(boundSql.getParameterMappings());
/*     */     }
/* 126 */     newParameterMappings.add(new ParameterMapping.Builder(configuration, "First_PageHelper", Integer.class).build());
/* 127 */     newParameterMappings.add(new ParameterMapping.Builder(configuration, "Second_PageHelper", Integer.class).build());
/* 128 */     return newParameterMappings;
/*     */   }
/*     */
/*     */   public Map setPageParameter(MappedStatement ms, Object parameterObject, BoundSql boundSql, Page page) {
/* 132 */     return processParameter(ms, parameterObject, boundSql);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\omg.framework.data.mybatis\omg.framework.data.mybatis-mybatis-pagination\1.4.2-SNAPSHOT\omg.framework.data.mybatis-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\omg.framework.data.mybatis\pagination\pagehelper\parser\impl\AbstractParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */