package com.cj.core.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cj.common.pojo.PictureResult;
import com.cj.common.utils.JsonUtils;
import com.cj.core.service.PictureService;

/**
 * 图片上传用controller
 * @author 崔健
 * @date 2016年7月31日下午10:33:31
 */
@Controller
public class PictureController {
	
	@Resource
	private PictureService pictureService;
	
	@RequestMapping(value="/pic/upload")
	@ResponseBody
	//如果使用springmvc的"多媒体视图解析器"来处理上传,则参数类型必须为MultipartFile.
	//注意,一般来讲,这里应该直接返回PictureResult这个pojo的.用注解自动转换成JSON即可. 
	//但是,在"上传文件"功能里,如果返回对象类型,则会导致:"返回数据的兼容性"问题. 最好返回String类型.
	//BB项目中用的是void返回类型:public void uploadPic(@RequestParam(required = false) MultipartFile pic,HttpServletResponse response) {
	public String uploadFile(MultipartFile uploadFile) {
		PictureResult pictureResult = pictureService.uploadPic(uploadFile);
		//由于上面返回值是string类型了.所以这里调用引入的工具类,把上行返回的object转成JSON.
		String json = JsonUtils.objectToJson(pictureResult);
		//返回JSON字符串.
		return json;
	}
}
