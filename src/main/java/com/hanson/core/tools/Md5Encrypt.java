package com.hanson.core.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @类名称: Md5Encrypt
 * @类描述: 对密码进行加密和验证
 */
public class Md5Encrypt {

	/**
	 * @Description: 用MD5算法加密字符串
	 * @param text 要加密的字符串
	 * @return
	 */
	public static String md5(String text) {

		MessageDigest msgDigest = null;
		try { // 获得支持MD5算法的MessageDigest
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("System doesn't support MD5 algorithm.");
		}
		try {// 获取字符串的utf-8编码的字节，并加密
			msgDigest.update(text.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("System doesn't support your  EncodingException.");
		}

		byte[] bytes = msgDigest.digest();// 获取加密后的字节

		String md5Str = new String(encodeHex(bytes));// 根据加密字节获取加密后的字符串

		return md5Str;// 返回加密后的字符串
	}

	/**
	 * @Description: 将加密后的字符串字节数组转化为字符数组 将加密后的字符串字节数组与16进制进行相关运算
	 * @param data
	 * @return
	 */
	public static char[] encodeHex(byte[] data) {
		int l = data.length;

		char[] out = new char[l << 1];

		int i = 0;
		for (int j = 0; i < l; i++) {
			out[(j++)] = DIGITS[((0xF0 & data[i]) >>> 4)];
			out[(j++)] = DIGITS[(0xF & data[i])];
		}

		return out;
	}

	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

}
