package com.schedario.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.schedario.utils.Appsettings;
import com.schedario.utils.LoginInfo;

public class BaseActivity extends Activity implements OnClickListener {

	public Appsettings app = null;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (Appsettings) getApplication();
		if (!app.init) {
			app.init = true;
			app.setLogininfo(new LoginInfo(this));

		}
	}

	@Override
	public void onClick(View v) {

	}

	public void hideKeyBoard(EditText et) {
		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

	public void doShowLoading() {
		dialog = new ProgressDialog(BaseActivity.this);
		dialog.setMessage("Please wait..........");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.show();

	}

	public void doRemoveLoading() {
		dialog.dismiss();

	}

	
}
