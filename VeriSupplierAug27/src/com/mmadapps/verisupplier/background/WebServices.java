package com.mmadapps.verisupplier.background;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.util.Log;

public class WebServices {
		
	//private static String locatservice="http://192.168.30.65/";
	private static String devservice="http://vsuppliervm.cloudapp.net:9097/";
	private static String cloudservice="http://vsuppliervm.cloudapp.net:50666/";
	private static String production="http://vsuppliervm.cloudapp.net:50667/";
	
	//dev local services
	private static String CATEGORIES_SERVICES=devservice+"CategoriesService.svc/";
	private static String PRODUCT_SERVICES=devservice+"ProductService.svc/";
	private static String MANUFACTURER_SERVICES=devservice+"ManufacturerService.svc/";
	private static String ORDER_SERVICES =devservice+"OrderService.svc/";
	private static String QUOTATION_SERVICES =devservice+"QuotationService.svc/";
	private static String AUTHENTICATION_SERVICES=devservice+"AuthenticationService.svc/";
	private static String USER_SERVICES=devservice+"UserService.svc/";
	private static String MASTER_SERVICE=devservice+"MasterService.svc/";
	private static final String WAREHOUSE_API =devservice+"WarehouseService.svc/";
	
	//post urls
	public String SENDUSERREGISTRATION=devservice+"UserService.svc/UserRegistration";
	public String SendEmailTemplate=devservice+"EmailService.svc/SendEmailTemplate";
	public String SaveQuotation=devservice+"QuotationService.svc/SaveQuotation";
	public String FILEUPLOAD=devservice+"FileAttachmentService.svc/FileUpload";
	public String SAVEORDER=devservice+"OrderService.svc/SaveOrder";
	public String SAVEOPENQUOTE=devservice+"QuotationService.svc/SaveOpenQuote";
	public String UPDATEQUOTATION=devservice+"QuotationService.svc/UpdateQuotation";
	public String ACCEPTQUOTATION=devservice+"QuotationService.svc/AcceptQuotation";
	public String CREATECHANNEL=devservice+"WarehouseService.svc/CreateChannel";
	public String CREATEWAREHOUSE=devservice+"WarehouseService.svc/CU_Warehouse";
	public String CREATEINFOFEEDBACK=devservice+"ManufacturerService.svc/CreateAdditionalInfo";
	public String SENDREVIEW=devservice+"ProductService.svc/SaveProductReview";
	public String MANUFACTURERREVIEW=devservice+"ManufacturerService.svc/SaveManufacturerReview";
	public String UPDATEPAYMENT=devservice+"OrderService.svc/UpdatePaymentDetail";
	public String CREATEINVENTORY=devservice+"WarehouseService.svc/CreateInventoryProduct";
	public String UPDATEINVENTORY=devservice+"WarehouseService.svc/UpdateInventory";
	
	//new mobile services
	private static String QUOTATION_SERVICES_M=devservice+"MobileService/QuotationServiceM.svc/";
	private static String ORDER_SERVICES_M=devservice+"MobileService/OrderServiceM.svc/";
	private static String PRODUCT_SERVICES_M=devservice+"MobileService/ProductServiceM.svc/";
	private static String MANUFACTURER_services_M=devservice+"MobileService/ManufacturerServiceM.svc/";
	private static String SEARCH_SERVICES_M=devservice+"SearchService.svc/";
	
	//production cloud services
	/*private static String CATEGORIES_SERVICES=production+"CategoriesService.svc/";
	private static String PRODUCT_SERVICES=production+"ProductService.svc/";
	private static String MANUFACTURER_SERVICES=production+"ManufacturerService.svc/";
	private static String ORDER_SERVICES =production+"OrderService.svc/";
	private static String QUOTATION_SERVICES =production+"QuotationService.svc/";
	private static String AUTHENTICATION_SERVICES=production+"AuthenticationService.svc/";
	private static String USER_SERVICES=production+"UserService.svc/";
	private static String MASTER_SERVICE=production+"MasterService.svc/";
	private static final String WAREHOUSE_API =production+"WarehouseService.svc/";
	
	//post urls
	public String SENDUSERREGISTRATION=production+"UserService.svc/UserRegistration";
	public String SendEmailTemplate=production+"EmailService.svc/SendEmailTemplate";
	public String SaveQuotation=production+"QuotationService.svc/SaveQuotation";
	public String FILEUPLOAD=production+"FileAttachmentService.svc/FileUpload";
	public String SAVEORDER=production+"OrderService.svc/SaveOrder";
	public String SAVEOPENQUOTE=production+"QuotationService.svc/SaveOpenQuote";
	public String UPDATEQUOTATION=production+"QuotationService.svc/UpdateQuotation";
	public String CREATECHANNEL=production+"WarehouseService.svc/CreateChannel";
	public String CREATEWAREHOUSE=production+"WarehouseService.svc/CU_Warehouse";
	public String CREATEINFOFEEDBACK=production+"ManufacturerService.svc/CreateAdditionalInfo";
	public String SENDREVIEW=production+"ProductService.svc/SaveProductReview";
	public String MANUFACTURERREVIEW=production+"ManufacturerService.svc/SaveManufacturerReview";
	
	//new mobile services
	private static String QUOTATION_SERVICES_M=production+"MobileService/QuotationServiceM.svc/";
	private static String ORDER_SERVICES_M=production+"MobileService/OrderServiceM.svc/";
	private static String PRODUCT_SERVICES_M=production+"MobileService/ProductServiceM.svc/";
	private static String MANUFACTURER_services_M=production+"MobileService/ManufacturerServiceM.svc/";
	private static String SEARCH_SERVICES_M=production+"SearchService.svc/";*/
	
	
	//test cloud services
	/*private static String CATEGORIES_SERVICES=cloudservice+"CategoriesService.svc/";
	private static String PRODUCT_SERVICES=cloudservice+"ProductService.svc/";
	private static String MANUFACTURER_SERVICES=cloudservice+"ManufacturerService.svc/";
	private static String ORDER_SERVICES =cloudservice+"OrderService.svc/";
	private static String QUOTATION_SERVICES =cloudservice+"QuotationService.svc/";
	private static String AUTHENTICATION_SERVICES=cloudservice+"AuthenticationService.svc/";
	private static String USER_SERVICES=cloudservice+"UserService.svc/";
	private static String MASTER_SERVICE=cloudservice+"MasterService.svc/";
	private static final String WAREHOUSE_API =cloudservice+"WarehouseService.svc/";
		
	//post urls
	public String SENDUSERREGISTRATION=cloudservice+"UserService.svc/UserRegistration";
	public String SendEmailTemplate=cloudservice+"EmailService.svc/SendEmailTemplate";
	public String SaveQuotation=cloudservice+"QuotationService.svc/SaveQuotation";
	public String FILEUPLOAD=cloudservice+"FileAttachmentService.svc/FileUpload";
	public String SAVEORDER=cloudservice+"OrderService.svc/SaveOrder";
	public String SAVEOPENQUOTE=cloudservice+"QuotationService.svc/SaveOpenQuote";
	public String UPDATEQUOTATION=cloudservice+"QuotationService.svc/UpdateQuotation";
	public String CREATECHANNEL=cloudservice+"WarehouseService.svc/CreateChannel";
	public String CREATEWAREHOUSE=cloudservice+"WarehouseService.svc/CU_Warehouse";
	public String CREATEINFOFEEDBACK=cloudservice+"ManufacturerService.svc/CreateAdditionalInfo";
	public String SENDREVIEW=cloudservice+"ProductService.svc/SaveProductReview";
	public String MANUFACTURERREVIEW=cloudservice+"ManufacturerService.svc/SaveManufacturerReview";
		
	//new mobile services
	private static String QUOTATION_SERVICES_M=cloudservice+"MobileService/QuotationServiceM.svc/";
	private static String ORDER_SERVICES_M=cloudservice+"MobileService/OrderServiceM.svc/";
	private static String PRODUCT_SERVICES_M=cloudservice+"MobileService/ProductServiceM.svc/";
	private static String MANUFACTURER_services_M=cloudservice+"MobileService/ManufacturerServiceM.svc/";
	private static String SEARCH_SERVICES_M=cloudservice+"SearchService.svc/";*/
	
	
	public static enum ApiType {GetCategories,GetFeaturedProducts,GetVerifiedManufacturer,GetProductDetail,GetManufacturerDetail,GetOrder,GetOrderDetail,GetQuotation,GetQuotationHistory,Search,ValidateUser,UserDetails,GetLookups,IsAccountExist,GetQuoteDetailsForQuickOrder,WarehouseApi,GetManufacturerListForService,GetProductListOfManufacturer,GetCountries,GetChannel,GetInventory,GettradeOrder,GetWarehouse,GetInventoryWarehouseList,GetWishList,DeleteChannel,ProductsForInventory,AlertGetMessages,GetWarehouseList,DeleteWarehouse};

	public String CallWebHTTPBindingService(ApiType Api_Type,String MethodName, String Inparam) {
		String resultOutparam = "";
		switch (Api_Type) {
		
		case GetCategories:
			try {
				String saveURL = CATEGORIES_SERVICES+ MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetCategories", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		/*case GetFeaturedProducts:
			try {
				String saveURL = PRODUCT_SERVICES+ MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetFeaturedProducts", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;*/
			
		case GetFeaturedProducts:
			try {
				String saveURL = PRODUCT_SERVICES_M+ MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetFeaturedProducts", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case GetVerifiedManufacturer:
			try {
				String saveURL = MANUFACTURER_SERVICES+ MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetVerifiedManufacturer", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case GetProductDetail:
			try {
				String saveURL = PRODUCT_SERVICES_M+ MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetProductDetail", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		/*case GetManufacturerDetail:
			try {
				String saveURL = MANUFACTURER_SERVICES+ MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetManufacturerDetail", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;*/
			
		case GetManufacturerDetail:
			try {
				String saveURL = MANUFACTURER_services_M+ MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetManufacturerDetail", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
	
			
		/*case GetOrder:
			try{
				Inparam = Uri.encode(Inparam);
				String saveUrl = ORDER_SERVICES+MethodName+Inparam;
				resultOutparam = getResponseFromService(saveUrl);
				Log.e("Getorder", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;*/
			
		case GetOrder:
			try{
				Inparam = Uri.encode(Inparam);
				String saveUrl = ORDER_SERVICES_M+MethodName+Inparam;
				resultOutparam = getResponseFromService(saveUrl);
				Log.e("Getorder", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case GetOrderDetail:
			try{
				Inparam = Uri.encode(Inparam);
				String saveUrl = ORDER_SERVICES+MethodName+Inparam;
				resultOutparam = getResponseFromService(saveUrl);
				Log.e("Getorder", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		/*case GetQuotation:
			try{
				Inparam = Uri.encode(Inparam);
				String saveUrl = QUOTATION_SERVICES+MethodName+Inparam;
				resultOutparam = getResponseFromService(saveUrl);
				Log.e("Getqoutation", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;*/
			
		case GetQuotationHistory:
			try{
				Inparam = Uri.encode(Inparam);
				String saveUrl = QUOTATION_SERVICES+MethodName+Inparam;
				resultOutparam = getResponseFromService(saveUrl);
				Log.e("Getqoutation", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case GetQuotation:
			try{
				Inparam = Uri.encode(Inparam);
				String saveUrl = QUOTATION_SERVICES_M+MethodName+Inparam;
				resultOutparam = getResponseFromService(saveUrl);
				Log.e("Getqoutation", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case Search:
			try{
				Inparam = Uri.encode(Inparam);
				String saveUrl = SEARCH_SERVICES_M+MethodName+Inparam;
				resultOutparam = getResponseFromService(saveUrl);
				Log.e("Search", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case ValidateUser:
			try{
				String saveUrl = AUTHENTICATION_SERVICES+MethodName+Inparam;
				resultOutparam = getResponseFromService(saveUrl);
				Log.e("ValidateUser", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case UserDetails:
			try{
				Inparam = Uri.encode(Inparam);
				String saveUrl = USER_SERVICES+MethodName+Inparam;
				resultOutparam = getResponseFromService(saveUrl);
				Log.e("UserDetails", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case GetLookups:
			try{
				String saveUrl = MASTER_SERVICE+MethodName+Inparam;
				resultOutparam = getResponseFromService(saveUrl);
				Log.e("UserDetails", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
			
		case IsAccountExist:
			try
			{
			Inparam = Uri.encode(Inparam);
			String saveUrl = AUTHENTICATION_SERVICES+MethodName+Inparam;
			resultOutparam = getResponseFromService(saveUrl);
			Log.e("IsAccountExist", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case GetQuoteDetailsForQuickOrder:
			try
			{
			Inparam = Uri.encode(Inparam);
			String saveUrl = QUOTATION_SERVICES+MethodName+Inparam;
			resultOutparam = getResponseFromService(saveUrl);
			Log.e("GetQuoteDetailsForQuickOrder", saveUrl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case WarehouseApi:
			try{
				Inparam = Uri.encode(Inparam);
				String url = WAREHOUSE_API+MethodName+Inparam;
				resultOutparam = getResponseFromService(url);
				Log.e("WAREHOUSE API "+MethodName, url);
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
			
		case GetManufacturerListForService:
			try{
				Inparam = Uri.encode(Inparam);
				String url = MANUFACTURER_services_M+MethodName+Inparam;
				resultOutparam = getResponseFromService(url);
				Log.e("GetManufacturerListForService"+MethodName, url);
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
			
		case GetProductListOfManufacturer:
			try{
				Inparam = Uri.encode(Inparam);
				String url = PRODUCT_SERVICES_M+MethodName+Inparam;
				resultOutparam = getResponseFromService(url);
				Log.e("GetProductListOfManufacturer"+MethodName, url);
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
			
		case GetCountries:
			try{
				Inparam = Uri.encode(Inparam);
				String url = MASTER_SERVICE+MethodName+Inparam;
				resultOutparam = getResponseFromService(url);
				Log.e("GetProductListOfManufacturer"+MethodName, url);
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
			
		case GetChannel:
			try {
				String saveURL = WAREHOUSE_API + MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetCategories", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		break;
		
		case GetInventory:
			try {
				String saveURL = WAREHOUSE_API + MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetCategories", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case GettradeOrder:
			try {
				String saveURL = WAREHOUSE_API + MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetWarehouse", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case GetWarehouse:
			try {
				String saveURL = WAREHOUSE_API + MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetWarehouse", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case GetInventoryWarehouseList:
			try {
				String saveURL = WAREHOUSE_API + MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetInventoryWarehouseList", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case GetWishList:
			try{
                String saveURL = PRODUCT_SERVICES_M + MethodName + Inparam;
                resultOutparam = getResponseFromService(saveURL);
                Log.e("GetWishList", saveURL);
            }catch (Exception e) {
                e.printStackTrace();
            }
			break;
			
		case DeleteChannel:
			try{
				String saveURL = WAREHOUSE_API + MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("DeleteChannel", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case ProductsForInventory:
			try{
				String saveURL = WAREHOUSE_API + MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("ProductsForInventory", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case GetWarehouseList:
			try {
				String saveURL = WAREHOUSE_API + MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("GetWarehouseLIST", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		case AlertGetMessages:
			try{
				String saveURL = WAREHOUSE_API + MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("AlertGetMessages", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case DeleteWarehouse:
			try{
				String saveURL = WAREHOUSE_API + MethodName + Inparam;
				resultOutparam = getResponseFromService(saveURL);
				Log.e("DeleteWarehouse", saveURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		}
		Log.e("resultOutparam",""+resultOutparam);
		return resultOutparam;
	}
	
	public String getResponseFromService(String url) {
		String finalResult = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
		httpget.setHeader("type","mobile|android");
		httpget.setHeader("JWTKEY","Verisupplier.android.d2335fhfg4564hghjghjghjget45ert.1.0");
		httpget.setHeader("OS","ANDROID");
		httpget.setHeader("USERID","");
		try {
			response = httpclient.execute(httpget);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 404) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			StringBuffer out = new StringBuffer();
			byte[] b = EntityUtils.toByteArray(entity);
			out.append(new String(b, 0, b.length));
			finalResult = out.toString();
		} catch (ClientProtocolException e) {
			Log.e("REST", "There was a protocol based error", e);
		} catch (IOException e) {
			Log.e("REST", "There was an IO Stream related error", e);
		}
		return finalResult;
	}

	
}
