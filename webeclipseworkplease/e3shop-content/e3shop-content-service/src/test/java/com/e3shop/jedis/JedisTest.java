package com.e3shop.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
public class JedisTest {
	//@Test
	public void jedisTest1() throws Exception{
		Jedis jedis = new Jedis("192.168.25.133", 6379);
		jedis.set("tom", "this is my fist");
		String string = jedis.get("tom");
		System.out.println(string);
		jedis.close();
	}
	//通过连接池
	//@Test
	public void JedisPool() throws Exception{
		JedisPool pool = new JedisPool("192.168.25.133", 6379);
		Jedis resource = pool.getResource();
		String string = resource.get("tom");
		System.out.println(string);
		//归还连接资源
		resource.close();
		//关闭连接池
		pool.close();
	}
	
	//连接redis集群
	//@Test
	public void JedisCuster() throws Exception{
		//里面包含参数nodes 这是一个set集合 里面包含多个HostAndPort
		//直接使用jedisredis这个对象来操作
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.133", 7001));
		nodes.add(new HostAndPort("192.168.25.133", 7002));
		nodes.add(new HostAndPort("192.168.25.133", 7003));
		nodes.add(new HostAndPort("192.168.25.133", 7004));
		nodes.add(new HostAndPort("192.168.25.133", 7005));
		nodes.add(new HostAndPort("192.168.25.133", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("name", "123");
		String string = jedisCluster.get("name");
		System.out.println(string);
	}
}



