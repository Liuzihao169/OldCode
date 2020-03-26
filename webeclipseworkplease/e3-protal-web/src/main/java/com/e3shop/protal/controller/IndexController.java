package com.e3shop.protal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3shop.content.service.ContentService;
import com.e3shop.pojo.TbContent;

/**
 * @author Hao
 * @date: 2019年1月4日 下午8:22:11 
 * @Description: 首页展示
 */
@Controller
public class IndexController {
	@Value("${LUNBOTU_CATEGORY_ID}")
	private Long id;
	@Autowired
	private ContentService contentService;
	/**
	 * @author: Hao
	 * @Description:
	 * @throws：
	 * @return
	 * @date: 2019年1月4日 下午8:25:47 
	 *
	 */
	@RequestMapping("/index.html")
	public String showIndex(Model model){
		List<TbContent> ad1List = contentService.getContentListById(id);
		model.addAttribute("ad1List",ad1List);
		return "index";
	}
	
	
}
