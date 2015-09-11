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
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.DatePicker;
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
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.QuotationDashboardHistory;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.TwoWayView;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.fileattachment.FileExplorer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ShippingBuyerChatDetailsActivity extends BaseActionBarActivity{
	
	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	//Buyerdetails
	private ImageView vI_SDM_productImage;
	private TextView vT_SDM_productName;
	ImageView vI_SDM_createQuotation;
	
	//Listview for chat
	ListView mMessageList;
	QuortationHistoryAdapter mQuotationHistoryAdapter;
	
	//webservices
	WebServices webServices;
	JsonParserClass jsonParserClass;
	private ProgressDialog pdLoading=null;
	
	//from database
	UserDetails mUserDetails;
	ArrayList<QuotationDashboardHistory> shippingHistoryList;
	ArrayList<QuotationDashboardHistory> shippingHistoryListNew;
	static ArrayList<String> arrayList = new ArrayList<String>();
	static ArrayList<String> attachList = new ArrayList<String>();
	static ArrayList<String> attachListNew = new ArrayList<String>();	
	
	String additionalinfo;
	String mUserSubject;
	String mUserMessage;
	String mResponseUserType;
	String mSelectedQuantity;
	String mQuantity;
	String mQuotationStatusId;
	String quoatation_id;
	String mCountryId;
	String mFromCountryId;
	String mToCountryId;
	String mFromCountryName;
	String mFromStateName;
	String mToCountryName;
	String mToStateName;
	String mSelectedFromCountry;
	String mSelectedFromState;
	String mSelectedToCountry;
	String mSelectedToState;
	int selectedFCountryId;
	int selectedFStateId;
	int selectedTCountryId;
	int selectedTStateId;
	String mWidth;
	String mLenght;
	String mHeight;
	String mWeight;
	String mPackages;
	String mShipDate;
	String isPickUpSelected;
	
	List<CountryDetails> mCountries=new ArrayList<CountryDetails>();
	List<CountryDetails> mStates=new ArrayList<CountryDetails>();
	
	private static final int REQUEST_GET_SINGLE_FILE = 0;
	ListView attachmentList;
	AttachmentListAdapter mAttachmentAdapter;
	ArrayList<String> document_url_list;
	DocumentAdapter documentadapter;
		
	TextView shipDate;
	String attachimageUrl="";
	String mAttachmentList;
	String mAttachment="";
	TwoWayView document_list;
	
	String mType;
	int mYear,mMonth,mDay;
	int mFirstManufacturerPosition;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shippingquotation_buyerdetails);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.vBuyerImage.setVisibility(View.VISIBLE);
		
		initializeView();
		getUserDetail();
		
	}

	private void getCountriesList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mCountries=helper.getAllCountries();
		helper.close();
	}
	
	private void getStatesList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mStates=helper.getStateWithId(mCountryId);
		helper.close();
	}

	private void getUserDetail() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}
	
	private void initializeView() {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(ShippingBuyerChatDetailsActivity.this));
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
		
		vI_SDM_productImage = (ImageView) findViewById(R.id.vI_ashbd_productImage);
		vT_SDM_productName = (TextView) findViewById(R.id.vT_ashbd_productName);
		vI_SDM_createQuotation = (ImageView) findViewById(R.id.vI_ashbd_createQuotation);
		
		mMessageList=(ListView)findViewById(R.id.vI_ashbd_messageLists);

		vI_SDM_createQuotation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				arrayList = new ArrayList<String>();
				attachList=new ArrayList<String>();
				createPopupDialog(additionalinfo);
			}
		});
		
		getHistoryFromDbAndsetAdapter();
	}

	private void getHistoryFromDbAndsetAdapter(){
		Helper helper = new Helper(ShippingBuyerChatDetailsActivity.this);
		helper.openDataBase();
		shippingHistoryList = helper.getAllDashboardQuotationHistory(null);
		helper.close();
		
		if(shippingHistoryList == null || shippingHistoryList.size() == 0){
			
		}else{
			QuortationHistoryAdapter adapter1 = new QuortationHistoryAdapter(shippingHistoryList);
			quoatation_id= shippingHistoryList.get(0).getHistory_id();
			String contactPersonPicUrl = shippingHistoryList.get(0).getHistory_Buyerimage();
			imageLoader.displayImage(contactPersonPicUrl, BaseActionBarActivity.vBuyerImage, options);
			
			BaseActionBarActivity.setmUserSubTitle(""+shippingHistoryList.get(0).getHistory_Manufacturename());
			BaseActionBarActivity.setmUserName(""+shippingHistoryList.get(0).getHistory_productname());
			
			vT_SDM_productName.setText(""+shippingHistoryList.get(0).getHistory_productname());
			imageLoader.displayImage(shippingHistoryList.get(0).getHistory_productimage(), vI_SDM_productImage, options);
			
			mMessageList.setAdapter(adapter1);
		}
}
	
	private class QuortationHistoryAdapter extends BaseAdapter{
		
		private ArrayList<QuotationDashboardHistory> list;
		private LinearLayout.LayoutParams lp;
	
		public QuortationHistoryAdapter(ArrayList<QuotationDashboardHistory> arrayList) {
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
			if(convertView==null){
				convertView = inflater.inflate(R.layout.adapter_shippingquotation_buyerchat, parent, false);
			}
			LinearLayout responce_layout=(LinearLayout)convertView.findViewById(R.id.vL_ashbc_additionalinfo_layout);
			LinearLayout main_layout = (LinearLayout) convertView.findViewById(R.id.vL_ashbc_rootLayout);
			LinearLayout from_layout=(LinearLayout)convertView.findViewById(R.id.vL_ashbc_fromLayout);
			
			TextView dateTime = (TextView) convertView.findViewById(R.id.vT_ashbc_dateTimeText);
			TextView subject = (TextView) convertView.findViewById(R.id.vT_ashbc_subject);
			TextView service_type=(TextView)convertView.findViewById(R.id.vT_ashbc_servicetype);
			TextView message_text=(TextView)convertView.findViewById(R.id.vT_ashbc_message);
			TextView from_city=(TextView)convertView.findViewById(R.id.vT_ashbc_fromcity);
			TextView from_country=(TextView)convertView.findViewById(R.id.vT_ashbc_fromcountry);
			TextView to_city=(TextView)convertView.findViewById(R.id.vT_ashbc_tocity);
			TextView to_country=(TextView)convertView.findViewById(R.id.vT_ashbc_tocountry);
			//LinearLayout additionalinfo_layout=(LinearLayout)convertView.findViewById(R.id.vL_ashbc_additionalinfo_layout);
			TextView box_size=(TextView)convertView.findViewById(R.id.vT_ashbc_boxSize);
			TextView no_packages=(TextView)convertView.findViewById(R.id.vT_ashbc_packages);
			TextView weight=(TextView)convertView.findViewById(R.id.vT_ashbc_weight);
			TextView ship_date=(TextView)convertView.findViewById(R.id.vT_ashbc_shipdate);
			TextView ship_type=(TextView)convertView.findViewById(R.id.vT_ashbc_shiptype);
			LinearLayout bestprice_layout=(LinearLayout)convertView.findViewById(R.id.vL_ashbc_bestprice_layout);
			TextView bestprice=(TextView)convertView.findViewById(R.id.vT_ashbc_bestprice);
			TextView price_unit=(TextView)convertView.findViewById(R.id.vT_ashbc_bestprice_unit);
			LinearLayout document_layout=(LinearLayout)convertView.findViewById(R.id.vL_ashbc_documentLayout);
			TextView accept=(TextView)convertView.findViewById(R.id.vT_ashbc_acceptButton);
			TextView reject=(TextView)convertView.findViewById(R.id.vT_ashbc_rejectButton);
			document_list=(TwoWayView)convertView.findViewById(R.id.vL_ashbc_document_list);
			View attachment_view=(View)convertView.findViewById(R.id.view_ashbc_attachment_view);
			View fromto_view=(View)convertView.findViewById(R.id.view_ashbc_fromto_view);
			View additionalinfo_view=(View)convertView.findViewById(R.id.view_ashbc_additionalinfo_view);
			
            QuotationDashboardHistory history = getItem(position);
			
            additionalinfo= history.getmAdditionalInfo();
			String msgType = history.getChat_ResponseUserType();
			String dateTime1 = shippingHistoryList.get(position).getChat_ResponseDate().split(" ")[1]+" "+shippingHistoryList.get(position).getChat_ResponseDate().split(" ")[2];
			
			dateTime.setText(dateTime1);
			subject.setText(""+shippingHistoryList.get(position).getHistory_subject());
			service_type.setText("Shipping Service");
			message_text.setText(""+shippingHistoryList.get(position).getChat_message());
			from_city.setText(shippingHistoryList.get(position).getmShipping_FromCity());
			from_country.setText(","+shippingHistoryList.get(position).getmShipping_FromCountry());
			to_city.setText(shippingHistoryList.get(position).getmShipping_ToCity());
			to_country.setText(","+shippingHistoryList.get(position).getmShipping_ToCountry());
			no_packages.setText(shippingHistoryList.get(position).getmTotalNumberOfCatons());
			weight.setText(shippingHistoryList.get(position).getmTotalWeight());
			ship_date.setText(shippingHistoryList.get(position).getmShipping_DeliveryDate());
			box_size.setText(""+shippingHistoryList.get(position).getmLength()+"x"+""+shippingHistoryList.get(position).getmWidth()+"x"+shippingHistoryList.get(position).getmHeight());
			bestprice.setText(shippingHistoryList.get(position).getOffered_price());
			if(shippingHistoryList.get(position).getChat_UnitsOfMeasurement()==null || shippingHistoryList.get(position).getChat_UnitsOfMeasurement().length()==0){
				price_unit.setText("");
			}else{
				price_unit.setText("/"+shippingHistoryList.get(position).getChat_UnitsOfMeasurement());
			}
				
			if(shippingHistoryList.get(position).getmShipping_pickup()==null || shippingHistoryList.get(position).getmShipping_pickup().equalsIgnoreCase("null")){
			}else{
				if(shippingHistoryList.get(position).getmShipping_pickup().equalsIgnoreCase("true")){
					ship_type.setText("Pickup");
				}else{
					ship_type.setText("DropOff");
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
				accept.setVisibility(View.GONE);
				reject.setVisibility(View.GONE);
				bestprice_layout.setVisibility(View.GONE);
				from_layout.setVisibility(View.VISIBLE);
				additionalinfo_view.setVisibility(View.GONE);
			}else{
				accept.setVisibility(View.VISIBLE);
				reject.setVisibility(View.VISIBLE);
				bestprice_layout.setVisibility(View.VISIBLE);
				responce_layout.setVisibility(View.GONE);
				from_layout.setVisibility(View.GONE);
				
				fromto_view.setVisibility(View.GONE);
				additionalinfo_view.setVisibility(View.GONE);
				
				main_layout.setBackground(getResources().getDrawable(R.drawable.chat_blue));
				dateTime.setTextColor(getResources().getColor(R.color.datetime_blue));
				
				for(int i=0;i<shippingHistoryList.size();i++){
					if(shippingHistoryList.get(i).getChat_ResponseUserType().equalsIgnoreCase("2")){
						mFirstManufacturerPosition=i;
						Log.e("mFirstManufacturerPosition", ""+i);
						break;
					}else{
					}
				}
				
				if(position==mFirstManufacturerPosition){
					accept.setVisibility(View.VISIBLE);
					reject.setVisibility(View.VISIBLE);
				}else{
					accept.setVisibility(View.GONE);
					reject.setVisibility(View.GONE);
				}
				
				
				accept.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						getShippingData();
						mType="Accept";
						mResponseUserType="1";
						mQuotationStatusId="5";
						new AsyncQuotationDetails().execute();
					}
				});
				
				reject.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mType="Reject";
						mQuotationStatusId="3";
						mResponseUserType = "1";
						new AsyncQuotationDetails().execute();
					}
				});
			}
			return convertView;
		}
	}
	
	protected void createPopupDialog(String mProvider) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ShippingBuyerChatDetailsActivity.this);
		final View view = getLayoutInflater().inflate(R.layout.activity_createquotation_shipping, null);
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
		LinearLayout vL_acqs_productlayout=(LinearLayout)view.findViewById(R.id.vL_acqs_productlayout);
		vL_acqs_productlayout.setVisibility(View.GONE);
		LinearLayout vL_acqs_popup_close=(LinearLayout)view.findViewById(R.id.vL_acqs_popup_close);
		vL_acqs_popup_close.setVisibility(View.VISIBLE);
		
		ImageView vI_acqs_dialogClose=(ImageView)view.findViewById(R.id.vI_acqs_dialogClose);
		ImageView vI_acqs_send=(ImageView)view.findViewById(R.id.vI_acqs_send);
		
		ImageView vI_acqs_attachfile=(ImageView)view.findViewById(R.id.vI_acqs_attachfile);
		
		final EditText subject=(EditText)view.findViewById(R.id.vE_acqs_subject);
		final EditText message=(EditText)view.findViewById(R.id.vE_acqs_message);
		final EditText length=(EditText)view.findViewById(R.id.vE_acqs_length);
		final EditText width=(EditText)view.findViewById(R.id.vE_acqs_width);
		final EditText height=(EditText)view.findViewById(R.id.vE_acqs_height);
		final EditText packages=(EditText)view.findViewById(R.id.vE_acqs_packages);
		final EditText weight=(EditText)view.findViewById(R.id.vE_acqs_weight);
		shipDate=(TextView)view.findViewById(R.id.vE_acqs_date);
		attachmentList=(ListView)view.findViewById(R.id.vL_scq_attachmentlist);
		final ImageView vI_SCQ_PICKUP = (ImageView) view.findViewById(R.id.vI_SCQ_PICKUP);
		final ImageView vI_SCQ_DROP = (ImageView)view. findViewById(R.id.vI_SCQ_DROP);
		
		length.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mLenght=length.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		weight.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mWeight=weight.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		height.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mHeight=height.getText().toString();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		packages.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mPackages=packages.getText().toString();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		width.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mWidth=width.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		final Spinner fromcountrySpinner,toCountrySpinner;
		final Spinner fromstateSpinner;
		final Spinner toStateSpinner;
		
		getShippingData();
		subject.setText(""+shippingHistoryListNew.get(0).getHistory_subject());
		message.setText(""+shippingHistoryListNew.get(0).getChat_message());
		length.setText(""+shippingHistoryListNew.get(0).getmLength());
		width.setText(""+shippingHistoryListNew.get(0).getmWidth());
		height.setText(""+shippingHistoryListNew.get(0).getmHeight());
		packages.setText(""+shippingHistoryListNew.get(0).getmTotalNumberOfCatons());
		weight.setText(""+shippingHistoryListNew.get(0).getmWeight());
		
		shipDate.setText(""+shippingHistoryListNew.get(0).getmShipping_DeliveryDate());
		mShipDate=shipDate.getText().toString();
		mSelectedFromCountry=shippingHistoryListNew.get(0).getmShipping_FromCountry();
		
		mSelectedToCountry=shippingHistoryListNew.get(0).getmShipping_ToCountry();
		mSelectedToState=shippingHistoryListNew.get(0).getmShipping_ToCity();
		
		if(shippingHistoryListNew.get(0).getmShipping_pickup()==null || shippingHistoryListNew.get(0).getmShipping_pickup().equalsIgnoreCase("null")){
		}else{
			if(shippingHistoryListNew.get(0).getmShipping_pickup().equalsIgnoreCase("true")){
				vI_SCQ_DROP.setImageResource(R.raw.icon_radio_unclicked);
				vI_SCQ_PICKUP.setImageResource(R.raw.icon_radio_clicked);
			}else{
				vI_SCQ_DROP.setImageResource(R.raw.icon_radio_clicked);
				vI_SCQ_PICKUP.setImageResource(R.raw.icon_radio_unclicked);
			}
			
		}
		
		getCountriesList();
		getStatesList();
		
		fromcountrySpinner=(Spinner)view.findViewById(R.id.vS_acqs_fromcountry);
		fromstateSpinner=(Spinner)view.findViewById(R.id.vS_acqs_fromCity);
		
		toCountrySpinner=(Spinner)view.findViewById(R.id.vS_acqs_tocountry);
		toStateSpinner=(Spinner)view.findViewById(R.id.vS_acqs_toCity);
		
		for(int i=0;i<mCountries.size();i++){
			if(mCountries.get(i).getmCountryName().equalsIgnoreCase(mSelectedFromCountry)){
				selectedFCountryId=i;
				break;
			}
		}
		fromcountrySpinner.setAdapter(new SpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mCountries)); 
		fromcountrySpinner.setSelection(selectedFCountryId);
	
		fromcountrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mFromCountryId=mCountries.get(position).getmCountryId();
				mFromCountryName=mCountries.get(position).getmCountryName();
				mCountryId=mCountries.get(position).getmCountryId();
				getStatesList();
				fromstateSpinner.setAdapter(new StateSpinnerAdapter(getApplicationContext(),R.layout.spinner_view,mStates));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		
		fromstateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mFromStateName=mStates.get(position).getmStateName();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		
		for(int i=0;i<mCountries.size();i++){
			if(mCountries.get(i).getmCountryName().equalsIgnoreCase(mSelectedToCountry)){
				selectedTCountryId=i;
				break;
			}
		}
		toCountrySpinner.setAdapter(new SpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mCountries));
		toCountrySpinner.setSelection(selectedTCountryId);		
		toCountrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mToCountryId=mCountries.get(position).getmCountryId();
				mToCountryName=mCountries.get(position).getmCountryName();
				mCountryId=mCountries.get(position).getmCountryId();
				getStatesList();
				for(int i=0;i<mStates.size();i++){
					if(mStates.get(i).getmStateName().equalsIgnoreCase(mSelectedToState)){
						selectedTStateId=i;
						break;
					}
				}
				toStateSpinner.setSelection(selectedFStateId);
				toStateSpinner.setAdapter(new StateSpinnerAdapter(getApplicationContext(),R.layout.spinner_view,mStates));
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
				
		});
		
		toStateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mToStateName=mStates.get(position).getmStateName();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		
		vI_SCQ_PICKUP.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String tag1 = vI_SCQ_PICKUP.getTag().toString();
				if(tag1.equals("0")){
					vI_SCQ_PICKUP.setTag("1");
					vI_SCQ_DROP.setImageResource(R.raw.icon_radio_unclicked);
					vI_SCQ_PICKUP.setImageResource(R.raw.icon_radio_clicked);
					isPickUpSelected="true";
				}else{
					vI_SCQ_PICKUP.setTag("0");
					vI_SCQ_PICKUP.setImageResource(R.raw.icon_radio_unclicked);
					vI_SCQ_DROP.setImageResource(R.raw.icon_radio_clicked);
				}
			}
		});
		
		vI_SCQ_DROP.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String tag = vI_SCQ_DROP.getTag().toString();
				if(tag.equals("0")){
					vI_SCQ_DROP.setTag("1");
					vI_SCQ_PICKUP.setImageResource(R.raw.icon_radio_unclicked);
					vI_SCQ_DROP.setImageResource(R.raw.icon_radio_clicked);
				}else{
					vI_SCQ_DROP.setTag("0");
					vI_SCQ_PICKUP.setImageResource(R.raw.icon_radio_clicked);
					vI_SCQ_DROP.setImageResource(R.raw.icon_radio_unclicked);
					isPickUpSelected="false";
				}
			}
		});
		
		shipDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callDatePicker();
			}
		});
		
		subject.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mUserSubject=subject.getText().toString();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			
			}
		});
		
		message.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mUserMessage=message.getText().toString();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		vI_acqs_attachfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ShippingBuyerChatDetailsActivity.this, FileExplorer.class);
				startActivityForResult(i, REQUEST_GET_SINGLE_FILE);
			}
		});
		
		vI_acqs_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mUserMessage==null || mUserMessage.length()==0){
					
				}else {
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					mResponseUserType = "1";
					mQuotationStatusId="2";
					new UpdateQuotation().execute();
				}
			}
		});
		
		vI_acqs_dialogClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
			}
		});
		
	}
	

	private void callDatePicker() {
		DatePickerDialog dpd = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
			 
            @Override
            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
            	shipDate.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
            }
        }, mYear, mMonth, mDay);
		dpd.setTitle("Select Date");
		dpd.show();
	}
	
	
	private void getShippingData() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		shippingHistoryListNew=helper.getAllDashboardQuotationHistory("1");
		helper.close();
	}

	private class UpdateQuotation extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(ShippingBuyerChatDetailsActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
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
			if(pdLoading != null && pdLoading.isShowing()){
				pdLoading.cancel();
			}
			if(result){
				getHistoryFromDbAndsetAdapter();
				Toast.makeText(ShippingBuyerChatDetailsActivity.this, "success", Toast.LENGTH_SHORT).show();
			
			}else{
				Toast.makeText(ShippingBuyerChatDetailsActivity.this, "failure", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}


	public Boolean callUpdateQuotation() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		try{
			String jsonData = null;
			getShippingData();
			if(shippingHistoryList != null && shippingHistoryList.size() >0){
				jsonData = createJson();
			}
			if(jsonData != null){
				HttpClient client = new DefaultHttpClient();  
				String postURL = webServices.UPDATEQUOTATION;
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

	private String createJson() {
		String json = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
			Date today = new Date();
			String date = sdf.format(today);
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
			
			//Shipping
			JSONObject shippingObject = new JSONObject();
			shippingObject.put("DeliveryDate", mShipDate);
			shippingObject.put("FromCity", mFromStateName);
			shippingObject.put("FromCountry", mFromCountryName);
			shippingObject.put("ToCountry", mToCountryName);
			shippingObject.put("ToCity", mToStateName);
			shippingObject.put("TotalNumberOfCatons", mPackages);
			shippingObject.put("TotalWeight", mWeight);
			
			JSONObject DimensionLwh = new JSONObject();
			DimensionLwh.put("Length", mLenght);
			DimensionLwh.put("Width", mWidth);
			DimensionLwh.put("Height", mHeight);
			DimensionLwh.put("Weight", mWeight);
			/*DimensionLwh.put("UnitsOfMeasurement1", 52);
			DimensionLwh.put("UnitsOfMeasurement2", 25);*/
			shippingObject.put("DimensionLwh", DimensionLwh);
			
			
			JSONObject DimensionWeight = new JSONObject();
		//	DimensionWeight.put("Length", 0);
		//	DimensionWeight.put("Width", 0);
		//	DimensionWeight.put("Height", 0);
			DimensionWeight.put("Weight", mWeight);
			/*DimensionWeight.put("UnitsOfMeasurement1", 52);
			DimensionWeight.put("UnitsOfMeasurement2", 0);*/
			shippingObject.put("DimensionWeight", DimensionWeight);
			
			//chatobject
			chatObject.put("Specs", specsObject);
			
			chatObject.put("Message", mUserMessage);
			chatObject.put("ResponseUserType", ""+mResponseUserType);
			chatObject.put("ResponseDate", ""+date);
			chatObject.put("UnitsOfMeasurement", mSelectedQuantity);
			//chatObject.put("RequiredQty", ""+mQuantity);
			//chatObject.put("AdditionalInfo", "");
			chatObject.put("QuotationResponseId", 2);
			chatObject.put("Attachments", mAttachmentList);
			chatObject.put("Specs", specsObject);
			chatObject.put("ShippingAttributes", shippingObject);
			chatArray.put(chatObject);
			
			// quotation
			quotationJson.put("LastResponseDate", ""+date);
			quotationJson.put("Subject", mUserSubject);
			quotationJson.put("Id", ""+shippingHistoryList.get(0).getHistory_id());
			quotationJson.put("QuotationStatusId", mQuotationStatusId);
			quotationJson.put("LastResponseSequenceNumber", ""+shippingHistoryList.get(0).getHistory_LastResponseSequenceNumber());
			quotationJson.put("LastManufacturerResponseId", ""+shippingHistoryList.get(0).getHistory_LastManufacturerResponseId());
			quotationJson.put("ServiceGroupType", 2);
			
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
					Helper helper = new Helper(ShippingBuyerChatDetailsActivity.this);
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
	
	private String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;
	}
	
	public class SpinnerAdapter extends ArrayAdapter<CountryDetails> {
		TextView tv;
		List<CountryDetails> mTsoList;
		Context mContext;
		int mResource = 0;
		public SpinnerAdapter(Context context, int resource, List<CountryDetails> objects) {
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
			String name = mTsoList.get(position).getmCountryName().toString();
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
			String name = mTsoList.get(position).getmCountryName().toString();
			tv.setText(""+name);
			return convertView;
		}
	}
	
	private class StateSpinnerAdapter extends ArrayAdapter<CountryDetails> {
		TextView tv;
		List<CountryDetails> mTsoList;
		Context mContext;
		@SuppressWarnings("unused")
		int mResource = 0;
		public StateSpinnerAdapter(Context context, int resource, List<CountryDetails> objects) {
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
			String name = mTsoList.get(position).getmStateName().toString();
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
			String name = mTsoList.get(position).getmStateName().toString();
			tv.setText(""+name);
			return convertView;
		}
	}
	
	private class AttachmentListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return attachList.size();
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
				convertView =getLayoutInflater().inflate(R.layout.activity_attachdoc, parent, false);
			}
			
			TextView file_name = (TextView) convertView.findViewById(R.id.file_path);
			ImageView close_file = (ImageView) convertView.findViewById(R.id.close_attach);
			file_name.setText(""+attachList.get(position).split("@")[0]);
			
			close_file.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					attachList.remove(position);
					setAttchmentListAdapter();
				}
			});
			return convertView;
		}
		
	}
	
	private void setAttchmentListAdapter() {
		if(attachList.size()==0){
		}else{
		}
		mAttachmentAdapter=new AttachmentListAdapter();
		attachmentList.setAdapter(mAttachmentAdapter);
		setListViewHeightBasedOnChildren(attachmentList);
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            return;
        }

        int Size=0;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            Size=attachList.size()+50;
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount())))+listAdapter.getCount()+Size;
        listView.setLayoutParams(params);
        listView.requestLayout();
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		
		case REQUEST_GET_SINGLE_FILE: {
			if (resultCode == RESULT_OK) {
				String filePath = "";
				Uri uri = data.getParcelableExtra("file");
				
				if(filePath.length() == 0){
					filePath = uri.getPath();
				}
				String[] args = filePath.split("/");
				String mFileName = args[args.length - 1];
				System.out.println("mFileName---" + mFileName);
				attachList.add(mFileName+"@"+filePath);
				setAttchmentListAdapter();
			}
		}
		break;
		
		default:
			break;
		}
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
	
	private Boolean UpdateQuotation() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		try{
			String jsonData = null;
			getShippingData();
			if(shippingHistoryListNew != null && shippingHistoryListNew.size() >0){
				jsonData = createUpdateJson();
			}
			if(jsonData != null){
				HttpClient client = new DefaultHttpClient();  
				String postURL = webServices.UPDATEQUOTATION;
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

	private String createUpdateJson() {
		String json = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
			Date today = new Date();
			String date = sdf.format(today);
			//mUserSubject = shippingHistoryList.get(0).getHistory_subject();
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
			
			//Shipping
			JSONObject shippingObject = new JSONObject();
			shippingObject.put("DeliveryDate", shippingHistoryListNew.get(0).getmShipping_DeliveryDate());
			shippingObject.put("FromCity", shippingHistoryListNew.get(0).getmShipping_FromCity());
			shippingObject.put("FromCountry", shippingHistoryListNew.get(0).getmShipping_FromCountry());
			shippingObject.put("ToCountry", shippingHistoryListNew.get(0).getmShipping_ToCountry());
			shippingObject.put("ToCity", shippingHistoryListNew.get(0).getmShipping_ToCity());
			shippingObject.put("TotalNumberOfCatons", shippingHistoryListNew.get(0).getmTotalNumberOfCatons());
			shippingObject.put("TotalWeight", shippingHistoryListNew.get(0).getmWeight());
			shippingObject.put("PickUp",isPickUpSelected);
			
			JSONObject DimensionLwh = new JSONObject();
			DimensionLwh.put("Length", shippingHistoryListNew.get(0).getmLength());
			DimensionLwh.put("Width", shippingHistoryListNew.get(0).getmWidth());
			DimensionLwh.put("Height", shippingHistoryListNew.get(0).getmHeight());
			DimensionLwh.put("Weight", shippingHistoryListNew.get(0).getmWeight());
			/*DimensionLwh.put("UnitsOfMeasurement1", 52);
			DimensionLwh.put("UnitsOfMeasurement2", 25);*/
			shippingObject.put("DimensionLwh", DimensionLwh);
			
			
			JSONObject DimensionWeight = new JSONObject();
		//	DimensionWeight.put("Length", 0);
		//	DimensionWeight.put("Width", 0);
		//	DimensionWeight.put("Height", 0);
			DimensionWeight.put("Weight", shippingHistoryListNew.get(0).getmWeight());
			/*DimensionWeight.put("UnitsOfMeasurement1", 52);
			DimensionWeight.put("UnitsOfMeasurement2", 0);*/
			shippingObject.put("DimensionWeight", DimensionWeight);
			
			//chatobject
			chatObject.put("Specs", specsObject);
			
			chatObject.put("Message", shippingHistoryListNew.get(0).getChat_message());
			chatObject.put("ResponseUserType", ""+mResponseUserType);
			chatObject.put("ResponseDate", ""+date);
			chatObject.put("UnitsOfMeasurement", shippingHistoryListNew.get(0).getChat_UnitsOfMeasurement());
			//chatObject.put("RequiredQty", ""+mQuantity);
			//chatObject.put("AdditionalInfo", "");
			chatObject.put("QuotationResponseId", 2);//quotationHistoryList.get(0).getHistory_QuotationResponseId());
			chatObject.put("Attachments", shippingHistoryListNew.get(0).getmAttachment());//https://vsupplierstorage.blob.core.windows.net/images/Xperia.jpg
			//chatObject.put("Specs", specsObject);
			chatObject.put("ShippingAttributes", shippingObject);
			chatArray.put(chatObject);
			
			// quotation
			quotationJson.put("LastResponseDate", ""+date);
			quotationJson.put("Subject", mUserSubject);
			quotationJson.put("Id", ""+shippingHistoryList.get(0).getHistory_id());
			quotationJson.put("QuotationStatusId", mQuotationStatusId);
			quotationJson.put("LastResponseSequenceNumber", ""+shippingHistoryList.get(0).getHistory_LastResponseSequenceNumber());
			quotationJson.put("LastManufacturerResponseId", ""+shippingHistoryList.get(0).getHistory_LastManufacturerResponseId());
			quotationJson.put("ServiceGroupType", 2);
			
			quotationJson.put("ChatData", chatArray);
			
			jsn.put("quotation", quotationJson);
			json = jsn.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}

	private Boolean callQuotationOrderService() {
		Boolean isValid = false;
		webServices = new WebServices();
		jsonParserClass = new JsonParserClass();
		String Inparam = shippingHistoryList.get(0).getHistory_id();
		String resultOutParam = webServices.CallWebHTTPBindingService(ApiType.GetQuoteDetailsForQuickOrder,"GetQuoteDetailsForOrder/", Inparam);
		if (resultOutParam == null || resultOutParam.length() == 0) {
			isValid = false;
		} else {
			VerisupplierUtils.mQuotationDetails = jsonParserClass.parseQuickOrderResult(resultOutParam,"GetQuoteDetailsForOrderResult");
			if (VerisupplierUtils.mQuotationDetails != null) {
				isValid = true;
			}
		}
		return isValid;
	}
	
	private class AsyncQuotationDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(ShippingBuyerChatDetailsActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			UpdateQuotation();
			if(mType.equalsIgnoreCase("Accept")){
				return callQuotationOrderService();
			}else{
				return getQuotaionHistory();
			}
			
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			if(result){
				if(mType.equalsIgnoreCase("Accept")){
					Intent inspectionIntent=new Intent(ShippingBuyerChatDetailsActivity.this,PlaceOrderShippingActivity.class);
					inspectionIntent.putExtra("FROMACTIVITY", "dashboard");
					startActivity(inspectionIntent);
					overridePendingTransition(0, 0);
					finish();
				}else{
					getHistoryFromDbAndsetAdapter();
				}
			}else{
				Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
			}
		}
		
	}

}
