 package com.omg.framework.common.exception;
 
 public class CannotSetOffsetException extends RuntimeException
 {
   private static final long serialVersionUID = 7804271651659850182L;
   
   public CannotSetOffsetException(String msg) {
     super(msg);
   }
   
 
   public CannotSetOffsetException() {}
   
   public CannotSetOffsetException(Throwable e)
   {
     super(e);
   }
 }
