package com.mmadapps.verisupplier.details;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.ProductImageDetails;
import com.mmadapps.verisupplier.beans.ProductSpecificationDetails;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.beans.WishlistDetails;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.customservices.CustomCreateQuotation;
import com.mmadapps.verisupplier.dashboard.ProductQuotationBuyerDetailActivity;
import com.mmadapps.verisupplier.inspection.InspectionCreateQuotation;
import com.mmadapps.verisupplier.login.SigninPageActivity;
import com.mmadapps.verisupplier.placeorder.PlaceorderShipping;
import com.mmadapps.verisupplier.products.ProductCreateQuotationActivity;
import com.mmadapps.verisupplier.shipping.ShippingCreateQuotation;
import com.mmadapps.verisupplier.warehouse.WarehouseCreateQuotation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.viewpagerindicator.CirclePageIndicator;

public class ProductDetailsActivity  extends BaseActionBarActivity implements OnClickListener{

	//for imageloader initialization
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	//from database
	List<ProductDetails> mProductDetails = new ArrayList<ProductDetails>();
	List<ProductSpecificationDetails> mSpecificationDetails = new ArrayList<ProductSpecificationDetails>();
	List<WishlistDetails> mwishlistDetails = new ArrayList<WishlistDetails>();
	List<WishlistDetails> mwishlistDetails2=new ArrayList<WishlistDetails>();
	List<ProductImageDetails> mProductImagesList=new ArrayList<ProductImageDetails>();
	List<ManufacturerDetails> mManufacturerDetails=new ArrayList<ManufacturerDetails>();
	List<ProductSpecificationDetails> mManufacturerAttributes=new ArrayList<ProductSpecificationDetails>();
	UserDetails mUserDetails=new UserDetails();
	public static List<ProductDetails> mSavePrequotationValues;
	
	List<CountryDetails> mCounList=new ArrayList<CountryDetails>();
	List<CountryDetails> mStateList=new ArrayList<CountryDetails>();
	List<Lookups> mInspectionType=new ArrayList<Lookups>();
	List<Lookups> mIndustryList=new ArrayList<Lookups>();
	List<Lookups> mInspectorList=new ArrayList<Lookups>();
	
	//services
	WebServices webServices;
	JsonParserClass jsonParserClass;
	
	//for product images
	ProductImageAdapter mImagePagerAdapter;
	ViewPager mProductsPager;
	CirclePageIndicator oCirclePageIndicator;
	
	//TextView 
	TextView product_name,manufacturerName,product_price,product_unit,averagerating_text,manufacturer_description,aboutmanufacturer_text,basicinfo_text,moq_peice,moq_unit,price_text,minord_text,dollar_text,moq_text,feedback_viewmore,manufacturer_viewmore;
	
	//Textview for fonts
	TextView productdetails_text,quickorder_text,feedback_text,manufacturerdetails_text;
	
	//dividerview
	View manufacturerdivider;
	
	//LinearLayout
	LinearLayout feedback_view,manufacture_view,getprice_layout,moreinfo_layout,getQuotation_layout,placeorder_layout;
	
	//product rating
	RatingBar averageRating;
	
	//product specification list
	ListView mDescriptionList,mManufacuturerAttributesList;
	DescriptionAdapter mAdapter;
	ManufacuturerAttributesAdapter mManufactureAttributeAdapter;
	
	//loading bar
	private ProgressDialog pdLoading=null;
	
	public static String mProductId="";
	public static String mManufacturerId="";
	
	//imageview for wishlist and share
	ImageView shareProduct,wishlist_blank,wishlist_filled;
	String value;
	
	//google map
	private GoogleMap mMap;
	double latitude, longitude;
	
	List<Lookups> mUnitOfMeasurentList=new ArrayList<Lookups>();
	List<Lookups> mQuotationLookupList=new ArrayList<Lookups>();
	List<Lookups> mMoreinfoSubjectList=new ArrayList<Lookups>();
	List<Lookups> mMoreinfoLookupList=new ArrayList<Lookups>();
	
	//time differeance 24 hours
	final double DIFFTIME=8.64e+7;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productdetail);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		
		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			BaseActionBarActivity.setmUserName("Product Details");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
			BaseActionBarActivity.setmUserName("Inspections Details");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
			BaseActionBarActivity.setmUserName("Shipping Details");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
			BaseActionBarActivity.setmUserName("Customs Details");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
			BaseActionBarActivity.setmUserName("Warehouse Details");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("EC")){
			BaseActionBarActivity.setmUserName("E-Credit Details");
		}else{
			BaseActionBarActivity.setmUserName("Product Details");
		}
		
		mProductId = getIntent().getStringExtra("PRODUCTID");
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		ProductDetails();
		initializeView();
		initilizeMap();
	}
	
	private void initilizeMap() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_apd)).getMap();
			mMap.getUiSettings().setZoomControlsEnabled(false);
			// check if map is created successfully or not
			if (mMap == null) {
				Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			} else {
			}
			try{
				latitude= 35.0000;
				longitude= 103.0000;
				mapzooming(latitude, longitude);
				mMap.clear();
				mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.raw.marker)));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void mapzooming(Double lat, Double longs) {
		CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, longs)).zoom(5).build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

	}

	private void ProductDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mProductDetails=helper.getAllProductDetails(mProductId);
		mProductImagesList=helper.getProductImageDetails(mProductId);
		mSpecificationDetails=helper.getSellingDetails(mProductId);
		mwishlistDetails= helper.getwishlistdetails();
		if(mProductDetails==null  || mProductDetails.size()==0){
		}else{
			mManufacturerId=mProductDetails.get(0).getmManufacturer_id();
			mManufacturerDetails=helper.getManufacturerDetails(mManufacturerId);
			mManufacturerAttributes=helper.getManufacturerAttributes(mManufacturerId);
		}
		helper.close();
	}

	private void initializeView() {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
		try {
			options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.EXACTLY)
			.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
			.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
			.build();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		mProductsPager = (ViewPager)findViewById(R.id.apd_adds_pager);
		oCirclePageIndicator = (CirclePageIndicator)findViewById(R.id.apd_adds_indicator);
		oCirclePageIndicator.setFillColor(getResources().getColor(R.color.red_textcolor));
	
		product_name=(TextView)findViewById(R.id.vT_apd_productName);
		manufacturerName=(TextView)findViewById(R.id.vT_apd_manufacturerName);
		price_text=(TextView)findViewById(R.id.vT_apd_priceText);
		dollar_text=(TextView)findViewById(R.id.vT_apd_dollarText);
		product_price=(TextView)findViewById(R.id.vT_apd_productprice);
		product_unit=(TextView)findViewById(R.id.vT_apd_productunit);
		
		minord_text=(TextView)findViewById(R.id.vT_apd_moq_text);
		moq_peice=(TextView)findViewById(R.id.vT_apd_moq_quantity);
		moq_text=(TextView)findViewById(R.id.vT_apd_moq_orderText);
		
		averagerating_text=(TextView)findViewById(R.id.vT_apd_average_rating);
		averageRating=(RatingBar)findViewById(R.id.rb_apd_averagerating);
		feedback_viewmore=(TextView)findViewById(R.id.vT_apd_feedback_viewmore);
		feedback_viewmore.setOnClickListener(this);
		feedback_view=(LinearLayout)findViewById(R.id.vL_apd_feedbackdetail_layout);
		feedback_view.setOnClickListener(this);
		
		mDescriptionList=(ListView)findViewById(R.id.vL_apd_productdetailsList);
		mDescriptionList.setFocusable(false);
		
		shareProduct=(ImageView)findViewById(R.id.vI_apd_shareproduct);
		shareProduct.setOnClickListener(this);
		
		aboutmanufacturer_text=(TextView)findViewById(R.id.vT_apd_aboutmanufacturer_text);
		manufacturerdivider=(View)findViewById(R.id.view_apd_manufacturerdevider);
		basicinfo_text=(TextView)findViewById(R.id.vT_apd_basicinformation_text);
		manufacturer_description=(TextView)findViewById(R.id.vT_apd_manufacturerdescription);
		mManufacuturerAttributesList=(ListView)findViewById(R.id.vL_apd_manufacuturerAttributesList);
		manufacture_view=(LinearLayout)findViewById(R.id.vL_apd_manufacturerdetail_layout);
		manufacturer_viewmore=(TextView)findViewById(R.id.vT_apd_manufacturer_viewmore);
		manufacture_view.setOnClickListener(this);
		manufacturer_viewmore.setOnClickListener(this);
		
		getprice_layout=(LinearLayout)findViewById(R.id.vL_apd_getprice_layout);
		moreinfo_layout=(LinearLayout)findViewById(R.id.vL_apd_moreinfo_layout);
		getQuotation_layout=(LinearLayout)findViewById(R.id.vL_apd_getquotation_layout);
		placeorder_layout=(LinearLayout)findViewById(R.id.vL_apd_placeorder_layout);
		
		getprice_layout.setOnClickListener(this);
		moreinfo_layout.setOnClickListener(this);
		getQuotation_layout.setOnClickListener(this);
		placeorder_layout.setOnClickListener(this);
		
		wishlist_blank=(ImageView)findViewById(R.id.vI_apd_wishlist_blank);
		wishlist_blank.setOnClickListener(this);
		wishlist_filled=(ImageView)findViewById(R.id.vI_apd_wishlist_filled);
		wishlist_filled.setOnClickListener(this);
		
		productdetails_text=(TextView)findViewById(R.id.vT_apd_productdetails_text);
		quickorder_text=(TextView)findViewById(R.id.vT_apd_quickorder_text);
		feedback_text=(TextView)findViewById(R.id.vT_apd_feedbackText);
		manufacturerdetails_text=(TextView)findViewById(R.id.vT_apd_manufacturerdetails_text);
		
		setValues();
	}

	private void setValues() {
		final Typeface segeo_Regular;
		final Typeface segeo_Regular_Bold;
		segeo_Regular = BaseActionBarActivity.getSegeo_Regular();
		segeo_Regular_Bold = BaseActionBarActivity.getSegeo_Bold();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					manufacturerName.setTypeface(segeo_Regular);
					price_text.setTypeface(segeo_Regular);
					dollar_text.setTypeface(segeo_Regular);
					product_unit.setTypeface(segeo_Regular);
					minord_text.setTypeface(segeo_Regular);
					moq_peice.setTypeface(segeo_Regular);
					moq_text.setTypeface(segeo_Regular);
					quickorder_text.setTypeface(segeo_Regular);
					feedback_text.setTypeface(segeo_Regular);
					averagerating_text.setTypeface(segeo_Regular);
					aboutmanufacturer_text.setTypeface(segeo_Regular);
					manufacturer_description.setTypeface(segeo_Regular);
					basicinfo_text.setTypeface(segeo_Regular);
					manufacturerdetails_text.setTypeface(segeo_Regular);
					productdetails_text.setTypeface(segeo_Regular);
					product_name.setTypeface(segeo_Regular);
				} catch (Exception e) {
				}
			}
		};
		new Thread(runnable).run();
		Runnable runnable2 = new Runnable() {
			@Override
			public void run() {
				try {
					product_price.setTypeface(segeo_Regular_Bold);
				} catch (Exception e) {
				}
			}
		};
		new Thread(runnable2).run();
		
		if(mProductImagesList==null|| mProductImagesList.size()==0){
		}else{
			mImagePagerAdapter = new ProductImageAdapter();
			oCirclePageIndicator.setSnap(true);
			oCirclePageIndicator.setFillColor(0xFF0979FF);
			oCirclePageIndicator.setStrokeColor(0xFFBDBDBD);
			oCirclePageIndicator.setRadius(6 * 1);
			mProductsPager.setAdapter(mImagePagerAdapter);
			oCirclePageIndicator.setViewPager(mProductsPager);
			mImagePagerAdapter.notifyDataSetChanged();
			mProductsPager.getAdapter().notifyDataSetChanged();
		}
		
		if(mProductDetails==null || mProductDetails.size()==0){
		}else{
			mManufacturerId=mProductDetails.get(0).getmManufacturer_id();
			mProductId=mProductDetails.get(0).getmProduct_Id();
			product_name.setText(""+mProductDetails.get(0).getmProduct_Name());
			product_price.setText("$"+mProductDetails.get(0).getmProduct_price());
			product_unit.setText("/"+mProductDetails.get(0).getmProduct_UnitOfMeasurement());
			manufacturerName.setText(""+mProductDetails.get(0).getmManufacturer_name());
			moq_peice.setText(""+mProductDetails.get(0).getmProduct_moq());
			
			if(mProductDetails.get(0).getmProduct_averageRating()==null || mProductDetails.get(0).getmProduct_averageRating().length()==0){
				averagerating_text.setText("0");
				averageRating.setRating(0);
			}else{
				averagerating_text.setText(""+mProductDetails.get(0).getmProduct_averageRating());
				averageRating.setRating(Float.parseFloat(""+mProductDetails.get(0).getmProduct_averageRating()));
			}
			
			if(mManufacturerDetails==null|| mManufacturerDetails.size()==0){
				aboutmanufacturer_text.setVisibility(View.GONE);
				manufacturerdivider.setVisibility(View.GONE);
				manufacturer_description.setVisibility(View.GONE);
				basicinfo_text.setVisibility(View.GONE);
			}else{
				if(mManufacturerDetails.get(0).getmShortDescription()==null || mManufacturerDetails.get(0).getmShortDescription().length()==0){
					aboutmanufacturer_text.setVisibility(View.GONE);
					manufacturer_description.setVisibility(View.GONE);
					manufacturer_description.setVisibility(View.GONE);
				}else{
					manufacturer_description.setText(""+mManufacturerDetails.get(0).getmShortDescription());
				}
			}
		}
		
		if(mSpecificationDetails==null || mSpecificationDetails.size()==0){
		}else{
			mAdapter=new DescriptionAdapter();
			mDescriptionList.setAdapter(mAdapter);
			setListViewHeightBasedOnChildren(mDescriptionList);
		}
		
		if(mManufacturerAttributes==null || mManufacturerAttributes.size()==0){
		}else{
			mManufactureAttributeAdapter=new ManufacuturerAttributesAdapter();
			mManufacuturerAttributesList.setAdapter(mManufactureAttributeAdapter);
			setListViewHeightBasedOnManufacturer(mManufacuturerAttributesList);
		}
		
		if(mwishlistDetails==null || mwishlistDetails.size()==0){
		}else{
			for (int i = 0; i < mwishlistDetails.size(); i++) {
				if(mwishlistDetails.get(i).getProduct_id().equalsIgnoreCase(mProductDetails.get(0).getmProduct_Id())){
					wishlist_filled.setVisibility(View.VISIBLE);
					wishlist_blank.setVisibility(View.GONE);
					break;
				}else{
					wishlist_filled.setVisibility(View.GONE);
					wishlist_blank.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private class ProductImageAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			if(mProductImagesList==null || mProductImagesList.size()==0){
				return 0;
			}else{
				return mProductImagesList.size();
			}
		}
		
		@Override
		public Object instantiateItem(View collection, final int position) {
			LayoutInflater mLayoutInflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View LeagueButtonView = mLayoutInflater.inflate(R.layout.adapter_imageslide_pager, null);
			ImageView im_tipImage = (ImageView) LeagueButtonView.findViewById(R.id.pager_add_image);
			imageLoader.displayImage(mProductImagesList.get(position).getmProductImage_url(),im_tipImage, options);
			((ViewPager) collection).addView(LeagueButtonView, 0);
			return LeagueButtonView;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + listView.getDividerHeight() * (listAdapter.getCount()+listAdapter.getViewTypeCount()))+15;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
	
	public static void setListViewHeightBasedOnManufacturer(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + listView.getDividerHeight() * (listAdapter.getCount()+listAdapter.getViewTypeCount()))+150;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
	
	private class DescriptionAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(mSpecificationDetails==null || mSpecificationDetails.size()==0){
				return 0;
			}else{
				return mSpecificationDetails.size();
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
			if(convertView==null){
				convertView=getLayoutInflater().inflate(R.layout.description_view, parent, false);
			}
			final TextView typeText=(TextView)convertView.findViewById(R.id.vT_dv_type);
			final TextView detailText=(TextView)convertView.findViewById(R.id.vT_dv_detail);
			
			final Typeface segeo_Regular;
			segeo_Regular = BaseActionBarActivity.getSegeo_Regular();
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						typeText.setTypeface(segeo_Regular);
						detailText.setTypeface(segeo_Regular);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(runnable).run();
			
			if(mSpecificationDetails.size()==0 || mSpecificationDetails==null){
			}else{
				typeText.setText(""+mSpecificationDetails.get(position).getmProductAttribute_Name());
				detailText.setText(": "+mSpecificationDetails.get(position).getmProductAttribute_Value());
			}
			return convertView;
		}
	}
	
	private class ManufacuturerAttributesAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(mManufacturerAttributes==null || mManufacturerAttributes.size()==0){
				return 0;
			}else{
				return mManufacturerAttributes.size();
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
			if(convertView==null){
				convertView=getLayoutInflater().inflate(R.layout.description_view, parent, false);
			}
			final TextView typeText=(TextView)convertView.findViewById(R.id.vT_dv_type);
			final TextView detailText=(TextView)convertView.findViewById(R.id.vT_dv_detail);
			
			final Typeface segeo_Regular;
			segeo_Regular = BaseActionBarActivity.getSegeo_Regular();
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						typeText.setTypeface(segeo_Regular);
						detailText.setTypeface(segeo_Regular);
					} catch (Exception e) {
					}
				}
			};
			new Thread(runnable).run();
			typeText.setText(""+mManufacturerAttributes.get(position).getmProductAttribute_Name());
			detailText.setText(""+mManufacturerAttributes.get(position).getmProductAttribute_Value());
			
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vT_apd_feedback_viewmore:
		case R.id.vL_apd_feedbackdetail_layout:
			saveValues();
			Intent feedbackIntent = new Intent(ProductDetailsActivity.this, NewFeedbackActivity.class);
			feedbackIntent.putExtra("FROMACTIVITY", "productdetails");
			feedbackIntent.putExtra("PRODUCTID", mProductId);
			feedbackIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(feedbackIntent);
			overridePendingTransition(0, 0);
			break;
			
		case R.id.vL_apd_manufacturerdetail_layout:
		case R.id.vT_apd_manufacturer_viewmore:
			mManufacturerId=mProductDetails.get(0).getmManufacturer_id();
			Intent manufacturerIntent = new Intent(ProductDetailsActivity.this, ManufacturerDetailActivity.class);
			manufacturerIntent.putExtra("MANUFACTURERID", mManufacturerId);
			manufacturerIntent.putExtra("PRODUCTID", mProductId);
			startActivity(manufacturerIntent);
			overridePendingTransition(0, 0);
			break;
			
		case R.id.vI_apd_shareproduct:
			createShareIntent();
			break;
			
		case R.id.vL_apd_getprice_layout:
			/*saveValues();
			getUserId();
			mManufacturerId=mProductDetails.get(0).getmManufacturer_id();
			if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
				Intent getPriceIntent=new Intent(ProductDetailsActivity.this,UserSignPopup.class);
				getPriceIntent.putExtra("FROMACTIVITY", "getprice");
				getPriceIntent.putExtra("MANUFACTURERID", mManufacturerId);
				startActivity(getPriceIntent);
				overridePendingTransition(0, 0);
			}else{
				new AsyncLookUpDetails().execute("getprice");
			}*/
			break;
			
		case R.id.vL_apd_moreinfo_layout:
			saveValues();
			getUserId();
			mManufacturerId=mProductDetails.get(0).getmManufacturer_id();
			if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
				Intent getPriceIntent=new Intent(ProductDetailsActivity.this,SigninPageActivity.class);
				getPriceIntent.putExtra("FROMACTIVITY", "moreinfo");
				getPriceIntent.putExtra("MANUFACTURERID", mManufacturerId);
				startActivity(getPriceIntent);
				overridePendingTransition(0, 0);
			}else{
				new AsyncLookUpDetails().execute("moreinfo");
			}
			break;
			
		case R.id.vL_apd_getquotation_layout:
			saveValues();
			getUserId();
			mManufacturerId=mProductDetails.get(0).getmManufacturer_id();
			if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
				Intent getPriceIntent=new Intent(ProductDetailsActivity.this,SigninPageActivity.class);
				getPriceIntent.putExtra("FROMACTIVITY", "getquotation");
				getPriceIntent.putExtra("MANUFACTURERID", mManufacturerId);
				startActivity(getPriceIntent);
				overridePendingTransition(0, 0);
			}else{
				new AsyncLookUpDetails().execute("getquotation");
			}
			break;
			
		case R.id.vL_apd_placeorder_layout:
			saveValues();
			getUserId();
			mManufacturerId=mProductDetails.get(0).getmManufacturer_id();
			if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
				Intent getPriceIntent=new Intent(ProductDetailsActivity.this,SigninPageActivity.class);
				getPriceIntent.putExtra("FROMACTIVITY", "placeorder");
				getPriceIntent.putExtra("MANUFACTURERID", mManufacturerId);
				getPriceIntent.putExtra("PRODUCTID", mProductId);
				startActivity(getPriceIntent);
				overridePendingTransition(0, 0);
			}else{
				new AsyncLookUpDetails().execute("placeorder");
			}
			break;
			
		case R.id.vI_apd_wishlist_blank:
			Helper helper=new Helper(getApplicationContext());
            helper.openDataBase();
            mwishlistDetails=helper.getwishlistdetails();
            helper.close();
            if(mwishlistDetails==null || mwishlistDetails.size()==0){
                addToWishlish();
            }else{
                Helper helper1=new Helper(getApplicationContext());
                helper1.openDataBase();
                mwishlistDetails=helper1.getwishlistdetails(mProductDetails.get(0).getmProduct_Id());
                helper1.close();
                if(mwishlistDetails==null || mwishlistDetails.size()==0){
                    addToWishlish();
                }else{
                    wishlist_filled.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Product is already exist in wishlist", Toast.LENGTH_SHORT).show();
                }
            }
            
            /*Helper helper=new Helper(getApplicationContext());
            helper.openDataBase();
            mwishlistDetails=helper.getwishlistdetails();
            helper.close();
            if(mwishlistDetails==null || mwishlistDetails.size()==0){
                addToWishlish();
            }else{
                Helper helper1=new Helper(getApplicationContext());
                helper1.openDataBase();
                mwishlistDetails=helper1.getwishlistdetails(mProductDetails.get(0).getmProduct_Id());
                helper1.close();
                if(mwishlistDetails==null || mwishlistDetails.size()==0){
                    addToWishlish();
                }else{
                    Toast.makeText(getApplicationContext(), "Product is already exist in wishlist", Toast.LENGTH_SHORT).show();
                }
            }*/
            
            
            
            
            /*Helper helper=new Helper(getApplicationContext());
            helper.openDataBase();
            mwishlistDetails=helper.getwishlistdetails();
            helper.close();
            if(mwishlistDetails==null || mwishlistDetails.size()==0){
                addToWishlish();
            }else{
                Helper helper1=new Helper(getApplicationContext());
                helper1.openDataBase();
                mwishlistDetails=helper1.getwishlistdetails(mProductDetails.get(0).getmProduct_Id());
                helper1.close();
                if(mwishlistDetails==null || mwishlistDetails.size()==0){
                    addToWishlish();
                }else{
                    Toast.makeText(getApplicationContext(), "Product is already exist in wishlist", Toast.LENGTH_SHORT).show();
                }
            }*/
            
            
			/*Helper helper=new Helper(getApplicationContext());
			helper.openDataBase();
			mwishlistDetails=helper.getwishlistdetails();
			helper.close();
			if(mwishlistDetails==null || mwishlistDetails.size()==0){
				addToWishlish();
			}else{
				Helper helper1=new Helper(getApplicationContext());
				helper1.openDataBase();
				mwishlistDetails=helper1.getwishlistdetails(mProductDetails.get(0).getmProduct_Id());
				helper1.close();
				if(mwishlistDetails==null || mwishlistDetails.size()==0){
					addToWishlish();
				}else{
					Toast.makeText(getApplicationContext(), "Product is already exist in wishlist", Toast.LENGTH_SHORT).show();
				}
			}*/
			break;
			
		case R.id.vI_apd_wishlist_filled:
			getUserId();
            mProductId = mProductDetails.get(0).getmProduct_Id();
            new AsyncDeleteWishList().execute();
            
            wishlist_blank.setVisibility(View.VISIBLE);
            wishlist_filled.setVisibility(View.GONE);
           // Toast.makeText(getApplicationContext(), "Product is removed form wishlist", Toast.LENGTH_SHORT).show();
			
			
			/*Helper helper1=new Helper(getApplicationContext());
			helper1.openDataBase();
			SQLiteDatabase sdb=helper1.getWritableDatabase();
			value=""+mProductDetails.get(0).getmProduct_Id();
			sdb.execSQL("DELETE FROM whistlist_productmaster where product_id='"+value+"'");
			helper1.close();
			wishlist_blank.setVisibility(View.VISIBLE);
			wishlist_filled.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(), "Product is removed form wishlist", Toast.LENGTH_SHORT).show();*/
		default:
			break;
		}
	}
	
	private void addToWishlish() {
		wishlist_blank.setVisibility(View.GONE);
		wishlist_filled.setVisibility(View.VISIBLE);
		mProductId = mProductDetails.get(0).getmProduct_Id();
		getUserId();
		if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().length()==0){
			Intent getPriceIntent=new Intent(ProductDetailsActivity.this,SigninPageActivity.class);
			getPriceIntent.putExtra("FROMACTIVITY", "wishlistproduct");
			getPriceIntent.putExtra("PRODUCTID", mProductId);
			startActivity(getPriceIntent);
			overridePendingTransition(0, 0);
		}else{
			new AsyncCreateWishList().execute();
		}
		
		/*wishlist_blank.setVisibility(View.GONE);
		wishlist_filled.setVisibility(View.VISIBLE);
		
		WishlistDetails mwishlist= new WishlistDetails();
		mwishlist.setProduct_id(mProductDetails.get(0).getmProduct_Id());
		mwishlist.setProduct_nam(mProductDetails.get(0).getmProduct_Name());
		mwishlist.setProduct_manufname(mProductDetails.get(0).getmManufacturer_name());
		mwishlist.setProduct_img(mProductImagesList.get(0).getmProductImage_url());
		mwishlist.setmProductPrice(mProductDetails.get(0).getmProduct_price());
		mwishlist.setmProductUnit(mProductDetails.get(0).getmProduct_UnitOfMeasurement());
		
		List<WishlistDetails> mwishlistDetails= new ArrayList<WishlistDetails>();
		mwishlistDetails.add(mwishlist);
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		helper.insertwhistlistdetails(mwishlistDetails);
	    helper.close();
		Toast.makeText(getApplicationContext(), "Product is added to wishlist", Toast.LENGTH_SHORT).show();*/
	}
	
	private void saveValues(){
		mSavePrequotationValues=new ArrayList<ProductDetails>();
		ProductDetails mPlaceorderDetails=new ProductDetails();
		mPlaceorderDetails.setmProduct_Id(mProductId);
		mPlaceorderDetails.setmProduct_Name(mProductDetails.get(0).getmProduct_Name());
		mPlaceorderDetails.setmProduct_Image(mProductImagesList.get(0).getmProductImage_url());
		mPlaceorderDetails.setmManufacturer_id(mManufacturerId);
		mPlaceorderDetails.setmManufacturer_name(mProductDetails.get(0).getmManufacturer_name());
		mPlaceorderDetails.setmProduct_price(mProductDetails.get(0).getmProduct_price());
		mPlaceorderDetails.setmProduct_moq(mProductDetails.get(0).getmProduct_moq());
		
		mSavePrequotationValues.add(mPlaceorderDetails);
	}
	
	private void getUserId() {
		mUserDetails=new UserDetails();
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}
	
	private class AsyncLookUpDetails extends AsyncTask<String, Void, Boolean>{
		String mType = null;
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(ProductDetailsActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			mType = params[0];
			if(mType.equalsIgnoreCase("getprice")){
				getQuantityList();
				return getManufacturerDetaisl();
			}else if(mType.equalsIgnoreCase("moreinfo")){
				getManufacturerDetaisl();
				getSubjectDetails();
				getMoreInfoLookups();
				return true;
			}else if(mType.equalsIgnoreCase("getquotation")){
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
			}else if(mType.equalsIgnoreCase("placeorder")){
				if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
					callQuotationOrderService();
					//getQuantityList();
					//getQuotationLookups();
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
			else{
				return getManufacturerDetaisl();
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			if(result){
				if(mType.equalsIgnoreCase("getquotation")){
					forwardTogetQuotationActivity(mType);
				}else if(mType.equalsIgnoreCase("getprice")){
					/*Intent getQuotationIntent = new Intent(ProductDetailsActivity.this, GetQuotationActivityNew.class);
					getQuotationIntent.putExtra("FROMACTIVITY", ""+mType);
					getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
					startActivity(getQuotationIntent);
					overridePendingTransition(0, 0);*/
				}else if(mType.equalsIgnoreCase("moreinfo")){
					forwardToMoreInfoActivity();
				}else if(mType.equalsIgnoreCase("placeorder")){
					forwardToPlaceOrderActivity();
				}
				else{
					forwardToManufacturerActivity();
				}
			}else{
				Toast.makeText(ProductDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	private Boolean getManufacturerDetaisl() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		boolean isValid=false;
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mManufacturerDetails=helper.getManufacturerDetails(mManufacturerId);
		helper.close();
		
		if(mManufacturerDetails==null || mManufacturerDetails.size()==0){
			webServices =new WebServices();
			jsonParserClass=new JsonParserClass();
			String Inparam=mManufacturerId;
			String result=webServices.CallWebHTTPBindingService(ApiType.GetManufacturerDetail, "GetManufacturerDetail/", Inparam);
			if(result==null || result.length()==0){
				isValid = false;
			}else{
				mManufacturerDetails=jsonParserClass.parseManufacturerDetails(result);
				if(mManufacturerDetails==null || mManufacturerDetails.size()==0){
				}else{
					helper.openDataBase();
					helper.insertManufacturerDetails(mManufacturerDetails, mManufacturerId);
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
	
	@Override
	public void onResume() {
		if (pdLoading != null && pdLoading.isShowing()) {
			pdLoading.cancel();
		}
		VerisupplierUtils.mMoreInfoLookups = new ArrayList<Lookups>();
		VerisupplierUtils.mQuotationLookups = new ArrayList<Lookups>();
		VerisupplierUtils.mQuantityList=new ArrayList<Lookups>();
		VerisupplierUtils.mSubjectLookups=new ArrayList<Lookups>();
		setValues();
		super.onResume();
	}
	
	private void forwardToManufacturerActivity() {
		Intent mfrdescriptionIntent = new Intent(ProductDetailsActivity.this, ManufacturerDetailActivity.class);
		mfrdescriptionIntent.putExtra("MANUFACTURERID", mManufacturerId);
		startActivity(mfrdescriptionIntent);
		overridePendingTransition(0, 0);
	}
	
	private void createShareIntent() {
		 Intent shareIntent = new Intent(Intent.ACTION_SEND);
		 shareIntent.setType("text/plain");
		 shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
		 shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,"");        
		 startActivity(shareIntent);   
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
		}*/
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
		}*/
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

	/*private void getQuotationDetails() {
		webServices =new WebServices();
		jsonParserClass=new JsonParserClass();
		String Inparam=mUserDetails.getmUserID()+"/"+mManufacturerId+"/"+mProductId;
		String outputParm=webServices.CallWebHTTPBindingService(ApiType.GetQuoteDetailsForQuickOrder, "GetQuoteDetailsForQuickOrder/", Inparam);
		if(outputParm==null || outputParm.length()==0){
		}else{
			VerisupplierUtils.mQuotationDetails=jsonParserClass.parseQuickOrderResult(outputParm,"GetQuoteDetailsForQuickOrderResult");
		}
	}*/
	
	public void forwardTogetQuotationActivity(String mType) {
		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			Intent getQuotationIntent = new Intent(ProductDetailsActivity.this, ProductCreateQuotationActivity.class);
			getQuotationIntent.putExtra("FROMACTIVITY", ""+mType);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
			Intent getQuotationIntent = new Intent(ProductDetailsActivity.this, InspectionCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
		}
		else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
			Intent getQuotationIntent = new Intent(ProductDetailsActivity.this, ShippingCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
			Intent getQuotationIntent = new Intent(ProductDetailsActivity.this, CustomCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
			Intent getQuotationIntent = new Intent(ProductDetailsActivity.this, WarehouseCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
		}
		
		
	}
	
	private void forwardToPlaceOrderActivity() {
		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			PlaceorderShipping.mFromActivity="PD";
			Intent placeOrderIntent = new Intent(ProductDetailsActivity.this, PlaceorderShipping.class);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);
			/*Intent placeOrderIntent = new Intent(ProductDetailsActivity.this, ProductCreateQuotationActivity.class);
			placeOrderIntent.putExtra("FROMACTIVITY", "getquotation");
			placeOrderIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(placeOrderIntent);
			overridePendingTransition(0, 0);*/
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
			Intent getQuotationIntent = new Intent(ProductDetailsActivity.this, ShippingCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
			Intent getQuotationIntent = new Intent(ProductDetailsActivity.this, CustomCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
			Intent getQuotationIntent = new Intent(ProductDetailsActivity.this, InspectionCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
			Intent getQuotationIntent = new Intent(ProductDetailsActivity.this, WarehouseCreateQuotation.class);
			getQuotationIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(getQuotationIntent);
			overridePendingTransition(0, 0);
		}
	}
	
	public void forwardToMoreInfoActivity() {
		Intent moreInfoIntent = new Intent(ProductDetailsActivity.this, MoreInfoActivityNew.class);
		startActivity(moreInfoIntent);
		overridePendingTransition(0, 0);
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
		List<CountryDetails> mCountryList=new ArrayList<CountryDetails>();
		String outputResult=webServices.CallWebHTTPBindingService(ApiType.GetCountries, "GetCountries", "");
		if(outputResult==null || outputResult.length()==0){
		}else{
			mCountryList=jsonParserClass.parseCountries(outputResult,getApplicationContext());
			if(mCountryList==null || mCountryList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertCountries(mCountryList);
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
		List<CountryDetails> mStateList=new ArrayList<CountryDetails>();
		String outputResult=webServices.CallWebHTTPBindingService(ApiType.GetCountries, "GetStates/", "0");
		if(outputResult==null || outputResult.length()==0){
		}else{
			mStateList=jsonParserClass.parseStates(outputResult,getApplicationContext());
			if(mStateList==null || mStateList.size()==0){
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertStates(mStateList);
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
	
	public class AsyncCreateWishList extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            pdLoading = new ProgressDialog(ProductDetailsActivity.this);
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
            }else{
                Toast.makeText(getApplicationContext(), "Product is not added to wishlist", Toast.LENGTH_SHORT).show();
            }
        }
        
    }
    
    
    private Boolean createWishList() {
        Boolean isValid=false;
        webServices=new WebServices();
        jsonParserClass=new JsonParserClass();
        String mResult = null;
        String inParam =mProductId+"/"+mUserDetails.getmUserID();
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
    
    public class AsyncDeleteWishList extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            pdLoading = new ProgressDialog(ProductDetailsActivity.this);
            pdLoading.setMessage("Please wait...");
            pdLoading.show();
            pdLoading.setCancelable(false);
            pdLoading.setCanceledOnTouchOutside(false);
            super.onPreExecute();
        }
        
        
        @Override
        protected Boolean doInBackground(String... params) {
            return deleteWishList();
        }
        
    
    
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (pdLoading != null && pdLoading.isShowing()) {
                pdLoading.cancel();
            }
            if(result){
                Helper helper1=new Helper(getApplicationContext());
                helper1.openDataBase();
                SQLiteDatabase sdb=helper1.getWritableDatabase();
                value=""+mProductDetails.get(0).getmProduct_Id();
                sdb.execSQL("DELETE FROM whistlist_productmaster where product_id='"+value+"'");
                helper1.close();
                
                wishlist_blank.setVisibility(View.VISIBLE);
                wishlist_filled.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Product is Deleted from wishlist", Toast.LENGTH_SHORT).show();
            }else{
                wishlist_blank.setVisibility(View.GONE);
                wishlist_filled.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Product is not Deleted from wishlist", Toast.LENGTH_SHORT).show();
            }
        }
        
    }
    
    
    private Boolean deleteWishList() {
        Boolean isValid=false;
        webServices=new WebServices();
        jsonParserClass=new JsonParserClass();
        String mResult = null;
        String inParam =mUserDetails.getmUserID()+"/"+mProductId;
        String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetWishList, "DeleteWishlist/", inParam);
        if(resultOutparam==null || resultOutparam.length()==0){
            isValid = false;
        }else{
            mResult = jsonParserClass.parseDeleteWishlist(resultOutparam);
            if(mResult.equalsIgnoreCase("false") || mResult==null){
                isValid=false;
            }else{
                isValid=true;
            }
        }
        return isValid;
    }
    
    private void callQuotationOrderService() {
		webServices = new WebServices();
		jsonParserClass = new JsonParserClass();
		String Inparam = ""+mUserDetails.getmUserID()+"/"+mManufacturerId+"/"+mProductId;
		String resultOutParam = webServices.CallWebHTTPBindingService(ApiType.GetQuoteDetailsForQuickOrder,"GetQuoteDetailsForQuickOrder/", Inparam);
		if (resultOutParam == null || resultOutParam.length() == 0) {
		} else {
			VerisupplierUtils.mQuotationDetails = jsonParserClass.parseQuickOrderResult(resultOutParam,"GetQuoteDetailsForQuickOrderResult");
			if (VerisupplierUtils.mQuotationDetails != null) {
			}
		}
	}
	
}
