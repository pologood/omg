/*    */ package com.omg.framework.data.mybatis.pagination.pagehelper.sqlsource;
/*    */ 
/*    */ import com.omg.framework.data.mybatis.pagination.orderbyhelper.OrderByParser;
/*    */ import com.omg.framework.data.mybatis.pagination.orderbyhelper.sqlsource.OrderBySqlSource;
/*    */ import com.omg.framework.data.mybatis.pagination.pagehelper.PageHelper;
/*    */ import com.omg.framework.data.mybatis.pagination.pagehelper.parser.Parser;
/*    */ import java.util.List;
/*    */ import org.apache.ibatis.builder.StaticSqlSource;
/*    */ import org.apache.ibatis.mapping.BoundSql;
/*    */ import org.apache.ibatis.mapping.ParameterMapping;
/*    */ import org.apache.ibatis.mapping.SqlSource;
/*    */ import org.apache.ibatis.reflection.MetaObject;
/*    */ import org.apache.ibatis.reflection.SystemMetaObject;
/*    */ import org.apache.ibatis.session.Configuration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PageStaticSqlSource
/*    */   extends PageSqlSource
/*    */   implements OrderBySqlSource
/*    */ {
/*    */   private String sql;
/*    */   private List<ParameterMapping> parameterMappings;
/*    */   private Configuration configuration;
/*    */   private Parser parser;
/*    */   private SqlSource original;
/*    */   
/*    */   public PageStaticSqlSource(StaticSqlSource sqlSource, Parser parser)
/*    */   {
/* 32 */     MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
/* 33 */     this.sql = ((String)metaObject.getValue("sql"));
/* 34 */     this.parameterMappings = ((List)metaObject.getValue("parameterMappings"));
/* 35 */     this.configuration = ((Configuration)metaObject.getValue("configuration"));
/* 36 */     this.original = sqlSource;
/* 37 */     this.parser = parser;
/*    */   }
/*    */   
/*    */   protected BoundSql getDefaultBoundSql(Object parameterObject)
/*    */   {
/* 42 */     String tempSql = this.sql;
/* 43 */     String orderBy = PageHelper.getOrderBy();
/* 44 */     if (orderBy != null) {
/* 45 */       tempSql = OrderByParser.converToOrderBySql(this.sql, orderBy);
/*    */     }
/* 47 */     return new BoundSql(this.configuration, tempSql, this.parameterMappings, parameterObject);
/*    */   }
/*    */   
/*    */   protected BoundSql getCountBoundSql(Object parameterObject)
/*    */   {
/* 52 */     return new BoundSql(this.configuration, this.parser.getCountSql(this.sql), this.parameterMappings, parameterObject);
/*    */   }
/*    */   
/*    */   protected BoundSql getPageBoundSql(Object parameterObject)
/*    */   {
/* 57 */     String tempSql = this.sql;
/* 58 */     String orderBy = PageHelper.getOrderBy();
/* 59 */     if (orderBy != null) {
/* 60 */       tempSql = OrderByParser.converToOrderBySql(this.sql, orderBy);
/*    */     }
/* 62 */     tempSql = this.parser.getPageSql(tempSql);
/* 63 */     return new BoundSql(this.configuration, tempSql, this.parser.getPageParameterMapping(this.configuration, this.original.getBoundSql(parameterObject)), parameterObject);
/*    */   }
/*    */   
/*    */   public SqlSource getOriginal() {
/* 67 */     return this.original;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\omg.framework.data.mybatis\omg.framework.data.mybatis-mybatis-pagination\1.4.2-SNAPSHOT\omg.framework.data.mybatis-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\omg.framework.data.mybatis\pagination\pagehelper\sqlsource\PageStaticSqlSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */