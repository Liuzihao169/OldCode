package com.e3shop.sso.service;

import com.e3shop.common.utils.E3Result;
import com.e3shop.pojo.TbUser;

public interface RegisterService {
		E3Result checkData(String param,Integer type);
		E3Result createUser(TbUser user);
}
