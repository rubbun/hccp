package com.schedario.activity;

import com.schedario.constants.Constants;
import com.schedario.util.ImageLoader;

import android.os.Bundle;
import android.widget.ImageView;

public class ShowImage extends BaseActivity{
	
	public ImageLoader imageloader;
	private ImageView iv_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image);
		
		imageloader = new ImageLoader(ShowImage.this);
		String imagePath = getIntent().getExtras().getString("imagepath");
		
		iv_image = (ImageView)findViewById(R.id.iv_image);
		imageloader.DisplayImage(Constants.IMAGE+imagePath, iv_image);
		
	}
}
