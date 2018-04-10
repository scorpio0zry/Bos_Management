package com.rongyue.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.rongyue.domain.base.Area;

public interface AreaRepository extends JpaRepository<Area, String> ,JpaSpecificationExecutor<Area>{
	@Query("delete from Area where id = ?")
	@Modifying
	void del(String id);

	Area findByProvinceAndCityAndDistrict(String province, String city, String district);
}
