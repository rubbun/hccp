package com.schedario.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Schedario extends BaseActivity {

	private Intent mIntent;
	private ImageView iv_materie, iv_maintenance, iv_supplier, iv_frigoriferi,
			iv_autorita, iv_congelatori, iv_laboratorio,consumatori,conformita;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedario);

		iv_materie = (ImageView) findViewById(R.id.iv_materie);
		iv_materie.setOnClickListener(this);

		iv_maintenance = (ImageView) findViewById(R.id.iv_maintenance);
		iv_maintenance.setOnClickListener(this);

		iv_supplier = (ImageView) findViewById(R.id.iv_supplier);
		iv_supplier.setOnClickListener(this);

		iv_frigoriferi = (ImageView) findViewById(R.id.iv_frigoriferi);
		iv_frigoriferi.setOnClickListener(this);

		iv_congelatori = (ImageView) findViewById(R.id.iv_congelatori);
		iv_congelatori.setOnClickListener(this);
		
		conformita = (ImageView) findViewById(R.id.conformita);
		conformita.setOnClickListener(this);

		iv_laboratorio = (ImageView) findViewById(R.id.iv_laboratorio);
		iv_laboratorio.setOnClickListener(this);

		findViewById(R.id.pulizia).setOnClickListener(this);
		findViewById(R.id.animali).setOnClickListener(this);
		findViewById(R.id.consumatori).setOnClickListener(this);
		findViewById(R.id.autorita).setOnClickListener(this);
		findViewById(R.id.ceck).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.iv_materie:
			mIntent = new Intent(Schedario.this, MaterieActivity.class);
			startActivity(mIntent);
			break;

		case R.id.iv_maintenance:
			mIntent = new Intent(Schedario.this, MaintenanceActivity.class);
			startActivity(mIntent);
			break;

		case R.id.iv_supplier:
			mIntent = new Intent(Schedario.this, SupplierListActivity.class);
			startActivity(mIntent);
			break;

		case R.id.iv_frigoriferi:
			mIntent = new Intent(Schedario.this, RefregeratorListActivity.class);
			startActivity(mIntent);
			break;

		case R.id.iv_congelatori:
			mIntent = new Intent(Schedario.this, FreezeActivity.class);
			startActivity(mIntent);
			break;

		case R.id.iv_laboratorio:
			mIntent = new Intent(Schedario.this, LaboratoryActivity.class);
			startActivity(mIntent);
			break;

		case R.id.pulizia:
			mIntent = new Intent(Schedario.this, PulicyActivity.class);
			startActivity(mIntent);
			break;
		case R.id.animali:

			mIntent = new Intent(Schedario.this, AnimalActivity.class);
			startActivity(mIntent);
			break;
			
		case R.id.consumatori:
			mIntent = new Intent(Schedario.this, UserdataInfoActivity.class);
			startActivity(mIntent);
			break;
			
		case R.id.conformita:
			mIntent = new Intent(Schedario.this, MeasureActivity.class);
			startActivity(mIntent);
			break;
			
		case R.id.autorita:
			
			mIntent = new Intent(Schedario.this,AuthorityActivity.class);
			startActivity(mIntent);
			break;
		case R.id.ceck:
			
			mIntent = new Intent(Schedario.this,CompilatorActivity.class);
			startActivity(mIntent);
			break;
		}
	}
}
