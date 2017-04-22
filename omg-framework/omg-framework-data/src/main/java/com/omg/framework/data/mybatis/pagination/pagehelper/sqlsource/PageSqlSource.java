/*    */ package com.omg.framework.data.mybatis.pagination.pagehelper.sqlsource;
/*    */ 
/*    */ import com.omg.framework.data.mybatis.pagination.pagehelper.SqlUtil;
/*    */ import org.apache.ibatis.mapping.BoundSql;
/*    */ import org.apache.ibatis.mapping.SqlSource;
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
/*    */ public abstract class PageSqlSource
/*    */   implements SqlSource
/*    */ {
/*    */   protected Boolean getCount()
/*    */   {
/* 22 */     return SqlUtil.getCOUNT();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract BoundSql getDefaultBoundSql(Object paramObject);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract BoundSql getCountBoundSql(Object paramObject);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract BoundSql getPageBoundSql(Object paramObject);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BoundSql getBoundSql(Object parameterObject)
/*    */   {
/* 56 */     Boolean count = getCount();
/* 57 */     if (count == null)
/* 58 */       return getDefaultBoundSql(parameterObject);
/* 59 */     if (count.booleanValue()) {
/* 60 */       return getCountBoundSql(parameterObject);
/*    */     }
/* 62 */     return getPageBoundSql(parameterObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\omg.framework.data.mybatis\omg.framework.data.mybatis-mybatis-pagination\1.4.2-SNAPSHOT\omg.framework.data.mybatis-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\omg.framework.data.mybatis\pagination\pagehelper\sqlsource\PageSqlSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */