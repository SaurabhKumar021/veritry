package com.mmadapps.verisupplier.trademanager;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.InventoryWarehouseList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class InventoryDetails extends BaseActionBarActivity implements OnClickListener {

	ProgressDialog progressDialog;
	LinearLayout mainlayout;
	BaseActionBarActivity actionBarActivity;
	Button updateButton;
	public static List<InventoryWarehouseList> inventorywarehouseList;
	Warehouselistadapter adapter = new Warehouselistadapter();
	public static ListView vL_inventorywarehouseList;
	public static int total_product_qty=0;
	public static String product_id;
	public static TextView vT_id_totalQtytext,vT_id_totalunittext;
	TextView vT_id_producttext,vT_id_mCompanyNametext,vT_id_Qtytext,vT_id_piecetext;
	ImageView vI_ir_logo;
	
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory_details);
		total_product_qty=0;
		
		 imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration
					.createDefault(getApplicationContext()));
			try {
				options = new DisplayImageOptions.Builder()
						.showStubImage(R.drawable.ic_launcher)
						.showImageForEmptyUri(R.drawable.ic_launcher)
						.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
						.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
						.build();
			} catch (Exception e) {
			}
		
		initControl();
	//	Log.e("getintent",getIntent().getStringExtra("ProductName"));
		
		

    
	}
	
	private void initControl() {
		// TODO Auto-generated method stub
		
		//Product Header Details
		vT_id_producttext = (TextView) findViewById(R.id.vT_id_producttext);
		vT_id_mCompanyNametext = (TextView) findViewById(R.id.vT_id_mCompanyNametext);
		vT_id_Qtytext = (TextView) findViewById(R.id.vT_id_Qtytext);
		vT_id_piecetext = (TextView) findViewById(R.id.vT_id_piecetext);
		vI_ir_logo = (ImageView) findViewById(R.id.vI_ir_logo);
		
		vT_id_producttext.setText(getIntent().getStringExtra("ProductName"));
		vT_id_mCompanyNametext.setText(getIntent().getStringExtra("ManufacturerName"));
		vT_id_Qtytext.setText(getIntent().getStringExtra("StockQuantity"));
		vT_id_piecetext.setText(getIntent().getStringExtra("StockUnit"));
		
		
		product_id = getIntent().getStringExtra("ProductId");
		
		imageLoader.displayImage(getIntent().getStringExtra("ProductImage"), vI_ir_logo, options);
		//Product Header Details ends
		
		
		vT_id_totalQtytext = (TextView) findViewById(R.id.vT_id_totalQtytext);
		vT_id_totalunittext = (TextView) findViewById(R.id.vT_id_totalunittext);
		
		
		
		actionBarActivity.vUserTitel.setText("Inventory Details");
        actionBarActivity.vUserTitel.setTextColor(Color.parseColor("#4F4F4F"));
        actionBarActivity.vUserSubTitle.setVisibility(View.GONE);
        actionBarActivity.qrscan_image.setVisibility(View.GONE);
        actionBarActivity.vBackView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        updateButton=(Button) findViewById(R.id.vB_id_updateButton);
        updateButton.setOnClickListener(this);
        
       new AsyncInventoryWarehouse().execute();
        
        callService();
       //updateTotalQuantity();
        //inventorywarehouseList = new List<InventoryWarehouseList>();
	}
	
	public class AsyncInventoryWarehouse extends AsyncTask<String, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			callService();
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			updateTotalQuantity();
		}
		
	}
	
	
	
	private void updateTotalQuantity() {
		// TODO Auto-generated method stub
		for(int i=0;i<inventorywarehouseList.size();i++)
		{
			total_product_qty = total_product_qty + Integer.parseInt(inventorywarehouseList.get(i).getAvailablequantity());
		}
		
		vT_id_totalQtytext.setText(total_product_qty+"");
		vT_id_totalunittext.setText(" / "+inventorywarehouseList.get(0).getAvailableproductunit());
	}

	private Boolean callService() {
  		// TODO Auto-generated method stub
		//Service Strings
		WebServices webServices;
		String Inparam="";
		JsonParserClass jsonParserClass;
      	webServices = new WebServices();
      	jsonParserClass=new JsonParserClass();
      	Log.e("callservice",product_id);
      	Inparam="/"+product_id;
      	String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetInventoryWarehouseList,"GetInventoryDetailsByProductId", Inparam);
  		if(resultOutparam==null || resultOutparam.length()==0){
  			//isValid=false;
  		}else{
  			inventorywarehouseList = jsonParserClass.parseinventorywarehouseList(resultOutparam);
  			 vL_inventorywarehouseList = (ListView) findViewById(R.id.vL_inventorywarehouseList);
  	        vL_inventorywarehouseList.setAdapter(adapter);
  	        
  	      //vT_id_totalQtytext.setText(total_product_qty+"");
  		Log.e("resultchannel",resultOutparam);
  		}
  		return true;
  	}
	

	private class Warehouselistadapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			try{
			return inventorywarehouseList.size();
			} catch(Exception e){
				Log.e("inventorywarehouseList","null");
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			Log.e("total_prod_qty",total_product_qty+"");
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.adapter_inventory_warehouse_list, parent, false);
			}
			
			TextView vT_warehousename = (TextView) convertView.findViewById(R.id.vT_warehousename);
			TextView vT_availablequantity = (TextView) convertView.findViewById(R.id.vT_availablequantity);
			TextView vT_availableproductunit = (TextView) convertView.findViewById(R.id.vT_availableproductunit);
			
			vT_warehousename.setText(inventorywarehouseList.get(position).getWarehousename());
			vT_availablequantity.setText(inventorywarehouseList.get(position).getAvailablequantity());
			vT_availableproductunit.setText(" / "+inventorywarehouseList.get(position).getAvailableproductunit());
			
		//	total_product_qty = total_product_qty + Integer.parseInt(inventorywarehouseList.get(position).getAvailablequantity());
			//vT_id_totalQtytext.setText(total_product_qty+"");
			
			return convertView;
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.vB_id_updateButton:
			Log.e("iv","qw");
			Log.e("iv","qw");
			Intent intent=new Intent(InventoryDetails.this,Inventorypopup.class);
			startActivity(intent);
			
			break;

		default:
			break;
		}
		
		
	}
	
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
}
