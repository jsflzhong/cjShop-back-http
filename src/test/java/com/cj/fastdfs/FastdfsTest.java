package com.cj.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.cj.common.utils.FastDFSClient;


/**
 * 测试上传图片到 Fastdfs+nginx 图片服务器.
 * 
 * @author 崔健
 * @date 2016年7月31日下午8:33:16
 */
public class FastdfsTest {
	
	//测试手动上传文件
	@Test
	public void testUpLoad() throws Exception {
		// 1、把FastDFS提供的jar包添加到工程中
		// 2、初始化全局配置。加载一个配置文件. 拷贝配置文件的全路径,并换双斜杠.
		// 右键这个配置文件,看属性里,有全路径.
		ClientGlobal.init( "F:\\Workspaces\\cjShop-back\\src\\main\\resources\\properties\\client.conf");

		// 3、创建一个TrackerClient对象。
		TrackerClient trackerClient = new TrackerClient();

		// 4、创建一个TrackerServer对象。用TrackerClient对象来创建.
		TrackerServer trackerServer = trackerClient.getConnection();
		if (trackerServer == null) {
			System.out.println("trackerServer is null......");
		} else {
			System.out.println("trackerServer is ..." + trackerServer);
		}

		// 5、声明一个StorageServer对象，null。
		StorageServer storageServer = null;

		// 6、获得StorageClient对象。 把上面俩server塞进来.
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		// 7、直接调用StorageClient对象方法上传文件即可。
		// 参数1:本地图片的路径,双斜杠哦.注意要写到图片名为止.
		// 参数2:该图片的扩展名.不要包含点.
		// 参数3:图片的属性,一般为null
		// 返回一个数组.
		// 这行报错...............socket time out exception
		String[] strings = storageClient.upload_file("D:\\666.jpg", "jpg", null);
		// 迭代看看返回的数组是啥
		for (String string : strings) {
			System.out.println(string);
		}
	}
	
	//测试用util工具类上传文件.
	@Test
	public void testFastDfsClient() throws Exception {
		FastDFSClient client = new FastDFSClient("F:\\Workspaces\\cjShop-back\\src\\main\\resources\\properties\\client.conf");
		String uploadFile = client.uploadFile("D:\\666.jpg", "jpg");
		System.out.println(uploadFile);
		//group1/M00/00/00/wKishled-weAdF_yAACiTJGvRtc274.jpg
		//注意,返回的图片地址,都是从组Id开始的.并没有前面图片服务器的url和端口的部分!
	}
	
	

}
