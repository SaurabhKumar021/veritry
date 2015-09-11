package com.mmadapps.verisupplier.trademanager;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mmadapps.verisupplier.R;

public class Fulfilpopup extends Activity implements OnClickListener{
	LinearLayout mainlayout,vL_ff_update;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		setContentView(R.layout.activity_fulfilpopup);

		vL_ff_update=(LinearLayout) findViewById(R.id.vL_ff_update);
		vL_ff_update.setOnClickListener(this);
		
		mainlayout=(LinearLayout) findViewById(R.id.vL_ff_mainlayout);
		android.view.ViewGroup.LayoutParams layoutParams = mainlayout.getLayoutParams();
		layoutParams.width = getYOffSetbyDensityforCalwid();
		layoutParams.height = getYOffSetbyDensityforCal();
		mainlayout.setLayoutParams(layoutParams);
	
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.vL_ff_update:
			finish();
			break;

		default:
			break;
		}
		
	}
	
private int getYOffSetbyDensityforCalwid() {
	int density = getResources().getDisplayMetrics().densityDpi;
	switch (density) {
	case DisplayMetrics.DENSITY_HIGH:
		return 470;
	case DisplayMetrics.DENSITY_XHIGH:
		return 600;
	case DisplayMetrics.DENSITY_XXHIGH:
		return 700;
	default:
		return 470;
	}
	}
private int getYOffSetbyDensityforCal() {
		
		int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_HIGH:
			return 350;
		case DisplayMetrics.DENSITY_XHIGH:
			return 400;
		case DisplayMetrics.DENSITY_XXHIGH:
			return 500;
		default:
			return 350;
		}
	}

public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	finish();
}
}
