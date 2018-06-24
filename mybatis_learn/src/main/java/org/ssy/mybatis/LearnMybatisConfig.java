package org.ssy.mybatis;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.ssy.mybatis.common.Business;
import org.ssy.mybatis.common.BusinessMapper;
import org.ssy.mybatis.util.DataSourceUtil;

/**
 * Created by manager on 2018/6/24.
 */
public class LearnMybatisConfig {

  public static void main(String args[]) throws SQLException {
    SqlSessionFactory sqlSessionFactory = DataSourceUtil.getSqlSessionFactory();
    SqlSession session = sqlSessionFactory.openSession();
    try {
      BusinessMapper mapper = session.getMapper(BusinessMapper.class);
      Business business = mapper.selectBusiness(1);
      Gson gson = new Gson();
      System.out.println(gson.toJson(business));
    } finally {
      session.close();
    }
  }


}
