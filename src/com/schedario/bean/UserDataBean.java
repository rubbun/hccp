package com.schedario.bean;


public class UserDataBean {

	String id, date, company_name, lot_number, product_details;

	public UserDataBean(String id, String date, String company_name,
			String lot_number, String product_details) {
		super();
		this.id = id;
		this.date = date;
		this.company_name = company_name;
		this.lot_number = lot_number;
		this.product_details = product_details;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getLot_number() {
		return lot_number;
	}

	public void setLot_number(String lot_number) {
		this.lot_number = lot_number;
	}

	public String getProduct_details() {
		return product_details;
	}

	public void setProduct_details(String product_details) {
		this.product_details = product_details;
	}

}
