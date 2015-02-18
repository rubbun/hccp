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

import com.schedario.adapter.FreezeAdapter;
import com.schedario.bean.FreezeBean;
import com.schedario.bean.FreezeSubBean;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

public class FreezeActivity extends BaseActivity{
	
	private LinearLayout ll_freezetemperature_list,ll_add_freez_temp,ll_include_add_freezelist,ll_include_freeze_list;
	private LinearLayout ll_select_date;
	
	private TextView tv_date;
	private String temperature,year,month,day;
	private int selection_value = -1;
	
	private EditText et_temperature;
	private Button btn_addTemp,btn_addfreeze;
	private ListView lv_refrigerator;
	private ArrayList<FreezeBean> freezeList = new ArrayList<FreezeBean>();
	public FreezeAdapter adapter;
	
	private Spinner spinner_freeze_list;
	private List<String> spinnerArray =  new ArrayList<String>();
	private List<String> spinnerItemId =  new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_freez);
		
		ll_add_freez_temp = (LinearLayout)findViewById(R.id.ll_add_freez_temp);
		ll_add_freez_temp.setOnClickListener(this);
		
		ll_freezetemperature_list = (LinearLayout)findViewById(R.id.ll_freezetemperature_list);
		ll_freezetemperature_list.setOnClickListener(this);
		
		ll_include_add_freezelist = (LinearLayout)findViewById(R.id.ll_include_add_freezelist);
		ll_include_freeze_list = (LinearLayout)findViewById(R.id.ll_include_freeze_list);
		
		et_temperature = (EditText)findViewById(R.id.et_temperature);
		tv_date = (TextView)findViewById(R.id.tv_date);
		
		spinner_freeze_list = (Spinner)findViewById(R.id.spinner_freeze_list);
		lv_refrigerator = (ListView)findViewById(R.id.lv_refrigerator);
		
		btn_addfreeze = (Button)findViewById(R.id.btn_addfreeze);
		btn_addfreeze.setOnClickListener(this);
		
		btn_addTemp = (Button)findViewById(R.id.btn_addTemp);
		btn_addTemp.setOnClickListener(this);
		
		displayView(0);
		setCurrentdate();
		getSpinnerItem();
		
		spinner_freeze_list.setOnItemSelectedListener(new OnItemSelectedListener() {

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
	
	private void displayView(int position) {
		switch (position) {
		case 0:
			ll_include_freeze_list.setVisibility(View.GONE);
			ll_include_add_freezelist.setVisibility(View.VISIBLE);
			break;
		case 1:
			ll_include_freeze_list.setVisibility(View.VISIBLE);
			ll_include_add_freezelist.setVisibility(View.GONE);
			
			hideKeyBoard(et_temperature);
			
			new CallServerForgetRefregeratorTempList().execute();
			
			break;
		}
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll_add_freez_temp:
			displayView(0);
			break;

		case R.id.ll_freezetemperature_list:
			displayView(1);
			break;
			
		case R.id.btn_addfreeze:
			addFreeze();
			break;
			
		case R.id.btn_addTemp:
			addRefregeratorTemp();
			break;
		}
	}
	
	private void addRefregeratorTemp() {
		if (isValid()) {
			sendValueToServer();
		}
	}
	
	private void sendValueToServer() {
		new AsyncFreezeAddTemp().execute();
	}
	
	public class AsyncFreezeAddTemp extends AsyncTask<Void, Void, String>{

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
				req.put("type", "freeze");
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
	
	private boolean isValid() {
		temperature = et_temperature.getText().toString().trim();

		boolean flag = true;
		if (temperature.length() == 0) {
			et_temperature.setError("Please Enter Temperature");
			flag = false;
		}else if(selection_value == -1) {
			flag = false;
			Toast.makeText(getApplicationContext(), "Please add an Freeze first", Toast.LENGTH_LONG).show();
		}
		return flag;
	}
	
	private void addFreeze() {
		new AsyncFreezeAdd().execute();
	}
	
	public class AsyncFreezeAdd extends AsyncTask<Void, Void, String>{

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
				req.put("type", "freeze");
				req.put("name", "freeze1");
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
	
	private void getSpinnerItem() {
		new AsyncFreezeList().execute();
	}
	
	public class AsyncFreezeList extends AsyncTask<Void, Void, Void>{

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
				req.put("type", "freeze");
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
							spinnerArray.add("Freeze"+(i+1));
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
				Toast.makeText(getApplicationContext(), "Please add Freeze first...", Toast.LENGTH_LONG).show();
			}else{
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					    FreezeActivity.this, android.R.layout.simple_spinner_item, spinnerArray);

					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_freeze_list.setAdapter(adapter);
					selection_value = 0;
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
				req.put("type", "freeze");
				String response = KlHttpClient.SendHttpPost(Constants.SHOW_REFREGERATOR_OR_FREEZ_TEMP, req.toString());
				if (response != null) {
					try {
						JSONObject ob = new JSONObject(response);
						if (ob.getBoolean("status")) {
							JSONArray arr = ob.getJSONArray("temperature");
							freezeList.clear();
							for(int i = 0; i < arr.length() ; i++){
								JSONObject obj = arr.getJSONObject(i);
								JSONArray jarr = obj.getJSONArray("refFreeze");
								ArrayList<FreezeSubBean> sublist = new ArrayList<FreezeSubBean>();
								for(int j = 0 ; j < jarr.length(); j++){
									JSONObject object = jarr.getJSONObject(j);
									sublist.add(new FreezeSubBean("Freeze"+(j+1),
											object.getString("degree")));
								}
								freezeList.add(new FreezeBean(obj.getString("date"),
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
			
			adapter = new FreezeAdapter(FreezeActivity.this, R.layout.refrigerator_row, freezeList);
			lv_refrigerator.setAdapter(adapter);
			
		}
	}
}
