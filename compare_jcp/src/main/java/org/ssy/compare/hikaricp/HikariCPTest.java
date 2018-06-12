package org.ssy.compare.hikaricp;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
public class HikariCPTest {


  public static void main(String args[]) throws SQLException {

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


  public static DataSource getHikariDataSource() {
    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl("x");
    ds.setUsername("x");
    ds.setPassword("x");
    ds.setDriverClassName("com.mysql.jdbc.Driver");
    return ds;
  }

  public static SqlSessionFactory getSqlSessionFactory() {
    DataSource dataSource = getHikariDataSource();
    TransactionFactory transactionFactory = new JdbcTransactionFactory();
    Environment environment = new Environment("development", transactionFactory, dataSource);
    Configuration configuration = new Configuration(environment);
    configuration.addMapper(BusinessMapper.class);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    return sqlSessionFactory;
  }


}
