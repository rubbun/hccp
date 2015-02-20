package com.schedario.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.schedario.adapter.AuthorityAdapter;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

public class AuthorityActivity extends BaseActivity implements OnClickListener{

	EditText et_date, et_business, et_product, et_description, et_lotnumber, et_amountofproduct, et_grandfather, et_requirements, 
		et_reasonforwithdrawal, et_result, et_action, et_risk, et_timetable;
	String date, business, product, description, lotnumber, amountofproduct, grandfather, requirements, reasonforwithdrawal, result, 
		action, risk, timetable;
	ViewFlipper viewFlipper;
	ListView lv_authority;
	 public String checkAddorShow="";
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.activity_authority);
	        
	     initview();
	        
	 }
	 
	 @Override
	 public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_authority:	
			if(isValid())
			if(isNetworkAvailable(this)){
				viewFlipper.showNext();	
				checkAddorShow = "add";
				new AddorShowAuthority().execute();
			}else{
				Toast.makeText(this, "Please check internet connectivity", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.ll_add_authority:	
			viewFlipper.showPrevious();	
			break;
		case R.id.ll_authority_list:	
			if(isNetworkAvailable(this)){
				viewFlipper.showNext();	
				checkAddorShow = "show";
				new AddorShowAuthority().execute();
			}else{
				Toast.makeText(this, "Please check internet connectivity", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
	 
	 boolean resultResponse;
	 String message="";
	 ArrayList<HashMap<String, String>> arrhash = new ArrayList<HashMap<String,String>>();
	 public class AddorShowAuthority extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				doShowLoading();
			}

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				String response = null;
				try {
					JSONObject req = new JSONObject();
					
					req.put("user_id", app.getUserinfo().user_id);
					if(checkAddorShow.equals("add")){
					
					req.put("added_date", date);
					req.put("business_name_company", business);
					req.put("product", product);
					req.put("description", description);
					req.put("lot_number", lotnumber);
					req.put("amount_product", amountofproduct);
					req.put("grandfather_meets", grandfather);
					req.put("requirements", requirements);
					req.put("reason_withdrawal", reasonforwithdrawal);
					req.put("result_investigation", result);
					req.put("action_prevent", action);
					req.put("risk_consumers", risk);
					req.put("timetable_withdrawal_product", timetable);
					
					response = KlHttpClient.SendHttpPost(Constants.ADD_AUTHORITY, req.toString());
					
					if (response != null) {
						JSONObject ob = new JSONObject(response);
						resultResponse = ob.getBoolean("status");
						message = ob.getString("message");
					}
					
					
				}else if(checkAddorShow.equals("show")){
						
						response = KlHttpClient.SendHttpPost(Constants.SHOW_AUTHORITY, req.toString());
						System.out.println("SHOW_AUTHORITY"+response);
						if (response != null) {
							JSONObject ob = new JSONObject(response);
							resultResponse = ob.getBoolean("status");
							
							if (resultResponse) {
								Log.e("!reach here", "reach here");
								JSONArray jArr = ob.getJSONArray("authority");
								arrhash.clear();
								HashMap<String, String> hash;
								for(int i=0; i<jArr.length(); i++){
									
									hash = new HashMap<String, String>();
									JSONObject jObj = jArr.getJSONObject(i);
									hash.put("id", jObj.getString("id"));
									hash.put("action_prevent", jObj.getString("action_prevent"));
									hash.put("added_date", jObj.getString("added_date"));
									hash.put("amount_product", jObj.getString("amount_product"));
									hash.put("business_name_company", jObj.getString("business_name_company"));
									hash.put("description", jObj.getString("description"));
									hash.put("grandfather_meets", jObj.getString("grandfather_meets"));
									hash.put("lot_number", jObj.getString("lot_number"));
									hash.put("product", jObj.getString("product"));
									hash.put("reason_withdrawal", jObj.getString("reason_withdrawal"));
									hash.put("requirements", jObj.getString("requirements"));
									hash.put("result_investigation", jObj.getString("result_investigation"));
									hash.put("risk_consumers", jObj.getString("risk_consumers"));
									hash.put("timetable_withdrawal_product", jObj.getString("timetable_withdrawal_product"));
									
									arrhash.add(hash);
								}
								return arrhash;
							}else{
								message = ob.getString("message");
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> arrresult) {
				super.onPostExecute(arrresult);
				doRemoveLoading();
				if(resultResponse){
					if(checkAddorShow.equals("show")){
						AuthorityAdapter adapter = new AuthorityAdapter(AuthorityActivity.this, R.layout.authority_list_row, arrresult);
						lv_authority.setAdapter(adapter);
					}else if(checkAddorShow.equals("add")){
						Toast.makeText(AuthorityActivity.this, message, Toast.LENGTH_LONG).show();
					}
				}
			}
		}

	 private boolean isValid() {
		 date = et_date.getText().toString().trim();
		 business = et_business.getText().toString().trim();
		 product = et_product.getText().toString().trim();
		 description = et_description.getText().toString().trim();
		 lotnumber = et_lotnumber.getText().toString().trim();
		 amountofproduct = et_amountofproduct.getText().toString().trim();
		 grandfather = et_grandfather.getText().toString().trim();
		 requirements = et_requirements.getText().toString().trim();
		 reasonforwithdrawal = et_reasonforwithdrawal.getText().toString().trim();
		 result = et_result.getText().toString().trim();
		 action = et_action.getText().toString().trim();
		 risk = et_risk.getText().toString().trim();
		 timetable = et_timetable.getText().toString().trim();

		 boolean flag = true;
			if (date.length() == 0) {
				et_date.setError("Please Enter your Company name");
				flag = false;
			} else if (business.length() == 0) {
				et_business.setError("Please Enter your Address");
				flag = false;
			} else if (product.length() == 0) {
				et_product.setError("Please Enter your City name");
				flag = false;
			} else if (description.length() == 0) {
				et_description.setError("Please Enter your Zip code");
				flag = false;
			} else if (lotnumber.length() == 0) {
				et_lotnumber.setError("Please Enter your Name");
				flag = false;
			} else if (amountofproduct.length() == 0) {
				et_amountofproduct.setError("Please Enter your Surname");
				flag = false;
			} else if (grandfather.length() == 0) {
				et_grandfather.setError("Please Enter your Province name");
				flag = false;
			} else if (requirements.length() == 0) {
				et_requirements.setError("Please Enter your Post code");
				flag = false;
			} else if (reasonforwithdrawal.length() == 0) {
				et_reasonforwithdrawal.setError("Please Enter your Company Name");
				flag = false;
			} else if (result.length() == 0) {
				et_result.setError("Please Enter your Phone no");
				flag = false;
			} else if (action.length() == 0) {
				et_action.setError("Please Enter your Date of Birth.");
				flag = false;
			} else if (risk.length() == 0) {
				et_risk.setError("Please Enter your Indirizzo e-mail.");
				flag = false;
			} else if (timetable.length() == 0) {
				et_timetable.setError("Please enter valid Email id.");
				flag = false;
			} 
			return flag;
		}

	 void initview(){
		 
		 viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
		 lv_authority = (ListView)findViewById(R.id.lv_authority);
		 et_date = (EditText)findViewById(R.id.et_date);
		 et_business = (EditText)findViewById(R.id.et_business);
		 et_product = (EditText)findViewById(R.id.et_product);
		 et_description = (EditText)findViewById(R.id.et_description);
		 et_lotnumber = (EditText)findViewById(R.id.et_lotnumber);
		 et_amountofproduct = (EditText)findViewById(R.id.et_amountofproduct);
		 et_grandfather = (EditText)findViewById(R.id.et_grandfather);
		 et_requirements = (EditText)findViewById(R.id.et_requirements);
		 et_reasonforwithdrawal = (EditText)findViewById(R.id.et_reasonforwithdrawal);
		 et_result = (EditText)findViewById(R.id.et_result);
		 et_action = (EditText)findViewById(R.id.et_action);
		 et_risk = (EditText)findViewById(R.id.et_risk);
		 et_timetable = (EditText)findViewById(R.id.et_timetable);
		 
		 findViewById(R.id.btn_authority).setOnClickListener(this);
		 findViewById(R.id.ll_add_authority).setOnClickListener(this);
		 findViewById(R.id.ll_authority_list).setOnClickListener(this);
			
	 }
	 
	 public boolean isNetworkAvailable(Context context) {
		    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
		}

	 
}
