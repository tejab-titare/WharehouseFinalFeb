package com.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.ISaleOrderDao;
import com.app.model.SaleOrder;
import com.app.service.ISaleOrderService;

@Service
public class SaleOrderServiceImpl implements ISaleOrderService {

	@Autowired
	private ISaleOrderDao dao;
	
	@Transactional
	public Integer saveSaleOrder(SaleOrder saleOrder) {
		return dao.saveSaleOrder(saleOrder);
	}
	
	@Transactional
	public void updateSaleOrder(SaleOrder saleOrder) {
		dao.updateSaleOrder(saleOrder);
	}

	@Transactional
	public void deleteSaleOrder(Integer saleOrderId) {
		dao.deleteSaleOrder(saleOrderId);
	}
	
	@Transactional(readOnly=true)
	public SaleOrder getOneSaleOrder(Integer saleOrderId) {
		return dao.getOneSaleOrder(saleOrderId);
	}
	
	@Transactional(readOnly=true)
	public List<SaleOrder> getAllSaleOrders() {
		return dao.getAllSaleOrders();
	}

	@Transactional(readOnly=true)
	public boolean isOrderCodeExist(String saleOrderCode) {
		return dao.isOrderCodeExist(saleOrderCode);
	}

}
