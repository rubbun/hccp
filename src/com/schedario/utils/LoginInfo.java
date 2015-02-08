package com.schedario.utils;

import com.schedario.constants.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoginInfo {

	public String username,password = null;
	public SharedPreferences pref;
	
	public LoginInfo(Context context){
	
		pref = context.getSharedPreferences(Constants.values.LOGININFO.name(), Context.MODE_PRIVATE);
		username = pref.getString(Constants.values.USERNAME.name(), null);
		password = pref.getString(Constants.values.PASSWORD.name(), null);
	}
	
	public void setLoginInfo(String username,String password){
		this.username = username;
		this.password = password;
		
		Editor edit = pref.edit();
		edit.putString(Constants.values.USERNAME.name(), username);
		edit.putString(Constants.values.PASSWORD.name(), password);
		
		edit.commit();
	}
}
