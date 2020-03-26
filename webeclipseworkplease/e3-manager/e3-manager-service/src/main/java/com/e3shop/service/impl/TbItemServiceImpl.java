package com.e3shop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.e3shop.common.jedis.JedisClient;
import com.e3shop.common.pojo.EasyUiDataDridResult;
import com.e3shop.common.utils.E3Result;
import com.e3shop.common.utils.IDUtils;
import com.e3shop.common.utils.JsonUtils;
import com.e3shop.mapper.TbItemDescMapper;
import com.e3shop.mapper.TbItemMapper;
import com.e3shop.pojo.TbItem;
import com.e3shop.pojo.TbItemDesc;
import com.e3shop.pojo.TbItemExample;
import com.e3shop.service.TbItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * @author Hao
 * @date: 2019年1月4日 下午2:13:34 
 * @Description: 
 */
@Service("tbItemServiceImpl")
public class TbItemServiceImpl implements TbItemService {
	//在springmvc用动态代理的方式 实例化 直接到spring工厂中拿就可以
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_REDIES_PRE}")
	private String ITEM_REDIES_PRE;
	@Value("${LIVE_TIME}")
	private Integer LIVE_TIME;
	//利用缓存
	public TbItem findByid(long id) {
		try {
			//先从redis中拿缓存
			String json = jedisClient.get(ITEM_REDIES_PRE + ":" + id + ":BASE");
			//判断为是否为空
			if (StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果是空，那么从数据库中查
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
		//然后再将数据存储到redis当中
		try {
			String json = JsonUtils.objectToJson(tbItem);
			jedisClient.set(ITEM_REDIES_PRE + ":" + id + ":BASE", json);
		//设置缓存时间
			jedisClient.expire(ITEM_REDIES_PRE + ":" + id + ":BASE", LIVE_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItem;
	}
	public EasyUiDataDridResult getItemList(Integer page, Integer rows) {
	
		//查询之前进行包装 设置查找的起始页数和每页的条数
		if(page==null){
			//默认参数初始化
			page=1;
			rows=10;
		}
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		PageInfo <TbItem>pageInfo = new PageInfo<TbItem>(list);
		EasyUiDataDridResult itemResult = new EasyUiDataDridResult();
		itemResult.setRows(pageInfo.getList());
		itemResult.setTotal(pageInfo.getTotal());
		return itemResult;
	}
	
	//添加商品
	public E3Result addItem(TbItem item, String desc) {
		//获得随机id
		final long id = IDUtils.genItemId();
		//封装item
		item.setId(id);
		item.setStatus((byte)1);;//1-正常 2-下架 3—删除
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//封装item_desc
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(id);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		//保存商品 
		tbItemMapper.insert(item);
		//保存商品信息
		tbItemDescMapper.insert(tbItemDesc);
		
		//添加商品的时候，像activeMQ发送id的消息
		//获得jms模板对象
		//获得destincatio topic
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session ) throws JMSException {
				TextMessage textMessage = session.createTextMessage(id+"");
				return textMessage;
			}
		});
		//发送消息
		return E3Result.ok();
	}
	
	/**
	 * @author: Hao
	 * @Description: 删除商品
	 * @throws：
	 * @param Ids 前台传过来的ids 是id用逗号分隔开
	 * @return add
	 * @date: 2019年1月4日 下午2:35:40 
	 *
	 */
	public E3Result deleteItemByIds(String Ids) {
		//删除是逻辑上的删除 只要把商品的状态改变成 3 就是代表删除
		TbItem item = new TbItem();
		String[] arr = Ids.split(",");
		for (String string : arr) {
			Integer parseInt = Integer.parseInt(string);
			Long id = parseInt.longValue();
			item.setId(id);
			item.setStatus((byte)3);
			tbItemMapper.updateByPrimaryKeySelective(item);
		}
		return E3Result.ok();
	}
	//根据id查询Desc这张表
	public TbItemDesc getTbItemDescById(long id) {
		try {
			//先从redis中拿缓存
			String json = jedisClient.get(ITEM_REDIES_PRE + ":" + id + ":DESC");
			//判断为是否为空
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);
		try {
			String json = JsonUtils.objectToJson(tbItemDesc);
			jedisClient.set(ITEM_REDIES_PRE + ":" + id + ":DESC", json);
			//设置缓存时间
			jedisClient.expire(ITEM_REDIES_PRE + ":" + id + ":BASE", LIVE_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}
	


}
