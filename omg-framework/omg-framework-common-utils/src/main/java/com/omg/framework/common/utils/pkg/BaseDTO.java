/*    */ package com.omg.framework.common.utils.pkg;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.lang.builder.ToStringBuilder;
/*    */ import org.apache.commons.lang.builder.ToStringStyle;
/*    */ 
/*    */ public abstract class BaseDTO implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 752625707505718769L;
/*    */   
/*    */   public String toString()
/*    */   {
/*    */     try
/*    */     {
/* 15 */       return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
/*    */     } catch (Exception e) {
/* 17 */       e.printStackTrace();
/*    */     }
/* 19 */     return super.toString();
/*    */   }
/*    */ }
