package com.bj.pojo;

import java.util.List;

import net.sf.json.JSONArray;

public class TUser {
	private Integer id;
	private String username;
	private String password;
	private String password2;
	private String tel;
	private String email;
	private String desc;
	private List<String> tMCodes;
	
	private Integer[] roleIds;
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the password2
	 */
	public String getPassword2() {
		return password2;
	}
	/**
	 * @param password2 the password2 to set
	 */
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the tMCodes
	 */
	public List<String> gettMCodes() {
		return tMCodes;
	}
	/**
	 * @param tMCodes the tMCodes to set
	 */
	public void settMCodes(List<String> tMCodes) {
		this.tMCodes = tMCodes;
	}
	/**
	 * @return the roleIds
	 */
	public Integer[] getRoleIds() {
		return roleIds;
	}
	/**
	 * @param roleIds the roleIds to set
	 */
	public void setRoleIds(Integer[] roleIds) {
		this.roleIds = roleIds;
	}
	/**
	 * @return the roleIds
	 */
	public String getRoleIdsJson() {
		JSONArray obj = JSONArray.fromObject(this.roleIds);
		return obj.toString();
	}



}
