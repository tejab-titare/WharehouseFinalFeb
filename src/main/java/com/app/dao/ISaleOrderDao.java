package com.app.dao;

import java.util.List;

import com.app.model.SaleOrder;

public interface ISaleOrderDao {
	
	public Integer saveSaleOrder(SaleOrder saleOrder);
	public void updateSaleOrder(SaleOrder saleOrder);
	public void deleteSaleOrder(Integer saleOrderId);
	public SaleOrder getOneSaleOrder(Integer saleOrderId);
	public List<SaleOrder> getAllSaleOrders();
	public boolean isOrderCodeExist(String saleOrderCode);

}
