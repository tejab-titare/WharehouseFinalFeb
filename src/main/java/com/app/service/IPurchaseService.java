package com.app.service;

import java.util.List;

import com.app.model.Purchase;

public interface IPurchaseService {

	public Integer savePurchase(Purchase purchase);
	public void updatePurchase(Purchase purchase);
	public void deletePurchase(Integer orderId);
	public Purchase getPurchaseById(Integer orderId);
	public List<Purchase> getAllPurchases();
	public boolean isOrderCodeExist(String orderCode);
	public void deletePurchaseDtlById(Integer orderDtlId);
}
