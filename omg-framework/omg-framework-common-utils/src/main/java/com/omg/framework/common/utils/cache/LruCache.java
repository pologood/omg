 package com.omg.framework.common.utils.cache;
 
 import java.util.LinkedHashMap;
 import java.util.Map;
 import java.util.Map.Entry;

 public class LruCache implements Cache {
   private final Map<Object, Object> store;

   public LruCache(final int maxSize) {
     this.store = new LinkedHashMap<Object, Object>() {
       private static final long serialVersionUID = 3335996441470174026L;

       protected boolean removeEldestEntry(Entry<Object, Object> eldest) {
         return this.size() > maxSize;
       }
     };
   }

   public void put(Object key, Object value) {
     Map var3 = this.store;
     synchronized(this.store) {
       this.store.put(key, value);
     }
   }

   public Object get(Object key) {
     Map var2 = this.store;
     synchronized(this.store) {
       return this.store.get(key);
     }
   }

   public Object remove(Object key) {
     Map var2 = this.store;
     synchronized(this.store) {
       return this.store.remove(key);
     }
   }

   public void clear() {
     Map var1 = this.store;
     synchronized(this.store) {
       this.store.clear();
     }
   }
 }
