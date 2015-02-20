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

import com.schedario.adapter.MeasureAdapter;
import com.schedario.bean.MeasureBean;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

public class MeasureActivity extends BaseActivity  {
	
	private LinearLayout ll_measure_data_list, ll_add_measure_data,
	ll_include_add_measure_data, ll_include_measuredata_list;

private TextView tv_date;
private String sample_data, name;
private String selection_value = null;

private EditText et_non_compliance, et_measure_text;
private Button btn_add;
private ListView lv_measuredatalist;
private ArrayList<MeasureBean> measureList = new ArrayList<MeasureBean>();
public MeasureAdapter adapter;

private Spinner spinner_result_data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measure);
		
		ll_add_measure_data = (LinearLayout) findViewById(R.id.ll_add_measure_data);
		ll_add_measure_data.setOnClickListener(this);

		ll_measure_data_list = (LinearLayout) findViewById(R.id.measure_data_list);
		ll_measure_data_list.setOnClickListener(this);

		ll_include_add_measure_data = (LinearLayout) findViewById(R.id.ll_include_add_measure_data);
		ll_include_measuredata_list = (LinearLayout) findViewById(R.id.ll_include_measuredata_list);

		et_non_compliance = (EditText) findViewById(R.id.et_non_compliance);
		et_measure_text = (EditText) findViewById(R.id.et_measure_text);

		tv_date = (TextView) findViewById(R.id.tv_date);

		spinner_result_data = (Spinner) findViewById(R.id.spinner_result_data);
		lv_measuredatalist = (ListView) findViewById(R.id.lv_measuredatalist);

		btn_add = (Button) findViewById(R.id.btn_add);
		btn_add.setOnClickListener(this);
		
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
			ll_include_measuredata_list.setVisibility(View.GONE);
			ll_include_add_measure_data.setVisibility(View.VISIBLE);
			break;
		case 1:
			ll_include_measuredata_list.setVisibility(View.VISIBLE);
			ll_include_add_measure_data.setVisibility(View.GONE);

			new CallMeasureDataList().execute();

			break;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll_add_measure_data:
			displayView(0);
			break;

		case R.id.measure_data_list:
			displayView(1);
			break;

		case R.id.btn_add:
			addMeasureData();
			break;
		}
	}
	
	private void addMeasureData() {
		if (isValid()) {
			sendValueToServer();
		}
	}
	
	private void sendValueToServer() {
		new AsyncMeasureAddData().execute();
	}
	
	private boolean isValid() {
		name = et_measure_text.getText().toString().trim();
		sample_data = et_non_compliance.getText().toString();

		boolean flag = true;
		if (sample_data.length() == 0) {
			flag = false;
			et_non_compliance.setError("Please enter Description of non-compliance");
		} else if (name.length() == 0) {
			et_measure_text.setError("Please Enter Measure Text");
			flag = false;
		}
		return flag;
	}
	public class AsyncMeasureAddData extends AsyncTask<Void, Void, String> {

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
				req.put("hour_add", selection_value);
				req.put("name",sample_data);
				req.put("date_add", tv_date.getText().toString());
				req.put("measure", name);
				String response = KlHttpClient.SendHttpPost(
						Constants.ADD_MEASURE_DATA,
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
					.equalsIgnoreCase("Measure already exists for this date")) {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_LONG).show();
				displayView(1);
			} else {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_LONG).show();
			}
		}
	}
	public class CallMeasureDataList extends AsyncTask<Void, Void, Void> {

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
						Constants.SHOW_MEASURE_DATA,
						req.toString());
				if (response != null) {
					try {
						JSONObject ob = new JSONObject(response);
						if (ob.getBoolean("status")) {
							JSONArray arr = ob.getJSONArray("measures");
							measureList.clear();
							for (int i = 0; i < arr.length(); i++) {
								JSONObject object = arr.getJSONObject(i);
								measureList.add(new MeasureBean(object.getString("id"),
										object.getString("name"),
										object.getString("measure"),
										object.getString("hour_add"),
										object.getString("date_add")));
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

			adapter = new MeasureAdapter(MeasureActivity.this,
					R.layout.measure_row, measureList);
			lv_measuredatalist.setAdapter(adapter);
		}
	}	
	
}
