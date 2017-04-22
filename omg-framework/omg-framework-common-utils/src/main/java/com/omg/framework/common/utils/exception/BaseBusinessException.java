/*    */ package com.omg.framework.common.utils.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BaseBusinessException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */   private String msg;
/*    */   
/*    */ 
/*    */   private String code;
/*    */   
/*    */ 
/*    */   private Throwable innerException;
/*    */   
/*    */   private String jsonContent;
/*    */   
/* 21 */   protected boolean monitored = false;
/* 22 */   protected boolean loggable = true;
/*    */   
/*    */   public BaseBusinessException(String msg, String code, Throwable ex, String jsonContent) {
/* 25 */     super(msg);
/* 26 */     this.msg = msg;
/* 27 */     this.code = code;
/* 28 */     this.innerException = ex;
/* 29 */     this.jsonContent = jsonContent;
/*    */   }
/*    */   
/*    */   public BaseBusinessException(String msg, String code, String jsonContent) {
/* 33 */     super(msg);
/* 34 */     this.msg = msg;
/* 35 */     this.code = code;
/* 36 */     this.jsonContent = jsonContent;
/*    */   }
/*    */   
/*    */   public BaseBusinessException(String msg, String code) {
/* 40 */     super(msg);
/* 41 */     this.msg = msg;
/* 42 */     this.code = code;
/*    */   }
/*    */   
/*    */   public String getMsg() {
/* 46 */     return this.msg;
/*    */   }
/*    */   
/*    */   public String getCode() {
/* 50 */     return this.code;
/*    */   }
/*    */   
/*    */   public void setCode(String code) {
/* 54 */     this.code = code;
/*    */   }
/*    */   
/*    */   public Throwable getInnerException() {
/* 58 */     return this.innerException;
/*    */   }
/*    */   
/*    */   public void setInnerException(Throwable innerException) {
/* 62 */     this.innerException = innerException;
/*    */   }
/*    */   
/*    */   public String getJsonContent() {
/* 66 */     return this.jsonContent;
/*    */   }
/*    */   
/*    */   public void setJsonContent(String jsonContent) {
/* 70 */     this.jsonContent = jsonContent;
/*    */   }
/*    */   
/*    */   public boolean isMonitored() {
/* 74 */     return this.monitored;
/*    */   }
/*    */   
/*    */   public void setMonitored(boolean monitored) {
/* 78 */     this.monitored = monitored;
/*    */   }
/*    */   
/*    */   public boolean isLoggable() {
/* 82 */     return this.loggable;
/*    */   }
/*    */   
/* 85 */   public void setLoggable(boolean loggable) { this.loggable = loggable; }
/*    */ }
