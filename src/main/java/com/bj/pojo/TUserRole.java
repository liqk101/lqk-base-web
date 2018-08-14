package com.bj.pojo;

import java.io.Serializable;


/**
 * The persistent class for the t_user_role database table.
 * 
 */
public class TUserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId;
	private int roleId;

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}