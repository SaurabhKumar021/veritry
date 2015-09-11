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

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.beans.WarehouseList;

public class Inventorypopup extends Activity implements OnClickListener{

	LinearLayout mainlayout;
	ImageView vI_pui_close,vI_pui_check;
	EditText quantity;
	Spinner warehousespinner;
	ArrayAdapter<String> warehouselist;
	ArrayList<String> warehouselistnew;
	TextView warehousename,quantityold;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	String warehouseid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		setContentView(R.layout.popup_updateinventory);
		
		vI_pui_close=(ImageView) findViewById(R.id.vI_pui_close);
		vI_pui_close.setOnClickListener(this);
		vI_pui_check=(ImageView) findViewById(R.id.vI_pui_check);
		vI_pui_check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callUpdateinventory();
			}
		});
		warehouselistnew=new ArrayList<String>();
		
		for (int i = 0; i < InventoryDetails.inventorywarehouseList.size(); i++) {
			String name=InventoryDetails.inventorywarehouseList.get(i).getWarehousename();
			warehouselistnew.add(name);
		}
		
		warehousename=(TextView) findViewById(R.id.vT_pui_placetext);
		quantityold=(TextView) findViewById(R.id.vT_pui_placeQtytext);
		
		quantity=(EditText) findViewById(R.id.vT_pui_uploadQtytext);
		warehousespinner=(Spinner) findViewById(R.id.vS_pui_selectSpinner);
		warehouselist=new ArrayAdapter<String>(Inventorypopup.this,R.layout.adapter_spinnerwarehouse,R.id.vT_asw_warehousename,warehouselistnew);
		warehousespinner.setAdapter(warehouselist);
		
		
		warehousespinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				String W_name=InventoryDetails.inventorywarehouseList.get(arg2).getWarehousename();
				String w_qua=InventoryDetails.inventorywarehouseList.get(arg2).getAvailablequantity();
				warehouseid=InventoryDetails.inventorywarehouseList.get(arg2).getWarehouseid();
				warehousename.setText(W_name);
				quantityold.setText(w_qua);
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
		
		mainlayout=(LinearLayout) findViewById(R.id.updateinventory);
		android.view.ViewGroup.LayoutParams layoutParams = mainlayout.getLayoutParams();
		layoutParams.width = LayoutParams.WRAP_CONTENT;
		layoutParams.height = getYOffSetbyDensityforCal();
		mainlayout.setLayoutParams(layoutParams);
//		initvalues();
		 
	}
	
	private int getYOffSetbyDensityforCal() {
		
		int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_HIGH:
			return 750;
		case DisplayMetrics.DENSITY_XHIGH:
			return 850;
		case DisplayMetrics.DENSITY_XXHIGH:
			return 900;
		default:
			return 750;
		}
	}
	
	
//	private void initvalues() {
//		facebook=(TextView) findViewById(R.id.vT_SUP_facebook);
//		facebooktext=(TextView) findViewById(R.id.vT_SUP_logface);
//		signinuser=(TextView) findViewById(R.id.vT_SUP_signup);
//		signinusertext=(TextView) findViewById(R.id.vT_SUP_logsign);
//		facebooklay=(LinearLayout) findViewById(R.id.vL_SUP_facebooklayout);
//		signinlay=(LinearLayout) findViewById(R.id.vL_SUP_signup);
//		facebooklay.setOnClickListener(this);
//		signinlay.setOnClickListener(this);
//	}
	
	
	private void callUpdateinventory() {
		// TODO Auto-generated method stub
		jsonParserClass = new JsonParserClass();
 		webServices = new WebServices();
		try {
			String jsonData = null;
			if (validate_form()) {
				jsonData = createJson();
				Log.e("json",jsonData);
				
				
				if (callUpdateService(jsonData)) {
					Log.e("jsonwarehouse", "warehouse Updated");
					Toast.makeText(getApplicationContext(), "Warehouse Updated", Toast.LENGTH_LONG).show();
					finish();
				} else {
					Log.e("jsonchannel", "Warehouse Updation failed");
					Toast.makeText(getApplicationContext(), "Error in Warehouse Updated", Toast.LENGTH_LONG).show();
				}
				
				
			}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
				
		private boolean callUpdateService(String jsonData) {
		// TODO Auto-generated method stub
		try {
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
		
		
		
	private boolean validate_form() {
		// TODO Auto-generated method stub
		return true;
	}
	
	private String createJson() {
		// TODO Auto-generated method stub
		String json = null;
		try {
			
//			{
//			    "updateInventory":{
//			        "CustomerId":"106",
//			        "InventoryId":"2",
//			        "OrderQuantity":"-100",
//			        "WareHouse":{
//			            "WarehouseId":"2"
//			        },
//			        "OrderStatusId":"1",    1-Fullfill,2-block,3-cancel,0-update warehouse
//			        "OrderNo":"0"
//			    }
//			}

			
			JSONObject jsn = new JSONObject();
			JSONObject updateInventory = new JSONObject();
			JSONObject wareHouse = new JSONObject();
			
			//wareHouse object
					
			wareHouse.put("WarehouseId",warehouseid);
			
			//inventoryProduct object
			
			updateInventory.put("CustomerId", "106");
			updateInventory.put("InventoryId", "2");
			updateInventory.put("OrderQuantity", "100");
			updateInventory.put("WareHouse", wareHouse);

			// update inventory
			//Master Object
			jsn.put("updateInventory", updateInventory);
			jsn.put("OrderStatusId","0");
			jsn.put("OrderNo", "0");
			json = jsn.toString();
			
		} catch (Exception e) {

		}
		return json;
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vI_pui_close:
			finish();
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
