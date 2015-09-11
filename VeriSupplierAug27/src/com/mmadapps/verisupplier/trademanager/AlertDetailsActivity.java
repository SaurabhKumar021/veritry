package com.mmadapps.verisupplier.trademanager;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.os.Build;

public class AlertDetailsActivity extends BaseActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert_details);
		//BaseActionBarActivity.vBackView.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Alert Message");
       // actionBarActivity.vUserTitel.setTextColor(Color.parseColor("#4F4F4F"));
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);

		BaseActionBarActivity.vBackView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
	

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
