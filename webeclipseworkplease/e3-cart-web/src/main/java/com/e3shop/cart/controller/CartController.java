package com.e3shop.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.taglibs.standard.lang.jstl.test.beans.PublicBean1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3shop.cart.service.CartService;
import com.e3shop.common.jedis.JedisClient;
import com.e3shop.common.utils.CookieUtils;
import com.e3shop.common.utils.E3Result;
import com.e3shop.common.utils.JsonUtils;
import com.e3shop.pojo.TbItem;
import com.e3shop.pojo.TbUser;
import com.e3shop.service.TbItemService;

/**购物车的Controller
 * @author Hao
 * @date: 2019年2月3日 上午11:21:15 
 * @Description: 
 */
@Controller
public class CartController {
	
	//添加商品到购物车
	//返回到购物车列表视图
	@Autowired
	private TbItemService tbItemService;
	@Value("${Cart_COOKIE_TIMEOUT}")
	private Integer Cart_COOKIE_TIMEOUT;
	@Autowired
	private CartService CartService;
	//添加购物车
	@RequestMapping("/cart/add/{productId}")
	public String addItemToCart(@PathVariable Long productId, @RequestParam(defaultValue="1")Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//从request当中获取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null){
			//判断用户是否登陆，如果登陆，购物车信息存储在redie当中
			CartService.addCartItem2Reids(user.getId(), productId, num);
			//返回逻辑视图
			return "cartSuccess";
			
		}
		//参数 item_id num
		//从cookie当中取出信息。
		List<TbItem> list = getItemListFromCookie(request, "cart");
		//判断用户信息是否存在
		boolean flag = false;
		for (TbItem tbItem : list) {
			//如果存在，那么取出然后num相加
			if(tbItem.getId()==productId.longValue()){
			tbItem.setNum(tbItem.getNum()+num);
			flag=true;
			break;
			}
		}
		//如果不存在。那么写入商品信息
		//从数据库当中查询商品信息，修改商品信息
		if(!flag){
			TbItem tbItem = tbItemService.findByid(productId);
			tbItem.setNum(num);
			//修改图片信息，因为是一个图片字符串，只取一张图片作为购物车图片即可
			if(StringUtils.isNotBlank(tbItem.getImage())){
			tbItem.setImage(tbItem.getImage().split(",")[0]);
			}
			list.add(tbItem);
		}
		//存储cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), Cart_COOKIE_TIMEOUT, true);
		//返回添加成功
 
		return "cartSuccess";
	}
	/*
	 * 展示购物车信息*/
	@RequestMapping("/cart/cart")
	public String showCart(HttpServletRequest request,HttpServletResponse response){
		List<TbItem> list = getItemListFromCookie(request, "cart");
		//先判断用户是否登录
		 TbUser user = (TbUser) request.getAttribute("user");
		//如果登陆状态
		 if(user!=null){
			 //从cookies将信息取出来
			 //将redis中的数据 与未登陆状态cookie当中的信息合并
			 //将信息写入redis当中
			 CartService.mergeCart(user.getId(),list);
			 //将cookies当中的信息删除
			 CookieUtils.deleteCookie(request, response, "cart");
			 //从redis当中取购物车信息
			list = CartService.getItemListFromReidsById(user.getId());
		 }
//		如果用户登陆状态
		//从cookie当中取出数据，然后传到cart页面进行遍历
		request.setAttribute("cartList", list);
		return "cart";
	}
	
	
	
	
	//更新商品的小计/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".action"
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartItemNum(@PathVariable Long itemId, @PathVariable Integer num
			,HttpServletRequest request,HttpServletResponse response){
		TbUser user = (TbUser) request.getAttribute("user");
		//登陆状态，从修改redis当中的商品信息状态
		if(user != null){
			CartService.updateCartItem(user.getId(), itemId, num);
		}
		//判断用户是否是登陆状态，未登录状态，从cookies当中取信息，然后再修改商品信息，再写入cookies当中
		List<TbItem> list = getItemListFromCookie(request,"cart");
		for (TbItem tbItem : list) {
			//找到ID相同的 然后取出，添加数量
			if(tbItem.getId()==itemId.longValue()){
				//更新数量
				tbItem.setNum(num);
				//跳出循环
				break;
			}
		}
		//写入cookie信息
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), Cart_COOKIE_TIMEOUT, true);
		return E3Result.ok();
	}
	/*
	 * 删除购物车里面一个商品*/
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
		List<TbItem> list = getItemListFromCookie(request,"cart");
		TbUser user = (TbUser) request.getAttribute("user");
		//判断用户是否登陆
		if(user!=null){
			CartService.deleteCartItem(user.getId(), itemId);
			//返回逻辑视图
			return "redirect:/cart/cart.html";
		}
		for (TbItem tbItem : list) {
			if(tbItem.getId().longValue()==itemId){
				list.remove(tbItem);
				//跳出循环b
				break;
			}
			
		}
		//重新写cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), Cart_COOKIE_TIMEOUT, true);
		//删除了之后，跳转到cart.html重新加载
		return "redirect:/cart/cart.html";
	}
	//从cookie当中取数据
	private List<TbItem> getItemListFromCookie(HttpServletRequest request,String cookieName){
		String json = CookieUtils.getCookieValue(request, cookieName, true);
		if(StringUtils.isBlank(json)){
			//如果没有这个cookie，则创建一个
			return new ArrayList<>();
		}
		return  JsonUtils.jsonToList(json, TbItem.class);
	}
}
