package com.mmadapps.verisupplier.dashboard;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
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
import com.mmadapps.verisupplier.beans.Milestones;
import com.mmadapps.verisupplier.beans.QuotationDashboardHistory;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.TwoWayView;
import com.mmadapps.verisupplier.fileattachment.FileExplorer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ProductQuotationManufacturerDetailsActivity extends BaseActionBarActivity {
	
	String buyer_name, buyer_companyname, buyer_subject, manufacture_name, manufactude_companyname, manufacture_subject, quoatation_id;
	ProgressDialog pdLoading; 
	ArrayList<QuotationDashboardHistory> quotationHistoryList;
	WebServices webServices;
	JsonParserClass jsonParserClass;
	String  complete_datetime,Inparam;
	ImageLoader imageLoader;
	static ArrayList<String> arrayList = new ArrayList<String>();
	ImageView vI_AQD_createQuotation;
	DisplayImageOptions options;
	ListView attach_list;
	TextView quoatation_buyername,quotation_buyercompany_name,quotation_buyersubject,head,vT_LR_subjectLable,dateTime;
	boolean isInbox = true;
	private int REQUEST_GET_SINGLE_FILE = 111;
	private GetQuortationAdapter mAdapter;
	
	// baskar
	private ImageView vI_AQD_productImage;
	private ListView vI_AQD_messageLists,vL_aqsr_milestoneList;
	private TextView vT_AQD_productName;
	private UserDetails mUserDetails;
	
	double remainPercentage = 100.0;
	double prevPercentage = 0;
	
	//Document
	TwoWayView document_list;
	DocumentAdapter documentadapter;
	String mAttachment="";
	ArrayList<String> document_url_list;
	
	
	//MileStone
	ListView vL_AQD_milestoneList;
	MileStoneAdapter milestoneadapter;
	MileStoneDashBoardAdapter mileStoneDashBoardAdapter;
	private ArrayList<Milestones> mMilestoneList ;
	ArrayList<Milestones> getMilestones;
	
	private static Typeface segeo_wp;
	private static Typeface segeo_wp_bold;
	
	
	EditText message,quantity;
	private String mResponseUserType = "2";
	String mSelectedQuantity,mQuotationStatusId="1",mUserSubject;
	
	
	//aditional info
	String mAdditionalInfo;
	ArrayList<String> additionalinfo_list;
	String additionalinfoText="";
	TwoWayView madditional_twowaylist;
	//AdditionalInfo madditionaladapter;
	

	
	//Attach URL
	String attachimageUrl="";
	String mAttachmentList;
	
	//milestones dates value
	String DateValues[] = new String[100];
    String dateselected;
	

	public static Typeface getSegeo_wp() {
		return segeo_wp;
	}

	public static void setSegeo_wp(Typeface segeo_wp) {
		ProductQuotationManufacturerDetailsActivity.segeo_wp = segeo_wp;
	}

	public static Typeface getSegeo_wp_bold() {
		return segeo_wp_bold;
	}

	public static void setSegeo_wp_bold(Typeface segeo_wp_bold) {
		ProductQuotationManufacturerDetailsActivity.segeo_wp_bold = segeo_wp_bold;
	}
	
	//Spinner Quantity
    String mOriginalQuantityUnit;
    int mOriginalQuantityUnitId;
    //from utils
  	List<Lookups> mUnitOfMeasurementList=new ArrayList<Lookups>();
  	List<Lookups> mQuotationLookup=new ArrayList<Lookups>();
  	ListView mLookUpList;
  	LookUpAdapter mlookupAdapter;
  	List<Lookups> checkOptionalDetail=new ArrayList<Lookups>();
  	List<String> newLookups;
  	List<String> mLookUpListWithId=new ArrayList<String>();
  	List<String> mSelectedLookUps=new ArrayList<String>();
  	CheckBox optional_text;
  	TextView vC_aclu_optional_textview;
  	String mQuantity,mUserMessage,mPrice;
  	EditText price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quotation_details);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.vBuyerImage.setVisibility(View.VISIBLE);

		initControl();	
		getHistoryFromDbAndsetAdapter();
	}
	
	private void initControl() {
		
		for(int i=1;i<=99;i++){
	          DateValues[i] = ""+i;
	        }
		
		mMilestoneList = new ArrayList<Milestones>();
		milestoneadapter = new MileStoneAdapter(mMilestoneList);
		
		vI_AQD_productImage = (ImageView) findViewById(R.id.vI_AQD_productImage);
		vI_AQD_messageLists = (ListView) findViewById(R.id.vI_AQD_messageLists);
		vT_AQD_productName = (TextView) findViewById(R.id.vT_AQD_productName);
		vI_AQD_createQuotation = (ImageView) findViewById(R.id.vI_AQD_createQuotation);
		
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(ProductQuotationManufacturerDetailsActivity.this));
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
		
		jsonParserClass = new JsonParserClass();
		webServices = new WebServices();

		Helper helper = new Helper(ProductQuotationManufacturerDetailsActivity.this);
		helper.openDataBase();
		mUserDetails = helper.getUserDetails();
		helper.close();
		
		vI_AQD_createQuotation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				arrayList = new ArrayList<String>();
				newLookups = new ArrayList<String>();
				mLookUpListWithId = new ArrayList<String>();
				createPopupDialog();
			}
		});
	}
	private void getHistoryFromDbAndsetAdapter(){
		Helper helper = new Helper(ProductQuotationManufacturerDetailsActivity.this);
		helper.openDataBase();
		quotationHistoryList = helper.getAllDashboardQuotationHistory(null);
		mUnitOfMeasurementList=helper.getUnitOfMeasurentList();
		mQuotationLookup=helper.getQuotationLookupList();
		helper.close();
		
		if(quotationHistoryList == null || quotationHistoryList.size() == 0){
			
		}else{
			QuortationHistoryAdapter adapter1 = new QuortationHistoryAdapter(quotationHistoryList);
			quoatation_id= quotationHistoryList.get(0).getHistory_id();
			String contactPersonPicUrl = quotationHistoryList.get(0).getHistory_Buyerimage();
			imageLoader.displayImage(contactPersonPicUrl, BaseActionBarActivity.vBuyerImage, options);
			
			BaseActionBarActivity.setmUserSubTitle(""+quotationHistoryList.get(0).getHistory_Manufacturename());
			BaseActionBarActivity.setmUserName(""+quotationHistoryList.get(0).getHistory_productname());
			
			vT_AQD_productName.setText(""+quotationHistoryList.get(0).getHistory_productname());
			imageLoader.displayImage(quotationHistoryList.get(0).getHistory_productimage(), vI_AQD_productImage, options);
			
			vI_AQD_messageLists.setAdapter(adapter1);
		}
		
		
	}
	
	private class QuortationHistoryAdapter extends BaseAdapter { 
		private ArrayList<QuotationDashboardHistory> list;
		private LinearLayout.LayoutParams lp;
	
		public QuortationHistoryAdapter(ArrayList<QuotationDashboardHistory> arrayList) {
			this.list = arrayList;
			this.lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.setMargins(5, 0, 5, 0);
		}
	
		@Override
		public int getCount() {
			if(list==null || list.size()==0){
				return 0;
			}else{
				return list.size();
			}
		}

		@Override
		public QuotationDashboardHistory getItem(int position) {
			return list.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.adapter_quotation_seller_detail, parent, false);
			}
			
			
			Helper helper = new Helper(getApplicationContext());
			helper.openDataBase();
			getMilestones = helper.getMilestonesByQuotationHistoryId(list.get(position).getHistory_QuotationResponseId());
			helper.close();
			
			LinearLayout document_layout = (LinearLayout) convertView.findViewById(R.id.vT_aqsc_document_twoview_layout);
			//LinearLayout document_layout = (LinearLayout) convertView.findViewById(R.id.vL_AQD_documentLayout);
			LinearLayout vL_AQD_managerOptions = (LinearLayout) convertView.findViewById(R.id.vL_AQD_managerOptions);
			LinearLayout vL_AQD_rootLayout = (LinearLayout) convertView.findViewById(R.id.vL_AQD_rootLayout);
			LinearLayout additionalinfo_layout=(LinearLayout)convertView.findViewById(R.id.vL_apqsc_additionalinfo_layout);
			LinearLayout vL_AQD_milestoneLayout = (LinearLayout) convertView.findViewById(R.id.vL_AQD_milestoneLayout);
			
			TextView vT_AQD_dateTimeText = (TextView) convertView.findViewById(R.id.vT_AQD_dateTimeText);
			TextView vT_AQD_optionsText = (TextView) convertView.findViewById(R.id.vT_AQD_optionsText);
			TextView vT_AQD_quotationMessage = (TextView) convertView.findViewById(R.id.vT_AQD_quotationMessage);
			TextView vT_AQD_requiredQty = (TextView) convertView.findViewById(R.id.vT_AQD_requiredQty);
			TextView vT_AQD_qtyUnit = (TextView) convertView.findViewById(R.id.vT_AQD_qtyUnit);
			View vV_AQD_viewLine = convertView.findViewById(R.id.vV_AQD_viewLine);
			LinearLayout vL_AQD_productPriceLayout = (LinearLayout) convertView.findViewById(R.id.vL_AQD_productPriceLayout);
			TextView vT_AQD_document_lbl = (TextView) convertView.findViewById(R.id.vT_AQD_document_lbl);
			document_list = (TwoWayView) convertView.findViewById(R.id.vL_AQD_document_list);
			//madditional_twowaylist = (TwoWayView) convertView.findViewById(R.id.vL_AQD_additonal_list);
			ListView vL_AQD_milestoneList = (ListView) convertView.findViewById(R.id.vL_AQD_milestoneList);
			View vV_AQD_documentView = convertView.findViewById(R.id.vV_AQD_documentView);
			View vV_AQD_documentView1 = convertView.findViewById(R.id.vV_AQD_documentView1);
			TextView vT_AQD_attachment_lbl = (TextView) convertView.findViewById(R.id.vT_AQD_attachment_lbl);
			
			QuotationDashboardHistory history = getItem(position);
			String msgType = history.getChat_ResponseUserType();
			String dateTime = list.get(position).getChat_ResponseDate().split(" ")[1]+" "+list.get(position).getChat_ResponseDate().split(" ")[2];
			vT_AQD_dateTimeText.setText(""+dateTime);
			vT_AQD_quotationMessage.setText(""+history.getChat_message());
			vT_AQD_requiredQty.setText(""+history.getChat_RequiredQty());
			vT_AQD_qtyUnit.setText(" "+history.getChat_UnitsOfMeasurement());
			
			
			
			mAdditionalInfo=history.getmAdditionalInfo();
			additionalinfoText="";
			if(mAdditionalInfo==null || mAdditionalInfo.length()==0){
				additionalinfo_layout.setVisibility(View.GONE);
			}else{
				additionalinfo_layout.setVisibility(View.VISIBLE);
				additionalinfo_list=new ArrayList<String>();
					StringTokenizer st = new StringTokenizer(mAdditionalInfo, ",");
					while (st.hasMoreTokens()) { 
						additionalinfo_list.add(st.nextToken()); 
					}
						if(additionalinfo_list==null || additionalinfo_list.size()==0){
						}else{
							

							if(additionalinfo_list.contains("12"))
								additionalinfoText= additionalinfoText+"Independent,";
							if(additionalinfo_list.contains("13"))
								additionalinfoText= additionalinfoText+"ODM,";
							if(additionalinfo_list.contains("14"))
								additionalinfoText= additionalinfoText+"OEM,";
							
							/*madditionaladapter = new AdditionalInfo(additionalinfo_list);
							madditional_twowaylist.setAdapter(madditionaladapter);*/
						
					   }
			}
			vT_AQD_optionsText.setText(additionalinfoText);
			
			/*mAdditionalInfo=history.getmAdditionalInfo();
			if(mAdditionalInfo==null || mAdditionalInfo.length()==0){
				additionalinfo_layout.setVisibility(View.GONE);
			}else{
				additionalinfo_layout.setVisibility(View.VISIBLE);
				additionalinfo_list=new ArrayList<String>();
				if(mAdditionalInfo.contains(",")){
					StringTokenizer st = new StringTokenizer(mAttachment,",");  
					while (st.hasMoreTokens()) { 
						additionalinfo_list.add(st.nextToken()); 	 
					} 
				}else{
					additionalinfo_list.add(mAdditionalInfo);
				}
				
				if(additionalinfo_list==null || additionalinfo_list.size()==0){
				}else{
					
				}
			}*/
			
			mAttachment=quotationHistoryList.get(position).getmAttachment();
			if(mAttachment==null || mAttachment.length()==0){
				document_layout.setVisibility(View.GONE);
				vV_AQD_documentView1.setVisibility(View.GONE);
			}else{
				document_layout.setVisibility(View.VISIBLE);
				document_url_list = new ArrayList<String>();
				if(mAttachment.contains(",")){
					StringTokenizer st = new StringTokenizer(mAttachment,",");  
					while (st.hasMoreTokens()) { 
						document_url_list.add(st.nextToken()); 	 
					}  
				}else{
					document_url_list.add(mAttachment);
				}
				
				if(document_url_list == null || document_url_list.size() == 0){
					   
				}else{
					documentadapter=new DocumentAdapter(document_url_list);
					document_list.setAdapter(documentadapter);   
				}
			}
			
			if(msgType == null || msgType.length() == 0){
				
			}else if(msgType.equals("1")){
				vL_AQD_managerOptions.setVisibility(View.GONE);	
				vV_AQD_viewLine.setVisibility(View.GONE);
				vL_AQD_productPriceLayout.setVisibility(View.GONE);	
				vL_AQD_milestoneLayout.setVisibility(View.GONE);
				vT_AQD_attachment_lbl.setText("Do you provide");
			}else{
				vL_AQD_productPriceLayout.setVisibility(View.VISIBLE);
				TextView vT_AQD_productPrice = (TextView) convertView.findViewById(R.id.vT_AQD_productPrice);
				TextView vT_AQD_acceptButton = (TextView) convertView.findViewById(R.id.vT_AQD_acceptButton);
				TextView vT_AQD_rejectButton = (TextView) convertView.findViewById(R.id.vT_AQD_rejectButton);
				vT_AQD_attachment_lbl.setText("We provide");
				
				vT_AQD_acceptButton.setVisibility(View.GONE);
				vT_AQD_rejectButton.setVisibility(View.GONE);
				vL_AQD_milestoneLayout.setVisibility(View.VISIBLE);
				vT_AQD_productPrice.setText(""+history.getOffered_price());
				vL_AQD_rootLayout.setBackground(getResources().getDrawable(R.drawable.chat_mfr_blue));
				vT_AQD_dateTimeText.setTextColor(getResources().getColor(R.color.datetime_blue));
				
				
				if(quotationHistoryList.get(position).getHistory_QuotationStatusId().equalsIgnoreCase("5") || quotationHistoryList.get(position).getHistory_QuotationStatusId().equalsIgnoreCase("3")){
					//vI_AQD_createQuotation.setVisibility(View.GONE);
				}else{
					//vI_AQD_createQuotation.setVisibility(View.VISIBLE);
				}
				
				
				if(getMilestones==null || getMilestones.size()==0){
					
				}else{
					mileStoneDashBoardAdapter = new MileStoneDashBoardAdapter(getMilestones);
					vL_AQD_milestoneList.setAdapter(mileStoneDashBoardAdapter);
					mileStoneDashBoardAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(vL_AQD_milestoneList);
				}
				
				vT_AQD_acceptButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Toast.makeText(ProductQuotationManufacturerDetailsActivity.this, "Accept", Toast.LENGTH_SHORT).show();
					}
				});
				
				vT_AQD_rejectButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Toast.makeText(ProductQuotationManufacturerDetailsActivity.this, "Reject", Toast.LENGTH_SHORT).show();
					}
				});
			}
			return convertView;
		}
	}
	
	//TODO quotation seller reply
	
	protected void createPopupDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ProductQuotationManufacturerDetailsActivity.this);
		final View view = getLayoutInflater().inflate(R.layout.activity_quotation_seller_reply, null);
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
		dialog.show();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
				
		if(view == null){
			if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
			}
		}
		message = (EditText) view.findViewById(R.id.vE_aqsr_quotationMessage);
		attach_list = (ListView) view.findViewById(R.id.vL_aqsr_attachList);
		vL_aqsr_milestoneList = (ListView) view.findViewById(R.id.vL_aqsr_milestoneList);
		quantity = (EditText) view.findViewById(R.id.vE_aqsr_quantity);
		String quantity_value = quotationHistoryList.get(0).getChat_RequiredQty();
		quantity.setText(""+quantity_value);
		mQuantity=quantity.getText().toString().trim();
		mSelectedQuantity=quotationHistoryList.get(0).getChat_UnitsOfMeasurement();
		
		price=(EditText)view.findViewById(R.id.vE_aqsr_price);
		price.setText(""+quotationHistoryList.get(0).getOffered_price());
		
		TextView vL_AM_milestoneAddButton = (TextView) view.findViewById(R.id.vL_aqsr_milestoneAddButton);
		
		
	
		
		vL_AM_milestoneAddButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createMilestonePopup();
			}
		});
		
		view.findViewById(R.id.vI_aqsr_dialogClose).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
					checkOptionalDetail.clear();
					mLookUpListWithId.clear();
				}
			}
		});
		
		
		message.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mUserMessage=message.getText().toString().trim();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		price.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mPrice=price.getText().toString().trim();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		
		if(quotationHistoryList.get(0).getmAdditionalInfo()==null || quotationHistoryList.get(0).getmAdditionalInfo().length()==0){
			
		}else{
		String[] args=quotationHistoryList.get(0).getmAdditionalInfo().split(",");
		for(int i=0;i<args.length;i++){
			mLookUpListWithId.add(args[i]);
		
		}
		}
		
		ListView mLookUpList=(ListView)view.findViewById(R.id.vL_aqbs_lookuplist);
		mlookupAdapter=new LookUpAdapter(mQuotationLookup);
		mLookUpList.setAdapter(mlookupAdapter);
		setListViewHeightBasedOnChildren(mLookUpList);
		
		
		mSelectedQuantity=quotationHistoryList.get(0).getChat_UnitsOfMeasurement();

		view.findViewById(R.id.vI_aqsr_sendButton).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mUserMessage==null || mUserMessage.equalsIgnoreCase("null")){
					Toast.makeText(getApplicationContext(), "PLease enter the message", Toast.LENGTH_SHORT).show();

				}else if(mPrice==null || mPrice.length()==0){
					Toast.makeText(getApplicationContext(), "PLease enter the price", Toast.LENGTH_SHORT).show();
				}else{
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					
					new UpdateQuotation().execute();
				}
			}
		});
		
		view.findViewById(R.id.vI_aqsr_attachButton).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProductQuotationManufacturerDetailsActivity.this,FileExplorer.class);
				startActivityForResult(i, REQUEST_GET_SINGLE_FILE);
			}
		});
		
		Spinner spinner = (Spinner) view.findViewById(R.id.vI_aqsr_spinner);
        spinner.setAdapter(new SpinnerAdapter(getApplicationContext(), R.layout.spinner_view,mUnitOfMeasurementList));
        mOriginalQuantityUnit=quotationHistoryList.get(0).getChat_UnitsOfMeasurement();
        
        for(int i=0;i<mUnitOfMeasurementList.size();i++){
            if(mUnitOfMeasurementList.get(i).getmName().equalsIgnoreCase(mOriginalQuantityUnit)){
                mOriginalQuantityUnitId=i;
                break;
            }
        }
        
        spinner.setSelection(mOriginalQuantityUnitId);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                mSelectedQuantity=mUnitOfMeasurementList.get(position).getmName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                
            }
            
        });
		
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == REQUEST_GET_SINGLE_FILE){
				if (resultCode == RESULT_OK) {
					String filePath = "";
					Uri uri = data.getParcelableExtra("file");
					if(filePath.length() == 0){
					filePath = uri.getPath();
					}
					String[] args = filePath.split("/");
					String mFileName = args[args.length - 1];
					System.out.println("mFileName---" + mFileName);
					arrayList.add(mFileName+"@"+filePath);
					mAdapter = new GetQuortationAdapter(ProductQuotationManufacturerDetailsActivity.this, arrayList);
					attach_list.setAdapter(mAdapter);
					setListViewHeightBasedOnChildren(attach_list);
				}
			}
		}
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            return;
        }

      
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount())))+listAdapter.getCount()+50;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
	
	class GetQuortationAdapter extends BaseAdapter {
		private Context context;
		LayoutInflater inflater;
		private boolean isMilestone;
		private List<String> list;

		public GetQuortationAdapter(Context context,ArrayList<String> arrayList) {
			this.context = context;
			this.list = arrayList;
			inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			if(list == null || list.size() == 0){
				return 0;
			}
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {
			if(view == null){
			view = inflater.inflate(R.layout.activity_attachdoc, arg2,false);
			}
			
			ImageView attch_img = (ImageView) view.findViewById(R.id.attch_img);
			TextView file_name = (TextView) view.findViewById(R.id.file_path);
			ImageView close_file = (ImageView) view.findViewById(R.id.close_attach);
			if(isMilestone){
				attch_img.setVisibility(View.GONE);
			}
			file_name.setText(""+list.get(position).split("@")[0]);
			
			//icon.setVisibility(View.GONE);
			close_file.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					list.remove(position);
					
				}
			});
			return view;
		}

		protected void resetList() {
			if(mAdapter != null){
				mAdapter.notifyDataSetChanged();
			}
			setListViewHeightBasedOnChildren(attach_list);
		}
	}
	
	
	
	
	//TODO milestone popup
	
	private void createMilestonePopup(){
		AlertDialog.Builder builder = new AlertDialog.Builder(ProductQuotationManufacturerDetailsActivity.this);
		final View view = getLayoutInflater().inflate(R.layout.popup_add_milestones, null);
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
		
		dialog.show();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
				
		if(view == null){
			if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
			}
		}
		
		final EditText vE_AM_subject = (EditText) view.findViewById(R.id.vE_AM_subject);
		final EditText vE_AM_percentage = (EditText) view.findViewById(R.id.vE_AM_percentage);
		//final EditText vE_AM_date = (EditText) view.findViewById(R.id.vE_AM_date);
		
		final ListView vL_AM_milestoneListLocal = (ListView) view.findViewById(R.id.vL_AM_milestoneList);
		
		ImageView vI_AM_dialogClose = (ImageView) view.findViewById(R.id.vI_AM_dialogClose);
		
		vI_AM_dialogClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*if(mMilestoneList != null && mMilestoneList.size()>0){
					GetQuortationAdapter adapter = new GetQuortationAdapter(mMilestoneList, true);
					vL_AM_milestoneList.setAdapter(adapter);
					setListViewHeightBasedOnChildren(vL_AM_milestoneList);
				}*/
				if(dialog == null || dialog.isShowing()){
					dialog.cancel();
				}				
			}
		});
		
		Spinner vE_AM_date = (Spinner) view.findViewById(R.id.vE_AM_date);
        vE_AM_date.setAdapter(new SpinnerDateAdapter(getApplicationContext(), R.layout.spinner_view,DateValues));
        
        vE_AM_date.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int newposition = arg2+1; 
                dateselected = DateValues[newposition];
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                
                
            }
        });
		
		view.findViewById(R.id.vI_AQR_sendButton).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mMilestoneList != null && mMilestoneList.size()>0){
					vL_aqsr_milestoneList.setAdapter(milestoneadapter);
					milestoneadapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(vL_aqsr_milestoneList);
				}
				if(dialog == null || dialog.isShowing()){
					dialog.cancel();
				}
			}
		});
		
		TextView vT_AM_addButton = (TextView) view.findViewById(R.id.vT_AM_addButton);
		vT_AM_addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Milestones milestones = new Milestones();
				
				String subject = vE_AM_subject.getText().toString().trim();
				String percentage = vE_AM_percentage.getText().toString().trim();
				
				if(subject.length() == 0){
					Toast.makeText(getApplicationContext(), "enter subject", Toast.LENGTH_SHORT).show();
				}else if(percentage.length() == 0){
					Toast.makeText(getApplicationContext(), "enter percentage", Toast.LENGTH_SHORT).show();
				}else{
					percentage = percentage.replace("%", "");
					double currper = Double.parseDouble(percentage);
					remainPercentage = remainPercentage-currper;   //100.0-50.0
					prevPercentage = currper;                       
					
					if(remainPercentage<0.0){
						remainPercentage = currper+remainPercentage;
						Toast.makeText(getApplicationContext(), "You are paying more than remaing balance", Toast.LENGTH_LONG).show();
					}else{
					vE_AM_percentage.setText(""+remainPercentage+"%");
					
					
					milestones.setmName(subject);
					milestones.setmPercentBreakup(percentage);
					milestones.setmBriefDescription(dateselected);
				
					mMilestoneList.add(milestones);
					if(mMilestoneList != null && mMilestoneList.size()>0){
						vL_AM_milestoneListLocal.setAdapter(milestoneadapter);
						milestoneadapter.notifyDataSetChanged();
						setListViewHeightBasedOnChildren(vL_aqsr_milestoneList);
					}
					}
				}
			}
		});
	}
	
	
	
	private class UpdateQuotation extends AsyncTask<Void, Void, Boolean>{

		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(ProductQuotationManufacturerDetailsActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.show();
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			if(arrayList != null && arrayList.size()>0){
				int len = arrayList.size();
				for(int i=0; i<len; i++){
					uploadFiles(arrayList.get(i).split("@")[0],arrayList.get(i).split("@")[1]);
				}
			}
			 callUpdateQuotation();
			return getQuotaionHistory();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(pDialog != null && pDialog.isShowing()){
				pDialog.cancel();
			}
			if(result){
				getHistoryFromDbAndsetAdapter();
				Toast.makeText(ProductQuotationManufacturerDetailsActivity.this, "success", Toast.LENGTH_SHORT).show();
			
			}else{
				Toast.makeText(ProductQuotationManufacturerDetailsActivity.this, "failure", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}


	public Boolean callUpdateQuotation() {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		try{
			String jsonData = null;
			if(quotationHistoryList != null && quotationHistoryList.size() >0){
				jsonData = createJson();
			}
			if(jsonData != null){
				HttpClient client = new DefaultHttpClient();  
				String postURL = webServices.UPDATEQUOTATION;//"http://vsuppliervm.cloudapp.net:5132/QuotationService.svc/UpdateQuotation";
				InputStream inputStream = null;
				
				HttpPost httpPost = new HttpPost(postURL);
				
				StringEntity se = new StringEntity(jsonData, HTTP.UTF_8);
				httpPost.setEntity(se);
				
				httpPost.setHeader("Content-type", "application/json");
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("JWTKEY","Verisupplier.android.d2335fhfg4564hghjghjghjget45ert.1.0");
				httpPost.setHeader("OS","ANDROID");
				httpPost.setHeader("USERID",mUserDetails.getmUserID());
				
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
					jsonParserClass = new JsonParserClass();
					String rslt=jsonParserClass.parseUpdateQuotationResult(result);
					if(rslt != null && rslt.equalsIgnoreCase("true")){
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
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
			Date today = new Date();
			String date = sdf.format(today);
			mUserSubject = quotationHistoryList.get(0).getHistory_subject();
			
			JSONObject jsn = new JSONObject();
			JSONObject quotationJson = new JSONObject();
			JSONArray chatArray = new JSONArray();
			JSONObject chatObject = new JSONObject();
			JSONObject specsObject = new JSONObject();
			JSONArray additionalinfoArray = new JSONArray();
			JSONArray milestoneArray=new JSONArray();
			JSONObject mileStoneObject=new JSONObject();
		
			if(attachimageUrl==null || attachimageUrl.length()==0){
				//mAttachmentList = ""+mAttachment;
			}else{
				mAttachmentList = ""+mAttachment+""+attachimageUrl;
			}
			
			
			
			if(newLookups!=null || newLookups.size()>0){
				for(int i=0;i<newLookups.size();i++){
					additionalinfoArray.put(newLookups.get(i));
				}
			}else{
				additionalinfoArray.put(0);
			}
			
		
			// specs
			specsObject.put("AdditionalInfo", additionalinfoArray);
			
           if(mMilestoneList==null || mMilestoneList.size()==0){
				
			}else{
			for(int i=0;i<mMilestoneList.size();i++){
				mileStoneObject.put("Name", mMilestoneList.get(i).getmName());
                mileStoneObject.put("PercentBreakup", mMilestoneList.get(i).getmPercentBreakup());
                mileStoneObject.put("NoOfWeeks", mMilestoneList.get(i).getmBriefDescription());
                /*mileStoneObject.put("QuotationId", quoatation_id);
                mileStoneObject.put("QuotationResponseId", 2);*/
                milestoneArray.put(mileStoneObject);
			}
			}
			
			
			specsObject.put("MileStones", milestoneArray);
			
			//chatobject
			chatObject.put("Specs", specsObject);
			chatObject.put("Message", ""+message.getText().toString());
			chatObject.put("ResponseUserType", ""+mResponseUserType);
			chatObject.put("ResponseDate", ""+date);
			chatObject.put("UnitsOfMeasurement", mSelectedQuantity);
			chatObject.put("RequiredQty", ""+quantity.getText().toString().trim());
			
			chatObject.put("OfferedPrize", mPrice); // Doubt
			chatObject.put("QuotationResponseId", 2);//quotationHistoryList.get(0).getHistory_QuotationResponseId());
			chatObject.put("Attachments", mAttachmentList);//https://vsupplierstorage.blob.core.windows.net/images/Xperia.jpg
			chatObject.put("Specs", specsObject);
			chatArray.put(chatObject);
			
			// quotation
			quotationJson.put("LastResponseDate", ""+date);
			quotationJson.put("Subject", mUserSubject);
			quotationJson.put("ServiceGroupType", 1);
			quotationJson.put("Id", ""+quotationHistoryList.get(0).getHistory_id());
			quotationJson.put("QuotationStatusId", mQuotationStatusId);
			quotationJson.put("LastResponseSequenceNumber", ""+quotationHistoryList.get(0).getHistory_LastResponseSequenceNumber());
			quotationJson.put("LastManufacturerResponseId", ""+quotationHistoryList.get(0).getHistory_LastManufacturerResponseId());
			quotationJson.put("ChatData", chatArray);
			
			jsn.put("quotation", quotationJson);
			json = jsn.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}
	
	private Boolean uploadFiles(String mFileName,String mFilePath) {
		webServices=new WebServices();
		jsonParserClass=new JsonParserClass();
		boolean isValid=false;
		byte[] bytes = null ;
		String url = "";
		File file = null;
		try {
			url =webServices.FILEUPLOAD+"/"+mFileName;//"http://vsuppliervm.cloudapp.net:5132/FileAttachmentService.svc/FileUpload/";
			file = new File(mFilePath);         
		}catch(Exception e) {
			e.printStackTrace();
		}
		try { 
			FileInputStream fis = new FileInputStream(file);
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    byte[] buf = new byte[1024];
		    try {
		    	for (int readNum; (readNum = fis.read(buf)) != -1;) {
		    		bos.write(buf, 0, readNum); //no doubt here is 0
		            System.out.println("read " + readNum + " bytes,");
		        }
		    } catch (IOException ex) {
		    }
	        bytes = bos.toByteArray();
	        
		} catch (Exception e) {              
			Log.d("Exception","Exception");
			e.printStackTrace();
		}                
		try {
			ByteArrayInputStream instream = new ByteArrayInputStream(bytes);          
		    HttpClient httpclient = new DefaultHttpClient();
		    url +="/"+bytes.length;
		    HttpPost httppost = new HttpPost(url);
		    InputStreamEntity reqEntity = new InputStreamEntity(instream,bytes.length);
		    httppost.setEntity(reqEntity);
		    reqEntity.setContentType("binary/octet-stream");
		      
		    httppost.setHeader("Content-type", "application/octet-stream");
		    reqEntity.setChunked(true); // Send in multiple parts if needed
		    reqEntity.setContentEncoding("utf-8");
		    HttpResponse response = httpclient.execute(httppost);
		    HttpEntity entity = response.getEntity();             
		    String result = EntityUtils.toString(entity);
		    
		    if(result==null || result.length()==0){
		    	isValid=false;
		    }else{
		    	 attachimageUrl =attachimageUrl+","+jsonParserClass.parseAttachImageUrl(result);
		    	 isValid=true;
		    }
		    
		    
		    int statusCode = response.getStatusLine().getStatusCode();
		    Log.d("statusCode",""+statusCode);
		    if(statusCode != 200){
		    	return false;
		    }
		    isValid=true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Exception in AsyncTask-doInBackgroung", e.toString());
		} 
		return isValid;
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
	
	public Boolean getQuotaionHistory() {
		Boolean isValidHistory = false;
		String quoatation_id = quotationHistoryList.get(0).getHistory_id();
		if (isValidHistory.equals(false)) {
			Log.e("Quotation Id", quoatation_id);
			String resultOutparam = webServices.CallWebHTTPBindingService(ApiType.GetQuotationHistory, "GetQuotationHistory/",quoatation_id);
			if (resultOutparam == null || resultOutparam.length() == 0) {
				isValidHistory = false;
			} else {
				List<QuotationDashboardHistory> quotationDashboardHistories = jsonParserClass.parseAllQuotationHistory(resultOutparam);
				if(quotationDashboardHistories == null|| quotationDashboardHistories.size() == 0){
					isValidHistory = false;
				} else {
					isValidHistory = true;
					Helper helper = new Helper(ProductQuotationManufacturerDetailsActivity.this);
					helper.openDataBase();
					helper.insertQuotationHistory(quotationDashboardHistories);
					helper.close();
				}
			}
		} else {
			isValidHistory = false;
		}

		return isValidHistory;
	}
	
	private class LookUpAdapter extends BaseAdapter{
		
		List<Lookups> lookups;

		public LookUpAdapter(List<Lookups> mQuotationLookups) {
			this.lookups = mQuotationLookups;
		}

		@Override
		public int getCount() {
			if(lookups == null || lookups.size()==0){
				return 0;
			}else{
				return lookups.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView =getLayoutInflater().inflate(R.layout.customlookup_view, parent, false);
			}
			
			optional_text=(CheckBox)convertView.findViewById(R.id.vC_aclu_check);
			vC_aclu_optional_textview = (TextView) convertView.findViewById(R.id.vC_aclu_optional_textview);
			final Lookups mLookups = mQuotationLookup.get(position);
			
			Log.e("Lookups",""+mLookups);
			vC_aclu_optional_textview.setText(""+mLookups.getmName());
			
			if(mLookUpListWithId.contains(mLookups.getmId())){
				optional_text.setChecked(true);
				
				if(newLookups.contains(mLookups.getmId())){
					
				}else{
				newLookups.add(mLookups.getmId());
				Log.e("The new List", ""+newLookups.size());
				}
			}else{
				optional_text.setChecked(false);
				
			}
			
			
			
			optional_text.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						if(checkOptionalDetail.contains(mLookups)){
							checkOptionalDetail.remove(mLookups);
							newLookups.remove(mLookups.getmId());
						}else{
							checkOptionalDetail.add(mLookups);
							newLookups.add(mLookups.getmId());
						}
					}else{
							checkOptionalDetail.remove(mLookups);
							newLookups.remove(mLookups.getmId());
							//mLookUpListWithId = new ArrayList<String>();
						
					}
				}
			});
			
		
			
			return convertView;
		}
		
	}
	
	public class SpinnerAdapter extends ArrayAdapter<Lookups> {
        TextView tv;
        List<Lookups> mTsoList;
        Context mContext;
        int mResource = 0;
        public SpinnerAdapter(Context context, int resource, List<Lookups> objects) {
            super(context, resource, objects);
            this.mContext = context;
            this.mResource = resource;
            this.mTsoList = objects;
        }

        @Override
        public int getCount() {
            return mTsoList.size();
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_view, parent, false);
            }
            
            tv = (TextView) convertView.findViewById(R.id.quantity_name_spinner);
            String name = mTsoList.get(position).getmName().toString();
            //mSelectedQuantity=name;
            
            tv.setTextSize(15);
            tv.setText(""+name);
            return convertView;
        }
        
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_view, parent, false);
            }
            tv = (TextView) convertView.findViewById(R.id.quantity_name_spinner);
            String name = mTsoList.get(position).getmName().toString();
            tv.setText(""+name);
            //mSelectedQuantity=name;
            return convertView;
        }
    }
	
	public class DocumentAdapter extends BaseAdapter{
	
	ArrayList<String> doc_arraylist;

		
		public DocumentAdapter(ArrayList<String> document_url_list) {
			this.doc_arraylist = document_url_list;
		}

	@Override
	public int getCount() {
		if(doc_arraylist==null || doc_arraylist.size()==0){
			return 0;
		}else{
			return doc_arraylist.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = getLayoutInflater().inflate(R.layout.adapter_document, null);
		}
		ImageView document_image = (ImageView) convertView.findViewById(R.id.document1);
		
		document_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			try{	
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(doc_arraylist.get(position)));
			startActivity(browserIntent);
			}catch(Exception e){
				
			}
			}
			
		});
		
		return convertView;
	}
}
	
	
	
	
	public class MileStoneAdapter extends BaseAdapter{
		ArrayList<Milestones> add_milestone;
		
		public MileStoneAdapter(ArrayList<Milestones> mMilestoneList){
			this.add_milestone = mMilestoneList;
		}

		@Override
		public int getCount() {
			return add_milestone.size();
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
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			if(convertView==null){
				convertView = getLayoutInflater().inflate(R.layout.adapter_milestone, null);
			}
			
			
			TextView vT_AM_milestoneName = (TextView) convertView.findViewById(R.id.vT_AM_milestoneName);
			TextView vT_AM_percentBreakup = (TextView) convertView.findViewById(R.id.vT_AM_percentBreakup);
			TextView vT_AM_briefDescription = (TextView) convertView.findViewById(R.id.vT_AM_briefDescription);
			
			vT_AM_milestoneName.setText(add_milestone.get(arg0).getmName());
			vT_AM_percentBreakup.setText(add_milestone.get(arg0).getmPercentBreakup());
			vT_AM_briefDescription.setText(add_milestone.get(arg0).getmBriefDescription());
			
			
			return convertView;
		}
		
		
		
	}
	
	
	public class MileStoneDashBoardAdapter extends BaseAdapter{
		ArrayList<Milestones> add_milestonedashboard;
		
		public MileStoneDashBoardAdapter(ArrayList<Milestones> mMilestoneList){
			this.add_milestonedashboard = mMilestoneList;
		}

		@Override
		public int getCount() {
			return add_milestonedashboard.size();
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
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			if(convertView==null){
				convertView = getLayoutInflater().inflate(R.layout.adapter_milestonedashboard, null);
			}
			
			String milestonenamevalue = add_milestonedashboard.get(arg0).getmName();
			String milestonepercentagevalue = add_milestonedashboard.get(arg0).getmPercentBreakup();
			String milestonedatevalue = add_milestonedashboard.get(arg0).getmBriefDescription();
			
			String mMilestonewholevalue = milestonenamevalue+" "+milestonepercentagevalue+"% on "+milestonedatevalue;
			
			TextView vT_AM_milestoneName = (TextView) convertView.findViewById(R.id.vT_AMD_milestoneName);
		    
			vT_AM_milestoneName.setText(mMilestonewholevalue);
			
			
			return convertView;
		}
		
	}
	
	public class SpinnerDateAdapter extends ArrayAdapter<Lookups> {
        TextView tv;
        int name;
        String mDateArray[];
        Context mContext;
        int mResource = 0;
        
        public SpinnerDateAdapter(Context context, int resource, String objects[]) {
            super(context, resource);
            this.mContext = context;
            this.mResource = resource;
            this.mDateArray = objects;
        }

        @Override
        public int getCount() {
            return 99;//mDateArray.length;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_view, parent, false);
            }
            int weeks = position+1;
            tv = (TextView) convertView.findViewById(R.id.quantity_name_spinner);
            tv.setText(""+weeks);
            
            
            return convertView;
        }
        
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_view, parent, false);
            }
            int weeks = position+1;
            tv = (TextView) convertView.findViewById(R.id.quantity_name_spinner);
            tv.setText(""+weeks);
            return convertView;
        }
    }
	
	
	
}
