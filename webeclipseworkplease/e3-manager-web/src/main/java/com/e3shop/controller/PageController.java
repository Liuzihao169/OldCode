package com.e3shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/*
 *跳转到主页
 */
@Controller
public class PageController {
	@RequestMapping(value="/")
	public String showIndex(){
		return "index";
	}
	//把每个url转发到相应的jsp页面
	@RequestMapping(value="/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
}
