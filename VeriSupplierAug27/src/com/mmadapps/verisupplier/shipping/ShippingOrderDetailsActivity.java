package com.mmadapps.verisupplier.shipping;

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

public class ShippingOrderDetailsActivity extends BaseActionBarActivity{
	
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placeorder_shipping);
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
		
		submitButton.setVisibility(View.GONE);
		ordernumber_layout.setVisibility(View.VISIBLE);
		setValues();
	}

	private void setValues() {
		if(VerisupplierUtils.mOrderDetails==null){
			
		}else{
			orderNumber.setText("Order Number: "+VerisupplierUtils.mOrderDetails.getmOrderNumber());
			imageLoader.displayImage(VerisupplierUtils.mOrderDetails.getmProductImage(), productImage, options);
			productName.setText(""+VerisupplierUtils.mOrderDetails.getmProductName());
		
			shipDate.setText(""+VerisupplierUtils.mOrderDetails.getmDeliveryDate());
			String mFrom=VerisupplierUtils.mOrderDetails.getmFromCountry()+","+VerisupplierUtils.mOrderDetails.getmFromCity();
			shippingFrom.setText(""+mFrom);
			String mTo=VerisupplierUtils.mOrderDetails.getmToCountry()+","+VerisupplierUtils.mOrderDetails.getmToCity();
			shippingTo.setText(""+mTo);
			String mBoxSize=VerisupplierUtils.mOrderDetails.getmLength()+"x"+VerisupplierUtils.mOrderDetails.getmWidth()+"x"+VerisupplierUtils.mOrderDetails.getmHeight();
			BoxSize.setText(""+mBoxSize);
			packages.setText(""+VerisupplierUtils.mOrderDetails.getmTotalNumberOfCatons());
			weight.setText(""+VerisupplierUtils.mOrderDetails.getmShippingWeight());
			totalWeight.setText(""+VerisupplierUtils.mOrderDetails.getmTotalWeight());
			shipType.setText("");
			totalPrice.setText(""+VerisupplierUtils.mOrderDetails.getmOfferedPrice());
		}
	}

}
