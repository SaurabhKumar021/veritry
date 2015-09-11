package com.mmadapps.verisupplier.warehouse;

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

public class WarehouseOrderDetailsActivity extends BaseActionBarActivity{
	
	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
		
	//layout variables
	ImageView productImage;
	TextView productName,areaRequirement,address,price,submitButton,orderNumber;
		
	LinearLayout ordernumber_layout;
		
	String mFromActivity;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placeorder_warehouse);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Order Details");
		
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
		
		productImage=(ImageView)findViewById(R.id.vI_apowh_productImage);
		
		productName=(TextView)findViewById(R.id.vT_apowh_productName);
		areaRequirement=(TextView)findViewById(R.id.vT_apowh_area);
		address=(TextView)findViewById(R.id.vT_apowh_address);
		price=(TextView)findViewById(R.id.vT_apowh_price);
		submitButton=(TextView)findViewById(R.id.vT_apowh_submitButton);
		submitButton.setOnClickListener(this);
		
		ordernumber_layout=(LinearLayout)findViewById(R.id.vL_apowh_ordernumber_layout);
		orderNumber=(TextView)findViewById(R.id.vT_apowh_ordernumber);
		
		submitButton.setVisibility(View.GONE);
		ordernumber_layout.setVisibility(View.VISIBLE);
		
		setValues();
	}

	private void setValues() {
		if(VerisupplierUtils.mOrderDetails==null){
			
		}else{
			orderNumber.setText("Order Number: "+VerisupplierUtils.mOrderDetails.getmOrderNumber());
			imageLoader.displayImage(VerisupplierUtils.mOrderDetails.getmProductImage(), productImage, options);
			productName.setText(VerisupplierUtils.mOrderDetails.getmProductName());
		
			String mAddress=VerisupplierUtils.mOrderDetails.getmAddress();
			address.setText(""+mAddress);
			
			String mArea=VerisupplierUtils.mOrderDetails.getmLength()+"x"+VerisupplierUtils.mOrderDetails.getmWidth()+"x"+VerisupplierUtils.mOrderDetails.getmHeight();
			areaRequirement.setText(""+mArea);
			price.setText(""+VerisupplierUtils.mOrderDetails.getmOfferedPrice());
		}
	}

}
