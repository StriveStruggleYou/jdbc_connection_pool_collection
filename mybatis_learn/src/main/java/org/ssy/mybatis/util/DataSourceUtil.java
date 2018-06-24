package org.ssy.mybatis.util;

import com.zaxxer.hikari.HikariDataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.ssy.mybatis.common.BusinessMapper;
import org.ssy.mybatis.interceptor.SqlCostInterceptor;

/**
 * Created by manager on 2018/6/24.
 */
public class DataSourceUtil {

  public static DataSource getHikariDataSource() {
    HikariDataSource ds = new HikariDataSource();
    Properties properties = new Properties();

    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader(
          "/Users/manager/jdbc_connection_pool_collection/compare_jcp/src/main/resources/jdbc.properties"));
      properties.load(bufferedReader);
    } catch (Exception e) {
      e.printStackTrace();

    }
    ds.setJdbcUrl(properties.getProperty("url"));
    ds.setUsername(properties.getProperty("user"));
    ds.setPassword(properties.getProperty("password"));
    ds.setDriverClassName("com.mysql.jdbc.Driver");
    return ds;
  }

  public static SqlSessionFactory getSqlSessionFactory() {
    DataSource dataSource = DataSourceUtil.getHikariDataSource();
    TransactionFactory transactionFactory = new JdbcTransactionFactory();
    Environment environment = new Environment("development", transactionFactory, dataSource);
    Configuration configuration = new Configuration(environment);
    configuration.addMapper(BusinessMapper.class);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    return sqlSessionFactory;
  }


  public static SqlSessionFactory getSqlSessionFactoryInterceptor() {
    DataSource dataSource = DataSourceUtil.getHikariDataSource();
    TransactionFactory transactionFactory = new JdbcTransactionFactory();
    Environment environment = new Environment("development", transactionFactory, dataSource);
    Configuration configuration = new Configuration(environment);
    configuration.addInterceptor(new SqlCostInterceptor(dataSource));
    configuration.addMapper(BusinessMapper.class);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    return sqlSessionFactory;
  }

}
