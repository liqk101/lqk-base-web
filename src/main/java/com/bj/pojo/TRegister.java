package com.bj.pojo;

import java.util.Date;

public class TRegister {
	private Integer id;
	private Integer status;
	private Date createTime;
	private Date updateTime;
	private String mac;
	private String cpuId;
	private TLicense tLicense;
	private Integer regTimes;
	private String remoteIP;
	
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
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the regTimes
	 */
	public Integer getRegTimes() {
		return regTimes;
	}
	/**
	 * @param regTimes the regTimes to set
	 */
	public void setRegTimes(Integer regTimes) {
		this.regTimes = regTimes;
	}
	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}
	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}
	/**
	 * @return the cpuId
	 */
	public String getCpuId() {
		return cpuId;
	}
	/**
	 * @param cpuId the cpuId to set
	 */
	public void setCpuId(String cpuId) {
		this.cpuId = cpuId;
	}
	/**
	 * @return the tLicense
	 */
	public TLicense gettLicense() {
		return tLicense;
	}
	/**
	 * @param tLicense the tLicense to set
	 */
	public void settLicense(TLicense tLicense) {
		this.tLicense = tLicense;
	}
	/**
	 * @return the remoteIP
	 */
	public String getRemoteIP() {
		return remoteIP;
	}
	/**
	 * @param remoteIP the remoteIP to set
	 */
	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}
}
