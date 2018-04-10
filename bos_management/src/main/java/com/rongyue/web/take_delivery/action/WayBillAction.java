package com.rongyue.web.take_delivery.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.domain.take_delivery.WayBill;
import com.rongyue.service.take_delivery.WayBillService;
import com.rongyue.utils.FileUtils;
import com.rongyue.web.base.action.BaseAction;


//运单
public class WayBillAction extends BaseAction<WayBill> {
	@Autowired
	private WayBillService wayBillService;

	// 运单的保存
	@Action(value = "waybill_save", results = { @Result(name = "success", type = "json") })
	public String save() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			wayBillService.save(model);
			map.put("success", true);
			map.put("msg", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "保存失败");
		}

		ActionContext.getContext().getValueStack().push(map);

		return SUCCESS;
	}

	// 分页显示运单数据信息
	@Action(value = "waybill_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Sort.Order(Direction.DESC, "id")));
		Page<WayBill> page = wayBillService.findAll(model,pageable);
		pushPageToStack(page);
		return SUCCESS;
	}
	
	//运单号异步从数据库加载信息
	@Action(value="waybill_findByWayBillNum",results={@Result(name="success",type="json")})
	public String findByWayBillNum(){
		Map<String, Object> map = new HashMap<String, Object>();
		WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
		if(wayBill != null){
			map.put("success", true);
			map.put("wayBillData", wayBill);
		}else{
			map.put("success", false);
		}
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	//导出Excel报表
	@Action(value="report_exportXls")
	public String exportXls(){
		List<WayBill> wayBills = wayBillService.findWayBills(model);
		//生成excel表格
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		HSSFSheet sheet = hssfWorkbook.createSheet();
		
		//表头
		HSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("运单号");
		headRow.createCell(1).setCellValue("寄件人");
		headRow.createCell(2).setCellValue("寄件人电话");
		headRow.createCell(3).setCellValue("寄件人地址");
		headRow.createCell(4).setCellValue("收件人");
		headRow.createCell(5).setCellValue("收件人电话");
		headRow.createCell(6).setCellValue("收件人地址");
		
		//表格数据
		for (WayBill wayBill : wayBills) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
			dataRow.createCell(0).setCellValue(wayBill.getWayBillNum());
			dataRow.createCell(1).setCellValue(wayBill.getSendName());
			dataRow.createCell(2).setCellValue(wayBill.getSendMobile());
			dataRow.createCell(3).setCellValue(wayBill.getSendAddress());
			dataRow.createCell(4).setCellValue(wayBill.getRecName());
			dataRow.createCell(5).setCellValue(wayBill.getRecMobile());
			dataRow.createCell(6).setCellValue(wayBill.getRecAddress());
		}
		
		//下载导出
		//设置头信息
		ServletActionContext.getResponse().setContentType("application/vnd.ms-excel");
		String filename = "运单数据.xls";
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		try {
			filename = FileUtils.encodeDownloadFilename(filename, agent);
			ServletActionContext.getResponse().setHeader("Content-Disposition",
					"attachment;filename=" + filename);

			ServletOutputStream outputStream = ServletActionContext.getResponse()
					.getOutputStream();
			hssfWorkbook.write(outputStream);

			// 关闭
			hssfWorkbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return NONE;
	}
	
}
