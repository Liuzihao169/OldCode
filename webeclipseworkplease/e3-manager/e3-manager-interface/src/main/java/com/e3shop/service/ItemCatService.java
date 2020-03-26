package com.e3shop.service;

import java.util.List;

import com.e3shop.common.pojo.EasyUiTreeNode;
public interface ItemCatService {
	List<EasyUiTreeNode>getEasyUiTreeNode(Long parentId);
}
