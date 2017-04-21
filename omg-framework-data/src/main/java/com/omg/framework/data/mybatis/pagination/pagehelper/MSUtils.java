package com.omg.framework.data.mybatis.pagination.pagehelper;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMap.Builder;
import org.apache.ibatis.mapping.ResultMapping;

public class MSUtils
  implements Constant
{
  private static final List<ResultMapping> EMPTY_RESULTMAPPING = new ArrayList(0);
   public static MappedStatement newCountMappedStatement(MappedStatement ms)
  {
    MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId() + "_COUNT", ms.getSqlSource(), ms.getSqlCommandType());
    builder.resource(ms.getResource());
    builder.fetchSize(ms.getFetchSize());
    builder.statementType(ms.getStatementType());
    builder.keyGenerator(ms.getKeyGenerator());
    if ((ms.getKeyProperties() != null) && (ms.getKeyProperties().length != 0)) {
      StringBuilder keyProperties = new StringBuilder();
      for (String keyProperty : ms.getKeyProperties()) {
        keyProperties.append(keyProperty).append(",");
      }
      keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
      builder.keyProperty(keyProperties.toString());
    }
    builder.timeout(ms.getTimeout());
    builder.parameterMap(ms.getParameterMap());
    
    List<ResultMap> resultMaps = new ArrayList();
    ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), Integer.TYPE, EMPTY_RESULTMAPPING).build();
    resultMaps.add(resultMap);
    builder.resultMaps(resultMaps);
    builder.resultSetType(ms.getResultSetType());
    builder.cache(ms.getCache());
    builder.flushCacheRequired(ms.isFlushCacheRequired());
    builder.useCache(ms.isUseCache());
    
    return builder.build();
  }
}