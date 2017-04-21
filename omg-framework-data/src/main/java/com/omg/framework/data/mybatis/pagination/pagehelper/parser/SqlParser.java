package com.omg.framework.data.mybatis.pagination.pagehelper.parser;
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;

public class SqlParser {
    private static final List<SelectItem> COUNT_ITEM = new ArrayList();
    private static final Alias TABLE_ALIAS;
    private Map<String, String> CACHE = new ConcurrentHashMap();

    public SqlParser() {
    }

    public void isSupportedSql(String sql) {
        if(sql.trim().toUpperCase().endsWith("FOR UPDATE")) {
            throw new RuntimeException("分页插件不支持包含for update的sql");
        }
    }

    public String getSmartCountSql(String sql) {
        this.isSupportedSql(sql);
        if(this.CACHE.get(sql) != null) {
            return (String)this.CACHE.get(sql);
        } else {
            Statement stmt = null;

            try {
                stmt = CCJSqlParserUtil.parse(sql);
            } catch (Throwable var6) {
                String selectBody = this.getSimpleCountSql(sql);
                this.CACHE.put(sql, selectBody);
                return selectBody;
            }

            Select select = (Select)stmt;
            SelectBody selectBody1 = select.getSelectBody();
            this.processSelectBody(selectBody1);
            this.processWithItemsList(select.getWithItemsList());
            this.sqlToCount(select);
            String result = select.toString();
            this.CACHE.put(sql, result);
            return result;
        }
    }

    public String getSimpleCountSql(String sql) {
        this.isSupportedSql(sql);
        StringBuilder stringBuilder = new StringBuilder(sql.length() + 40);
        stringBuilder.append("select count(*) from (");
        stringBuilder.append(sql);
        stringBuilder.append(") tmp_count");
        return stringBuilder.toString();
    }

    public void sqlToCount(Select select) {
        SelectBody selectBody = select.getSelectBody();
        if(selectBody instanceof PlainSelect && this.isSimpleCount((PlainSelect)selectBody)) {
            ((PlainSelect)selectBody).setSelectItems(COUNT_ITEM);
        } else {
            PlainSelect plainSelect = new PlainSelect();
            SubSelect subSelect = new SubSelect();
            subSelect.setSelectBody(selectBody);
            subSelect.setAlias(TABLE_ALIAS);
            plainSelect.setFromItem(subSelect);
            plainSelect.setSelectItems(COUNT_ITEM);
            select.setSelectBody(plainSelect);
        }

    }

    public boolean isSimpleCount(PlainSelect select) {
        if(select.getGroupByColumnReferences() != null) {
            return false;
        } else if(select.getDistinct() != null) {
            return false;
        } else {
            Iterator i$ = select.getSelectItems().iterator();

            SelectItem item;
            do {
                if(!i$.hasNext()) {
                    return true;
                }

                item = (SelectItem)i$.next();
                if(item.toString().contains("?")) {
                    return false;
                }
            } while(!(item instanceof SelectExpressionItem) || !(((SelectExpressionItem)item).getExpression() instanceof Function));

            return false;
        }
    }

    public void processSelectBody(SelectBody selectBody) {
        if(selectBody instanceof PlainSelect) {
            this.processPlainSelect((PlainSelect) selectBody);
        } else if(selectBody instanceof WithItem) {
            WithItem operationList = (WithItem)selectBody;
            if(operationList.getSelectBody() != null) {
                this.processSelectBody(operationList.getSelectBody());
            }
        } else {
            SetOperationList operationList1 = (SetOperationList)selectBody;
            if(operationList1.getPlainSelects() != null && operationList1.getPlainSelects().size() > 0) {
                List plainSelects = operationList1.getPlainSelects();
                Iterator i$ = plainSelects.iterator();

                while(i$.hasNext()) {
                    PlainSelect plainSelect = (PlainSelect)i$.next();
                    this.processPlainSelect(plainSelect);
                }
            }

            if(!this.orderByHashParameters(operationList1.getOrderByElements())) {
                operationList1.setOrderByElements((List)null);
            }
        }

    }

    public void processPlainSelect(PlainSelect plainSelect) {
        if(!this.orderByHashParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements((List)null);
        }

        if(plainSelect.getFromItem() != null) {
            this.processFromItem(plainSelect.getFromItem());
        }

        if(plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List joins = plainSelect.getJoins();
            Iterator i$ = joins.iterator();

            while(i$.hasNext()) {
                Join join = (Join)i$.next();
                if(join.getRightItem() != null) {
                    this.processFromItem(join.getRightItem());
                }
            }
        }

    }

    public void processWithItemsList(List<WithItem> withItemsList) {
        if(withItemsList != null && withItemsList.size() > 0) {
            Iterator i$ = withItemsList.iterator();

            while(i$.hasNext()) {
                WithItem item = (WithItem)i$.next();
                this.processSelectBody(item.getSelectBody());
            }
        }

    }

    public void processFromItem(FromItem fromItem) {
        if(fromItem instanceof SubJoin) {
            SubJoin lateralSubSelect = (SubJoin)fromItem;
            if(lateralSubSelect.getJoin() != null && lateralSubSelect.getJoin().getRightItem() != null) {
                this.processFromItem(lateralSubSelect.getJoin().getRightItem());
            }

            if(lateralSubSelect.getLeft() != null) {
                this.processFromItem(lateralSubSelect.getLeft());
            }
        } else if(fromItem instanceof SubSelect) {
            SubSelect lateralSubSelect1 = (SubSelect)fromItem;
            if(lateralSubSelect1.getSelectBody() != null) {
                this.processSelectBody(lateralSubSelect1.getSelectBody());
            }
        } else if(!(fromItem instanceof ValuesList) && fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect2 = (LateralSubSelect)fromItem;
            if(lateralSubSelect2.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect2.getSubSelect();
                if(subSelect.getSelectBody() != null) {
                    this.processSelectBody(subSelect.getSelectBody());
                }
            }
        }

    }

    public boolean orderByHashParameters(List<OrderByElement> orderByElements) {
        if(orderByElements == null) {
            return false;
        } else {
            Iterator i$ = orderByElements.iterator();

            OrderByElement orderByElement;
            do {
                if(!i$.hasNext()) {
                    return false;
                }

                orderByElement = (OrderByElement)i$.next();
            } while(!orderByElement.toString().contains("?"));

            return true;
        }
    }

    static {
        COUNT_ITEM.add(new SelectExpressionItem(new Column("count(*)")));
        TABLE_ALIAS = new Alias("table_count");
        TABLE_ALIAS.setUseAs(false);
    }
}