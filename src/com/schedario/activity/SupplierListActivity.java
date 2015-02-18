package com.schedario.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.schedario.adapter.SupplierAdapter;
import com.schedario.bean.SupplierBean;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

public class SupplierListActivity extends BaseActivity {
	
	private String product,supplier,phone,contact_person,address;

	private EditText et_product, et_supplier, et_phone, et_contact_person,
			et_address;
	
	private Button btn_add;
	private ArrayList<SupplierBean> list = new ArrayList<SupplierBean>();
	private ListView lv_supplier;
	public SupplierAdapter adapter;
	
	private LinearLayout ll_add_supplier,ll_supplier_list,ll_include_add_supplier,ll_include_supplier_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier);
		
		et_product= (EditText)findViewById(R.id.et_product);
		et_supplier= (EditText)findViewById(R.id.et_supplier);
		et_phone= (EditText)findViewById(R.id.et_phone);
		et_contact_person= (EditText)findViewById(R.id.et_contact_person);
		et_address= (EditText)findViewById(R.id.et_address);
		
		btn_add = (Button)findViewById(R.id.btn_add);
		btn_add.setOnClickListener(this);
		
		ll_add_supplier = (LinearLayout)findViewById(R.id.ll_add_supplier);
		ll_add_supplier.setOnClickListener(this);
		
		ll_supplier_list = (LinearLayout)findViewById(R.id.ll_supplier_list);
		ll_supplier_list.setOnClickListener(this);
		
		ll_include_add_supplier = (LinearLayout)findViewById(R.id.ll_include_add_supplier);
		ll_include_supplier_list = (LinearLayout)findViewById(R.id.ll_include_supplier_list);
		
		lv_supplier = (ListView)findViewById(R.id.lv_supplier);
		
		displayView(0);
	}
	
	private void displayView(int position) {
		switch (position) {
		case 0:
			ll_include_supplier_list.setVisibility(View.GONE);
			ll_include_add_supplier.setVisibility(View.VISIBLE);
			break;
		case 1:
			ll_include_supplier_list.setVisibility(View.VISIBLE);
			ll_include_add_supplier.setVisibility(View.GONE);
			
			new SupplierListAsynctask().execute();
			break;
		}
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_add:
			addSupplier();
			break;
		case R.id.ll_add_supplier:
			displayView(0);
			break;

		case R.id.ll_supplier_list:
			displayView(1);
			break;
		}
	}
	private void addSupplier() {
		if (isValid()) {
			sendValueToServer();
		}
	}
	private boolean isValid() {
		product = et_product.getText().toString().trim();
		supplier = et_supplier.getText().toString().trim();
		phone = et_phone.getText().toString().trim();
		contact_person = et_contact_person.getText().toString().trim();
		address = et_address.getText().toString().trim();

		boolean flag = true;
		if (product.length() == 0) {
			et_product.setError("Please Enter product name");
			flag = false;
		} else if (supplier.length() == 0) {
			et_supplier.setError("Please Enter supplier name");
			flag = false;
		} else if (phone.length() == 0) {
			et_phone.setError("Please Enter your phone no");
			flag = false;
		} else if (contact_person.length() == 0) {
			et_contact_person.setError("Please Enter contact person name");
			flag = false;
		} else if (address.length() == 0) {
			et_address.setError("Please Enter your address");
			flag = false;
		} 
		return flag;
	}
	private void sendValueToServer() {
		new CallServerForAddSupplier().execute();
	}
	
	public class CallServerForAddSupplier extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				JSONObject req = new JSONObject();
				req.put("user_id", app.getUserinfo().getUser_id());
				req.put("product", product);
				req.put("supplier", supplier);
				req.put("phone", phone);
				req.put("contact_person", contact_person);
				req.put("street address", address);

				String response = KlHttpClient.SendHttpPost(Constants.ADD_SUPPLIER, req.toString());
				if (response != null) {
					try {
						JSONObject ob = new JSONObject(response);
						if (ob.getBoolean("status")) {
							return ob.getString("message");
						}else{
							return ob.getString("message");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			doRemoveLoading();
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
			et_product.setText("");
			et_supplier.setText("");
			et_phone.setText("");
			et_contact_person.setText("");
			et_address.setText("");
			
			displayView(1);
		}
	}
	
	public class SupplierListAsynctask extends AsyncTask<Void, Void, ArrayList<SupplierBean>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}

		@Override
		protected ArrayList<SupplierBean> doInBackground(Void... params) {

			try {
				JSONObject req = new JSONObject();
				req.put("user_id", app.getUserinfo().getUser_id());
				String response = KlHttpClient.SendHttpPost(Constants.SHOW_SUPPLIER, req.toString());
				if (response != null) {
					JSONObject ob = new JSONObject(response);
					if (ob.getBoolean("status")) {
						Log.e("!reach here", "reach here");
						JSONArray jArr = ob.getJSONArray("product_details");
						list.clear();
						for(int i=0; i<jArr.length(); i++){
							JSONObject job = jArr.getJSONObject(i);
							list.add(new SupplierBean(job.getString("user_id"),
									job.getString("product"),
									job.getString("supplier"),
									job.getString("phone"),
									job.getString("contact_person"),
									job.getString("street_address")));
						}
						return list;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<SupplierBean> result) {
			super.onPostExecute(result);
			doRemoveLoading();
			if(result!=null){
				adapter = new SupplierAdapter(SupplierListActivity.this, R.layout.supplierlist_row, result);
				lv_supplier.setAdapter(adapter);
			}
		}
	}
}
