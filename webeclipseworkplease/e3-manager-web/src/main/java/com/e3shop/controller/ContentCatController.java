package com.e3shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3shop.common.pojo.EasyUiDataDridResult;
import com.e3shop.common.pojo.EasyUiTreeNode;
import com.e3shop.common.utils.E3Result;
import com.e3shop.content.service.ContentCategoryService;

/**
 * @author Hao
 * @date: 2019年1月4日 下午9:06:29 
 * @Description: 这个是后台管理内容分类的控制层
 */
@Controller
public class ContentCatController {
	
	
	/**
	 * @author: Hao
	 * @Description:初始化内容页面的 树
	 * @throws：
	 * @param id parentid
	 * @return
	 * @date: 2019年1月4日 下午9:15:29 
	 *
	 */
	@Autowired
	ContentCategoryService contentCategoryService;
	@RequestMapping(value="/content/category/list")
	@ResponseBody
	public List<EasyUiTreeNode> getEasyUiTreeNode(
			@RequestParam(defaultValue="0")Long id){//会自动转成你所想要的数据类型
		List<EasyUiTreeNode> list = contentCategoryService.getContentCategorybyId(id);
		return list;
	}
	
	
	//添加分类
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	//parentId:node.parentId,name:node.text
	public E3Result creatContentCategory(Long parentId,String name){
		E3Result result = contentCategoryService.creatContentNode(parentId, name);
		return result;
	}
	
	//删除
	@RequestMapping(value="/content/category/delete",method=RequestMethod.POST)
	public void deleteContentCategory(Long id,Long parentId){
		contentCategoryService.deleteContentCategory(id, parentId);
	}
	
	//Content商品内容信息详情
	@ResponseBody
	@RequestMapping(value="/content/query/list")
	public EasyUiDataDridResult getasyUiDataDridResult(
			@RequestParam(defaultValue="30") Long categoryId, 
			@RequestParam(defaultValue="1") Integer page,
			@RequestParam(defaultValue="10")Integer rows){
		EasyUiDataDridResult result = contentCategoryService.getContentListByCategoryId(categoryId, page, rows);
		return result;
	}
}
