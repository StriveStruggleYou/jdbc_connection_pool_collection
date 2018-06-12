package org.ssy.compare.common;

import org.apache.ibatis.annotations.Select;
import org.ssy.compare.common.Business;

/**
 * Created by manager on 2018/6/12.
 */
public interface BusinessMapper {

    @Select("SELECT * FROM business WHERE bus_id = #{id}")
    Business selectBusiness(int id);



}
