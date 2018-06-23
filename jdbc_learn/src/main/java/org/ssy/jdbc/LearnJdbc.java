package org.ssy.jdbc;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manager on 2018/6/24.
 */
public class LearnJdbc {

  public static void main(String args[]) throws ClassNotFoundException {
    try {

      //获取mysql驱动
      Class.forName("com.mysql.jdbc.Driver");
      //2.获得数据库链接
      Connection conn = DriverManager.getConnection(
          "xxxx",
          "xx", "xx");

      //获取 sql 语句的

      Statement statement = conn.createStatement();

      ResultSet resultSet = statement
          .executeQuery("explain SELECT * FROM business WHERE bus_id = 1");
      Gson gson = new Gson();
      ResultSetMetaData rsmd = resultSet.getMetaData();
      //column index 是从1开始

      List<String> columnList = new ArrayList<String>();
      int columnSize = rsmd.getColumnCount();
      for (int i = 1; i < columnSize; i++) {
        columnList.add(rsmd.getColumnName(i));
//        System.out.println("column:" + rsmd.getColumnName(i));
      }

      while (resultSet.next()) {
        for (String columnName:columnList) {
          System.out.print("column name:"+columnName+" column value:"+resultSet.getString(columnName)+" ");
        }
        System.out.println("");
      }

      //关闭资源
      resultSet.close();
      statement.close();
      conn.close();


    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
