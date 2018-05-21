package com.cj.core.service;

import com.cj.common.pojo.TaotaoResult;
import com.cj.core.pojo.TbContent;

public interface ContentService {
	
	/**
	 * 新增内容
	 * @author cj
	 * @date 2016年10月7日下午7:58:16
	 * @param content
	 * @return
	 */
	TaotaoResult insertContent(TbContent content);
}
