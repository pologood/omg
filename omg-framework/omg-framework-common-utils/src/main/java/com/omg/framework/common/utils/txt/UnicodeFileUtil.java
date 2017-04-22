/*    */ package com.omg.framework.common.utils.txt;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class UnicodeFileUtil
/*    */ {
/*    */   public static List<String> readFile(InputStream in)
/*    */     throws IOException
/*    */   {
/* 16 */     BufferedReader br = new BufferedReader(new UnicodeReader(in, Charset.defaultCharset().name()));
/*    */     
/* 18 */     List<String> list = new ArrayList();
/*    */     try
/*    */     {
/* 21 */       String line = br.readLine();
/*    */       
/* 23 */       while (line != null) {
/* 24 */         list.add(line);
/* 25 */         line = br.readLine();
/*    */       }
/*    */     }
/*    */     finally {
/* 29 */       br.close();
/* 30 */       in.close();
/*    */     }
/* 32 */     return list;
/*    */   }
/*    */   
/*    */   public static List<String> readFile(String filePath) throws IOException {
/* 36 */     FileInputStream in = new FileInputStream(filePath);
/* 37 */     return readFile(in);
/*    */   }
/*    */ }

