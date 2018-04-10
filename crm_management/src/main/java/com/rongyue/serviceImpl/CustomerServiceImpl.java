package com.rongyue.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.crm.domain.Customer;
import com.rongyue.dao.CustomerRepository;
import com.rongyue.service.CustomerService;
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	
	//查询所有没有与定区关联的Customer对象
	@Override
	public List<Customer> findNoFixedArea() {
		return customerRepository.findByFixedAreaIdIsNull();
	}
	
	//查询已经关联到指定定区的客户列表
	@Override
	public List<Customer> findByFixedArea(String fixedAreaId) {
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}
	
	//将客户关联到定区 customerIds有“，”分割拼接，根据多个客户ID修改fixedAreaId的值
	@Override
	public void updateFixedArea(String customerIds, String fixedAreaId) {
		customerRepository.clearFixedArea(fixedAreaId);
		if("null".equals(customerIds)){
			return;
		}
		String[] idStr = customerIds.split(",");
		for (String id : idStr) {
			customerRepository.updateFixedArea(Integer.parseInt(id),fixedAreaId);
		}
	}
	
	//客户注册
	@Override
	public void register(Customer customer) {
		customerRepository.save(customer);
	}
	
	//查询手机号的邮箱是否已被激活
	@Override
	public Customer findByMobilePhone(String mobilePhone) {
		return customerRepository.findByMobilePhone(mobilePhone);
	}
	
	//将手机号的邮箱激活 type设置为1
	@Override
	public void updateType(String mobliePhone) {
		customerRepository.updateType(mobliePhone);		
	}
	
	//检验邮箱是否被注册
	@Override
	public Customer findByEmail(String email) {
		return customerRepository.findByEmail(email);
	}
	
	//登录页面校验用户是否存在
	@Override
	public Customer login(String mobilePhone, String password) {
		return customerRepository.findByMobilePhoneAndPassword(mobilePhone,password);
	}

	@Override
	public String findFixedAreaIdByAddress(String address) {
		return customerRepository.findFixedAreaIdByAddress(address);
	}

}
