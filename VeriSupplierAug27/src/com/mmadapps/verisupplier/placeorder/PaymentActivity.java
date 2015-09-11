package com.mmadapps.verisupplier.placeorder;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.Environment;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.classes.CitrusException;
import com.citrus.sdk.payment.MerchantPaymentOption;
import com.citrus.sdk.payment.NetbankingOption;
import com.citrus.sdk.payment.PaymentType;
import com.citrus.sdk.response.CitrusError;
import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.beans.BankList;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;

public class PaymentActivity extends BaseActionBarActivity implements OnClickListener{
	
	LinearLayout payment_gate, payment_gate_sub;
	LinearLayout net_banking, net_banking_sub,netbankingLists_layout;
	LinearLayout debit_card, debit_card_sub;
	LinearLayout credit_card, credit_card_sub;
	LinearLayout emi_opt, emi_opt_sub;
	LinearLayout layout;

	TextView sbi_debit;
	
	LinearLayout debit_sbi,debit_hdfc,debit_icici,net_sbi,net_hdfc,net_icici,payment_sbi,payment_hdfc,payment_icici,cash_sbi,cash_hdfc,cash_icici,emi_sbi,emi_hdfc,emi_icici;
	ListView netbankingList;
	BankAdapter mBankAdapter;
	
	CitrusClient citrusClient;
	PaymentType.PGPayment pgPayment = null;
	public NetbankingOption netbankingOption;
	private static final java.lang.String BILL_URL = "http://chatappsrvropfr.cloudapp.net/citrus/BillGenerator.php";
	
	String mBankName;
	String mBankCID;
	int selectedSwitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		//BaseActionBarActivity.search_layout.setVisibility(View.GONE);
		//BaseActionBarActivity.title_layout.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Payment");
		
		initializeAllValues();
		initCitrus();
		
	}
	
	
	


	private void initCitrus() {
		citrusClient = CitrusClient.getInstance(getApplicationContext());
		citrusClient.init("5gkf0ij3br-signup",
				"c963e11a37703f523b33b3ee36dbc7b1", "5gkf0ij3br-signin",
				"f140bd5794c7294ea7875eddaacf3bea", "5gkf0ij3br",
				Environment.SANDBOX);
	}

	private void initializeAllValues() {
		net_banking = (LinearLayout) findViewById(R.id.net_banking);
		net_banking_sub = (LinearLayout) findViewById(R.id.netbanking_sub);
		net_banking.setOnClickListener(this);
		
		netbankingLists_layout=(LinearLayout)findViewById(R.id.vL_app_netbankingLists_layout);
		netbankingList=(ListView)findViewById(R.id.vI_app_netbankingLists);
		
		payment_gate = (LinearLayout) findViewById(R.id.payment_gate);
		payment_gate_sub = (LinearLayout) findViewById(R.id.payment_gate_sub);
		payment_gate.setOnClickListener(this);

		debit_card = (LinearLayout) findViewById(R.id.debit_card);
		debit_card_sub = (LinearLayout) findViewById(R.id.debit_card_sub);
		sbi_debit = (TextView) findViewById(R.id.sbi);

		sbi_debit.setOnClickListener(this);
		debit_card.setOnClickListener(this);

		credit_card = (LinearLayout) findViewById(R.id.credit_card);
		credit_card_sub = (LinearLayout) findViewById(R.id.credit_card_sub);
		credit_card.setOnClickListener(this);

		emi_opt = (LinearLayout) findViewById(R.id.emi_opt);
		emi_opt_sub = (LinearLayout) findViewById(R.id.emi_opt_sub);
		emi_opt.setOnClickListener(this);

		debit_sbi=(LinearLayout)findViewById(R.id.vL_debit_sbi);
		debit_hdfc=(LinearLayout)findViewById(R.id.vL_debit_hdfc);
		debit_icici=(LinearLayout)findViewById(R.id.vL_debit_icici);
		
		net_sbi=(LinearLayout)findViewById(R.id.vL_netban_sbi);
		net_hdfc=(LinearLayout)findViewById(R.id.vL_netban_hbfc);
		net_icici=(LinearLayout)findViewById(R.id.vL_netban_icici);
		
		payment_sbi=(LinearLayout)findViewById(R.id.vL_payment_sbi);
		payment_hdfc=(LinearLayout)findViewById(R.id.vL_payment_hdfc);
		payment_icici=(LinearLayout)findViewById(R.id.vL_payment_icici);
		
		cash_sbi=(LinearLayout)findViewById(R.id.vL_credit_sbi);
		cash_hdfc=(LinearLayout)findViewById(R.id.vL_credit_hdfc);
		cash_icici=(LinearLayout)findViewById(R.id.vL_credit_icici);
		
		emi_sbi=(LinearLayout)findViewById(R.id.vL_emi_sbi);
		emi_hdfc=(LinearLayout)findViewById(R.id.vL_emi_hdfc);
		emi_icici=(LinearLayout)findViewById(R.id.vL_emi_icici);
		
		debit_sbi.setOnClickListener(this);
		debit_hdfc.setOnClickListener(this);
		debit_icici.setOnClickListener(this);
		
		net_sbi.setOnClickListener(this);
		net_hdfc.setOnClickListener(this);
		net_icici.setOnClickListener(this);
		
		payment_sbi.setOnClickListener(this);
		payment_hdfc.setOnClickListener(this);
		payment_icici.setOnClickListener(this);
		
		cash_sbi.setOnClickListener(this);
		cash_hdfc.setOnClickListener(this);
		cash_icici.setOnClickListener(this);
		
		emi_sbi.setOnClickListener(this);
		emi_hdfc.setOnClickListener(this);
		emi_icici.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.net_banking:
			callBankList();
			
			payment_gate_sub.setVisibility(View.GONE);
			debit_card_sub.setVisibility(View.GONE);
			credit_card_sub.setVisibility(View.GONE);
			emi_opt_sub.setVisibility(View.GONE);
			
			if (netbankingLists_layout.isShown()) {
				netbankingLists_layout.setVisibility(View.GONE);
			
				//net_banking_sub.setVisibility(View.GONE);
			} else {
				netbankingLists_layout.setVisibility(View.VISIBLE);
				setAdapter();
				//net_banking_sub.setVisibility(View.VISIBLE);
			}
			break;
			
		case R.id.payment_gate:
			net_banking_sub.setVisibility(View.GONE);
			debit_card_sub.setVisibility(View.GONE);
			credit_card_sub.setVisibility(View.GONE);
			emi_opt_sub.setVisibility(View.GONE);
			
			if (payment_gate_sub.isShown()) {
				payment_gate_sub.setVisibility(View.GONE);
			} else {
				payment_gate_sub.setVisibility(View.VISIBLE);
			}
			break;
			
		case R.id.debit_card:
			selectedSwitch=2;
			Intent intent = new Intent(PaymentActivity.this, MakePaymentNewActivity.class);
			intent.putExtra("Type", "netbanking");
			intent.putExtra("SWITCH", selectedSwitch);
			startActivity(intent);
			overridePendingTransition(0, 0);
			finish();
			
			/*net_banking_sub.setVisibility(View.GONE);
			payment_gate_sub.setVisibility(View.GONE);
			credit_card_sub.setVisibility(View.GONE);
			emi_opt_sub.setVisibility(View.GONE);
			
			if (debit_card_sub.isShown()) {
				debit_card_sub.setVisibility(View.GONE);
			} else {
				debit_card_sub.setVisibility(View.VISIBLE);
			}*/
			break;
			
		case R.id.credit_card:
			selectedSwitch=3;
			Intent creditintent = new Intent(PaymentActivity.this, MakePaymentNewActivity.class);
			creditintent.putExtra("Type", "netbanking");
			creditintent.putExtra("SWITCH", selectedSwitch);
			startActivity(creditintent);
			overridePendingTransition(0, 0);
			finish();
			/*net_banking_sub.setVisibility(View.GONE);
			payment_gate_sub.setVisibility(View.GONE);
			debit_card_sub.setVisibility(View.GONE);
			emi_opt_sub.setVisibility(View.GONE);
			
			if (credit_card_sub.isShown()) {
				credit_card_sub.setVisibility(View.GONE);
			} else {
				credit_card_sub.setVisibility(View.VISIBLE);
			}*/
			break;
			
		case R.id.emi_opt:
			/*net_banking_sub.setVisibility(View.GONE);
			payment_gate_sub.setVisibility(View.GONE);
			debit_card_sub.setVisibility(View.GONE);
			credit_card_sub.setVisibility(View.GONE);

			if (emi_opt_sub.isShown()) {
				emi_opt_sub.setVisibility(View.GONE);
			} else {
				emi_opt_sub.setVisibility(View.VISIBLE);
			}*/
			break;
			
		case R.id.sbi:
		case R.id.vL_debit_sbi:
		case R.id.vL_debit_hdfc:
		case R.id.vL_debit_icici:
		case R.id.vL_payment_sbi:
		case R.id.vL_payment_icici:
		case R.id.vL_payment_hdfc:
		case R.id.vL_netban_sbi:
		case R.id.vL_netban_hbfc:
		case R.id.vL_netban_icici:
		case R.id.vL_credit_sbi:
		case R.id.vL_credit_hdfc:
		case R.id.vL_credit_icici:
		case R.id.vL_emi_sbi:
		case R.id.vL_emi_hdfc:
		case R.id.vL_emi_icici:
			/*Intent intent = new Intent(PaymentActivity.this, MakePaymentNewActivity.class);
			intent.putExtra("Type", "DebitCard");
			startActivity(intent);
			overridePendingTransition(0, 0);*/

		}
	}
	
	private void callBankList() {
		citrusClient.getMerchantPaymentOptions(new Callback<MerchantPaymentOption>() {
			
			@Override
			public void success(MerchantPaymentOption merchantPaymentOption) {
				VerisupplierUtils.mBankList=new ArrayList<BankList>();
				for (int i = 0; i < merchantPaymentOption.getNetbankingOptionList().size(); i++) {
					Log.e("bankdetails","Bank Name: "+ merchantPaymentOption
							.getNetbankingOptionList()
							.get(i).getBankName().toString()
							+ " CID: "
							+ merchantPaymentOption
							.getNetbankingOptionList()
							.get(i).getBankCID().toString());
					
					BankList mBankList=new BankList();
					mBankList.setmBankName(merchantPaymentOption.getNetbankingOptionList().get(i).getBankName());
					mBankList.setmBankCID(merchantPaymentOption.getNetbankingOptionList().get(i).getBankCID());
					
					VerisupplierUtils.mBankList.add(mBankList);
				}
						
			}
			
			@Override
			public void error(CitrusError error) {
			}
			
		});
	}
	
	private void setAdapter() {
		mBankAdapter=new BankAdapter();
		netbankingList.setAdapter(mBankAdapter);
	}

	private class BankAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return VerisupplierUtils.mBankList.size();
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
				convertView = getLayoutInflater().inflate(R.layout.spinner_view, parent, false);
			}
			
			TextView bankName=(TextView)convertView.findViewById(R.id.quantity_name_spinner);
			bankName.setText(""+VerisupplierUtils.mBankList.get(position).getmBankName());
			
			netbankingList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					mBankName=VerisupplierUtils.mBankList.get(position).getmBankName();
					mBankCID=VerisupplierUtils.mBankList.get(position).getmBankCID();
					callpaymentMethod();
			
				}
			});
			
			return convertView;
		}


		
		
	}
	
	protected void callpaymentMethod() {
		initCitrus();
		
		//callBankList();

		switch (1) {
		
		case 1:
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
	
	protected void netPayment() {
		citrusClient = CitrusClient.getInstance(getApplicationContext()); // Activity Context
		// No need to call init on CitrusClient if already done.
		netbankingOption = new NetbankingOption(""+mBankName ,""+mBankCID);
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
	
	@Override
	protected void onResume() {
		VerisupplierUtils.mBankList=new ArrayList<BankList>();
		super.onResume();
	}
	

}
