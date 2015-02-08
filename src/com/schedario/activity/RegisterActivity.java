package com.schedario.activity;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.schedario.constants.Constants;
import com.schedario.network.HttpClient;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private EditText et_company, et_address, et_city, et_zipcode, et_name;
	private EditText et_surname, et_province, et_postcode, et_phone, et_dob;
	private EditText et_email, et_username, et_password;
	private Button btn_register;
	private CheckBox cb_trial;

	private String company_name, address, city, zip_code, name, surname, province, post_code, phone, dob, email, username, password;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);
		et_company = (EditText) findViewById(R.id.et_company_name);
		et_address = (EditText) findViewById(R.id.et_address);
		et_city = (EditText) findViewById(R.id.et_city);
		et_zipcode = (EditText) findViewById(R.id.et_zipcode);
		et_name = (EditText) findViewById(R.id.et_name);
		et_surname = (EditText) findViewById(R.id.et_surname);
		et_province = (EditText) findViewById(R.id.et_province);
		et_postcode = (EditText) findViewById(R.id.et_postcode);
		et_phone = (EditText) findViewById(R.id.et_phoneno);
		et_dob = (EditText) findViewById(R.id.et_dob);
		et_email = (EditText) findViewById(R.id.et_mail);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);

		btn_register = (Button) findViewById(R.id.btn_register);

		btn_register.setOnClickListener(this);

	}

	private void registerMe() {
		if (isValid()) {
			sendValueToServer();
		}

	}

	private void sendValueToServer() {
		new CallServerForRegistration().execute();
	}

	public class CallServerForRegistration extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				JSONObject req = new JSONObject();
				req.put("companyname", company_name);
				req.put("legal_address", address);
				req.put("city", city);
				req.put("zip", zip_code);
				req.put("name", name);
				req.put("surname", surname);
				req.put("province", province);
				req.put("postcode", post_code);
				req.put("phone", phone);
				req.put("dob", dob);
				req.put("email", email);
				req.put("username", username);
				req.put("password", password);

				String response = HttpClient.SendHttpPost(Constants.SIGNUP, req.toString());
				if (response != null) {
					try {
						JSONObject ob = new JSONObject(response);
						if (ob.getBoolean("status")) {
							return ob.getString("message");
						}else{
							return ob.getString("message");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			doRemoveLoading();
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
			finish();
		}
	}

	private boolean isValid() {
		company_name = et_company.getText().toString().trim();
		address = et_address.getText().toString().trim();
		city = et_city.getText().toString().trim();
		zip_code = et_zipcode.getText().toString().trim();
		name = et_name.getText().toString().trim();
		surname = et_surname.getText().toString().trim();
		post_code = et_postcode.getText().toString().trim();
		province = et_province.getText().toString().trim();
		phone = et_phone.getText().toString().trim();
		dob = et_dob.getText().toString().trim();
		email = et_email.getText().toString().trim();
		username = et_username.getText().toString().trim();
		password = et_password.getText().toString().trim();

		boolean flag = true;
		if (company_name.length() == 0) {
			et_company.setError("Please Enter your Company name");
			flag = false;
		} else if (address.length() == 0) {
			et_address.setError("Please Enter your Address");
			flag = false;
		} else if (city.length() == 0) {
			et_city.setError("Please Enter your City name");
			flag = false;
		} else if (zip_code.length() == 0) {
			et_zipcode.setError("Please Enter your Zip code");
			flag = false;
		} else if (name.length() == 0) {
			et_name.setError("Please Enter your Name");
			flag = false;
		} else if (surname.length() == 0) {
			et_surname.setError("Please Enter your Surname");
			flag = false;
		} else if (province.length() == 0) {
			et_province.setError("Please Enter your Province name");
			flag = false;
		} else if (post_code.length() == 0) {
			et_postcode.setError("Please Enter your Post code");
			flag = false;
		} else if (company_name.length() == 0) {
			et_company.setError("Please Enter your Company Name");
			flag = false;
		} else if (phone.length() == 0) {
			et_phone.setError("Please Enter your Phone no");
			flag = false;
		} else if (dob.length() == 0) {
			et_dob.setError("Please Enter your Date of Birth.");
			flag = false;
		} else if (email.length() == 0) {
			et_email.setError("Please Enter your Indirizzo e-mail.");
			flag = false;
		} else if (!isvalidMailid(email)) {
			et_email.setError("Please enter valid Email id.");
			flag = false;
		} else if (username.length() == 0) {
			et_username.setError("Please Enter your Username.");
			flag = false;
		} else if (password.length() == 0) {
			et_password.setError("Please Enter your Password.");
			flag = false;
		}
		return flag;
	}

	public boolean isvalidMailid(String mail) {
		return Pattern.compile(EMAIL_PATTERN).matcher(mail).matches();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_register:
			registerMe();
			break;

		}
	}
}
