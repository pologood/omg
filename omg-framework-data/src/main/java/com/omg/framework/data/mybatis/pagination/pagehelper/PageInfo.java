/*     */ package com.omg.framework.data.mybatis.pagination.pagehelper;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
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
/*     */ public class PageInfo<T>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private int pageNum;
/*     */   private int pageSize;
/*     */   private int size;
/*     */   private String orderBy;
/*     */   private int startRow;
/*     */   private int endRow;
/*     */   private long total;
/*     */   private int pages;
/*     */   private List<T> list;
/*     */   private int firstPage;
/*     */   private int prePage;
/*     */   private int nextPage;
/*     */   private int lastPage;
/*  77 */   private boolean isFirstPage = false;
/*     */   
/*  79 */   private boolean isLastPage = false;
/*     */   
/*  81 */   private boolean hasPreviousPage = false;
/*     */   
/*  83 */   private boolean hasNextPage = false;
/*     */   
/*     */ 
/*     */   private int navigatePages;
/*     */   
/*     */ 
/*     */   private int[] navigatepageNums;
/*     */   
/*     */ 
/*     */ 
/*     */   public PageInfo() {}
/*     */   
/*     */ 
/*     */   public PageInfo(List<T> list)
/*     */   {
/*  98 */     this(list, 8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PageInfo(List<T> list, int navigatePages)
/*     */   {
/* 108 */     if ((list instanceof Page)) {
/* 109 */       Page page = (Page)list;
/* 110 */       this.pageNum = page.getPageNum();
/* 111 */       this.pageSize = page.getPageSize();
/* 112 */       this.orderBy = page.getOrderBy();
/*     */       
/* 114 */       this.pages = page.getPages();
/* 115 */       this.list = page;
/* 116 */       this.size = page.size();
/* 117 */       this.total = page.getTotal();
/*     */       
/* 119 */       if (this.size == 0) {
/* 120 */         this.startRow = 0;
/* 121 */         this.endRow = 0;
/*     */       } else {
/* 123 */         this.startRow = (page.getStartRow() + 1);
/*     */         
/* 125 */         this.endRow = (this.startRow - 1 + this.size);
/*     */       }
/* 127 */     } else if ((list instanceof Collection)) {
/* 128 */       this.pageNum = 1;
/* 129 */       this.pageSize = list.size();
/*     */       
/* 131 */       this.pages = 1;
/* 132 */       this.list = list;
/* 133 */       this.size = list.size();
/* 134 */       this.total = list.size();
/* 135 */       this.startRow = 0;
/* 136 */       this.endRow = (list.size() > 0 ? list.size() - 1 : 0);
/*     */     }
/* 138 */     if ((list instanceof Collection)) {
/* 139 */       this.navigatePages = navigatePages;
/*     */       
/* 141 */       calcNavigatepageNums();
/*     */       
/* 143 */       calcPage();
/*     */       
/* 145 */       judgePageBoudary();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void calcNavigatepageNums()
/*     */   {
/* 154 */     if (this.pages <= this.navigatePages) {
/* 155 */       this.navigatepageNums = new int[this.pages];
/* 156 */       for (int i = 0; i < this.pages; i++) {
/* 157 */         this.navigatepageNums[i] = (i + 1);
/*     */       }
/*     */     } else {
/* 160 */       this.navigatepageNums = new int[this.navigatePages];
/* 161 */       int startNum = this.pageNum - this.navigatePages / 2;
/* 162 */       int endNum = this.pageNum + this.navigatePages / 2;
/*     */       
/* 164 */       if (startNum < 1) {
/* 165 */         startNum = 1;
/*     */         
/* 167 */         for (int i = 0; i < this.navigatePages; i++) {
/* 168 */           this.navigatepageNums[i] = (startNum++);
/*     */         }
/* 170 */       } else if (endNum > this.pages) {
/* 171 */         endNum = this.pages;
/*     */         
/* 173 */         for (int i = this.navigatePages - 1; i >= 0; i--) {
/* 174 */           this.navigatepageNums[i] = (endNum--);
/*     */         }
/*     */       }
/*     */       else {
/* 178 */         for (int i = 0; i < this.navigatePages; i++) {
/* 179 */           this.navigatepageNums[i] = (startNum++);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void calcPage()
/*     */   {
/* 189 */     if ((this.navigatepageNums != null) && (this.navigatepageNums.length > 0)) {
/* 190 */       this.firstPage = this.navigatepageNums[0];
/* 191 */       this.lastPage = this.navigatepageNums[(this.navigatepageNums.length - 1)];
/* 192 */       if (this.pageNum > 1) {
/* 193 */         this.prePage = (this.pageNum - 1);
/*     */       }
/* 195 */       if (this.pageNum < this.pages) {
/* 196 */         this.nextPage = (this.pageNum + 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void judgePageBoudary()
/*     */   {
/* 205 */     this.isFirstPage = (this.pageNum == 1);
/* 206 */     this.isLastPage = (this.pageNum == this.pages);
/* 207 */     this.hasPreviousPage = (this.pageNum > 1);
/* 208 */     this.hasNextPage = (this.pageNum < this.pages);
/*     */   }
/*     */   
/*     */   public int getPageNum() {
/* 212 */     return this.pageNum;
/*     */   }
/*     */   
/*     */   public void setPageNum(int pageNum) {
/* 216 */     this.pageNum = pageNum;
/*     */   }
/*     */   
/*     */   public int getPageSize() {
/* 220 */     return this.pageSize;
/*     */   }
/*     */   
/*     */   public void setPageSize(int pageSize) {
/* 224 */     this.pageSize = pageSize;
/*     */   }
/*     */   
/*     */   public int getSize() {
/* 228 */     return this.size;
/*     */   }
/*     */   
/*     */   public void setSize(int size) {
/* 232 */     this.size = size;
/*     */   }
/*     */   
/*     */   public String getOrderBy() {
/* 236 */     return this.orderBy;
/*     */   }
/*     */   
/*     */   public void setOrderBy(String orderBy) {
/* 240 */     this.orderBy = orderBy;
/*     */   }
/*     */   
/*     */   public int getStartRow() {
/* 244 */     return this.startRow;
/*     */   }
/*     */   
/*     */   public void setStartRow(int startRow) {
/* 248 */     this.startRow = startRow;
/*     */   }
/*     */   
/*     */   public int getEndRow() {
/* 252 */     return this.endRow;
/*     */   }
/*     */   
/*     */   public void setEndRow(int endRow) {
/* 256 */     this.endRow = endRow;
/*     */   }
/*     */   
/*     */   public long getTotal() {
/* 260 */     return this.total;
/*     */   }
/*     */   
/*     */   public void setTotal(long total) {
/* 264 */     this.total = total;
/*     */   }
/*     */   
/*     */   public int getPages() {
/* 268 */     return this.pages;
/*     */   }
/*     */   
/*     */   public void setPages(int pages) {
/* 272 */     this.pages = pages;
/*     */   }
/*     */   
/*     */   public List<T> getList() {
/* 276 */     return this.list;
/*     */   }
/*     */   
/*     */   public void setList(List<T> list) {
/* 280 */     this.list = list;
/*     */   }
/*     */   
/*     */   public int getFirstPage() {
/* 284 */     return this.firstPage;
/*     */   }
/*     */   
/*     */   public void setFirstPage(int firstPage) {
/* 288 */     this.firstPage = firstPage;
/*     */   }
/*     */   
/*     */   public int getPrePage() {
/* 292 */     return this.prePage;
/*     */   }
/*     */   
/*     */   public void setPrePage(int prePage) {
/* 296 */     this.prePage = prePage;
/*     */   }
/*     */   
/*     */   public int getNextPage() {
/* 300 */     return this.nextPage;
/*     */   }
/*     */   
/*     */   public void setNextPage(int nextPage) {
/* 304 */     this.nextPage = nextPage;
/*     */   }
/*     */   
/*     */   public int getLastPage() {
/* 308 */     return this.lastPage;
/*     */   }
/*     */   
/*     */   public void setLastPage(int lastPage) {
/* 312 */     this.lastPage = lastPage;
/*     */   }
/*     */   
/*     */   public boolean isIsFirstPage() {
/* 316 */     return this.isFirstPage;
/*     */   }
/*     */   
/*     */   public void setIsFirstPage(boolean isFirstPage) {
/* 320 */     this.isFirstPage = isFirstPage;
/*     */   }
/*     */   
/*     */   public boolean isIsLastPage() {
/* 324 */     return this.isLastPage;
/*     */   }
/*     */   
/*     */   public void setIsLastPage(boolean isLastPage) {
/* 328 */     this.isLastPage = isLastPage;
/*     */   }
/*     */   
/*     */   public boolean isHasPreviousPage() {
/* 332 */     return this.hasPreviousPage;
/*     */   }
/*     */   
/*     */   public void setHasPreviousPage(boolean hasPreviousPage) {
/* 336 */     this.hasPreviousPage = hasPreviousPage;
/*     */   }
/*     */   
/*     */   public boolean isHasNextPage() {
/* 340 */     return this.hasNextPage;
/*     */   }
/*     */   
/*     */   public void setHasNextPage(boolean hasNextPage) {
/* 344 */     this.hasNextPage = hasNextPage;
/*     */   }
/*     */   
/*     */   public int getNavigatePages() {
/* 348 */     return this.navigatePages;
/*     */   }
/*     */   
/*     */   public void setNavigatePages(int navigatePages) {
/* 352 */     this.navigatePages = navigatePages;
/*     */   }
/*     */   
/*     */   public int[] getNavigatepageNums() {
/* 356 */     return this.navigatepageNums;
/*     */   }
/*     */   
/*     */   public void setNavigatepageNums(int[] navigatepageNums) {
/* 360 */     this.navigatepageNums = navigatepageNums;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 365 */     StringBuffer sb = new StringBuffer("PageInfo{");
/* 366 */     sb.append("pageNum=").append(this.pageNum);
/* 367 */     sb.append(", pageSize=").append(this.pageSize);
/* 368 */     sb.append(", size=").append(this.size);
/* 369 */     sb.append(", startRow=").append(this.startRow);
/* 370 */     sb.append(", endRow=").append(this.endRow);
/* 371 */     sb.append(", total=").append(this.total);
/* 372 */     sb.append(", pages=").append(this.pages);
/* 373 */     sb.append(", list=").append(this.list);
/* 374 */     sb.append(", firstPage=").append(this.firstPage);
/* 375 */     sb.append(", prePage=").append(this.prePage);
/* 376 */     sb.append(", nextPage=").append(this.nextPage);
/* 377 */     sb.append(", lastPage=").append(this.lastPage);
/* 378 */     sb.append(", isFirstPage=").append(this.isFirstPage);
/* 379 */     sb.append(", isLastPage=").append(this.isLastPage);
/* 380 */     sb.append(", hasPreviousPage=").append(this.hasPreviousPage);
/* 381 */     sb.append(", hasNextPage=").append(this.hasNextPage);
/* 382 */     sb.append(", navigatePages=").append(this.navigatePages);
/* 383 */     sb.append(", navigatepageNums=");
/* 384 */     if (this.navigatepageNums == null) { sb.append("null");
/*     */     } else {
/* 386 */       sb.append('[');
/* 387 */       for (int i = 0; i < this.navigatepageNums.length; i++)
/* 388 */         sb.append(i == 0 ? "" : ", ").append(this.navigatepageNums[i]);
/* 389 */       sb.append(']');
/*     */     }
/* 391 */     sb.append('}');
/* 392 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\eif\framework\eif-framework-mybatis-pagination\1.4.2-SNAPSHOT\eif-framework-mybatis-pagination-1.4.2-20161102.103954-2.jar!\com\eif\framework\pagination\pagehelper\PageInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */