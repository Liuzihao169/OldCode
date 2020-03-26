package com.e3shop.content.service;

import java.util.List;

import com.e3shop.common.pojo.EasyUiDataDridResult;
import com.e3shop.common.pojo.EasyUiTreeNode;
import com.e3shop.common.utils.E3Result;

public interface ContentCategoryService {
	List<EasyUiTreeNode>getContentCategorybyId(Long id) ;
	E3Result creatContentNode(Long parentId,String text);
	void deleteContentCategory(Long id,Long parent);
	EasyUiDataDridResult getContentListByCategoryId(Long CategoryId,Integer page,Integer rows);

}
