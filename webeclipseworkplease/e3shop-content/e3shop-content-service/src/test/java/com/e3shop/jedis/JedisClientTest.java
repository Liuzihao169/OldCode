package com.e3shop.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.e3shop.common.jedis.JedisClient;

public class JedisClientTest {
	//@Test
	public void test1() throws Exception{
		ApplicationContext ac =new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient jedisClient = ac.getBean(JedisClient.class);
		 jedisClient.set("name", "tom");
		 String string = jedisClient.get("name");
		 System.out.println(string+"1");
	}
}
