package com.rongyue.web.base.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.constants.Constants;
import com.rongyue.crm.domain.Customer;
import com.rongyue.domain.base.FixedArea;
import com.rongyue.service.base.FixedAreaService;

public class FixedAreaAction extends BaseAction<FixedArea> {
	@Autowired
	private FixedAreaService fixedAreaService;

	// 定区存储
	@Action(value = "fixedarea_save", results = {
			@Result(name = "success", location = "pages/base/fixed_area.html", type = "redirect") })
	public String save() {
		fixedAreaService.save(model);
		return SUCCESS;
	}

	// 定区条件查询
	@Action(value = "fixedarea_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);
		Specification<FixedArea> specification = new Specification<FixedArea>() {
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				// 按照定区编码查找
				if (StringUtils.isNotBlank(model.getFixedAreaNum())) {
					Predicate p1 = cb.like(root.get("fixedAreaNum").as(String.class),
							"%" + model.getFixedAreaNum() + "%");
					list.add(p1);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Page<FixedArea> page = fixedAreaService.findAll(pageable, specification);
		pushPageToStack(page);
		return SUCCESS;
	}

	// 异步显示未关联用户
	@Action(value = "fixedarea_findNoFixedArea", results = { @Result(name = "success", type = "json") })
	public String findNoFixedArea() {
		Collection<? extends Customer> collection = WebClient
				.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/findnofixedarea")
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}

	// 异步显示已关联定区的用户
	@Action(value = "fixedarea_findByFixedArea", results = { @Result(name = "success", type = "json") })
	public String findByFixedArea() {
		Collection<? extends Customer> collection = WebClient.create(
				Constants.CRM_MANAGEMENT_URL + "/services/customerService/findbyfixedarea/" + model.getId())
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}

	// 将用户关联到选定定区
	private String[] customerIds;

	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}
	
	@Action(value = "fixedarea_updateFixedArea", results = {
			@Result(name = "success", location = "/pages/base/fixed_area.html", type = "redirect") })
	public String updateFixedArea() {
		String customerIdStr = StringUtils.join(customerIds, ",");
		WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/updatefixedarea?customerIds="
				+ customerIdStr + "&fixedAreaId=" + model.getId())
		.put(null);
		return SUCCESS;
	}
	
	//分区subarea添加定区中：查找所有定区
	@Action(value="fixedarea_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		List<FixedArea> list = fixedAreaService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	
}
