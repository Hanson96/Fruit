package com.hanson.test;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * 数据加密测试
 * @author hanson
 *
 */
public class EncryptTest {

	@Test
	public void md5(){
		String data = "123456";
		System.out.println(DigestUtils.md5Hex(data));
	}
	
	@Test
	public void sha1(){
		String data = "123456";
		System.out.println(DigestUtils.sha1Hex(data));
	}
	
	@Test
	public void base64(){
		String data = "123456";
		byte[] data_encode = Base64.encodeBase64(data.getBytes(), true);
		System.out.println(data_encode);
		
		byte[] b = Base64.decodeBase64(data_encode);
		System.out.println(new String(b));
	}
}
