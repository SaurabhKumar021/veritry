package com.mmadapps.verisupplier.leftmenu;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmadapps.verisupplier.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GalleryActivity extends Activity {

	private ArrayList<File> fileList = new ArrayList<File>();
	private ImageAdapter imageAdapter;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_imagegallery);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		try {
			options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
			.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
			.build();
		} catch (Exception e) {
		}

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
		imageAdapter = new ImageAdapter(this);
		gridview.setAdapter(imageAdapter);
		String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File targetDirector = new File(ExternalStorageDirectoryPath);
		getfile(targetDirector);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent resultdata = new Intent();
				resultdata.putExtra("filepath", ""+ fileList.get(position).getAbsolutePath());
				setResult(RESULT_OK, resultdata);
				finish();
			}
		});
	}

	public ArrayList<File> getfile(File dir) {
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isDirectory()) {
					getfile(listFile[i]);
				} else {
					if (listFile[i].getName().endsWith(".png") || listFile[i].getName().endsWith(".jpg")
							|| listFile[i].getName().endsWith(".jpeg")) {
						if (Integer.parseInt(String.valueOf(listFile[i].length() / 1024)) > 50) {
							fileList.add(listFile[i]);
							imageAdapter.add(listFile[i].getAbsolutePath());
						}
					}
				}
			}
		}
		return fileList;
	}

	class ImageAdapter extends BaseAdapter {
		private Context context;
		ArrayList<String> imageList = new ArrayList<String>();

		public ImageAdapter(Context c) {
			context = c;
		}

		void add(String path) {
			imageList.add(path);
		}

		@Override
		public int getCount() {
			return imageList.size();
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
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.adapter_gridview_images, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.imageView_gridSelection);
				holder.nameOfImage = (TextView) convertView.findViewById(R.id.nameOfImage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			imageLoader.displayImage("file:///" + imageList.get(position),holder.image, options);
			holder.nameOfImage.setText(""+ imageList.get(position).split("/")[imageList.get(position).split("/").length - 1]);
			return convertView;
		}
	}

	class ViewHolder {
		TextView nameOfImage;
		ImageView image;
	}
}
