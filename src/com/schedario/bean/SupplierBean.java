package com.schedario.bean;

public class SupplierBean {

	public String id, product, supplier, phone, contact_person, address;

	public SupplierBean(String id, String product, String supplier,
			String phone, String contact_person, String address) {
		super();
		this.id = id;
		this.product = product;
		this.supplier = supplier;
		this.phone = phone;
		this.contact_person = contact_person;
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
