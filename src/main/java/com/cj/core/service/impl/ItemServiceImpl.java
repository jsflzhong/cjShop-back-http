package com.cj.core.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cj.common.pojo.EasyUIDataGridResult;
import com.cj.common.pojo.TaotaoResult;
import com.cj.common.utils.IDUtils;
import com.cj.common.utils.JsonUtils;
import com.cj.core.mapper.TbItemDescMapper;
import com.cj.core.mapper.TbItemMapper;
import com.cj.core.mapper.TbItemParamItemMapper;
import com.cj.core.mapper.TbItemParamMapper;
import com.cj.core.pojo.TbItem;
import com.cj.core.pojo.TbItemDesc;
import com.cj.core.pojo.TbItemExample;
import com.cj.core.pojo.TbItemExample.Criteria;
import com.cj.core.pojo.TbItemParamItem;
import com.cj.core.pojo.TbItemParamItemExample;
import com.cj.core.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 商品的service
 * @author 崔健
 * @date 2016年7月31日上午12:16:49
 */
@Service
public class ItemServiceImpl implements ItemService {
	
	@Resource 
	private TbItemMapper itemMapper;
	@Resource
	private TbItemDescMapper itemDescMapper;
	@Resource
	private TbItemParamItemMapper itemParamItemMapper;
	
	
	
	/**
	 * 根据商品ID查询商品
	 * @param itemId
	 * @return
	 * 2016年7月31日
	 */
	@Override
	public TbItem getItemById(Long itemId) {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		TbItem item = null;
		if(list !=null && list.size()>0) {
			item = list.get(0);
		}
		return item;
	}
	
	/**
	 * 分页显示商品列表
	 * @param page
	 * @param rows
	 * @return
	 * 2016年7月31日
	 */
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {	
		
		//固定先实例化一个pagehelper的对象
		//错误:这里直接调用PageHelper的静态方法,设置当前页和页大小! 不用实例化PageHelper对象! 别忘记设置!
		PageHelper.startPage(page, rows);
		//查出TbItem表的所有的数据.
		TbItemExample example = new TbItemExample();
		example.setOrderByClause("created DESC");
		List<TbItem> list = itemMapper.selectByExample(example);
		//用返回的数据集,实例化一个PageInfo对象. 该对象也是PageHelper包里的.
		PageInfo<TbItem> pageInfo = new PageInfo(list);
		//可以直接get到很多很多的分页相关数据了. 多到你用不到!
		long total = pageInfo.getTotal();
		
		//封装进给datagrid返回的pojo里.
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
		easyUIDataGridResult.setRows(list);
		easyUIDataGridResult.setTotal(total);
		
		return easyUIDataGridResult;
	}
	
	/**
	 * 新增商品.(需要向商品表,商品类目表,商品描述表这三张表插入数据)
	 * @author cj
	 * @param item
	 * @param desc
	 * @return
	 * 2016年9月24日
	 */
	@Override
	public TaotaoResult createItem(TbItem item, String desc,String itemParams) {
		
		// 1.商品表tb_item的内容.............................................
		long itemId = IDUtils.genItemId();
		// 补全TbItem属性.........
		// 补全id.
		item.setId(itemId);
		// 补全status
		// 补全这个之前,应该把表中,这个字段的注释拷贝过来,便于对应查看.
		// '商品状态，1-正常，2-下架，3-删除'
		item.setStatus((byte) 1);
		// 补全创建时间和更新时间.
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		
		// 调用itemMapper, 插入商品表
		itemMapper.insert(item);
		
		
		// 2.商品描述表tb_item_desc的内容..........................................
		// 创建商品描述对象.
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全字段......
		// 补全主键id,就是上面的商品id.
		itemDesc.setItemId(itemId);
		// 补全商品描述信息,从形参传入的.
		itemDesc.setItemDesc(desc);
		// 补全俩时间,上面拿到了date时间.
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);	
		// 调用上面注入的mapper,插入商品描述数据
		itemDescMapper.insert(itemDesc);
		
		//3.商品规格参数表tb_item_param_item的内容.......................................
		//创建规格参数表的pojo
		TbItemParamItem tbItemParamItem = new TbItemParamItem();
		//开始填充字段
		tbItemParamItem.setItemId(itemId); //id已经在上面用工具了生成了.
		tbItemParamItem.setParamData(itemParams); //传入的.
		tbItemParamItem.setCreated(date); //上面生成的.
		tbItemParamItem.setUpdated(date);
		//调用新注入的规格参数表的mapper,插入pojo.
		itemParamItemMapper.insert(tbItemParamItem);
		
		//返回pojo对象
		//调用的pojo的静态方法:ok().
		//这个方法,会在体内调用单参数构函,new一个TaotaoResult对象,并且把它的data字段初始化为Null.
		//因为这次,前台不需要data这个数据.
		//去看这个pojo即知.
		return TaotaoResult.ok();
	}
	
	/**
	 * 根据商品id,查找商品的规格参数.
	 * 并拼接成html供前端显示.
	 * @param itemId 前端传到controller的商品id.
	 * @return 拼接好的html标签.
	 */
	@Override
	public String getItemParamHtml(Long itemId) {
		//根据商品id查询规格参数
		TbItemParamItemExample example = new TbItemParamItemExample();
		com.cj.core.pojo.TbItemParamItemExample.Criteria criteria = example.createCriteria();
		//添加查询条件. 这个写法这次记住了吧.
		criteria.andItemIdEqualTo(itemId);
		//调用规格参数表的mapper,执行查询. 查的是tb_item_param_item表.
		/**
		 注意!! 要用selectByExampleWithBLOBs()方法,才能取到表中的param_data这一列数据!
		 不要用简单的selectByExample();以前说过,这个方法不取某些列. 看SQL就知道了.
		 */
		List<TbItemParamItem>list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		//判断,如果没查到.
		if (list == null || list.isEmpty()) {
			//返回空串即可.
			return"";
		}
		//如果查到了.
		//取规格参数. 上面返回的list,因为是根据商品id查的,所以list里面只会有一条记录.
		//返回规格参数对象. 因为上面调用的是规格参数表的mapper执行查询的.
		TbItemParamItem itemParamItem = list.get(0);
		//取对象中的paramData字段,是个json字符串.
		//private String paramData;
		String paramData = itemParamItem.getParamData();
		//把JSON字符串,转换成java对象. 使用工具类.
		//转成List. 第二参是,List中每个元素的类型. JSON字符串是用key:value形式的,所以用Map是很OK的.
		List<Map> mapList = JsonUtils.jsonToList(paramData, Map.class);
		
		//遍历list,生成html........................
		
		/**
		 技巧: 
		 	    看京东手机的规格参数,看源码,是html的格式. 复制到sublime中. 
		 	    点中sublime最右下角的java字样,选中html格式.
		 	    然后: Edit--tag--Auto-Format,把这段html格式化一下.(需要插件)
		 	    只留一组规格组和规格项的标签,用于循环.
		 转义:
		 	    由于引号必须得转义, 所以,我们可以利用navicat数据库软件,来转义.
		 	    把上面处理完的html代码片段,复制到navicat中的"查询"中.
		 	    划上这段html代码,右键--含引号复制--java/C#
		 	    就自动把这段html给转义了.
		 拼接:
		 	     把转义完的html代码,复制回sublime,shift+右键,选中最左边一列,(或ctrl+alt+下)(或按滚轮)
		 	     由于要使用stringbuffer,所以:
		 	     输入:"sb.append(" (不带引号)
		 	     按end键,到最右边一列后,
		 	     输入:退格键,去掉加号,和加号左边的空格,
		 	     输入:");"(不带引号)
		 	     然后,在最后一行,的");"左边,加上">""(尖括号和一个双引号)
		 粘贴:
		 	    把上面生成的代码,粘贴过来到下面.	   
		 */
		//先创建一个stringbuffer,用来放拼接的html.
		StringBuffer sb = new StringBuffer();
		
		//这里是头两行,不需要循环.
		sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
		sb.append("	<tbody>\n");
		
		//循环迭代上面用JSON转换后的List.
		for (Map map : mapList) {
			//注意,循环应该只针对<tr>..</tr>这里面的内容........
			sb.append("		<tr>\n");
			
			//取规格参数的"规格组"的值......
			/**
			应该从迭代的,从JSON转成的list中的元素(Map)中,动态拿. 但是map中的key是啥?
			我们就要去看tb_item_param_item表中,param_data这列的JSON数据格式了.
			在该表中,的param_data列中,拷贝出一条JSON数据来.
			打开HiJson v2.1.2(终于又用到了),把上面的JSON数据复制进去,点击工具栏中的"格式化".
			就能清晰的看到,表中这列JSON数据的"数据格式"了. 规格组的key是"group",规格项的key是"params".
			*/
			sb.append("			<th class=\"tdTitle\" colspan=\"2\">"+map.get("group")+"</th>\n");
			
			sb.append("		</tr>\n");
			
			//取规格参数的"规格项"的值......
			//由于规格项有很多,看数据结构,规格项的值,也是个List数组[].这个List中,也是map{}.
			//即:规格项的值,是[{}]这种结构的,即是List<Map>了.
			List<Map> mapList2 = (List<Map>) map.get("params"); //先取出所有的规格项的值.
			//循环迭代规格项们.
			for (Map map2 : mapList2) {
				//每迭代出一个规格项,就添加一组<tr></tr>
				sb.append("		<tr>\n");
				//动态拿"品牌". 去看HiJson软件,里面的数据结构很清晰. map里的key是"k"
				sb.append("			<td class=\"tdTitle\">"+map2.get("k")+"</td>\n");
				//动态拿"型号". 去看HiJson软件,里面的数据结构很清晰. map里的key是"v"
				sb.append("			<td>"+map2.get("v")+"</td>\n");
				sb.append("		</tr>\n");
			}
		}
		
		//这里是尾两行,不需要循环.
		sb.append("	</tbody>\n");
		sb.append("</table>");
		
		//最后,这个stringbuffer里面,就装有一个完整的html片段了.
		//返回它即可.
		return sb.toString();
	}
	
	

}










