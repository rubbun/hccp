package com.schedario.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.schedario.activity.BaseActivity;
import com.schedario.activity.R;

public class CompilatorAdapter  extends ArrayAdapter<String>{
	
	private BaseActivity activity;
	private ViewHolder mHolder;
	public ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();

	
	public setHomeFragmentListener listener; 
	public JSONArray jsonArray;
	
	public interface setHomeFragmentListener{
		public void loadHomeFragment();
	}
	
	public CompilatorAdapter(BaseActivity activity,JSONArray jsonArray,int textViewResourceId,ArrayList<String> items) {
		super(activity,textViewResourceId, items);
		this.jsonArray = jsonArray;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return jsonArray.length();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView( final int position,  View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.row_compilator, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			mHolder.tv_info = (TextView)v.findViewById(R.id.tv_info);
			mHolder.btn_email = (Button)v.findViewById(R.id.btn_email);
			
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}			
		
		JSONObject object;
		try {
			object = jsonArray.getJSONObject(position);
			if(object != null){
				
				mHolder.tv_info.setText(object.getString("compilator")+"\n"+object.getString("added_date"));	
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		mHolder.btn_email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try{
					JSONObject object;
					object = jsonArray.getJSONObject(position);
					String st1 = "The records of the company handbook for food safety are updated:"+((object.getString("handbook_security").equalsIgnoreCase("0")?"NO":"YES"));
					String st2 = "The critical control points are monitored and recorded:"+((object.getString("control_points").equalsIgnoreCase("0") ?"NO":"YES"));
					String st3 = "The incoming raw materials are controlled:"+(object.getString("raw_materials").equalsIgnoreCase("0") ?"NO":"YES");
					String st4 = "The sanitation plan provided for herein is applied:"+(object.getString("sanitation_plan").equalsIgnoreCase("0") ?"NO":"YES");
					String st5 = "The premises are clean:"+(object.getString("premises").equalsIgnoreCase("0") ?"NO":"YES");
					String st6 = "The premises are clear of foreign material:"+(object.getString("foreign_material").equalsIgnoreCase("0") ?"NO":"YES");
					String st7 = "The equipment and utensils you have cleaned:"+(object.getString("equipment").equalsIgnoreCase("0") ?"NO":"YES");
					String st8 = "The material for the sanitization is properly stored and segregated:"+(object.getString("sanitization_material").equalsIgnoreCase("0") ?"NO":"YES");
					String st9 = "Are absent signs of pests:"+(object.getString("absent_signs_pest").equalsIgnoreCase("0") ?"NO":"YES");
					String st10 = "The changing rooms and lockers personnel are clean and tidy:"+(object.getString("rooms_lockers").equalsIgnoreCase("0") ?"NO":"YES");
					String st11 = "The hygienic means washing hands are complete:"+(object.getString("hygienic").equalsIgnoreCase("0") ?"NO":"YES");
					String st12 = "The waste is placed away from food:"+(object.getString("food_waste").equalsIgnoreCase("0") ?"NO":"YES");
					String st13 = "Waste disposal is done properly:"+(object.getString("waste_disposal").equalsIgnoreCase("0") ?"NO":"YES");
					String st14 = "The temperature of refrigerated and non-refrigerated is respected:"+(object.getString("refrigerated_temperature").equalsIgnoreCase("0") ?"NO":"YES");
					String st15 = "Registration forms are properly filled in temperature / archived:"+(object.getString("temperature_archived").equalsIgnoreCase("0") ?"NO":"YES");
					String st16 = "The processing flow respects the separation between clean and dirty circuit:"+(object.getString("clean_dirty_circuit").equalsIgnoreCase("0") ?"NO":"YES");
					String st17 = "All processes are carried out in suitable environmental conditions and plant clean:"+(object.getString("plant_clean").equalsIgnoreCase("0") ?"NO":"YES");
					String st18 = "The packaging materials and packaging are protected and cleany:"+(object.getString("packaging_materials").equalsIgnoreCase("0") ?"NO":"YES");
					String st19 = "The packaging materials and packaging are protected and cleany:"+(object.getString("corrective_action").equalsIgnoreCase("0") ?"NO":"YES");
	
				String st = st1+"\n"+st2+"\n"+st3+"\n"+st4+"\n"+st5+"\n"+st6+"\n"+st7+"\n"+st8+"\n"+st9+"\n"+st10+"\n"+st11+"\n"+st12+"\n"+st13+"\n"+st14+"\n"+st15+"\n"+st16+"\n"+st17+"\n"+st18+"\n"+st19;
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("plain/text");
				i.putExtra(android.content.Intent.EXTRA_EMAIL, "");
				i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Compilator");
				i.putExtra(android.content.Intent.EXTRA_TEXT, st);
				activity.startActivity(Intent.createChooser(i, "Send email"));
				}catch(Exception e){
					
				}
				
				
			}
		});
				
		return v;
	}

	class ViewHolder {
		public TextView tv_info;
		public Button btn_email;
	}
	
	
}
