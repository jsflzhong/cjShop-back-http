package com.cj.core.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cj.common.pojo.TaotaoResult;
import com.cj.core.pojo.TbContent;
import com.cj.core.service.ContentService;

@Controller
@RequestMapping(value="/content")
public class ContentController {
	
	@Resource
	private ContentService contentService;
	
	/**
	 * 新增内容
	 * @author cj
	 * @date 2016年10月7日下午8:01:42
	 * @param content
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult insertContent(TbContent content) {
		 TaotaoResult result = contentService.insertContent(content);
		 return result;
	}
}
