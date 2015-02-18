package com.schedario.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.schedario.adapter.RefregeratorAdapter;
import com.schedario.bean.RefregeratorBean;
import com.schedario.bean.RefregeratorSubBean;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

public class RefregeratorListActivity extends BaseActivity{
	private LinearLayout ll_reftemperature_list,ll_add_reftemperature,ll_include_add_templist,ll_include_reftemp_list;
	private LinearLayout ll_select_date;
	
	private TextView tv_date;
	private String temperature,year,month,day;
	private int selection_value = -1;
	
	private EditText et_temperature;
	private Button btn_addTemp,btn_addRefrigerator;
	private ListView lv_refrigerator;
	private ArrayList<RefregeratorBean> refList = new ArrayList<RefregeratorBean>();
	public RefregeratorAdapter adapter;
	
	private Spinner spinner_refrigerator_list;
	private List<String> spinnerArray =  new ArrayList<String>();
	private List<String> spinnerItemId =  new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refregerator);
		
		ll_add_reftemperature = (LinearLayout)findViewById(R.id.ll_add_reftemperature);
		ll_add_reftemperature.setOnClickListener(this);
		
		ll_reftemperature_list = (LinearLayout)findViewById(R.id.ll_reftemperature_list);
		ll_reftemperature_list.setOnClickListener(this);
		
		ll_include_add_templist = (LinearLayout)findViewById(R.id.ll_include_add_templist);
		ll_include_reftemp_list = (LinearLayout)findViewById(R.id.ll_include_reftemp_list);
		
		et_temperature = (EditText)findViewById(R.id.et_temperature);
		tv_date = (TextView)findViewById(R.id.tv_date);
		
		spinner_refrigerator_list = (Spinner)findViewById(R.id.spinner_refrigerator_list);
		lv_refrigerator = (ListView)findViewById(R.id.lv_refrigerator);
		
		btn_addRefrigerator = (Button)findViewById(R.id.btn_addRefrigerator);
		btn_addRefrigerator.setOnClickListener(this);
		
		btn_addTemp = (Button)findViewById(R.id.btn_addTemp);
		btn_addTemp.setOnClickListener(this);
		
		displayView(0);
		setCurrentdate();
		getSpinnerItem();
		
		spinner_refrigerator_list.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				selection_value = pos;
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

	private void getSpinnerItem() {
		new AsyncRefrigeratorList().execute();
	}
	
	public class AsyncRefrigeratorList extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				JSONObject req = new JSONObject();
				req.put("user_id", app.getUserinfo().getUser_id());
				req.put("type", "ref");
				String response = KlHttpClient.SendHttpPost(Constants.REFREGERATOR_OR_FREEZ_LIST, req.toString());
				if(response != null){
					JSONObject ob = new JSONObject(response);
					if (ob.getBoolean("status")) {
						JSONArray arr = ob.getJSONArray("refrigeratorFreeze");
						spinnerItemId.clear();
						spinnerArray.clear();
						for(int i = 0;i<arr.length(); i++){
							JSONObject obj = arr.getJSONObject(i);
							spinnerItemId.add(obj.getString("id"));
							spinnerArray.add("Refregerator"+(i+1));
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			doRemoveLoading();
			if(spinnerArray.size() == 0){
				Toast.makeText(getApplicationContext(), "Please add Refregerator first...", Toast.LENGTH_LONG).show();
			}else{
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					    RefregeratorListActivity.this, android.R.layout.simple_spinner_item, spinnerArray);

					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_refrigerator_list.setAdapter(adapter);
					selection_value = 0;
			}
		}
	}

	private void displayView(int position) {
		switch (position) {
		case 0:
			ll_include_reftemp_list.setVisibility(View.GONE);
			ll_include_add_templist.setVisibility(View.VISIBLE);
			break;
		case 1:
			ll_include_reftemp_list.setVisibility(View.VISIBLE);
			ll_include_add_templist.setVisibility(View.GONE);
			
			hideKeyBoard(et_temperature);
			
			new CallServerForgetRefregeratorTempList().execute();
			
			break;
		}
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll_add_reftemperature:
			displayView(0);
			break;

		case R.id.ll_reftemperature_list:
			displayView(1);
			break;
			
		case R.id.btn_addRefrigerator:
			addRefregerator();
			break;
			
		case R.id.btn_addTemp:
			addRefregeratorTemp();
			break;
		}
	}
	private void addRefregerator() {
		new AsyncRefrigeratorAdd().execute();
	}

	public class AsyncRefrigeratorAdd extends AsyncTask<Void, Void, String>{

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
				req.put("type", "ref");
				req.put("name", "ref1");
				String response = KlHttpClient.SendHttpPost(Constants.ADD_REFREGERATOR_OR_FREEZ, req.toString());
				if(response != null){
					JSONObject ob = new JSONObject(response);
					if (ob.getBoolean("status")) {
						return ob.getString("message");
					}else{
						return ob.getString("message");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			doRemoveLoading();
			if(result.equalsIgnoreCase("Successfully added")){
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
				getSpinnerItem();
				
			}else{
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void addRefregeratorTemp() {
		if (isValid()) {
			sendValueToServer();
		}
	}
	private boolean isValid() {
		temperature = et_temperature.getText().toString().trim();

		boolean flag = true;
		if (temperature.length() == 0) {
			et_temperature.setError("Please Enter Temperature");
			flag = false;
		}else if(selection_value == -1) {
			flag = false;
			Toast.makeText(getApplicationContext(), "Please add an Refregerator first", Toast.LENGTH_LONG).show();
		}
		return flag;
	}
	private void sendValueToServer() {
		new AsyncRefrigeratorAddTemp().execute();
	}
	
	public class AsyncRefrigeratorAddTemp extends AsyncTask<Void, Void, String>{

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
				req.put("degree", temperature);
				req.put("refrigeratorFreeze_id", spinnerItemId.get(selection_value));
				req.put("added_date", tv_date.getText().toString());
				req.put("type", "ref");
				String response = KlHttpClient.SendHttpPost(Constants.ADD_REFREGERATOR_OR_FREEZ_TEMP, req.toString());
				if(response != null){
					JSONObject ob = new JSONObject(response);
					if (ob.getBoolean("status")) {
						return ob.getString("message");
					}else{
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
			if(!result.equalsIgnoreCase("Temperature already exists for this date")){
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
				displayView(1);
			}else{
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public class CallServerForgetRefregeratorTempList extends AsyncTask<Void, Void, Void> {

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
				req.put("type", "ref");
				String response = KlHttpClient.SendHttpPost(Constants.SHOW_REFREGERATOR_OR_FREEZ_TEMP, req.toString());
				if (response != null) {
					try {
						JSONObject ob = new JSONObject(response);
						if (ob.getBoolean("status")) {
							JSONArray arr = ob.getJSONArray("temperature");
							refList.clear();
							for(int i = 0; i < arr.length() ; i++){
								JSONObject obj = arr.getJSONObject(i);
								JSONArray jarr = obj.getJSONArray("refFreeze");
								ArrayList<RefregeratorSubBean> sublist = new ArrayList<RefregeratorSubBean>();
								for(int j = 0 ; j < jarr.length(); j++){
									JSONObject object = jarr.getJSONObject(j);
									sublist.add(new RefregeratorSubBean("Refregerator"+(j+1),
											object.getString("degree")));
								}
								refList.add(new RefregeratorBean(obj.getString("date"),
										sublist));
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
			
			adapter = new RefregeratorAdapter(RefregeratorListActivity.this, R.layout.refrigerator_row, refList);
			lv_refrigerator.setAdapter(adapter);
			
		}
	}
}
