package com.mmadapps.verisupplier.homepage;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.mmadapps.verisupplier.background.Helper;
import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class AllCategoryActivity extends BaseActionBarActivity{
	
	static ListView listView;
	static ListAdapter listAdapter;
	String text[];
	int image[];
	private static String selected = "";
	
	private List<CategoryDetails> mMainCategoryList=new ArrayList<CategoryDetails>();
	private Helper mHelper;
	String mProducyID="";
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	String home_selected="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		//BaseActionBarActivity.search_layout.setVisibility(View.GONE);
		//BaseActionBarActivity.title_layout.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		BaseActionBarActivity.setmUserName("All Categories");
		
		mHelper = new Helper(AllCategoryActivity.this);
		mHelper.openDataBase();
	
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
		
		
		String head = "";		
		try{
		    selected = getIntent().getStringExtra("CATEGORYID");
		    home_selected=getIntent().getStringExtra("CATEGORYID");
		    head = getIntent().getStringExtra("CATEGORYNAME");
		    if(selected == null || selected.length() == 0){
		    	selected = "";
		    }
		    if(head == null || head.length() == 0){
		    	head = "All Categories";
		    }
		}catch(Exception e){
			head = "All Categories";
		    selected = "";
		    e.printStackTrace();
		}
		
		vBackView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(home_selected == selected){
					finish();
				}else if(selected == null || selected.equalsIgnoreCase("") || mMainCategoryList == null || mMainCategoryList.size() == 0){
					finish();
				}else{
					String pid = mMainCategoryList.get(0).getmCategory_ParentId();
					CategoryDetails ac = mHelper.getCategorywithId(pid);
					if(ac == null || ac.getmCategory_Id() == null || ac.getmCategory_ParentId() == null){
						finish();
					}else{
						selected = ac.getmCategory_ParentId();
						mMainCategoryList = mHelper.getAllCategories(selected);
						
						if(selected.equalsIgnoreCase("0")){
							reftreshList("All Categories");
						}else{
							CategoryDetails a = mHelper.getCategorywithId(ac.getmCategory_ParentId());
							reftreshList(""+a.getmCategory_Name());
						}
					}
				}
			}
		});
		
		initializeValues();
		loadDataFromDB();
		reftreshList(head);
	}
	
	private void loadDataFromDB() {
		Helper helper=new Helper(getApplicationContext());
		helper.openDataBase();
		mMainCategoryList = helper.getAllCategories(selected);
		helper.close();
	}
	
	private void initializeValues() {
		listView = (ListView) findViewById(R.id.listView1);
	}
	
	protected void reftreshList(String s) {
		BaseActionBarActivity.setmUserName(""+s);
		listAdapter = new ListAdapter();
		listView.setAdapter(listAdapter);        
	}
	
	private class ListAdapter extends  BaseAdapter{
		
		@Override
		public int getCount() {
			mHelper = new Helper(AllCategoryActivity.this);
			mHelper.openDataBase();
			if(mMainCategoryList == null || mMainCategoryList.size() ==0){
				return 0;
			}
			return mMainCategoryList.size();
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
				convertView = getLayoutInflater().inflate(R.layout.activity_allcategories, parent, false);
			}
			final TextView t1 = (TextView) convertView.findViewById(R.id.textview1);
			ImageView image1 = (ImageView) convertView.findViewById(R.id.imageview1);
			ImageView vI_Image_Arrow_next = (ImageView) convertView.findViewById(R.id.vI_Image_Arrow_next);
			
			CategoryDetails category = mMainCategoryList.get(position);
			final String key = category.getmCategory_Id();
			t1.setText(category.getmCategory_Name());
			if(selected == null || selected.equalsIgnoreCase("0") || selected.equalsIgnoreCase("")){
				image1.setVisibility(View.VISIBLE);
				imageLoader.displayImage(category.getmCategory_Image(), image1, options);
			}else{
				image1.setVisibility(View.GONE);
			}
			
			final List<CategoryDetails> categories = mHelper.getAllCategories(key);
			
			if(categories == null || categories.size() ==0){
				vI_Image_Arrow_next.setVisibility(View.GONE);
			}else{
				vI_Image_Arrow_next.setVisibility(View.VISIBLE);
			}
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selected = key;
					if(categories != null && categories.size() != 0){
						mMainCategoryList = categories;
						reftreshList(""+t1.getText().toString());
					}else{
						mProducyID=selected;
					}		 
				}
			});
			
			mHelper.close();
			return convertView;
		}
	}
	
	@Override
	public void onBackPressed() {
		vBackView.performClick();
	}

}
