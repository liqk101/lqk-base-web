/**
 * 
 */
package com.bj.util;

import java.util.Base64;

/**
 * @author LQK
 *
 */
public class Base64Util {
    //Base64
	private static final Base64.Decoder decoder = Base64.getDecoder();
	private static final Base64.Encoder encoder = Base64.getEncoder();
	
	/**
	 * base64 加密
	 */
	public static String encodeToString(byte[] src) {
		return encoder.encodeToString(src);
	}

	/**
	 * base64 解密
	 */
	public static byte[] decode(String src) {
		return decoder.decode(src);
	}
	
	/**
	 * {
	"msg1" : "MDAwMDAwMDA=",
	"msg2" : "rSPvtVv5k0R9XP6DSoNRZqoffQxi9Cwwo0gPOYFw4MhstD1TTdgHwqvzBgPTUOXUoizJc5wynDzniilCLbCv67X4dDmByloB3j849ottmXxQ55xxtSvPqz4YYRLa8qOU0L5thiUI+FgU9AH7+tDi/VDvYlQfK7oY8gXvoE9SnB8=",
	"msg3" : "Wq2XMkp6beLKsaivOZCC7ZjM/DRnARe3vyP8gAuaiLUrx1KOAPZrtxR86d+xE1xSIac/Khs0g8JMmuBSe/QgEHEsUYp/arwgsNncwXn+JOcgzjEXLu1xu+UMSaduxHeMWoLhnWhOL69CWz3+HrVCWQCnseANFzSwWCCJFrz1FtY=",
	"msg4" : "FfOT5hQ/ScbrW6niL3negw=="
}
	 * @param args
	 */
	public static void main(String[] args) {
		String message = "MDAwMDAw";
		String ss = new String(decode(message));
		System.out.println(ss);
	}
}
