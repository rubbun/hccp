package com.schedario.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.schedario.activity.BaseActivity;
import com.schedario.activity.R;
import com.schedario.bean.LabBean;
import com.schedario.bean.MeasureBean;
import com.schedario.util.ImageLoader;

public class MeasureAdapter extends ArrayAdapter<MeasureBean>{
	
	private BaseActivity activity;
	private ViewHolder mHolder;
	public ImageLoader imageLoader;
	public ArrayList<MeasureBean> item = new ArrayList<MeasureBean>();
	public String date,doctor_id;
	public boolean isClicked = false;
	
	public setHomeFragmentListener listener; 
	
	public interface setHomeFragmentListener{
		public void loadHomeFragment();
	}
	
	public MeasureAdapter(BaseActivity activity,int textViewResourceId,ArrayList<MeasureBean> items) {
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
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.measure_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.tv_date = (TextView)v.findViewById(R.id.tv_date);
			mHolder.tv_name = (TextView)v.findViewById(R.id.tv_name);
			
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}			
		
		final MeasureBean mVendor = item.get(position);

		if(mVendor != null){

			mHolder.tv_date.setText(mVendor.getAdded_date());
			mHolder.tv_name.setText(mVendor.getName());
		}		
		return v;
	}

	class ViewHolder {
		public TextView tv_date,tv_name;
	}
}
