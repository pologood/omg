/*    */ package com.omg.framework.common.utils.pkg;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.lang.builder.ToStringBuilder;
/*    */ import org.apache.commons.lang.builder.ToStringStyle;
/*    */ 

/*    */ public class BaseRequest
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public String toString()
/*    */   {
/*    */     try
/*    */     {
/* 24 */       return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
/*    */     } catch (Exception e) {
/* 26 */       e.printStackTrace();
/*    */     }
/* 28 */     return super.toString();
/*    */   }
/*    */ }
