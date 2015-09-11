package com.mmadapps.verisupplier.details;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MoreInfoActivityNew extends BaseActionBarActivity implements OnClickListener{
	
	//Image loader
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	//String variables
	String mQuotationStatusId;
	String mUserMessage;
	
	ImageView product_image,submit;
	TextView product_name;
	EditText message;
	
	//optional details list
	ListView mOptionalList;	
	OptionalDetailsAdapter mOptionalDetailAdapter;
	List<Lookups> mMoreinfoLookupList=new ArrayList<Lookups>();
	List<Lookups> checkOptionalDetail=new ArrayList<Lookups>();
	
	//from database
	UserDetails mUserDetails=new UserDetails();
	
	//from utils
	Spinner SubjectSpinner;
	List<Lookups> mSubjectList=new ArrayList<Lookups>();
	
	//for services
	private ProgressDialog pdLoading=null;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	String[] mResult;
	
	//string variables
	String mSelectedSubject;
	String mSelectedCheckbox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createmoreinfo_product);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("More Info");
		mQuotationStatusId="2";
		
		getUserId();
		getMoreinfoLookup();
		initializeView();
		
	}

	private void getUserId() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
	}
	
	private void getMoreinfoLookup() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mSubjectList=helper.getMoreinfoSubjectList();
		mMoreinfoLookupList=helper.getMoreinfoLookupList();
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
		
		message=(EditText)findViewById(R.id.vE_acmip_message);
		mOptionalList=(ListView)findViewById(R.id.vL_acmip_optionaldetailslist);
		SubjectSpinner=(Spinner)findViewById(R.id.vS_acmip_subjectspinner);
		
		submit=(ImageView)findViewById(R.id.vI_acmip_submit);
		submit.setOnClickListener(this);
		
		
		setValues();
		
	}

	private void setValues() {
		if(ProductDetailsActivity.mSavePrequotationValues==null || ProductDetailsActivity.mSavePrequotationValues.size()==0){
			
		}else{
			
		}
		
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
		
		SubjectSpinner.setAdapter(new SpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mSubjectList));
		SubjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mSelectedSubject=mSubjectList.get(position).getmName();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
			
		});
		
		
		mOptionalDetailAdapter=new OptionalDetailsAdapter();
		mOptionalList.setAdapter(mOptionalDetailAdapter);
		setListViewHeightBasedOnChildrenOptionalDetails(mOptionalList);
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vI_acmip_submit:
			if(mUserMessage==null || mUserMessage.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter the message", Toast.LENGTH_SHORT).show();
			}else{
				ConnectionDetector mDetector=new ConnectionDetector(getApplicationContext());
				if(mDetector.isConnectingToInternet()){
					webServices=new WebServices();
					jsonParserClass=new JsonParserClass();
					new AsyncMoreInfoService().execute();
				}else{
					Toast.makeText(getApplicationContext(), "Please check internet", Toast.LENGTH_SHORT).show();
				}
			}
			break;

		default:
			break;
		}
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
			if(mMoreinfoLookupList == null || mMoreinfoLookupList.size()==0){
				return 0;
			}else{
				return mMoreinfoLookupList.size();
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
			final Lookups mLookups = mMoreinfoLookupList.get(position);
			
			vC__optional_textview.setText(""+mLookups.getmName());
			
			optionalDetails.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						if(checkOptionalDetail.contains(mLookups)){
						}else{
							mSelectedCheckbox=mLookups.getmName();
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
	
	
	private class AsyncMoreInfoService extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(MoreInfoActivityNew.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return callService();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pdLoading.cancel();
			if(pdLoading.isShowing())
				pdLoading.dismiss();
			if(result){
				Toast.makeText(getApplicationContext(), "Thanks for creating request with us.", Toast.LENGTH_LONG).show();
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Thanks for creating request with us.", Toast.LENGTH_LONG).show();
				finish();
				//Toast.makeText(getApplicationContext(), "request failed Please try again.", Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	private Boolean callService() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();  
			String postURL = webServices.CREATEINFOFEEDBACK;
			InputStream inputStream = null;
			HttpPost httpPost = new HttpPost(postURL);
			String json = ""; 
			
			Calendar cal=Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
			String mCurrentDate=sdf.format(cal.getTime());
			System.out.println(""+mCurrentDate);
			
			String mUserId = mUserDetails.getmUserID();
			String mProductId = ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Id();
			
			try {
				JSONObject jsonObject = new JSONObject();
				JSONObject feedbackObject = new JSONObject();
				
				JSONObject productObject=new JSONObject();
				productObject.put("ProductId", mProductId);
				feedbackObject.put("Product", productObject);
				feedbackObject.put("CustomerId", mUserId);
				feedbackObject.put("Subject", mSelectedSubject);
				feedbackObject.put("Message", mUserMessage);
				feedbackObject.put("AdditionalInfo", mSelectedCheckbox);
				
				jsonObject.put("feedback", feedbackObject);
				json = jsonObject.toString();
				Log.e("Save Quotatiion", json);
				
			} catch (Exception e) {
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
			mResult=jsonParserClass.parseMoreInfoResult(result, getApplicationContext());
			if(mResult[1].equalsIgnoreCase("false")){
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
