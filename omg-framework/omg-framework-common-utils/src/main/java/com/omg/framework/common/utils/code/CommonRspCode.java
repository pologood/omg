/*    */ package com.omg.framework.common.utils.code;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CommonRspCode
/*    */   implements RspCode
/*    */ {
/*  9 */   SUCCESS("成功", "1000"), 
/* 10 */   REPEAT("重复交易", "1001"), 
/* 11 */   OFFLINE("系统离线状态", "1002"), 
/* 12 */   ERROR("异常", "1003"), 
/* 13 */   PARAM_ERROR("参数不完整", "1004"), 
/* 14 */   FAILD("失败", "1024"), 
/* 15 */   METHOD_NOT_SUPPORTED("方法不支持", "1025"), 
/*    */   
/*    */ 
/* 18 */   SIGNATURE_FAIL("验签失败", "1010"), 
/* 19 */   FORMAT_FAIL("请求数据格式非法", "1011"), 
/* 20 */   VERIFY_FAIL("请求数据校验失败", "1012"), 
/* 21 */   DATA_NOT_EXIST("数据不存在", "1013"), 
/* 22 */   DATA_REPEAT("数据重复", "1014"), 
/* 23 */   ROLE_ERROR("权限不足", "1015"), 
/*    */   
/*    */ 
/* 26 */   DB_ERROR("数据库异常", "1020"), 
/* 27 */   SYS_TIMEOUT("系统超时", "1021"), 
/* 28 */   SYS_ERROR("系统错误", "1022"), 
/* 29 */   REDIS_ERROR("缓存异常", "1023"), 
/* 30 */   RPC_ERROR("远程调用异常", "1024"), 
/* 31 */   CIR_BRE("熔断控制", "1025"), 
/* 32 */   MAX_CON("并发控制", "1026"), 
/* 33 */   CB_ERROR("熔断错误", "1027"), 
/*    */   
/*    */ 
/* 36 */   TIMEOUT("连接超时失效", "1030"), 
/* 37 */   ILLEGAL_IP("请求IP非法", "1031"), 
/*    */   
/* 39 */   ACQUIRE_REDIS_LOCK_ERROR("操作过于频繁", "1040"), 
/* 40 */   REDIS_LOCK_OP_ERROR("操作过于频繁", "1041");
/*    */   
/*    */   private String code;
/*    */   private String message;
/*    */   
/*    */   private CommonRspCode(String message, String code)
/*    */   {
/* 47 */     this.code = code;
/* 48 */     this.message = message;
/*    */   }
/*    */   
/*    */   public String getCode() {
/* 52 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 56 */     return this.message;
/*    */   }
/*    */   
/*    */   public static CommonRspCode getEnum(String value) {
/* 60 */     CommonRspCode[] crc = values();
/* 61 */     for (int i = 0; i < crc.length; i++) {
/* 62 */       if (crc[i].getCode().equals(value)) {
/* 63 */         return crc[i];
/*    */       }
/*    */     }
/* 66 */     return null;
/*    */   }
/*    */ }

