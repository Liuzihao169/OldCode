package com.e3shop.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.e3shop.common.utils.CookieUtils;
import com.e3shop.common.utils.E3Result;
import com.e3shop.pojo.TbUser;
import com.e3shop.sso.service.TokenService;

public class CheckUserLoginInterceptor implements HandlerInterceptor {
	@Autowired
	private TokenService tokenService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//执行handler方法之前
		//从cookie当中取token
		String token = CookieUtils.getCookieValue(request, "token");
		if(StringUtils.isBlank(token)){
			//没取到那么用户未登陆 放行
			return true;
		}
		//取到，从redies当中取用户信息
		E3Result result = tokenService.getUserByTken(token);
		if(result.getStatus() != 200){
			//未取到，用户信息过期
			return true;
		}
		//取到，将user取出来，存放在request域当中
		TbUser user = (TbUser) result.getData();
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
