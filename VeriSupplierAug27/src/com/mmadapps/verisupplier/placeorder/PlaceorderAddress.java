package com.mmadapps.verisupplier.placeorder;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class PlaceorderAddress extends BaseActionBarActivity implements OnClickListener {

	private LinearLayout vL_POA_addressNewLayout,vL_POA_addressLayout,vL_POA_changeAddressLayout,vL_POA_nextButtonLayout;
	
	private TextView vT_POA_customerName, vT_POA_customerAddress1,vT_POA_customerAddress2,vT_POA_customerAddress3,vT_POA_customerAddress4,vT_POA_buttonNext;
	private EditText vE_POA_firstName,vE_POA_lastName,vE_POA_email,vE_POA_company,vE_POA_country,vE_POA_province,vE_POA_city,vE_POA_address1,vE_POA_address2,
					vE_POA_zip,vE_POA_phoneNumber,vE_POA_faxNumber;
	
	ImageLoader imageLoader;
	DisplayImageOptions options;
	UserDetails mUserDetails=new UserDetails();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placeorder_address);
		
		//BaseActionBarActivity.search_layout.setVisibility(View.GONE);
		//BaseActionBarActivity.title_layout.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		
		BaseActionBarActivity.setmUserName("Place Order");
		
		initViews();
		setonClick();
		
		vBackView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent shipping = new Intent(PlaceorderAddress.this, PlaceorderShipping.class);
				startActivity(shipping);
				overridePendingTransition(0, 0);
				finish();
			}
		});
		UserDetails();
	}

	private void UserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
		
	}

	private void setonClick() {
		vL_POA_changeAddressLayout.setOnClickListener(this);
		vL_POA_nextButtonLayout.setOnClickListener(this);
	}

	private void initViews() {
		
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		vL_POA_addressNewLayout = (LinearLayout) findViewById(R.id.vL_POA_addressNewLayout);
		vL_POA_addressLayout = (LinearLayout) findViewById(R.id.vL_POA_addressLayout);
		vL_POA_changeAddressLayout = (LinearLayout) findViewById(R.id.vL_POA_changeAddressLayout);
		vL_POA_nextButtonLayout = (LinearLayout) findViewById(R.id.vL_POA_nextButtonLayout);
		
		vT_POA_customerName = (TextView) findViewById(R.id.vT_POA_customerName);
		vT_POA_customerAddress1 = (TextView) findViewById(R.id.vT_POA_customerAddress1);
		vT_POA_customerAddress2 = (TextView) findViewById(R.id.vT_POA_customerAddress2);
		//vT_POA_customerAddress3 = (TextView) findViewById(R.id.vT_POA_customerAddress3);
		//vT_POA_customerAddress4 = (TextView) findViewById(R.id.vT_POA_customerAddress4);
		vT_POA_buttonNext = (TextView) findViewById(R.id.vT_POA_buttonNext);
		
		vE_POA_firstName = (EditText) findViewById(R.id.vE_POA_firstName);
		vE_POA_lastName = (EditText) findViewById(R.id.vE_POA_lastName);
		vE_POA_email = (EditText) findViewById(R.id.vE_POA_email);
		vE_POA_company = (EditText) findViewById(R.id.vE_POA_company);
		vE_POA_country = (EditText) findViewById(R.id.vE_POA_country);
		vE_POA_province = (EditText) findViewById(R.id.vE_POA_province);
		vE_POA_city = (EditText) findViewById(R.id.vE_POA_city);
		vE_POA_address1 = (EditText) findViewById(R.id.vE_POA_address1);
		vE_POA_address2 = (EditText) findViewById(R.id.vE_POA_address2);
		vE_POA_zip = (EditText) findViewById(R.id.vE_POA_zip);
		vE_POA_phoneNumber = (EditText) findViewById(R.id.vE_POA_phoneNumber);
		vE_POA_faxNumber = (EditText) findViewById(R.id.vE_POA_faxNumber);
		
		//vL_POA_changeAddressLayout.setVisibility(View.GONE);
		setValues();
	}

	private void setValues() {
		if(VerisupplierUtils.mQuotationDetails==null){
			
		}else{
			vT_POA_customerName.setText(""+VerisupplierUtils.mQuotationDetails.getmFirstName());
			vT_POA_customerAddress1.setText(""+VerisupplierUtils.mQuotationDetails.getmAddress());
			vT_POA_customerAddress2.setText(""+VerisupplierUtils.mQuotationDetails.getmCity()+","+VerisupplierUtils.mQuotationDetails.getmCountry());
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.vL_POA_changeAddressLayout:
			vL_POA_addressLayout.setVisibility(View.GONE);
			vL_POA_addressNewLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.vL_POA_nextButtonLayout:
			//redirect to confirmation pop up
			showConfirmation();
			break;
		}
	}

	private void showConfirmation() {
		View view = getLayoutInflater().inflate(R.layout.activity_placeorder_shipping_cart, null, false);
		LinearLayout vL_POS_addressLayout = (LinearLayout) view.findViewById(R.id.vL_POS_addressLayout);
		LinearLayout vL_POS_buttonLayout = (LinearLayout) view.findViewById(R.id.vL_POS_buttonLayout);
		//TextView vL_POS_address = (TextView) view.findViewById(R.id.vL_POS_address);
		TextView button_next = (TextView) view.findViewById(R.id.button_next);
		LinearLayout vL_POS_titleLayout = (LinearLayout) view.findViewById(R.id.vL_POS_titleLayout);
	
		//vL_POS_address.setCompoundDrawablesWithIntrinsicBounds(0, R.raw.placeorder_address_white, 0, 0);
		//(R.raw.placeorder_address_white);
		vL_POS_titleLayout.setVisibility(View.GONE);
		button_next.setText("CONFIRM");
		vL_POS_addressLayout.setVisibility(View.VISIBLE);

		final AlertDialog ad = new AlertDialog.Builder(PlaceorderAddress.this).create();
		ad.setView(view);
		ad.show();
		ad.setCancelable(false);
		ad.setCanceledOnTouchOutside(false);
		
		
		ImageView vI_POS_productImage=(ImageView)view.findViewById(R.id.vI_POS_productImage);
		TextView vT_POS_productName=(TextView)view.findViewById(R.id.vT_POS_productName);
		TextView vT_POS_unitPriceValue=(TextView)view.findViewById(R.id.vT_POS_unitPriceValue);
		TextView vT_POS_quantityValue=(TextView)view.findViewById(R.id.vT_POS_quantityValue);
		TextView vT_POS_subtotalValue=(TextView)view.findViewById(R.id.vT_POS_subtotalValue);
		
		ImageView vI_POS_manufacturerImage=(ImageView)view.findViewById(R.id.vI_POS_manufacturerImage);
		TextView vT_POS_manufacturerName=(TextView)view.findViewById(R.id.vT_POS_manufacturerName);
		TextView vT_POS_manufacturerEmail=(TextView)view.findViewById(R.id.vT_POS_manufacturerEmail);
		TextView vT_POS_manufacturerAddress=(TextView)view.findViewById(R.id.vT_POS_manufacturerAddress);
		
		if(VerisupplierUtils.mQuotationDetails==null){
		}else{
			imageLoader.displayImage(VerisupplierUtils.mQuotationDetails.getmProductImage(),vI_POS_productImage, options);
			vT_POS_productName.setText(""+VerisupplierUtils.mQuotationDetails.getmProductName());
			vT_POS_unitPriceValue.setText(""+VerisupplierUtils.mQuotationDetails.getmOfferedPrice());
			vT_POS_quantityValue.setText(""+VerisupplierUtils.mQuotationDetails.getmOrderQuantity());
			vT_POS_subtotalValue.setText(""+VerisupplierUtils.mQuotationDetails.getmTotalProductCost());
			
			imageLoader.displayImage(VerisupplierUtils.mQuotationDetails.getmManufacturerImage(),vI_POS_manufacturerImage, options);
			vT_POS_manufacturerName.setText(""+VerisupplierUtils.mQuotationDetails.getmMAnufacturerName());
			vT_POS_manufacturerEmail.setText(""+VerisupplierUtils.mQuotationDetails.getmManufacturerEmail());
			vT_POS_manufacturerAddress.setText(""+VerisupplierUtils.mQuotationDetails.getmManufacturerAddress());
			
			
			TextView vT_POS_customerName=(TextView)view.findViewById(R.id.vT_POS_customerName);
			TextView customerAddress1=(TextView)view.findViewById(R.id.customerAddress1);
			TextView customerAddress2=(TextView)view.findViewById(R.id.customerAddress2);
			
			vT_POS_customerName.setText(""+VerisupplierUtils.mQuotationDetails.getmFirstName());
			customerAddress1.setText(""+VerisupplierUtils.mQuotationDetails.getmAddress());
			customerAddress2.setText(""+VerisupplierUtils.mQuotationDetails.getmCity()+","+VerisupplierUtils.mQuotationDetails.getmCountry());
		}
		
		
		vL_POS_buttonLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ad != null && ad.isShowing()){
					ad.cancel();
				}
				Intent payment = new Intent(PlaceorderAddress.this, PaymentActivity.class);
				startActivity(payment);
				overridePendingTransition(0, 0);
				finish();
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		vBackView.performClick();
		super.onBackPressed();
	}
}
