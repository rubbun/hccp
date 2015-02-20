package com.schedario.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;

import com.schedario.adapter.PulicyListAdapter;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;
import com.schedario.util.Utility;

public class PulicyActivity extends BaseActivity implements OnClickListener{
    
	Spinner buttonPulicy;
    public TextView textDate;
    public String month, day, year;
    ViewFlipper viewFlipper;
    public String checkAddorShow="";
    ListView lv_pulicy;
    private int selection_value = -1;
    //public boolean addedFromBackground;
    private List<String> spinnerArray =  new ArrayList<String>();
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
         if(textDate!=null)
        	 parseSetDate();
         	 textDate.setText(month+"/"+day+"/"+year);
        	 
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulicy);
        
        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        textDate = (TextView)findViewById(R.id.textDate);
        buttonPulicy = (Spinner)findViewById(R.id.buttonPulicy);
        spinnerArray.add("PO");
        spinnerArray.add("PS");
        buttonPulicy.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				selection_value = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        		PulicyActivity.this, android.R.layout.simple_spinner_item, spinnerArray);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			buttonPulicy.setAdapter(adapter);
			selection_value = 0;
        
        //buttonPulicy.setOnClickListener(this);
        lv_pulicy = (ListView)findViewById(R.id.lv_pulicy);
        findViewById(R.id.buttonAddPulicy).setOnClickListener(this);
		findViewById(R.id.ll_add_pulicy).setOnClickListener(this);
		findViewById(R.id.ll_pulicy_list).setOnClickListener(this);
		
		parseSetDate();
		textDate.setText(month+"/"+day+"/"+year);
        
        
    }
   /* LinearLayout laydivider = null;
    void dialogPulicySelection(){
    	
    	final Dialog dialog = new Dialog(this, android.R.style.Animation_Dialog);
    	dialog.getWindow().setLayout(500, LayoutParams.WRAP_CONTENT); 
    	LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	LinearLayout linear = new LinearLayout(this);
    	linear.setOrientation(1);
    	linear.setGravity(Gravity.CENTER);
    	for (int i = 0; i < 2; i++) {
    		View lay = inflater.inflate(R.layout.pulicylist_row, null);
    		TextView text = (TextView) lay.findViewById(R.id.tv_pulicy_info);
    		if(i==0){
    			text.setText("PO");
    		}else{
    			text.setText("PS");
    		}
        	text.setTag(i);
        	
        	text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					buttonPulicy.setText(((TextView)v).getText().toString());
					dialog.dismiss();
				}
			});
        	linear.addView(lay);
		}
    	
    	
    	dialog.setContentView(linear);
    	dialog.show();
    	
    }*/
    
    public void parseSetDate(){
    	
    	final Calendar calendar = Calendar.getInstance();
    	if(calendar.get((Calendar.MONTH))+1<10){
    		
    		month = "0"+(calendar.get((Calendar.MONTH))+1);
    	}else {
    		month = ""+(calendar.get((Calendar.MONTH))+1);
    	}
    	
    	if(calendar.get(Calendar.DAY_OF_MONTH)<10){
    		
    		day = "0"+(calendar.get(Calendar.DAY_OF_MONTH));
    	}else {
    		day = ""+(calendar.get(Calendar.DAY_OF_MONTH));
    	}
    	year = ""+calendar.get(Calendar.YEAR);
    	/*runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				textDate.setText(month+"/"+day+"/"+year);
			}
		});*/
    	
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonPulicy:
			
			//dialogPulicySelection();
			break;
		case R.id.ll_add_pulicy:
			viewFlipper.showPrevious();	
			break;
		case R.id.ll_pulicy_list:
			
			if(isNetworkAvailable(this)){
				viewFlipper.showNext();	
				checkAddorShow = "show";
				new AddorShowPulicy().execute();
			}else{
				Toast.makeText(this, "Please check internet connectivity", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.buttonAddPulicy:
			
		if(isNetworkAvailable(this)){
			checkAddorShow = "add";
			if(!spinnerArray.get(selection_value).toString().equals("Select Pulicy")){
				Constants.ADD_PULICY_CHECK = textDate.getText().toString()+"("+"Pulicy"+")";
				String check = Utility.getValueFromPersistence(this, Constants.ADD_PULICY_CHECK);
				if(!Utility.getValueFromPersistence(PulicyActivity.this, Constants.ADD_PULICY_CHECK).equalsIgnoreCase("Yes")){
				new AddorShowPulicy().execute();
				}else{
					Toast.makeText(this, "Already Added a pulicy for today", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(this, "Please select a pulicy", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(this, "Please check internet connectivity", Toast.LENGTH_LONG).show();
		}
			break;
		default:
			break;
		}
	}

	
	boolean result;
	String message="";
	ArrayList<HashMap<String, String>> arrhash = new ArrayList<HashMap<String,String>>();
	public class AddorShowPulicy extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {

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
					req.put("name", spinnerArray.get(selection_value).toString());
					parseSetDate();
					//textDate.setText(month+"/"+day+"/"+year);
					Constants.ADD_PULICY_CHECK = month+"/"+day+"/"+year+"("+"Pulicy"+")";
					req.put("added_date", month+"/"+day+"/"+year);
					if(!Utility.getValueFromPersistence(PulicyActivity.this, Constants.ADD_PULICY_CHECK).equalsIgnoreCase("Yes"))
					
					response = KlHttpClient.SendHttpPost(Constants.ADD_PULICY, req.toString());
					if (response != null) {
						JSONObject ob = new JSONObject(response);
						result = ob.getBoolean("status");
						message = ob.getString("message");
					}
					
				}else if(checkAddorShow.equals("show")){
					
					response = KlHttpClient.SendHttpPost(Constants.SHOW_PULICY, req.toString());
					if (response != null) {
						JSONObject ob = new JSONObject(response);
						result = ob.getBoolean("status");
						
						if (result) {
							Log.e("!reach here", "reach here");
							JSONArray jArr = ob.getJSONArray("RefFreezePos");
							arrhash.clear();
							HashMap<String, String> hash;
							for(int i=0; i<jArr.length(); i++){
								
								hash = new HashMap<String, String>();
								JSONObject jObj = jArr.getJSONObject(i);
								hash.put("id", jObj.getString("id"));
								hash.put("name", jObj.getString("name"));
								hash.put("added_date", jObj.getString("added_date"));
								
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
			if(result){
				if(checkAddorShow.equals("show")){
					PulicyListAdapter adapter = new PulicyListAdapter(PulicyActivity.this, R.layout.pulicylist_row, arrresult);
					lv_pulicy.setAdapter(adapter);
				}else if(checkAddorShow.equals("add")){
					Toast.makeText(PulicyActivity.this, message, Toast.LENGTH_LONG).show();
					Constants.ADD_PULICY_CHECK = textDate.getText().toString()+"("+"Pulicy"+")";
					Utility.storeValueOnPersistence(PulicyActivity.this, Constants.ADD_PULICY_CHECK, "Yes");
					/*if(addedFromBackground){
						
						alarm.cancelAlarm(PulicyActivity.this);
						alarm.setAlarm(PulicyActivity.this);
					}*/
					//addedFromBackground = false;
				}
			}
		}
	}

	
	public boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}