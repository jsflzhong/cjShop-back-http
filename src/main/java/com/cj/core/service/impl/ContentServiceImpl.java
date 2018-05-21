package com.cj.core.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cj.common.pojo.TaotaoResult;
import com.cj.core.mapper.TbContentMapper;
import com.cj.core.pojo.TbContent;
import com.cj.core.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {
	
	@Resource
	private TbContentMapper contentMapper;
	
	/**
	 * 新增内容
	 * @author cj
	 * @param content
	 * @return
	 * 2016年10月7日
	 */
	@Override
	public TaotaoResult insertContent(TbContent content) {
		//补完两个时间属性.
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入数据.
		contentMapper.insert(content);
		//返回pojo.
		return TaotaoResult.ok();
	}

}
