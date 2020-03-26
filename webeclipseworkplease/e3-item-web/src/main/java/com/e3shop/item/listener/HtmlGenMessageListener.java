package com.e3shop.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.e3shop.item.pojo.Item;
import com.e3shop.pojo.TbItem;
import com.e3shop.pojo.TbItemDesc;
import com.e3shop.service.TbItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;
/**ItemInfo的监听，订阅消息，然后根据id查询完整商品详情
 * @author Hao
 * @date: 2019年1月30日 下午9:30:49 
 * @Description: 
 */
public class HtmlGenMessageListener implements MessageListener {
	@Autowired
	private TbItemService tbItemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	//生成文件所在的路径
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	public void onMessage(Message message) {
		try {
			//获得Configureer
			Configuration configuration =freeMarkerConfigurer.getConfiguration(); 
			//获得模板
			Template template = configuration.getTemplate("item.ftl");
			TextMessage textMessage =(TextMessage) message;
			String string = textMessage.getText();
			Long id = new Long(string);
			//根据id查询商品信息
			//等待数据库插入
			Thread.sleep(1000);
			TbItem tbItem = tbItemService.findByid(id);
			//生成所需要的Item
			Item item = new Item(tbItem);
			TbItemDesc descItem = tbItemService.getTbItemDescById(id);
			//生成数据data
			Map data =new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", descItem);
			//生成输出流
			Writer out=new FileWriter(new File(HTML_GEN_PATH+tbItem.getId()+".html"));
			//通过data生成模板文件
			template.process(data, out);
			//关闭流
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
