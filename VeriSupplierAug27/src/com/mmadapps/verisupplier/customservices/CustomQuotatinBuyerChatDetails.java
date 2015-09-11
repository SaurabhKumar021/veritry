package com.mmadapps.verisupplier.customservices;

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
import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.QuotationDashboardHistory;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.TwoWayView;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.fileattachment.FileExplorer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CustomQuotatinBuyerChatDetails extends BaseActionBarActivity{
	
	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	//from database
	UserDetails mUserDetails;
	ArrayList<QuotationDashboardHistory> customsHistoryList;
	ArrayList<QuotationDashboardHistory> customsHistoryListNew;

	static ArrayList<String> arrayList = new ArrayList<String>();
	
	//webservices
	WebServices webServices;
	JsonParserClass jsonParserClass;
	ProgressDialog pdLoading=null;
	
	//Buyerdetails
	ImageView vI_acqbcd_productImage;
	ListView vI_acqbcd_messageLists;
	TextView vT_acqbcd_productName;
	ImageView vI_acqbcd_createQuotation;
	
	String quoatation_id;
	String additionalinfo;
	
	String mUserSubject;
	String mUserMessage;
	String mCity;
	String mAddress;
	String mCompanyName;
	String mCategoryId;
	String mSubCategoryId;
	String mSelectedCategoryName;
	String mSelectedSubCategoryName;
	String mCountryId;
	String mSelectedCountryName;
	String mSelectedStateName;
	String mSelectedStateId;
	String mOriginalQuantityUnit;
	String mQuantity;
	String mSelectedQuantityId;
	String mSelectedQuantity;
	int mOriginalQuantityUnitId;
	int CountryId;
	int StateId;
	int CategoryId;
	int SubCategoryId;
	
	String mResponseUserType;
	String mQuotationStatusId;
	
	List<CategoryDetails> mCategoryList=new ArrayList<CategoryDetails>();
	List<CategoryDetails> mSubCategoryList=new ArrayList<CategoryDetails>();
	List<CountryDetails> mCountryList=new ArrayList<CountryDetails>();
	List<CountryDetails> mStateList=new ArrayList<CountryDetails>();
	List<Lookups> mUnitOfMeasurementList=new ArrayList<Lookups>();
	
	String attachimageUrl="";
	String mAttachmentList;
	String mAttachment="";
	TwoWayView document_list;
	ArrayList<String> document_url_list;
	DocumentAdapter documentadapter;
	
	private static final int REQUEST_GET_SINGLE_FILE = 0;
	ListView attachmentList;
	static ArrayList<String> attachList = new ArrayList<String>();
	AttachmentListAdapter mAttachmentAdapter;
	String mType;
	
	int mFirstManufacturerPosition;
	
	
	public static List<QuotationDashboardHistory> saveCustomsQuotationList=new ArrayList<QuotationDashboardHistory>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customquotation_buyerchatdeatils);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.vBuyerImage.setVisibility(View.VISIBLE);
		
		initializeView();
		getUserDetail();
		
	}
	
	private void getUserDetail() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mCountryList=helper.getAllCountries();
		mUserDetails=helper.getUserDetails();
		mUnitOfMeasurementList=helper.getUnitOfMeasurentList();
		helper.close();
	}
	
	private void getCategoryList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mCategoryList=helper.getAllCategories("0");
		helper.close();
	}
	
	private void getSubCategoryList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mSubCategoryList=helper.getAllCategories(mCategoryId);
		helper.close();
	}
	
	private void getStatesList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mStateList=helper.getStateWithId(mCountryId);
		helper.close();
	}

	private void initializeView() {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(CustomQuotatinBuyerChatDetails.this));
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
		
		vI_acqbcd_productImage = (ImageView) findViewById(R.id.vI_acqbcd_productImage);
		vT_acqbcd_productName = (TextView) findViewById(R.id.vT_acqbcd_productName);
		vI_acqbcd_messageLists = (ListView) findViewById(R.id.vI_acqbcd_messageLists);
		vI_acqbcd_createQuotation = (ImageView) findViewById(R.id.vI_acqbcd_createQuotation);
		
		vI_acqbcd_createQuotation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				arrayList = new ArrayList<String>();
				attachList=new ArrayList<String>();
				createPopupDialog(additionalinfo);
			}
		});
		
		getHistoryFromDbAndsetAdapter();
	}

	private void getHistoryFromDbAndsetAdapter() {
		Helper helper = new Helper(CustomQuotatinBuyerChatDetails.this);
		helper.openDataBase();
		customsHistoryList = helper.getAllDashboardQuotationHistory(null);
		helper.close();
		
		if(customsHistoryList == null || customsHistoryList.size() == 0){
			
		}else{
			QuortationHistoryAdapter adapter1 = new QuortationHistoryAdapter(customsHistoryList);
			quoatation_id= customsHistoryList.get(0).getHistory_id();
			String contactPersonPicUrl = customsHistoryList.get(0).getHistory_Buyerimage();
			imageLoader.displayImage(contactPersonPicUrl, BaseActionBarActivity.vBuyerImage, options);
			
			BaseActionBarActivity.setmUserSubTitle(""+customsHistoryList.get(0).getHistory_Manufacturename());
			BaseActionBarActivity.setmUserName(""+customsHistoryList.get(0).getHistory_productname());
			
			vT_acqbcd_productName.setText(""+customsHistoryList.get(0).getHistory_productname());
			imageLoader.displayImage(customsHistoryList.get(0).getHistory_productimage(), vI_acqbcd_productImage, options);
			
			vI_acqbcd_messageLists.setAdapter(adapter1);
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
				convertView = inflater.inflate(R.layout.adapter_customquotation_buyerchat, parent, false);
			}
			LinearLayout main_layout = (LinearLayout) convertView.findViewById(R.id.vL_acqbc_rootLayout);
			LinearLayout additionalinfo_layout=(LinearLayout)convertView.findViewById(R.id.vL_acqbc_additionalinfo_layout);
			LinearLayout bestprice_layout=(LinearLayout)convertView.findViewById(R.id.vL_acqbc_bestprice_layout);
			LinearLayout document_layout=(LinearLayout)convertView.findViewById(R.id.vL_acqbc_attachment_layout);
			
			TextView dateTime = (TextView) convertView.findViewById(R.id.vT_acqbc_dateTimeText);
			TextView subject = (TextView) convertView.findViewById(R.id.vT_acqbc_subject);
			TextView message_text=(TextView)convertView.findViewById(R.id.vT_acqbc_message);
			TextView manufacturer_name=(TextView)convertView.findViewById(R.id.vT_acqbc_manufacturerComapnyName);
			TextView manufacturer_address=(TextView)convertView.findViewById(R.id.vT_acqbc_manufacturerAddress);
			TextView category=(TextView)convertView.findViewById(R.id.VT_acqbc_category);
			TextView subCategory=(TextView)convertView.findViewById(R.id.VT_acqbc_subcategory);
			TextView quantity=(TextView)convertView.findViewById(R.id.VT_acqbc_quantity);
			TextView quantity_unit=(TextView)convertView.findViewById(R.id.VT_acqbc_quantity_unit);
			TextView bestprice=(TextView)convertView.findViewById(R.id.VT_acqbc_bestprice);
			TextView price_unit=(TextView)convertView.findViewById(R.id.VT_acqbc_bestprice_unit);
			View attachment_view=(View)convertView.findViewById(R.id.view_acqbc_attachmentview);
			document_list=(TwoWayView)convertView.findViewById(R.id.vL_acqbc_documentlist);
			TextView accept=(TextView)convertView.findViewById(R.id.vT_acqbc_acceptButton);
			TextView reject=(TextView)convertView.findViewById(R.id.vT_acqbc_rejectButton);
			View quantity_view=(View)convertView.findViewById(R.id.view_acqbc_quantityview);
			
            QuotationDashboardHistory history = getItem(position);
			
            additionalinfo= history.getmAdditionalInfo();
			String msgType = history.getChat_ResponseUserType();
			String dateTime1 = customsHistoryList.get(position).getChat_ResponseDate().split(" ")[1]+" "+customsHistoryList.get(position).getChat_ResponseDate().split(" ")[2];
			
			dateTime.setText(dateTime1);
			subject.setText(""+customsHistoryList.get(position).getHistory_subject());
			message_text.setText(""+customsHistoryList.get(position).getChat_message());
			manufacturer_name.setText(""+customsHistoryList.get(position).getmCompanyName());
			manufacturer_address.setText(""+customsHistoryList.get(position).getmCountryName()+","+customsHistoryList.get(position).getmStateName()+","+customsHistoryList.get(position).getmAddress());
			category.setText(""+customsHistoryList.get(position).getmCategoryName());
			subCategory.setText(""+customsHistoryList.get(position).getmSubCategoryName());
			quantity.setText(""+customsHistoryList.get(position).getmQuantity());
			bestprice.setText(""+customsHistoryList.get(position).getOffered_price());
			quantity_unit.setText("/"+customsHistoryList.get(position).getChat_UnitsOfMeasurement());
			if(customsHistoryList.get(position).getChat_UnitsOfMeasurement()==null || customsHistoryList.get(position).getChat_UnitsOfMeasurement().length()==0){
				price_unit.setText("");
			}else{
				price_unit.setText("/"+customsHistoryList.get(position).getChat_UnitsOfMeasurement());
			}
			
			mAttachment=customsHistoryList.get(position).getmAttachment();
			if(mAttachment==null || mAttachment.length()==0){
				document_layout.setVisibility(View.GONE);
				attachment_view.setVisibility(View.GONE);
			}else{
				document_layout.setVisibility(View.VISIBLE);
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
				additionalinfo_layout.setVisibility(View.VISIBLE);
				bestprice_layout.setVisibility(View.GONE);
				accept.setVisibility(View.GONE);
				reject.setVisibility(View.GONE);
				quantity_view.setVisibility(View.GONE);
				main_layout.setBackground(getResources().getDrawable(R.drawable.chat_red));
			}else{
				additionalinfo_layout.setVisibility(View.GONE);
				bestprice_layout.setVisibility(View.VISIBLE);
				quantity_view.setVisibility(View.GONE);
				
				main_layout.setBackground(getResources().getDrawable(R.drawable.chat_blue));
				dateTime.setTextColor(getResources().getColor(R.color.datetime_blue));
				
				for(int i=0;i<customsHistoryList.size();i++){
					if(customsHistoryList.get(i).getChat_ResponseUserType().equalsIgnoreCase("2")){
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
						getCustomData();
						mType="Accept";
						mResponseUserType="1";
						mQuotationStatusId="5";
						getCustomData();
						VerisupplierUtils.mDashboardQuotationDetails.setmOfferedPrice(customsHistoryList.get(0).getOffered_price());
						VerisupplierUtils.mDashboardQuotationDetails.setmCustomAddress(customsHistoryListNew.get(0).getmAddress());
						VerisupplierUtils.mDashboardQuotationDetails.setmCustomCountryName(customsHistoryListNew.get(0).getmCountryName());
						VerisupplierUtils.mDashboardQuotationDetails.setmCustomStateName(customsHistoryListNew.get(0).getmStateName());
						VerisupplierUtils.mDashboardQuotationDetails.setmCustomCategory(customsHistoryListNew.get(0).getmCountryName());
						VerisupplierUtils.mDashboardQuotationDetails.setmCustomSubCategory(customsHistoryListNew.get(0).getmSubCategoryName());
						VerisupplierUtils.mDashboardQuotationDetails.setmCustomQuantity(customsHistoryListNew.get(0).getmQuantity());
						VerisupplierUtils.mDashboardQuotationDetails.setmCustomsUnitOfMeasurement(customsHistoryListNew.get(0).getChat_UnitsOfMeasurement());
						VerisupplierUtils.mDashboardQuotationDetails.setmProductImage(customsHistoryListNew.get(0).getHistory_productimage());
						VerisupplierUtils.mDashboardQuotationDetails.setmProductName(customsHistoryListNew.get(0).getHistory_productname());
						
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
		AlertDialog.Builder builder = new AlertDialog.Builder(CustomQuotatinBuyerChatDetails.this);
		final View view = getLayoutInflater().inflate(R.layout.activity_createquotation_custom, null);
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
		LinearLayout product_layout=(LinearLayout)view.findViewById(R.id.vL_acqc_productLayout);
		product_layout.setVisibility(View.GONE);
		LinearLayout popup_close=(LinearLayout)view.findViewById(R.id.vL_acqc_popup_close);
		popup_close.setVisibility(View.VISIBLE);
		
		ImageView vI_acqs_dialogClose=(ImageView)view.findViewById(R.id.vI_acqc_dialogClose);
		ImageView vI_acqs_send=(ImageView)view.findViewById(R.id.vI_acqc_sendquotation);
		ImageView vI_acqs_attachfile=(ImageView)view.findViewById(R.id.vI_acqc_attachfile);
		
		final EditText subject=(EditText)view.findViewById(R.id.vE_acqc_subject);
		final EditText message=(EditText)view.findViewById(R.id.vE_acqc_message);
		final EditText company_name=(EditText)view.findViewById(R.id.vE_acqc_companyName);
		final EditText city=(EditText)view.findViewById(R.id.vE_acqc_city);
		final EditText address=(EditText)view.findViewById(R.id.vE_acqc_address);
		final EditText quantity=(EditText)view.findViewById(R.id.vE_acqc_quantityText);
		
		final Spinner categorySpinner=(Spinner)view.findViewById(R.id.vS_acqc_categorySpinner);
		final Spinner subCategorySpinner=(Spinner)view.findViewById(R.id.vS_acqc_subcategorySpinner);
		final Spinner quantitySpinner=(Spinner)view.findViewById(R.id.vS_acqc_quantitySpinner);
		final Spinner CountrySpinner=(Spinner)view.findViewById(R.id.vS_acqc_countrySpinner);
		final Spinner StateSpinner=(Spinner)view.findViewById(R.id.vS_acqc_stateSpinner);
		attachmentList=(ListView)view.findViewById(R.id.vL_acqc_attachList);
		
		
		getCustomData();
		subject.setText(""+customsHistoryListNew.get(0).getHistory_subject());
		mUserSubject=subject.getText().toString().trim();
		subject.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mUserSubject=subject.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		message.setText(""+customsHistoryListNew.get(0).getChat_message());
		mUserMessage=message.getText().toString().trim();
		message.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mUserMessage=message.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		company_name.setText(""+customsHistoryListNew.get(0).getmCompanyName());
		mCompanyName=company_name.getText().toString();
		company_name.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCompanyName=company_name.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		city.setText(""+customsHistoryListNew.get(0).getmCity());
		mCity=city.getText().toString().trim();
		city.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCity=city.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		address.setText(""+customsHistoryListNew.get(0).getmAddress());
		mAddress=address.getText().toString().trim();
		address.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mAddress=address.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		quantity.setText(""+customsHistoryListNew.get(0).getmQuantity());
		mQuantity=quantity.getText().toString().trim();
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
		
		
		getCategoryList();
		mSelectedCategoryName=customsHistoryListNew.get(0).getmCategoryName();
		categorySpinner.setAdapter(new CategorySpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mCategoryList));
		for(int i=0;i<mCategoryList.size();i++){
			if(mCategoryList.get(i).getmCategory_Name().equalsIgnoreCase(mSelectedCategoryName)){
				CategoryId=i;
				break;
			}
		}
		categorySpinner.setSelection(CategoryId);
		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mCategoryId=mCategoryList.get(position).getmCategory_Id();
				mSelectedCategoryName=mCategoryList.get(position).getmCategory_Name();
				getSubCategoryList();
				subCategorySpinner.setAdapter(new CategorySpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mSubCategoryList));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		mSelectedSubCategoryName=customsHistoryListNew.get(0).getmSubCategoryName();
		for(int i=0;i<mSubCategoryList.size();i++){
			if(mSubCategoryList.get(i).getmCategory_Name().equalsIgnoreCase(mSelectedSubCategoryName)){
				SubCategoryId=i;
				break;
			}
		}
		subCategorySpinner.setSelection(SubCategoryId);
		subCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mSubCategoryId=mSubCategoryList.get(position).getmCategory_Id();
				mSelectedSubCategoryName=mSubCategoryList.get(position).getmCategory_Name();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		
		mOriginalQuantityUnit=customsHistoryListNew.get(0).getChat_UnitsOfMeasurement();
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
		
		mSelectedCountryName=customsHistoryListNew.get(0).getmCountryName();
		CountrySpinner.setAdapter(new CountrySpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mCountryList));
		for(int i=0;i<mCountryList.size();i++){
			if(mCountryList.get(i).getmCountryName().equalsIgnoreCase(mSelectedCountryName)){
				CountryId=i;
				break;
			}
		}
		CountrySpinner.setSelection(CountryId);
		CountrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mCountryId=mCountryList.get(position).getmCountryId();
				mSelectedCountryName=mCountryList.get(position).getmCountryName();
				getStatesList();
				StateSpinner.setAdapter(new StateSpinnerAdapter(getApplicationContext(),R.layout.spinner_view,mStateList));
				StateSpinner.setSelection(StateId);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		
		mSelectedStateName=customsHistoryListNew.get(0).getmStateName();
		for(int i=0;i<mStateList.size();i++){
			if(mStateList.get(i).getmStateName().equalsIgnoreCase(mSelectedStateName)){
				StateId=i;
				break;
			}
		}
		
		StateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mSelectedStateId=mStateList.get(position).getmStateId();
				mSelectedStateName=mStateList.get(position).getmStateName();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
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
		
		vI_acqs_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mUserMessage==null || mUserMessage.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the message", Toast.LENGTH_SHORT).show();
				}
				else if(mCompanyName==null || mCompanyName.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the company", Toast.LENGTH_SHORT).show();
				}
				else if(mCity==null || mCity.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the city", Toast.LENGTH_SHORT).show();
				}
				else if(mAddress==null || mAddress.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the address", Toast.LENGTH_SHORT).show();
				}
				else if(mQuantity==null || mQuantity.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the quantity", Toast.LENGTH_SHORT).show();
				}
				else{
					mResponseUserType = "1";
					mQuotationStatusId="2";
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					new UpdateCustomQuotation().execute();
				}
				
				
			}
		});
		
		vI_acqs_attachfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(CustomQuotatinBuyerChatDetails.this, FileExplorer.class);
				startActivityForResult(i, REQUEST_GET_SINGLE_FILE);
			}
		});
	}
	
	 private void getCustomData() {
		 Helper helper=new Helper(getApplicationContext());
		 helper.openDataBase();
		 customsHistoryListNew=helper.getAllDashboardQuotationHistory("1");
		 helper.close();
	}

	private class UpdateCustomQuotation extends AsyncTask<Void, Void, Boolean>{

			private ProgressDialog pDialog;
			@Override
			protected void onPreExecute() {
				pDialog = new ProgressDialog(CustomQuotatinBuyerChatDetails.this);
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
					Toast.makeText(CustomQuotatinBuyerChatDetails.this, "success", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(CustomQuotatinBuyerChatDetails.this, "failure", Toast.LENGTH_SHORT).show();
				}
				super.onPostExecute(result);
			}
		}
	  
	 public Boolean callUpdateQuotation() {
			webServices=new WebServices();
			jsonParserClass=new JsonParserClass();
			try{
				String jsonData = null;
				if(customsHistoryList != null && customsHistoryList.size() >0){
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
	 
	 public Boolean getQuotaionHistory() {
			Boolean isValidHistory = false;
			String quoatation_id = customsHistoryList.get(0).getHistory_id();
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
						Helper helper = new Helper(CustomQuotatinBuyerChatDetails.this);
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
	 
	 private String createJson() {
			String json = null;
			try{
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
				Date today = new Date();
				String date = sdf.format(today);
				
				mUserSubject = customsHistoryList.get(0).getHistory_subject();
				JSONObject quotation=new JSONObject();
				JSONObject quotationObject=new JSONObject();
				
				if(attachimageUrl==null || attachimageUrl.length()==0){
					mAttachmentList = ""+mAttachment;
				}else{
					mAttachmentList = ""+mAttachment+","+attachimageUrl;
				}
				
				quotationObject.put("CreatedDate", date);
				quotationObject.put("LastResponseDate", date);
				quotationObject.put("QuotationStatusId", mQuotationStatusId);
				quotationObject.put("LastResponseSequenceNumber", customsHistoryList.get(0).getHistory_LastResponseSequenceNumber());
				quotationObject.put("LastManufacturerResponseId", customsHistoryList.get(0).getHistory_LastManufacturerResponseId());
				quotationObject.put("ServiceGroupType", 4);
				quotationObject.put("Id", customsHistoryListNew.get(0).getHistory_id());
				
				
				/*JSONObject Manufacturer=new JSONObject();
			
				JSONObject ContactDetails=new JSONObject();
				JSONArray ContactPerson=new JSONArray();
				JSONObject ContactPersonObject=new JSONObject();
				ContactPerson.put(ContactPersonObject);
				ContactDetails.put("ContactPerson", ContactPerson);
				Manufacturer.put("ContactDetails", ContactDetails);
				quotationObject.put("Manufacturer", Manufacturer);*/
				
				JSONArray ChatData=new JSONArray();
				JSONObject ChatObject=new JSONObject();
				ChatObject.put("QuotationResponseId", 2);
				ChatObject.put("Message", mUserMessage);
				ChatObject.put("ResponseUserType", mResponseUserType);
				ChatObject.put("Attachments", mAttachmentList);
				ChatObject.put("ResponseBy", mUserDetails.getmUserID());
				ChatObject.put("ResponseDate", date);
				ChatObject.put("OfferedPrize", "0");
				
				JSONObject CustomAttributes=new JSONObject();
				JSONObject categoryObject=new JSONObject();
				categoryObject.put("Id", mCategoryId);
				categoryObject.put("Value", mSelectedCategoryName);
				CustomAttributes.put("Category", categoryObject);
				
				JSONObject subCategoryObject=new JSONObject();
				subCategoryObject.put("Id", mSubCategoryId);
				subCategoryObject.put("Value", mSelectedSubCategoryName);
				CustomAttributes.put("SubCategory", subCategoryObject);
				
				//CustomAttributes.put("CategoryId", mCategoryId);
				//CustomAttributes.put("SubCategory", mSubCategoryId);
				
				JSONObject AddressObject=new JSONObject();
				AddressObject.put("Company", mCompanyName);
				AddressObject.put("City", mCity);
				AddressObject.put("Address1", mAddress);
				AddressObject.put("CountryId", mCountryId);
				AddressObject.put("CountryName", mSelectedCountryName);
				AddressObject.put("StateId", mSelectedStateId);
				AddressObject.put("StateName", mSelectedStateName);
				CustomAttributes.put("Address", AddressObject);
				
				JSONObject QuantityObject=new JSONObject();
				QuantityObject.put("Quantity", mQuantity);
				JSONObject UnitMeasurementObject=new JSONObject();
				UnitMeasurementObject.put("Id", mSelectedQuantityId);
				UnitMeasurementObject.put("Value", mSelectedQuantity);
				QuantityObject.put("UnitOfMeasurement", UnitMeasurementObject);
				CustomAttributes.put("Quantity", QuantityObject);
				
				ChatObject.put("CustomAttributes", CustomAttributes);
				
				ChatData.put(ChatObject);
				quotationObject.put("ChatData", ChatData);
				quotation.put("quotation", quotationObject);
				json = quotation.toString();
				Log.e("Save Quotatiion", json);
			}catch(Exception e){
				e.printStackTrace();
			}
			return json;
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
	
	 public class CategorySpinnerAdapter extends ArrayAdapter<CategoryDetails> {
			TextView tv;
			List<CategoryDetails> mTsoList;
			Context mContext;
			int mResource = 0;
			public CategorySpinnerAdapter(Context context, int resource, List<CategoryDetails> objects) {
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
				String name = mTsoList.get(position).getmCategory_Name().toString();
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
				String name = mTsoList.get(position).getmCategory_Name().toString();
				tv.setText(""+name);
				//mSelectedQuantity=name;
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
	 
	 public class CountrySpinnerAdapter extends ArrayAdapter<CountryDetails> {
	   		TextView tv;
	   		List<CountryDetails> mTsoList;
	   		Context mContext;
	   		int mResource = 0;
	   		public CountrySpinnerAdapter(Context context, int resource, List<CountryDetails> objects) {
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
	   			String name = mTsoList.get(position).getmCountryName().toString();
	   			tv.setText(""+name);
	   			//mSelectedQuantity=name;
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
	   			String name = mTsoList.get(position).getmStateName().toString();
	   			tv.setText(""+name);
	   			//mSelectedQuantity=name;
	   			return convertView;
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
	  
	  /*private class AsyncQuotationOrderDetails extends AsyncTask<Void, Void, Boolean>{
			
			@Override
			protected void onPreExecute() {
				pdLoading = new ProgressDialog(CustomQuotatinBuyerChatDetails.this);
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
						PlaceorderShipping.mFromActivity="PD";
						Intent placeOrderIntent = new Intent(CustomQuotatinBuyerChatDetails.this, PlaceorderShipping.class);
						startActivity(placeOrderIntent);
						overridePendingTransition(0, 0);
						finish();
					}else{
						getHistoryFromDbAndsetAdapter();
					}
					
				}else{
					Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
				}
			}
			
		}*/
	  
	  private Boolean UpdateQuotation() {
			webServices=new WebServices();
			jsonParserClass=new JsonParserClass();
			try{
				String jsonData = null;
				getCustomData();
				if(customsHistoryListNew != null && customsHistoryListNew.size() >0){
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
				
				mUserSubject = customsHistoryList.get(0).getHistory_subject();
				JSONObject quotation=new JSONObject();
				JSONObject quotationObject=new JSONObject();
				
				if(attachimageUrl==null || attachimageUrl.length()==0){
					mAttachmentList = ""+mAttachment;
				}else{
					mAttachmentList = ""+mAttachment+","+attachimageUrl;
				}
				
				quotationObject.put("CreatedDate", date);
				quotationObject.put("LastResponseDate", date);
				quotationObject.put("QuotationStatusId", mQuotationStatusId);
				quotationObject.put("LastResponseSequenceNumber", customsHistoryList.get(0).getHistory_LastResponseSequenceNumber());
				quotationObject.put("LastManufacturerResponseId", customsHistoryList.get(0).getHistory_LastManufacturerResponseId());
				quotationObject.put("ServiceGroupType", VerisupplierUtils.mCustomsParam);
				quotationObject.put("Id", customsHistoryListNew.get(0).getHistory_id());
				
				
				/*JSONObject Manufacturer=new JSONObject();
			
				JSONObject ContactDetails=new JSONObject();
				JSONArray ContactPerson=new JSONArray();
				JSONObject ContactPersonObject=new JSONObject();
				ContactPerson.put(ContactPersonObject);
				ContactDetails.put("ContactPerson", ContactPerson);
				Manufacturer.put("ContactDetails", ContactDetails);
				quotationObject.put("Manufacturer", Manufacturer);*/
				
				JSONArray ChatData=new JSONArray();
				JSONObject ChatObject=new JSONObject();
				ChatObject.put("QuotationResponseId", 2);
				ChatObject.put("Message", customsHistoryListNew.get(0).getChat_message());
				ChatObject.put("ResponseUserType", mResponseUserType);
				ChatObject.put("Attachments", customsHistoryListNew.get(0).getmAttachment());
				ChatObject.put("ResponseBy", mUserDetails.getmUserID());
				ChatObject.put("ResponseDate", date);
				ChatObject.put("OfferedPrize", customsHistoryListNew.get(0).getOffered_price());
				
				JSONObject CustomAttributes=new JSONObject();
				JSONObject categoryObject=new JSONObject();
				categoryObject.put("Id", customsHistoryListNew.get(0).getmCategoryId());
				categoryObject.put("Value", customsHistoryListNew.get(0).getmCategoryName());
				CustomAttributes.put("Category", categoryObject);
				
				JSONObject subCategoryObject=new JSONObject();
				subCategoryObject.put("Id", customsHistoryListNew.get(0).getmSubCategoryId());
				subCategoryObject.put("Value", customsHistoryListNew.get(0).getmSubCategoryName());
				CustomAttributes.put("SubCategory", subCategoryObject);
				
				//CustomAttributes.put("CategoryId", mCategoryId);
				//CustomAttributes.put("SubCategory", mSubCategoryId);
				
				JSONObject AddressObject=new JSONObject();
				AddressObject.put("Company", customsHistoryListNew.get(0).getmCompanyName());
				AddressObject.put("City", customsHistoryListNew.get(0).getmCity());
				AddressObject.put("Address1", customsHistoryListNew.get(0).getmAddress());
				AddressObject.put("CountryId", customsHistoryListNew.get(0).getmCountryId());
				AddressObject.put("CountryName", customsHistoryListNew.get(0).getmCountryName());
				AddressObject.put("StateId", customsHistoryListNew.get(0).getmStateId());
				AddressObject.put("StateName", customsHistoryListNew.get(0).getmStateName());
				CustomAttributes.put("Address", AddressObject);
				
				JSONObject QuantityObject=new JSONObject();
				QuantityObject.put("Quantity", customsHistoryListNew.get(0).getmQuantity());
				JSONObject UnitMeasurementObject=new JSONObject();
				//UnitMeasurementObject.put("Id", mSelectedQuantityId);
				UnitMeasurementObject.put("Value", customsHistoryListNew.get(0).getChat_UnitsOfMeasurement());
				QuantityObject.put("UnitOfMeasurement", UnitMeasurementObject);
				CustomAttributes.put("Quantity", QuantityObject);
				
				ChatObject.put("CustomAttributes", CustomAttributes);
				
				ChatData.put(ChatObject);
				quotationObject.put("ChatData", ChatData);
				quotation.put("quotation", quotationObject);
				json = quotation.toString();
				Log.e("Save Quotatiion", json);
			}catch(Exception e){
				e.printStackTrace();
			}
			return json;
		}
	  
	  private Boolean callQuotationOrderService() {
			Boolean isValid = false;
			webServices = new WebServices();
			jsonParserClass = new JsonParserClass();
			String Inparam = customsHistoryList.get(0).getHistory_id();
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
				pdLoading = new ProgressDialog(CustomQuotatinBuyerChatDetails.this);
				pdLoading.setMessage("Please wait...");
				pdLoading.show();
				pdLoading.setCancelable(false);
				pdLoading.setCanceledOnTouchOutside(false);
				super.onPreExecute();
			}

		@Override
		protected Boolean doInBackground(Void... params) {
			
			if(mType.equalsIgnoreCase("Accept")){
				return UpdateQuotation();
				//return callQuotationOrderService();
			}else{
				UpdateQuotation();
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
					Intent placeOrderIntent=new Intent(CustomQuotatinBuyerChatDetails.this,PlaceOrderCustomsActivity.class);
					placeOrderIntent.putExtra("FROMACTIVITY", "dashboard");
					startActivity(placeOrderIntent);
					overridePendingTransition(0, 0);
					finish();
				}else{
					getHistoryFromDbAndsetAdapter();
				}
			}else{
				Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
			}
		}
		  
	  }
}
