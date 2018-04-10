package com.rongyue.web.base.action;



import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.domain.base.Standard;
import com.rongyue.service.base.StandardService;


public class StandardAction extends BaseAction<Standard>{
	
	@Autowired
	private StandardService standardService;
	
	@Action(value="standard_save",results={@Result(name="success",location="/pages/base/standard.html",type="redirect")})
	public String save(){
		standardService.save(model);
		return SUCCESS;
	}
	
	//分页查询
	@Action(value="standard_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Standard> pageData = standardService.pageQuery(pageable);
		pushPageToStack(pageData);
		return SUCCESS;
	}
	
	//取派员courier界面查询standard数据
	@Action(value="standard_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		java.util.List<Standard> list = standardService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	//作废和还原功能
	private String ids;
	private String flag;
	public void setIds(String ids) {
		this.ids = ids;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Action(value="standard_delTag",results={@Result(name="success",location="/pages/base/standard.html",type="redirect")})
	public String delTag(){
		String[] idArr = ids.split(",");
		Character deltag = flag.toCharArray()[0];
		standardService.updateDelTag(idArr,deltag);
		return SUCCESS;
	}
	

}
