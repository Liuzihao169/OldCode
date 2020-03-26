package com.e3shop.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3shop.common.jedis.JedisClient;
import com.e3shop.common.utils.E3Result;
import com.e3shop.mapper.TbOrderItemMapper;
import com.e3shop.mapper.TbOrderMapper;
import com.e3shop.mapper.TbOrderShippingMapper;
import com.e3shop.order.pojo.OrderInfo;
import com.e3shop.order.service.OrderService;
import com.e3shop.pojo.TbOrder;
import com.e3shop.pojo.TbOrderItem;
import com.e3shop.pojo.TbOrderShipping;
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${Order_ID}")
	private String Order_ID;
	@Value("${DEFAULT_ORDER_ID_VALUE}")
	private String DEFAULT_ORDER_ID_VALUE;
	@Value("${ORDER_DEC_ID}")
	private String ORDER_DEC_ID;
	@Override
	public E3Result createOrder(OrderInfo orderInfo) {
		//如果不存在这个，设置一个初始值
		if(!jedisClient.exists(Order_ID)){
			jedisClient.set(Order_ID, DEFAULT_ORDER_ID_VALUE);
		}
		Long orderId = jedisClient.incr(Order_ID);
//		封装order表的数据
		orderInfo.setOrderId(orderId.toString());
		//'状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭',
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//插入数据
		tbOrderMapper.insert(orderInfo);
		//取出订单详情表
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//插入订单详情表
			Long decId = jedisClient.incr(ORDER_DEC_ID);
			tbOrderItem.setId(decId.toString());
			tbOrderItem.setOrderId(orderId.toString());
			tbOrderItemMapper.insert(tbOrderItem);
		}
		//插入物流信息表
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		//插入订单号
		orderShipping.setOrderId(orderId.toString());
		orderShipping.setUpdated(new Date());
		orderShipping.setCreated(new Date());
		tbOrderShippingMapper.insert(orderShipping);
		return E3Result.ok(orderId);
	}

}
