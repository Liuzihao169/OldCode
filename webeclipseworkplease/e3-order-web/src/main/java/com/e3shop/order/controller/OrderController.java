package com.e3shop.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3shop.cart.service.CartService;
import com.e3shop.common.utils.E3Result;
import com.e3shop.order.pojo.OrderInfo;
import com.e3shop.order.service.OrderService;
import com.e3shop.pojo.TbItem;
import com.e3shop.pojo.TbUser;

/**
 * @author Hao
 * @date: 2019年2月9日 下午6:03:25 
 * @Description: 订单管理的Controller
 */
@Controller
public class OrderController {
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	@RequestMapping("order/order-cart")
	public String showOrderCart(Model model,HttpServletRequest request){
		TbUser user = (TbUser) request.getAttribute("user");
		//从redis当中获得购物车
		List<TbItem> list = cartService.getItemListFromReidsById(user.getId());
		model.addAttribute("cartList", list);
		return "order-cart";
	}
	
	//创建订单/order/create.html"
	@RequestMapping("/order/create.html")
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
		TbUser user = (TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		//service层千万不要用trycatch
		E3Result result = orderService.createOrder(orderInfo);
		
		if(result.getStatus()==200){
			//清空购物车
			cartService.clearCart(user.getId());
			
		}
		//取出orderID
		request.setAttribute("payment", orderInfo.getPayment());
		request.setAttribute("orderId", result.getData());
		//传递参数到页面上去
		
		return "success";
	}
}
