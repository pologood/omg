/*    */ package com.omg.framework.data.mybatis.pagination.orderbyhelper.sqlsource;
/*    */ 
/*    */ import org.apache.ibatis.builder.StaticSqlSource;
/*    */ import org.apache.ibatis.mapping.BoundSql;
/*    */ import org.apache.ibatis.mapping.SqlSource;
/*    */ import org.apache.ibatis.reflection.MetaObject;
/*    */ import org.apache.ibatis.reflection.SystemMetaObject;
/*    */ import org.apache.ibatis.scripting.defaults.RawSqlSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OrderByRawSqlSource
/*    */   implements SqlSource, OrderBySqlSource
/*    */ {
/*    */   private final SqlSource sqlSource;
/*    */   private SqlSource original;
/*    */   
/*    */   public OrderByRawSqlSource(RawSqlSource sqlSource)
/*    */   {
/* 22 */     MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
/* 23 */     this.sqlSource = new OrderByStaticSqlSource((StaticSqlSource)metaObject.getValue("sqlSource"));
/* 24 */     this.original = sqlSource;
/*    */   }
/*    */   
/*    */   public BoundSql getBoundSql(Object parameterObject) {
/* 28 */     return this.sqlSource.getBoundSql(parameterObject);
/*    */   }
/*    */   
/*    */   public SqlSource getOriginal() {
/* 32 */     return this.original;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\eif\framework\eif-framework-mybatis-pagination\1.4.2-SNAPSHOT\eif-framework-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\eif\framework\pagination\orderbyhelper\sqlsource\OrderByRawSqlSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */