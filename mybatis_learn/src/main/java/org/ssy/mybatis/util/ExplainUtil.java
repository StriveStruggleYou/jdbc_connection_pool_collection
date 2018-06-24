package org.ssy.mybatis.util;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.ssy.mybatis.interceptor.SqlCostInterceptor;

/**
 * Created by manager on 2018/6/24.
 */
public class ExplainUtil implements Runnable {


  DataSource dataSource;

  public ExplainUtil(DataSource dataSource) {
    this.dataSource = dataSource;
  }


  Set<String> oldExplainSql = new HashSet<>();


  Set<String> newExplainSql = new HashSet<>();


  @Override
  public void run() {

    while (true) {
      diffSql();
      if (newExplainSql.size() == 0) {
        try {
          Thread.sleep(1000);
          System.out.println("no new sql");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        continue;
      }
      for (String explainSql : newExplainSql) {
        doExplain(explainSql);
        oldExplainSql.add(explainSql);
      }


    }
  }

  public void diffSql() {
    newExplainSql.clear();
    Map<String, String> map = SqlCostInterceptor.sqlTemplateAndSql;
    for (String key : map.keySet()) {
      if (oldExplainSql.contains(map.get(key))) {
        continue;
      }
      newExplainSql.add(map.get(key));
    }
  }


  public void doExplain(String sql) {

    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement
          .executeQuery("explain " + sql);
      Gson gson = new Gson();
      ResultSetMetaData rsmd = resultSet.getMetaData();
      //column index 是从1开始

      List<String> columnList = new ArrayList<String>();
      int columnSize = rsmd.getColumnCount();
      String columns = "|";
      for (int i = 1; i < columnSize; i++) {
        columnList.add(rsmd.getColumnName(i));
        columns += rsmd.getColumnName(i) + "|";
      }
      System.out.println(columns);
      while (resultSet.next()) {
        String columnResult = "|";
        for (String columnName : columnList) {
          columnResult += resultSet.getString(columnName) + "|";
        }
        System.out.print(columnResult);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

}
