/*    */ package com.omg.framework.common.utils.cache;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractCacheFactory
/*    */   implements CacheFactory
/*    */ {
/* 11 */   private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap();
/*    */   
/*    */ 
/*    */ 
/*    */   private static final int DEFAULT_MAX_SIZE = 1000;
/*    */   
/*    */ 
/*    */ 
/*    */   public Cache getCache(String key)
/*    */   {
/* 21 */     Cache cache = (Cache)this.caches.get(key);
/* 22 */     if (cache == null) {
/* 23 */       this.caches.put(key, createCache(1000));
/* 24 */       cache = (Cache)this.caches.get(key);
/*    */     }
/* 26 */     return cache;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Cache getCache(String key, int maxSize)
/*    */   {
/* 36 */     Cache cache = (Cache)this.caches.get(key);
/* 37 */     if (cache == null) {
/* 38 */       if (maxSize < 1) {
/* 39 */         maxSize = 1000;
/*    */       }
/* 41 */       this.caches.put(key, createCache(maxSize));
/* 42 */       cache = (Cache)this.caches.get(key);
/*    */     }
/* 44 */     return cache;
/*    */   }
/*    */   
/*    */   protected abstract Cache createCache(int paramInt);
/*    */ }
