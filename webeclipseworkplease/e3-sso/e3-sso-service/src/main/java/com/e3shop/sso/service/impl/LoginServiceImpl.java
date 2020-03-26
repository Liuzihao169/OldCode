package com.e3shop.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3shop.common.jedis.JedisClient;
import com.e3shop.common.utils.E3Result;
import com.e3shop.common.utils.JsonUtils;
import com.e3shop.common.utils.MD5Utils;
import com.e3shop.mapper.TbUserMapper;
import com.e3shop.pojo.TbUser;
import com.e3shop.pojo.TbUserExample;
import com.e3shop.pojo.TbUserExample.Criteria;
import com.e3shop.sso.service.LoginService;
@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_TOKEN_TIMEOUT}")
	private Integer SESSION_TOKEN_TIMEOUT;
	@Override
	public E3Result login(String username, String password) {
		TbUserExample example =new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
	// 1.根据用户名，密码查询数据库
		List<TbUser> list = tbUserMapper.selectByExample(example);
	// 2.判断是否正确
	// 3.如果不正确那么返回用户名或者密码错误
		if(list==null||list.size()==0){
			return new E3Result().build(400, "用户名或者密码不正确");
		}
		//如果用户名存在了那么比较密码是否相同
		TbUser user = list.get(0);
		if(!user.getPassword().equals(MD5Utils.md5(password))){
			return new E3Result().build(400, "用户名或者密码不正确");
		}
		String token = UUID.randomUUID().toString();
	// 4.如果用户名和密码正确，生成toke
		//将token写入到redies当中 key :token  value-user
		//去除信息
		user.setPassword(null);
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		//设置过期时间
		jedisClient.expire("SESSION:"+token,SESSION_TOKEN_TIMEOUT);
	// 5.将toke添加到结果集然后返回*/
		return E3Result.ok(token);
	}

}
