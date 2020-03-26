package com.e3shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3shop.common.utils.E3Result;
import com.e3shop.serach.service.ItemSearchService;

/**将商品信息导入到索引库
 * @author Hao
 * @date: 2019年1月24日 上午11:22:34 
 * @Description: 
 */
@Controller
public class SearchItemController {
	@Autowired
	private ItemSearchService searchItemService;
	@ResponseBody
	@RequestMapping(value="/index/item/import",method=RequestMethod.POST)
	public E3Result importItem(){
		E3Result result = searchItemService.importAllItem();
		return result; 
	}
}
