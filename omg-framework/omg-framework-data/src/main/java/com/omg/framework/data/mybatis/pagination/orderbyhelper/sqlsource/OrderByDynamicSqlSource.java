/*    */ package com.omg.framework.data.mybatis.pagination.orderbyhelper.sqlsource;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.apache.ibatis.builder.SqlSourceBuilder;
/*    */ import org.apache.ibatis.builder.StaticSqlSource;
/*    */ import org.apache.ibatis.mapping.BoundSql;
/*    */ import org.apache.ibatis.mapping.SqlSource;
/*    */ import org.apache.ibatis.reflection.MetaObject;
/*    */ import org.apache.ibatis.reflection.SystemMetaObject;
/*    */ import org.apache.ibatis.scripting.xmltags.DynamicContext;
/*    */ import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
/*    */ import org.apache.ibatis.scripting.xmltags.SqlNode;
/*    */ import org.apache.ibatis.session.Configuration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OrderByDynamicSqlSource
/*    */   implements SqlSource, OrderBySqlSource
/*    */ {
/*    */   private Configuration configuration;
/*    */   private SqlNode rootSqlNode;
/*    */   private SqlSource original;
/*    */   
/*    */   public OrderByDynamicSqlSource(DynamicSqlSource sqlSource)
/*    */   {
/* 29 */     MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
/* 30 */     this.configuration = ((Configuration)metaObject.getValue("configuration"));
/* 31 */     this.rootSqlNode = ((SqlNode)metaObject.getValue("rootSqlNode"));
/* 32 */     this.original = sqlSource;
/*    */   }
/*    */   
/*    */   public BoundSql getBoundSql(Object parameterObject) {
/* 36 */     DynamicContext context = new DynamicContext(this.configuration, parameterObject);
/* 37 */     this.rootSqlNode.apply(context);
/* 38 */     SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
/* 39 */     Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
/* 40 */     SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
/* 41 */     sqlSource = new OrderByStaticSqlSource((StaticSqlSource)sqlSource);
/* 42 */     BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
/* 43 */     for (Entry<String, Object> entry : context.getBindings().entrySet()) {
/* 44 */       boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
/*    */     }
/* 46 */     return boundSql;
/*    */   }
/*    */   
/*    */   public SqlSource getOriginal() {
/* 50 */     return this.original;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\eif\framework\eif-framework-mybatis-pagination\1.4.2-SNAPSHOT\eif-framework-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\eif\framework\pagination\orderbyhelper\sqlsource\OrderByDynamicSqlSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */