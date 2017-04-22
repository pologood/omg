/*    */ package com.omg.framework.common.utils.mobile;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.lang.builder.ReflectionToStringBuilder;
/*    */ 
/*    */ public abstract class FrontRequest
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -7250473166523181107L;
/*    */   private String clientId;
/*    */   private String appId;
/*    */   
/*    */   public String getClientId()
/*    */   {
/* 15 */     return this.clientId;
/*    */   }
/*    */   
/*    */   public void setClientId(String clientId) {
/* 19 */     this.clientId = clientId;
/*    */   }
/*    */   
/*    */   public String getAppId() {
/* 23 */     return this.appId;
/*    */   }
/*    */   
/*    */   public void setAppId(String appId) {
/* 27 */     this.appId = appId;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/*    */     try {
/* 33 */       return ReflectionToStringBuilder.toString(this);
/*    */     } catch (Exception e) {
/* 35 */       e.printStackTrace();
/*    */     }
/* 37 */     return super.toString();
/*    */   }
/*    */ }
