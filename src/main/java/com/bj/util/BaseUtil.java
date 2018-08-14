/**
 * 
 */
package com.bj.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LQK
 *
 */
public class BaseUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseUtil.class);

	/**
	 * 生成32位md5码
	 * 
	 * @param password
	 * @return
	 */
	public static String md5(String password) {
		try {
			// 得到一个信息摘要器
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			// 把每一个byte 做一个与运算 0xff;
			for (byte b : result) {
				// 与运算
				int number = b & 0xff;// 加盐
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					buffer.append("0");
				}
				buffer.append(str);
			}
			// 标准的md5加密后的结果
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
        	LOGGER.error(e.getMessage(),e);
			return "";
		}
	}
	
	/**
	 * 生成随机数字和字母,  
	 * @param length
	 * @return
	 */
    public static String getStrRandom(int length) {
        String val = "";  
        Random random = new Random(); 
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val.toUpperCase();  
    }  
    
    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
    	if(str != null && str.trim().length() > 0) {
    		return false;
    	}else {
    		return true;
    	}
    }
	
    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String format(Date date) {
    	return format(date, "yyyy-MM-dd HH:mm:ss");
    }
	
    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String format(Date date, String format) {
    	if(date != null) {
    		SimpleDateFormat df = new SimpleDateFormat(format);
    		return df.format(date);
    	}
    	return "";
    }
    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static java.sql.Date format(String date, String format) {
    	if(date != null) {
    		SimpleDateFormat df = new SimpleDateFormat(format);
    		try {
    			return new java.sql.Date(df.parse(date).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    	return null;
    }

    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String format(java.sql.Date date, String foramt) {
    	if(date != null) {
    		SimpleDateFormat df = new SimpleDateFormat(foramt);
    		return df.format(date);
    	}
    	return "";
    }
    
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println(md5("1"));
	}
}
