package com.schedario.bean;

public class SupplierBean {

	public String user_id, supplier_id, product, supplier, phone,
			contact_person, address;

	public SupplierBean(String user_id, String supplier_id, String product,
			String supplier, String phone, String contact_person, String address) {
		super();
		this.user_id = user_id;
		this.supplier_id = supplier_id;
		this.product = product;
		this.supplier = supplier;
		this.phone = phone;
		this.contact_person = contact_person;
		this.address = address;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContact_person() {
		return contact_person;
	}

	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
