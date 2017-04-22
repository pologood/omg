/*    */ package com.omg.framework.common.id;
/*    */ 
/*    */ import com.omg.framework.common.utils.Assert;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.concurrent.ArrayBlockingQueue;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class IdGenerator
/*    */   implements InitializingBean
/*    */ {
/* 17 */   protected BlockingQueue<Integer> ids = new ArrayBlockingQueue(1000);
/*    */   protected String machineId;
/*    */   protected String dateFormat;
/*    */   
/*    */   public SimpleDateFormat getDateFormat() {
/* 22 */     Assert.hasText(this.dateFormat);
/* 23 */     return new SimpleDateFormat(this.dateFormat);
/*    */   }
/*    */   
/*    */   public void setDateFormat(String dateFormat) {
/* 27 */     this.dateFormat = dateFormat;
/*    */   }
/*    */   
/*    */   protected abstract void setDefaultDateFormat();
/*    */   
/*    */   public String genId() {
/* 33 */     Assert.hasText(this.machineId);
/*    */     
/* 35 */     Integer id = null;
/*    */     try {
/* 37 */       id = (Integer)this.ids.poll(1000L, TimeUnit.MILLISECONDS);
/*    */     } catch (InterruptedException e) {
/* 39 */       e.printStackTrace();
/*    */     }
/*    */     
/* 42 */     if (id == null) {
/* 43 */       id = (Integer)this.ids.poll();
/*    */     }
/*    */     
/* 46 */     if (id == null)
/* 47 */       throw new RuntimeException("can not generate id");
/* 48 */     if (id.intValue() == 999) {
/* 49 */       fillingIds();
/*    */     }
/*    */     
/* 52 */     return getDateFormat().format(new Date()) + StringUtils.leftPad(this.machineId, 2, "0") + StringUtils.leftPad(String.valueOf(id), 3, "0");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setMachineId(String machineId)
/*    */   {
/* 66 */     Assert.hasText(machineId);
/* 67 */     if (machineId.length() > 2) {
/* 68 */       this.machineId = StringUtils.right(machineId, 2);
/*    */     } else {
/* 70 */       this.machineId = machineId;
/*    */     }
/*    */   }
/*    */   
/*    */   private void fillingIds() {
/* 75 */     for (int i = 0; i < 1000; i++) {
/* 76 */       this.ids.offer(Integer.valueOf(i));
/*    */     }
/*    */   }
/*    */   
/*    */   public void afterPropertiesSet() throws Exception
/*    */   {
/* 82 */     if (StringUtils.isBlank(this.dateFormat)) {
/* 83 */       setDefaultDateFormat();
/*    */     }
/* 85 */     fillingIds();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\eif\framework\eif-framework-common-utils\1.4.2-SNAPSHOT\eif-framework-common-utils-1.4.2-20161102.103954-2.jar!\com\eif\framework\common\id\IdGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
