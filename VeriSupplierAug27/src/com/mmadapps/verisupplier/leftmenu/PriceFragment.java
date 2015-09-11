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
import com.mmadapps.verisupplier.beans.QuoatationDashboard;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PriceFragment extends Fragment {

	LinearLayout vL_QF_titleLayout;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	private ListView priceListView;
	PriceAdapter mPriceAdapter;
	
	WebServices webServices;
	JsonParserClass jsonParserClass;
	List<QuoatationDashboard> QuotationList=new ArrayList<QuoatationDashboard>();
	List<QuoatationDashboard> mpriceDashboardList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.quotation_fragment, container, false);
		getPriceList();
		initControl(view);
		return view;
	}
	
	private void getPriceList() {
		Helper helper=new Helper(getActivity().getApplicationContext());
		helper.openDataBase();
		QuotationList=helper.getDashboardQuotation();
		helper.close();
	}

	private void initControl(View view) {
		vL_QF_titleLayout = (LinearLayout) view.findViewById(R.id.vL_QF_titleLayout);
		vL_QF_titleLayout.setVisibility(View.GONE);
		
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
		
		priceListView=(ListView)view.findViewById(R.id.fp_message_list1);
		priceListView.setVisibility(View.VISIBLE);
		mPriceAdapter = new PriceAdapter();
		priceListView.setAdapter(mPriceAdapter);
		
		mpriceDashboardList=new ArrayList<QuoatationDashboard>();
		for (int i = 0; i < QuotationList.size(); i++) {
			if(QuotationList.get(i).getmQuotationStatusId().equalsIgnoreCase("6")){
				mpriceDashboardList.add(QuotationList.get(i));
			}else{
			}
		}
		
	}
	
	private class PriceAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(mpriceDashboardList==null || mpriceDashboardList.size()==0){
				return 0;
			}else{
				return mpriceDashboardList.size();
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
		
			
			vT_AD_qtnLable.setText("QuotationNumber:");
			vT_AD_qtnValue.setText(""+mpriceDashboardList.get(position).getmQuotation_Number());
			vT_AD_dateTime.setText("today");
			vT_AD_productNameValue.setText(""+mpriceDashboardList.get(position).getQuotation_productname());
			vT_AD_mfrNameValue.setText(""+mpriceDashboardList.get(position).getQuotation_manufacturename());
			vT_AD_dateTime.setText(""+mpriceDashboardList.get(position).getmQuotation_Datetime());
			
			if(mpriceDashboardList.get(position).getmQuotationStatusId().equalsIgnoreCase("6")){
				quotationstatus.setText("Requested Latest Price");
				quotationstatus.setTextColor(getResources().getColor(R.color.orange));
			}else if(mpriceDashboardList.get(position).getmQuotationStatusId().equalsIgnoreCase("7")){
				quotationstatus.setText("Latest Price Sent");
			}else{
				
			}
				
			
			
			
			return convertView;
		}
		
	}
}
