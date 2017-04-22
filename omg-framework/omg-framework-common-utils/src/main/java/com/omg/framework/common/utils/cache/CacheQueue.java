/*    */ package com.omg.framework.common.utils.cache;
/*    */ 
/*    */ import com.google.common.cache.CacheBuilder;
/*    */ 
/*    */ public class CacheQueue<T>
/*    */ {
/*  7 */   private com.google.common.cache.Cache<T, Object> cache = null;
/*    */   
/*    */   public CacheQueue(int maxSize) {
/* 10 */     this.cache = CacheBuilder.newBuilder().initialCapacity(maxSize).maximumSize(maxSize).build();
/*    */   }
/*    */   
/*    */   public boolean isExist(T key) {
/* 14 */     return this.cache.getIfPresent(key) != null;
/*    */   }
/*    */   
/*    */   public void put(T key) {
/* 18 */     this.cache.put(key, new Object());
/*    */   }
/*    */ }

