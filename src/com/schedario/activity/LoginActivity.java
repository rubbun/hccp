package com.schedario.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;
import com.schedario.util.SampleAlarmReceiver;
import com.schedario.util.Utility;
import com.schedario.utils.UserInfo;

public class LoginActivity extends BaseActivity {

	private EditText et_username, et_password;
	private Button btn_login, btn_register;
	private CheckBox cb_remember;
	SampleAlarmReceiver alarm = new SampleAlarmReceiver();
	Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		activity = LoginActivity.this;
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);

		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);

		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);

		cb_remember = (CheckBox) findViewById(R.id.cb_remember);
	}

	private boolean isValid() {
		String username = et_username.getText().toString();
		String password = et_password.getText().toString();
		boolean flag = true;
		if (username.length() == 0) {
			et_username.setError("Please enter your username");
			flag = false;
		} else if (password.length() == 0) {
			et_password.setError("Please enter your password");
			flag = false;
		}
		return flag;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.btn_login:
			if(isValid()){
				new LoginAsynctask().execute();
			}
			break;

		case R.id.btn_register:
			Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(i);
			break;

		}
	}
	String user_id;
	public class LoginAsynctask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			String username = et_username.getText().toString().trim();
			String password = et_password.getText().toString().trim();
			try {
				JSONObject req = new JSONObject();
				req.put("username", username);
				req.put("password", password);
				String response = KlHttpClient.SendHttpPost(Constants.SIGNIN, req.toString());
				if (response != null) {
					JSONObject ob = new JSONObject(response);
					if (ob.getBoolean("status")) {
						Log.e("!reach here", "reach here");
						user_id = ob.getString("id");
						String company = ob.getString("company_name");
						String address = ob.getString("legal_address");
						String city = ob.getString("city");
						String zipcode = ob.getString("zip");
						String name = ob.getString("name");
						String surname = ob.getString("surname");
						String province = ob.getString("province");
						String postcode = ob.getString("postcode");
						String phone = ob.getString("phone");
						String dob = ob.getString("dob");
						String email = ob.getString("email");
						String uname = ob.getString("username");
						String passwrd = ob.getString("password");

						app.getUserinfo().setUserInfo(user_id, company, address, city, zipcode, name, surname, province, postcode, phone, dob, email, uname, passwrd);

						app.getLogininfo().setLoginInfo(username, password,true);
						return ob.getBoolean("status");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			doRemoveLoading();
			if(result){
				boolean alarmUp = (PendingIntent.getBroadcast(activity, 10, new Intent(activity, SampleAlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);
		    	if(!alarmUp){
		        alarm.setAlarm(activity);
		    	}
		    	Utility.storeValueOnPersistence(activity, Constants.USERID, user_id);
				Intent intent = new Intent(LoginActivity.this, Schedario.class);
				startActivity(intent);
				LoginActivity.this.finish();
			}
		}
	}

}