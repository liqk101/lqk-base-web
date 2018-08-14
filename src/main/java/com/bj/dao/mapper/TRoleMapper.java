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

import com.bj.pojo.TMenu;
import com.bj.pojo.TRole;
import com.bj.pojo.TRoleMenu;

/**
 * Created by LQK on 2018-5-25.
 */
@Mapper
public interface TRoleMapper {	
	@Select("SELECT * FROM t_role where id=#{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
    		@Result(property = "tMenus", javaType = List.class, column = "id", many = @Many(select = "com.bj.dao.mapper.TRoleMapper.findMenusByRoleId"))})
	TRole findById(@Param("id") int id);
	
    @Select("select a.* from t_menu a left join t_role_menu b on a.id=b.menu_id where b.role_id=#{roleId}")
    List<TMenu> findMenusByRoleId(@Param("roleId") String roleId);
	
    @Select("SELECT * FROM t_role order by id desc " +
            "LIMIT #{offset}, #{rowCount} ")
    List<TRole> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);

    @Select("SELECT count(1) FROM t_role")
    int countAll();
    
    @Update("UPDATE t_role t set t.name = #{name} where t.id = #{id}")
	int update(TRole tRole);

    @Insert("INSERT INTO t_role (`name`) VALUES (#{name})")
    @Options(useGeneratedKeys=true,keyColumn="id")
    int insert(TRole tRole);

    @Delete("DELETE FROM t_role where id=#{id}")
    int delete(int id);
    
    @Select("SELECT * FROM t_menu order by id")
    @Results(value = {
            @Result(property = "parentId", column = "parent_id")})
    List<TMenu> findAllMenu();

    @Select("SELECT * FROM t_menu where parent_id=#{pId} order by id ")
    List<TMenu> findMenuByParentId(@Param("pId") int pId);

    @Insert("INSERT INTO t_role_menu (role_id, menu_id) VALUES (#{roleId}, #{menuId})")
    int insertTRM(TRoleMenu trm);

    @Select("SELECT * FROM t_role_menu where parent_id=#{pId} order by id ")
    List<TMenu> findMenuByRoleId(@Param("pId") int pId);
    
    @Delete("DELETE FROM t_role_menu where role_id=#{roleId}")
    int deleteTRM(int roleId);
}
