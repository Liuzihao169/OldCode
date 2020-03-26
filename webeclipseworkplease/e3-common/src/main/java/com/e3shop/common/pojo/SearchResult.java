package com.e3shop.common.pojo;

import java.io.Serializable;
import java.util.List;

/**solr返回结果集
 * @author Hao
 * @date: 2019年1月24日 下午7:34:01 
 * @Description: 
 */
public class SearchResult implements Serializable{
	//总记录数
	private Long totalPages;
	//总页数
	private Integer recourdCount;
	//返回的需要的SearItem
	private List<SearchItem>searchItemlist;
	
	public Long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}
	public Integer getRecourdCount() {
		return recourdCount;
	}
	public void setRecourdCount(Integer recourdCount) {
		this.recourdCount = recourdCount;
	}
	public List<SearchItem> getSearchItemlist() {
		return searchItemlist;
	}
	public void setSearchItemlist(List<SearchItem> searchItemlist) {
		this.searchItemlist = searchItemlist;
	}
}
