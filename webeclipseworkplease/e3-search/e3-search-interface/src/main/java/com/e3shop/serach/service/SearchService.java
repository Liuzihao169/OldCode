package com.e3shop.serach.service;
import com.e3shop.common.pojo.SearchResult;


public interface SearchService {
	SearchResult search(String Keywords,Integer page,Integer rows);
}
