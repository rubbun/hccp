package com.schedario.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.schedario.activity.BaseActivity;
import com.schedario.activity.R;
import com.schedario.activity.ShowImage;
import com.schedario.bean.SecondImageBean;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

public class MaintanenceGalleryAdapter extends ArrayAdapter<SecondImageBean> {

	private BaseActivity activity;
	private ViewHolder mHolder;
	int pos= -1;
	public ArrayList<SecondImageBean> item = new ArrayList<SecondImageBean>();

	public MaintanenceGalleryAdapter(BaseActivity activity,
			int textViewResourceId, ArrayList<SecondImageBean> items) {
		super(activity, textViewResourceId, items);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_gallery, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);

			mHolder.imageView1 = (ImageView) v.findViewById(R.id.imageView1);
			mHolder.ll_container = (LinearLayout)v.findViewById(R.id.ll_container);

		} else {
			mHolder = (ViewHolder) v.getTag();
		}

		activity.imageLoader.DisplayImage(Constants.IMAGE_SECOND
				+ item.get(position).getImage_name(), mHolder.imageView1);
		
		mHolder.ll_container.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(activity, "Test", Toast.LENGTH_LONG).show();
				
			}
		});

		mHolder.imageView1.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				

				AlertDialog.Builder alert = new AlertDialog.Builder(activity);
				alert.setMessage("Are you sure, you want to delete?");
				alert.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

								new AsyncDeleteSecondImage().execute(""+item.get(position).getId());
								item.remove(position);
								notifyDataSetChanged();
							}
						});

				alert.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						});
				alert.show();
				return false;
			}
		});

		mHolder.imageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(activity, ShowImage.class)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("imagepath", item.get(position).getImage_name());
				i.putExtra("type", "second");
				activity.startActivity(i);
			}
		});

		return v;
	}

	class ViewHolder {
		public ImageView imageView1;
		public LinearLayout ll_container;
	}
	
	public class AsyncDeleteSecondImage extends AsyncTask<String, Void, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		@Override
		protected String doInBackground(String... params) {
			JSONObject req = new JSONObject();
			try {
				int val1 = Integer.parseInt(params[0]);
				req.put("user_id", activity.app.getUserinfo().getUser_id());
				req.put("id", params[0]);
				String respponse = KlHttpClient.SendHttpPost(Constants.DELETE_IMAGE_SECOND, req.toString());
				if(respponse != null){
					JSONObject ob = new JSONObject(respponse);
					if(ob.getBoolean("status")){
						return ob.getString("message");
					}else{
						return ob.getString("message");
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
			
		}
	}

}
