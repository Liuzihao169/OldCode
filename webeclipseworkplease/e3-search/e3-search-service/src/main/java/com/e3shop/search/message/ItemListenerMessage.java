package com.e3shop.search.message;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.e3shop.common.pojo.SearchItem;
import com.e3shop.serach.mapper.SearchItemMapper;

/**这个是监听消息，然后从消息队列当中获得商品的id
 * @author Hao
 * @date: 2019年1月28日 下午6:57:58 
 * @Description: 
 */
public class ItemListenerMessage implements MessageListener {
	@Autowired
	SolrServer solrServer;
	@Autowired 
	private SearchItemMapper searchItemMapper;
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage=(TextMessage) message;
			String testid = textMessage.getText();
			long id = Long.parseLong(testid);
			System.out.println(id);
			//预防那边还没有提交到数据库，先让线程等待一段时间；
			Thread.sleep(1000);
			//然后根据id到数据库当中查询商品
			SearchItem searchItem = searchItemMapper.getSearchItemById(id);
			SolrInputDocument  document = new SolrInputDocument();
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCatagory_name());
			solrServer.add(document);
			//记得提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
