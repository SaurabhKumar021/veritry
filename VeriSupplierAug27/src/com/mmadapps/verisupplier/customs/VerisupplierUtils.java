package com.mmadapps.verisupplier.customs;

import java.util.ArrayList;
import java.util.List;

import android.R.string;

import com.citrus.netbank.Bank;
import com.mmadapps.verisupplier.beans.BankList;
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.OrderDashboard;
import com.mmadapps.verisupplier.beans.PaymentStatus;
import com.mmadapps.verisupplier.beans.QuickOrderResult;


public class VerisupplierUtils {
	
	public static String steptwoselectedvalue="";
	public static String stepthreeselectedvalue="";
	public static String stepfourselectedvalue="";
	
	public static String mCustomServiceId="";
	public static String mInspectionServiceId="";
	public static String mShippingServiceId="";
	
	public static QuickOrderResult mQuotationDetails=new QuickOrderResult();
	public static List<Lookups> mQuantityList=new ArrayList<Lookups>();
	public static List<Lookups> mQuotationLookups=new ArrayList<Lookups>();
	public static List<Lookups> mMoreInfoLookups=new ArrayList<Lookups>();
	public static List<Lookups> mSubjectLookups=new ArrayList<Lookups>();
	public static List<CountryDetails> mCountriesList=new ArrayList<CountryDetails>();
	public static List<CountryDetails> mStatesList=new ArrayList<CountryDetails>();
	
	public static List<OrderDashboard> mOrderDashboardList=new ArrayList<OrderDashboard>();
	
	public static String mServiceGroupType;
	public static String mProductParam="1/1";
	public static String mProductServiceGroupType="1";
	public static String mShippingParam="2";
	public static String mCustomsParam="4";
	public static String mInspectionParam="3";
	public static String mWarehouseParam="7";
	public static String mEcreditParam="8";
	
	public static String mServiceGroupTypeId;
	
	
	public static String mQuotationId;
	
	public static QuickOrderResult mOrderResult;
	
	public static String mFromActivity;
	public static String mOrderNumber;
	public static String mOrderId;
	
	public static PaymentStatus mPaymentStatus=new PaymentStatus();
	public static List<BankList> mBankList=new ArrayList<BankList>();
	
	public static String mDashboardCurrentType;
	public static String mDashboardServiceGroupType;
	public static QuickOrderResult mOrderDetails=new QuickOrderResult();
	public static QuickOrderResult mDashboardQuotationDetails=new QuickOrderResult();


}
