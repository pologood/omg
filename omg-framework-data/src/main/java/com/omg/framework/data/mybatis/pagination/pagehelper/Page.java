/*     */ package com.omg.framework.data.mybatis.pagination.pagehelper;
/*     */
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;

/*     */ public class Page<E>
/*     */   extends ArrayList<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private int pageNum;
/*     */   private int pageSize;
/*     */   private int startRow;
/*     */   private int endRow;
/*     */   private long total;
/*     */   private int pages;
/*     */   private boolean count;
/*     */   private Boolean countSignal;
/*     */   private String orderBy;
/*     */   private boolean orderByOnly;
/*     */   private Boolean reasonable;
/*     */   private Boolean pageSizeZero;
/*     */
/*     */   public Page() {}
/*     */
/*     */   public Page(int pageNum, int pageSize)
/*     */   {
/*  93 */     this(pageNum, pageSize, true, null);
/*     */   }
/*     */
/*     */   public Page(int pageNum, int pageSize, boolean count) {
/*  97 */     this(pageNum, pageSize, count, null);
/*     */   }
/*     */
/*     */   private Page(int pageNum, int pageSize, boolean count, Boolean reasonable) {
/* 101 */     super(0);
/* 102 */     if ((pageNum == 1) && (pageSize == Integer.MAX_VALUE)) {
/* 103 */       this.pageSizeZero = Boolean.valueOf(true);
/* 104 */       pageSize = 0;
/*     */     }
/* 106 */     this.pageNum = pageNum;
/* 107 */     this.pageSize = pageSize;
/* 108 */     this.count = count;
/* 109 */     calculateStartAndEndRow();
/* 110 */     setReasonable(reasonable);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Page(int[] rowBounds, boolean count)
/*     */   {
/* 119 */     super(0);
/* 120 */     if ((rowBounds[0] == 0) && (rowBounds[1] == Integer.MAX_VALUE)) {
/* 121 */       this.pageSizeZero = Boolean.valueOf(true);
/* 122 */       this.pageSize = 0;
/*     */     } else {
/* 124 */       this.pageSize = rowBounds[1];
/* 125 */       this.pageNum = (rowBounds[1] != 0 ? (int)Math.ceil((rowBounds[0] + rowBounds[1]) / rowBounds[1]) : 0);
/*     */     }
/* 127 */     this.startRow = rowBounds[0];
/* 128 */     this.count = count;
/* 129 */     this.endRow = (this.startRow + rowBounds[1]);
/*     */   }
/*     */
/*     */   public List<E> getResult() {
/* 133 */     return this;
/*     */   }
/*     */
/*     */   public int getPages() {
/* 137 */     return this.pages;
/*     */   }
/*     */
/*     */   public void setPages(int pages) {
/* 141 */     this.pages = pages;
/*     */   }
/*     */
/*     */   public int getEndRow() {
/* 145 */     return this.endRow;
/*     */   }
/*     */
/*     */   public void setEndRow(int endRow) {
/* 149 */     this.endRow = endRow;
/*     */   }
/*     */
/*     */   public int getPageNum() {
/* 153 */     return this.pageNum;
/*     */   }
/*     */
/*     */   public void setPageNum(int pageNum)
/*     */   {
/* 158 */     this.pageNum = ((this.reasonable != null) && (this.reasonable.booleanValue()) && (pageNum <= 0) ? 1 : pageNum);
/*     */   }
/*     */
/*     */   public int getPageSize() {
/* 162 */     return this.pageSize;
/*     */   }
/*     */
/*     */   public void setPageSize(int pageSize) {
/* 166 */     this.pageSize = pageSize;
/*     */   }
/*     */
/*     */   public int getStartRow() {
/* 170 */     return this.startRow;
/*     */   }
/*     */
/*     */   public void setStartRow(int startRow) {
/* 174 */     this.startRow = startRow;
/*     */   }
/*     */
/*     */   public long getTotal() {
/* 178 */     return this.total;
/*     */   }
/*     */
/*     */   public void setTotal(long total) {
/* 182 */     this.total = total;
/* 183 */     if (total == -1L) {
/* 184 */       this.pages = 1;
/* 185 */       return;
/*     */     }
/* 187 */     if (this.pageSize > 0) {
/* 188 */       this.pages = ((int)(total / this.pageSize + (total % this.pageSize == 0L ? 0 : 1)));
/*     */     } else {
/* 190 */       this.pages = 0;
/*     */     }
/*     */
/* 193 */     if ((this.reasonable != null) && (this.reasonable.booleanValue()) && (this.pageNum > this.pages)) {
/* 194 */       this.pageNum = this.pages;
/* 195 */       calculateStartAndEndRow();
/*     */     }
/*     */   }
/*     */
/*     */   public Boolean getReasonable() {
/* 200 */     return this.reasonable;
/*     */   }
/*     */
/*     */   public void setReasonable(Boolean reasonable) {
/* 204 */     if (reasonable == null) {
/* 205 */       return;
/*     */     }
/* 207 */     this.reasonable = reasonable;
/*     */
/* 209 */     if ((this.reasonable.booleanValue()) && (this.pageNum <= 0)) {
/* 210 */       this.pageNum = 1;
/* 211 */       calculateStartAndEndRow();
/*     */     }
/*     */   }
/*     */
/*     */   public Boolean getPageSizeZero() {
/* 216 */     return this.pageSizeZero;
/*     */   }
/*     */
/*     */   public void setPageSizeZero(Boolean pageSizeZero) {
/* 220 */     if (pageSizeZero != null) {
/* 221 */       this.pageSizeZero = pageSizeZero;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   private void calculateStartAndEndRow()
/*     */   {
/* 229 */     this.startRow = (this.pageNum > 0 ? (this.pageNum - 1) * this.pageSize : 0);
/* 230 */     this.endRow = (this.startRow + this.pageSize * (this.pageNum > 0 ? 1 : 0));
/*     */   }
/*     */
/*     */   public boolean isCount() {
/* 234 */     return this.count;
/*     */   }
/*     */
/*     */   public void setCount(boolean count) {
/* 238 */     this.count = count;
/*     */   }
/*     */
/*     */   public String getOrderBy() {
/* 242 */     return this.orderBy;
/*     */   }
/*     */
/*     */   public void setOrderBy(String orderBy) {
/* 246 */     this.orderBy = orderBy;
/*     */   }
/*     */
/*     */   public boolean isOrderByOnly() {
/* 250 */     return this.orderByOnly;
/*     */   }
/*     */
/*     */   public void setOrderByOnly(boolean orderByOnly) {
/* 254 */     this.orderByOnly = orderByOnly;
/*     */   }
/*     */
/*     */   public Boolean getCountSignal() {
/* 258 */     return this.countSignal;
/*     */   }
/*     */
/*     */   public void setCountSignal(Boolean countSignal) {
/* 262 */     this.countSignal = countSignal;
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
/*     */   public Page<E> pageNum(int pageNum)
/*     */   {
/* 275 */     this.pageNum = ((this.reasonable != null) && (this.reasonable.booleanValue()) && (pageNum <= 0) ? 1 : pageNum);
/* 276 */     return this;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Page<E> pageSize(int pageSize)
/*     */   {
/* 286 */     this.pageSize = pageSize;
/* 287 */     calculateStartAndEndRow();
/* 288 */     return this;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Page<E> count(Boolean count)
/*     */   {
/* 298 */     this.count = count.booleanValue();
/* 299 */     return this;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Page<E> reasonable(Boolean reasonable)
/*     */   {
/* 309 */     setReasonable(reasonable);
/* 310 */     return this;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Page<E> pageSizeZero(Boolean pageSizeZero)
/*     */   {
/* 320 */     setPageSizeZero(pageSizeZero);
/* 321 */     return this;
/*     */   }
/*     */
/*     */   public String toString()
/*     */   {
/* 326 */     return "Page{count=" + this.count + ", pageNum=" + this.pageNum + ", pageSize=" + this.pageSize + ", startRow=" + this.startRow + ", endRow=" + this.endRow + ", total=" + this.total + ", pages=" + this.pages + ", countSignal=" + this.countSignal + ", orderBy='" + this.orderBy + '\'' + ", orderByOnly=" + this.orderByOnly + ", reasonable=" + this.reasonable + ", pageSizeZero=" + this.pageSizeZero + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\eif\framework\eif-framework-mybatis-pagination\1.4.2-SNAPSHOT\eif-framework-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\eif\framework\pagination\pagehelper\Page.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */