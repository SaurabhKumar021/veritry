package com.mmadapps.verisupplier.leftmenu;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.beans.WishlistDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class WishListActivity extends BaseActionBarActivity{
	
	ListView wishList;
	WishlistAdapter madapter;
	
	LinearLayout vL_wishlist_layout, vL_wishlistImage_layout;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	String value;
	String mProductId;
	String mManufacturerId;
	List<WishlistDetails> mwishlis= new ArrayList<WishlistDetails>();
	private ProgressDialog pdLoading=null;
	
	WebServices webServices;
	JsonParserClass jsonParserClass;
	UserDetails mUserDetails=new UserDetails();
	List<ProductDetails> mProductDetails = new ArrayList<ProductDetails>();
	String mwishlistServiceGroup_type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deals);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Wish List");
		
		getWishlistDetails();
		initView();

	}

	private void getWishlistDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mwishlis=helper.getwishlistdetails();
		helper.close();
	}

	private void initView() {
		
		wishList = (ListView) findViewById(R.id.dealslist);
		vL_wishlist_layout = (LinearLayout) findViewById(R.id.vL_deals_wishlist);
		vL_wishlist_layout.setVisibility(View.GONE);
       
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
		try {
			options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.ic_launcher)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
					.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
					.build();
		} catch (Exception e) {
		}
		
		madapter=new WishlistAdapter();
		wishList.setAdapter(madapter);
		//DealsAdapter adapter=new DealsAdapter();
		//wishList.setAdapter(adapter);
		
		wishList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				mProductId=mwishlis.get(position).getProduct_id();
				mManufacturerId=mwishlis.get(position).getmManufactureId();
				mwishlistServiceGroup_type=mwishlis.get(position).getmServiceGroupType();
				CallServices();
			}
			
			
		});
	
	}

	
	private class WishlistAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			if(mwishlis==null || mwishlis.size()==0){
				return 0;
			}else{
				return mwishlis.size();
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
				convertView = getLayoutInflater().inflate(R.layout.adapter_wishlistnew, parent, false);
			}
			ImageView vI_hd_Image = (ImageView) convertView.findViewById(R.id.vI_wl_Image);
			TextView pro_name= (TextView) convertView.findViewById(R.id.vT_wl_prodouctname);
			TextView remove_wish=(TextView)convertView.findViewById(R.id.vI_wish_remove);	
			TextView usdollar=(TextView)convertView.findViewById(R.id.vT_wl_usdollar);
			TextView price = (TextView) convertView.findViewById(R.id.vT_wl_price);
			TextView unit=(TextView)convertView.findViewById(R.id.vT_wl_unit);
			
			pro_name.setText(""+mwishlis.get(position).getProduct_nam());
			imageLoader.displayImage(mwishlis.get(position).getProduct_img(),vI_hd_Image, options);
			
			if(mwishlis.get(position).getmProductPrice()==null || mwishlis.get(position).getmProductPrice().equalsIgnoreCase("null")){
			}else{
				usdollar.setText("US$");
				price.setText(" "+mwishlis.get(position).getmProductPrice());
				unit.setText(""+mwishlis.get(position).getmProductUnit());
			}
				
			remove_wish.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getUserId();
					 mProductId = mwishlis.get(0).getProduct_id();
					 new AsyncDeleteWishList().execute();
				
					/*Helper helper=new Helper(getApplicationContext());
					helper.openDataBase();
					SQLiteDatabase sdb=helper.getWritableDatabase();
					value=""+mwishlis.get(position).getProduct_id();
					sdb.execSQL("DELETE FROM whistlist_productmaster where product_id='"+value+"'");
					helper.close();
					mwishlis.remove(position);
					madapter.notifyDataSetChanged();*/
					
										
				}
			});
			
			return convertView;
		}

	}
	
	protected void getUserId() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}
	
	public class AsyncDeleteWishList extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            pdLoading = new ProgressDialog(WishListActivity.this);
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
            	Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				SQLiteDatabase sdb=helper.getWritableDatabase();
				value=mProductId;
				sdb.execSQL("DELETE FROM whistlist_productmaster where product_id='"+value+"'");
				helper.close();
				getWishlistDetails();
				madapter.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(), "product is removed from wishlist", Toast.LENGTH_SHORT).show();
            }else{
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
			 pdLoading = new ProgressDialog(WishListActivity.this);
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
    
    private Boolean isValidDetails() {
		 Boolean isValid=false;
		 webServices=new WebServices();
		 jsonParserClass=new JsonParserClass();
		 List<ProductDetails> mProductDetail = new ArrayList<ProductDetails>();
		 Helper helper = new Helper(WishListActivity.this);
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
    
    private void forwardToProductdetails(String mProductId){
    	if(mwishlistServiceGroup_type.equalsIgnoreCase("1")){
            VerisupplierUtils.mServiceGroupType="PR"; 
        }else if(mwishlistServiceGroup_type.equalsIgnoreCase("2")){
            VerisupplierUtils.mServiceGroupType="SH";
        }else if(mwishlistServiceGroup_type.equalsIgnoreCase("3")){
            VerisupplierUtils.mServiceGroupType="IN";
        }else if(mwishlistServiceGroup_type.equalsIgnoreCase("4")){
            VerisupplierUtils.mServiceGroupType="CU";
        }else if(mwishlistServiceGroup_type.equalsIgnoreCase("7")){
            VerisupplierUtils.mServiceGroupType="WR";
        }else if(mwishlistServiceGroup_type.equalsIgnoreCase("8")){
            VerisupplierUtils.mServiceGroupType="EC";
        }
    	Intent newIntent = new Intent(WishListActivity.this,ProductDetailsActivity.class);
    	newIntent.putExtra("PRODUCTID", mProductId);
    	startActivity(newIntent);
    	overridePendingTransition(0, 0);
	 }
	
}
