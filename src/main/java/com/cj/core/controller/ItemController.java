package com.cj.core.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cj.common.pojo.EasyUIDataGridResult;
import com.cj.common.pojo.TaotaoResult;
import com.cj.core.pojo.TbItem;
import com.cj.core.service.ItemService;


/**
 * 商品的controller
 * @author 崔健
 * @date 2016年7月31日上午12:16:34
 */
@Controller
public class ItemController {
	@Resource
	private ItemService itemService;
	
	
	/**
	 * 根据商品ID查询商品.返回JSON.
	 * @param itemId
	 * @return
	 * 2016年7月31日
	 * 测试:localhost:8080/item/738388  (id是从数据库中查的)
	 */
	@RequestMapping(value="/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem item = itemService.getItemById(itemId);
		return item;
	}
	
	@RequestMapping(value="/item/list")
	@ResponseBody
	//返回JSON时,返回格式直接写要返回的pojo,注解会帮我把它自动转成JSON.(返回JSON的2种方式)
	public EasyUIDataGridResult getItemList(Integer page,Integer rows) {
		EasyUIDataGridResult easyUIDataGridResult = itemService.getItemList(page, rows);
		//直接返回这个对象,jackson就能帮我把它转成JSON.
		return easyUIDataGridResult;
		
	}
	
	/**
	 * 新增商品handler.
	 * @author cj
	 * @date 2016年9月24日下午11:51:00
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createItem(TbItem item, String desc,String itemParams) {
		//调用service的方法,传参,返回pojo.封装了响应信息.
		TaotaoResult result = itemService.createItem(item, desc,itemParams);
		
		//返回这个封装了响应信息的pojo.
		return result;
	}
	
	/**
	 * 4.根据商品id,查找商品的规格参数
	 * 对应: 
	 * @param itemId 商品id.
	 * @param model 给前端响应数据用的.
	 * @return 逻辑视图.
	 */
	@RequestMapping("/page/item/{itemId}")
	public String showItemParam(@PathVariable Long itemId, Model model) {
		String html = itemService.getItemParamHtml(itemId);
		//用Model对象,把在service层拼接到的html,放进域对象.供前台调用.
		model.addAttribute("myhtml", html);
		//返回的是啥? 应该是一个jsp页面的名字.
		//这是一个逻辑视图,应该在springmvc的配置那边,用视图解析器,拼接上前后缀.
		//注意,现在前端还没有这个jsp,那边还得新建!
		return"itemParam";
	}

}
