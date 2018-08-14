package com.bj.pojo;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the t_menu database table.
 * 
 */
public class TMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;

	private String code;

	private String name;

	private int parentId;

	private List<TMenu> tMenus;

	public TMenu() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the tMenus
	 */
	public List<TMenu> gettMenus() {
		return tMenus;
	}

	/**
	 * @param tMenus the tMenus to set
	 */
	public void settMenus(List<TMenu> tMenus) {
		this.tMenus = tMenus;
	}

}