package com.hanson.test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class UtilTest {

	@Test
	public void test1(){
		List<String> list = new ArrayList(){
			{
				add("111");
				add("222");
				add("333");
				add("444");
			}
		};
		List<String> list_sub = list.subList(0, 2);
		for(String str : list_sub){
			System.out.println(str);
		}
	}
	
	@Test
	public void test2(){
		BigDecimal aa = new BigDecimal(1.25);
		System.out.println(aa);
	}
	
	@Test
	public void test3() {
		long youNumber = 11;
		// 0 代表前面补充0
		// 4 代表长度为4
		// d 代表参数为正数型
		String str = String.format("%04d", youNumber);
		System.out.println(str); // 0001
		youNumber = 111111;
		System.out.println(String.format("%04d", youNumber));
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		System.out.println(df.format(date));
	}
	
	@Test
	public void test4(){
		Random rand = new Random();
		System.out.println(rand.nextInt(2));
	}
}
