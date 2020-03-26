package com.kuake.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;

import org.junit.Test;

public class DemoTest {
	@Test
	public void test1(){
		System.out.println(ItemStatus.PACKING_HAS_USED.getUseStatus());
	}
	@Test
	public void test2(){
		String a = "liuzi,hao";
		int indexOf = a.indexOf(",");
		System.out.println(indexOf);
		String string = a.substring(indexOf, a.length());
		System.out.println(string);
	}
	@Test
	public void test3(){
		System.out.println("第一句");
		Cat cat = new  Cat();
	}
	@Test
	public void test4() throws ParseException{
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-mm-dd");
		Date date = dateFormat.parse("2018-01-01");
		
		System.out.println(date.getTime());
	}
	@Test
	public void test5() throws ParseException{
		int[]arr={1,2,3,4};
		int j=0;
		for (int i : arr) {
			System.out.print(j++);
			System.out.println(j);
		}
	}
	
	@Test
	public void test6() throws ParseException{
		Dog dog = new Dog();
		System.out.println(dog);
		}
	@Test
	public void test7() throws ParseException{
		String user="";
		System.out.println("".equals(""));
		}
	@Test
	public void test8() throws ParseException{
		List<String> list = new ArrayList<>();
		System.out.println(list);
		}
	@Test
	public void test9() throws ParseException{
		}
	}

