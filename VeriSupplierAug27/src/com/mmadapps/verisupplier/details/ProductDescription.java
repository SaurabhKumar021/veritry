package com.mmadapps.verisupplier.details;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.ProductSpecificationDetails;

public class ProductDescription extends BaseActionBarActivity{
	
	
	LinearLayout description_layout;
	TextView productname,product_description;
	LinearLayout descriptionLayout;
	List<ProductDetails> mProductDetails=new ArrayList<ProductDetails>();
	List<ProductSpecificationDetails> mSpecificationDetails;
	ListView mDescriptionList;
	DescriptionAdapter mAdapter;
	String mProductid;
	View packagingDevider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_description);
		
		//BaseActionBarActivity.search_layout.setVisibility(View.GONE);
		//BaseActionBarActivity.title_layout.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Product Description");
		mProductid = getIntent().getStringExtra("productid");
		product_description = (TextView) findViewById(R.id.vT_PD_packagingDelivery);
		descriptionLayout = (LinearLayout) findViewById(R.id.vL_PD_descriptionLayout);
		
		mSpecificationDetails=new ArrayList<ProductSpecificationDetails>();
		getProductDetails();
		initView();
		setValues();
		
	}
	
	private void getProductDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mProductDetails=helper.getAllProductDetails(mProductid);
		mSpecificationDetails=helper.getSellingDetails(mProductid);
		helper.close();
	}
	
	
	private void initView() {
		mDescriptionList=(ListView)findViewById(R.id.vL_PD_descriptionlist);
		mAdapter=new DescriptionAdapter();
		mDescriptionList.setAdapter(mAdapter);
		setListViewHeightBasedOnChildren(mDescriptionList);
		
		packagingDevider=(View)findViewById(R.id.view_apd_packaging);
		//setValues();
	}
	
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + listView.getDividerHeight() * (listAdapter.getCount()+listAdapter.getViewTypeCount()))+15;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
	
	private void setValues() {
		if(mProductDetails==null || mProductDetails.size()==0){
			descriptionLayout.setVisibility(View.GONE);
		}else{
			String desc = mProductDetails.get(0).getmProduct_description();
			if(desc == null || desc.equalsIgnoreCase("null") || desc.length() == 0){
				packagingDevider.setVisibility(View.GONE);
				descriptionLayout.setVisibility(View.GONE);
			}else{
				packagingDevider.setVisibility(View.VISIBLE);
				product_description.setText(""+desc);
			}
		}
	}
	
	private class DescriptionAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(mSpecificationDetails==null || mSpecificationDetails.size()==0){
				return 0;
			}else{
				return mSpecificationDetails.size();
			}
		
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=getLayoutInflater().inflate(R.layout.description_view, parent, false);
			}
			TextView typeText=(TextView)convertView.findViewById(R.id.vT_dv_type);
			TextView detailText=(TextView)convertView.findViewById(R.id.vT_dv_detail);
			
			if(mSpecificationDetails.size()==0 || mSpecificationDetails==null){
			}else{
				typeText.setText(""+mSpecificationDetails.get(position).getmProductAttribute_Name());
				detailText.setText(": "+mSpecificationDetails.get(position).getmProductAttribute_Value());
			}
			return convertView;
		}
	}

	
}
