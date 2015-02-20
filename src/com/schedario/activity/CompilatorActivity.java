package com.schedario.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.schedario.adapter.AuthorityAdapter;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

public class CompilatorActivity extends BaseActivity implements OnClickListener{

	ViewFlipper viewFlipper;
	ListView lv_Checklist;
	CheckBox checkBoxrecords, checkBoxcritical, checkBoxincoming, checkBoxsanitation, checkBoxpremises, checkBoxpremisesforeign, 
		checkBoxequipment, checkBoxmaterial, checkBoxabsent, checkBoxchanging, checkBoxhygienic, checkBoxwaste, checkwastedisposal, 
		checkBoxtemperature, checkBoxRegistration, checkBoxprocessing, checkBoxprocesses, checkBoxpackaging; 
	EditText editCompiler, editdate, editcorrective;
	 public String checkAddorShow="";
	 
	 
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.activity_compilator);
	        
	     initview();
	        
	 }
	 
	 @Override
	 public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonCheckList:	
			//if(isValid())
			if(isNetworkAvailable(this)){
				//viewFlipper.showNext();	
				checkAddorShow = "add";
				new AddorShowCompilator().execute();
			}else{
				Toast.makeText(this, "Please check internet connectivity", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.ll_add_authority:	
			//viewFlipper.showPrevious();	
			break;
		
		}
	}
	 
	 String records, critical, incoming, sanitation, premises, premisesforeign, equipment, material, absent, changing, hygienic, waste, 
	 		wastedisposal, temperature, Registration, processing, processes, packaging, compiler, date, corrective;
	 private boolean isValid() {
		 records = checkBoxrecords.getText().toString().trim();
		 critical = checkBoxcritical.getText().toString().trim();
		 incoming = checkBoxincoming.getText().toString().trim();
		 sanitation = checkBoxsanitation.getText().toString().trim();
		 premises = checkBoxpremises.getText().toString().trim();
		 premisesforeign = checkBoxpremisesforeign.getText().toString().trim();
		 equipment = checkBoxequipment.getText().toString().trim();
		 material = checkBoxmaterial.getText().toString().trim();
		 absent = checkBoxabsent.getText().toString().trim();
		 changing = checkBoxchanging.getText().toString().trim();
		 hygienic = checkBoxhygienic.getText().toString().trim();
		 waste = checkBoxwaste.getText().toString().trim();
		 wastedisposal = checkwastedisposal.getText().toString().trim();
		 temperature = checkBoxtemperature.getText().toString().trim();
		 Registration = checkBoxRegistration.getText().toString().trim();
		 processing = checkBoxprocessing.getText().toString().trim();
		 processes = checkBoxprocesses.getText().toString().trim();
		 packaging = checkBoxpackaging.getText().toString().trim();
		 compiler = editCompiler.getText().toString().trim();
		 date = editdate.getText().toString().trim();
		 corrective = editcorrective.getText().toString().trim();

		 boolean flag = true;
			if (compiler.length() == 0) {
				editCompiler.setError("Please Enter your Company name");
				flag = false;
			} else if (date.length() == 0) {
				editdate.setError("Please Enter your Address");
				flag = false;
			} else if (corrective.length() == 0) {
				editcorrective.setError("Please Enter your City name");
				flag = false;
			} 
			return flag;
		}

	 boolean resultResponse;
	 String message="";
	 ArrayList<HashMap<String, String>> arrhash = new ArrayList<HashMap<String,String>>();
	 public class AddorShowCompilator extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {

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
					req.put("compilator", compiler);
					req.put("handbook_security", records);
					req.put("control_points", critical);
					req.put("raw_materials", incoming);
					req.put("sanitation_plan", sanitation);
					req.put("premises", premises);
					req.put("foreign_material", premisesforeign);
					req.put("equipment", equipment);
					req.put("sanitization_material", material);
					req.put("absent_signs_pest", absent);
					req.put("rooms_lockers", changing);
					req.put("hygienic", hygienic);
					req.put("food_waste", waste);
					req.put("waste_disposal", wastedisposal);
					req.put("refrigerated_temperature", temperature);
					req.put("temperature_archived", Registration);
					req.put("clean_dirty_circuit", processing);
					req.put("plant_clean", processes);
					req.put("packaging_materials", packaging);
					req.put("corrective_action", corrective);
					
					response = KlHttpClient.SendHttpPost(Constants.ADD_COMPILER, req.toString());
					
					if (response != null) {
						JSONObject ob = new JSONObject(response);
						resultResponse = ob.getBoolean("status");
						message = ob.getString("message");
					}
					
					
				}else if(checkAddorShow.equals("show")){/*
						
						response = KlHttpClient.SendHttpPost(Constants.SHOW_COMPILER, req.toString());
						System.out.println("SHOW_COMPILER"+response);
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
					*/}
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
						AuthorityAdapter adapter = new AuthorityAdapter(CompilatorActivity.this, R.layout.authority_list_row, arrresult);
						lv_Checklist.setAdapter(adapter);
					}else if(checkAddorShow.equals("add")){
						
						Toast.makeText(CompilatorActivity.this, message, Toast.LENGTH_LONG).show();
					}
				}
			}
		}
 void initview(){
		 
		 viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
		 lv_Checklist = (ListView)findViewById(R.id.lv_Checklist);
		 checkBoxrecords = (CheckBox)findViewById(R.id.checkBoxrecords);
		 checkBoxcritical = (CheckBox)findViewById(R.id.checkBoxcritical);
		 checkBoxincoming = (CheckBox)findViewById(R.id.checkBoxincoming);
		 checkBoxsanitation = (CheckBox)findViewById(R.id.checkBoxsanitation);
		 checkBoxpremises = (CheckBox)findViewById(R.id.checkBoxpremises);
		 checkBoxpremisesforeign = (CheckBox)findViewById(R.id.checkBoxpremisesforeign);
		 checkBoxequipment = (CheckBox)findViewById(R.id.checkBoxequipment);
		 checkBoxmaterial = (CheckBox)findViewById(R.id.checkBoxmaterial);
		 checkBoxabsent = (CheckBox)findViewById(R.id.checkBoxabsent);
		 checkBoxchanging = (CheckBox)findViewById(R.id.checkBoxchanging);
		 checkBoxhygienic = (CheckBox)findViewById(R.id.checkBoxhygienic);
		 checkBoxwaste = (CheckBox)findViewById(R.id.checkBoxwaste);
		 checkwastedisposal = (CheckBox)findViewById(R.id.checkwastedisposal);
		 checkBoxtemperature = (CheckBox)findViewById(R.id.checkBoxtemperature);
		 checkBoxRegistration = (CheckBox)findViewById(R.id.checkBoxRegistration);
		 checkBoxprocessing = (CheckBox)findViewById(R.id.checkBoxprocessing);
		 checkBoxprocesses = (CheckBox)findViewById(R.id.checkBoxprocesses);
		 checkBoxpackaging = (CheckBox)findViewById(R.id.checkBoxpackaging);
		 
		 editCompiler = (EditText)findViewById(R.id.editCompiler);
		 editdate = (EditText)findViewById(R.id.editdate);
		 editcorrective = (EditText)findViewById(R.id.editcorrective);
		 
		 findViewById(R.id.buttonCheckList).setOnClickListener(this);
		 findViewById(R.id.ll_add_CheckList).setOnClickListener(this);
		 findViewById(R.id.ll_CheckList_list).setOnClickListener(this);
			
	 }
 
 public boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
}
