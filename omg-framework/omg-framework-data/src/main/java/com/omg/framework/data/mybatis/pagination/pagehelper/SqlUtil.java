package com.omg.framework.data.mybatis.pagination.pagehelper;

import com.omg.framework.data.mybatis.pagination.orderbyhelper.sqlsource.OrderBySqlSource;
import com.omg.framework.data.mybatis.pagination.pagehelper.parser.Parser;
import com.omg.framework.data.mybatis.pagination.pagehelper.parser.impl.AbstractParser;
import com.omg.framework.data.mybatis.pagination.pagehelper.sqlsource.PageDynamicSqlSource;
import com.omg.framework.data.mybatis.pagination.pagehelper.sqlsource.PageProviderSqlSource;
import com.omg.framework.data.mybatis.pagination.pagehelper.sqlsource.PageRawSqlSource;
import com.omg.framework.data.mybatis.pagination.pagehelper.sqlsource.PageSqlSource;
import com.omg.framework.data.mybatis.pagination.pagehelper.sqlsource.PageStaticSqlSource;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.session.RowBounds;



public class SqlUtil implements Constant {
    private static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal();
    private static Map<String, String> PARAMS = new HashMap(5);
    private static Boolean hasRequest;
    private static Class<?> requestClass;
    private static Method getParameterMap;
    private final Map<String, MappedStatement> msCountMap = new ConcurrentHashMap();
    private boolean offsetAsPageNum = false;
    private boolean rowBoundsWithCount = false;
    private boolean pageSizeZero = false;
    private boolean reasonable = false;
    private Parser parser;
    private ReturnPageInfo returnPageInfo;
    private Map<String, Boolean> returnPageInfoMap;
    private boolean supportMethodsArguments;

    public SqlUtil(String strDialect) {
        this.returnPageInfo = ReturnPageInfo.NONE;
        this.returnPageInfoMap = new ConcurrentHashMap();
        this.supportMethodsArguments = false;
        if(strDialect != null && !"".equals(strDialect)) {
            Object exception = null;

            try {
                Dialect e = Dialect.of(strDialect);
                this.parser = AbstractParser.newParser(e);
            } catch (Exception var8) {
                exception = var8;

                try {
                    Class ex = Class.forName(strDialect);
                    if(Parser.class.isAssignableFrom(ex)) {
                        this.parser = (Parser)ex.newInstance();
                    }
                } catch (ClassNotFoundException var5) {
                    exception = var5;
                } catch (InstantiationException var6) {
                    exception = var6;
                } catch (IllegalAccessException var7) {
                    exception = var7;
                }
            }

            if(this.parser == null) {
                throw new RuntimeException((Throwable)exception);
            }
        } else {
            throw new IllegalArgumentException("Mybatis分页插件无法获取dialect参数!");
        }
    }

    public static Boolean getCOUNT() {
        Page page = getLocalPage();
        return page != null?page.getCountSignal():null;
    }

    public static Page getLocalPage() {
        return (Page)LOCAL_PAGE.get();
    }

    public static void setLocalPage(Page page) {
        LOCAL_PAGE.set(page);
    }

    public static void clearLocalPage() {
        LOCAL_PAGE.remove();
    }

    public static Page getPageFromObject(Object params) {
        MetaObject paramsObject = null;
        if(params == null) {
            throw new NullPointerException("无法获取分页查询参数!");
        } else {
            if(hasRequest.booleanValue() && requestClass.isAssignableFrom(params.getClass())) {
                try {
                    paramsObject = SystemMetaObject.forObject(getParameterMap.invoke(params, new Object[0]));
                } catch (Exception var10) {
                    ;
                }
            } else {
                paramsObject = SystemMetaObject.forObject(params);
            }

            if(paramsObject == null) {
                throw new NullPointerException("分页查询参数处理失败!");
            } else {
                Object orderBy = getParamValue(paramsObject, "orderBy", false);
                boolean hasOrderBy = false;
                if(orderBy != null && orderBy.toString().length() > 0) {
                    hasOrderBy = true;
                }

                int pageNum;
                int pageSize;
                Object _count;
                try {
                    Object page = getParamValue(paramsObject, "pageNum", !hasOrderBy);
                    _count = getParamValue(paramsObject, "pageSize", !hasOrderBy);
                    if(page == null || _count == null) {
                        Page reasonable1 = new Page();
                        reasonable1.setOrderBy(orderBy.toString());
                        reasonable1.setOrderByOnly(true);
                        return reasonable1;
                    }

                    pageNum = Integer.parseInt(String.valueOf(page));
                    pageSize = Integer.parseInt(String.valueOf(_count));
                } catch (NumberFormatException var11) {
                    throw new IllegalArgumentException("分页参数不是合法的数字类型!");
                }

                Page page1 = new Page(pageNum, pageSize);
                _count = getParamValue(paramsObject, "count", false);
                if(_count != null) {
                    page1.setCount(Boolean.valueOf(String.valueOf(_count)).booleanValue());
                }

                if(hasOrderBy) {
                    page1.setOrderBy(orderBy.toString());
                }

                Object reasonable = getParamValue(paramsObject, "reasonable", false);
                if(reasonable != null) {
                    page1.setReasonable(Boolean.valueOf(String.valueOf(reasonable)));
                }

                Object pageSizeZero = getParamValue(paramsObject, "pageSizeZero", false);
                if(pageSizeZero != null) {
                    page1.setPageSizeZero(Boolean.valueOf(String.valueOf(pageSizeZero)));
                }

                return page1;
            }
        }
    }

    public static Object getParamValue(MetaObject paramsObject, String paramName, boolean required) {
        Object value = null;
        if(paramsObject.hasGetter((String)PARAMS.get(paramName))) {
            value = paramsObject.getValue((String)PARAMS.get(paramName));
        }

        if(value != null && value.getClass().isArray()) {
            Object[] values = (Object[])((Object[])value);
            if(values.length == 0) {
                value = null;
            } else {
                value = values[0];
            }
        }

        if(required && value == null) {
            throw new RuntimeException("分页查询缺少必要的参数:" + (String)PARAMS.get(paramName));
        } else {
            return value;
        }
    }

    public static boolean isPageSqlSource(MappedStatement ms) {
        return ms.getSqlSource() instanceof PageSqlSource;
    }

    public static void testSql(String dialect, String originalSql) {
        testSql(Dialect.of(dialect), originalSql);
    }

    public static void testSql(Dialect dialect, String originalSql) {
        Parser parser = AbstractParser.newParser(dialect);
        if(dialect == Dialect.sqlserver) {
            setLocalPage(new Page(1, 10));
        }

        String countSql = parser.getCountSql(originalSql);
        System.out.println(countSql);
        String pageSql = parser.getPageSql(originalSql);
        System.out.println(pageSql);
        if(dialect == Dialect.sqlserver) {
            clearLocalPage();
        }

    }

    public void processMappedStatement(MappedStatement ms, Parser parser) throws Throwable {
        SqlSource sqlSource = ms.getSqlSource();
        MetaObject msObject = SystemMetaObject.forObject(ms);
        SqlSource tempSqlSource = sqlSource;
        if(sqlSource instanceof OrderBySqlSource) {
            tempSqlSource = ((OrderBySqlSource)sqlSource).getOriginal();
        }

        Object pageSqlSource;
        if(tempSqlSource instanceof StaticSqlSource) {
            pageSqlSource = new PageStaticSqlSource((StaticSqlSource)tempSqlSource, parser);
        } else if(tempSqlSource instanceof RawSqlSource) {
            pageSqlSource = new PageRawSqlSource((RawSqlSource)tempSqlSource, parser);
        } else if(tempSqlSource instanceof ProviderSqlSource) {
            pageSqlSource = new PageProviderSqlSource((ProviderSqlSource)tempSqlSource, parser);
        } else {
            if(!(tempSqlSource instanceof DynamicSqlSource)) {
                throw new RuntimeException("无法处理该类型[" + sqlSource.getClass() + "]的SqlSource");
            }

            pageSqlSource = new PageDynamicSqlSource((DynamicSqlSource)tempSqlSource, parser);
        }

        msObject.setValue("sqlSource", pageSqlSource);
        this.msCountMap.put(ms.getId(), MSUtils.newCountMappedStatement(ms));
    }

    public Page getPage(Object[] args) {
        Page page = getLocalPage();
        if(page == null || page.isOrderByOnly()) {
            Page oldPage = page;
            if((args[2] == null || args[2] == RowBounds.DEFAULT) && page != null) {
                return page;
            }

            if(args[2] instanceof RowBounds && args[2] != RowBounds.DEFAULT) {
                RowBounds e = (RowBounds)args[2];
                if(this.offsetAsPageNum) {
                    page = new Page(e.getOffset(), e.getLimit(), this.rowBoundsWithCount);
                } else {
                    page = new Page(new int[]{e.getOffset(), e.getLimit()}, this.rowBoundsWithCount);
                    page.setReasonable(Boolean.valueOf(false));
                }
            } else {
                try {
                    page = getPageFromObject(args[1]);
                } catch (Exception var5) {
                    return null;
                }
            }

            if(oldPage != null) {
                page.setOrderBy(oldPage.getOrderBy());
            }

            setLocalPage(page);
        }

        if(page.getReasonable() == null) {
            page.setReasonable(Boolean.valueOf(this.reasonable));
        }

        if(page.getPageSizeZero() == null) {
            page.setPageSizeZero(Boolean.valueOf(this.pageSizeZero));
        }

        return page;
    }

    public Object processPage(Invocation invocation) throws Throwable {
        Object var3;
        try {
            Object result = this._processPage(invocation);
            var3 = result;
        } finally {
            clearLocalPage();
        }

        return var3;
    }

    private Object _processPage(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Page page = null;
        if(this.supportMethodsArguments) {
            page = this.getPage(args);
        }

        RowBounds rowBounds = (RowBounds)args[2];
        if(this.supportMethodsArguments && page == null || !this.supportMethodsArguments && getLocalPage() == null && rowBounds == RowBounds.DEFAULT) {
            return invocation.proceed();
        } else {
            if(page == null) {
                page = this.getPage(args);
            }

            page = this.doProcessPage(invocation, page, args);
            return this.processPageInfo(page, args);
        }
    }

    private Object processPageInfo(Page<?> page, Object[] args)
  {
    switch (this.returnPageInfo) {
    case NONE: 
      return page;
    case ALWAYS: 
      return returnPageInfo(page);
    case CHECK: 
      if (isReturnPageInfo((MappedStatement)args[0])) {
        return returnPageInfo(page);
      }
      return page;
    }
    
    return page;
  }

    private Object returnPageInfo(Page<?> page) {
        ArrayList list = new ArrayList();
        list.add(new PageInfo(page));
        return list;
    }

    private boolean isReturnPageInfo(MappedStatement ms) {
        String msId = ms.getId();
        if(!this.returnPageInfoMap.containsKey(msId)) {
            String _interface = msId.substring(0, msId.lastIndexOf("."));
            String _methodName = msId.substring(_interface.length() + 1);

            try {
                Class e = Class.forName(_interface);
                Method[] methods = e.getDeclaredMethods();
                Method m = null;
                Method[] returnClass = methods;
                int len$ = methods.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    Method method = returnClass[i$];
                    if(method.getName().equals(_methodName)) {
                        m = method;
                        break;
                    }
                }

                if(m == null) {
                    this.returnPageInfoMap.put(msId, Boolean.valueOf(false));
                } else {
                    Class var14 = m.getReturnType();
                    if(var14.equals(PageInfo.class)) {
                        this.returnPageInfoMap.put(msId, Boolean.valueOf(true));
                    } else {
                        this.returnPageInfoMap.put(msId, Boolean.valueOf(false));
                    }
                }
            } catch (ClassNotFoundException var12) {
                this.returnPageInfoMap.put(msId, Boolean.valueOf(false));
            } catch (Exception var13) {
                throw new RuntimeException(var13);
            }
        }

        return ((Boolean)this.returnPageInfoMap.get(msId)).booleanValue();
    }

    private boolean isQueryOnly(Page page) {
        return page.isOrderByOnly() || page.getPageSizeZero() != null && page.getPageSizeZero().booleanValue() && page.getPageSize() == 0;
    }

    private Page doQueryOnly(Page page, Invocation invocation) throws Throwable {
        page.setCountSignal((Boolean)null);
        Object result = invocation.proceed();
        page.addAll((List)result);
        page.setPageNum(1);
        page.setPageSize(page.size());
        page.setTotal((long)page.size());
        return page;
    }

    private Page doProcessPage(Invocation invocation, Page page, Object[] args) throws Throwable {
        RowBounds rowBounds = (RowBounds)args[2];
        MappedStatement ms = (MappedStatement)args[0];
        if(!isPageSqlSource(ms)) {
            this.processMappedStatement(ms, this.parser);
        }

        args[2] = RowBounds.DEFAULT;
        if(this.isQueryOnly(page)) {
            return this.doQueryOnly(page, invocation);
        } else {
            if(page.isCount()) {
                page.setCountSignal(Boolean.TRUE);
                args[0] = this.msCountMap.get(ms.getId());
                Object boundSql = invocation.proceed();
                args[0] = ms;
                page.setTotal((long)((Integer)((List)boundSql).get(0)).intValue());
                if(page.getTotal() == 0L) {
                    return page;
                }
            } else {
                page.setTotal(-1L);
            }

            if(page.getPageSize() > 0 && (rowBounds == RowBounds.DEFAULT && page.getPageNum() > 0 || rowBounds != RowBounds.DEFAULT)) {
                page.setCountSignal((Boolean)null);
                BoundSql boundSql1 = ms.getBoundSql(args[1]);
                args[1] = this.parser.setPageParameter(ms, args[1], boundSql1, page);
                page.setCountSignal(Boolean.FALSE);
                Object result = invocation.proceed();
                page.addAll((List)result);
            }

            return page;
        }
    }

    public void setProperties(Properties p) {
        String offsetAsPageNum = p.getProperty("offsetAsPageNum");
        this.offsetAsPageNum = Boolean.parseBoolean(offsetAsPageNum);
        String rowBoundsWithCount = p.getProperty("rowBoundsWithCount");
        this.rowBoundsWithCount = Boolean.parseBoolean(rowBoundsWithCount);
        String pageSizeZero = p.getProperty("pageSizeZero");
        this.pageSizeZero = Boolean.parseBoolean(pageSizeZero);
        String reasonable = p.getProperty("reasonable");
        this.reasonable = Boolean.parseBoolean(reasonable);
        String supportMethodsArguments = p.getProperty("supportMethodsArguments");
        this.supportMethodsArguments = Boolean.parseBoolean(supportMethodsArguments);
        String returnPageInfo = p.getProperty("returnPageInfo");
        if(returnPageInfo != null && returnPageInfo.length() > 0) {
            this.returnPageInfo = ReturnPageInfo.valueOf(returnPageInfo.toUpperCase());
        }

        PARAMS.put("pageNum", "pageNum");
        PARAMS.put("pageSize", "pageSize");
        PARAMS.put("count", "countSql");
        PARAMS.put("orderBy", "orderBy");
        PARAMS.put("reasonable", "reasonable");
        PARAMS.put("pageSizeZero", "pageSizeZero");
        String params = p.getProperty("params");
        if(params != null && params.length() > 0) {
            String[] ps = params.split("[;|,|&]");
            String[] arr$ = ps;
            int len$ = ps.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String s = arr$[i$];
                String[] ss = s.split("[=|:]");
                if(ss.length == 2) {
                    PARAMS.put(ss[0], ss[1]);
                }
            }
        }

    }

    static {
        try {
            requestClass = Class.forName("javax.servlet.ServletRequest");
            getParameterMap = requestClass.getMethod("getParameterMap", new Class[0]);
            hasRequest = Boolean.valueOf(true);
        } catch (Exception var1) {
            hasRequest = Boolean.valueOf(false);
        }

    }
}
