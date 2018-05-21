package com.cj.core.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cj.common.pojo.EasyUIDataGridResult;
import com.cj.common.pojo.TaotaoResult;
import com.cj.core.mapper.TbItemParamMapper;
import com.cj.core.pojo.TbItemParam;
import com.cj.core.pojo.TbItemParamExample;
import com.cj.core.pojo.TbItemParamExample.Criteria;
import com.cj.core.service.ItemParamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ItemParamServiceImpl implements ItemParamService {
	
	@Resource
	private TbItemParamMapper itemParamMapper;
	
	/**
	 * 商品规格参数的列表,用pagehelper分页插件来进行分页.
	 * @author cj
	 * @date 2016年9月25日下午11:09:15
	 * @param page 当前页(easyui)
	 * @param rows 页大小(easyUI)
	 * @return 用来封装前端easyUI的datagrid的所需数据的pojo.内有total和rows俩字段.分别封装总记录数,和当前页的数据集.
	 */
	@Override
	public EasyUIDataGridResult getList(int page, int rows) {
		//List<TbItemParamMapper> list2 = itemParamMapper.getList();
		//System.out.println(list2);
		//写入MyBatis的分页插件.用pagehelper插件来设置每页的当前页号和页大小.只对下一个查询出来的list起作用.
		PageHelper.startPage(page, rows);
		List<TbItemParam> list = itemParamMapper.getList();
		PageInfo pageInfo = new PageInfo(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		//封装前端easyui需要的两个数据.
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		
		return result;
	}
	
	 /**
	 * 根据cid,查询该类目的规格参数模板,是否已经存在了.
	 * 对应:item-param-add.jsp中,点击新增按钮后,选择类目后,该类目的规格参数模板,是否已经存在于数据库中,是个验证功能.
	 * @param cid
	 * @return 为AJAX写的响应pojo.
	 */
	@Override
	public TaotaoResult getItemParamByCid(Long cid) {
		TbItemParamExample example = new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(cid);
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
		//判断是否查询到了结果.
		if(list!=null && list.size()>0) { //表示查询到了结果.
			//拿出list中的第一个元素,即TbItemParam对象.
			TbItemParam tbItemParam = list.get(0);
			//把查询到的TbItemParam对象,塞给pojo.再返回.
			return TaotaoResult.ok(tbItemParam);
			
		}
		//如果没值,说明没有查到存在的模板,可以直接返回pojo.
		return TaotaoResult.ok();
	}
	
	/**
	 * 插入一个填写完的商品的规格参数模板.
	 * @param cid 前端传过来的商品类目ID.
	 * @param paramData 前端传过来的规格参数的数据.
	 * @return 自定义的pojo. 内含status响应码和msg响应信息.
	 */
	@Override
	public TaotaoResult insertItemParam(Long cid, String paramData) {
		//创建一个数据库实体pojo
		TbItemParam itemParam = new TbItemParam();
		//填充字段.
		//id不用手动生成,因为是自增长的.
		itemParam.setItemCatId(cid);
		itemParam.setParamData(paramData);
		itemParam.setCreated(new Date());
		itemParam.setUpdated(new Date());
		//把该对象插入数据库
		itemParamMapper.insert(itemParam);
		//返回响应pojo.
		/*	
		 *	那里会处理正常的响应信息,前端会用到.
		 * 	我只需要调用那边的ok()这个静态方法即可. 看:
		 * 	this.status = 200;
         *  this.msg = "OK";
		 */
		return TaotaoResult.ok();
	}
}
