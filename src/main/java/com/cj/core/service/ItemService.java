package com.cj.core.service;

import com.cj.common.pojo.EasyUIDataGridResult;
import com.cj.common.pojo.TaotaoResult;
import com.cj.core.pojo.TbItem;


public interface ItemService {
	//根据商品id查询商品信息.
	public TbItem getItemById(Long itemId);
	//分页显示商品列表,给datagrid提供数据.
	public EasyUIDataGridResult getItemList(int page,int rows);
	//添加商品,商品类目,商品描述
	public TaotaoResult createItem(TbItem item,String desc,String itemParams);
	//根据商品ID,获取商品类目名称
	String getItemParamHtml(Long itemId);
	

	
}
