package com.schedario.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

	private static final int SPLASH_DISPLAY_TIME = 4000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(app.getLogininfo().getLoginStatus()){
			Intent i = new Intent(SplashActivity.this,Schedario.class);
			startActivity(i);
			finish();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
				SplashActivity.this.startActivity(mainIntent);

				SplashActivity.this.finish();

				overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
		}, SPLASH_DISPLAY_TIME);
	}
}