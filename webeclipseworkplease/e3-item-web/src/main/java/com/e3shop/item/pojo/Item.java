package com.e3shop.item.pojo;

import java.util.Date;

import com.e3shop.pojo.TbItem;

public class Item extends TbItem {
	/*
	 *     private String title;

    private String sellPoint;

    private Long price;

    private Integer num;

    private String barcode;

    private String image;

    private Long cid;

    private Byte status;

    private Date created;**/
	public Item(TbItem tbItem){
		//将查询到了属性值，全部复制一遍
		this.setId(tbItem.getId());
		this.setTitle(tbItem.getTitle());
		this.setSellPoint(tbItem.getSellPoint());
		this.setPrice(tbItem.getPrice());
		this.setNum(tbItem.getNum());
		this.setBarcode(tbItem.getBarcode());
		this.setImage(tbItem.getImage());
		this.setCid(tbItem.getCid());
		this.setStatus(tbItem.getStatus());
		this.setCreated(tbItem.getCreated());
		this.setUpdated(tbItem.getUpdated());
	}
	public String [] getImages(){
		String string = this.getImage();
		if(string!=null&&!"".equals(string)){
			String[] strings = string.split(",");
			return strings;
		}
		return null;
	}
}
