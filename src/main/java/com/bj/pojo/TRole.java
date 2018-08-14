package com.bj.pojo;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the t_role database table.
 * 
 */
public class TRole implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String name;

	private List<TMenu> tMenus;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TMenu> gettMenus() {
		return tMenus;
	}

	public void settMenus(List<TMenu> tMenus) {
		this.tMenus = tMenus;
	}



}