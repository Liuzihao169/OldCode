package com.e3shop.serach.mapper;

import java.util.List;

import com.e3shop.common.pojo.SearchItem;

public interface SearchItemMapper {
	List<SearchItem>getSearchItem();
	SearchItem getSearchItemById(Long id);
}
