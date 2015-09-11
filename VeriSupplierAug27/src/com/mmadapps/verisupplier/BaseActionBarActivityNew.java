package com.mmadapps.verisupplier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.QuoatationDashboard;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.dashboard.DashboardActivity;
import com.mmadapps.verisupplier.leftmenu.Myopenquoate;
import com.mmadapps.verisupplier.leftmenu.WishListActivity;

public class BaseActionBarActivityNew extends ActionBarActivity{
	
	LinearLayout home_layout,wishlist_layout,dashboard_layout,openquote_layout,userprofile_layout;
	//user profile dialog
	private static Point mPoint;
	private static int[] location = new int[2];
	private int mHightofView=0;
	PopupWindow userMenu;
	
	UserDetails mUserDetails=new UserDetails();
	
	private static Typeface segeo_Bold;
	private static Typeface segeo_Regular;
	public static Typeface getSegeo_Bold() {
		return segeo_Bold;
	}

	public static void setSegeo_Bold(Typeface segeo_Bold) {
		BaseActionBarActivityNew.segeo_Bold = segeo_Bold;
	}

	public static Typeface getSegeo_Regular() {
		return segeo_Regular;
	}

	public static void setSegeo_Regular(Typeface segeo_Regular) {
		BaseActionBarActivityNew.segeo_Regular = segeo_Regular;
	}
	
	private static Typeface montserrat_Bold;
	
	public static Typeface getMontserrat_Bold() {
		return montserrat_Bold;
	}

	public static void setMontserrat_Bold(Typeface montserrat_Bold) {
		BaseActionBarActivityNew.montserrat_Bold = montserrat_Bold;
	}

	//progress loading
	private ProgressDialog pdLoading=null;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	String Inparam;
	
	//from data base
	ArrayList<QuoatationDashboard> quoatationDashboards;
	List<CategoryDetails> mCategoryDetails=new ArrayList<CategoryDetails>();
	final double DIFFTIME=8.64e+7;
	
	public static ImageView homeselected,home_notselected,wishselected,wish_notselected,dashboardselected,dashboard_notselected,open_selected,open_notselected,user_selected,user_notselected;
	public static TextView signin_text,useremail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar_activity_new);
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					segeo_Bold = Typeface.createFromAsset(getAssets(),"segeo-wp-bold.ttf");
					//Sui_Light = Typeface.createFromAsset(getAssets(),"segeouil.ttf");
					segeo_Regular=Typeface.createFromAsset(getAssets(), "segeo-wp.ttf");
					//montserrat_Bold=Typeface.createFromAsset(getAssets(), "Montserrt-Bold.ttf");
				} catch (Exception e) {

				}
			}
		};
		new Thread(runnable).start();
		
		initializwView();
	}

	private void initializwView() {
		home_layout=(LinearLayout)findViewById(R.id.vL_actionbar_home);
		wishlist_layout=(LinearLayout)findViewById(R.id.vL_actionbar_wishlist);
		dashboard_layout=(LinearLayout)findViewById(R.id.vL_actionbar_dashboard);
		openquote_layout=(LinearLayout)findViewById(R.id.vL_actionbar_openquote);
		userprofile_layout=(LinearLayout)findViewById(R.id.vL_actionbar_profile);
		
		homeselected=(ImageView)findViewById(R.id.vI_ab_homeselected);
		home_notselected=(ImageView)findViewById(R.id.vI_ab_home_notselected);
		wishselected=(ImageView)findViewById(R.id.vI_ab_wishselected);
		wish_notselected=(ImageView)findViewById(R.id.vI_ab_wish_notselected);
		dashboardselected=(ImageView)findViewById(R.id.vI_ab_dashboardselected);
		dashboard_notselected=(ImageView)findViewById(R.id.vI_ab_dashboard_notselected);
		open_selected=(ImageView)findViewById(R.id.vI_ab_openquoteselected);
		open_notselected=(ImageView)findViewById(R.id.vI_ab_openquote_notselected);
		user_selected=(ImageView)findViewById(R.id.vI_ab_userselected);
		user_notselected=(ImageView)findViewById(R.id.vI_ab_user_notselected);
		
		wishlist_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				homeselected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				
				wishselected.setVisibility(View.VISIBLE);
				wish_notselected.setVisibility(View.GONE);
				Intent wishIntent=new Intent(BaseActionBarActivityNew.this,WishListActivity.class);
				startActivity(wishIntent);
				overridePendingTransition(0, 0);
			}
		});
		
		userprofile_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*homeselected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				
				user_selected.setVisibility(View.VISIBLE);
				user_notselected.setVisibility(View.GONE);*/
				
				getUserDetails();
				location = new int[2];
				v.getLocationOnScreen(location);
				onWindowFocusChanged(true);
				ShowUserProfileBox(BaseActionBarActivityNew.mPoint);
				
			}
		});
		
		dashboard_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				homeselected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				
				dashboardselected.setVisibility(View.VISIBLE);
				dashboard_notselected.setVisibility(View.GONE);
				
				getUserId();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(BaseActionBarActivityNew.this,UserSignPopup.class);
					signinIntent.putExtra("FROMACTIVITY", "marketdashboard");
					startActivity(signinIntent);
					overridePendingTransition(0, 0);
				}else{
					new AsyncDashboard().execute();
				}
			}
		});
		
		openquote_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				homeselected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				
				open_selected.setVisibility(View.VISIBLE);
				open_notselected.setVisibility(View.GONE);
				
				getUserId();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(BaseActionBarActivityNew.this,UserSignPopup.class);
					signinIntent.putExtra("FROMACTIVITY", "marketopenquote");
					startActivity(signinIntent);
					overridePendingTransition(0, 0);
				}else{
					new AsyncCategoryDetails().execute();
				}
			}
		});
	}
	
	private void getUserId() {
		mUserDetails=new UserDetails();
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}
	
	private void getUserDetails() {
		
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
		
	}
	
	public void ShowUserProfileBox(Point mPoint) {
		ScrollView viewGroup = (ScrollView) findViewById(R.id.menuleft_window);
		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View layout = layoutInflater.inflate(R.layout.left_menu, viewGroup);
		   
		userMenu = new PopupWindow(this);
		userMenu.setContentView(layout);
		userMenu.setWidth(LayoutParams.WRAP_CONTENT);
		userMenu.setHeight(LayoutParams.WRAP_CONTENT);
		userMenu.setFocusable(true);
		// Clear the default translucent background
		userMenu.setBackgroundDrawable(new BitmapDrawable());
		userMenu.setAnimationStyle(R.style.animations_rightpopup); 
		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
		         mHightofView = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
		} 
		int x = mPoint.x;
		int y = mPoint.y+mHightofView-20;
		userMenu.showAtLocation(layout, Gravity.END|Gravity.TOP, -x,y);
		//userMenu.showAtLocation(layout, Gravity.LEFT|Gravity.TOP+50, -mPoint.x, mPoint.y+mHightofView);
		
		final TextView username_text=(TextView)layout.findViewById(R.id.vT_leftmenu_username);
		useremail=(TextView)layout.findViewById(R.id.vT_leftmenu_useremail);
		final TextView search_text=(TextView)layout.findViewById(R.id.vT_leftmenu_search);
		final TextView home_text=(TextView)layout.findViewById(R.id.vT_leftmenu_home);
		final TextView myverisupplier_text=(TextView)layout.findViewById(R.id.vT_leftmenu_myverisupplier);
		final TextView wishlist_text=(TextView)layout.findViewById(R.id.vT_leftmenu_wishlist);
		//messages_text=(TextView)layout.findViewById(R.id.vT_leftmenu_messages);
		final TextView setting_text=(TextView)layout.findViewById(R.id.vT_leftmenu_setting);
		final TextView appfeedback_text=(TextView)layout.findViewById(R.id.vT_leftmenu_appfeedback);
		final TextView onlinesupport_text=(TextView)layout.findViewById(R.id.vT_leftmenu_onlinesupport);
		final TextView helpcenter_text=(TextView)layout.findViewById(R.id.vT_leftmenu_helpcenter);
		final TextView aboutus_text=(TextView)layout.findViewById(R.id.vT_leftmenu_aboutus);
		LinearLayout qrscan_layout=(LinearLayout)layout.findViewById(R.id.vL_lm_qrscan);
		signin_text=(TextView)layout.findViewById(R.id.vT_leftmenu_signin);
		
		LinearLayout signin_layout=(LinearLayout)layout.findViewById(R.id.vL_leftmenu_signin_layout);
		LinearLayout trademanager_layout=(LinearLayout)layout.findViewById(R.id.vL_leftmenu_trademanagerlayout);
		final TextView trademanager=(TextView)layout.findViewById(R.id.vT_leftmenu_trademanager);
		
		final Typeface segeo_Bold;
		final Typeface segeo_Regular;
		segeo_Bold = BaseActionBarActivityNew.getSegeo_Bold();
		segeo_Regular=BaseActionBarActivityNew.getSegeo_Regular();
		Runnable runnable=new Runnable() {
			@Override
			public void run() {
				try{
					username_text.setTypeface(segeo_Regular);
					search_text.setTypeface(segeo_Regular);
					home_text.setTypeface(segeo_Regular);
					myverisupplier_text.setTypeface(segeo_Regular);
					//messages_text.setTypeface(segeo_Regular);
					wishlist_text.setTypeface(segeo_Regular);
					setting_text.setTypeface(segeo_Regular);
					appfeedback_text.setTypeface(segeo_Regular);
					onlinesupport_text.setTypeface(segeo_Regular);
					helpcenter_text.setTypeface(segeo_Regular);
					aboutus_text.setTypeface(segeo_Regular);
					trademanager.setTypeface(segeo_Regular);
				}catch(Exception e){
				} 
			}
		};
		new Thread(runnable).run(); 
		if(mUserDetails.getmUserEmail()==null || mUserDetails.getmUserEmail().equalsIgnoreCase("null")){
			useremail.setText("");
		}else{
			signin_text.setText("Log out");
			useremail.setText(""+mUserDetails.getmUserEmail());
		}
		
		ImageView userImage=(ImageView)layout.findViewById(R.id.vI_leftmenu_userimage);
		userImage.setBackground(getResources().getDrawable(R.drawable.custom_background_gray_square));
		//userImage.setBorderWidth(4);
		LinearLayout myveri_supplier=(LinearLayout)layout.findViewById(R.id.vL_myverisupplier_layout);
		LinearLayout home=(LinearLayout)layout.findViewById(R.id.vL_home_layout);
		LinearLayout wishlist=(LinearLayout)layout.findViewById(R.id.vL_wishlist_layout);
		LinearLayout getopenquote=(LinearLayout)layout.findViewById(R.id.vL_getopenqoute_layout);
		
		myveri_supplier.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent myveriIntent=new Intent(BaseActionBarActivity.this,MyVeriSupplierActivity.class);
				startActivity(myveriIntent);
				overridePendingTransition(0, 0);*/
			}
		});
		
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent myveriIntent=new Intent(BaseActionBarActivity.this,MarketHomeScreen.class);
				startActivity(myveriIntent);
				overridePendingTransition(0, 0);
				userMenu.dismiss();*/
				
			}
		});
		
		wishlist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent wishIntent=new Intent(BaseActionBarActivityNew.this,WishListActivity.class);
				startActivity(wishIntent);
				overridePendingTransition(0, 0);
				userMenu.dismiss();
			}
		});
		
		getopenquote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent wishIntent=new Intent(BaseActionBarActivity.this,UIActivity.class);
				startActivity(wishIntent);
				overridePendingTransition(0, 0);*/
				
			}
		});
		
		qrscan_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                intent.putExtra("SCAN_FORMATS", "QR_CODE,EAN_13,EAN_8,RSS_14,UPC_A,UPC_E,CODE_39,CODE_93,CODE_128,ITF,CODABAR,DATA_MATRIX");
                intent.setAction(Intents.Scan.ACTION);
               // startActivity(intent);
                startActivityForResult(intent, 0);
			}
		});
		
		signin_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getUserDetails();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(BaseActionBarActivityNew.this,UserSignPopup.class);
					signinIntent.putExtra("FROMACTIVITY", "signin");
					startActivity(signinIntent);
					userMenu.dismiss();
				}else{
					showLogoutAlert();
					/*signin_text.setText("Login");
					useremail.setText("");
					DeleteUserDetails();*/
					//Toast.makeText(getApplicationContext(), "Log Out Successfull", Toast.LENGTH_SHORT).show();
				}
				
			}

		});
		
		trademanager_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent tradeIntent=new Intent(BaseActionBarActivity.this,TradeManagerActivity.class);
				startActivity(tradeIntent);
				overridePendingTransition(0, 0);*/
			}
		});
		
		
	
	}
	
	private void showLogoutAlert() {
		new AlertDialog.Builder(this)
		.setMessage("Are you sure do you want to logout")
		.setPositiveButton("Continue", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				signin_text.setText("Login");
				useremail.setText("");
				DeleteUserDetails();
				userMenu.dismiss();
			}

		}).setNegativeButton("Cancel", null)
		.show();
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		default:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				v.setAlpha(.6F);
			else
				v.setAlpha(1F);
			break;
		}
		return false;
	} 
	
	private void DeleteUserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		helper.deleteUserTable();
		helper.close();
	}
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (true) {
			mPoint = new Point();
			mPoint.x = location[0];
			mPoint.y = location[1];
		}
	} 

	public class AsyncDashboard extends AsyncTask<String, Void, Boolean> {

		Boolean IsQuotation = false;
		Boolean Isorder = false;

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(BaseActionBarActivityNew.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			getQuantityList();
			getAllOrderList();
			return IsQuotation = getAllQuotation();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pdLoading.isShowing();
			pdLoading.cancel();
			if (result) {
				Intent dashboardIntent = new Intent(BaseActionBarActivityNew.this, DashboardActivity.class);
				dashboardIntent.putExtra("FROMPAGE", "quotation");
				startActivity(dashboardIntent);
				overridePendingTransition(0, 0);
			} else {
				Toast.makeText(getApplicationContext(), "Hello Exception",Toast.LENGTH_LONG).show();
			}
		}

	}
	
	private void getQuantityList() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		
		String Inparam = "Units_Of_Measurement/Quotation";
		String result = webServices.CallWebHTTPBindingService(ApiType.GetLookups, "GetLookups/", Inparam);
		if(result==null || result.length()==0){
		}else{
			List<Lookups> mLookups=new ArrayList<Lookups>();
			mLookups= jsonParserClass.pareseLookups(result);
			if(mLookups==null || mLookups.size()==0){
			}else{
				VerisupplierUtils.mQuantityList.addAll(mLookups);
			}
		}
	}
	
	private Boolean getAllQuotation() {
		Boolean isValid = false;

		if (isValid.equals(false)) {
			Inparam = mUserDetails.getmUserID()+"/0/1/1/20";//"160" + "/" + "0" + "/" + "1" + "/" + "1" + "/" + "20";
			String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetQuotation, "GetQuotation/", Inparam);
			if (resultOutparam == null || resultOutparam.length() == 0) {
				isValid = false;
			} else {
				quoatationDashboards = jsonParserClass.ParseDashboardQuaotation(resultOutparam);
				/*if (quoatationDashboards == null|| quoatationDashboards.size() == 0) {
					isValid = true;
				} else {*/
					isValid = true;
					Helper helper = new Helper(getApplicationContext());
					helper.openDataBase();
					helper.insertDashboardQuotation(quoatationDashboards);
					helper.close();
				//}
			}

		} else {
			isValid = false;
		}
		return isValid;
	}
	
	private void getAllOrderList() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		Inparam = mUserDetails.getmUserID()+"/0/1/1/10";
		String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetOrder, "GetOrder/", Inparam);
		if (resultOutparam == null || resultOutparam.length() == 0) {
		} else {
			VerisupplierUtils.mOrderDashboardList=jsonParserClass.ParseDashboardOrder(resultOutparam);
		/*if(VerisupplierUtils.mOrderDashboardList==null || VerisupplierUtils.mOrderDashboardList.size()==0){
			
		}else{*/
			Helper helper=new Helper(getApplicationContext());
			helper.openDataBase();
			helper.insertdashboardOrderValues(VerisupplierUtils.mOrderDashboardList);
			helper.close();
		//}
		}
	}
	
	private class AsyncCategoryDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//Toast.makeText(MarketHomeScreen.this, "Called", Toast.LENGTH_SHORT).show();
			pdLoading = new ProgressDialog(BaseActionBarActivityNew.this);
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
			Intent homeIntent=new Intent(BaseActionBarActivityNew.this,Myopenquoate.class);
			startActivity(homeIntent);
			overridePendingTransition(0, 0);
		}
		
	}
	
	private void downloadAllCategories() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mCategoryDetails=helper.getAllCategories("");
		if(mCategoryDetails == null || mCategoryDetails.size()==0){
			downloadCategories();
		}else if(checkdownloadTime(mCategoryDetails.get(0).getmLastDownloadDateTime())){
			
		}else{
			downloadAllCategories();
		}
		helper.close();
	}
	
	private void downloadCategories(){
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
				/*System.out.println("if");
				Intent homeIntent=new Intent(MarketHomeScreen.this,HomePageActivity.class);
				//homeIntent.putExtra("FROMPAGE", "products");
				startActivity(homeIntent);
				overridePendingTransition(0, 0);*/
			}else{
				System.out.println("else");
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

}
