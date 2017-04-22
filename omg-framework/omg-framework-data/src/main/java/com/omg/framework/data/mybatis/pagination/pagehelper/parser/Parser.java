package com.omg.framework.data.mybatis.pagination.pagehelper.parser;

import com.omg.framework.data.mybatis.pagination.pagehelper.Page;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

public abstract interface Parser
{
  public abstract boolean isSupportedMappedStatementCache();
  
  public abstract String getCountSql(String paramString);
  
  public abstract String getPageSql(String paramString);
  
  public abstract List<ParameterMapping> getPageParameterMapping(Configuration paramConfiguration, BoundSql paramBoundSql);
  
  public abstract Map setPageParameter(MappedStatement paramMappedStatement, Object paramObject, BoundSql paramBoundSql, Page paramPage);
}

