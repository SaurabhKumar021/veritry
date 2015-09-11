package com.mmadapps.verisupplier.trademanager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.mmadapps.verisupplier.leftmenu.Myopenquoate;

public class CreateChannelActivity extends BaseActionBarActivity implements
		OnClickListener {
	// added
	public Uri mImageCaptureUri;
	public AlertDialog dialog;
	public static final int PICK_FROM_CAMERA = 1;
	public static final int PICK_FROM_FILE = 2;
	String attachimageUrl;
	String mFileName;
	String mFilePath = "";
	ProgressDialog pdLoading = null;
	int prod_image[];
	public static List<String> myList;
	ListView vL_cc_channel_logo;
	Channelogoadapter cAdapter;
	String result = "";
	ListView attachmentList;
	Boolean mvalue = false;
	TextView addchennalimage;
	// Form components
	EditText vE_add_warehousename, vE_add_street, vE_add_city, vE_add_country,
			vE_add_web;
	ImageView vI_nw_checkmark;

	// service
	WebServices webServices;
	JsonParserClass jsonParserClass;

	BaseActionBarActivity actionBarActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_channel);
		initControl();

	}

	private void initControl() {
		// TODO Auto-generated method stub
		actionBarActivity.vUserTitel.setText("New Channel");
		actionBarActivity.vUserTitel.setTextColor(Color.parseColor("#4F4F4F"));
		actionBarActivity.vUserSubTitle.setVisibility(View.GONE);
		actionBarActivity.qrscan_image.setVisibility(View.GONE);
		vL_cc_channel_logo = (ListView) findViewById(R.id.vL_cc_channel_logo);
		addchennalimage = (TextView) findViewById(R.id.vT_cc_addchannel);
		addchennalimage.setOnClickListener(this);
		cAdapter = new Channelogoadapter();
		vL_cc_channel_logo.setAdapter(cAdapter);
		myList = new ArrayList<String>();
		// services
		jsonParserClass = new JsonParserClass();
		webServices = new WebServices();
		// Form components
		vE_add_warehousename = (EditText) findViewById(R.id.vE_add_warehousename);
		vE_add_street = (EditText) findViewById(R.id.vE_add_street);
		vE_add_city = (EditText) findViewById(R.id.vE_add_city);
		vE_add_country = (EditText) findViewById(R.id.vE_add_country);
		vE_add_web = (EditText) findViewById(R.id.vE_add_web);

		vI_nw_checkmark = (ImageView) findViewById(R.id.vI_nw_checkmark);

		vI_nw_checkmark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callCreateChannel();
			}
		});

		actionBarActivity.vBackView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}

		});
	}

	private void callCreateChannel() {
		// TODO Auto-generated method stub
		try {
			String jsonData = null;
			if (validate_form()) {
				jsonData = createJson();
				
				
				Log.e("jsonchannel", jsonData);
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
					Log.e("jsonchannel", "channel Created");
					Toast.makeText(getApplicationContext(), "Channel Created", Toast.LENGTH_LONG).show();
					finish();
				} else {
					Log.e("jsonchannel", "channel Creation failed");
					Toast.makeText(getApplicationContext(), "Error in channel creation", Toast.LENGTH_LONG).show();
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
				String postURL = webServices.CREATECHANNEL;// "http://vsuppliervm.cloudapp.net:5132/QuotationService.svc/UpdateQuotation";
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
					String rslt = jsonParserClass
							.parseCreateChannelResult(result);
					if (rslt != null && rslt.equalsIgnoreCase("true")) {
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
			JSONObject channelJson = new JSONObject();
			channelJson.put("ChannelName", vE_add_warehousename.getText());
			channelJson.put("ChannelAddress", vE_add_street.getText());
			channelJson.put("City", vE_add_city.getText());
			channelJson.put("Country", vE_add_country.getText());
			channelJson.put("WebUrl", vE_add_web.getText());
			channelJson
					.put("PictureURL",
							"http://logok.org/wp-content/uploads/2014/09/Alibaba_group-logo.png");
			jsn.put("channel", channelJson);
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
		case R.id.vT_cc_addchannel:
			fillContactImage();

			break;

		default:
			break;
		}

	}

	private class Channelogoadapter extends BaseAdapter {

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
						R.layout.adapter_cwimages, parent, false);
			}

			TextView vT_cwi_attachmentname = (TextView) convertView
					.findViewById(R.id.vT_cwi_attachmentname);
			ImageView vI_cwi_cancelattach = (ImageView) convertView
					.findViewById(R.id.vI_cwi_cancelattach);
			String[] split = CreateChannelActivity.myList.get(arg0).split("/");
			mFileName = split[split.length - 1];
			vT_cwi_attachmentname.setText(mFileName);

			vI_cwi_cancelattach.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
			return convertView;
		}

	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
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
					Intent intent = new Intent(CreateChannelActivity.this,
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

	private Boolean uploadFiles(String mFileName, String mFilePath) {
		Log.e("jsonchannel", "called");
		boolean isValid = false;
		byte[] bytes = null;
		String url = "";
		File file = null;
		try {
			url = webServices.FILEUPLOAD;// "http://vsuppliervm.cloudapp.net:5132/FileAttachmentService.svc/FileUpload/";
			file = new File(mFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum); // no doubt here is 0
					System.out.println("read " + readNum + " bytes,");
				}
			} catch (IOException ex) {
			}
			bytes = bos.toByteArray();

		} catch (Exception e) {
			Log.d("Exception", "Exception");
			e.printStackTrace();
		}
		try {
			ByteArrayInputStream instream = new ByteArrayInputStream(bytes);
			HttpClient httpclient = new DefaultHttpClient();
			url += "/" + bytes.length;
			HttpPost httppost = new HttpPost(url);
			InputStreamEntity reqEntity = new InputStreamEntity(instream,
					bytes.length);
			httppost.setEntity(reqEntity);
			reqEntity.setContentType("binary/octet-stream");

			httppost.setHeader("Content-type", "application/octet-stream");
			reqEntity.setChunked(true); // Send in multiple parts if needed
			reqEntity.setContentEncoding("utf-8");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);

			if (result == null || result.length() == 0) {
				isValid = false;
				Log.e("jsonchannel", "imageprob");
			} else {
				attachimageUrl = attachimageUrl + ","
						+ jsonParserClass.parseAttachImageUrl(result);
				Log.e("jsonchannel", attachimageUrl);
				isValid = true;
			}

			int statusCode = response.getStatusLine().getStatusCode();
			Log.d("statusCode", "" + statusCode);
			if (statusCode != 200) {
				return false;
			}
			isValid = true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Exception in AsyncTask-doInBackgroung", e.toString());
		}
		return isValid;
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

	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// if(myList.size()>0 && mvalue==false)
	// {
	// mvalue=true;
	//
	//
	// }
	// else if (mvalue==true) {
	// cAdapter.notifyDataSetChanged();
	// }
	//
	//
	// }

}
