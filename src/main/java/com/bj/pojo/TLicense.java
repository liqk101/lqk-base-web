package com.bj.pojo;

import java.sql.Date;

public class TLicense {
	private Integer id;
	private String version;
	private String username;
	private Integer userType;
	private String userCode;
	private String licType;
	private Integer licNum;
	private Integer maxRegTimes;
	private Date expiryDate;
	private String aesKey;
	private String aesEncode;
	private String rsaPrivateKey;
	private String rsaPublicKey;
	
	private int regDeviceNum;
	
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
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
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
	 * @return the userType
	 */
	public Integer getUserType() {
		return userType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}
	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	/**
	 * @return the licType
	 */
	public String getLicType() {
		return licType;
	}
	/**
	 * @param licType the licType to set
	 */
	public void setLicType(String licType) {
		this.licType = licType;
	}
	/**
	 * @return the licNum
	 */
	public Integer getLicNum() {
		return licNum;
	}
	/**
	 * @param licNum the licNum to set
	 */
	public void setLicNum(Integer licNum) {
		this.licNum = licNum;
	}
	/**
	 * @return the maxRegTimes
	 */
	public Integer getMaxRegTimes() {
		return maxRegTimes;
	}
	/**
	 * @param maxRegTimes the maxRegTimes to set
	 */
	public void setMaxRegTimes(Integer maxRegTimes) {
		this.maxRegTimes = maxRegTimes;
	}
	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}
	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	/**
	 * @return the aesKey
	 */
	public String getAesKey() {
		return aesKey;
	}
	/**
	 * @param aesKey the aesKey to set
	 */
	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}
	/**
	 * @return the aesEncode
	 */
	public String getAesEncode() {
		return aesEncode;
	}
	/**
	 * @param aesEncode the aesEncode to set
	 */
	public void setAesEncode(String aesEncode) {
		this.aesEncode = aesEncode;
	}
	/**
	 * @return the rsaPrivateKey
	 */
	public String getRsaPrivateKey() {
		return rsaPrivateKey;
	}
	/**
	 * @param rsaPrivateKey the rsaPrivateKey to set
	 */
	public void setRsaPrivateKey(String rsaPrivateKey) {
		this.rsaPrivateKey = rsaPrivateKey;
	}
	/**
	 * @return the rsaPublicKey
	 */
	public String getRsaPublicKey() {
		return rsaPublicKey;
	}
	/**
	 * @param rsaPublicKey the rsaPublicKey to set
	 */
	public void setRsaPublicKey(String rsaPublicKey) {
		this.rsaPublicKey = rsaPublicKey;
	}
	/**
	 * @return the regDeviceNum
	 */
	public int getRegDeviceNum() {
		return regDeviceNum;
	}
	/**
	 * @param regDeviceNum the regDeviceNum to set
	 */
	public void setRegDeviceNum(int regDeviceNum) {
		this.regDeviceNum = regDeviceNum;
	}
	
}
