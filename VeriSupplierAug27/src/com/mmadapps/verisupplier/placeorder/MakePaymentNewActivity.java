package com.mmadapps.verisupplier.placeorder;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.Environment;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.classes.CitrusException;
import com.citrus.sdk.classes.Month;
import com.citrus.sdk.classes.Year;
import com.citrus.sdk.payment.CreditCardOption;
import com.citrus.sdk.payment.DebitCardOption;
import com.citrus.sdk.payment.MerchantPaymentOption;
import com.citrus.sdk.payment.NetbankingOption;
import com.citrus.sdk.payment.PaymentType;
import com.citrus.sdk.response.CitrusError;
import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.customservices.CustomOrderDetailsActivity;
import com.mmadapps.verisupplier.inspection.InspectionOrderDetailsActivity;
import com.mmadapps.verisupplier.products.ProductOrderDetailsActivity;
import com.mmadapps.verisupplier.shipping.ShippingOrderDetailsActivity;
import com.mmadapps.verisupplier.warehouse.WarehouseOrderDetailsActivity;

public class MakePaymentNewActivity  extends BaseActionBarActivity{
	
	
	TextView makepayment;
	LinearLayout makepayment_layout;
	
	private ProgressDialog pdLoading=null;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	String mParam;
	String mQuotationId;
	//private QuickOrderResult orderResult;
	String mResult[] = new String[2];
	String mOrderNumber;
	UserDetails mUserDetails=new UserDetails();
	String mUserId;
	
	CitrusClient citrusClient;
	PaymentType.PGPayment pgPayment = null;
	public NetbankingOption netbankingOption;

	private static final java.lang.String BILL_URL = "http://chatappsrvropfr.cloudapp.net/citrus/BillGenerator.php";
	private static final java.lang.String DEBIT_CARD_HOLDER_NAME = "HARI PRASAD";
	private static final java.lang.String DEBIT_CARD_HOLDER_NUMBER = "5326760126549034";
	private static final java.lang.String DEBIT_CARD_CVV = "556";
	private static final java.lang.String DEBIT_CARD_EXPIRY_MONTH = "08";
	private static final java.lang.String DEBIT_CARD_EXPIRY_YEAR = "24";
	
	
	EditText card_number,cardholder_name,card_expirymonth,card_expiryyear,card_cvv;
	TextView cardtype;
	
	String mCardNumber;
	String mCardType;
	String mCardHolderName;
	String mCardExpiryMonth;
	String mCardExpiryYear;
	String mCardCVC;
	
	JSONObject jObj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_makepayment);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.vBuyerImage.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Payment");
		
		getmUserId();
		initializeView();
		
	}



	private void getmUserId() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mUserDetails=helper.getUserDetails();
		mUserId=mUserDetails.getmUserID();
		helper.close();
	}

	private void initializeView() {
		makepayment_layout=(LinearLayout)findViewById(R.id.vL_makepaymentlayout);
		makepayment=(TextView)findViewById(R.id.amp_makepaytext);
		
		card_number=(EditText)findViewById(R.id.vE_amp_cardnumber);
		cardtype=(TextView)findViewById(R.id.vT_amp_cardtype);
		cardholder_name=(EditText)findViewById(R.id.vE_amp_card_holdername);
		card_expirymonth=(EditText)findViewById(R.id.vE_amp_cardexpiry_month);
		card_expiryyear=(EditText)findViewById(R.id.vE_amp_cardexpiry_year);
		card_cvv=(EditText)findViewById(R.id.vE_amp_card_cvv);
		
		setValues();
		
		makepayment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCardNumber==null || mCardNumber.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the card number", Toast.LENGTH_SHORT).show();
				}else if(mCardHolderName==null || mCardHolderName.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the cardholder name", Toast.LENGTH_SHORT).show();
				}else if(mCardExpiryMonth==null || mCardExpiryMonth.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the card expiry month", Toast.LENGTH_SHORT).show();
				}else if(mCardExpiryYear==null || mCardExpiryYear.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the card expiry year", Toast.LENGTH_SHORT).show();
				}else if(mCardCVC==null || mCardCVC.length()==0){
					Toast.makeText(getApplicationContext(), "Please enter the card CVC", Toast.LENGTH_SHORT).show();
				}else{
					callpaymentMethod();
				}
			}
		});
				
	}
	
	private void setValues() {
		card_number.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCardNumber=card_number.getText().toString().trim();
				if(mCardNumber.startsWith("5")){
					cardtype.setText("MasterCard");
				}else{
					cardtype.setText("VISA Card");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		
		
		cardholder_name.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCardHolderName=cardholder_name.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		card_expirymonth.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCardExpiryMonth=card_expirymonth.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		card_expiryyear.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCardExpiryYear=card_expiryyear.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		card_cvv.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCardCVC=card_cvv.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
	}



	public void callMethod(){
		if(VerisupplierUtils.mPaymentStatus.getmStatus().equalsIgnoreCase("SUCCESSFUL")){
			new AsyncPlaceOrder().execute();
			Log.e("METHOD", "DONE");
		}else{
			Log.e("METHOD", "NOT DONE");
		}
	}
	

	
	private class AsyncPlaceOrder extends AsyncTask<Void, Void, Boolean>{
		
	

		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(MakePaymentNewActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			getOrderDetails();
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
					Intent placeOrderIntent=new Intent(MakePaymentNewActivity.this,ProductOrderDetailsActivity.class);
					placeOrderIntent.putExtra("FROMACTIVITY", VerisupplierUtils.mFromActivity);
					startActivity(placeOrderIntent);
					overridePendingTransition(0, 0);				
				}else if(VerisupplierUtils.mFromActivity.equalsIgnoreCase("inspectionplaceorder")){
					Intent placeOrderIntent=new Intent(MakePaymentNewActivity.this,InspectionOrderDetailsActivity.class);
					placeOrderIntent.putExtra("FROMACTIVITY", VerisupplierUtils.mFromActivity);
					startActivity(placeOrderIntent);
					overridePendingTransition(0, 0);
				}else if(VerisupplierUtils.mFromActivity.equalsIgnoreCase("customplaceorder")){
					Intent placeOrderIntent=new Intent(MakePaymentNewActivity.this,CustomOrderDetailsActivity.class);
					placeOrderIntent.putExtra("FROMACTIVITY", VerisupplierUtils.mFromActivity);
					startActivity(placeOrderIntent);
					overridePendingTransition(0, 0);
				}else if(VerisupplierUtils.mFromActivity.equalsIgnoreCase("shippingplaceorder")){
					Intent placeOrderIntent=new Intent(MakePaymentNewActivity.this,ShippingOrderDetailsActivity.class);
					placeOrderIntent.putExtra("FROMACTIVITY", VerisupplierUtils.mFromActivity);
					startActivity(placeOrderIntent);
					overridePendingTransition(0, 0);
				}else if(VerisupplierUtils.mFromActivity.equalsIgnoreCase("warehouselaceorder")){
					Intent placeOrderIntent=new Intent(MakePaymentNewActivity.this,WarehouseOrderDetailsActivity.class);
					placeOrderIntent.putExtra("FROMACTIVITY", VerisupplierUtils.mFromActivity);
					startActivity(placeOrderIntent);
					overridePendingTransition(0, 0);
					
				}
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Order not created", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	
	}
	
	
	
	/*public void getOrderData() {
		webServices = new WebServices();
		jsonParserClass=new JsonParserClass();
		mQuotationId=VerisupplierUtils.mQuotationId;
		mParam = mQuotationId;
		String result = webServices.CallWebHTTPBindingService(ApiType.GetQuoteDetailsForQuickOrder, "GetQuoteDetailsForOrder/", mParam);
		if(result == null || result.length() == 0){	
		}else{
			jsonParserClass = new JsonParserClass();
			orderResult = jsonParserClass.parseQuickOrderResult(result, "GetQuoteDetailsForOrderResult");
			if(orderResult != null){
			}
		}
	}*/

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
				jsonData=cerateUpdatePaymentJson();
				
				//jsonData = createJson();
			}if(jsonData == null || jsonData.length() == 0){
			}else{
				HttpClient client = new DefaultHttpClient();  
				String postURL = webServices.UPDATEPAYMENT;
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
					mResult=jsonParserClass.parseUpdatePaymentDetail(result);
					if(mResult[0].equalsIgnoreCase("false")){
						//mOrderNumber=mResult[1];
						isValid=true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isValid;
	}


	public void getOrderDetails() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetOrderDetail, "GetOrderDetail/",VerisupplierUtils.mOrderId);
		if (resultOutparam == null || resultOutparam.length() == 0) {
		} else {
			VerisupplierUtils.mOrderDetails=jsonParserClass.parseOrderDetailsResult(resultOutparam);
		}
	}



	private String cerateUpdatePaymentJson() {
		String json = null;
		try {
			
			if(VerisupplierUtils.mPaymentStatus.getmStatus().equalsIgnoreCase("SUCCESSFUL")){
				JSONObject jsonObject = new JSONObject();
				JSONObject paymentObject=new JSONObject();
				paymentObject.put("TxId", VerisupplierUtils.mPaymentStatus.getmTxId());
				paymentObject.put("TxStatus", VerisupplierUtils.mPaymentStatus.getmStatus());
				paymentObject.put("TxStatusId", "1");
				paymentObject.put("TxMsg", VerisupplierUtils.mPaymentStatus.getmMessage());
				paymentObject.put("Amount", VerisupplierUtils.mPaymentStatus.getmTransactionAmount());
				paymentObject.put("PgTxnNo", VerisupplierUtils.mPaymentStatus.getmPgTxnNo());
				paymentObject.put("IssuerRefNo", VerisupplierUtils.mPaymentStatus.getmIssuerRefNo());
				paymentObject.put("AuthIdCode", VerisupplierUtils.mPaymentStatus.getmAuthIdCode());
				paymentObject.put("PaymentMode", VerisupplierUtils.mPaymentStatus.getmPaymentMode());
				paymentObject.put("MaskedCardNumber", VerisupplierUtils.mPaymentStatus.getmMaskedCardNumber());
				paymentObject.put("TxnDateTime", VerisupplierUtils.mPaymentStatus.getmTransactionDateTime());
				paymentObject.put("CardType", VerisupplierUtils.mPaymentStatus.getmCardType());
				paymentObject.put("TransactionId", VerisupplierUtils.mPaymentStatus.getmTransactionId());
				paymentObject.put("Currency", VerisupplierUtils.mPaymentStatus.getmCurrency());
				paymentObject.put("TxGateway", VerisupplierUtils.mPaymentStatus.getmTransactionGateway());
				paymentObject.put("Signature", VerisupplierUtils.mPaymentStatus.getmSignature());
				paymentObject.put("TxnType", VerisupplierUtils.mPaymentStatus.getmTxnType());
				paymentObject.put("TxRefNo", VerisupplierUtils.mPaymentStatus.getmTxRefNo());
				//paymentObject.put("Address", VerisupplierUtils.mPaymentStatus.getmAddress());
				paymentObject.put("Response", VerisupplierUtils.mPaymentStatus.getmStatusJson());
				paymentObject.put("OrderId", VerisupplierUtils.mOrderId);
				jsonObject.put("payment", paymentObject);
				
				json = jsonObject.toString();
			}else{
				JSONObject jsonObject = new JSONObject();
				JSONObject paymentObject=new JSONObject();
				paymentObject.put("TxId", "");
				paymentObject.put("TxStatus", VerisupplierUtils.mPaymentStatus.getmStatus());
				paymentObject.put("TxStatusId", "2");
				paymentObject.put("TxMsg", "");
				paymentObject.put("Amount", "");
				paymentObject.put("PgTxnNo","");
				paymentObject.put("IssuerRefNo", "");
				paymentObject.put("AuthIdCode", "");
				paymentObject.put("PaymentMode", "");
				paymentObject.put("MaskedCardNumber", "");
				paymentObject.put("TxnDateTime", "");
				paymentObject.put("CardType", "");
				paymentObject.put("TransactionId","");
				paymentObject.put("Currency", "");
				paymentObject.put("TxGateway", "");
				paymentObject.put("Signature", "");
				paymentObject.put("TxnType", "");
				paymentObject.put("TxRefNo", "");
				paymentObject.put("Address", "");
				paymentObject.put("Response", "");
				paymentObject.put("OrderId", "");
				jsonObject.put("payment", "");
				
				json = jsonObject.toString();
			}
			
			Log.e("JSON", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
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
	
	private void callpaymentMethod() {
		initCitrus();
		
		callBankList();

		switch (1) {
		case 1:
			debitCard();
			break;
		case 2:
			
			creditCard();
			
			break;
		case 3:
			
			netPayment();
			
			break;

		}

		citrusClient.pgPayment(pgPayment,new Callback<TransactionResponse>() {
			
			@Override
			public void success(TransactionResponse transactionResponse) {
				Log.e("error", "payment success"+ transactionResponse.getMessage());
				Log.e("error", "payment success json"+ transactionResponse.getResponseCode());
				Log.e("error", "payment success json"+ transactionResponse.getJsonResponse());
				Log.e("error","payment success status" + transactionResponse.getTransactionStatus());
				
				//transactionResponse.getPaymentMode();
				
				
				VerisupplierUtils.mPaymentStatus.setmStatus(""+transactionResponse.getTransactionStatus());
				VerisupplierUtils.mPaymentStatus.setmStatusJson(""+transactionResponse.getJsonResponse());
				VerisupplierUtils.mPaymentStatus.setmAddress(""+transactionResponse.getCitrusUser().getAddress());
				VerisupplierUtils.mPaymentStatus.setmMessage(""+transactionResponse.getMessage());
				VerisupplierUtils.mPaymentStatus.setmTransactionAmount(""+transactionResponse.getTransactionAmount().getValue());
				VerisupplierUtils.mPaymentStatus.setmPgTxnNo(""+transactionResponse.getTransactionDetails().getPgTxnNo());
				VerisupplierUtils.mPaymentStatus.setmIssuerRefNo(""+transactionResponse.getTransactionDetails().getIssuerRefNo());
				VerisupplierUtils.mPaymentStatus.setmAuthIdCode(""+transactionResponse.getAuthIdCode());
				VerisupplierUtils.mPaymentStatus.setmPaymentMode(""+transactionResponse.getPaymentMode());
				VerisupplierUtils.mPaymentStatus.setmTransactionDateTime(""+transactionResponse.getTransactionDetails().getTransactionDateTime());
				VerisupplierUtils.mPaymentStatus.setmTransactionId(""+transactionResponse.getTransactionDetails().getTransactionId());
				VerisupplierUtils.mPaymentStatus.setmCurrency(""+transactionResponse.getTransactionAmount().getCurrency());
				VerisupplierUtils.mPaymentStatus.setmTransactionGateway(""+transactionResponse.getTransactionDetails().getTransactionGateway());
				VerisupplierUtils.mPaymentStatus.setmSignature(""+transactionResponse.getSignature());
				VerisupplierUtils.mPaymentStatus.setmTxRefNo(""+transactionResponse.getTransactionDetails().getTxRefNo());
			
				
				try {
		            jObj = new JSONObject(transactionResponse.getJsonResponse());
		            VerisupplierUtils.mPaymentStatus.setmTxId( jObj.getString("TxId"));
		            VerisupplierUtils.mPaymentStatus.setmMaskedCardNumber( jObj.getString("maskedCardNumber"));
		            VerisupplierUtils.mPaymentStatus.setmCardType( jObj.getString("cardType"));
		            
				} 
				catch (JSONException e) 
				{
		             e.printStackTrace();
				}
				/*try{
					
			           jObj.getString("TxId");
			            jObj.getString("maskedCardNumber");
			            jObj.getString("cardType");
			           }
				catch(Exception e)
				{
					Log.e("error",e.getMessage());
				}*/
				callMethod();
				Toast.makeText(getApplicationContext(),"Payment succesfull", Toast.LENGTH_LONG).show();
			}

			@Override
			public void error(CitrusError error) {
				//error.
				Log.e("error","payment failure" + error.getMessage());
				Log.e("error","payment failure" + error.getStatus());
				
				VerisupplierUtils.mPaymentStatus.setmStatus("Failure");
				VerisupplierUtils.mPaymentStatus.setmStatusJson("error json");
				
				Toast.makeText(getApplicationContext(),"Payment failure", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	private void callBankList() {
		citrusClient.getMerchantPaymentOptions(new Callback<MerchantPaymentOption>() {
		
			@Override
			public void success(MerchantPaymentOption merchantPaymentOption) {
				for (int i = 0; i < merchantPaymentOption.getNetbankingOptionList().size(); i++) {
					Log.e("bankdetails","Bank Name: "+ merchantPaymentOption
							.getNetbankingOptionList()
							.get(i).getBankName().toString()
							+ " CID: "
							+ merchantPaymentOption
							.getNetbankingOptionList()
							.get(i).getBankCID().toString());
				}
						
			}
			
			@Override
			public void error(CitrusError error) {
				// Utils.showToast(getActivity(),
				// error.getMessage());
			}
			
		});
		
	}

	private void netPayment() {
		// TODO Auto-generated method stub
		 citrusClient = CitrusClient.getInstance(getApplicationContext()); // Activity Context
		 // No need to call init on CitrusClient if already done.
		 netbankingOption = new NetbankingOption("ICICI Bank" ,"CID001");
		 // Init Net Banking PaymentType
		 Amount amount = new Amount("7");
		 try {
			pgPayment = new PaymentType.PGPayment(amount, BILL_URL, netbankingOption, new CitrusUser("developercitrus@gmail.com","9876543210"));
		} catch (CitrusException e) {
			// TODO Auto-generated catch block
			Log.e("error", "eror");
			e.printStackTrace();
		}
		 
		
	}

	private void creditCard() {
		// TODO Auto-generated method stub
		 citrusClient = CitrusClient.getInstance(getApplicationContext()); // Activity Context
		 // No need to call init on CitrusClient if already done.
		 CreditCardOption creditCardOption = new CreditCardOption("HARI PRASAD", "4893772400492951", "123", Month.getMonth("12"), Year.getYear("18"));
		 Amount amount = new Amount("5");
		 // Init PaymentType 
		 try {
		 pgPayment = new PaymentType.PGPayment(amount, BILL_URL, creditCardOption, new CitrusUser("developercitrus@gmail.com","9876543210"));
		 } catch (CitrusException e) {
				e.printStackTrace();

				Log.e("error", "eror");
			}
		        
	}

	private void debitCard() {

		DebitCardOption debitCardOption = new DebitCardOption(
				DEBIT_CARD_HOLDER_NAME, DEBIT_CARD_HOLDER_NUMBER,
				DEBIT_CARD_CVV, Month.getMonth(DEBIT_CARD_EXPIRY_MONTH),
				Year.getYear(DEBIT_CARD_EXPIRY_YEAR));
		Amount amount = new Amount("10");
		// CitrusClient citrusClient = CitrusClient.getInstance(context);
		// Init PaymentType

		try {
			pgPayment = new PaymentType.PGPayment(amount, BILL_URL,debitCardOption, new CitrusUser("hariprasad@mmadapps.com", "8861099488"));
		} catch (CitrusException e) {
			e.printStackTrace();

			Log.e("error", "eror");
		}
	}

	private void initCitrus() {
		citrusClient = CitrusClient.getInstance(getApplicationContext());
		citrusClient.init("5gkf0ij3br-signup",
				"c963e11a37703f523b33b3ee36dbc7b1", "5gkf0ij3br-signin",
				"f140bd5794c7294ea7875eddaacf3bea", "5gkf0ij3br",
				Environment.SANDBOX);
	}

}
