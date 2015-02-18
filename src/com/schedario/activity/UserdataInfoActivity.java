package com.schedario.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.schedario.adapter.UserDataAdapter;
import com.schedario.bean.UserDataBean;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

public class UserdataInfoActivity extends BaseActivity {

	private LinearLayout ll_add_user_data, ll_user_data_list,
			ll_include_userdata_list, ll_include_add_userdata;
	private TextView tv_date;
	private EditText et_company_name, et_lot_number, et_description;
	private Button btn_add;
	private ArrayList<UserDataBean> list = new ArrayList<UserDataBean>();
	private ListView lv_user_data;
	public UserDataAdapter adapter;
	private String company_name, lot_number, description = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);

		ll_include_add_userdata = (LinearLayout) findViewById(R.id.ll_include_add_userdata);
		ll_include_userdata_list = (LinearLayout) findViewById(R.id.ll_include_userdata_list);

		tv_date = (TextView) findViewById(R.id.tv_date);

		ll_add_user_data = (LinearLayout) findViewById(R.id.ll_add_user_data);
		ll_add_user_data.setOnClickListener(this);

		ll_user_data_list = (LinearLayout) findViewById(R.id.ll_user_data_list);
		ll_user_data_list.setOnClickListener(this);

		et_company_name = (EditText) findViewById(R.id.et_company_name);
		et_lot_number = (EditText) findViewById(R.id.et_lot_number);
		et_description = (EditText) findViewById(R.id.et_description);

		btn_add = (Button) findViewById(R.id.btn_add);
		btn_add.setOnClickListener(this);

		lv_user_data = (ListView) findViewById(R.id.lv_user_data);

		displayView(0);
		setCurrentdate();
	}

	@SuppressLint("SimpleDateFormat")
	private void setCurrentdate() {
		Date cDate = new Date();
		String fDate = new SimpleDateFormat("dd-MM-yyyy").format(cDate);
		tv_date.setText(fDate);
	}

	private void displayView(int position) {
		switch (position) {
		case 0:
			ll_include_userdata_list.setVisibility(View.GONE);
			ll_include_add_userdata.setVisibility(View.VISIBLE);
			break;
		case 1:
			ll_include_userdata_list.setVisibility(View.VISIBLE);
			ll_include_add_userdata.setVisibility(View.GONE);

			new CallUserDataList().execute();

			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_add_user_data:
			displayView(0);
			break;

		case R.id.ll_user_data_list:
			displayView(1);
			break;

		case R.id.btn_add:
			addLabData();
			break;
		}
	}

	private void addLabData() {
		if (isValid()) {
			sendValueToServer();
		}
	}

	private boolean isValid() {
		company_name = et_company_name.getText().toString().trim();
		lot_number = et_lot_number.getText().toString();
		description = et_description.getText().toString();

		boolean flag = true;
		if (company_name.length() == 0) {
			et_company_name.setError("Please Enter company name");
			flag = false;
		} else if (lot_number.length() == 0) {
			flag = false;
			et_lot_number.setError("Please enter lot number");
		} else if (description.length() == 0) {
			flag = false;
			et_description.setError("Please enter description");
		}
		return flag;
	}

	private void sendValueToServer() {
		new AsyncLaboratoryAddData().execute();
	}

	public class AsyncLaboratoryAddData extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			try {
				JSONObject req = new JSONObject();
				req.put("user_id", app.getUserinfo().getUser_id());
				req.put("company", company_name);
				req.put("lot_number", lot_number);
				req.put("product_desc", description);
				req.put("added_date", tv_date.getText().toString());
				String response = KlHttpClient.SendHttpPost(
						Constants.ADD_USERS_DATA, req.toString());
				if (response != null) {
					JSONObject ob = new JSONObject(response);
					if (ob.getBoolean("status")) {
						return ob.getString("message");
					} else {
						return ob.getString("message");
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
			if (!result
					.equalsIgnoreCase("Temperature already exists for this date")) {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_LONG).show();
				et_company_name.setText("");
				et_description.setText("");
				et_lot_number.setText("");
				displayView(1);
			} else {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public class CallUserDataList extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				JSONObject req = new JSONObject();
				req.put("user_id", app.getUserinfo().getUser_id());
				String response = KlHttpClient.SendHttpPost(
						Constants.SHOW_USERS_DATA, req.toString());
				if (response != null) {
					try {
						JSONObject ob = new JSONObject(response);
						if (ob.getBoolean("status")) {
							JSONArray arr = ob.getJSONArray("userCompany");
							list.clear();
							for (int i = 0; i < arr.length(); i++) {
								JSONObject object = arr.getJSONObject(i);
								list.add(new UserDataBean(object
										.getString("id"), object
										.getString("added_date"), object
										.getString("company"), object
										.getString("lot_number"), object
										.getString("product_desc")));
							}
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
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			doRemoveLoading();
			adapter = new UserDataAdapter(UserdataInfoActivity.this,
					R.layout.userdata_row, list);
			lv_user_data.setAdapter(adapter);
		}
	}
}
