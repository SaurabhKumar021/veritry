package com.mmadapps.verisupplier.trademanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.leftmenu.GalleryActivity;


public class CreateWareHouseActivity extends BaseActionBarActivity implements OnClickListener {
	public Uri mImageCaptureUri;
	public AlertDialog dialog;
	public static final int PICK_FROM_CAMERA = 1;
	public static final int PICK_FROM_FILE = 2;
	String attachimageUrl;
	String mFileName;
	String mFilePath = "";
	ProgressDialog pdLoading = null;
	int prod_image[];
	Warehouselogoadapter cAdapter;
	public static List<String> myList;
	ListView vL_cc_warehouse_logo;
	BaseActionBarActivity actionBarActivity;
	//Form Components
	ImageView vI_nw_checkmark;
	EditText vE_add_warehousename,vE_add_street,vE_add_city,vE_add_web,vE_add_country;
	// service
		WebServices webServices;
		JsonParserClass jsonParserClass;
		
	


		TextView addwarehouseimage;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_ware_house);
		
		initControl();
		
		
	}

	private void initControl() {
		// TODO Auto-generated method stub
		actionBarActivity.vUserTitel.setText("New Warehouse");
        actionBarActivity.vUserTitel.setTextColor(Color.parseColor("#4F4F4F"));
        actionBarActivity.vUserSubTitle.setVisibility(View.GONE);
        actionBarActivity.qrscan_image.setVisibility(View.GONE);
        vL_cc_warehouse_logo = (ListView) findViewById(R.id.vL_cc_warehouse_logo);
		addwarehouseimage = (TextView) findViewById(R.id.vT_nw_warehousephotoText);
		addwarehouseimage.setOnClickListener(this);
		cAdapter = new Warehouselogoadapter();
		vL_cc_warehouse_logo.setAdapter(cAdapter);
		myList = new ArrayList<String>();
		// services
		jsonParserClass = new JsonParserClass();
		webServices = new WebServices();
     //Form components
        
        vE_add_warehousename = (EditText) findViewById(R.id.vE_acwh_warehousename);
        vE_add_street = (EditText) findViewById(R.id.vE_acwh_street);
        vE_add_city = (EditText) findViewById(R.id.vE_acwh_city);
        vE_add_web = (EditText) findViewById(R.id.vE_acwh_web);
        vE_add_country=(EditText) findViewById(R.id.vE_acwh_country);
        
        // Form components
        vI_nw_checkmark = (ImageView) findViewById(R.id.vI_acwh_checkmark);
        
        vI_nw_checkmark.setOnClickListener(new OnClickListener() {

        	@Override
        	public void onClick(View v) {
        		callWareHouse();
        	}
        });
        
        actionBarActivity.vBackView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
	private void fillContactImage() {
		final String dir = Environment.getExternalStorageDirectory()
				+ "/VERISUPPLIER/";
		File newdir = new File(dir);
		newdir.mkdirs();
		final String[] items = new String[] { "Take from camera",
				"Select from gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) { // pick from
				// camera
				if (item == 0) {
					File newfile = new File(dir, "IMG_VERISUPPLIER"
							+ (System.currentTimeMillis()) + ".jpg");
					if (newfile.exists()) {
						newfile.delete();
					}
					try {
						newfile.createNewFile();
					} catch (IOException e) {
						Log.d("unable to create file", "unable to create file");
						e.printStackTrace();
					}
					mImageCaptureUri = Uri.fromFile(newfile);
					Intent cameraIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					try {
						System.out.print("Image URI is " + mImageCaptureUri);
						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								mImageCaptureUri);
						
						startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					// pick from file
					Intent intent = new Intent(CreateWareHouseActivity.this,
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
	
	
	private void callWareHouse() {
		// TODO Auto-generated method stub
		jsonParserClass = new JsonParserClass();
 		webServices = new WebServices();
		try {
			String jsonData = null;
			if (validate_form()) {
				jsonData = createJson();
				
				
				Log.e("jsonwarehouse", jsonData);
				/*Log.e("jsonchannel", myList.get(0).toString());

				String[] args = myList.get(0).toString().split("/");
				mFileName = args[args.length - 1];
				String filePath = "";
				// Log.e("jsonchannel",myList.get(0).toString());
				for (int i = 0; i < args.length - 1; i++)
					filePath = filePath + args[i] + "/";
				Log.e("jsonchannel", "fp" + filePath);
				Log.e("jsonchannel", "fp" + mFileName);*/
				
				//uploadFiles(filePath, mFileName);

				if (callService(jsonData)) {
					Log.e("jsonwarehouse", "warehouse Created");
					Toast.makeText(getApplicationContext(), "Warehouse Created", Toast.LENGTH_LONG).show();
					finish();
				} else {
					Log.e("jsonchannel", "Warehouse Creation failed");
					Toast.makeText(getApplicationContext(), "Error in Warehouse creation", Toast.LENGTH_LONG).show();
				}

			}
		} catch (Exception e) {

		}
	}

	private boolean callService(String jsonData) {
		// TODO Auto-generated method stub
		try {
			if (jsonData != null) {
				HttpClient client = new DefaultHttpClient();
				String postURL = webServices.CREATEWAREHOUSE;// "http://vsuppliervm.cloudapp.net:5132/QuotationService.svc/UpdateQuotation";
				InputStream inputStream = null;

				HttpPost httpPost = new HttpPost(postURL);

				StringEntity se = new StringEntity(jsonData, HTTP.UTF_8);
				httpPost.setEntity(se);

				httpPost.setHeader("Content-type", "application/json");
				httpPost.setHeader("Accept", "application/json");

				HttpResponse httpResponse = client.execute(httpPost);
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				Log.d("statusCode", "" + statusCode);
				if (statusCode != 200) {
					return false;
				}
				inputStream = httpResponse.getEntity().getContent();
				System.out.println("" + inputStream);
				String result = null;
				if (inputStream != null) {
					result = convertInputStreamToString(inputStream);
					Log.e("jsonchannel", result);
				}
				if (result == null || result.length() == 0) {

				} else {
					jsonParserClass = new JsonParserClass();
					String rslt = jsonParserClass.parseCreateWareHouseResult(result);
					if (rslt != null && rslt.equalsIgnoreCase("false")) {
						return true;
					}
				}
			}
		} catch (Exception e) {

		}
		return true;
	}

	private String createJson() {
		// TODO Auto-generated method stub
		String json = null;
		try {
			
			JSONObject jsn = new JSONObject();
			JSONObject warehouseJson = new JSONObject();
			JSONObject warehouseDetailsJson = new JSONObject();
			JSONObject addressJson = new JSONObject();
			
			//address object
			addressJson.put("Address1", vE_add_warehousename.getText());
			addressJson.put("StateName", "Jharkhand");
			addressJson.put("CountryName", vE_add_country.getText());
			addressJson.put("ZipCode", "803101");
			addressJson.put("City", vE_add_city.getText());
			addressJson.put("FaxNumber", "8974563210");
			addressJson.put("Email", "lens@kart.com");
			addressJson.put("PhoneNumber", "7894561230");
			
			//warehouseDetails object
			warehouseDetailsJson.put("Name", vE_add_warehousename.getText());
			warehouseDetailsJson.put("IsOwnWarehouse", "1");
			warehouseDetailsJson.put("IsActive", "1");
			warehouseDetailsJson.put("WarehouseId", "0");
			warehouseDetailsJson.put("Address", addressJson);
			
			//warehouse object
			warehouseJson.put("OperationType", "1");
			warehouseJson.put("CustomerId", "2");
			warehouseJson.put("WarehouseDetails", warehouseDetailsJson);
			
			//Master Object
			jsn.put("warehouse", warehouseJson);
			
			json = jsn.toString();
			
		} catch (Exception e) {

		}
		return json;
	}

	private boolean validate_form() {
		// TODO Auto-generated method stub
		return true;
	}

	private String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vT_nw_warehousephotoText:
			fillContactImage();

			break;

		default:
			break;
		}

	}
	
	
	private class Warehouselogoadapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (myList == null || myList.size() == 0) {
				return 0;
			} else {
				return myList.size();
			}
		}

		@Override
		public Object getItem(int arg0) {

			return arg0;
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.adapter_whimage, parent, false);
			}

			TextView vT_whi_attachmentname = (TextView) convertView
					.findViewById(R.id.vT_whi_attachmentname);
			ImageView vI_whi_cancelattach = (ImageView) convertView
					.findViewById(R.id.vI_whi_cancelattach);
			String[] split = CreateWareHouseActivity.myList.get(arg0).split("/");
			mFileName = split[split.length - 1];
			vT_whi_attachmentname.setText(mFileName);

			vI_whi_cancelattach.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
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
			try {
				System.gc();
				Uri imgUri = Uri.parse(mImageCaptureUri.getPath());
				if (filePath.length() == 0) {
					filePath = imgUri.getPath();
				}
				String[] args = filePath.split("/");
				mFileName = args[args.length - 1];
				System.out.println("mFileName---" + mFileName);
				// myList.add(mFileName);
				myList.add(filePath);
				cAdapter.notifyDataSetChanged();
			} catch (Exception e) {

			}
			break;

		case PICK_FROM_FILE:
			Log.e("return", "pf");
			if (dialog != null || dialog.isShowing())
				dialog.dismiss();
			try {
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
					// myList.add(mFileName);
					myList.add(pathNew);
					cAdapter.notifyDataSetChanged();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("return", "ex");
			}
			break;
		}
	}

	

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
