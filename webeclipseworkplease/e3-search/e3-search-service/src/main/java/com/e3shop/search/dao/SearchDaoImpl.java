package com.e3shop.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3shop.common.pojo.SearchItem;
import com.e3shop.common.pojo.SearchResult;

/**查询query
 * @author Hao
 * @date: 2019年1月24日 下午8:07:42 
 * @Description: 
 */
@Repository
public class SearchDaoImpl {
	@Autowired
	SolrServer solrServer;
	public SearchResult search(SolrQuery solrQuery) throws Exception{
		SearchResult result = new SearchResult();
		List<SearchItem> list = new ArrayList<>();
		QueryResponse response = solrServer.query(solrQuery);
		//获得结果集合
		SolrDocumentList documentList = response.getResults();
		//获得总的条数
		long totalPages = documentList.getNumFound();
		//获得高亮结果集合
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for (SolrDocument solrDocument : documentList) {
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			//searchItem.setTitle((String) solrDocument.get("item_title"));
			List<String> list2 = highlighting.get(solrDocument.get("id")).get("item_title");
			String item_title = "";
			//判断是否查询到了高亮 如果没有查询 那么就正常显示
			if(list2 != null && list2.size()>0){
				item_title = list2.get(0);
			}else{
				item_title=(String) solrDocument.get("item_title");
			}
			searchItem.setTitle(item_title);
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			searchItem.setCatagory_name( (String) solrDocument.get("item_category_name"));
			list.add(searchItem);
		}
		//封住result
		result.setSearchItemlist(list);
		result.setTotalPages(totalPages);
		return result;
	}
}
