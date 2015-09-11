package com.mmadapps.verisupplier.login;

import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.UserRegistrationPopupNew;
import com.mmadapps.verisupplier.UserSignPopup;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;

public class SigninPageActivity extends Activity implements OnClickListener {
	TextView LoginText,sigupText;
	
	
	//intent
	 String mFromActivity;
	 String mManufacturerId;
	 String mProductID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_page);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().hide();
        initializeValues();
        
      //from Intent
      	mFromActivity=getIntent().getStringExtra("FROMACTIVITY");
      	mManufacturerId=getIntent().getStringExtra("MANUFACTURERID");
      	mProductID=getIntent().getStringExtra("PRODUCTID");
        
    }
	
    
    private void initializeValues() {
    	LoginText=(TextView) findViewById(R.id.vT_sp_login);
		sigupText=(TextView) findViewById(R.id.vT_sp_signup);
		LoginText.setOnClickListener(this);
		sigupText.setOnClickListener(this);
	}
	
    
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vT_sp_login:
			Intent signinIntent=new Intent(SigninPageActivity.this,LoginEmailActivity.class);
			signinIntent.putExtra("CLICKED", "login");
			signinIntent.putExtra("FROMACTIVITY", mFromActivity);
			signinIntent.putExtra("MANUFACTURERID", mManufacturerId);
			signinIntent.putExtra("PRODUCTID", mProductID);
			startActivity(signinIntent);
			overridePendingTransition(0, 0);
			finish();
			break;
			
	    case R.id.vT_sp_signup:
	    	//go to signup activity
			Intent signupIntent=new Intent(SigninPageActivity.this,LoginEmailActivity.class);
			signupIntent.putExtra("CLICKED", "register");
			signupIntent.putExtra("FROMACTIVITY", mFromActivity);
			signupIntent.putExtra("MANUFACTURERID", mManufacturerId);
			signupIntent.putExtra("PRODUCTID", mProductID);
			startActivity(signupIntent);
			overridePendingTransition(0, 0);
			finish();
		default:
			break;
		}
	
	}

}
