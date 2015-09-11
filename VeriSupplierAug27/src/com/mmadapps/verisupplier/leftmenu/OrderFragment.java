package com.mmadapps.verisupplier.leftmenu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.beans.OrderDashboard;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class OrderFragment extends Fragment {
	
	LinearLayout vL_QF_titleLayout;
	private ListView orderListView;
	OrderAdapter mOrderAdapter;
	
	
	ImageLoader imageLoader;
	DisplayImageOptions options;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	List<OrderDashboard> mOrderDashboardList=new ArrayList<OrderDashboard>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.quotation_fragment, container, false);
		getOderList();
		initControl(view);
		return view;
	}


	private void initControl(View view) {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		try {
			options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
			.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
			.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		vL_QF_titleLayout = (LinearLayout) view.findViewById(R.id.vL_QF_titleLayout);
		vL_QF_titleLayout.setVisibility(View.GONE);
		orderListView=(ListView)view.findViewById(R.id.fp_message_list1);
		orderListView.setVisibility(View.VISIBLE);
		mOrderAdapter = new OrderAdapter();
		orderListView.setAdapter(mOrderAdapter);
	}


	private void getOderList() {
		Helper helper=new Helper(getActivity().getApplicationContext());
		helper.openDataBase();
		mOrderDashboardList=helper.getDashboardOrder();
		helper.close();
	}
	
	private class OrderAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(mOrderDashboardList==null || mOrderDashboardList.size()==0){
				return 0;
			}else{
				return mOrderDashboardList.size();
			}
			
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
			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(convertView==null){
				convertView = inflater.inflate(R.layout.adapter_dashboard, parent, false);
			}
			TextView vT_AD_qtnLable = (TextView) convertView.findViewById(R.id.vT_AD_qtnLable);
			TextView vT_AD_qtnValue = (TextView) convertView.findViewById(R.id.vT_AD_qtnValue);
			TextView vT_AD_dateTime = (TextView) convertView.findViewById(R.id.vT_AD_dateTime);
			TextView vT_AD_productNameValue = (TextView) convertView.findViewById(R.id.vT_AD_productNameValue);
			TextView vT_AD_mfrNameValue = (TextView) convertView.findViewById(R.id.vT_AD_mfrNameValue);
			TextView vT_AD_productNameLable = (TextView) convertView.findViewById(R.id.vT_AD_productNameLable);
			TextView vT_AD_mfrNameLable = (TextView) convertView.findViewById(R.id.vT_AD_mfrNameLable);
			TextView quotationstatus=(TextView)convertView.findViewById(R.id.vT_AD_quotationstatus);
			quotationstatus.setVisibility(View.GONE);
			vT_AD_productNameLable.setVisibility(View.GONE);
			vT_AD_mfrNameLable.setVisibility(View.GONE);
			
			vT_AD_qtnLable.setText("OrderNumber:");
			vT_AD_qtnValue.setText(""+mOrderDashboardList.get(position).getmOrder_Number());
			vT_AD_dateTime.setText("today");
			vT_AD_productNameValue.setText(""+mOrderDashboardList.get(position).getmOrder_Productname());
			vT_AD_productNameValue.setTextColor(getResources().getColor(R.color.green_new));
			vT_AD_productNameValue.setTextSize(15);
			vT_AD_mfrNameValue.setText(""+mOrderDashboardList.get(position).getmOrder_Manufacturname());
			vT_AD_dateTime.setText(""+mOrderDashboardList.get(position).getmOrder_Date());
			
					
			return convertView;
		}
		
	}

}
