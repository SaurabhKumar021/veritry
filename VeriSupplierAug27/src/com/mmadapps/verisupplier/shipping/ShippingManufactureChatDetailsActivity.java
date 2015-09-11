package com.mmadapps.verisupplier.shipping;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.QuotationDashboardHistory;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.TwoWayView;
import com.mmadapps.verisupplier.fileattachment.FileExplorer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ShippingManufactureChatDetailsActivity extends BaseActionBarActivity {
	
	//Common
	ImageLoader imageLoader;
	DisplayImageOptions options;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	private UserDetails mUserDetails;
	
	ImageView vI_SDM_createQuotation;
	
	//Buyerdetails
	private ImageView vI_SDM_productImage;
	private ListView vI_SDM_messageLists;
	private TextView vT_SDM_productName;

	EditText message,quantity;
	
	//Arraylist
	static ArrayList<String> arrayList = new ArrayList<String>();
	ArrayList<String> document_url_list;
	ArrayList<QuotationDashboardHistory> shippingHistoryList;
	
	//Strings
	String quoatation_id;
	String mUserSubject;
	String mUserMessage;
	String mResponseUserType="2";
	String mSelectedQuantity;
	String mSelectedQuantityId;
	String mQuantity;
	String mQuotationStatusId="1";
	List<Lookups> mUnitOfMeasurementList=new ArrayList<Lookups>();
	String mOriginalQuantityUnit;
	int mOriginalQuantityUnitId;
	String mBestPrice;
	
	
	//Attachment
	private int REQUEST_GET_SINGLE_FILE = 111;
	private GetQuortationAdapter mAdapter;
	ListView attach_list;
	
	
	private static Typeface segeo_wp;
	private static Typeface segeo_wp_bold;
	
	
	public static Typeface getSegeo_wp() {
		return segeo_wp;
	}

	public static void setSegeo_wp(Typeface segeo_wp) {
		ShippingManufactureChatDetailsActivity.segeo_wp = segeo_wp;
	}

	public static Typeface getSegeo_wp_bold() {
		return segeo_wp_bold;
	}

	public static void setSegeo_wp_bold(Typeface segeo_wp_bold) {
		ShippingManufactureChatDetailsActivity.segeo_wp_bold = segeo_wp_bold;
	}
	
	String additionalinfo;
	
	String mAttachment="";
	TwoWayView document_list;
	DocumentAdapter documentadapter;
	static ArrayList<String> attachList = new ArrayList<String>();
	String attachimageUrl="";
	String mAttachmentList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shippingquotation_buyerdetails);
		
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.vBuyerImage.setVisibility(View.VISIBLE);
		
		
		initControl();
		getHistoryFromDbAndsetAdapter();
		
	}

	private void initControl() {
		
		vI_SDM_productImage = (ImageView) findViewById(R.id.vI_ashbd_productImage);
		vI_SDM_messageLists = (ListView) findViewById(R.id.vI_ashbd_messageLists);
		vT_SDM_productName = (TextView) findViewById(R.id.vT_ashbd_productName);
		vI_SDM_createQuotation = (ImageView) findViewById(R.id.vI_ashbd_createQuotation);
		
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(ShippingManufactureChatDetailsActivity.this));
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
		
		jsonParserClass = new JsonParserClass();
		webServices = new WebServices();

		Helper helper = new Helper(ShippingManufactureChatDetailsActivity.this);
		helper.openDataBase();
		mUserDetails = helper.getUserDetails();
		helper.close();
		
		vI_SDM_createQuotation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				arrayList = new ArrayList<String>();
				attachList=new  ArrayList<String>();
				createPopupDialog();
				
				/*Intent intent = new Intent(ShippingDetailsManufactureActivity.this,ShippingUpdateQuotationBuyer.class);
				startActivity(intent);*/
			}
		});
		
	}
	
	private void getHistoryFromDbAndsetAdapter(){
		Helper helper = new Helper(ShippingManufactureChatDetailsActivity.this);
		helper.openDataBase();
		shippingHistoryList = helper.getAllDashboardQuotationHistory(null);
		mUnitOfMeasurementList=helper.getUnitOfMeasurentList();
		helper.close();
		
		if(shippingHistoryList == null || shippingHistoryList.size() == 0){
			
		}else{
			ShipingHistoryAdapter adapter1 = new ShipingHistoryAdapter(shippingHistoryList);
			quoatation_id= shippingHistoryList.get(0).getHistory_id();
			String contactPersonPicUrl = shippingHistoryList.get(0).getHistory_Buyerimage();
			imageLoader.displayImage(contactPersonPicUrl, BaseActionBarActivity.vBuyerImage, options);
			
			BaseActionBarActivity.setmUserSubTitle(""+shippingHistoryList.get(0).getHistory_Manufacturename());
			BaseActionBarActivity.setmUserName(""+shippingHistoryList.get(0).getHistory_productname());
			
			vT_SDM_productName.setText(""+shippingHistoryList.get(0).getHistory_productname());
			imageLoader.displayImage(shippingHistoryList.get(0).getHistory_productimage(), vI_SDM_productImage, options);
			
			vI_SDM_messageLists.setAdapter(adapter1);
		}
}
	
	
	private class ShipingHistoryAdapter extends BaseAdapter { 
		private ArrayList<QuotationDashboardHistory> list;
		private LinearLayout.LayoutParams lp;
	
		public ShipingHistoryAdapter(ArrayList<QuotationDashboardHistory> arrayList) {
			this.list = arrayList;
			this.lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.setMargins(5, 0, 5, 0);
		}
	
		@Override
		public int getCount() {
			if(list==null || list.size()==0){
				return 0;
			}else{
				return list.size();
			}
		}

		@Override
		public QuotationDashboardHistory getItem(int position) {
			return list.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.adapter_shippingquotation_sellerchat, parent, false);
			}
			
			LinearLayout vL_SDM_manufactureOptions = (LinearLayout) convertView.findViewById(R.id.vL_ashsc_additionalinfo_layout);
			LinearLayout vL_SDM_rootLayout = (LinearLayout) convertView.findViewById(R.id.vL_ashsc_rootLayout);
			LinearLayout fromLayout = (LinearLayout) convertView.findViewById(R.id.vL_ashsc_fromLayout);
			LinearLayout ToLayout = (LinearLayout) convertView.findViewById(R.id.vT_ashsc_ToLayout);
			LinearLayout vL_ashbc_bestprice_layout =  (LinearLayout) convertView.findViewById(R.id.vL_ashsc_bestprice_layout);
			
			TextView vT_SDM_dateTimeText = (TextView) convertView.findViewById(R.id.vT_ashsc_dateTimeText);
			TextView vT_ashbc_subject =(TextView) convertView.findViewById(R.id.vT_ashsc_subject);
			TextView vT_ashbc_servicetype = (TextView) convertView.findViewById(R.id.vT_ashsc_servicetype);
			TextView vT_ashbc_message = (TextView) convertView.findViewById(R.id.vT_ashsc_message);
			TextView vT_ashbc_fromcity = (TextView) convertView.findViewById(R.id.vT_ashsc_fromcity);
			TextView vT_ashbc_fromcountry = (TextView) convertView.findViewById(R.id.vT_ashsc_fromcountry);
			TextView vT_ashbc_tocity = (TextView) convertView.findViewById(R.id.vT_ashsc_tocity);
			TextView vT_ashbc_tocountry = (TextView) convertView.findViewById(R.id.vT_ashsc_tocountry);
			TextView vT_ashbc_boxSize = (TextView) convertView.findViewById(R.id.vT_ashsc_boxSize);
			TextView vT_ashbc_packages = (TextView) convertView.findViewById(R.id.vT_ashsc_packages);
			TextView vT_ashbc_weight = (TextView) convertView.findViewById(R.id.vT_ashsc_weight);
			TextView vT_ashbc_shipdate = (TextView) convertView.findViewById(R.id.vT_ashsc_shipdate);
			TextView vT_ashbc_shiptype = (TextView) convertView.findViewById(R.id.vT_ashsc_shiptype);
			LinearLayout document_layout=(LinearLayout)convertView.findViewById(R.id.vL_ashsc_documentLayout);
			document_list = (TwoWayView) convertView.findViewById(R.id.vL_ashsc_document_list);
			
			TextView vT_ashbc_bestprice = (TextView) convertView.findViewById(R.id.vT_ashsc_bestprice);
			TextView price_unit=(TextView)convertView.findViewById(R.id.vT_ashsc_bestprice_unit);
			TextView vT_ashbc_acceptButton = (TextView) convertView.findViewById(R.id.vT_ashsc_acceptButton);
			TextView vT_ashbc_rejectButton = (TextView) convertView.findViewById(R.id.vT_ashsc_rejectButton);
			View attachment_view=(View)convertView.findViewById(R.id.view_ashsc_attachment_view);
			View fromto_view=(View)convertView.findViewById(R.id.view_ashsc_fromto_view);
			View additionalinfo_view=(View)convertView.findViewById(R.id.view_ashsc_additionalinfo_view);
			
			
			QuotationDashboardHistory history = getItem(position);
			
			String msgType = history.getChat_ResponseUserType();
			String dateTime = list.get(position).getChat_ResponseDate().split(" ")[1]+" "+list.get(position).getChat_ResponseDate().split(" ")[2];
			vT_SDM_dateTimeText.setText(dateTime);
			vT_ashbc_subject.setText(shippingHistoryList.get(position).getHistory_subject());
			vT_ashbc_servicetype.setText("Shipping");
			vT_ashbc_message.setText(list.get(position).getChat_message());
			vT_ashbc_fromcity.setText(list.get(position).getmShipping_FromCity()+",");
			vT_ashbc_fromcountry.setText(list.get(position).getmShipping_FromCountry());
			vT_ashbc_tocity.setText(list.get(position).getmShipping_ToCity()+",");
			vT_ashbc_tocountry.setText(list.get(position).getmShipping_ToCountry());
			vT_ashbc_boxSize.setText(""+list.get(position).getmLength()+"x"+list.get(position).getmWidth()+"x"+list.get(position).getmHeight());
			vT_ashbc_packages.setText(list.get(position).getmTotalNumberOfCatons());
			vT_ashbc_weight.setText(list.get(position).getmTotalWeight());
			vT_ashbc_shipdate.setText(list.get(position).getmShipping_DeliveryDate());
			vT_ashbc_bestprice.setText(shippingHistoryList.get(position).getOffered_price());
			price_unit.setText("/"+shippingHistoryList.get(position).getChat_UnitsOfMeasurement());
			
			if(shippingHistoryList.get(position).getmShipping_pickup()==null || shippingHistoryList.get(position).getmShipping_pickup().equalsIgnoreCase("null")){
			}else{
				if(shippingHistoryList.get(position).getmShipping_pickup().equalsIgnoreCase("true")){
					vT_ashbc_shiptype.setText("Pickup");
				}else{
					vT_ashbc_shiptype.setText("DropOff");
				}
			}
			
			mAttachment=shippingHistoryList.get(position).getmAttachment();
			if(mAttachment==null || mAttachment.length()==0){
				document_layout.setVisibility(View.GONE);
				attachment_view.setVisibility(View.GONE);
			}else{
				document_layout.setVisibility(View.VISIBLE);
				attachment_view.setVisibility(View.VISIBLE);
				document_url_list = new ArrayList<String>();
				if(mAttachment.contains(",")){
					StringTokenizer st = new StringTokenizer(mAttachment,",");  
					while (st.hasMoreTokens()) { 
						document_url_list.add(st.nextToken()); 	 
					}  
				}else{
					document_url_list.add(mAttachment);
				}
				
				if(document_url_list == null || document_url_list.size() == 0){
					   
				}else{
					documentadapter=new DocumentAdapter(document_url_list);
					document_list.setAdapter(documentadapter);   
				}
			}
			
            if(msgType == null || msgType.length() == 0){
				
			}else if(msgType.equals("1")){
				vT_ashbc_acceptButton.setVisibility(View.GONE);
				vT_ashbc_rejectButton.setVisibility(View.GONE);
				vL_SDM_manufactureOptions.setVisibility(View.VISIBLE);
				fromLayout.setVisibility(View.VISIBLE);
				ToLayout.setVisibility(View.VISIBLE);
				vL_ashbc_bestprice_layout.setVisibility(View.GONE);
				
				additionalinfo_view.setVisibility(View.GONE);
			}else{
				
				vL_SDM_manufactureOptions.setVisibility(View.GONE);
				vT_ashbc_acceptButton.setVisibility(View.GONE);
				vT_ashbc_rejectButton.setVisibility(View.GONE);
				fromLayout.setVisibility(View.GONE);
				ToLayout.setVisibility(View.GONE);
				vL_ashbc_bestprice_layout.setVisibility(View.VISIBLE);
				
				fromto_view.setVisibility(View.GONE);
				additionalinfo_view.setVisibility(View.GONE);
				
				
				vL_SDM_rootLayout.setBackground(getResources().getDrawable(R.drawable.chat_mfr_blue));
				vT_SDM_dateTimeText.setTextColor(getResources().getColor(R.color.datetime_blue));
				
			}
			
			return convertView;		
		}
	}
	
	protected void createPopupDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ShippingManufactureChatDetailsActivity.this);
		final View view = getLayoutInflater().inflate(R.layout.activity_quotation_seller_reply, null);
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
		dialog.show();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
				
		if(view == null){
			if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
			}
		}
		LinearLayout vL_AQR_milestoneLayout = (LinearLayout) view.findViewById(R.id.vL_aqsr_milestoneLayout);
		LinearLayout WholeLayout = (LinearLayout) view.findViewById(R.id.vL_aqsr_WholeLayout);
		ImageView vI_AQR_attachButton = (ImageView) view.findViewById(R.id.vI_aqsr_attachButton);
		message = (EditText) view.findViewById(R.id.vE_aqsr_quotationMessage);
		vL_AQR_milestoneLayout.setVisibility(View.GONE);
		WholeLayout.setVisibility(View.GONE);
		
		LinearLayout quantuty_layout=(LinearLayout)view.findViewById(R.id.vL_aqsr_quantity_layout);
		quantuty_layout.setVisibility(View.GONE);
		
		final Spinner quantitySpinner=(Spinner)view.findViewById(R.id.vI_aqsr_spinner);
		
		
		message.setText(""+shippingHistoryList.get(0).getChat_message());
		mUserMessage=message.getText().toString().trim();
		message.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mUserMessage=message.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		quantity = (EditText) view.findViewById(R.id.vE_aqsr_quantity);
		final EditText price=(EditText)view.findViewById(R.id.vE_aqsr_price);
		price.setText(""+shippingHistoryList.get(0).getOffered_price());
		mBestPrice=price.getText().toString();
		price.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mBestPrice=price.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		
		String quantity_value = shippingHistoryList.get(0).getChat_RequiredQty();
		quantity.setText(""+quantity_value);
		mQuantity=quantity.getText().toString();
		quantity.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mQuantity=quantity.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		
		view.findViewById(R.id.vI_aqsr_dialogClose).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
			}
		});
		
		mOriginalQuantityUnit=shippingHistoryList.get(0).getChat_UnitsOfMeasurement();
		quantitySpinner.setAdapter(new SpinnerQuantityAdapter(getApplicationContext(), R.layout.spinner_view,mUnitOfMeasurementList));

		for(int i=0;i<mUnitOfMeasurementList.size();i++){
			if(mUnitOfMeasurementList.get(i).getmName().equalsIgnoreCase(mOriginalQuantityUnit)){
				mOriginalQuantityUnitId=i;
				break;
			}
		}
		
		quantitySpinner.setSelection(mOriginalQuantityUnitId);
		quantitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mSelectedQuantityId=mUnitOfMeasurementList.get(position).getmId();
				mSelectedQuantity=mUnitOfMeasurementList.get(position).getmName();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		
		
		attach_list = (ListView) view.findViewById(R.id.attach_list);
		vI_AQR_attachButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(ShippingManufactureChatDetailsActivity.this,FileExplorer.class);
				startActivityForResult(i, REQUEST_GET_SINGLE_FILE);
				
			}
		});
		
		
		view.findViewById(R.id.vI_aqsr_sendButton).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mUserMessage==null || mUserMessage.length()==0  ){
					Toast.makeText(getApplicationContext(), "please enter the message", Toast.LENGTH_SHORT).show();
				}
				/*else if(mQuantity==null || mQuantity.length()==0){
					Toast.makeText(getApplicationContext(), "please enter the quantity", Toast.LENGTH_SHORT).show();
				}
				else if(mQuantity.equalsIgnoreCase("0") || mQuantity.equalsIgnoreCase("0.0")){
					Toast.makeText(getApplicationContext(), "quantity should be grater than zero", Toast.LENGTH_SHORT).show();
				}*/
				else if(mBestPrice==null|| mBestPrice.length()==0){
					Toast.makeText(getApplicationContext(), "please enter the price", Toast.LENGTH_SHORT).show();
				}
				else if(mBestPrice.equalsIgnoreCase("0")|| mBestPrice.equalsIgnoreCase("0.0")){
					Toast.makeText(getApplicationContext(), "price should be grater than zero", Toast.LENGTH_SHORT).show();
				}
				else{
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					new UpdateQuotation().execute();
				}
				
			}
		});
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == REQUEST_GET_SINGLE_FILE){
				if (resultCode == RESULT_OK) {
					String filePath = "";
					Uri uri = data.getParcelableExtra("file");
					if(filePath.length() == 0){
					filePath = uri.getPath();
					}
					String[] args = filePath.split("/");
					String mFileName = args[args.length - 1];
					System.out.println("mFileName---" + mFileName);
					arrayList.add(mFileName+"@"+filePath);
					mAdapter = new GetQuortationAdapter(ShippingManufactureChatDetailsActivity.this);
					attach_list.setAdapter(mAdapter);
					setListViewHeightBasedOnChildren(attach_list);
				}
			}
		}
	}
	
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            return;
        }

      
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount())))+listAdapter.getCount()+50;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
	
	
	
	
	class GetQuortationAdapter extends BaseAdapter {
		private Context context;
		LayoutInflater inflater;

		public GetQuortationAdapter(Context context) {
			this.context = context;
			inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			if(arrayList == null || arrayList.size() == 0){
				return 0;
			}
			return arrayList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {
			if(view == null){
			view = inflater.inflate(R.layout.activity_attachdoc, arg2,false);
			}
			TextView file_name = (TextView) view.findViewById(R.id.file_path);
			ImageView close_file = (ImageView) view.findViewById(R.id.close_attach);


			file_name.setText(""+arrayList.get(position).split("@")[0]);
			
			//icon.setVisibility(View.GONE);
			close_file.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					arrayList.remove(position);
					resetList();
				}
			});
			return view;
		}

		protected void resetList() {
			if(mAdapter != null){
				mAdapter.notifyDataSetChanged();
			}
			setListViewHeightBasedOnChildren(attach_list);
		}
	}
	
	private class UpdateQuotation extends AsyncTask<Void, Void, Boolean>{

		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(ShippingManufactureChatDetailsActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.show();
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			if(attachList != null && attachList.size()>0){
				int len = attachList.size();
				for(int i=0; i<len; i++){
					uploadFiles(attachList.get(i).split("@")[0],attachList.get(i).split("@")[1]);
				}
			}
			 callUpdateQuotation();
			return getQuotaionHistory();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(pDialog != null && pDialog.isShowing()){
				pDialog.cancel();
			}
			if(result){
				getHistoryFromDbAndsetAdapter();
			}else{
				Toast.makeText(ShippingManufactureChatDetailsActivity.this, "please try again", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}


	public Boolean callUpdateQuotation() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		try{
			String jsonData = null;
			if(shippingHistoryList != null && shippingHistoryList.size() >0){
				jsonData = createJson();
			}
			if(jsonData != null){
				HttpClient client = new DefaultHttpClient();  
				String postURL = webServices.UPDATEQUOTATION;//"http://vsuppliervm.cloudapp.net:5132/QuotationService.svc/UpdateQuotation";
				InputStream inputStream = null;
				
				HttpPost httpPost = new HttpPost(postURL);
				
				StringEntity se = new StringEntity(jsonData, HTTP.UTF_8);
				httpPost.setEntity(se);
				
				httpPost.setHeader("Content-type", "application/json");
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("JWTKEY","Verisupplier.android.d2335fhfg4564hghjghjghjget45ert.1.0");
				httpPost.setHeader("OS","ANDROID");
				httpPost.setHeader("USERID",mUserDetails.getmUserID());
				
				HttpResponse httpResponse = client.execute(httpPost);
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				Log.d("statusCode",""+statusCode);
				if(statusCode !=200){
					return false;
				}
				inputStream = httpResponse.getEntity().getContent();
				System.out.println(""+inputStream);
				String result = null;
				if(inputStream != null){
					result = convertInputStreamToString(inputStream);
		        }
				if(result == null || result.length() == 0){
					
				}else{
					jsonParserClass = new JsonParserClass();
					String rslt=jsonParserClass.parseUpdateQuotationResult(result);
					if(rslt != null && rslt.equalsIgnoreCase("true")){
						return true;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	
	private String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;
	}

	private String createJson() {
		String json = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
			Date today = new Date();
			String date = sdf.format(today);
			mUserSubject = shippingHistoryList.get(0).getHistory_subject();
			JSONObject jsn = new JSONObject();
			JSONObject quotationJson = new JSONObject();
			JSONArray chatArray = new JSONArray();
			JSONObject chatObject = new JSONObject();
			JSONObject specsObject = new JSONObject();
			
			if(attachimageUrl==null || attachimageUrl.length()==0){
				mAttachmentList = ""+mAttachment;
			}else{
				mAttachmentList = ""+mAttachment+","+attachimageUrl;
			}
		
			//chatobject
			chatObject.put("Specs", specsObject);
			chatObject.put("Message", ""+message.getText().toString());
			chatObject.put("ResponseUserType", ""+mResponseUserType);
			chatObject.put("ResponseDate", ""+date);
			chatObject.put("UnitsOfMeasurement", mSelectedQuantity);
			chatObject.put("RequiredQty", ""+quantity.getText().toString().trim());
			chatObject.put("AdditionalInfo", "");
			chatObject.put("QuotationResponseId", 2);//quotationHistoryList.get(0).getHistory_QuotationResponseId());
			chatObject.put("Attachments", mAttachmentList);//https://vsupplierstorage.blob.core.windows.net/images/Xperia.jpg
			chatObject.put("OfferedPrize", mBestPrice);
			chatObject.put("Specs", specsObject);
			chatArray.put(chatObject);
			
			// quotation
			quotationJson.put("LastResponseDate", ""+date);
			quotationJson.put("Subject", mUserSubject);
			quotationJson.put("Id", ""+shippingHistoryList.get(0).getHistory_id());
			quotationJson.put("QuotationStatusId", mQuotationStatusId);
			quotationJson.put("ServiceGroupType", 2);
			quotationJson.put("LastResponseSequenceNumber", ""+shippingHistoryList.get(0).getHistory_LastResponseSequenceNumber());
			quotationJson.put("LastManufacturerResponseId", ""+shippingHistoryList.get(0).getHistory_LastManufacturerResponseId());
			quotationJson.put("ChatData", chatArray);
			
			jsn.put("quotation", quotationJson);
			json = jsn.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}
	
	public Boolean getQuotaionHistory() {
		Boolean isValidHistory = false;
		String quoatation_id = shippingHistoryList.get(0).getHistory_id();
		if (isValidHistory.equals(false)) {
			Log.e("Quotation Id", quoatation_id);
			String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetQuotationHistory, "GetQuotationHistory/",quoatation_id);
			if (resultOutparam == null || resultOutparam.length() == 0) {
				isValidHistory = false;
			} else {
				List<QuotationDashboardHistory> quotationDashboardHistories = jsonParserClass.parseAllQuotationHistory(resultOutparam);
				if(quotationDashboardHistories == null|| quotationDashboardHistories.size() == 0){
					isValidHistory = false;
				} else {
					isValidHistory = true;
					Helper helper = new Helper(ShippingManufactureChatDetailsActivity.this);
					helper.openDataBase();
					helper.insertQuotationHistory(quotationDashboardHistories);
					helper.close();
				}
			}
		} else {
			isValidHistory = false;
		}

		return isValidHistory;
	}
	
public class DocumentAdapter extends BaseAdapter{
		
		ArrayList<String> doc_arraylist;

		public DocumentAdapter(ArrayList<String> document_url_list) {
			this.doc_arraylist = document_url_list;
		}

		@Override
		public int getCount() {
			return doc_arraylist.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = getLayoutInflater().inflate(R.layout.adapter_document, null);
			}
			@SuppressWarnings("unused")
			ImageView document_image = (ImageView) convertView.findViewById(R.id.document1);
			
			document_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(doc_arraylist.get(position)));
				startActivity(browserIntent);
					
				}
				
			});
			
			return convertView;
		}
	}

public class SpinnerQuantityAdapter extends ArrayAdapter<Lookups> {
	TextView tv;
	List<Lookups> mTsoList;
	Context mContext;
	int mResource = 0;
	public SpinnerQuantityAdapter(Context context, int resource, List<Lookups> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.mResource = resource;
		this.mTsoList = objects;
	}

	@Override
	public int getCount() {
		return mTsoList.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.spinner_view, parent, false);
		}
		
		tv = (TextView) convertView.findViewById(R.id.quantity_name_spinner);
		String name = mTsoList.get(position).getmName().toString();
		//mSelectedQuantity=name;
		
		tv.setTextSize(15);
		tv.setText(""+name);
		return convertView; 
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.spinner_view, parent, false);
		}
		tv = (TextView) convertView.findViewById(R.id.quantity_name_spinner);
		String name = mTsoList.get(position).getmName().toString();
		tv.setText(""+name);
		//mSelectedQuantity=name;
		return convertView;
	}
}

private Boolean uploadFiles(String mFileName,String mFilePath) {
	webServices=new WebServices();
	jsonParserClass=new JsonParserClass();
	boolean isValid=false;
	byte[] bytes = null ;
	String url = "";
	File file = null;
	try {
		url =webServices.FILEUPLOAD+"/"+mFileName;//"http://vsuppliervm.cloudapp.net:5132/FileAttachmentService.svc/FileUpload/";
		file = new File(mFilePath);         
	}catch(Exception e) {
		e.printStackTrace();
	}
	try { 
		FileInputStream fis = new FileInputStream(file);
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    byte[] buf = new byte[1024];
	    try {
	    	for (int readNum; (readNum = fis.read(buf)) != -1;) {
	    		bos.write(buf, 0, readNum); //no doubt here is 0
	            System.out.println("read " + readNum + " bytes,");
	        }
	    } catch (IOException ex) {
	    }
        bytes = bos.toByteArray();
        
	} catch (Exception e) {              
		Log.d("Exception","Exception");
		e.printStackTrace();
	}                
	try {
		ByteArrayInputStream instream = new ByteArrayInputStream(bytes);          
	    HttpClient httpclient = new DefaultHttpClient();
	    url +="/"+bytes.length;
	    HttpPost httppost = new HttpPost(url);
	    InputStreamEntity reqEntity = new InputStreamEntity(instream,bytes.length);
	    httppost.setEntity(reqEntity);
	    reqEntity.setContentType("binary/octet-stream");
	      
	    httppost.setHeader("Content-type", "application/octet-stream");
	    reqEntity.setChunked(true); // Send in multiple parts if needed
	    reqEntity.setContentEncoding("utf-8");
	    HttpResponse response = httpclient.execute(httppost);
	    HttpEntity entity = response.getEntity();             
	    String result = EntityUtils.toString(entity);
	    
	    if(result==null || result.length()==0){
	    	isValid=false;
	    }else{
	    	 attachimageUrl =attachimageUrl+","+jsonParserClass.parseAttachImageUrl(result);
	    	 isValid=true;
	    }
	    
	    
	    int statusCode = response.getStatusLine().getStatusCode();
	    Log.d("statusCode",""+statusCode);
	    if(statusCode != 200){
	    	return false;
	    }
	    isValid=true;
	} catch (Exception e) {
		e.printStackTrace();
		Log.e("Exception in AsyncTask-doInBackgroung", e.toString());
	} 
	return isValid;
}
}
