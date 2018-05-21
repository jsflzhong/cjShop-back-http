package com.cj.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.springframework.stereotype.Service;

import com.cj.common.pojo.EasyUITreeNode;
import com.cj.core.mapper.TbItemCatMapper;
import com.cj.core.mapper.TbItemDescMapper;
import com.cj.core.pojo.TbItemCat;
import com.cj.core.pojo.TbItemCatExample;
import com.cj.core.pojo.TbItemCatExample.Criteria;
import com.cj.core.service.ItemCatService;
/**
 * 商品分类类目service.
 * @author 崔健
 * @date 2016年7月31日上午11:16:47
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Resource
	private TbItemCatMapper itemCatMapper;
	
	
	/**
	 * 根据parentId,查询商品分类类目.给前端的tree组件提供数据.
	 * @param parentId
	 * @return
	 * 2016年7月31日
	 */
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		
		//前端tree要求的响应数据格式是:[{}].所以,得返回个"集合包对象"的格式.
		//实例化一个自己的集合.准备塞pojo.
		List<EasyUITreeNode> resultList = new ArrayList<>();
		//迭代返回的集合,并封装进专用于tree的pojo中.
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			//前端要的id,就是分类id,表`tb_item_cat`的主键而已.
			easyUITreeNode.setId(tbItemCat.getId());
			//前端要的text,就是类目name,显示到tree型图上.
			easyUITreeNode.setText(tbItemCat.getName());
			//根据isparent字段,来判断状态.
			easyUITreeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			//往自己的List中塞入这个pojo
			resultList.add(easyUITreeNode);
		}
		return resultList;
	}

}







