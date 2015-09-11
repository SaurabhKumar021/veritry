package com.mmadapps.verisupplier.details;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.ProductSpecificationDetails;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ManufacturerDetailActivity extends BaseActionBarActivity{
	
	//for imageloader initialization
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	//intent variables
	String mFromActivity;
	String mManufacturerId;
	String mProductId;
	
	//database variables
	List<ManufacturerDetails> mManufacturerDetails=new ArrayList<ManufacturerDetails>();
	
	//textview variables
	TextView manufacturerCompanyName,manufacturerAddress,manufacturerEmail,basic_information,contact_information,manufacturer_description;
	
	//listview variables
	ListView mManufacuturerAttributesList;
	ManufacuturerAttributesAdapter mManufactureAttributeAdapter;
	List<ProductSpecificationDetails> mManufacturerAttributes=new ArrayList<ProductSpecificationDetails>();
	
	ImageView manufacturerLogo,ratenow_image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manufacturerdetails);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Manufacturer Details");
		
		mManufacturerId = getIntent().getStringExtra("MANUFACTURERID");
		mProductId=getIntent().getStringExtra("PRODUCTID");
		mFromActivity=getIntent().getStringExtra("FROMACTIVITY");
		
		getManufacturerDetails();
		initializeView();
	}

	private void getManufacturerDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mManufacturerDetails=helper.getManufacturerDetails(mManufacturerId);
		mManufacturerAttributes=helper.getManufacturerAttributes(mManufacturerId);
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
		
		manufacturerLogo=(ImageView)findViewById(R.id.vI_amd_manufacturerImage);
		manufacturerCompanyName=(TextView)findViewById(R.id.vT_amd_ManufacturerCompanyName);
		manufacturerAddress=(TextView)findViewById(R.id.vT_amd_ManufacturerAddress);
		manufacturerEmail=(TextView)findViewById(R.id.vT_amd_ManufacturerEmail);
		basic_information=(TextView)findViewById(R.id.vT_amd_basicinformation_text);
		mManufacuturerAttributesList=(ListView)findViewById(R.id.vL_amd_manufacuturerAttributesList);
		
		ratenow_image=(ImageView)findViewById(R.id.vI_amd_ratenow);
		ratenow_image.setOnClickListener(this);
		
		setValues();
	}

	private void setValues() {
		
		if(mManufacturerDetails==null || mManufacturerDetails.size()==0){
		}else{
			imageLoader.displayImage(mManufacturerDetails.get(0).getmManufacturerLogo(),manufacturerLogo, options);
			manufacturerCompanyName.setText(mManufacturerDetails.get(0).getmManufacturerName());
			manufacturerAddress.setText("");
			manufacturerEmail.setText("");
			//manufacturer_description.setText(""+mManufacturerDetails.get(0).getmShortDescription());
		}
		
		if(mManufacturerAttributes==null || mManufacturerAttributes.size()==0){
			basic_information.setVisibility(View.GONE);
		}else{
			basic_information.setVisibility(View.VISIBLE);
			mManufactureAttributeAdapter=new ManufacuturerAttributesAdapter();
			mManufacuturerAttributesList.setAdapter(mManufactureAttributeAdapter);
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
			TextView typeText=(TextView)convertView.findViewById(R.id.vT_dv_type);
			TextView detailText=(TextView)convertView.findViewById(R.id.vT_dv_detail);
			
			
			typeText.setText(""+mManufacturerAttributes.get(position).getmProductAttribute_Name());
			detailText.setText(""+mManufacturerAttributes.get(position).getmProductAttribute_Value());
			
			return convertView;
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vI_amd_ratenow:
			Intent feedbackIntent=new Intent(ManufacturerDetailActivity.this,UserFeedBackPopupActivity.class);
			feedbackIntent.putExtra("FROMACTIVITY", "manufacturerfeedback");
			feedbackIntent.putExtra("PRODUCTID", mProductId);
			feedbackIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(feedbackIntent);
			break;

		default:
			break;
		}
	}

}
