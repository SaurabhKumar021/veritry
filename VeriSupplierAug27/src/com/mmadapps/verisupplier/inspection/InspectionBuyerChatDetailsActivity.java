package com.mmadapps.verisupplier.inspection;

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
import java.util.Calendar;
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
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.QuotationDashboardHistory;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.TwoWayView;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.mmadapps.verisupplier.fileattachment.FileExplorer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class InspectionBuyerChatDetailsActivity extends  BaseActionBarActivity{
	
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	//Buyerdetails
	private ImageView vI_aiqcd_productImage;
	private ListView vI_aiqcd_messageLists;
	private TextView vT_aiqcd_productName;
	ImageView vI_aiqcd_createQuotation;
	
	
	//webservice
	WebServices webServices;
	JsonParserClass jsonParserClass;
	
	//from database
	UserDetails mUserDetails;
	ArrayList<QuotationDashboardHistory> inspectionHistoryList;
	ArrayList<QuotationDashboardHistory> inspectionHistoryListNew;
	List<ManufacturerDetails> mManufacturerDetails=new ArrayList<ManufacturerDetails>();
	static ArrayList<String> arrayList = new ArrayList<String>();
	List<Lookups> mInspectionTypes=new ArrayList<Lookups>();
	List<Lookups> mIndustryList=new ArrayList<Lookups>();
	List<Lookups> mInspectorList=new ArrayList<Lookups>();
	List<CountryDetails> mCountryList=new ArrayList<CountryDetails>();
	List<CountryDetails> mStateList=new ArrayList<CountryDetails>();
	
	
	//String
	String quoatation_id;
	String mResponseUserType;
	String mQuotationStatusId;
	String mSubject,mUserMessage;
	String mCountryName;
	String mStateName;
	String mCity;
	String mAddress;
	String mSelectedIndustory;
	String mSelectedInspectortype;
	String mSelectedInspectionType;
	String mSelectedIndustoryId;
	String mSelectedInspectortypeId;
	String mSelectedInspectionTypeId;
	String mCountryId;
	String mSelectedCountryName;
	String mSelectedCountryId;
	String mSelectedStateName;
	String mSelectedStateId;
	int  IndustryId;
	int  InspectionId;
	int  InspectorId;
	int countryId;
	int stateId;
	
	//attachment variables
	String attachimageUrl="";
	String mAttachmentList;
	String mAttachment="";
	TwoWayView document_list;
	ArrayList<String> document_url_list;
	DocumentAdapter documentadapter;
	private static final int REQUEST_GET_SINGLE_FILE = 0;
	AttachmentListAdapter mAttachmentAdapter;
	ListView attachmentList;
	static ArrayList<String> attachList = new ArrayList<String>();	
	
	String mType;
	ProgressDialog pdLoading=null;
	
	public static List<QuotationDashboardHistory> saveQuotationList=new ArrayList<QuotationDashboardHistory>();
	
	int mFirstManufacturerPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspectionquotation_chatdetails);
		
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.vBuyerImage.setVisibility(View.VISIBLE);
		
		getInspectionList();
		getManufacturerDetails();
		getUserDetails();
		initializeView();
		getHistoryFromDbAndsetAdapter();
		
	}
	
	
	//Incepection Detail From Database
	private void getInspectionList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mCountryList=helper.getAllCountries();
		mStateList=helper.getStateWithId(mCountryId);
		mInspectionTypes=helper.getInspectionType();
		mIndustryList=helper.getTypeOfIndustry();
		mInspectorList=helper.getTypeOfInspector();
		helper.close();
	}
	
	
	private void getStatesList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mStateList=helper.getStateWithId(mCountryId);
		helper.close();
	}
	
	
	//Manufacture Details From Datatbase
	private void getManufacturerDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mManufacturerDetails=helper.getManufacturerDetails(ProductDetailsActivity.mManufacturerId);
		helper.close();
	}

	//Userdetail from Database
	private void getUserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}


	private void initializeView() {
		webServices = new WebServices();
		jsonParserClass = new JsonParserClass();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(InspectionBuyerChatDetailsActivity.this));
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
		
		vI_aiqcd_productImage = (ImageView)findViewById(R.id.vI_aiqcd_productImage);
		vI_aiqcd_messageLists = (ListView) findViewById(R.id.vI_aiqcd_messageLists);
		vI_aiqcd_createQuotation = (ImageView) findViewById(R.id.vI_aiqcd_createQuotation);
		vT_aiqcd_productName = (TextView) findViewById(R.id.vT_aiqcd_productName);
		
		vI_aiqcd_createQuotation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				attachList=new  ArrayList<String>();
				createPopupDialog();
				
			}
		});
		
		
		
	}
	
	private void getHistoryFromDbAndsetAdapter(){
		Helper helper = new Helper(InspectionBuyerChatDetailsActivity.this);
		helper.openDataBase();
		inspectionHistoryList = helper.getAllDashboardQuotationHistory(null);
		helper.close();
		
		if(inspectionHistoryList == null || inspectionHistoryList.size() == 0){
			
		}else{
			InspectionBuyerAdapter adapter1 = new InspectionBuyerAdapter(inspectionHistoryList);
			quoatation_id= inspectionHistoryList.get(0).getHistory_id();
			String contactPersonPicUrl = inspectionHistoryList.get(0).getHistory_Buyerimage();
			imageLoader.displayImage(contactPersonPicUrl, BaseActionBarActivity.vBuyerImage, options);
			
			BaseActionBarActivity.setmUserSubTitle(""+inspectionHistoryList.get(0).getHistory_Manufacturename());
			BaseActionBarActivity.setmUserName(""+inspectionHistoryList.get(0).getHistory_productname());
			
			vT_aiqcd_productName.setText(""+inspectionHistoryList.get(0).getHistory_productname());
			imageLoader.displayImage(inspectionHistoryList.get(0).getHistory_productimage(), vI_aiqcd_productImage, options);
			
			vI_aiqcd_messageLists.setAdapter(adapter1);
		}
    }
	
	
	private class InspectionBuyerAdapter extends BaseAdapter{
		
		private ArrayList<QuotationDashboardHistory> list;
		private LinearLayout.LayoutParams lp;
	
		public InspectionBuyerAdapter(ArrayList<QuotationDashboardHistory> arrayList) {
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
				convertView = inflater.inflate(R.layout.adapter_inspectionquotation_buyerchat, parent, false);
			}
			
			LinearLayout vL_SDM_rootLayout = (LinearLayout) convertView.findViewById(R.id.vL_aiqbc_rootLayout);
			LinearLayout vL_aiqbc_bestprice_layout = (LinearLayout) convertView.findViewById(R.id.vL_aiqbc_bestprice_layout);
			LinearLayout vL_aiqbc_additionalinfo_layout = (LinearLayout) convertView.findViewById(R.id.vL_aiqbc_additionalinfo_layout);
			LinearLayout vT_aiqbc_placeto_inspectlayout = (LinearLayout) convertView.findViewById(R.id.vT_aiqbc_placeto_inspectlayout);
			LinearLayout document_layout=(LinearLayout)convertView.findViewById(R.id.vL_aiqbc_document_layout);
			
			
			//datetime
			TextView vT_aiqbc_dateTimeText = (TextView) convertView.findViewById(R.id.vT_aiqbc_dateTimeText);
			//subject,message
			TextView vT_aiqbc_subject = (TextView) convertView.findViewById(R.id.vT_aiqbc_subject);
			TextView vT_aiqbc_message = (TextView) convertView.findViewById(R.id.vT_aiqbc_message);
			//manufacture
			TextView vT_aiqbc_manufactureName = (TextView) convertView.findViewById(R.id.vT_aiqbc_manufactureName);
			TextView vT_aiqbc_manufacture_address = (TextView) convertView.findViewById(R.id.vT_aiqbc_manufacture_address);
			//Inspection
			TextView vT_aiqbc_inspection_typeofindustry = (TextView) convertView.findViewById(R.id.vT_aiqbc_inspection_typeofindustry);
			TextView vT_aiqbc_inspector_type = (TextView) convertView.findViewById(R.id.vT_aiqbc_inspector_type);
			TextView vT_aiqbc_inspection_type = (TextView) convertView.findViewById(R.id.vT_aiqbc_inspection_type);
			//BestPrice
			TextView vT_aiqbc_bestprice = (TextView) convertView.findViewById(R.id.vT_aiqbc_bestprice);
			TextView vT_aiqbc_bestprice_unit = (TextView) convertView.findViewById(R.id.vT_aiqbc_bestprice_unit);
			//Accept Reject
			TextView vT_aiqbc_acceptButton = (TextView) convertView.findViewById(R.id.vT_aiqbc_acceptButton);
			TextView vT_aiqbc_rejectButton = (TextView) convertView.findViewById(R.id.vT_aiqbc_rejectButton);
			document_list=(TwoWayView)convertView.findViewById(R.id.vL_aiqbc_document_list);
			View industry_view=(View)convertView.findViewById(R.id.view_aiqbc_industry_view);
			View inspector_view=(View)convertView.findViewById(R.id.view_aiqbc_inspector_view);
			View inspection_view=(View)convertView.findViewById(R.id.view_aiqbc_inspection_view);
			View placetoinspect_view=(View)convertView.findViewById(R.id.view_aiqbc_placetoinspect_view);
			View attachment_view=(View)convertView.findViewById(R.id.view_aiqbc_attachmentView);
			
			 QuotationDashboardHistory history = getItem(position);
			 
			 String msgType = history.getChat_ResponseUserType();
			 String dateTime1 = inspectionHistoryList.get(position).getChat_ResponseDate().split(" ")[1]+" "+inspectionHistoryList.get(position).getChat_ResponseDate().split(" ")[2];
				
			
			 //Setting date Time
			 vT_aiqbc_dateTimeText.setText(dateTime1);
				
			 //Seting Subject,message
			 vT_aiqbc_subject.setText(history.getHistory_subject());
			 vT_aiqbc_message.setText(history.getChat_message());
			 
			 //Setting manufacture Address Pending
			 String Wholeaddress = history.getmAddress()+","+history.getmCity()+","+history.getmStateName()+","+history.getmCountryName();
			 vT_aiqbc_manufactureName.setText(history.getHistory_Manufacturename());
			 vT_aiqbc_manufacture_address.setText(Wholeaddress);
			 
			 //Setting Inspection typr of industry pending
			 vT_aiqbc_inspection_typeofindustry.setText(history.getmTypeOfIndustry());
			 vT_aiqbc_inspector_type.setText(history.getmInspectorType());
			 vT_aiqbc_inspection_type.setText(history.getmInspectionType());
			 vT_aiqbc_bestprice.setText(history.getOffered_price());
			 if(history.getChat_UnitsOfMeasurement()==null || history.getChat_UnitsOfMeasurement().length()==0){
				 vT_aiqbc_bestprice_unit.setText("");
			 }else{
				 vT_aiqbc_bestprice_unit.setText("/"+history.getChat_UnitsOfMeasurement());
			 }
			 
			 mAttachment=inspectionHistoryList.get(position).getmAttachment();
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
					vT_aiqbc_acceptButton.setVisibility(View.GONE);
					vT_aiqbc_rejectButton.setVisibility(View.GONE);	
					vL_aiqbc_bestprice_layout.setVisibility(View.GONE);
					inspection_view.setVisibility(View.GONE);
				}else{
					vT_aiqbc_acceptButton.setVisibility(View.VISIBLE);
					vT_aiqbc_rejectButton.setVisibility(View.VISIBLE);	
					vL_aiqbc_bestprice_layout.setVisibility(View.VISIBLE);
					
					vT_aiqbc_placeto_inspectlayout.setVisibility(View.GONE);
					vL_aiqbc_additionalinfo_layout.setVisibility(View.GONE);
					
					placetoinspect_view.setVisibility(View.GONE);
					industry_view.setVisibility(View.GONE);
					inspector_view.setVisibility(View.GONE);
					inspection_view.setVisibility(View.GONE);
					
					vL_SDM_rootLayout.setBackground(getResources().getDrawable(R.drawable.chat_blue));
					vT_aiqbc_dateTimeText.setTextColor(getResources().getColor(R.color.datetime_blue));
				
					for(int i=0;i<inspectionHistoryList.size();i++){
						if(inspectionHistoryList.get(i).getChat_ResponseUserType().equalsIgnoreCase("2")){
							mFirstManufacturerPosition=i;
							Log.e("mFirstManufacturerPosition", ""+i);
							break;
						}else{
						}
					}
					
					if(position==mFirstManufacturerPosition){
						vT_aiqbc_acceptButton.setVisibility(View.VISIBLE);
						vT_aiqbc_rejectButton.setVisibility(View.VISIBLE);
					}else{
						vT_aiqbc_acceptButton.setVisibility(View.GONE);
						vT_aiqbc_rejectButton.setVisibility(View.GONE);
					}
					
					vT_aiqbc_acceptButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							getInspectionData();
							mType="Accept";
							mResponseUserType="1";
							mQuotationStatusId="5";
							new AsyncQuotationDetails().execute();
						}
					});
					
					vT_aiqbc_rejectButton.setOnClickListener(new View.OnClickListener() {
						
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
    
       //Creation Of Dialof For Buyer
       protected void createPopupDialog() {
   		AlertDialog.Builder builder = new AlertDialog.Builder(InspectionBuyerChatDetailsActivity.this);
   		final View view = getLayoutInflater().inflate(R.layout.activity_createquotation_inspection, null);
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
   		
   		//Layout
   		LinearLayout vT_acqi_popup_close = (LinearLayout) view.findViewById(R.id.vL_acqi_popup_close);
   		LinearLayout vI_acqi_productlayout = (LinearLayout) view.findViewById(R.id.vL_acqi_productlayout);
   		ImageView attach_document=(ImageView)view.findViewById(R.id.vI_acqi_attachfile);
   		attachmentList=(ListView)view.findViewById(R.id.vL_acqi_attachmentlist);
   		
   		vT_acqi_popup_close.setVisibility(View.VISIBLE);
   		vI_acqi_productlayout.setVisibility(View.GONE);
   		
   		//Imageview
   		ImageView vT_acqi_dialogClose = (ImageView) view.findViewById(R.id.vI_acqi_dialogClose);
   		
   		//EditText
   		final EditText subject;
		final EditText message;
		final EditText city;
		final EditText address;
		
		//Spinner
		Spinner InspectionTypeSpinner,IndustrySpinner,InspectorTypeSpinner;
		final Spinner StateSpinner;
		Spinner CountrySpinner;
		
		IndustrySpinner=(Spinner)view.findViewById(R.id.vS_acqi_industrytypeSpinner);
		InspectorTypeSpinner=(Spinner)view.findViewById(R.id.vS_acqi_inspectorSpinner);
		InspectionTypeSpinner=(Spinner)view.findViewById(R.id.vS_acqi_inspectionSpinner);
		
   		ImageView vI_acqs_send = (ImageView) view.findViewById(R.id.vI_acqi_sendquotation);
   		subject=(EditText) view.findViewById(R.id.vE_acqi_subject);
		message=(EditText) view.findViewById(R.id.vE_acqi_message);
		
		getInspectionData();
		
		subject.setText(""+inspectionHistoryListNew.get(0).getHistory_subject());
		mSubject=subject.getText().toString();
		message.setText(""+inspectionHistoryListNew.get(0).getChat_message());
		mUserMessage=message.getText().toString();
		
		city=(EditText)view.findViewById(R.id.vE_acqi_city);
		address=(EditText)view.findViewById(R.id.vE_acqi_address);
		
		city.setText(inspectionHistoryListNew.get(0).getmCity());
		mCity=city.getText().toString();
		address.setText(inspectionHistoryListNew.get(0).getmAddress());
		mAddress=address.getText().toString();
		
		CountrySpinner=(Spinner)view.findViewById(R.id.vS_acqi_countrySpinner);
		StateSpinner=(Spinner)view.findViewById(R.id.vS_acqi_stateSpinner);
		
		
		vT_acqi_dialogClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
			}
		});
   		
       
		subject.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mSubject=subject.getText().toString().trim();
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
				mUserMessage=message.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		
		city.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCity=city.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		address.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mAddress=address.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		IndustrySpinner.setAdapter(new SpinnerLookAdapter(getApplicationContext(), R.layout.spinner_view,mIndustryList));
		mSelectedIndustory=inspectionHistoryListNew.get(0).getmTypeOfIndustry();
		for(int i=0;i<mIndustryList.size();i++){
	            if(mIndustryList.get(i).getmName().equalsIgnoreCase(mSelectedIndustory)){
	               IndustryId=i;
	                break;
	            }
	        }
		
		 IndustrySpinner.setSelection(IndustryId);
		 IndustrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mSelectedIndustory=mIndustryList.get(position).getmName();
				mSelectedIndustoryId=mIndustryList.get(position).getmId();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		
		InspectorTypeSpinner.setAdapter(new SpinnerLookAdapter(getApplicationContext(), R.layout.spinner_view,mInspectorList));
		mSelectedInspectortype=inspectionHistoryListNew.get(0).getmInspectorType();
		for(int i=0;i<mInspectorList.size();i++){
	            if(mInspectorList.get(i).getmName().equalsIgnoreCase(mSelectedInspectortype)){
	               InspectorId=i;
	                break;
	            }
	        }
		
		InspectorTypeSpinner.setSelection(InspectorId);
		InspectorTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mSelectedInspectortype=mInspectorList.get(position).getmName();
				mSelectedInspectortypeId=mInspectorList.get(position).getmId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		
		InspectionTypeSpinner.setAdapter(new SpinnerLookAdapter(getApplicationContext(), R.layout.spinner_view,mInspectionTypes));
		mSelectedInspectionType=inspectionHistoryListNew.get(0).getmInspectionType();
		for(int i=0;i<mInspectionTypes.size();i++){
	            if(mInspectionTypes.get(i).getmName().equalsIgnoreCase(mSelectedInspectionType)){
	               InspectionId=i;
	                break;
	            }
	        }
		
		InspectionTypeSpinner.setSelection(InspectionId);
		InspectionTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mSelectedInspectionType=mInspectionTypes.get(position).getmName();
				mSelectedInspectionTypeId=mInspectionTypes.get(position).getmId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		
		CountrySpinner.setAdapter(new SpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mCountryList));
		mSelectedCountryName=inspectionHistoryListNew.get(0).getmCountryName();
		for(int i=0;i<mCountryList.size();i++){
            if(mCountryList.get(i).getmCountryName().equalsIgnoreCase(mSelectedCountryName)){
               countryId=i;
                break;
            }
        }
		mCountryId=inspectionHistoryListNew.get(0).getmCountryId();
		CountrySpinner.setSelection(countryId);
		CountrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mCountryId=mCountryList.get(position).getmCountryId();
				mSelectedCountryName=mCountryList.get(position).getmCountryName();
				getStatesList();
				StateSpinner.setAdapter(new StateSpinnerAdapter(getApplicationContext(),R.layout.spinner_view,mStateList));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		
		mSelectedStateName=inspectionHistoryListNew.get(0).getmStateName();
		for(int i=0;i<mStateList.size();i++){
            if(mStateList.get(i).getmStateName().equalsIgnoreCase(mSelectedStateName)){
               stateId=i;
                break;
            }
        }
		StateSpinner.setSelection(stateId);
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
		
	
   		
   		vI_acqs_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mResponseUserType = "1";
				mQuotationStatusId="2";
				new UpdateInspectionQuotation().execute();
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
				
			}
		});
   		
   		attach_document.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(InspectionBuyerChatDetailsActivity.this, FileExplorer.class);
				startActivityForResult(i, REQUEST_GET_SINGLE_FILE);
			}
		});
   		
       }
       
       //Spinner Adapter CreateDialogBox
       
       public class SpinnerLookAdapter extends ArrayAdapter<Lookups> {
   		TextView tv;
   		List<Lookups> mTsoList;
   		Context mContext;
   		int mResource = 0;
   		public SpinnerLookAdapter(Context context, int resource, List<Lookups> objects) {
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
       
       
       //Service Integration Update Quotation
       private class UpdateInspectionQuotation extends AsyncTask<Void, Void, Boolean>{

   		private ProgressDialog pDialog;
   		@Override
   		protected void onPreExecute() {
   			pDialog = new ProgressDialog(InspectionBuyerChatDetailsActivity.this);
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
   			 callInspectionUpdateQuotation();
   			return getInspectionQuotaionHistory();
   		}
   		
 
		@Override
   		protected void onPostExecute(Boolean result) {
   			if(pDialog != null && pDialog.isShowing()){
   				pDialog.cancel();
   			}
   			if(result){
   				getHistoryFromDbAndsetAdapter();
   				Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();	
   			}else{
   				Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
   			}
   			super.onPostExecute(result);
   		}
   	}
       
       
       private Boolean callInspectionUpdateQuotation() {
   		try{
   			String jsonData = null;
   			if(inspectionHistoryList != null && inspectionHistoryList.size() >0){
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
       
       
		private String createJson() {
			String json = null;
			try{
				Calendar cal=Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
				String mCurrentDate=sdf.format(cal.getTime());
				
				//String
				String mUserId = mUserDetails.getmUserID();
				JSONObject quotation=new JSONObject();
				JSONObject quotationObject=new JSONObject();
				
				if(attachimageUrl==null || attachimageUrl.length()==0){
					mAttachmentList = ""+mAttachment;
				}else{
					mAttachmentList = ""+mAttachment+","+attachimageUrl;
				}
				
				quotationObject.put("IsExternalProduct", "false");
				quotationObject.put("CreatedDate", mCurrentDate);
				quotationObject.put("LastResponseDate", mCurrentDate);
				quotationObject.put("QuotationStatusId", mQuotationStatusId);
				quotationObject.put("LastResponseSequenceNumber", inspectionHistoryList.get(0).getHistory_LastResponseSequenceNumber());
				quotationObject.put("LastManufacturerResponseId", inspectionHistoryList.get(0).getHistory_LastManufacturerResponseId());
				quotationObject.put("ServiceGroupType", 3);
				quotationObject.put("Id", inspectionHistoryList.get(0).getHistory_id());
				
				
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
				ChatObject.put("ResponseBy", mUserId);
				ChatObject.put("ResponseDate", mCurrentDate);
				ChatObject.put("OfferedPrize", "0");
				
				JSONObject InspectionAttirbutes=new JSONObject();
				/*InspectionAttirbutes.put("TypeOfIndustry", mSelectedIndustoryId);
				InspectionAttirbutes.put("InspectorType", mSelectedInspectortypeId);
				InspectionAttirbutes.put("InspectionType", mSelectedInspectionTypeId);*/
				
				JSONObject TypeOfIndustoryObject=new JSONObject();
				TypeOfIndustoryObject.put("Id", mSelectedIndustoryId);
				TypeOfIndustoryObject.put("Value", mSelectedIndustory);
				InspectionAttirbutes.put("TypeOfIndustry", TypeOfIndustoryObject);
				
				JSONObject InspectorTypeObject=new JSONObject();
				InspectorTypeObject.put("Id", mSelectedInspectortypeId);
				InspectorTypeObject.put("Value", mSelectedInspectortype);
				InspectionAttirbutes.put("InspectorType", InspectorTypeObject);
				
				JSONObject InspectionTypeObject=new JSONObject();
				InspectionTypeObject.put("Id", mSelectedInspectionTypeId);
				InspectionTypeObject.put("Value", mSelectedInspectionType);
				InspectionAttirbutes.put("InspectionType", InspectionTypeObject);
				
				JSONObject PlaceToInspectObject=new JSONObject();
				PlaceToInspectObject.put("City", mCity);
				PlaceToInspectObject.put("Address1", mAddress);
				PlaceToInspectObject.put("CountryName", mSelectedCountryName);
				PlaceToInspectObject.put("CountryId", mCountryId);
				PlaceToInspectObject.put("StateName", mSelectedStateName);
				PlaceToInspectObject.put("StateId", mSelectedStateId);
				InspectionAttirbutes.put("PlaceToInspect", PlaceToInspectObject);
				
				ChatObject.put("InspectionAttirbutes", InspectionAttirbutes);
				ChatData.put(ChatObject);
				quotationObject.put("ChatData", ChatData);
				quotation.put("quotation", quotationObject);
				
				json = quotation.toString();
				Log.e("Save Quotatiion", json);
				}catch(Exception e){
					Log.e("SAVE QUOTATION", "ERROR IN CREATING JSON");
					e.printStackTrace();
				}
	
			return json;
	}



		public Boolean getInspectionQuotaionHistory() {
			Boolean isValidHistory = false;
			String quoatation_id = inspectionHistoryList.get(0).getHistory_id();
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
						Helper helper = new Helper(InspectionBuyerChatDetailsActivity.this);
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
		 
	 private Boolean UpdateQuotation() {
				webServices=new WebServices();
				jsonParserClass=new JsonParserClass();
				try{
					String jsonData = null;
					getInspectionData();
					if(inspectionHistoryList != null && inspectionHistoryList.size() >0){
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
		 
		 private void getInspectionData() {
			 Helper helper=new Helper(getApplicationContext());
			 helper.openDataBase();
			 inspectionHistoryListNew=helper.getAllDashboardQuotationHistory("1");
			 helper.close();
		}


		private String createUpdateJson() {
			String json = null;
			try{
				Calendar cal=Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
				String mCurrentDate=sdf.format(cal.getTime());
				
				//String
				String mUserId = mUserDetails.getmUserID();
				JSONObject quotation=new JSONObject();
				JSONObject quotationObject=new JSONObject();
				
				if(attachimageUrl==null || attachimageUrl.length()==0){
					mAttachmentList = ""+mAttachment;
				}else{
					mAttachmentList = ""+mAttachment+","+attachimageUrl;
				}
				
				quotationObject.put("IsExternalProduct", "false");
				quotationObject.put("CreatedDate", mCurrentDate);
				quotationObject.put("LastResponseDate", mCurrentDate);
				quotationObject.put("QuotationStatusId", mQuotationStatusId);
				quotationObject.put("LastResponseSequenceNumber", inspectionHistoryList.get(0).getHistory_LastResponseSequenceNumber());
				quotationObject.put("LastManufacturerResponseId", inspectionHistoryList.get(0).getHistory_LastManufacturerResponseId());
				quotationObject.put("ServiceGroupType", 3);
				quotationObject.put("Id", inspectionHistoryList.get(0).getHistory_id());
				
				
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
				ChatObject.put("Message", inspectionHistoryListNew.get(0).getChat_message());
				ChatObject.put("ResponseUserType", mResponseUserType);
				ChatObject.put("Attachments", inspectionHistoryListNew.get(0).getmAttachment());
				ChatObject.put("ResponseBy", mUserId);
				ChatObject.put("ResponseDate", mCurrentDate);
				ChatObject.put("OfferedPrize", inspectionHistoryListNew.get(0).getOffered_price());
				
				JSONObject InspectionAttirbutes=new JSONObject();
				/*InspectionAttirbutes.put("TypeOfIndustry", mSelectedIndustoryId);
				InspectionAttirbutes.put("InspectorType", mSelectedInspectortypeId);
				InspectionAttirbutes.put("InspectionType", mSelectedInspectionTypeId);*/
				
				JSONObject TypeOfIndustoryObject=new JSONObject();
				TypeOfIndustoryObject.put("Id", inspectionHistoryListNew.get(0).getmTypeOfIndustryId());
				TypeOfIndustoryObject.put("Value", inspectionHistoryListNew.get(0).getmTypeOfIndustry());
				InspectionAttirbutes.put("TypeOfIndustry", TypeOfIndustoryObject);
				
				JSONObject InspectorTypeObject=new JSONObject();
				InspectorTypeObject.put("Id", inspectionHistoryListNew.get(0).getmInspectorTypeId());
				InspectorTypeObject.put("Value", inspectionHistoryListNew.get(0).getmInspectorType());
				InspectionAttirbutes.put("InspectorType", InspectorTypeObject);
				
				JSONObject InspectionTypeObject=new JSONObject();
				InspectionTypeObject.put("Id", inspectionHistoryListNew.get(0).getmInspectionTypeId());
				InspectionTypeObject.put("Value", inspectionHistoryListNew.get(0).getmInspectionType());
				InspectionAttirbutes.put("InspectionType", InspectionTypeObject);
				
				JSONObject PlaceToInspectObject=new JSONObject();
				PlaceToInspectObject.put("City", inspectionHistoryListNew.get(0).getmCity());
				PlaceToInspectObject.put("Address1", inspectionHistoryListNew.get(0).getmAddress());
				PlaceToInspectObject.put("CountryName", inspectionHistoryListNew.get(0).getmCountryName());
				PlaceToInspectObject.put("CountryId", inspectionHistoryListNew.get(0).getmCountryId());
				PlaceToInspectObject.put("StateName", inspectionHistoryListNew.get(0).getmStateName());
				PlaceToInspectObject.put("StateId", inspectionHistoryListNew.get(0).getmStateId());
				InspectionAttirbutes.put("PlaceToInspect", PlaceToInspectObject);
				
				ChatObject.put("InspectionAttirbutes", InspectionAttirbutes);
				ChatData.put(ChatObject);
				quotationObject.put("ChatData", ChatData);
				quotation.put("quotation", quotationObject);
				
				json = quotation.toString();
				Log.e("Save Quotatiion", json);
				}catch(Exception e){
					Log.e("SAVE QUOTATION", "ERROR IN CREATING JSON");
					e.printStackTrace();
				}
	
			return json;
		 }
		 
		 public Boolean getQuotaionHistory() {
				Boolean isValidHistory = false;
				String quoatation_id = inspectionHistoryList.get(0).getHistory_id();
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
							Helper helper = new Helper(InspectionBuyerChatDetailsActivity.this);
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
		 
		 private Boolean callQuotationOrderService() {
				Boolean isValid = false;
				webServices = new WebServices();
				jsonParserClass = new JsonParserClass();
				String Inparam = inspectionHistoryList.get(0).getHistory_id();
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

		 
		 private class AsyncQuotationDetails extends AsyncTask<Void, Void, Boolean>{
			 
			 @Override
			 protected void onPreExecute() {
				 pdLoading = new ProgressDialog(InspectionBuyerChatDetailsActivity.this);
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
						Intent inspectionIntent=new Intent(InspectionBuyerChatDetailsActivity.this,PlaceOrderInspectionActivity.class);
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
