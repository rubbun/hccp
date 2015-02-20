package com.schedario.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.schedario.activity.R;
import com.schedario.activity.BaseActivity;


public class AuthorityAdapter extends ArrayAdapter<HashMap<String, String>> implements OnClickListener{
	
	private BaseActivity activity;
	private ViewHolder mHolder;
	public ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();

	
	public setHomeFragmentListener listener; 
	
	public interface setHomeFragmentListener{
		public void loadHomeFragment();
	}
	
	public AuthorityAdapter(BaseActivity activity,int textViewResourceId,ArrayList<HashMap<String, String>> items) {
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
			v = vi.inflate(R.layout.authority_list_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			//mHolder.tv_id_info = (TextView)v.findViewById(R.id.tv_id_info);
			mHolder.tv_date_info = (TextView)v.findViewById(R.id.tv_date_info);
			mHolder.tv_business_info = (TextView)v.findViewById(R.id.tv_business_info);
			//mHolder.tv_product_info = (TextView)v.findViewById(R.id.tv_product_info);
			mHolder.tv_decription_info = (TextView)v.findViewById(R.id.tv_decription_info);
			mHolder.tv_lot_info = (TextView)v.findViewById(R.id.tv_lot_info);
			/*mHolder.tv_amount_info = (TextView)v.findViewById(R.id.tv_amount_info);
			mHolder.tv_grandfather_info = (TextView)v.findViewById(R.id.tv_grandfather_info);
			mHolder.tv_requirement_info = (TextView)v.findViewById(R.id.tv_requirement_info);
			mHolder.tv_reason_info = (TextView)v.findViewById(R.id.tv_reason_info);
			mHolder.tv_result_info = (TextView)v.findViewById(R.id.tv_result_info);
			mHolder.tv_action_info = (TextView)v.findViewById(R.id.tv_action_info);
			mHolder.tv_risk_info = (TextView)v.findViewById(R.id.tv_risk_info);
			mHolder.tv_time_info = (TextView)v.findViewById(R.id.tv_time_info);*/
			mHolder.buttonEmail = (Button)v.findViewById(R.id.buttonEmail);
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}			
		
		final HashMap<String, String> hash = item.get(position);
		if(hash != null){

			//mHolder.tv_id_info.setText(hash.get("id"));
			mHolder.tv_date_info.setText(hash.get("added_date"));
			mHolder.tv_business_info.setText(hash.get("business_name_company"));
			//mHolder.tv_product_info.setText(hash.get("product"));
			mHolder.tv_decription_info.setText(hash.get("description"));
			mHolder.tv_lot_info.setText(hash.get("lot_number"));
			/*mHolder.tv_amount_info.setText(hash.get("amount_product"));
			mHolder.tv_grandfather_info.setText(hash.get("grandfather_meets"));
			mHolder.tv_requirement_info.setText(hash.get("requirements"));
			mHolder.tv_reason_info.setText(hash.get("reason_withdrawal"));
			mHolder.tv_result_info.setText(hash.get("result_investigation"));
			mHolder.tv_action_info.setText(hash.get("action_prevent"));
			mHolder.tv_risk_info.setText(hash.get("risk_consumers"));
			mHolder.tv_time_info.setText(hash.get("timetable_withdrawal_product"));*/
			
			String sendEmailBody="";
			sendEmailBody = Html.fromHtml("<b>Id:</b>").toString()+hash.get("id")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>date: </b>").toString()+hash.get("added_date")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>Business name company: </b>").toString()+hash.get("business_name_company")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>product: </b>").toString()+hash.get("product")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>Description: </b>").toString()+hash.get("description")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>Lot number: </b>").toString()+hash.get("lot_number")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>Amount of product: </b>").toString()+hash.get("amount_product")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>grandfather meets: </b>").toString()+hash.get("grandfather_meets")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>requirements: </b>").toString()+hash.get("requirements")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>Reason for withdrawal: </b>").toString()+hash.get("reason_withdrawal")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>result of the investigation: </b>").toString()+hash.get("result_investigation")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>Action taken to prevent: </b>").toString()+hash.get("action_prevent")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>risk consumers: </b>").toString()+hash.get("risk_consumers")+"\n";
			sendEmailBody = sendEmailBody + Html.fromHtml("<b>Timetable for the withdrawal of the product: </b>").toString()+hash.get("timetable_withdrawal_product")+"\n";
			
			mHolder.buttonEmail.setTag(sendEmailBody);
			mHolder.buttonEmail.setOnClickListener(new OnItemClickListener(position));
			
		}		
		return v;
	}

	class ViewHolder {
		public TextView tv_id_info, tv_date_info, tv_business_info, tv_product_info, tv_decription_info, tv_lot_info, tv_amount_info, 
			tv_grandfather_info, tv_requirement_info, tv_reason_info, tv_result_info, tv_action_info, tv_risk_info, tv_time_info;
				Button buttonEmail;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		sharewithemail(v);
	}
	
	  
    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements OnClickListener{           
        private int mPosition;
        
       OnItemClickListener(int position){
        	 mPosition = position;
        }
        
       
		@Override
        public void onClick(View arg0) {
        	switch (arg0.getId()) {
			case R.id.buttonEmail:
				
				sharewithemail(arg0);
				break;
			
			default:
				break;
			}
        	
        	
        }               
    }
	
	public void sharewithemail(View v){				
		 final Intent intent = new Intent(Intent.ACTION_SEND);
          intent.setType("plain/text");
          intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
          intent.putExtra(Intent.EXTRA_SUBJECT, "Authority");
          intent.putExtra(Intent.EXTRA_TEXT, v.getTag().toString());
          activity.startActivity(intent);
          
	}
	
}
