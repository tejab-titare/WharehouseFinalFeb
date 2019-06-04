package com.app.service;

import java.util.List;

import com.app.model.SaleOrder;

public interface ISaleOrderService {
	
	public Integer saveSaleOrder(SaleOrder saleOrder);
	public void updateSaleOrder(SaleOrder saleOrder);
	public void deleteSaleOrder(Integer saleOrderId);
	public SaleOrder getOneSaleOrder(Integer saleOrderId);
	public List<SaleOrder> getAllSaleOrders();
	public boolean isOrderCodeExist(String saleOrderCode);

}
