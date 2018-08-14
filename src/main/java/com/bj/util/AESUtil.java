package com.bj.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * AES工具类 
 * @author LQK
 */  
public class AESUtil {  
	private static final Logger LOGGER = LoggerFactory.getLogger(AESUtil.class);
	
    private static String transformation = "AES/ECB/NoPadding";
    private static String algorithm = "AES";

    /** 
     * 加密 
     * @return base46
     */  
	public static String encrypt(String content, String key){
		try {
        	LOGGER.info("AES加密前：{}", content);
			//创建AES加密实例  
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), algorithm);    
			Cipher cip = Cipher.getInstance(transformation);//算法/模式/补码方式  
			cip.init(Cipher.ENCRYPT_MODE,skeySpec);    
			//分块加密  
			int black= (content.length()-1)/16+1;  
			byte[] out = new byte[black * 16];  
			int b = 0;  
			while(b < black){
			    //计算偏移  
			    int offset = b * 16;  
			    //计算剩余长度  
			    int len = (content.length()-16-offset) > 0 ? 16:(content.length() - offset);  
			    byte[] input = new byte[16];  
			    System.arraycopy(content.getBytes(), offset, input, 0, len);  
			    byte[] output=cip.doFinal(input);  
			    System.arraycopy(output, 0, out,offset, 16);  
			    b++;  
			}
			String resultStr = Base64Util.encodeToString(out);
			LOGGER.info("AES加密后：{}", resultStr);
			return resultStr;
	    } catch (Exception e) {
	    	LOGGER.error("AES加密失败：{}",e);
	    }
	    return null;  
	}
	
    /** 
     * AES 解密 
     */  
    public static String decrypt(String base64_content, String key) {  
        try {
        	LOGGER.info("AES解密前：{}", base64_content);
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algorithm);  
            Cipher cipher = Cipher.getInstance(transformation);  
            cipher.init(Cipher.DECRYPT_MODE, skey);// 初始化  
            byte[] result = cipher.doFinal(Base64Util.decode(base64_content));  
            String resultStr = new String(result).trim(); // 解密
        	LOGGER.info("AES解密后：{}", resultStr);
            return resultStr;
        } catch (Exception e) {
        	LOGGER.error("AES解密失败：{}",e);
        }
        return null;
    }
  
    public static void main(String[] args) throws Exception {
    	/**
    	 * {
        "msg1" : "VUc5S1c1QzE=",
        "msg2" : "fc7MBJaKU+g0tAuDu9ef0JegTnoqgQzreyk+Lhh0Pm8XLAqDTdvPKcJHZgaMIy/HY7cYj+scgn5P5k+V2fqsGeqomxrWEdNF2DsTO1zWtaLttKAavmjYLZFmz7a1zZ4Ei8UAhD9n7TnK3j4bGnjoxl+EsSvnNFLMu0CugpBf1js=",
        "msg3" : "W52RnBbm9KVnp5Btve8pJPLSO9U7zRNp/VXUEq/QEnzzV1CjI6FTV80eM5Ww1iIDJ53NuWQKQFRMBPt+Fzgj673eD13vlq7zLijyeMMtjdBJDbPCOSF82ZweFkYurATXg2r737QIj8Dv9xvmR6r92bP9CWu1fgjDjboa0kU4bQc=",
        "msg4" : "9mDSezbJ7V9FsMRJEeEzRvDmO+tlxda5on8whcJbZ1o="
}

    	 */
    	String key = "9462492754199980";
        String s = "10229AX96VPN0110"; //yDDla0S+RXz7k63ypHFL6w==
        String kk = encrypt(s,key); 
        String rr = decrypt(kk,key);
    	LOGGER.info("res：{}", new String(Base64Util.decode("9mDSezbJ7V9FsMRJEeEzRvDmO+tlxda5on8whcJbZ1o=")));
    	
    	
    }  
}  
