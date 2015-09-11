package com.mmadapps.verisupplier;

import com.mmadapps.verisupplier.customs.CircularImageView;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class BaseFragmentActivity extends FragmentActivity implements OnTouchListener, OnQueryTextListener, OnCloseListener{
	
	public static LinearLayout vLeftView,vRigthView,vBackView;
	static TextView vUserTitel;
	private static Point mPoint;
	private static int[] location = new int[2];
	private int mHightofView=0;
	SharedPreferences sharedPreferences;
	Editor editor;
	LinearLayout getopenquote,home,myveri_supplier,wishlist,messages,setting,app_feedback,online_support,help,about_us;
	public static LinearLayout title_layout,search_layout;
	TextView username_text,search_text,home_text,myverisupplier_text,wishlist_text,messages_text,setting_text,appfeedback_text,onlinesupport_text,helpcenter_text,aboutus_text;
	TextView businessidentity_text,tradeassurance_text,ecreditline_text,inspectionservices_text;
	CircularImageView userImage;
	private static String mUserName;
	public static String getmUserName() {
		return mUserName;
	}

	public static void setmUserName(String mUserName) {
		vUserTitel.setText(""+mUserName);
		BaseFragmentActivity.mUserName = mUserName;
	}
	
	/*private static Typeface Sui_Bold;
	private static Typeface Sui_Light;

	public static Typeface getSui_Bold() {
		return Sui_Bold;
	}

	public static void setSui_Bold(Typeface sui_Bold) {
		Sui_Bold = sui_Bold;
	}

	public static Typeface getSui_Light() {
		return Sui_Light;
	}

	public static void setSui_Light(Typeface sui_Light) {
		Sui_Light = sui_Light;
	}*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar_layout);
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					/*Sui_Bold = Typeface.createFromAsset(getAssets(),"Roboto-Bold.ttf");
					Sui_Light = Typeface.createFromAsset(getAssets(),"Roboto-Light.ttf");*/
					//vUserTitel.setTypeface(Sui_Bold);
				} catch (Exception e) {

				}
			}
		};
		new Thread(runnable).start();
		
		sharedPreferences = getApplicationContext().getSharedPreferences("MEETING_APP", MODE_PRIVATE);
	    editor = sharedPreferences.edit();
	    setCustomActionBar();
	}

	private void setCustomActionBar() {
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView= (SearchView)findViewById(R.id.searchView);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
		
		int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) searchView.findViewById(id);
		textView.setTextColor(Color.BLACK);
		vRigthView = (LinearLayout) findViewById(R.id.action_rightmunu_layout);
		vLeftView =(LinearLayout)findViewById(R.id.action_leftmenu_layout);
		vUserTitel = (TextView) findViewById(R.id.action_titel_text);
		vBackView=(LinearLayout)findViewById(R.id.action_back_layout);
		title_layout=(LinearLayout)findViewById(R.id.vL_title_layout);
		search_layout=(LinearLayout)findViewById(R.id.vL_search_layout);
		vRigthView.setOnTouchListener(this);
		vLeftView.setOnTouchListener(this);
		
		// Calculate ActionBar height
		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
		{
			mHightofView = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
		}  
		
		vRigthView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				location = new int[2];
				v.getLocationOnScreen(location);
				onWindowFocusChanged(true);
			//	ShowUserRightProfileBox(BaseFragmentActivity.mPoint);
			}
		});
		
		vLeftView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				location = new int[2];
				v.getLocationOnScreen(location);
				onWindowFocusChanged(true);
				ShowUserProfileBox(BaseFragmentActivity.mPoint);
				
			}
		});
		
		vBackView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}	
	
	@SuppressWarnings("deprecation")
	public void ShowUserProfileBox(Point mPoint) {
		ScrollView viewGroup = (ScrollView) findViewById(R.id.menuleft_window);
		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.left_menu, viewGroup);
		   
		final PopupWindow userMenu = new PopupWindow(this);
		userMenu.setContentView(layout);
		userMenu.setWidth(LayoutParams.WRAP_CONTENT);
		userMenu.setHeight(LayoutParams.WRAP_CONTENT);
		userMenu.setFocusable(true);
		// Clear the default translucent background
		userMenu.setBackgroundDrawable(new BitmapDrawable());
		userMenu.setAnimationStyle(R.style.animations_popup); 
		userMenu.showAtLocation(layout, Gravity.LEFT|Gravity.TOP+100, -mPoint.x, mPoint.y+mHightofView);
		
		username_text=(TextView)layout.findViewById(R.id.vT_leftmenu_username);
		search_text=(TextView)layout.findViewById(R.id.vT_leftmenu_search);
		home_text=(TextView)layout.findViewById(R.id.vT_leftmenu_home);
		myverisupplier_text=(TextView)layout.findViewById(R.id.vT_leftmenu_myverisupplier);
		wishlist_text=(TextView)layout.findViewById(R.id.vT_leftmenu_wishlist);
		//messages_text=(TextView)layout.findViewById(R.id.vT_leftmenu_messages);
		setting_text=(TextView)layout.findViewById(R.id.vT_leftmenu_setting);
		appfeedback_text=(TextView)layout.findViewById(R.id.vT_leftmenu_appfeedback);
		onlinesupport_text=(TextView)layout.findViewById(R.id.vT_leftmenu_onlinesupport);
		helpcenter_text=(TextView)layout.findViewById(R.id.vT_leftmenu_helpcenter);
		aboutus_text=(TextView)layout.findViewById(R.id.vT_leftmenu_aboutus);
		
		/*final Typeface Sui_Bold;
		Sui_Bold = BaseFragmentActivity.getSui_Bold();
		Runnable runnable=new Runnable() {
			@Override
			public void run() {
				try{
					username_text.setTypeface(Sui_Bold);
					search_text.setTypeface(Sui_Bold);
					home_text.setTypeface(Sui_Bold);
					myverisupplier_text.setTypeface(Sui_Bold);
					messages_text.setTypeface(Sui_Bold);
					wishlist_text.setTypeface(Sui_Bold);
					setting_text.setTypeface(Sui_Bold);
					appfeedback_text.setTypeface(Sui_Bold);
					onlinesupport_text.setTypeface(Sui_Bold);
					helpcenter_text.setTypeface(Sui_Bold);
					aboutus_text.setTypeface(Sui_Bold);
				}catch(Exception e){
				} 
			}
		};
		new Thread(runnable).run(); */
		
		
		userImage=(CircularImageView)layout.findViewById(R.id.vI_leftmenu_userimage);
		userImage.setBorderColor(getResources().getColor(R.color.circular_grayborder));
		userImage.setBorderWidth(4);
		myveri_supplier=(LinearLayout)layout.findViewById(R.id.vL_myverisupplier_layout);
		home=(LinearLayout)layout.findViewById(R.id.vL_home_layout);
		getopenquote=(LinearLayout)layout.findViewById(R.id.vL_getopenqoute_layout);
		
		myveri_supplier.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent myveriIntent=new Intent(BaseFragmentActivity.this,MyVeriSupplierActivity.class);
				startActivity(myveriIntent);
				overridePendingTransition(0, 0);*/
			}
		});
		
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent myveriIntent=new Intent(BaseFragmentActivity.this,MarketHomePage.class);
				startActivity(myveriIntent);
				overridePendingTransition(0, 0);
				finish();*/
			}
		});
		
		getopenquote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent wishIntent=new Intent(BaseFragmentActivity.this,UIActivity.class);
				startActivity(wishIntent);
				overridePendingTransition(0, 0);*/
			}
		});
	
	}
	
	/*@SuppressWarnings("deprecation")
	public void ShowUserRightProfileBox(Point mPoint) {
		LinearLayout viewGroup = (LinearLayout) findViewById(R.id.menuright_window);
		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.right_menu, viewGroup);
		   
		final PopupWindow userMenu = new PopupWindow(this);
		userMenu.setContentView(layout);
		userMenu.setWidth(LayoutParams.WRAP_CONTENT);
		userMenu.setHeight(LayoutParams.WRAP_CONTENT);
		userMenu.setFocusable(true);
		// Clear the default translucent background
		userMenu.setBackgroundDrawable(new BitmapDrawable());
		userMenu.setAnimationStyle(R.style.animations_rightpopup); 
		userMenu.showAtLocation(layout, Gravity.RIGHT|Gravity.TOP, -mPoint.x, mPoint.y+mHightofView);
		
		businessidentity_text=(TextView)layout.findViewById(R.id.vT_rightmenu_businessidentity);
		tradeassurance_text=(TextView)layout.findViewById(R.id.vT_rightmenu_tradeassurance);
		ecreditline_text=(TextView)layout.findViewById(R.id.vT_rightmenu_ecreditline);
		inspectionservices_text=(TextView)layout.findViewById(R.id.vT_rightmenu_inspectionservice);
		
		final Typeface Sui_Bold;
		Sui_Bold = BaseFragmentActivity.getSui_Bold();
		Runnable runnable=new Runnable() {
			@Override
			public void run() {
				try{
					businessidentity_text.setTypeface(Sui_Bold);
					tradeassurance_text.setTypeface(Sui_Bold);
					ecreditline_text.setTypeface(Sui_Bold);
					inspectionservices_text.setTypeface(Sui_Bold);
				}catch(Exception e){
				} 
			}
		};
		new Thread(runnable).run(); 
	}*/

	@Override
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

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (true) {
			mPoint = new Point();
			mPoint.x = location[0];
			mPoint.y = location[1];
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	@Override
	public boolean onClose() {
		return false;
	}
}
