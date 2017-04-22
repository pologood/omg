/*    */ package com.omg.framework.common.utils;
/*    */ 
/*    */ public abstract class Assert {
/*    */   public static void hasText(String text) {
/*  5 */     hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
/*    */   }
/*    */   
/*    */   public static void hasText(String text, String message) {
/*  9 */     if (!StringUtils.hasText(text)) {
/* 10 */       throw new IllegalArgumentException(message);
/*    */     }
/*    */   }
/*    */ }
