package com.rongyue.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.rongyue.domain.base.Standard;

public interface StandardRepository extends JpaRepository<Standard, Integer> {
	@Query("update Standard set deltag = ?2 where id = ?1")
	@Modifying
	void updateDelTag(int id, Character deltag);
	
	@Query("select s from Standard s where s.deltag = 0")
	@Modifying
	List<Standard> findDelTag();

}
