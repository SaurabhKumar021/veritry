package com.mmadapps.verisupplier.search;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.ProductList;
import com.mmadapps.verisupplier.beans.SearchResults;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;
import com.mmadapps.verisupplier.details.ProductDetailsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class SearchActivity extends BaseActionBarActivity {
	
	//searchtext variable from homepage
	String searchTerm, previousTerm;
	ImageView search;
	EditText searchText;
	//from database
	List<ProductList> mSearchProductList=new ArrayList<ProductList>();
	List<ProductDetails> mProductDetails=new ArrayList<ProductDetails>();
	List<SearchResults> mSearchNewList=new ArrayList<SearchResults>();
	//services
	WebServices webServices;
	JsonParserClass jsonParserClass;
	
	//imageloader
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	//Listview variables
	ListView mSearchList;
	SearchAdapter mSearchAdapter;
	
	
	Spinner vI_as_servicespinner;
	String Service[] = {"Products","Inspection","Customs","Shipping","WareHouse"};
	String Service_selected_name;
	String Service_grouptype;
	
	
	//onscroll
	private int lastCurrentItemPosition;
	private int pageNo = 0;
	private RotateAnimation rotate=null;
	private View pendingView=null;
	
	//loading bar
	private ProgressDialog pdLoading=null;
	
	
	String mProductId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("VeriSupplier");
		//searchtext=getIntent().getStringExtra("searchText");
		
		initializeView();
		getSearchList();
	}

	private void getSearchList() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		searchTerm = searchText.getText().toString().trim();
		mSearchProductList=helper.getSearchList();
		helper.close();
		
		if(mSearchNewList==null || mSearchNewList.size()==0){
		
		}else{
			mSearchAdapter=new SearchAdapter();
			mSearchList.setAdapter(mSearchAdapter);
		}
	}
	
	private void initializeView() {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
		try {
			options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
			.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
			.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		searchText = (EditText) findViewById(R.id.vE_as_searchText);
		search = (ImageView) findViewById(R.id.vI_as_search);
		
		
		
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				searchTerm = searchText.getText().toString().trim();
				if(searchTerm==null || searchTerm.length()==0){	
					Toast.makeText(getApplicationContext(), "Please fill search box", Toast.LENGTH_LONG).show();
				}else{
					mSearchNewList = new ArrayList<SearchResults>();
					new AsyncSearchAll().execute(1);
				}
			}
		});
		
		
		
		
		
		/*search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchTerm = searchText.getText().toString().trim();
				if(searchTerm.equalsIgnoreCase(previousTerm)){
					Toast.makeText(SearchActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();					
				}else if(searchTerm == null || searchTerm.length() == 0){
					Toast.makeText(SearchActivity.this, "Search term is empty", Toast.LENGTH_SHORT).show();
				}else{
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
					
					mSearchProductList = new ArrayList<ProductList>();
					Toast.makeText(SearchActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();
					new addMoreProductOnScroll().execute(0);
					previousTerm = searchTerm;
				}
			}
		});*/
		
		mSearchList=(ListView)findViewById(R.id.vL_as_searchlist);
		
		rotate=new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setDuration(600);
		rotate.setRepeatMode(Animation.RESTART);
		rotate.setRepeatCount(Animation.INFINITE);
		
		pendingView=findViewById(android.R.id.text1);
		pendingView=findViewById(R.id.vI_as_scrollImage);
		pendingView.setVisibility(View.GONE);
		
		vI_as_servicespinner = (Spinner) findViewById(R.id.vI_as_servicespinner);	
		vI_as_servicespinner.setAdapter(new ServiceSpinnerAdapter(getApplicationContext(), R.layout.spinner_view, Service));
		
		
		vI_as_servicespinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
				if(position==0){
					Service_grouptype = VerisupplierUtils.mProductServiceGroupType;
				}else if(position==1){
					Service_grouptype = VerisupplierUtils.mInspectionParam;
				}else if(position==2){
					Service_grouptype = VerisupplierUtils.mCustomsParam;
				}else if(position==3){
					Service_grouptype = VerisupplierUtils.mShippingParam;
				}else if(position==4){
					Service_grouptype = VerisupplierUtils.mWarehouseParam;
				}else{
					Toast.makeText(getApplicationContext(), "No Service Group Type",Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
	
		/*mSearchList.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int currentItemPosition = mSearchList.getFirstVisiblePosition();
				if(currentItemPosition > lastCurrentItemPosition){
					
					//Toast.makeText(getApplicationContext(), "Down", Toast.LENGTH_SHORT).show();
					int totalCount = mSearchProductList.size();
					int lastItemPosition = mSearchList.getLastVisiblePosition();
					if(lastItemPosition+1 == totalCount){
						if(mSearchProductList.size() < Integer.parseInt(mSearchProductList.get(0).getmProductTotalCount())){
							pageNo++;
							pendingView.setVisibility(View.VISIBLE);
							startProgressAnimation();
							new AsyncSearchAll().execute(pageNo);
						}
					}
				}
				lastCurrentItemPosition = currentItemPosition;
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				
			}
		});*/
	}

	private class SearchAdapter extends BaseAdapter{
		
		
		@Override
		public int getCount() {
			if(mSearchNewList==null || mSearchNewList.size()==0 || mSearchNewList.get(0).getmSearchProductsList()==null || mSearchNewList.get(0).getmSearchProductsList().size()==0){
				return 0;
			}else{
				return mSearchNewList.get(0).getmSearchProductsList().size();
			}
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
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.productlist_view, parent, false);
			}
			
			ImageView productImage=(ImageView)convertView.findViewById(R.id.vI_pv_producuImage);
			TextView productName=(TextView)convertView.findViewById(R.id.vT_pv_productname);
			TextView manufacturerName=(TextView)convertView.findViewById(R.id.vT_pv_manufacturername);
			TextView productPriceText=(TextView)convertView.findViewById(R.id.vT_pv_productprice_text);
			TextView productPrice=(TextView)convertView.findViewById(R.id.vT_pv_productprice);
			ImageView wishlist=(ImageView)convertView.findViewById(R.id.vI_pv_wishlist_blank);
			wishlist.setVisibility(View.GONE);
			
			imageLoader.displayImage(mSearchNewList.get(0).getmSearchProductsList().get(position).getmProductPicture(), productImage, options);
			productName.setText(""+mSearchNewList.get(0).getmSearchProductsList().get(position).getmProductName());
			manufacturerName.setText(""+mSearchNewList.get(0).getmSearchProductsList().get(position).getmMfrManufacturerName());
			productPrice.setText(" "+mSearchNewList.get(0).getmSearchProductsList().get(position).getmPrice());//+"/"+mSearchProductList.get(position).getmUnitOfMeasurement());
			
			mSearchList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					mProductId=mSearchNewList.get(0).getmSearchProductsList().get(position).getmProductId();
					new AsyncProductDetails().execute();
				}
				
			});
			
			return convertView;
		}
		
	}
	
	void startProgressAnimation() {
	    if (pendingView!=null) {
	      pendingView.startAnimation(rotate);
	      
	    }
	  }
	
	/*private class addMoreProductOnScroll extends AsyncTask<Integer, Void, Boolean>{
		boolean setAgain = false;
		@Override
		protected Boolean doInBackground(Integer... params) {
			if(mSearchProductList == null || mSearchProductList.size() == 0){
				setAgain = true;
			}else{
				setAgain = false;
			}
			return getProducts(params[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pendingView.clearAnimation();
			pendingView.setVisibility(View.GONE);
			if(setAgain){
				mSearchAdapter = new SearchAdapter();
				mSearchList.setAdapter(mSearchAdapter);
			}else{
				mSearchAdapter.notifyDataSetChanged();	
			}
		}
		
	}*/
	
	

	public Boolean getSearchData(int pageno) {
		Boolean isValid=false;
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		//List<ProductList> mProductDetails=new ArrayList<ProductList>();
		List<SearchResults> mSearchResults = new ArrayList<SearchResults>();
		//ProductList list = new ProductList();
		String inParam =""+searchTerm+"/"+Service_grouptype+"/"+"0"+"/"+pageno+"/"+"10";
		String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.Search, "SearchM/", inParam);
		if (resultOutparam == null) {
			isValid=false;
		} else {
			mSearchResults=jsonParserClass.parseSearchResults(resultOutparam);
			if(mSearchResults==null || mSearchResults.size()==0){
				isValid = false;
			}else{
				mSearchNewList.addAll(mSearchResults);
				isValid=true;
			}
		}
		return isValid;
	}
	
	private class AsyncProductDetails extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(SearchActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return getProductDetail();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			if(result){
				forwordToProductDetailPage();
			}else{
				
			}
		}

	}
	
	
  private class AsyncSearchAll extends AsyncTask<Integer, Void, Boolean>{
	   boolean setAgain = false;
		@Override
		protected void onPreExecute() {
			pdLoading = new ProgressDialog(SearchActivity.this);
			pdLoading.setMessage("Please wait...");
			pdLoading.show();
			pdLoading.setCancelable(false);
			pdLoading.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			if(mSearchProductList == null || mSearchProductList.size() == 0){
				setAgain = true;
			}else{
				setAgain = false;
			}
			return getSearchData(params[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (pdLoading != null && pdLoading.isShowing()) {
				pdLoading.cancel();
			}
			if(setAgain){
				mSearchAdapter = new SearchAdapter();
				mSearchList.setAdapter(mSearchAdapter);
			}else{
				
				mSearchAdapter.notifyDataSetChanged();	
			}
			
		}

	}
	
	private Boolean getProductDetail() {
		boolean isValid=false;
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mProductDetails=helper.getAllProductDetails(mProductId);
		helper.close();
		
		if(mProductDetails==null || mProductDetails.size()==0){
			webServices =new WebServices();
			jsonParserClass=new JsonParserClass();
			String Inparam=mProductId;
			String outParam=webServices.CallWebHTTPBindingService(ApiType.GetProductDetail, "GetProductDetail/", Inparam);
			if(outParam==null || outParam.length()==0){
				isValid = false;
			}else{
				mProductDetails=jsonParserClass.parseProductDetails(outParam, getApplicationContext());
				if(mProductDetails==null || mProductDetails.size()==0){
					isValid=false;
				}else{
					helper.insertProductDetails(mProductDetails);
					helper.insertSpecificationDetails(mProductDetails,mProductId);
					helper.insertProductImageDetails(mProductDetails,mProductId);
					helper.insertUserFeedback(mProductDetails,mProductId);
					isValid=true;
				}
			}
		}else{
			isValid =true;
		}
		return isValid;
	}
	
	private void forwordToProductDetailPage() {
		Intent newIntent = new Intent(SearchActivity.this,ProductDetailsActivity.class);
		newIntent.putExtra("PRODUCTID", mProductId);
		startActivity(newIntent);
		overridePendingTransition(0, 0);
	}
	
	
	public class ServiceSpinnerAdapter extends ArrayAdapter<Lookups> {
		TextView tv;
		String Service[];
		Context mContext;
		int mResource = 0;
		
 	public ServiceSpinnerAdapter(Context context, int resource, String[] service) {
			super(context, resource);
			this.mContext = context;
			this.mResource = resource;
			this.Service = service;
		}

		@Override
		public int getCount() {
			return Service.length;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.spinner_view, parent, false);
			}
			
			tv = (TextView) convertView.findViewById(R.id.quantity_name_spinner);
			Service_selected_name = Service[position];
			tv.setTextSize(15);
			tv.setText(""+Service_selected_name);
			return convertView;
		}
		
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.spinner_view, parent, false);
			}
			tv = (TextView) convertView.findViewById(R.id.quantity_name_spinner);
			String name = Service[position];
			tv.setText(""+name);
			return convertView;
		}

		
	}

}
