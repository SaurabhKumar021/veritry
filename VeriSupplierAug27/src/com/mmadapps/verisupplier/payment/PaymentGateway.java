package com.mmadapps.verisupplier.payment;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.Environment;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.TransactionResponse.TransactionStatus;
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
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.placeorder.MakePaymentNewActivity;
public class PaymentGateway {
	
	CitrusClient citrusClient;
	PaymentType.PGPayment pgPayment = null;
	public NetbankingOption netbankingOption;
	Context context;

	private static final java.lang.String BILL_URL = "http://chatappsrvropfr.cloudapp.net/citrus/BillGenerator.php";
	private static final java.lang.String DEBIT_CARD_HOLDER_NAME = "HARI PRASAD";
	private static final java.lang.String DEBIT_CARD_HOLDER_NUMBER = "5326760126549034";
	private static final java.lang.String DEBIT_CARD_CVV = "556";
	private static final java.lang.String DEBIT_CARD_EXPIRY_MONTH = "08";
	private static final java.lang.String DEBIT_CARD_EXPIRY_YEAR = "24";
	
	public PaymentGateway(final Context con) {
		super();
		this.context = con;
		
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
				//transactionResponse.g
				
				
				VerisupplierUtils.mPaymentStatus.setmStatus(""+transactionResponse.getTransactionStatus());
				VerisupplierUtils.mPaymentStatus.setmStatusJson(""+transactionResponse.getJsonResponse());
				Toast.makeText(context.getApplicationContext(),"Payment succesfull", Toast.LENGTH_LONG).show();
			}

			@Override
			public void error(CitrusError error) {
				//error.
				Log.e("error","payment failure" + error.getMessage());
				Log.e("error","payment failure" + error.getStatus());
				
				VerisupplierUtils.mPaymentStatus.setmStatus("Failure");
				VerisupplierUtils.mPaymentStatus.setmStatusJson("error json");
				
				Toast.makeText(context.getApplicationContext(),"Payment failure", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	private void callBankList() {
		// TODO Auto-generated method stub
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
		 citrusClient = CitrusClient.getInstance(context.getApplicationContext()); // Activity Context
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
		 citrusClient = CitrusClient.getInstance(context.getApplicationContext()); // Activity Context
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
			pgPayment = new PaymentType.PGPayment(amount, BILL_URL,
					debitCardOption, new CitrusUser(
							"hariprasad@mmadapps.com", "8861099488"));
		} catch (CitrusException e) {
			e.printStackTrace();

			Log.e("error", "eror");
		}
	}

	private void initCitrus() {
		citrusClient = CitrusClient.getInstance(context.getApplicationContext());
		citrusClient.init("5gkf0ij3br-signup",
				"c963e11a37703f523b33b3ee36dbc7b1", "5gkf0ij3br-signin",
				"f140bd5794c7294ea7875eddaacf3bea", "5gkf0ij3br",
				Environment.SANDBOX);
	}
	


}
