package org.ssy.mybatis;

import com.google.gson.Gson;
import java.sql.SQLException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.ssy.mybatis.common.Business;
import org.ssy.mybatis.common.BusinessMapper;
import org.ssy.mybatis.util.DataSourceUtil;

/**
 * Created by manager on 2018/6/24.
 */
public class LearnMybatisInterceptor {

  public static void main(String args[]) throws SQLException {
    SqlSessionFactory sqlSessionFactory = DataSourceUtil.getSqlSessionFactoryInterceptor();
    SqlSession session = sqlSessionFactory.openSession();
    try {
      BusinessMapper mapper = session.getMapper(BusinessMapper.class);

      Business business = mapper.selectBusinessName("'云片2'");
      Thread.sleep(10000);
      business = mapper.selectBusiness(1);

      Gson gson = new Gson();
      System.out.println(gson.toJson(business));

    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      session.close();
    }
  }

}
