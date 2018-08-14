package com.bj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bj.pojo.TUser;
import com.bj.pojo.TUserRole;

/**
 * Created by LQK on 2018-5-25.
 */
@Mapper
public interface TUserMapper {	
    @Select("SELECT * FROM t_user order by id desc " +
            "LIMIT #{offset}, #{rowCount} ")
    List<TUser> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);

    @Select("SELECT count(1) FROM t_user")
    int countAll();
    
	@Select("SELECT * FROM t_user where username=#{username}")
    @Results(value = {
    		@Result(property = "tMCodes", javaType = List.class, column = "id", many = @Many(select = "com.bj.dao.mapper.TUserMapper.findMenusByUserId"))})
    TUser findByUsername(@Param("username") String username);
	
    @Select("select a.code from t_menu a left join t_role_menu b on a.id=b.menu_id left join t_user_role c on b.role_id=c.role_id where c.user_id=#{userId}")
    List<String> findMenusByUserId(@Param("userId") String userId);
    
	@Select("SELECT * FROM t_user where id=#{id}")
    TUser findById(@Param("id") int id);
    
	@Select("SELECT * FROM t_user where username=#{username} and username != #{excludeName}")
    TUser findByUsernameAndExclude(@Param("username") String username, @Param("excludeName") String excludeName);

    @Update("<script>" + 
    		"UPDATE t_user t set " +
    		"   <if test=\"password != null\"> t.password = #{password}, </if>" +
    		"   t.tel = #{tel}, " +
    		"   t.email = #{email}, " +
    		"   t.desc = #{desc} " +
    		"   where t.id = #{id}" +
    		"</script>")
	int update(TUser tUser);

    @Insert("INSERT INTO t_user " +
            "   (username,password,tel,email,`desc`) " +
            "VALUES " +
            "   (#{username}, #{password}, #{tel}, #{email}, #{desc})")
    @Options(useGeneratedKeys=true,keyColumn="id")
    int insert(TUser tUser);

    @Delete("DELETE FROM t_user where id=#{id}")
    int delete(int id);

    
    @Select("select a.role_id from t_user_role a where a.user_id=#{userId}")
    Integer[] findRolesByUserId(@Param("userId") int userId);

    @Insert("INSERT INTO t_user_role " +
            "   (user_id, role_id) " +
            "VALUES " +
            "   (#{userId}, #{roleId})")
    int insertUR(TUserRole ur);

    @Delete("DELETE FROM t_user_role where user_id=#{userId}")
    int deleteUR(int userId);
}
