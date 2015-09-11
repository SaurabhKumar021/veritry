package com.mmadapps.verisupplier.leftmenu;

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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.TwoWayView;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class Myopenquoate extends BaseActionBarActivity {
	
	public Uri mImageCaptureUri;
	public AlertDialog dialog;
	public static final int PICK_FROM_CAMERA = 1;
	public static final int PICK_FROM_FILE = 2;
	LinearLayout uploadPhoto;
	ImageView productImage;
	SharedPreferences sharedPreferences;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	String response;
	String productname,mUserMessage,email,mobile_number,quantity;
	String attachimageUrl;
	
	String mFileName;
	Spinner category, sub_category;
	Editor editor;
	String subcatstring;
	String[] subcategory_items;
	
	String mFilePath="";
	ProgressDialog pdLoading=null;
	
	String result="";
	ListView attachmentList;
	EditText authoriser_name,authoriser_email,authoriser_num,openquote_message,product_quantity;
	
	public static String authoriserName,authoriserEmail,authoriserMobile,authoriserDes,category_te,sub_category_te;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	public static String mDocumentId="";
	
	Button submit, cancel;
	ProductAdapter pAdapter;
	int prod_image[];
	
	TwoWayView productList;
	public static List<String> myList;
	List<CategoryDetails> mallCategories= new ArrayList<CategoryDetails>();	
	List<CategoryDetails> subCategory = new ArrayList<CategoryDetails>();
	
	TwoWayView twoWayView;
	private AlertDialog aDialog;
	private ProgressBar pBar;
	Helper helper;
	String key;
	UserDetails mUserDetails=new UserDetails();
	String mUserId="";
	Spinner quantityunit_spinner;
	List<Lookups> mQuantityList=new ArrayList<Lookups>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_openquote);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Open Quote");
		
		UserDetails();
		
		jsonParserClass = new JsonParserClass();
		initView();
	
		sharedPreferences = getApplicationContext().getSharedPreferences("VERISUPPLIER_ACCESS", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
		
		myList = new ArrayList<String>();
				
	    helper=new Helper(getApplicationContext());
		helper.openDataBase();
		helper.getReadableDatabase();
		mallCategories=helper.getAllCategories("");
		Log.e("size",""+ mallCategories.size());
		
		String mine="";
		String[] category_items = new String[10];
		for (int i = 0; i < mallCategories.size(); i++) {
			mine=mine+","+mallCategories.get(i).getmCategory_Name();
		}
		if(mine.length() > 0){
		String catstring=mine.substring(1);
		category_items = catstring.split(",");
		}
		
		
		final ArrayAdapter<String> adapter_category = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, category_items);
		category.setAdapter(adapter_category);
		category_te=category.getSelectedItem().toString();
		
		
		category.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                key = mallCategories.get(arg2).getmCategory_Id();
				subCategory = helper.getAllCategories(key);
				subcategory_items = new String[subCategory.size()];
				//subCategory.add(subcategoryDetails);
				
				String sub="";
				for(int i=0;i<subCategory.size();i++){
					sub = sub+","+subCategory.get(i).getmCategory_Name();
				}
				if(sub.length() > 0){
					subcatstring = sub.substring(1);
					subcategory_items = subcatstring.split(",");
				}
				ArrayAdapter<String> adapter=new ArrayAdapter<String>(Myopenquoate.this,android.R.layout.simple_spinner_dropdown_item,subcategory_items);
	            sub_category.setAdapter(adapter);
							
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
		
		
		sub_category.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int arg2, long arg3) {
				sub_category_te=sub_category.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	
			}
		});

		
	
		uploadPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fillContactImage();
				String imageUrl = sharedPreferences.getString("imageURL", "");
				if (imageUrl == null || imageUrl.equalsIgnoreCase("")
						|| imageUrl.trim().length() == 0) {

				} else {
				}
			}
		});
		
		
		productList=(TwoWayView)findViewById(R.id.product_list);
		pAdapter=new ProductAdapter();
		productList.setAdapter(pAdapter);
		productList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent newIntent=new Intent(Myopenquoate.this,ProdGridActivity.class);
				//newIntent.putExtra("IMAGENAME", ""+productImage[position]);
				startActivity(newIntent);
			}
		});
		

	}
	

    private void UserDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		helper.close();
		if(mUserDetails.getmUserID()==null || mUserDetails.getmUserID().equalsIgnoreCase("null")){
			mUserId="106";
		}else{
			mUserId=mUserDetails.getmUserID();
		}
	}


	private void initView() {
	
	uploadPhoto = (LinearLayout) findViewById(R.id.uploadPhoto);
	productImage = (ImageView) findViewById(R.id.productImage);
	category = (Spinner) findViewById(R.id.quantity_spinner);
	sub_category = (Spinner) findViewById(R.id.quantity_spinner_subCatergory);
	submit = (Button) findViewById(R.id.submit);
	//attachmentList=(ListView) findViewById(R.id.vL_endlist);
	
	authoriser_name=(EditText)findViewById(R.id.vE_name);
	authoriser_email=(EditText)findViewById(R.id.vE_email);
	authoriser_num=(EditText)findViewById(R.id.vE_num);
	openquote_message=(EditText)findViewById(R.id.vE_voq_message);
	product_quantity=(EditText)findViewById(R.id.vE_voq_quantity);
	quantityunit_spinner=(Spinner)findViewById(R.id.voq_quantityunit_spinner);
	mQuantityList=VerisupplierUtils.mQuantityList;
	quantityunit_spinner.setAdapter(new SpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mQuantityList));
	
	authoriser_name.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			authoriser_name.setError(null);
			productname=authoriser_name.getText().toString().trim();
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
		
	});
	
	authoriser_email.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			authoriser_email.setError(null);
			email=authoriser_email.getText().toString().trim();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
		
	});
	openquote_message.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			openquote_message.setError(null);
			mUserMessage=openquote_message.getText().toString().trim();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
		
	});
	
	product_quantity.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			product_quantity.setError(null);
			quantity=product_quantity.getText().toString().trim();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	});
	
	setValues();
	}


   private void setValues() {
	   authoriser_email.setText(""+mUserDetails.getmUserEmail());
	   if(mUserDetails.getmUserMobile()==null || mUserDetails.getmUserMobile().equalsIgnoreCase("null")){
		   authoriser_num.setText("");
	   }else{
		   authoriser_num.setText(""+mUserDetails.getmUserMobile());  
	   }
	  
	}


   private class ProductAdapter extends BaseAdapter{
		
	   @Override
	   public int getCount() {
		   return myList.size();
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
			   convertView =getLayoutInflater().inflate(R.layout.product_imageview, parent, false);
		   }
		   ImageView category_image=(ImageView)convertView.findViewById(R.id.prodImage);
		   //final TextView category_name=(TextView)convertView.findViewById(R.id.vT_categoryText);
		   Log.e("path", myList.get(position));
		   //Bitmap myBitmap = BitmapFactory.decodeFile(myList.get(position));
		   //category_image.setImageBitmap(myBitmap);
		   imageLoader.displayImage("file:///" + Myopenquoate.myList.get(position), category_image,options);
		   //category_image.setImageResource(prod_image[position]);
		   //category_name.setText(text[position]);
			return convertView;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("return", "ok");
		if (resultCode != RESULT_OK)
			return;
		Log.e("return", "ok1");
		switch (requestCode) {
	
		case PICK_FROM_CAMERA:
			String filePath = "";
			Log.e("return", "c1");
			if (dialog != null || dialog.isShowing())
				dialog.dismiss();
			Log.e("return", "c2");
			try{
			System.gc();
			Uri imgUri = Uri.parse(mImageCaptureUri.getPath());
			 if(filePath.length() == 0)
			 {
					filePath = imgUri.getPath();
			}
			 String[] args = filePath.split("/");
			  mFileName = args[args.length - 1];
			 System.out.println("mFileName---" + mFileName);
			 myList.add(filePath);
			 pAdapter.notifyDataSetChanged();	
			}catch (Exception e) {
				
			}
			break;
	
		case PICK_FROM_FILE:
			Log.e("return", "pf");
			if (dialog != null || dialog.isShowing())
				dialog.dismiss();
			try{
				String pathNew = data.getStringExtra("filepath");
				if (pathNew == null) {
					Toast.makeText(
							getApplicationContext(),
							"Sorry,image path not avaliable please select another image",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.e("return", pathNew);
					String[] args = pathNew.split("/");
					mFileName = args[args.length - 1];
					myList.add(pathNew);
					pAdapter.notifyDataSetChanged();
				} 
			 }   catch (Exception e) {
				e.printStackTrace();
				Log.e("return", "ex");
			}
			break;
		}
	}

	private void fillContactImage() {
		final String dir = Environment.getExternalStorageDirectory()+ "/VERISUPPLIER/";
		File newdir = new File(dir);
		newdir.mkdirs();
		final String[] items = new String[] { "Take from camera","Select from gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) { // pick from
				// camera
				if (item == 0) {
					File newfile = new File(dir, "IMG_VERISUPPLIER"+ (System.currentTimeMillis()) + ".jpg");
					if (newfile.exists()) {
						newfile.delete();
					}
					try {
						newfile.createNewFile();
					} catch(IOException e) {
						Log.d("unable to create file", "unable to create file");
						e.printStackTrace();
					}
					mImageCaptureUri = Uri.fromFile(newfile);
					Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					try {
						System.out.print("Image URI is " + mImageCaptureUri);
						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
						startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					// pick from file
					Intent intent = new Intent(Myopenquoate.this,
							GalleryActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivityForResult(intent, PICK_FROM_FILE);
				}
			}
		});
		builder.setCancelable(true);
		dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.show();
	}
		
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
			if (myList.size()==0){
				Toast.makeText(getApplicationContext(), "please select image", Toast.LENGTH_SHORT).show();
			}
			else{
				if(productname==null || productname.length()==0){
					authoriser_name.setError("Please enter product name");
				}else if(authoriser_email.getText().toString()==null || authoriser_email.getText().toString().length()==0){
					authoriser_email.setError("Please enter email address");
				}else if(authoriser_num.getText().toString().trim()==null|| authoriser_num.getText().toString().trim().length()==0){
					authoriser_num.setError("Please enter contact number");
				}else if(product_quantity.getText().toString().trim()==null || product_quantity.getText().toString().trim().length()==0){
					product_quantity.setError("Please enter the quantity");
				}else if(openquote_message.getText().toString().trim()==null || openquote_message.getText().toString().trim().length()==0){
					openquote_message.setError("Please enter the message");
				}
				else{
					productname = authoriser_name.getText().toString().trim();
					email = authoriser_email.getText().toString().trim();
					mobile_number = authoriser_num.getText().toString().trim();
					new AsyncAttachFile().execute();
					
				}
			}
			break;
			
		default:
			break;
		}
	}
	
	public  class AsyncAttachFile extends AsyncTask<String, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(Myopenquoate.this);
			pdLoading.setMessage("please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}   
			
		@Override
		protected Boolean doInBackground(String... params) {
			boolean isUploaded = false;
			if(myList != null && myList.size()>0){
				//myList.add(filePath);//mFileName+"@"+
				//int len = myList.size();
				for(int i=0; i<myList.size(); i++){
					isUploaded = uploadFiles(myList.get(i));//myList.get(1).split("@")[1],myList.get(1).split("@")[1]
				}
			}
			return sendOpenquotedetails(attachimageUrl);
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
				Toast.makeText(getApplicationContext(), "Sorry your request is not created.Please try again.", Toast.LENGTH_LONG).show();
			}
		}
	}
	           
	private boolean uploadFiles(String filepath) {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		
		boolean isValid = false;
		byte[] bytes = null ;
		String url = "";
		File file = null;
					
		if(isValid==false){
			try {
				url =webServices.FILEUPLOAD;//"http://192.168.30.65/FileAttachmentService.svc/FileUpload/"+mFileName;
				file = new File(filepath);         
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
				int statusCode = response.getStatusLine().getStatusCode();
				Log.d("statusCode",""+statusCode);
				if(statusCode != 200){
					return false;
				}
				result = EntityUtils.toString(entity);
				if(result==null || result.length()==0){
					isValid=false;
				}else{
					attachimageUrl = jsonParserClass.parseAttachImageUrl(result);
					isValid=true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = "";
				Log.e("Exception in AsyncTask-doInBackgroung", e.toString());
			} 
		}else{
		}
		return isValid;
	}
	
	private Boolean sendOpenquotedetails(String attachimageUrl) {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
		String mCurrentDate=sdf.format(cal.getTime());
		System.out.println(""+mCurrentDate);
		
		Boolean isValid = false;
		if (isValid.equals(false)) {
			String result = "";
			
			HttpClient client = new DefaultHttpClient();
			String postURL = webServices.SAVEOPENQUOTE;
			//http://192.168.30.65/QuotationService.svc/SaveOpenQuote
			//"http://192.168.30.65/QuotationService.svc/SaveOpenQuote";
			InputStream inputStream = null;
			
			HttpPost httpPost = new HttpPost(postURL);
			String json = "";
			
			JSONObject jsonObjectmain = new JSONObject();
			JSONObject jsonObject = new JSONObject();
			
			try{
				jsonObject.put("QuotationNumber", null);
				jsonObject.put("LastResponseDate", null);
				jsonObject.put("LastResponseSequenceNumber", 1);
				jsonObject.put("Subject", "product Name 11 82");
				jsonObject.put("QuotationStatusId", 0);
				jsonObject.put("QuoteStatus", null);
				jsonObject.put("CreatedDate", "2015-06-24T12:47:00+05:30");
				jsonObject.put("CategoryId", "9");
				jsonObject.put("SubCategoryId", "12");
				jsonObject.put("SubTotal", "0");
				jsonObject.put("TotalCount", 0);
				jsonObject.put("QuotationCommunicator", 0);
				jsonObject.put("LastManufacturerResponseId", 0);
				jsonObject.put("Id", 0);
				
				JSONObject manufacturesub = new JSONObject();
				manufacturesub.put("Id", 0);
				manufacturesub.put("Name", null);
				manufacturesub.put("Logo", null);
				manufacturesub.put("ContactDetails", null);
				manufacturesub.put("ProductLiteList", null);
	 
				jsonObject.put("Manufacturer", manufacturesub);
				JSONObject productsub = new JSONObject();
				productsub.put("Id", 0);
				productsub.put("ProductImage", null);
				productsub.put("ProductName", ""+productname);
				productsub.put("ShortDescription", ""+mUserMessage);
				productsub.put("ManufacturerLite", null);
				productsub.put("Ratings", 0);
				productsub.put("StockQuantity", 0);
				productsub.put("Pricing", null);
				
				jsonObject.put("Product", productsub);
				JSONObject buyersub = new JSONObject();
				buyersub.put("Name", null);
				buyersub.put("Id", mUserId);
				buyersub.put("Address", null);
				buyersub.put("City", null);
				buyersub.put("State", null);
				buyersub.put("Zip", null);
				buyersub.put("Country", null);
				buyersub.put("PhoneNumber", null);
				buyersub.put("MobileNumber", ""+mobile_number);
				buyersub.put("Email", ""+email);
				buyersub.put("Image", attachimageUrl);
				jsonObject.put("Buyer", buyersub);
	    		JSONArray chatdata = new JSONArray();
				
				for(int i=0;i<1;i++){
					JSONObject chatdataobject = new JSONObject();
					chatdataobject.put("QuotationResponseId", 0);
					chatdataobject.put("RequiredQty", 11);
					chatdataobject.put("Message", "details of product");
					chatdataobject.put("UnitsOfMeasurement", "82");
					chatdataobject.put("ResponseUserType", 1);
					chatdataobject.put("AdditionalInfo", null);
					chatdataobject.put("Attachments", "Attachments");
					chatdataobject.put("OfferedPrize", 0);
					chatdataobject.put("InCurrency", null);
					chatdataobject.put("ResponseBy", 0);
					chatdataobject.put("ResponseDate", mCurrentDate);//2105-06-24T12:47:00+05:30
					// JSONObject specsmain = new JSONObject();
					JSONObject specs = new JSONObject();
	    			specs.put("MileStones", null);
	    			JSONArray additionalinfo = new JSONArray();
	    			// for(int j=0;j<1;j++){
	    			//additionalinfo.put("0");
	    			// String addition = additionObject.getString("0");
	    			specs.put("AdditionalInfo", additionalinfo.put(0));
	    			//}
	    			chatdataobject.put("Specs", specs);
	    			// additionalinfo.put(specsmain);
	    			
	    			chatdata.put(chatdataobject);
	    			jsonObject.put("ChatData", chatdata);
	    		}
				jsonObjectmain.put("quotation", jsonObject);
			}catch(Exception e){
				e.printStackTrace();
			}	
			json = jsonObjectmain.toString();
			
			if (json == null || json.length() == 0) {
				isValid = false;
			} else {
				try {
					StringEntity entity = new StringEntity(json, HTTP.UTF_8);
					httpPost.setEntity(entity);
					
					httpPost.setHeader("Content-type", "application/json");
					httpPost.setHeader("Accept", "application/json");
					httpPost.setHeader("JWTKEY","Verisupplier.android.d2335fhfg4564hghjghjghjget45ert.1.0");
					httpPost.setHeader("OS","ANDROID");
					httpPost.setHeader("USERID",mUserDetails.getmUserID());

					HttpResponse httpResponse = client.execute(httpPost);
					int statuscode = httpResponse.getStatusLine().getStatusCode();
					if (statuscode != 200) {
						return isValid;
					}
					inputStream = httpResponse.getEntity().getContent();
					if (inputStream != null) {
						result = convertInputStreamToString(inputStream);
					}
					String response = jsonParserClass.parseSaveOpenQuotation(result);
					
					if(response.equalsIgnoreCase("true")){
						isValid = true;
					}else{
						isValid = false;
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return isValid;
	}
	
	private static String convertInputStreamToString(InputStream inputStream)throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;
		
		inputStream.close();
		return result;
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
