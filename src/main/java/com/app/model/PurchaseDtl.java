package com.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="podtl_tab")
public class PurchaseDtl {

	@Id
	@Column(name="dtl_id")
	@GeneratedValue(generator="purchaseDtl")
	@GenericGenerator(name="purchaseDtl",strategy="increment")
	private Integer orderDtlId;
	
	@Column(name="poHdr_Id")
	private Integer poHdrId;

	@Column(name="dtl_slno")
	private int slno;

	@ManyToOne
	@JoinColumn(name="item_id_fk")
	private Item itemDtl;

	@Column(name="bs_cost")
	private Double baseCost;
	
	@Column(name="itm_qty")
	private Long itemsQty;
	
	@Column(name="line_val")
	private Double lineValue;

	
	public PurchaseDtl() {
		super();
	}

	public PurchaseDtl(Integer orderDtlId) {
		super();
		this.orderDtlId = orderDtlId;
	}

	public Integer getOrderDtlId() {
		return orderDtlId;
	}

	public void setOrderDtlId(Integer orderDtlId) {
		this.orderDtlId = orderDtlId;
	}

	public Integer getPoHdrId() {
		return poHdrId;
	}

	public void setPoHdrId(Integer poHdrId) {
		this.poHdrId = poHdrId;
	}

	public int getSlno() {
		return slno;
	}

	public void setSlno(int slno) {
		this.slno = slno;
	}

	public Item getItemDtl() {
		return itemDtl;
	}

	public void setItemDtl(Item itemDtl) {
		this.itemDtl = itemDtl;
	}

	public Double getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(Double baseCost) {
		this.baseCost = baseCost;
	}

	public Long getItemsQty() {
		return itemsQty;
	}

	public void setItemsQty(Long itemsQty) {
		this.itemsQty = itemsQty;
	}

	public Double getLineValue() {
		return lineValue;
	}

	public void setLineValue(Double lineValue) {
		this.lineValue = lineValue;
	}

	@Override
	public String toString() {
		return "PurchaseDtl [orderDtlId=" + orderDtlId + ", poHdrId=" + poHdrId + ", slno=" + slno + ", itemDtl="
				+ itemDtl + ", baseCost=" + baseCost + ", itemsQty=" + itemsQty + ", lineValue=" + lineValue + "]";
	}
	
		
}
