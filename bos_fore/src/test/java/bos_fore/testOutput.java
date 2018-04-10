package bos_fore;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class testOutput {

	@Test
	public void test() throws IOException, TemplateException {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
		configuration.setDirectoryForTemplateLoading(new File("src/main/webapp/WEB-INF/template"));
		
		Template template = configuration.getTemplate("hello.ftl");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "Freemarker");
		map.put("msg", "框架");
		
		template.process(map, new PrintWriter(System.out));
	}

}
