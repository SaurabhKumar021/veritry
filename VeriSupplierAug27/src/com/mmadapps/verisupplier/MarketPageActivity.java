package com.mmadapps.verisupplier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.ManufacturerList;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.ProductList;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.beans.WishlistDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.mmadapps.verisupplier.leftmenu.Myopenquoate;
import com.mmadapps.verisupplier.leftmenu.WishListActivity;
import com.mmadapps.verisupplier.login.SigninPageActivity;
import com.mmadapps.verisupplier.me.UserMeActivity;
import com.mmadapps.verisupplier.search.SearchActivity;
import com.mmadapps.verisuppliernew.products.ProductListActivity;

public class MarketPageActivity extends Activity implements OnClickListener{
	
	//imageview for products,inscpction,shipping,customs,warehouse and ecredit
	ImageView product_image,inspection_image,shipping_image,customs_image,warehouse_image,ecredit_image,search_image,qrcode_image;
	
	//from databse
	UserDetails mUserDetails=new UserDetails();
	List<CategoryDetails> mCategoryDetails=new ArrayList<CategoryDetails>();
	List<ProductList> mProductList=new ArrayList<ProductList>();
	public static List<ManufacturerList> mManufacturerList=new ArrayList<ManufacturerList>();
	List<Lookups> mUnitOfMeasurentList=new ArrayList<Lookups>();
	
	//services
	WebServices webServices;
	JsonParserClass jsonParserClass;
	String mInputParam;
	String result;
	
	//progress dialog
	private ProgressDialog pdLoading=null;
	
	//time differeance 24 hours
	final double DIFFTIME=8.64e+7;
	
	//network connector
	ConnectionDetector mDetector;
	
	//qrcode scan
	String contents="";
	String mProductId="";
	List<ProductDetails> mProductDetails=new ArrayList<ProductDetails>();
	
	//bottom layout for homepage,wishlist,dashboard,openquote,userprofile
	LinearLayout home_layout,wishlist_layout,tradedashboard_layout,openquote_layout,userprofile_layout;
	
	//bottombar imageview
	public static ImageView home_selected,home_notselected,wishlist_selected,wishlist_notselected,tradedashboard_selected,tradedashboard_notselected,openquote_selected,openquote_notselected,userprofile_selected,userprofile_notselected;
	
	//bottombar variables
	String Inparam;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_homepage);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		initializeView();
	}

	private void initializeView() {
		mDetector=new ConnectionDetector(getApplicationContext());
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		
		product_image=(ImageView)findViewById(R.id.vI_vshp_products);
		product_image.setOnClickListener(this);
		
		inspection_image=(ImageView)findViewById(R.id.vI_vshp_inspections);
		inspection_image.setOnClickListener(this);
		
		shipping_image=(ImageView)findViewById(R.id.vI_vshp_shipping);
		shipping_image.setOnClickListener(this);
		
		customs_image=(ImageView)findViewById(R.id.vI_vshp_customs);
		customs_image.setOnClickListener(this);
		
		warehouse_image=(ImageView)findViewById(R.id.vI_vshp_warehouse);
		warehouse_image.setOnClickListener(this);
		
		ecredit_image=(ImageView)findViewById(R.id.vI_vshp_ecredit);
		ecredit_image.setOnClickListener(this);
		
		search_image=(ImageView)findViewById(R.id.vI_vshp_search);
		search_image.setOnClickListener(this);
		
		qrcode_image=(ImageView)findViewById(R.id.vI_vshp_qrcode);
		qrcode_image.setOnClickListener(this);
		
		home_layout=(LinearLayout)findViewById(R.id.vL_bottombar_home);
		home_selected=(ImageView)findViewById(R.id.vI_bottombar_homeselected);
		home_notselected=(ImageView)findViewById(R.id.vI_bottombar_home_notselected);
		home_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		wishlist_layout=(LinearLayout)findViewById(R.id.vL_bottombar_wishlist);
		wishlist_selected=(ImageView)findViewById(R.id.vI_bottombar_wishselected);
		wishlist_notselected=(ImageView)findViewById(R.id.vI_bottombar_wish_notselected);
		wishlist_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getUserId();
				home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				wishlist_selected.setVisibility(View.VISIBLE);
				wishlist_notselected.setVisibility(View.GONE);
				
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(MarketPageActivity.this,SigninPageActivity.class);
                    signinIntent.putExtra("FROMACTIVITY", "wishlist");
                    startActivity(signinIntent);
                    overridePendingTransition(0, 0);
                }else{
                    new AsyncGetWhishList().execute();
                }
				
				
				/*Intent homeIntent=new Intent(MarketPageActivity.this,WishListActivity.class);
				startActivity(homeIntent);
				overridePendingTransition(0, 0);*/
			}
		});
		
		tradedashboard_layout=(LinearLayout)findViewById(R.id.vL_bottombar_dashboard);
		tradedashboard_selected=(ImageView)findViewById(R.id.vI_bottombar_dashboardselected);
		tradedashboard_notselected=(ImageView)findViewById(R.id.vI_bottombar_dashboard_notselected);
		tradedashboard_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				tradedashboard_selected.setVisibility(View.VISIBLE);
				openquote_selected.setVisibility(View.GONE);
				openquote_notselected.setVisibility(View.VISIBLE);
				tradedashboard_notselected.setVisibility(View.GONE);
				wishlist_selected.setVisibility(View.GONE);
				wishlist_notselected.setVisibility(View.VISIBLE);
				
				getUserId();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(MarketPageActivity.this,SigninPageActivity.class);
					signinIntent.putExtra("FROMACTIVITY", "tradedashboard");
					startActivity(signinIntent);
					overridePendingTransition(0, 0);
				}else{
					Intent intent=new Intent(MarketPageActivity.this,com.mmadapps.verisupplier.trademanager.DashboardActivity.class);
					startActivity(intent);
					overridePendingTransition(0, 0);
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
				
				getUserId();
				if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
					Intent signinIntent=new Intent(MarketPageActivity.this,SigninPageActivity.class);
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
				
				home_selected.setVisibility(View.GONE);
				home_notselected.setVisibility(View.VISIBLE);
				
				userprofile_selected.setVisibility(View.VISIBLE);
				userprofile_notselected.setVisibility(View.GONE);
				Intent homeIntent=new Intent(MarketPageActivity.this,UserMeActivity.class);
				startActivity(homeIntent);
				overridePendingTransition(0, 0);
				
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
	
	private void getUserId() {
		mUserDetails=new UserDetails();
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//product onclick
		case R.id.vI_vshp_products:
			VerisupplierUtils.mServiceGroupType="PR";
			mInputParam=VerisupplierUtils.mProductParam;
			VerisupplierUtils.mServiceGroupTypeId=VerisupplierUtils.mProductServiceGroupType;
			//check network connection
			if(mDetector.isConnectingToInternet()){
				//get products list
				new AsyncProductListDetails().execute();
			}else{
				Toast.makeText(getApplicationContext(), "Please check internet", Toast.LENGTH_SHORT).show();
			}
			break;
			
		//inspection onclick
		case R.id.vI_vshp_inspections:
			VerisupplierUtils.mServiceGroupType="IN";
			mInputParam=VerisupplierUtils.mInspectionParam;
			VerisupplierUtils.mServiceGroupTypeId="3";
			//check network connection
			if(mDetector.isConnectingToInternet()){
				//get manufacturers list
				new AsyncManufacturerListDetails().execute();
			}else{
				Toast.makeText(getApplicationContext(), "Please check internet", Toast.LENGTH_SHORT).show();
			}	
			break;
			
			//shipping onclick
		case R.id.vI_vshp_shipping:
			VerisupplierUtils.mServiceGroupType="SH";
			mInputParam=VerisupplierUtils.mShippingParam;
			VerisupplierUtils.mServiceGroupTypeId=VerisupplierUtils.mShippingParam;
			//check network connection
			if(mDetector.isConnectingToInternet()){
				//get manufacturers list
				new AsyncManufacturerListDetails().execute();
			}else{
				Toast.makeText(getApplicationContext(), "Please check internet", Toast.LENGTH_SHORT).show();
			}
			break;
		
		//customs onclick	
		case R.id.vI_vshp_customs:
			VerisupplierUtils.mServiceGroupType="CU";
			mInputParam=VerisupplierUtils.mCustomsParam;
			VerisupplierUtils.mServiceGroupTypeId="4";
			//check network connection
			if(mDetector.isConnectingToInternet()){
				//get manufacturers list
				new AsyncManufacturerListDetails().execute();
			}else{
				Toast.makeText(getApplicationContext(), "Please check internet", Toast.LENGTH_SHORT).show();
			}
			break;
		
		//warehouse onclick	
		case R.id.vI_vshp_warehouse:
			VerisupplierUtils.mServiceGroupType="WR";
			mInputParam=VerisupplierUtils.mWarehouseParam;
			VerisupplierUtils.mServiceGroupTypeId="7";
			//check network connection
			if(mDetector.isConnectingToInternet()){
				//get manufacturers list
				new AsyncManufacturerListDetails().execute();
			}else{
				Toast.makeText(getApplicationContext(), "Please check internet", Toast.LENGTH_SHORT).show();
			}
			break;
		
		//ecredit onclick
		case R.id.vI_vshp_ecredit:
			VerisupplierUtils.mServiceGroupType="EC";
			mInputParam=VerisupplierUtils.mEcreditParam;
			VerisupplierUtils.mServiceGroupTypeId="8";
			//check network connection
			if(mDetector.isConnectingToInternet()){
				//get manufacturers list
				new AsyncManufacturerListDetails().execute();
			}else{
				Toast.makeText(getApplicationContext(), "Please check internet", Toast.LENGTH_SHORT).show();
			}
			break;
		
		//search onclick	
		case R.id.vI_vshp_search:
			VerisupplierUtils.mServiceGroupType="PR";
			mInputParam=VerisupplierUtils.mProductParam;
			VerisupplierUtils.mServiceGroupTypeId="1";
			//to go search page
			Intent searchIntent=new Intent(MarketPageActivity.this,SearchActivity.class);
			startActivity(searchIntent);
			overridePendingTransition(0,0);
			
			break;
		
		//qrscan onclick	
		case R.id.vI_vshp_qrcode:
			VerisupplierUtils.mServiceGroupType="PR";
			mInputParam=VerisupplierUtils.mProductParam;
			VerisupplierUtils.mServiceGroupTypeId="1";
			//to go scanpage
			Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
            intent.putExtra("SCAN_FORMATS", "QR_CODE,EAN_13,EAN_8,RSS_14,UPC_A,UPC_E,CODE_39,CODE_93,CODE_128,ITF,CODABAR,DATA_MATRIX");
            intent.setAction(Intents.Scan.ACTION);
            startActivityForResult(intent, 0);
			break;
		default:
			break;
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
	
	private class AsyncProductListDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdLoading = new ProgressDialog(MarketPageActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			//get category and featuired products list
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
			//to go productlist activity
			Intent productIntent=new Intent(MarketPageActivity.this,ProductListActivity.class);
			startActivity(productIntent);
			overridePendingTransition(0, 0);
		}
		
	}
	
	private void downloadAllCategories() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mCategoryDetails=helper.getAllCategories("");
		helper.close();
		if(mCategoryDetails == null || mCategoryDetails.size()==0){
			downloadCategories();
		}else if(checkdownloadTime(mCategoryDetails.get(0).getmLastDownloadDateTime())){
		}else{
			downloadAllCategories();
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
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertCategoryDetails(mCategoryDetails);
				helper.close();
			}
		}
	}
	
	private void downloadFeaturedProducts() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mProductList = helper.getFeaturedProducts();
		helper.close();
		if(mProductList == null || mProductList.size() == 0){
			downloadFeatureProducts();
		}else if(checkdownloadTime(mProductList.get(0).getmLastDownloadTime())){
		}else{
			downloadFeaturedProducts();
		}
	}
	
	private void downloadFeatureProducts(){
		List<ProductList> mFeaturedProductList=new ArrayList<ProductList>();
		//incase of products
		if(VerisupplierUtils.mServiceGroupTypeId.equalsIgnoreCase("1")){
			result = webServices.CallWebHTTPBindingService(ApiType.GetFeaturedProducts, "GetFeaturedProducts/", mInputParam);
			if(result==null || result.length()==0){
			}else{
				mFeaturedProductList=jsonParserClass.parseFeaturedProducts(result,getApplicationContext());
				if(mFeaturedProductList==null || mFeaturedProductList.size()==0){
				}else{
					Helper helper=new Helper(getApplicationContext());
					helper.openDataBase();
					helper.insertFeaturedProducts(mFeaturedProductList);
					helper.close();
				}
			}
		}
		//for all servicetypes
		else{
			result = webServices.CallWebHTTPBindingService(ApiType.GetManufacturerListForService, "GetManufacturerListForService/", mInputParam);
			if(result==null || result.length()==0){
			}else{
				mManufacturerList=jsonParserClass.parseManufacturerList(result,getApplicationContext());
				if(mManufacturerList==null || mManufacturerList.size()==0){
				}else{
				}
			}
		
		}
	}
	
	private class AsyncManufacturerListDetails extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdLoading = new ProgressDialog(MarketPageActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			downloadAllCategories();
			downloadFeatureProducts();
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			Intent productIntent=new Intent(MarketPageActivity.this,ProductListActivity.class);
			startActivity(productIntent);
			overridePendingTransition(0, 0);
		}
		
	}
	
	//scan activityresult
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == BaseActionBarActivity.RESULT_OK) {
            contents = intent.getStringExtra(Intents.Scan.RESULT);
            String formatName = intent.getStringExtra(Intents.Scan.RESULT_FORMAT);
            Log.e("Result of Scanning", contents);
            Log.e("Result of Scanning", formatName);
            mProductId=contents;
			getProductDetails();
			if(mProductDetails==null || mProductDetails.size()==0){
				new AsyncProductDetails().execute(contents);
			}else{
				Intent newIntent = new Intent(MarketPageActivity.this,ProductDetailsActivity.class);
				newIntent.putExtra("PRODUCTID", mProductId);
				startActivity(newIntent);
				overridePendingTransition(0, 0);
			}
        } else if (resultCode == Activity.RESULT_CANCELED) {
        }
    }
	
	private void getProductDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mProductDetails=helper.getAllProductDetails(mProductId);
		helper.close();
	}
	
	class AsyncProductDetails extends AsyncTask<String, Void, Boolean>{
        @Override
        protected void onPreExecute() {
            pdLoading = new ProgressDialog(MarketPageActivity.this);
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
           if(pdLoading.isShowing())
               pdLoading.dismiss();
           if(result){
               Intent newIntent=new Intent(MarketPageActivity.this,ProductDetailsActivity.class);
               newIntent.putExtra("PRODUCTID", contents);
               startActivity(newIntent);
               overridePendingTransition(0, 0);
           }else{
               Toast.makeText(getApplicationContext(), "product is not exist", Toast.LENGTH_SHORT).show();
           }
        }
    }
	
	private Boolean isValidDetails(String contents) {
        boolean isValid=false;
        webServices=new WebServices();
        jsonParserClass=new JsonParserClass();
        List<ProductDetails> mDetails=new ArrayList<ProductDetails>();
        String Inparam =contents;
        String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetProductDetail,"GetProductDetail/",Inparam);
        if(resultOutparam==null){
            isValid=false;
        }else{
            mDetails=jsonParserClass.parseProductDetails(resultOutparam, getApplicationContext());
            if(mDetails==null || mDetails.size()==0){
            }else{
            	Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertProductDetails(mDetails);
				helper.insertSpecificationDetails(mDetails,Inparam);
				helper.insertProductImageDetails(mDetails,mProductId);
				helper.insertUserFeedback(mDetails,Inparam);
				helper.close();
				isValid=true;
            }
        }
        return isValid;
    }
	
	private class AsyncCategoryDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdLoading = new ProgressDialog(MarketPageActivity.this);
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
			Intent homeIntent=new Intent(MarketPageActivity.this,Myopenquoate.class);
			startActivity(homeIntent);
			overridePendingTransition(0, 0);
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
	
	private class AsyncGetWhishList extends AsyncTask<Void, Void, Boolean>{
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = new ProgressDialog(MarketPageActivity.this);
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
                Intent homeIntent=new Intent(MarketPageActivity.this,WishListActivity.class);
                startActivity(homeIntent);
                overridePendingTransition(0, 0);
        	}else{
                pdLoading.cancel();
                Toast.makeText(getApplicationContext(), "No WishList", Toast.LENGTH_LONG).show();
            }
        }
	}
	
	 private Boolean getWhishList() {
		   Boolean isValid=false;
		   List<WishlistDetails> mWishDetails=new ArrayList<WishlistDetails>();
		   String inParam = ""+mUserDetails.getmUserID()+"/"+"1"+"/"+"20";
		   String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetWishList, "GetWishList/", inParam);
		   if (resultOutparam == null) {
			   isValid=false;
		   }else {
			   mWishDetails=jsonParserClass.parseWishListDetails(resultOutparam,getApplicationContext());
			   if(mWishDetails==null || mWishDetails.size()==0){
			   }else{
				   isValid=true;
				   Helper helper = new Helper(getApplicationContext());
				   helper.openDataBase();
				   helper.insertwhistlistdetails(mWishDetails);
				   helper.close();
			   }
		   }
		   return isValid;
	   }

}
