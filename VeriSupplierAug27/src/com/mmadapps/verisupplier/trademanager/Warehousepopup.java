package com.mmadapps.verisupplier.trademanager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.mmadapps.verisupplier.R;

public class Warehousepopup extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warehousepopup);


	}
	
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
