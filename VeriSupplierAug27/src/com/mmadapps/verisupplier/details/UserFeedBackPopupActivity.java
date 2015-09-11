package com.mmadapps.verisupplier.details;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

//import com.google.android.gms.internal.he;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.login.SigninPageActivity;

public class UserFeedBackPopupActivity extends Activity implements OnClickListener{
	
	//layout variables
	LinearLayout main_layout;
	EditText user_feedback;
	RatingBar userRating;
	TextView feedback_submit;
	
	//services
	WebServices webServices;
	JsonParserClass jsonParserClass;
	private ProgressDialog pdLoading=null;
	
	//string variables
	String mFromActivity;
	String mUserFeedback;
	double ratingValue;
	String mProductId;
	String mManufacturerId;
	
	//from database
	UserDetails mUserDetails=new UserDetails();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature (Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userfeedback_popup);
		
		mFromActivity=getIntent().getStringExtra("FROMACTIVITY");
		mManufacturerId=getIntent().getStringExtra("MANUFACTURERID");
		mProductId=getIntent().getStringExtra("PRODUCTID");
		
		main_layout=(LinearLayout)findViewById(R.id.vL_ufpu_mainlayout);
		android.view.ViewGroup.LayoutParams layoutParams = main_layout.getLayoutParams();
		layoutParams.width = getXOffSetbyDensityforCal();
		layoutParams.height =getYOffSetbyDensityforCal();
		main_layout.setLayoutParams(layoutParams);
		
		initializeView();
		getUserDetails();
	}
	
	private int getXOffSetbyDensityforCal(){
		int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_HIGH: 
			return 500; 
		case DisplayMetrics.DENSITY_XHIGH: 
			return 680;
		case DisplayMetrics.DENSITY_XXHIGH: 
			return 750;
		default: 
			return 500;
		}
	}
	
	private int getYOffSetbyDensityforCal() {
		int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_HIGH: 
			return 500; 
		case DisplayMetrics.DENSITY_XHIGH: 
			return 680;
		case DisplayMetrics.DENSITY_XXHIGH: 
			return 750;
		default: 
			return 500;
		}

	}
	
	private void initializeView() {
		userRating=(RatingBar)findViewById(R.id.rb_fbf_userRating);
		user_feedback=(EditText)findViewById(R.id.vT_fbf_userfeedback);
		feedback_submit=(TextView)findViewById(R.id.vT_fbf_feedbacksubmit);
		feedback_submit.setOnClickListener(this);
		
		setValues();
	}
	
	private void setValues() {
		userRating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
				ratingValue=rating;
			}
		});
		
		user_feedback.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mUserFeedback=user_feedback.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void getUserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vT_fbf_feedbacksubmit:
			callService();
			break;

		default:
			break;
		}
	}
	
	private void callService() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		ConnectionDetector mDetector=new ConnectionDetector(getApplicationContext());
		if(mDetector.isConnectingToInternet()){
			if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().length()==0){
				Intent getPriceIntent=new Intent(UserFeedBackPopupActivity.this,SigninPageActivity.class);
				getPriceIntent.putExtra("FROMACTIVITY", "userfeedback");
				startActivity(getPriceIntent);
				overridePendingTransition(0, 0);
				Toast.makeText(getApplicationContext(), "Please Login to give feedback", Toast.LENGTH_SHORT).show();
			}else{
				new AsyncFeedbackDetails().execute();
			}
		}else{
			Toast.makeText(getApplicationContext(), "Please check the internet", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class AsyncFeedbackDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(UserFeedBackPopupActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return callfeedbackService();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			if(result){
				UpdateProductFeedback();
			}else{
				Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	public Boolean callfeedbackService() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String result = "";
		String postURL="";
		try {
			HttpClient client = new DefaultHttpClient();  
			if(mFromActivity.equalsIgnoreCase("productfeedback")){
				postURL = webServices.SENDREVIEW;
			}else{
				postURL = webServices.MANUFACTURERREVIEW;
			}
	
			InputStream inputStream = null;
			HttpPost httpPost = new HttpPost(postURL);
			String json = ""; 
	
			Calendar cal=Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
			String mCurrentDate=sdf.format(cal.getTime());
			System.out.println(""+mCurrentDate);
			
			String mUserId = mUserDetails.getmUserID();
			try {
				JSONObject jsonObject = new JSONObject();
				
				JSONObject productReviewModelObject=new JSONObject();
				productReviewModelObject.put("IsApproved", 1);
				productReviewModelObject.put("Title", "Rating");
				productReviewModelObject.put("ReviewText", mUserFeedback);
				productReviewModelObject.put("Rating", ratingValue);
				productReviewModelObject.put("CustomerId", mUserId);
				
				if(mFromActivity.equalsIgnoreCase("productfeedback")){
					JSONObject productlObject=new JSONObject();
					productlObject.put("ProductId", mProductId);
					productReviewModelObject.put("Product", productlObject);
					jsonObject.put("productReviewModel", productReviewModelObject);
				}else{
					JSONObject manufacturerObject=new JSONObject();
					manufacturerObject.put("ManufacturerId", mManufacturerId);
					productReviewModelObject.put("Manufacturer", manufacturerObject);
					jsonObject.put("ManufacturerReviewModel", productReviewModelObject);
				}
				
				json = jsonObject.toString();
				Log.e("Save Quotatiion", json);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			StringEntity se = new StringEntity(json, HTTP.UTF_8);
			httpPost.setEntity(se);
			
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("JWTKEY","Verisupplier.android.d2335fhfg4564hghjghjghjget45ert.1.0");
			httpPost.setHeader("OS","ANDROID");
			httpPost.setHeader("USERID",mUserDetails.getmUserID());
			
			HttpResponse httpResponse = client.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.d("statusCode",""+statusCode);
			if(statusCode !=200){
				isValid=false;
				return false;
			}
			inputStream = httpResponse.getEntity().getContent();
			System.out.println(""+inputStream);
			if(inputStream != null){
				result = convertInputStreamToString(inputStream);
	        }
			if(result == null || result.length() == 0){
				isValid=false;
			}else{
				jsonParserClass = new JsonParserClass();
				if(mFromActivity.equalsIgnoreCase("productfeedback")){
					String rslt=jsonParserClass.parseProductReviewResult(result);
					if(rslt != null && rslt.equalsIgnoreCase("true")){
						isValid=true;
					}
				}else{
					String rslt=jsonParserClass.parseManufacturerReviewResult(result);
					if(rslt != null && rslt.equalsIgnoreCase("true")){
						isValid=true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isValid;
	}
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;
	}
	
	private void UpdateProductFeedback() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		ConnectionDetector mDetector=new ConnectionDetector(getApplicationContext());
		if(mDetector.isConnectingToInternet()){
			new AsyncProductDetails().execute();
		}else{
			Toast.makeText(getApplicationContext(), "Please check internet", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class AsyncProductDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		 protected void onPreExecute() {
			 pdLoading = new ProgressDialog(UserFeedBackPopupActivity.this);
			 pdLoading.setMessage("please wait...");
			 pdLoading.show();
			 pdLoading.setCancelable(false);
			 pdLoading.setCanceledOnTouchOutside(false);
			 super.onPreExecute();
		 }

		@Override
		protected Boolean doInBackground(Void... params) {
			return isValidDetails();
		}
		
		@Override
		 protected void onPostExecute(Boolean result) {
			 super.onPostExecute(result);
			 pdLoading.cancel();
			 if (pdLoading.isShowing())
				 pdLoading.dismiss();
			 if(result){
				finish();
			 }else{
				 Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_SHORT).show();
			 }
			
		 }
	}
	
	private Boolean isValidDetails() {
		 Boolean isValid=false;
		 webServices=new WebServices();
		 jsonParserClass=new JsonParserClass();
		 List<ProductDetails> mProductDetail = new ArrayList<ProductDetails>();
		 
		 String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetProductDetail,"GetProductDetail/", mProductId);
		 if(resultOutparam==null || resultOutparam.length()==0){
			 isValid=false;
		 }else{
			 mProductDetail=jsonParserClass.parseProductDetails(resultOutparam,getApplicationContext());
			 if(mProductDetail==null || mProductDetail.size()==0){
			 }else{
				 Helper helper=new Helper(getApplicationContext());
				 helper.openDataBase();
				 helper.UpdateProductDetails(mProductDetail,mProductId);
				 helper.UpdateSpecificationDetails(mProductDetail,mProductId);
				 helper.UpdateProductImageDetails(mProductDetail,mProductId);
				 helper.UpdateUserFeedback(mProductDetail,mProductId);
				 helper.close();
				 isValid=true;
			 }
		 }
		return isValid;
	}
	
	@Override
	protected void onResume() {
		getUserDetails();
		super.onResume();
	}

}
