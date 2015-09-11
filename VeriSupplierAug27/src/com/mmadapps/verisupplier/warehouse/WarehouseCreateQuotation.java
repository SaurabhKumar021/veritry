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
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.mmadapps.verisupplier.fileattachment.FileExplorer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class WarehouseCreateQuotation extends BaseActionBarActivity implements OnClickListener{
	
	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
		
	String mManufacturerId;
	String mQuotationStatusId;
	String[] mResult;
	String mQuotationNumber="";
	
	ImageView product_image,send,attachfile;
	
	WebServices webServices;
	JsonParserClass jsonParserClass;
	private ProgressDialog pdLoading=null;
	
	//from database
	UserDetails mUserDetails=new UserDetails();
	List<ManufacturerDetails> mManufacturerDetails=new ArrayList<ManufacturerDetails>();
	List<Lookups> checkOptionalDetail=new ArrayList<Lookups>();
	
	TextView productname,manufacturername;
	EditText subject,message,length,width,height,city;
	
	//service string variables
	String mUserMessage;
	//String mSubject;
	String mLenght;
	String mHeight;
	String mWidth;
	String mCity;
	String mUserSubject;
	
	//Attachment
	LinearLayout attachment_layout;
	private static final int REQUEST_GET_SINGLE_FILE = 0;
	ListView attachmentList;
	AttachmentListAdapter mAttachmentAdapter;
	static ArrayList<String> attachList = new ArrayList<String>();
	String attachimageUrl="";
	View attachmentView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createquotation_warehouse);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Get Quotation");
		
		mManufacturerId=getIntent().getStringExtra("MANUFACTURERID");
		mQuotationStatusId="2";
		getManufacturerDetails();
		initializeView();
	}

	private void getManufacturerDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mManufacturerDetails=helper.getManufacturerDetails(mManufacturerId);
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
		
		product_image=(ImageView)findViewById(R.id.vI_acqwh_productImage);
		manufacturername=(TextView)findViewById(R.id.vT_acqwh_manufacturerName);
		
		subject=(EditText)findViewById(R.id.vE_acqwh_subject);
		message=(EditText)findViewById(R.id.vE_acqwh_message);
		
		length=(EditText)findViewById(R.id.vE_acqwh_length);
		height=(EditText)findViewById(R.id.vE_acqwh_height);
		width=(EditText)findViewById(R.id.vE_acqwh_width);
		city=(EditText)findViewById(R.id.vE_acqwh_city);
		
		send=(ImageView)findViewById(R.id.vI_acqwh_sendquotation);
		send.setOnClickListener(this);
		
		attachfile = (ImageView) findViewById(R.id.vI_acqwh_attachfile);
		attachfile.setOnClickListener(this);
		
		attachmentView =(View)findViewById(R.id.vL_acqwh_attachview);
		attachmentList = (ListView) findViewById(R.id.vL_acqwh_attachments);
		
		setValues();
		setAttchmentListAdapter();
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


	private void setValues() {
		if(ProductDetailsActivity.mSavePrequotationValues==null || ProductDetailsActivity.mSavePrequotationValues.size()==0){
		}else{
			imageLoader.displayImage(ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Image(),product_image, options);
			manufacturername.setText(""+ProductDetailsActivity.mSavePrequotationValues.get(0).getmManufacturer_name());
			
			String sub = "Request for quote "+ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Name();//+" from "+mManufacturerDetails.get(0).getmManufacturerName();
			subject.setText(""+sub);
			mUserSubject=subject.getText().toString();
		
		}
		
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
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vI_acqwh_sendquotation:
			if(mUserMessage==null || mUserMessage.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the message", Toast.LENGTH_SHORT).show();
			}else if(mLenght==null ||mLenght.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the lenght", Toast.LENGTH_SHORT).show();
			}else if(mWidth==null || mWidth.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the width", Toast.LENGTH_SHORT).show();
			}else if(mHeight==null || mHeight.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the height", Toast.LENGTH_SHORT).show();
			}else if(mCity==null || mCity.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the city", Toast.LENGTH_SHORT).show();
			}else{
				callService();
			}
		
			break;
			
		case R.id.vI_acqwh_attachfile:
			Intent i = new Intent(WarehouseCreateQuotation.this, FileExplorer.class);
			startActivityForResult(i, REQUEST_GET_SINGLE_FILE);
			break;

		default:
			break;
		}
	}
	
	private void callService() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		ConnectionDetector mDetector=new ConnectionDetector(getApplicationContext());
		if(mDetector.isConnectingToInternet()){
			getUserDetails();
			new AsyncGetQuotationService().execute();
		}else{
			
		}
	}
	
	private void getUserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}
	
	private class AsyncGetQuotationService extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(WarehouseCreateQuotation.this);
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
			return callCreateQuotationservice();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pdLoading.cancel();
			if(pdLoading.isShowing())
				pdLoading.dismiss();
			if(result){
				Toast.makeText(getApplicationContext(), "Quote request successfully submited.Quotation Number "+mQuotationNumber, Toast.LENGTH_LONG).show();
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "request failed Please try again.", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private Boolean callCreateQuotationservice() {
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
			
			String mAttachment = attachimageUrl; //Productdetails.mSavePrequotationValues.get(0).getmProductImage_Details().get(0).getmProductImage_url();
			
			try{
				JSONObject quotation=new JSONObject();
				JSONObject quotationObject=new JSONObject();
				
				quotationObject.put("IsExternalProduct", "false");
				quotationObject.put("CreatedDate", mCurrentDate);
				quotationObject.put("LastResponseDate", mCurrentDate);
				quotationObject.put("QuotationStatusId", mQuotationStatusId);
				quotationObject.put("LastResponseSequenceNumber", "1");
				quotationObject.put("ServiceGroupType", VerisupplierUtils.mWarehouseParam);
				quotationObject.put("Subject", mUserSubject);
				
				
				JSONObject Manufacturer=new JSONObject();
				Manufacturer.put("Name", mManufacturerName);
				Manufacturer.put("Id", mManufacturerId);
				/*JSONObject ContactDetails=new JSONObject();
				JSONArray ContactPerson=new JSONArray();
				JSONObject ContactPersonObject=new JSONObject();
				ContactPersonObject.put("UserId", mManufacturerId);
				ContactPerson.put(ContactPersonObject);
				ContactDetails.put("ContactPerson", ContactPerson);
				Manufacturer.put("ContactDetails", ContactDetails);*/
				quotationObject.put("Manufacturer", Manufacturer);
				
				JSONObject Product=new JSONObject();
				Product.put("ProductName", mProductName);
				Product.put("Id", mProductId);
				quotationObject.put("Product", Product);
				
				JSONObject Buyer=new JSONObject();
				Buyer.put("Id", mUserId);
				quotationObject.put("Buyer", Buyer);
				
				JSONArray ChatData=new JSONArray();
				JSONObject ChatObject=new JSONObject();
				ChatObject.put("QuotationResponseId", "0");
				ChatObject.put("Message", mUserMessage);
				ChatObject.put("ResponseUserType", "1");
				ChatObject.put("Attachments", mAttachment);
				ChatObject.put("ResponseBy", "0");
				ChatObject.put("ResponseDate", mCurrentDate);
				//ChatObject.put("OfferedPrize", "0");
				
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
				
				ChatObject.put("WareHouseAttributes", WareHouseAttributes);
				ChatData.put(ChatObject);
				quotationObject.put("ChatData", ChatData);
				quotation.put("quotation", quotationObject);
				
				json = quotation.toString();
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
