package org.ssy.mybatis;

/**
 * Created by manager on 2018/6/24.
 */
public class LearnMybatisInterceptor {

  private static LearnMybatisInterceptor ourInstance = new LearnMybatisInterceptor();

  public static LearnMybatisInterceptor getInstance() {
    return ourInstance;
  }

  private LearnMybatisInterceptor() {
  }
}
