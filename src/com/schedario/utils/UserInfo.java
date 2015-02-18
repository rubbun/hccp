package com.schedario.utils;

import com.schedario.constants.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserInfo {

	public String user_id = null;
	public String company_name = null;
	public String address = null;
	public String city = null;
	public String zip_code = null;
	public String name = null;
	public String surname = null;
	public String province;
	public String post_code = null;
	public String phone = null;
	public String dob = null;
	public String email = null;
	public String username = null;
	public String password = null;
	public SharedPreferences pref;
	
	public UserInfo(Context context){
		pref = context.getSharedPreferences(Constants.values.USERINFO.name(), Context.MODE_PRIVATE);
		user_id = pref.getString(Constants.values.USERID.name(), null);
		company_name = pref.getString(Constants.values.COMPANYNAME.name(), null);
		address = pref.getString(Constants.values.ADDRESS.name(), null);
		city = pref.getString(Constants.values.CITY.name(), null);
		
		zip_code = pref.getString(Constants.values.ZIPCODE.name(), null);
		name = pref.getString(Constants.values.NAME.name(), null);
		surname = pref.getString(Constants.values.SURNAME.name(), null);
		province = pref.getString(Constants.values.PROVINCE.name(), null);
		
		post_code = pref.getString(Constants.values.POSTCODE.name(), null);
		phone = pref.getString(Constants.values.PHONE.name(), null);
		dob = pref.getString(Constants.values.DOB.name(), null);
		email = pref.getString(Constants.values.EMAIL.name(), null);
		
	}

	public void setUserInfo(String user_id,String company_name, String address, String city,
			String zip_code, String name, String surname, String province,
			String post_code, String phone, String dob, String email,
			String username, String password) {
		
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
		
		Editor edit = pref.edit();
		edit.putString(Constants.values.USERID.name(), user_id);
		edit.putString(Constants.values.COMPANYNAME.name(), company_name);
		edit.putString(Constants.values.ADDRESS.name(), address);
		edit.putString(Constants.values.CITY.name(), city);
		
		edit.putString(Constants.values.ZIPCODE.name(), zip_code);
		edit.putString(Constants.values.NAME.name(), name);
		edit.putString(Constants.values.SURNAME.name(), surname);
		edit.putString(Constants.values.PROVINCE.name(), province);
		
		edit.putString(Constants.values.POSTCODE.name(), post_code);
		edit.putString(Constants.values.PHONE.name(), phone);
		edit.putString(Constants.values.DOB.name(), dob);
		edit.putString(Constants.values.EMAIL.name(), email);
		
		edit.commit();
		
		edit.commit();
	}

	public String getUser_id() {
		return pref.getString(Constants.values.USERID.name(), null);
	}

	public String getCompany_name() {
		return pref.getString(Constants.values.COMPANYNAME.name(), null);
	}

	public String getAddress() {
		return pref.getString(Constants.values.ADDRESS.name(), null);
	}

	public String getCity() {
		return pref.getString(Constants.values.CITY.name(), null);
	}

	public String getZip_code() {
		return pref.getString(Constants.values.ZIPCODE.name(), null);
	}

	public String getName() {
		return pref.getString(Constants.values.NAME.name(), null);
	}

	public String getSurname() {
		return pref.getString(Constants.values.SURNAME.name(), null);
	}
	
	public String getProvince() {
		return pref.getString(Constants.values.PROVINCE.name(), null);
	}

	public String getPost_code() {
		return pref.getString(Constants.values.PROVINCE.name(), null);
	}

	public String getPhone() {
		return pref.getString(Constants.values.PHONE.name(), null);
	}

	public String getDob() {
		return pref.getString(Constants.values.DOB.name(), null);
	}

	public String getEmail() {
		return pref.getString(Constants.values.EMAIL.name(), null);
	}
}
