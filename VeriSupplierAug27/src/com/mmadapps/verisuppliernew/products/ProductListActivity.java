package com.mmadapps.verisuppliernew.products;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.MarketPageActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.ManufacturerList;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.ProductList;
import com.mmadapps.verisupplier.beans.WishlistDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.details.ManufacturerDetailActivity;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ProductListActivity  extends BaseActionBarActivity{
	
	//products list
	ListView mProductsList;
	ProductAdapter mProductAdapter;
	
	//from database
	public static List<ProductList> mFeaturedProductsList = new ArrayList<ProductList>();
	List<WishlistDetails> mwishlistDetails = new ArrayList<WishlistDetails>();
	List<ProductDetails> mProductDetails = new ArrayList<ProductDetails>();
	public static List<ManufacturerList> mProductListOfManufacturer=new ArrayList<ManufacturerList>();
	
	//for image
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	String mProductId;
	String mProductName;
	String mManufacturerName;
	String mProductImage;
	String mManufacturerId;
	String mProductPrice;
	String mProductUnit;
	
	//services
	WebServices webServices;
	JsonParserClass jsonParserClass;
	
	//progress loading
	private ProgressDialog pdLoading=null;
	
	//from homepage
	List<ManufacturerList> mManufacturerList=new ArrayList<ManufacturerList>();
	
	//wishlist
	//ImageView wishlist,wishlist_green;
	String value;
	
	//tabbottombar
	LinearLayout home_layout,wishlist_layout,dashboard_layout,openquote_layout,user_profile;
	ImageView wishlist_blank,wishlist_filled;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productlist);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		
		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			BaseActionBarActivity.setmUserName("Products");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("IN")){
			BaseActionBarActivity.setmUserName("Inspections");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("SH")){
			BaseActionBarActivity.setmUserName("Shipping");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("CU")){
			BaseActionBarActivity.setmUserName("Customs");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("WR")){
			BaseActionBarActivity.setmUserName("Warehouse");
		}else if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("EC")){
			BaseActionBarActivity.setmUserName("E-Credit");
		}
		
		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			getProductsListFromDB();
		}else{
			mManufacturerList=MarketPageActivity.mManufacturerList;
		}
		getWishList();
		initializeView();
	}
	
	private void getWishList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mwishlistDetails=helper.getwishlistdetails();
		helper.close();
	}

	private void getProductsListFromDB() {
		Helper helper = new Helper(getApplicationContext());
		helper.openDataBase();
		mFeaturedProductsList = helper.getFeaturedProducts();
		helper.close();
	}

	private void initializeView() {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
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
		
		mProductsList=(ListView)findViewById(R.id.vL_apd_productlist);
		setFeaturedProducts();
		
	}
	
	private void setFeaturedProducts() {
		if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
			if (mFeaturedProductsList == null || mFeaturedProductsList.size() == 0) {
			} else {
				mProductAdapter=new ProductAdapter();
				mProductsList.setAdapter(mProductAdapter);
			}
		}else{
			if (mManufacturerList == null || mManufacturerList.size() == 0) {
			} else {
				mProductAdapter=new ProductAdapter();
				mProductsList.setAdapter(mProductAdapter);
			}
		}
	}

	private class ProductAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
				return mFeaturedProductsList.size();
			}else{
				return mManufacturerList.size();
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
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.productlist_view, parent, false);
			}
			ImageView productImage=(ImageView)convertView.findViewById(R.id.vI_pv_producuImage);
			final TextView product_name=(TextView)convertView.findViewById(R.id.vT_pv_productname);
			final TextView manufacturer_name=(TextView)convertView.findViewById(R.id.vT_pv_manufacturername);
			final TextView product_price_text=(TextView)convertView.findViewById(R.id.vT_pv_productprice_text);
			final TextView product_price=(TextView)convertView.findViewById(R.id.vT_pv_productprice);
			wishlist_blank=(ImageView)convertView.findViewById(R.id.vI_pv_wishlist_blank);
			wishlist_filled=(ImageView)convertView.findViewById(R.id.vI_pv_wishlist_filled);
			
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
						e.printStackTrace();
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
						e.printStackTrace();
					}
				}
			};
			new Thread(runnable2).run();
			
			if(VerisupplierUtils.mServiceGroupType.equalsIgnoreCase("PR")){
				wishlist_blank.setVisibility(View.GONE);
				wishlist_filled.setVisibility(View.GONE);
				/*if(mwishlistDetails==null || mwishlistDetails.size()==0){
				}else{
					for (int i = 0; i < mwishlistDetails.size(); i++) {
						if(mwishlistDetails.get(i).getProduct_id().equalsIgnoreCase(mFeaturedProductsList.get(position).getmProduct_Id())){
							wishlist_filled.setVisibility(View.VISIBLE);
							wishlist_blank.setVisibility(View.GONE);
							break;
						}else{
							wishlist_filled.setVisibility(View.GONE);
							wishlist_blank.setVisibility(View.VISIBLE);
						}
					}
				}*/
				product_name.setText(""+mFeaturedProductsList.get(position).getmProduct_Name());
				manufacturer_name.setText(""+mFeaturedProductsList.get(position).getmManufacturer_Name());
				imageLoader.displayImage(mFeaturedProductsList.get(position).getmProduct_Image(), productImage, options);
				product_price.setText(""+mFeaturedProductsList.get(position).getmProductPrice()+""+mFeaturedProductsList.get(position).getmUnitOfMeasurement());
				
				wishlist_blank.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						WishlistDetails mwishlist= new WishlistDetails();
						mwishlist.setProduct_id(mFeaturedProductsList.get(position).getmProduct_Id());
						mwishlist.setProduct_nam(mFeaturedProductsList.get(position).getmProduct_Name());
						mwishlist.setProduct_manufname(mFeaturedProductsList.get(position).getmManufacturer_Name());
						mwishlist.setProduct_img(mFeaturedProductsList.get(position).getmProduct_Image());
						mwishlist.setmProductPrice(mFeaturedProductsList.get(position).getmProductPrice());
						mwishlist.setmProductUnit(mFeaturedProductsList.get(position).getmUnitOfMeasurement());
						
						List<WishlistDetails> mwishlistDetails= new ArrayList<WishlistDetails>();
						mwishlistDetails.add(mwishlist);
						Helper helper=new Helper(getApplicationContext());
						helper.openDataBase();
						helper.insertwhistlistdetails(mwishlistDetails);
					    helper.close();
					    
					    getWishList();
					    mProductAdapter.notifyDataSetChanged();
						Toast.makeText(getApplicationContext(), "Product is added to wishlist", Toast.LENGTH_SHORT).show();
					}
				});
				
				wishlist_filled.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Helper helper=new Helper(getApplicationContext());
						helper.openDataBase();
						SQLiteDatabase sdb=helper.getWritableDatabase();
						value=mFeaturedProductsList.get(position).getmProduct_Id();
						sdb.execSQL("DELETE FROM whistlist_productmaster where product_id='"+value+"'");
						helper.close();
						getWishList();
						setFeaturedProducts();
						Toast.makeText(getApplicationContext(), "Product is removed form wishlist", Toast.LENGTH_SHORT).show();
					}
				});
				
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mProductId=mFeaturedProductsList.get(position).getmProduct_Id();
						mManufacturerId=mFeaturedProductsList.get(position).getmManufacturer_Id();
						CallServices();
					}
				});
				
			}else{
				wishlist_blank.setVisibility(View.GONE);
				product_name.setText(""+mManufacturerList.get(position).getmName());
				manufacturer_name.setVisibility(View.GONE);
				imageLoader.displayImage(mManufacturerList.get(position).getmLogo(), productImage, options);
				product_price.setVisibility(View.GONE);
				product_price_text.setVisibility(View.GONE);
				
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mManufacturerId=mManufacturerList.get(position).getmId();
						CallManufacturerProductsServices();
					}

				});
			}
			return convertView;
		}
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
			
		 @Override
		 protected void onPreExecute() {
			 pdLoading = new ProgressDialog(ProductListActivity.this);
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
				Toast.makeText(getApplicationContext(), "Network is slow", Toast.LENGTH_SHORT).show();	
			 }
		 }
			
	 }
	 
	 private Boolean isValidDetails() {
		 Boolean isValid=false;
		 webServices=new WebServices();
		 jsonParserClass=new JsonParserClass();
		 List<ProductDetails> mProductDetail = new ArrayList<ProductDetails>();
		 Helper helper = new Helper(ProductListActivity.this);
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
		 Intent moreInfoIntent = new Intent(ProductListActivity.this, ManufacturerDetailActivity.class);
		 moreInfoIntent.putExtra("MANUFACTURERID", mManufacturerId);
		 startActivity(moreInfoIntent);
		 overridePendingTransition(0, 0);
	 }
	 
	 private void forwardToProductdetails(String mProductId){
		 Intent newIntent = new Intent(ProductListActivity.this,ProductDetailsActivity.class);
		 newIntent.putExtra("PRODUCTID", mProductId);
		 startActivity(newIntent);
		 overridePendingTransition(0, 0);
	 }
	 
	 private void CallManufacturerProductsServices() {
		 webServices=new WebServices();
		 jsonParserClass=new JsonParserClass();
		 ConnectionDetector mDetector=new ConnectionDetector(getApplicationContext());
		 if(mDetector.isConnectingToInternet()){
			 new AsyncProductList().execute();
		 }else{
			 Toast.makeText(getApplicationContext(), "Please check Internet", Toast.LENGTH_SHORT).show();
		 }	
	 }
	 
	 private class AsyncProductList extends AsyncTask<Void, Void, Boolean>{
		 
		 @Override
		 protected void onPreExecute() {
			 pdLoading = new ProgressDialog(ProductListActivity.this);
			 pdLoading.setMessage("please wait...");
			 pdLoading.show();
			 pdLoading.setCancelable(false);
			 pdLoading.setCanceledOnTouchOutside(false);
			 super.onPreExecute();
		 }
		 
		 @Override
		 protected Boolean doInBackground(Void... params) {
			 return callProductList();
		 }
		 
		 @Override
		 protected void onPostExecute(Boolean result) {
			 super.onPostExecute(result);
			 pdLoading.cancel();
			 if (pdLoading.isShowing())
				 pdLoading.dismiss();
			 forwordToServiceListActivity();
		 }
	 }
	 
	 private Boolean callProductList() {
		 Boolean isValid=false;
		 webServices=new WebServices();
		 jsonParserClass=new JsonParserClass();
		 String Inparam=mManufacturerId+"/"+VerisupplierUtils.mServiceGroupTypeId;
		 String result=webServices.CallWebHTTPBindingService(ApiType.GetProductListOfManufacturer, "GetProductListOfManufacturer/", Inparam);
		 if(result==null || result.length()==0){
		 }else{
			 mProductListOfManufacturer=jsonParserClass.parseManufacturerProductsList(result,getApplicationContext());
		 }
		 return isValid;
	 }
	 
	 private void forwordToServiceListActivity() {
		 Intent newIntent = new Intent(ProductListActivity.this,ServiceListClassActivity.class);
		 newIntent.putExtra("MANUFACTURERID", mManufacturerId);
		 startActivity(newIntent);
		 overridePendingTransition(0, 0);	
	 }
	 
	 @Override
	protected void onResume() {
		super.onResume();
		
		
	}
	 
}
