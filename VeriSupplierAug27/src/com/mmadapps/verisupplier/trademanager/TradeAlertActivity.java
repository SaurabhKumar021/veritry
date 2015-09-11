package com.mmadapps.verisupplier.trademanager;

import java.util.List;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.AlertGetMessages;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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


public class TradeAlertActivity extends BaseActionBarActivity {
	ListView alertlist;
	AlertAdapter alretadapter;
	TextView seemore;
	//Service Strings
			WebServices webServices;
			String Inparam="";
			private JsonParserClass jsonParserClass;
			
			//Adapter Strings
					private List<AlertGetMessages> mAlertList;
					private ImageLoader imageLoader;
					
	BaseActionBarActivity actionBarActivity;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_trade_alert);
			 actionBarActivity.vUserTitel.setText("Alert Message");
		       	actionBarActivity.vUserTitel.setTextColor(Color.parseColor("#4F4F4F"));
		        actionBarActivity.vUserSubTitle.setVisibility(View.GONE);
		        actionBarActivity.Trademenu.setVisibility(View.VISIBLE);
		        actionBarActivity.Trademenu=(ImageView) findViewById(R.id.vI_ab_trademenu);
		        actionBarActivity.vBackView=(ImageView) findViewById(R.id.vI_ab_backview);
		        actionBarActivity.vBackView.setVisibility(View.GONE);
		        actionBarActivity.qrscan_image=(ImageView) findViewById(R.id.vI_PD_qrscan);
		        actionBarActivity.qrscan_image.setVisibility(View.GONE);
		        
			alertlist=(ListView) findViewById(R.id.vL_ac_alertmessagelist);
			alretadapter=new AlertAdapter();
			

			 new AsyncAlertGetMessages().execute();
		}
		
		private class AlertAdapter extends BaseAdapter
		{

			@Override
			public int getCount() {
				try{
				return mAlertList.size();
				}catch(Exception e){
					Log.e("mAlertList","null");
					return 0;
					
				}
			}

			@Override
			public Object getItem(int arg0) {
				
				return arg0;
			}

			@Override
			public long getItemId(int arg0) {
				
				return arg0;
			}

			@Override
			public View getView(int arg0, View convertView, ViewGroup parent) {
				 
				if (convertView == null) {
					convertView = getLayoutInflater().inflate(
							R.layout.adapter_alert, parent, false);
					//TextView vT_adb_timeText=(TextView) convertView.findViewById(R.id.vT_adb_timeText);
					TextView vT_ad_message=(TextView) convertView.findViewById(R.id.vT_adb_message);
					vT_ad_message.setText(mAlertList.get(arg0).getmAlertMessages());
					
				}
				return convertView;
			}
			
		}
		private Boolean callService() {
	  		// TODO Auto-generated method stub
	      	webServices = new WebServices();
	      	jsonParserClass=new JsonParserClass();
	      	Inparam="/106/1/1000";
	      	String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.AlertGetMessages,"GetMessages", Inparam);
	      	Log.e("abc",resultOutparam);
	  		if(resultOutparam==null || resultOutparam.length()==0){
	  			//isValid=false;
	  		}else{
	  			mAlertList = jsonParserClass.parseAlertGetMessages(resultOutparam);
	  		Log.e("resultchannel",resultOutparam);
	  		}
	  		return true;
	  	}
		private class AsyncAlertGetMessages extends AsyncTask<Void, Void, Boolean>{
			
			private ProgressDialog pdLoading;
			@Override
	  		protected void onPreExecute() {
	  			pdLoading = new ProgressDialog(TradeAlertActivity.this);
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
	  			alertlist.setAdapter(alretadapter);
	  			alertlist.setOnItemClickListener(new OnItemClickListener() {

	  				@Override
	  				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	  						long arg3) {
	  					Intent intent=new Intent(TradeAlertActivity.this,AlertDetailsActivity.class);
	  					startActivity(intent);
	  					
	  				}
	  			});
	  			//initview();
	  			if(result){
	  				//after otp generation go to userOtppopup activity 
	  			
	  			}else{
	  				Toast.makeText(getApplicationContext(), "Please try agin", Toast.LENGTH_SHORT).show();
	  			}
	  		}
		}
	}
