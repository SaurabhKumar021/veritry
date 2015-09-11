package com.mmadapps.verisupplier.placeorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;

public class PlaceorderPayment extends BaseActionBarActivity implements OnClickListener{
	public static PlaceorderPayment pop;
	private ImageView vI_POP_productImage;
	private TextView vT_POP_productName,vT_POP_productDescription,vT_POP_unitPriceValue,vT_POP_quantityValue,vT_POP_subtotalValue,vT_POP_subTotalValue,vT_POP_taxValue,vT_POP_totalValue,vT_POP_buttonNext;
	private LinearLayout vL_POP_nextButtonLayout; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placeorder_payment);
		pop = this;
		//BaseActionBarActivity.search_layout.setVisibility(View.GONE);
		//BaseActionBarActivity.title_layout.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("PlaceOrder");
		
		initView();
		setonClick();
		
		vBackView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PlaceorderPayment.this, PlaceorderAddress.class);
				startActivity(intent);
				overridePendingTransition(0, 0);
				finish();
			}
		});
	}

	private void setonClick() {
		vL_POP_nextButtonLayout.setOnClickListener(this);
	}

	private void initView() {
		vI_POP_productImage = (ImageView) findViewById(R.id.vI_POP_productImage);
		vT_POP_productName = (TextView) findViewById(R.id.vT_POP_productName);
		vT_POP_productDescription = (TextView) findViewById(R.id.vT_POP_productDescription);
		vT_POP_unitPriceValue= (TextView) findViewById(R.id.vT_POP_unitPriceValue);
		vT_POP_quantityValue =(TextView) findViewById(R.id.vT_POP_quantityValue);
		vT_POP_subtotalValue = (TextView) findViewById(R.id.vT_POP_subtotalValue);
		vT_POP_subTotalValue = (TextView) findViewById(R.id.vT_POP_subTotalValue);
		vT_POP_taxValue = (TextView) findViewById(R.id.vT_POP_taxValue);
		vT_POP_totalValue = (TextView) findViewById(R.id.vT_POP_totalValue);
		vT_POP_buttonNext = (TextView) findViewById(R.id.vT_POP_buttonNext);
		          
		vL_POP_nextButtonLayout = (LinearLayout) findViewById(R.id.vL_POP_nextButtonLayout);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.vL_POP_nextButtonLayout:
			if(PlaceorderShipping.mFromActivity.equalsIgnoreCase("PD")){
				new PlaceOrder(PlaceorderPayment.this, true, "106",PlaceorderShipping.mManufacturerId,PlaceorderShipping.mProductId).execute();
			}else{
				new PlaceOrder(PlaceorderPayment.this, false, PlaceorderShipping.mQuotationId);
			}
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		vBackView.performClick();
		super.onBackPressed();
	}
}
