package com.e3shop.service;

import com.e3shop.common.pojo.EasyUiDataDridResult;
import com.e3shop.common.utils.E3Result;
import com.e3shop.pojo.TbItem;
import com.e3shop.pojo.TbItemDesc;

public interface TbItemService {
	TbItem findByid(long id);
	//商品信息的结果集
	EasyUiDataDridResult getItemList(Integer page,Integer rows);
	E3Result addItem(TbItem item,String desc);
	E3Result deleteItemByIds(String Ids);
	TbItemDesc getTbItemDescById(long id);
}

