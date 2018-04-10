package com.rongyue.web.transit.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.domain.transit.TransitInfo;
import com.rongyue.service.transit.TransitInfoService;
import com.rongyue.web.base.action.BaseAction;

public class TransitInfoAction extends BaseAction<TransitInfo>{
	@Autowired
	private TransitInfoService transitInfoService;
	
	private String wayBillIds;
	
	//创建运输配送信息
	public void setWayBillIds(String wayBillIds) {
		this.wayBillIds = wayBillIds;
	}

	@Action(value="transit_create",results={@Result(name="success",type="json")})
	public String create(){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			if(wayBillIds == null){
				throw new RuntimeException();
			}
			
			transitInfoService.createTransitInfo(wayBillIds);
			map.put("success", true);
			map.put("msg", "开启中转配送！");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "开启中转失败！");
		}
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
		
	}
	
	//显示运输管理信息
	@Action(value="transit_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<TransitInfo> list = transitInfoService.findPage(pageable);
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
}
