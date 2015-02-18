package com.schedario.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.schedario.activity.BaseActivity;
import com.schedario.activity.R;

public class AnimalListAdapter  extends ArrayAdapter<HashMap<String, String>>{
	
	private BaseActivity activity;
	private ViewHolder mHolder;
	public ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();

	
	public setHomeFragmentListener listener; 
	
	public interface setHomeFragmentListener{
		public void loadHomeFragment();
	}
	
	public AnimalListAdapter(BaseActivity activity,int textViewResourceId,ArrayList<HashMap<String, String>> items) {
		super(activity,textViewResourceId, items);
		this.item = items;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return item.size();
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
			v = inflater.inflate(R.layout.animal_list_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			mHolder.tv_animal_info = (TextView)v.findViewById(R.id.tv_animal_info);
			
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}			
		
		final HashMap<String, String> hash = item.get(position);
		if(hash != null){

			mHolder.tv_animal_info.setText(hash.get("added_date")+" | "+hash.get("name"));
			
		}		
		return v;
	}

	class ViewHolder {
		public TextView tv_animal_info;
	}
	
	
}
