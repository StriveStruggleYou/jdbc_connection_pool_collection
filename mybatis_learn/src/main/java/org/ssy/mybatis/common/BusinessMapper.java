package org.ssy.mybatis.common;

import org.apache.ibatis.annotations.Select;

/**
 * Created by manager on 2018/6/12.
 */
public interface BusinessMapper {

  @Select("SELECT * FROM business WHERE bus_id = #{id}")
  Business selectBusiness(int id);


  @Select("SELECT * FROM business WHERE bus_name = #{busName}")
  Business selectBusinessName(String busName);

  @Select("SELECT * FROM business WHERE bus_name = #{busName}")
  Business selectBusinessObject(String busName);


}
