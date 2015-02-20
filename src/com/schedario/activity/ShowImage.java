package com.schedario.activity;

import com.schedario.constants.Constants;
import com.schedario.util.ImageLoader;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ShowImage extends BaseActivity{
	
	public ImageLoader imageloader;
	private ImageView iv_image;
	Animation anim1,anim2,anim3,anim4;
	private LinearLayout ll_footer;
	public int val = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image);
		
		String value = getIntent().getExtras().getString("type");
		
		anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
		anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim2);
		anim3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim3);
		anim4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim4);
		
		ll_footer = (LinearLayout) findViewById(R.id.ll_footer);
		ll_footer.setOnClickListener(this);
		
		imageloader = new ImageLoader(ShowImage.this);
		String imagePath = getIntent().getExtras().getString("imagepath");
		
		iv_image = (ImageView)findViewById(R.id.iv_image);
		
		if(value.equalsIgnoreCase("first")){
			imageloader.DisplayImage(Constants.IMAGE+imagePath, iv_image);
		}else{
			imageloader.DisplayImage(Constants.IMAGE_SECOND+imagePath, iv_image);
		}
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll_footer:
			if(val == 1){
				val = val + 1;
				iv_image.startAnimation(anim1);
			}else if(val == 2){
				val = val + 1;
				iv_image.startAnimation(anim2);
			}else if(val == 3){
				val = val + 1;
				iv_image.startAnimation(anim3);
			}else if(val == 4){
				val = 1;
				iv_image.startAnimation(anim4);
			}
			
			break;
		}
	}
}
