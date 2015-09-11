package com.mmadapps.verisupplier.homepage;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.ProductList;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class FeaturedProductsActivity extends BaseActionBarActivity {

	private GridView dealsGrid;
	private LinearLayout vL_hotdeals;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	String mProductId="";
	WebServices webServices;
	JsonParserClass jsonParserClass;
	ProgressDialog pdLoading=null;
	List<ProductDetails> mProductDetails=new ArrayList<ProductDetails>();
	private List<ProductList> mFeaturedList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_featured_products);
		//BaseActionBarActivity.search_layout.setVisibility(View.GONE);
		//BaseActionBarActivity.title_layout.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("VeriSupplier");
		
		getFeaturedProductsFromDB();
		initView();
	}

	private void getFeaturedProductsFromDB() {
		Helper helper = new Helper(getApplicationContext());
		helper.openDataBase();
		mFeaturedList = helper.getFeaturedProducts();
		helper.close();
	}
	
	private void initView() {
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
			e.printStackTrace();
		}
		
		dealsGrid = (GridView) findViewById(R.id.dealsGrid);
		vL_hotdeals = (LinearLayout) findViewById(R.id.vL_deals_hot_deals);
		vL_hotdeals.setVisibility(View.VISIBLE);
		
		
		
		dealsGrid.setAdapter(new DealsAdapter());
		
		dealsGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				ConnectionDetector mConnectionDector=new ConnectionDetector(getApplicationContext());
				if(mConnectionDector.isConnectingToInternet()){
					mProductId=mFeaturedList.get(position).getmProduct_Id();
					getProductDetails();
					if(mProductDetails==null || mProductDetails.size()==0){
						new AsyncProductDetails().execute();
					}else{
						Intent newIntent = new Intent(FeaturedProductsActivity.this,ProductDetailsActivity.class);
						newIntent.putExtra("PRODUCTID", mProductId);
						startActivity(newIntent);
						overridePendingTransition(0, 0);
					}
				}else{
					Toast.makeText(getApplicationContext(), "please check your network", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private class DealsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(mFeaturedList == null || mFeaturedList.size() == 0){
				return 0;
			}else{
				return mFeaturedList.size();
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
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.hotdeal_viewnew, parent, false);
			}
			LinearLayout enquire_layout=(LinearLayout)convertView.findViewById(R.id.vL_product_enquirenow);
			enquire_layout.setVisibility(View.GONE);
			LinearLayout productdetails=(LinearLayout)convertView.findViewById(R.id.vL_productdetail);
			productdetails.setVisibility(View.VISIBLE);
			LinearLayout manufacturerDetails=(LinearLayout)convertView.findViewById(R.id.vL_manufacturerdetail);
			manufacturerDetails.setVisibility(View.GONE);
			
			TextView product_offer = (TextView) convertView.findViewById(R.id.vT_productOffer);
			ImageView product_image = (ImageView) convertView.findViewById(R.id.vI_productImage);
			TextView productname=(TextView)convertView.findViewById(R.id.vT_productname);
			TextView manufacturername=(TextView)convertView.findViewById(R.id.vT_manufacturername);
			
			if(mFeaturedList.get(position).getmProduct_OfferPrice()==null || mFeaturedList.get(position).getmProduct_OfferPrice().length()==0 || mFeaturedList.get(position).getmProduct_OfferPrice().equalsIgnoreCase("0")){
				product_offer.setVisibility(View.GONE);
			}else{
				product_offer.setText(mFeaturedList.get(position).getmProduct_OfferPrice());

			}
			productname.setText(""+mFeaturedList.get(position).getmProduct_Name());
			manufacturername.setText(""+mFeaturedList.get(position).getmManufacturer_Name());
			imageLoader.displayImage(mFeaturedList.get(position).getmProduct_Image(), product_image, options);
			
			return convertView;
		}
	}
	
	private void getProductDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mProductDetails=helper.getAllProductDetails(mProductId);
		helper.close();
	}
	
	private class AsyncProductDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(FeaturedProductsActivity.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return isValidDetails();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pdLoading.cancel();
			if (pdLoading.isShowing())
				pdLoading.dismiss();
			if(result){
				Intent newIntent = new Intent(FeaturedProductsActivity.this,ProductDetailsActivity.class);
				newIntent.putExtra("PRODUCTID", mProductId);
				startActivity(newIntent);
				overridePendingTransition(0, 0);
			}else{
				
			}
		}
		
	}
	
	private Boolean isValidDetails() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		List<ProductDetails> mProductDetails = new ArrayList<ProductDetails>();
		String Inparam = mProductId;
		String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetProductDetail,"GetProductDetail/", Inparam);
		if(resultOutparam==null || resultOutparam.length()==0){
			isValid=false;
		}else{
			mProductDetails=jsonParserClass.parseProductDetails(resultOutparam,getApplicationContext());
			if(mProductDetails==null || mProductDetails.size()==0){
				isValid=false;
			}else{
				Helper helper=new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertProductDetails(mProductDetails);
				helper.insertSpecificationDetails(mProductDetails,Inparam);
				helper.insertUserFeedback(mProductDetails,Inparam);
				helper.close();
				isValid=true;
			}
		}
		return isValid;
	}
	
}
