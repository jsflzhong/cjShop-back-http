package com.cj.pageHelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cj.core.mapper.TbItemMapper;
import com.cj.core.pojo.TbItem;
import com.cj.core.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TestPageHelper {
	
	@Test //用junit运行测试...
	public void testPageHelper() throws Exception {
		//1、获得对应数据库表的对应的mapper的代理对象(从IOC容器中拿)
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class); //这个mapper只会从TbItem这个表中查数据.

		//2、设置分页(pageHelper分页插件首次亮相!) (静态方法哦.)
		PageHelper.startPage(1, 30); //第1页,每页显示30条.(就是前台传过来的page和rows!!!)
		//注意,这条用插件设置分页的语句,只对下面"第一条"select语句有效!!

		//3、执行查询
		/**
		调用逆向生成的example条件查询方法.
		example为查询条件. 
		如果你不设置条件,那么下面的方法就是:查询所有的数据.

		注意,逆向生成的select方法,只有两种:
		selectByPrimarykey(Long id);  //这个明显只能返回一条记录.
		itemMapper.selectByExample(example); //条件查询,可以返回很多条. 如果条件为空,则返回全部数据.等于selectAll.

		*/
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example); //查询所有的数据.

		//4、取分页后结果
		//用上面返回的list结果集,创建一个pageInfo对象.
		PageInfo<TbItem> pageInfo = new PageInfo(list);
		//总记录数.
		long total = pageInfo.getTotal();
		System.out.println("total:" + total);//3096
		//总页数.
		int pages = pageInfo.getPages();
		System.out.println("pages:" + pages);//104
		//页大小.
		int pageSize = pageInfo.getPageSize();
		System.out.println("pageSize:" + pageSize);//30
		
	}

}