package com.e3shop.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3shop.common.jedis.JedisClient;
import com.e3shop.common.utils.E3Result;
import com.e3shop.common.utils.JsonUtils;
import com.e3shop.pojo.TbUser;
import com.e3shop.sso.service.TokenService;
import com.sun.tools.internal.xjc.api.J2SJAXBModel;

/**根据token取用户信息
 * @author Hao
 * @date: 2019年2月2日 上午11:04:59 
 * @Description: 
 */
@Service
public class TokenServiceImpl implements TokenService {
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_TOKEN_TIMEOUT}")
	private Integer SESSION_TOKEN_TIMEOUT;
	@Override
	public E3Result getUserByTken(String token) {
		//根据token往redis当中取用户信息
		String json = jedisClient.get("SESSION:"+token);
		//如果为空，那么返回用户登录信息已经过期的信息
		if(StringUtils.isBlank(json)){
			return E3Result.build(201, "用户信息已经失效");
		}
		//并且重置过期时间。
		jedisClient.expire(token,SESSION_TOKEN_TIMEOUT );
		//不为空，那么取出json 转换成user对象 然后封装到E3result当中
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return E3Result.ok(user);
	}

}
