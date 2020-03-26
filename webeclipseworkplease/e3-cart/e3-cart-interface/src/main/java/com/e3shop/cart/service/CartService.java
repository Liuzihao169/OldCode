package com.e3shop.cart.service;

import java.util.List;

import com.e3shop.common.utils.E3Result;
import com.e3shop.pojo.TbItem;

public interface CartService {
	E3Result addCartItem2Reids(Long uid,Long ItemId,Integer num);
	//合并购物车
	E3Result mergeCart(Long uid,List<TbItem>ItemList);
	//从redis当中获得ItemList
	List<TbItem> getItemListFromReidsById(Long uid);
	E3Result updateCartItem(Long uid,Long ItemId,Integer num);
	E3Result deleteCartItem(Long uid,Long ItemId);
	E3Result clearCart(Long uid);
}
