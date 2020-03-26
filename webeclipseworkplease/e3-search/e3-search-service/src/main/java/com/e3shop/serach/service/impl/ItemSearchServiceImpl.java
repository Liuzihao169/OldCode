package com.e3shop.serach.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3shop.common.pojo.SearchItem;
import com.e3shop.common.utils.E3Result;
import com.e3shop.serach.mapper.SearchItemMapper;
import com.e3shop.serach.service.ItemSearchService;


/**索引库维护service
 * @author Hao
 * @date: 2019年1月23日 下午9:27:26 
 * @Description: 
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService {
	@Autowired
	private SearchItemMapper  searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	public E3Result importAllItem() {
		//查询商品列表
		try {
			List<SearchItem> list = searchItemMapper.getSearchItem();
			//遍历商品列表
			//创建文档对象
			//添加域
			//保存文档
			//提价
			for (SearchItem searchItem : list) {
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCatagory_name());
				solrServer.add(document);
			}
			solrServer.commit();
			
			return new E3Result().ok();
		} catch (Exception e) {
			e.printStackTrace();
			return new E3Result().build(500, "导入数据发生异常");
		}
	}

}
