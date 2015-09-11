package com.mmadapps.verisupplier.trademanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.InventoryWarehouseList;
import com.mmadapps.verisupplier.trademanager.DashboardActivity.MyAdapter;

public class Selectwarehousepopup extends Activity implements OnClickListener {

	BaseActionBarActivity actionBarActivity;
	LinearLayout mainlayout, vL_sw_edittext, vL_aop_afteradd;
	Boolean check = false;
	EditText vE_aop_addqty;
	ImageView vI_pui_check, vI_aop_close;
	OrderActivity activity;
	TextView vT_aop_doyouadd,vT_sw_placeQtytext,vT_sw_unittext,vT_sw_requirementQtytext,vT_sw_stockinwh,vT_sw_addwarehousetext;
	static String mquantity,mWarehouseId;
	public static String product_id;
	public static List<InventoryWarehouseList> inventorywarehouseList;
	
	public static InventoryWarehouseList inventorywarehouse; 
	
	List<String> WareHouse = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		setContentView(R.layout.activity_selectwarehousepopup);

		initControl();
		product_id = getIntent().getStringExtra("ProductId");
		vT_sw_requirementQtytext.setText(getIntent().getStringExtra("Quantity")); 
	//	= getIntent().getStringExtra("ProductId");
		new AsyncWareHouse().execute();
	}

	private void initControl() {
		// TODO Auto-generated method stub
		activity = new OrderActivity();
		vI_pui_check = (ImageView) findViewById(R.id.vI_pui_check);
		vI_pui_check.setOnClickListener(this);
		vL_sw_edittext = (LinearLayout) findViewById(R.id.vL_sw_edittext);
		vL_aop_afteradd = (LinearLayout) findViewById(R.id.vL_aop_afteradd);
		vI_aop_close = (ImageView) findViewById(R.id.vI_aop_close);
		vE_aop_addqty = (EditText) findViewById(R.id.vE_aop_addqty);
		vT_aop_doyouadd = (TextView) findViewById(R.id.vT_aop_doyouadd);
		vI_aop_close.setOnClickListener(this);

		vL_sw_edittext.setVisibility(View.VISIBLE);
		vL_aop_afteradd.setVisibility(View.GONE);
		mainlayout = (LinearLayout) findViewById(R.id.vL_sw_mainlayout);
		android.view.ViewGroup.LayoutParams layoutParams = mainlayout
				.getLayoutParams();
		layoutParams.width = getYOffSetbyDensityforCalWID();
		layoutParams.height = getYOffSetbyDensityforCal();
		mainlayout.setLayoutParams(layoutParams);
		
		
		vT_sw_placeQtytext = (TextView) findViewById(R.id.vT_sw_placeQtytext);
		vT_sw_unittext = (TextView) findViewById(R.id.vT_sw_unittext);
		vT_sw_requirementQtytext = (TextView) findViewById(R.id.vT_sw_requirementQtytext);
		vT_sw_stockinwh = (TextView) findViewById(R.id.vT_sw_stockinwh);
		vT_sw_addwarehousetext = (TextView) findViewById(R.id.vT_sw_addwarehousetext);

		// vS_sw_selectWareHouse = (Spinner)
		// findViewById(R.id.vS_sw_selectWareHouse);
	}

	private class AsyncWareHouse extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub"
			Log.e("sw","called");
			callService();
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			populateWareHouseList();
			setWareHouseSpinner();
		}

		private void setWareHouseSpinner() {
			// TODO Auto-generated method stub

			Spinner vS_sw_selectWareHouse = (Spinner) findViewById(R.id.vS_sw_selectWareHouse);
			vS_sw_selectWareHouse.setAdapter(new WareHouseSpinnerAdapter(
					Selectwarehousepopup.this, R.layout.spinner_adapter,
					WareHouse));
			
			vS_sw_selectWareHouse.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					vT_sw_placeQtytext.setText(inventorywarehouseList.get(position).getAvailablequantity());
					vT_sw_unittext.setText(" / "+inventorywarehouseList.get(position).getAvailableproductunit());
					vT_sw_stockinwh.setText(inventorywarehouseList.get(position).getWarehousename());
					vT_sw_addwarehousetext.setText(inventorywarehouseList.get(position).getWarehousename());
					mWarehouseId = inventorywarehouseList.get(position).getWarehouseid();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
		}

		private void populateWareHouseList() {
			// TODO Auto-generated method stub
			for (int i = 0; i < inventorywarehouseList.size(); i++)
				WareHouse.add(inventorywarehouseList.get(i).getWarehousename());
		}

	}

	private Boolean callService() {
		// TODO Auto-generated method stub
		// Service Stringson
		WebServices webServices;
		String Inparam = "";
		JsonParserClass jsonParserClass;
		webServices = new WebServices();
		jsonParserClass = new JsonParserClass();
		Log.e("callservice", product_id);
		Inparam = "/" + product_id;
		String resultOutparam = webServices.CallWebHTTPBindingService(
				ApiType.GetInventoryWarehouseList,
				"GetInventoryDetailsByProductId", Inparam);
		if (resultOutparam == null || resultOutparam.length() == 0) {
			// isValid=false;
		} else {
			inventorywarehouseList = jsonParserClass
					.parseinventorywarehouseList(resultOutparam);
			// vL_inventorywarehouseList = (ListView)
			// findViewById(R.id.vL_inventorywarehouseList);
			// vL_inventorywarehouseList.setAdapter(adapter);

			// vT_id_totalQtytext.setText(total_product_qty+"");
			Log.e("selwh", resultOutparam);
		}
		return true;
	}

	public class WareHouseSpinnerAdapter extends ArrayAdapter<String> {

		public WareHouseSpinnerAdapter(Context context, int textViewResourceId,
				List<String> wareHouse) {
			super(context, textViewResourceId, wareHouse);
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
			TextView wareHouseName = (TextView) row.findViewById(R.id.company);
			wareHouseName.setText(WareHouse.get(position));

			TextView sub = (TextView) row.findViewById(R.id.sub);
			sub.setVisibility(View.GONE);
			// sub.setText(subs[position]);

			ImageView icon = (ImageView) row.findViewById(R.id.image);
			// icon.setImageResource(arr_images[position]);

			return row;
		}
	}

	private int getYOffSetbyDensityforCalWID() {
		int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_HIGH:
			return 490;
		case DisplayMetrics.DENSITY_XHIGH:
			return 650;
		case DisplayMetrics.DENSITY_XXHIGH:
			return 750;
		default:
			return 490;
		}
	}

	private int getYOffSetbyDensityforCal() {

		int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_HIGH:
			return 800;
		case DisplayMetrics.DENSITY_XHIGH:
			return 900;
		case DisplayMetrics.DENSITY_XXHIGH:
			return 950;
		default:
			return 800;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vI_pui_check:
			if (check == false) {

				mquantity = vE_aop_addqty.getText().toString();
				if (mquantity.length() == 0 || mquantity == null) {
					Toast.makeText(getApplicationContext(),
							"please enter quantity", Toast.LENGTH_SHORT).show();
					check = false;
				} else {
					vL_sw_edittext.setVisibility(View.GONE);
					vL_aop_afteradd.setVisibility(View.VISIBLE);
					vT_aop_doyouadd.setText(mquantity);
					check = true;
				}

			} else {
				finish();
				inventorywarehouse = new InventoryWarehouseList();
				inventorywarehouse.setWarehousename(vT_sw_addwarehousetext.getText().toString());
				inventorywarehouse.setWarehouseid(mWarehouseId);
				inventorywarehouse.setAvailablequantity(mquantity);
				inventorywarehouse.setAvailableproductunit(vT_sw_unittext.getText().toString());
				OrderActivity.inventorywarehouseList.add(inventorywarehouse);
			//	OrderActivity.se
				OrderActivity.vL_ao_warehouselist.setVisibility(View.VISIBLE);
			}

			break;

		case R.id.vI_aop_close:

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
