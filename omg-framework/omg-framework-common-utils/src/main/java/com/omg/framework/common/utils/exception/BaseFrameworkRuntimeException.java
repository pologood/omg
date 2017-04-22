/*    */ package com.omg.framework.common.utils.exception;
/*    */ 
/*    */ import com.omg.framework.common.utils.exception.baseface.BaseFrameworkException;
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
/*    */ public class BaseFrameworkRuntimeException
/*    */   extends RuntimeException
/*    */   implements BaseFrameworkException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String msg;
/*    */   private String code;
/*    */   private Throwable innerException;
/*    */   private String jsonContent;
/* 23 */   protected boolean monitored = false;
/* 24 */   protected boolean loggable = true;
/*    */   
/*    */   public BaseFrameworkRuntimeException(String msg, String code, Throwable ex, String jsonContent) {
/* 27 */     super(msg);
/* 28 */     this.msg = msg;
/* 29 */     this.code = code;
/* 30 */     this.innerException = ex;
/* 31 */     this.jsonContent = jsonContent;
/*    */   }
/*    */   
/*    */   public BaseFrameworkRuntimeException(String msg, String code, String jsonContent) {
/* 35 */     super(msg);
/* 36 */     this.msg = msg;
/* 37 */     this.code = code;
/* 38 */     this.jsonContent = jsonContent;
/*    */   }
/*    */   
/*    */   public BaseFrameworkRuntimeException(String msg, String code) {
/* 42 */     super(msg);
/* 43 */     this.msg = msg;
/* 44 */     this.code = code;
/*    */   }
/*    */   
/*    */   public String getMsg() {
/* 48 */     return this.msg;
/*    */   }
/*    */   
/*    */   public String getCode() {
/* 52 */     return this.code;
/*    */   }
/*    */   
/*    */   public void setCode(String code) {
/* 56 */     this.code = code;
/*    */   }
/*    */   
/*    */   public Throwable getInnerException() {
/* 60 */     return this.innerException;
/*    */   }
/*    */   
/*    */   public void setInnerException(Throwable innerException) {
/* 64 */     this.innerException = innerException;
/*    */   }
/*    */   
/*    */   public String getJsonContent() {
/* 68 */     return this.jsonContent;
/*    */   }
/*    */   
/*    */   public void setJsonContent(String jsonContent) {
/* 72 */     this.jsonContent = jsonContent;
/*    */   }
/*    */   
/*    */   public boolean isMonitored() {
/* 76 */     return this.monitored;
/*    */   }
/*    */   
/*    */   public void setMonitored(boolean monitored) {
/* 80 */     this.monitored = monitored;
/*    */   }
/*    */   
/*    */   public boolean isLoggable() {
/* 84 */     return this.loggable;
/*    */   }
/*    */   
/* 87 */   public void setLoggable(boolean loggable) { this.loggable = loggable; }
/*    */ }
