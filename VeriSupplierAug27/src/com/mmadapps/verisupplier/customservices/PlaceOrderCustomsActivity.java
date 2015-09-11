package com.mmadapps.verisupplier.customservices;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.placeorder.PaymentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PlaceOrderCustomsActivity extends BaseActionBarActivity implements OnClickListener{
	
	
	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	//layout variables
	ImageView productImage;
	TextView productName,address,category,subCategory,quantity,totalPrice,submitButton,orderNumber;
	LinearLayout ordernumber_layout;
	
	String mFromActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placeorder_custom);
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
		
		productImage=(ImageView)findViewById(R.id.vI_apoc_productImage);
		productName=(TextView)findViewById(R.id.vT_apoc_productName);
		address=(TextView)findViewById(R.id.vT_apoc_address);
		category=(TextView)findViewById(R.id.vT_apoc_category);
		subCategory=(TextView)findViewById(R.id.vT_apoc_subcategory);
		quantity=(TextView)findViewById(R.id.vT_apoc_quantity);
		totalPrice=(TextView)findViewById(R.id.vT_apoc_price);
		
		submitButton=(TextView)findViewById(R.id.vT_apoc_submitButton);
		submitButton.setOnClickListener(this);
		
		ordernumber_layout=(LinearLayout)findViewById(R.id.vL_apoc_ordernumber_layout);
		orderNumber=(TextView)findViewById(R.id.vT_apoc_ordernumber);
		
		setValues();
	}

	private void setValues() {
		if(mFromActivity.equalsIgnoreCase("dashboard")){
			ordernumber_layout.setVisibility(View.GONE);
		}else{
			ordernumber_layout.setVisibility(View.VISIBLE);
			orderNumber.setText("Order Number: "+VerisupplierUtils.mOrderNumber);
		}
		
		imageLoader.displayImage(VerisupplierUtils.mDashboardQuotationDetails.getmProductImage(), productImage, options);
		productName.setText(""+VerisupplierUtils.mDashboardQuotationDetails.getmProductName());
	
		String mAddress=VerisupplierUtils.mDashboardQuotationDetails.getmCustomAddress()+","+VerisupplierUtils.mDashboardQuotationDetails.getmCustomStateName()+","+VerisupplierUtils.mDashboardQuotationDetails.getmCustomCountryName();
		address.setText(""+mAddress);
		category.setText(""+VerisupplierUtils.mDashboardQuotationDetails.getmCustomCategory());
		subCategory.setText(""+VerisupplierUtils.mDashboardQuotationDetails.getmCustomSubCategory());
		quantity.setText(""+VerisupplierUtils.mDashboardQuotationDetails.getmCustomQuantity()+" "+VerisupplierUtils.mDashboardQuotationDetails.getmUnitsOfMeasurement());
		totalPrice.setText(""+VerisupplierUtils.mDashboardQuotationDetails.getmOfferedPrice());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vT_apoc_submitButton:
			VerisupplierUtils.mFromActivity="customplaceorder";
			Intent paymentIntent=new Intent(PlaceOrderCustomsActivity.this,PaymentActivity.class);
			startActivity(paymentIntent);
			overridePendingTransition(0, 0);
			finish();
			break;

		default:
			break;
		}
	}

}
