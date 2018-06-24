package org.ssy.mybatis;

import org.apache.ibatis.jdbc.SQL;

/**
 * Created by manager on 2018/6/24.
 */
public class LearnBuildSQL {

  public static void main(String args[]) {
    SQL sql = new SQL();
    String sqlStr = sql.SELECT("name").FROM("bussiness").toString();
    System.out.println(sqlStr);
  }


}
