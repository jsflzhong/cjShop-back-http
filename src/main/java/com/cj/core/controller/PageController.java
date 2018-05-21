package com.cj.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转用controller
 * @author 崔健
 * @date 2016年7月31日上午2:26:53
 */
@Controller
public class PageController {
	
	
	/**
	 * 当访问服务器的ip+端口号时,跳转到这里,然后跳转到WEB-INF/index.jsp
	 * @return
	 * 2016年7月31日
	 */
	@RequestMapping(value="/")
	public String showIndex() {
		return "index";
	}
	
	/**
	 * 跳转到其他页面.
	 * @param page
	 * @return
	 * 2016年7月31日
	 */
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
	
}






