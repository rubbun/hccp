package com.schedario.utils;

public class UserInfo {

	private String user_id,company_name,address,city,zip_code,name,surname,province,post_code,phone,dob,email,username,password = null;

	public UserInfo(String user_id,String company_name, String address, String city,
			String zip_code, String name, String surname, String province,
			String post_code, String phone, String dob, String email,
			String username, String password) {
		super();
		this.user_id = user_id;
		this.company_name = company_name;
		this.address = address;
		this.city = city;
		this.zip_code = zip_code;
		this.name = name;
		this.surname = surname;
		this.province = province;
		this.post_code = post_code;
		this.phone = phone;
		this.dob = dob;
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip_code() {
		return zip_code;
	}

	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPost_code() {
		return post_code;
	}

	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	} 
}
