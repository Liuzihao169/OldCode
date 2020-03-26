package com.e3shop.content.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3shop.common.jedis.JedisClient;
import com.e3shop.common.utils.E3Result;
import com.e3shop.common.utils.JsonUtils;
import com.e3shop.content.service.ContentService;
import com.e3shop.mapper.TbContentMapper;
import com.e3shop.pojo.TbContent;
import com.e3shop.pojo.TbContentExample;
import com.e3shop.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {
	@Value("${CONTENT_LUNBOTU}")
	String CONTENT_LUNBOTU;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbContentMapper tbContentMapper;

	@Override
	public E3Result addContent(TbContent tbContent) {
		tbContentMapper.insert(tbContent);
		//添加之后删除缓存
		jedisClient.hdel(CONTENT_LUNBOTU, tbContent.getCategoryId().toString());
		return E3Result.ok();
	}

	// 查找轮播图的方法
	@Override
	public List<TbContent> getContentListById(Long id) {
		try {
			// 先从缓存当中拿，如果缓存为null
			String json = jedisClient.hget(CONTENT_LUNBOTU, id.toString());
			if (StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(id);
		// 从数据库当中查询
		List<TbContent> selectByExample = tbContentMapper.selectByExample(example);
		try {
			String json = JsonUtils.objectToJson(selectByExample);
			// //把查询的信息存储到缓存当中
			jedisClient.hset(CONTENT_LUNBOTU, id.toString(), json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectByExample;
	}

}
