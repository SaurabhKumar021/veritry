package com.mmadapps.verisupplier.shipping;

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

public class PlaceOrderShippingActivity extends BaseActionBarActivity implements OnClickListener{
	
	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	//layout variables
	ImageView productImage;
	TextView submitButton,productName,shipDate,shippingFrom,shippingTo,BoxSize,packages,weight,totalWeight,shipType,totalPrice,orderNumber;
	LinearLayout ordernumber_layout;
	
	String mFromActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placeorder_shipping);
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
		
		productImage=(ImageView)findViewById(R.id.vI_apos_productImage);
		productName=(TextView)findViewById(R.id.vT_apos_productName);
		shipDate=(TextView)findViewById(R.id.vT_apos_deliverydate);
		shippingFrom=(TextView)findViewById(R.id.vT_apos_shippingfrom);
		shippingTo=(TextView)findViewById(R.id.vT_apos_shippingto);
		BoxSize=(TextView)findViewById(R.id.vT_apos_boxsize);
		packages=(TextView)findViewById(R.id.vT_apos_nopackages);
		weight=(TextView)findViewById(R.id.vT_apos_weight);
		totalWeight=(TextView)findViewById(R.id.vT_apos_totalweight);
		shipType=(TextView)findViewById(R.id.vT_apos_shiptype);
		totalPrice=(TextView)findViewById(R.id.vT_apos_totalprice);
		
		submitButton=(TextView)findViewById(R.id.vT_apos_submitButton);
		submitButton.setOnClickListener(this);
		ordernumber_layout=(LinearLayout)findViewById(R.id.vL_apos_ordernumber_layout);
		orderNumber=(TextView)findViewById(R.id.vT_apos_ordernumber);
		
		setValues();
		
	}

	private void setValues() {
		
		if(mFromActivity.equalsIgnoreCase("dashboard")){
			ordernumber_layout.setVisibility(View.GONE);
		}else{
			ordernumber_layout.setVisibility(View.VISIBLE);
			orderNumber.setText("Order Number: "+VerisupplierUtils.mOrderNumber);
		}
		imageLoader.displayImage(VerisupplierUtils.mQuotationDetails.getmProductImage(), productImage, options);
		productName.setText(""+VerisupplierUtils.mQuotationDetails.getmProductName());
	
		shipDate.setText(""+VerisupplierUtils.mQuotationDetails.getmDeliveryDate());
		String mFrom=VerisupplierUtils.mQuotationDetails.getmFromCountry()+","+VerisupplierUtils.mQuotationDetails.getmFromCity();
		shippingFrom.setText(""+mFrom);
		String mTo=VerisupplierUtils.mQuotationDetails.getmToCountry()+","+VerisupplierUtils.mQuotationDetails.getmToCity();
		shippingTo.setText(""+mTo);
		String mBoxSize=VerisupplierUtils.mQuotationDetails.getmLength()+"x"+VerisupplierUtils.mQuotationDetails.getmWidth()+"x"+VerisupplierUtils.mQuotationDetails.getmHeight();
		BoxSize.setText(""+mBoxSize);
		packages.setText(""+VerisupplierUtils.mQuotationDetails.getmTotalNumberOfCatons());
		weight.setText(""+VerisupplierUtils.mQuotationDetails.getmShippingWeight());
		totalWeight.setText(""+VerisupplierUtils.mQuotationDetails.getmTotalWeight());
		shipType.setText("");
		totalPrice.setText(""+VerisupplierUtils.mQuotationDetails.getmOfferedPrice());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vT_apos_submitButton:
			VerisupplierUtils.mFromActivity="shippingplaceorder";
			Intent paymentIntent=new Intent(PlaceOrderShippingActivity.this,PaymentActivity.class);
			startActivity(paymentIntent);
			overridePendingTransition(0, 0);
			finish();
			break;

		default:
			break;
		}
	}
}
