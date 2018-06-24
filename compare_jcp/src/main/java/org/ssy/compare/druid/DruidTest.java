package org.ssy.compare.druid;

import com.alibaba.druid.pool.DruidDataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import org.ssy.compare.common.BusinessMapper;
import org.ssy.compare.common.SelectRunnable;

/**
 * Created by manager on 2018/6/12.
 */
public class DruidTest {

  public static void main(String args[]) {
    SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

//    SqlSession session = sqlSessionFactory.openSession()
    List<Thread> threads = new ArrayList<Thread>();
    for (int i = 0; i < 20; i++) {
      SqlSession session = sqlSessionFactory.openSession();
      Thread thread = new Thread(new SelectRunnable(session));
      threads.add(thread);
    }
    for (Thread thread : threads) {
      thread.start();
    }
  }

  public static SqlSessionFactory getSqlSessionFactory() {
    DataSource dataSource = getDruidDataSource();

    TransactionFactory transactionFactory = new JdbcTransactionFactory();
    Environment environment = new Environment("development", transactionFactory, dataSource);
    Configuration configuration = new Configuration(environment);
    configuration.addMapper(BusinessMapper.class);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    return sqlSessionFactory;
  }

  public static DataSource getDruidDataSource() {
    Properties properties = new Properties();
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader(
          "/Users/manager/jdbc_connection_pool_collection/compare_jcp/src/main/resources/jdbc.properties"));
      properties.load(bufferedReader);
    } catch (Exception e) {
      e.printStackTrace();

    }

    DruidDataSource ds = new DruidDataSource();
    ds.setUrl(properties.getProperty("url"));
    ds.setUsername(properties.getProperty("user"));
    ds.setPassword(properties.getProperty("password"));
    ds.setDriverClassName("com.mysql.jdbc.Driver");
    return ds;
  }


}
