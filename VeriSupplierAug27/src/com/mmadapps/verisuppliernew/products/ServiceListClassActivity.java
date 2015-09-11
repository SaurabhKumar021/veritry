package com.mmadapps.verisuppliernew.products;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.ManufacturerList;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.WishlistDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.details.ManufacturerDetailActivity;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ServiceListClassActivity extends BaseActionBarActivity{
	
	//for image
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	ListView mProductsList;
	ProductAdapter mProductAdapter;
	
	List<ManufacturerList> mProductListOfManufacturer=new ArrayList<ManufacturerList>();
	List<WishlistDetails> mwishlistDetails = new ArrayList<WishlistDetails>();
	List<ProductDetails> mProductDetails = new ArrayList<ProductDetails>();
	
	String mProductId;
	String mProductName;
	String mManufacturerName;
	String mProductImage;
	String mManufacturerId;
	
	WebServices webServices;
	JsonParserClass jsonParserClass;
	private ProgressDialog pdLoading=null;
	String value;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productlist);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		
		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			BaseActionBarActivity.setmUserName("Product Services");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
			BaseActionBarActivity.setmUserName("Inspections Services");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
			BaseActionBarActivity.setmUserName("Shipping Services");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
			BaseActionBarActivity.setmUserName("Customs Services");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
			BaseActionBarActivity.setmUserName("Warehouse Services");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("EC")){
			BaseActionBarActivity.setmUserName("E-Credit Services");
		}else{
			BaseActionBarActivity.setmUserName("Product Services");
		}
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		mManufacturerId=getIntent().getStringExtra("MANUFACTURERID");
		getWishlist();
		initializeView();
	}

	private void getWishlist() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mwishlistDetails=helper.getwishlistdetails();
		helper.close();
	}

	private void initializeView() {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(getApplicationContext()));
		try {
			options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.ic_launcher)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.EXACTLY)
					.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
					.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
					.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mProductListOfManufacturer=ProductListActivity.mProductListOfManufacturer;
		
		mProductsList=(ListView)findViewById(R.id.vL_apd_productlist);
		setFeaturedProducts();
	}
	
	private void setFeaturedProducts() {
		if (mProductListOfManufacturer == null || mProductListOfManufacturer.size() == 0) {
		} else {
			mProductAdapter=new ProductAdapter();
			mProductsList.setAdapter(mProductAdapter);
		}
	}
	
	private class ProductAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mProductListOfManufacturer.size();
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
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.productlist_view, parent, false);
			}
			ImageView productImage=(ImageView)convertView.findViewById(R.id.vI_pv_producuImage);
			final TextView product_name=(TextView)convertView.findViewById(R.id.vT_pv_productname);
			final TextView manufacturer_name=(TextView)convertView.findViewById(R.id.vT_pv_manufacturername);
			final TextView product_price_text=(TextView)convertView.findViewById(R.id.vT_pv_productprice_text);
			final TextView product_price=(TextView)convertView.findViewById(R.id.vT_pv_productprice);
			final ImageView wishlist=(ImageView)convertView.findViewById(R.id.vI_pv_wishlist_blank);
			final ImageView wishlish_green=(ImageView)convertView.findViewById(R.id.vI_pv_wishlist_filled);
			
			final Typeface segeo_Regular;
			final Typeface segeo_Regular_Bold;
			segeo_Regular = BaseActionBarActivity.getSegeo_Light();
			segeo_Regular_Bold = BaseActionBarActivity.getSegeo_Bold();
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						product_name.setTypeface(segeo_Regular);
						manufacturer_name.setTypeface(segeo_Regular);
						
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
			wishlist.setVisibility(View.GONE);
			if(mwishlistDetails==null || mwishlistDetails.size()==0){
			}else{
				for (int i = 0; i < mwishlistDetails.size(); i++) {
					if(mwishlistDetails.get(i).getProduct_id().equalsIgnoreCase(mProductListOfManufacturer.get(position).getmId())){
						wishlish_green.setVisibility(View.VISIBLE);
						wishlist.setVisibility(View.GONE);
						break;
					}else{
						wishlish_green.setVisibility(View.GONE);
						wishlist.setVisibility(View.VISIBLE);
					}
				}
			}
			
			product_name.setText(""+mProductListOfManufacturer.get(position).getmName());
			manufacturer_name.setVisibility(View.GONE);
			imageLoader.displayImage(mProductListOfManufacturer.get(position).getmLogo(), productImage, options);
			product_price.setVisibility(View.GONE);
			product_price_text.setVisibility(View.GONE);
			
			wishlist.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					List<WishlistDetails> mwishlistDetails= new ArrayList<WishlistDetails>();
					WishlistDetails mwishlist= new WishlistDetails();
					mwishlist.setProduct_id(mProductListOfManufacturer.get(position).getmId());
					mwishlist.setProduct_nam(mProductListOfManufacturer.get(position).getmName());
					mwishlist.setProduct_manufname("");
					mwishlist.setProduct_img(mProductListOfManufacturer.get(position).getmLogo());
					mwishlist.setmProductPrice("");
					mwishlist.setmProductUnit("");
					mwishlistDetails.add(mwishlist);
					
					Helper helper=new Helper(getApplicationContext());
					helper.openDataBase();
					helper.insertwhistlistdetails(mwishlistDetails);
				    helper.close();
				    
				    getWishlist();
				    setFeaturedProducts();
					Toast.makeText(getApplicationContext(), "Product is added to wishlist", Toast.LENGTH_SHORT).show();
				}
			});
			
			wishlish_green.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Helper helper=new Helper(getApplicationContext());
					helper.openDataBase();
					SQLiteDatabase sdb=helper.getWritableDatabase();
					value=""+mProductListOfManufacturer.get(position).getmId();
					sdb.execSQL("DELETE FROM whistlist_productmaster where product_id='"+value+"'");
					helper.close();
					getWishlist();
				    setFeaturedProducts();
					Toast.makeText(getApplicationContext(), "Product is removed form wishlist", Toast.LENGTH_SHORT).show();
				}
			});
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//mProductId=mManufacturerList.get(position).getmId();
					mProductId=mProductListOfManufacturer.get(position).getmId();
					CallServices();
					
					//mManufacturerId=mProductListOfManufacturer.get(position).getmId();
					//Toast.makeText(getApplicationContext(), ""+mProductListOfManufacturer.get(position).getmName(), Toast.LENGTH_SHORT).show();
				}
			});
			
			return convertView;
		}
		
		
	}
	
	private void addToWishList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mwishlistDetails=helper.getwishlistdetails();
		helper.close();
		if(mwishlistDetails==null || mwishlistDetails.size()==0){
			/*new AlertDialog.Builder(this)
			.setMessage("Are you sure you want add product to Wish List?")
			.setPositiveButton("Continue", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					addToWishlish();
				}

			}).setNegativeButton("Cancel", null)
			.show();*/
			addToWishlish();
		}else{
			Helper helper1=new Helper(getApplicationContext());
			helper1.openDataBase();
			mwishlistDetails=helper1.getwishlistdetails(mProductId);
			helper1.close();
			
			if(mwishlistDetails==null || mwishlistDetails.size()==0){
				/*new AlertDialog.Builder(this)
				.setMessage("Are you sure you want add product to Wish List?")
				.setPositiveButton("Continue", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						addToWishlish();
					}

				}).setNegativeButton("Cancel", null)
				.show();*/
				addToWishlish();
			}else{
				Toast.makeText(getApplicationContext(), "Product is already exist in wishlist", Toast.LENGTH_SHORT).show();
				/*new AlertDialog.Builder(this)
				.setMessage("Product is already exist in Wish List")
				.setPositiveButton("OK", null)
				.show();*/
			}
		}
	}
	
	private void addToWishlish() {
		//wishlist_white.setVisibility(View.GONE);
		//wishlist_green.setVisibility(View.VISIBLE);
		
		WishlistDetails mwishlist= new WishlistDetails();
		mwishlist.setProduct_id(mProductId);
		mwishlist.setProduct_nam(mProductName);
		mwishlist.setProduct_manufname(mManufacturerName);
		mwishlist.setProduct_img(mProductImage);
		
		List<WishlistDetails> mwishlistDetails= new ArrayList<WishlistDetails>();
		mwishlistDetails.add(mwishlist);
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		helper.insertwhistlistdetails(mwishlistDetails);
	    helper.close();
	    getWishlist();
	    mProductAdapter.notifyDataSetChanged();
		Toast.makeText(getApplicationContext(), "Product is added to wishlist", Toast.LENGTH_SHORT).show();
	}
	
	private void CallServices() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		ConnectionDetector mDetector=new ConnectionDetector(getApplicationContext());
		if(mDetector.isConnectingToInternet()){
			new AsyncProductDetails().execute();
		}else{
			Toast.makeText(getApplicationContext(), "Please check Internet", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	 private class AsyncProductDetails extends AsyncTask<String, Void, Boolean>{
	    	String param = "";
			@Override
			protected void onPreExecute() {
				pdLoading = new ProgressDialog(ServiceListClassActivity.this);
				pdLoading.setMessage("please wait...");
				pdLoading.show();
				pdLoading.setCancelable(false);
				pdLoading.setCanceledOnTouchOutside(false);
				super.onPreExecute();
			}

			@Override
			protected Boolean doInBackground(String... params) {
				getManufacturerDetaisl();
				return isValidDetails();
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				pdLoading.cancel();
				if (pdLoading.isShowing())
					pdLoading.dismiss();
				if(result){
					forwardToProductdetails(mProductId);
				}else{
				}
			}
			
		}
	 
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
					isValid = false;
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
	 
	 public void forwardToManufacturerActivity() {
			Intent moreInfoIntent = new Intent(ServiceListClassActivity.this, ManufacturerDetailActivity.class);
			moreInfoIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(moreInfoIntent);
			overridePendingTransition(0, 0);
		}
	 
	 private void forwardToProductdetails(String mProductId){
		 Intent newIntent = new Intent(ServiceListClassActivity.this,ProductDetailsActivity.class);
		 newIntent.putExtra("PRODUCTID", mProductId);
		 startActivity(newIntent);
		 overridePendingTransition(0, 0);
	 }
	 
	 private Boolean isValidDetails() {
			Boolean isValid=false;
			webServices=new WebServices();
			jsonParserClass=new JsonParserClass();
			List<ProductDetails> mProductDetail = new ArrayList<ProductDetails>();
			Helper helper = new Helper(ServiceListClassActivity.this);
			helper.openDataBase();
			mProductDetails = helper.getAllProductDetails(mProductId);
			if(mProductDetails == null || mProductDetails.size() == 0){
				String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetProductDetail,"GetProductDetail/", mProductId);
				if(resultOutparam==null || resultOutparam.length()==0){
					isValid=false;
				}else{
					mProductDetail=jsonParserClass.parseProductDetails(resultOutparam,getApplicationContext());
					if(mProductDetail==null || mProductDetail.size()==0){
						isValid=false;
					}else{
						helper.insertProductDetails(mProductDetail);
						helper.insertSpecificationDetails(mProductDetail,mProductId);
						helper.insertProductImageDetails(mProductDetail,mProductId);
						helper.insertUserFeedback(mProductDetail,mProductId);
						isValid=true;
					}
				}
			}else{
				isValid=true;
				Log.e("Product details Service calling", "product already exists"+mProductId);
			}
			helper.close();
			return isValid;
		}
	 
	 @Override
		protected void onResume() {
			super.onResume();
			getWishlist();
			setFeaturedProducts();
		}

}
