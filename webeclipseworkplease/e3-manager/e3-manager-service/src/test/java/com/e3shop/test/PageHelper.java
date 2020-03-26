package com.e3shop.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.e3shop.mapper.TbItemMapper;
import com.e3shop.pojo.TbItem;
import com.e3shop.pojo.TbItemExample;
import com.github.pagehelper.PageInfo;
public class PageHelper {
	@Test
	public void test1(){
		//初始化容器
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//就是获得了总的数据之后，然后经过pageHelper 就会处理成你想要的分页效果
		TbItemMapper itemMapper = ac.getBean(TbItemMapper.class);
		TbItemExample example =new TbItemExample();
		//执行查询之前 添加进去分页信息
		com.github.pagehelper.PageHelper.startPage(1, 20);
		List<TbItem> list = itemMapper.selectByExample(example);
		//设置分页参数
		PageInfo<TbItem>info=new PageInfo<>(list);
		System.out.println(info.getTotal());
		System.out.println(info.getPages());
		
	}
}
