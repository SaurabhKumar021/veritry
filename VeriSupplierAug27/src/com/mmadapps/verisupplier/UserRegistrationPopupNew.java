package com.mmadapps.verisupplier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.customs.Encrypt;

public class UserRegistrationPopupNew extends Activity implements OnClickListener  {
	
	//registration button layout
	LinearLayout main_layout,register_layout;
	
	//popup close
	ImageView registration_close;
	
	//edittext for email,mobile number,password and confirm password
	EditText regemail_id,regmobile_num,reg_pass,regconfirm_pass;
	
	//textview for registrationtext
	TextView register_text;
	
	//encryption key
	private String KEY="1429111390460322";
	static String IV="90460322";
	
	//service call
	private ProgressDialog pdLoading;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	
	//email validtion
	static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]|$";
	
	//fro OTP value
	String OTP = "";
	public static String mOTP="";
	
	//from intent
	String mFromActivity="";
	String mProductID,mManufacturerId;

	//string variables for useremail,passwor and mobile number
	String mUserEmail,mUserPassword,mUserMobile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.user_registrationpopup);
		
		//from intent
		mFromActivity=getIntent().getStringExtra("FROMACTIVITY");
		mManufacturerId=getIntent().getStringExtra("MANUFACTURERID");
		mProductID=getIntent().getStringExtra("PRODUCTID");
		
		//popup layout width and height variables
		main_layout = (LinearLayout) findViewById(R.id.vL_registermain_layout);
		android.view.ViewGroup.LayoutParams layoutParams = main_layout.getLayoutParams();
		layoutParams.width = LayoutParams.WRAP_CONTENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;//getYOffSetbyDensityforCal();
		main_layout.setLayoutParams(layoutParams);
		
		initializeView();
	}
	
	private void initializeView() {
		registration_close=(ImageView)findViewById(R.id.vI_urpu_closeImage);
		registration_close.setOnClickListener(this);
		register_layout=(LinearLayout)findViewById(R.id.vL_urpu_register_layout);
		register_text=(TextView)findViewById(R.id.vT_urpu_register_text);
		regemail_id=(EditText) findViewById(R.id.vE_urpu_emailId);
		regmobile_num=(EditText) findViewById(R.id.vE_urpu_mobile);
		reg_pass=(EditText) findViewById(R.id.vE_urpu_password);
		regconfirm_pass=(EditText) findViewById(R.id.vE_urpu_confirmpassword);
		
		register_layout.setOnClickListener(this);
		register_text.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//popup close click
		case R.id.vI_urpu_closeImage:
			finish();
			break;
		
		//popup registration click
		case R.id.vL_urpu_register_layout:
		case R.id.vT_urpu_register_text:
			//check useremail,password and mobile number validation
			getAllreginformations();
			break;
		default:
			break;
		}
		
	}

	private void getAllreginformations() {
		String useremail=""+regemail_id.getEditableText().toString().trim();
		String usermobile=""+regmobile_num.getEditableText().toString().trim();
		String userpassword=""+reg_pass.getEditableText().toString().trim();
		String userconpass=""+regconfirm_pass.getEditableText().toString().trim();
		
		if(useremail == null || useremail.length() == 0){
			regemail_id.setError("please enter email");
		}
		
		regemail_id.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				regemail_id.setError(null);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		if (usermobile == null || usermobile.length() == 0){
			regmobile_num.setError("Please enter mobilenumber");
		}
		
		regmobile_num.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				regmobile_num.setError(null);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		if (userpassword == null || userpassword.length() == 0){
			reg_pass.setError("please enter password");
		}
		
		reg_pass.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				reg_pass.setError(null);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		if (useremail == null || useremail.length() == 0 || 
				usermobile == null || usermobile.length() == 0 ||
				userpassword == null || userpassword.length() == 0||
				userconpass == null || userconpass.length() == 0) {
			
			Toast.makeText(getApplicationContext(), "please fill all details",Toast.LENGTH_SHORT).show();
		}else if(!isValiedID(useremail)){
			regemail_id.setError("Please enter vaild EmailId");
		}else if(!isValidPhoneNumber(usermobile)){
			regmobile_num.setError("please enter valid contact number");
		}else{
			mUserEmail=regemail_id.getEditableText().toString().trim();
			mUserMobile=regmobile_num.getText().toString().trim();
			if (userpassword.length() == userconpass.length() && userconpass.equalsIgnoreCase(userpassword)){
				try{
					//password encryption
					userpassword = new Encrypt().encryptText(userpassword,KEY,IV);
					mUserPassword=userpassword;
				}catch(Exception e){
					e.printStackTrace();
				}
				//service call to check is user exist
				CallServices();
			}else{
				regconfirm_pass.setError("Both passwords should match");
			}
		}
	}
	
	//email validation
	private boolean isValiedID(String email) {
		return email.matches(EMAIL_REGEX);
	}
	
	//phone number validation
	public static final boolean isValidPhoneNumber(CharSequence target) {
		if (target.length() != 10) {
			return false;
		} else {
			return android.util.Patterns.PHONE.matcher(target).matches();
		}
	}
	
	private void CallServices() {
		//check network connection
		ConnectionDetector mDetector=new ConnectionDetector(getApplicationContext());
		if(mDetector.isConnectingToInternet()){
			new AsyncIsExistUser().execute();
		}else{
			Toast.makeText(getApplicationContext(), "Please check Internet", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class AsyncIsExistUser extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(UserRegistrationPopupNew.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return isExistUser();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			 pdLoading.cancel();
			   if(pdLoading.isShowing())
				   pdLoading.dismiss();
			   if(result){
				   //if user already exist go to signin page
				   Toast.makeText(getApplicationContext(), "You are already exist user.Please Login", Toast.LENGTH_LONG).show();
				   Intent signinIntent=new Intent(UserRegistrationPopupNew.this,UserSignPopup.class);
				   startActivity(signinIntent);
				   finish();
			   }else{
				   //generate otp
				   OTP = generateOTP(); 
				   //send otp
				   new AsyncSendOTP().execute();
			   }
		}
	}
	
	private Boolean isExistUser() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String Inparam=mUserEmail+"/"+mUserMobile;
		String resultOutput=webServices.CallWebHTTPBindingService(ApiType.IsAccountExist, "IsAccountExist/", Inparam);
		if(resultOutput==null || resultOutput.length()==0){
			isValid=false;
		}else{
			String mUserDetails=jsonParserClass.parseregUserDetails(resultOutput);
			if(mUserDetails.equalsIgnoreCase("false")){
				isValid=false;
			}else{
				isValid=true;
			}
		}
		return isValid;
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
	
	private class AsyncSendOTP extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(UserRegistrationPopupNew.this);
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
				//after otp generation go to userOtppopup activity 
				Intent otpIntent=new Intent(UserRegistrationPopupNew.this,UserOTPpopup.class);
				otpIntent.putExtra("FROMACTIVITY", mFromActivity);
				otpIntent.putExtra("MANUFACTURERID", mManufacturerId);
				otpIntent.putExtra("PRODUCTID", mProductID);
				otpIntent.putExtra("USEREMAIL", mUserEmail);
				otpIntent.putExtra("MOBILE", mUserMobile);
				otpIntent.putExtra("USEROTP", mOTP);
				otpIntent.putExtra("USERPASSWORD", mUserPassword);
				startActivity(otpIntent);
				overridePendingTransition(0, 0);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Please try agin", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private Boolean sendOTP() {
		Boolean isValid=false;
		webServices = new WebServices();
		jsonParserClass = new JsonParserClass();
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			String postURL = webServices.SendEmailTemplate;//"http://vsuppliervm.cloudapp.net:50666/EmailService.svc/SendEmailTemplate";
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
				mOTP=response[0];
				isValid = true;
			}else{
				isValid = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
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

}
