package com.mmadapps.verisupplier.inspection;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class InspectionOrderDetailsActivity extends BaseActionBarActivity{
	

	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	//layout variables
	ImageView productImage;
	TextView productName,address,typeOfIndustry,inspectorType,inspectionType,totalPrice,submitButton,orderNumber;
	LinearLayout ordernumber_layout;
	
	String mFromActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placeorder_inspection);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Place Order");
		
		mFromActivity=getIntent().getStringExtra("FROMACTIVITY");
				
		
		initializeView();
	}
	
	private void initializeView() {
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
		
		productImage=(ImageView)findViewById(R.id.vI_apoi_productImage);
		productName=(TextView)findViewById(R.id.vT_apoi_productName);
		
		address=(TextView)findViewById(R.id.vT_apoi_address);
		typeOfIndustry=(TextView)findViewById(R.id.vT_apoi_typeofindustry);
		inspectorType=(TextView)findViewById(R.id.vT_apoi_inspectortype);
		inspectionType=(TextView)findViewById(R.id.vT_apoi_inspectiontype);
		totalPrice=(TextView)findViewById(R.id.vT_apoi_price);
		submitButton=(TextView)findViewById(R.id.vT_apoi_submitButton);
		submitButton.setOnClickListener(this);
		
		ordernumber_layout=(LinearLayout)findViewById(R.id.vL_apoi_ordernumber_layout);
		orderNumber=(TextView)findViewById(R.id.vT_apoi_ordernumber);
		
		submitButton.setVisibility(View.GONE);
		ordernumber_layout.setVisibility(View.VISIBLE);
		setValues();
		
	}

	private void setValues() {
		if(VerisupplierUtils.mOrderDetails==null){
			
		}else{
			orderNumber.setText("Order Number: "+VerisupplierUtils.mOrderDetails.getmOrderNumber());
			String Wholeaddress = VerisupplierUtils.mOrderDetails.getmInspectionAddress1()+","+VerisupplierUtils.mOrderDetails.getmInspectionStateName()+","+VerisupplierUtils.mOrderDetails.getmInspectionCountryName();
			imageLoader.displayImage(VerisupplierUtils.mOrderDetails.getmProductImage(), productImage, options);
			productName.setText(""+VerisupplierUtils.mOrderDetails.getmProductName());
			address.setText(Wholeaddress);
			typeOfIndustry.setText(""+VerisupplierUtils.mOrderDetails.getmTypeOfIndustry());
			inspectorType.setText(""+VerisupplierUtils.mOrderDetails.getmInspectorType());
			inspectionType.setText(""+VerisupplierUtils.mOrderDetails.getmInspectionType());
			totalPrice.setText(""+VerisupplierUtils.mOrderDetails.getmOfferedPrice());
		}
	}

}
