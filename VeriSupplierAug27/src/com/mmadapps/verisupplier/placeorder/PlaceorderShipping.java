package com.mmadapps.verisupplier.placeorder;

import java.util.ArrayList;
import java.util.List;

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
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class PlaceorderShipping extends BaseActionBarActivity implements OnClickListener {

	private ImageView vI_POS_productImage,vI_POS_manufacturerImage;
	private TextView vT_POS_productName,vT_POS_productDescription,vT_POS_unitPriceValue,vT_POS_quantityValue,
					vT_POS_subtotalValue,vT_POS_manufacturerName,vT_POS_manufacturerEmail,vT_POS_manufacturerAddress,
					vT_POS_advancePercentage,vT_POS_advanceDate,vT_POS_assemblyPercentage,vT_POS_assemblyDate,vT_POS_shippingPercentage,
					vT_POS_shippingDate;
	private LinearLayout vL_POS_addressLayout, vL_POS_buttonLayout;
	private TextView button_next;
	
	ImageLoader imageLoader;
	DisplayImageOptions options;
	//List<ManufacturerDetails> mManufacturerDetails = new ArrayList<ManufacturerDetails>();
	public static String mProductId="",mManufacturerId="", mQuotationId="";
	public static String mFromActivity = "";
	//public static List<ProductDetails> mSaveOrderValues;
	//private List<ProductDetails> mProductDetails;
	//List<ProductSpecificationDetails> mSpecificationDetails = new ArrayList<ProductSpecificationDetails>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placeorder_shipping_cart);
		initViews();
		setonClick();
		
		//BaseActionBarActivity.search_layout.setVisibility(View.GONE);
		//BaseActionBarActivity.title_layout.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Place Order");
		
		vBackView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//finish();
			}
		});
		
	}

	private void setonClick() {
		vL_POS_buttonLayout.setOnClickListener(this);
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
		
		vI_POS_productImage = (ImageView) findViewById(R.id.vI_POS_productImage);
		vI_POS_manufacturerImage = (ImageView) findViewById(R.id.vI_POS_manufacturerImage);
		
		vT_POS_productName = (TextView) findViewById(R.id.vT_POS_productName);
		vT_POS_productDescription = (TextView) findViewById(R.id.vT_POS_productDescription);
		vT_POS_unitPriceValue = (TextView) findViewById(R.id.vT_POS_unitPriceValue);
		vT_POS_quantityValue = (TextView) findViewById(R.id.vT_POS_quantityValue);
		vT_POS_subtotalValue = (TextView) findViewById(R.id.vT_POS_subtotalValue);
		vT_POS_manufacturerName = (TextView) findViewById(R.id.vT_POS_manufacturerName);
		vT_POS_manufacturerEmail = (TextView) findViewById(R.id.vT_POS_manufacturerEmail);
		vT_POS_manufacturerAddress = (TextView) findViewById(R.id.vT_POS_manufacturerAddress);
		vT_POS_advancePercentage = (TextView) findViewById(R.id.vT_POS_advancePercentage);
		vT_POS_advanceDate = (TextView) findViewById(R.id.vT_POS_advanceDate);
		vT_POS_assemblyPercentage = (TextView) findViewById(R.id.vT_POS_assemblyPercentage);
		vT_POS_assemblyDate = (TextView) findViewById(R.id.vT_POS_assemblyDate);
		vT_POS_shippingPercentage = (TextView) findViewById(R.id.vT_POS_shippingPercentage);
		vT_POS_shippingDate = (TextView) findViewById(R.id.vT_POS_shippingDate);
		
		vL_POS_addressLayout = (LinearLayout) findViewById(R.id.vL_POS_addressLayout);
		vL_POS_buttonLayout = (LinearLayout) findViewById(R.id.vL_POS_buttonLayout);
		button_next = (TextView) findViewById(R.id.button_next);
		vL_POS_addressLayout.setVisibility(View.GONE);

		setValues();
	}

	private void setValues() {
		if(VerisupplierUtils.mQuotationDetails==null){
			
		}else{
			if(VerisupplierUtils.mQuotationDetails.getmProductImage()==null){
				
			}else{
				imageLoader.displayImage(VerisupplierUtils.mQuotationDetails.getmProductImage(),vI_POS_productImage, options);
			}
			
			if(VerisupplierUtils.mQuotationDetails.getmProductName()==null){
				vT_POS_productName.setText("");
			}else{
				vT_POS_productName.setText(""+VerisupplierUtils.mQuotationDetails.getmProductName());
			}
			
			
			if(VerisupplierUtils.mQuotationDetails.getmOfferedPrice()==null){
				vT_POS_unitPriceValue.setText("");
			}else{
				vT_POS_unitPriceValue.setText("US$"+VerisupplierUtils.mQuotationDetails.getmOfferedPrice());
			}
			
			if(VerisupplierUtils.mQuotationDetails.getmOrderQuantity()==null){
				vT_POS_quantityValue.setText("");
			}else{
				vT_POS_quantityValue.setText(""+VerisupplierUtils.mQuotationDetails.getmOrderQuantity());
			}
			
			
			
			if(VerisupplierUtils.mQuotationDetails.getmTotalProductCost()==null){
				vT_POS_subtotalValue.setText("");
			}else{
				vT_POS_subtotalValue.setText("US$"+VerisupplierUtils.mQuotationDetails.getmTotalProductCost());
			}
		
			
			if(VerisupplierUtils.mQuotationDetails.getmMAnufacturerName()==null){
				vT_POS_manufacturerName.setText("");
			}else{
				vT_POS_manufacturerName.setText(""+VerisupplierUtils.mQuotationDetails.getmMAnufacturerName());
			}
			
			if(VerisupplierUtils.mQuotationDetails.getmManufacturerEmail()==null){
				vT_POS_manufacturerEmail.setText("");
			}else{
				vT_POS_manufacturerEmail.setText(""+VerisupplierUtils.mQuotationDetails.getmManufacturerEmail());
			}
			if(VerisupplierUtils.mQuotationDetails.getmManufacturerAddress()==null){
				vT_POS_manufacturerAddress.setText("");
			}else{
				vT_POS_manufacturerAddress.setText(""+VerisupplierUtils.mQuotationDetails.getmManufacturerAddress());
			}
			if(VerisupplierUtils.mQuotationDetails.getmManufacturerImage()==null){
				
			}else{
				imageLoader.displayImage(""+VerisupplierUtils.mQuotationDetails.getmManufacturerImage(),vI_POS_manufacturerImage, options);
			}	
		}
		
		
		
		
		
		
		/*if (mManufacturerDetails == null || mManufacturerDetails.size() == 0) {
		} else {
			
			
			String[] address = mManufacturerDetails.get(0).getmAddressDetails().split(",");
			String add1 = address[0];

			String[] email = mManufacturerDetails.get(0).getmEmailDetails().split(",");
			String email1 = email[0];
			
			
			
		}*/
		
		/*mSaveOrderValues=new ArrayList<ProductDetails>();
		ProductDetails mOrderDetails=new ProductDetails();
		mOrderDetails.setmProduct_Name(VerisupplierUtils.mQuotationDetails.getmProductName());
		mOrderDetails.setmProduct_Image(VerisupplierUtils.mQuotationDetails.getmProductImage());
		mOrderDetails.setmProduct_Id(VerisupplierUtils.mQuotationDetails.getmProductId());
		mOrderDetails.setmProduct_price(VerisupplierUtils.mQuotationDetails.getmOfferedPrice());
		mOrderDetails.setmProduct_moq(VerisupplierUtils.mQuotationDetails.getmOrderQuantity());
		mOrderDetails.setmManufacturer_id(VerisupplierUtils.mQuotationDetails.getmManufacturerId());
		mOrderDetails.setmManufacturer_name(VerisupplierUtils.mQuotationDetails.getmMAnufacturerName());
		mOrderDetails.setmManufacturer_image(VerisupplierUtils.mQuotationDetails.getmManufacturerImage());
		mOrderDetails.setmManufacturerEmail(VerisupplierUtils.mQuotationDetails.getmManufacturerEmail());
		mOrderDetails.setmManufacturerAddress(VerisupplierUtils.mQuotationDetails.getmManufacturerAddress());
		mOrderDetails.setmProductSubTotal(VerisupplierUtils.mQuotationDetails.getmTotalProductCost());
		mSaveOrderValues.add(mOrderDetails);*/
	
	}

	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.vL_POS_buttonLayout:
				Intent address = new Intent(PlaceorderShipping.this, PlaceorderAddress.class);
				startActivity(address);
				overridePendingTransition(0, 0);
				finish();
				break;
		}
	}
	
	@Override
	public void onBackPressed() {
		vBackView.performClick();
		super.onBackPressed();
	}
}
