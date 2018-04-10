package com.rongyue.action.web;


import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.constants.Constants;
import com.rongyue.crm.domain.Customer;
import com.rongyue.domain.base.Area;
import com.rongyue.domain.take_delivery.Order;
//订单相关处理
public class OrderAction extends BaseAction<Order>{
	/**
	 * 存储订单信息
	 */
	private String sendAreaInfo; //寄件人省市区信息
	private String recAreaInfo; //收件人省市区信息
	
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}
	
	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}
	
	@Action(value="order_add",results={@Result(name="success",location="index.html",type="redirect")})
	public String add(){
		//关联当前客户编号
		Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
		
		model.setCustomer_id(customer.getId());
		
		//将寄件人与收件人的省市区信息取出存入到order的sendArea和recArea中
		Area sendArea = new Area();
		String[] sendAreaData = sendAreaInfo.split("/");
		sendArea.setProvince(sendAreaData[0]);
		sendArea.setCity(sendAreaData[1]);
		sendArea.setDistrict(sendAreaData[2]);
		
		Area recArea = new Area();
		String[] recAreaData = recAreaInfo.split("/");
		sendArea.setProvince(recAreaData[0]);
		sendArea.setCity(recAreaData[1]);
		sendArea.setDistrict(recAreaData[2]);
		
		model.setSendArea(sendArea);
		model.setRecArea(recArea);
		
		
		//将信息传送到后台写入数据库
		WebClient.create(Constants.BOS_MANAGEMENT_URL + "/services/orderService/saveOrder")
			.type(MediaType.APPLICATION_JSON)
			.post(model);
		return  SUCCESS;
	}
}
