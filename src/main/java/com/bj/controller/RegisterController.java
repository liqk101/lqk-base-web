/**
 * 
 */
package com.bj.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bj.dao.mapper.TLicenseMapper;
import com.bj.dao.mapper.TRegisterMapper;
import com.bj.dao.mapper.TSystemParamMapper;
import com.bj.job.QueryAreaJob;
import com.bj.pojo.Message;
import com.bj.pojo.TLicense;
import com.bj.pojo.TRegister;
import com.bj.util.AESUtil;
import com.bj.util.Base64Util;
import com.bj.util.BaseUtil;
import com.bj.util.Contants;
import com.bj.util.RSAUtil;

import net.sf.json.JSONObject;

/**
 * @author LQK
 *
 */
@Controller
public class RegisterController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);
	
	@Resource
	private TRegisterMapper tRegisterMapper;
	@Resource
	private TLicenseMapper tLicenseMapper;
	@Resource
	private TSystemParamMapper tSystemParamMapper;
    @Resource
    private QueryAreaJob queryAreaJob;
	
    @Value("${baidu.api.url}")
    private String apiUrl;
    
    /**
	 * V1
	 * @param message
	 * 客户端发送
		{
		"msg1" : "usercode base64”
		"msg2" : "mac rsa base64"
		"msg3" : "hard aes rsa base64"
		"msg4" : "productid aes base64”
		}
	 * @return
	 *		600 未授权
			601 授权码无效
			602 授权码已被注册
			603 授权码已达到最大用户数
			604 已经注册过了
			605 授权码已过期
			999 系统错误
	 * @throws Exception
	 */
    @Transactional
    @PostMapping("/v1/register")
    public @ResponseBody String v1Register(HttpServletRequest request, @RequestParam(value = "message") String message) throws Exception {
    	String result = "";
    	try {
    		String remoteIP = getRemoteIP(request);
	    	LOGGER.info("收到[{}]注册请求：{}", remoteIP, message);
	    	String msg = new String(Base64Util.decode(message));
	    	LOGGER.info("Base64解密后：{}", msg);
	    	JSONObject obj = JSONObject.fromObject(msg);
	    	Message reqMsg = (Message)JSONObject.toBean(obj,Message.class);
	    	String userCode = new String(Base64Util.decode(reqMsg.getMsg1()));
	    	String productId = reqMsg.getMsg4();
	    	LOGGER.info("userCode={}, productId={}", userCode, productId);
	    	
	    	int userType = 1;
	    	String mac = "";
	    	String cpuId = "";
	    	TLicense tLicense = null;
	    	//检查授权码是否有效
	    	if(tSystemParamMapper.findByKey(Contants.PERSON_USER_CODE_NAME).equals(userCode)) { 
	    		//个人
	    		userType = 1;
	    		String privateKey = tSystemParamMapper.findByKey(Contants.PERSON_RSA_PRIVATE_KEY_NAME);
	    		String publicKey = tSystemParamMapper.findByKey(Contants.PERSON_RSA_PUBLIC_KEY_NAME);
	    		String aesKey = tSystemParamMapper.findByKey(Contants.PERSON_AES_KEY_NAME);
		    	mac = new String(RSAUtil.decrypt(Base64Util.decode(reqMsg.getMsg2()), privateKey));
		    	cpuId = AESUtil.decrypt(Base64Util.encodeToString(RSAUtil.decrypt(Base64Util.decode(reqMsg.getMsg3()), privateKey)), aesKey);
		    	LOGGER.info("MAC={},HardInfo={}", mac, cpuId);
		    	String username = mac + "||" + cpuId;
		    	tLicense = tLicenseMapper.findByUserName(username);
		    	if(tLicense == null) {
		    		String expiryDate = tSystemParamMapper.findByKey(Contants.PERSON_EXPIRY_DATE_NAME);
			    	//version(3),usertype(1),userCode(8),licType(2),licNum
			    	String productIdMW = AESUtil.decrypt(reqMsg.getMsg4(), aesKey);
			    	LOGGER.info("productId_MW={}", productIdMW);
			    	String version = productIdMW.substring(0, 3);
			    	String licType = productIdMW.substring(12, 14);
		    		tLicense = new TLicense();
		    		tLicense.setVersion(version);
		    		tLicense.setUserType(1);
		    		tLicense.setUsername(username);
		    		tLicense.setUserCode(userCode);
		    		tLicense.setRsaPublicKey(publicKey);
		    		tLicense.setRsaPrivateKey(privateKey);
		    		tLicense.setRegDeviceNum(0);
		    		tLicense.setMaxRegTimes(0);
		    		tLicense.setLicType(licType);
		    		tLicense.setLicNum(0);
		    		tLicense.setExpiryDate(BaseUtil.format(expiryDate, "yyyy-MM-dd"));
		    		tLicense.setAesKey(aesKey);
		    		tLicense.setAesEncode(productId);
		    		tLicenseMapper.insert(tLicense);
		    	}
	    	}else {
	    		//企业、试用
		    	tLicense = tLicenseMapper.findByAesEncode(productId);
		    	if(tLicense == null) {
			    	LOGGER.warn("[601]授权码不存在：{}",msg);
		        	return Base64Util.encodeToString("{\"error\":\"601\"}".getBytes());
		    	}else {
		    		//检查是否在有效期
		    		userType = tLicense.getUserType();
		    		int expiryInt = Integer.parseInt(BaseUtil.format(tLicense.getExpiryDate(),"yyyyMMdd"));
		    		int nowInt = Integer.parseInt(BaseUtil.format(new Date(),"yyyyMMdd"));
		    		if(nowInt > expiryInt) {
				    	LOGGER.warn("[605]授权码已过期[{}]：{}", expiryInt, msg);
			        	return Base64Util.encodeToString("{\"error\":\"605\"}".getBytes());
		    		}
			    	mac = new String(RSAUtil.decrypt(Base64Util.decode(reqMsg.getMsg2()), tLicense.getRsaPrivateKey()));
			    	cpuId = AESUtil.decrypt(Base64Util.encodeToString(RSAUtil.decrypt(Base64Util.decode(reqMsg.getMsg3()), tLicense.getRsaPrivateKey())), tLicense.getAesKey());
		    	}
	    	}
	    	
	    	//检查此设备是否注册过
	    	List<TRegister> list = tRegisterMapper.findByMacAndCpuId(mac, cpuId);
	    	if(list.size() <= 0) {
	    		int regNum = tRegisterMapper.countByLicId(tLicense.getId());
	    		if(regNum >= tLicense.getLicNum() && tLicense.getLicNum() != 0) {
			    	LOGGER.warn("[603]该授权码设备注册数量已达上限，[已注册：{}, 最大授权数：{}]", regNum, tLicense.getLicNum());
		        	return Base64Util.encodeToString("{\"error\":\"603\"}".getBytes());
	    		}else {
	    			Date now = new Date();
		    		TRegister tRegister = new TRegister();
		    		tRegister.setCpuId(cpuId);
		    		tRegister.setCreateTime(now);
		    		tRegister.setUpdateTime(now);
		    		tRegister.setMac(mac);
		    		tRegister.setStatus(1);
		    		tRegister.settLicense(tLicense);
		    		tRegister.setRegTimes(1);
		    		tRegister.setRemoteIP(remoteIP);
		    		tRegisterMapper.insert(tRegister);
	    		}
	    	}else {
	    		TRegister tRegister = list.get(0);
	    		int regTimes = tRegister.getRegTimes();
	    		if(regTimes >= tLicense.getMaxRegTimes() && tLicense.getMaxRegTimes() != 0) {
			    	LOGGER.warn("[604]该设备注册次数已达上限，[已注册：{}, 最大注册数：{}]", regTimes, tLicense.getMaxRegTimes());
		        	return Base64Util.encodeToString("{\"error\":\"604\"}".getBytes());
	    		}else {
		    		tRegister.setUpdateTime(new Date());
		    		tRegister.setRegTimes(regTimes + 1);
		    		tRegister.setRemoteIP(remoteIP);
		    		tRegisterMapper.update(tRegister);
	    		}
	    	}
	    	/**
	    	 * 服务器返回：
				{
				"msg1":  "mac rsa base64"
				"msg2":  "hard aes rsa base64"
				"msg3":  "deadline rsa base64”
				"msg4": “licType” 
				}
	    	 */
	    	Message rspMsg = new Message();
	    	rspMsg.setError("0");
	    	rspMsg.setMsg1(reqMsg.getMsg2());
	    	rspMsg.setMsg2(reqMsg.getMsg3());
	    	String expiryDate = BaseUtil.format(tLicense.getExpiryDate(), "yyyyMMdd");
	    	rspMsg.setMsg3(Base64Util.encodeToString(RSAUtil.encrypt(expiryDate.getBytes(), tLicense.getRsaPublicKey())));
	    	rspMsg.setMsg4(tLicense.getLicType());
	    	String str = JSONObject.fromObject(rspMsg).toString();
	    	LOGGER.info("返回：{}", str);
	    	result = Base64Util.encodeToString(str.getBytes());
	    	LOGGER.info("加密返回：{}", result);
	    	registerLog(remoteIP, userType, mac+cpuId);
    	}catch (Exception e) {
    		LOGGER.error("[999]注册失败：{}", e);
        	return Base64Util.encodeToString("{\"error\":\"999\"}".getBytes());
		}
    	return result;
    }
    
    /**
     * 
     * @param ip
     * @param userType
     * @param macCpu
     */
    private void registerLog(String ip, int userType, String macCpu) {
		QueryAreaJob.BaiduApiJob job = queryAreaJob.new BaiduApiJob(apiUrl,ip,userType,macCpu);
		Thread t = new Thread(job);
		t.start();
    }
    
    /**
     * 
     * @param request
     * @return
     */
    private String getRemoteIP(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
    	LOGGER.info("客户端IP：{}", remoteAddr);
        return remoteAddr;
    }
}
