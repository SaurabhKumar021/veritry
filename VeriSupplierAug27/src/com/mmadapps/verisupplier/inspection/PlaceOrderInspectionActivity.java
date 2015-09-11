package com.mmadapps.verisupplier.inspection;

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

public class PlaceOrderInspectionActivity extends BaseActionBarActivity implements OnClickListener{
	
	
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
		
		setValues();
	}

	private void setValues() {
		if(mFromActivity.equalsIgnoreCase("dashboard")){
			ordernumber_layout.setVisibility(View.GONE);
		}else{
			ordernumber_layout.setVisibility(View.VISIBLE);
			orderNumber.setText("Order Number: "+VerisupplierUtils.mOrderNumber);
		}
		
		String Wholeaddress = VerisupplierUtils.mQuotationDetails.getmInspectionAddress1()+","+VerisupplierUtils.mQuotationDetails.getmInspectionStateName()+","+VerisupplierUtils.mQuotationDetails.getmInspectionCountryName();
		
		imageLoader.displayImage(VerisupplierUtils.mQuotationDetails.getmProductImage(), productImage, options);
		productName.setText(""+VerisupplierUtils.mQuotationDetails.getmProductName());
		address.setText(Wholeaddress);
		typeOfIndustry.setText(""+VerisupplierUtils.mQuotationDetails.getmTypeOfIndustry());
		inspectorType.setText(""+VerisupplierUtils.mQuotationDetails.getmInspectorType());
		inspectionType.setText(""+VerisupplierUtils.mQuotationDetails.getmInspectionType());
		totalPrice.setText(""+VerisupplierUtils.mQuotationDetails.getmOfferedPrice());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vT_apoi_submitButton:
			VerisupplierUtils.mFromActivity="inspectionplaceorder";
			Intent paymentIntent=new Intent(PlaceOrderInspectionActivity.this,PaymentActivity.class);
			startActivity(paymentIntent);
			overridePendingTransition(0, 0);
			finish();
			break;

		default:
			break;
		}
	}

}
