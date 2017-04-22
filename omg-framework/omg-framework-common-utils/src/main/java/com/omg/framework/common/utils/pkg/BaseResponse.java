/*    */ package com.omg.framework.common.utils.pkg;
/*    */ 
/*    */ import com.omg.framework.common.utils.code.CommonRspCode;
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.lang.builder.ToStringBuilder;
/*    */ import org.apache.commons.lang.builder.ToStringStyle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BaseResponse
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String respCode;
/*    */   private String msg;
/*    */   
/*    */   public String getMsg()
/*    */   {
/* 23 */     return this.msg;
/*    */   }
/*    */   
/*    */   public void setMsg(String msg) {
/* 27 */     this.msg = msg;
/*    */   }
/*    */   
/*    */   public String getRespCode() {
/* 31 */     return this.respCode;
/*    */   }
/*    */   
/*    */   public void setRespCode(String respCode) {
/* 35 */     this.respCode = respCode;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/*    */     try {
/* 41 */       return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
/*    */     } catch (Exception e) {
/* 43 */       e.printStackTrace();
/*    */     }
/* 45 */     return super.toString();
/*    */   }
/*    */   
/*    */   public boolean isSuccess() {
/* 49 */     return this.respCode.equals(CommonRspCode.SUCCESS.getCode());
/*    */   }
/*    */ }

