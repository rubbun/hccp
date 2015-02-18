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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.schedario.adapter.LabAdapter;
import com.schedario.bean.LabBean;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

public class LaboratoryActivity extends BaseActivity {

	private LinearLayout ll_lab_data_list, ll_add_lab_data,
			ll_include_add_lab_data, ll_include_labdata_list;

	private TextView tv_date;
	private String sample_data, name;
	private String selection_value = null;

	private EditText et_sample_data, et_name;
	private Button btn_add;
	private ListView lv_labdatalist;
	private ArrayList<LabBean> labList = new ArrayList<LabBean>();
	public LabAdapter adapter;

	private Spinner spinner_result_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_laboratory);
		
		ll_add_lab_data = (LinearLayout) findViewById(R.id.ll_add_lab_data);
		ll_add_lab_data.setOnClickListener(this);

		ll_lab_data_list = (LinearLayout) findViewById(R.id.ll_lab_data_list);
		ll_lab_data_list.setOnClickListener(this);

		ll_include_add_lab_data = (LinearLayout) findViewById(R.id.ll_include_add_lab_data);
		ll_include_labdata_list = (LinearLayout) findViewById(R.id.ll_include_labdata_list);

		et_sample_data = (EditText) findViewById(R.id.et_sample_data);
		et_name = (EditText) findViewById(R.id.et_name);

		tv_date = (TextView) findViewById(R.id.tv_date);

		spinner_result_data = (Spinner) findViewById(R.id.spinner_result_data);
		lv_labdatalist = (ListView) findViewById(R.id.lv_labdatalist);

		btn_add = (Button) findViewById(R.id.btn_add);
		btn_add.setOnClickListener(this);

		displayView(0);
		setCurrentdate();

		spinner_result_data
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						selection_value = spinner_result_data.getItemAtPosition(pos).toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
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
			ll_include_labdata_list.setVisibility(View.GONE);
			ll_include_add_lab_data.setVisibility(View.VISIBLE);
			break;
		case 1:
			ll_include_labdata_list.setVisibility(View.VISIBLE);
			ll_include_add_lab_data.setVisibility(View.GONE);

			new CallLabDataList().execute();

			break;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll_add_lab_data:
			displayView(0);
			break;

		case R.id.ll_lab_data_list:
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
				req.put("result", selection_value);
				req.put("sample",sample_data);
				req.put("added_date", tv_date.getText().toString());
				req.put("responsible_food_safety", name);
				String response = KlHttpClient.SendHttpPost(
						Constants.ADD_LAB_DATA,
						req.toString());
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
				displayView(1);
			} else {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private boolean isValid() {
		name = et_name.getText().toString().trim();
		sample_data = et_sample_data.getText().toString();

		boolean flag = true;
		if (name.length() == 0) {
			et_name.setError("Please Enter Name and Surname");
			flag = false;
		} else if (sample_data.length() == 0) {
			flag = false;
			et_sample_data.setError("Please enter sample data");
		}
		return flag;
	}

	public class CallLabDataList extends AsyncTask<Void, Void, Void> {

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
				req.put("type", "freeze");
				String response = KlHttpClient.SendHttpPost(
						Constants.SHOW_LAB_DATA,
						req.toString());
				if (response != null) {
					try {
						JSONObject ob = new JSONObject(response);
						if (ob.getBoolean("status")) {
							JSONArray arr = ob.getJSONArray("laboratory");
							labList.clear();
							for (int i = 0; i < arr.length(); i++) {
								JSONObject object = arr.getJSONObject(i);
								labList.add(new LabBean(object.getString("id"),
										object.getString("result"),
										object.getString("sample"),
										object.getString("responsible_food_safety"),
										object.getString("added_date")));
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

			adapter = new LabAdapter(LaboratoryActivity.this,
					R.layout.lab_row, labList);
			lv_labdatalist.setAdapter(adapter);
		}
	}
}
