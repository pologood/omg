package com.omg.framework.data.mybatis.pagination.pagehelper.sqlsource;

import com.omg.framework.data.mybatis.pagination.orderbyhelper.sqlsource.OrderBySqlSource;
import com.omg.framework.data.mybatis.pagination.orderbyhelper.sqlsource.OrderByStaticSqlSource;
import com.omg.framework.data.mybatis.pagination.pagehelper.Constant;
import com.omg.framework.data.mybatis.pagination.pagehelper.parser.Parser;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

public class PageDynamicSqlSource
  extends PageSqlSource
  implements OrderBySqlSource, Constant
{
  private Configuration configuration;
  private SqlNode rootSqlNode;
  private SqlSource original;
  private Parser parser;
  
  public PageDynamicSqlSource(DynamicSqlSource sqlSource, Parser parser)
  {
    MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
    this.configuration = ((Configuration)metaObject.getValue("configuration"));
    this.rootSqlNode = ((SqlNode)metaObject.getValue("rootSqlNode"));
    this.original = sqlSource;
    this.parser = parser;
  }
  
  protected BoundSql getDefaultBoundSql(Object parameterObject)
  {
    DynamicContext context = new DynamicContext(this.configuration, parameterObject);
    this.rootSqlNode.apply(context);
    SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
    Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
    SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
    sqlSource = new OrderByStaticSqlSource((StaticSqlSource)sqlSource);
    BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
    
    for (Entry<String, Object> entry : context.getBindings().entrySet()) {
      boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
    }
    return boundSql;
  }
  
  protected BoundSql getCountBoundSql(Object parameterObject)
  {
    DynamicContext context = new DynamicContext(this.configuration, parameterObject);
    this.rootSqlNode.apply(context);
    SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
    Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
    SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
    BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
    sqlSource = new StaticSqlSource(this.configuration, this.parser.getCountSql(boundSql.getSql()), boundSql.getParameterMappings());
    boundSql = sqlSource.getBoundSql(parameterObject);
    
    for (Entry<String, Object> entry : context.getBindings().entrySet()) {
      boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
    }
    return boundSql;
  }
  

  protected BoundSql getPageBoundSql(Object parameterObject)
  {
    DynamicContext context;
    if ((parameterObject != null) && ((parameterObject instanceof Map)) && (((Map)parameterObject).containsKey("_ORIGINAL_PARAMETER_OBJECT")))
    {

      context = new DynamicContext(this.configuration, ((Map)parameterObject).get("_ORIGINAL_PARAMETER_OBJECT"));
    } else {
      context = new DynamicContext(this.configuration, parameterObject);
    }
    this.rootSqlNode.apply(context);
    SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
    Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
    SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
    sqlSource = new OrderByStaticSqlSource((StaticSqlSource)sqlSource);
    BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
    sqlSource = new StaticSqlSource(this.configuration, this.parser.getPageSql(boundSql.getSql()), this.parser.getPageParameterMapping(this.configuration, boundSql));
    boundSql = sqlSource.getBoundSql(parameterObject);
    
    for (Entry<String, Object> entry : context.getBindings().entrySet()) {
      boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
    }
    return boundSql;
  }
  
  public SqlSource getOriginal() {
    return this.original;
  }
}