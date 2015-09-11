package com.mmadapps.verisupplier;

import java.io.IOException;

import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.payment.PaymentGateway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class SplashScreenActivity extends Activity{
	
	//splashscreen timeout
	private static int SPLASH_TIME_OUT = 1500;
	
	//handler for splashscreen
	Handler mHandlerTime;
	Runnable mRunnableTimeOut;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		LinearLayout linearLayout =new LinearLayout(this);
		linearLayout.setBackgroundResource(R.raw.splash);
		getActionBar().hide();
		setContentView(linearLayout);
		
		//create the database
		//check1
		createDatabase();
		
		//splashscreen time
		mHandlerTime = new Handler();
		mRunnableTimeOut = new Runnable() {
			@Override
			public void run() {
				Intent iRedirectScreen = new Intent(SplashScreenActivity.this,MarketPageActivity.class);
				iRedirectScreen.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(iRedirectScreen);
				overridePendingTransition(0, 0);
				finish();
			}
		};
	}
	
	private void createDatabase() {
		try {
			Helper helper = new Helper(getApplicationContext());
			helper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		mHandlerTime.removeCallbacks(mRunnableTimeOut);
		super.onPause();
	}

	@Override
	protected void onResume() {
		mHandlerTime.postDelayed(mRunnableTimeOut, SPLASH_TIME_OUT);
		super.onResume();
	}

}
