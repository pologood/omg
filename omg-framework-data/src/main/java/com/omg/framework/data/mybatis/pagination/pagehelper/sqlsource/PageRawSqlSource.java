/*    */ package com.omg.framework.data.mybatis.pagination.pagehelper.sqlsource;
/*    */ 
/*    */ import com.omg.framework.data.mybatis.pagination.orderbyhelper.sqlsource.OrderBySqlSource;
/*    */ import com.omg.framework.data.mybatis.pagination.pagehelper.parser.Parser;
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
/*    */ public class PageRawSqlSource
/*    */   extends PageSqlSource
/*    */   implements OrderBySqlSource
/*    */ {
/*    */   private PageSqlSource sqlSource;
/*    */   private SqlSource original;
/*    */   
/*    */   public PageRawSqlSource(RawSqlSource sqlSource, Parser parser)
/*    */   {
/* 24 */     MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
/* 25 */     this.sqlSource = new PageStaticSqlSource((StaticSqlSource)metaObject.getValue("sqlSource"), parser);
/* 26 */     this.original = sqlSource;
/*    */   }
/*    */   
/*    */   protected BoundSql getDefaultBoundSql(Object parameterObject)
/*    */   {
/* 31 */     return this.sqlSource.getDefaultBoundSql(parameterObject);
/*    */   }
/*    */   
/*    */   protected BoundSql getCountBoundSql(Object parameterObject)
/*    */   {
/* 36 */     return this.sqlSource.getCountBoundSql(parameterObject);
/*    */   }
/*    */   
/*    */   protected BoundSql getPageBoundSql(Object parameterObject)
/*    */   {
/* 41 */     return this.sqlSource.getPageBoundSql(parameterObject);
/*    */   }
/*    */   
/*    */   public SqlSource getOriginal() {
/* 45 */     return this.original;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\omg.framework.data.mybatis\omg.framework.data.mybatis-mybatis-pagination\1.4.2-SNAPSHOT\omg.framework.data.mybatis-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\omg.framework.data.mybatis\pagination\pagehelper\sqlsource\PageRawSqlSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */