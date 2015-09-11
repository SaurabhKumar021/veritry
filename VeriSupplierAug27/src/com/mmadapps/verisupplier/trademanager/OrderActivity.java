package com.mmadapps.verisupplier.trademanager;

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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.mmadapps.verisupplier.beans.InventoryWarehouseList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class OrderActivity extends BaseActionBarActivity implements OnClickListener {
	LinearLayout vL_ao_selectwarehouse;
	ImageView fulfill,vI_ao_selectware,vI_oa_productimage,vI_oa_channelimage,vI_ao_block,vI_ao_fulfil,vI_ao_cancel;
	BaseActionBarActivity actionBarActivity;
	public static LinearLayout vL_ao_warehouselist;
	public static List<InventoryWarehouseList> inventorywarehouseList =new ArrayList<InventoryWarehouseList>();
	InventoryWarehouseadapter adapter = new InventoryWarehouseadapter();
	
	ListView vL_ao_warehouselistview;
	
	TextView vT_oa_channelnanme, vT_oa_productnanme, vT_oa_quantity,vT_ao_totalquantity,
	vT_oa_orderNumber, vT_oa_date, vT_oa_productname_producttitle,
	vT_oa_manufacturername;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	String warehouseid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		
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
     		}
     		
     		vI_ao_block=(ImageView) findViewById(R.id.vI_ao_block);
     		vI_ao_block.setOnClickListener(new View.OnClickListener() {
				
				//private String OrderStatusId;

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("button","block clicked");
					callBlockorder("1");
				}
			});
     		
     		/*vI_ao_fulfil=(ImageView) findViewById(R.id.vI_ao_fulfil);
     		vI_ao_fulfil.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					callBlockorder("2");
				}
			});*/
     		
     		vI_ao_cancel=(ImageView) findViewById(R.id.vI_ao_cancel);
     		vI_ao_cancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					callBlockorder("3");
				}
			});
     		
		initControl();
		
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 setAdapter();
	}
	private void initControl() {
		// TODO Auto-generated method stub
		actionBarActivity.vUserTitel.setText("Order List");
        actionBarActivity.vUserTitel.setTextColor(Color.parseColor("#4F4F4F"));
        actionBarActivity.vUserSubTitle.setVisibility(View.GONE);
        actionBarActivity.qrscan_image.setVisibility(View.GONE);
        actionBarActivity.vBackView.setOnClickListener(new OnClickListener() {
        
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
        
        //inventorywarehouseList 
        
        vL_ao_selectwarehouse=(LinearLayout) findViewById(R.id.vL_ao_selectwarehouse);
        fulfill=(ImageView) findViewById(R.id.vI_ao_fulfil);
        vL_ao_warehouselist=(LinearLayout) findViewById(R.id.vL_ao_warehouselist);
        vT_ao_totalquantity = (TextView) findViewById(R.id.vT_ao_totalquantity);
        
        setAdapter();
        
        
        fulfill.setOnClickListener(this);
        vL_ao_selectwarehouse.setOnClickListener(this);
        
        //Orderinfo header
        vT_oa_channelnanme=(TextView) findViewById(R.id.vT_oa_channelnanme);
        vT_oa_productnanme=(TextView) findViewById(R.id.vT_oa_productnanme);
        vT_oa_quantity = (TextView) findViewById(R.id.vT_oa_quantity);
        vT_oa_orderNumber = (TextView) findViewById(R.id.vT_oa_orderNumber);
        vT_oa_date = (TextView) findViewById(R.id.vT_oa_date);
        vI_oa_channelimage = (ImageView) findViewById(R.id.vI_oa_channelimage);
        
     // Productinfo header
     		vT_oa_productname_producttitle = (TextView) findViewById(R.id.vT_oa_productname_producttitle);
     		vT_oa_manufacturername = (TextView) findViewById(R.id.vT_oa_manufacturername);
     		vI_oa_productimage = (ImageView) findViewById(R.id.vI_oa_productImage);
        setOrderInfo();
        setProductInfo();
        
	}
	public void setAdapter() {
		// TODO Auto-generated method stub
		vL_ao_warehouselistview = (ListView) findViewById(R.id.vL_ao_warehouselistview);
		vL_ao_warehouselistview.setAdapter(adapter);
		
	}
	private void setOrderInfo() {
		// TODO Auto-generated method stub
		vT_oa_channelnanme.setText(getIntent().getStringExtra("ChannelName"));
		vT_oa_productnanme.setText(getIntent().getStringExtra("ProductName"));
		vT_oa_quantity.setText(getIntent().getStringExtra("Quantity"));
		vT_oa_orderNumber.setText(getIntent().getStringExtra("OrderId"));
		vT_oa_date.setText(getIntent().getStringExtra("OrderDate"));
		
		imageLoader.displayImage(getIntent().getStringExtra("ChannelLogo"), vI_oa_channelimage, options);
	}
	private void setProductInfo() {
		// TODO Auto-generated method stub
		vT_oa_productname_producttitle.setText(getIntent().getStringExtra(
				"ProductName"));
		Log.e("productlogo",getIntent().getStringExtra("ProductLogo"));
		imageLoader.displayImage(getIntent().getStringExtra("ProductLogo"),
				vI_oa_productimage, options);
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vI_ao_fulfil:
			callBlockorder("2");
			Intent intent=new Intent(OrderActivity.this,Fulfilpopup.class);
			startActivity(intent);
			
			break;
		case R.id.vL_ao_selectwarehouse:
			
			Intent intent1 = new Intent(OrderActivity.this,
					Selectwarehousepopup.class);
			intent1.putExtra("ProductId", getIntent().getStringExtra("ProductId"));
			intent1.putExtra("Quantity", getIntent().getStringExtra("Quantity"));
			startActivity(intent1);

			
			break;

		default:
			break;
		}
		
	}
	
	
	private class InventoryWarehouseadapter extends BaseAdapter {

		@Override
		public int getCount() {
			try {
				return inventorywarehouseList.size();
			} catch (Exception e) {
				Log.e("OrderList","null");
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		
		//	Log.e("total_prod_qty",total_product_qty+"");
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.adapter_inventory_warehouse_list, parent, false);
			}
			
			TextView vT_warehousename = (TextView) convertView.findViewById(R.id.vT_warehousename);
			TextView vT_availablequantity = (TextView) convertView.findViewById(R.id.vT_availablequantity);
			TextView vT_availableproductunit = (TextView) convertView.findViewById(R.id.vT_availableproductunit);
			
			vT_warehousename.setText(inventorywarehouseList.get(position).getWarehousename());
			vT_availablequantity.setText(inventorywarehouseList.get(position).getAvailablequantity());
			vT_availableproductunit.setText(inventorywarehouseList.get(position).getAvailableproductunit());

			return convertView;
		}

	}
	private void callBlockorder(String orderStatusId) {
		// TODO Auto-generated method stub
		Log.e("button","block method called");
		jsonParserClass = new JsonParserClass();
 		webServices = new WebServices();
		try {
			String jsonData = null;
			if (validate_form()) {
				jsonData = createJson(orderStatusId);
				Log.e("json",jsonData);
				
				
				if (callBlockService(jsonData)) {
					Log.e("jsonwarehouse", "warehouse Updated");
					Toast.makeText(getApplicationContext(), "order Block", Toast.LENGTH_LONG).show();
					finish();
				} else {
					Log.e("jsonchannel", "order blocked  failed");
					Toast.makeText(getApplicationContext(), "Error in blocking", Toast.LENGTH_LONG).show();
				}
				
				
			}
			}catch (Exception e) {
				// TODO: handle exception
				Log.e("button","err"+e.getMessage());
			}
		}
				
		private boolean validate_form() {
		// TODO Auto-generated method stub
		return true;
	}
		private String createJson(String orderStatusId) {
			// TODO Auto-generated method stub
			String json = null;
			try {
				
//				{
//				    "updateInventory":{
//				        "CustomerId":"106",
//				        "InventoryId":"2",
//				        "OrderQuantity":"-100",
//				        "WareHouse":{
//				            "WarehouseId":"2"
//				        },
//				        "OrderStatusId":"1",    1-Fullfill,2-block,3-cancel,0-update warehouse
//				        "OrderNo":"0"
//				    }
//				}

				
				JSONObject jsn = new JSONObject();
				JSONObject updateInventory = new JSONObject();
				JSONObject wareHouse = new JSONObject();
				
				//wareHouse object
						
				wareHouse.put("WarehouseId",warehouseid);
				
				//inventoryProduct object
				
				updateInventory.put("CustomerId", "106");
				updateInventory.put("InventoryId", "2");
				updateInventory.put("OrderQuantity", vT_ao_totalquantity.getText().toString());
				updateInventory.put("WareHouse", wareHouse);

				// update inventory
				//Master Object
				jsn.put("updateInventory", updateInventory);
				jsn.put("OrderStatusId",orderStatusId);
				jsn.put("OrderNo", "0");
				json = jsn.toString();
				
			} catch (Exception e) {

			}
			return json;
		}

		
		
		private boolean callBlockService(String jsonData) {
		// TODO Auto-generated method stub
		try {
			Log.e("button","block service called");
			if (jsonData != null) {
				HttpClient client = new DefaultHttpClient();
				String postURL = webServices.UPDATEINVENTORY;// "http://vsuppliervm.cloudapp.net:5132/QuotationService.svc/UpdateQuotation";
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

	

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
