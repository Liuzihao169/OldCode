package com.e3shop.order.intecerptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.e3shop.cart.service.CartService;
import com.e3shop.common.utils.CookieUtils;
import com.e3shop.common.utils.E3Result;
import com.e3shop.common.utils.JsonUtils;
import com.e3shop.pojo.TbItem;
import com.e3shop.pojo.TbUser;
import com.e3shop.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {
	@Value("${SSO_URL}")
	private String SSO_URL;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1、从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		//2、如果没有取到，没有登录，跳转到sso系统的登录页面。拦截,如果登陆成功跳转到当前页面继续执行操作
		if(StringUtils.isBlank(token)){
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			return false;//拦截
		}
		//3、如果取到token。判断登录是否过期，需要调用sso系统的服务，根据token取用户信息
		E3Result e3Result = tokenService.getUserByTken(token);
		//4、如果没有取到用户信息，登录已经过期，重新登录。跳转到登录页面。拦截
		if(e3Result.getStatus()!=200){
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			return false;//拦截
		}
		//5、如果取到用户信息，用户已经是登录状态，把用户信息保存到request中。放行
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute("user", user);
		//6、判断cookie中是否有购物车信息，如果有合并购物车
		String cookie = CookieUtils.getCookieValue(request, "cart",true);
		if(StringUtils.isNotBlank(cookie)){
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cookie, TbItem.class));
			//删除购物车里面的信息
			CookieUtils.deleteCookie(request, response, "cart");
		}
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
