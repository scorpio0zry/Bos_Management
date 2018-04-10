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

import com.rongyue.domain.base.Area;
import com.rongyue.domain.base.FixedArea;
import com.rongyue.domain.base.SubArea;
import com.rongyue.service.base.SubAreaService;

public class SubAreaAction extends BaseAction<SubArea> {
	@Autowired
	private SubAreaService areaService;

	// 添加分区信息
	@Action(value = "subarea_save", results = {
			@Result(name = "success", location = "/pages/base/sub_area.html", type = "redirect") })
	public String save() {
		areaService.save(model);
		return SUCCESS;
	}

	// 条件查询分区信息
	@Action(value = "subarea_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);
		Specification<SubArea> specification = new Specification<SubArea>() {
			@Override
			public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				// 如果区域area的省不为空
				Join<SubArea, Area> areaRoot = root.join("area", JoinType.INNER);
				if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getProvince())) {
					Predicate p1 = cb.like(areaRoot.get("province").as(String.class),
							"%" + model.getArea().getProvince() + "%");
					list.add(p1);
				}
				// 如果区域area的市不为空
				if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getCity())) {
					Predicate p2 = cb.like(areaRoot.get("city").as(String.class),
							"%" + model.getArea().getCity() + "%");
					list.add(p2);
				}
				// 如果区域area的区不为空
				if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getDistrict())) {
					Predicate p3 = cb.like(areaRoot.get("district").as(String.class),
							"%" + model.getArea().getDistrict() + "%");
					list.add(p3);
				}
				// 如果定区fixedarea编号不为空
				Join<SubArea, FixedArea> fixedAreaRoot = root.join("fixedArea", JoinType.INNER);
				if (model.getFixedArea() != null && StringUtils.isNotBlank(model.getFixedArea().getFixedAreaNum())) {
					Predicate p4 = cb.like(fixedAreaRoot.get("fixedAreaNum").as(String.class),
							"%" + model.getFixedArea().getFixedAreaNum() + "%");
					list.add(p4);
				}
				// 如果关键字不为空
				if(StringUtils.isNotBlank(model.getKeyWords())){
					Predicate p5 = cb.like(root.get("keyWords").as(String.class),"%" + model.getKeyWords() + "%");
					list.add(p5);
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Page<SubArea> page = areaService.pageQuery(pageable, specification);
		pushPageToStack(page);
		return SUCCESS;
	}
}
