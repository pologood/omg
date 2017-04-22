/*    */ package com.omg.framework.data.mybatis.pagination.orderbyhelper;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.sf.jsqlparser.parser.CCJSqlParserUtil;
/*    */ import net.sf.jsqlparser.statement.Statement;
/*    */ import net.sf.jsqlparser.statement.select.OrderByElement;
/*    */ import net.sf.jsqlparser.statement.select.PlainSelect;
/*    */ import net.sf.jsqlparser.statement.select.Select;
/*    */ import net.sf.jsqlparser.statement.select.SelectBody;
/*    */ import net.sf.jsqlparser.statement.select.SetOperationList;
/*    */ import net.sf.jsqlparser.statement.select.WithItem;
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
/*    */ public class OrderByParser
/*    */ {
/*    */   public static String converToOrderBySql(String sql, String orderBy)
/*    */   {
/* 30 */     Statement stmt = null;
/*    */     try {
/* 32 */       stmt = CCJSqlParserUtil.parse(sql);
/* 33 */       Select select = (Select)stmt;
/* 34 */       SelectBody selectBody = select.getSelectBody();
/*    */       
/* 36 */       List<OrderByElement> orderByElements = extraOrderBy(selectBody);
/* 37 */       String defaultOrderBy = PlainSelect.orderByToString(orderByElements);
/* 38 */       if (defaultOrderBy.indexOf('?') != -1) {
/* 39 */         throw new RuntimeException("原SQL[" + sql + "]中的order by包含参数，因此不能使用OrderBy插件进行修改!");
/*    */       }
/*    */       
/* 42 */       sql = select.toString();
/*    */     } catch (Throwable e) {
/* 44 */       e.printStackTrace();
/*    */     }
/* 46 */     return sql + " order by " + orderBy;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static List<OrderByElement> extraOrderBy(SelectBody selectBody)
/*    */   {
/* 55 */     if ((selectBody instanceof PlainSelect)) {
/* 56 */       List<OrderByElement> orderByElements = ((PlainSelect)selectBody).getOrderByElements();
/* 57 */       ((PlainSelect)selectBody).setOrderByElements(null);
/* 58 */       return orderByElements; }
/* 59 */     if ((selectBody instanceof WithItem)) {
/* 60 */       WithItem withItem = (WithItem)selectBody;
/* 61 */       if (withItem.getSelectBody() != null) {
/* 62 */         return extraOrderBy(withItem.getSelectBody());
/*    */       }
/*    */     } else {
/* 65 */       SetOperationList operationList = (SetOperationList)selectBody;
/* 66 */       if ((operationList.getPlainSelects() != null) && (operationList.getPlainSelects().size() > 0)) {
/* 67 */         List<PlainSelect> plainSelects = operationList.getPlainSelects();
/* 68 */         List<OrderByElement> orderByElements = ((PlainSelect)plainSelects.get(plainSelects.size() - 1)).getOrderByElements();
/* 69 */         ((PlainSelect)plainSelects.get(plainSelects.size() - 1)).setOrderByElements(null);
/* 70 */         return orderByElements;
/*    */       }
/*    */     }
/* 73 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\eif\framework\eif-framework-mybatis-pagination\1.4.2-SNAPSHOT\eif-framework-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\eif\framework\pagination\orderbyhelper\OrderByParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */