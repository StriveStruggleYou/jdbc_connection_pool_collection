package org.ssy.compare.interceptor;

import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * Created by manager on 2018/6/22.
 */
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class LogInterceptor implements Interceptor {

  Set sqlSet=new HashSet();

  public Object intercept(Invocation invocation) throws Throwable {

    if (invocation.getTarget() instanceof StatementHandler) {
      StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
      MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);

      BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");

      String sql = boundSql.getSql();
      System.out.println("原始SQL: " + sql);
      sqlSet.add(sql);


      String pageSql = overrideSQL(sql);
      System.out.println("改写后的SQL: " + pageSql+" size:"+sqlSet.size());

      metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
    }
    return invocation.proceed();
  }

  public String overrideSQL(String sql) {
    sql = sql.replace("SELECT", "select");
    return sql;
  }

  public Object plugin(Object o) {
    if (o instanceof StatementHandler) {
      return Plugin.wrap(o, this);
    } else {
      return o;
    }
  }

  public void setProperties(Properties properties) {

    System.out.println("setProperties....:" + properties.toString());

  }
}
