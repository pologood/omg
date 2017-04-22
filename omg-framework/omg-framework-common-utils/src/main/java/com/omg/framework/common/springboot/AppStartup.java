/*    */ package com.omg.framework.common.springboot;
/*    */ 
/*    */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*    */ 
/*    */ public class AppStartup
/*    */ {
/*    */   private long start;
/*    */   private long end;
/*    */   
/*    */   protected AppStartup(String... configLocations) {
/* 11 */     beforeLoadConfig();
/*    */     try { ClassPathXmlApplicationContext classPathXmlApplicationContext;
/* 13 */       if ((configLocations == null) || (configLocations.length == 0)) {
/* 14 */         classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath*:/spring/applicationContext*.xml");
/*    */       }
/*    */       else
/* 17 */         classPathXmlApplicationContext = new ClassPathXmlApplicationContext(configLocations);
/*    */     } catch (Throwable e) {
/*    */       ClassPathXmlApplicationContext classPathXmlApplicationContext;
/* 20 */       System.err.println("failed to start up, error: " + e.getMessage());
/* 21 */       System.exit(-1);
/*    */     }
/* 23 */     afterLoadConfig();
/*    */   }
/*    */   
/*    */   public static void lanch(String... configLocations) {
/* 27 */     new AppStartup(configLocations);
/*    */   }
/*    */   
/*    */   protected void beforeLoadConfig() {
/* 31 */     if ((System.getProperty("protocol") == null) || (System.getProperty("protocol").equals(""))) {
/* 32 */       System.setProperty("protocol", "file");
/*    */     }
/* 34 */     this.start = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   protected void afterLoadConfig() {
/* 38 */     this.end = System.currentTimeMillis();
/* 39 */     System.out.println("eif application started in " + (this.end - this.start) + " ms");
/*    */   }
/*    */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\eif\framework\eif-framework-common-utils\1.4.2-SNAPSHOT\eif-framework-common-utils-1.4.2-20161102.103954-2.jar!\com\eif\framework\common\springboot\AppStartup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */