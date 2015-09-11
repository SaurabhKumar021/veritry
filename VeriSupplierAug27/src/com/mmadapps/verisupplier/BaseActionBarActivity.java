package com.mmadapps.verisupplier;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.OrderDashboard;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.QuoatationDashboard;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.CircularImageView;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.dashboard.DashboardActivity;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.mmadapps.verisupplier.leftmenu.WishListActivity;
import com.mmadapps.verisupplier.trademanager.ChannellistActivity;
import com.mmadapps.verisupplier.trademanager.InventorylistActivity;
import com.mmadapps.verisupplier.trademanager.OrderListActivity;
import com.mmadapps.verisupplier.trademanager.TradeAlertActivity;
import com.mmadapps.verisupplier.trademanager.WarehouselistActivity;

public class BaseActionBarActivity extends ActionBarActivity implements OnTouchListener,OnClickListener {

	public static LinearLayout vL_menu_QuotesLayout,vL_orderLayout;

	public LinearLayout vL_menuPriceLayout;

	public LinearLayout vL_moreInfoLayout;
	
	LinearLayout getopenquote, home, myveri_supplier, wishlist, messages,
			setting, app_feedback, online_support, help, about_us,
			qrscan_layout, signin_layout;

	TextView username_text, useremail, search_text, home_text,
			myverisupplier_text, wishlist_text, messages_text, setting_text,
			appfeedback_text, onlinesupport_text, helpcenter_text,
			aboutus_text;
	TextView businessidentity_text, tradeassurance_text, ecreditline_text,
			inspectionservices_text;
	public static LinearLayout vLeftView, vRigthView;
	public static TextView vUserTitel, vUserSubTitle;
	public static CircularImageView vBuyerImage;
	private static ImageView vShoppingCart;
	private static Point mPoint;
	private static int[] location = new int[2];
	private int mHightofView = 0;
	public static LinearLayout title_layout, search_layout;
	ImageView userImage;
	List<ProductDetails> mProductDetails = new ArrayList<ProductDetails>();
	List<OrderDashboard> mOrderList=new ArrayList<OrderDashboard>();
	ArrayList<QuoatationDashboard> quoatationDashboards;
	public static ImageView vBackView,qrscan_image, dashboardMenu,dashboardmenuleft,vI_ab_dashboardproductMenu,vI_ab_dashboardshippingMenu,vI_ab_dashboardinspectionMenu,vI_ab_dashboardcustomsMenu,vI_ab_dashboardwarehouseMenu;
	UserDetails mUserDetails = new UserDetails();
	public static TextView signin_text;

	private static String mUserName;

	public static String getmUserName() {
		return mUserName;
	}

	public static void setmUserName(String mUserName) {
		vUserTitel.setText("" + mUserName);
		BaseActionBarActivity.mUserName = mUserName;
	}

	public static void setmUserSubTitle(String mSubTitle) {
		vUserSubTitle.setTextSize(14);
		vUserTitel.setTextSize(14);
		vUserSubTitle.setVisibility(View.VISIBLE);
		vUserSubTitle.setText("" + mSubTitle);
		vShoppingCart.setVisibility(View.GONE);
	}

	/*private static Typeface Sui_Bold;
	private static Typeface Sui_Light;
	private static Typeface Sui_Normal;

	public static Typeface getSui_Normal() {
		return Sui_Normal;
	}

	public static void setSui_Normal(Typeface sui_Normal) {
		Sui_Normal = sui_Normal;
	}

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
	
	private static Typeface Segeo_Bold;
	private static Typeface Segeo_Light;
	private static Typeface Segeo_Regular;
	

	public static Typeface getSegeo_Bold() {
		return Segeo_Bold;
	}

	public static void setSegeo_Bold(Typeface segeo_Bold) {
		Segeo_Bold = segeo_Bold;
	}

	public static Typeface getSegeo_Light() {
		return Segeo_Light;
	}

	public static void setSegeo_Light(Typeface segeo_Light) {
		Segeo_Light = segeo_Light;
	}

	public static Typeface getSegeo_Regular() {
		return Segeo_Regular;
	}

	public static void setSegeo_Regular(Typeface segeo_Regular) {
		Segeo_Regular = segeo_Regular;
	}

	private ProgressDialog pdLoading = null;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	String contents = "";
	String mProductId = "";
	PopupWindow userMenu;
	public static ImageView Trademenu,vtradeBack;
	
	String mType;
	String mInparam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar_new);
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					Segeo_Bold=Typeface.createFromAsset(getAssets(),"segeo-wp-bold.ttf");
					Segeo_Light=Typeface.createFromAsset(getAssets(),"segoeuil.ttf");
					Segeo_Regular=Typeface.createFromAsset(getAssets(),"segeo-wp.ttf");
					
					/*Sui_Bold = Typeface.createFromAsset(getAssets(),"segeo-wp-bold.ttf");
					Sui_Light = Typeface.createFromAsset(getAssets(),"Roboto-Light.ttf");
					Sui_Normal = Typeface.createFromAsset(getAssets(),"segeo-wp.ttf");*/
					vUserTitel.setTypeface(Segeo_Regular);
					vUserSubTitle.setTypeface(Segeo_Regular);
				} catch (Exception e) {

				}
			}
		};
		new Thread(runnable).start();
		setCustomActionBar();

	}

	private void getUserDetails() {
		Helper helper = new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails = helper.getUserDetails();
		helper.close();

	}

	private void setCustomActionBar() {
		vRigthView = (LinearLayout) findViewById(R.id.action_rightmunu_layout);
		vLeftView = (LinearLayout) findViewById(R.id.action_leftmenu_layout);
		vBackView = (ImageView) findViewById(R.id.vI_ab_backview);
		vUserTitel = (TextView) findViewById(R.id.action_titel_text);
		vUserSubTitle = (TextView) findViewById(R.id.action_subtitel_text);
		vBuyerImage = (CircularImageView) findViewById(R.id.vI_ab_buyerImage);
		title_layout = (LinearLayout) findViewById(R.id.vL_title_layout);
		search_layout = (LinearLayout) findViewById(R.id.vL_search_layout);
		vShoppingCart = (ImageView) findViewById(R.id.vI_ab_shoppingCart);
		vRigthView.setOnTouchListener(this);
		qrscan_image = (ImageView) findViewById(R.id.vI_PD_qrscan);
		dashboardMenu = (ImageView) findViewById(R.id.vI_ab_dashboardMenu);
		vI_ab_dashboardproductMenu = (ImageView) findViewById(R.id.vI_ab_dashboardproductMenu);
		vI_ab_dashboardshippingMenu = (ImageView) findViewById(R.id.vI_ab_dashboardshippingMenu);
		vI_ab_dashboardinspectionMenu = (ImageView) findViewById(R.id.vI_ab_dashboardinspectionMenu);
		vI_ab_dashboardcustomsMenu = (ImageView) findViewById(R.id.vI_ab_dashboardcustomsMenu);
		vI_ab_dashboardwarehouseMenu = (ImageView) findViewById(R.id.vI_ab_dashboardwarehouseMenu);
		vI_ab_dashboardproductMenu.setOnClickListener(this);
		vI_ab_dashboardshippingMenu.setOnClickListener(this);
		vI_ab_dashboardinspectionMenu.setOnClickListener(this);
		vI_ab_dashboardcustomsMenu.setOnClickListener(this);
		vI_ab_dashboardwarehouseMenu.setOnClickListener(this);
		Trademenu=(ImageView) findViewById(R.id.vI_ab_trademenu);
		//vtradeBack=(ImageView)findViewById(R.id.backview);
		dashboardmenuleft=(ImageView)findViewById(R.id.vI_ab_dashboardmenuleft);
		
		// Calculate ActionBar height
		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			mHightofView = TypedValue.complexToDimensionPixelSize(tv.data,
					getResources().getDisplayMetrics());
		}

		qrscan_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		vLeftView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getUserDetails();
				location = new int[2];
				v.getLocationOnScreen(location);
				onWindowFocusChanged(true);
				ShowUserProfileBox(BaseActionBarActivity.mPoint);

			}
		});

		vBackView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					MarketPageActivity.home_selected.setVisibility(View.VISIBLE);
					MarketPageActivity.home_notselected.setVisibility(View.GONE);
					
					MarketPageActivity.wishlist_notselected.setVisibility(View.VISIBLE);
					MarketPageActivity.wishlist_selected.setVisibility(View.GONE);
					
					MarketPageActivity.tradedashboard_notselected.setVisibility(View.VISIBLE);
					MarketPageActivity.tradedashboard_selected.setVisibility(View.GONE);
					
					MarketPageActivity.openquote_notselected.setVisibility(View.VISIBLE);
					MarketPageActivity.openquote_selected.setVisibility(View.GONE);
					
					MarketPageActivity.userprofile_notselected.setVisibility(View.VISIBLE);
					MarketPageActivity.userprofile_selected.setVisibility(View.GONE);
				}catch(Exception e){
					e.printStackTrace();
				}
				finish();
			}
		});

		dashboardMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				location = new int[2];
				v.getLocationOnScreen(location);
				onWindowFocusChanged(true);
				ShowDashboardAlert(BaseActionBarActivity.mPoint);
				if(DashboardActivity.mDrawerLayout.isShown()){
					DashboardActivity.mDrawerLayout.closeDrawers();
				}
			}
		});
		
		Trademenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				location = new int[2];
				v.getLocationOnScreen(location);
				onWindowFocusChanged(true);
				ShowTradeManagerAlert(BaseActionBarActivity.mPoint);
				
				
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	private void ShowDashboardAlert(Point mPoint) {
		LinearLayout viewGroup = (LinearLayout) findViewById(R.id.menu);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.dashboard_menu, viewGroup);

		final PopupWindow userMenu = new PopupWindow(this);
		userMenu.setContentView(layout);
		userMenu.setWidth(500);
		userMenu.setHeight(LayoutParams.WRAP_CONTENT);
		userMenu.setFocusable(true);
		// Clear the default translucent background
		userMenu.setBackgroundDrawable(new BitmapDrawable());
		userMenu.setAnimationStyle(R.style.alertanimation_popup);
		userMenu.showAtLocation(layout, Gravity.RIGHT | Gravity.TOP, -mPoint.x-10, mPoint.y + mHightofView - 30);

		vL_menu_QuotesLayout = (LinearLayout) layout.findViewById(R.id.vL_menu_QuotesLayout);
		vL_menuPriceLayout = (LinearLayout) layout.findViewById(R.id.vL_menuPriceLayout);
		vL_moreInfoLayout = (LinearLayout) layout.findViewById(R.id.vL_moreInfoLayout);
		vL_orderLayout = (LinearLayout) layout.findViewById(R.id.vL_orderLayout);
		
		final TextView vT_viewQuotes = (TextView) layout.findViewById(R.id.vT_viewQuotes);
		final TextView vT_priceRequests = (TextView) layout.findViewById(R.id.vT_priceRequests);
		final TextView vT_moreInfoRequests = (TextView) layout.findViewById(R.id.vT_moreInfoRequests);
		final TextView vT_orderList = (TextView) layout.findViewById(R.id.vT_orderList);
		
		vL_menu_QuotesLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vL_menu_QuotesLayout.setBackgroundColor(getResources().getColor(R.color.white));
				vL_menuPriceLayout.setBackgroundColor(Color.parseColor("#FF5A5F"));
				vL_moreInfoLayout.setBackgroundColor(Color.parseColor("#FF5A5F"));
				vL_orderLayout.setBackgroundColor(Color.parseColor("#FF5A5F"));
				
				vT_viewQuotes.setCompoundDrawables(getResources().getDrawable(R.raw.icon_menu_quotes_r), null, null, null);
				vT_priceRequests.setCompoundDrawables(getResources().getDrawable(R.raw.icon_menu_price_w), null, null, null);
				vT_moreInfoRequests.setCompoundDrawables(getResources().getDrawable(R.raw.icon_menu_mi_w), null, null, null);
				vT_orderList.setCompoundDrawables(getResources().getDrawable(R.raw.icon_menu_order_w), null, null, null);
				vT_viewQuotes.setTextColor(Color.parseColor("#FF5A5F"));
				
				if(userMenu != null && userMenu.isShowing()){
					userMenu.dismiss();
				}
				mType="quotation";
				getUserDetails();
				new AsyncOrderList().execute();
			}
		});

		vL_orderLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vL_menu_QuotesLayout.setBackgroundColor(Color.parseColor("#FF5A5F"));
				vL_menuPriceLayout.setBackgroundColor(Color.parseColor("#FF5A5F"));
				vL_moreInfoLayout.setBackgroundColor(Color.parseColor("#FF5A5F"));
				vL_orderLayout.setBackgroundColor(getResources().getColor(R.color.white));
				
				vT_viewQuotes.setCompoundDrawables(getResources().getDrawable(R.raw.icon_menu_quotes_w), null, null, null);
				vT_priceRequests.setCompoundDrawables(getResources().getDrawable(R.raw.icon_menu_price_w), null, null, null);
				vT_moreInfoRequests.setCompoundDrawables(getResources().getDrawable(R.raw.icon_menu_mi_w), null, null, null);
				vT_orderList.setCompoundDrawables(getResources().getDrawable(R.raw.icon_menu_order_r), null, null, null);
				vT_orderList.setTextColor(Color.parseColor("#FF5A5F"));
				if(userMenu != null && userMenu.isShowing()){
					userMenu.dismiss();
				}
				mType="order";
				getUserDetails();
				new AsyncOrderList().execute();
			}
		});

	}
	
	@SuppressWarnings("deprecation")
	public void ShowUserProfileBox(Point mPoint) {
		ScrollView viewGroup = (ScrollView) findViewById(R.id.menuleft_window);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View layout = layoutInflater.inflate(R.layout.left_menu, viewGroup);

		userMenu = new PopupWindow(this);
		userMenu.setContentView(layout);
		userMenu.setWidth(LayoutParams.WRAP_CONTENT);
		userMenu.setHeight(LayoutParams.MATCH_PARENT);
		userMenu.setFocusable(true);
		// Clear the default translucent background
		userMenu.setBackgroundDrawable(new BitmapDrawable());
		userMenu.setAnimationStyle(R.style.animations_popup);
		userMenu.showAtLocation(layout, Gravity.LEFT | Gravity.TOP - 50,
				-mPoint.x, mPoint.y + mHightofView);

		/*
		 * WindowManager.LayoutParams lp = getWindow().getAttributes();
		 * lp.dimAmount = 0.0f;
		 * getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		 */

		username_text = (TextView) layout
				.findViewById(R.id.vT_leftmenu_username);
		useremail = (TextView) layout.findViewById(R.id.vT_leftmenu_useremail);
		search_text = (TextView) layout.findViewById(R.id.vT_leftmenu_search);
		home_text = (TextView) layout.findViewById(R.id.vT_leftmenu_home);
		myverisupplier_text = (TextView) layout
				.findViewById(R.id.vT_leftmenu_myverisupplier);
		wishlist_text = (TextView) layout
				.findViewById(R.id.vT_leftmenu_wishlist);
		// messages_text=(TextView)layout.findViewById(R.id.vT_leftmenu_messages);
		setting_text = (TextView) layout.findViewById(R.id.vT_leftmenu_setting);
		appfeedback_text = (TextView) layout
				.findViewById(R.id.vT_leftmenu_appfeedback);
		onlinesupport_text = (TextView) layout
				.findViewById(R.id.vT_leftmenu_onlinesupport);
		helpcenter_text = (TextView) layout
				.findViewById(R.id.vT_leftmenu_helpcenter);
		aboutus_text = (TextView) layout.findViewById(R.id.vT_leftmenu_aboutus);
		qrscan_layout = (LinearLayout) layout.findViewById(R.id.vL_lm_qrscan);
		signin_text = (TextView) layout.findViewById(R.id.vT_leftmenu_signin);

		signin_layout = (LinearLayout) layout
				.findViewById(R.id.vL_leftmenu_signin_layout);
		LinearLayout trademanager_layout = (LinearLayout) layout
				.findViewById(R.id.vL_leftmenu_trademanagerlayout);
		final TextView trademanager = (TextView) layout
				.findViewById(R.id.vT_leftmenu_trademanager);

		Segeo_Light = BaseActionBarActivity.getSegeo_Light();
		Segeo_Regular = BaseActionBarActivity.getSegeo_Regular();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					username_text.setTypeface(Segeo_Regular);
					search_text.setTypeface(Segeo_Regular);
					home_text.setTypeface(Segeo_Regular);
					myverisupplier_text.setTypeface(Segeo_Regular);
					messages_text.setTypeface(Segeo_Regular);
					wishlist_text.setTypeface(Segeo_Regular);
					setting_text.setTypeface(Segeo_Regular);
					appfeedback_text.setTypeface(Segeo_Regular);
					onlinesupport_text.setTypeface(Segeo_Regular);
					helpcenter_text.setTypeface(Segeo_Regular);
					aboutus_text.setTypeface(Segeo_Regular);
					trademanager.setTypeface(Segeo_Regular);
				} catch (Exception e) {
				}
			}
		};
		new Thread(runnable).run();
		if (mUserDetails.getmUserEmail() == null
				|| mUserDetails.getmUserEmail().equalsIgnoreCase("null")) {
			useremail.setText("");
		} else {
			signin_text.setText("Log out");
			useremail.setText("" + mUserDetails.getmUserEmail());
		}

		userImage = (ImageView) layout.findViewById(R.id.vI_leftmenu_userimage);
		userImage.setBackground(getResources().getDrawable(
				R.drawable.custom_background_gray_square));
		// userImage.setBorderWidth(4);
		myveri_supplier = (LinearLayout) layout
				.findViewById(R.id.vL_myverisupplier_layout);
		home = (LinearLayout) layout.findViewById(R.id.vL_home_layout);
		wishlist = (LinearLayout) layout.findViewById(R.id.vL_wishlist_layout);
		getopenquote = (LinearLayout) layout
				.findViewById(R.id.vL_getopenqoute_layout);

		myveri_supplier.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Intent myveriIntent=new
				 * Intent(BaseActionBarActivity.this,MyVeriSupplierActivity
				 * .class); startActivity(myveriIntent);
				 * overridePendingTransition(0, 0);
				 */
			}
		});

		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent myveriIntent = new Intent(BaseActionBarActivity.this,MarketHomeScreen.class);
				startActivity(myveriIntent);
				overridePendingTransition(0, 0);
				userMenu.dismiss();*/

			}
		});

		wishlist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent wishIntent = new Intent(BaseActionBarActivity.this,WishListActivity.class);
				startActivity(wishIntent);
				overridePendingTransition(0, 0);
				userMenu.dismiss();
			}
		});

		getopenquote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Intent wishIntent=new
				 * Intent(BaseActionBarActivity.this,UIActivity.class);
				 * startActivity(wishIntent); overridePendingTransition(0, 0);
				 */

			}
		});

		qrscan_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
				intent.putExtra("SCAN_FORMATS","QR_CODE,EAN_13,EAN_8,RSS_14,UPC_A,UPC_E,CODE_39,CODE_93,CODE_128,ITF,CODABAR,DATA_MATRIX");
				intent.setAction(Intents.Scan.ACTION);
				startActivityForResult(intent, 0);*/
			}
		});

		signin_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getUserDetails();
				if (mUserDetails.getmUserID() == null || mUserDetails.getmUserID().equalsIgnoreCase("null")) {
					Intent signinIntent = new Intent(BaseActionBarActivity.this, UserSignPopup.class);
					signinIntent.putExtra("FROMACTIVITY", "signin");
					startActivity(signinIntent);
					userMenu.dismiss();
				} else {
					showLogoutAlert();
				}

			}

		});

		trademanager_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Intent tradeIntent = new Intent(BaseActionBarActivity.this,TradeManagerActivit.class);
				startActivity(tradeIntent);
				overridePendingTransition(0, 0);*/
			}
		});

	}

	private void showLogoutAlert() {
		new AlertDialog.Builder(this)
		.setMessage("Are you sure do you want to logout")
		.setPositiveButton("Continue",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which) {
				signin_text.setText("Login");
				useremail.setText("");
				DeleteUserDetails();
				userMenu.dismiss();
				Toast.makeText(getApplicationContext(), "logout successfull", Toast.LENGTH_SHORT).show();
			}

		}).setNegativeButton("Cancel", null).show();
	}

	private void DeleteUserDetails() {
		Helper helper = new Helper(getApplicationContext());
		helper.openDataBase();
		helper.deleteUserTable();
		helper.close();
	}

	/*
	 * @SuppressWarnings("deprecation") public void
	 * ShowUserRightProfileBox(Point mPoint) { LinearLayout viewGroup =
	 * (LinearLayout) findViewById(R.id.menuright_window); LayoutInflater
	 * layoutInflater =
	 * (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); View
	 * layout = layoutInflater.inflate(R.layout.right_menu, viewGroup);
	 * 
	 * final PopupWindow userMenu = new PopupWindow(this);
	 * userMenu.setContentView(layout);
	 * userMenu.setWidth(LayoutParams.WRAP_CONTENT);
	 * userMenu.setHeight(LayoutParams.WRAP_CONTENT);
	 * userMenu.setFocusable(true); // Clear the default translucent background
	 * userMenu.setBackgroundDrawable(new BitmapDrawable());
	 * userMenu.setAnimationStyle(R.style.animations_rightpopup);
	 * userMenu.showAtLocation(layout, Gravity.RIGHT|Gravity.TOP, -mPoint.x,
	 * mPoint.y+mHightofView);
	 * 
	 * businessidentity_text=(TextView)layout.findViewById(R.id.
	 * vT_rightmenu_businessidentity);
	 * tradeassurance_text=(TextView)layout.findViewById
	 * (R.id.vT_rightmenu_tradeassurance);
	 * ecreditline_text=(TextView)layout.findViewById
	 * (R.id.vT_rightmenu_ecreditline);
	 * inspectionservices_text=(TextView)layout.
	 * findViewById(R.id.vT_rightmenu_inspectionservice);
	 * 
	 * final Typeface Sui_Bold; Sui_Bold = BaseActionBarActivity.getSui_Bold();
	 * Runnable runnable=new Runnable() {
	 * 
	 * @Override public void run() { try{
	 * businessidentity_text.setTypeface(Sui_Bold);
	 * tradeassurance_text.setTypeface(Sui_Bold);
	 * ecreditline_text.setTypeface(Sui_Bold);
	 * inspectionservices_text.setTypeface(Sui_Bold);
	 * 
	 * }catch(Exception e){ } } }; new Thread(runnable).run(); }
	 */

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

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == BaseActionBarActivity.RESULT_OK) {
			contents = intent.getStringExtra(Intents.Scan.RESULT);
			String formatName = intent
					.getStringExtra(Intents.Scan.RESULT_FORMAT);
			Log.e("Result of Scanning", contents);
			Log.e("Result of Scanning", formatName);
			// new AsyncProductDetails().execute(contents);
			mProductId = contents;
			getProductDetails();
			if (mProductDetails == null || mProductDetails.size() == 0) {
				new AsyncProductDetails().execute(contents);
			} else {
				Intent newIntent = new Intent(BaseActionBarActivity.this,ProductDetailsActivity.class);
				newIntent.putExtra("PRODUCTID", mProductId);
				startActivity(newIntent);
				overridePendingTransition(0, 0);
			}

		} else if (resultCode == Activity.RESULT_CANCELED) {
		}
	}

	private void getProductDetails() {
		Helper helper = new Helper(getApplicationContext());
		helper.openDataBase();
		mProductDetails = helper.getAllProductDetails(mProductId);
		helper.close();
	}

	class AsyncProductDetails extends AsyncTask<String, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(BaseActionBarActivity.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			return isValidDetails(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pdLoading.cancel();
			if (pdLoading.isShowing())
				pdLoading.dismiss();
			if (result) {
				Intent newIntent = new Intent(BaseActionBarActivity.this,ProductDetailsActivity.class);
				newIntent.putExtra("PRODUCTID", contents);
				startActivity(newIntent);
				overridePendingTransition(0, 0);
			} else {
				Toast.makeText(getApplicationContext(), "product is not exist",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private Boolean isValidDetails(String contents) {
		boolean isValid = false;
		webServices = new WebServices();
		jsonParserClass = new JsonParserClass();
		List<ProductDetails> mDetails = new ArrayList<ProductDetails>();
		String Inparam = contents;
		String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetProductDetail, "GetProductDetail/", Inparam);
		if (resultOutparam == null) {
			isValid = false;
		} else {
			mDetails = jsonParserClass.parseProductDetails(resultOutparam,getApplicationContext());
			if (mDetails == null || mDetails.size() == 0) {
			} else {
				Helper helper = new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertProductDetails(mDetails);
				helper.insertSpecificationDetails(mDetails, Inparam);
				helper.insertUserFeedback(mDetails, Inparam);
				helper.close();
				isValid = true;
			}
		}

		return isValid;
	}
	
	@SuppressWarnings("deprecation")
	private void ShowTradeManagerAlert(Point mPoint) {
	LinearLayout viewGroup = (LinearLayout) findViewById(R.id.vL_popupDashboard);
	LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View layout = layoutInflater.inflate(R.layout.popup_dashboard, viewGroup);

	final PopupWindow userMenutrade = new PopupWindow(this);
	userMenutrade.setContentView(layout);
	userMenutrade.setWidth(400);
	userMenutrade.setHeight(LayoutParams.MATCH_PARENT);
	userMenutrade.setFocusable(true);
	// Clear the default translucent background
	userMenutrade.setBackgroundDrawable(new BitmapDrawable());
	userMenutrade.setAnimationStyle(R.style.animations_popup);
//	userMenutrade.showAtLocation(layout, Gravity.LEFT |  Gravity.TOP - 40, -mPoint.x, mPoint.y + mHightofView);

	userMenutrade.showAtLocation(layout, Gravity.LEFT |  Gravity.TOP -40, 0,70);
	
	LinearLayout vL_pui_alert=(LinearLayout) layout.findViewById(R.id.vL_pui_alert);
	LinearLayout vL_ds_order=(LinearLayout) layout.findViewById(R.id.vL_ds_order);
	LinearLayout vL_ds_inventory=(LinearLayout) layout.findViewById(R.id.vL_ds_inventory);
	LinearLayout vL_ds_channel=(LinearLayout) layout.findViewById(R.id.vL_ds_channel);
	LinearLayout vL_ds_warehouse=(LinearLayout) layout.findViewById(R.id.vL_ds_warehouse);
	LinearLayout vL_ds_Home=(LinearLayout) layout.findViewById(R.id.vL_ds_Home);
	
	vL_pui_alert.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {

			if(userMenutrade != null && userMenutrade.isShowing()){
				userMenutrade.dismiss();
			}

			Intent intent=new Intent(BaseActionBarActivity.this,TradeAlertActivity.class);
			startActivity(intent);
			
			overridePendingTransition(0, 0);
			
		}
	});
	vL_ds_Home.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {

			if(userMenutrade != null && userMenutrade.isShowing()){
				userMenutrade.dismiss();
			}

			Intent intent=new Intent(BaseActionBarActivity.this,com.mmadapps.verisupplier.trademanager.DashboardActivity.class);
			startActivity(intent);
			
			overridePendingTransition(0, 0);
		}
	});
	vL_ds_order.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			
			
			if(userMenutrade != null && userMenutrade.isShowing()){
				userMenutrade.dismiss();
			}
			
			Intent intent=new Intent(BaseActionBarActivity.this,OrderListActivity.class);
			startActivity(intent);
			
			overridePendingTransition(0, 0);
		}
	});
	
	vL_ds_inventory.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			if(userMenutrade != null && userMenutrade.isShowing()){
				userMenutrade.dismiss();
			}
			
			Intent intent=new Intent(BaseActionBarActivity.this,InventorylistActivity.class);
			startActivity(intent);
			
			overridePendingTransition(0, 0);
		}
	});

	vL_ds_channel.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		if(userMenutrade != null && userMenutrade.isShowing()){
			userMenutrade.dismiss();
		}
		
		Intent intent=new Intent(BaseActionBarActivity.this,ChannellistActivity.class);
		startActivity(intent);
		
		overridePendingTransition(0, 0);
	}
});
	vL_ds_warehouse.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		if(userMenutrade != null && userMenutrade.isShowing()){
			userMenutrade.dismiss();
		}
		
		Intent intent=new Intent(BaseActionBarActivity.this,WarehouselistActivity.class);
		startActivity(intent);
		
		overridePendingTransition(0, 0);
	}
});

}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vI_ab_dashboardproductMenu:
			location = new int[2];
			v.getLocationOnScreen(location);
			onWindowFocusChanged(true);
			ShowDashboardAlert(BaseActionBarActivity.mPoint);
			if(DashboardActivity.mDrawerLayout.isShown()){
				DashboardActivity.mDrawerLayout.closeDrawers();
			}
			break;
			
		case R.id.vI_ab_dashboardshippingMenu:
			location = new int[2];
			v.getLocationOnScreen(location);
			onWindowFocusChanged(true);
			ShowDashboardAlert(BaseActionBarActivity.mPoint);
			if(DashboardActivity.mDrawerLayout.isShown()){
				DashboardActivity.mDrawerLayout.closeDrawers();
			}
			break;
			
		case R.id.vI_ab_dashboardinspectionMenu:
			location = new int[2];
			v.getLocationOnScreen(location);
			onWindowFocusChanged(true);
			ShowDashboardAlert(BaseActionBarActivity.mPoint);
			if(DashboardActivity.mDrawerLayout.isShown()){
				DashboardActivity.mDrawerLayout.closeDrawers();
			}
			break;
			
		case R.id.vI_ab_dashboardcustomsMenu:
			location = new int[2];
			v.getLocationOnScreen(location);
			onWindowFocusChanged(true);
			ShowDashboardAlert(BaseActionBarActivity.mPoint);
			if(DashboardActivity.mDrawerLayout.isShown()){
				DashboardActivity.mDrawerLayout.closeDrawers();
			}
			break;
			
		case R.id.vI_ab_dashboardwarehouseMenu:
			location = new int[2];
			v.getLocationOnScreen(location);
			onWindowFocusChanged(true);
			ShowDashboardAlert(BaseActionBarActivity.mPoint);
			if(DashboardActivity.mDrawerLayout.isShown()){
				DashboardActivity.mDrawerLayout.closeDrawers();
			}
			break;

		default:
			break;
		}
		
	}
	
	private class AsyncOrderList extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdLoading = new ProgressDialog(BaseActionBarActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			
		}


		@Override
		protected Boolean doInBackground(Void... params) {
			if(mType.equalsIgnoreCase("order")){
				return callOrderList();
			}else{
				return callQuotationList();
				
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			if(mType.equalsIgnoreCase("order")){
				Intent dashboardIntent = new Intent(BaseActionBarActivity.this, DashboardActivity.class);
				dashboardIntent.putExtra("Type", "order");
				startActivity(dashboardIntent);
				overridePendingTransition(0, 0);
				finish();
			}else{
				Intent dashboardIntent = new Intent(BaseActionBarActivity.this, DashboardActivity.class);
				dashboardIntent.putExtra("Type", "dashboard");
				startActivity(dashboardIntent);
				overridePendingTransition(0, 0);
				finish();
			}
		}
		
	}

	public Boolean callOrderList() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		if(VerisupplierUtils.mDashboardCurrentType.equalsIgnoreCase("dashboard") || VerisupplierUtils.mDashboardCurrentType.equalsIgnoreCase("order")){
			if(mUserDetails.getmUserType()==null || mUserDetails.getmUserType().length()==0){
			}else{
				if(mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
					if(VerisupplierUtils.mDashboardServiceGroupType==null || VerisupplierUtils.mDashboardServiceGroupType.equalsIgnoreCase("product")){
						mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
					}
					else if(VerisupplierUtils.mDashboardServiceGroupType.equalsIgnoreCase("shipping")){
						mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mShippingParam+"/1/20";
					}
					else if(VerisupplierUtils.mDashboardServiceGroupType.equalsIgnoreCase("inspection")){
						mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mInspectionParam+"/1/20";
					}
					else if(VerisupplierUtils.mDashboardServiceGroupType.equalsIgnoreCase("custom")){
						mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mCustomsParam+"/1/20";
					}
					else if(VerisupplierUtils.mDashboardServiceGroupType.equalsIgnoreCase("warehouse")){
						mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mWarehouseParam+"/1/20";
					}
				}
				else if(mUserDetails.getmUserType().equalsIgnoreCase("Manufacturer") || mUserDetails.getmUserType().equalsIgnoreCase("Product")){
					mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
				}
				else if(mUserDetails.getmUserType().equalsIgnoreCase("Customs")){
					mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mCustomsParam+"/1/20";
				}
				else if(mUserDetails.getmUserType().equalsIgnoreCase("Shipment")){
					mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mShippingParam+"/1/20";
				}
				else if(mUserDetails.getmUserType().equalsIgnoreCase("Inspection")){
					mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mInspectionParam+"/1/20";
				}
				else if(mUserDetails.getmUserType().equalsIgnoreCase("Warehouse")){
					mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mWarehouseParam+"/1/20";
				}
			}
		}else{
			mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
		}
		//mInparam=""+mUserDetails.getmUserID()+"/0"+"/"+VerisupplierUtils.mProductServiceGroupType+"/1/10";
		
		String mOutputParam=webServices.CallWebHTTPBindingService(ApiType.GetOrder, "GetOrder/", mInparam);
		if(mOutputParam==null || mOutputParam.length()==0){
		}else{
			mOrderList=jsonParserClass.ParseDashboardOrder(mOutputParam);
			if(mOrderList==null || mOrderList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertdashboardOrderValues(mOrderList);
				helper.close();
			}
		}
		
		return isValid;
	}

	public Boolean callQuotationList() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		if(mUserDetails.getmUserType() == null ){
		}else{
			if(VerisupplierUtils.mDashboardCurrentType.equalsIgnoreCase("dashboard") || VerisupplierUtils.mDashboardCurrentType.equalsIgnoreCase("order")){
				if(mUserDetails.getmUserType()==null || mUserDetails.getmUserType().length()==0){
				}else{
					if(mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
						if(VerisupplierUtils.mDashboardServiceGroupType==null || VerisupplierUtils.mDashboardServiceGroupType.equalsIgnoreCase("product")){
							mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
						}
						else if(VerisupplierUtils.mDashboardServiceGroupType.equalsIgnoreCase("shipping")){
							mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mShippingParam+"/1/20";
						}
						else if(VerisupplierUtils.mDashboardServiceGroupType.equalsIgnoreCase("inspection")){
							mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mInspectionParam+"/1/20";
						}
						else if(VerisupplierUtils.mDashboardServiceGroupType.equalsIgnoreCase("custom")){
							mInparam =mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mCustomsParam+"/1/20";
						}
						else if(VerisupplierUtils.mDashboardServiceGroupType.equalsIgnoreCase("warehouse")){
							mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mWarehouseParam+"/1/20";
						}
					}
					else if(mUserDetails.getmUserType().equalsIgnoreCase("Manufacturer") || mUserDetails.getmUserType().equalsIgnoreCase("Product")){
						String mManufacturerId=mUserDetails.getmManufacturerId();
						mInparam = "0"+"/"+mManufacturerId+"/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
					}
					else if(mUserDetails.getmUserType().equalsIgnoreCase("Customs")){
						String mManufacturerId=mUserDetails.getmManufacturerId();
						mInparam ="0"+"/"+mManufacturerId+"/"+VerisupplierUtils.mCustomsParam+"/1/20";
					}
					else if(mUserDetails.getmUserType().equalsIgnoreCase("Shipment")){
						String mManufacturerId=mUserDetails.getmManufacturerId();
						mInparam = "0"+"/"+mManufacturerId+"/"+VerisupplierUtils.mShippingParam+"/1/20";
					}
					else if(mUserDetails.getmUserType().equalsIgnoreCase("Inspection")){
						String mManufacturerId=mUserDetails.getmManufacturerId();
						mInparam = "0"+"/"+mManufacturerId+"/"+VerisupplierUtils.mInspectionParam+"/1/20";
					}
					else if(mUserDetails.getmUserType().equalsIgnoreCase("Warehouse")){
						String mManufacturerId=mUserDetails.getmManufacturerId();
						mInparam = "0"+"/"+mManufacturerId+"/"+VerisupplierUtils.mWarehouseParam+"/1/20";
					}
				}
			}else{
				mInparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
			}
			
			//String Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
			String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetQuotation, "GetQuotation/", mInparam);
			if (resultOutparam == null || resultOutparam.length() == 0) {
				isValid=false;
			} else {
				quoatationDashboards = jsonParserClass.ParseDashboardQuaotation(resultOutparam);
				Helper helper = new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertDashboardQuotation(quoatationDashboards);
				helper.close();
				isValid=true;
			}
		}
		
		return isValid;
	}

}
