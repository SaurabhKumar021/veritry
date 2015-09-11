package com.mmadapps.verisupplier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.ProductList;
import com.mmadapps.verisupplier.beans.QuickOrderResult;
import com.mmadapps.verisupplier.beans.QuoatationDashboard;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.customs.Encrypt;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.customservices.CustomCreateQuotation;
import com.mmadapps.verisupplier.dashboard.DashboardActivity;
import com.mmadapps.verisupplier.details.GetQuotationActivityNew;
import com.mmadapps.verisupplier.details.MoreInfoActivity;
import com.mmadapps.verisupplier.details.MoreInfoActivityNew;
import com.mmadapps.verisupplier.inspection.InspectionCreateQuotation;
import com.mmadapps.verisupplier.leftmenu.Myopenquoate;
import com.mmadapps.verisupplier.placeorder.PlaceorderShipping;
import com.mmadapps.verisupplier.products.ProductCreateQuotationActivity;
import com.mmadapps.verisupplier.shipping.ShippingCreateQuotation;
import com.mmadapps.verisupplier.warehouse.WarehouseCreateQuotation;

public class UserSignPopup extends Activity implements OnClickListener{
	
	//signin and signup layouts
	LinearLayout main_layout,signin_layout,signup_layout;
	
	//dialog close
	ImageView close;
	
	//textview for signin and signup
	TextView signin_text,signup_text;
	
	//edittext email and password
	EditText email,password;
	
	//service call
	ProgressDialog pdLoading=null;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	
	//encription key 
	private String KEY="1429111390460322";
	static String IV="90460322";
	
	//intent
	String mFromActivity;
	String mManufacturerId;
	String mProductID="";
	
	//String Variables
	String mUserEmail;
	String mUserPassword;
	
	//from database 
	UserDetails mUserDetail=new UserDetails();
	List<CategoryDetails> mCategoryDetails=new ArrayList<CategoryDetails>();
	ArrayList<QuoatationDashboard> quoatationDashboards;
	QuickOrderResult mQuotationDetails=new QuickOrderResult();
	
	//service call after 24 hours time differance
	final double DIFFTIME=8.64e+7;
	
	List<CountryDetails> mCounList=new ArrayList<CountryDetails>();
	List<CountryDetails> mStateList=new ArrayList<CountryDetails>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.user_signinpopup);
		
		//from Intent
		mFromActivity=getIntent().getStringExtra("FROMACTIVITY");
		mManufacturerId=getIntent().getStringExtra("MANUFACTURERID");
		mProductID=getIntent().getStringExtra("PRODUCTID");
		
		//dialog layout width and height parameters
		main_layout = (LinearLayout) findViewById(R.id.vL_main_layout);
		android.view.ViewGroup.LayoutParams layoutParams = main_layout.getLayoutParams();
		layoutParams.width = LayoutParams.WRAP_CONTENT;
		layoutParams.height = getYOffSetbyDensityforCal();
		main_layout.setLayoutParams(layoutParams);
		
		initializeView();
	}
	
	private int getYOffSetbyDensityforCal() {
		int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_HIGH:
			return 800;
		case DisplayMetrics.DENSITY_XHIGH:
			return 900;
		case DisplayMetrics.DENSITY_XXHIGH:
			return 950;
		default:
			return 800;
		}
	}

	private void initializeView() {
		close=(ImageView)findViewById(R.id.vI_usipu_closeImage);
		close.setOnClickListener(this);
		
		signin_layout=(LinearLayout)findViewById(R.id.vL_usipu_signin_layout);
		signin_text=(TextView)findViewById(R.id.vT_usipu_signin_text);
		signin_layout.setOnClickListener(this);
		signin_text.setOnClickListener(this);
		
		signup_layout=(LinearLayout)findViewById(R.id.vL_usipu_signup_layout);
		signup_text=(TextView)findViewById(R.id.vT_usipu_signup_text);
		signup_layout.setOnClickListener(this);
		signup_text.setOnClickListener(this);
		
		email=(EditText)findViewById(R.id.vE_usipu_emailId);
		password=(EditText)findViewById(R.id.vE_usipu_password);
		
		setValues();
	}

	private void setValues() {
		email.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mUserEmail=email.getText().toString().trim();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mUserPassword=password.getText().toString().trim();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//popup close
		case R.id.vI_usipu_closeImage:
			finish();
			break;
		
		//popup sigin click
		case R.id.vL_usipu_signin_layout:
		case R.id.vT_usipu_signin_text:
			//check whether useremail and password is null
			if(mUserEmail==null || mUserEmail.length()==0 || mUserPassword==null || mUserPassword.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the email and password", Toast.LENGTH_SHORT).show();
			}else if(mUserPassword==null || mUserPassword.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the password", Toast.LENGTH_SHORT).show();
			}
			else{
				try{
					//encrypt user password
					String target=""+mUserPassword;
					mUserPassword = new Encrypt().encryptText(target,KEY,IV);
					System.out.println("result--"+mUserPassword);
					
					//check network connection
					ConnectionDetector mDetector=new ConnectionDetector(getApplicationContext());
					if(mDetector.isConnectingToInternet()){
						//to validate user
						new AsyncUserLogin().execute();
					}else{
						Toast.makeText(getApplicationContext(), "Please check internet", Toast.LENGTH_SHORT).show();
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
			
		//popup signup click
		case R.id.vL_usipu_signup_layout:
		case R.id.vT_usipu_signup_text:
			//go to signup activity
			Intent signupIntent=new Intent(UserSignPopup.this,UserRegistrationPopupNew.class);
			signupIntent.putExtra("FROMACTIVITY", mFromActivity);
			signupIntent.putExtra("MANUFACTURERID", mManufacturerId);
			signupIntent.putExtra("PRODUCTID", mProductID);
			startActivity(signupIntent);
			overridePendingTransition(0, 0);
			finish();
			break;
		default:
			break;
		}
	}
	
	private class AsyncUserLogin extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(UserSignPopup.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return isValidUser();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			if(result){
				//if user validation is correct collect the user details
				new AsyncUserDetails().execute();
			}else{
				password.setText("");
				Toast.makeText(getApplicationContext(), "Invalid credential", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private Boolean isValidUser() {
		Boolean isValid=false;
		//initialize the webservice and jsonparser calss
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		
		//encode the email and encrypted password
		String emailEncode=Uri.encode(""+mUserEmail);
		String passwordEncode=Uri.encode(mUserPassword);
		
		String Inparam="?email="+emailEncode+"&secret="+passwordEncode;
		String resultOutParam=webServices.CallWebHTTPBindingService(ApiType.ValidateUser, "ValidateUser", Inparam);
		if(resultOutParam==null || resultOutParam.length()==0){
			isValid=false;
		}else{
			//parse the output
			String[] result=jsonParserClass.parserAuthentication(resultOutParam,getApplicationContext());
			if(result.length==0 || result[1].equalsIgnoreCase("true")){
				isValid=true;
			}else{
				isValid=false;
			}
		}
		return isValid;
	}

	
	private class AsyncUserDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(UserSignPopup.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			//get the userdetails
			boolean isvalidUser = isValidUserDetails();
			if(isvalidUser){
				//from  product detail page getprice onclick
				if(mFromActivity.equalsIgnoreCase("getprice")){
					getQuantityList();
					return getManufacturerDetaisl();
				}
				//from product detail page getquotation onclick
				else if(mFromActivity.equalsIgnoreCase("getquotation")){
					getQuantityList();
					getManufacturerDetaisl();
					return getQuotationLookups();
				}
				//from product detail page moreinfo onclick
				else if(mFromActivity.equalsIgnoreCase("moreinfo")){
					getManufacturerDetaisl();
					//getQuotationLookups();
					getSubjectDetails();
					return getMoreInfoLookups();
				}
				//from product detail page placeorder onclick
				else if(mFromActivity.equalsIgnoreCase("overviewplaceorder")){
					return isValidUser();
				}
				//from marketplace page openquote onclick
				else if(mFromActivity.equalsIgnoreCase("marketopenquote")){
					getQuantityList();
					LastDownloadTime();
				}
				//from marketplace page dashboard onclick
				else if(mFromActivity.equalsIgnoreCase("marketdashboard")){
					return true;
				}
				else if (mFromActivity.equalsIgnoreCase("tradedashboard")) {
					return true;
					/*getQuantityList();
					LastDownloadTime();
					
					Intent intent=new Intent(UserSignPopup.this,com.mmadapps.verisupplier.trademanager.DashboardActivity.class);
					startActivity(intent);*/
					
				}
				//from marketplace page usermenu signin onclick
				else if(mFromActivity.equalsIgnoreCase("signin")){
					return true;
				}
				// from product detail page placeorder onclick
				else if(mFromActivity.equalsIgnoreCase("placeorder")){
					getQuotationDetails();
					return getManufacturerDetaisl();
				}
				//from product detail page moreinfo onclick
				else{
					getQuantityList();
					getManufacturerDetaisl();
					return getMoreInfoLookups();
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			if(result){
				//to go getprice activity
				if(mFromActivity.equalsIgnoreCase("getprice")){
					Intent getPriceIntent=new Intent(UserSignPopup.this,GetQuotationActivityNew.class);
					getPriceIntent.putExtra("FROMACTIVITY", "getprice");
					startActivity(getPriceIntent);
					overridePendingTransition(0, 0);
					finish();
				}
				//to go getquotation activity
				else if(mFromActivity.equalsIgnoreCase("getquotation")){
					forwardTogetQuotationActivity();
					/*Intent getquotationIntent=new Intent(UserSignPopup.this,GetQuotationActivityNew.class);
					getquotationIntent.putExtra("FROMACTIVITY", "getquotation");
					startActivity(getquotationIntent);
					overridePendingTransition(0, 0);
					finish();*/
				}
				//to go moreinfo activity
				else if(mFromActivity.equalsIgnoreCase("moreinfo")){
					forwardToMoreInfoActivity();
				}
				
				//to go placeorder activity
				else if(mFromActivity.equalsIgnoreCase("overviewplaceorder")){
					Intent placeorderIntent=new Intent(UserSignPopup.this,PlaceorderShipping.class);
					startActivity(placeorderIntent);
					overridePendingTransition(0, 0);
					finish();
				}
				//to go openquote activity
				else if(mFromActivity.equalsIgnoreCase("marketopenquote")){
					checkLastDownloadTime();
				}
				//to go dashboard activity
				else if(mFromActivity.equalsIgnoreCase("marketdashboard")){
					UserDetails();
					new AsyncDashboard().execute();
				}
				else if (mFromActivity.equalsIgnoreCase("tradedashboard")) {
					getQuantityList();
					LastDownloadTime();
					
					Intent intent=new Intent(UserSignPopup.this,com.mmadapps.verisupplier.trademanager.DashboardActivity.class);
					startActivity(intent);
					
				}
				//to get signin from marketplace usermenu signin
				else if(mFromActivity.equalsIgnoreCase("signin")){
					//UserMeActivity.signin_text.setText("Log out");
					finish();
				}
				//to go to placeorder activity
				else if(mFromActivity.equalsIgnoreCase("placeorder")){
					PlaceorderShipping.mFromActivity="PD";
					PlaceorderShipping.mProductId=mProductID;
					PlaceorderShipping.mManufacturerId=mManufacturerId;
					Intent placeOrderIntent = new Intent(UserSignPopup.this, PlaceorderShipping.class);
					startActivity(placeOrderIntent);
					overridePendingTransition(0, 0);
					finish();
				}
				//to go moreinfo activity
				else {
					Intent placeorderIntent=new Intent(UserSignPopup.this,MoreInfoActivity.class);
					startActivity(placeorderIntent);
					overridePendingTransition(0, 0);
					finish();
				}
			}else{
				//not confirmrd
				if(mFromActivity.equalsIgnoreCase("marketopenquote")){
					CallServices();
				}else if(mFromActivity.equalsIgnoreCase("marketdashboard")){
					UserDetails();
					new AsyncDashboard().execute();
				}else if(mFromActivity.equalsIgnoreCase("signin")){
					finish();
				}
				//to show error from service side
				else{
					Toast.makeText(UserSignPopup.this, "error in signup", Toast.LENGTH_SHORT).show();
				}
			}
		}

	}
	
	private void forwardTogetQuotationActivity() {
		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			Intent getQuotationIntent = new Intent(UserSignPopup.this, ProductCreateQuotationActivity.class);
			getQuotationIntent.putExtra("FROMACTIVITY", "getquotation");
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
			Intent getQuotationIntent = new Intent(UserSignPopup.this, InspectionCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}
		else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
			Intent getQuotationIntent = new Intent(UserSignPopup.this, ShippingCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
			Intent getQuotationIntent = new Intent(UserSignPopup.this, CustomCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
			Intent getQuotationIntent = new Intent(UserSignPopup.this, WarehouseCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}
	}
	
	
	private void forwardToMoreInfoActivity() {
		Intent moreInfoIntent = new Intent(UserSignPopup.this, MoreInfoActivityNew.class);
		startActivity(moreInfoIntent);
		overridePendingTransition(0, 0);
		finish();
	}
	
	private void getQuotationDetails() {
		UserDetails();
		webServices =new WebServices();
		jsonParserClass=new JsonParserClass();
		String Inparam=mUserDetail.getmUserID()+"/"+mManufacturerId+"/"+mProductID;
		String outputParm=webServices.CallWebHTTPBindingService(ApiType.GetQuoteDetailsForQuickOrder, "GetQuoteDetailsForQuickOrder/", Inparam);
		Log.e("GetQuoteDetailsForQuickOrder", outputParm);
		if(outputParm==null || outputParm.length()==0){
		}else{
			VerisupplierUtils.mQuotationDetails=jsonParserClass.parseQuickOrderResult(outputParm,"GetQuoteDetailsForQuickOrderResult");
		}
	}
	
	private Boolean getQuantityList() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		
		String Inparam = "Units_Of_Measurement/Quotation";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", Inparam);
		if(result==null || result.length()==0){
			isValid=false;
		}else{
			List<Lookups> mLookups=new ArrayList<Lookups>();
			mLookups= jsonParserClass.pareseLookups(result);
			if(mLookups==null || mLookups.size()==0){
				isValid=false;
			}else{
				VerisupplierUtils.mQuantityList.addAll(mLookups);
				isValid=true;
			}
		}
		return isValid;
	}
	
	private void UserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetail=helper.getUserDetails();
		helper.close();
	}

	private Boolean LastDownloadTime() {
		Boolean isValid=false;
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mCategoryDetails=helper.getAllCategories("");
		helper.close();
		if(mCategoryDetails.size()==0|| mCategoryDetails==null){
			isValid=false;
		}else{
			isValid=true;
		}
		return isValid;
		
	}
	
	private void checkLastDownloadTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		
		String mDate=mCategoryDetails.get(0).getmLastDownloadDateTime();
		System.out.println(""+mDate);
		
		Date d1 = null;
		Date d2 = null;
		try {
			d1=format.parse(dateTime);
			d2=format.parse(mDate);
			System.out.println(""+d1);
			System.out.println(""+d2);
			long diff = d1.getTime() - d2.getTime();
			System.out.println(""+diff);
			if(DIFFTIME>diff){
				System.out.println("if");
				Intent homeIntent=new Intent(UserSignPopup.this,Myopenquoate.class);
				startActivity(homeIntent);
				overridePendingTransition(0, 0);
				finish();
			}else{
				System.out.println("else");
				DeleteAllTables();
				CallServices();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void DeleteAllTables() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		helper.deleteAllTables();
		helper.close();
	}

	private Boolean isValidUserDetails() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		UserDetails mUserDetails=new UserDetails();
		String Inparam=mUserEmail;
		String resultOutput=webServices.CallWebHTTPBindingService(ApiType.UserDetails, "UserDetails/", Inparam);
		if(resultOutput==null || resultOutput.length()==0){
			isValid=false;
		}else{
			mUserDetails=jsonParserClass.parseUserDetails(resultOutput,getApplicationContext());
			Helper helper=new Helper(getApplicationContext());
			helper.openDataBase();
			helper.insertUserDetails(mUserDetails,true);
			helper.close();
			isValid=true;
		}
		
		return isValid;
	}
	
	// Baskar on 2nd Jul
	private Boolean getManufacturerDetaisl() {
		boolean isValid=false;
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		List<ManufacturerDetails> mManufacturerDetails=helper.getManufacturerDetails(mManufacturerId);
		helper.close();
		
		if(mManufacturerDetails==null || mManufacturerDetails.size()==0){
			webServices =new WebServices();
			jsonParserClass=new JsonParserClass();
			String Inparam=mManufacturerId;
			String result=webServices.CallWebHTTPBindingService(ApiType.GetManufacturerDetail, "GetManufacturerDetail/", Inparam);
			if(result==null || result.length()==0){
			}else{
				mManufacturerDetails=jsonParserClass.parseManufacturerDetails(result);
				if(mManufacturerDetails==null || mManufacturerDetails.size()==0){
				}else{
					helper.openDataBase();
					helper.insertManufacturerDetails(mManufacturerDetails, mManufacturerId);
					helper.insertManufacturerProducts(mManufacturerDetails, mManufacturerId);
					helper.insertManufacturerAttributes(mManufacturerDetails,mManufacturerId);
					helper.close();
					isValid = true;
				}
			}
		}else{
			isValid =true;
		}
		return isValid;
	}

	private Boolean getMoreInfoLookups() {
		Boolean isValid=false;
		String inparam = "Types_of_Additional_Info/Additional_Info";
		VerisupplierUtils.mMoreInfoLookups = new ArrayList<Lookups>();
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", inparam);
		if(result==null || result.length()==0){
			isValid=false;
		}else{
			List<Lookups> mLookups = jsonParserClass.pareseLookups(result);
			if(mLookups==null || mLookups.size()==0){
				isValid=false;
			}else{
				VerisupplierUtils.mMoreInfoLookups.addAll(mLookups);
				isValid=true;
			}
		}
		return isValid;
	}
	
	private Boolean getSubjectDetails() {
		Boolean isValid=false;
		String inparam = "Types_of_Additional_Info/Additional_Info";
		VerisupplierUtils.mSubjectLookups = new ArrayList<Lookups>();
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", inparam);
		if(result==null || result.length()==0){
			isValid=false;
		}else{
			List<Lookups> mLookups = jsonParserClass.pareseLookups(result);
			if(mLookups==null || mLookups.size()==0){
				isValid=false;
			}else{
				VerisupplierUtils.mSubjectLookups.addAll(mLookups);
				isValid=true;
			}
		}
		return isValid;
	}

	private Boolean getQuotationLookups() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String Inparam = "Type_of_Packaging/Quotation";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", Inparam);
		if(result==null || result.length()==0){
			isValid=false;
		}else{
			List<Lookups> mLookups=new ArrayList<Lookups>();
			mLookups= jsonParserClass.pareseLookups(result);
			if(mLookups==null || mLookups.size()==0){
				isValid=false;
			}else{
				VerisupplierUtils.mQuotationLookups.addAll(mLookups);
				isValid=true;
			}
		}
		return isValid;
	}
	
	@Override
	protected void onResume() {
		VerisupplierUtils.mMoreInfoLookups = new ArrayList<Lookups>();
		VerisupplierUtils.mQuotationLookups = new ArrayList<Lookups>();
		VerisupplierUtils.mSubjectLookups=new ArrayList<Lookups>();
		VerisupplierUtils.mQuantityList=new ArrayList<Lookups>();
		super.onResume();
	}
	
	private void CallServices() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		ConnectionDetector mDetector=new ConnectionDetector(getApplicationContext());
		if(mDetector.isConnectingToInternet()){
			new AsyncProductListDetails().execute();
		}else{
			Toast.makeText(getApplicationContext(), "Please check Internet", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private class AsyncProductListDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdLoading = new ProgressDialog(UserSignPopup.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			downloadAllCategories();
			downloadFeaturedProducts();
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			Intent homeIntent=new Intent(UserSignPopup.this,Myopenquoate.class);
			startActivity(homeIntent);
			overridePendingTransition(0, 0);
			finish();
		}
		
	}

	private void downloadAllCategories() {
		List<CategoryDetails> mCategoryDetails=new ArrayList<CategoryDetails>();
		String mInparam="1";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetCategories, "GetCategories/", mInparam);
		if(result==null || result.length()==0){
		}else{
			mCategoryDetails=jsonParserClass.parseCategoryDetails(result,getApplicationContext());
			if(mCategoryDetails==null || mCategoryDetails.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertCategoryDetails(mCategoryDetails);
				helper.close();
			}
		}
	}

	/*private void downloadNewArrivals() {
		List<ProductList> mNewArrivalList=new ArrayList<ProductList>();
		String mInparam="1";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetFeaturedProducts, "GetFeaturedProducts/", mInparam);
		if(result==null || result.length()==0){
		}else{
			mNewArrivalList=jsonParserClass.parseNewArrival(result,getApplicationContext());
			if(mNewArrivalList==null || mNewArrivalList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertNewArrivalProducts(mNewArrivalList);
				helper.close();
			}
		}
	}*/

	private void downloadFeaturedProducts() {
		List<ProductList> mFeaturedProductList=new ArrayList<ProductList>();
		String mInparam="2";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetFeaturedProducts, "GetFeaturedProducts/", mInparam);
		if(result==null || result.length()==0){
		}else{
			mFeaturedProductList=jsonParserClass.parseNewArrival(result,getApplicationContext());
			if(mFeaturedProductList==null || mFeaturedProductList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertFeaturedProducts(mFeaturedProductList);
				helper.close();
			}
		}
	}

	/*private void downloadQualityPicks() {
		List<ProductList> mQualityPicksList=new ArrayList<ProductList>();
		String result = webServices.CallWebHTTPBindingService(ApiType.GetFeaturedProducts, "GetProductQualityPicks", "");
		if(result==null || result.length()==0){
		}else{
			mQualityPicksList=jsonParserClass.parseQualityPicks(result,getApplicationContext());
			if(mQualityPicksList==null || mQualityPicksList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertQualityPicks(mQualityPicksList);
				helper.close();
			}
		}
	}*/

	/*private void downloadVerifiedManufacturer() {
		List<ProductList> mVerifiedManufacturerList=new ArrayList<ProductList>();
		String result = webServices.CallWebHTTPBindingService(ApiType.GetVerifiedManufacturer, "GetVerifiedManufacturer", "");
		if(result==null || result.length()==0){
		}else{
			mVerifiedManufacturerList=jsonParserClass.parseVerifiedManufacturer(result,getApplicationContext());
			if(mVerifiedManufacturerList==null || mVerifiedManufacturerList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertVerifiedManufacturer(mVerifiedManufacturerList);
				helper.close();
			}
		}
	}*/

	public class AsyncDashboard extends AsyncTask<String, Void, Boolean> {
		Boolean IsQuotation = false;
		Boolean Isorder = false;
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(UserSignPopup.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			//Isorder = getAllDashboardService();
			getQuantityList();
			getAllOrderList();
			downloadCountries();
			downloadStates();
			return IsQuotation = getAllQuotation();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pdLoading.isShowing();
			pdLoading.cancel();
			if (result) {
				Intent dashboardIntent = new Intent(UserSignPopup.this, DashboardActivity.class);
				dashboardIntent.putExtra("FROMPAGE", "quotation");
				startActivity(dashboardIntent);
				overridePendingTransition(0, 0);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "Hello Exception",Toast.LENGTH_LONG).show();
			}
		}
	}

	private Boolean getAllQuotation() {
		Boolean isValid = false;
		if (isValid.equals(false)) {
			String Inparam = mUserDetail.getmUserID()+"/0/1/1/20";//"160" + "/" + "0" + "/" + "1" + "/" + "1" + "/" + "20";
			String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetQuotation, "GetQuotation/", Inparam);
			if (resultOutparam == null || resultOutparam.length() == 0) {
				isValid = false;
			} else {
				quoatationDashboards = jsonParserClass.ParseDashboardQuaotation(resultOutparam);
				Helper helper = new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertDashboardQuotation(quoatationDashboards);
				helper.close();
				isValid = true;
			}
		} else {
			isValid = false;
		}
		return isValid;
	}

	private void getAllOrderList() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String Inparam = mUserDetail.getmUserID()+"/0/1/1/10";
		String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetOrder, "GetOrder/", Inparam);
		if (resultOutparam == null || resultOutparam.length() == 0) {
		} else {
			VerisupplierUtils.mOrderDashboardList=jsonParserClass.ParseDashboardOrder(resultOutparam);
			Helper helper=new Helper(getApplicationContext());
			helper.openDataBase();
			helper.insertdashboardOrderValues(VerisupplierUtils.mOrderDashboardList);
			helper.close();
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

	
}
