/*    */ package com.omg.framework.data.mybatis.pagination.pagehelper.parser.impl;
/*    */ 
/*    */ import com.omg.framework.data.mybatis.pagination.pagehelper.Page;
/*    */ import java.util.Map;
/*    */ import org.apache.ibatis.mapping.BoundSql;
/*    */ import org.apache.ibatis.mapping.MappedStatement;
/*    */
public class MysqlParser extends AbstractParser {
    public MysqlParser() {
    }

    @Override
    public String getPageSql(String sql) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
        sqlBuilder.append(" limit ?,?");
        return sqlBuilder.toString();
    }


    @Override
    public Map setPageParameter(MappedStatement ms, Object parameterObject, BoundSql boundSql, Page page) {
        Map paramMap = super.setPageParameter(ms, parameterObject, boundSql, page);
        paramMap.put("First_PageHelper", Integer.valueOf(page.getStartRow()));
        paramMap.put("Second_PageHelper", Integer.valueOf(page.getPageSize()));
        return paramMap;
    }
}
