/*     */ package com.omg.framework.data.mybatis.pagination.pagehelper;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ import javax.sql.DataSource;
/*     */ import org.apache.ibatis.executor.Executor;
/*     */ import org.apache.ibatis.mapping.MappedStatement;
/*     */ import org.apache.ibatis.plugin.Interceptor;
/*     */ import org.apache.ibatis.plugin.Intercepts;
/*     */ import org.apache.ibatis.plugin.Invocation;
/*     */ import org.apache.ibatis.plugin.Plugin;
/*     */ import org.apache.ibatis.reflection.MetaObject;
/*     */ import org.apache.ibatis.reflection.SystemMetaObject;
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
/*     */ @Intercepts({@org.apache.ibatis.plugin.Signature(type=Executor.class, method="query", args={MappedStatement.class, Object.class, org.apache.ibatis.session.RowBounds.class, org.apache.ibatis.session.ResultHandler.class})})
/*     */ public class PageHelper
/*     */   implements Interceptor
/*     */ {
/*     */   private SqlUtil sqlUtil;
/*     */   private Properties properties;
/*     */   private Boolean autoDialect;
/*     */   
/*     */   public static Page startPage(int pageNum, int pageSize)
/*     */   {
/*  63 */     return startPage(pageNum, pageSize, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Page startPage(int pageNum, int pageSize, boolean count)
/*     */   {
/*  74 */     return startPage(pageNum, pageSize, count, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Page startPage(int pageNum, int pageSize, String orderBy)
/*     */   {
/*  85 */     Page page = startPage(pageNum, pageSize);
/*  86 */     page.setOrderBy(orderBy);
/*  87 */     return page;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Page offsetPage(int offset, int limit)
/*     */   {
/*  97 */     return offsetPage(offset, limit, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Page offsetPage(int offset, int limit, boolean count)
/*     */   {
/* 108 */     Page page = new Page(new int[] { offset, limit }, count);
/*     */     
/* 110 */     Page oldPage = SqlUtil.getLocalPage();
/* 111 */     if ((oldPage != null) && (oldPage.isOrderByOnly())) {
/* 112 */       page.setOrderBy(oldPage.getOrderBy());
/*     */     }
/* 114 */     SqlUtil.setLocalPage(page);
/* 115 */     return page;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Page offsetPage(int offset, int limit, String orderBy)
/*     */   {
/* 126 */     Page page = offsetPage(offset, limit);
/* 127 */     page.setOrderBy(orderBy);
/* 128 */     return page;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Page startPage(int pageNum, int pageSize, boolean count, Boolean reasonable)
/*     */   {
/* 140 */     return startPage(pageNum, pageSize, count, reasonable, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Page startPage(int pageNum, int pageSize, boolean count, Boolean reasonable, Boolean pageSizeZero)
/*     */   {
/* 153 */     Page page = new Page(pageNum, pageSize, count);
/* 154 */     page.setReasonable(reasonable);
/* 155 */     page.setPageSizeZero(pageSizeZero);
/*     */     
/* 157 */     Page oldPage = SqlUtil.getLocalPage();
/* 158 */     if ((oldPage != null) && (oldPage.isOrderByOnly())) {
/* 159 */       page.setOrderBy(oldPage.getOrderBy());
/*     */     }
/* 161 */     SqlUtil.setLocalPage(page);
/* 162 */     return page;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Page startPage(Object params)
/*     */   {
/* 171 */     Page page = SqlUtil.getPageFromObject(params);
/*     */     
/* 173 */     Page oldPage = SqlUtil.getLocalPage();
/* 174 */     if ((oldPage != null) && (oldPage.isOrderByOnly())) {
/* 175 */       page.setOrderBy(oldPage.getOrderBy());
/*     */     }
/* 177 */     SqlUtil.setLocalPage(page);
/* 178 */     return page;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void orderBy(String orderBy)
/*     */   {
/* 187 */     Page page = SqlUtil.getLocalPage();
/* 188 */     if (page != null) {
/* 189 */       page.setOrderBy(orderBy);
/*     */     } else {
/* 191 */       page = new Page();
/* 192 */       page.setOrderBy(orderBy);
/* 193 */       page.setOrderByOnly(true);
/* 194 */       SqlUtil.setLocalPage(page);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getOrderBy()
/*     */   {
/* 204 */     Page page = SqlUtil.getLocalPage();
/* 205 */     if (page != null) {
/* 206 */       String orderBy = page.getOrderBy();
/* 207 */       if ((orderBy == null) || (orderBy.length() == 0)) {
/* 208 */         return null;
/*     */       }
/* 210 */       return orderBy;
/*     */     }
/*     */     
/* 213 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object intercept(Invocation invocation)
/*     */     throws Throwable
/*     */   {
/* 224 */     if (this.autoDialect.booleanValue()) {
/* 225 */       initSqlUtil(invocation);
/*     */     }
/* 227 */     return this.sqlUtil.processPage(invocation);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void initSqlUtil(Invocation invocation)
/*     */   {
/* 236 */     if (this.sqlUtil == null) {
/* 237 */       String url = null;
/*     */       try {
/* 239 */         MappedStatement ms = (MappedStatement)invocation.getArgs()[0];
/* 240 */         MetaObject msObject = SystemMetaObject.forObject(ms);
/* 241 */         DataSource dataSource = (DataSource)msObject.getValue("configuration.environment.dataSource");
/* 242 */         url = dataSource.getConnection().getMetaData().getURL();
/*     */       } catch (SQLException e) {
/* 244 */         throw new RuntimeException("分页插件初始化异常:" + e.getMessage());
/*     */       }
/* 246 */       if ((url == null) || (url.length() == 0)) {
/* 247 */         throw new RuntimeException("无法自动获取jdbcUrl，请在分页插件中配置dialect参数!");
/*     */       }
/* 249 */       String dialect = Dialect.fromJdbcUrl(url);
/* 250 */       if (dialect == null) {
/* 251 */         throw new RuntimeException("无法自动获取数据库类型，请通过dialect参数指定!");
/*     */       }
/* 253 */       this.sqlUtil = new SqlUtil(dialect);
/* 254 */       this.sqlUtil.setProperties(this.properties);
/* 255 */       this.properties = null;
/* 256 */       this.autoDialect = Boolean.valueOf(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object plugin(Object target)
/*     */   {
/* 267 */     if ((target instanceof Executor)) {
/* 268 */       return Plugin.wrap(target, this);
/*     */     }
/* 270 */     return target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperties(Properties p)
/*     */   {
/*     */     try
/*     */     {
/* 282 */       Class.forName("org.apache.ibatis.scripting.xmltags.SqlNode");
/*     */     } catch (ClassNotFoundException e) {
/* 284 */       throw new RuntimeException("您使用的MyBatis版本太低，MyBatis分页插件PageHelper支持MyBatis3.2.0及以上版本!");
/*     */     }
/*     */     
/* 287 */     String dialect = p.getProperty("dialect");
/* 288 */     if ((dialect == null) || (dialect.length() == 0)) {
/* 289 */       this.autoDialect = Boolean.valueOf(true);
/* 290 */       this.properties = p;
/*     */     } else {
/* 292 */       this.autoDialect = Boolean.valueOf(false);
/* 293 */       this.sqlUtil = new SqlUtil(dialect);
/* 294 */       this.sqlUtil.setProperties(p);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\eif\framework\eif-framework-mybatis-pagination\1.4.2-SNAPSHOT\eif-framework-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\eif\framework\pagination\pagehelper\PageHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */