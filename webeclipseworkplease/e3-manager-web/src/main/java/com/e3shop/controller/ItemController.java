package com.e3shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3shop.common.pojo.EasyUiDataDridResult;
import com.e3shop.common.utils.E3Result;
import com.e3shop.pojo.TbItem;
import com.e3shop.service.TbItemService;

/**
 * @author Hao
 * @date: 2019年1月4日 下午3:06:48 
 * @Description: 
 */
@Controller
public class ItemController {
	@Autowired
	private TbItemService tbItemService;
	@RequestMapping(value="/item")
	@ResponseBody
	public TbItem findItemById(long id){
	TbItem tbItem=tbItemService.findByid(id);
		return tbItem;
	}///item/list
	/*
	 * 
	 * 分页表格数据
	 */
	@RequestMapping(value="/item/list")
	@ResponseBody
	public EasyUiDataDridResult getItemList(Integer page,Integer rows){
		EasyUiDataDridResult itemList = tbItemService.getItemList(page, rows);
		return itemList;
	}
	/**
	 * 添加商品
	 * 
	 * 
	 */
	@RequestMapping(value="/item/save")
	@ResponseBody
	public E3Result addItem(TbItem item,String desc){
		E3Result e3Result = tbItemService.addItem(item, desc);
		return e3Result;
	}
	
	
	
	/**
	 * @author: Hao
	 * @Description:
	 * @throws：
	 * @param ids 
	 * @return
	 * @date: 2019年1月4日 下午3:06:53 
	 *
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public E3Result deleteItem(String ids){
		E3Result e3Result = tbItemService.deleteItemByIds(ids);
		return e3Result;
	}
}
