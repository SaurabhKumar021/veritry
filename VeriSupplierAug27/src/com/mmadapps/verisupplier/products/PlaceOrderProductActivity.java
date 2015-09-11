package com.mmadapps.verisupplier.products;

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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PlaceOrderProductActivity extends BaseActionBarActivity implements OnClickListener{
	
	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	String mFromActivity;
	
	//layout variables
	ImageView product_image,manufacturer_image;
	TextView ordernumber,orderdate,productname,unitprice,quantity,subtotal,manufacturer_name,manufacturer_address,manufacturer_email,username,useraddress_one,useraddress_two,user_phonenumber;
	LinearLayout confirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placeorder_product);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.vI_ab_dashboardproductMenu.setVisibility(View.VISIBLE);		
		BaseActionBarActivity.setmUserName("Place Order");
		
		mFromActivity=getIntent().getStringExtra("FROMACTIVITY");
		
		initializeValues();
	}

	private void initializeValues() {
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
		
		product_image=(ImageView)findViewById(R.id.vI_apop_productImage);
		manufacturer_image=(ImageView)findViewById(R.id.vI_apop_manufacturerImage);
		
		ordernumber=(TextView)findViewById(R.id.vT_apop_ordernumber);
		orderdate=(TextView)findViewById(R.id.vT_apop_orderdate);
		
		productname=(TextView)findViewById(R.id.vT_apop_productName);
		unitprice=(TextView)findViewById(R.id.vT_apop_unitPrice);
		quantity=(TextView)findViewById(R.id.vT_apop_quantity);
		subtotal=(TextView)findViewById(R.id.vT_apop_subtotal);
		
		manufacturer_name=(TextView)findViewById(R.id.vT_apop_manufacturerName);
		manufacturer_address=(TextView)findViewById(R.id.vT_apop_manufacturerAddress);
		manufacturer_email=(TextView)findViewById(R.id.vT_apop_manufacturerEmail);
		
		username=(TextView)findViewById(R.id.vT_apop_customerName);
		useraddress_one=(TextView)findViewById(R.id.vT_apop_customerAddress1);
		useraddress_two=(TextView)findViewById(R.id.vT_apop_customerAddress2);
		user_phonenumber=(TextView)findViewById(R.id.vT_apop_customerphonenubmer);
		
		confirm=(LinearLayout)findViewById(R.id.vL_apop_confirm);
		confirm.setOnClickListener(this);
		
		setValues();
		
	}

	private void setValues() {
		if(VerisupplierUtils.mQuotationDetails==null){
		}else{
			imageLoader.displayImage(VerisupplierUtils.mQuotationDetails.getmProductImage(), product_image, options);
			imageLoader.displayImage(VerisupplierUtils.mQuotationDetails.getmManufacturerImage(), manufacturer_image, options);

			ordernumber.setText("Order Number:"+VerisupplierUtils.mOrderNumber);
			orderdate.setVisibility(View.GONE);
			
			productname.setText(""+VerisupplierUtils.mQuotationDetails.getmProductName());
			unitprice.setText(""+VerisupplierUtils.mQuotationDetails.getmOfferedPrice());
			quantity.setText(""+VerisupplierUtils.mQuotationDetails.getmOrderQuantity());
			subtotal.setText(""+VerisupplierUtils.mQuotationDetails.getmTotalProductCost());
			
			manufacturer_name.setText(""+VerisupplierUtils.mQuotationDetails.getmMAnufacturerName());
			manufacturer_address.setText(""+VerisupplierUtils.mQuotationDetails.getmManufacturerAddress());
			manufacturer_email.setText(""+VerisupplierUtils.mQuotationDetails.getmManufacturerEmail());
			
			username.setText(""+VerisupplierUtils.mQuotationDetails.getmFirstName());
			useraddress_one.setText(""+VerisupplierUtils.mQuotationDetails.getmAddress());
			useraddress_two.setText(""+VerisupplierUtils.mQuotationDetails.getmAddress2());
			user_phonenumber.setText(""+VerisupplierUtils.mQuotationDetails.getmPhoneNumber());
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vL_apop_confirm:
			finish();
			break;

		default:
			break;
		}
	}

}
