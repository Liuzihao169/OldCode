package com.e3shop.sso.service;

import com.e3shop.common.utils.E3Result;

public interface TokenService {
	E3Result getUserByTken(String token);
}
