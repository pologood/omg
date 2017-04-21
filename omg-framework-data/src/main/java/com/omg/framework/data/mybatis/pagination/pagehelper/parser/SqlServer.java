/*     */ package com.omg.framework.data.mybatis.pagination.pagehelper.parser;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.sf.jsqlparser.expression.Alias;
/*     */ import net.sf.jsqlparser.expression.LongValue;
/*     */ import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
/*     */ import net.sf.jsqlparser.parser.CCJSqlParserUtil;
/*     */ import net.sf.jsqlparser.schema.Column;
/*     */ import net.sf.jsqlparser.statement.Statement;
/*     */ import net.sf.jsqlparser.statement.select.AllColumns;
/*     */ import net.sf.jsqlparser.statement.select.AllTableColumns;
/*     */ import net.sf.jsqlparser.statement.select.FromItem;
/*     */ import net.sf.jsqlparser.statement.select.Join;
/*     */ import net.sf.jsqlparser.statement.select.LateralSubSelect;
/*     */ import net.sf.jsqlparser.statement.select.OrderByElement;
/*     */ import net.sf.jsqlparser.statement.select.PlainSelect;
/*     */ import net.sf.jsqlparser.statement.select.Select;
/*     */ import net.sf.jsqlparser.statement.select.SelectBody;
/*     */ import net.sf.jsqlparser.statement.select.SelectExpressionItem;
/*     */ import net.sf.jsqlparser.statement.select.SelectItem;
/*     */ import net.sf.jsqlparser.statement.select.SetOperationList;
/*     */ import net.sf.jsqlparser.statement.select.SubJoin;
/*     */ import net.sf.jsqlparser.statement.select.SubSelect;
/*     */ import net.sf.jsqlparser.statement.select.Top;
/*     */ import net.sf.jsqlparser.statement.select.ValuesList;
/*     */ import net.sf.jsqlparser.statement.select.WithItem;
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
/*     */ public class SqlServer
/*     */ {
/*  58 */   private static final Map<String, String> CACHE = new ConcurrentHashMap();
/*     */   
/*  60 */   private static final String START_ROW = String.valueOf(Long.MIN_VALUE);
/*     */   
/*  62 */   private static final String PAGE_SIZE = String.valueOf(Long.MAX_VALUE);
/*     */   
/*     */   private static final String WRAP_TABLE = "WRAP_OUTER_TABLE";
/*     */   
/*     */   private static final String PAGE_TABLE_NAME = "PAGE_TABLE_ALIAS";
/*     */   
/*  68 */   public static final Alias PAGE_TABLE_ALIAS = new Alias("PAGE_TABLE_ALIAS");
/*     */   
/*     */   private static final String PAGE_ROW_NUMBER = "PAGE_ROW_NUMBER";
/*     */   
/*  72 */   private static final Column PAGE_ROW_NUMBER_COLUMN = new Column("PAGE_ROW_NUMBER");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   private static final Top TOP100_PERCENT = new Top();
/*  79 */   static { TOP100_PERCENT.setRowCount(100L);
/*  80 */     TOP100_PERCENT.setPercentage(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String convertToPageSql(String sql, int offset, int limit)
/*     */   {
/*  92 */     String pageSql = (String)CACHE.get(sql);
/*  93 */     if (pageSql == null)
/*     */     {
/*     */       Statement stmt;
/*     */       try {
/*  97 */         stmt = CCJSqlParserUtil.parse(sql);
/*     */       } catch (Throwable e) {
/*  99 */         throw new RuntimeException("不支持该SQL转换为分页查询!");
/*     */       }
/* 101 */       if (!(stmt instanceof Select)) {
/* 102 */         throw new RuntimeException("分页语句必须是Select查询!");
/*     */       }
/*     */       
/* 105 */       Select pageSelect = getPageSelect((Select)stmt);
/* 106 */       pageSql = pageSelect.toString();
/* 107 */       CACHE.put(sql, pageSql);
/*     */     }
/* 109 */     pageSql = pageSql.replace(START_ROW, String.valueOf(offset));
/* 110 */     pageSql = pageSql.replace(PAGE_SIZE, String.valueOf(limit));
/* 111 */     return pageSql;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Select getPageSelect(Select select)
/*     */   {
/* 121 */     SelectBody selectBody = select.getSelectBody();
/* 122 */     if ((selectBody instanceof SetOperationList)) {
/* 123 */       selectBody = wrapSetOperationList((SetOperationList)selectBody);
/*     */     }
/*     */     
/* 126 */     if (((PlainSelect)selectBody).getTop() != null) {
/* 127 */       throw new RuntimeException("被分页的语句已经包含了Top，不能再通过分页插件进行分页查询!");
/*     */     }
/*     */     
/* 130 */     List<SelectItem> selectItems = getSelectItems((PlainSelect)selectBody);
/*     */     
/* 132 */     addRowNumber((PlainSelect)selectBody);
/*     */     
/* 134 */     processSelectBody(selectBody, 0);
/*     */     
/*     */ 
/* 137 */     Select newSelect = new Select();
/* 138 */     PlainSelect newSelectBody = new PlainSelect();
/*     */     
/* 140 */     Top top = new Top();
/* 141 */     top.setRowCount(Long.MAX_VALUE);
/* 142 */     newSelectBody.setTop(top);
/*     */     
/* 144 */     List<OrderByElement> orderByElements = new ArrayList();
/* 145 */     OrderByElement orderByElement = new OrderByElement();
/* 146 */     orderByElement.setExpression(PAGE_ROW_NUMBER_COLUMN);
/* 147 */     orderByElements.add(orderByElement);
/* 148 */     newSelectBody.setOrderByElements(orderByElements);
/*     */     
/* 150 */     GreaterThan greaterThan = new GreaterThan();
/* 151 */     greaterThan.setLeftExpression(PAGE_ROW_NUMBER_COLUMN);
/* 152 */     greaterThan.setRightExpression(new LongValue(Long.MIN_VALUE));
/* 153 */     newSelectBody.setWhere(greaterThan);
/*     */     
/* 155 */     newSelectBody.setSelectItems(selectItems);
/*     */     
/* 157 */     SubSelect fromItem = new SubSelect();
/* 158 */     fromItem.setSelectBody(selectBody);
/* 159 */     fromItem.setAlias(PAGE_TABLE_ALIAS);
/* 160 */     newSelectBody.setFromItem(fromItem);
/*     */     
/* 162 */     newSelect.setSelectBody(newSelectBody);
/* 163 */     if (isNotEmptyList(select.getWithItemsList())) {
/* 164 */       newSelect.setWithItemsList(select.getWithItemsList());
/*     */     }
/* 166 */     return newSelect;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SelectBody wrapSetOperationList(SetOperationList setOperationList)
/*     */   {
/* 177 */     PlainSelect plainSelect = (PlainSelect)setOperationList.getPlainSelects().get(setOperationList.getPlainSelects().size() - 1);
/*     */     
/* 179 */     PlainSelect selectBody = new PlainSelect();
/* 180 */     List<SelectItem> selectItems = getSelectItems(plainSelect);
/* 181 */     selectBody.setSelectItems(selectItems);
/*     */     
/*     */ 
/* 184 */     SubSelect fromItem = new SubSelect();
/* 185 */     fromItem.setSelectBody(setOperationList);
/* 186 */     fromItem.setAlias(new Alias("WRAP_OUTER_TABLE"));
/* 187 */     selectBody.setFromItem(fromItem);
/*     */     
/* 189 */     if (isNotEmptyList(plainSelect.getOrderByElements())) {
/* 190 */       selectBody.setOrderByElements(plainSelect.getOrderByElements());
/* 191 */       plainSelect.setOrderByElements(null);
/*     */     }
/* 193 */     return selectBody;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<SelectItem> getSelectItems(PlainSelect plainSelect)
/*     */   {
/* 204 */     List<SelectItem> selectItems = new ArrayList();
/* 205 */     for (SelectItem selectItem : plainSelect.getSelectItems())
/*     */     {
/* 207 */       if ((selectItem instanceof SelectExpressionItem)) {
/* 208 */         SelectExpressionItem selectExpressionItem = (SelectExpressionItem)selectItem;
/* 209 */         if (selectExpressionItem.getAlias() != null)
/*     */         {
/* 211 */           Column column = new Column(selectExpressionItem.getAlias().getName());
/* 212 */           SelectExpressionItem expressionItem = new SelectExpressionItem(column);
/* 213 */           selectItems.add(expressionItem);
/* 214 */         } else if ((selectExpressionItem.getExpression() instanceof Column)) {
/* 215 */           Column column = (Column)selectExpressionItem.getExpression();
/* 216 */           SelectExpressionItem item = null;
/* 217 */           if (column.getTable() != null) {
/* 218 */             Column newColumn = new Column(column.getColumnName());
/* 219 */             item = new SelectExpressionItem(newColumn);
/* 220 */             selectItems.add(item);
/*     */           } else {
/* 222 */             selectItems.add(selectItem);
/*     */           }
/*     */         } else {
/* 225 */           selectItems.add(selectItem);
/*     */         }
/* 227 */       } else if ((selectItem instanceof AllTableColumns)) {
/* 228 */         selectItems.add(new AllColumns());
/*     */       } else {
/* 230 */         selectItems.add(selectItem);
/*     */       }
/*     */     }
/* 233 */     return selectItems;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addRowNumber(PlainSelect plainSelect)
/*     */   {
/* 243 */     StringBuilder orderByBuilder = new StringBuilder();
/* 244 */     orderByBuilder.append("ROW_NUMBER() OVER (");
/* 245 */     if (isNotEmptyList(plainSelect.getOrderByElements()))
/*     */     {
/* 247 */       orderByBuilder.append(PlainSelect.orderByToString(false, plainSelect.getOrderByElements()));
/*     */     } else {
/* 249 */       throw new RuntimeException("请您在sql中包含order by语句!");
/*     */     }
/*     */     
/* 252 */     if (isNotEmptyList(plainSelect.getOrderByElements())) {
/* 253 */       plainSelect.setOrderByElements(null);
/*     */     }
/* 255 */     orderByBuilder.append(") ");
/* 256 */     orderByBuilder.append("PAGE_ROW_NUMBER");
/* 257 */     Column orderByColumn = new Column(orderByBuilder.toString());
/* 258 */     plainSelect.getSelectItems().add(0, new SelectExpressionItem(orderByColumn));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void processSelectBody(SelectBody selectBody, int level)
/*     */   {
/* 267 */     if ((selectBody instanceof PlainSelect)) {
/* 268 */       processPlainSelect((PlainSelect)selectBody, level + 1);
/* 269 */     } else if ((selectBody instanceof WithItem)) {
/* 270 */       WithItem withItem = (WithItem)selectBody;
/* 271 */       if (withItem.getSelectBody() != null) {
/* 272 */         processSelectBody(withItem.getSelectBody(), level + 1);
/*     */       }
/*     */     } else {
/* 275 */       SetOperationList operationList = (SetOperationList)selectBody;
/* 276 */       if ((operationList.getPlainSelects() != null) && (operationList.getPlainSelects().size() > 0)) {
/* 277 */         List<PlainSelect> plainSelects = operationList.getPlainSelects();
/* 278 */         for (PlainSelect plainSelect : plainSelects) {
/* 279 */           processPlainSelect(plainSelect, level + 1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void processPlainSelect(PlainSelect plainSelect, int level)
/*     */   {
/* 291 */     if ((level > 1) && 
/* 292 */       (isNotEmptyList(plainSelect.getOrderByElements())) && 
/* 293 */       (plainSelect.getTop() == null)) {
/* 294 */       plainSelect.setTop(TOP100_PERCENT);
/*     */     }
/*     */     
/*     */ 
/* 298 */     if (plainSelect.getFromItem() != null) {
/* 299 */       processFromItem(plainSelect.getFromItem(), level + 1);
/*     */     }
/* 301 */     if ((plainSelect.getJoins() != null) && (plainSelect.getJoins().size() > 0)) {
/* 302 */       List<Join> joins = plainSelect.getJoins();
/* 303 */       for (Join join : joins) {
/* 304 */         if (join.getRightItem() != null) {
/* 305 */           processFromItem(join.getRightItem(), level + 1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void processFromItem(FromItem fromItem, int level)
/*     */   {
/* 317 */     if ((fromItem instanceof SubJoin)) {
/* 318 */       SubJoin subJoin = (SubJoin)fromItem;
/* 319 */       if ((subJoin.getJoin() != null) && 
/* 320 */         (subJoin.getJoin().getRightItem() != null)) {
/* 321 */         processFromItem(subJoin.getJoin().getRightItem(), level + 1);
/*     */       }
/*     */       
/* 324 */       if (subJoin.getLeft() != null) {
/* 325 */         processFromItem(subJoin.getLeft(), level + 1);
/*     */       }
/* 327 */     } else if ((fromItem instanceof SubSelect)) {
/* 328 */       SubSelect subSelect = (SubSelect)fromItem;
/* 329 */       if (subSelect.getSelectBody() != null) {
/* 330 */         processSelectBody(subSelect.getSelectBody(), level + 1);
/*     */       }
/* 332 */     } else if (!(fromItem instanceof ValuesList))
/*     */     {
/* 334 */       if ((fromItem instanceof LateralSubSelect)) {
/* 335 */         LateralSubSelect lateralSubSelect = (LateralSubSelect)fromItem;
/* 336 */         if (lateralSubSelect.getSubSelect() != null) {
/* 337 */           SubSelect subSelect = lateralSubSelect.getSubSelect();
/* 338 */           if (subSelect.getSelectBody() != null) {
/* 339 */             processSelectBody(subSelect.getSelectBody(), level + 1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isNotEmptyList(List<?> list)
/*     */   {
/* 353 */     if ((list == null) || (list.size() == 0)) {
/* 354 */       return false;
/*     */     }
/* 356 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isNotEmptyString(String str)
/*     */   {
/* 366 */     if ((str == null) || (str.length() == 0)) {
/* 367 */       return false;
/*     */     }
/* 369 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\eif\framework\eif-framework-mybatis-pagination\1.4.2-SNAPSHOT\eif-framework-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\eif\framework\pagination\pagehelper\parser\SqlServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */