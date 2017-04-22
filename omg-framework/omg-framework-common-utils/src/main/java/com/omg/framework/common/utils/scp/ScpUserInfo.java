 package com.omg.framework.common.utils.scp;
 
 import com.jcraft.jsch.UserInfo;
 
 public class ScpUserInfo implements UserInfo
 {
   private String passwd;
   
   public ScpUserInfo(String passwd)
   {
     this.passwd = passwd;
   }
   
 
 
   public String getPassphrase()
   {
     return null;
   }
   
 
 
   public String getPassword()
   {
     return this.passwd;
   }
   
 
 
   public boolean promptPassword(String message)
   {
     return true;
   }
   
 
 
   public boolean promptPassphrase(String message)
   {
     return true;
   }
   
 
 
   public boolean promptYesNo(String message)
   {
     return true;
   }
   
   public void showMessage(String message) {}
 }
