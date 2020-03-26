package com.e3shop.sso.service.impl;

import java.util.Date;

import org.apache.activemq.filter.function.replaceFunction;
import org.apache.ibatis.executor.ReuseExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3shop.common.utils.E3Result;
import com.e3shop.common.utils.MD5Utils;
import com.e3shop.mapper.TbUserMapper;
import com.e3shop.pojo.TbUser;
import com.e3shop.pojo.TbUserExample;
import com.e3shop.pojo.TbUserExample.Criteria;
import com.e3shop.sso.service.RegisterService;

import javassist.expr.NewArray;

/**
 * @author Hao
 * @date: 2019年2月1日 上午10:00:36
 * @Description: 注册的service层
 */
@Service
public class RegisterServiceImpl implements RegisterService {
	/**
	 *
	 * @Description: @throws：
	 * @param param
	 *            传过来的参数
	 * @param type
	 *            1、2、3分别代表username、phone、email
	 * @return
	 * @return
	 * @date: 2019年2月1日 上午10:01:56
	 *
	 */
	@Autowired
	private TbUserMapper tbUserMapper;

	public E3Result checkData(String param, Integer type) {
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		// 封装查询条件
		if (type == 1) {
			criteria.andUsernameEqualTo(param);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(param);
		} else if (type == 3) {
			criteria.andEmailEqualTo(param);
		} else {
			return new E3Result().build(400, "信息加载错误");
		}
		// 执行查询
		int count = tbUserMapper.countByExample(tbUserExample);
		// 查询数据库看是否有相同的信息
		if (count == 1) {
			return new E3Result().ok(false);
		}
		return new E3Result().ok(true);
	}

	// 注册用户
	@Override
	public E3Result createUser(TbUser user) {
		// 校验数据的完整性
		if ("".equals(user.getUsername()) || "".equals(user.getPassword()) || "".equals(user.getPhone())) {
			return new E3Result().build(400, "数据不完整");
		}
		// 补全pojo
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 将密码进行数据库加密
		user.setPassword(MD5Utils.md5(user.getPassword()));
		// 插入数据
		tbUserMapper.insert(user);
		return E3Result.ok();
	}
}
