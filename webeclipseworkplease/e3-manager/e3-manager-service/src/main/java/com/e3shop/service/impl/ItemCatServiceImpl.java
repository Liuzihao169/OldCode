 package com.e3shop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3shop.common.pojo.EasyUiTreeNode;
import com.e3shop.mapper.TbItemCatMapper;
import com.e3shop.pojo.TbItemCat;
import com.e3shop.pojo.TbItemCatExample;
import com.e3shop.pojo.TbItemCatExample.Criteria;
import com.e3shop.service.ItemCatService;
@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	public List<EasyUiTreeNode> getEasyUiTreeNode(Long parentId) {
		//单表查询，可以使用逆向工程
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		List<EasyUiTreeNode>listResult = new ArrayList<EasyUiTreeNode>();
		for (TbItemCat tbItemCat : list) {
			EasyUiTreeNode treeNode = new EasyUiTreeNode();
			treeNode.setId(tbItemCat.getId());
			treeNode.setText(tbItemCat.getName());
			treeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			listResult.add(treeNode);
		}
		return listResult;
	}
}
