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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.Inventory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class InventorylistActivity extends BaseActionBarActivity implements OnClickListener {
ListView productlist;
BaseActionBarActivity actionBarActivity;
InventorylistAdapter adapter = new InventorylistAdapter();
private DisplayImageOptions options;
ImageView addInventory;

// Service Strings
WebServices webServices;
String Inparam = "";
private JsonParserClass jsonParserClass;

// Adapter Strings
private List<Inventory> mInventoryList;
private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventorylist);
		BaseActionBarActivity.vUserTitel.setText("Inventory List");
		BaseActionBarActivity.vUserTitel.setTextColor(Color.parseColor("#4F4F4F"));
		BaseActionBarActivity.vUserSubTitle.setVisibility(View.GONE);
		BaseActionBarActivity.Trademenu.setVisibility(View.VISIBLE);
       // actionBarActivity.vtradeBack.setVisibility(View.GONE);
        BaseActionBarActivity.vBackView.setVisibility(View.GONE);
        BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
        
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
		
		
		new AsyncInventoryList().execute();
		
	}
	
	private void InitViews() {
		// TODO Auto-generated method stub
		addInventory = (ImageView) findViewById(R.id.vI_wh_CreateInventory);
		addInventory.setOnClickListener(this);
		
		productlist = (ListView) findViewById(R.id.vL_il_inventorylist);
		productlist.setAdapter(adapter);
		productlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
			Log.e("invlist",mInventoryList.get(position).getmProductName());
				
				Intent intent=new Intent(InventorylistActivity.this,InventoryDetails.class);
				intent.putExtra("ProductId", mInventoryList.get(position).getmProductId());
				intent.putExtra("ProductName", mInventoryList.get(position).getmProductName());
				intent.putExtra("ManufacturerName", mInventoryList.get(position).getmManufactureName());
				intent.putExtra("StockQuantity", mInventoryList.get(position).getmTotalStockQuantity());
				intent.putExtra("StockUnit", " / "+mInventoryList.get(position).getmUnit());
				intent.putExtra("ProductImage", mInventoryList.get(position).getMlogo());
				startActivity(intent);
				
			}
		});
	}
	private Boolean callService() {
		// TODO Auto-generated method stub
		webServices = new WebServices();
		jsonParserClass = new JsonParserClass();
		Inparam="106/1/10";
		String resultOutparam = webServices.CallWebHTTPBindingService(
				ApiType.GetInventory, "GetInventoryProductByCustomerId/", Inparam);
		if (resultOutparam == null || resultOutparam.length() == 0) {
			// isValid=false;
		} else {
			mInventoryList = jsonParserClass.parseInventoryList(resultOutparam);
			String size= ""+mInventoryList.size();
			Log.e("resultinventory", size);
		}
		return true;
	}
	
	
	private class AsyncInventoryList extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog pdLoading;

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(InventorylistActivity.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return callService();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pdLoading.cancel();
			if (pdLoading.isShowing())
				pdLoading.dismiss();
			InitViews();
			if (result) {
				// after otp generation go to userOtppopup activity

			} else {
				Toast.makeText(getApplicationContext(), "Please try agin",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class InventorylistAdapter extends BaseAdapter {

		@Override
		public int getCount() {
         try{
			return mInventoryList.size();
         }catch(Exception e){
        	 Log.e("mInventoryList","null");
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

			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.adapter_inventoryproducts, parent, false);
			}
			TextView vT_ai_productname = (TextView) convertView.findViewById(R.id.vT_ai_productname);
			TextView vT_ai_manufactname = (TextView) convertView.findViewById(R.id.vT_ai_manufactname);
			TextView vT_ai_quant = (TextView) convertView.findViewById(R.id.vT_ai_quant);
			TextView vT_ai_measurement = (TextView) convertView.findViewById(R.id.vT_ai_measurement);
		
			ImageView inventoryImage = (ImageView) convertView.findViewById(R.id.inventoryImage);
			
			/*vT_ai_productname.setText("Watch");
			vT_ai_manufactname.setText("titan");
			vT_ai_quant.setText("5");
			vT_ai_measurement.setText("abc");
			*/
			
			vT_ai_productname.setText(mInventoryList.get(position).getmProductName());
			vT_ai_manufactname.setText(mInventoryList.get(position).getmManufactureName());
			vT_ai_quant.setText(" "+mInventoryList.get(position).getmTotalStockQuantity());
			vT_ai_measurement.setText(" / "+mInventoryList.get(position).getmUnit());
			imageLoader.displayImage(""+mInventoryList.get(position).getMlogo(), inventoryImage, options);
			
			
			

			return convertView;
		}
	}
	
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.vI_wh_CreateInventory:
			Intent intent=new Intent(InventorylistActivity.this,CreateInventoryActivity.class);
			startActivity(intent);
			
			break;

		default:
			break;
		}
		
	}
}

