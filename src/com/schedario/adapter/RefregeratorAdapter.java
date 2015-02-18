package com.schedario.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schedario.activity.BaseActivity;
import com.schedario.activity.R;
import com.schedario.bean.RefregeratorBean;
import com.schedario.util.ImageLoader;

public class RefregeratorAdapter extends ArrayAdapter<RefregeratorBean>{
	
	private BaseActivity activity;
	private ViewHolder mHolder;
	public ImageLoader imageLoader;
	public ArrayList<RefregeratorBean> item = new ArrayList<RefregeratorBean>();
	public String date,doctor_id;
	public boolean isClicked = false;
	
	public setHomeFragmentListener listener; 
	
	public interface setHomeFragmentListener{
		public void loadHomeFragment();
	}
	
	public RefregeratorAdapter(BaseActivity activity,int textViewResourceId,ArrayList<RefregeratorBean> items) {
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
			v = vi.inflate(R.layout.refrigerator_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.tv_date_info = (TextView)v.findViewById(R.id.tv_date_info);
			mHolder.ll_cointainer = (LinearLayout)v.findViewById(R.id.ll_cointainer);
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}			
		
		final RefregeratorBean mVendor = item.get(position);

		if(mVendor != null){

			mHolder.tv_date_info.setText(mVendor.getDate());
			
			System.out.println("!!size:"+mVendor.getList().size());
			
			for(int i = 0; i <mVendor.getList().size() ; i++){
				View view = View.inflate(activity, R.layout.refregerator_temp_list_row, null);
				
				TextView tv_refrigerator_name = (TextView)view.findViewById(R.id.tv_refrigerator_name);
				tv_refrigerator_name.setText(""+mVendor.getList().get(i).getRefregerator_id());
				
				System.out.println("!!values here"+mVendor.getList().get(i).getRef_temp());
				
				TextView tv_temp = (TextView)view.findViewById(R.id.tv_temp);
				tv_temp.setText(""+mVendor.getList().get(i).getRef_temp());
				
				mHolder.ll_cointainer.addView(view);
			}
		}		
		return v;
	}

	class ViewHolder {
		public TextView tv_date_info;
		public LinearLayout ll_cointainer;
	}
}
