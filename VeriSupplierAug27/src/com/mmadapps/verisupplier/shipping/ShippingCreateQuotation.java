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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
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
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.mmadapps.verisupplier.fileattachment.FileExplorer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ShippingCreateQuotation extends BaseActionBarActivity implements OnClickListener{
	
	//image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	TextView product_name,shipdate;
	ImageView product_image,sendquotation,attachfile;
	EditText subject,message,length,width,height,packages,weight;
	
	//from intent
	String mManufacturerId;
	
	//from database
	List<CountryDetails> mCountries=new ArrayList<CountryDetails>();
	List<CountryDetails> mStates=new ArrayList<CountryDetails>();
	UserDetails mUserDetails=new UserDetails();
	List<ManufacturerDetails> mManufacturerDetails=new ArrayList<ManufacturerDetails>();
	
	
	//spinner for cuontry and city
	Spinner fromcountrySpinner,toCountrySpinner;
	Spinner fromstateSpinner,toStateSpinner;
	
	//webservices 
	WebServices webServices;
	JsonParserClass jsonParserClass;
	private ProgressDialog pdLoading=null;
	
	String mQuotationStatusId;
	String[] mResult;
	String mQuotationNumber="";
	
	//additionalinfo variables
	String mLength;
	String mWidth;
	String mHeight;
	String mPackeges;
	String mWeight;
	String mDate;
	String mUserMessage;
	String mCountryId;
	String mFromCountryId;
	String mFromStateId;
	String mToCountryId;
	String mToStateId;
	String mFromCountryName;
	String mFromStateName;
	String mToCountryName;
	String mToStateName;
	String mUserSubject;
	
	//Attachment
	LinearLayout attachment_layout;
	private static final int REQUEST_GET_SINGLE_FILE = 0;
	ListView attachmentList;
	AttachmentListAdapter mAttachmentAdapter;
	static ArrayList<String> attachList = new ArrayList<String>();
	String attachimageUrl="";
	View attachmentView;
	
	//radio buttons
	ImageView vI_SCQ_PICKUP,vI_SCQ_DROP;
	
	String mShipDate;
	int mYear,mMonth,mDay;
	
	String isPickUpSelected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createquotation_shipping);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Get Quotation");
		
		mManufacturerId=getIntent().getStringExtra("MANUFACTURERID");
		getCountriesList();
		getStatesList();
		getManufacturerDetails();
		initializeView();
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
		
		product_image=(ImageView)findViewById(R.id.vI_acqs_productImage);
		product_name=(TextView)findViewById(R.id.vT_acqs_productName);
		subject=(EditText)findViewById(R.id.vE_acqs_subject);
		
		fromcountrySpinner=(Spinner)findViewById(R.id.vS_acqs_fromcountry);
		fromstateSpinner=(Spinner)findViewById(R.id.vS_acqs_fromCity);
		
		toCountrySpinner=(Spinner)findViewById(R.id.vS_acqs_tocountry);
		toStateSpinner=(Spinner)findViewById(R.id.vS_acqs_toCity);
		
		length=(EditText)findViewById(R.id.vE_acqs_length);
		width=(EditText)findViewById(R.id.vE_acqs_width);
		height=(EditText)findViewById(R.id.vE_acqs_height);
		
		message=(EditText)findViewById(R.id.vE_acqs_message);
		packages=(EditText)findViewById(R.id.vE_acqs_packages);
		weight=(EditText)findViewById(R.id.vE_acqs_weight);
		shipdate=(TextView)findViewById(R.id.vE_acqs_date);
		
		sendquotation=(ImageView)findViewById(R.id.vI_acqs_send);
		sendquotation.setOnClickListener(this);
		attachfile=(ImageView)findViewById(R.id.vI_acqs_attachfile);
		attachfile.setOnClickListener(this);
		
		attachmentView=(View)findViewById(R.id.scq_attachmentviewship);	
		attachmentList=(ListView)findViewById(R.id.vL_scq_attachmentlist);
		
		vI_SCQ_PICKUP = (ImageView) findViewById(R.id.vI_SCQ_PICKUP);
		vI_SCQ_DROP = (ImageView) findViewById(R.id.vI_SCQ_DROP);
		
		vI_SCQ_PICKUP.setOnClickListener(this);
		vI_SCQ_DROP.setOnClickListener(this);
		
		setValues();
		setAttchmentListAdapter();
		
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
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
			product_name.setText(""+ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Name());
			
			String sub = "Request for quote "+ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Name();//+" from "+mManufacturerDetails.get(0).getmManufacturerName();
			subject.setText(""+sub);
			mUserSubject=subject.getText().toString().trim();
		}
		
		if(mCountries==null || mCountries.size()==0){
		}else{
			fromcountrySpinner.setAdapter(new SpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mCountries));
			
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
					mFromStateId=mStates.get(position).getmStateId();
					mFromStateName=mStates.get(position).getmStateName();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
				
			});
			
			
			toCountrySpinner.setAdapter(new SpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mCountries));
		
			toCountrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
					mToCountryId=mCountries.get(position).getmCountryId();
					mToCountryName=mCountries.get(position).getmCountryName();
					mCountryId=mCountries.get(position).getmCountryId();
					getStatesList();
					toStateSpinner.setAdapter(new StateSpinnerAdapter(getApplicationContext(),R.layout.spinner_view,mStates));
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
				
			});
			
		}
		
		toStateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mToStateId=mStates.get(position).getmStateId();
				mToStateName=mStates.get(position).getmStateName();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		
		length.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mLength=length.getText().toString().trim();
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
		
		packages.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mPackeges=packages.getText().toString().trim();
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
		
		shipdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callDatePicker();
			}

		});
		
		
	}
	
	private void callDatePicker() {
		DatePickerDialog dpd = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
			 
            @Override
            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                shipdate.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
 
            }
        }, mYear, mMonth, mDay);
		dpd.setTitle("Select Date");
		dpd.show();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vI_acqs_send:
			mShipDate=shipdate.getText().toString();
			if(mUserMessage==null || mUserMessage.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the message", Toast.LENGTH_SHORT).show();
			}else if(mLength==null || mLength.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the length", Toast.LENGTH_SHORT).show();
			}else if(mWidth==null || mWidth.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the width", Toast.LENGTH_SHORT).show();
			}else if(mHeight==null || mHeight.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the height", Toast.LENGTH_SHORT).show();
			}else if(mPackeges==null || mPackeges.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the packages", Toast.LENGTH_SHORT).show();
			}else if(mWeight==null || mWeight.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the weight", Toast.LENGTH_SHORT).show();
			}else if(mShipDate==null || mShipDate.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the ship date", Toast.LENGTH_SHORT).show();
			}else{
				callService();
			}
			
			break;
			
		case R.id.vI_acqs_attachfile:
			Intent i = new Intent(ShippingCreateQuotation.this, FileExplorer.class);
			startActivityForResult(i, REQUEST_GET_SINGLE_FILE);
			break;
			
		case R.id.vI_SCQ_DROP:	
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
			Log.e("vI_SCQ_DROP", ""+isPickUpSelected);
			break;
			
		case R.id.vI_SCQ_PICKUP:
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
			
			Log.e("vI_SCQ_PICKUP", ""+isPickUpSelected);
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
			mQuotationStatusId="2";
			getUserDetails();
			new AsyncGetQuotationService().execute();
		}else{
			Toast.makeText(getApplicationContext(), "Please check internet", Toast.LENGTH_LONG).show();
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
			pdLoading = new ProgressDialog(ShippingCreateQuotation.this);
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
			
			mShipDate=shipdate.getText().toString();
			String mAttachment = attachimageUrl;
			
			try{
				JSONObject quotation=new JSONObject();
				JSONObject quotationObject=new JSONObject();
				
				quotationObject.put("CreatedDate", mCurrentDate);
				quotationObject.put("LastResponseDate", mCurrentDate);
				quotationObject.put("QuotationStatusId", mQuotationStatusId);
				quotationObject.put("LastResponseSequenceNumber", "1");
				quotationObject.put("ServiceGroupType", 2);
				quotationObject.put("Subject", mUserSubject);
				
				JSONObject Manufacturer=new JSONObject();
				Manufacturer.put("Name", mManufacturerName);
				Manufacturer.put("Id", mManufacturerId);
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
				ChatObject.put("ResponseDate", mCurrentDate);
				//ChatObject.put("OfferedPrize", "0");
				
				JSONObject ShippingAttributes=new JSONObject();
				ShippingAttributes.put("FromCountry", mFromCountryName);
				ShippingAttributes.put("ToCountry", mToCountryName);
				ShippingAttributes.put("FromCity", mFromStateName);
				ShippingAttributes.put("ToCity", mToStateName);
				ShippingAttributes.put("TotalNumberOfCatons", mPackeges);
				ShippingAttributes.put("TotalWeight", mWeight);
				ShippingAttributes.put("DeliveryDate", mShipDate);
				ShippingAttributes.put("PickUp", isPickUpSelected);
				
				JSONObject DimensionLwhObject=new JSONObject();
				DimensionLwhObject.put("Length", mLength);
				DimensionLwhObject.put("Width", mWidth);
				DimensionLwhObject.put("Height", mHeight);
				//DimensionLwhObject.put("UnitsOfMeasureMent1", "");
				//DimensionLwhObject.put("UnitsOfMeasureMent2", "");
				ShippingAttributes.put("DimensionLwh", DimensionLwhObject);
				
				JSONObject DimensionWeightObject=new JSONObject();
				DimensionWeightObject.put("Weight", mWeight);
				//DimensionWeightObject.put("UnitsOfMeasureMent1", "");
				//DimensionWeightObject.put("UnitsOfMeasureMent2", "");
				ShippingAttributes.put("DimensionWeight", DimensionWeightObject);
				
				ChatObject.put("ShippingAttributes", ShippingAttributes);
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
