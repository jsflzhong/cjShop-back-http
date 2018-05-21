package com.cj.core.service;

import com.cj.common.pojo.EasyUIDataGridResult;
import com.cj.common.pojo.TaotaoResult;

public interface ItemParamService {

	/**
	 * 查询商品规格参数的列表,在service中用pagehelper插件完成分页.
	 * @author cj
	 * @date 2016年9月25日下午11:09:15
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGridResult getList(int page,int rows);
	
	 /**
	 * 根据cid,查询该类目的规格参数模板,是否已经存在了.
	 * 对应:item-param-add.jsp中,点击新增按钮后,选择类目后,该类目的规格参数模板,是否已经存在于数据库中,是个验证功能.
	 * @param cid
	 * @return 为AJAX写的响应pojo.
	 */
	TaotaoResult getItemParamByCid(Long cid);
	
	/**
	 * 插入一个填写完的商品的规格参数模板.
	 * @param cid 前端传过来的商品类目ID.
	 * @param paramData 前端传过来的规格参数的数据.
	 * @return 自定义的pojo. 内含status响应码和msg响应信息.
	 */
	TaotaoResult insertItemParam(Long cid,String paramData);
}
