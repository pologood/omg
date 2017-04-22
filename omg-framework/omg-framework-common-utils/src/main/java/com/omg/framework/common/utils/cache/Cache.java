package com.omg.framework.common.utils.cache;

public abstract interface Cache
{
  public abstract void put(Object paramObject1, Object paramObject2);
  
  public abstract Object get(Object paramObject);
  
  public abstract Object remove(Object paramObject);
  
  public abstract void clear();
}
