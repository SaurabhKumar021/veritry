package com.mmadapps.verisupplier.homepage;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.ProductList;
import com.mmadapps.verisupplier.details.ManufacturerDetailActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ManufacturarActivity extends BaseActionBarActivity{

	private GridView dealsGrid;
	private LinearLayout vL_manufacturars;//,vL_allCategories, vL_hotdeals, vL_newArrivals, vL_qualityPics;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	String mManufacturerId="";
	ProgressDialog pdLoading=null;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	private List<ProductList> mVarifiedManufacturer;
	
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
		
		//getManufacturersFromDB();
		initView();
	}
	
	/*private void getManufacturersFromDB() {
		Helper helper = new Helper(getApplicationContext());
		helper.openDataBase();
		mVarifiedManufacturer = helper.getVerifiedManufacturer();
		helper.close();
	}*/

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
		}

		dealsGrid = (GridView) findViewById(R.id.dealsGrid);
		vL_manufacturars = (LinearLayout) findViewById(R.id.vL_deals_manufacturer);
		vL_manufacturars.setVisibility(View.VISIBLE);
		dealsGrid.setAdapter(new DealsAdapter());
	}
	
	private class DealsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if (mVarifiedManufacturer == null || mVarifiedManufacturer.size() == 0) {
				return 0;
			}
			return mVarifiedManufacturer.size();
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
				convertView = getLayoutInflater().inflate(R.layout.hotdeal_viewnew, parent, false);
			}
			LinearLayout enquire_layout=(LinearLayout)convertView.findViewById(R.id.vL_product_enquirenow);
			enquire_layout.setVisibility(View.GONE);
			LinearLayout productdetails=(LinearLayout)convertView.findViewById(R.id.vL_productdetail);
			productdetails.setVisibility(View.GONE);
			LinearLayout manufacturerDetails=(LinearLayout)convertView.findViewById(R.id.vL_manufacturerdetail);
			manufacturerDetails.setVisibility(View.VISIBLE);
			
			TextView offer_text=(TextView)convertView.findViewById(R.id.vT_productOffer);
			TextView manufacturer_name=(TextView)convertView.findViewById(R.id.vT_manufacturer_name);
			TextView likes_text=(TextView)convertView.findViewById(R.id.vT_manufacturer_liketext);
			ImageView manufacture_image=(ImageView)convertView.findViewById(R.id.vI_productImage);
			
			imageLoader.displayImage(""+mVarifiedManufacturer.get(position).getmManufacturer_Image(), manufacture_image, options);
			offer_text.setVisibility(View.GONE);
			//likes_text.setText(""+HomePageActivity.mVarifiedManufacturer.get(position).getmManufacturer_Likes());
			manufacturer_name.setText(mVarifiedManufacturer.get(position).getmManufacturer_Name());
			likes_text.setText("Likes "+mVarifiedManufacturer.get(position).getmManufacturer_Likes()+"  Positive Feedback "+""+mVarifiedManufacturer.get(position).getmManufacturer_Feedback());
			dealsGrid.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					String mMftrId =mVarifiedManufacturer.get(position).getmManufacturer_Id();
					if (mMftrId == null || mMftrId.length() == 0) {
						Log.e("MANUFACTURER ID","MANUFACTURER ID IS NULL OR EMPTY");
					} else {
						Helper helper = new Helper(getApplicationContext());
						helper.openDataBase();
						List<ManufacturerDetails> mMfrList = helper.getManufacturerDetails(mMftrId);
						if (mMfrList != null && mMfrList.size() > 0) {
							forwardToMfrActivity(mMftrId);
						} else {
							new GetMftrById().execute(mMftrId);
						}
					}
				}
			});
			return convertView;
		}
	}
	
	public void forwardToMfrActivity(String mId) {
		Intent mdaIntent = new Intent(ManufacturarActivity.this, ManufacturerDetailActivity.class);
		mdaIntent.putExtra("MANUFACTURERID", mId);
		mdaIntent.putExtra("FROMACTIVITY", "overview");
		overridePendingTransition(0, 0);
		startActivity(mdaIntent);
	}
	
	private class GetMftrById extends AsyncTask<String, Void, Boolean>{
		String mId = "";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(pdLoading != null && pdLoading.isShowing())
				pdLoading.cancel();
			pdLoading = new ProgressDialog(ManufacturarActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
		}
		@Override
		protected Boolean doInBackground(String... params) {
			mId = params[0];
			return getManufactDetailsById(mId);
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(pdLoading != null && pdLoading.isShowing())
				pdLoading.cancel();
			forwardToMfrActivity(mId);
		}
	}
	
	public Boolean getManufactDetailsById(String mId) {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String result = webServices.CallWebHTTPBindingService(ApiType.GetManufacturerDetail, "GetManufacturerDetail/", mId);

		if(result == null || result.length() == 0){
			Log.e("Manufacturer Details by ID", "Null");
		}else{
			List<ManufacturerDetails> mDetailsList = jsonParserClass.parseManufacturerDetails(result);
			if(mDetailsList != null && mDetailsList.size() > 0){
				Helper helper = new Helper(getApplicationContext());
				helper.openDataBase();
				helper.insertManufacturerDetails(mDetailsList, mId);
				helper.insertManufacturerProducts(mDetailsList, mId);
				helper.close();
			}
		}
		return false;
	}
	
}
