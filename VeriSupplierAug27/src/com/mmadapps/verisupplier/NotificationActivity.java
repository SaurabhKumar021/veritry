package com.mmadapps.verisupplier;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class NotificationActivity extends BaseActionBarActivity {
	
	//notification listview
	ListView notificationlist;
	
	//notification adapter
	NotificationAdapter notifyadapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);		
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Notification");
		initView();
	}

	private void initView() {
		notificationlist = (ListView) findViewById(R.id.vL_PD_notificationlist);
		notifyadapter = new NotificationAdapter();
		notificationlist.setAdapter(notifyadapter);
	}
	
	class NotificationAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=getLayoutInflater().inflate(R.layout.adapter_notification, parent, false);
			}
			
			@SuppressWarnings("unused")
			ImageView product_image=(ImageView)convertView.findViewById(R.id.vI_nv_product_image);
			
			return convertView;
		}
		
	}

}
