package com.rongyue.serviceimpl.base;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.CourierRepository;
import com.rongyue.dao.base.FixedAreaRepository;
import com.rongyue.dao.base.TakeTimeRepository;
import com.rongyue.domain.base.Courier;
import com.rongyue.domain.base.FixedArea;
import com.rongyue.domain.base.TakeTime;
import com.rongyue.service.base.CourierService;
@Service
@Transactional
public class CourierServiceImpl implements CourierService {
	@Autowired
	private CourierRepository courierRepository;
	
	/*分页查询取派员数据*/
	@Override
	public Page<Courier> findAll(Specification<Courier> specification,Pageable pageable) {
		return courierRepository.findAll(specification, pageable);
	}

	/*添加取派员*/
	@Override
	@RequiresPermissions("courier:add")
	public void save(Courier courier) {
		courierRepository.save(courier);
	}
	
	/*更改取派员是否作废状态*/
	@Override
	public void updateDelTag(String[] idArr, Character flag) {
		for (String id : idArr) {
			courierRepository.updateDelTag(Integer.parseInt(id),flag);
		}
		
	}
	
	//fixed_area页面关联快递员显示未选中定区的快递员
	@Override
	public List<Courier> findAll() {
		Specification<Courier> specification = new Specification<Courier>() {
			
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				return p;
			}
		};
		return courierRepository.findAll(specification);
	}

	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	//fixed_area页面，快递员关联定区和收派时间
	@Override
	public void updateCourier(String courierId, String fixedAreaId, String takeTimeId) {
		FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
		TakeTime takeTime = takeTimeRepository.findOne(Integer.parseInt(takeTimeId));
		Courier courier = courierRepository.findOne(Integer.parseInt(courierId));
		fixedArea.getCouriers().add(courier);
		
		courier.setTakeTime(takeTime);
		
	}

}
