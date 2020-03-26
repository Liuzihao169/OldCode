package com.e3shop.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.w3c.dom.ls.LSInput;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTest  {
	@Test
	public void testFreemarker() throws Exception{
		// 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本
		Configuration con = new Configuration(Configuration.getVersion());
		// 第二步：设置模板文件所在的路径。
		con.setDirectoryForTemplateLoading(new File("F:/webeclipseworkplease/e3-item-web/src/main/webapp/WEB-INF"));
		// 第三步：设置模板文件使用的字符集。一般就是utf-8.
		con.setDefaultEncoding("utf-8");
		// 第四步：通过configaction获得 加载一个模板，创建一个模板对象。
//		Template template =con.getTemplate("student.ftl");
		Template template =con.getTemplate("studentList.ftl");
		// 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
		Student student = new Student(12, "liuzihao", "山下刘家");
		Map map = new HashMap<>();
		//向数据集中添加数据
		map.put("hello", "this is my fistFreemarker");
		List<Student>list = new ArrayList<>();
		list.add(new Student(1, "小明1", "回容洞"));
		list.add(new Student(2, "小明2", "回容洞"));
		list.add(new Student(3, "小明3", "回容洞"));
		list.add(new Student(4, "小明4", "回容洞"));
		list.add(new Student(5, "小明5", "回容洞"));
		list.add(new Student(6, "小明6", "回容洞"));
		map.put("student", student);
		map.put("studentList",list);
		// 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
		Writer writer = new FileWriter(new File("D:/linux临时传输/freemarker/studentList.html")); 
		// 第七步：调用模板对象的process方法输出文件。
		template.process(map, writer);
		//freemarker模板的是文件格式推荐用ftl
		writer.close();
	}
}
