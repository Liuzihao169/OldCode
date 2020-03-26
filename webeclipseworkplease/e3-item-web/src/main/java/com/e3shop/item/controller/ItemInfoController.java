package com.e3shop.item.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3shop.item.pojo.Item;
import com.e3shop.pojo.TbItem;
import com.e3shop.pojo.TbItemDesc;
import com.e3shop.service.TbItemService;

/**
 * @author Hao
 * @date: 2019年1月29日 下午6:04:09 
 * @Description:商品详情的Controller 
 */
@Controller
public class ItemInfoController {
	@Autowired
	private TbItemService ibItemService;
	@RequestMapping("/item/{id}")
	public String getItemInfo(Model model,@PathVariable(value="id") long id){
		TbItemDesc descById = ibItemService.getTbItemDescById(id);
		TbItem tbItem = ibItemService.findByid(id);
		Item item = new Item(tbItem);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", descById);
		return "item";
	}
}
