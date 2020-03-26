package com.e3shop.serach.service.impl;


import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3shop.common.pojo.SearchResult;
import com.e3shop.search.dao.SearchDaoImpl;
import com.e3shop.serach.service.SearchService;

/**search的service层
 * @author Hao
 * @date: 2019年1月24日 下午8:49:34 
 * @Description: 
 */
@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	SearchDaoImpl searchDao	;
	/**
	 *
	 * @Description:
	 * @throws：
	 * @param Keywords 关键字
	 * @param page 当前页
	 * @param rows 每页显示的条数
	 * @return  
	 * @return 
	 * @date: 2019年1月24日 下午9:15:28 
	 *
	 */
	@Override
	public SearchResult search(String Keywords, Integer page, Integer rows) {
		//封装query 向dao层查找
		SolrQuery query = new SolrQuery();
		//开启高亮显示
		query.setShowDebugInfo(true);
		query.setHighlightSimplePre("<em style='color:red'>");
		query.setHighlightSimplePost("</em>");
		//设置高亮的域
		query.addHighlightField("item_title");
		//通过page  和rows 计算出总页数
		query.set("q", Keywords);
		//也可以不设置默认查询的域 query.set("q", "item_title：");
		//设置默认查询的域
		query.set("df", "item_title");
		Integer startIndex=(page-1)*rows;
		query.setStart(startIndex);
		query.setRows(rows);
		SearchResult  searchResult=null;
		try {
			  searchResult = searchDao.search(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long totalPages = searchResult.getTotalPages();
		//总页数
		 Integer recourdCount= (totalPages.intValue()+rows-1)/rows;
		 searchResult.setRecourdCount(recourdCount);
		return searchResult;
	}

}
