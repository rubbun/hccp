package com.schedario.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.schedario.adapter.PulicyListAdapter;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;
import com.schedario.util.SampleAlarmReceiver;
import com.schedario.util.Utility;

public class AnimalActivity extends BaseActivity implements OnClickListener{
    SampleAlarmReceiver alarm = new SampleAlarmReceiver();
    
    Button buttonAnimal;
    public TextView textDate;
    public String month, day, year;
    ViewFlipper viewFlipper;
    public String checkAddorEdit="";
    ListView lv_animals;
    //public boolean addedFromBackground;
    String[] listAnimals = {"No Animali infestanti/No Pets weeds", "roditori/rodents", "insetti/insects", "uccelli/birds"};
    
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
        setContentView(R.layout.activity_animal);
        
        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        textDate = (TextView)findViewById(R.id.textDate);
        buttonAnimal = (Button)findViewById(R.id.buttonAnimal);
        buttonAnimal.setOnClickListener(this);
        lv_animals = (ListView)findViewById(R.id.lv_animals);
        findViewById(R.id.buttonAddAnimal).setOnClickListener(this);
		findViewById(R.id.ll_add_animal).setOnClickListener(this);
		findViewById(R.id.ll_animals_list).setOnClickListener(this);
		
		parseSetDate();
		textDate.setText(month+"/"+day+"/"+year);
        /*boolean alarmUp = (PendingIntent.getBroadcast(this, 10, new Intent(this, SampleAlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);
    	if(!alarmUp)
        alarm.setAlarm(this);*/
        
    }
    LinearLayout laydivider = null;
    void dialogPulicySelection(){
    	
    	final Dialog dialog = new Dialog(this, android.R.style.Animation_Dialog);
    	dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
    	LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View lay = null;
    	LinearLayout linear = new LinearLayout(this);
    	linear.setOrientation(1);
    	linear.setGravity(Gravity.CENTER);
    	for (int i = 0; i < 4; i++) {
    		lay = inflater.inflate(R.layout.animal_list_row, null);
    		TextView text = (TextView) lay.findViewById(R.id.tv_animal_info);
    		text.setText(listAnimals[i]);
        	text.setTag(i);
        	text.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					buttonAnimal.setText(((TextView)v).getText().toString());
					dialog.dismiss();
				}
			});
        	
        	linear.addView(lay);
		}
    	
    	
    	dialog.setContentView(linear);
    	dialog.show();
    	
    }
    
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
		case R.id.buttonAnimal:
			
			dialogPulicySelection();
			break;
		case R.id.ll_add_animal:
			viewFlipper.showPrevious();	
			break;
		case R.id.ll_animals_list:
			
			if(isNetworkAvailable(this)){
				viewFlipper.showNext();	
				checkAddorEdit = "show";
				new AddorShowPulicy().execute();
			}else{
				Toast.makeText(this, "Please check internet connectivity", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.buttonAddAnimal:
			
		if(isNetworkAvailable(this)){
			checkAddorEdit = "add";
			if(!buttonAnimal.getText().toString().equals("Select Animal")){
				Constants.ADD_ANIMAL_CHECK = textDate.getText().toString()+"("+"Animal"+")";
				String check = Utility.getValueFromPersistence(this, Constants.ADD_ANIMAL_CHECK);
				if(Utility.getValueFromPersistence(AnimalActivity.this, Constants.ADD_ANIMAL_CHECK).equalsIgnoreCase("NA")
						&& !Utility.getValueFromPersistence(AnimalActivity.this, Constants.ADD_ANIMAL_CHECK).equalsIgnoreCase("Yes")){
				new AddorShowPulicy().execute();
				}else{
					Toast.makeText(this, "Already Added an animal for today", Toast.LENGTH_LONG).show();
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
				
				if(checkAddorEdit.equals("add")){
					req.put("name", buttonAnimal.getText().toString());
					parseSetDate();
					//textDate.setText(month+"/"+day+"/"+year);
					req.put("added_date", month+"/"+day+"/"+year);
					Constants.ADD_ANIMAL_CHECK = month+"/"+day+"/"+year+"("+"Animal"+")";
					String check = Utility.getValueFromPersistence(AnimalActivity.this, Constants.ADD_ANIMAL_CHECK);
					if(Utility.getValueFromPersistence(AnimalActivity.this, Constants.ADD_ANIMAL_CHECK).equalsIgnoreCase("NA")
							&& !Utility.getValueFromPersistence(AnimalActivity.this, Constants.ADD_ANIMAL_CHECK).equalsIgnoreCase("Yes"))
					
					response = KlHttpClient.SendHttpPost(Constants.ADD_ANIMAL, req.toString());
					if (response != null) {
						JSONObject ob = new JSONObject(response);
						result = ob.getBoolean("status");
						message = ob.getString("message");
					}
				}else if(checkAddorEdit.equals("show")){
					
					response = KlHttpClient.SendHttpPost(Constants.SHOW_ANIMAL, req.toString());
					if (response != null) {
						JSONObject ob = new JSONObject(response);
						result = ob.getBoolean("status");
						
						if (result) {
							Log.e("!reach here", "reach here");
							JSONArray jArr = ob.getJSONArray("animals");
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
				if(checkAddorEdit.equals("show")){
					PulicyListAdapter adapter = new PulicyListAdapter(AnimalActivity.this, R.layout.pulicylist_row, arrresult);
					lv_animals.setAdapter(adapter);
				}else if(checkAddorEdit.equals("add")){
					Toast.makeText(AnimalActivity.this, message, Toast.LENGTH_LONG).show();
					Constants.ADD_ANIMAL_CHECK = textDate.getText().toString()+"("+"Animal"+")";
					Utility.storeValueOnPersistence(AnimalActivity.this, Constants.ADD_ANIMAL_CHECK, "Yes");
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