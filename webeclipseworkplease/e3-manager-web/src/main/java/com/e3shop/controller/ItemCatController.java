package com.e3shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3shop.common.pojo.EasyUiTreeNode;
import com.e3shop.service.ItemCatService;

@Controller
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	@ResponseBody
	@RequestMapping(value="/item/cat/list")
	//获得添加商品的 那个tree目录 使用的是延迟加载的  每次打开一个文件夹之后，就会传一个id过来 作为父id
	public List<EasyUiTreeNode> getEasyUiTreeNode(
			@RequestParam(name="id",defaultValue="0")Long parentId){
		List<EasyUiTreeNode> list = itemCatService.getEasyUiTreeNode(parentId);
		return list;
	}
}
