package com.e3shop.testActiveMq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ActiveMQSpring {
	@Test
	public void testSendMessage(){
		//创建一个spring容器
		ApplicationContext acApplicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//获得模板对象
		JmsTemplate jmsTemplate = acApplicationContext.getBean(JmsTemplate.class);
		//从容器当中获得Destination对象
		Destination destination = (Destination) acApplicationContext.getBean("queueDestination");
		//利用模板发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("active spring queue");
			}
		});
	}
	
	//接收消息
	public void testGetMessage(){
		//创建一个spring容器加载配置文件
		//获得模板对象
		//获得destinnation对象
		//获得消息
	}
}
