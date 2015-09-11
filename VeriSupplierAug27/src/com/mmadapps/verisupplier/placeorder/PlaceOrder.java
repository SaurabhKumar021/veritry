package com.mmadapps.verisupplier.placeorder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.QuickOrderResult;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;

public class PlaceOrder extends AsyncTask<Void, Void, Boolean>{

	private Context mContext;
	private QuickOrderResult orderResult;
	private ProgressDialog pDialog;
	private WebServices webServices;
	private JsonParserClass jsonParserClass;
	private String mManufacturerId = null, mProductId = null, mQuotationId = null, mCustomerId =null;
	private boolean isQuickOrder = false;
	String mResult[] = new String[2];
	private String TAG = "com.mmadapps.verisupplier.placeorder class";
	
	public PlaceOrder(Context mContext, boolean isQuickOrder, String mCustomerId, String mManufacturerId, String mProductId){
		this.mContext = mContext;
		this.isQuickOrder = isQuickOrder;
		this.mCustomerId = mCustomerId;
		this.mManufacturerId = mManufacturerId;
		this.mProductId = mProductId;
	}
	public PlaceOrder(Context mContext, boolean isQuickOrder, String mQuotationId){
		this.mContext = mContext;
		this.isQuickOrder = isQuickOrder;
		this.mQuotationId = mQuotationId;
	}
	@Override
	protected void onPreExecute() {
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("please wait...");
		pDialog.show();
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		super.onPreExecute();
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		if(isQuickOrder){
			GetQuoteDetailsForQuickOrder("GetQuoteDetailsForQuickOrderResult");
		}else{
			GetQuoteDetailsForQuickOrder("GetQuoteDetailsForOrderResult");
		}
		return postPlaceOrder();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if(pDialog != null && pDialog.isShowing()){
			pDialog.cancel();
		}
		if(result){
			// show the pop up result
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View view = inflater.inflate(R.layout.popup_orderconfirmation, null, false);
			final AlertDialog ad = new AlertDialog.Builder(mContext).setView(view).create();
			ad.show();
			ad.setCancelable(false);
			ad.setCanceledOnTouchOutside(false);
			TextView vT_Ok = (TextView) view.findViewById(R.id.vT_OK);
			TextView vT_OC_thankYou = (TextView) view.findViewById(R.id.vT_OC_thankYou);
			TextView vT_OC_resultText = (TextView) view.findViewById(R.id.vT_OC_resultText);
			TextView vT_OC_orderNumberLabel = (TextView) view.findViewById(R.id.vT_OC_orderNumberLabel);
			TextView vT_OC_orderNumber = (TextView) view.findViewById(R.id.vT_OC_orderNumber);
			if(mResult[0].equalsIgnoreCase("true")){
				vT_OC_thankYou.setText("Order Failed!");
				vT_OC_resultText.setText(""+mResult[1]);
				vT_OC_orderNumberLabel.setVisibility(View.GONE);
				vT_OC_orderNumber.setVisibility(View.GONE);
			}else{
				vT_OC_orderNumber.setText(""+mResult[1]);
			}
			vT_Ok.setOnClickListener(new OnClickListener() {
						
				@Override
				public void onClick(View v) {
					if(ad != null && ad.isShowing()){
						ad.cancel();
					}
					/*if(MakePaymentActivity.pop != null){
						MakePaymentActivity.pop.finish();
					}*/
					
				}
			});
		}else{
			Toast.makeText(mContext, "Order Failure", Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}
	

	public Boolean GetQuoteDetailsForQuickOrder(String mJsonObjectName) {
		boolean isValid = false;
		String result = null;
		String mParam = null;
		String mMethodName = null;
		webServices = new WebServices();
		
		if(isQuickOrder){
			mParam = mCustomerId+"/"+mManufacturerId+"/"+mProductId;
			mMethodName = "GetQuoteDetailsForQuickOrder/";
		}
		else{
			mParam = mQuotationId;
			mMethodName = "GetQuoteDetailsForOrder/";
		}
		result = webServices.CallWebHTTPBindingService(ApiType.GetQuoteDetailsForQuickOrder, mMethodName, mParam);
		if(result == null || result.length() == 0){	
		}else{
			jsonParserClass = new JsonParserClass();
			orderResult = jsonParserClass.parseQuickOrderResult(result, mJsonObjectName);
			if(orderResult != null){
				isValid = true;
			}
		}
		return isValid;
	}

	private Boolean postPlaceOrder() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		try{
			String jsonData = null;
			if(orderResult == null){
				Log.e(TAG, "orderResult is null");
				return false;
			}else{
				jsonData = createJson();
			}
			if(jsonData == null || jsonData.length() == 0){
			}else{
				HttpClient client = new DefaultHttpClient();  
				String postURL = webServices.SAVEORDER;//"http://vsuppliervm.cloudapp.net:5132/OrderService.svc/SaveOrder";
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
					
				}else{
					mResult=jsonParserClass.parseSaveOrderResult(result);
					if(mResult[0] != null && mResult[1] != null){
						return true;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	private String createJson() {
		String json = null;
		try{
			JSONObject jsonObject = new JSONObject();
			JSONObject jProductOrder = new JSONObject();
			JSONObject jAddress = new JSONObject();
			//Address
			//jAddress.put("", ); //"10");
			jAddress.put("Address1", orderResult.getmAddress()); //"BTm");
			jAddress.put("Address2","Arikere");
			jAddress.put("City",""+orderResult.getmCity()); //"Vijaywada");
			jAddress.put("Company",null);
			jAddress.put("CountryId",null);
			jAddress.put("CountryName",orderResult.getmCountry()); //"India");
			jAddress.put("Email",""+orderResult.getmEmail()); //"Chiranjeevi@gmail.com");
			jAddress.put("FaxNumber",orderResult.getmFaxNumber()); //"165484784694846");
			jAddress.put("FirstName",orderResult.getmFirstName()); //"Chiranjeevi");
			jAddress.put("LastName","Chiranjeevi");
			jAddress.put("PhoneNumber",orderResult.getmPhoneNumber()); //"7896541235");
			jAddress.put("StateId",null);
			jAddress.put("StateName",orderResult.getmState()); //"andhra pradesh");
			jAddress.put("ZipCode",orderResult.getmZip()); //"446899");
			//Order details
			jProductOrder.put("BillingAddressId", orderResult.getmBillingAddressId());
			jProductOrder.put("CreatedBy", mCustomerId);
			jProductOrder.put("CreatedDate","/Date(-62135596800000)/");
			jProductOrder.put("CustomerId", mCustomerId);
			jProductOrder.put("CustomerIp","");
			jProductOrder.put("ManufacturerId", mManufacturerId);
			jProductOrder.put("ModifiedBy", mCustomerId);
			jProductOrder.put("ModifiedDate","/Date(-62135596800000)/");
			jProductOrder.put("OfferedPrice",orderResult.getmOfferedPrice());
			jProductOrder.put("OrderGuid","00000000-0000-0000-0000-000000000000");
			jProductOrder.put("OrderNumber","");
			jProductOrder.put("OrderProductCost",orderResult.getmOfferedPrice());
			jProductOrder.put("OrderProductDiscount",0);
			jProductOrder.put("OrderProductTax",0);
			jProductOrder.put("OrderQuantity",orderResult.getmOrderQuantity());
			jProductOrder.put("OrderStatusId",0);
			jProductOrder.put("PaymentId",0);
			jProductOrder.put("ProductId",mProductId);
			jProductOrder.put("QuotationId", orderResult.getmQuotationId());
			jProductOrder.put("TotalProductCost",orderResult.getmTotalProductCost());
			jProductOrder.put("ServiceGroupType",VerisupplierUtils.mServiceGroupTypeId);
		
	
			jProductOrder.put("Address", jAddress);
			jsonObject.put("order", jProductOrder);
			
			json = jsonObject.toString();
		}catch(Exception e){
			e.printStackTrace();
			return null;
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
