package com.rongyue.action.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.aspectj.util.FileUtil;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.constants.Constants;
import com.rongyue.domain.PageBean;
import com.rongyue.domain.Promotion;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class PromotionAction extends BaseAction<Promotion>{
	//将promotion数据展示在前台页面上
	private Integer page;
	private Integer pageSize;
	
	public void setPage(Integer page) {
		this.page = page;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	@SuppressWarnings("all")
	@Action(value="promotion_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		PageBean<Promotion> pageBean = WebClient.create(Constants.BOS_MANAGEMENT_URL + "/services/promotionService/findPageData?page="+
				page+"&rows="+pageSize).accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("totalCount", pageBean.getTotalCount());
		map.put("pageData", pageBean.getPageData());
		
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	//Freemarker生成宣传活动的静态页面
	@Action(value="promotion_showDetail")
	public String showDetail(){
		String htmlRealPath = ServletActionContext.getServletContext().getRealPath("/freemaker");
		File htmlFile = new File(htmlRealPath + "/" + model.getId() + ".html");
		
		try {
			//如果html文件不存在，查询数据库，生成freemarker静态页面
			if(!htmlFile.exists()){
				Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
					configuration.setDirectoryForTemplateLoading(new File(
						ServletActionContext.getServletContext().getRealPath("/WEB-INF/template")));
				//获取模板对象
				Template template = configuration.getTemplate("promotion_detail.ftl");
				//获取数据
				Promotion promotion = WebClient.create(Constants.BOS_MANAGEMENT_URL + "/services/promotionService/findById/"+model.getId())
					.accept(MediaType.APPLICATION_JSON).get(Promotion.class);
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("promotion", promotion);
				//合并输出
				template.process(map, new OutputStreamWriter(new FileOutputStream(htmlFile),"utf-8"));
				}
			//将文件返回
			ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return NONE;
	}
}
