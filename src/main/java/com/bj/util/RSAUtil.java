/**
 * 
 */
package com.bj.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LQK
 *
 */
public class RSAUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtil.class);	
    
	private static String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM4C8rxF4PNob7jUaxunu/HkwsmlaJqIdjKWA2+jdzTW2ykV9GJHE1bTwQiW+GLPbPf8JDWXAa7+ZPcdLtuorQfpCAgCZ90K+rsGSimdtlMHGCfYwLZcHqM4rTl943Wn3X5LgITObw5rO8XfY+fbXrKDjNNVQQaS4eh93vX7qkM9AgMBAAECgYBr1FE3SKAw54YqyUxywj32o9Vs9F4nHKw5WBneUkJv7tHx1GMBGdC+jsGn/FVwar/PoTNWtX6VGOTCD41aPhSqcH1I560UwxfERmE35DWwEoHULESwtBW/3YmXxI389jPqTcHbwljTbEiKZsfcZTAw3k+MaXndCUz0215pmNrMAQJBAP+lxu2kKut4cHpXib96tpa7RiyVvuk9LEz7oBNh4EgMsLS8cAPt3cXJA05I5nnPKtA+wNUSc9E1vsdyB6RR7D0CQQDOS6dSvIsGDI29107oRgrIscoU4YCBtMUCp1fNE/q76KaXGb55OrCPRNAy+zwIHQa82M9KJBduSh8gtc54/CMBAkBRfOwt77hmmltwA9s6l8j/vu4dZBYYjpFCcKqTww0UqkbhNXeXKQkZ9HsDRyWAClMhUf9xa5JTS4hmVqis7hgZAkAzogXQFHarXesAD+qlAhri3nTYwxQZ4rfIkT/NiWmhLHq9qOjeiMz2HuAJUo5U7YbpL066nMvsg8rEshxqdZkBAkEA/QdNJHCA75VfGPFDfzpUQEIipuH4M74zuiKOSF2ZH+OiuaITkgUwHgAPkphcuPh5lltKYTbdyo//d1ARXmckug==";
	private static String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDOAvK8ReDzaG+41Gsbp7vx5MLJpWiaiHYylgNvo3c01tspFfRiRxNW08EIlvhiz2z3/CQ1lwGu/mT3HS7bqK0H6QgIAmfdCvq7BkopnbZTBxgn2MC2XB6jOK05feN1p91+S4CEzm8OazvF32Pn216yg4zTVUEGkuHofd71+6pDPQIDAQAB";
	
    //公钥加密  
    public static byte[] encrypt(byte[] content, String publicKeyStr) throws Exception{
        PublicKey publicKey = getPublicKey(publicKeyStr);  
        Cipher cipher=Cipher.getInstance("RSA");//java默认"RSA"="RSA/ECB/PKCS1Padding"   RSA_PKCS1_PADDING
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
        return cipher.doFinal(content);  
    }  
      
    //私钥解密  
    public static byte[] decrypt(byte[] content, String privateKeyStr) throws Exception{
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher=Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
        return cipher.doFinal(content);  
    }  
	
    //将base64编码后的公钥字符串转成PublicKey实例  
    private static PublicKey getPublicKey(String publicKey) throws Exception{
        byte[] keyBytes = Base64.decodeBase64(publicKey.trim());
        X509EncodedKeySpec keySpec=new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");  
        return keyFactory.generatePublic(keySpec);
    }
      
    //将base64编码后的私钥字符串转成PrivateKey实例
    private static PrivateKey getPrivateKey(String privateKey) throws Exception{
        byte[] keyBytes = Base64.decodeBase64(privateKey.trim());
        PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");  
        return keyFactory.generatePrivate(keySpec);
    }
    
    public static void main(String[] args) throws Exception {
    	/*String ddd = "";
    	String data = "00010a002700000e00029900064325934000320201221000401";
        //公钥加密  
    	byte[] encryptedBytes = Base64Util.decode(ddd);
        LOGGER.info("BASE64解密后：{}", new String(encryptedBytes));  
        encrypt("AAABBB".getBytes(), publicKey);
        
        //私钥解密  
        byte[] decryptedBytes=decrypt(encrypt("AAABBB".getBytes(), "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDOAvK8ReDzaG+41Gsbp7vx5MLJpWiaiHYylgNvo3c01tspFfRiRxNW08EIlvhiz2z3/CQ1lwGu/mT3HS7bqK0H6QgIAmfdCvq7BkopnbZTBxgn2MC2XB6jOK05feN1p91+S4CEzm8OazvF32Pn216yg4zTVUEGkuHofd71+6pDPQIDAQAB"), "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM4C8rxF4PNob7jUaxunu/HkwsmlaJqIdjKWA2+jdzTW2ykV9GJHE1bTwQiW+GLPbPf8JDWXAa7+ZPcdLtuorQfpCAgCZ90K+rsGSimdtlMHGCfYwLZcHqM4rTl943Wn3X5LgITObw5rO8XfY+fbXrKDjNNVQQaS4eh93vX7qkM9AgMBAAECgYBr1FE3SKAw54YqyUxywj32o9Vs9F4nHKw5WBneUkJv7tHx1GMBGdC+jsGn/FVwar/PoTNWtX6VGOTCD41aPhSqcH1I560UwxfERmE35DWwEoHULESwtBW/3YmXxI389jPqTcHbwljTbEiKZsfcZTAw3k+MaXndCUz0215pmNrMAQJBAP+lxu2kKut4cHpXib96tpa7RiyVvuk9LEz7oBNh4EgMsLS8cAPt3cXJA05I5nnPKtA+wNUSc9E1vsdyB6RR7D0CQQDOS6dSvIsGDI29107oRgrIscoU4YCBtMUCp1fNE/q76KaXGb55OrCPRNAy+zwIHQa82M9KJBduSh8gtc54/CMBAkBRfOwt77hmmltwA9s6l8j/vu4dZBYYjpFCcKqTww0UqkbhNXeXKQkZ9HsDRyWAClMhUf9xa5JTS4hmVqis7hgZAkAzogXQFHarXesAD+qlAhri3nTYwxQZ4rfIkT/NiWmhLHq9qOjeiMz2HuAJUo5U7YbpL066nMvsg8rEshxqdZkBAkEA/QdNJHCA75VfGPFDfzpUQEIipuH4M74zuiKOSF2ZH+OiuaITkgUwHgAPkphcuPh5lltKYTbdyo//d1ARXmckug==");
        LOGGER.info("RSA解密后：{}", new String(decryptedBytes));*/
    	LOGGER.info("12345678".substring(3, 5));
    } 
}
