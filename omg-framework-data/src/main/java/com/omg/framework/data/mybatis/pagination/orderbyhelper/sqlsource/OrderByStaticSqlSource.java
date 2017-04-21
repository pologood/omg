/*    */ package com.omg.framework.data.mybatis.pagination.orderbyhelper.sqlsource;
/*    */ 
/*    */ import com.omg.framework.data.mybatis.pagination.orderbyhelper.OrderByParser;
/*    */ import com.omg.framework.data.mybatis.pagination.pagehelper.PageHelper;
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
/*    */ 
/*    */ public class OrderByStaticSqlSource
/*    */   implements SqlSource, OrderBySqlSource
/*    */ {
/*    */   private String sql;
/*    */   private List<ParameterMapping> parameterMappings;
/*    */   private Configuration configuration;
/*    */   private SqlSource original;
/*    */   
/*    */   public OrderByStaticSqlSource(StaticSqlSource sqlSource)
/*    */   {
/* 29 */     MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
/* 30 */     this.sql = ((String)metaObject.getValue("sql"));
/* 31 */     this.parameterMappings = ((List)metaObject.getValue("parameterMappings"));
/* 32 */     this.configuration = ((Configuration)metaObject.getValue("configuration"));
/* 33 */     this.original = sqlSource;
/*    */   }
/*    */   
/*    */   public BoundSql getBoundSql(Object parameterObject) {
/* 37 */     String orderBy = PageHelper.getOrderBy();
/* 38 */     String tempSql = this.sql;
/* 39 */     if (orderBy != null) {
/* 40 */       tempSql = OrderByParser.converToOrderBySql(this.sql, orderBy);
/*    */     }
/* 42 */     return new BoundSql(this.configuration, tempSql, this.parameterMappings, parameterObject);
/*    */   }
/*    */   
/*    */   public SqlSource getOriginal() {
/* 46 */     return this.original;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\omg.framework.data.mybatis\omg.framework.data.mybatis-mybatis-pagination\1.4.2-SNAPSHOT\omg.framework.data.mybatis-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\omg.framework.data.mybatis\pagination\orderbyhelper\sqlsource\OrderByStaticSqlSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */