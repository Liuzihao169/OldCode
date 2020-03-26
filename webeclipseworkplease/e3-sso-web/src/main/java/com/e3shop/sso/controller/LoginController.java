package com.e3shop.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3shop.common.utils.CookieUtils;
import com.e3shop.common.utils.E3Result;
import com.e3shop.sso.service.LoginService;

/**
 * @author Hao
 * @date: 2019年2月1日 下午7:55:28 
 * @Description: 这是login的Controller
 */
@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;
	@Value("${SESSION_ID}")
	private String SESSION_ID;
	@ResponseBody
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	public E3Result login(String username,String password,HttpServletRequest request,HttpServletResponse response){
		E3Result e3Result = loginService.login(username, password);
		//如果用户名和密码正确那么将token存入Cookie当中
		if(e3Result.getStatus()==200){
			CookieUtils.setCookie(request, response, SESSION_ID, e3Result.getData().toString());
		}
		return e3Result;
	}
}
