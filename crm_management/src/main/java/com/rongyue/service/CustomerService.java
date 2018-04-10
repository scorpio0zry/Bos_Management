package com.rongyue.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.rongyue.crm.domain.Customer;

public interface CustomerService{
	//查询所有未关联定区的客户
	@Path(value="/findnofixedarea")
	@GET
	@Produces({"application/xml","application/json"})
	public List<Customer> findNoFixedArea();
	
	//查询已经关联到指定定区的客户列表
	@Path("/findbyfixedarea/{fixedareaid}")
	@GET
	@Produces({"application/xml","application/json"})
	public List<Customer> findByFixedArea(@PathParam("fixedareaid") String fixedAreaId);
	
	//将客户关联到定区
	@Path("/updatefixedarea")
	@PUT
	public void updateFixedArea(@QueryParam("customerIds") String customerIds,
			@QueryParam("fixedAreaId") String fixedAreaId);
	
	//客户注册
	@Path("/register")
	@POST
	@Consumes({"application/xml","application/json"})
	public void register(Customer customer);
	
	//查询手机号的邮箱是否已被激活
	@Path("/findByMobilePhone/{mobilePhone}")
	@GET
	@Produces({"application/xml","application/json"})
	public Customer findByMobilePhone(@PathParam("mobilePhone") String mobilePhone);
	
	//检验邮箱是否被注册
	@Path("/findByEmail/{email}")
	@GET
	@Produces({"application/xml","application/json"})
	public Customer findByEmail(@PathParam("email") String email);
	
	//将手机号的邮箱激活
	@Path("/updateType/{mobliePhone}")
	@POST
	public void updateType(@PathParam("mobilePhone") String mobilePhone);
	
	//登录页面校验用户是否存在
	@Path("/login")
	@GET
	@Produces({"application/xml","application/json"})
	public Customer login(@QueryParam("mobilePhone") String mobilePhone,
			@QueryParam("password") String password);
	
	//根据寄件人的地址查询定区编码
	@Path("/findFixedAreaIdByAddress/{address}")
	@GET
	public String findFixedAreaIdByAddress(@PathParam("address") String address);
}
