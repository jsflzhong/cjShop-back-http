package com.cj.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cj.common.pojo.EasyUITreeNode;
import com.cj.common.pojo.TaotaoResult;
import com.cj.core.mapper.TbContentCategoryMapper;
import com.cj.core.pojo.TbContentCategory;
import com.cj.core.pojo.TbContentCategoryExample;
import com.cj.core.pojo.TbContentCategoryExample.Criteria;
import com.cj.core.service.ContentCatgoryService;

@Service
public class ContentCatgoryServiceImpl implements ContentCatgoryService {
	
	@Resource
	private TbContentCategoryMapper contentCategoryMapper;
	/**
	 * 查询内容分类列表.
	 * @author cj
	 * @date 2016年10月6日下午11:34:09
	 * @param parentId 父节点id
	 * @return List<EasyUITreeNode>
	 */
	@Override
	public List<EasyUITreeNode> getContentCatList(Long parentId) {
		//设置查询条件
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询. 查询所有父节点为传入的节点的数据.
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);

		//把查询结果,封装进响应用的pojo里:EasyUITreeNode对象.
		//然后把pojo,封装进集合. 因为前端要列表,那么这里肯定是数据集了.
		//创建列表集合.
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			//创建一EasyUITreeNode节点
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			//如果是true,说明它下面还有子节点. 那么它的状态应该是closed,因为不想每个父节点都展开了.
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			//添加到列表集合.
			resultList.add(node);
		}
		//返回集合.
		return resultList;
	}
	
	/**
	 * 新增一个内容分类.
	 * @author cj
	 * @date 2016年10月7日下午5:05:27
	 * @param parentId
	 * @param name
	 * @return
	 */
	@Override
	public TaotaoResult insertCategory(Long parentId, String name) {
		//创建一个pojo对象
		TbContentCategory contentCategory = new TbContentCategory();
		//补全字段.
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		//1(正常),2(删除)
		contentCategory.setStatus(1);
		//'该类目是否为父类目，1为true，0为false' 新建的节点,肯定是叶子节点.不是父类目.  
		contentCategory.setIsParent(false);
		//'排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数'
		//这个最简单,看表中实际的那些记录的此列的值,全是1.所以给个1完事.
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//调用mapper插入数据到数据库.
		contentCategoryMapper.insert(contentCategory);

		/**
		 注意,主键是数据库自增长,自己生成的.不用我们在这里填充.
		 但是,前端要用这个主键id,怎么办?
		  所以我要想办法取得新插入的对象的主键id.
		 注意,用MyBatis的这个"返回主键策略".
		
		 需要2步:
			 1>.在这里用刚才插入的对象,取id.
			 2>.还要去修改上面用到的mapper配置文件中的对应的SQL语句!!
		 */
		Long id = contentCategory.getId();

		/**
		 这里,要弄一下isparent这个字段.
		 因为: 当你插入一个节点A后,它的isparent为0.就是说,它不是父节点,是叶子节点.
		      然后,你想在这个节点A下,右键添加一个节点B.就会出问题. 因为,A的isparent依然是0
		 所以,在这里,用内层子节点的parentId,找到他的父节点.因为子节点的parentId,就是父节点的id.
		 然后,手动把父节点的isparent字段的值,改成true. 手动使它成为一个父节点,就好了.                       
		 */
		//判断父节点的isparent属性
		//查询父节点
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
		//如果是叶子节点.
		if (!parentNode.getIsParent()) {
			parentNode.setIsParent(true);
			//更新父节点
			contentCategoryMapper.updateByPrimaryKey(parentNode);
		}
		//返回pojo,把id传过去,返给前端
		return TaotaoResult.ok(id);
	}
	
	
	
}
