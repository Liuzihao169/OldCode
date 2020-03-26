package com.e3shop.search.test;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class Test {
	@org.junit.Test
	public void addDocument() throws Exception {
		// 创建solrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");// 单机版
		// 创建solrInputDocument 对象
		SolrInputDocument document = new SolrInputDocument();
		// 往文档中添加域 然后添加内容
		document.addField("id", "boot");
		document.addField("item_title", "这是一件衣服");
		// 保存文档对象
		solrServer.add(document);
		// commint提交
		solrServer.commit();
	}

	@org.junit.Test
	public void simpleSelect() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");// 单机版
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.set("q", "飞利浦下单");
		//记得设置默认查找的域，不然会只查找text默认的域
		//可以设置多个默认查询域
		solrQuery.set("df", "item_title");
		solrQuery.set("df", "item_sell_point");
		QueryResponse response = solrServer.query(solrQuery);
		SolrDocumentList list = response.getResults();
		long found = list.getNumFound();
		System.out.println(found);
		for (SolrDocument solrDocument : list) {
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
		}
	}
	
	@org.junit.Test
	public void fuzaSelect() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");// 单机版
		SolrQuery solrQuery = new SolrQuery();
		//开启高亮显示
		solrQuery.set("q", "飞利浦黑色");
		//记得设置默认查找的域，不然会只查找text默认的域
		//可以设置多个默认查询域
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);
		solrQuery.setHighlightSimplePre("<p style='color : red'>");
		solrQuery.setHighlightSimplePost("</p>");
		solrQuery.addHighlightField("item_title");
		QueryResponse response = solrServer.query(solrQuery);
		Map<String, Map<String, List<String>>> map = response.getHighlighting();
		SolrDocumentList list = response.getResults();
		for (SolrDocument solrDocument : list) {
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(map.get(solrDocument.get("id")).get("item_title").get(0));
		}
		
	}
	}
	
