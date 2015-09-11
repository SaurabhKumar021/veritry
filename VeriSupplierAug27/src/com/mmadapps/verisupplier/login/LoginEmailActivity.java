package com.mmadapps.verisupplier.login;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.UserOTPpopup;
import com.mmadapps.verisupplier.UserSignPopup;
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
import com.mmadapps.verisupplier.beans.WishlistDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.customs.Encrypt;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.customservices.CustomCreateQuotation;
import com.mmadapps.verisupplier.dashboard.DashboardActivity;
import com.mmadapps.verisupplier.details.GetQuotationActivityNew;
import com.mmadapps.verisupplier.details.MoreInfoActivity;
import com.mmadapps.verisupplier.details.MoreInfoActivityNew;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.mmadapps.verisupplier.inspection.InspectionCreateQuotation;
import com.mmadapps.verisupplier.leftmenu.Myopenquoate;
import com.mmadapps.verisupplier.leftmenu.WishListActivity;
import com.mmadapps.verisupplier.placeorder.PlaceorderShipping;
import com.mmadapps.verisupplier.products.ProductCreateQuotationActivity;
import com.mmadapps.verisupplier.shipping.ShippingCreateQuotation;
import com.mmadapps.verisupplier.warehouse.WarehouseCreateQuotation;

public class LoginEmailActivity extends ActionBarActivity implements OnClickListener {
	
	//intent
	String mClicked;
	String mFromActivity;
	String mManufacturerId;
	String mProductID="";
	
	EditText vE_ase_emailaddrs,vE_ase_password,vE_ase_cnfrmpassword,vE_ase_fname,vE_ase_mobnum;
	TextView vT_ase_signup,vT_ase_loginText,vT_ase_loginleftText,vT_ase_forgotText,login_titel_text;
	LinearLayout vT_ase_loginlayout;
	LinearLayout vE_ase_signuplayout;
	TextView vT_ase_login;
	
	//service call
	ProgressDialog pdLoading=null;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	
	//String Variables
	String mUserEmail;
	String mUserPassword,mUserMobile;
	String Inparam;
	String regusermobile,reguserconpass,regusername,reguserpassword;
	 
	Boolean isFirst=false;
	View password_downview;
	 
	//from database 
	UserDetails mUserDetail=new UserDetails();
	List<CategoryDetails> mCategoryDetails=new ArrayList<CategoryDetails>();
	ArrayList<QuoatationDashboard> quoatationDashboards;
	QuickOrderResult mQuotationDetails=new QuickOrderResult();
	List<Lookups> mInspectionType=new ArrayList<Lookups>();
	List<Lookups> mIndustryList=new ArrayList<Lookups>();
	List<Lookups> mInspectorList=new ArrayList<Lookups>();
	List<Lookups> mUnitOfMeasurentList=new ArrayList<Lookups>();
	List<Lookups> mQuotationLookupList=new ArrayList<Lookups>();
	List<Lookups> mMoreinfoLookupList=new ArrayList<Lookups>();
	List<Lookups> mMoreinfoSubjectList=new ArrayList<Lookups>();
	
	//service call after 24 hours time differance
	final double DIFFTIME=8.64e+7;
	
	List<CountryDetails> mCounList=new ArrayList<CountryDetails>();
	List<CountryDetails> mStateList=new ArrayList<CountryDetails>();

	//email validtion
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX =  Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);	 
		 
	//encription key 
	private String KEY="1429111390460322";
	static String IV="90460322";
	
	//fro OTP value
	String OTP = "";
	public static String mOTP="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_email);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getActionBar().hide();
	
		//from Intent
		mClicked = getIntent().getStringExtra("CLICKED");
		mFromActivity=getIntent().getStringExtra("FROMACTIVITY");
		Log.e("From Activit", mFromActivity);
		mManufacturerId=getIntent().getStringExtra("MANUFACTURERID");
		mProductID=getIntent().getStringExtra("PRODUCTID");
		
		initializeValues();
		 
		if(mClicked.equalsIgnoreCase("login")){
			vT_ase_signup.setVisibility(View.VISIBLE);
			vT_ase_loginText.setVisibility(View.GONE);
			vE_ase_signuplayout.setVisibility(View.GONE);
		}else if(mClicked.equalsIgnoreCase("register")){
			vT_ase_signup.setVisibility(View.GONE);
			vT_ase_loginText.setVisibility(View.VISIBLE);
			vE_ase_signuplayout.setVisibility(View.VISIBLE);
		}	
	}

	

	private void initializeValues() {
		//Login Button
		vT_ase_loginlayout = (LinearLayout) findViewById(R.id.vT_ase_loginlayout);
		vT_ase_loginlayout.setOnClickListener(this);
		vT_ase_login = (TextView) findViewById(R.id.vT_ase_login);
		
		vE_ase_signuplayout = (LinearLayout) findViewById(R.id.vE_ase_signuplayout);
		vE_ase_emailaddrs=(EditText) findViewById(R.id.vE_ase_emailaddrs);
		vE_ase_password=(EditText) findViewById(R.id.vE_ase_password);
		
		vE_ase_cnfrmpassword = (EditText) findViewById(R.id.vE_ase_cnfrmpassword);
		vE_ase_fname = (EditText) findViewById(R.id.vE_ase_fname);
		vE_ase_mobnum = (EditText) findViewById(R.id.vE_ase_mobnum);
		
		//TextView Login and Signup
		vT_ase_loginText = (TextView) findViewById(R.id.vT_ase_loginText);
		vT_ase_loginText.setOnClickListener(this);
		
		vT_ase_signup = (TextView) findViewById(R.id.vT_ase_signup);
		vT_ase_signup.setOnClickListener(this);
		
		vT_ase_loginleftText=(TextView)findViewById(R.id.vT_ase_loginleftText);
		vT_ase_loginleftText.setOnClickListener(this);
		
		vT_ase_forgotText=(TextView)findViewById(R.id.vT_ase_forgotText);
		vT_ase_forgotText.setOnClickListener(this);
		
		login_titel_text=(TextView)findViewById(R.id.login_titel_text);
		password_downview=(View)findViewById(R.id.password_downview);
		
		TextChanger();
	}
	
	private void TextChanger() {
		vE_ase_emailaddrs.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mUserEmail=vE_ase_emailaddrs.getText().toString().trim();
                mUserPassword = vE_ase_password.getText().toString().trim();
                
                if(mClicked.equalsIgnoreCase("login")){
                if(mUserEmail.length()==0 || mUserPassword.length()==0){
                    vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
                }else{
                    vT_ase_login.setBackgroundColor(Color.rgb(255, 90, 96));
                }
                }else if(mClicked.equalsIgnoreCase("register")){
                    getAllreginformations();
                }else if(mClicked.equalsIgnoreCase("forget")){
                    if(mUserEmail.length()==0){
                        vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
                    }else{
                        vT_ase_login.setBackgroundColor(Color.rgb(255, 90, 96));
                    }
                }
                
            }
            
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                
                
            }
            
            @Override
            public void afterTextChanged(Editable arg0) {
            
                
            }
        });
        
        
        vE_ase_password.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserEmail=vE_ase_emailaddrs.getText().toString().trim();
                mUserPassword=vE_ase_password.getText().toString().trim();
                
                if(mClicked.equalsIgnoreCase("login")){
                if(mUserPassword.length()==0 ||  mUserEmail.length()==0){
                    vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
                }else{
                    vT_ase_login.setBackgroundColor(Color.rgb(255, 90, 96));
                }
                } else if(mClicked.equalsIgnoreCase("register")){
                    getAllreginformations();
                } else if(mClicked.equalsIgnoreCase("")){
                    
                }
                
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                
                
            }
        });
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		  case R.id.vT_ase_signup:
	            login_titel_text.setText("Sign-up");
	            vT_ase_forgotText.setVisibility(View.GONE);
	            
	            
	            if(vT_ase_loginleftText.getVisibility()==View.VISIBLE){
	                vT_ase_loginleftText.performClick();
	            }
	            vE_ase_signuplayout.setVisibility(View.VISIBLE);
	            vT_ase_loginText.setVisibility(View.VISIBLE);
	            vT_ase_signup.setVisibility(View.GONE);
	            
	            
	            Animation animation   =   AnimationUtils.loadAnimation(this, R.anim.slide_down);
	            animation.setDuration(500);
	            vE_ase_signuplayout.setAnimation(animation);
	            vE_ase_signuplayout.animate();
	            animation.start();
	            vT_ase_login.setText("Sign Up");
	            mClicked = "register";
	            
	            vE_ase_emailaddrs.setText("");
	            vE_ase_password.setText("");
	            vE_ase_cnfrmpassword.setText("");
	            vE_ase_fname.setText("");
	            vE_ase_mobnum.setText("");
	            break;
	            
		  case R.id.vT_ase_loginText:    
	            login_titel_text.setText("LogIn with email");
	            vT_ase_forgotText.setVisibility(View.VISIBLE);
	            vT_ase_loginText.setVisibility(View.GONE);
	            vT_ase_signup.setVisibility(View.VISIBLE);
	            
	            
	            Animation animation1   =    AnimationUtils.loadAnimation(this, R.anim.slide_up);
	            animation1.setDuration(500);
	            vE_ase_signuplayout.setAnimation(animation1);
	            vE_ase_signuplayout.animate();
	            animation1.start();
	            
	            
	            vE_ase_signuplayout.setVisibility(View.GONE);
	            vT_ase_login.setText("Login");
	            mClicked = "login";
	            
	            vE_ase_emailaddrs.setText("");
	            vE_ase_password.setText("");
	            
	            break;
	            
		  case R.id.vT_ase_loginlayout:
	            if(mClicked.equalsIgnoreCase("login")){
	                //check whether useremail and password is null
	                if(mUserEmail==null || mUserEmail.length()==0 || mUserPassword==null || mUserPassword.length()==0){
	                    //Toast.makeText(getApplicationContext(), "Please enter the email and password", Toast.LENGTH_SHORT).show();
	                }else if(mUserPassword==null || mUserPassword.length()==0){
	                    //Toast.makeText(getApplicationContext(), "Please enter the password", Toast.LENGTH_SHORT).show();
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
	                            
	                            if(!isValiedID(mUserEmail)){
	                                vE_ase_emailaddrs.setError("Please enter vaild EmailId");
	                            }else{
	                            
	                            //to validate user
	                            new AsyncUserLogin().execute();
	                            }
	                        }else{
	                            Toast.makeText(getApplicationContext(), "Please check internet", Toast.LENGTH_SHORT).show();
	                        }
	                    }catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	            }else if(mClicked.equalsIgnoreCase("register")){
	                getAllreginformations();
	            }
	            else if(mClicked.equalsIgnoreCase("forget")){
	                if(mUserEmail==null || mUserEmail.length()==0){
	                  vE_ase_emailaddrs.setError("please enter email");
	                }else{
	                    if(!isValiedID(mUserEmail)){
	                        vE_ase_emailaddrs.setError("Please enter vaild EmailId");
	                    }else{
	                        new AsyncForgetPassword().execute();
	                        finish();
	                    }
	                }    
	            }
	            break;
			
		  case R.id.vT_ase_forgotText:
	            if(vT_ase_loginText.getVisibility()==View.VISIBLE){
	                vT_ase_loginText.performClick();
	            }
	            vT_ase_login.setText("Send");
	            vT_ase_loginleftText.setVisibility(View.VISIBLE);
	            vT_ase_forgotText.setVisibility(View.GONE);
	            vE_ase_password.setVisibility(View.GONE);
	            password_downview.setVisibility(View.GONE);
	            login_titel_text.setText("Forgot Password");
	            /*Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_up);
	            animation2.setDuration(300);
	            vE_ase_password.setAnimation(animation2);
	            vE_ase_password.animate();
	            animation2.start();*/
	            mClicked = "forget";
	            
	            vE_ase_emailaddrs.setText("");
	            break;
            
		  case R.id.vT_ase_loginleftText:
	             isFirst = false;    
	             vT_ase_login.setText("Login");
	             vT_ase_loginleftText.setVisibility(View.GONE);
	             vT_ase_forgotText.setVisibility(View.VISIBLE);
	             vE_ase_password.setVisibility(View.VISIBLE);
	             password_downview.setVisibility(View.VISIBLE);
	             login_titel_text.setText("LogIn with email");
	             
	             
	             Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.slide_down);
	             animation3.setDuration(300);
	             vE_ase_password.setAnimation(animation3);
	             vE_ase_password.animate();
	             animation3.start();
	             mClicked = "login";
	             
	            vE_ase_emailaddrs.setText("");
	             vE_ase_password.setText("");
	             
	             break;
	             
		  case R.id.vI_login_backview:
	             finish();
          break;
            
		default:
			break;
		}
	}
	
	private void getAllreginformations() {
        
        regusermobile=""+vE_ase_mobnum.getEditableText().toString().trim();
        reguserconpass=""+vE_ase_cnfrmpassword.getEditableText().toString().trim();
        regusername = ""+vE_ase_fname.getEditableText().toString().trim();
        
        Log.e("", ""+reguserconpass);
        Log.e("name", regusername);
        Log.e("mobile", regusermobile);
        
       /*if(reguseremail == null || reguseremail.length()==0){
           vE_ase_emailaddrs.setError("please enter email");
       }*/
       
       vE_ase_emailaddrs.addTextChangedListener(new TextWatcher() {
           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               
               if(mUserEmail.length()!=0){
                   if(mUserPassword.length()==0 || reguserconpass.length()==0 || regusermobile.length()==0 || regusername.length()==0){
                       vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
                   }else{
                       vT_ase_login.setBackgroundColor(Color.rgb(255, 90, 96));
                   }
                   
               }else{
                   vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
               }
               vE_ase_emailaddrs.setError(null);
           }
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count,int after) {
           }
           @Override
           public void afterTextChanged(Editable s) {
           }
       });
       
       /*if (regusermobile == null || regusermobile.length() == 0){
           vE_ase_mobnum.setError("Please enter mobilenumber");
       }*/
       
       vE_ase_mobnum.addTextChangedListener(new TextWatcher() {
           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               vE_ase_mobnum.setError(null);
               regusermobile=""+vE_ase_mobnum.getEditableText().toString().trim();
               if(regusermobile.length()!=0){
                   if(mUserPassword.length()==0 || reguserconpass.length()==0 || mUserEmail.length()==0 || regusername.length()==0){
                       vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
                   }else{
                       vT_ase_login.setBackgroundColor(Color.rgb(255, 90, 96));
                   }
               }else{
                   vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
               }
           }
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count,int after) {
           }
           @Override
           public void afterTextChanged(Editable s) {
               
           }
       });
   /*    
       if (reguserpassword == null || reguserpassword.length() == 0){
           vE_ase_password.setError("please enter password");
       }*/
       
       vE_ase_password.addTextChangedListener(new TextWatcher() {
           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(mUserPassword.length()!=0){
                   if(mUserEmail.length()==0 || reguserconpass.length()==0 || regusermobile.length()==0 || regusername.length()==0){
                       vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
                   }else{
                       vT_ase_login.setBackgroundColor(Color.rgb(255, 90, 96));
                   }
                   
               }else{
                   vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
               }
               vE_ase_password.setError(null);
           }
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count,int after) {
           }
           @Override
           public void afterTextChanged(Editable s) {
           }
       });
       
       vE_ase_cnfrmpassword.addTextChangedListener(new TextWatcher() {
           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               reguserconpass=""+vE_ase_cnfrmpassword.getEditableText().toString().trim();
               if(reguserconpass.length()!=0){
                   if(mUserPassword.length()==0 || mUserEmail.length()==0 || regusermobile.length()==0 || regusername.length()==0){
                       vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
                   }else{
                       vT_ase_login.setBackgroundColor(Color.rgb(255, 90, 96));
                   }
                   
               }else{
                   vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
                   
               }
               vE_ase_cnfrmpassword.setError(null);
           }
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count,int after) {
           }
           @Override
           public void afterTextChanged(Editable s) {
           }
       });
       
       
       vE_ase_fname.addTextChangedListener(new TextWatcher() {
           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                regusername = ""+vE_ase_fname.getEditableText().toString().trim();
               if(regusername.length()!=0){
                   if(mUserPassword.length()==0 || reguserconpass.length()==0 || regusermobile.length()==0 || mUserEmail.length()==0){
                       vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
                   }else{
                       vT_ase_login.setBackgroundColor(Color.rgb(255, 90, 96));
                   }
                   
               }else{
                   vT_ase_login.setBackgroundColor(Color.parseColor("#FFD7D8"));
               }
               vE_ase_fname.setError(null);
           }
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count,int after) {
           }
           @Override
           public void afterTextChanged(Editable s) {
           }
       });
       
       
       if (mUserEmail == null || mUserEmail.length() == 0 || 
               regusermobile == null || regusermobile.length() == 0 ||
                       mUserPassword == null || mUserPassword.length() == 0||
                               reguserconpass == null || reguserconpass.length() == 0) {
           
           //Toast.makeText(getApplicationContext(), "please fill all details",Toast.LENGTH_SHORT).show();
       }else if(!isValiedID(mUserEmail)){
           vE_ase_emailaddrs.setError("Please enter vaild EmailId");
       }else if(!isValidPhoneNumber(regusermobile)){
           vE_ase_mobnum.setError("please enter valid contact number");
       }else{
           mUserEmail=vE_ase_emailaddrs.getEditableText().toString().trim();
           mUserMobile=vE_ase_mobnum.getText().toString().trim();
           if (mUserPassword.length() == reguserconpass.length() && reguserconpass.equals(mUserPassword)){
               try{
                   //password encryption
                   mUserPassword = new Encrypt().encryptText(mUserPassword,KEY,IV);
                   reguserpassword=mUserPassword;
               }catch(Exception e){
                   e.printStackTrace();
               }
               //service call to check is user exist
               CallServices1();
           }else{
               vE_ase_cnfrmpassword.setError("Both passwords should match");
           }
       }
   }
	
	
	/*//email validation
			private boolean isValiedID(String email) {
				return email.matches(EMAIL_REGEX);
			}*/
			
			public static boolean isValiedID(String emailStr) {
                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
                return matcher.find();
             }
			
			//phone number validation
			public static final boolean isValidPhoneNumber(CharSequence target) {
				if (target.length() != 10) {
					return false;
				} else {
					return android.util.Patterns.PHONE.matcher(target).matches();
				}
			}
			
			private void CallServices1() {
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
					pdLoading = new ProgressDialog(LoginEmailActivity.this);
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
						   Animation animation1   =    AnimationUtils.loadAnimation(LoginEmailActivity.this, R.anim.slide_up);
						   animation1.setDuration(500);
						   vE_ase_signuplayout.setAnimation(animation1);
						   vE_ase_signuplayout.animate();
						   animation1.start();
						   
						   
						   vT_ase_loginleftText.setVisibility(View.GONE);
						   vT_ase_forgotText.setVisibility(View.VISIBLE);
						   vE_ase_password.setVisibility(View.VISIBLE);
						   password_downview.setVisibility(View.VISIBLE);
						   login_titel_text.setText("LogIn with email");
				            
				            vE_ase_signuplayout.setVisibility(View.GONE);
				            vT_ase_login.setText("Login");
				            mClicked = "login";
				            
				            vE_ase_emailaddrs.setText("");
				            vE_ase_password.setText("");
					   
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
					pdLoading = new ProgressDialog(LoginEmailActivity.this);
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
						Intent otpIntent=new Intent(LoginEmailActivity.this,UserOTPpopup.class);
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
					httpPost.setHeader("JWTKEY","Verisupplier.android.d2335fhfg4564hghjghjghjget45ert.1.0");
					httpPost.setHeader("OS","ANDROID");
					httpPost.setHeader("USERID",mUserDetail.getmUserID());
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
	
	
	
	private class AsyncUserLogin extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(LoginEmailActivity.this);
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
				callServicesForNextPage();
			}else{
				vE_ase_password.setText("");
				Toast.makeText(getApplicationContext(), "Invalid user credential", Toast.LENGTH_SHORT).show();
			}
			
			
			/*if(result){
				new AsyncUserDetails().execute();
			}else{
				
			}*/
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
			UserDetails mUserDetails=new UserDetails();
			mUserDetails=jsonParserClass.parseUserAuthentication(resultOutParam,getApplicationContext());
			if(mUserDetails.getmUserID()==null){
				isValid=false;
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertUserDetails(mUserDetails, true);
				helper.close();
				isValid=true;
			}
		}
		return isValid;
	}
	
	 public void callServicesForNextPage() {
		 new AsyncUserDetails().execute();
	 }
  



private class AsyncUserDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(LoginEmailActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			//get the userdetails
			//boolean isvalidUser = isValidUserDetails();
				//from  product detail page getprice onclick
				if(mFromActivity.equalsIgnoreCase("getprice")){
					getQuantityList();
					return getManufacturerDetaisl();
				}
				//from product detail page getquotation onclick
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
				else if(mFromActivity.equalsIgnoreCase("moreinfo")){
					getManufacturerDetaisl();
					getSubjectDetails();
					getMoreInfoLookups();
					return true;
				}
				//from product detail page placeorder onclick
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
				}
				else if(mFromActivity.equalsIgnoreCase("userfeedback")){
					return true;
				}
				else if(mFromActivity.equalsIgnoreCase("wishlist")){
					return true;
				}
				else if(mFromActivity.equalsIgnoreCase("wishlistproduct")){
					return true;
				}
				//from product detail page moreinfo onclick
				else{
					getManufacturerDetaisl();
					getSubjectDetails();
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
					Intent getPriceIntent=new Intent(LoginEmailActivity.this,GetQuotationActivityNew.class);
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
				else if(mFromActivity.equalsIgnoreCase("moreinfo")){
					forwardToMoreInfoActivity();
				}
				//to go placeorder activity
				else if(mFromActivity.equalsIgnoreCase("overviewplaceorder")){
					forwardToPlaceOrderActivity();
					/*Intent placeorderIntent=new Intent(LoginEmailActivity.this,PlaceorderShipping.class);
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
				else if (mFromActivity.equalsIgnoreCase("tradedashboard")) {
					getQuantityList();
					LastDownloadTime();
					
					Intent intent=new Intent(LoginEmailActivity.this,com.mmadapps.verisupplier.trademanager.DashboardActivity.class);
					startActivity(intent);
					finish();
					
				}
				//to get signin from marketplace usermenu signin
				else if(mFromActivity.equalsIgnoreCase("signin")){
					//BaseActionBarActivityNew.signin_text.setText("Log out");
					finish();
				}
				else if(mFromActivity.equalsIgnoreCase("wishlist")){
					UserDetails();
					 new AsyncGetWhishList().execute();
					//finish();
				}
				else if(mFromActivity.equalsIgnoreCase("wishlistproduct")){
					UserDetails();
					new AsyncCreateWishList().execute();
				}
				else if(mFromActivity.equalsIgnoreCase("userfeedback")){
					
					finish();
				}
				//to go to placeorder activity
				else if(mFromActivity.equalsIgnoreCase("placeorder")){
					forwardToPlaceOrderActivity();
					/*PlaceorderShipping.mFromActivity="PD";
					PlaceorderShipping.mProductId=mProductID;
					PlaceorderShipping.mManufacturerId=mManufacturerId;
					Intent placeOrderIntent = new Intent(LoginEmailActivity.this, PlaceorderShipping.class);
					startActivity(placeOrderIntent);
					overridePendingTransition(0, 0);
					finish();*/
				}
				//to go moreinfo activity
				else {
					Intent placeorderIntent=new Intent(LoginEmailActivity.this,MoreInfoActivity.class);
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
					Toast.makeText(LoginEmailActivity.this, "error in signup", Toast.LENGTH_SHORT).show();
				}
			}
		}
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
   
   private void forwardToPlaceOrderActivity() {
		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			PlaceorderShipping.mFromActivity="PD";
			Intent placeOrderIntent = new Intent(LoginEmailActivity.this, PlaceorderShipping.class);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);
			finish();
			
			
			/*Intent placeOrderIntent = new Intent(LoginEmailActivity.this, ProductCreateQuotationActivity.class);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);
			finish();*/
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
			Intent getQuotationIntent = new Intent(LoginEmailActivity.this, ShippingCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
			/*PlaceorderShipping.mFromActivity="PD";
			PlaceorderShipping.mProductId=mProductId;
			PlaceorderShipping.mManufacturerId=mManufacturerId;
			Intent placeOrderIntent = new Intent(ProductDetailsActivity.this, PlaceOrderShippingActivity.class);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);*/
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
			Intent getQuotationIntent = new Intent(LoginEmailActivity.this, CustomCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
			/*Intent placeOrderIntent = new Intent(ProductDetailsActivity.this, PlaceOrderCustomsActivity.class);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);*/
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
			Intent getQuotationIntent = new Intent(LoginEmailActivity.this, InspectionCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
			/*Intent placeOrderIntent = new Intent(ProductDetailsActivity.this, PlaceOrderInspectionActivity.class);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);*/
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
			Intent getQuotationIntent = new Intent(LoginEmailActivity.this, WarehouseCreateQuotation.class);
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
 	
 	
 	private void forwardTogetQuotationActivity() {
 		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			Intent getQuotationIntent = new Intent(LoginEmailActivity.this, ProductCreateQuotationActivity.class);
			getQuotationIntent.putExtra("FROMACTIVITY", mFromActivity);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
			Intent getQuotationIntent = new Intent(LoginEmailActivity.this, InspectionCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}
		else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
			Intent getQuotationIntent = new Intent(LoginEmailActivity.this, ShippingCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
			Intent getQuotationIntent = new Intent(LoginEmailActivity.this, CustomCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
			Intent getQuotationIntent = new Intent(LoginEmailActivity.this, WarehouseCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
			finish();
		}
	}
 	
 	public void forwardToMoreInfoActivity() {
		Intent moreInfoIntent = new Intent(LoginEmailActivity.this, MoreInfoActivityNew.class);
		startActivity(moreInfoIntent);
		overridePendingTransition(0, 0);
		finish();
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
				Intent homeIntent=new Intent(LoginEmailActivity.this,Myopenquoate.class);
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
 	
 	
 	private void UserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetail=helper.getUserDetails();
		helper.close();
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
 	
 	
 	public class AsyncDashboard extends AsyncTask<String, Void, Boolean> {
		Boolean IsQuotation = false;
		Boolean Isorder = false;
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(LoginEmailActivity.this);
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
				Intent dashboardIntent = new Intent(LoginEmailActivity.this, DashboardActivity.class);
				dashboardIntent.putExtra("Type", mFromActivity);
				startActivity(dashboardIntent);
				overridePendingTransition(0, 0);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "Hello Exception",Toast.LENGTH_LONG).show();
			}
		}
	}
 	
 	
 	private void DeleteAllTables() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		helper.deleteAllTables();
		helper.close();
	}
 	
 	
 private class AsyncProductListDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdLoading = new ProgressDialog(LoginEmailActivity.this);
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
			Intent homeIntent=new Intent(LoginEmailActivity.this,Myopenquoate.class);
			startActivity(homeIntent);
			overridePendingTransition(0, 0);
			finish();
		}
		
	}

/* private void getAllOrderList() {
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
}*/

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
	 Boolean isValid = false;
		if (isValid.equals(false)) {
			if(mFromActivity.equalsIgnoreCase("dashboard")){
				if(mUserDetail.getmUserType()==null || mUserDetail.getmUserType().length()==0){
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
            pdLoading = new ProgressDialog(LoginEmailActivity.this);
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
        	}
        	if(result){
        		   Intent homeIntent=new Intent(LoginEmailActivity.this,WishListActivity.class);
                   startActivity(homeIntent);
                   overridePendingTransition(0, 0);
                   finish();
        	}else{
                Toast.makeText(getApplicationContext(), "No WishList", Toast.LENGTH_LONG).show();
        	}
        }
	}
	
	private Boolean getWhishList() {
		Boolean isValid=false;
		List<WishlistDetails> mWishDetails=new ArrayList<WishlistDetails>();
		String inParam = ""+mUserDetail.getmUserID()+"/"+"1"+"/"+"20";
		String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetWishList, "GetWishList/", inParam);
		if (resultOutparam == null) {
			isValid=false;
		}else {
			mWishDetails=jsonParserClass.parseWishListDetails(resultOutparam,getApplicationContext());
			if(mWishDetails==null || mWishDetails.size()==0){
			}else{
				Helper helper = new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertwhistlistdetails(mWishDetails);
				helper.close();
				isValid=true;
			}
		}
		return isValid;
	}
	
	public class AsyncCreateWishList extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            pdLoading = new ProgressDialog(LoginEmailActivity.this);
            pdLoading.setMessage("Please wait...");
            pdLoading.show();
            pdLoading.setCancelable(false);
            pdLoading.setCanceledOnTouchOutside(false);
            super.onPreExecute();
        }
        
        
        @Override
        protected Boolean doInBackground(String... params) {
            return createWishList();
        }
    
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (pdLoading != null && pdLoading.isShowing()) {
                pdLoading.cancel();
            }
            if(result){
                Toast.makeText(getApplicationContext(), "Product is added to wishlist", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Product is not added to wishlist", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        
    }
	
	 private Boolean createWishList() {
		 Boolean isValid=false;
		 webServices=new WebServices();
		 jsonParserClass=new JsonParserClass();
		 String mResult = null;
		 String inParam =mProductID+"/"+mUserDetail.getmUserID();
		 String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetWishList, "CreateUpdateWishlist/", inParam);
		 if(resultOutparam==null || resultOutparam.length()==0){
			 isValid = false;
		 }else{
			 mResult = jsonParserClass.parseCreateWishlist(resultOutparam);
			 if(mResult==null){
				 isValid=false;
			 }else{
				 isValid=true;
			 }
		 }
		 return isValid;
	 }
	 
	 public class AsyncForgetPassword extends AsyncTask<String, Void, Boolean>{
	        
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pdLoading = new ProgressDialog(LoginEmailActivity.this);
	            pdLoading.setMessage("Please wait...");
	            pdLoading.show();
	            pdLoading.setCancelable(false);
	            pdLoading.setCanceledOnTouchOutside(false);
	        }
	        
	        

	        @Override
	        protected Boolean doInBackground(String... params) {
	            return getForgetPassword();
	        }
	        
	        
	        @Override
	        protected void onPostExecute(Boolean result) {
	            super.onPostExecute(result);
	            if(result){
	                Toast.makeText(getApplicationContext(), "Your password will sent shortly to registered Email Id", Toast.LENGTH_LONG).show();
	                pdLoading.isShowing();
	                pdLoading.cancel();
	            }else{
	                pdLoading.isShowing();
	                pdLoading.cancel();
	            }
	        }
	        
	    }
	    
	    

	    private Boolean getForgetPassword() {
	        webServices = new WebServices();
	        jsonParserClass = new JsonParserClass();
	         Boolean isValid=false;
	            String mResponseObject=null;
	            String inParam = mUserEmail;
	            String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.UserDetails, "GetforgotPassword/", inParam);
	            if (resultOutparam == null) {
	                isValid=false;
	            }else {
	                mResponseObject=jsonParserClass.parseForgotPassword(resultOutparam);
	                if(mResponseObject==null || mResponseObject.length()==0){
	                    isValid=false;
	                }else{
	                    isValid=true;    
	                }
	            }
	            return isValid;
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

}
