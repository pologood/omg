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
/*    */ public class BaseFrameworkCheckedException
/*    */   extends Exception
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
/*    */   public BaseFrameworkCheckedException(String msg, String code, Throwable ex, String jsonContent)
/*    */   {
/* 28 */     super(msg);
/* 29 */     this.msg = msg;
/* 30 */     this.code = code;
/* 31 */     this.innerException = ex;
/* 32 */     this.jsonContent = jsonContent;
/*    */   }
/*    */   
/*    */   public BaseFrameworkCheckedException(String msg, String code, String jsonContent)
/*    */   {
/* 37 */     super(msg);
/* 38 */     this.msg = msg;
/* 39 */     this.code = code;
/* 40 */     this.jsonContent = jsonContent;
/*    */   }
/*    */   
/*    */   public BaseFrameworkCheckedException(String msg, String code) {
/* 44 */     super(msg);
/* 45 */     this.msg = msg;
/* 46 */     this.code = code;
/*    */   }
/*    */   
/*    */   public String getMsg() {
/* 50 */     return this.msg;
/*    */   }
/*    */   
/*    */   public String getCode() {
/* 54 */     return this.code;
/*    */   }
/*    */   
/*    */   public void setCode(String code) {
/* 58 */     this.code = code;
/*    */   }
/*    */   
/*    */   public Throwable getInnerException() {
/* 62 */     return this.innerException;
/*    */   }
/*    */   
/*    */   public void setInnerException(Throwable innerException) {
/* 66 */     this.innerException = innerException;
/*    */   }
/*    */   
/*    */   public String getJsonContent() {
/* 70 */     return this.jsonContent;
/*    */   }
/*    */   
/*    */   public void setJsonContent(String jsonContent) {
/* 74 */     this.jsonContent = jsonContent;
/*    */   }
/*    */   
/*    */   public boolean isMonitored() {
/* 78 */     return this.monitored;
/*    */   }
/*    */   
/*    */   public void setMonitored(boolean monitored) {
/* 82 */     this.monitored = monitored;
/*    */   }
/*    */   
/*    */   public boolean isLoggable() {
/* 86 */     return this.loggable;
/*    */   }
/*    */   
/*    */   public void setLoggable(boolean loggable) {
/* 90 */     this.loggable = loggable;
/*    */   }
/*    */ }

