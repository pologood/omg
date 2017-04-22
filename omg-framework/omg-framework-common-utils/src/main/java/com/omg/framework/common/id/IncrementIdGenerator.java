/*     */ package com.omg.framework.common.id;
/*     */ 
/*     */ import com.omg.framework.common.exception.CannotIncreaseException;
/*     */ import com.omg.framework.common.exception.CannotSetOffsetException;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.curator.framework.CuratorFramework;
/*     */ import org.apache.curator.framework.CuratorFrameworkFactory;
/*     */ import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
/*     */ import org.apache.curator.framework.recipes.atomic.AtomicValue;
/*     */ import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
/*     */ import org.apache.curator.retry.ExponentialBackoffRetry;
/*     */ import org.apache.curator.retry.RetryNTimes;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ 
/*     */ 
/*     */ public class IncrementIdGenerator
/*     */   implements InitializingBean
/*     */ {
/*  22 */   protected BlockingQueue<Long> ids = new ArrayBlockingQueue(5000);
/*  23 */   protected long increment = 5000L;
/*     */   private Long offset;
/*     */   private String moduleName;
/*     */   private String idNodeName;
/*     */   private String zkAddress;
/*  28 */   private int length = 9;
/*     */   
/*     */ 
/*     */   public String genId()
/*     */   {
/*  33 */     Long id = null;
/*     */     try {
/*  35 */       id = (Long)this.ids.poll(3000L, TimeUnit.MILLISECONDS);
/*     */     } catch (InterruptedException e) {
/*  37 */       e.printStackTrace();
/*     */     }
/*     */     
/*  40 */     if (id == null) {
/*  41 */       id = (Long)this.ids.poll();
/*     */     }
/*     */     
/*  44 */     if (id == null)
/*  45 */       throw new RuntimeException("can not generate id");
/*  46 */     if (this.ids.isEmpty()) {
/*  47 */       reset();
/*     */     }
/*     */     
/*  50 */     return StringUtils.leftPad(String.valueOf(id), this.length, "0");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized void reset()
/*     */   {
/*  60 */     if (this.ids.isEmpty()) {
/*  61 */       long curValue = incDistributedValue();
/*  62 */       for (long i = curValue; i < curValue + this.increment; i += 1L) {
/*  63 */         this.ids.offer(Long.valueOf(i));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void init() {
/*  69 */     long curValue = initDistributedValue();
/*  70 */     for (long i = curValue; i < curValue + this.increment; i += 1L) {
/*  71 */       this.ids.offer(Long.valueOf(i));
/*     */     }
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/*  77 */     init();
/*     */   }
/*     */   
/*     */   private long incDistributedValue() {
/*  81 */     CuratorFramework client = null;
/*     */     try {
/*  83 */       client = CuratorFrameworkFactory.builder().connectString(this.zkAddress).retryPolicy(new ExponentialBackoffRetry(1000, 3)).sessionTimeoutMs(5000).build();
/*     */       
/*  85 */       client.start();
/*  86 */       DistributedAtomicLong count = new DistributedAtomicLong(client, getRootPath(), new RetryNTimes(10, 10));
/*     */       
/*  88 */       AtomicValue<Long> ac = null;
/*  89 */       for (int i = 0; i < 5; i++) {
/*  90 */         ac = count.add(Long.valueOf(this.increment));
/*  91 */         if (ac.succeeded()) {
/*  92 */           return ((Long)ac.preValue()).longValue();
/*     */         }
/*     */       }
/*  95 */       throw new CannotIncreaseException();
/*     */     } catch (Throwable e) {
/*  97 */       throw new CannotIncreaseException();
/*     */     } finally {
/*  99 */       if (client != null) {
/* 100 */         client.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private long initDistributedValue() {
/* 106 */     CuratorFramework client = null;
/*     */     try {
/* 108 */       client = CuratorFrameworkFactory.builder().connectString(this.zkAddress).retryPolicy(new ExponentialBackoffRetry(1000, 3)).sessionTimeoutMs(5000).build();
/*     */       
/* 110 */       client.start();
/* 111 */       DistributedAtomicLong count = new DistributedAtomicLong(client, getRootPath(), new RetryNTimes(10, 10));
/* 112 */       if (this.offset != null) {
/* 113 */         AtomicValue<Long> av = null;
/* 114 */         for (int i = 0; i < 5; i++) {
/* 115 */           av = count.trySet(this.offset);
/* 116 */           if (av.succeeded()) {
/*     */             break;
/*     */           }
/* 119 */           if (i == 4) {
/* 120 */             throw new CannotSetOffsetException();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 128 */       AtomicValue<Long> av = null;
/* 129 */       for (int i = 0; i < 5; i++) {
/* 130 */         av = count.add(Long.valueOf(this.increment));
/* 131 */         if (av.succeeded()) {
/* 132 */           return ((Long)av.preValue()).longValue();
/*     */         }
/*     */       }
/* 135 */       throw new CannotIncreaseException();
/*     */     } catch (Throwable e) {
/* 137 */       throw new CannotIncreaseException();
/*     */     } finally {
/* 139 */       if (client != null) {
/* 140 */         client.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private String getRootPath() {
/* 146 */     return "/eif/" + this.moduleName + "/" + this.idNodeName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getModuleName()
/*     */   {
/* 162 */     return this.moduleName;
/*     */   }
/*     */   
/*     */   public void setModuleName(String moduleName) {
/* 166 */     this.moduleName = moduleName;
/*     */   }
/*     */   
/*     */   public String getIdNodeName() {
/* 170 */     return this.idNodeName;
/*     */   }
/*     */   
/*     */   public void setIdNodeName(String idNodeName) {
/* 174 */     this.idNodeName = idNodeName;
/*     */   }
/*     */   
/*     */   public String getZkAddress() {
/* 178 */     return this.zkAddress;
/*     */   }
/*     */   
/*     */   public void setZkAddress(String zkAddress) {
/* 182 */     this.zkAddress = zkAddress;
/*     */   }
/*     */   
/*     */   public int getLength() {
/* 186 */     return this.length;
/*     */   }
/*     */   
/*     */   public void setLength(int length) {
/* 190 */     if (length < 9) {
/* 191 */       throw new IllegalArgumentException("length must be greater than or equal to 9");
/*     */     }
/* 193 */     this.length = length;
/*     */   }
/*     */   
/*     */   public Long getOffset() {
/* 197 */     return this.offset;
/*     */   }
/*     */   
/*     */   public void setOffset(Long offset) {
/* 201 */     if ((offset != null) && (offset.longValue() < 0L)) {
/* 202 */       throw new IllegalArgumentException("offset must be greater than or equal to 0");
/*     */     }
/* 204 */     this.offset = offset;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\eif\framework\eif-framework-common-utils\1.4.2-SNAPSHOT\eif-framework-common-utils-1.4.2-20161102.103954-2.jar!\com\eif\framework\common\id\IncrementIdGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
