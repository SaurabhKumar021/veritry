package com.mmadapps.verisupplier.me;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.QuoatationDashboard;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.beans.WishlistDetails;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.dashboard.DashboardActivity;
import com.mmadapps.verisupplier.leftmenu.Myopenquoate;
import com.mmadapps.verisupplier.leftmenu.WishListActivity;
import com.mmadapps.verisupplier.login.SigninPageActivity;

public class UserMeActivity extends Activity implements OnClickListener{
	
	
	//bottom layout for homepage,wishlist,dashboard,openquote,userprofile
	LinearLayout home_layout,wishlist_layout,trademanager_layout,openquote_layout,userprofile_layout;
		
	//bottombar imageview
	public static ImageView home_selected,home_notselected,wishlist_selected,wishlist_notselected,trademanager_selected,trademanager_notselected,openquote_selected,openquote_notselected,userprofile_selected,userprofile_notselected;
		
	//from database
	UserDetails mUserDetails;
	List<CategoryDetails> mCategoryDetails=new ArrayList<CategoryDetails>();
	List<CountryDetails> mCounList=new ArrayList<CountryDetails>();
	List<CountryDetails> mStateList=new ArrayList<CountryDetails>();
	List<Lookups> mInspectionType=new ArrayList<Lookups>();
	List<Lookups> mIndustryList=new ArrayList<Lookups>();
	List<Lookups> mInspectorList=new ArrayList<Lookups>();
	ArrayList<QuoatationDashboard> quoatationDashboards;
	List<Lookups> mUnitOfMeasurentList=new ArrayList<Lookups>();
	List<Lookups> mQuotationLookupList=new ArrayList<Lookups>();
	
	//service call
	private ProgressDialog pdLoading=null;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	
	//time differeance 24 hours
	final double DIFFTIME=8.64e+7;
	
	//this activity layouts
	LinearLayout dashboard_layout,notification_layout,settings_layout,appfeedback_layout,onlinesupport_layout,aboutus_layout,logout_layout,switchuser_layout,profileimage_layout;
	public static TextView signin_text,useremail,username;
	
	//string variables
	String Inparam;
	String mType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_userme);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		getUserId();
		initializeView();
	}

	private void initializeView() {
		signin_text=(TextView)findViewById(R.id.vT_meuser_signinText);
		useremail=(TextView)findViewById(R.id.vT_meuser_useremail);
		username=(TextView)findViewById(R.id.vT_meuser_username);
		switchuser_layout=(LinearLayout)findViewById(R.id.vL_meuser_switchuser_layout);
		switchuser_layout.setOnClickListener(this);
		profileimage_layout=(LinearLayout)findViewById(R.id.vL_meuser_userprofile_layout);
		
		
		setLoginDetails();
		
		dashboard_layout=(LinearLayout)findViewById(R.id.vL_meuser_dashboard_layout);
		dashboard_layout.setOnClickListener(this);
		
		logout_layout=(LinearLayout)findViewById(R.id.vL_meuser_signin_layout);
		logout_layout.setOnClickListener(this);
		
		userprofile_layout=(LinearLayout)findViewById(R.id.vL_bottombar_profile);
		userprofile_selected=(ImageView)findViewById(R.id.vI_bottombar_userselected);
		userprofile_notselected=(ImageView)findViewById(R.id.vI_bottombar_user_notselected);
		userprofile_selected.setVisibility(View.VISIBLE);
		userprofile_notselected.setVisibility(View.GONE);
		
		home_layout=(LinearLayout)findViewById(R.id.vL_bottombar_home);
		home_selected=(ImageView)findViewById(R.id.vI_bottombar_homeselected);
		home_notselected=(ImageView)findViewById(R.id.vI_bottombar_home_notselected);
		home_selected.setVisibility(View.GONE);
		home_notselected.setVisibility(View.VISIBLE);
		home_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				home_selected.setVisibility(View.VISIBLE);
				home_notselected.setVisibility(View.GONE);
				userprofile_selected.setVisibility(View.GONE);
				userprofile_notselected.setVisibility(View.VISIBLE);
				/*Intent homeIntent=new Intent(UserMeActivity.this,MarketPageActivity.class);
				startActivity(homeIntent);
				overridePendingTransition(0, 0);*/
				finish();
			}
		});
		
		wishlist_layout=(LinearLayout)findViewById(R.id.vL_bottombar_wishlist);
		wishlist_selected=(ImageView)findViewById(R.id.vI_bottombar_wishselected);
		wishlist_notselected=(ImageView)findViewById(R.id.vI_bottombar_wish_notselected);
		wishlist_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getUserId();
				home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				wishlist_selected.setVisibility(View.VISIBLE);
				wishlist_notselected.setVisibility(View.GONE);
				
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(UserMeActivity.this,SigninPageActivity.class);
                    signinIntent.putExtra("FROMACTIVITY", "wishlist");
                    startActivity(signinIntent);
                    overridePendingTransition(0, 0);
                    finish();
                }else{
                    new AsyncGetWhishList().execute();
                }
				
				/*Intent homeIntent=new Intent(UserMeActivity.this,WishListActivity.class);
				startActivity(homeIntent);
				overridePendingTransition(0, 0);
				finish();*/
			}
		});
		
		trademanager_layout=(LinearLayout)findViewById(R.id.vL_bottombar_dashboard);
		trademanager_selected=(ImageView)findViewById(R.id.vI_bottombar_dashboardselected);
		trademanager_notselected=(ImageView)findViewById(R.id.vI_bottombar_dashboard_notselected);
		trademanager_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				trademanager_selected.setVisibility(View.VISIBLE);
				openquote_selected.setVisibility(View.GONE);
				openquote_notselected.setVisibility(View.VISIBLE);
				trademanager_notselected.setVisibility(View.GONE);
				wishlist_selected.setVisibility(View.GONE);
				wishlist_notselected.setVisibility(View.VISIBLE);
				
				getUserId();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(UserMeActivity.this,SigninPageActivity.class);
					signinIntent.putExtra("FROMACTIVITY", "tradedashboard");
					startActivity(signinIntent);
					overridePendingTransition(0, 0);
					finish();
				}else{
					
					Intent intent=new Intent(UserMeActivity.this,com.mmadapps.verisupplier.trademanager.DashboardActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
		
		openquote_layout=(LinearLayout)findViewById(R.id.vL_bottombar_openquote);
		openquote_selected=(ImageView)findViewById(R.id.vI_bottombar_openquoteselected);
		openquote_notselected=(ImageView)findViewById(R.id.vI_bottombar_openquote_notselected);
		openquote_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				openquote_selected.setVisibility(View.VISIBLE);
				openquote_notselected.setVisibility(View.GONE);
				
				getUserId();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(UserMeActivity.this,SigninPageActivity.class);
					signinIntent.putExtra("FROMACTIVITY", "marketopenquote");
					startActivity(signinIntent);
					overridePendingTransition(0, 0);
					finish();
				}else{
					new AsyncCategoryDetails().execute();
				}
			}
		});
		
		
		
		userprofile_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				
				
				Intent homeIntent=new Intent(MarketPageActivity.this,UserMeActivity.class);
				startActivity(homeIntent);
				overridePendingTransition(0, 0);*/
				
				/*userprofile_selected.setVisibility(View.GONE);
				userprofile_notselected.setVisibility(View.VISIBLE);
				
				//getUserDetails();
				location = new int[2];
				v.getLocationOnScreen(location);
				onWindowFocusChanged(true);
				ShowUserProfileBox(MarketPageActivity.mPoint);*/
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vL_meuser_dashboard_layout:
			mType="dashboard";
			getUserId();
			if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
				Intent signinIntent=new Intent(UserMeActivity.this,SigninPageActivity.class);
				signinIntent.putExtra("FROMACTIVITY", mType);
				startActivity(signinIntent);
				overridePendingTransition(0, 0);
				finish();
			}else{
				new AsyncDashboard().execute();
			}
			
			break;
			
		case R.id.vL_meuser_signin_layout:
			getUserId();
			if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
				Intent signinIntent=new Intent(UserMeActivity.this,SigninPageActivity.class);
				signinIntent.putExtra("FROMACTIVITY", "signin");
				startActivity(signinIntent);
			}else{
				showLogoutAlert();
			}
			break;
			
		case R.id.vL_meuser_switchuser_layout:
			mType="switchdashboard";
			getUserId();
			if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
				Intent signinIntent=new Intent(UserMeActivity.this,SigninPageActivity.class);
				signinIntent.putExtra("FROMACTIVITY", mType);
				startActivity(signinIntent);
				overridePendingTransition(0, 0);
				finish();
			}else{
				new AsyncDashboard().execute();
			}

		default:
			break;
		}
	}
	
	
	private void getUserId() {
		mUserDetails=new UserDetails();
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}
	
	private class AsyncCategoryDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdLoading = new ProgressDialog(UserMeActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			downloadAllCategories();
			getQuantityList();
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			Intent homeIntent=new Intent(UserMeActivity.this,Myopenquoate.class);
			startActivity(homeIntent);
			overridePendingTransition(0, 0);
			finish();
		}
		
	}

	private void downloadAllCategories() {
		Helper helper=new Helper(getApplicationContext());
		helper = new Helper(getApplicationContext());
		helper.openDataBase();
		mCategoryDetails = helper.getAllCategories("");
		if (mCategoryDetails == null || mCategoryDetails.size() == 0) {
			downloadCategories();
		} else if (checkdownloadTime(mCategoryDetails.get(0).getmLastDownloadDateTime())) {

		} else {
			downloadAllCategories();
		}
		helper.close();
	}

	private void downloadCategories() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		List<CategoryDetails> mCategoryDetails = new ArrayList<CategoryDetails>();
		String mInparam = "1";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetCategories, "GetCategories/", mInparam);
		if (result == null || result.length() == 0) {
		} else {
			mCategoryDetails = jsonParserClass.parseCategoryDetails(result,
					getApplicationContext());
			if (mCategoryDetails == null || mCategoryDetails.size() == 0) {
			} else {
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertCategoryDetails(mCategoryDetails);
				helper.close();
			}
		}
	}
	
	private boolean checkdownloadTime(String lastdownloadTime){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd hh:mm:ss",Locale.getDefault());
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		System.out.println(""+lastdownloadTime);
		
		Date d1 = null;
		Date d2 = null;
		try {
			d1=format.parse(dateTime);
			d2=format.parse(lastdownloadTime);
			System.out.println(""+d1);
			System.out.println(""+d2);
			long diff = d1.getTime() - d2.getTime();
			System.out.println(""+diff);
			if(DIFFTIME>diff){
				return true;
			}else{
				DeleteAllTables();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void DeleteAllTables() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		helper.deleteAllTables();
		helper.close();
	}
	
	private void getQuantityList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUnitOfMeasurentList=helper.getUnitOfMeasurentList();
		helper.close();
		
		if(mUnitOfMeasurentList == null || mUnitOfMeasurentList.size()==0){
			downloadUnitOfMeasurementList();
		}else if(checkdownloadTime(mUnitOfMeasurentList.get(0).getmLastDownloadTime())){
		}else{
			getQuantityList();
		}
	}
	
	private void downloadUnitOfMeasurementList() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		List<Lookups> mUnitOfMeasurement=new ArrayList<Lookups>();
		String mInparam="Units_Of_Measurement/Quotation";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", mInparam);
		if(result==null || result.length()==0){
		}else{
			mUnitOfMeasurement=jsonParserClass.pareseLookups(result);
			if(mUnitOfMeasurement==null || mUnitOfMeasurement.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertUnitOfMeasurementList(mUnitOfMeasurement);
				helper.close();
			}
		}
	}
	
	private void getQuotationLookups() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mQuotationLookupList=helper.getQuotationLookupList();
		helper.close();
		
		if(mQuotationLookupList == null || mQuotationLookupList.size()==0){
			downloadQuotationLookupList();
		}else if(checkdownloadTime(mQuotationLookupList.get(0).getmLastDownloadTime())){
		}else{
			getQuotationLookups();
		}
	}
	
	private void downloadQuotationLookupList() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		List<Lookups> mQuotationLookupList=new ArrayList<Lookups>();
		String mInparam="Type_of_Packaging/Quotation";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", mInparam);
		if(result==null || result.length()==0){
		}else{
			mQuotationLookupList=jsonParserClass.pareseLookups(result);
			if(mQuotationLookupList==null || mQuotationLookupList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertQuotationLookupList(mQuotationLookupList);
				helper.close();
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		VerisupplierUtils.mQuantityList=new ArrayList<Lookups>();
		getUserId();
		setLoginDetails();
		
	}

	
	
	private void setLoginDetails() {
		if(mUserDetails==null){
			profileimage_layout.setVisibility(View.GONE);
		}else{
			if(mUserDetails.getmUserEmail()==null || mUserDetails.getmUserEmail().equalsIgnoreCase("null")){
				useremail.setText("");
				username.setText("");
				profileimage_layout.setVisibility(View.GONE);
			}else{
				signin_text.setText("Log out");
				useremail.setText(""+mUserDetails.getmUserEmail());
				username.setText(""+mUserDetails.getmUserName());
				profileimage_layout.setVisibility(View.VISIBLE);
			}
			
			if(mUserDetails.getmUserType()==null || mUserDetails.getmUserType().equalsIgnoreCase("null")){
				switchuser_layout.setVisibility(View.GONE);
			}else{
				if(mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
					switchuser_layout.setVisibility(View.GONE);
				}else{
					switchuser_layout.setVisibility(View.VISIBLE);
				}
			}
		}
		
		
	}

	private void showLogoutAlert() {
		new AlertDialog.Builder(this)
		.setMessage("Are you sure do you want to logout")
		.setPositiveButton("Continue", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				signin_text.setText("Login");
				useremail.setText("");
				username.setText("");
				switchuser_layout.setVisibility(View.GONE);
				profileimage_layout.setVisibility(View.GONE);
				DeleteUserDetails();
				
			}

		}).setNegativeButton("Cancel", null)
		.show();
	}
	
	private void DeleteUserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		helper.deleteUserTable();
		helper.close();
	}
	
	public class AsyncDashboard extends AsyncTask<String, Void, Boolean> {

		Boolean IsQuotation = false;

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(UserMeActivity.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			if(mUserDetails.getmUserType()==null || mUserDetails.getmUserType().length()==0){
			}else{
				if(mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
					getQuantityList();
					getQuotationLookups();
				}
				else if(mUserDetails.getmUserType().equalsIgnoreCase("Manufacturer") || mUserDetails.getmUserType().equalsIgnoreCase("Product")){
					getQuantityList();
					getQuotationLookups();
				}
				else if(mUserDetails.getmUserType().equalsIgnoreCase("Customs")){
					downloadAllCategories();
					getQuantityList();
					downloadCountries();
					downloadStates();
				}
				else if(mUserDetails.getmUserType().equalsIgnoreCase("Shipment")){
					downloadCountries();
					downloadStates();
				}
				else if(mUserDetails.getmUserType().equalsIgnoreCase("Inspection")){
					downloadCountries();
					downloadStates();
					downloadTypeOfIndustry();
					downloadInspectorType();
					downloadInspectionTypes();
				}
				else if(mUserDetails.getmUserType().equalsIgnoreCase("Warehouse")){
					
				}
			}
			return IsQuotation = getAllQuotation();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pdLoading.isShowing();
			pdLoading.cancel();
			if (result) {
				Intent dashboardIntent = new Intent(UserMeActivity.this, DashboardActivity.class);
				dashboardIntent.putExtra("Type", mType);
				startActivity(dashboardIntent);
				overridePendingTransition(0, 0);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "QuotationList is null",Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
	
	private void downloadCountries() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mCounList=helper.getAllCountries();
		helper.close();
		
		if(mCounList==null || mCounList.size()==0){
			downloadAllCountries();
		}else if(checkdownloadTime(mCounList.get(0).getmLastDownloadTime())){
			
		}else{
			downloadCountries();
		}
		
	}

	private void downloadAllCountries() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String outputResult=webServices.CallWebHTTPBindingService(ApiType.GetCountries, "GetCountries", "");
		if(outputResult==null || outputResult.length()==0){
		}else{
			VerisupplierUtils.mCountriesList=jsonParserClass.parseCountries(outputResult,getApplicationContext());
			if(VerisupplierUtils.mCountriesList==null || VerisupplierUtils.mCountriesList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertCountries(VerisupplierUtils.mCountriesList);
				helper.close();
			}
		}
	}
	

	private void downloadStates() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mStateList=helper.getAllStates();
		helper.close();
		
		if(mStateList==null || mStateList.size()==0){
			downloadAllStates();
		}else if(checkdownloadTime(mStateList.get(0).getmLastDownloadTime())){
			
		}else{
			downloadStates();
		}
	}
	
	private void downloadAllStates() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String outputResult=webServices.CallWebHTTPBindingService(ApiType.GetCountries, "GetStates/", "0");
		if(outputResult==null || outputResult.length()==0){
		}else{
			VerisupplierUtils.mStatesList=jsonParserClass.parseStates(outputResult,getApplicationContext());
			if(VerisupplierUtils.mStatesList==null || VerisupplierUtils.mStatesList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertStates(VerisupplierUtils.mStatesList);
				helper.close();
			}
		}
	}

	private Boolean getAllQuotation() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		Boolean isValid = false;
		if (isValid.equals(false)) {
			if(mType.equalsIgnoreCase("dashboard")){
				if(mUserDetails.getmUserType() == null ){
				}else{
					if( mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
					}
					else if(mUserDetails.getmUserType().equalsIgnoreCase("Manufacturer") || mUserDetails.getmUserType().equalsIgnoreCase("Product")){
						String mManufacturerId=mUserDetails.getmManufacturerId();
						Inparam = "0/"+mManufacturerId+"/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
					}
					else if(mUserDetails.getmUserType().equalsIgnoreCase("Customs")){
						String mManufacturerId=mUserDetails.getmManufacturerId();
						Inparam = "0/"+mManufacturerId+"/"+VerisupplierUtils.mCustomsParam+"/1/20";
					}
					else if(mUserDetails.getmUserType().equalsIgnoreCase("Shipment")){
						String mManufacturerId=mUserDetails.getmManufacturerId();
						Inparam = "0/"+mManufacturerId+"/"+VerisupplierUtils.mShippingParam+"/1/20";
					}
					else if(mUserDetails.getmUserType().equalsIgnoreCase("Inspection")){
						String mManufacturerId=mUserDetails.getmManufacturerId();
						Inparam = "0/"+mManufacturerId+"/"+VerisupplierUtils.mInspectionParam+"/1/20";
					}
					else if(mUserDetails.getmUserType().equalsIgnoreCase("Warehouse")){
						String mManufacturerId=mUserDetails.getmManufacturerId();
						Inparam = "0/"+mManufacturerId+"/"+VerisupplierUtils.mWarehouseParam+"/1/20";
					}
				}
			}else{
				Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
			}
			String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetQuotation, "GetQuotation/", Inparam);
			if (resultOutparam == null || resultOutparam.length() == 0) {
			} else {
				quoatationDashboards = jsonParserClass.ParseDashboardQuaotation(resultOutparam);
				Helper helper = new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertDashboardQuotation(quoatationDashboards);
				helper.close();
			}
			isValid=true;
		} else {
			isValid = false;
		}
		return isValid;
	}
	
	private void downloadTypeOfIndustry() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mIndustryList=helper.getTypeOfIndustry();
		helper.close();
		if(mIndustryList == null || mIndustryList.size()==0){
			downloadIndustryList();
		}else if(checkdownloadTime(mIndustryList.get(0).getmLastDownloadTime())){
		}else{
			downloadTypeOfIndustry();
		}
	}
	
	private void downloadIndustryList() {
		List<Lookups> mTypeOfIndustryList=new ArrayList<Lookups>();
		String mInparam="Type_Of_Industry/Inspection";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", mInparam);
		if(result==null || result.length()==0){
		}else{
			mTypeOfIndustryList=jsonParserClass.pareseLookups(result);
			if(mTypeOfIndustryList==null || mTypeOfIndustryList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertTypeOfIndustrys(mTypeOfIndustryList);
				helper.close();
			}
		}
	}
	
	private void downloadInspectorType() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mInspectorList=helper.getTypeOfInspector();
		helper.close();
		if(mIndustryList == null || mIndustryList.size()==0){
			downloadInspectorList();
		}else if(checkdownloadTime(mIndustryList.get(0).getmLastDownloadTime())){
		}else{
			downloadInspectorType();
		}
	}

	private void downloadInspectorList() {
		List<Lookups> mInspectorList=new ArrayList<Lookups>();
		String mInparam="Inspector_Type/Inspection";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", mInparam);
		if(result==null || result.length()==0){
		}else{
			mInspectorList=jsonParserClass.pareseLookups(result);
			if(mInspectorList==null || mInspectorList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertTypeOfInspector(mInspectorList);
				helper.close();
			}
		}
	}
	
	private void downloadInspectionTypes() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mInspectionType=helper.getInspectionType();
		helper.close();
		if(mInspectionType == null || mInspectionType.size()==0){
			downloadInspectionList();
		}else if(checkdownloadTime(mInspectionType.get(0).getmLastDownloadTime())){
			
		}else{
			downloadInspectionTypes();
		}
	}
	
	private void downloadInspectionList() {
		List<Lookups> mInspectionDetails=new ArrayList<Lookups>();
		String mInparam="Inspection_Type/Inspection";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", mInparam);
		if(result==null || result.length()==0){
		}else{
			mInspectionDetails=jsonParserClass.pareseLookups(result);
			if(mInspectionDetails==null || mInspectionDetails.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertInspectionList(mInspectionDetails);
				helper.close();
			}
		}
	}
	

private class AsyncGetWhishList extends AsyncTask<Void, Void, Boolean>{
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = new ProgressDialog(UserMeActivity.this);
            pdLoading.setMessage("Please wait...");
            pdLoading.show();
            pdLoading.setCancelable(false);
            pdLoading.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return getWhishList();
        }
        
        @Override
        protected void onPostExecute(Boolean result) {
        	super.onPostExecute(result);
        	if (pdLoading != null && pdLoading.isShowing() && result) {
                pdLoading.cancel();
                Intent homeIntent=new Intent(UserMeActivity.this,WishListActivity.class);
                startActivity(homeIntent);
                overridePendingTransition(0, 0);
        	}else{
                pdLoading.cancel();
                Toast.makeText(getApplicationContext(), "No WishList", Toast.LENGTH_LONG).show();
            }
        }
	}
	
	 private Boolean getWhishList() {
		 webServices=new WebServices();
		 jsonParserClass=new JsonParserClass();
		   Boolean isValid=false;
		   List<WishlistDetails> mWishDetails=new ArrayList<WishlistDetails>();
		   String inParam = ""+mUserDetails.getmUserID()+"/"+"1"+"/"+"20";
		   String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetWishList, "GetWishList/", inParam);
		   if (resultOutparam == null) {
			   isValid=false;
		   }else {
			   mWishDetails=jsonParserClass.parseWishListDetails(resultOutparam,getApplicationContext());
			   if(mWishDetails==null || mWishDetails.size()==0){
			   }else{
				   isValid=true;
				   Helper helper = new Helper(getApplicationContext());
				   helper.openDataBase();
				   helper.insertwhistlistdetails(mWishDetails);
				   helper.close();
			   }
		   }
		   return isValid;
	   }
	
}
