package com.mmadapps.verisupplier.trademanager;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.graphics.Bitmap;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.Channel;
import com.mmadapps.verisupplier.trademanager.WarehouselistActivity.AsyncDeleteWareHouse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ChannellistActivity extends BaseActionBarActivity implements OnClickListener {
	SwipeListView vL_channelList;
	private int mPosition =0;
	LinearLayout laychannel;
	ChannellistAdapter adapter = new ChannellistAdapter();
	String pari[]={"dfh","fgd","sdfha"};
	BaseActionBarActivity actionBarActivity;
	ImageView addchannel,callchannel,msgchannel,deletechannel;
	private DisplayImageOptions options;
	
	//Service Strings
		WebServices webServices;
		String Inparam="";
		private JsonParserClass jsonParserClass;
		
		//Adapter Strings
		private List<Channel> mChannelList;
		private ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channellist);
        BaseActionBarActivity.vUserTitel.setText("Channel List");
        BaseActionBarActivity.vUserTitel.setTextColor(Color.parseColor("#4F4F4F"));
        BaseActionBarActivity.vUserSubTitle.setVisibility(View.GONE);
        BaseActionBarActivity.Trademenu.setVisibility(View.VISIBLE);
       // actionBarActivity.vtradeBack.setVisibility(View.GONE);
        BaseActionBarActivity.vBackView.setVisibility(View.GONE);
        BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
       
        
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
        new AsyncCallChannel().execute();  
      
}

    private void initview() {
    	
    	addchannel=(ImageView) findViewById(R.id.vI_cl_Createchannel);
        addchannel.setOnClickListener(this);
        
              vL_channelList = (SwipeListView) findViewById(R.id.vL_cl_OutletList);
              laychannel=(LinearLayout) findViewById(R.id.vL_cl_channelselected);
              laychannel.setVisibility(View.GONE);
             
              
//            android.view.ViewGroup.LayoutParams layoutParams = vL_channelList.getLayoutParams();
//      		layoutParams.width = LayoutParams.WRAP_CONTENT;
//      		layoutParams.height = getYOffSetbyDensityforCal();
//      		vL_channelList.setLayoutParams(layoutParams);
      		
              vL_channelList.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT); // there are five swiping modes
              vL_channelList.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_NONE); //there are four swipe actions 
              vL_channelList.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
              vL_channelList.setOffsetLeft(convertDpToPixel(0f)); // left side offset
              vL_channelList.setOffsetRight(convertDpToPixel(0f)); // right side offset
              vL_channelList.setAnimationTime(250); // Animation time
              vL_channelList.setSwipeOpenOnLongPress(false);
              vL_channelList.setAdapter(adapter);
          
              
              vL_channelList.setSwipeListViewListener(new BaseSwipeListViewListener(){
      		@Override
      		public void onOpened(int position, boolean toRight) {
      			super.onOpened(position, toRight);
//      			laychannel.setVisibility(View.VISIBLE);
      			Inparam=mChannelList.get(position).getmChannelId();
      			vL_channelList.openAnimate(position);
      			if(position != mPosition)
      				try{
      					vL_channelList.closeAnimate(mPosition);
      				}catch(Exception e){
      					e.printStackTrace();
      				}
      			mPosition = position;
      		}
      		
      		@Override
      		public void onClosed(int position, boolean fromRight) {
      			super.onClosed(position, fromRight);
      			laychannel.setVisibility(View.GONE);
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
 
 @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vI_cl_Createchannel:
			Intent intent=new Intent(ChannellistActivity.this,CreateChannelActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
 
    
    public int convertDpToPixel(float dp) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}
    
private int getYOffSetbyDensityforCal() {
		
		int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_HIGH:
			return 300;
		case DisplayMetrics.DENSITY_XHIGH:
			return 500;
		case DisplayMetrics.DENSITY_XXHIGH:
			return 700;
		default:
			return 300;
		}
	}
	
private class ChannellistAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		try{
			return mChannelList.size();
		} catch(Exception e){
			Log.e("mChannelList","null");
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
					R.layout.adapter_channel, parent, false);
		}
		
		TextView vT_channel_name = (TextView) convertView.findViewById(R.id.vT_channel_name);
		ImageView manufacture_image = (ImageView) convertView.findViewById(R.id.channel_logo);
		
		vT_channel_name.setText(mChannelList.get(position).getmChannelName());
		imageLoader.displayImage(""+mChannelList.get(position).getmPictureURL(), manufacture_image, options);
		//deleting the channel
		deletechannel=(ImageView)convertView.findViewById(R.id.vI_ad_delete);
		deletechannel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AsyncDeleteChannel().execute();
				
				//Toast.makeText(getApplicationContext(), "warehouse deleted", Toast.LENGTH_LONG).show();
			}
		});
		
		
// calling the channel
		callchannel=(ImageView)convertView.findViewById(R.id.vI_ad_call);
		callchannel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "warehouse calling" +mWareHouseList.get(position).getmWareHousePhoneNo(), Toast.LENGTH_LONG).show();
				Intent callIntent = new Intent(Intent.ACTION_CALL);  
             callIntent.setData(Uri.parse("tel:"+mChannelList.get(position).getmPhoneNo())); 
                startActivity(callIntent);  
			}
		});
	
		 msgchannel=(ImageView)convertView.findViewById(R.id.vI_ad_message);
		 msgchannel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String phonenum=mChannelList.get(position).getmPhoneNo();
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(phonenum, null, "sms message", null, null);
				
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("address", phonenum);
				sendIntent.putExtra("sms_body", "default content"); 
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivity(sendIntent);
			}
		});
		return convertView;
   
	 }
	
}


private class AsyncCallChannel extends AsyncTask<Void, Void, Boolean>{
		
		private ProgressDialog pdLoading;

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(ChannellistActivity.this);
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


private Boolean callService() {
	// TODO Auto-generated method stub
	webServices = new WebServices();
	jsonParserClass=new JsonParserClass();
	String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetChannel,"GetChannel","");
	if(resultOutparam==null || resultOutparam.length()==0){
		//isValid=false;
	}else{
		mChannelList = jsonParserClass.parseChannelList(resultOutparam);
	Log.e("resultchannel",resultOutparam);
	}
	return true;
}
private class AsyncDeleteChannel extends AsyncTask<Void, Void, Boolean>{
	
	private ProgressDialog pdLoading;

	@Override
	protected void onPreExecute() {
		pdLoading = new ProgressDialog(ChannellistActivity.this);
		pdLoading.setMessage("please wait...");
		pdLoading.show();
		pdLoading.setCancelable(false);
		pdLoading.setCanceledOnTouchOutside(false);
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		return deleteService();
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
			new AsyncCallChannel().execute(); 
		
		}else{
			Toast.makeText(getApplicationContext(), "Please try agin", Toast.LENGTH_SHORT).show();
		}
	}
} 	
private Boolean deleteService() {
	// TODO Auto-generated method stub
	webServices = new WebServices();
	jsonParserClass=new JsonParserClass();
	String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.DeleteChannel,"DeleteChannelByChannelId/", Inparam);
	if(resultOutparam==null || resultOutparam.length()==0){
		//isValid=false;
	}else{
		mChannelList = jsonParserClass.parseChannelList(resultOutparam);
	Log.e("resultchannel",resultOutparam);
	}
	return true;
}

@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	finish();
}

}
