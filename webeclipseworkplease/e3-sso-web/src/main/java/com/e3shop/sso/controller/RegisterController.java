package com.e3shop.sso.controller;

import java.io.UnsupportedEncodingException;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3shop.common.utils.E3Result;
import com.e3shop.pojo.TbUser;
import com.e3shop.sso.service.RegisterService;

/**
 * @author Hao
 * @date: 2019年1月31日 下午7:25:04 
 * @Description: 注册的Controller
 */
@Controller
public class RegisterController {
	@Autowired
	private RegisterService registerService;
	//跳转到登陆或者注册页面
	@RequestMapping("/page/{page}")
	public  String showRegister( @PathVariable String page,String redirect,Model model){
		model.addAttribute("redirect", redirect);
		return page;
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param,@PathVariable Integer type) throws Exception{
		String string = new String(param.getBytes("iso-8859-1"), "utf-8");
		E3Result result = registerService.checkData(string, type);
		return result;
	}
	//注册用户
	@RequestMapping("/user/register")
	@ResponseBody
	public E3Result doRegister(TbUser user){
		E3Result result = registerService.createUser(user);
		return result;
	}
}
