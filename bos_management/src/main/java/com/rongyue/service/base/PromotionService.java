package com.rongyue.service.base;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rongyue.domain.PageBean;
import com.rongyue.domain.Promotion;

public interface PromotionService {

	void save(Promotion model);

	Page<Promotion> findAll(Pageable pageable);
	
	//webService 前台页面访问查询数据
	@Path("/findPageData")
	@GET
	@Produces({"application/xml","application/json"})
	public PageBean<Promotion> findPageData(@QueryParam("page") int page 
			, @QueryParam("rows") int rows);   //page当前页数  rows每页行数
	
	//webService 获取前台数据 promotion_detail
	@Path("/findById/{id}")
	@GET
	@Produces({"application/xml","application/json"})
	public Promotion findById(@PathParam("id") Integer id);

	void updateStatus(Date date);

}
