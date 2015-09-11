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
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.MyGridView;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.fileattachment.FileExplorer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MoreInfoActivity extends BaseActionBarActivity implements OnClickListener{
	
	EditText quantity,subject,message;
	TextView add, sub,username,useremail,productname,productmin_quantity;
	TextView getprice_ok,to,subjectte,quantityt,messaget,pieces,file_path,attachment_text,optional_details;
	ImageView productImage,plus;
	MyGridView optionalGrid;
	Spinner info_spinner;
	String subtoset;
	
	String[] stringArray = { "Piece(s)","Acre(s)","Ampere(s)","Bag(s)","Barrel(s)","Blade(s)","Box(s)","Bushel(s)","carat(s)","carton(s)" };
	List<Lookups> checkOptionalDetail;
	List<Lookups> mQuantityList=new ArrayList<Lookups>();
	SpinnerAdapter mSpinnerAdapter;
	
	private static final int REQUEST_GET_SINGLE_FILE = 0;
	private static String filePath = "";
	String mFilePath = "";
	ImageView attah;
	TextView file_name;
	ImageView close_file;
	ListView attach_list;
	static ArrayList<String> arrayList = new ArrayList<String>();
	UserDetails mUserDetails;
	String[] mResult;
	String minqty="";
	String qtyUnit="";
	String mCurrency="";
	//CheckBox box1,box2,box3,box4,box5,box6;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	View pluslay;
	ProgressDialog pdLoading=null;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	String mUserSubject="";
	String mUserMessage="";
	String mOriginalQuantity;
	View attachmentView;
	String mSelectedQuantity;
	String mQuotationStatusId;
	String mQuotationNumber="";
	String mOriginalQuantityUnit;
	int mOriginalQuantityUnitId;
	
	private static Typeface segeo_wp;
	private static Typeface segeo_wp_bold;
	
	public static Typeface getSegeo_wp() {
		return segeo_wp;
	}

	public static void setSegeo_wp(Typeface segeo_wp) {
		MoreInfoActivity.segeo_wp = segeo_wp;
	}

	public static Typeface getSegeo_wp_bold() {
		return segeo_wp_bold;
	}

	public static void setSegeo_wp_bold(Typeface segeo_wp_bold) {
		MoreInfoActivity.segeo_wp_bold = segeo_wp_bold;
	}
	
	List<ManufacturerDetails> mManufacturerDetails=new ArrayList<ManufacturerDetails>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_info);
	//	BaseActionBarActivity.search_layout.setVisibility(View.GONE);
		//BaseActionBarActivity.title_layout.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("More Info");
		mQuotationStatusId="2";
		
		subtoset = ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Name();
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					segeo_wp = Typeface.createFromAsset(getAssets(),"segeo-wp.ttf");
					segeo_wp_bold = Typeface.createFromAsset(getAssets(),"segeo-wp-bold.ttf");
					to.setTypeface(segeo_wp_bold);
					username.setTypeface(segeo_wp_bold);
					subjectte.setTypeface(segeo_wp_bold);
					messaget.setTypeface(segeo_wp_bold);
					quantityt.setTypeface(segeo_wp_bold);
					quantity.setTypeface(segeo_wp_bold);
					attachment_text.setTypeface(segeo_wp_bold);
					getprice_ok.setTypeface(segeo_wp_bold);
					subject.setTypeface(segeo_wp);
					message.setTypeface(segeo_wp);
					file_name.setTypeface(segeo_wp);
					pieces.setTypeface(segeo_wp);
					file_path.setTypeface(segeo_wp);
					optional_details.setTypeface(segeo_wp_bold);
					//box1.setTypeface(segeo_wp);
					//box2.setTypeface(segeo_wp);
					//box3.setTypeface(segeo_wp);
					//box4.setTypeface(segeo_wp);
					//box5.setTypeface(segeo_wp);
					//box6.setTypeface(segeo_wp);
				} catch (Exception e) {

				}
			}
		};
		new Thread(runnable).start();
		
		initializeView();
		attah=(ImageView)findViewById(R.id.vI_moreinfo_attach);
		attah.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MoreInfoActivity.this, FileExplorer.class);
				startActivityForResult(i, REQUEST_GET_SINGLE_FILE);

			}
		});
		getUserDetails();
		
		//SetValues();
		
		values();
	}
	
	private void getManufacturerDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mManufacturerDetails=helper.getManufacturerDetails(ProductDetailsActivity.mManufacturerId);
		helper.close();
	}
	
	private void getUserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
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
		
		optionalGrid = (MyGridView) findViewById(R.id.vG_Minfo_optionalGrid);
		checkOptionalDetail = new ArrayList<Lookups>();
		optionalGrid.setAdapter(new GridAdapterClass());
		
		username=(TextView)findViewById(R.id.vT_mia_username);
//		useremail=(TextView)findViewById(R.id.vT_mia_useremail);
//		productname=(TextView)findViewById(R.id.vT_mia_product_name);
//		productmin_quantity=(TextView)findViewById(R.id.vT_mia_minquantity);
//		productImage=(ImageView)findViewById(R.id.vI_mia_product_image);
		plus=(ImageView) findViewById(R.id.plus);
		optional_details=(TextView) findViewById(R.id.Optional_Details_text);
		//box1=(CheckBox) findViewById(R.id.checkBox1);
		//box2=(CheckBox) findViewById(R.id.checkBox2);
		//box3=(CheckBox) findViewById(R.id.checkBox3);
		//box4=(CheckBox) findViewById(R.id.checkBox4);
		//box5=(CheckBox) findViewById(R.id.checkBox5);
		//box6=(CheckBox) findViewById(R.id.checkBox6);
		pluslay=findViewById(R.id.pluslayout);
		pieces=(TextView) findViewById(android.R.id.text1);
		to=(TextView) findViewById(R.id.to1);
		subjectte=(TextView) findViewById(R.id.subject1);
		messaget=(TextView) findViewById(R.id.message1);
		quantityt=(TextView) findViewById(R.id.quantity1);
		attachment_text=(TextView) findViewById(R.id.vT_mia_attach);
		
		
		info_spinner=(Spinner)findViewById(R.id.moreinfo_spinner);
		//info_spinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item, stringArray));
		
		
		attach_list=(ListView)findViewById(R.id.moreinfo_attachmentLists);
//		add = (TextView) findViewById(R.id.adding);
//		sub = (TextView) findViewById(R.id.subtract);
//		add.setOnClickListener(this);
//		sub.setOnClickListener(this);
		quantity = (EditText) findViewById(R.id.vE_mia_quantity);
		getprice_ok=(TextView)findViewById(R.id.send);
		getprice_ok.setOnClickListener(this);
		
		subject=(EditText)findViewById(R.id.vE_mia_subject);
		message=(EditText)findViewById(R.id.vE_mia_message);
		
		subject.setText("Requested for "+subtoset);
		mUserSubject=subject.getText().toString().trim();
		
		attachmentView=(View)findViewById(R.id.view_mi_attachmentview);
		attachmentView.setVisibility(View.GONE);
		
		plus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (pluslay.isShown()) {
					
					pluslay.setVisibility(View.INVISIBLE);
					plus.setImageResource(R.raw.plus_symbol);
					
				}
				else {
					pluslay.setVisibility(View.VISIBLE);
					plus.setImageResource(R.raw.minus_icon);
					
				}
				
			}
		});
		subject.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				mUserSubject=subject.getText().toString();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
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
		
		SetValues();
	}
	
	private void SetValues() {
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
		mSelectedQuantity=qtyUnit;
		
		/*String moq = Productdetails.mSavePrequotationValues.get(0).getmProduct_moq();
		if(moq== null || moq.length()==0){
			
		}else{
			if(moq.contains(" ")){
				String[] qty=moq.split(" ");
				minqty=qty[0];
				qtyUnit=qty[1];
			}
		}*/
		//mCurrency=OverViewFragment.mSavePrequotationValues.get(0).getMcurrency();
		getManufacturerDetails();
		if(mManufacturerDetails==null || mManufacturerDetails.size()==0){
			username.setText("");
		}else{
			username.setText(""+mManufacturerDetails.get(0).getmManufacturerName());
		}
		
	//	useremail.setText(""+mUserDetails.getmUserEmail());
		//productname.setText(""+OverViewFragment.mSavePrequotationValues.get(0).getmProduct_Name());
		//productmin_quantity.setText(""+minqty);
	//	imageLoader.displayImage(OverViewFragment.mSavePrequotationValues.get(0).getmProduct_Image(),productImage, options);
		mQuantityList=VerisupplierUtils.mQuantityList;
		info_spinner.setAdapter(new SpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mQuantityList));
		
		for(int i=0;i<mQuantityList.size();i++){
			if(mQuantityList.get(i).getmName().equalsIgnoreCase(mOriginalQuantityUnit)){
				mOriginalQuantityUnitId=i;
				break;
			}
		}
		
		info_spinner.setSelection(mOriginalQuantityUnitId);
		info_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				mSelectedQuantity=mQuantityList.get(position).getmName();
				
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
		
		quantity.setText(""+minqty);
	}

	private void values() {
		if(arrayList.size()==0){
			attachmentView.setVisibility(View.GONE);
		}else{
			attachmentView.setVisibility(View.VISIBLE);
		}
		GetQuortationAdapter adapter = new GetQuortationAdapter(MoreInfoActivity.this);
		attach_list.setAdapter(adapter);
		setListViewHeightBasedOnChildren(attach_list);
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
            Size=arrayList.size();
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()+listView.getChildCount())))+Size;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*case R.id.adding:
			String pos_number = quantity.getText().toString();
			int s = Integer.parseInt(pos_number);
			int final_pos_number = s + 1;

			if (final_pos_number < 0) {
				quantity.setText("" + 0);
			}
			quantity.setText("" + final_pos_number);
			break;
			
		case R.id.subtract:

			String neg_number = quantity.getText().toString();
			int s1 = Integer.parseInt(neg_number);
			int final_neg_number = s1 - 1;

			if (final_neg_number < 0) {
				quantity.setText("" + 0);
			} else {
				quantity.setText("" + final_neg_number);
			}
			break;*/
			
			case R.id.send:
				if(message.getText().toString().trim()==null || message.getText().toString().trim().length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the message", Toast.LENGTH_SHORT).show();
				}else if(quantity.getText().toString().trim()==null || quantity.getText().toString().trim().length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the quantity", Toast.LENGTH_SHORT).show();
				}else{
					if(Integer.parseInt(mOriginalQuantity)>Integer.parseInt(minqty)){
						Toast.makeText(getApplicationContext(), "Quantity should not be less than product minimum quantity", Toast.LENGTH_LONG).show();
					}else{
						new AsyncMoreInfoService().execute();
					}
				}
				/*Toast.makeText(getApplicationContext(), "Thanks for creating request with us.", Toast.LENGTH_SHORT).show();

				Intent nextIntent=new Intent(MoreInfoActivity.this,DetailsActivity.class);
				startActivity(nextIntent);
				finish();*/
				break;

		default:
			break;
		}

	}
	
	class GetQuortationAdapter extends BaseAdapter {

		private Context context;
		LayoutInflater inflater;

		public GetQuortationAdapter(Context context) {
			this.context = context;
			inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
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
		public View getView(final int position, View arg1, ViewGroup arg2) {
			View view = inflater.inflate(R.layout.activity_attachdoc, arg2,false);
			file_name = (TextView) view.findViewById(R.id.file_path);
			close_file = (ImageView) view.findViewById(R.id.close_attach);
			file_name.setText(""+arrayList.get(position).split("@")[0]);
			
			close_file.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					arrayList.remove(position);
					attach_list.setAdapter(new GetQuortationAdapter(context));
					setListViewHeightBasedOnChildren(attach_list);
				}
			});
			
			return view;
		}

	}
	
	private class GridAdapterClass extends BaseAdapter{

		@Override
		public int getCount() {
			if(VerisupplierUtils.mMoreInfoLookups == null){
				return 0;
			}
			return VerisupplierUtils.mMoreInfoLookups.size();
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
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.adapter_lookups, parent,false);
			}
			CheckBox optionalDetails = (CheckBox) convertView.findViewById(R.id.vC_OptionalDetail);
			final Lookups mLookups = VerisupplierUtils.mMoreInfoLookups.get(position);
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
			//Utils.setGridViewHeightBasedOnChildren(optionalGrid);
			return convertView;
		}
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_GET_SINGLE_FILE: {
			if (resultCode == RESULT_OK) {
				String filePath = "";
				Uri uri = data.getParcelableExtra("file");
				//FileObj fileobj = new FileObj();
				//fileobj.setmFilePath(uri.getPath());
				if(filePath.length() == 0){
				filePath = uri.getPath();
				}/*else{
				filePath += ","+uri.getPath();
				}*/
				/*String[] args=filePath.split("/");
				mFilePath=args[args.length-1];*/

				String[] args = filePath.split("/");
				String mFileName = args[args.length - 1];
				System.out.println("mFileName---" + mFileName);
				// file_name.setText(""+mFilePath)
				arrayList.add(mFileName+"@"+filePath);
				values();
			}
		}
		break;
		}
	}
	
	
	class AsyncMoreInfoService extends AsyncTask<String, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(MoreInfoActivity.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			if(arrayList != null && arrayList.size()>0){
				int len = arrayList.size();
				for(int i=0; i<len; i++){
					boolean isUploaded = uploadFiles(arrayList.get(i).split("@")[0],arrayList.get(i).split("@")[1]);
				}
			}
			return callgetMoreInfoservice();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pdLoading.cancel();
			if(pdLoading.isShowing())
				pdLoading.dismiss();
			if(result){
				Toast.makeText(getApplicationContext(), "Thanks for creating request with us.", Toast.LENGTH_SHORT).show();
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Sorry your request is not created", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	
	private Boolean callgetMoreInfoservice() {
		/*Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();  
			String postURL = webServices.SaveQuotation;//"http://vsuppliervm.cloudapp.net:5132/QuotationService.svc/SaveQuotation";
			InputStream inputStream = null;
			
			HttpPost httpPost = new HttpPost(postURL);
			String json = ""; 
			
			JSONObject jsonObject=new JSONObject();
			JSONObject jsonObject1=new JSONObject();
			
			jsonObject1.put("ProductId", ""+Productdetails.mSavePrequotationValues.get(0).getmProduct_Id());
			//jsonObject1.put("ManufacturerId", ""+OverViewFragment.mSavePrequotationValues.get(0).getmManufacturerId());
			//jsonObject1.put("ManufacturerAccountId", ""+OverViewFragment.mSavePrequotationValues.get(0).getmManufacturerId());
			jsonObject1.put("BuyerAccountId", "1");
			jsonObject1.put("IsCombinedOrder", "false");
			jsonObject1.put("BuyerId",mUserDetails.getmUserID());
			jsonObject1.put("QuantityNeeded", minqty);//
			jsonObject1.put("Message", mUserMessage);
			jsonObject1.put("Unit", qtyUnit);//
			jsonObject1.put("Subject", mUserSubject);
			jsonObject1.put("ResponseUserType", "1");
			jsonObject1.put("CreatedDate", mCurrentDate);
			jsonObject1.put("AdditionalInfo", null);
			jsonObject1.put("QuotationImage", null);
			jsonObject1.put("ResponseImages", null);
			jsonObject1.put("OfferedPrize", "200");
			jsonObject1.put("InCurrency", mCurrency);//mCurrency
			jsonObject1.put("ResponseBy", mUserDetails.getmUserID());
			jsonObject1.put("ResponseDate", mCurrentDate);
			jsonObject1.put("QuoteStatus", "2");
			
			try {
				JSONObject jsonarrayObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				if(checkedOD != null){
					for(Lookups l:checkedOD){
						jsonArray.put(l.getmId());
					}
				}
				jsonarrayObject.put("AdditionalInfo", jsonArray);
				jsonObject1.put("Specs", jsonarrayObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			jsonObject.put("quotation", jsonObject1);
			
			json = jsonObject.toString();
			StringEntity se = new StringEntity(json, HTTP.UTF_8);
			httpPost.setEntity(se);
			
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			
			HttpResponse httpResponse = client.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.d("statusCode",""+statusCode);
			if(statusCode == 404){
				return null;
			}
			inputStream = httpResponse.getEntity().getContent();
			System.out.println(""+inputStream);
			if(inputStream != null){
				result = convertInputStreamToString(inputStream);
	        }
			mResult=jsonParserClass.parseQuotationResult(result, getApplicationContext());
			if(mResult[0].equalsIgnoreCase("true")){
				isValid=true;
			}else{
				isValid=false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValid;
		*/
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();  
			String postURL = webServices.SaveQuotation;//"http://vsuppliervm.cloudapp.net:5132/QuotationService.svc/SaveQuotation";	 //"http://111.221.97.232:5742/QuotationService.svc/SaveQuotation";
			InputStream inputStream = null;
			HttpPost httpPost = new HttpPost(postURL);
			String json = ""; 
	
			Calendar cal=Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
			String mCurrentDate=sdf.format(cal.getTime());
			System.out.println(""+mCurrentDate);
			String mUserId = mUserDetails.getmUserID();
			String mProductId = ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Id();
			String mProductName = ProductDetailsActivity.mSavePrequotationValues.get(0).getmProduct_Name();
			String mManufacturerId = mManufacturerDetails.get(0).getmManufactureID();
			String mManufacturerName = mManufacturerDetails.get(0).getmManufacturerName();
			
			String mAttachment = "" ; //Productdetails.mSavePrequotationValues.get(0).getmProductImage_Details().get(0).getmProductImage_url();
			
			try{
				JSONObject jsonObject = new JSONObject();
				JSONObject quotation = new JSONObject();
				JSONObject Buyer = new JSONObject();
				JSONObject Manufacturer = new JSONObject();
				JSONObject Product = new JSONObject();
				JSONArray ChatData =  new JSONArray();
				
				Buyer.put("Id", ""+mUserId);
				
				Manufacturer.put("Id", ""+mManufacturerId);
				Manufacturer.put("Name", ""+mManufacturerName);
				JSONObject ContactDetails = new JSONObject();
				JSONArray ContactPerson = new JSONArray();
				JSONObject data = new JSONObject();
				data.put("UserId", ""+mUserId);
				ContactPerson.put(data);
				ContactDetails.put("ContactPerson", ContactPerson);
				Manufacturer.put("ContactDetails", ContactDetails);
				
				Product.put("Id", ""+mProductId);
				Product.put("ProductName", ""+mProductName);
				
				JSONObject CDObj = new JSONObject();
				//CDObj.put("QuotationResponseId","3");
				CDObj.put("RequiredQty", ""+minqty); 
				CDObj.put("Message", ""+mUserMessage);
				CDObj.put("UnitsOfMeasurement", mSelectedQuantity);
				CDObj.put("ResponseUserType", "1");
				CDObj.put("AdditionalInfo", "null");
				CDObj.put("QuotationImage", "null");
				CDObj.put("Attachments", ""+mAttachment); 
				CDObj.put("ResponseImages", "null");
				CDObj.put("OfferedPrize", "0"); // Doubt
				CDObj.put("InCurrency","null");
				CDObj.put("ResponseBy","0");
				CDObj.put("ResponseDate", ""+mCurrentDate);
				
				JSONObject Specs = new JSONObject();
				JSONArray AdditionalInfo = new JSONArray();
				//JSONArray MileStones = new JSONArray();
				
				if(checkOptionalDetail != null && checkOptionalDetail.size() > 0){
					for(Lookups l:checkOptionalDetail){
						AdditionalInfo.put(l.getmId());
					}
				}else{
					AdditionalInfo.put(0);
				}
				
				Specs.put("AdditionalInfo", AdditionalInfo);
				ChatData.put(CDObj);
				
				/*JSONObject MilesObj = new JSONObject();
				MilesObj.put("QuotationId", "0");
				MilesObj.put("QuotationResponseId", "0");
				MilesObj.put("Name", "ss"); // Doubt
				MilesObj.put("BriefDescription", "2");
				MilesObj.put("PercentBreakup", "10");
				MilesObj.put("QuotationMilestoneSerialized", "null");
				
				MileStones.put(MilesObj);*/
				Specs.put("MileStones", null);
				CDObj.put("Specs", Specs);
					
				quotation.put("LastResponseDate", ""+mCurrentDate);
				quotation.put("LastResponseSequenceNumber", "1");
				quotation.put("QuotationStatusId", mQuotationStatusId);
				quotation.put("CreatedDate", ""+mCurrentDate);
				quotation.put("IsExternalProduct", "false"); // static false.
				
				quotation.put("Buyer", Buyer);
				quotation.put("Manufacturer", Manufacturer);
				quotation.put("Product", Product);
				quotation.put("ChatData", ChatData);
				quotation.put("Subject", mUserSubject);
				jsonObject.put("quotation", quotation);
				
				json = jsonObject.toString();
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
	
	private Boolean uploadFiles(String mFileName,String mFilePath) {
		boolean isValid=false;
		byte[] bytes = null ;
		String url = "";
		File file = null;
		try {
			url =webServices.FILEUPLOAD;// "http://vsuppliervm.cloudapp.net:5132/FileAttachmentService.svc/FileUpload/";
			file = new File(mFilePath);         
		}catch(Exception e) {
			e.printStackTrace();
		}
		/*try {
		FileInputStream fInputStream = new FileInputStream(file);
		bytes = new byte[(int) file.length()];
		fInputStream.read(bytes, 0, (int)file.length());
		fInputStream.close();
		} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}*/
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
		     
		        String result;
		try {
		        ByteArrayInputStream instream = new ByteArrayInputStream(bytes);          
		       HttpClient httpclient = new DefaultHttpClient();
		       HttpPost httppost = new HttpPost(url);
		       InputStreamEntity reqEntity = new InputStreamEntity(instream,bytes.length);
		       httppost.setEntity(reqEntity);
		       reqEntity.setContentType("binary/octet-stream");
		      
		       httppost.setHeader("fileName", ""+mFilePath);
		       reqEntity.setChunked(true); // Send in multiple parts if needed
		       reqEntity.setContentEncoding("utf-8");
		       HttpResponse response = httpclient.execute(httppost);
		       HttpEntity entity = response.getEntity();             
		       result = EntityUtils.toString(entity);          
		       int statusCode = response.getStatusLine().getStatusCode();
		       Log.d("statusCode",""+statusCode);
		       if(statusCode == 404){
		        return false;
		       }
		       
		       /*String[] mResult=mParserClass.parserDocumentID(result,getApplicationContext());
		       if(mResult[0].equalsIgnoreCase("false")){
		       }else{
		        mDocumentId=mResult[1];
		       }
		      
		      // mDocumentId=result.replace("\"", "");
		      
		       System.out.println(""+mDocumentId);*/
		       isValid=true;
		    } catch (Exception e) {
		    e.printStackTrace();
		    result = "";
		    Log.e("Exception in AsyncTask-doInBackgroung", e.toString());
		    } 
		       // mDocumentId=result.replace("\"", "");
		       // System.out.println(""+mDocumentId);
		return isValid;
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

}
