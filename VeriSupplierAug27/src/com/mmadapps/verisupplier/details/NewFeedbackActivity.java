package com.mmadapps.verisupplier.details;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.background.JsonParserClass;
import com.mmadapps.verisupplier.background.WebServices;
import com.mmadapps.verisupplier.background.WebServices.ApiType;
import com.mmadapps.verisupplier.beans.FeedbackDetails;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.customs.ConnectionDetector;

public class NewFeedbackActivity extends BaseActionBarActivity {
	
	List<ProductDetails> mProductDetails=new ArrayList<ProductDetails>();
	List<FeedbackDetails> mFeedbackDetails=new ArrayList<FeedbackDetails>();
	
	String mProductid;
	String mManufacturerId;
	ListView feedbackList;
	FeedbackAdapter mAdapter;
	TextView rate_data,average_data,feedback_totalcount;
	TextView onestar_value,twostare_value,threestar_value,fourstar_value,fivestar_value;
	RatingBar averageRating;
	ProgressBar onestar_progress,twostar_progress,threestar_progress,fourstar_progress,fivestar_progress;
	String valueone,valuetwo,valuethree,valuefour,valuefive;
	LinearLayout feedbackdetail_layout,nofeedback_layout;
	ImageView ratenow_image;
	
	String mFromActivity;
	String mUserFeedback;
	double ratingValue;
	
	//services
	WebServices webServices;
	JsonParserClass jsonParserClass;
	private ProgressDialog pdLoading=null;
	
	//from database
	UserDetails mUserDetails=new UserDetails();
	List<ManufacturerDetails> mManufacturerDetails=new ArrayList<ManufacturerDetails>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_fragment);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("Feedback Details");
		
		mProductid = getIntent().getStringExtra("PRODUCTID");
		mFromActivity=getIntent().getStringExtra("FROMACTIVITY");
		mManufacturerId=getIntent().getStringExtra("MANUFACTURERID");
		getFeedbackDetails();
		initView();
		
	}
	

	private void getFeedbackDetails() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mProductDetails=helper.getAllProductDetails(mProductid);
		mFeedbackDetails=helper.getFeedbackDetails(mProductid);
		mUserDetails=helper.getUserDetails();
		mManufacturerDetails=helper.getManufacturerDetails(mManufacturerId);
		helper.close();
	}
	
	private void initView() {
		feedbackdetail_layout=(LinearLayout)findViewById(R.id.vL_ff_feedbackDetails);
		nofeedback_layout=(LinearLayout)findViewById(R.id.vL_ff_nofeedbacklayout);
		
		rate_data = (TextView) findViewById(R.id.vT_fbf_feedbackcount_text);
		average_data = (TextView) findViewById(R.id.vT_fbf_averagestar_text);
		feedback_totalcount=(TextView) findViewById(R.id.vT_fbf_totalfeedback);
		averageRating=(RatingBar) findViewById(R.id.rb_fbf_average_rating);
		onestar_value=(TextView) findViewById(R.id.vT_fbf_onestar_value);
		twostare_value=(TextView) findViewById(R.id.vT_fbf_twostar_value);
		threestar_value=(TextView) findViewById(R.id.vT_fbf_threestar_value);
		fourstar_value=(TextView) findViewById(R.id.vT_fbf_fourstar_value);
		fivestar_value=(TextView) findViewById(R.id.vT_fbf_fivestar_value);
		
		onestar_progress=(ProgressBar) findViewById(R.id.pb_fbf_onestar_progress);
		twostar_progress=(ProgressBar) findViewById(R.id.pb_fbf_twostar_progress);
		threestar_progress=(ProgressBar) findViewById(R.id.pb_fbf_threestar_progress);
		fourstar_progress=(ProgressBar) findViewById(R.id.pb_fbf_fourstar_progress);
		fivestar_progress=(ProgressBar) findViewById(R.id.pb_fbf_fivestar_progress);
		
		feedbackList = (ListView) findViewById(R.id.feedBackList);
		setUserFeedbackAdapter();
		
		ratenow_image=(ImageView)findViewById(R.id.vI_fbf_ratenow);
		ratenow_image.setOnClickListener(this);
		
		final Typeface Sui_Bold;
		final Typeface Sui_Light;
		Sui_Bold = BaseActionBarActivity.getSegeo_Bold();
		Sui_Light = BaseActionBarActivity.getSegeo_Light();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					average_data.setTypeface(Sui_Bold);
				} catch (Exception e) {
				}
			}
		};
		new Thread(runnable).run();
		Runnable runnable2 = new Runnable() {
			@Override
			public void run() {
				try {
					rate_data.setTypeface(Sui_Light);
				} catch (Exception e) {
				}
			}
		};
		new Thread(runnable2).run();
		setValues();
	}
	
	private void setValues() {
		if(mProductDetails==null || mProductDetails.size()==0){
			nofeedback_layout.setVisibility(View.VISIBLE);
			feedbackdetail_layout.setVisibility(View.GONE);
		}else{
			nofeedback_layout.setVisibility(View.GONE);
			feedbackdetail_layout.setVisibility(View.VISIBLE);
			if(mProductDetails.get(0).getmProduct_averageRating()==null || mProductDetails.get(0).getmProduct_averageRating().length()==0){
				nofeedback_layout.setVisibility(View.VISIBLE);
				feedbackdetail_layout.setVisibility(View.GONE);
			}else{
				averageRating.setRating(Float.parseFloat(""+mProductDetails.get(0).getmProduct_averageRating()));
			}
			
			if(mProductDetails.get(0).getmFeedback_stars()==null || mProductDetails.get(0).getmFeedback_stars().length()==0){
				nofeedback_layout.setVisibility(View.VISIBLE);
				feedbackdetail_layout.setVisibility(View.GONE);
			}else{
				if(mProductDetails.get(0).getmFeedback_stars().contains(",")){
					String[] args=mProductDetails.get(0).getmFeedback_stars().split(",");
					valueone=args[0];
					valuetwo=args[1];
					valuethree=args[2];
					valuefour=args[3];
					valuefive=args[4];
					
					onestar_value.setText(""+valueone);
					twostare_value.setText(""+valuetwo);
					threestar_value.setText(""+valuethree);
					fourstar_value.setText(""+valuefour);
					fivestar_value.setText(""+valuefive);
					
					onestar_progress.setProgress(Integer.parseInt(valueone));
					twostar_progress.setProgress(Integer.parseInt(valuetwo));
					threestar_progress.setProgress(Integer.parseInt(valuethree));
					fourstar_progress.setProgress(Integer.parseInt(valuefour));
					fivestar_progress.setProgress(Integer.parseInt(valuefive));
				}else{
					onestar_value.setText(""+mProductDetails.get(0).getmFeedback_stars());
					onestar_progress.setProgress(Integer.parseInt(mProductDetails.get(0).getmFeedback_stars()));
				}
				
			}
			
			feedback_totalcount.setText("Feedback ("+mProductDetails.get(0).getmTotalFeedBackCount()+")");
		}
		
	}
	
	private void setUserFeedbackAdapter() {
		mAdapter = new FeedbackAdapter();
		feedbackList.setAdapter(mAdapter);
		setListViewHeightBasedOnChildren(feedbackList);
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
        params.height = (totalHeight + listView.getDividerHeight() * (listAdapter.getCount()+listAdapter.getViewTypeCount()))+15;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
	
	private class FeedbackAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if(mFeedbackDetails==null || mFeedbackDetails.size()==0){
				return 0;
			}else{
				return mFeedbackDetails.size();
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
			if (convertView == null) {
				convertView =getLayoutInflater().inflate(R.layout.feedback_view, parent, false);
			}
			final TextView feedtext = (TextView) convertView.findViewById(R.id.vT_fv_feedbcktxt);
			final TextView feedbackdate = (TextView) convertView.findViewById(R.id.vT_fv_feedback_date);
			final TextView feedbackuser =(TextView)convertView.findViewById(R.id.vT_fv_feedback_username);
			final TextView feedbackcountryname=(TextView)convertView.findViewById(R.id.vT_fv_feedback_countryname);
			final RatingBar feedbackRating=(RatingBar)convertView.findViewById(R.id.rb_fv_feedbackrating);

			final Typeface Sui_Bold;
			final Typeface Sui_Light;
			Sui_Bold = BaseActionBarActivity.getSegeo_Bold();
			Sui_Light = BaseActionBarActivity.getSegeo_Light();
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						feedtext.setTypeface(Sui_Bold);
					} catch (Exception e) {
					}
				}
			};
			new Thread(runnable).run();
			Runnable runnable2 = new Runnable() {
				@Override
				public void run() {
					try {
						feedbackdate.setTypeface(Sui_Light);
						feedbackuser.setTypeface(Sui_Light);
						feedbackcountryname.setTypeface(Sui_Light);
					} catch (Exception e) {
					}
				}
			};
			new Thread(runnable2).run();
			feedtext.setText(""+mFeedbackDetails.get(position).getmFeedBackText());
			feedbackdate.setText(""+mFeedbackDetails.get(position).getmCreatedDate());
			feedbackuser.setText(""+mFeedbackDetails.get(position).getmFeedbackUserName());
			feedbackcountryname.setText(""+mFeedbackDetails.get(position).getmFeedbackLocation());
			feedbackRating.setRating(Float.parseFloat(mFeedbackDetails.get(position).getmFeedbackRating()));

			return convertView;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vI_fbf_ratenow:
			Intent feedbackIntent=new Intent(NewFeedbackActivity.this,UserFeedBackPopupActivity.class);
			feedbackIntent.putExtra("FROMACTIVITY", "productfeedback");
			feedbackIntent.putExtra("PRODUCTID", mProductid);
			feedbackIntent.putExtra("MANUFACTURERID", mManufacturerId);
			startActivity(feedbackIntent);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		getFeedbackDetails();
		setValues();
		setUserFeedbackAdapter();
		super.onResume();
	}
	
}
