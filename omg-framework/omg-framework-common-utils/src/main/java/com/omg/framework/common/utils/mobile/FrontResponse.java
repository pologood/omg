/*    */ package com.omg.framework.common.utils.mobile;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.lang.builder.ReflectionToStringBuilder;
/*    */ 
/*    */ public abstract class FrontResponse
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -6080858828270195838L;
/*    */   private String resultCode;
/*    */   private String resultMsg;
/*    */   private String traceNo;
/*    */   
/*    */   public String getResultCode()
/*    */   {
/* 16 */     return this.resultCode;
/*    */   }
/*    */   
/*    */   public void setResultCode(String resultCode) {
/* 20 */     this.resultCode = resultCode;
/*    */   }
/*    */   
/*    */   public String getResultMsg() {
/* 24 */     return this.resultMsg;
/*    */   }
/*    */   
/*    */   public void setResultMsg(String resultMsg) {
/* 28 */     this.resultMsg = resultMsg;
/*    */   }
/*    */   
/*    */   public String getTraceNo() {
/* 32 */     return this.traceNo;
/*    */   }
/*    */   
/*    */   public void setTraceNo(String traceNo) {
/* 36 */     this.traceNo = traceNo;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/*    */     try {
/* 42 */       return ReflectionToStringBuilder.toString(this);
/*    */     } catch (Exception e) {
/* 44 */       e.printStackTrace();
/*    */     }
/* 46 */     return super.toString();
/*    */   }
/*    */ }
