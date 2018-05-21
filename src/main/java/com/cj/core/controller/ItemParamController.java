package com.cj.core.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cj.common.pojo.EasyUIDataGridResult;
import com.cj.common.pojo.TaotaoResult;
import com.cj.core.service.ItemParamService;

@Controller
@RequestMapping(value="/item/param")
public class ItemParamController {
	
	@Resource
	private ItemParamService itemParamService;
	
	/**
	 * 分页显示规格参数模板.
	 * @author cj
	 * @date 2016年10月2日下午11:59:14
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public EasyUIDataGridResult getList(Integer page,Integer rows) {
		EasyUIDataGridResult result = itemParamService.getList(page, rows);
		return result;
		
	}
	
	/**
	 * 根据cid,查询该类目的规格参数模板,是否已经存在了.
	 * @author cj
	 * @date 2016年10月3日上午12:00:24
	 * @param cid
	 * @return
	 */
	@RequestMapping(value="/query/itemcatid/{cid}")
	@ResponseBody
	public TaotaoResult getItemCatByCid(@PathVariable Long cid) {
		TaotaoResult result = itemParamService.getItemParamByCid(cid);
		return result;
	}
	
	/**
	 * 插入一个填写完的商品的规格参数模板.
	 * 对应: item-param-add.jsp中的新增按钮,所打开的弹窗.
	 * @param cid  前端传过来的商品类目ID
	 * @param paramData  前端传过来的规格参数的数据.
	 * @return  自定义的pojo.内含status响应码和msg响应信息.
	 */
	@RequestMapping("/save/{cid}")
	@ResponseBody
	public TaotaoResult insertItemParam(@PathVariable Long cid, String paramData) {
		TaotaoResult result = itemParamService.insertItemParam(cid, paramData);
		return result;
	}
}
