package com.schedario.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.schedario.activity.BaseActivity;
import com.schedario.activity.R;
import com.schedario.bean.UserDataBean;
import com.schedario.util.ImageLoader;

public class UserDataAdapter extends ArrayAdapter<UserDataBean>{
	
	private BaseActivity activity;
	private ViewHolder mHolder;
	public ImageLoader imageLoader;
	public ArrayList<UserDataBean> item = new ArrayList<UserDataBean>();
	public String date,doctor_id;
	public boolean isClicked = false;
	public int position= 0;
	
	public setHomeFragmentListener listener; 
	
	public interface setHomeFragmentListener{
		public void loadHomeFragment();
	}
	
	public UserDataAdapter(BaseActivity activity,int textViewResourceId,ArrayList<UserDataBean> items) {
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
		this.position = position;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.userdata_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.tv_date = (TextView)v.findViewById(R.id.tv_date);
			mHolder.tv_desc = (TextView)v.findViewById(R.id.tv_desc);
			mHolder.btn_send_mail = (Button)v.findViewById(R.id.btn_mail);
			
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}			
		
		final UserDataBean mVendor = item.get(position);

		if(mVendor != null){
			mHolder.tv_date.setText(mVendor.getDate());
			mHolder.tv_desc.setText(mVendor.getProduct_details());
		}		
		
		mHolder.btn_send_mail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("plain/text");
				i.putExtra(android.content.Intent.EXTRA_EMAIL, "");
				i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Communication to consumers");
				i.putExtra(android.content.Intent.EXTRA_TEXT, getPrivactPolicyTextStyly().toString());
				activity.startActivity(Intent.createChooser(i, "Send email"));
			}
		});
		
		return v;
	}

	class ViewHolder {
		public TextView tv_date,tv_desc;
		public Button btn_send_mail;
	}
	
	public SpannableStringBuilder getPrivactPolicyTextStyly(){
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		
		ssb.append("Company Name: "+activity.app.getUserinfo().company_name);	
		ssb.append("\n");
		ssb.append("Address: "+activity.app.getUserinfo().address);
		ssb.append("\n");
		ssb.append("City: "+activity.app.getUserinfo().city);
		ssb.append("\n");
		ssb.append("Zip Code: "+activity.app.getUserinfo().zip_code);
		ssb.append("\n");
		ssb.append("Name: "+activity.app.getUserinfo().name);	
		ssb.append("\n");
		ssb.append("Surname: "+activity.app.getUserinfo().surname);
		ssb.append("\n");
		ssb.append("Province: "+activity.app.getUserinfo().province);
		ssb.append("\n");
		ssb.append("Post Code: "+activity.app.getUserinfo().post_code);
		ssb.append("\n");
		ssb.append("Phone: "+activity.app.getUserinfo().phone);	
		ssb.append("\n");
		ssb.append("D.O.B: "+activity.app.getUserinfo().dob);
		ssb.append("\n");
		ssb.append("Email: "+activity.app.getUserinfo().email);
		ssb.append("\n");
		ssb.append("\n");
		
		
		ssb.append("Date: "+item.get(position).getDate());	
		ssb.append("\n");
		ssb.append("Company Name: "+item.get(position).getCompany_name());
		ssb.append("\n");
		ssb.append("Lot No.: "+item.get(position).getLot_number());
		ssb.append("\n");
		ssb.append("Description: "+item.get(position).getProduct_details());
		
		return ssb;
	}
	
	public SpannableStringBuilder getAllUserInfo(){
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append("Date: "+item.get(position).getDate());	
		ssb.append("\n");
		ssb.append("Company Name: "+item.get(position).getCompany_name());
		ssb.append("\n");
		ssb.append("Lot No.: "+item.get(position).getLot_number());
		ssb.append("\n");
		ssb.append("Description: "+item.get(position).getProduct_details());
		
		return ssb;
	}
}
