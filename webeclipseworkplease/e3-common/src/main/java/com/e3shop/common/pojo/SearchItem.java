package com.e3shop.common.pojo;

import java.io.Serializable;

/**
 * @author Hao
 * @date: 2019年1月23日 下午8:12:45 
 * @Description: slor索引库需要的数据的pojo
 */
public class SearchItem implements Serializable {
	private String id;
	private String title;
	private String sell_point;
	private long price;
	private String image;
	private String  catagory_name;
	public String []getImages(){
		String[] Images = image.split(",");
		return Images;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSell_point() {
		return sell_point;
	}
	public void setSell_point(String sell_point) {
		this.sell_point = sell_point;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCatagory_name() {
		return catagory_name;
	}
	public void setCatagory_name(String catagory_name) {
		this.catagory_name = catagory_name;
	}
}
