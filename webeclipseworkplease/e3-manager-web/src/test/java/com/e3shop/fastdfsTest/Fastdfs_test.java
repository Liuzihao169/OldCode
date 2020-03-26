package com.e3shop.fastdfsTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class Fastdfs_test {
	@Test
	public void test1() throws FileNotFoundException, IOException, MyException{
		//1、加载配置文件，配置文件中的内容就是tracker服务的地址。
		ClientGlobal.init("F:\\webeclipseworkplease\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
		//配置文件内容：tracker_server=192.168.25.133:22122
		///2、创建一个TrackerClient对象。直接new一个。
		TrackerClient trackerClient = new TrackerClient();
		//3、使用TrackerClient对象创建连接，获得一个TrackerServer对象。
		TrackerServer trackerServer = trackerClient.getConnection();
		//4、创建一个StorageServer的引用，值为null
		StorageServer storageServer = null;
		//5、创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//、使用StorageClient对象上传图片。
		String[] upload_file = storageClient.upload_file("D:\\abc\\mm15315583017553897152.jpg", "jpg", null);
		//7、返回数组。包含组名和图片的路径。
		for (String string : upload_file) {
			System.out.println(string);
		}
	}
	@Test
	public void test2(){
		Map map=new HashMap();
		map.put("error", 1);
		map.put("error1", "input");
		System.out.println(map.get("error"));
		System.out.println(map.get("error1"));
		System.out.println(map);
	}
}
