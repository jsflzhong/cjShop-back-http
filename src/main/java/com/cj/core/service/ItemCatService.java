package com.cj.core.service;

import java.util.List;

import com.cj.common.pojo.EasyUITreeNode;


/**
 * 商品分类类目接口.
 * @author 崔健
 * @date 2016年7月31日上午11:16:47
 */
public interface ItemCatService {
	
	//查询商品类目,给tree组件提供数据
	public List<EasyUITreeNode> getItemCatList(long parentId);
	
}
