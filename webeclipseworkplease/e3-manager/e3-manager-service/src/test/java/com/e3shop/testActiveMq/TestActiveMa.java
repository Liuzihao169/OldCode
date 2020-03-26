package com.e3shop.testActiveMq;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
public class TestActiveMa {
	// 脱离spring使用
	@Test
	public  void testActiveQueu() throws Exception{
		//创建工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//获得连接
		Connection connection = factory.createConnection();
		//调用start方法
		connection.start();
		//创建session 第一个参数是否开启事物  第二个参数 设置应答模式
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//,利用session 创建Distiction 对象  queue  topic
		Queue queue = session.createQueue("test1-qeueu");
		//使用Sssion创建一个Product对象
		MessageProducer producer = session.createProducer(queue);
		//创建一个testMasage对象
		TextMessage message = session.createTextMessage("activemq queue");
		producer.send(message);
		//发送消息
		//关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	//接收消息
	@Test
	public void testQueueCustomer() throws Exception{
		//创建连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//获得连接
		Connection connection = factory.createConnection();
		//开启连接
		connection.start();
		//使用conection获得一个session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个destination对象  Queue
		Queue queue = session.createQueue("test1-qeueu");
		//使用Session创建一个	Customer对象
		MessageConsumer consumer = session.createConsumer(queue);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				try {
					TextMessage textMessage = (TextMessage) message;
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//打印结果
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
		//关闭资源
	}
	
	@Test
	public  void testActiveTopic() throws Exception{
		//创建工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//获得连接
		Connection connection = factory.createConnection();
		//调用start方法
		connection.start();
		//创建session 第一个参数是否开启事物  第二个参数 设置应答模式
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//,利用session 创建Distiction 对象  queue  topic 这里使用topic
		Topic topic = session.createTopic("topic");
		//使用Sssion创建一个Product对象
		MessageProducer producer = session.createProducer(topic);
		//创建一个testMasage对象
		TextMessage message = session.createTextMessage("topic-my");
		producer.send(message);
		//发送消息
		//关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	@Test
	public  void testActiveTopicCustomer() throws Exception{
		//创建工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//获得连接
		Connection connection = factory.createConnection();
		//调用start方法
		connection.start();
		//创建session 第一个参数是否开启事物  第二个参数 设置应答模式
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//,利用session 创建Distiction 对象  queue  topic 这里使用topic
		Topic topic = session.createTopic("topic");
		//使用Session创建一个	Customer对象
		MessageConsumer consumer = session.createConsumer(topic);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				try {
					TextMessage textMessage = (TextMessage) message;
					System.out.println("这是消费3");
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//打印结果
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
		//关闭资源
	}
}
