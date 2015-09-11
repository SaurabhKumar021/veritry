package com.mmadapps.verisupplier.trademanager;



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
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.MarketPageActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.UserSignPopup;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.ManufacturerList;
import com.mmadapps.verisupplier.beans.ProductList;
import com.mmadapps.verisupplier.beans.QuoatationDashboard;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.leftmenu.Myopenquoate;
import com.mmadapps.verisupplier.leftmenu.WishListActivity;

public class DashboardActivity extends BaseActionBarActivity {

	Spinner sel_spinner;
	ListView Selectlist;
	BaseActionBarActivity actionBarActivity;
	LinearLayout layoutitem, layoutselect;
	View viewitem;
	UserDetails mUserDetails=new UserDetails();
	//bottom layout for homepage,wishlist,dashboard,openquote,userprofile
		LinearLayout home_layout,wishlist_layout,dashboard_layout,openquote_layout,userprofile_layout;

		//services
		WebServices webServices;
		JsonParserClass jsonParserClass;
		String mInputParam;
		String result;
		List<CategoryDetails> mInspectionType=new ArrayList<CategoryDetails>();
		List<Lookups> mIndustryList=new ArrayList<Lookups>();
		List<Lookups> mInspectorList=new ArrayList<Lookups>();
		
		//time differeance 24 hours
		final double DIFFTIME=8.64e+7;
		

		//bottombar variables
		String Inparam;
		public static TextView signin_text,useremail;
		
		//from databse
		Helper helper;
		List<CategoryDetails> mCategoryDetails=new ArrayList<CategoryDetails>();
		List<ProductList> mProductList=new ArrayList<ProductList>();
		public static List<ManufacturerList> mManufacturerList=new ArrayList<ManufacturerList>();
		
		ArrayList<QuoatationDashboard> quoatationDashboards;
		List<CountryDetails> mCounList=new ArrayList<CountryDetails>();
		List<CountryDetails> mStateList=new ArrayList<CountryDetails>();
		
	//bottombar imageview
	    public static ImageView home_selected,home_notselected,wishlist_selected,wishlist_notselected,dashboard_selected,dashboard_notselected,openquote_selected,openquote_notselected,userprofile_selected,userprofile_notselected;
		
	String[] strings = { "Bluetooth", "Wrist Watch", "Intel stick", "Mobile" };

	String[] subs = { "Manufacture Company Name ", "Manufacture Company Name",
			"Manufacture Company Name", "Manufacture Company Name" };

	int arr_images[] = { R.raw.intel_stick, R.raw.intel_stick,
			R.raw.intel_stick, R.raw.intel_stick };

	//progress
		ProgressDialog pdLoading=null;
		
		//user profile dialog
		private static Point mPoint;
		private static int[] location = new int[2];
		private int mHightofView=0;
		PopupWindow userMenu;
		
		
	// vI_PD_menuicon
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_dashboardtrade);
		actionBarActivity.vUserTitel.setText("Trade Manager");
		actionBarActivity.vUserTitel.setTextColor(Color.parseColor("#4F4F4F"));
		actionBarActivity.vUserSubTitle.setVisibility(View.GONE);
		actionBarActivity.Trademenu.setVisibility(View.VISIBLE);
		actionBarActivity.vBackView.setVisibility(View.GONE);
		//actionBarActivity.vtradeBack.setVisibility(View.GONE);
		
		 actionBarActivity.qrscan_image.setVisibility(View.GONE);
		layoutselect = (LinearLayout) findViewById(R.id.layoutselect);
		layoutitem = (LinearLayout) findViewById(R.id.layoutitem);
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		
		BarChart chart = (BarChart) findViewById(R.id.vC_bc_chart);
		BarData data = new BarData(getXAxisValues(), getDataSet());
		chart.setData(data);
		chart.setDescription("");
		chart.animateXY(3000, 3000);
		chart.invalidate();
		YAxis y = chart.getAxisLeft();
		YAxis x = chart.getAxisRight();
		y.setAxisMaxValue(3000);
		y.setAxisMinValue(500);
		x.setAxisMaxValue(0);
		x.setAxisMinValue(0);
		BarChart chart1 = (BarChart) findViewById(R.id.vC_bc_chartone);
		BarData data1 = new BarData(getXAxisValues1(), getDataSet1());
		chart1.setData(data1);
		chart1.setDescription("");
		chart1.animateXY(3000, 3000);
		chart1.invalidate();
		YAxis y1 = chart1.getAxisLeft();
		YAxis x1 = chart1.getAxisRight();
		y1.setAxisMaxValue(3000);
		y1.setAxisMinValue(500);
		x1.setAxisMaxValue(0);
		x1.setAxisMinValue(0);

		/*
		 * layoutitem.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub //Log.e("clicked","clicked"); Log.e("clicked", "clicked");
		 * layoutitem.setVisibility(View.GONE);
		 * 
		 * } });
		 */

		// layoutitem=(LinearLayout) findViewById(R.id.layoutitem);
		// viewitem=findViewById(R.id.viewgone);
		// layoutitem.setVisibility(View.GONE);
		// viewitem.setVisibility(View.GONE);

		final Spinner mySpinner = (Spinner) findViewById(R.id.vS_adb_selectSpinner);
		// mySpinner.setVisibility(View.GONE);
		layoutitem.setVisibility(View.GONE);
		layoutselect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("clicked", "clicked");
				layoutselect.setVisibility(View.GONE);
				layoutitem.setVisibility(View.VISIBLE);
				mySpinner.performClick();

			}
		});

		mySpinner.setAdapter(new MyAdapter(DashboardActivity.this,
				R.layout.spinner_adapter, strings));

		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// layoutitem.setVisibility(View.VISIBLE);
				// viewitem.setVisibility(View.VISIBLE);

				BarChart chart = (BarChart) findViewById(R.id.vC_bc_chart);
				BarData data = new BarData(getXAxisValues(), getDataSet());
				chart.setData(data);
				chart.setDescription("");
				chart.animateXY(3000, 3000);
				chart.invalidate();
				YAxis y = chart.getAxisLeft();
				YAxis x = chart.getAxisRight();
				y.setAxisMaxValue(3000);
				y.setAxisMinValue(500);
				x.setAxisMaxValue(0);
				x.setAxisMinValue(0);
				BarChart chart1 = (BarChart) findViewById(R.id.vC_bc_chartone);
				BarData data1 = new BarData(getXAxisValues1(), getDataSet1());
				chart1.setData(data1);
				chart1.setDescription("");
				chart1.animateXY(3000, 3000);
				chart1.invalidate();
				YAxis y1 = chart1.getAxisLeft();
				YAxis x1 = chart1.getAxisRight();
				y1.setAxisMaxValue(3000);
				y1.setAxisMinValue(500);
				x1.setAxisMaxValue(0);
				x1.setAxisMinValue(0);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		
		
		home_layout=(LinearLayout)findViewById(R.id.vL_bottombar_home);
		home_selected=(ImageView)findViewById(R.id.vI_bottombar_homeselected);
		home_notselected=(ImageView)findViewById(R.id.vI_bottombar_home_notselected);
		home_notselected.setVisibility(View.VISIBLE);
		home_selected.setVisibility(View.GONE);
		home_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				home_selected.setVisibility(View.VISIBLE);
				home_notselected.setVisibility(View.GONE);
				dashboard_selected.setVisibility(View.GONE);
				dashboard_notselected.setVisibility(View.VISIBLE);
				wishlist_selected.setVisibility(View.GONE);
				wishlist_notselected.setVisibility(View.VISIBLE);
				openquote_selected.setVisibility(View.GONE);
				openquote_notselected.setVisibility(View.VISIBLE);
				
				Intent intent=new Intent(DashboardActivity.this,MarketPageActivity.class);
				startActivity(intent);
			}
		});
		
		wishlist_layout=(LinearLayout)findViewById(R.id.vL_bottombar_wishlist);
		wishlist_selected=(ImageView)findViewById(R.id.vI_bottombar_wishselected);
		wishlist_notselected=(ImageView)findViewById(R.id.vI_bottombar_wish_notselected);
		wishlist_notselected.setVisibility(View.VISIBLE);
		wishlist_selected.setVisibility(View.GONE);
		wishlist_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				dashboard_selected.setVisibility(View.GONE);
				dashboard_notselected.setVisibility(View.VISIBLE);
				wishlist_selected.setVisibility(View.VISIBLE);
				wishlist_notselected.setVisibility(View.GONE);
				Intent homeIntent=new Intent(DashboardActivity.this,WishListActivity.class);
				startActivity(homeIntent);
				overridePendingTransition(0, 0);
			}
		});
		
		dashboard_layout=(LinearLayout)findViewById(R.id.vL_bottombar_dashboard);

		dashboard_selected=(ImageView)findViewById(R.id.vI_bottombar_dashboardselected);
		
		dashboard_notselected=(ImageView)findViewById(R.id.vI_bottombar_dashboard_notselected);
		dashboard_selected.setVisibility(View.VISIBLE);
		dashboard_notselected.setVisibility(View.GONE);
		dashboard_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				dashboard_selected.setVisibility(View.VISIBLE);
				openquote_selected.setVisibility(View.GONE);
				openquote_notselected.setVisibility(View.VISIBLE);
				dashboard_notselected.setVisibility(View.GONE);
				wishlist_selected.setVisibility(View.GONE);
				wishlist_notselected.setVisibility(View.VISIBLE);
				
				getUserId();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					
					Intent signinIntent=new Intent(DashboardActivity.this,UserSignPopup.class);
					signinIntent.putExtra("FROMACTIVITY", "marketdashboard");
					startActivity(signinIntent);
					overridePendingTransition(0, 0);
				}else{
					new AsyncCategoryDetails().execute();
				}
			}
		});
		
		openquote_layout=(LinearLayout)findViewById(R.id.vL_bottombar_openquote);
		openquote_selected=(ImageView)findViewById(R.id.vI_bottombar_openquoteselected);
		
		openquote_notselected=(ImageView)findViewById(R.id.vI_bottombar_openquote_notselected);
		openquote_notselected.setVisibility(View.VISIBLE);
		openquote_selected.setVisibility(View.GONE);
		openquote_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				openquote_selected.setVisibility(View.VISIBLE);
				openquote_notselected.setVisibility(View.GONE);
				dashboard_selected.setVisibility(View.GONE);
				dashboard_notselected.setVisibility(View.VISIBLE);
				getUserId();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(DashboardActivity.this,UserSignPopup.class);
					signinIntent.putExtra("FROMACTIVITY", "marketopenquote");
					startActivity(signinIntent);
					overridePendingTransition(0, 0);
				}else{
					new AsyncCategoryDetails().execute();
				}
			}
		});
		
		userprofile_layout=(LinearLayout)findViewById(R.id.vL_bottombar_profile);
		userprofile_selected=(ImageView)findViewById(R.id.vI_bottombar_userselected);
		userprofile_notselected=(ImageView)findViewById(R.id.vI_bottombar_user_notselected);
		userprofile_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userprofile_selected.setVisibility(View.GONE);
				userprofile_notselected.setVisibility(View.VISIBLE);
				
				getUserDetails();
				location = new int[2];
				v.getLocationOnScreen(location);
				onWindowFocusChanged(true);
				ShowUserProfileBox(DashboardActivity.mPoint);
			}
		});
	}
	
	
private class AsyncCategoryDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//Toast.makeText(MarketHomeScreen.this, "Called", Toast.LENGTH_SHORT).show();
			pdLoading = new ProgressDialog(DashboardActivity.this);
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
			Intent homeIntent=new Intent(DashboardActivity.this,Myopenquoate.class);
			startActivity(homeIntent);
			overridePendingTransition(0, 0);
			finish();
		}
		
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
			helper.insertCategoryDetails(mCategoryDetails);
		}
	}
}
	
private void downloadAllCategories() {
	helper=new Helper(getApplicationContext());
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

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (true) {
			mPoint = new Point();
			mPoint.x = location[0];
			mPoint.y = location[1];
		}
	} 

	public class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row = inflater
					.inflate(R.layout.spinner_adapter, parent, false);
			TextView label = (TextView) row.findViewById(R.id.company);
			label.setText(strings[position]);

			TextView sub = (TextView) row.findViewById(R.id.sub);
			sub.setText(subs[position]);

			ImageView icon = (ImageView) row.findViewById(R.id.image);
			icon.setImageResource(arr_images[position]);

			return row;
		}
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


	private ArrayList<BarDataSet> getDataSet() {
		ArrayList<BarDataSet> dataSets = null;

		ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
		BarEntry v1e1 = new BarEntry(1880.000f, 0); // w1
		valueSet1.add(v1e1);
		BarEntry v1e2 = new BarEntry(440.000f, 1); // w2
		valueSet1.add(v1e2);
		BarEntry v1e3 = new BarEntry(2660.000f, 2); // w3
		valueSet1.add(v1e3);
		BarEntry v1e4 = new BarEntry(330.000f, 3); // w4
		valueSet1.add(v1e4);
		BarEntry v1e5 = new BarEntry(290.000f, 4); // w5
		valueSet1.add(v1e5);

		ArrayList<BarEntry> valueSet2 = new ArrayList<BarEntry>();
		int i = 1500, j = 2000, k = 800, l = 2500, m = 1300;
		BarEntry v2e1 = new BarEntry(i, 0);// w1
		valueSet2.add(v2e1);
		BarEntry v2e2 = new BarEntry(j, 1); // w2
		valueSet2.add(v2e2);
		BarEntry v2e3 = new BarEntry(k, 2); // w3
		valueSet2.add(v2e3);
		BarEntry v2e4 = new BarEntry(l, 3); // w4
		valueSet2.add(v2e4);
		BarEntry v2e5 = new BarEntry(m, 4); // w5
		valueSet2.add(v2e5);

		BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Pending");
		barDataSet1.setColor(Color.rgb(148, 205, 113));
		BarDataSet barDataSet2 = new BarDataSet(valueSet2, "fulfilled");
		barDataSet2.setColor(Color.rgb(0, 137, 147));
		dataSets = new ArrayList<BarDataSet>();
		dataSets.add(barDataSet1);
		dataSets.add(barDataSet2);
		return dataSets;
	}

	private ArrayList<String> getXAxisValues() {
		ArrayList<String> xAxis = new ArrayList<String>();
		xAxis.add("week1");
		xAxis.add("week2");
		xAxis.add("week3");
		xAxis.add("week4");
		xAxis.add("week5");
		return xAxis;
	}

	private ArrayList<BarDataSet> getDataSet1() {
		ArrayList<BarDataSet> dataSets1 = null;

		ArrayList<BarEntry> valueSet3 = new ArrayList<BarEntry>();
		int i = 1500, j = 2000, k = 800, l = 2500, m = 1300;
		BarEntry v2e1 = new BarEntry(i, 0);// w1
		valueSet3.add(v2e1);
		BarEntry v2e2 = new BarEntry(j, 1); // w2
		valueSet3.add(v2e2);
		BarEntry v2e3 = new BarEntry(k, 2); // w3
		valueSet3.add(v2e3);
		BarEntry v2e4 = new BarEntry(l, 3); // w4
		valueSet3.add(v2e4);
		BarEntry v2e5 = new BarEntry(m, 4); // w5
		valueSet3.add(v2e5);

		BarDataSet barDataSet3 = new BarDataSet(valueSet3, "CurrentStock");
		barDataSet3.setColor(Color.parseColor("#008acc"));
		dataSets1 = new ArrayList<BarDataSet>();
		dataSets1.add(barDataSet3);
		return dataSets1;
	}

	private ArrayList<String> getXAxisValues1() {
		ArrayList<String> xAxis1 = new ArrayList<String>();
		xAxis1.add("BNG");
		xAxis1.add("CHE");
		xAxis1.add("KLK");
		xAxis1.add("MUM");
		xAxis1.add("DLH");
		return xAxis1;
	}
	
	
	@SuppressWarnings("deprecation")
	public void ShowUserProfileBox(Point mPoint) {
		ScrollView viewGroup = (ScrollView) findViewById(R.id.menuleft_window);
		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View layout = layoutInflater.inflate(R.layout.left_menu, viewGroup);
		   
		userMenu = new PopupWindow(this);
		userMenu.setContentView(layout);
		userMenu.setWidth(LayoutParams.WRAP_CONTENT);
		userMenu.setHeight(LayoutParams.MATCH_PARENT);
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
		
		final Typeface segeo_Regular;
		segeo_Regular=BaseActionBarActivity.getSegeo_Regular();
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
				getUserId();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(DashboardActivity.this,UserSignPopup.class);
					signinIntent.putExtra("FROMACTIVITY", "marketdashboard");
					startActivity(signinIntent);
					overridePendingTransition(0, 0);
					userMenu.dismiss();
				}else{
					new AsyncDashboard().execute();
				}
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
				Intent wishIntent=new Intent(DashboardActivity.this,WishListActivity.class);
				startActivity(wishIntent);
				overridePendingTransition(0, 0);
				userMenu.dismiss();
			}
		});
		
		getopenquote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		
		qrscan_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                intent.putExtra("SCAN_FORMATS", "QR_CODE,EAN_13,EAN_8,RSS_14,UPC_A,UPC_E,CODE_39,CODE_93,CODE_128,ITF,CODABAR,DATA_MATRIX");
                intent.setAction(Intents.Scan.ACTION);
                startActivityForResult(intent, 0);
			}
		});
		
		signin_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getUserDetails();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(DashboardActivity.this,UserSignPopup.class);
					signinIntent.putExtra("FROMACTIVITY", "signin");
					startActivity(signinIntent);
					userMenu.dismiss();
				}else{
					showLogoutAlert();
				}
				
			}

		});
		
		trademanager_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
	private void DeleteUserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		helper.deleteUserTable();
		helper.close();
	}
	
	

	public class AsyncDashboard extends AsyncTask<String, Void, Boolean> {

		Boolean IsQuotation = false;
		Boolean Isorder = false;

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(DashboardActivity.this);
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
				Intent dashboardIntent = new Intent(DashboardActivity.this, DashboardActivity.class);
				dashboardIntent.putExtra("FROMPAGE", "quotation");
				startActivity(dashboardIntent);
				overridePendingTransition(0, 0);
				userMenu.dismiss();
			} else {
				Toast.makeText(getApplicationContext(), "Hello Exception",Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private Boolean getAllQuotation() {
		Boolean isValid = false;
		if (isValid.equals(false)) {
			if(mUserDetails.getmUserType() == null || mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
				Inparam = mUserDetails.getmUserID()+"/0/1/1/20";//"160" + "/" + "0" + "/" + "1" + "/" + "1" + "/" + "20";
			}else{
				String mManufacturerId=mUserDetails.getmManufacturerId();
				Inparam = "0/"+mManufacturerId+"/1/1/20";//"160" + "/" + "0" + "/" + "1" + "/" + "1" + "/" + "20";
			}
			String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetQuotation, "GetQuotation/", Inparam);
			if (resultOutparam == null || resultOutparam.length() == 0) {
				isValid = false;
			} else {
				quoatationDashboards = jsonParserClass.ParseDashboardQuaotation(resultOutparam);
				isValid = true;
				Helper helper = new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertDashboardQuotation(quoatationDashboards);
				helper.close();
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
			Helper helper=new Helper(getApplicationContext());
			helper.openDataBase();
			helper.insertdashboardOrderValues(VerisupplierUtils.mOrderDashboardList);
			helper.close();
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
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		home_notselected.setVisibility(View.VISIBLE);
		home_selected.setVisibility(View.GONE);
		openquote_notselected.setVisibility(View.VISIBLE);
		openquote_selected.setVisibility(View.GONE);
		dashboard_selected.setVisibility(View.VISIBLE);
		dashboard_notselected.setVisibility(View.GONE);
		wishlist_notselected.setVisibility(View.VISIBLE);
		wishlist_selected.setVisibility(View.GONE);
		
	}
}
