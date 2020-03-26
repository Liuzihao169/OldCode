package com.e3shop.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3shop.cart.service.CartService;
import com.e3shop.common.jedis.JedisClient;
import com.e3shop.common.utils.E3Result;
import com.e3shop.common.utils.JsonUtils;
import com.e3shop.mapper.TbItemMapper;
import com.e3shop.pojo.TbItem;

/**
 * @author Hao
 * @date: 2019年2月5日 下午4:52:09 
 * @Description:购物车的service层
 */
@Service
public class CartServiceImpl implements CartService{
	@Autowired 
	private JedisClient JedisClient;
	@Autowired
	private TbItemMapper tbItemMapper;
	@Value("${CART_USER_PRE}")
	private String CART_USER_PRE;
	@Autowired
	private JedisClient jedisClient;
	@Override
	public E3Result addCartItem2Reids(Long uid, Long ItemId, Integer num) {
		//根据用户 前缀+用户ID 作为key存储hashMap
		Boolean hexists = JedisClient.hexists(CART_USER_PRE+":"+uid, ItemId+"");
		TbItem tbItem = new TbItem();
		if(hexists){
			//先判断该商品是否存在 如果存在那么商品num直接相加
			//先把商品取出来，根据Itemid
			String json = JedisClient.hget(CART_USER_PRE+":"+uid, ItemId+"");
			 tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum()+num);
			//存入
		}else{
			//如果不存在根据商品的id 查询商品信息
			 tbItem = tbItemMapper.selectByPrimaryKey(ItemId);
			String image = tbItem.getImage();
			tbItem.setImage(image.split(",")[0]);
			//更新商品信息的num
			//存入数据
			tbItem.setNum(num);
		}
		JedisClient.hset(CART_USER_PRE+":"+uid, ItemId+"", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}
	
	
	//合并购物车
	@Override
	public E3Result mergeCart(Long uid, List<TbItem> ItemList) {
		//遍历列表，判断redis当中是否有此商品，如果有此商品，那么数目相加如果没有此商品，添加商品信息
		for (TbItem tbItem : ItemList) {
			Boolean hexists = jedisClient.hexists(CART_USER_PRE+":"+uid, tbItem.getId().toString());
 			if(hexists){
				String json = JedisClient.hget(CART_USER_PRE+":"+uid, tbItem.getId().toString());
				TbItem	item = JsonUtils.jsonToPojo(json, TbItem.class);
				item.setNum(tbItem.getNum()+ item.getNum());
				JedisClient.hset(CART_USER_PRE+":"+uid, tbItem.getId().toString(), JsonUtils.objectToJson(item));
			}else{
				//将信息写入redis当中
				JedisClient.hset(CART_USER_PRE+":"+uid, tbItem.getId().toString(), JsonUtils.objectToJson(tbItem));
			}
		}
		return E3Result.ok();
	}


	@Override
	public List<TbItem> getItemListFromReidsById(Long uid) {
		List<String> list = jedisClient.hvals(CART_USER_PRE+":"+uid);
		List<TbItem>CartList = new ArrayList<>();
		for (String string : list) {
			TbItem tbItem = JsonUtils.jsonToPojo(string, TbItem.class);
			CartList.add(tbItem);
		}
		return CartList;
	}


	@Override
	public E3Result updateCartItem(Long uid, Long ItemId, Integer num) {
		String json = JedisClient.hget(CART_USER_PRE+":"+uid, ItemId+"");
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		//修改商品数量
		tbItem.setNum(num);
		//写回
		JedisClient.hset(CART_USER_PRE+":"+uid, tbItem.getId().toString(), JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}


	@Override
	public E3Result deleteCartItem(Long uid, Long ItemId) {
		 JedisClient.hdel(CART_USER_PRE+":"+uid, ItemId+"");
		return E3Result.ok();
	}


	@Override
	public E3Result clearCart(Long uid) {
		 JedisClient.del(CART_USER_PRE+":"+uid);
		return E3Result.ok();
	}

}
