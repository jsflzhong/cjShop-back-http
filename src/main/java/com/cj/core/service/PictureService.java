package com.cj.core.service;

import org.springframework.web.multipart.MultipartFile;

import com.cj.common.pojo.PictureResult;


/**
 * 上传图片用
 * @author 崔健
 * @date 2016年7月31日下午9:33:16
 */
public interface PictureService {
	
	//上传图片到Linux上的图片服务器
	public PictureResult uploadPic(MultipartFile picFile);
}
