package com.cj.core.service;

import java.util.List;

import com.cj.common.pojo.EasyUITreeNode;
import com.cj.common.pojo.TaotaoResult;

public interface ContentCatgoryService {
	
	/**
	 * 查询内容分类列表.
	 * @author cj
	 * @date 2016年10月6日下午11:34:09
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNode> getContentCatList(Long parentId);
	
	/**
	 * 新增一个内容分类.
	 * @author cj
	 * @date 2016年10月7日下午5:05:27
	 * @param parentId
	 * @param name
	 * @return
	 */
	TaotaoResult insertCategory(Long parentId,String name);
	
}
