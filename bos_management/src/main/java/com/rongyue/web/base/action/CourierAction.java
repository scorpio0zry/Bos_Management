package com.rongyue.web.base.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.domain.base.Courier;
import com.rongyue.domain.base.Standard;
import com.rongyue.service.base.CourierService;


public class CourierAction extends BaseAction<Courier>{
	
	@Autowired
	private CourierService courierService;


	/* 查询所有的取派员 */
	/*根据条件查询取派员，若没有条件参数传入则查询所有取派员*/
	@Action(value = "courier_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		Pageable pageable = new PageRequest(page-1, rows);
		// 封装条件查询对象 Specification
		Specification<Courier> specification = new Specification<Courier>() {
			// Root 用于获取属性字段，CriteriaQuery可以用于简单条件查询，CriteriaBuilder 用于构造复杂条件查询
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();   
				//简单单表查询
				//工号查询
				if(StringUtils.isNotBlank(model.getCourierNum())){
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), model.getCourierNum());
					list.add(p1);
				}
				//公司查询
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%");
					list.add(p2);
				}
				
				//快递员类型
				if(StringUtils.isNotBlank(model.getType())){
					Predicate p3 = cb.equal(root.get("type").as(String.class),model.getType());
					list.add(p3);
				}
				
				//复杂表单查询
				//使用Courier/root关联 Standard对象
				Join<Courier, Standard> standardRoot = root.join("standard", JoinType.INNER);
				if(model.getStandard() != null && StringUtils.isNotBlank(model.getStandard().getName())){
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class), model.getStandard().getName());
					list.add(p4);
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Page<Courier> pageData = courierService.findAll(specification,pageable);
		pushPageToStack(pageData);
		return SUCCESS;
	}
	
	/*添加取派员数据*/
	@Action(value = "courier_save",results = {@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String save(){
		model.setDeltag('0');
		courierService.save(model);
		return SUCCESS;
	}
	
	/*取派员作废和还原*/
	private String ids;
	private String flag;

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	@Action(value="courier_delTag",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String delTag(){
		String[] idArr = ids.split(",");
		Character deltag = flag.toCharArray()[0];
		courierService.updateDelTag(idArr,deltag);
		return SUCCESS;
	}
	
	//fixed_area页面关联快递员显示未选中定区的快递员
	@Action(value="courier_findNoFixedArea",results={@Result(name="success",type="json")})
	public String findNoFixedArea(){
		List<Courier> list = courierService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	//fixed_area页面，快递员关联定区和收派时间
	private String fixedAreaId;
	private String takeTimeId;
	private String courierId;
	
	public void setFixedAreaId(String fixedAreaId) {
		this.fixedAreaId = fixedAreaId;
	}
	
	public void setTakeTimeId(String takeTimeId) {
		this.takeTimeId = takeTimeId;
	}
	
	public void setCourierId(String courierId) {
		this.courierId = courierId;
	}

	@Action(value="courier_courierToFixedArea",results={@Result(name="success",location="pages/base/fixed_area.html",type="redirect")})
	public String courierToFixedArea(){
		courierService.updateCourier(courierId,fixedAreaId,takeTimeId);
		return SUCCESS;
	}

}
