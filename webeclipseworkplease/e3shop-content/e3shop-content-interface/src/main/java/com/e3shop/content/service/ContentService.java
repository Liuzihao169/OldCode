package com.e3shop.content.service;

import java.util.List;

import com.e3shop.common.utils.E3Result;
import com.e3shop.pojo.TbContent;

public interface ContentService {
	E3Result addContent(TbContent tbContent);
	List<TbContent> getContentListById(Long id);
}
