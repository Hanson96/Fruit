package com.hanson.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
}
