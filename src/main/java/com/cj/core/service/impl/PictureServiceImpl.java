package com.cj.core.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cj.common.pojo.PictureResult;
import com.cj.common.utils.FastDFSClient;
import com.cj.core.service.PictureService;

/**
 * 上传图片用service
 * @author 崔健
 * @date 2016年7月31日下午9:36:19
 */
@Service
public class PictureServiceImpl implements PictureService {
		
	//注入资源文件中的图片服务器的url (resource.properties)
	@Value("${IMAGE_SERVER_BASE_URL}")
	private String IMAGE_SERVER_BASE_URL;
	
	/**
	 * 上传图片
	 * @param picFile
	 * @return
	 * 2016年7月31日
	 */
	@Override //如果使用springmvc的"多媒体视图解析器"来处理上传,则参数类型必须为MultipartFile.
	public PictureResult uploadPic(MultipartFile picFile) {
		
		//实例化响应数据用的pojo
		PictureResult pictureResult = new PictureResult();
		//判断形参的值是否为空
		if(picFile.isEmpty()) {
			//如果为空,就出错了,就把pojo的error字段的值设为1. 这是KindEditor 富文本编辑器的要求.
			pictureResult.setError(1);
			//根据KindEditor的要求,失败时还要返回错误信息
			pictureResult.setMessage("图片为空");
			//返回这个封装了错误信息的响应pojo.
			return pictureResult;
		}
		
		try {
			//如果形参的值不为空.
			//取到传入图片的名称和扩展名,一会上传时得用.
			String fileName = picFile.getOriginalFilename();
			String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
			//实例化上传图片util对象.
			FastDFSClient client = new FastDFSClient("classpath:properties/client.conf");
			//FastDFSClient client = new FastDFSClient("F:\\Workspaces\\cjShop-back\\src\\main\\resources\\properties\\client.conf");
			//开始上传
			//第一参要求是String或字节数组.所以这里可以直接把传入的值转成字节数组.
			//这里返回的url,不是图片在服务器上的全url,只是后半部分,并没有前半部分:服务器的url
			//返回的是这样的://group1/M00/00/00/wKishled-weAdF_yAACiTJGvRtc274.jpg
			String url = client.uploadFile(picFile.getBytes(), extName);
			//上面从注解,注入了从spring容器中读取到的,资源文件中的配置,也就是图片服务器的url.
			//与上面返回的url拼接在一起,就是刚上传的图片的全部url了. 返回给前端,前端kindEditor固定需要这个属性,用来回显图片.
			url = IMAGE_SERVER_BASE_URL + url;
			
			//封装进响应pojo里.
			pictureResult.setUrl(url);
			pictureResult.setError(0);//成功时返回零,也是kindEditor的硬性要求.
			
		} catch (Exception e) {
			//如果上面报异常,则要给前端返回装有错误信息的响应pojo
			pictureResult.setError(1);
			pictureResult.setMessage("图片上传失败");
		}
		//返回响应pojo.
		return pictureResult;
	}

}





