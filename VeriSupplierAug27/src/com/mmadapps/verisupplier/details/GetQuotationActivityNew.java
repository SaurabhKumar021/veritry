package com.mmadapps.verisupplier.details;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.MyGridView;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.fileattachment.FileExplorer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GetQuotationActivityNew extends BaseActionBarActivity implements OnClickListener{
	
	String mFromActivity="";
	boolean isGetPrice = false;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	TextView toaddress,getquotation_text;
	EditText message,quantity,subject;
	LinearLayout getquotation_layout,getprice_checkbox;
	
	//quantity variables
	Spinner quantityunit_spinner;
	
	//attachment variables
	LinearLayout attachment_layout;
	private static final int REQUEST_GET_SINGLE_FILE = 0;
	ListView attachmentList;
	AttachmentListAdapter mAttachmentAdapter;
	static ArrayList<String> attachList = new ArrayList<String>();
	
	//optionaldetails variables
	LinearLayout pluslayout,optionaldetails_layout;
	MyGridView mOptionalGridView;
	List<Lookups> checkOptionalDetail=new ArrayList<Lookups>();
	ImageView minus_icon;
	
	//fromdatabase variables
	UserDetails mUserDetails=new UserDetails();
	List<ManufacturerDetails> mManufacturerDetails=new ArrayList<ManufacturerDetails>();
	List<ProductDetails> mProductDetails=new ArrayList<ProductDetails>();
	
	//from utils
	List<Lookups> mQuantityList=new ArrayList<Lookups>();
	
	//setvalues variables
	String minqty="";
	String qtyUnit="";
	String mUserSubject;
	String mUserMessage;
	String mEditedQuantity;
	
	//servicecall
	ProgressDialog pdLoading=null;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	String[] mResult;
	String mQuotationNumber="";
	View attachmentView,optionalDeatilView;
	String mSelectedQuantity;
	String mSelectedQuantityId;
	String mOriginalQuantity;
	String mQuotationStatusId;
	String mOriginalQuantityUnit;
	int mOriginalQuantityUnitId;
	
	//Attach URL
	String attachimageUrl="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getquotation);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		
		mFromActivity=getIntent().getStringExtra("FROMACTIVITY");
		if(mFromActivity == null || mFromActivity.length() == 0){
			isGetPrice = false;
		}else if(mFromActivity.equalsIgnoreCase("getprice")){
			isGetPrice = true;
			BaseActionBarActivity.setmUserName("Get Price");
			mQuotationStatusId="6";
		}else{
			isGetPrice = false;
			BaseActionBarActivity.setmUserName("Get Quotation");
			mQuotationStatusId="2";
		}
		getUserDetails();
		getManufacturerDetails();
		initializeView();
	}

	private void getUserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}

	private void getManufacturerDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mManufacturerDetails=helper.getManufacturerDetails(ProductDetailsActivity.mManufacturerId);
		mProductDetails=helper.getAllProductDetails(ProductDetailsActivity.mProductId);
		helper.close();
	}

	private void initializeView() {
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
			e.printStackTrace();
		}
		
		toaddress=(TextView)findViewById(R.id.vT_agq_toaddress);
		subject=(EditText)findViewById(R.id.vE_agq_subject);
		message=(EditText)findViewById(R.id.vE_agq_message);
		quantity=(EditText)findViewById(R.id.vE_agq_quantity);
		
		quantityunit_spinner=(Spinner)findViewById(R.id.agq_getqua_spinner);
		attachment_layout=(LinearLayout)findViewById(R.id.vL_agq_attachmentlayout);
		attachment_layout.setOnClickListener(this);
		attachmentList=(ListView)findViewById(R.id.vL_agq_attachmentlist);
		
		optionaldetails_layout=(LinearLayout)findViewById(R.id.vL_agq_optionaldetaillayout);
		optionaldetails_layout.setOnClickListener(this);
		pluslayout=(LinearLayout)findViewById(R.id.vL_agq_pluslayout);
		mOptionalGridView=(MyGridView)findViewById(R.id.vG_agq_optionalGrid);
		mOptionalGridView.setAdapter(new OptionalGridAdapterClass());
		minus_icon=(ImageView)findViewById(R.id.vI_agq_optional_minus);
		
		getquotation_layout=(LinearLayout)findViewById(R.id.vL_agq_getquotation);
		getquotation_layout.setOnClickListener(this);
		getquotation_text=(TextView)findViewById(R.id.vT_agq_getquotation_text);
		
		getprice_checkbox=(LinearLayout)findViewById(R.id.vL_agq_checkboxgetprice);
		attachmentView=(View)findViewById(R.id.agq_attachmentview);
		optionalDeatilView=(View)findViewById(R.id.agq_optionaldetailsview);
		
		if(isGetPrice){
			getprice_checkbox.setVisibility(View.GONE);
			attachment_layout.setVisibility(View.GONE);
			optionaldetails_layout.setVisibility(View.GONE);
			pluslayout.setVisibility(View.GONE);
			attachmentView.setVisibility(View.GONE);
			optionalDeatilView.setVisibility(View.GONE);
			getquotation_text.setText("Get Price");
		}else{
			attachmentView.setVisibility(View.GONE);
		}
		
		setValues();
	}

	private void setValues() {
		if(ProductDetailsActivity.mSavePrequotationValues==null || ProductDetailsActivity.mSavePrequotationValues.size()==0){
		}else if(ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_moq()==null || ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_moq().length()==0){
			minqty="0";
			qtyUnit="0";
			mOriginalQuantity=minqty;
			mOriginalQuantityUnit=qtyUnit;
		}else{
			String[] qty=ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_moq().split(" ");
			if(qty.length>1){
				minqty=qty[0];
				qtyUnit=qty[1];
				mOriginalQuantity=minqty;
				mOriginalQuantityUnit=qtyUnit;
			}else{
				minqty="0";
				mOriginalQuantity=minqty;
			}
		}
		
		if(mManufacturerDetails==null ||mManufacturerDetails.size()==0){
		}else{
			toaddress.setText(""+mManufacturerDetails.get(0).getmManufacturerName());
			String sub = "Request for quote "+ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Name();//+" from "+mManufacturerDetails.get(0).getmManufacturerName();
			subject.setText(""+sub);
			mUserSubject=subject.getText().toString().trim();
		}
		quantity.setText(""+minqty);
		mSelectedQuantity=qtyUnit;
		mQuantityList=VerisupplierUtils.mQuantityList;
		quantityunit_spinner.setAdapter(new SpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mQuantityList));
		
		for(int i=0;i<mQuantityList.size();i++){
			if(mQuantityList.get(i).getmName().equalsIgnoreCase(mOriginalQuantityUnit)){
				mOriginalQuantityUnitId=i;
				break;
			}
		}
		
		quantityunit_spinner.setSelection(mOriginalQuantityUnitId);
		quantityunit_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mSelectedQuantity=mQuantityList.get(position).getmName();
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
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
		
		quantity.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				minqty=quantity.getText().toString();
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
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {			
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	
	private void setAttchmentListAdapter() {
		if(attachList.size()==0){
			attachmentView.setVisibility(View.GONE);
		}else{
			attachmentView.setVisibility(View.VISIBLE);
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
           Size=attachList.size();
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount())))+listAdapter.getCount()+Size;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


	public class SpinnerAdapter extends ArrayAdapter<Lookups> {
		TextView tv;
		List<Lookups> mTsoList;
		Context mContext;
		int mResource = 0;
		public SpinnerAdapter(Context context, int resource, List<Lookups> objects) {
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
	
	private class OptionalGridAdapterClass extends BaseAdapter{

		@Override
		public int getCount() {
			if(VerisupplierUtils.mQuotationLookups == null || VerisupplierUtils.mQuotationLookups.size()==0){
				return 0;
			}else{
				return VerisupplierUtils.mQuotationLookups.size();
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
			if(convertView==null){
				convertView =getLayoutInflater().inflate(R.layout.adapter_lookups, parent, false);
			}
			CheckBox optionalDetails = (CheckBox) convertView.findViewById(R.id.vC_OptionalDetail);
			final Lookups mLookups = VerisupplierUtils.mQuotationLookups.get(position);
			
			optionalDetails.setText(""+mLookups.getmName());
			
			optionalDetails.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						if(checkOptionalDetail.contains(mLookups)){
						}else{
							checkOptionalDetail.add(mLookups);
						}
					}else{
						if(checkOptionalDetail.contains(mLookups)){
							checkOptionalDetail.remove(mLookups);
						}
					}
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
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vL_agq_attachmentlayout:
			Intent i = new Intent(GetQuotationActivityNew.this, FileExplorer.class);
			startActivityForResult(i, REQUEST_GET_SINGLE_FILE);
			break;
			
		case R.id.vL_agq_optionaldetaillayout:
			if(pluslayout.isShown()){
				pluslayout.setVisibility(View.GONE);
				minus_icon.setImageResource(R.raw.plus_symbol);
			}else{
				pluslayout.setVisibility(View.VISIBLE);
				minus_icon.setImageResource(R.raw.minus_icon);
			}
			break;
			
		case R.id.vL_agq_getquotation:
			if(message.getText().toString().trim()==null || message.getText().toString().trim().length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the message", Toast.LENGTH_SHORT).show();
			}else if(quantity.getText().toString().trim()==null || quantity.getText().toString().trim().length()==0){
				Toast.makeText(getApplicationContext(), "Please enter quantity", Toast.LENGTH_SHORT).show();
			}else{
				if(Integer.parseInt(mOriginalQuantity)>Integer.parseInt(minqty)){
					Toast.makeText(getApplicationContext(), "Quantity should not be less than product minimum quantity", Toast.LENGTH_LONG).show();
				}else{
					getUserDetails();
					new AsyncGetQuotationService().execute();
				}
			}
			break;

		default:
			break;
		}
	}
	
	private class AsyncGetQuotationService extends AsyncTask<Void, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(GetQuotationActivityNew.this);
			pdLoading.setMessage("please wait...");
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
			return callgetGetQuotationservice();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pdLoading.cancel();
			if(pdLoading.isShowing())
				pdLoading.dismiss();
			if(result){
				if(mFromActivity.equalsIgnoreCase("getprice")){
					Toast.makeText(getApplicationContext(), "Thanks for creating request with us.", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(), "Quote request successfully submited.Quotation Number "+mQuotationNumber, Toast.LENGTH_LONG).show();
				}
				
				finish();
			}else{
				if(mFromActivity.equalsIgnoreCase("getquotation")){
					Toast.makeText(getApplicationContext(), "request failed Please try again.", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(), "Quote request failed Please try again.", Toast.LENGTH_LONG).show();
				}
				
			}
          attachList= new ArrayList<String>();
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
			url =webServices.FILEUPLOAD+"/"+mFileName;
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
		    		bos.write(buf, 0, readNum);
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
		    reqEntity.setChunked(true); 
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
	
	private Boolean callgetGetQuotationservice() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();  
			String postURL = webServices.SaveQuotation;
			InputStream inputStream = null;
			HttpPost httpPost = new HttpPost(postURL);
			String json = ""; 
	
			Calendar cal=Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
			String mCurrentDate=sdf.format(cal.getTime());
			System.out.println(""+mCurrentDate);
			String mUserId = mUserDetails.getmUserID();
			String mProductId = ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Id();
			String mProductName = ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Name();
			String mManufacturerId = mManufacturerDetails.get(0).getmManufactureID();
			String mManufacturerName = mManufacturerDetails.get(0).getmManufacturerName();
			String mAttachment = attachimageUrl;
			try{
				JSONObject jsonObject = new JSONObject();
				JSONObject quotation = new JSONObject();
				
				JSONObject Buyer = new JSONObject();
				Buyer.put("Id", ""+mUserId);
				
				JSONObject Manufacturer = new JSONObject();
				Manufacturer.put("Id", mManufacturerId);
				Manufacturer.put("Name", ""+mManufacturerName);
				
				JSONObject Product = new JSONObject();
				Product.put("Id", ""+mProductId);
				Product.put("ProductName", ""+mProductName);
				
				JSONArray ChatData =  new JSONArray();
				JSONObject CDObj = new JSONObject();
				CDObj.put("RequiredQty", minqty); 
				CDObj.put("Message", mUserMessage);
				CDObj.put("UnitsOfMeasurement", mSelectedQuantity);
				CDObj.put("ResponseDate", ""+mCurrentDate);
				CDObj.put("ResponseUserType", "1");
				CDObj.put("ResponseBy",mUserId);
				CDObj.put("Attachments", ""+mAttachment); 
				//CDObj.put("ResponseImages", "null");
				//CDObj.put("OfferedPrize", "0");
				//CDObj.put("InCurrency","null");
				//CDObj.put("AdditionalInfo", "null");
				//CDObj.put("QuotationImage", "null");
				
				JSONObject Specs = new JSONObject();
				JSONArray AdditionalInfo = new JSONArray();
				if(checkOptionalDetail != null && checkOptionalDetail.size() > 0){
					for(Lookups l:checkOptionalDetail){
						AdditionalInfo.put(l.getmId());
					}
				}else{
					AdditionalInfo.put(0);
				}
				Specs.put("AdditionalInfo", AdditionalInfo);
				ChatData.put(CDObj);
				Specs.put("MileStones", null);
				CDObj.put("Specs", Specs);
					
				quotation.put("LastResponseDate", ""+mCurrentDate);
				quotation.put("LastResponseSequenceNumber", "1");
				quotation.put("QuotationStatusId", mQuotationStatusId);
				quotation.put("CreatedDate", ""+mCurrentDate);
				quotation.put("ServiceGroupType", VerisupplierUtils.mServiceGroupTypeId);
				quotation.put("Buyer", Buyer);
				quotation.put("Manufacturer", Manufacturer);
				quotation.put("Product", Product);
				quotation.put("ChatData", ChatData);
				quotation.put("Subject", mUserSubject);
				jsonObject.put("quotation", quotation);
				
				json = jsonObject.toString();
				Log.e("Save Quotatiion", json);
				}catch(Exception e){
					Log.e("SAVE QUOTATION", "ERROR IN CREATING JSON");
					e.printStackTrace();
				}
			StringEntity se = new StringEntity(json, HTTP.UTF_8);
			httpPost.setEntity(se);
			
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			
			HttpResponse httpResponse = client.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.d("statusCode",""+statusCode);
			if(statusCode !=200){
				return false;
			}
			inputStream = httpResponse.getEntity().getContent();
			System.out.println(""+inputStream);
			if(inputStream != null){
				result = convertInputStreamToString(inputStream);
	        }
			mResult=jsonParserClass.parseQuotationResult(result, getApplicationContext());
			if(mResult[1].equalsIgnoreCase("false")){
				mQuotationNumber=mResult[0];
				isValid=true;
			}else{
				isValid=false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValid;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;
	}
	
}
