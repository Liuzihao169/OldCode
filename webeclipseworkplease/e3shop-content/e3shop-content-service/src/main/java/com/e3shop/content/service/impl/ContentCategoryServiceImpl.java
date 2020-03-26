package com.e3shop.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3shop.common.pojo.EasyUiDataDridResult;
import com.e3shop.common.pojo.EasyUiTreeNode;
import com.e3shop.common.utils.E3Result;
import com.e3shop.content.service.ContentCategoryService;
import com.e3shop.mapper.TbContentCategoryMapper;
import com.e3shop.mapper.TbContentMapper;
import com.e3shop.pojo.TbContent;
import com.e3shop.pojo.TbContentCategory;
import com.e3shop.pojo.TbContentCategoryExample;
import com.e3shop.pojo.TbContentCategoryExample.Criteria;
import com.e3shop.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	@Autowired
	private TbContentMapper tbContentMapper;
	
	/**
	 * @author: Hao
	 * @Description:
	 * @throws：
	 * @param id 传过来的parentId
	 * @return EasyUiTreeNodelsit
	 * @date: 2019年1月4日 下午8:49:19 
	 *
	 */
	public List<EasyUiTreeNode> getContentCategorybyId(Long id) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		List<EasyUiTreeNode>result = new ArrayList<EasyUiTreeNode>();
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		for (TbContentCategory tbContentCategory : list) {
			EasyUiTreeNode easyUiTreeNode = new EasyUiTreeNode();
			easyUiTreeNode.setId(tbContentCategory.getId());
			easyUiTreeNode.setText(tbContentCategory.getName());
			easyUiTreeNode.setState(tbContentCategory.getIsParent()?"closed":"open");
			result.add(easyUiTreeNode);
		}
		return result;
	}

	/**
	 * @author: Hao
	 * @Description: 创建一个商品分类
	 * @throws：
	 * @param parentId 父id
	 * @param text
	 * @return
	 * @date: 2019年1月15日 下午7:36:51 
	 *生成分类的节点
	 */
	@Override
	public E3Result creatContentNode(Long parentId, String text) {
		// 这个就是要封装一个ContentCategory的pojo 
		//创建一个ContentCategory 
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setParentId(parentId);
		//状态。可选值:1(正常),2(删除)',
		tbContentCategory.setStatus(1);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		//一定不是父节点
		tbContentCategory.setIsParent(false);
		tbContentCategory.setName(text);
		//保存，以为修改了，代码 增加了返回ID的语句，插入tbContentCategory之后会有一个ID返回到改对象当中
		/*<selectKey order="AFTER" keyProperty="id" resultType="long" >
  		select last_insert_id()
  		</selectKey>*/
		tbContentCategoryMapper.insert(tbContentCategory);
		//判断他的父是否子节点 如果是子节点 然后变成父节点
		TbContentCategory selectByPrimaryKey = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!selectByPrimaryKey.getIsParent()){
			selectByPrimaryKey.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKey(selectByPrimaryKey);
		}
		return new E3Result(tbContentCategory);
	}

	@Override
	public void deleteContentCategory(Long id, Long parent) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parent);
		//根据id删除 然后判断有没有兄弟节点 如果没有那么父节点要变成叶子节点
		int count = tbContentCategoryMapper.countByExample(example);
		if(count==1){
			TbContentCategory selectByPrimaryKey = tbContentCategoryMapper.selectByPrimaryKey(parent);
			selectByPrimaryKey.setIsParent(false);
			tbContentCategoryMapper.updateByPrimaryKey(selectByPrimaryKey);
		}
		tbContentCategoryMapper.deleteByPrimaryKey(id);
	}


	
	/**
	 * @author: Hao
	 * @Description:内容管理的分页，根据
	 * @throws：
	 * @param CategoryId
	 * @param page
	 * @param rows
	 * @return
	 * @date: 2019年1月18日 下午6:59:16 
	 *
	 */
	@Override
	public EasyUiDataDridResult getContentListByCategoryId(Long CategoryId, Integer page, Integer rows) {
		/*
		 * 	//查询之前进行包装 设置查找的起始页数和每页的条数
		if(page==null){
			//默认参数初始化
			page=1;
			rows=10;
		}
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		PageInfo <TbItem>pageInfo = new PageInfo<TbItem>(list);
		EasyUiDataDridResult itemResult = new EasyUiDataDridResult();
		itemResult.setRows(pageInfo.getList());
		itemResult.setTotal(pageInfo.getTotal());
		return itemResult;*/
	
		PageHelper.startPage(page, rows);
		TbContentExample contentExample = new TbContentExample();
		com.e3shop.pojo.TbContentExample.Criteria createCriteria = contentExample.createCriteria();
		createCriteria.andCategoryIdEqualTo(CategoryId);
		List<TbContent> list = tbContentMapper.selectByExample(contentExample);
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		EasyUiDataDridResult ContentResult = new EasyUiDataDridResult();
		ContentResult.setTotal(pageInfo.getTotal());
		ContentResult.setRows(pageInfo.getList());
		return ContentResult;
	}
	
}
