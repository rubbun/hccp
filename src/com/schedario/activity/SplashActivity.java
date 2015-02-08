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