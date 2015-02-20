package com.schedario.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.GridView;

import com.schedario.activity.BaseActivity;
import com.schedario.activity.R;
import com.schedario.adapter.GalleryAdapter;
import com.schedario.constants.Constants;
import com.schedario.network.KlHttpClient;

@SuppressLint("ValidFragment")
public class GalleryFragment extends Fragment{
	
	public BaseActivity base;
	private ArrayList<String> list = new ArrayList<String>();
	private GalleryAdapter adapter;
	private  GridView gridView;
	
	public GalleryFragment(BaseActivity base){
		this.base = base;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_gallery, container, false);
		gridView = (GridView)v.findViewById(R.id.gv_gallery);
		
		new GalleryAsynctask().execute();
		
		return v;
	}
	public class GalleryAsynctask extends AsyncTask<Void, Void, ArrayList<String>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			base.doShowLoading();
		}

		@Override
		protected ArrayList<String> doInBackground(Void... params) {

			try {
				JSONObject req = new JSONObject();
				req.put("id", base.app.getUserinfo().user_id);
				String response = KlHttpClient.SendHttpPost(Constants.GALLERY, req.toString());
				if (response != null) {
					JSONObject ob = new JSONObject(response);
					if (ob.getBoolean("status")) {
						Log.e("!reach here", "reach here");
						JSONArray jArr = ob.getJSONArray("images");
						list.clear();
						for(int i=0; i<jArr.length(); i++){
							String image  = jArr.getJSONObject(i).getString("image");
						list.add(image);
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
		protected void onPostExecute(ArrayList<String> result) {
			super.onPostExecute(result);
			base.doRemoveLoading();
			if(result!=null){
				/*adapter = new GalleryAdapter(base, R.layout.row_gallery, result);
				gridView.setAdapter(adapter);*/
			}
		}
	}
}
