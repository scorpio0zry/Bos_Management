package bos_management;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rongyue.service.take_delivery.WayBillService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class Test {
	@Autowired
	private WayBillService wayBillService;

	@org.junit.Test
	public void test() {
		wayBillService.syncIndex();
	}

}
