package com.mmadapps.verisupplier.trademanager;

//import android.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
//import com.mmadapps.verisupplier.beans.AlertGetMessages;
import com.mmadapps.verisupplier.beans.Inventory;
import com.mmadapps.verisupplier.beans.InventoryWarehouseList;
import com.mmadapps.verisupplier.beans.WarehouseList;
import com.mmadapps.verisupplier.trademanager.DashboardActivity.MyAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class CreateInventoryActivity extends BaseActionBarActivity {
	BaseActionBarActivity actionBarActivity;
	LinearLayout vL_ni_layoutselprod, vL_ni_layoutselware;
	String[] strings = { "Bluetooth", "Wrist Watch", "Intel stick", "Mobile" };
	//Service Strings
	WebServices webServices;
	String Inparam="";
	private JsonParserClass jsonParserClass;
	//Adapter Strings
	private List<Inventory> mProductList;
	private List<WarehouseList> mWareHouseList;
	private ImageLoader imageLoader;
	ImageView vI_ni_checkmark;
	List<String> productList = new ArrayList<String>();
	List<String> wareHouseList = new ArrayList<String>();
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_create_inventory);
		
		initControl();
		
		actionBarActivity.vUserTitel.setText("New Inventory");
        actionBarActivity.vUserTitel.setTextColor(Color.parseColor("#4F4F4F"));
        actionBarActivity.vUserSubTitle.setVisibility(View.GONE);
        actionBarActivity.qrscan_image.setVisibility(View.GONE);
        //onclick of checkbutton
        vI_ni_checkmark=(ImageView) findViewById(R.id.vI_ni_checkmark);
        vI_ni_checkmark.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callCreateNewinventory();
			}
		});
		 new AsyncProductsForInventory().execute();
	
	}
private void initControl() {
		// TODO Auto-generated method stub
	 
//		layoutselect.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Log.e("clicked", "clicked");
//				layoutselect.setVisibility(View.GONE);
//				layoutitem.setVisibility(View.VISIBLE);
//				mySpinner.performClick();
//
//			}
//		});
	}
//this adapter for select the product
	
	public class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context context, int textViewResourceId,
				List<String> productList) {
			super(context, textViewResourceId, productList);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
			
			
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row = inflater
					.inflate(R.layout.spinner_adapter, parent, false);
			TextView label = (TextView) row.findViewById(R.id.company);
			label.setText(productList.get(position));

			TextView sub = (TextView) row.findViewById(R.id.sub);
			//sub.setText(subs[position]);

			ImageView icon = (ImageView) row.findViewById(R.id.image);
			//icon.setImageResource(arr_images[position]);

			return row;
		}
	}
	
	//this adapter for select the warehouse
	public class MyAdapterWare extends ArrayAdapter<String> {

		public MyAdapterWare(Context context, int textViewResourceId,
				List<String> wareHouseList) {
			super(context, textViewResourceId, wareHouseList);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row = inflater
					.inflate(R.layout.spinner_adapter, parent, false);
			TextView label = (TextView) row.findViewById(R.id.company);
			label.setText(wareHouseList.get(position));

			TextView sub = (TextView) row.findViewById(R.id.sub);
			//sub.setText(subs[position]);

			ImageView icon = (ImageView) row.findViewById(R.id.image);
			//icon.setImageResource(arr_images[position]);

			return row;
		}
	}
//call service
	private Boolean callService() {
  		// TODO Auto-generated method stub
      	webServices = new WebServices();
      	jsonParserClass=new JsonParserClass();
      	Inparam="/106/1/1000";
      	String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.ProductsForInventory,"GetProductsForInventory", Inparam);
      	Log.e("abc",resultOutparam);
  		if(resultOutparam==null || resultOutparam.length()==0){
  			//isValid=false;
  		}else{
  			mProductList = jsonParserClass.parseproductsForInventory(resultOutparam);
  		Log.e("resultchannel",resultOutparam);
  		}
  		
  		
  		
  		resultOutparam = webServices.CallWebHTTPBindingService(ApiType.ProductsForInventory,"GetWarehouseList", Inparam);
      	Log.e("abc","ware" +resultOutparam);
  		if(resultOutparam==null || resultOutparam.length()==0){
  			//isValid=false;
  		}else{
  			mWareHouseList = jsonParserClass.parseWarehouseList(resultOutparam);
  		Log.e("resultWarehouse",resultOutparam);
  		}
  		
  		
  		return true;
  	}
	private void callCreateNewinventory() {
		// TODO Auto-generated method stub
		jsonParserClass = new JsonParserClass();
 		webServices = new WebServices();
		try {
			String jsonData = null;
			if (validate_form()) {
				jsonData = createJson();
				Log.e("json",jsonData);
				
				
				if (callcreateService(jsonData)) {
					Log.e("jsonwarehouse", "warehouse Created");
					Toast.makeText(getApplicationContext(), "Warehouse Created", Toast.LENGTH_LONG).show();
					finish();
				} else {
					Log.e("jsonchannel", "Warehouse Creation failed");
					Toast.makeText(getApplicationContext(), "Error in Warehouse creation", Toast.LENGTH_LONG).show();
				}
				
				
			}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
				
		private boolean callcreateService(String jsonData) {
		// TODO Auto-generated method stub
		try {
			if (jsonData != null) {
				HttpClient client = new DefaultHttpClient();
				String postURL = webServices.CREATEINVENTORY;// "http://vsuppliervm.cloudapp.net:5132/QuotationService.svc/UpdateQuotation";
				InputStream inputStream = null;

				HttpPost httpPost = new HttpPost(postURL);

				StringEntity se = new StringEntity(jsonData, HTTP.UTF_8);
				httpPost.setEntity(se);

				httpPost.setHeader("Content-type", "application/json");
				httpPost.setHeader("Accept", "application/json");

				HttpResponse httpResponse = client.execute(httpPost);
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				Log.d("statusCode", "" + statusCode);
				if (statusCode != 200) {
					return false;
				}
				inputStream = httpResponse.getEntity().getContent();
				//Log.d("statusCode", "" + inputStream);
				//System.out.println("" + inputStream);
				String result = null;
				if (inputStream != null) {
					result = convertInputStreamToString(inputStream);
					Log.e("jsonchannelinve", result);
				}
				if (result == null || result.length() == 0) {

				} else {
					jsonParserClass = new JsonParserClass();
					String rslt = jsonParserClass.parseCreateWareHouseResult(result);
					if (rslt != null && rslt.equalsIgnoreCase("false")) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log.e("json",e.getMessage());
		}
		return true;
	}
		
		
		private String convertInputStreamToString(InputStream inputStream)
				throws IOException {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = "";
			String result = "";
			while ((line = bufferedReader.readLine()) != null)
				result += line;
			inputStream.close();
			return result;
		}
		
		
		
	private boolean validate_form() {
		// TODO Auto-generated method stub
		return true;
	}
	private String createJson() {
		// TODO Auto-generated method stub
		String json = null;
		try {
			
			JSONObject jsn = new JSONObject();
			JSONObject inventoryProduct = new JSONObject();
			JSONObject wareHouse = new JSONObject();
			
			//wareHouse object
					
			wareHouse.put("WarehouseId","2");
			
			//inventoryProduct object
			
			inventoryProduct.put("CustomerId", "106");
			inventoryProduct.put("ProductId", "59");
			inventoryProduct.put("WareHouse", wareHouse);
			
			
			
		
			
			//Master Object
			jsn.put("inventoryProduct", inventoryProduct);
			jsn.put("StockQuantity", "2000");
			jsn.put("ThresholdQuantity", "200");
			
			json = jsn.toString();
			
		} catch (Exception e) {

		}
		return json;
	}
	
	private class AsyncProductsForInventory extends AsyncTask<Void, Void, Boolean>{
		
		private ProgressDialog pdLoading;
		@Override
  		protected void onPreExecute() {
  			pdLoading = new ProgressDialog(CreateInventoryActivity.this);
  			pdLoading.setMessage("please wait...");
  			pdLoading.show();
  			pdLoading.setCancelable(false);
  			pdLoading.setCanceledOnTouchOutside(false);
  			super.onPreExecute();
  		}
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return callService();
		}
		@Override
  		protected void onPostExecute(Boolean result) {
  			super.onPostExecute(result);
  			pdLoading.cancel();
  			if(pdLoading.isShowing())
  				pdLoading.dismiss();
  			/*alertlist.setAdapter(MyAdapter);
  			alertlist.setOnItemClickListener(new OnItemClickListener() {

  				@Override
  				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
  						long arg3) {
  					Intent intent=new Intent(TradeAlertActivity.this,AlertDetailsActivity.class);
  					startActivity(intent);
  					
  				}
  			});*/
  			//initview();
  			populateproductList();
			setproductSpinner();
			
			populateWareHouseList();
			setWareHouseSpinner();
			
		//	callCreateNewinventory();
  			
  			if(result){
  				//after otp generation go to userOtppopup activity 
  			
  			}else{
  				Toast.makeText(getApplicationContext(), "Please try agin", Toast.LENGTH_SHORT).show();
  			}
  		}
		
		
		
		
		
		
		
		
		private void setproductSpinner() {
			// TODO Auto-generated method stub
		
			
			 final Spinner mySpinner = (Spinner) findViewById(R.id.vS_ni_selectSpinner);
		      vL_ni_layoutselprod=(LinearLayout) findViewById(R.id.vL_ni_layoutselprod);
				//mySpinner.setVisibility(View.GONE);
				//vL_ni_layoutselprod.setVisibility(View.GONE);
				
				mySpinner.setAdapter(new MyAdapter(CreateInventoryActivity.this,
						R.layout.spinner_adapter, productList));
				
				 
		}
		private void populateproductList() {
			// TODO Auto-generated method stub
			for (int i = 0; i < mProductList.size(); i++)
				productList.add(mProductList.get(i).getmProductName());
			
		}
		private void populateWareHouseList() {
			// TODO Auto-generated method stub
			for (int i = 0; i < mWareHouseList.size(); i++)
				wareHouseList.add(mWareHouseList.get(i).getmWareHouseName());
			
		}
		private void setWareHouseSpinner() {
			// TODO Auto-generated method stub
			final Spinner mySpinnerWare = (Spinner) findViewById(R.id.vS_ni_wareSpinner);
			 vL_ni_layoutselware=(LinearLayout) findViewById(R.id.vL_ni_layoutselware);
			 
			 mySpinnerWare.setAdapter(new MyAdapterWare(CreateInventoryActivity.this,
						R.layout.spinner_adapter, wareHouseList));
		}
	}
}
