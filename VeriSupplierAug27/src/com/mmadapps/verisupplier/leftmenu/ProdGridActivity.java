package com.mmadapps.verisupplier.leftmenu;

import android.app.ActionBar.LayoutParams;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.mmadapps.verisupplier.BaseActionBarActivity;
import com.mmadapps.verisupplier.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ProdGridActivity extends BaseActionBarActivity {
	GridView productsGrid;
	PopupWindow mpopup;  
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prod_grid);
		productsGrid = (GridView) findViewById(R.id.dealsGrid);
		
		//BaseActionBarActivity.search_layout.setVisibility(View.GONE);
		//BaseActionBarActivity.title_layout.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vBackView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.vLeftView.setVisibility(View.GONE);
		BaseActionBarActivity.vRigthView.setVisibility(View.VISIBLE);
		BaseActionBarActivity.qrscan_image.setVisibility(View.GONE);
		
		BaseActionBarActivity.setmUserName("MyOpen Quote");
		try {
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.ic_launcher)
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                    .cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
                    .build();
        } catch (Exception e) {
        }
		
		productsGrid.setAdapter(new ProductsAdapter());
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(getApplicationContext()));
		productsGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				 View popUpView = getLayoutInflater().inflate(R.layout.activity_image_pop_up,
				            null); // inflating popup layout
				    mpopup = new PopupWindow(popUpView, LayoutParams.FILL_PARENT,
				            LayoutParams.WRAP_CONTENT, true); // Creation of popup
				  mpopup = new PopupWindow(popUpView, LayoutParams.FILL_PARENT-50,
     LayoutParams.WRAP_CONTENT-20, true); // Creation of popup
				 mpopup = new PopupWindow(popUpView, LayoutParams.MATCH_PARENT,
					        LayoutParams.MATCH_PARENT, true);
				 mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
				    mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0); // Displaying popup
					//Button btnCancel = (Button) popUpView.findViewById(R.id.);
				
				    ImageView product = (ImageView)popUpView.findViewById(R.id.grid_product_Image);
				    try
					{
				//   Bitmap myBitmap = BitmapFactory.decodeFile(UIActivity.myList.get(position));
				    	//Bitmap myBitmap =BitmapFactory.decodeFileDescriptor(UIActivity.myList.get(position));
				    //	Bitmap myBitmap =BitmapFactory.decodeFileDescriptor(UIActivity.myList.get(position))
					imageLoader.displayImage("file:///" + Myopenquoate.myList.get(position), product,
							options);
				   // product.setImageBitmap(myBitmap);
//				    myBitmap=null;
				    
					//clearBitmap(myBitmap);
					}
					catch(Exception e)
					{
						imageLoader.displayImage("file:///" + Myopenquoate.myList.get(position), product,
								options);
						//Toast.makeText(getApplicationContext(), "out of memory", Toast.LENGTH_LONG).show();
					}
				    product.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mpopup.dismiss();
						}
					});
				
/*				  ImageView tempImageView = imageView;


			        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
			        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

			        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
			                (ViewGroup) findViewById(R.id.layout_root));
			        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
			        image.setImageDrawable(tempImageView.getDrawable());
			        imageDialog.setView(layout);
			        imageDialog.setPositiveButton(resources.getString(R.string.ok_button), new DialogInterface.OnClickListener(){

			            public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			            }

			        });


			        imageDialog.create();
			        imageDialog.show();  */
				
				
				
				
				
			}
		});
		
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		 if(mpopup.isShowing())
		 mpopup.dismiss();
	}
	
	public static void clearBitmap(Bitmap bm) {

		bm.recycle();

		System.gc();

		}

	private class ProductsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return Myopenquoate.myList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.adapter_products, parent, false);
			}
			ImageView vI_hd_Image = (ImageView) convertView.findViewById(R.id.vI_hd_Image);
//			=null;
			try
			{
//				Bitmap myBitmap = BitmapFactory.decodeFile(UIActivity.myList.get(position));
				imageLoader.displayImage("file:///" + Myopenquoate.myList.get(position), vI_hd_Image,
						options);
			//	vI_hd_Image.setImageBitmap(myBitmap);
//				myBitmap=null;
			//	clearBitmap(myBitmap);
				
			}
			catch(Exception e)
			{
				imageLoader.displayImage("file:///" + Myopenquoate.myList.get(position), vI_hd_Image,
						options);
				//Toast.makeText(getApplicationContext(), "out of memory", Toast.LENGTH_LONG).show();
			}
			//vI_hd_Image.setImageBitmap(myBitmap);
			//vI_hd_Image.setImageResource(HomeActivity.productImage[position]);
			return convertView;
		}
		
	}
}
