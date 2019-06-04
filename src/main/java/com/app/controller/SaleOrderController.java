package com.app.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.app.model.SaleOrder;
import com.app.service.ISaleOrderService;
import com.app.service.IShipmentTypeService;
import com.app.service.IWhUserTypeService;
import com.app.validator.SaleOrderValidator;
import com.app.view.SaleOrderExcelView;
import com.app.view.SaleOrderPdfView;

@Controller
@RequestMapping("/sale")
public class SaleOrderController {

	@Autowired
	private ISaleOrderService saleOrderService;
	@Autowired
	private IShipmentTypeService shipmentTypeService;
	@Autowired
	private IWhUserTypeService whUserTypeService;
	@Autowired
	private SaleOrderValidator validator;

	@RequestMapping("/register")
	public String showRegister(ModelMap map) {
		map.addAttribute("saleOrder", new SaleOrder());
		map.addAttribute("whUserType", whUserTypeService.getAllWhUserByType("CUSTOMER"));
		map.addAttribute("shipmentType", shipmentTypeService.getEnableShipmentIdsAndCodes());
		return "SaleOrderRegister";
	}

	@RequestMapping("/insert")
	public String saveSaleOrder(@ModelAttribute SaleOrder saleOrder,
			Errors errors,
			ModelMap map) {
		validator.validate(saleOrder, errors);

		if (errors.hasErrors()) {
			map.addAttribute("message", "please check all fields !!");
		} else {
			map.addAttribute("message", "SaleOrder saved with Id : "+saleOrderService.saveSaleOrder(saleOrder));
			map.addAttribute("saleOrder", new SaleOrder());
		}
		map.addAttribute("whUserType", whUserTypeService.getAllWhUserByType("CUSTOMER"));
		map.addAttribute("shipmentType", shipmentTypeService.getEnableShipmentIdsAndCodes());
		return "SaleOrderRegister";
	}

	/*@RequestMapping("/viewAll")
	public String showAll(ModelMap map) {

		map.addAttribute("saleOrder", saleOrderService.getAllSaleOrders());
		return "SaleOrderData";
	}*/

	@RequestMapping("/view")
	public String viewOne(@RequestParam(required=false,defaultValue="0") Integer saleOrderId,ModelMap map) {

		String page=null;
		if (saleOrderId!=0) {
			map.addAttribute("saleOrder", saleOrderService.getOneSaleOrder(saleOrderId));
			page="SaleOrderView";
		} else {
			map.addAttribute("saleOrder", saleOrderService.getAllSaleOrders());
			page="SaleOrderData";
		}

		return page;
	}

	@RequestMapping("/delete")
	public String deleteOne(@RequestParam Integer saleOrderId,ModelMap map) {
		try {
			saleOrderService.deleteSaleOrder(saleOrderId);
			map.addAttribute("message", "One record deleted with Id : "+saleOrderId);
		} catch (Exception e) {
			map.addAttribute("message", saleOrderId+" is not found");
			e.printStackTrace();
		}
		return "SaleOrderData";
	}

	@RequestMapping("/edit")
	public String editOne(@RequestParam Integer saleOrderId,ModelMap map) {

		map.addAttribute("saleOrder", saleOrderService.getOneSaleOrder(saleOrderId));
		map.addAttribute("whUserType", whUserTypeService.getAllWhUserByType("CUSTOMER"));
		map.addAttribute("shipmentType", shipmentTypeService.getEnableShipmentIdsAndCodes());
		return "SaleOrderEdit"; 
	}

	@RequestMapping("/update")
	public String updateSaleOrder(@ModelAttribute SaleOrder saleOrder,Errors errors,ModelMap map) {

		saleOrderService.updateSaleOrder(saleOrder);
		map.addAttribute("message", "SaleOrder is updated succesfully");
		map.addAttribute("saleOrder", saleOrderService.getAllSaleOrders());
		return "SaleOrderData";
	}

	@RequestMapping("/excelExport")
	public ModelAndView excelExport(@RequestParam(required=false,defaultValue="0") 
	Integer saleOrderId,ModelMap map) {
		ModelAndView mv=null;
		if (saleOrderId!=0) {
			mv=new ModelAndView(new SaleOrderExcelView(), "saleOrder", Arrays.asList(saleOrderService.getOneSaleOrder(saleOrderId)));
		} else {
			mv=new ModelAndView(new SaleOrderExcelView(), "saleOrder", saleOrderService.getAllSaleOrders());
		}
		return mv;
	}
	@RequestMapping("/pdfExport")
	public ModelAndView pdfExport(@RequestParam(required=false,defaultValue="0") 
	Integer saleOrderId,ModelMap map) {
		ModelAndView mv=null;
		if (saleOrderId!=0) {
			mv=new ModelAndView(new SaleOrderPdfView(), "saleOrder", Arrays.asList(saleOrderService.getOneSaleOrder(saleOrderId)));
		} else {
			mv=new ModelAndView(new SaleOrderPdfView(), "saleOrder", saleOrderService.getAllSaleOrders());
		}
		return mv;
	}




}
