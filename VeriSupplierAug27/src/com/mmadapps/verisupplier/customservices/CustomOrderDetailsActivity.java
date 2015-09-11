package com.mmadapps.verisupplier.customservices;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.beans.QuotationDashboardHistory;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CustomOrderDetailsActivity extends BaseActionBarActivity{
	
	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	//layout variables
	ImageView productImage;
	TextView productName,address,category,subCategory,quantity,totalPrice,submitButton,orderNumber;
	LinearLayout ordernumber_layout;
	
	ArrayList<QuotationDashboardHistory> mOrderDetails;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placeorder_custom);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Order Details");
		
		initializeView();
		getOrderDetails();
	}

	private void getOrderDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mOrderDetails=helper.getAllDashboardQuotationHistory(null);
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
		
			String mAddress=VerisupplierUtils.mOrderDetails.getmCustomAddress()+","+VerisupplierUtils.mOrderDetails.getmCustomStateName()+","+VerisupplierUtils.mOrderDetails.getmCustomCountryName();
			address.setText(""+mAddress);
			category.setText(""+VerisupplierUtils.mOrderDetails.getmCustomCategory());
			subCategory.setText(""+VerisupplierUtils.mOrderDetails.getmCustomSubCategory());
			quantity.setText(""+VerisupplierUtils.mOrderDetails.getmCustomQuantity()+" "+VerisupplierUtils.mOrderDetails.getmUnitsOfMeasurement());
			totalPrice.setText(""+VerisupplierUtils.mOrderDetails.getmOfferedPrice());
		}
		
	}

}
