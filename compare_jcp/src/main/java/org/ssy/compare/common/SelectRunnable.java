package org.ssy.compare.common;

import com.google.gson.Gson;
import org.apache.ibatis.session.SqlSession;
import org.ssy.compare.common.BusinessMapper;

/**
 * Created by manager on 2018/6/12.
 */
public class SelectRunnable implements Runnable {


  SqlSession sqlSession;

  long startTime;


  public SelectRunnable(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
    this.startTime = System.currentTimeMillis();
  }

  public void run() {
    Gson gson = new Gson();
    try {
      for (int i = 0; i < 10000; i++) {
        BusinessMapper mapper = sqlSession.getMapper(BusinessMapper.class);

        mapper.selectBusiness(1);
        // System.out.println(gson.toJson(mapper.selectBusiness(1)));
        // do work
      }
    } finally {
      System.out.println("cost:" + (System.currentTimeMillis() - startTime));
      sqlSession.close();
    }
  }
}
