package com.mmadapps.verisupplier.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.UserSignPopup;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.OrderDashboard;
import com.mmadapps.verisupplier.beans.QuoatationDashboard;
import com.mmadapps.verisupplier.beans.QuotationDashboardHistory;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.NavDrawerItem;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.customservices.CustomOrderDetailsActivity;
import com.mmadapps.verisupplier.customservices.CustomQuotatinBuyerChatDetails;
import com.mmadapps.verisupplier.customservices.CustomQuotationManufacturerChatDetails;
import com.mmadapps.verisupplier.inspection.InspectionBuyerChatDetailsActivity;
import com.mmadapps.verisupplier.inspection.InspectionManufacturerChatDetails;
import com.mmadapps.verisupplier.inspection.InspectionOrderDetailsActivity;
import com.mmadapps.verisupplier.leftmenu.Myopenquoate;
import com.mmadapps.verisupplier.leftmenu.WishListActivity;
import com.mmadapps.verisupplier.me.UserMeActivity;
import com.mmadapps.verisupplier.products.ProductOrderDetailsActivity;
import com.mmadapps.verisupplier.shipping.ShippingBuyerChatDetailsActivity;
import com.mmadapps.verisupplier.shipping.ShippingManufactureChatDetailsActivity;
import com.mmadapps.verisupplier.shipping.ShippingOrderDetailsActivity;
import com.mmadapps.verisupplier.warehouse.WarehouseBuyerChatDetailsActivity;
import com.mmadapps.verisupplier.warehouse.WarehouseManufacturerChatDetailsActivity;
import com.mmadapps.verisupplier.warehouse.WarehouseOrderDetailsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class DashboardActivity extends BaseActionBarActivity {

	SwipeListView vL_QuotationList;
	ListView OrderList;
	QuotationListAdapter mQuotationAdapter;
	OrderListAdapter mOrderAdapter;
	private int mPosition = 0;
	private ProgressDialog pDialog;
	private String[] contactname = {"Antony","Jacobe","Martine","Luther","Kingso","David","Cameron"};
	private static ArrayList<String> flagedList = new ArrayList<String>();

	ArrayList<QuoatationDashboard> QuotationList;
	List<OrderDashboard> mOrderList=new ArrayList<OrderDashboard>();
	ArrayList<QuotationDashboardHistory> quotationDashboardHistories;
	ArrayList<QuotationDashboardHistory> orderDetailsDashboard;
	UserDetails mUserDetails=new UserDetails();
	WebServices webServices;
	JsonParserClass jsonParserClass;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	TextView vT_AD_qtnLable, vT_AD_qtnValue, vT_AD_dateTime,
			vT_AD_productNameLable, vT_AD_productNameValue, vT_AD_mfrNameLable,
			vT_AD_mfrNameValue, quotationstatus;

	List<QuoatationDashboard> mQuotationList;

	public static DrawerLayout mDrawerLayout;
	View lenear_view;
	ImageView imageViewon;
	public static ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	// nav drawer title
    private CharSequence mDrawerTitle;
 
    // used to store app title
    private CharSequence mTitle;
    
    String mOnClicktitle;
 
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
 
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    
    TextView vI_dq_scrollImage;
	
	private ProgressDialog pdLoading=null;
	String Inparam;
	
	List<CategoryDetails> mCategoryDetails=new ArrayList<CategoryDetails>();
	List<CountryDetails> mCounList=new ArrayList<CountryDetails>();
	List<CountryDetails> mStateList=new ArrayList<CountryDetails>();
	List<Lookups> mInspectionType=new ArrayList<Lookups>();
	List<Lookups> mIndustryList=new ArrayList<Lookups>();
	List<Lookups> mInspectorList=new ArrayList<Lookups>();
	List<Lookups> mUnitOfMeasurentList=new ArrayList<Lookups>();
	List<Lookups> mQuotationLookupList=new ArrayList<Lookups>();
	
	//time differeance 24 hours
	final double DIFFTIME=8.64e+7;
	
	//bottom layout for homepage,wishlist,dashboard,openquote,userprofile
	LinearLayout home_layout,wishlist_layout,dashboard_layout,openquote_layout,userprofile_layout;
	
	//bottombar imageview
	public static ImageView home_selected,home_notselected,wishlist_selected,wishlist_notselected,dashboard_selected,dashboard_notselected,openquote_selected,openquote_notselected,userprofile_selected,userprofile_notselected;
	
	String mType;
	
	//OnScroll Increase List
    private int pagenumber = 1;
    private final int MAX_ITEMS_PER_PAGE = 10;
    private boolean isloading = false;
    private MyTask task;
    int totalcount;
    int loadedItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_quotation);

		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.dashboardMenu.setVisibility(View.GONE);
		
		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.VISIBLE);
		BaseActionBarActivity.setmUserName("Get Quotation");
		
		mType=getIntent().getStringExtra("Type");
		VerisupplierUtils.mDashboardCurrentType=mType;
		 Log.e("From Activit", mType);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		initControl();
		UserDetails();
		
		if(mUserDetails.getmUserType()==null || mUserDetails.getmUserType().equalsIgnoreCase("null")){
        	
        }else{
        	if(mType.equalsIgnoreCase("dashboard")){
        		if(mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		
            		BaseActionBarActivity.dashboardmenuleft.setOnClickListener(new OnClickListener() {
            			@Override
            			public void onClick(View v) {
            				mDrawerLayout.openDrawer(mDrawerList);
            			}
            		});
            	}
        		else if(mUserDetails.getmUserType().equalsIgnoreCase("Manufacturer") || mUserDetails.getmUserType().equalsIgnoreCase("Product")){
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setClickable(false);
            		
            		BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
            	}
            	else if(mUserDetails.getmUserType().equalsIgnoreCase("Shipment")){
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setClickable(false);
            		
            		BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
            	}
            	else if(mUserDetails.getmUserType().equalsIgnoreCase("Customs")){
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setClickable(false);
            		
            		BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
            	}
            	else if(mUserDetails.getmUserType().equalsIgnoreCase("Inspection")){
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setClickable(false);
            		
            		BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
            	}
            	else if(mUserDetails.getmUserType().equalsIgnoreCase("Warehouse")){
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setClickable(false);
            		
            		BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.VISIBLE);
            	}
        	}
        	else if(mType.equalsIgnoreCase("order")){
        		if(mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		
            		BaseActionBarActivity.dashboardmenuleft.setOnClickListener(new OnClickListener() {
            			@Override
            			public void onClick(View v) {
            				mDrawerLayout.openDrawer(mDrawerList);
            			}
            		});
            	}
        		else if(mUserDetails.getmUserType().equalsIgnoreCase("Manufacturer") || mUserDetails.getmUserType().equalsIgnoreCase("Product")){
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setClickable(false);
            		
            		BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
            	}
            	else if(mUserDetails.getmUserType().equalsIgnoreCase("Shipment")){
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setClickable(false);
            		
            		BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
            	}
            	else if(mUserDetails.getmUserType().equalsIgnoreCase("Customs")){
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setClickable(false);
            		
            		BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
            	}
            	else if(mUserDetails.getmUserType().equalsIgnoreCase("Inspection")){
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setClickable(false);
            		
            		BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
            	}
            	else if(mUserDetails.getmUserType().equalsIgnoreCase("Warehouse")){
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setClickable(false);
            		
            		BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
            		BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.VISIBLE);
            	}
        	}
        	else{
            		BaseActionBarActivity.vBackView.setVisibility(View.GONE);
            		BaseActionBarActivity.dashboardmenuleft.setVisibility(View.VISIBLE);
            		
            		BaseActionBarActivity.dashboardmenuleft.setOnClickListener(new OnClickListener() {
            			@Override
            			public void onClick(View v) {
            				mDrawerLayout.openDrawer(mDrawerList);
            			}
            		});
        	}
        }
		createNavigationDrawer();
	}

	

	private void createNavigationDrawer() {
		 
		lenear_view=findViewById(R.id.lenear_view);
		imageViewon=(ImageView) findViewById(R.id.imageViewon);
		mTitle = mDrawerTitle = getTitle();
		
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        
        if(mUserDetails.getmUserType()==null || mUserDetails.getmUserType().equalsIgnoreCase("null")){
        }else{
        	if(mType.equals("dashboard") && mType.equalsIgnoreCase("order")){
        		if(mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
        			navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        			navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        			navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        			navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        			navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        			navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        		}else if(mUserDetails.getmUserType().equalsIgnoreCase("Shipment")){
        		}else if(mUserDetails.getmUserType().equalsIgnoreCase("Inspection")){
        		}else if(mUserDetails.getmUserType().equalsIgnoreCase("Customs")){
        		}else if(mUserDetails.getmUserType().equalsIgnoreCase("Warehouse")){
        		}else{
        		}
        	}else{
        		 navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
           		 navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
           		 navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
           		 navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
           		 navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
           		 navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        	}
        }
 
        navMenuIcons.recycle();
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
        		R.drawable.ic_launcher, //nav menu toggle icon
        		R.string.app_name, // nav drawer open - description for accessibility
        		R.string.app_name // nav drawer close - description for accessibility
        		){
        	public void onDrawerClosed(View view) {
        		getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
                imageViewon.setVisibility(View.INVISIBLE);
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
                imageViewon.setVisibility(View.INVISIBLE);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
        /*imageViewon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		});*/
        
       
        
        
		
	}

	private void initControl() {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(DashboardActivity.this));
		try {
			options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.ic_launcher)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
					.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
					.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		vL_QuotationList = (SwipeListView) findViewById(R.id.vL_QuotationList);
		
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vI_dq_scrollImage = (TextView) inflater.inflate(R.layout.footer, null);
        vL_QuotationList.addFooterView(vI_dq_scrollImage);
		
		jsonParserClass = new JsonParserClass();
		webServices = new WebServices();
		QuotationList = new ArrayList<QuoatationDashboard>();

		setQuotationList();
		vL_QuotationList.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onOpened(int position, boolean toRight) {
				super.onOpened(position, toRight);
				vL_QuotationList.openAnimate(position);
				if (position != mPosition)
					try {
						vL_QuotationList.closeAnimate(mPosition);
					} catch (Exception e) {
						e.printStackTrace();
					}
				mPosition = position;
			}
			@Override
			public void onClosed(int position, boolean fromRight) {
				super.onClosed(position, fromRight);
			}
			@Override
			public void onListChanged() {
				super.onListChanged();
			}
			@Override
			public void onMove(int position, float x) {
				super.onMove(position, x);
			}

			@Override
			public void onStartOpen(int position, int action,
				boolean right) {
				super.onStartOpen(position, action, right);
			}

			@Override
			public void onStartClose(int position, boolean right) {
				super.onStartClose(position, right);
			}

			@Override
			public void onClickFrontView(int position) {
				if(mType.equalsIgnoreCase("order")){
					// TODO Auto-generated method stub
					new OrderDetails().execute(mOrderList.get(position).getmOrder_Id());
				}else{
					new QuotationHistory().execute(QuotationList.get(position).getQuotation_Id());
				}
			}

			@Override
			public void onClickBackView(int position) {
				super.onClickBackView(position);
			}
		});
		
		vL_QuotationList.setOnScrollListener(new OnScrollListener() {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {    
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
                loadedItems = firstVisibleItem + visibleItemCount;
                if((loadedItems == totalItemCount) && !isloading){
                    
                    if(totalcount > loadedItems){
                        vI_dq_scrollImage.setVisibility(View.VISIBLE);
                        task = new MyTask();
                        task.execute();
                        SystemClock.sleep(1000);
                        isloading = true;    
                        pagenumber += 1;
                    }else{
                        vI_dq_scrollImage.setVisibility(View.GONE);
                    }
                    //if(task != null && (task.getStatus() == AsyncTask.Status.FINISHED)){
                        
                    //}
                }
                
            }
        });
		
		
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
				/*Intent homeIntent=new Intent(DashboardActivity.this,MarketPageActivity.class);
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
				home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				
				wishlist_selected.setVisibility(View.VISIBLE);
				wishlist_notselected.setVisibility(View.GONE);
				Intent homeIntent=new Intent(DashboardActivity.this,WishListActivity.class);
				startActivity(homeIntent);
				overridePendingTransition(0, 0);
				finish();
			}
		});
		
		dashboard_layout=(LinearLayout)findViewById(R.id.vL_bottombar_dashboard);
		dashboard_selected=(ImageView)findViewById(R.id.vI_bottombar_dashboardselected);
		dashboard_notselected=(ImageView)findViewById(R.id.vI_bottombar_dashboard_notselected);
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
				
				UserDetails();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(DashboardActivity.this,UserSignPopup.class);
					signinIntent.putExtra("FROMACTIVITY", "tradedashboard");
					startActivity(signinIntent);
					overridePendingTransition(0, 0);
					finish();
				}else{
					
					Intent intent=new Intent(DashboardActivity.this,com.mmadapps.verisupplier.trademanager.DashboardActivity.class);
					startActivity(intent);
					overridePendingTransition(0, 0);
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
				
				UserDetails();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(DashboardActivity.this,UserSignPopup.class);
					signinIntent.putExtra("FROMACTIVITY", "marketopenquote");
					startActivity(signinIntent);
					overridePendingTransition(0, 0);
					finish();
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
				
				home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				
				userprofile_selected.setVisibility(View.VISIBLE);
				userprofile_notselected.setVisibility(View.GONE);
				Intent homeIntent=new Intent(DashboardActivity.this,UserMeActivity.class);
				startActivity(homeIntent);
				overridePendingTransition(0, 0);
				finish();
				
				/*userprofile_selected.setVisibility(View.GONE);
				userprofile_notselected.setVisibility(View.VISIBLE);
				
				getUserDetails();
				location = new int[2];
				v.getLocationOnScreen(location);
				onWindowFocusChanged(true);
				ShowUserProfileBox(MarketPageActivity.mPoint);*/
			}
		});
	}
	
	
	private void UserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}


	public int convertDpToPixel(float dp) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}

	private class QuotationListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if(mType.equalsIgnoreCase("order")){
				if (mOrderList == null || mOrderList.size() == 0) {
					return 0;
				}
				return mOrderList.size();
			}else{
				if (QuotationList == null || QuotationList.size() == 0) {
					return 0;
				}
				return QuotationList.size();
			}
			
			
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.adapter_quotation, parent, false);
			}
				TextView vT_AQ_Name = (TextView) convertView.findViewById(R.id.vT_AQ_Name);
				TextView vT_AQ_manufacturerName = (TextView) convertView.findViewById(R.id.vT_AQ_manufacturerName);
				TextView vT_AQ_productName = (TextView) convertView.findViewById(R.id.vT_AQ_productName);
				TextView vT_AQ_time = (TextView) convertView.findViewById(R.id.vT_AQ_time);
				ImageView vI_AQ_buyerImage = (ImageView) convertView.findViewById(R.id.vI_AQ_buyerImage);
				TextView response_ststus=(TextView)convertView.findViewById(R.id.vT_AQ_status);
				TextView quotation_number=(TextView)convertView.findViewById(R.id.vT_aq_quotationnumber);
				
				if(mType.equalsIgnoreCase("order")){
					String personName =  mOrderList.get(position).getmOrder_Buyername();
					if(personName == null || personName.length() == 0){
					Random rnd = new Random();
					int i = rnd.nextInt((contactname.length)-1);
					personName = contactname[i];
					}else{
					}
					vT_AQ_Name.setText(""+ personName);
					
					vT_AQ_manufacturerName.setText(""+ mOrderList.get(position).getmOrder_Manufacturname());
					vT_AQ_productName.setText(""+ mOrderList.get(position).getmOrder_Productname());
					vT_AQ_time.setText(""+ mOrderList.get(position).getmOrder_Date());

					imageLoader.displayImage(mOrderList.get(position).getmOrder_ProductImage(), vI_AQ_buyerImage, options);

					final String qid = mOrderList.get(position).getmOrder_Id();
					ImageView vI_AQ_flag = (ImageView) convertView.findViewById(R.id.vI_AQ_flag);
					final ImageView vI_AQ_flagImage = (ImageView) convertView.findViewById(R.id.vI_AQ_flagImage);

					if (flagedList == null || flagedList.size() == 0 || flagedList.contains(qid)) {
						vI_AQ_flagImage.setVisibility(View.INVISIBLE);
					}
					
					quotation_number.setText("Order Number:"+mOrderList.get(position).getmOrder_Number());
					response_ststus.setVisibility(View.GONE);
					
				}else{
					String personName =  QuotationList.get(position).getQuotation_buyername();
					if(personName == null || personName.length() == 0){
					Random rnd = new Random();
					int i = rnd.nextInt((contactname.length)-1);
					personName = contactname[i];
					}else{
					}
					vT_AQ_Name.setText(""+ personName);
					vT_AQ_manufacturerName.setText(""+ QuotationList.get(position).getQuotation_manufacturename());
					vT_AQ_productName.setText(""+ QuotationList.get(position).getQuotation_productname());
					vT_AQ_time.setText(""+ QuotationList.get(position).getmQuotation_Datetime());

					imageLoader.displayImage(QuotationList.get(position).getmProduct_Image(), vI_AQ_buyerImage, options);

					final String qid = QuotationList.get(position).getQuotation_Id();
					ImageView vI_AQ_flag = (ImageView) convertView.findViewById(R.id.vI_AQ_flag);
					final ImageView vI_AQ_flagImage = (ImageView) convertView.findViewById(R.id.vI_AQ_flagImage);

					if (flagedList == null || flagedList.size() == 0 || flagedList.contains(qid)) {
						vI_AQ_flagImage.setVisibility(View.INVISIBLE);
					}
					
					if(QuotationList.get(position).getmQuotationStatusId().equalsIgnoreCase("5")){
						response_ststus.setText("Accept");
						response_ststus.setTextColor(getResources().getColor(R.color.price_color));
					}else if(QuotationList.get(position).getmQuotationStatusId().equalsIgnoreCase("3")){
						response_ststus.setText("Rejected");
						response_ststus.setTextColor(getResources().getColor(R.color.red_wishlist));
					}else{
						response_ststus.setText("Draft");
						response_ststus.setTextColor(getResources().getColor(R.color.orange));
					}
					
					quotation_number.setText("Quotation Number:"+QuotationList.get(position).getmQuotation_Number());

					vI_AQ_flag.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (flagedList.contains(qid)) {
								flagedList.remove(qid);
								vI_AQ_flagImage.setVisibility(View.INVISIBLE);
							} else {
								flagedList.add(qid);
								vI_AQ_flagImage.setVisibility(View.VISIBLE);
							}
						}
					});
				}
				

				
			return convertView;
		}
	}

	private class QuotationHistory extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(DashboardActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.show();
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			getQuantityList();
			return getQuotaionHistory(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pDialog != null || pDialog.isShowing())
				pDialog.cancel();
			if (result) {
				forwardToQuotationDetailsActivity();
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
	public void onResume() {
		if(pdLoading != null && pdLoading.isShowing()){
			pdLoading.cancel();
		}
		VerisupplierUtils.mQuotationLookups = new ArrayList<Lookups>();
		super.onResume();
	}

	public Boolean getQuotaionHistory(String quoatation_id) {
		Boolean isValidHistory = false;

		if (isValidHistory.equals(false)) {
			Log.e("Quotation Id", quoatation_id);
			String resultOutparam = webServices.CallWebHTTPBindingService(
					ApiType.GetQuotationHistory, "GetQuotationHistory/",
					quoatation_id);
			if (resultOutparam == null || resultOutparam.length() == 0) {
				isValidHistory = false;
			} else {
				quotationDashboardHistories = jsonParserClass.parseAllQuotationHistory(resultOutparam);
				if (quotationDashboardHistories == null|| quotationDashboardHistories.size() == 0) {
					isValidHistory = false;
				} else {
					isValidHistory = true;
					Helper helper = new Helper(DashboardActivity.this);
					helper.openDataBase();
					helper.insertQuotationHistory(quotationDashboardHistories);
					helper.close();
				}
			}
		} else {
			isValidHistory = false;
		}

		return isValidHistory;
	}

	
	
	private void setQuotationList() {
		mQuotationList = new ArrayList<QuoatationDashboard>();
		if(mType.equalsIgnoreCase("order")){
			Helper helper = new Helper(DashboardActivity.this);
			helper.openDataBase();
			mOrderList = helper.getDashboardOrder();
			helper.close();
			
			//mOrderAdapter=new OrderListAdapter();
			//vL_QuotationList.setAdapter(mOrderAdapter);
			
		}else{
			Helper helper = new Helper(DashboardActivity.this);
			helper.openDataBase();
			QuotationList = helper.getDashboardQuotation();
			helper.close();
			
			//totalcount = Integer.parseInt(QuotationList.get(0).getmTotalCount());
	        //Log.e("Total Count", ""+totalcount);
			//mQuotationAdapter = new QuotationListAdapter();
			//vL_QuotationList.setAdapter(mQuotationAdapter);
		}
		
		mQuotationAdapter = new QuotationListAdapter();
		vL_QuotationList.setAdapter(mQuotationAdapter);
		
		/*for (int i = 0; i < QuotationList.size(); i++) {
			mQuotationList = new ArrayList<QuoatationDashboard>();
		}*/
		
		

	}
	
	public class NavDrawerListAdapter extends BaseAdapter {
	    
		   private Context context;
		   private ArrayList<NavDrawerItem> navDrawerItems;
		   
		    
		   public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		       this.context = context;
		       this.navDrawerItems = navDrawerItems;
		   }

		   @Override
		   public int getCount() {
		       return navDrawerItems.size();
		   }

		   @Override
		   public Object getItem(int position) {       
		       return navDrawerItems.get(position);
		   }

		   @Override
		   public long getItemId(int position) {
		       return position;
		   }

		  

		   @Override
		   public View getView(final int position, View convertView, ViewGroup parent) {
			   if (convertView == null) {
				   LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				   convertView = mInflater.inflate(R.layout.naviitems, null);
			   }
			   
			   ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
			   TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
			   
			   
			   imgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
			   txtTitle.setText(navDrawerItems.get(position).getTitle());
		    
			   convertView.setOnClickListener(new OnClickListener() {
				
				   @Override
				   public void onClick(View v) {
					   if(navDrawerItems.get(position).getTitle().equalsIgnoreCase("product")){
						   VerisupplierUtils.mDashboardServiceGroupType="product";
						   mOnClicktitle = navDrawerItems.get(position).getTitle();
						   BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.VISIBLE);
						   BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
						   new AsyncDashboard().execute();
					   }else if(navDrawerItems.get(position).getTitle().equalsIgnoreCase("shipping")){
						   VerisupplierUtils.mDashboardServiceGroupType="shipping";
						   mOnClicktitle = navDrawerItems.get(position).getTitle();
						   BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.VISIBLE);
						   BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
						   new AsyncDashboard().execute();
					   }else if(navDrawerItems.get(position).getTitle().equalsIgnoreCase("inspection")){
						   VerisupplierUtils.mDashboardServiceGroupType="inspection";
						   mOnClicktitle = navDrawerItems.get(position).getTitle();
						   BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.VISIBLE);
						   BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
						   new AsyncDashboard().execute();
					   }else if(navDrawerItems.get(position).getTitle().equalsIgnoreCase("custom")){
						   VerisupplierUtils.mDashboardServiceGroupType="custom";
						   mOnClicktitle = navDrawerItems.get(position).getTitle();
						   BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.VISIBLE);
						   BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.GONE);
						   new AsyncDashboard().execute();
					   }else if(navDrawerItems.get(position).getTitle().equalsIgnoreCase("warehouse")){
						   VerisupplierUtils.mDashboardServiceGroupType="warehouse";
						   mOnClicktitle = navDrawerItems.get(position).getTitle();
						   BaseActionBarActivity.vI_ab_dashboardshippingMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardinspectionMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardcustomsMenu.setVisibility(View.GONE);
						   BaseActionBarActivity.vI_ab_dashboardwarehouseMenu.setVisibility(View.VISIBLE);
						   new AsyncDashboard().execute();
					   }
						DashboardActivity.mDrawerLayout.closeDrawer(DashboardActivity.mDrawerList);
				   }
			   });
		   
			   
			   return convertView;
		   }

		}
	
	private class AsyncDashboard extends AsyncTask<Void, Void, Boolean>{

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
		protected Boolean doInBackground(Void... params) {
			
			if(mOnClicktitle.equalsIgnoreCase("product")){
				getQuantityList();
				getQuotationLookups();
			}
			else if(mOnClicktitle.equalsIgnoreCase("shipping")){
				downloadCountries();
				downloadStates();
			}
			else if(mOnClicktitle.equalsIgnoreCase("inspection")){
				downloadCountries();
				downloadStates();
				downloadTypeOfIndustry();
				downloadInspectorType();
				downloadInspectionTypes();
			}
			else if(mOnClicktitle.equalsIgnoreCase("custom")){
				downloadAllCategories();
				getQuantityList();
				downloadCountries();
				downloadStates();
			}
			else if(mOnClicktitle.equalsIgnoreCase("warehouse")){
				
			}
			
			/*downloadAllCategories();
			downloadCountries();
			downloadStates();
			downloadInspectorType();
			downloadTypeOfIndustry();
			downloadInspectionTypes();
			getQuantityList();*/
			//getAllOrderList();
			return getShippingQuotation();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pdLoading.cancel();
			if(pdLoading.isShowing())
				pdLoading.dismiss();
			if(result){
				setQuotationList();
			}else{
			}
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
	
	/*private void getAllOrderList() {
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
	}*/
	
	
	private Boolean getShippingQuotation() {
		Boolean isValid = false;
		if (isValid.equals(false)) {
			if(mType.equalsIgnoreCase("dashboard") ){
				if(mUserDetails.getmUserType()==null){
					
				}else{
					if(mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("product")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
					}else if(mUserDetails.getmUserType().equalsIgnoreCase("Manufacturer") && mOnClicktitle.equalsIgnoreCase("product") && mUserDetails.getmUserType().equalsIgnoreCase("Product")){
						Inparam = "0/"+mUserDetails.getmManufacturerId()+"/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
					}
					
					if(mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("shipping")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mShippingParam+"/1/20";
					}else if(mUserDetails.getmUserType().equalsIgnoreCase("Shipment") && mOnClicktitle.equalsIgnoreCase("shipping")){
						Inparam = "0/"+mUserDetails.getmManufacturerId()+"/"+VerisupplierUtils.mShippingParam+"/1/20";
					}
					
					if(mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("inspection")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mInspectionParam+"/1/20";
					}else if( mUserDetails.getmUserType().equalsIgnoreCase("Inspection") && mOnClicktitle.equalsIgnoreCase("inspection")){
						Inparam = "0/"+mUserDetails.getmManufacturerId()+"/"+VerisupplierUtils.mInspectionParam+"/1/20";
					}
					
					
					if( mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("custom")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mCustomsParam+"/1/20";
					}else if(mUserDetails.getmUserType().equalsIgnoreCase("Customs") && mOnClicktitle.equalsIgnoreCase("custom")){
						Inparam = "0/"+mUserDetails.getmManufacturerId()+"/"+VerisupplierUtils.mCustomsParam+"/1/20";
					}
					
					if( mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("warehouse")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mWarehouseParam+"/1/20";
					}else if(mUserDetails.getmUserType().equalsIgnoreCase("Warehouse") && mOnClicktitle.equalsIgnoreCase("warehouse")){
						Inparam = "0/"+mUserDetails.getmManufacturerId()+"/"+VerisupplierUtils.mWarehouseParam+"/1/20";
					}
				}
			}else if(mType.equalsIgnoreCase("order")){
				if(mUserDetails.getmUserType()==null){
				}else{
					if(mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("product")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
					}else if(mUserDetails.getmUserType().equalsIgnoreCase("Manufacturer") && mOnClicktitle.equalsIgnoreCase("product") && mUserDetails.getmUserType().equalsIgnoreCase("Product")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
					}
					
					if(mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("shipping")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mShippingParam+"/1/20";
					}else if(mUserDetails.getmUserType().equalsIgnoreCase("Shipment") && mOnClicktitle.equalsIgnoreCase("shipping")){
						Inparam = mUserDetails.getmManufacturerId()+"/0/"+VerisupplierUtils.mShippingParam+"/1/20";
					}
					
					if(mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("inspection")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mInspectionParam+"/1/20";
					}else if( mUserDetails.getmUserType().equalsIgnoreCase("Inspection") && mOnClicktitle.equalsIgnoreCase("inspection")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mInspectionParam+"/1/20";
					}
					
					if( mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("custom")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mCustomsParam+"/1/20";
					}else if(mUserDetails.getmUserType().equalsIgnoreCase("Customs") && mOnClicktitle.equalsIgnoreCase("custom")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mCustomsParam+"/1/20";
					}
					
					if( mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("warehouse")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mWarehouseParam+"/1/20";
					}else if(mUserDetails.getmUserType().equalsIgnoreCase("Warehouse") && mOnClicktitle.equalsIgnoreCase("warehouse")){
						Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mWarehouseParam+"/1/20";
					}
				}
			}
			else{
				if(mOnClicktitle.equalsIgnoreCase("product")){
					Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
				}
				else if(mOnClicktitle.equalsIgnoreCase("shipping")){
					Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mShippingParam+"/1/20";
				}
				else if(mOnClicktitle.equalsIgnoreCase("custom")){
					Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mCustomsParam+"/1/20";
				}
				else if(mOnClicktitle.equalsIgnoreCase("inspection")){
					Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mInspectionParam+"/1/20";
				}
				else if(mOnClicktitle.equalsIgnoreCase("warehouse")){
					Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mWarehouseParam+"/1/20";
				}
			}
			
			if(mType.equalsIgnoreCase("order")){
				String mOutputParam=webServices.CallWebHTTPBindingService(ApiType.GetOrder, "GetOrder/", Inparam);
				if(mOutputParam==null || mOutputParam.length()==0){
					isValid = false;
				}else{
					mOrderList=jsonParserClass.ParseDashboardOrder(mOutputParam);
					if(mOrderList==null || mOrderList.size()==0){
					}else{
						Helper helper=new Helper(getApplicationContext());
						helper.openDataBase();
						helper.insertdashboardOrderValues(mOrderList);
						helper.close();
						isValid = true;
					}
				}
			}else{
				String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetQuotation, "GetQuotation/", Inparam);
				if (resultOutparam == null || resultOutparam.length() == 0) {
					isValid = false;
				} else {
					ArrayList<QuoatationDashboard> quoatationDashboards = jsonParserClass.ParseDashboardQuaotation(resultOutparam);
					Helper helper = new Helper(getApplicationContext());
					helper.openDataBase();
					helper.insertDashboardQuotation(quoatationDashboards);
					helper.close();
					isValid = true;
				}
			}
			
		} else {
			isValid = false;
		}
		return isValid;
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
	
	private void downloadInspectorType() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mInspectorList=helper.getTypeOfInspector();
		if(mIndustryList == null || mIndustryList.size()==0){
			downloadInspectorList();
		}else if(checkdownloadTime(mIndustryList.get(0).getmLastDownloadTime())){
			
		}else{
			downloadInspectorType();
		}
		helper.close();
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
	
	
	
	private void downloadInspectionTypes() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mInspectionType=helper.getInspectionType();
		if(mInspectionType == null || mInspectionType.size()==0){
			downloadInspectionList();
		}else if(checkdownloadTime(mInspectionType.get(0).getmLastDownloadTime())){
			
		}else{
			downloadInspectionTypes();
		}
		helper.close();
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
	
	private class AsyncCategoryDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
	
	public void forwardToQuotationDetailsActivity() {
		Helper helper = new Helper(DashboardActivity.this);
		helper.openDataBase();
		UserDetails ud = helper.getUserDetails();
		helper.close();
		Intent intent = null;
		if(mType.equalsIgnoreCase("dashboard")){
			if(ud.getmUserType() == null || ud.getmUserType().length()==0){
			}else{
				if(ud.getmUserType().equalsIgnoreCase("Customer")){
					if(mOnClicktitle==null || mOnClicktitle.length()==0){
						intent = new Intent(DashboardActivity.this, ProductQuotationBuyerDetailActivity.class);
					}else{
						if(mOnClicktitle.equalsIgnoreCase("product")){
							intent = new Intent(DashboardActivity.this, ProductQuotationBuyerDetailActivity.class);
						}else if(mOnClicktitle.equalsIgnoreCase("shipping")){
							intent = new Intent(DashboardActivity.this, ShippingBuyerChatDetailsActivity.class);
						}else if(mOnClicktitle.equalsIgnoreCase("inspection")){
							intent = new Intent(DashboardActivity.this, InspectionBuyerChatDetailsActivity.class);
						}else if(mOnClicktitle.equalsIgnoreCase("custom")){
							intent = new Intent(DashboardActivity.this, CustomQuotatinBuyerChatDetails.class);
						}else if(mOnClicktitle.equalsIgnoreCase("warehouse")){
							intent = new Intent(DashboardActivity.this, WarehouseBuyerChatDetailsActivity.class);
						}
					}
				}
				else if(ud.getmUserType() == null || ud.getmUserType().equalsIgnoreCase("Manufacturer") || mUserDetails.getmUserType().equalsIgnoreCase("Product")){
					intent = new Intent(DashboardActivity.this, ProductQuotationManufacturerDetailsActivity.class);
				}
				else if(ud.getmUserType() == null || ud.getmUserType().equalsIgnoreCase("Customs")){
					intent = new Intent(DashboardActivity.this, CustomQuotationManufacturerChatDetails.class);
				}
				else if(ud.getmUserType() == null || ud.getmUserType().equalsIgnoreCase("Shipment")){
					intent = new Intent(DashboardActivity.this, ShippingManufactureChatDetailsActivity.class);
				}
				else if(ud.getmUserType() == null || ud.getmUserType().equalsIgnoreCase("Inspection")){
					intent = new Intent(DashboardActivity.this, InspectionManufacturerChatDetails.class);
				}
				else if(ud.getmUserType() == null || ud.getmUserType().equalsIgnoreCase("Warehouse")){
					intent = new Intent(DashboardActivity.this, WarehouseManufacturerChatDetailsActivity.class);
				}
			}
		}else{
			if(mOnClicktitle==null || mOnClicktitle.length()==0){
				intent = new Intent(DashboardActivity.this, ProductQuotationBuyerDetailActivity.class);
			}else if(mOnClicktitle.equalsIgnoreCase("product")){
				intent = new Intent(DashboardActivity.this, ProductQuotationBuyerDetailActivity.class);
			}
			else if(mOnClicktitle.equalsIgnoreCase("shipping")){
				intent = new Intent(DashboardActivity.this, ShippingBuyerChatDetailsActivity.class);
			}
			else if(mOnClicktitle.equalsIgnoreCase("inspection")){
				intent = new Intent(DashboardActivity.this, InspectionBuyerChatDetailsActivity.class);
			}
			else if(mOnClicktitle.equalsIgnoreCase("custom")){
				intent = new Intent(DashboardActivity.this, CustomQuotatinBuyerChatDetails.class);
			}
			else if(mOnClicktitle.equalsIgnoreCase("warehouse")){
				intent = new Intent(DashboardActivity.this, WarehouseBuyerChatDetailsActivity.class);
			}
		}
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
	
	
	private class OrderDetails extends AsyncTask<String, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdLoading = new ProgressDialog(DashboardActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			
		}

		@Override
		protected Boolean doInBackground(String... params) {
			return getOrderDetails(params[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			if (result) {
				forwardToOrderDetailsActivity();
			}
		}
	}

	public Boolean getOrderDetails(String order_id) {
		Boolean isValidHistory = false;

		if (isValidHistory.equals(false)) {
			Log.e("Order Id", order_id);
			String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetOrderDetail, "GetOrderDetail/",order_id);
			if (resultOutparam == null || resultOutparam.length() == 0) {
				isValidHistory = false;
			} else {
				VerisupplierUtils.mOrderDetails=jsonParserClass.parseOrderDetailsResult(resultOutparam);
				isValidHistory=true;
				/*orderDetailsDashboard = jsonParserClass.parseOrderDetailsDashboard(resultOutparam);
				if (orderDetailsDashboard == null|| orderDetailsDashboard.size() == 0) {
					isValidHistory = false;
				} else {
					
					isValidHistory = true;
					Helper helper = new Helper(DashboardActivity.this);
					helper.openDataBase();
					helper.insertQuotationHistory(orderDetailsDashboard);
					helper.close();
				}*/
			}
		} else {
			isValidHistory = false;
		}

		return isValidHistory;
	}



	public void forwardToOrderDetailsActivity() {
		getUserId();
		if(mUserDetails.getmUserType()==null){
		}else{
			if(mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
				if(mOnClicktitle==null || mOnClicktitle.length()==0){
					Intent orderIntent=new Intent(DashboardActivity.this,ProductOrderDetailsActivity.class);
					startActivity(orderIntent);
					overridePendingTransition(0, 0);
				}
				else if(mOnClicktitle.equalsIgnoreCase("product")){
					Intent orderIntent=new Intent(DashboardActivity.this,ProductOrderDetailsActivity.class);
					startActivity(orderIntent);
					overridePendingTransition(0, 0);
				}
				else if(mOnClicktitle.equalsIgnoreCase("shipping")){
					Intent orderIntent=new Intent(DashboardActivity.this,ShippingOrderDetailsActivity.class);
					startActivity(orderIntent);
					overridePendingTransition(0, 0);
				}
				else if(mOnClicktitle.equalsIgnoreCase("inspection")){
					Intent orderIntent=new Intent(DashboardActivity.this,InspectionOrderDetailsActivity.class);
					startActivity(orderIntent);
					overridePendingTransition(0, 0);
				}
				else if(mOnClicktitle.equalsIgnoreCase("custom")){
					Intent orderIntent=new Intent(DashboardActivity.this,CustomOrderDetailsActivity.class);
					startActivity(orderIntent);
					overridePendingTransition(0, 0);
				}
				else if(mOnClicktitle.equalsIgnoreCase("warehouse")){
					Intent orderIntent=new Intent(DashboardActivity.this,WarehouseOrderDetailsActivity.class);
					startActivity(orderIntent);
					overridePendingTransition(0, 0);
				}
			}
			else if(mUserDetails.getmUserType().equalsIgnoreCase("Manufacturer") || mUserDetails.getmUserType().equalsIgnoreCase("Product")){
				Intent orderIntent=new Intent(DashboardActivity.this,ProductOrderDetailsActivity.class);
				startActivity(orderIntent);
				overridePendingTransition(0, 0);
			}
			else if(mUserDetails.getmUserType().equalsIgnoreCase("Customs")){
				Intent orderIntent=new Intent(DashboardActivity.this,CustomOrderDetailsActivity.class);
				startActivity(orderIntent);
				overridePendingTransition(0, 0);
			}
			else if(mUserDetails.getmUserType().equalsIgnoreCase("Shipment")){
				Intent orderIntent=new Intent(DashboardActivity.this,ShippingOrderDetailsActivity.class);
				startActivity(orderIntent);
				overridePendingTransition(0, 0);
			}
			else if(mUserDetails.getmUserType().equalsIgnoreCase("Inspection")){
				Intent orderIntent=new Intent(DashboardActivity.this,InspectionOrderDetailsActivity.class);
				startActivity(orderIntent);
				overridePendingTransition(0, 0);
			}
			else if(mUserDetails.getmUserType().equalsIgnoreCase("Warehouse")){
				Intent orderIntent=new Intent(DashboardActivity.this,WarehouseOrderDetailsActivity.class);
				startActivity(orderIntent);
				overridePendingTransition(0, 0);
			}
		}
	}



	private void getUserId() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}
	
	private class OrderListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(mOrderList==null || mOrderList.size()==0){
				return 0;
			}else{
				return mOrderList.size();
			}
			
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.adapter_common_orderlist, parent, false);
			}
			
			ImageView product_image=(ImageView)convertView.findViewById(R.id.vI_aol_productimage);
			TextView product_name=(TextView)convertView.findViewById(R.id.vT_aol_productnanme);
			TextView manufacturer_name=(TextView)convertView.findViewById(R.id.vT_aol_manufacturename);
			TextView order_number=(TextView)convertView.findViewById(R.id.vT_aol_orderNumber);
			TextView ordered_date=(TextView)convertView.findViewById(R.id.vT_aol_orderdon);
			
			imageLoader.displayImage(mOrderList.get(position).getmOrder_ProductImage(), product_image, options);
			product_name.setText(""+ mOrderList.get(position).getmOrder_Productname());
			manufacturer_name.setText(""+ mOrderList.get(position).getmOrder_Manufacturname());
			ordered_date.setText(""+ mOrderList.get(position).getmOrder_Date());
		
			/*final String qid = mOrderList.get(position).getmOrder_Id();
			ImageView vI_AQ_flag = (ImageView) convertView.findViewById(R.id.vI_AQ_flag);
			final ImageView vI_AQ_flagImage = (ImageView) convertView.findViewById(R.id.vI_AQ_flagImage);

			if (flagedList == null || flagedList.size() == 0 || flagedList.contains(qid)) {
				vI_AQ_flagImage.setVisibility(View.INVISIBLE);
			}*/
			
			order_number.setText("OD No."+mOrderList.get(position).getmOrder_Number());
			
			return convertView;
		}
		
	}
	
	public class MyTask extends AsyncTask<String, Void, Boolean> {

        Boolean IsQuotation = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if(mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
                getQuantityList();
                getQuotationLookups();
            }
            else if(mUserDetails.getmUserType().equalsIgnoreCase("Manufacturer")){
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
               
               if(mOnClicktitle==null || mOnClicktitle.length()==0){
                   
               }else{
                 return getQuotationOtherService();
               }
               
               return IsQuotation = getQuotation();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                isloading = false;
               // mQuotationAdapter.notifyDataSetChanged();
                setQuotationList();
            }else{
            }
        }
    }

    public Boolean getQuotation(){
        Boolean isValid = false;
        if (isValid.equals(false)) {
            if(mType.equalsIgnoreCase("dashboard")){
                if(mUserDetails.getmUserType() == null || mUserDetails.getmUserType().equalsIgnoreCase("Customer")){
                	
                	
                    Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/"+pagenumber+"/"+"20";
                }else if(mUserDetails.getmUserType() == null || mUserDetails.getmUserType().equalsIgnoreCase("Manufacturer")){
                    Inparam = "0/"+mUserDetails.getmManufacturerId()+"/"+VerisupplierUtils.mProductServiceGroupType+"/"+pagenumber+"/20";
                }
                
            }else{
                if(mOnClicktitle.equalsIgnoreCase("product")){
                    Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mProductServiceGroupType+"/1/20";
                    
                }
                else if(mOnClicktitle.equalsIgnoreCase("shipping")){
                    Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mShippingParam+"/1/20";
                    
                }
                else if(mOnClicktitle.equalsIgnoreCase("custom")){
                    Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mCustomsParam+"/1/20";
                    
                }
                else if(mOnClicktitle.equalsIgnoreCase("inspection")){
                    Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mInspectionParam+"/1/20";
                    
                }
                else if(mOnClicktitle.equalsIgnoreCase("warehouse")){
                    Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mWarehouseParam+"/1/20";
                    
                }    
            }
            
            String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetQuotation, "GetQuotation/", Inparam);
            if (resultOutparam == null || resultOutparam.length() == 0) {
                isValid = false;
            } else {
                ArrayList<QuoatationDashboard> quoatationDashboards = jsonParserClass.ParseDashboardQuaotation(resultOutparam);
                QuotationList.addAll(quoatationDashboards);
                
                Helper helper = new Helper(getApplicationContext());
                helper.openDataBase();
                helper.insertDashboardQuotation(QuotationList);
                helper.close();
                isValid = true;
            }
        } else {
            isValid = false;
        }
        return isValid;
        
    }

    public Boolean getQuotationOtherService() {
        Boolean isValid = false;
        if (isValid.equals(false)){
        
        if(mUserDetails.getmUserType() == null || mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("shipping") ){
            Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mShippingParam+"/"+pagenumber+"/"+"20";
        }else if(mUserDetails.getmUserType() == null || mUserDetails.getmUserType().equalsIgnoreCase("Shipment")){
            Inparam = "0/"+mUserDetails.getmManufacturerId()+"/"+VerisupplierUtils.mShippingParam+"/"+pagenumber+"/20";
            
        }
        
        if(mUserDetails.getmUserType() == null || mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("inspection")){
            Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mInspectionParam+"/"+pagenumber+"/20";
        }else if(mUserDetails.getmUserType() == null || mUserDetails.getmUserType().equalsIgnoreCase("Inspection") ){
            Inparam = "0/"+mUserDetails.getmManufacturerId()+"/"+VerisupplierUtils.mInspectionParam+"/"+pagenumber+"/20";
        }
        
        
        if(mUserDetails.getmUserType() == null || mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("custom")){
            Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mCustomsParam+"/1/20";
        }else if(mUserDetails.getmUserType() == null || mUserDetails.getmUserType().equalsIgnoreCase("Customs") ){
            Inparam = "0/"+mUserDetails.getmManufacturerId()+"/"+VerisupplierUtils.mCustomsParam+"/1/20";
        }
        
        if(mUserDetails.getmUserType() == null || mUserDetails.getmUserType().equalsIgnoreCase("Customer") && mOnClicktitle.equalsIgnoreCase("warehouse")){
            Inparam = mUserDetails.getmUserID()+"/0/"+VerisupplierUtils.mWarehouseParam+"/1/20";
        }else if(mUserDetails.getmUserType() == null || mUserDetails.getmUserType().equalsIgnoreCase("Warehouse") ){
            Inparam = "0/"+mUserDetails.getmManufacturerId()+"/"+VerisupplierUtils.mWarehouseParam+"/1/20";
        }
        
        String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetQuotation, "GetQuotation/", Inparam);
        if (resultOutparam == null || resultOutparam.length() == 0) {
            isValid = false;
        } else {
            ArrayList<QuoatationDashboard> quoatationDashboards = jsonParserClass.ParseDashboardQuaotation(resultOutparam);
            QuotationList.addAll(quoatationDashboards);
            
            Helper helper = new Helper(getApplicationContext());
            helper.openDataBase();
            helper.insertDashboardQuotation(QuotationList);
            helper.close();
            isValid = true;
        }
    } else{
      isValid = false;
          }
        return isValid;
        
    }
	
	
}
