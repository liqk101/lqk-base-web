package com.bj.pojo;

import java.io.Serializable;


/**
 * The persistent class for the t_role_menu database table.
 * 
 */
public class TRoleMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	private int roleId;
	private int menuId;

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getMenuId() {
		return this.menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

}