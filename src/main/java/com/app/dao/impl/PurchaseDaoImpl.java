package com.app.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.app.dao.IPurchaseDao;
import com.app.model.Purchase;
import com.app.model.PurchaseDtl;

@Repository
public class PurchaseDaoImpl implements IPurchaseDao {
	
	@Autowired
	private HibernateTemplate ht;
	
	public Integer savePurchase(Purchase purchase) {
		return (Integer) ht.save(purchase);
	}

	public void updatePurchase(Purchase purchase) {
		ht.update(purchase);
	}

	public void deletePurchase(Integer orderId) {
		ht.delete(new Purchase(orderId));
	}

	public Purchase getPurchaseById(Integer orderId) {
		return ht.get(Purchase.class, orderId);
	}

	public List<Purchase> getAllPurchases() {
		return ht.loadAll(Purchase.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public boolean isOrderCodeExist(String orderCode) {
		
		long count=0;
		String hql = "select count(orderCode) from "+Purchase.class.getName()+" where orderCode=?";
		List<Long> purchases=(List<Long>) ht.find(hql, orderCode);
		if (purchases!=null && !purchases.isEmpty()) {
			count=purchases.get(0);
		}
		return count>0?true:false;
	}
	@Override
	public void deletePurchaseDtlById(Integer orderDtlId) {
		ht.delete(new PurchaseDtl(orderDtlId));
	}

}
