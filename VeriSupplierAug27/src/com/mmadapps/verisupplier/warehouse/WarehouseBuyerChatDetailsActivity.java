package com.mmadapps.verisupplier.warehouse;

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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.QuotationDashboardHistory;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.TwoWayView;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.fileattachment.FileExplorer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class WarehouseBuyerChatDetailsActivity extends BaseActionBarActivity{
	
	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
		
	//from database
	UserDetails mUserDetails;
	ArrayList<QuotationDashboardHistory> warehouseHistoryList;
	ArrayList<QuotationDashboardHistory> warehouseHistoryListNew;
	static ArrayList<String> arrayList = new ArrayList<String>();
		
	//webservices
	WebServices webServices;
	JsonParserClass jsonParserClass;
	
	//Buyerdetails
	ImageView vI_acqbcd_productImage;
	ListView vI_acqbcd_messageLists;
	TextView vT_acqbcd_productName;
	ImageView vI_acqbcd_createQuotation;
	
	String quoatation_id;
	String additionalinfo;
	String mResponseUserType;
	String mQuotationStatusId;
	
	static ArrayList<String> attachList = new ArrayList<String>();	
	
	String mUserMessage;
	String mUserSubject;
	String mWidth;
	String mLenght;
	String mHeight;
	String mCity;
	
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
	
	String mType;
	ProgressDialog pdLoading=null;
	
	int mFirstManufacturerPosition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warehousequotation_buyerdetails);
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
		mUserDetails=helper.getUserDetails();
		helper.close();
	}

	private void initializeView() {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(WarehouseBuyerChatDetailsActivity.this));
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
		
		vI_acqbcd_productImage = (ImageView) findViewById(R.id.vI_awhbcd_productImage);
		vT_acqbcd_productName = (TextView) findViewById(R.id.vT_awhbcd_productName);
		vI_acqbcd_messageLists = (ListView) findViewById(R.id.vI_awhbcd_messageLists);
		vI_acqbcd_createQuotation = (ImageView) findViewById(R.id.vI_awhbcd_createQuotation);
		
		vI_acqbcd_createQuotation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				arrayList = new ArrayList<String>();
				attachList=new  ArrayList<String>();
				createPopupDialog(additionalinfo);
			}
		});
		
		getHistoryFromDbAndsetAdapter();
	}

	private void getHistoryFromDbAndsetAdapter() {
		Helper helper = new Helper(WarehouseBuyerChatDetailsActivity.this);
		helper.openDataBase();
		warehouseHistoryList = helper.getAllDashboardQuotationHistory(null);
		helper.close();
		
		if(warehouseHistoryList == null || warehouseHistoryList.size() == 0){
		}else{
			QuortationHistoryAdapter adapter1 = new QuortationHistoryAdapter(warehouseHistoryList);
			quoatation_id= warehouseHistoryList.get(0).getHistory_id();
			String contactPersonPicUrl = warehouseHistoryList.get(0).getHistory_Buyerimage();
			imageLoader.displayImage(contactPersonPicUrl, BaseActionBarActivity.vBuyerImage, options);
			
			BaseActionBarActivity.setmUserSubTitle(""+warehouseHistoryList.get(0).getHistory_Manufacturename());
			BaseActionBarActivity.setmUserName(""+warehouseHistoryList.get(0).getHistory_productname());
			
			vT_acqbcd_productName.setText(""+warehouseHistoryList.get(0).getHistory_productname());
			imageLoader.displayImage(warehouseHistoryList.get(0).getHistory_productimage(), vI_acqbcd_productImage, options);
			
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
				convertView = inflater.inflate(R.layout.adapter_warehousequotation_buyerchat, parent, false);
			}
			LinearLayout main_layout = (LinearLayout) convertView.findViewById(R.id.vL_awhqbc_rootLayout);
			LinearLayout additionalinfo_layout=(LinearLayout)convertView.findViewById(R.id.vT_awhqbc_arearequirement_layout);
			LinearLayout bestprice_layout=(LinearLayout)convertView.findViewById(R.id.vL_awhqbc_bestprice_layout);
			LinearLayout document_layout=(LinearLayout)convertView.findViewById(R.id.vL_awhqbc_attachmentlist_layout);
			
			TextView dateTime = (TextView) convertView.findViewById(R.id.vT_awhqbc_dateTimeText);
			TextView subject = (TextView) convertView.findViewById(R.id.vT_awhqbc_subject);
			TextView message_text=(TextView)convertView.findViewById(R.id.vT_awhqbc_message);
			TextView measurement=(TextView)convertView.findViewById(R.id.vT_awhqbc_size);
			TextView location=(TextView)convertView.findViewById(R.id.vT_awhqbc_location);
			
			TextView bestprice=(TextView)convertView.findViewById(R.id.VT_awhqbc_bestprice);
			TextView price_unit=(TextView)convertView.findViewById(R.id.VT_awhqbc_bestprice_unit);
			TextView accept=(TextView)convertView.findViewById(R.id.vT_awhqbc_acceptButton);
			TextView reject=(TextView)convertView.findViewById(R.id.vT_awhqbc_rejectButton);
			document_list=(TwoWayView)convertView.findViewById(R.id.vL_awhqbc_documentlist);
			View attachment_view=(View)convertView.findViewById(R.id.view_awhqbc_attachmentview);
			View arearequirement_view=(View)convertView.findViewById(R.id.view_awhqbc_arearequirement_view);
			View location_view=(View)convertView.findViewById(R.id.view_awhqbc_locationview);
			
			
            QuotationDashboardHistory history = getItem(position);
			
            additionalinfo= history.getmAdditionalInfo();
			String msgType = history.getChat_ResponseUserType();
			String dateTime1 = warehouseHistoryList.get(position).getChat_ResponseDate().split(" ")[1]+" "+warehouseHistoryList.get(position).getChat_ResponseDate().split(" ")[2];
			
			dateTime.setText(dateTime1);
			subject.setText(""+warehouseHistoryList.get(position).getHistory_subject());
			message_text.setText(""+warehouseHistoryList.get(position).getChat_message());
			measurement.setText(""+warehouseHistoryList.get(position).getmLength()+"x"+warehouseHistoryList.get(position).getmWidth()+"x"+warehouseHistoryList.get(position).getmHeight());
			location.setText(""+warehouseHistoryList.get(position).getmCity());
			bestprice.setText(""+warehouseHistoryList.get(position).getOffered_price());
			if(warehouseHistoryList.get(position).getChat_UnitsOfMeasurement()==null || warehouseHistoryList.get(position).getChat_UnitsOfMeasurement().length()==0){
				price_unit.setText("");
			}else{
				price_unit.setText("/"+warehouseHistoryList.get(position).getChat_UnitsOfMeasurement());
			}
		
			
			mAttachment=warehouseHistoryList.get(position).getmAttachment();
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
				additionalinfo_layout.setVisibility(View.VISIBLE);
				bestprice_layout.setVisibility(View.GONE);
				accept.setVisibility(View.GONE);
				reject.setVisibility(View.GONE);
				location_view.setVisibility(View.GONE);
			}else{
				additionalinfo_layout.setVisibility(View.GONE);
				bestprice_layout.setVisibility(View.VISIBLE);
				accept.setVisibility(View.VISIBLE);
				reject.setVisibility(View.VISIBLE);
				
				arearequirement_view.setVisibility(View.GONE);
				location_view.setVisibility(View.GONE);
				
				main_layout.setBackground(getResources().getDrawable(R.drawable.chat_blue));
				dateTime.setTextColor(getResources().getColor(R.color.datetime_blue));
				
				for(int i=0;i<warehouseHistoryList.size();i++){
					if(warehouseHistoryList.get(i).getChat_ResponseUserType().equalsIgnoreCase("2")){
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
						getWareHouseData();
						
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
		AlertDialog.Builder builder = new AlertDialog.Builder(WarehouseBuyerChatDetailsActivity.this);
		final View view = getLayoutInflater().inflate(R.layout.activity_createquotation_warehouse, null);
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
		LinearLayout product_layout=(LinearLayout)view.findViewById(R.id.vL_acqwh_productlayout);
		product_layout.setVisibility(View.GONE);
		LinearLayout popup_close=(LinearLayout)view.findViewById(R.id.vL_acqwh_popup_close);
		popup_close.setVisibility(View.VISIBLE);
		
		ImageView vI_acqs_dialogClose=(ImageView)view.findViewById(R.id.vI_acqwh_dialogClose);
		ImageView vI_acqs_send=(ImageView)view.findViewById(R.id.vI_acqwh_sendquotation);
		ImageView vI_acqs_attachfile=(ImageView)view.findViewById(R.id.vI_acqwh_attachfile);
		
		final EditText subject=(EditText)view.findViewById(R.id.vE_acqwh_subject);
		final EditText message=(EditText)view.findViewById(R.id.vE_acqwh_message);
		final EditText length=(EditText)view.findViewById(R.id.vE_acqwh_length);
		final EditText width=(EditText)view.findViewById(R.id.vE_acqwh_width);
		final EditText height=(EditText)view.findViewById(R.id.vE_acqwh_height);
		final EditText city=(EditText)view.findViewById(R.id.vE_acqwh_city);
		attachmentList=(ListView)view.findViewById(R.id.vL_acqwh_attachments);
		
		getWareHouseData();
		
		subject.setText(""+warehouseHistoryListNew.get(0).getHistory_subject());
		mUserSubject=subject.getText().toString();
		subject.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mUserSubject=subject.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		message.setText(""+warehouseHistoryListNew.get(0).getChat_message());
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
		
		length.setText(""+warehouseHistoryListNew.get(0).getmLength());
		mLenght=length.getText().toString();
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
		
		width.setText(""+warehouseHistoryListNew.get(0).getmWidth());
		mWidth=width.getText().toString();
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
		
		height.setText(""+warehouseHistoryListNew.get(0).getmHeight());
		mHeight=height.getText().toString().trim();
		height.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mHeight=height.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		city.setText(""+warehouseHistoryListNew.get(0).getmCity());
		mCity=city.getText().toString();
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
		
		
		vI_acqs_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mUserMessage==null || mUserMessage.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the message", Toast.LENGTH_SHORT).show();
				}else if(mCity==null || mCity.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the city", Toast.LENGTH_SHORT).show();
				}else if(mLenght==null || mLenght.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the length", Toast.LENGTH_SHORT).show();
				}else if(mWidth==null || mWidth.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the width", Toast.LENGTH_SHORT).show();
				}else if(mHeight==null || mHeight.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the height", Toast.LENGTH_SHORT).show();
				}
				else{
					mResponseUserType = "1";
					mQuotationStatusId="2";
					new UpdateQuotation().execute();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				}
			}
		});
		
		vI_acqs_attachfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(WarehouseBuyerChatDetailsActivity.this, FileExplorer.class);
				startActivityForResult(i, REQUEST_GET_SINGLE_FILE);
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
	
	private void getWareHouseData() {
		Helper helper=new Helper(getApplicationContext());
		 helper.openDataBase();
		 warehouseHistoryListNew=helper.getAllDashboardQuotationHistory("1");
		 helper.close();
	}

	private class UpdateQuotation extends AsyncTask<Void, Void, Boolean>{

		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(WarehouseBuyerChatDetailsActivity.this);
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
				Toast.makeText(WarehouseBuyerChatDetailsActivity.this, "success", Toast.LENGTH_SHORT).show();
			
			}else{
				Toast.makeText(WarehouseBuyerChatDetailsActivity.this, "failure", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}
	
	public Boolean callUpdateQuotation() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		try{
			String jsonData = null;
			if(warehouseHistoryList != null && warehouseHistoryList.size() >0){
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
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
			Date today = new Date();
			String date = sdf.format(today);
			//mUserSubject = shippingHistoryList.get(0).getHistory_subject();
			JSONObject jsn = new JSONObject();
			JSONObject quotationJson = new JSONObject();
			JSONArray chatArray = new JSONArray();
			JSONObject chatObject = new JSONObject();
			
			if(attachimageUrl==null || attachimageUrl.length()==0){
				mAttachmentList = ""+mAttachment;
			}else{
				mAttachmentList = ""+mAttachment+","+attachimageUrl;
			}
			
			JSONObject WareHouseAttributes=new JSONObject();
			JSONObject AreaRequirement=new JSONObject();
			AreaRequirement.put("Length", mLenght);
			AreaRequirement.put("Width", mWidth);
			AreaRequirement.put("Height", mHeight);
			AreaRequirement.put("Weight", "0");
			AreaRequirement.put("UnitsOfMeasureMent1", "0");
			//AreaRequirement.put("UnitsOfMeasureMent2", "0");
			WareHouseAttributes.put("AreaRequirement", AreaRequirement);
			
			JSONObject WareHouseAddress=new JSONObject();
			WareHouseAddress.put("City", mCity);
			WareHouseAttributes.put("WareHouseAddress", WareHouseAddress);
			
			chatObject.put("WareHouseAttributes", WareHouseAttributes);
			
			chatObject.put("Message", mUserMessage);
			chatObject.put("ResponseUserType", ""+mResponseUserType);
			chatObject.put("ResponseDate", ""+date);
			chatObject.put("UnitsOfMeasurement", "");
			//chatObject.put("RequiredQty", ""+mQuantity);
			//chatObject.put("AdditionalInfo", "");
			chatObject.put("QuotationResponseId", 2);//quotationHistoryList.get(0).getHistory_QuotationResponseId());
			chatObject.put("Attachments", mAttachmentList);//https://vsupplierstorage.blob.core.windows.net/images/Xperia.jpg
			chatArray.put(chatObject);
			
			// quotation
			quotationJson.put("LastResponseDate", ""+date);
			quotationJson.put("Subject", mUserSubject);
			quotationJson.put("Id", ""+warehouseHistoryList.get(0).getHistory_id());
			quotationJson.put("QuotationStatusId", mQuotationStatusId);
			quotationJson.put("LastResponseSequenceNumber", ""+warehouseHistoryList.get(0).getHistory_LastResponseSequenceNumber());
			quotationJson.put("LastManufacturerResponseId", ""+warehouseHistoryList.get(0).getHistory_LastManufacturerResponseId());
			quotationJson.put("ServiceGroupType", 7);
			
			quotationJson.put("ChatData", chatArray);
			
			jsn.put("quotation", quotationJson);
			json = jsn.toString();
			Log.e("Json", ""+json);
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
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
		String quoatation_id = warehouseHistoryList.get(0).getHistory_id();
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
					Helper helper = new Helper(WarehouseBuyerChatDetailsActivity.this);
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
	 
	
	 
	 private Boolean UpdateQuotation() {
			webServices=new WebServices();
			jsonParserClass=new JsonParserClass();
			try{
				String jsonData = null;
				getWareHouseData();
				if(warehouseHistoryListNew != null && warehouseHistoryListNew.size() >0){
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
				
				if(attachimageUrl==null || attachimageUrl.length()==0){
					mAttachmentList = ""+mAttachment;
				}else{
					mAttachmentList = ""+mAttachment+","+attachimageUrl;
				}
				
				JSONObject WareHouseAttributes=new JSONObject();
				JSONObject AreaRequirement=new JSONObject();
				AreaRequirement.put("Length", warehouseHistoryListNew.get(0).getmLength());
				AreaRequirement.put("Width", warehouseHistoryListNew.get(0).getmWidth());
				AreaRequirement.put("Height", warehouseHistoryListNew.get(0).getmHeight());
				AreaRequirement.put("Weight", "0");
				AreaRequirement.put("UnitsOfMeasureMent1", "0");
				//AreaRequirement.put("UnitsOfMeasureMent2", "0");
				WareHouseAttributes.put("AreaRequirement", AreaRequirement);
				
				JSONObject WareHouseAddress=new JSONObject();
				WareHouseAddress.put("City", warehouseHistoryListNew.get(0).getmCity());
				WareHouseAttributes.put("WareHouseAddress", WareHouseAddress);
				
				chatObject.put("WareHouseAttributes", WareHouseAttributes);
				
				chatObject.put("Message", warehouseHistoryListNew.get(0).getChat_message());
				chatObject.put("ResponseUserType", ""+mResponseUserType);
				chatObject.put("ResponseDate", ""+date);
				chatObject.put("UnitsOfMeasurement", warehouseHistoryListNew.get(0).getChat_UnitsOfMeasurement());
				//chatObject.put("RequiredQty", ""+mQuantity);
				//chatObject.put("AdditionalInfo", "");
				chatObject.put("QuotationResponseId", 2);//quotationHistoryList.get(0).getHistory_QuotationResponseId());
				chatObject.put("Attachments", mAttachmentList);//https://vsupplierstorage.blob.core.windows.net/images/Xperia.jpg
				chatArray.put(chatObject);
				
				// quotation
				quotationJson.put("LastResponseDate", ""+date);
				quotationJson.put("Subject", mUserSubject);
				quotationJson.put("Id", warehouseHistoryList.get(0).getHistory_id());
				quotationJson.put("QuotationStatusId", mQuotationStatusId);
				quotationJson.put("LastResponseSequenceNumber", ""+warehouseHistoryList.get(0).getHistory_LastResponseSequenceNumber());
				quotationJson.put("LastManufacturerResponseId", ""+warehouseHistoryList.get(0).getHistory_LastManufacturerResponseId());
				quotationJson.put("ServiceGroupType", VerisupplierUtils.mWarehouseParam);
				
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
			String Inparam = warehouseHistoryList.get(0).getHistory_id();
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
				pdLoading = new ProgressDialog(WarehouseBuyerChatDetailsActivity.this);
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
					Intent placeOrderIntent=new Intent(WarehouseBuyerChatDetailsActivity.this,PlaceOrderWarehouseActivity.class);
					placeOrderIntent.putExtra("FROMACTIVITY", "dashboard");
					startActivity(placeOrderIntent);
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
	
	

}
