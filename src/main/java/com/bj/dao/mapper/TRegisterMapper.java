package com.bj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bj.pojo.TLicense;
import com.bj.pojo.TRegister;

/**
 * Created by LQK on 2018-5-25.
 */
@Mapper
public interface TRegisterMapper{
    @Select("SELECT * FROM t_register t where " +
    		" t.mac = #{mac} and t.cpu_id = #{cpuId}")
    @Results(value = {
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "cpuId", column = "cpu_id"),
            @Result(property = "regTimes", column = "reg_times"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "remoteIP", column = "remote_ip"),
            @Result(property = "tLicense", javaType = TLicense.class, column = "lic_id", one = @One(select = "com.bj.dao.mapper.TLicenseMapper.findById"))})
    List<TRegister> findByMacAndCpuId(@Param("mac") String mac, @Param("cpuId") String cpuId);
    
    @Select("SELECT t.* FROM t_register t left join t_license l on t.lic_id = l.id where l.user_type = ${userType} and" + 
    		" (t.mac like '%${filter}%' or t.cpu_id like '%${filter}%')" +
    		" order by id desc LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "cpuId", column = "cpu_id"),
            @Result(property = "regTimes", column = "reg_times"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "remoteIP", column = "remote_ip"),
            @Result(property = "tLicense", javaType = TLicense.class, column = "lic_id", one = @One(select = "com.bj.dao.mapper.TLicenseMapper.findById"))})
    List<TRegister> findAll(@Param("filter") String filter, @Param("userType") String userType, @Param("offset") int offset, @Param("rowCount") int rowCount);

    @Select("SELECT count(1) FROM t_register t left join t_license l on t.lic_id = l.id where l.user_type = ${userType} and" + 
    		" (t.mac like '%${filter}%' or t.cpu_id like '%${filter}%')")
    int countAll(@Param("filter") String filter, @Param("userType") String userType);
   
    @Update("UPDATE t_register t set" +
    		"   t.remote_ip = #{remoteIP}," +
    		"   t.update_time = #{updateTime}," +
    		"   t.reg_times = #{regTimes}" +
            "   where id=#{id}")
	int update(TRegister tRegister);

    @Insert("INSERT INTO t_register " +
            "   (lic_id, status, create_time, update_time, mac, cpu_id, reg_times, remote_ip) " +
            "VALUES " +
            "   (#{tLicense.id}, #{status}, #{createTime}, #{updateTime}, #{mac}, #{cpuId}, #{regTimes}, #{remoteIP})")
    @Options(useGeneratedKeys=true,keyColumn="id")
    int insert(TRegister tRegister);

    @Select("SELECT count(1)  FROM t_register t where t.lic_id = #{licId}")
    int countByLicId(@Param("licId") int licId);
}
