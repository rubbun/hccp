package com.schedario.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schedario.activity.BaseActivity;
import com.schedario.activity.R;
import com.schedario.bean.SupplierBean;
import com.schedario.util.ImageLoader;

public class SupplierAdapter extends ArrayAdapter<SupplierBean>{
	
	private BaseActivity activity;
	private ViewHolder mHolder;
	public ImageLoader imageLoader;
	public ArrayList<SupplierBean> item = new ArrayList<SupplierBean>();
	public String date,doctor_id;
	public boolean isClicked = false;
	
	public setHomeFragmentListener listener; 
	
	public interface setHomeFragmentListener{
		public void loadHomeFragment();
	}
	
	public SupplierAdapter(BaseActivity activity,int textViewResourceId,ArrayList<SupplierBean> items) {
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
			v = vi.inflate(R.layout.supplierlist_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
		
			mHolder.tv_product_info = (TextView)v.findViewById(R.id.tv_product_info);
			mHolder.tv_supplier_info = (TextView)v.findViewById(R.id.tv_supplier_info);
			mHolder.tv_phone_info = (TextView)v.findViewById(R.id.tv_phone_info);
			mHolder.tv_contact_info = (TextView)v.findViewById(R.id.tv_contact_info);
			mHolder.tv_address_info = (TextView)v.findViewById(R.id.tv_address_info);
			mHolder.ll_container = (LinearLayout)v.findViewById(R.id.ll_container);
			
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}			
		
		final SupplierBean mVendor = item.get(position);

		if(mVendor != null){

			mHolder.tv_product_info.setText(mVendor.getProduct());
			mHolder.tv_supplier_info.setText(mVendor.getSupplier());
			mHolder.tv_phone_info.setText(mVendor.getPhone());
			mHolder.tv_contact_info.setText(mVendor.getContact_person());
			mHolder.tv_address_info.setText(mVendor.getAddress());
		}		
		return v;
	}

	class ViewHolder {
		public TextView tv_product_info,tv_supplier_info,tv_phone_info,tv_contact_info,tv_address_info;
		public Button btn_edit;
		private LinearLayout ll_container;
	}
}
