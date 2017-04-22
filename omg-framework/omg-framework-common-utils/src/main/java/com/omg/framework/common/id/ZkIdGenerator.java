/*     */ package com.omg.framework.common.id;
/*     */ 
/*     */ import com.omg.framework.common.utils.Assert;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.curator.framework.CuratorFramework;
/*     */ import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
/*     */ import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
/*     */ import org.apache.curator.framework.api.CreateBuilder;
/*     */ import org.apache.curator.framework.api.GetChildrenBuilder;
/*     */ import org.apache.curator.framework.api.ProtectACLCreateModePathAndBytesable;
/*     */ import org.apache.zookeeper.CreateMode;
/*     */ import org.apache.zookeeper.data.Stat;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class ZkIdGenerator extends IdGenerator
/*     */ {
/*  21 */   private static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ZkIdGenerator.class);
/*     */   
/*     */   private static final String NODE_PREFIX = "/seq";
/*     */   private String zkAddress;
/*     */   private String moduleName;
/*     */   private String idNodeName;
/*     */   private CuratorFramework client;
/*     */   
/*     */   public void setModuleName(String moduleName)
/*     */   {
/*  31 */     Assert.hasText(moduleName);
/*  32 */     this.moduleName = moduleName;
/*     */   }
/*     */   
/*     */   public void setZkAddress(String zkAddress) {
/*  36 */     Assert.hasText(zkAddress);
/*  37 */     this.zkAddress = zkAddress;
/*     */   }
/*     */   
/*     */   public void setIdNodeName(String idNodeName) {
/*  41 */     Assert.hasText(idNodeName);
/*  42 */     this.idNodeName = idNodeName;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/*  47 */     Assert.hasText(this.zkAddress);
/*  48 */     Assert.hasText(this.moduleName);
/*  49 */     Assert.hasText(this.idNodeName);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  54 */     super.afterPropertiesSet();
/*     */     
/*  56 */     getMachineId();
/*     */   }
/*     */   
/*     */   protected void getMachineId() {
/*  60 */     this.client = org.apache.curator.framework.CuratorFrameworkFactory.builder().connectString(this.zkAddress).retryPolicy(new org.apache.curator.retry.ExponentialBackoffRetry(1000, 3)).sessionTimeoutMs(5000).build();
/*  61 */     this.client.start();
/*     */     try
/*     */     {
/*  64 */       String localAddress = getLocalAddress();
/*  65 */       if (StringUtils.isBlank(localAddress)) {
/*  66 */         setMachineId((String)((ACLBackgroundPathAndBytesable)this.client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)).forPath(getRootPath() + "/seq"));
/*     */       } else {
/*  68 */         Stat stat = (Stat)this.client.checkExists().forPath(getRootPath());
/*  69 */         if (stat == null) {
/*  70 */           setMachineId((String)((ACLBackgroundPathAndBytesable)this.client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)).forPath(getRootPath() + "/seq", localAddress.getBytes("UTF8")));
/*     */         }
/*     */         else {
/*  73 */           String nodeName = null;
/*  74 */           Iterator<String> it = ((List)this.client.getChildren().forPath(getRootPath())).iterator();
/*  75 */           while (it.hasNext()) {
/*  76 */             nodeName = (String)it.next();
/*  77 */             if (!StringUtils.isBlank(nodeName))
/*     */             {
/*     */ 
/*     */ 
/*  81 */               byte[] data = (byte[])this.client.getData().forPath(getRootPath() + "/" + nodeName);
/*  82 */               if (org.apache.commons.lang.ArrayUtils.isNotEmpty(data)) {
/*  83 */                 String nodeAddress = new String(data, "UTF8");
/*  84 */                 if ((StringUtils.isNotBlank(nodeAddress)) && (nodeAddress.equals(localAddress))) {
/*  85 */                   setMachineId(nodeName);
/*  86 */                   break;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*  91 */           if (StringUtils.isBlank(this.machineId)) {
/*  92 */             setMachineId((String)((ACLBackgroundPathAndBytesable)this.client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)).forPath(getRootPath() + "/seq", localAddress.getBytes("UTF8")));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable e) {
/*  98 */       LOGGER.error("failed to get machineId", e);
/*     */     } finally {
/* 100 */       this.client.close();
/*     */     }
/*     */   }
/*     */   
/*     */   private String getLocalAddress() {
/*     */     try {
/* 106 */       return InetAddress.getLocalHost().getHostAddress();
/*     */     } catch (UnknownHostException e) {
/* 108 */       LOGGER.error("failed to get local address", e);
/*     */     }
/* 110 */     return null;
/*     */   }
/*     */   
/*     */   private String getRootPath() {
/* 114 */     return "/eif/" + this.moduleName + "/" + this.idNodeName;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lenovo\.m2\repository\com\eif\framework\eif-framework-common-utils\1.4.2-SNAPSHOT\eif-framework-common-utils-1.4.2-20161102.103954-2.jar!\com\eif\framework\common\id\ZkIdGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
