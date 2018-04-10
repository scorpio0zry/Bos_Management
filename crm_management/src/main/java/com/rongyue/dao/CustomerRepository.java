package com.rongyue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.rongyue.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	List<Customer> findByFixedAreaIdIsNull();

	List<Customer> findByFixedAreaId(String fixedAreaId);
	
	@Query("update Customer set fixedAreaId = ?2 where id = ?1")
	@Modifying
	void updateFixedArea(Integer id, String fixedAreaId);
	
	@Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
	@Modifying
	void clearFixedArea(String fixedAreaId);

	Customer findByMobilePhone(String mobliePhone);
	
	@Query("update Customer set type = 1 where mobilePhone = ?")
	@Modifying
	void updateType(String mobliePhone);

	Customer findByEmail(String email);

	Customer findByMobilePhoneAndPassword(String mobilePhone, String password);
	
	@Query("select fixedAreaId from Customer where address = ?")
	String findFixedAreaIdByAddress(String address);
	
	
	
}
