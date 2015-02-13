package com.schedario.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Schedario extends BaseActivity {
	
	private Intent mIntent;
	private ImageView iv_materie,iv_maintenance,iv_supplier;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedario);
		
		iv_materie = (ImageView)findViewById(R.id.iv_materie);
		iv_materie.setOnClickListener(this);
		
		iv_maintenance = (ImageView)findViewById(R.id.iv_maintenance);
		iv_maintenance.setOnClickListener(this);
		
		iv_supplier = (ImageView)findViewById(R.id.iv_supplier);
		iv_supplier.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.iv_materie:
			mIntent = new Intent(Schedario.this,MaterieActivity.class);
			startActivity(mIntent);
			break;
			
		case R.id.iv_maintenance:
			mIntent = new Intent(Schedario.this,MaintenanceActivity.class);
			startActivity(mIntent);
			break;
			
		case R.id.iv_supplier:
			mIntent = new Intent(Schedario.this,SupplierListActivity.class);
			startActivity(mIntent);
			break;
		}
	}
}
