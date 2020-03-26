package com.e3shop.search.ActiveMQ;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class testActivemqConsumer {
	@Test
	public void testConsumer() throws Exception {
		ApplicationContext acApplicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//等待一定要让其处于等待状态，这样message就是一直会处于监听状态
		System.in.read();
	}
}
