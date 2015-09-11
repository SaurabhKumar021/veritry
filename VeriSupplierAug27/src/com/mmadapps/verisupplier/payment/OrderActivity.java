package com.mmadapps.verisupplier.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.customservices.PlaceOrderCustomsActivity;
import com.mmadapps.verisupplier.inspection.PlaceOrderInspectionActivity;
import com.mmadapps.verisupplier.shipping.PlaceOrderShippingActivity;
import com.mmadapps.verisupplier.warehouse.PlaceOrderWarehouseActivity;

public class OrderActivity extends AsyncTask<Void, Void, Boolean>{
	
	private Context mContext;
	
	private ProgressDialog pdLoading;
	private WebServices webServices;
	private JsonParserClass jsonParserClass;
	String mResult[] = new String[2];
	String mOrderNumber;
	UserDetails mUserDetails=new UserDetails();
	String mUserId;
	
	
	
	public OrderActivity(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	protected void onPreExecute() {
		pdLoading = new ProgressDialog(mContext);
		pdLoading.setMessage("Please wait...");
		pdLoading.show();
		//pdLoading.setCancelable(false);
		//pdLoading.setCanceledOnTouchOutside(false);
		super.onPreExecute();
	}

	

	@Override
	protected Boolean doInBackground(Void... params) {
		return callService();
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (pdLoading != null && pdLoading.isShowing()) {
			pdLoading.cancel();
		}
		if(result){
			VerisupplierUtils.mOrderNumber=mOrderNumber;
			if(VerisupplierUtils.mFromActivity==null ||VerisupplierUtils.mFromActivity.length()==0){
				Toast.makeText(mContext, "Order is Successfull.Your Order Number "+mOrderNumber, Toast.LENGTH_SHORT).show();
			}else if(VerisupplierUtils.mFromActivity.equalsIgnoreCase("inspectionplaceorder")){
				Intent placeOrderIntent=new Intent(mContext,PlaceOrderInspectionActivity.class);
				placeOrderIntent.putExtra("FROMACTIVITY", VerisupplierUtils.mFromActivity);
				mContext.getApplicationContext().startActivity(placeOrderIntent);
				
			}else if(VerisupplierUtils.mFromActivity.equalsIgnoreCase("customplaceorder")){
				Intent placeOrderIntent=new Intent(mContext,PlaceOrderCustomsActivity.class);
				placeOrderIntent.putExtra("FROMACTIVITY", VerisupplierUtils.mFromActivity);
				mContext.getApplicationContext().startActivity(placeOrderIntent);
			}else if(VerisupplierUtils.mFromActivity.equalsIgnoreCase("shippingplaceorder")){
				Intent placeOrderIntent=new Intent(mContext,PlaceOrderShippingActivity.class);
				placeOrderIntent.putExtra("FROMACTIVITY", VerisupplierUtils.mFromActivity);
				mContext.getApplicationContext().startActivity(placeOrderIntent);
			}else if(VerisupplierUtils.mFromActivity.equalsIgnoreCase("warehouselaceorder")){
				Intent placeOrderIntent=new Intent(mContext,PlaceOrderWarehouseActivity.class);
				placeOrderIntent.putExtra("FROMACTIVITY", VerisupplierUtils.mFromActivity);
				mContext.getApplicationContext().startActivity(placeOrderIntent);
			}
			
		}else{
			Toast.makeText(mContext, "Order not created", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	public Boolean callService() {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass =new JsonParserClass();
		try {
			String jsonData = null;
			if(VerisupplierUtils.mQuotationDetails == null){
				Log.e("ERROR", "orderResult is null");
				return false;
			}else{
				jsonData = createJson();
			}if(jsonData == null || jsonData.length() == 0){
			}else{
				HttpClient client = new DefaultHttpClient();  
				String postURL = webServices.SAVEORDER;
				InputStream inputStream = null;
				
				HttpPost httpPost = new HttpPost(postURL);
				
				StringEntity se = new StringEntity(jsonData, HTTP.UTF_8);
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
				String result = null;
				if(inputStream != null){
					result = convertInputStreamToString(inputStream);
		        }
				if(result == null || result.length() == 0){
					isValid=false;
				}else{
					mResult=jsonParserClass.parseSaveOrderResult(result);
					if(mResult[0] != null && mResult[1] != null){
						mOrderNumber=mResult[1];
						isValid=true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isValid;
	}

	
	private String createJson() {
		String json = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
			Date today = new Date();
			String date = sdf.format(today);
			
			JSONObject jsonObject = new JSONObject();
			JSONObject jProductOrder = new JSONObject();
			JSONObject jAddress = new JSONObject();
			
			jProductOrder.put("QuotationId", VerisupplierUtils.mQuotationDetails.getmQuotationId());
			jProductOrder.put("ManufacturerId", VerisupplierUtils.mQuotationDetails.getmManufacturerId());
			jProductOrder.put("OrderStatusId",1);
			jProductOrder.put("OrderProductCost",VerisupplierUtils.mQuotationDetails.getmOfferedPrice());
			jProductOrder.put("OrderProductDiscount",0);
			jProductOrder.put("OrderProductTax",0);
			jProductOrder.put("PaymentId",0);
			jProductOrder.put("ModifiedBy", mUserId);
			jProductOrder.put("ModifiedDate",date);
			jProductOrder.put("TotalProductCost",VerisupplierUtils.mQuotationDetails.getmTotalProductCost());
			jProductOrder.put("CustomerId", mUserId);
			jProductOrder.put("CustomerIp","");
			jProductOrder.put("CreatedBy", mUserId);
			jProductOrder.put("CreatedDate",date);
			jProductOrder.put("OrderQuantity",VerisupplierUtils.mQuotationDetails.getmOrderQuantity());
			jProductOrder.put("ProductId",VerisupplierUtils.mQuotationDetails.getmProductId());
			jProductOrder.put("OfferedPrice",VerisupplierUtils.mQuotationDetails.getmOfferedPrice());
			jProductOrder.put("BillingAddressId", VerisupplierUtils.mQuotationDetails.getmBillingAddressId());
			
			jAddress.put("FirstName",VerisupplierUtils.mQuotationDetails.getmFirstName());
			jAddress.put("LastName",VerisupplierUtils.mQuotationDetails.getmLastName());
			jAddress.put("Email",""+VerisupplierUtils.mQuotationDetails.getmEmail());
			jAddress.put("Company",null);
			jAddress.put("CountryId",null);
			jAddress.put("StateId",null);
			jAddress.put("City",""+VerisupplierUtils.mQuotationDetails.getmCity());
			jAddress.put("Address1", VerisupplierUtils.mQuotationDetails.getmAddress()); 
			jAddress.put("Address2",VerisupplierUtils.mQuotationDetails.getmAddress2());
			jAddress.put("ZipCode",VerisupplierUtils.mQuotationDetails.getmZip());
			jAddress.put("PhoneNumber",VerisupplierUtils.mQuotationDetails.getmPhoneNumber());
			jAddress.put("FaxNumber",VerisupplierUtils.mQuotationDetails.getmFaxNumber());
			
			jProductOrder.put("Address", jAddress);
			jsonObject.put("order", jProductOrder);
			
			json = jsonObject.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
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
	

}
