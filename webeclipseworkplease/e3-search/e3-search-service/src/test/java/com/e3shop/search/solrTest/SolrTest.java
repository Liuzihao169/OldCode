package com.e3shop.search.solrTest;


import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrTest {
	@Test
	public void testSolrCloud() throws  Exception{
	CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.128:2182,192.168.25.128:2183,192.168.25.128:2184");
	cloudSolrServer.setDefaultCollection("collection2");
	SolrInputDocument document = new SolrInputDocument();
	document.addField("id", "商品001");
	document.addField("item_title", "这是solrcloud");
	cloudSolrServer.add(document);
	cloudSolrServer.commit();
	}
}
