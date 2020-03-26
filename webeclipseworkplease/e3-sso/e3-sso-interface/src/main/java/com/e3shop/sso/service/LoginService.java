package com.e3shop.sso.service;

import com.e3shop.common.utils.E3Result;

public interface LoginService {
	E3Result login(String username,String password);
}
