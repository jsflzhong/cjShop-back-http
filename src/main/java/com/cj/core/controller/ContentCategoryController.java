package com.cj.core.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cj.common.pojo.EasyUITreeNode;
import com.cj.common.pojo.TaotaoResult;
import com.cj.core.service.ContentCatgoryService;

@Controller
@RequestMapping(value="/content/category")
public class ContentCategoryController {
	@Resource
	private ContentCatgoryService contentCategoryService;

	/**
	 * 查询内容分类管理列表
	 * 返回一个能够支持前端easyui tree组件的list.
	 * 注意形参注解. 因为tree发来的是id,而pojo中的字段叫parentId(便于理解).不对应,无法自动封装.
	 * 所以我用@RequestParam注解,把两个名连接起来.
	 * 同时给个默认值,看数据库中的parentId,有0这个值. 这样就能保证形参parentId肯定能拿到值了.
	 * 因为前端的tree组件一般都会传id(parentId)过来. 但如果是父节点,则传空值.
	 * @param parentId 父节点id
	 * @return List<EasyUITreeNode>
	 * @author cj
	 */
	@ResponseBody
	@RequestMapping("/list")
	List<EasyUITreeNode> getContentCatList(@RequestParam(value="id",defaultValue="0")Long parentId) {
		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		return list;
	}
	
	/**
	 * 新建内容节点.
	 * @author cj
	 * @date 2016年10月7日下午5:56:05
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public TaotaoResult createNode(Long parentId, String name) {
		TaotaoResult result = contentCategoryService.insertCategory(parentId, name);
		return result;
	}
	
}
