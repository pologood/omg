/*    */ package com.omg.framework.common.utils;
/*    */ 
/*    */ public abstract class StringUtils {
/*    */   public static boolean hasText(String str) {
/*  5 */     return hasText(str);
/*    */   }
/*    */   
/*    */   public static boolean hasText(CharSequence str) {
/*  9 */     if (!hasLength(str)) {
/* 10 */       return false;
/*    */     }
/* 12 */     int strLen = str.length();
/* 13 */     for (int i = 0; i < strLen; i++) {
/* 14 */       if (!Character.isWhitespace(str.charAt(i))) {
/* 15 */         return true;
/*    */       }
/*    */     }
/* 18 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean hasLength(CharSequence str) {
/* 22 */     return (str != null) && (str.length() > 0);
/*    */   }
/*    */   
/*    */   public static String replaceLast(String text, String regex, String replacement) {
/* 26 */     return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
/*    */   }
/*    */ }
