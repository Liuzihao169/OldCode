package com.e3shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.e3shop.common.utils.FastDFSClient;
import com.e3shop.common.utils.JsonUtils;

@Controller
public class PictureController {
	@Value(value="${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	@RequestMapping(value="/pic/upload")
	@ResponseBody
	public String fileUpload(MultipartFile uploadFile){
		String filename = uploadFile.getOriginalFilename();
		String extName = FilenameUtils.getExtension(filename);
		Map map = new HashMap<>();
		try {
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			url =IMAGE_SERVER_URL+url;
			map.put("error", 0);
			map.put("url", url);
			return JsonUtils.objectToJson(map);
		} catch (Exception e) {
			map.put("error", 1);
			map.put("message", "图片上传失败");
			return JsonUtils.objectToJson(map);
		}
	}
}
