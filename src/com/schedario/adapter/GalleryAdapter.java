package com.schedario.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.schedario.activity.BaseActivity;
import com.schedario.activity.R;
import com.schedario.activity.ShowImage;
import com.schedario.constants.Constants;

public class GalleryAdapter extends ArrayAdapter<String>{
	


	private BaseActivity activity;
	private ViewHolder mHolder;
	public ArrayList<String> item = new ArrayList<String>();
	
	public GalleryAdapter(BaseActivity activity, int textViewResourceId, ArrayList<String> items) {
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
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_gallery, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);

			mHolder.imageView1 = (ImageView) v.findViewById(R.id.imageView1);
			
		} else {
			mHolder = (ViewHolder) v.getTag();
		}

		
		activity.imageLoader.DisplayImage(Constants.IMAGE+item.get(position), mHolder.imageView1);
		
		mHolder.imageView1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(activity,ShowImage.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("imagepath", item.get(position).toString());
				activity.startActivity(i);
			}
		});
		
		return v;
	}

	class ViewHolder {
		public ImageView imageView1;
		
	}
}
