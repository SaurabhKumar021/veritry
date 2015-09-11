package com.mmadapps.verisupplier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.mmadapps.verisupplier.beans.QuoatationDashboard;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.customservices.CustomCreateQuotation;
import com.mmadapps.verisupplier.dashboard.DashboardActivity;
import com.mmadapps.verisupplier.details.GetQuotationActivityNew;
import com.mmadapps.verisupplier.details.MoreInfoActivity;
import com.mmadapps.verisupplier.details.MoreInfoActivityNew;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.mmadapps.verisupplier.inspection.InspectionCreateQuotation;
import com.mmadapps.verisupplier.leftmenu.Myopenquoate;
import com.mmadapps.verisupplier.placeorder.PlaceorderShipping;
import com.mmadapps.verisupplier.products.ProductCreateQuotationActivity;
import com.mmadapps.verisupplier.shipping.ShippingCreateQuotation;
import com.mmadapps.verisupplier.warehouse.WarehouseCreateQuotation;

public class UserOTPpopup extends Activity implements OnClickListener{
	
	//main layout to set layout width and height
	LinearLayout main_layout;
	
	//popup close
	ImageView otp_close;
	
	//textview for submit,cancle and regenerate
	TextView otp_submit,otp_cancel,Regenerate_otp;
	
	//edit text for otp
	EditText otp_text;
	
	//service call
	private ProgressDialog pdLoading;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	
	//from intent
	String mUserEmail,mUserPassword,mUserMobile,mUserOTP;
	String mFromActivity="";
	String mManufacturerId,mProductID;
	
	//from database
	List<CategoryDetails> mCategoryDetails=new ArrayList<CategoryDetails>();
	UserDetails mUserDetail=new UserDetails();
	ArrayList<QuoatationDashboard> quoatationDashboards;
	List<CountryDetails> mCounList=new ArrayList<CountryDetails>();
	List<CountryDetails> mStateList=new ArrayList<CountryDetails>();
	
	//24 hour time differance for service call
	final double DIFFTIME=8.64e+7;
	
	//fro OTP value
	String OTP = "";
	public static String mOTP="";
	String Inparam;
	
	List<Lookups> mInspectionType=new ArrayList<Lookups>();
	List<Lookups> mIndustryList=new ArrayList<Lookups>();
	List<Lookups> mInspectorList=new ArrayList<Lookups>();
	List<Lookups> mUnitOfMeasurentList=new ArrayList<Lookups>();
	List<Lookups> mQuotationLookupList=new ArrayList<Lookups>();
	List<Lookups> mMoreinfoSubjectList=new ArrayList<Lookups>();
	List<Lookups> mMoreinfoLookupList=new ArrayList<Lookups>();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.user_otppopup);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		//to setr the popup layout width and height
		main_layout = (LinearLayout) findViewById(R.id.vL_otpmain_layout);
		android.view.ViewGroup.LayoutParams layoutParams = main_layout.getLayoutParams();
		layoutParams.width = LayoutParams.WRAP_CONTENT;
		layoutParams.height = getYOffSetbyDensityforCal();
		main_layout.setLayoutParams(layoutParams);
		
		//from intent
		mFromActivity=getIntent().getStringExtra("FROMACTIVITY");
		mManufacturerId=getIntent().getStringExtra("MANUFACTURERID");
		mProductID=getIntent().getStringExtra("PRODUCTID");
		mUserEmail=getIntent().getStringExtra("USEREMAIL");
		mUserMobile=getIntent().getStringExtra("MOBILE");
		mUserPassword=getIntent().getStringExtra("USERPASSWORD");
		mUserOTP=getIntent().getStringExtra("USEROTP");
		
		initializeView();
	}
	
	private int getYOffSetbyDensityforCal() {
		int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_HIGH:
			return 800;
		case DisplayMetrics.DENSITY_XHIGH:
			return 950;
		case DisplayMetrics.DENSITY_XXHIGH:
			return 1000;
		default:
			return 800;
		}
	}

	private void initializeView() {
		otp_close=(ImageView)findViewById(R.id.vI_uotppu_closeImage);
		otp_submit=(TextView) findViewById(R.id.vT_uotppu_submit_text);
		otp_cancel=(TextView) findViewById(R.id.vT_uotppu_cancel_text);
		Regenerate_otp=(TextView) findViewById(R.id.vT_uotppu_generateotp_text);
		otp_text=(EditText) findViewById(R.id.vE_uotppu_otpnumber);
		otp_close.setOnClickListener(this);
		otp_submit.setOnClickListener(this);
		otp_cancel.setOnClickListener(this);
		Regenerate_otp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		//popup close click
		case R.id.vI_uotppu_closeImage:
			finish();
			break;
			
		//popup submit click	
		case R.id.vT_uotppu_submit_text:
			String otp_edit=otp_text.getText().toString().trim();
			//validate otp entered and generated is same 
			if (otp_edit.equals(mUserOTP)){
				CallServices();
			}else {
				Toast.makeText(getApplicationContext(), "Please enter valid otp", Toast.LENGTH_SHORT).show();
			}
			break;
			
		//popup cancel click	
		case R.id.vT_uotppu_cancel_text:
			finish();
			break;
		
		//popup regenerate click(pending)
		case R.id.vT_uotppu_generateotp_text:
			otp_text.setText("");
			//generate otp
			OTP = generateOTP(); 
			//send otp
			new AsyncSendOTP().execute();
			break;
			
		default:
			break;
		}
	}

	private void CallServices() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		//check network connection 
		ConnectionDetector mDetector=new ConnectionDetector(getApplicationContext());
		if(mDetector.isConnectingToInternet()){
			new AsyncreguserDetails().execute();
		}else{
			Toast.makeText(getApplicationContext(), "Please check Internet", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	class AsyncreguserDetails extends AsyncTask<String, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(UserOTPpopup.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
		    super.onPreExecute();  
		}
		@Override
		protected Boolean doInBackground(String... arg0) {
			//register the user
			return getresultresponse();
		}
		
		protected void onPostExecute(Boolean result) {
			   super.onPostExecute(result);
			   pdLoading.cancel();
			   if(pdLoading.isShowing())
				   pdLoading.dismiss();
			   if(result){
				   //to get the user details
				   getUserId();
				   new AsyncUserDetails().execute();
			   }else {
					Toast.makeText(getApplicationContext(), "Registration not successful", Toast.LENGTH_LONG).show();
			   }
			}
	}
	
	private void getUserId() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		UserDetails mUserDetails=new UserDetails();
		String Inparam=mUserEmail;
		String resultOutput=webServices.CallWebHTTPBindingService(ApiType.UserDetails, "UserDetails/", Inparam);
		if(resultOutput==null || resultOutput.length()==0){
		}else{
			mUserDetails=jsonParserClass.parseUserDetails(resultOutput,getApplicationContext());
			Helper helper=new Helper(getApplicationContext());
			helper.openDataBase();
			helper.insertUserDetails(mUserDetails,true);
			helper.close();
		}
	}
	
	private Boolean getresultresponse() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		if (isValid.equals(false)) {
			String result = "";
			HttpClient client = new DefaultHttpClient();
			String postURL =webServices.SENDUSERREGISTRATION;//"http://vsuppliervm.cloudapp.net:5132/UserService.svc/UserRegistration";
			InputStream inputStream = null;

			HttpPost httpPost = new HttpPost(postURL);
			String json = "";
			JSONObject jsonObject = new JSONObject();
			try{
				jsonObject.put("emailId", mUserEmail);
				jsonObject.put("password", mUserPassword);
				jsonObject.put("contactNumber", mUserMobile);
				//jsonObject.put("", "");
			}catch(Exception e){
				e.printStackTrace();
			}
			json = jsonObject.toString();
			if (json == null || json.length() == 0) {
				isValid = false;
			} else {
				try {
					StringEntity entity = new StringEntity(json, HTTP.UTF_8);
					httpPost.setEntity(entity);
					httpPost.setHeader("Content-Type", "application/json");
					httpPost.setHeader("Accept", "application/json");
					httpPost.setHeader("JWTKEY","Verisupplier.android.d2335fhfg4564hghjghjghjget45ert.1.0");
					httpPost.setHeader("OS","ANDROID");
					httpPost.setHeader("USERID",mUserDetail.getmUserID());
					HttpResponse httpResponse = client.execute(httpPost);
					int statuscode = httpResponse.getStatusLine().getStatusCode();

					if (statuscode != 200) {
						return isValid;
					}
					inputStream = httpResponse.getEntity().getContent();
					if (inputStream != null) {
						result = convertInputStreamToString(inputStream);
					}
					String response = jsonParserClass.parseregdetails(result);
					
					if(response.equalsIgnoreCase("true")){
						isValid = true;
					}else{
						isValid = false;
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return isValid;
	}
	
	private static String convertInputStreamToString(InputStream inputStream)throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;

	}
	
	private class AsyncUserDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(UserOTPpopup.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			//from product deatils page getprice onclick
			if(mFromActivity.equalsIgnoreCase("getprice")){
				getQuantityList();
				return getManufacturerDetaisl();
			}
			//from product deatils page getquotation onclick
			else if(mFromActivity.equalsIgnoreCase("getquotation")){
				if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
					getQuantityList();
					getQuotationLookups();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
					downloadCountries();
					downloadStates();
					downloadTypeOfIndustry();
					downloadInspectorType();
					downloadInspectionTypes();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
					downloadCountries();
					downloadStates();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
					getQuantityList();
					downloadCountries();
					downloadStates();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("EC")){
				}
				getManufacturerDetaisl();
				return true;
			}
			//from product details page moreinfo onclick 
			else if(mFromActivity.equalsIgnoreCase("moreinfo")){
				getManufacturerDetaisl();
				getSubjectDetails();
				getMoreInfoLookups();
				return true;
			}
			//from product details page placeorder onclick
			else if(mFromActivity.equalsIgnoreCase("overviewplaceorder")){
				if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
					//getQuotationDetails();
					callQuotationOrderService();
					getQuantityList();
					getQuotationLookups();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
					downloadCountries();
					downloadStates();
					downloadTypeOfIndustry();
					downloadInspectorType();
					downloadInspectionTypes();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
					downloadCountries();
					downloadStates();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
					downloadCountries();
					downloadStates();
					getQuantityList();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("EC")){
				}
				getManufacturerDetaisl();
				return true;
				//return isValidUser(); // Pending
			}
			//from marketplace page openquote onclick
			else if(mFromActivity.equalsIgnoreCase("marketopenquote")){
				getQuantityList();
				LastDownloadTime();
			}
			//from marketplace page dashboard onclick
			else if(mFromActivity.equalsIgnoreCase("dashboard")){
				return true;
			}
			else if(mFromActivity.equalsIgnoreCase("switchdashboard")){
				return true;
			}
			//from marketplace page userprofile signin onclick
			else if(mFromActivity.equalsIgnoreCase("signin")){
				return true;
			}
			//from product deatils page placeorder onclick
			else if(mFromActivity.equalsIgnoreCase("placeorder")){
				if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
					callQuotationOrderService();
					getQuantityList();
					getQuotationLookups();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
					downloadCountries();
					downloadStates();
					downloadTypeOfIndustry();
					downloadInspectorType();
					downloadInspectionTypes();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
					downloadCountries();
					downloadStates();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
					downloadCountries();
					downloadStates();
					getQuantityList();
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
				}
				else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("EC")){
				}
				getManufacturerDetaisl();
				return true;
			}
			else if(mFromActivity.equalsIgnoreCase("userfeedback")){
				return true;
			}
			//from product deatils page moreinfo onclick
			else{
				getQuantityList();
				getManufacturerDetaisl();
				getMoreInfoLookups();
				return true;
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
					Intent getPriceIntent=new Intent(UserOTPpopup.this,GetQuotationActivityNew.class);
					getPriceIntent.putExtra("FROMACTIVITY", "getprice");
					startActivity(getPriceIntent);
					overridePendingTransition(0, 0);
					finish();
				}
				//to go getquotation activity
				else if(mFromActivity.equalsIgnoreCase("getquotation")){
					forwardTogetQuotationActivity();
					/*Intent getquotationIntent=new Intent(UserOTPpopup.this,GetQuotationActivityNew.class);
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
					forwardToPlaceOrderActivity();
					/*Intent placeorderIntent=new Intent(UserOTPpopup.this,PlaceorderShipping.class);
					startActivity(placeorderIntent);
					overridePendingTransition(0, 0);
					finish();*/
				}
				//to go openquote activity
				else if(mFromActivity.equalsIgnoreCase("marketopenquote")){
					checkLastDownloadTime();
				}
				//to go dashboard activity
				else if(mFromActivity.equalsIgnoreCase("dashboard")){
					UserDetails();
					new AsyncDashboard().execute();
				}
				else if(mFromActivity.equalsIgnoreCase("switchdashboard")){
					UserDetails();
					new AsyncDashboard().execute();
				}
				//get signin from userprofile 
				else if(mFromActivity.equalsIgnoreCase("signin")){
					finish();
				}
				else if(mFromActivity.equalsIgnoreCase("userfeedback")){
					finish();
				}
				//to go placeorder activity
				else if(mFromActivity.equalsIgnoreCase("placeorder")){
					forwardToPlaceOrderActivity();
					
					/*PlaceorderShipping.mFromActivity="PD";
					PlaceorderShipping.mProductId=mProductID;
					PlaceorderShipping.mManufacturerId=mManufacturerId;
					Intent placeOrderIntent = new Intent(UserOTPpopup.this, PlaceorderShipping.class);
					startActivity(placeOrderIntent);
					overridePendingTransition(0, 0);
					finish();*/
				}
				//to go moreinfo activity
				else {
					Intent placeorderIntent=new Intent(UserOTPpopup.this,MoreInfoActivity.class);
					startActivity(placeorderIntent);
					overridePendingTransition(0, 0);
					finish();
				}
			}else{
				//not confirmed
				if(mFromActivity.equalsIgnoreCase("marketopenquote")){
					CallServices();
				}else if(mFromActivity.equalsIgnoreCase("marketdashboard")){
					UserDetails();
					new AsyncDashboard().execute();
				}else if(mFromActivity.equalsIgnoreCase("signin")){
					//BaseActionBarActivity.signin_text.setText("Log out");
					finish();
				}
				//to show service call error
				else{
					Toast.makeText(UserOTPpopup.this, "error in signup", Toast.LENGTH_SHORT).show();
				}
			}
		}

	}
	
	private void forwardTogetQuotationActivity() {
		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			Intent getQuotationIntent = new Intent(UserOTPpopup.this, ProductCreateQuotationActivity.class);
			getQuotationIntent.putExtra("FROMACTIVITY", "getquotation");
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
			Intent getQuotationIntent = new Intent(UserOTPpopup.this, InspectionCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}
		else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
			Intent getQuotationIntent = new Intent(UserOTPpopup.this, ShippingCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
			Intent getQuotationIntent = new Intent(UserOTPpopup.this, CustomCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
			Intent getQuotationIntent = new Intent(UserOTPpopup.this, WarehouseCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}
	}
	
	private void forwardToMoreInfoActivity() {
		Intent moreInfoIntent = new Intent(UserOTPpopup.this, MoreInfoActivityNew.class);
		startActivity(moreInfoIntent);
		overridePendingTransition(0, 0);
		finish();
	}
	
	private void forwardToPlaceOrderActivity() {
		
		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			PlaceorderShipping.mFromActivity="PD";
			Intent placeOrderIntent = new Intent(UserOTPpopup.this, PlaceorderShipping.class);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);
			finish();
			/*Intent placeOrderIntent = new Intent(UserOTPpopup.this, ProductCreateQuotationActivity.class);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);
			finish();*/
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
			Intent getQuotationIntent = new Intent(UserOTPpopup.this, ShippingCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
			Intent getQuotationIntent = new Intent(UserOTPpopup.this, CustomCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
			/*Intent placeOrderIntent = new Intent(ProductDetailsActivity.this, PlaceOrderCustomsActivity.class);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);*/
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
			Intent getQuotationIntent = new Intent(UserOTPpopup.this, InspectionCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
			/*Intent placeOrderIntent = new Intent(ProductDetailsActivity.this, PlaceOrderInspectionActivity.class);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);*/
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
			Intent getQuotationIntent = new Intent(UserOTPpopup.this, WarehouseCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
			/*Intent placeOrderIntent = new Intent(ProductDetailsActivity.this, PlaceOrderWarehouseActivity.class);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);*/
		}
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
		/*Boolean isValid=false;
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
		return isValid;*/
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
		/*Boolean isValid=false;
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
		return isValid;*/
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

	private void getMoreInfoLookups() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mMoreinfoLookupList=helper.getMoreinfoLookupList();
		helper.close();
		
		if(mMoreinfoLookupList == null || mMoreinfoLookupList.size()==0){
			downloadMoreinfoLookupList();
		}else if(checkdownloadTime(mMoreinfoLookupList.get(0).getmLastDownloadTime())){
		}else{
			getMoreInfoLookups();
		}
		/*Boolean isValid=false;
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
		return isValid;*/
	}
	
	private void downloadMoreinfoLookupList() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		List<Lookups> mMoreinfoLookupList=new ArrayList<Lookups>();
		String mInparam="Types_of_Additional_Info/Additional_Info";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", mInparam);
		if(result==null || result.length()==0){
		}else{
			mMoreinfoLookupList=jsonParserClass.pareseLookups(result);
			if(mMoreinfoLookupList==null || mMoreinfoLookupList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertMoreinfoLookupList(mMoreinfoLookupList);
				helper.close();
			}
		}
	}
	
	private void getSubjectDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mMoreinfoSubjectList=helper.getMoreinfoSubjectList();
		helper.close();
		
		if(mMoreinfoSubjectList == null || mMoreinfoSubjectList.size()==0){
			downloadMoreinfoSubjectList();
		}else if(checkdownloadTime(mMoreinfoSubjectList.get(0).getmLastDownloadTime())){
		}else{
			getQuantityList();
		}
		/*Boolean isValid=false;
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
		return isValid;*/
	}
	
	private void downloadMoreinfoSubjectList() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		List<Lookups> mMoreinfoSubjectList=new ArrayList<Lookups>();
		String mInparam="Types_of_Additional_Info/Additional_Info";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", mInparam);
		if(result==null || result.length()==0){
		}else{
			mMoreinfoSubjectList=jsonParserClass.pareseLookups(result);
			if(mMoreinfoSubjectList==null || mMoreinfoSubjectList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertMoreinfoSubjectList(mMoreinfoSubjectList);
				helper.close();
			}
		}
	}


	private Boolean isValidUser() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String emailEncode=Uri.encode(""+mUserEmail);
		String passwordEncode=Uri.encode(mUserPassword);
		String Inparam="?email="+emailEncode+"&secret="+passwordEncode;
		String resultOutParam=webServices.CallWebHTTPBindingService(ApiType.ValidateUser, "ValidateUser", Inparam);
		if(resultOutParam==null || resultOutParam.length()==0){
			isValid=false;
		}else{
			String[] result=jsonParserClass.parserAuthentication(resultOutParam,getApplicationContext());
			if(result.length==0 || result[1].equalsIgnoreCase("true")){
				isValid=true;
			}else{
				isValid=false;
			}
		}
		return isValid;
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

	private void getQuotationDetails() {
		UserDetails();
		webServices =new WebServices();
		jsonParserClass=new JsonParserClass();
		String Inparam=mUserDetail.getmUserID()+"/"+mManufacturerId+"/"+mProductID;
		String outputParm=webServices.CallWebHTTPBindingService(ApiType.GetQuoteDetailsForQuickOrder, "GetQuoteDetailsForQuickOrder/", Inparam);
		if(outputParm==null || outputParm.length()==0){
		}else{
			VerisupplierUtils.mQuotationDetails=jsonParserClass.parseQuickOrderResult(outputParm,"GetQuoteDetailsForQuickOrderResult");
		}
	}

	private void UserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetail=helper.getUserDetails();
		helper.close();
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
				Intent homeIntent=new Intent(UserOTPpopup.this,Myopenquoate.class);
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

	public class AsyncDashboard extends AsyncTask<String, Void, Boolean> {

		Boolean IsQuotation = false;
		Boolean Isorder = false;

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(UserOTPpopup.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			getQuantityList();
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
				Intent dashboardIntent = new Intent(UserOTPpopup.this, DashboardActivity.class);
				dashboardIntent.putExtra("Type", mFromActivity);
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
			if(mFromActivity.equalsIgnoreCase("dashboard")){
				if(mUserDetail.getmUserType() == null || mUserDetail.getmUserType().length()==0){
					
				}else{
					if(mUserDetail.getmUserType().equalsIgnoreCase("Customer")){
						Inparam = mUserDetail.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
					}
					else if(mUserDetail.getmUserType().equalsIgnoreCase("Manufacturer") || mUserDetail.getmUserType().equalsIgnoreCase("Product")){
						String mManufacturerId=mUserDetail.getmManufacturerId();
						Inparam = "0/"+mManufacturerId+"/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
					}
					else if(mUserDetail.getmUserType().equalsIgnoreCase("Customs")){
						String mManufacturerId=mUserDetail.getmManufacturerId();
						Inparam = "0/"+mManufacturerId+"/"+VerisupplierUtils.mCustomsParam+"/1/20";
					}
					else if(mUserDetail.getmUserType().equalsIgnoreCase("Shipment")){
						String mManufacturerId=mUserDetail.getmManufacturerId();
						Inparam = "0/"+mManufacturerId+"/"+VerisupplierUtils.mShippingParam+"/1/20";
					}
					else if(mUserDetail.getmUserType().equalsIgnoreCase("Inspection")){
						String mManufacturerId=mUserDetail.getmManufacturerId();
						Inparam = "0/"+mManufacturerId+"/"+VerisupplierUtils.mInspectionParam+"/1/20";
					}
					else if(mUserDetail.getmUserType().equalsIgnoreCase("Warehouse")){
						String mManufacturerId=mUserDetail.getmManufacturerId();
						Inparam = "0/"+mManufacturerId+"/"+VerisupplierUtils.mWarehouseParam+"/1/20";
					}
				}
			}else{
				Inparam = mUserDetail.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
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
	
	private class AsyncSendOTP extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(UserOTPpopup.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return sendOTP();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pdLoading.cancel();
			if(pdLoading.isShowing())
				pdLoading.dismiss();
			if(result){
				Toast.makeText(getApplicationContext(), "Your OTP is sent to ur mail please check", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "Please try agin", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private String generateOTP() {
		String otpValue = "";
		int lenthofpass = 6;
		String allowedChars = "";
		allowedChars += "1,2,3,4,5,6,7,8,9,0";
		String[] args = allowedChars.split(",");
		String temp = "";
		Random rand = new Random();
		for (int i = 0; i < lenthofpass; i++) {
			temp = args[rand.nextInt(args.length)];
			otpValue += temp;
		}
		return otpValue;
	}
	
	private Boolean sendOTP() {
		Boolean isValid=false;
		webServices = new WebServices();
		jsonParserClass = new JsonParserClass();
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			String postURL = webServices.SendEmailTemplate;
			InputStream inputStream = null;
			HttpPost httpPost = new HttpPost(postURL);
			String json = "";

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("action", "otp");
			try {
				JSONArray jsonArray = new JSONArray();
				jsonArray.put(mUserEmail);
				jsonArray.put(OTP);
				jsonObject.put("emailParam", jsonArray);
			} catch (Exception e) {
				e.printStackTrace();
			}

			json = jsonObject.toString();
			StringEntity se = new StringEntity(json, HTTP.UTF_8);
			httpPost.setEntity(se);

			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			HttpResponse httpResponse = client.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.d("statusCode", "" + statusCode);
			if (statusCode == 404) {
				return null;
			}

			inputStream = httpResponse.getEntity().getContent();
			System.out.println("" + inputStream);
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
			}

			//parse otp result and send to userotppopup activity
			String[] response = jsonParserClass.parseemailotp(result);
			if(response[1].equalsIgnoreCase("true")){
				mUserOTP=response[0];
				isValid = true;
			}else{
				isValid = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
	private Boolean callQuotationOrderService() {
		Boolean isValid = false;
		webServices = new WebServices();
		jsonParserClass = new JsonParserClass();
		String Inparam = ""+mUserDetail.getmUserID()+"/"+mManufacturerId+"/"+mProductID;
		String resultOutParam = webServices.CallWebHTTPBindingService(ApiType.GetQuoteDetailsForQuickOrder,"GetQuoteDetailsForQuickOrder/", Inparam);
		if (resultOutParam == null || resultOutParam.length() == 0) {
			isValid = false;
		} else {
			VerisupplierUtils.mQuotationDetails = jsonParserClass.parseQuickOrderResult(resultOutParam,"GetQuoteDetailsForOrderResult");
			if (VerisupplierUtils.mQuotationDetails != null) {
				isValid = true;
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
}