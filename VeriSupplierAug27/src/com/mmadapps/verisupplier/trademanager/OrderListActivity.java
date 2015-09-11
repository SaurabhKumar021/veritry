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
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.Order;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class OrderListActivity extends BaseActionBarActivity {

	ListView productlist;
	BaseActionBarActivity actionBarActivity;
	InventorylistAdapter adapter = new InventorylistAdapter();

	private DisplayImageOptions options;

	// User String
	UserDetails mUserDetails = new UserDetails();

	// Service Strings
	WebServices webServices;
	String Inparam = "";
	private JsonParserClass jsonParserClass;

	// Adapter Strings
	private List<Order> mOrderList;
	private ImageLoader imageLoader;
	private String customerId = "2";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		BaseActionBarActivity.vUserTitel.setText("Order List");
		BaseActionBarActivity.vUserTitel.setTextColor(Color
				.parseColor("#4F4F4F"));
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
		getUserDetails();
		new AsyncOrderList().execute();
		//PaymentGateway pg = new PaymentGateway(getApplicationContext());
	}

	
	
	private void InitViews() {
		// TODO Auto-generated method stub

		productlist = (ListView) findViewById(R.id.vL_ol_productlist);
		productlist.setAdapter(adapter);
		productlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(OrderListActivity.this,
						OrderActivity.class);
				intent.putExtra("ChannelName", mOrderList.get(position).getmChannelName());
				intent.putExtra("ChannelLogo", mOrderList.get(position).getmLogo());
				intent.putExtra("ProductName", mOrderList.get(position).getmProductName());
				intent.putExtra("ProductLogo", mOrderList.get(position).getmProductLogo());
				intent.putExtra("ProductId", mOrderList.get(position).getmProductId());
				intent.putExtra("Quantity", mOrderList.get(position).getmQuantity());
				//intent.putExtra("Unit", mOrderList.get(position).g);
				intent.putExtra("OrderId", mOrderList.get(position).getmOrderId());
				intent.putExtra("OrderDate", mOrderList.get(position).getmDate());
				//intent.putExtra("ProductManufactureName", mOrderList.get(position).g)
				startActivity(intent);

			}
		});
	}

	private class InventorylistAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			try {
				return mOrderList.size();
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
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.adapter_orderlist, parent, false);
			}
			TextView status = (TextView) convertView
					.findViewById(R.id.vT_oa_status);
			TextView OrderName = (TextView) convertView
					.findViewById(R.id.vT_oa_channelnanme);
			TextView productName = (TextView) convertView
					.findViewById(R.id.vT_oa_productnanme);
			TextView quantity = (TextView) convertView
					.findViewById(R.id.vT_oa_quantity);
			TextView orderNumber = (TextView) convertView
					.findViewById(R.id.vT_oa_orderNumber);
			TextView orderDate = (TextView) convertView
					.findViewById(R.id.vT_oa_date);

			ImageView order_image = (ImageView) convertView
					.findViewById(R.id.vI_oa_productimage);

			// status.setTextColor(Color.rgb(76, 168, 176));
			// status.setTextColor(Color.parseColor("#E1565A"));

			OrderName.setText(mOrderList.get(position).getmChannelName());
			productName.setText(mOrderList.get(position).getmProductName());
			quantity.setText(mOrderList.get(position).getmQuantity());
			orderNumber.setText(mOrderList.get(position).getmOrderId());
			orderDate.setText(mOrderList.get(position).getmDate());
			//status.setText(mOrderList.get(position).getmStatus());
			if (mOrderList.get(position).getmStatus().equals("1")) {
				status.setText("BLOCKED");
				status.setTextColor(Color.parseColor("#2F2F2F"));
			} else if (mOrderList.get(position).getmStatus().equals("2")) {
				status.setText("FULL FILLED");
				status.setTextColor(Color.parseColor("#3D7D86"));
			} else if (mOrderList.get(position).getmStatus().equals("3")) {
				status.setText("CANCELLED");
				status.setTextColor(Color.parseColor("#E1565A"));
			} else {
				productName.setTextColor(Color.parseColor("#E1565A"));
			}

			imageLoader.displayImage("" + mOrderList.get(position).getmLogo(),
					order_image, options);

			return convertView;
		}

	}

	private class AsyncOrderList extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog pdLoading;

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(OrderListActivity.this);
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

	private Boolean callService() {
		// TODO Auto-generated method stub
		webServices = new WebServices();
		jsonParserClass = new JsonParserClass();
		customerId = mUserDetails.getmUserID();
		Inparam = "/" + customerId+"/1/1000";
		String resultOutparam = webServices.CallWebHTTPBindingService(
				ApiType.GettradeOrder, "GetChannelOrder", Inparam);
		if (resultOutparam == null || resultOutparam.length() == 0) {
			// isValid=false;
		} else {
			mOrderList = jsonParserClass.parseOrderList(resultOutparam);
			//Log.e("resultchannel", resultOutparam);
		}
		return true;
	}

	private void getUserDetails() {
		Helper helper = new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails = helper.getUserDetails();
		helper.close();
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}