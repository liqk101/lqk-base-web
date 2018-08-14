package com.bj.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.bj.pojo.TRegisterLog;

/**
 * Created by LQK on 2018-6-30.
 */
@Mapper
public interface TRegisterLogMapper {	
    
    @Select("SELECT t.* FROM t_register_log t" +
    		" order by id desc LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "userType", column = "user_type"),
            @Result(property = "macCpu", column = "mac_cpu"),
            @Result(property = "createTime", column = "create_time")})
    List<TRegisterLog> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);

    @Select("SELECT count(1) FROM t_register_log t")
    int countAll();
    
    @Select("SELECT t.province,count(1) count FROM t_register_log t group by t.province ")
    @Results(value = {
            @Result(property = "userType", column = "user_type"),
            @Result(property = "macCpu", column = "mac_cpu"),
            @Result(property = "createTime", column = "create_time")})
    List<TRegisterLog> findGroupByProvince();
    
    @Select("SELECT t.city, count(1) count FROM t_register_log t where t.province = #{province} group by t.city")
    @Results(value = {
            @Result(property = "userType", column = "user_type"),
            @Result(property = "macCpu", column = "mac_cpu"),
            @Result(property = "createTime", column = "create_time")})
    List<TRegisterLog> findByProvince(@Param("province") String province);
    
    @Select("SELECT t.mac_cpu, count(1) count FROM t_register_log t where t.create_time>=#{sTime} and t.create_time<#{eTime} group by t.mac_cpu")
    @Results(value = {@Result(property = "macCpu", column = "mac_cpu")})
    List<TRegisterLog> countGroupByMacCpuByTime(@Param("sTime") Date sTime, @Param("eTime") Date eTime);
    
    @Select("SELECT count(1) FROM t_register_log t where t.create_time>=#{sTime} and t.create_time<#{eTime}")
    int countByTime(@Param("sTime") Date sTime, @Param("eTime") Date eTime);
    
    @Select("SELECT t.city,count(1) count,max(x) x, max(y) y FROM t_register_log t group by t.city ")
    @Results(value = {
            @Result(property = "userType", column = "user_type"),
            @Result(property = "macCpu", column = "mac_cpu"),
            @Result(property = "createTime", column = "create_time")})
    List<TRegisterLog> findGroupByCity();
    
    @Select("SELECT count(1) FROM t_register_log t where t.user_type=#{userType} ")
    int countByUserType(@Param("userType") int userType);

    @Select("SELECT t.* FROM t_register_log t where t.ip=#{ip} ")
    @Results(value = {
            @Result(property = "userType", column = "user_type"),
            @Result(property = "macCpu", column = "mac_cpu"),
            @Result(property = "createTime", column = "create_time")})
    TRegisterLog findByIp(@Param("ip") String ip);

    @Insert("INSERT INTO t_register_log " +
            "   (ip,user_type,nation,province,city,district,x,y,create_time,mac_cpu) " +
            "VALUES " +
            "   (#{ip}, #{userType}, #{nation}, #{province}, #{city}, #{district}, #{x}, #{y}, #{createTime}, #{macCpu})")
    @Options(useGeneratedKeys=true,keyColumn="id")
    int insert(TRegisterLog tRegisterLog);
}
