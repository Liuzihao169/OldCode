package com.e3shop.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3shop.common.utils.E3Result;
import com.e3shop.common.utils.JsonUtils;
import com.e3shop.sso.service.TokenService;

/**
 * @author Hao
 * @date: 2019年2月2日 上午11:30:03 
 * @Description: 根据token取用户信息的Controller
 */
@Controller
public class TokenController {
	@Autowired
	private  TokenService tokenService;
	//通过ajax请求，然后获得请求路径后面的参数 token
	@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,
			produces="application/json;charset=utf-8")
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){
		//获得秘钥，然后从cookie当中
		E3Result result = tokenService.getUserByTken(token);
		if(StringUtils.isNotBlank(callback)){
			return callback+"("+JsonUtils.objectToJson(result)+")";
		}
		return JsonUtils.objectToJson(result);
	}
}
