 package com.omg.framework.common.utils.scp;
 
 import org.apache.commons.lang.StringUtils;
 
 
 public class ScpBean
 {
   private String remoteAddress;
   private int remotePort;
   private String username;
   private String password;
   
   public boolean validate()
   {
     if (this.remotePort == 0) {
       this.remotePort = 22;
     }
     if ((StringUtils.isBlank(this.remoteAddress)) || (StringUtils.isBlank(this.username)) || (StringUtils.isBlank(this.password)) || (this.remotePort <= 0))
     {
       return false;
     }
     return true;
   }
   
 
 
 
 
   public String getRemoteAddress()
   {
     return this.remoteAddress;
   }
   
 
 
 
 
   public void setRemoteAddress(String remoteAddress)
   {
     this.remoteAddress = remoteAddress;
   }
   
 
 
 
 
   public int getRemotePort()
   {
     return this.remotePort;
   }
   
 
 
 
 
   public void setRemotePort(int remotePort)
   {
     this.remotePort = remotePort;
   }
   
 
 
 
 
   public String getUsername()
   {
     return this.username;
   }
   
 
 
 
 
   public void setUsername(String username)
   {
     this.username = username;
   }
   
 
 
 
 
   public String getPassword()
   {
     return this.password;
   }
   
 
 
 
 
   public void setPassword(String password)
   {
     this.password = password;
   }
 }
