package com.e3shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3shop.common.utils.E3Result;
import com.e3shop.content.service.ContentService;
import com.e3shop.pojo.TbContent;

@Controller
public class ContentController {
	///content/save
	@Autowired
	private ContentService  contentService; 
	/**
	 * @author: Hao
	 * @Description: 添加内内容
	 * @throws：
	 * @param tbContent
	 * @return
	 * @date: 2019年1月18日 下午9:36:35 
	 *
	 */
	@RequestMapping("content/save")
	@ResponseBody
	public E3Result addContent(TbContent tbContent){
		E3Result result = contentService.addContent(tbContent);
		return result;
	}
}
