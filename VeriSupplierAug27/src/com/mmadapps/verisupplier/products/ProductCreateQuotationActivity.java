package com.mmadapps.verisupplier.products;

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
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.details.GetQuotationActivityNew;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.mmadapps.verisupplier.fileattachment.FileExplorer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ProductCreateQuotationActivity extends BaseActionBarActivity implements OnClickListener {
	
	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	TextView productName;
	ImageView productImage,nextButton,sendQuotation,attachFile;
	
	EditText quantity,message;
	Spinner QuantitySpinner;
	ListView mOptionalList;
	ListView mAttachmentList;
	
	//fromdatabase variables
	UserDetails mUserDetails=new UserDetails();
	List<ManufacturerDetails> mManufacturerDetails=new ArrayList<ManufacturerDetails>();
	List<ProductDetails> mProductDetails=new ArrayList<ProductDetails>();
	List<Lookups> mUnitOfMeasurementList=new ArrayList<Lookups>();
	List<Lookups> mQuotationLookupList=new ArrayList<Lookups>();
	List<Lookups> checkOptionalDetail=new ArrayList<Lookups>();
	
	//setvalues variables
	String minqty="";
	String qtyUnit="";
	String mUserSubject;
	String mUserMessage;
	String mEditedQuantity;
	String mOriginalQuantityUnit;
	int mOriginalQuantityUnitId;
	String mSelectedQuantity;
	String mSelectedQuantityId;
	String mOriginalQuantity;
	
	String mFromActivity;
	boolean isGetPrice = false;
	String mQuotationStatusId;
	
	//attachment
	private static final int REQUEST_GET_SINGLE_FILE = 0;
	static ArrayList<String> attachList = new ArrayList<String>();
	AttachmentListAdapter mAttachmentAdapter;
	String attachimageUrl="";
	
	//optional details
	OptionalDetailsAdapter mOptionalDetailAdapter;
	
	//services
	WebServices webServices;
	JsonParserClass jsonParserClass;
	private ProgressDialog pdLoading=null;
	String[] mResult;
	String mQuotationNumber="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createquotation_product);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Get Quotation");
		
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
		getUnitOfMeasurementList();
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
	
	private void getUnitOfMeasurementList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUnitOfMeasurementList=helper.getUnitOfMeasurentList();
		mQuotationLookupList=helper.getQuotationLookupList();
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
		quantity=(EditText)findViewById(R.id.vE_acqp_quantity);
		message=(EditText)findViewById(R.id.vE_acqp_message);
		
		productImage=(ImageView)findViewById(R.id.vI_acqp_productImage);
		sendQuotation=(ImageView)findViewById(R.id.vI_acqp_sendquotation);
		attachFile=(ImageView)findViewById(R.id.vI_acqp_attachfile);
		
		productName=(TextView)findViewById(R.id.vT_acqp_productName);
		QuantitySpinner=(Spinner)findViewById(R.id.vS_acqp_quantitySpinner);
		
		mOptionalList=(ListView)findViewById(R.id.vL_acqp_optionaldetailslist);
		mAttachmentList=(ListView)findViewById(R.id.vL_acqp_attachmentlist);
		
		sendQuotation.setOnClickListener(this);
		attachFile.setOnClickListener(this);
		
		setValues();
	}

	private void setValues() {
		if(ProductDetailsActivity.mSavePrequotationValues==null || ProductDetailsActivity.mSavePrequotationValues.size()==0){
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
			
			productName.setText(""+ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Name());
			quantity.setText(""+minqty);
			
			imageLoader.displayImage(ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Image(), productImage, options);
		
			if(ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_moq()==null || ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_moq().length()==0){
				minqty="0";
				qtyUnit="0";
				mOriginalQuantity=minqty;
				mOriginalQuantityUnit=qtyUnit;
			}
		}
		
		mSelectedQuantity=qtyUnit;
		QuantitySpinner.setAdapter(new SpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mUnitOfMeasurementList));
		
		for(int i=0;i<mUnitOfMeasurementList.size();i++){
			if(mUnitOfMeasurementList.get(i).getmName().equalsIgnoreCase(mOriginalQuantityUnit)){
				mOriginalQuantityUnitId=i;
				break;
			}
		}
		
		mOptionalDetailAdapter=new OptionalDetailsAdapter();
		mOptionalList.setAdapter(mOptionalDetailAdapter);
		setListViewHeightBasedOnChildrenOptionalDetails(mOptionalList);
		
		QuantitySpinner.setSelection(mOriginalQuantityUnitId);
		QuantitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mSelectedQuantity=mUnitOfMeasurementList.get(position).getmName();
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
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
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
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
	
	private void setAttchmentListAdapter() {
		if(attachList.size()==0){
			//attachmentView.setVisibility(View.GONE);
		}else{
			//attachmentView.setVisibility(View.VISIBLE);
		}
		mAttachmentAdapter=new AttachmentListAdapter();
		mAttachmentList.setAdapter(mAttachmentAdapter);
		setListViewHeightBasedOnChildren(mAttachmentList);
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
	
	public static void setListViewHeightBasedOnChildrenOptionalDetails(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            return;
        }

        int Size=0;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
           Size=VerisupplierUtils.mQuotationLookups.size();
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount())))+listAdapter.getCount()+Size;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
	
	private class OptionalDetailsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(mQuotationLookupList == null || mQuotationLookupList.size()==0){
				return 0;
			}else{
				return mQuotationLookupList.size();
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
			TextView vC__optional_textview = (TextView) convertView.findViewById(R.id.vC__optional_textview);
			final Lookups mLookups = mQuotationLookupList.get(position);
			
			vC__optional_textview.setText(""+mLookups.getmName());
			
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vI_acqp_attachfile:
			Intent i = new Intent(ProductCreateQuotationActivity.this, FileExplorer.class);
			startActivityForResult(i, REQUEST_GET_SINGLE_FILE);
			break;
			
		
		case R.id.vI_acqp_sendquotation:
			if(message.getText().toString().trim()==null || message.getText().toString().trim().length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the message", Toast.LENGTH_SHORT).show();
			}else if(quantity.getText().toString().trim()==null || quantity.getText().toString().trim().length()==0){
				Toast.makeText(getApplicationContext(), "Please enter quantity", Toast.LENGTH_SHORT).show();
			}else if(quantity.getText().toString().equalsIgnoreCase("0")){
				Toast.makeText(getApplicationContext(), "Quantity should not be zero", Toast.LENGTH_SHORT).show();
			}
			else{
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
			pdLoading = new ProgressDialog(ProductCreateQuotationActivity.this);
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