package com.bj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bj.pojo.TLicense;

/**
 * Created by LQK on 2018-5-25.
 */
@Mapper
public interface TLicenseMapper {	
    @Select("SELECT * FROM t_license where aes_encode=#{aesEncode}")
    @Results(value = {
            @Result(property = "userType", column = "user_type"),
            @Result(property = "userCode", column = "user_code"),
            @Result(property = "licType", column = "lic_type"),
            @Result(property = "licNum", column = "lic_num"),
            @Result(property = "maxRegTimes", column = "max_reg_times"),
            @Result(property = "expiryDate", column = "expiry_date"),
            @Result(property = "aesKey", column = "aes_key"),
            @Result(property = "aesEncode", column = "aes_encode"),
            @Result(property = "rsaPrivateKey", column = "rsa_private_key"),
            @Result(property = "rsaPublicKey", column = "rsa_public_key")})
    TLicense findByAesEncode(@Param("aesEncode") String aesEncode);
    
    @Select("SELECT * FROM t_license where user_code=#{userCode}")
    @Results(value = {
            @Result(property = "userType", column = "user_type"),
            @Result(property = "userCode", column = "user_code"),
            @Result(property = "licType", column = "lic_type"),
            @Result(property = "licNum", column = "lic_num"),
            @Result(property = "maxRegTimes", column = "max_reg_times"),
            @Result(property = "expiryDate", column = "expiry_date"),
            @Result(property = "aesKey", column = "aes_key"),
            @Result(property = "aesEncode", column = "aes_encode"),
            @Result(property = "rsaPrivateKey", column = "rsa_private_key"),
            @Result(property = "rsaPublicKey", column = "rsa_public_key")})
    TLicense findByUserCode(@Param("userCode") String userCode);
    
    @Select("SELECT * FROM t_license where username=#{userName}")
    @Results(value = {
            @Result(property = "userType", column = "user_type"),
            @Result(property = "userCode", column = "user_code"),
            @Result(property = "licType", column = "lic_type"),
            @Result(property = "licNum", column = "lic_num"),
            @Result(property = "maxRegTimes", column = "max_reg_times"),
            @Result(property = "expiryDate", column = "expiry_date"),
            @Result(property = "aesKey", column = "aes_key"),
            @Result(property = "aesEncode", column = "aes_encode"),
            @Result(property = "rsaPrivateKey", column = "rsa_private_key"),
            @Result(property = "rsaPublicKey", column = "rsa_public_key")})
    TLicense findByUserName(@Param("userName") String userName);
    
    @Select("SELECT * FROM t_license where id=#{id}")
    @Results(value = {
            @Result(property = "userType", column = "user_type"),
            @Result(property = "userCode", column = "user_code"),
            @Result(property = "licType", column = "lic_type"),
            @Result(property = "licNum", column = "lic_num"),
            @Result(property = "maxRegTimes", column = "max_reg_times"),
            @Result(property = "expiryDate", column = "expiry_date"),
            @Result(property = "aesKey", column = "aes_key"),
            @Result(property = "aesEncode", column = "aes_encode"),
            @Result(property = "rsaPrivateKey", column = "rsa_private_key"),
            @Result(property = "rsaPublicKey", column = "rsa_public_key")})
    TLicense findById(@Param("id") int id);
    
    @Select("SELECT * FROM t_license t where t.user_type = #{userType} and" +
    		" (t.version like '%${filter}%' or" + 
    		" t.username like '%${filter}%')" + 
    		" order by id desc LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "userType", column = "user_type"),
            @Result(property = "userCode", column = "user_code"),
            @Result(property = "licType", column = "lic_type"),
            @Result(property = "licNum", column = "lic_num"),
            @Result(property = "maxRegTimes", column = "max_reg_times"),
            @Result(property = "expiryDate", column = "expiry_date"),
            @Result(property = "aesKey", column = "aes_key"),
            @Result(property = "aesEncode", column = "aes_encode"),
            @Result(property = "rsaPrivateKey", column = "rsa_private_key"),
            @Result(property = "rsaPublicKey", column = "rsa_public_key")})
    List<TLicense> findAll(@Param("filter") String filter, @Param("userType") String userType, @Param("offset") int offset, @Param("rowCount") int rowCount);

    @Select("SELECT count(1) FROM t_license t where t.user_type = #{userType} and" +
    		" (t.version like '%${filter}%' or" + 
    		" t.username like '%${filter}%')")
    int countAll(@Param("filter") String filter, @Param("userType") String userType);
    
    @Update("UPDATE t_license t set" +
    		"   t.version = #{version}," +
    		"   t.username = #{username}," +
    		"   t.user_type = #{userType}," +
    		"   t.lic_type = #{licType}," +
    		"   t.lic_num = #{licNum}," +
    		"   t.max_reg_times = #{maxRegTimes}," +
    		"   t.expiry_date = #{expiryDate}," +
    		"   t.aes_key = #{aesKey}," +
    		"   t.aes_encode = #{aesEncode}," +
    		"   t.rsa_private_key = #{rsaPrivateKey}," +
    		"   t.rsa_public_key = #{rsaPublicKey}" +
            "   where id=#{id}")
	int update(TLicense TLicense);

    @Insert("INSERT INTO t_license " +
            "   (version, username, user_type, user_code, lic_type, lic_num, max_reg_times, expiry_date, aes_key, aes_encode, rsa_private_key, rsa_public_key) " +
            "VALUES " +
            "   (#{version}, #{username}, #{userType}, #{userCode}, #{licType}, #{licNum}, #{maxRegTimes}, #{expiryDate}, #{aesKey}, #{aesEncode}, #{rsaPrivateKey}, #{rsaPublicKey})")
    @Options(useGeneratedKeys=true,keyColumn="id")
    int insert(TLicense tLicense);
    
    @Delete("DELETE FROM t_license where id=#{id}")
    int delete(int id);
}
