package com.mmadapps.verisupplier.trademanager;


import java.util.List;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.Warehouse;
import com.mmadapps.verisupplier.beans.WarehouseList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class WarehouselistActivity extends BaseActionBarActivity implements OnClickListener {


	SwipeListView vL_warehouseList;
	private int mPosition =0;
	LinearLayout laywarehouse;
	WarehouseAdapter adapter = new WarehouseAdapter();
	String pari[]={"dfh","fgd","sdfha"};
	BaseActionBarActivity actionBarActivity;
	ImageView addwarehouse,deletewarehouse,callwarehouse,msgwarehouse;
	private DisplayImageOptions options;
	
	//Service Strings
		WebServices webServices;
		String Inparam="";
		private JsonParserClass jsonParserClass;
		
		//Adapter Strings
		private List<WarehouseList> mWareHouseList;
		private ImageLoader imageLoader;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouselist);
        actionBarActivity.vUserTitel.setText("Warehouse List");
        actionBarActivity.vUserTitel.setTextColor(Color.parseColor("#4F4F4F"));
        actionBarActivity.vUserSubTitle.setVisibility(View.GONE);
        actionBarActivity.Trademenu.setVisibility(View.VISIBLE);
        actionBarActivity.Trademenu=(ImageView) findViewById(R.id.vI_ab_trademenu);
        actionBarActivity.vBackView=(ImageView) findViewById(R.id.vI_ab_backview);
        actionBarActivity.vBackView.setVisibility(View.GONE);
        actionBarActivity.vBackView.setVisibility(View.GONE);
        actionBarActivity.qrscan_image.setVisibility(View.GONE);
        
        vL_warehouseList = (SwipeListView) findViewById(R.id.vL_wh_warehouseList);
        Log.e("resultchannel","started");
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
        new AsyncWareHouse().execute();
        
        
}
    
private int getYOffSetbyDensityforCal() {
		
		int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_HIGH:
			return 800;
		case DisplayMetrics.DENSITY_XHIGH:
			return 1000;
		case DisplayMetrics.DENSITY_XXHIGH:
			return 1200;
		default:
			return 800;
		}
	}
    
    private void initview() {
    	
    	
    	addwarehouse=(ImageView) findViewById(R.id.vI_wh_CreateOutlet);
        addwarehouse.setOnClickListener(this);
      //  vL_warehouseList = (SwipeListView) findViewById(R.id.vL_wh_warehouseList);
        
        laywarehouse=(LinearLayout) findViewById(R.id.vL_wh_warehouseselected);
        laywarehouse.setVisibility(View.GONE);
        

		
		android.view.ViewGroup.LayoutParams layoutParams = vL_warehouseList.getLayoutParams();
		layoutParams.width = LayoutParams.WRAP_CONTENT;
	//	layoutParams.height = LayoutParams.WRAP_CONTENT;
		//layoutParams.height = getYOffSetbyDensityforCal();
		vL_warehouseList.setLayoutParams(layoutParams);
       
     
        vL_warehouseList.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT); // there are five swiping modes
        vL_warehouseList.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_NONE); //there are four swipe actions 
        vL_warehouseList.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        vL_warehouseList.setOffsetLeft(convertDpToPixel(0f)); // left side offset
        vL_warehouseList.setOffsetRight(convertDpToPixel(0f)); // right side offset
        vL_warehouseList.setAnimationTime(250); // Animation time
        vL_warehouseList.setSwipeOpenOnLongPress(false);
        vL_warehouseList.setAdapter(adapter);
    
        vL_warehouseList.setSwipeListViewListener(new BaseSwipeListViewListener(){
		@Override
		public void onOpened(int position, boolean toRight) {
			super.onOpened(position, toRight);
//			laywarehouse.setVisibility(View.VISIBLE);
			vL_warehouseList.openAnimate(position);
			if(position != mPosition)
				try{
					vL_warehouseList.closeAnimate(mPosition);
				}catch(Exception e){
					e.printStackTrace();
				}
			mPosition = position;
		}
		
		@Override
		public void onClosed(int position, boolean fromRight) {
			super.onClosed(position, fromRight);
			laywarehouse.setVisibility(View.GONE);
		}
		
		@Override
		public void onListChanged() {
			super.onListChanged();
		}
		
		@Override
		public void onMove(int position, float x) {
			super.onMove(position, x);
		}
		
		@Override
		public void onStartOpen(int position, int action, boolean right) {
			super.onStartOpen(position, action, right);
		}
		
		@Override
		public void onStartClose(int position, boolean right) {
			super.onStartClose(position, right);
		}
		
		@Override
		public void onClickFrontView(int position) {
			super.onClickFrontView(position);
			
			

		}
		
		@Override
		public void onClickBackView(int position) {
			super.onClickBackView(position);
		}
	});
        
	}

	public int convertDpToPixel(float dp) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}
    
    
	
	private class WarehouseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			try{
				return mWareHouseList.size();
			}catch(Exception e){
				Log.e("mWareHouseList","null");
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.adapter_warehouselist, parent, false);
			}
			
		TextView vT_nw_warehousenameText=(TextView) convertView.findViewById(R.id.vT_aw_warename);
		TextView vT_aw_locationdetails=(TextView) convertView.findViewById(R.id.vT_aw_locationdetils);
		TextView vT_aw_number = (TextView) convertView.findViewById(R.id.vT_aw_number);
		ImageView warehouse_image = (ImageView) convertView.findViewById(R.id.warehouse_logo);
		
 //deleteing the warehouse
		
		deletewarehouse=(ImageView)convertView.findViewById(R.id.vI_aw_delete);
		deletewarehouse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AsyncDeleteWareHouse().execute();
				
				//Toast.makeText(getApplicationContext(), "warehouse deleted", Toast.LENGTH_LONG).show();
			}
		});
		
		//calling phonenumber
		
		callwarehouse=(ImageView)convertView.findViewById(R.id.vI_aw_call);
		callwarehouse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "warehouse calling" +mWareHouseList.get(position).getmWareHousePhoneNo(), Toast.LENGTH_LONG).show();
				Intent callIntent = new Intent(Intent.ACTION_CALL);  
             callIntent.setData(Uri.parse("tel:"+mWareHouseList.get(position).getmWareHousePhoneNo())); 
                startActivity(callIntent);  
			}
		});
		
		//sending the sms
		
		 msgwarehouse=(ImageView)convertView.findViewById(R.id.vI_aw_message);
		 msgwarehouse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent=new Intent(getApplicationContext(),WarehouselistActivity.class);  
//				PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);  
//				  
//				//Get the SmsManager instance and call the sendTextMessage method to send message                 
//				SmsManager sms=SmsManager.getDefault();  
//				sms.sendTextMessage("9972777059", null, "hello javatpoint", pi,null); 
				
				String phonenum=mWareHouseList.get(position).getmWareHousePhoneNo();
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(phonenum, null, "sms message", null, null);
				
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("address", phonenum);
				sendIntent.putExtra("sms_body", "default content"); 
				sendIntent.setType("vnd.android-dir/mms-sms");
				
				//sendIntent.setData(Uri.parse("tel:"+mWareHouseList.get(position).getmWareHousePhoneNo()));
				startActivity(sendIntent);
			}
		});
			vT_nw_warehousenameText.setText(mWareHouseList.get(position).getmWareHouseName());
			vT_aw_locationdetails.setText(mWareHouseList.get(position).getmWareHouseAddress());
			vT_aw_number.setText(mWareHouseList.get(position).getmWareHousePhoneNo());
			imageLoader.displayImage(""+mWareHouseList.get(position).getmWareHousePictureURL(), warehouse_image, options);
			return convertView;
		
		
		}
	}

    
    
    private Boolean callService() {
  		// TODO Auto-generated method stub
      	webServices = new WebServices();
      	jsonParserClass=new JsonParserClass();
      	Inparam="/106/1/10";
      	String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetWarehouseList,"GetWarehouseList", Inparam);
  		if(resultOutparam==null || resultOutparam.length()==0){
  			//isValid=false;
  			Log.e("resultchannel===>",resultOutparam);
  		}else{
  			mWareHouseList = jsonParserClass.parseWarehouseList(resultOutparam);
  			Log.e("size of list======>", "size is"+mWareHouseList.size());
  		Log.e("resultchannel",resultOutparam);
  		}
  		return true;
  	}
    private Boolean callDeleteService() {
	  		// TODO Auto-generated method stub
	      	webServices = new WebServices();
	      	jsonParserClass=new JsonParserClass();
	      	Inparam="/"+mWareHouseList.get(mPosition).getmWareHouseId()+"/2";
	      	String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.DeleteWarehouse,"DeleteWareHouse", Inparam);
	      	String result_delete;
	  		if(resultOutparam==null || resultOutparam.length()==0){
	  			//isValid=false;
	  		}else{
	  			result_delete = jsonParserClass.parseDeleteWareHouseResult(resultOutparam);
	  		Log.e("resultDeletewarehouse",result_delete);
	  		}
			return true;
	  	}
	  

  private class AsyncWareHouse extends AsyncTask<Void, Void, Boolean>{
  		
  		private ProgressDialog pdLoading;

  		@Override
  		protected void onPreExecute() {
  			pdLoading = new ProgressDialog(WarehouselistActivity.this);
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
  			if(pdLoading.isShowing())
  				pdLoading.dismiss();
  			initview();
  			if(result){
  				//after otp generation go to userOtppopup activity 
  			
  			}else{
  				Toast.makeText(getApplicationContext(), "Please try agin", Toast.LENGTH_SHORT).show();
  			}
  		}
  }
  		
  	  public class AsyncDeleteWareHouse extends AsyncTask<Void, Void, Boolean>{
  			
  			private ProgressDialog pdLoading;

  			@Override
  			protected void onPreExecute() {
  				pdLoading = new ProgressDialog(WarehouselistActivity.this);
  				pdLoading.setMessage("please wait...");
  				pdLoading.show();
  				pdLoading.setCancelable(false);
  				pdLoading.setCanceledOnTouchOutside(false);
  				super.onPreExecute();
  			}

  			@Override
  			protected Boolean doInBackground(Void... params) {
  				return callDeleteService();
  			}
  			
  			@Override
  			protected void onPostExecute(Boolean result) {
  				super.onPostExecute(result);
  				pdLoading.cancel();
  				if(pdLoading.isShowing())
  					pdLoading.dismiss();
  				initview();
  				if(result){
  					new AsyncWareHouse().execute();
  					Toast.makeText(getApplicationContext(), "warehouse deleted", Toast.LENGTH_LONG).show();

  					//after otp generation go to userOtppopup activity 
  				
  				}else{
  					Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
  				}
  			}
  	  }


  @Override
	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.vI_wh_CreateOutlet:
		Intent intent=new Intent(WarehouselistActivity.this,CreateWareHouseActivity.class);
		startActivity(intent);
		
		break;
//	case R.id.vI_aw_delete:
//		Toast.makeText(getApplicationContext(), "warehose deleted", Toast.LENGTH_LONG).show();
//		break;


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
