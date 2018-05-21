package com.cj.core.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cj.common.pojo.EasyUITreeNode;
import com.cj.core.service.ItemCatService;

/**
 * 商品分类类目controller
 * @author 崔健
 * @date 2016年7月31日上午11:34:43
 */
@Controller
public class ItemCatController {
	
	@Resource
	private ItemCatService itemCatService;
	
	@RequestMapping(value="/item/cat/list")
	@ResponseBody
	//由于请求参数与接收参数名字不同,所以用请求参数注解,匹配一下,顺便再给个默认值.因为第一次加载tree时,应该没id传过来.注意字符串类型的值.
	public List<EasyUITreeNode> getItemCatList(@RequestParam(value="id",defaultValue="0") long parentId) {
		
		List<EasyUITreeNode> list = itemCatService.getItemCatList(parentId);
		//前端tree要求的响应数据的格式是:[{}].所以得返回一个list包着pojo,然后再用注解转成JSON返回即可!
		return list;
	}
}












