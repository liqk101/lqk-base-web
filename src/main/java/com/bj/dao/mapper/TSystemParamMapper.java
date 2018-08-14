package com.bj.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by LQK on 2018-5-25.
 */
@Mapper
public interface TSystemParamMapper {	
	@Select("SELECT value FROM t_sys_param where `key` = #{key}")
	String findByKey(@Param("key") String key);

    @Update("UPDATE t_sys_param set" +
    		"   value = #{value} " +
            "   where `key` = #{key}")
	int updateValue(@Param("key") String key, @Param("value") String value);
}
