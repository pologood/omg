package com.omg.framework.common.utils.cache;

public abstract interface CacheFactory
{
  public abstract Cache getCache(String paramString);
  
  public abstract Cache getCache(String paramString, int paramInt);
}