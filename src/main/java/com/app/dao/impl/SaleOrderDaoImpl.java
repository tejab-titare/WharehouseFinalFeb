package com.app.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.app.dao.ISaleOrderDao;
import com.app.model.SaleOrder;

@Repository
public class SaleOrderDaoImpl implements ISaleOrderDao {

	@Autowired
	private HibernateTemplate ht;
	
	public Integer saveSaleOrder(SaleOrder saleOrder) {
		return (Integer) ht.save(saleOrder);
	}

	public void updateSaleOrder(SaleOrder saleOrder) {
		ht.update(saleOrder);
	}

	public void deleteSaleOrder(Integer saleOrderId) {
		ht.delete(new SaleOrder(saleOrderId));
	}

	public SaleOrder getOneSaleOrder(Integer saleOrderId) {
		return ht.get(SaleOrder.class, saleOrderId);
	}

	public List<SaleOrder> getAllSaleOrders() {
		return ht.loadAll(SaleOrder.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public boolean isOrderCodeExist(String saleOrderCode) {

		long count=0;
		String hql="select count(saleOrderCode) from "+SaleOrder.class.getName()+" where saleOrderCode=?";
		List<Long> saleOrders=(List<Long>) ht.find(hql, saleOrderCode);
		if (saleOrders!=null && !saleOrders.isEmpty()) {
			count=saleOrders.get(0);
		}
		return count>0?true:false;
	}

}
