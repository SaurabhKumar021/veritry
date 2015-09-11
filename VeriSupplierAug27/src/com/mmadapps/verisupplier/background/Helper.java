package com.mmadapps.verisupplier.background;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.FeedbackDetails;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.ManufacturerProductDetails;
import com.mmadapps.verisupplier.beans.Milestones;
import com.mmadapps.verisupplier.beans.OrderDashboard;
import com.mmadapps.verisupplier.beans.Orders;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.ProductImageDetails;
import com.mmadapps.verisupplier.beans.ProductList;
import com.mmadapps.verisupplier.beans.ProductSpecificationDetails;
import com.mmadapps.verisupplier.beans.QuoatationDashboard;
import com.mmadapps.verisupplier.beans.QuotationDashboardHistory;
import com.mmadapps.verisupplier.beans.Reguserdetails;
import com.mmadapps.verisupplier.beans.ServiceDetails;
import com.mmadapps.verisupplier.beans.ServiceList;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.beans.WishlistDetails;
import com.mmadapps.verisupplier.customs.VerisupplierUtils;

public class Helper extends SQLiteOpenHelper{

	private static String DB_PATH = "/data/data/com.mmadapps.verisupplier/databases/";
	private static String DB_NAME = "VeriSupplier.sqlite";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private String TAG = "Helper";
	Cursor cursorGetData;
	String sigment;

	public Helper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 * creating database at first time of application Setup
	 * 
	 * @throws IOException
	 */
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * checking the database Availability based on Availability copying database
	 * to the device data
	 * 
	 * @return true (if Available)
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			Log.e(TAG, "Error is" + e.toString());
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * copying database from asserts to package location in mobile data
	 * 
	 * @throws IOException
	 */
	private void copyDataBase() throws IOException {
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/**
	 * Opening database for retrieving/inserting information
	 * 
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	/**
	 * Closing database after operation done
	 */
	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	/**
	 * getting information based on SQL Query
	 * 
	 * @param sql
	 * @return Output of Query
	 */
	private Cursor getData(String sql) {
		openDataBase();
		cursorGetData = getReadableDatabase().rawQuery(sql, null);
		return cursorGetData;
	}

	/**
	 * Inserting information based on table name and values
	 * 
	 * @param tableName
	 * @param values
	 * @return
	 */
	private long insertData(String tableName, ContentValues values) {
		openDataBase();
		return myDataBase.insert(tableName, null, values);
	}
	
	
	public void insertregUserDetails(Reguserdetails rUserDetails, boolean isnewlogIn) {
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM reguserdetails_master");
			Log.e("Deleting reguserdetails_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting reguserdetails_master working", "Error");
		}
		try {
			ContentValues initialValues = new ContentValues();
			
			initialValues.put("useremail",rUserDetails.getUseregmail());
			initialValues.put("usermob", rUserDetails.getUseregmobile());
			initialValues.put("userpass", rUserDetails.getUserregpass());
			initialValues.put("usertype", rUserDetails.getUserregtype());
			
			
			long rowId = insertData("reguserdetails_master", initialValues);
			if (rowId != 0) {
			}
			Log.e("Inserting reguserdetails_master", "Done" + mTotalInsertedValues);
		} catch (Exception e) {
			Log.e("Inserting reguserdetails_master", "Error");
			e.printStackTrace();
		}
	}
	
	/*public void insertUserDetails(UserDetails mUserDetails, boolean islogedIn) {
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM user_master");
			Log.e("Deleting user_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting user_master working", "Error");
		}
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put("user_id", mUserDetails.getmUserID());
			initialValues.put("user_name", mUserDetails.getmUserName());
			initialValues.put("user_email", mUserDetails.getmUserEmail());
			initialValues.put("user_mobile", mUserDetails.getmUserMobile());
			initialValues.put("user_password", mUserDetails.getmUserPassword());
			initialValues.put("user_IsActive", mUserDetails.getmIsActive());
			initialValues.put("user_image", mUserDetails.getmUserImage());
			initialValues.put("user_isLogedIn", islogedIn);
			
			long rowId = insertData("user_master", initialValues);
			if (rowId != 0) {
			}
			Log.e("Inserting user_master", "Done" + mTotalInsertedValues);
		} catch (Exception e) {
			Log.e("Inserting user_master", "Error");
			e.printStackTrace();
		}
	}

	public UserDetails getUserDetails() {
		Cursor cursor;
		UserDetails mUserDetails = new UserDetails();
		cursor = getData("SELECT * FROM user_master");

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				mUserDetails.setmUserID(cursor.getString(0));
				mUserDetails.setmUserName(cursor.getString(1));
				mUserDetails.setmUserEmail(cursor.getString(2));
				mUserDetails.setmUserMobile(cursor.getString(3));
				mUserDetails.setmUserPassword(cursor.getString(4));
				mUserDetails.setmUserImage(cursor.getString(5));
				mUserDetails.setmIsActive(cursor.getString(6));
				
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mUserDetails;
	}*/
	
	public void insertUserDetails(UserDetails mUserDetails, boolean islogedIn) {
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM user_master");
			Log.e("Deleting user_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting user_master working", "Error");
		}
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put("user_id", mUserDetails.getmUserID());
			initialValues.put("user_name", mUserDetails.getmName());
			initialValues.put("user_email", mUserDetails.getmUserEmail());
			initialValues.put("user_mobile", mUserDetails.getmUserMobile());
			initialValues.put("user_password", mUserDetails.getmUserPassword());
			initialValues.put("user_IsActive", mUserDetails.getmIsActive());
			initialValues.put("user_image", mUserDetails.getmUserImage());
			initialValues.put("user_isLogedIn", islogedIn);
			initialValues.put("user_type_id", mUserDetails.getmUserTypeId());
			initialValues.put("user_type", mUserDetails.getmUserType());
			initialValues.put("manufacturer_id", mUserDetails.getmManufacturerId());
			
			long rowId = insertData("user_master", initialValues);
			if (rowId != 0) {
			}
			Log.e("Inserting user_master", "Done" + mTotalInsertedValues);
		} catch (Exception e) {
			Log.e("Inserting user_master", "Error");
			e.printStackTrace();
		}
	}

	public UserDetails getUserDetails() {
		Cursor cursor;
		UserDetails mUserDetails = new UserDetails();
		cursor = getData("SELECT * FROM user_master");

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				mUserDetails.setmUserID(cursor.getString(0));
				mUserDetails.setmUserName(cursor.getString(1));
				mUserDetails.setmUserEmail(cursor.getString(2));
				mUserDetails.setmUserMobile(cursor.getString(3));
				mUserDetails.setmUserPassword(cursor.getString(4));
				mUserDetails.setmUserImage(cursor.getString(5));
				mUserDetails.setmIsActive(cursor.getString(6));
				mUserDetails.setmUserTypeId(cursor.getString(8));
				mUserDetails.setmUserType(cursor.getString(9));	
				mUserDetails.setmManufacturerId(cursor.getString(10));
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mUserDetails;
	}


	public long insertCategoryDetails(List<CategoryDetails> mCategoryDetails) {
		SQLiteDatabase db = this.getWritableDatabase();
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM category_master");
			Log.e("Deleting category_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting category_master working", "Error");
		}
		try {
			
			if (mCategoryDetails != null && mCategoryDetails.size() > 0) {
				for (CategoryDetails category : mCategoryDetails) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("category_id", "" +category.getmCategory_Id());
					initialValues.put("category_name", "" +category.getmCategory_Name());
					initialValues.put("category_image", "" +category.getmCategory_Image());
					initialValues.put("parent_id", "" +category.getmCategory_ParentId());
					initialValues.put("lastdownload_datetime", dateTime);
					long rowId = insertData("category_master", initialValues);
					if (rowId != 0) {
						mTotalInsertedValues++;
					}
				}
				Log.e("Inserted Rows in category_master", "Rows "+ mTotalInsertedValues);
			} else {
				Log.e("Insert category_master", "List Null or Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Insert category_master", "Insert All Categories Error");
		}
		return mTotalInsertedValues;
		
	}
	
	public List<CategoryDetails> getAllCategories(String parentId) {
		List<CategoryDetails> mAllCategoriesList = new ArrayList<CategoryDetails>();
		try {
			String sql;
			if (parentId.length() == 0 || parentId.equalsIgnoreCase("")) {
				sql = "SELECT * FROM category_master WHERE parent_id = '0' ";
			} else {
				sql = "SELECT * FROM category_master WHERE parent_id='"+ parentId + "'";
			}
			Cursor cursor = getData(sql);
			if (cursor == null || cursor.getCount() == 0) {
				Log.e("Get category_master", "No Data");
			} else {
				Log.e("Get category_master", "Number of categories "+ cursor.getCount());
				cursor.moveToFirst();
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) {
					CategoryDetails categoryDetail = new CategoryDetails();
					categoryDetail.setmCategory_Id(cursor.getString(0));
					categoryDetail.setmCategory_Name(cursor.getString(1));
					categoryDetail.setmCategory_Image(cursor.getString(2));
					categoryDetail.setmCategory_ParentId(cursor.getString(3));
					categoryDetail.setmLastDownloadDateTime(cursor.getString(4));
					cursor.moveToNext();
					mAllCategoriesList.add(categoryDetail);
				}
			}
		} catch (Exception e) {
			Log.e("Get category_master", "ERROR");
			e.printStackTrace();
		}
		return mAllCategoriesList;
	}
	
	public CategoryDetails getCategorywithId(String id) {
		CategoryDetails ac = new CategoryDetails();
		try {
			String sql = "SELECT * FROM category_master WHERE category_id = '"+ id + "'";
			Cursor catcursor = getData(sql);
			int count = catcursor.getCount();
			if (catcursor != null && count > 0) {
				catcursor.moveToFirst();
				for (int i = 0; i < count; i++) {
					ac.setmCategory_Id(catcursor.getString(0));
					ac.setmCategory_Name(catcursor.getString(1));
					ac.setmCategory_Image(catcursor.getString(2));
					ac.setmCategory_ParentId(catcursor.getString(3));
					ac.setmLastDownloadDateTime(catcursor.getString(4));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ac;
	}
	
	public long insertInspectionList(List<Lookups> mCategoryDetails) {
		SQLiteDatabase db = this.getWritableDatabase();
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM inspectiontype_master");
			Log.e("Deleting inspectiontype_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting inspectiontype_master working", "Error");
		}
		try {
			
			if (mCategoryDetails != null && mCategoryDetails.size() > 0) {
				for (Lookups category : mCategoryDetails) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("inspection_name", "" +category.getmName());
					initialValues.put("inspection_id", "" +category.getmId());
					initialValues.put("last_downloadtime", dateTime);
					long rowId = insertData("inspectiontype_master", initialValues);
					if (rowId != 0) {
						mTotalInsertedValues++;
					}
				}
				Log.e("Inserted Rows in inspectiontype_master", "Rows "+ mTotalInsertedValues);
			} else {
				Log.e("Insert inspectiontype_master", "List Null or Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Insert inspectiontype_master", "Insert inspectiontype_master Error");
		}
		return mTotalInsertedValues;
		
	}
	
	public List<Lookups> getInspectionType() {
		List<Lookups> mAllInspectionList = new ArrayList<Lookups>();
		try {
			String	sql = "SELECT * FROM inspectiontype_master";
			Cursor cursor = getData(sql);
			if (cursor == null || cursor.getCount() == 0) {
				Log.e("Get inspectiontype_master", "No Data");
			} else {
				Log.e("Get inspectiontype_master", "Number of categories "+ cursor.getCount());
				cursor.moveToFirst();
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) {
					Lookups InspectionDetail = new Lookups();
					InspectionDetail.setmName(cursor.getString(0));
					InspectionDetail.setmId(cursor.getString(1));
					InspectionDetail.setmLastDownloadTime(cursor.getString(4));
					cursor.moveToNext();
					mAllInspectionList.add(InspectionDetail);
				}
			}
		} catch (Exception e) {
			Log.e("Get inspectiontype_master", "ERROR");
			e.printStackTrace();
		}
		
		return mAllInspectionList;
	}
	
	public void insertTypeOfIndustrys(List<Lookups> mTypeOfIndustryList) {
		SQLiteDatabase db = this.getWritableDatabase();
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM inspectionindustry_master");
			Log.e("Deleting inspectionindustry_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting inspectionindustry_master working", "Error");
		}
		try {
			if (mTypeOfIndustryList != null && mTypeOfIndustryList.size() > 0) {
				for (Lookups category : mTypeOfIndustryList) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("Id", "" +category.getmId());
					initialValues.put("name", "" +category.getmName());
					initialValues.put("last_downloadtime", dateTime);
					long rowId = insertData("inspectionindustry_master", initialValues);
					if (rowId != 0) {
						mTotalInsertedValues++;
					}
				}
				Log.e("Inserted Rows in inspectionindustry_master", "Rows "+ mTotalInsertedValues);
			} else {
				Log.e("Insert inspectionindustry_master", "List Null or Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Insert inspectionindustry_master", "Insert inspectionindustry_master Error");
		}
	}

	public List<Lookups> getTypeOfIndustry() {
		List<Lookups> mIndustryList = new ArrayList<Lookups>();
		try {
			String	sql = "SELECT * FROM inspectionindustry_master";
			Cursor cursor = getData(sql);
			if (cursor == null || cursor.getCount() == 0) {
				Log.e("Get inspectionindustry_master", "No Data");
			} else {
				Log.e("Get inspectionindustry_master", "Number of categories "+ cursor.getCount());
				cursor.moveToFirst();
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) {
					Lookups IndustryDetail = new Lookups();
					IndustryDetail.setmId(cursor.getString(0));
					IndustryDetail.setmName(cursor.getString(1));
					IndustryDetail.setmLastDownloadTime(cursor.getString(2));
					cursor.moveToNext();
					mIndustryList.add(IndustryDetail);
				}
			}
		} catch (Exception e) {
			Log.e("Get inspectionindustry_master", "ERROR");
			e.printStackTrace();
		}
		
		return mIndustryList;
	}
	
	
	public void insertTypeOfInspector(List<Lookups> mInspectorList) {
		SQLiteDatabase db = this.getWritableDatabase();
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM inspectioninspector_master");
			Log.e("Deleting inspectioninspector_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting inspectioninspector_master working", "Error");
		}
		try {
			if (mInspectorList != null && mInspectorList.size() > 0) {
				for (Lookups category : mInspectorList) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("Id", "" +category.getmId());
					initialValues.put("name", "" +category.getmName());
					initialValues.put("last_downloadtime", dateTime);
					long rowId = insertData("inspectioninspector_master", initialValues);
					if (rowId != 0) {
						mTotalInsertedValues++;
					}
				}
				Log.e("Inserted Rows in inspectioninspector_master", "Rows "+ mTotalInsertedValues);
			} else {
				Log.e("Insert inspectioninspector_master", "List Null or Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Insert inspectioninspector_master", "Insert inspectioninspector_master Error");
		}
	}
	
	public List<Lookups> getTypeOfInspector() {
		List<Lookups> mInspectorList = new ArrayList<Lookups>();
		try {
			String	sql = "SELECT * FROM inspectioninspector_master";
			Cursor cursor = getData(sql);
			if (cursor == null || cursor.getCount() == 0) {
				Log.e("Get inspectioninspector_master", "No Data");
			} else {
				Log.e("Get inspectioninspector_master", "Number of categories "+ cursor.getCount());
				cursor.moveToFirst();
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) {
					Lookups IndustryDetail = new Lookups();
					IndustryDetail.setmId(cursor.getString(0));
					IndustryDetail.setmName(cursor.getString(1));
					IndustryDetail.setmLastDownloadTime(cursor.getString(2));
					cursor.moveToNext();
					mInspectorList.add(IndustryDetail);
				}
			}
		} catch (Exception e) {
			Log.e("Get inspectioninspector_master", "ERROR");
			e.printStackTrace();
		}
		
		return mInspectorList;
	}
	
	/*public long insertNewArrivalProducts(List<ProductList> mNewArrivalList) {
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM newarrival_master");
			Log.e("Deleting newarrival_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting newarrival_master working", "Error");
		}
		try {
			if (mNewArrivalList != null && mNewArrivalList.size() > 0) {
				for (ProductList productList : mNewArrivalList) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("product_id", "" + productList.getmProduct_Id());
					initialValues.put("product_name", "" + productList.getmProduct_Name());
					initialValues.put("product_image", "" + productList.getmProduct_Image());
					initialValues.put("manufacturer_name", "" + productList.getmManufacturer_Name());
					initialValues.put("manufacturer_id", productList.getmManufacturer_Id());
					initialValues.put("category_name", productList.getmCategory_Name());
					initialValues.put("category_id", productList.getmCategory_Id());
					initialValues.put("product_offerprice", productList.getmProduct_OfferPrice());
					
					long rowId = insertData("newarrival_master", initialValues);
					if (rowId != 0) {
						mTotalInsertedValues++;
					}
				}
				Log.e("Inserted Rows in newarrival_master", "Rows "+ mTotalInsertedValues);
			} else {
				Log.e("Insert newarrival_master", "List Null or Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
	}
	
	public List<ProductList> getAllNewArrivals() {
		List<ProductList> mNewArrivalsList = new ArrayList<ProductList>();
		try {
			String sql = "SELECT * FROM newarrival_master";
			Cursor cursor = getData(sql);
			if (cursor == null || cursor.getCount() == 0) {
				Log.e("Get newarrival_master", "No Data");
			} else {
				Log.e("Get newarrival_master", "newarrival_master"+ cursor.getCount());
				cursor.moveToFirst();
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) {
					ProductList product = new ProductList();
					product.setmProduct_Id(cursor.getString(0));
					product.setmProduct_Name(cursor.getString(1));
					product.setmProduct_Image(cursor.getString(2));
					product.setmManufacturer_Name(cursor.getString(3));
					product.setmManufacturer_Id(cursor.getString(4));
					product.setmCategory_Name(cursor.getString(5));
					product.setmCategory_Id(cursor.getString(6));
					product.setmProduct_OfferPrice(cursor.getString(7));
					cursor.moveToNext();
					mNewArrivalsList.add(product);
				}
			}
		} catch (Exception e) {
			Log.e("Get newarrival_master", "ERROR");
			e.printStackTrace();
		}
		return mNewArrivalsList;
	}*/

	public long insertFeaturedProducts(List<ProductList> mFeaturedProductList) {
		SQLiteDatabase db = this.getWritableDatabase();
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM featuredproduct_master where mservicegrouptype='"+VerisupplierUtils.mServiceGroupType+"'");
			Log.e("Deleting featuredproduct_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting featuredproduct_master working", "Error");
		}
		try {
			if (mFeaturedProductList != null && mFeaturedProductList.size() > 0) {
				for (ProductList productList : mFeaturedProductList) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("product_id", "" + productList.getmProduct_Id());
					initialValues.put("product_name", "" + productList.getmProduct_Name());
					initialValues.put("product_image", "" + productList.getmProduct_Image());
					initialValues.put("manufacturer_name", "" + productList.getmManufacturer_Name());
					initialValues.put("manufacturer_id", productList.getmManufacturer_Id());
					initialValues.put("category_name", productList.getmCategory_Name());
					initialValues.put("category_id", productList.getmCategory_Id());
					initialValues.put("product_offerprice", productList.getmProduct_OfferPrice());
					initialValues.put("product_price", productList.getmProductPrice());
					initialValues.put("product_unitofmeasurement", productList.getmUnitOfMeasurement());
					initialValues.put("mservicegrouptype", VerisupplierUtils.mServiceGroupType);
					initialValues.put("lastdownload_datetime", dateTime);
					
					
					
					long rowId = insertData("featuredproduct_master", initialValues);
					if (rowId != 0) {
						mTotalInsertedValues++;
					}
				}
				Log.e("Inserted Rows in featuredproduct_master", "Rows "+ mTotalInsertedValues);
			} else {
				Log.e("Insert featuredproduct_master", "List Null or Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
	}
	
	public List<ProductList> getFeaturedProducts() {
		List<ProductList> mFeaturedProductsList = new ArrayList<ProductList>();
		try {
			String sql = "SELECT * FROM featuredproduct_master where mservicegrouptype='"+VerisupplierUtils.mServiceGroupType+"'";
			Cursor cursor = getData(sql);
			if (cursor == null || cursor.getCount() == 0) {
				Log.e("Get featuredproduct_master", "No Data");
			} else {
				Log.e("Get featuredproduct_master", "featuredproduct_master"+ cursor.getCount());
				cursor.moveToFirst();
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) {
					ProductList product = new ProductList();
					product.setmProduct_Id(cursor.getString(0));
					product.setmProduct_Name(cursor.getString(1));
					product.setmProduct_Image(cursor.getString(2));
					product.setmManufacturer_Name(cursor.getString(3));
					product.setmManufacturer_Id(cursor.getString(4));
					product.setmCategory_Name(cursor.getString(5));
					product.setmCategory_Id(cursor.getString(6));
					product.setmProduct_OfferPrice(cursor.getString(7));
					product.setmProductPrice(cursor.getString(8));
					product.setmUnitOfMeasurement(cursor.getString(9));
					product.setmLastDownloadTime(cursor.getString(11));
					cursor.moveToNext();
					mFeaturedProductsList.add(product);
				}
			}
		} catch (Exception e) {
			Log.e("Get featuredproduct_master", "ERROR");
			e.printStackTrace();
		}
		return mFeaturedProductsList;
	}

	/*public long insertQualityPicks(List<ProductList> mQualityPicksList) {
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM qualitypicksproduct_master");
			Log.e("Deleting qualitypicksproduct_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting qualitypicksproduct_master working", "Error");
		}
		try {
			if (mQualityPicksList != null && mQualityPicksList.size() > 0) {
				for (ProductList productList : mQualityPicksList) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("product_id", "" + productList.getmProduct_Id());
					initialValues.put("product_name", "" + productList.getmProduct_Name());
					initialValues.put("product_image", "" + productList.getmProduct_Image());
					initialValues.put("manufacturer_name", "" + productList.getmManufacturer_Name());
					initialValues.put("manufacturer_id", productList.getmManufacturer_Id());
					initialValues.put("category_name", productList.getmCategory_Name());
					initialValues.put("category_id", productList.getmCategory_Id());
					initialValues.put("product_offerprice", productList.getmProduct_OfferPrice());
					
					long rowId = insertData("qualitypicksproduct_master", initialValues);
					if (rowId != 0) {
						mTotalInsertedValues++;
					}
				}
				Log.e("Inserted Rows in qualitypicksproduct_master", "Rows "+ mTotalInsertedValues);
			} else {
				Log.e("Insert qualitypicksproduct_master", "List Null or Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
	}
	
	public List<ProductList> getQualityPicksList() {
		List<ProductList> mFeaturedProductsList = new ArrayList<ProductList>();
		try {
			String sql = "SELECT * FROM qualitypicksproduct_master";
			Cursor cursor = getData(sql);
			if (cursor == null || cursor.getCount() == 0) {
				Log.e("Get qualitypicksproduct_master", "No Data");
			} else {
				Log.e("Get qualitypicksproduct_master", "qualitypicksproduct_master "+ cursor.getCount());
				cursor.moveToFirst();
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) {
					ProductList product = new ProductList();
					product.setmProduct_Id(cursor.getString(0));
					product.setmProduct_Name(cursor.getString(1));
					product.setmProduct_Image(cursor.getString(2));
					product.setmManufacturer_Name(cursor.getString(3));
					product.setmManufacturer_Id(cursor.getString(4));
					product.setmCategory_Name(cursor.getString(5));
					product.setmCategory_Id(cursor.getString(6));
					product.setmProduct_OfferPrice(cursor.getString(7));
					cursor.moveToNext();
					mFeaturedProductsList.add(product);
				}
			}
		} catch (Exception e) {
			Log.e("Get qualitypicksproduct_master", "ERROR");
			e.printStackTrace();
		}
		return mFeaturedProductsList;
	}*/
	
	/*public List<ProductList> getQualityPicksList(String category_Id) {
		List<ProductList> mFeaturedProductsList = new ArrayList<ProductList>();
		try {
			String sql;
			if(category_Id==null || category_Id.length()==0){
				sql = "SELECT * FROM qualitypicksproduct_master";
			}else{
				sql = "SELECT * FROM qualitypicksproduct_master where category_id='"+category_Id+"'";
			}
			Cursor cursor = getData(sql);
			if (cursor == null || cursor.getCount() == 0) {
				Log.e("Get qualitypicksproduct_master", "No Data");
			} else {
				Log.e("Get qualitypicksproduct_master", "qualitypicksproduct_master "+ cursor.getCount());
				cursor.moveToFirst();
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) {
					ProductList product = new ProductList();
					product.setmProduct_Id(cursor.getString(0));
					product.setmProduct_Name(cursor.getString(1));
					product.setmProduct_Image(cursor.getString(2));
					product.setmManufacturer_Name(cursor.getString(3));
					product.setmManufacturer_Id(cursor.getString(4));
					product.setmCategory_Name(cursor.getString(5));
					product.setmCategory_Id(cursor.getString(6));
					product.setmProduct_OfferPrice(cursor.getString(7));
					cursor.moveToNext();
					mFeaturedProductsList.add(product);
				}
			}
		} catch (Exception e) {
			Log.e("Get qualitypicksproduct_master", "ERROR");
			e.printStackTrace();
		}
		return mFeaturedProductsList;
	}*/

	/*public long insertVerifiedManufacturer(List<ProductList> mVerifiedManufacturerList) {
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM varifiedmanufacturer_master");
			Log.e("Deleting varifiedmanufacturer_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting varifiedmanufacturer_master working", "Error");
		}
		try {
			if (mVerifiedManufacturerList != null && mVerifiedManufacturerList.size() > 0) {
				for (ProductList productList : mVerifiedManufacturerList) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("manufacturer_id", "" + productList.getmManufacturer_Id());
					initialValues.put("manufacturer_name", "" + productList.getmManufacturer_Name());
					initialValues.put("manufacturer_image", "" + productList.getmManufacturer_Image());
					initialValues.put("manufacturer_likes", "" + productList.getmManufacturer_Likes());
					initialValues.put("manufacturer_feedback", productList.getmManufacturer_Feedback());
					
					long rowId = insertData("varifiedmanufacturer_master", initialValues);
					if (rowId != 0) {
						mTotalInsertedValues++;
					}
				}
				Log.e("Inserted varifiedmanufacturer_master", "Rows "+ mTotalInsertedValues);
			} else {
				Log.e("Insert varifiedmanufacturer_master", "List Null or Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
	}
	
	public List<ProductList> getVerifiedManufacturer() {
		List<ProductList> mFeaturedProductsList = new ArrayList<ProductList>();
		try {
			String sql = "SELECT * FROM varifiedmanufacturer_master";
			Cursor cursor = getData(sql);
			if (cursor == null || cursor.getCount() == 0) {
				Log.e("Get varifiedmanufacturer_master", "No Data");
			} else {
				Log.e("Get varifiedmanufacturer_master", "varifiedmanufacturer_master"+ cursor.getCount());
				cursor.moveToFirst();
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) {
					ProductList product = new ProductList();
					product.setmManufacturer_Id(cursor.getString(0));
					product.setmManufacturer_Name(cursor.getString(1));
					product.setmManufacturer_Image(cursor.getString(2));
					product.setmManufacturer_Likes(cursor.getString(3));
					product.setmManufacturer_Feedback(cursor.getString(4));
					
					cursor.moveToNext();
					mFeaturedProductsList.add(product);
				}
			}
		} catch (Exception e) {
			Log.e("Get varifiedmanufacturer_master", "ERROR");
			e.printStackTrace();
		}
		return mFeaturedProductsList;
	}*/

	public long insertProductDetails(List<ProductDetails> mProductDetails) {
		int mTotalInsertedValues = 0;
		List<ProductDetails> mgetProductList = new ArrayList<ProductDetails>();
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			String sql = "SELECT * FROM productdetail_master";
			Cursor cursor = getData(sql);
			Log.e("Get productdetail_master", "productdetail_master"+ cursor.getCount());
			if(cursor.getCount()>=6){
				Log.e("inside if", "write delete query");
				sql="Select * FROM productdetail_master where rowid= (SELECT MIN(rowid) FROM productdetail_master)";
				Cursor cursor1 = getData(sql);
				if (cursor1.getCount() > 0) {
					cursor1.moveToFirst();
					for (int size = 0; size < cursor1.getCount(); size++) {
						ProductDetails product=new ProductDetails();
						product.setmProduct_Id(cursor1.getString(0));
						mgetProductList.add(product);
						cursor1.moveToNext();
					}
					cursor1.close();
				}
				db.execSQL("DELETE FROM productspecification_master where product_id='"+mgetProductList.get(0).getmProduct_Id()+"'");
				db.execSQL("DELETE FROM product_userfeedback where product_id='"+mgetProductList.get(0).getmProduct_Id()+"'");
				db.execSQL("DELETE FROM productdetail_master where rowid= (SELECT MIN(rowid) FROM productdetail_master)");
				db.execSQL("DELETE FROM product_imagemaster where product_id='"+mgetProductList.get(0).getmProduct_Id()+"'");
			}
			if (mProductDetails != null && mProductDetails.size() > 0) {
				for (ProductDetails productDetails : mProductDetails) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("product_id", "" + productDetails.getmProduct_Id());
					initialValues.put("product_name", "" + productDetails.getmProduct_Name());
					initialValues.put("product_image", "" + productDetails.getmProduct_Image());
					initialValues.put("product_description", "" + productDetails.getmProduct_description());
					initialValues.put("product_averagerating", productDetails.getmProduct_averageRating());
					initialValues.put("product_price", productDetails.getmProduct_price());
					initialValues.put("manufacturer_name", productDetails.getmManufacturer_name());
					initialValues.put("manufacturer_id", productDetails.getmManufacturer_id());
					initialValues.put("manufacturer_image", productDetails.getmManufacturer_image());
					initialValues.put("manufacturer_likes", productDetails.getmManufacturer_likes());
					initialValues.put("manufacturer_feedback", productDetails.getmManufacturer_feedback());
					initialValues.put("product_quantity", productDetails.getmProduct_quantity());
					initialValues.put("feedback_ratingstars", productDetails.getmFeedback_stars());
					initialValues.put("shipping_cost", productDetails.getmShipping_cost());
					initialValues.put("shipping_date", productDetails.getmShipping_date());
					initialValues.put("shipping_type", productDetails.getmShipping_type());
					initialValues.put("product_imagelist", productDetails.getmProduct_Image());
					initialValues.put("totalfeedback_count", productDetails.getmTotalFeedBackCount());
					initialValues.put("product_unitofmeasurement", productDetails.getmProduct_UnitOfMeasurement());
					initialValues.put("product_moq", productDetails.getmProduct_moq());
					long rowId = insertData("productdetail_master", initialValues);
					if (rowId != 0) {
						mTotalInsertedValues++;
					}
				}
				Log.e("Inserted productdetail_master", "Rows "+ mTotalInsertedValues);
			} else {
				Log.e("Insert productdetail_master", "List Null or Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mTotalInsertedValues;
		
	}
	
	public List<ProductDetails> getAllProductDetails(String product_id) {
		Cursor cursor;
		List<ProductDetails> mProductList = new ArrayList<ProductDetails>();
		cursor = getData("SELECT * FROM productdetail_master where product_id='"+ product_id + "'");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ProductDetails mProductDetails = new ProductDetails();

				mProductDetails.setmProduct_Id(cursor.getString(0));
				mProductDetails.setmProduct_Name(cursor.getString(1));
				mProductDetails.setmProduct_Image(cursor.getString(2));
				mProductDetails.setmProduct_description(cursor.getString(3));
				mProductDetails.setmProduct_averageRating(cursor.getString(4));
				mProductDetails.setmProduct_price(cursor.getString(5));
				mProductDetails.setmProduct_moq(cursor.getString(6));
				mProductDetails.setmManufacturer_name(cursor.getString(7));
				mProductDetails.setmManufacturer_id(cursor.getString(8));
				mProductDetails.setmManufacturer_image(cursor.getString(9));
				mProductDetails.setmManufacturer_likes(cursor.getString(10));
				mProductDetails.setmManufacturer_feedback(cursor.getString(11));
				mProductDetails.setmProduct_quantity(cursor.getString(12));
				mProductDetails.setmFeedback_stars(cursor.getString(13));
				mProductDetails.setmShipping_cost(cursor.getString(14));
				mProductDetails.setmShipping_date(cursor.getString(15));
				mProductDetails.setmShipping_type(cursor.getString(16));
				mProductDetails.setmTotalFeedBackCount(cursor.getString(18));
				mProductDetails.setmProduct_UnitOfMeasurement(cursor.getString(19));

				mProductList.add(mProductDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}
		return mProductList;
	}
	
	public long insertProductImageDetails(List<ProductDetails> mProductDetail,String mProductId) {
		List<ProductImageDetails> mProductImagesList = new ArrayList<ProductImageDetails>();
		int mTotalInsertedValues = 0;
		try {
			for (int i = 0; i < mProductDetail.size(); i++) {
				ContentValues initialValues = new ContentValues();
				mProductImagesList = mProductDetail.get(i).getmProductsImagesList();
				if (mProductImagesList != null && mProductImagesList.size() > 0) {
					for (ProductImageDetails details : mProductImagesList) {
						initialValues.put("product_id", mProductId);
						initialValues.put("productimage_url", details.getmProductImage_url());
						
						long row = 0;
						row = insertData("product_imagemaster", initialValues);
						Log.e("insertion product_imagemaster Table","insertion product_imagemaster working");
						if (row != 0) {
							mTotalInsertedValues++;
						}
					}
				}
			}
			Log.e("Inserted product_imagemaster", "Rows "+ mTotalInsertedValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mTotalInsertedValues;
	}
	
	public List<ProductImageDetails> getProductImageDetails(String product_id) {
		Cursor cursor;
		List<ProductImageDetails> mProductImageList = new ArrayList<ProductImageDetails>();
		cursor = getData("SELECT * FROM product_imagemaster where product_id='"+product_id+"'");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ProductImageDetails mProductDetails = new ProductImageDetails();

				mProductDetails.setmProductImage_url(cursor.getString(1));
				mProductImageList.add(mProductDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}
		
		return mProductImageList;
	}

	public long insertSpecificationDetails(List<ProductDetails> mProductDetails, String mProductId) {
		List<ProductSpecificationDetails> mSpecificationDetails = new ArrayList<ProductSpecificationDetails>();
		int mTotalInsertedValues = 0;
		try {
			for (int i = 0; i < mProductDetails.size(); i++) {
				ContentValues initialValues = new ContentValues();
				mSpecificationDetails = mProductDetails.get(i).getmSpecificationList();
				if (mSpecificationDetails != null && mSpecificationDetails.size() > 0) {
					for (ProductSpecificationDetails details : mSpecificationDetails) {
						initialValues.put("product_id", mProductId);
						initialValues.put("attribute_name", details.getmProductAttribute_Name());
						initialValues.put("attribute_value",details.getmProductAttribute_Value());
						long row = 0;
						row = insertData("productspecification_master", initialValues);
						Log.e("insertion productspecification_master Table","insertion productspecification_master working");
						if (row != 0) {
							mTotalInsertedValues++;
						}
					}
				}
			}
			Log.e("Inserted productspecification_master", "Rows "+ mTotalInsertedValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mTotalInsertedValues;
	}
	
	public List<ProductSpecificationDetails> getSellingDetails(String product_id) {
		Cursor cursor;
		List<ProductSpecificationDetails> mSpecificationList = new ArrayList<ProductSpecificationDetails>();
		cursor = getData("SELECT * FROM productspecification_master where product_id='"+ product_id + "'");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ProductSpecificationDetails mSellingDetails = new ProductSpecificationDetails();
				mSellingDetails.setmProduct_Id(cursor.getString(0));
				mSellingDetails.setmProductAttribute_Name(cursor.getString(1));
				mSellingDetails.setmProductAttribute_Value(cursor.getString(2));

				mSpecificationList.add(mSellingDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mSpecificationList;
	}

	public long insertUserFeedback(List<ProductDetails> mProductDetails,String mProductId) {
		List<FeedbackDetails> mFeedbackDetails = new ArrayList<FeedbackDetails>();
		int mTotalInsertedValues = 0;
		try {
			for (int i = 0; i < mProductDetails.size(); i++) {
				ContentValues initialValues = new ContentValues();
				mFeedbackDetails = mProductDetails.get(i).getmUserFeedbackList();
				if (mFeedbackDetails != null && mFeedbackDetails.size() > 0) {
					for (FeedbackDetails details : mFeedbackDetails) {
						initialValues.put("product_id", mProductId);
						initialValues.put("feedback_date", details.getmCreatedDate());
						initialValues.put("feedback_text",details.getmFeedBackText());
						initialValues.put("feedback_location",details.getmFeedbackLocation());
						initialValues.put("feedback_username",details.getmFeedbackUserName());
						initialValues.put("feedback_userrating",details.getmFeedbackRating());
						long row = 0;
						row = insertData("product_userfeedback", initialValues);
						Log.e("insertion product_userfeedback Table","insertion product_userfeedback working");
						if (row != 0) {
							mTotalInsertedValues++;
						}
					}
				}
			}
			Log.e("Inserted product_userfeedback", "Rows "+ mTotalInsertedValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
	}
	
	public List<FeedbackDetails> getFeedbackDetails(String product_id) {
		Cursor cursor;
		List<FeedbackDetails> mFeedbackList = new ArrayList<FeedbackDetails>();
		cursor = getData("SELECT * FROM product_userfeedback where product_id='"+ product_id + "'");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				FeedbackDetails mFeedbackDetails = new FeedbackDetails();
				mFeedbackDetails.setmCreatedDate(cursor.getString(1));
				mFeedbackDetails.setmFeedBackText(cursor.getString(2));
				mFeedbackDetails.setmFeedbackLocation(cursor.getString(3));
				mFeedbackDetails.setmFeedbackUserName(cursor.getString(4));
				mFeedbackDetails.setmFeedbackRating(cursor.getString(5));
				mFeedbackList.add(mFeedbackDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mFeedbackList;
	}

	public void UpdateProductDetails(List<ProductDetails> mProductDetails,String mProductId) {
		long mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		if (mProductDetails == null || mProductDetails.size() == 0) {
		} else {
			for (ProductDetails productDetails : mProductDetails) {
				openDataBase();
				db.execSQL("delete from productdetail_master where product_id='"+mProductId+"'");
				ContentValues initialValues = new ContentValues();
				initialValues.put("product_id", "" + productDetails.getmProduct_Id());
				initialValues.put("product_name", "" + productDetails.getmProduct_Name());
				initialValues.put("product_image", "" + productDetails.getmProduct_Image());
				initialValues.put("product_description", "" + productDetails.getmProduct_description());
				initialValues.put("product_averagerating", productDetails.getmProduct_averageRating());
				initialValues.put("product_price", productDetails.getmProduct_price());
				initialValues.put("manufacturer_name", productDetails.getmManufacturer_name());
				initialValues.put("manufacturer_id", productDetails.getmManufacturer_id());
				initialValues.put("manufacturer_image", productDetails.getmManufacturer_image());
				initialValues.put("manufacturer_likes", productDetails.getmManufacturer_likes());
				initialValues.put("manufacturer_feedback", productDetails.getmManufacturer_feedback());
				initialValues.put("product_quantity", productDetails.getmProduct_quantity());
				initialValues.put("feedback_ratingstars", productDetails.getmFeedback_stars());
				initialValues.put("shipping_cost", productDetails.getmShipping_cost());
				initialValues.put("shipping_date", productDetails.getmShipping_date());
				initialValues.put("shipping_type", productDetails.getmShipping_type());
				initialValues.put("product_imagelist", productDetails.getmProduct_Image());
				initialValues.put("totalfeedback_count", productDetails.getmTotalFeedBackCount());
				initialValues.put("product_unitofmeasurement", productDetails.getmProduct_UnitOfMeasurement());
				initialValues.put("product_moq", productDetails.getmProduct_moq());
				long rowId = insertData("productdetail_master", initialValues);
				if (rowId != 0) {
					mTotalInsertedValues++;
				}
				Log.e("Inserted productdetail_master", "Rows "+ mTotalInsertedValues);
			}
		}
	}
	
	public void UpdateSpecificationDetails(List<ProductDetails> mProductDetails,String mProductid) {
		List<ProductSpecificationDetails> mSpecificationDetails = new ArrayList<ProductSpecificationDetails>();
		long mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		if (mProductDetails == null || mProductDetails.size() == 0) {
		} else {
			for (int i = 0; i < mProductDetails.size(); i++) {
				openDataBase();
				db.execSQL("delete from productspecification_master where product_id='"+mProductid+"'");
				ContentValues initialValues = new ContentValues();
				mSpecificationDetails = mProductDetails.get(i).getmSpecificationList();
				if (mSpecificationDetails != null && mSpecificationDetails.size() > 0) {
					for (ProductSpecificationDetails details : mSpecificationDetails) {
						initialValues.put("product_id", mProductid);
						initialValues.put("attribute_name", details.getmProductAttribute_Name());
						initialValues.put("attribute_value",details.getmProductAttribute_Value());
						long row = 0;
						row = insertData("productspecification_master", initialValues);
						Log.e("insertion productspecification_master Table","insertion productspecification_master working");
						if (row != 0) {
							mTotalInsertedValues++;
						}
					}
				}
			}
			Log.e("Inserted productspecification_master", "Rows "+ mTotalInsertedValues);
		}
	}
	
	public void UpdateProductImageDetails(List<ProductDetails> mProductDetail,String mProductId) {
		List<ProductImageDetails> mProductImagesList = new ArrayList<ProductImageDetails>();
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			for (int i = 0; i < mProductDetail.size(); i++) {
				openDataBase();
				db.execSQL("delete from product_imagemaster where product_id='"+mProductId+"'");
				ContentValues initialValues = new ContentValues();
				mProductImagesList = mProductDetail.get(i).getmProductsImagesList();
				if (mProductImagesList != null && mProductImagesList.size() > 0) {
					for (ProductImageDetails details : mProductImagesList) {
						initialValues.put("product_id", mProductId);
						initialValues.put("productimage_url", details.getmProductImage_url());
						long row = 0;
						row = insertData("product_imagemaster", initialValues);
						Log.e("insertion product_imagemaster Table","insertion product_imagemaster working");
						if (row != 0) {
							mTotalInsertedValues++;
						}
					}
				}
			}
			Log.e("Inserted product_imagemaster", "Rows "+ mTotalInsertedValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void UpdateUserFeedback(List<ProductDetails> mProductDetails,String mProductId) {
		List<FeedbackDetails> mFeedbackDetails = new ArrayList<FeedbackDetails>();
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			for (int i = 0; i < mProductDetails.size(); i++) {
				openDataBase();
				db.execSQL("delete from product_userfeedback where product_id='"+mProductId+"'");
				ContentValues initialValues = new ContentValues();
				mFeedbackDetails = mProductDetails.get(i).getmUserFeedbackList();
				if (mFeedbackDetails != null && mFeedbackDetails.size() > 0) {
					for (FeedbackDetails details : mFeedbackDetails) {
						initialValues.put("product_id", mProductId);
						initialValues.put("feedback_date", details.getmCreatedDate());
						initialValues.put("feedback_text",details.getmFeedBackText());
						initialValues.put("feedback_location",details.getmFeedbackLocation());
						initialValues.put("feedback_username",details.getmFeedbackUserName());
						initialValues.put("feedback_userrating",details.getmFeedbackRating());
						long row = 0;
						row = insertData("product_userfeedback", initialValues);
						Log.e("insertion product_userfeedback Table","insertion product_userfeedback working");
						if (row != 0) {
							mTotalInsertedValues++;
						}
					}
				}
			}
			Log.e("Inserted product_userfeedback", "Rows "+ mTotalInsertedValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteAllTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			openDataBase();
			db.execSQL("DELETE FROM category_master");
			db.execSQL("DELETE FROM featuredproduct_master");
			db.execSQL("DELETE FROM productdetail_master");
			db.execSQL("DELETE FROM productspecification_master");
			db.execSQL("DELETE FROM product_userfeedback");
			db.execSQL("DELETE FROM country_master");
			db.execSQL("DELETE FROM state_master");
			db.execSQL("DELETE FROM unitofmeasurement_master");
			db.execSQL("DELETE FROM quotationlookup_master");
			db.execSQL("DELETE FROM inspectionindustry_master");
			db.execSQL("DELETE FROM inspectioninspector_master");
			db.execSQL("DELETE FROM inspectiontype_master");
			close();
			
			Log.e("Deleting category_master working", "Done");
			Log.e("Deleting featuredproduct_master working", "Done");
			Log.e("Deleting productdetail_master working", "Done");
			Log.e("Deleting productspecification_master working", "Done");
			Log.e("Deleting product_userfeedback working", "Done");
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting tables notworking", "Error");
		}
	}
	
	public void deleteUserTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		try{
			openDataBase();
			db.execSQL("DELETE FROM user_master");
			db.execSQL("DELETE FROM dashboard_order_master");
			db.execSQL("DELETE FROM dashboard_quotation_master");
			close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public long insertwhistlistdetails(List<WishlistDetails> wDetails) {
		int pTotalInsertedValues = 0;
		try {
			for (int i = 0; i < wDetails.size(); i++) {
				ContentValues initialValues = new ContentValues();
				initialValues.put("product_id", wDetails.get(i).getProduct_id());
				initialValues.put("product_name", wDetails.get(i).getProduct_nam());
				initialValues.put("product_imgurl", wDetails.get(i).getProduct_img());
				initialValues.put("Product_manufname", wDetails.get(i).getProduct_manufname());
				initialValues.put("product_price", wDetails.get(i).getmProductPrice());
				initialValues.put("product_unit", wDetails.get(i).getmProductUnit());
				initialValues.put("user_id", wDetails.get(i).getmCustomerId());
				initialValues.put("manufacturer_id", wDetails.get(i).getmManufactureId());
				initialValues.put("service_groupId", wDetails.get(i).getmServiceGroupType());
				
				long row = 0;
				row = insertData("whistlist_productmaster", initialValues);
				Log.e("insertion-Sub: whistlist_productmaster Table",
						"insertion whistlist_productmaster working");
				if (row != 0) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pTotalInsertedValues;
	}

	public List<WishlistDetails> getwishlistdetails() {
		Cursor cursor;
		List<WishlistDetails> mwList = new ArrayList<WishlistDetails>();
		cursor = getData("SELECT DISTINCT * FROM whistlist_productmaster ");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				WishlistDetails mwishlistDetails = new WishlistDetails();
				mwishlistDetails.setProduct_id(cursor.getString(0));
				mwishlistDetails.setProduct_nam(cursor.getString(1));
				mwishlistDetails.setProduct_img(cursor.getString(2));
				mwishlistDetails.setProduct_manufname(cursor.getString(3));
				mwishlistDetails.setmProductPrice(cursor.getString(4));
				mwishlistDetails.setmProductUnit(cursor.getString(5));
				mwishlistDetails.setmCustomerId(cursor.getString(6));
				mwishlistDetails.setmManufactureId(cursor.getString(7));
				mwishlistDetails.setmServiceGroupType(cursor.getString(8));
				mwList.add(mwishlistDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}
		return mwList;
	}
	
	public List<WishlistDetails> getwishlistdetails(String mProductId) {
		Cursor cursor;
		List<WishlistDetails> mwList = new ArrayList<WishlistDetails>();
		cursor = getData("SELECT DISTINCT * FROM whistlist_productmaster where product_id='"+mProductId+"'");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				WishlistDetails mwishlistDetails = new WishlistDetails();
				mwishlistDetails.setProduct_id(cursor.getString(0));
				mwishlistDetails.setProduct_nam(cursor.getString(1));
				mwishlistDetails.setProduct_img(cursor.getString(2));
				mwishlistDetails.setProduct_manufname(cursor.getString(3));
				mwishlistDetails.setmCustomerId(cursor.getString(6));
				mwishlistDetails.setmManufactureId(cursor.getString(7));
				mwishlistDetails.setmServiceGroupType(cursor.getString(8));
				mwList.add(mwishlistDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}
		return mwList;
	}
	
	public long insertManufacturerDetails(List<ManufacturerDetails> mDetails,String manufacturerId) {
		SQLiteDatabase db = this.getWritableDatabase(); 
		int mTotalInsertedValues =0;
		try{
			openDataBase();
			Cursor cur = getData("select  DISTINCT manufacturer_id from manufacturer_master");
			if(cur.getCount() > 9){
				cur.moveToFirst();
				String mfr_id = cur.getString(cur.getColumnIndex("manufacturer_id"));
				System.out.println("MFR ID"+mfr_id);
				db.execSQL("DELETE * FROM manufacturer_master WHERE manufacturer_id='"+mfr_id+"'");
			}
			Log.e("Deleting manufacturer_master working", "Done");
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Deleting manufacturer_master working", "Error");
		}
		try {
			for(int i=0; i<mDetails.size();i++){
				ContentValues initialValues = new ContentValues();
				initialValues.put("manufacturer_id", manufacturerId);
				initialValues.put("manufacturer_name", mDetails.get(i).getmManufacturerName());
				initialValues.put("manufacturer_businesstype", mDetails.get(i).getmBusinessType());
				initialValues.put("manufacturer_yearestablished", mDetails.get(i).getmYearEstablished());
				initialValues.put("manufacturer_employee", mDetails.get(i).getmEmployeeCount());
				initialValues.put("manufacturer_address", mDetails.get(i).getmAddressDetails());
				initialValues.put("manufacturer_trademarket", mDetails.get(i).getmTradeDetail());
				initialValues.put("manufacturer_email", mDetails.get(i).getmEmailDetails());
				initialValues.put("manufacturer_phone", mDetails.get(i).getmPhoneDetails());
				initialValues.put("manufacturer_website", mDetails.get(i).getmWebsiteDetails());
				initialValues.put("manufacturer_fax", mDetails.get(i).getmFaxDetails());
				initialValues.put("manufacturer_logo", mDetails.get(i).getmManufacturerLogo());
				initialValues.put("manufacturer_likes", mDetails.get(i).getmLikes());
				initialValues.put("manufacturer_positivefeedback", mDetails.get(i).getmFeedback());
				initialValues.put("manufacturer_description", mDetails.get(i).getmShortDescription());
				

				long rowId = insertData("manufacturer_master", initialValues);
				if(rowId != 0){
					mTotalInsertedValues++;
				}
			}
			
			Log.e("Inserting manufacturer_master", "Done"+mTotalInsertedValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
	}
	
	public List<ManufacturerDetails> getManufacturerDetails(String manufacturerId) {
		Cursor cursor;
		List<ManufacturerDetails> mManufacturerList = new ArrayList<ManufacturerDetails>();
		cursor = getData("SELECT * FROM manufacturer_master where manufacturer_id='"+manufacturerId+"'");
		if (cursor.getCount() > 0) {
			
			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ManufacturerDetails mmanufacturerDetails = new ManufacturerDetails();
				mmanufacturerDetails.setmManufactureID(cursor.getString(0));
				mmanufacturerDetails.setmManufacturerName(cursor.getString(1));
				mmanufacturerDetails.setmBusinessType(cursor.getString(2));
				mmanufacturerDetails.setmYearEstablished(cursor.getString(3));
				mmanufacturerDetails.setmEmployeeCount(cursor.getString(4));
				mmanufacturerDetails.setmAddressDetails(cursor.getString(5));
				mmanufacturerDetails.setmTradeDetail(cursor.getString(6));
				mmanufacturerDetails.setmEmailDetails(cursor.getString(7));
				mmanufacturerDetails.setmPhoneDetails(cursor.getString(8));
				mmanufacturerDetails.setmWebsiteDetails(cursor.getString(9));
				mmanufacturerDetails.setmFaxDetails(cursor.getString(10));
				mmanufacturerDetails.setmManufacturerLogo(cursor.getString(11));
				mmanufacturerDetails.setmLikes(cursor.getString(12));
				mmanufacturerDetails.setmFeedback(cursor.getString(13));
				mmanufacturerDetails.setmShortDescription(cursor.getString(14));
				mManufacturerList.add(mmanufacturerDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}
		
		return mManufacturerList;
	}


	public long insertManufacturerProducts(List<ManufacturerDetails> mDetails,String manufacturerId) {
		List<ManufacturerProductDetails> mProductDetails = new ArrayList<ManufacturerProductDetails>();
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues =0;
		try{
			openDataBase();
			Cursor cur = getData("select DISTINCT manufacturer_id from manufacturer_productmaster");
			if(cur.getCount() > 9){
				cur.moveToFirst();
				String mfr_id = cur.getString(cur.getColumnIndex("manufacturer_id"));
				System.out.println("MFR ID "+mfr_id);
				db.execSQL("DELETE * FROM manufacturer_productmaster WHERE manufacturer_id = '"+mfr_id+"'");
			}
			Log.e("Deleting manufacturer_productmaster working", "Done");
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Deleting manufacturer_productmaster working", "Error");
		}
		try {
			for(int i=0; i<mDetails.size();i++){
				ContentValues initialValues = new ContentValues();
				
				mProductDetails = mDetails.get(i).getmProductDetails();
				if(mProductDetails != null && mProductDetails.size()>0){
					for(ManufacturerProductDetails details: mProductDetails){
						initialValues.put("manufacturer_id", manufacturerId);
						initialValues.put("manufacturer_productId", details.getmManufacturerProduct_Id());
						initialValues.put("manufacturer_productname", details.getmManufacturerProduct_Name());
						initialValues.put("manufacturer_productimage", details.getmManufacturerProduct_Image());
						initialValues.put("manufacturer_companyname", details.getmManufacturerProduct_manufName());
						
						long row=0;
						row = insertData("manufacturer_productmaster", initialValues);
						Log.e("insertion manufacturer_productmaster Table", "insertion manufacturer_productmaster working");
						if (row != 0) {
							mTotalInsertedValues++;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
	}
	
	public List<ManufacturerProductDetails> getManufacturerProductDetails(String manufacturerId) {
		Cursor cursor;
		List<ManufacturerProductDetails> mProductDetails = new ArrayList<ManufacturerProductDetails>();
		cursor = getData("SELECT * FROM manufacturer_productmaster where manufacturer_id='"+manufacturerId+"'");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ManufacturerProductDetails mmanufacturerDetails = new ManufacturerProductDetails();
				mmanufacturerDetails.setmManufacturerProduct_Id(cursor.getString(1));
				mmanufacturerDetails.setmManufacturerProduct_Name(cursor.getString(2));
				mmanufacturerDetails.setmManufacturerProduct_Image(cursor.getString(3));
				mmanufacturerDetails.setmManufacturerProduct_manufName(cursor.getString(4));
				
				mProductDetails.add(mmanufacturerDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}
		
		return mProductDetails;
	}
	
	public List<ManufacturerProductDetails> getlessManufacturerProductDetails(String manufacturerId) {
		Cursor cursor;
		List<ManufacturerProductDetails> mProductDetails = new ArrayList<ManufacturerProductDetails>();
		cursor = getData("SELECT * FROM manufacturer_productmaster where manufacturer_id='"+manufacturerId+"' LIMIT 40");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ManufacturerProductDetails mmanufacturerDetails = new ManufacturerProductDetails();
				mmanufacturerDetails.setmManufacturerProduct_Id(cursor.getString(1));
				mmanufacturerDetails.setmManufacturerProduct_Name(cursor.getString(2));
				mmanufacturerDetails.setmManufacturerProduct_Image(cursor.getString(3));
				mmanufacturerDetails.setmManufacturerProduct_manufName(cursor.getString(4));
				
				mProductDetails.add(mmanufacturerDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}
		
		return mProductDetails;
	}
	
	public long insertInspectionServiceDetails(
			List<ServiceDetails> mServiceDetails) {
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM inspectionservice_master");
			Log.e("Deleting inspectionservice_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting inspectionservice_master working", "Error");
		}

		try {
			for (int i = 0; i < mServiceDetails.size(); i++) {
				ContentValues initialValues = new ContentValues();

				initialValues.put("service_name", mServiceDetails.get(i)
						.getmServiceName());
				initialValues.put("service_id", mServiceDetails.get(i)
						.getmServiceId());
				initialValues.put("service_companylogo", mServiceDetails.get(i)
						.getmService_CompanyImageUrl());
				initialValues.put("service_description", mServiceDetails.get(i)
						.getmServiceDescription());
				initialValues.put("service_address", mServiceDetails.get(i)
						.getmAddress());
				initialValues.put("service_phone", mServiceDetails.get(i)
						.getmPhoneNumber());
				initialValues.put("service_contactperson",
						mServiceDetails.get(i).getmContactPerson());
				initialValues.put("service_email", mServiceDetails.get(i)
						.getmEmail());
				initialValues.put("service_website", mServiceDetails.get(i)
						.getmWebsite());
				initialValues.put("service_shipmenttype", mServiceDetails
						.get(i).getmShipmentType());

				long rowId = insertData("inspectionservice_master",
						initialValues);
				if (rowId != 0) {
					mTotalInsertedValues++;
				}
			}

			Log.e("Inserting inspectionservice_master", "Done"
					+ mTotalInsertedValues);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
	}
	
	public long insertInspectionServiceList(
			List<ServiceDetails> mCustomServiceDetails) {
		List<ServiceList> mServiceList = new ArrayList<ServiceList>();
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM inspectionservice_listmaster");
			Log.e("Deleting inspectionservice_listmaster working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting inspectionservice_listmaster working", "Error");
		}
		try {
			for (int i = 0; i < mCustomServiceDetails.size(); i++) {
				ContentValues initialValues = new ContentValues();
				mServiceList = mCustomServiceDetails.get(i).getmServiceList();
				if (mServiceList != null && mServiceList.size() > 0) {
					for (ServiceList details : mServiceList) {

						initialValues.put("inspectionservice_id",
								mCustomServiceDetails.get(i).getmServiceId());
						initialValues.put("curency", details.getmCurrency());
						initialValues.put("units", details.getmUnits());
						initialValues.put("price", details.getmPrice());
						initialValues.put("description",
								details.getmDescription());
						initialValues.put("id", details.getmId());
						initialValues.put("name", details.getmName());
						long row = 0;
						row = insertData("inspectionservice_listmaster",
								initialValues);
						Log.e("insertion inspectionservice_listmaster Table",
								"insertion inspectionservice_listmaster working");
						if (row != 0) {
							mTotalInsertedValues++;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;

	}

	public List<ServiceDetails> getInspectionServicesDetails() {
		List<ServiceDetails> mCustomServices = new ArrayList<ServiceDetails>();
		Cursor cursor;
		cursor = getData("SELECT * FROM inspectionservice_master");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ServiceDetails mCustomDetails = new ServiceDetails();

				mCustomDetails.setmServiceName(cursor.getString(0));
				mCustomDetails.setmServiceId(cursor.getString(1));
				mCustomDetails.setmService_CompanyImageUrl(cursor.getString(2));
				mCustomDetails.setmServiceDescription(cursor.getString(3));
				mCustomDetails.setmAddress(cursor.getString(4));
				mCustomDetails.setmPhoneNumber(cursor.getString(5));
				mCustomDetails.setmContactPerson(cursor.getString(6));
				mCustomDetails.setmEmail(cursor.getString(7));
				mCustomDetails.setmWebsite(cursor.getString(8));
				mCustomDetails.setmShipmentType(cursor.getString(9));

				mCustomServices.add(mCustomDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mCustomServices;

	}

	public List<ServiceDetails> getInspectionServicesDetailsById(
			String mServiceId) {

		List<ServiceDetails> mCustomServices = new ArrayList<ServiceDetails>();
		Cursor cursor;
		cursor = getData("SELECT * FROM inspectionservice_master where service_id='"
				+ mServiceId + "'");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ServiceDetails mCustomDetails = new ServiceDetails();

				mCustomDetails.setmServiceName(cursor.getString(0));
				mCustomDetails.setmServiceId(cursor.getString(1));
				mCustomDetails.setmService_CompanyImageUrl(cursor.getString(2));
				mCustomDetails.setmServiceDescription(cursor.getString(3));
				mCustomDetails.setmAddress(cursor.getString(4));
				mCustomDetails.setmPhoneNumber(cursor.getString(5));
				mCustomDetails.setmContactPerson(cursor.getString(6));
				mCustomDetails.setmEmail(cursor.getString(7));
				mCustomDetails.setmWebsite(cursor.getString(8));
				mCustomDetails.setmShipmentType(cursor.getString(9));

				mCustomServices.add(mCustomDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mCustomServices;

	}

	public List<ServiceList> getInspectionServiceListById(
			String mInspectionServiceId) {

		List<ServiceList> mCustomServices = new ArrayList<ServiceList>();
		Cursor cursor;
		cursor = getData("SELECT * FROM inspectionservice_listmaster where inspectionservice_id='"
				+ mInspectionServiceId + "'");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ServiceList mCustomDetails = new ServiceList();

				mCustomDetails.setmCurrency(cursor.getString(1));
				mCustomDetails.setmUnits(cursor.getString(2));
				mCustomDetails.setmPrice(cursor.getString(3));
				mCustomDetails.setmDescription(cursor.getString(4));
				mCustomDetails.setmId(cursor.getString(5));
				mCustomDetails.setmName(cursor.getString(6));

				mCustomServices.add(mCustomDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mCustomServices;

	}
	
	public long insertCustomServiceDetails(
			List<ServiceDetails> mCustomServiceDetails) {
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM customservice_master");
			Log.e("Deleting customservice_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting customservice_master working", "Error");
		}

		try {
			for (int i = 0; i < mCustomServiceDetails.size(); i++) {
				ContentValues initialValues = new ContentValues();

				initialValues.put("service_name", mCustomServiceDetails.get(i)
						.getmServiceName());
				initialValues.put("service_id", mCustomServiceDetails.get(i)
						.getmServiceId());
				initialValues.put("service_companylogo", mCustomServiceDetails
						.get(i).getmService_CompanyImageUrl());
				initialValues.put("service_description", mCustomServiceDetails
						.get(i).getmServiceDescription());
				initialValues.put("service_address",
						mCustomServiceDetails.get(i).getmAddress());
				initialValues.put("service_phone", mCustomServiceDetails.get(i)
						.getmPhoneNumber());
				initialValues.put("service_contactperson",
						mCustomServiceDetails.get(i).getmContactPerson());
				initialValues.put("service_email", mCustomServiceDetails.get(i)
						.getmEmail());
				initialValues.put("service_website",
						mCustomServiceDetails.get(i).getmWebsite());
				initialValues.put("service_shipmenttype", mCustomServiceDetails
						.get(i).getmShipmentType());

				long rowId = insertData("customservice_master", initialValues);
				if (rowId != 0) {
					mTotalInsertedValues++;
				}
			}

			Log.e("Inserting customservice_master", "Done"
					+ mTotalInsertedValues);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
	}
	
	public long insertCustomServiceList(
			List<ServiceDetails> mCustomServiceDetails) {
		List<ServiceList> mServiceList = new ArrayList<ServiceList>();
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM customservice_listmaster");
			Log.e("Deleting customservice_listmaster working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting customservice_listmaster working", "Error");
		}
		try {
			for (int i = 0; i < mCustomServiceDetails.size(); i++) {
				ContentValues initialValues = new ContentValues();
				mServiceList = mCustomServiceDetails.get(i).getmServiceList();
				if (mServiceList != null && mServiceList.size() > 0) {
					for (ServiceList details : mServiceList) {

						initialValues.put("customservice_id",
								mCustomServiceDetails.get(i).getmServiceId());
						initialValues.put("curency", details.getmCurrency());
						initialValues.put("units", details.getmUnits());
						initialValues.put("price", details.getmPrice());
						initialValues.put("description",
								details.getmDescription());
						initialValues.put("id", details.getmId());
						initialValues.put("name", details.getmName());
						long row = 0;
						row = insertData("customservice_listmaster",
								initialValues);
						Log.e("insertion customservice_listmaster Table",
								"insertion customservice_listmaster working");
						if (row != 0) {
							mTotalInsertedValues++;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;

	}

	public List<ServiceDetails> getCustomServicesDetails() {
		List<ServiceDetails> mCustomServices = new ArrayList<ServiceDetails>();
		Cursor cursor;
		cursor = getData("SELECT * FROM customservice_master");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ServiceDetails mCustomDetails = new ServiceDetails();

				mCustomDetails.setmServiceName(cursor.getString(0));
				mCustomDetails.setmServiceId(cursor.getString(1));
				mCustomDetails.setmService_CompanyImageUrl(cursor.getString(2));
				mCustomDetails.setmServiceDescription(cursor.getString(3));
				mCustomDetails.setmAddress(cursor.getString(4));
				mCustomDetails.setmPhoneNumber(cursor.getString(5));
				mCustomDetails.setmContactPerson(cursor.getString(6));
				mCustomDetails.setmEmail(cursor.getString(7));
				mCustomDetails.setmWebsite(cursor.getString(8));
				mCustomDetails.setmShipmentType(cursor.getString(9));

				mCustomServices.add(mCustomDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mCustomServices;

	}

	public List<ServiceDetails> getCustomServicesDetailsById(String mServiceId) {

		List<ServiceDetails> mCustomServices = new ArrayList<ServiceDetails>();
		Cursor cursor;
		cursor = getData("SELECT * FROM customservice_master where service_id='"
				+ mServiceId + "'");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ServiceDetails mCustomDetails = new ServiceDetails();

				mCustomDetails.setmServiceName(cursor.getString(0));
				mCustomDetails.setmServiceId(cursor.getString(1));
				mCustomDetails.setmService_CompanyImageUrl(cursor.getString(2));
				mCustomDetails.setmServiceDescription(cursor.getString(3));
				mCustomDetails.setmAddress(cursor.getString(4));
				mCustomDetails.setmPhoneNumber(cursor.getString(5));
				mCustomDetails.setmContactPerson(cursor.getString(6));
				mCustomDetails.setmEmail(cursor.getString(7));
				mCustomDetails.setmWebsite(cursor.getString(8));
				mCustomDetails.setmShipmentType(cursor.getString(9));

				mCustomServices.add(mCustomDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mCustomServices;

	}

	public List<ServiceList> getCustomServiceListById(String customserviceId) {

		List<ServiceList> mCustomServices = new ArrayList<ServiceList>();
		Cursor cursor;
		cursor = getData("SELECT * FROM customservice_listmaster where customservice_id='"
				+ customserviceId + "'");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ServiceList mCustomDetails = new ServiceList();

				mCustomDetails.setmCurrency(cursor.getString(1));
				mCustomDetails.setmUnits(cursor.getString(2));
				mCustomDetails.setmPrice(cursor.getString(3));
				mCustomDetails.setmDescription(cursor.getString(4));
				mCustomDetails.setmId(cursor.getString(5));
				mCustomDetails.setmName(cursor.getString(6));

				mCustomServices.add(mCustomDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mCustomServices;

	}
	
	public long insertShippingServiceDetails(
			List<ServiceDetails> mServiceDetails) {
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM shippingservice_master");
			Log.e("Deleting shippingservice_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting shippingservice_master working", "Error");
		}

		try {
			for (int i = 0; i < mServiceDetails.size(); i++) {
				ContentValues initialValues = new ContentValues();

				initialValues.put("service_name", mServiceDetails.get(i)
						.getmServiceName());
				initialValues.put("service_id", mServiceDetails.get(i)
						.getmServiceId());
				initialValues.put("service_companylogo", mServiceDetails.get(i)
						.getmService_CompanyImageUrl());
				initialValues.put("service_description", mServiceDetails.get(i)
						.getmServiceDescription());
				initialValues.put("service_address", mServiceDetails.get(i)
						.getmAddress());
				initialValues.put("service_phone", mServiceDetails.get(i)
						.getmPhoneNumber());
				initialValues.put("service_contactperson",
						mServiceDetails.get(i).getmContactPerson());
				initialValues.put("service_email", mServiceDetails.get(i)
						.getmEmail());
				initialValues.put("service_website", mServiceDetails.get(i)
						.getmWebsite());
				initialValues.put("service_shipmenttype", mServiceDetails
						.get(i).getmShipmentType());

				long rowId = insertData("shippingservice_master", initialValues);
				if (rowId != 0) {
					mTotalInsertedValues++;
				}
			}

			Log.e("Inserting shippingservice_master", "Done"
					+ mTotalInsertedValues);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
	}

	public long insertShippingServiceList(
			List<ServiceDetails> mCustomServiceDetails) {
		List<ServiceList> mServiceList = new ArrayList<ServiceList>();
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues = 0;
		try {
			openDataBase();
			db.execSQL("DELETE FROM shippingservice_listmaster");
			Log.e("Deleting shippingservice_listmaster working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting shippingservice_listmaster working", "Error");
		}
		try {
			for (int i = 0; i < mCustomServiceDetails.size(); i++) {
				ContentValues initialValues = new ContentValues();
				mServiceList = mCustomServiceDetails.get(i).getmServiceList();
				if (mServiceList != null && mServiceList.size() > 0) {
					for (ServiceList details : mServiceList) {

						initialValues.put("shippingservice_id",
								mCustomServiceDetails.get(i).getmServiceId());
						initialValues.put("curency", details.getmCurrency());
						initialValues.put("units", details.getmUnits());
						initialValues.put("price", details.getmPrice());
						initialValues.put("description",
								details.getmDescription());
						initialValues.put("id", details.getmId());
						initialValues.put("name", details.getmName());
						long row = 0;
						row = insertData("shippingservice_listmaster",
								initialValues);
						Log.e("insertion shippingservice_listmaster Table",
								"insertion shippingservice_listmaster working");
						if (row != 0) {
							mTotalInsertedValues++;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;

	}

	public List<ServiceDetails> getShippingServicesDetails(String shipment_type) {
		List<ServiceDetails> mCustomServices = new ArrayList<ServiceDetails>();
		Cursor cursor;
		cursor = getData("SELECT * FROM shippingservice_master where service_shipmenttype='"
				+ shipment_type + "'");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ServiceDetails mCustomDetails = new ServiceDetails();

				mCustomDetails.setmServiceName(cursor.getString(0));
				mCustomDetails.setmServiceId(cursor.getString(1));
				mCustomDetails.setmService_CompanyImageUrl(cursor.getString(2));
				mCustomDetails.setmServiceDescription(cursor.getString(3));
				mCustomDetails.setmAddress(cursor.getString(4));
				mCustomDetails.setmPhoneNumber(cursor.getString(5));
				mCustomDetails.setmContactPerson(cursor.getString(6));
				mCustomDetails.setmEmail(cursor.getString(7));
				mCustomDetails.setmWebsite(cursor.getString(8));
				mCustomDetails.setmShipmentType(cursor.getString(9));

				mCustomServices.add(mCustomDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mCustomServices;

	}

	public List<ServiceDetails> getShippingServicesDetailsById(String mServiceId) {

		List<ServiceDetails> mCustomServices = new ArrayList<ServiceDetails>();
		Cursor cursor;
		cursor = getData("SELECT * FROM shippingservice_master where service_id='"
				+ mServiceId + "'");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ServiceDetails mCustomDetails = new ServiceDetails();

				mCustomDetails.setmServiceName(cursor.getString(0));
				mCustomDetails.setmServiceId(cursor.getString(1));
				mCustomDetails.setmService_CompanyImageUrl(cursor.getString(2));
				mCustomDetails.setmServiceDescription(cursor.getString(3));
				mCustomDetails.setmAddress(cursor.getString(4));
				mCustomDetails.setmPhoneNumber(cursor.getString(5));
				mCustomDetails.setmContactPerson(cursor.getString(6));
				mCustomDetails.setmEmail(cursor.getString(7));
				mCustomDetails.setmWebsite(cursor.getString(8));
				mCustomDetails.setmShipmentType(cursor.getString(9));

				mCustomServices.add(mCustomDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mCustomServices;

	}

	public List<ServiceList> getShippingServiceListById(
			String mInspectionServiceId) {

		List<ServiceList> mCustomServices = new ArrayList<ServiceList>();
		Cursor cursor;
		cursor = getData("SELECT * FROM shippingservice_listmaster where shippingservice_id='"
				+ mInspectionServiceId + "'");
		if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ServiceList mCustomDetails = new ServiceList();

				mCustomDetails.setmCurrency(cursor.getString(1));
				mCustomDetails.setmUnits(cursor.getString(2));
				mCustomDetails.setmPrice(cursor.getString(3));
				mCustomDetails.setmDescription(cursor.getString(4));
				mCustomDetails.setmId(cursor.getString(5));
				mCustomDetails.setmName(cursor.getString(6));

				mCustomServices.add(mCustomDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mCustomServices;

	}
	
	//Shubham
	
	public void insertdashboardOrderValues(List<OrderDashboard> mOrderDashboardList) {
		SQLiteDatabase database = this.getWritableDatabase();
		try {
			openDataBase();
			database.execSQL("delete from dashboard_order_master");
			database.close();
			Log.e("Delete Success", "Success");
		} catch (Exception e) {
			Log.e("Exception", "Delete not done Excepton");
			e.printStackTrace();
		}
		long mTotalValues = 0;
		try {
			if(mOrderDashboardList==null || mOrderDashboardList.size()==0){
			}else{
				for (int i = 0; i < mOrderDashboardList.size(); i++) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("order_offeredprice", mOrderDashboardList.get(i).getmOrder_OfferPrice());
					initialValues.put("OrderDate",mOrderDashboardList.get(i).getmOrder_Date());
					initialValues.put("OrderId", mOrderDashboardList.get(i).getmOrder_Id());
					initialValues.put("OrderNumber", mOrderDashboardList.get(i).getmOrder_Number());
					initialValues.put("OrderQuantity", mOrderDashboardList.get(i).getmOrder_Quantity());
					initialValues.put("OrderStatusId",mOrderDashboardList.get(i).getmOrder_StatusId());
					initialValues.put("SubTotal",mOrderDashboardList.get(i).getmOrder_SubTotal());
					initialValues.put("TotalCount",mOrderDashboardList.get(i).getmOrder_TotalCount());
					initialValues.put("UnitsOfMeasurement",mOrderDashboardList.get(i).getmOrder_UnitOfMeasurement());
					initialValues.put("Order_ProductName",mOrderDashboardList.get(i).getmOrder_Productname());
					
					initialValues.put("Order_ProductImage",mOrderDashboardList.get(i).getmOrder_ProductImage());
					initialValues.put("Order_ProductId",mOrderDashboardList.get(i).getmOrder_ProductId());
					initialValues.put("Order_ManufacturerName",mOrderDashboardList.get(i).getmOrder_Manufacturname());
					initialValues.put("Order_ManufacturerId",mOrderDashboardList.get(i).getmOrder_ManufacturerId());
					initialValues.put("Order_ManufacturerLogo",mOrderDashboardList.get(i).getmOrder_ManufacturerLogo());
					initialValues.put("Order_BuyerName",mOrderDashboardList.get(i).getmOrder_Buyername());
					
					
					long rowid = 0;
					rowid = insertData("dashboard_order_master", initialValues);
					if (rowid != 0) {
						mTotalValues++;
					}
					Log.e("Total inserted values in dashboard_order_master", ""
							+ mTotalValues);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Exception", "Eroor");
		}

	}

	public ArrayList<OrderDashboard> getDashboardOrder() {
		ArrayList<OrderDashboard> list = new ArrayList<OrderDashboard>();
		Cursor cursor = getData("Select * from dashboard_order_master");
		if (cursor.getCount() == 0) {
		} else {
			cursor.moveToFirst();
			int count = cursor.getCount();
			for (int i = 0; i < count; i++) {
				OrderDashboard dashboard = new OrderDashboard();
				dashboard.setmOrder_OfferPrice(cursor.getString(0));
				dashboard.setmOrder_Date(cursor.getString(1));
				dashboard.setmOrder_Id(cursor.getString(2));
				dashboard.setmOrder_Number(cursor.getString(3));
				dashboard.setmOrder_Quantity(cursor.getString(4));
				dashboard.setmOrder_StatusId(cursor.getString(5));
				dashboard.setmOrder_SubTotal(cursor.getString(6));
				dashboard.setmOrder_TotalCount(cursor.getString(7));
				dashboard.setmOrder_UnitOfMeasurement(cursor.getString(8));
				dashboard.setmOrder_Productname(cursor.getString(9));
				dashboard.setmOrder_ProductImage(cursor.getString(10));
				dashboard.setmOrder_ProductId(cursor.getString(11));
				dashboard.setmOrder_Manufacturname(cursor.getString(12));
				dashboard.setmOrder_ManufacturerId(cursor.getString(13));
				dashboard.setmOrder_ManufacturerLogo(cursor.getString(14));
				dashboard.setmOrder_Buyername(cursor.getString(15));
				
				list.add(dashboard);
				cursor.moveToNext();
			}

		}
		return list;
	}

	public void insertDashboardQuotation(ArrayList<QuoatationDashboard> dashboards) {
		SQLiteDatabase database = this.getWritableDatabase();
		try {
			openDataBase();
			database.execSQL("delete from dashboard_quotation_master");
			Log.e("Deldte Sucess", "dashboard_quotation_master");
		} catch (Exception e) {
			Log.e("Deldte Failed", "dashboard_quotation_master");
		}
		long mTotalValues = 0;
		try {
			if(dashboards==null || dashboards.size()==0){
			}else{
				ContentValues initialvalues = new ContentValues();
				for (QuoatationDashboard qd : dashboards) {
					initialvalues.put("quotation_buyer",qd.getQuotation_buyername());
					initialvalues.put("quotation_manufacture",qd.getQuotation_manufacturename());
					initialvalues.put("quotation_productname",qd.getQuotation_productname());
					initialvalues.put("quotation_created_date",qd.getQuotation_createddate());
					initialvalues.put("quotation_id", qd.getQuotation_Id());
					initialvalues.put("quotation_isexternal_product",qd.getQuotation_isExternalProduct());
					initialvalues.put("quotation_subject",qd.getQuotation_Subject());
					initialvalues.put("product_image", qd.getmProduct_Image());
					initialvalues.put("quotation_number", qd.getmQuotation_Number());
					initialvalues.put("quotation_datetime", qd.getmQuotation_Datetime());
					initialvalues.put("quotationstatusId", qd.getmQuotationStatusId());
					initialvalues.put("totalcount", qd.getmTotalCount());
					long rowid = 0;
					rowid = insertData("dashboard_quotation_master", initialvalues);
					if (rowid != 0) {
						mTotalValues++;
					}
					Log.e("Total inserted values in dashboard_quotation_master", ""
							+ mTotalValues);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Exception in dashboard_quotation_master ", "Error");
		}

	}

	public ArrayList<QuoatationDashboard> getDashboardQuotation() {
		ArrayList<QuoatationDashboard> list = new ArrayList<QuoatationDashboard>();
		Cursor cursor = getData("Select * from dashboard_quotation_master");
		if (cursor.getCount() == 0) {

		} else {
			cursor.moveToFirst();
			int count = cursor.getCount();
			for (int i = 0; i < count; i++) {
				QuoatationDashboard qudashboard = new QuoatationDashboard();
				qudashboard.setQuotation_buyername(cursor.getString(0));
				qudashboard.setQuotation_manufacturename(cursor.getString(1));
				qudashboard.setQuotation_productname(cursor.getString(2));
				qudashboard.setQuotation_createddate(cursor.getString(3));
				qudashboard.setQuotation_Id(cursor.getString(4));
				qudashboard.setQuotation_isExternalProduct(cursor.getString(5));
				qudashboard.setQuotation_Subject(cursor.getString(6));
				qudashboard.setmProduct_Image(cursor.getString(8));
				qudashboard.setmQuotation_Number(cursor.getString(9));
				qudashboard.setmQuotation_Datetime(cursor.getString(10));
				qudashboard.setmQuotationStatusId(cursor.getString(11));
				qudashboard.setmTotalCount(cursor.getString(12));
				
				list.add(qudashboard);
				cursor.moveToNext();
			}

		}
		return list;
	}

	/*public void insertQuotationHistory(ArrayList<QuotationDashboardHistory> dashboardHistories) {
		SQLiteDatabase database = this.getWritableDatabase();
		try {
			openDataBase();
			database.execSQL("delete from dashboard_quotationhistory");
			database.execSQL("delete from milestones");
			database.close();
			Log.e("delete Success", "dashboard_quotationhistory Success");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("delete Error", "dashboard_quotationhistory Error");
		}
		long mTotalValues = 0;
		try {
			ContentValues initialvalues = new ContentValues();
			for (QuotationDashboardHistory qdh : dashboardHistories) {
				initialvalues.put("hbuyer_name", qdh.getHistory_Buyername());
				initialvalues.put("hbuyer_image", qdh.getHistory_Buyerimage());
				initialvalues.put("hman_name", qdh.getHistory_Manufacturename());
				initialvalues.put("hman_image",qdh.getHistory_Manufactureimage());
				initialvalues.put("hpro_name", qdh.getHistory_productname());
				initialvalues.put("hpro_image", qdh.getHistory_productimage());
				initialvalues.put("hsubject", qdh.getHistory_subject());
				initialvalues.put("h_createddate", qdh.getHistory_createddate());
				initialvalues.put("h_id", qdh.getHistory_id());
				initialvalues.put("his_externalpro",qdh.getHistory_isexternalproduct());
				initialvalues.put("hlst_manresponseid",qdh.getHistory_LastManufacturerResponseId());
				initialvalues.put("last_responsedate",qdh.getHistory_LastResponseDate());
				initialvalues.put("last_responsesequencenum",qdh.getHistory_LastResponseSequenceNumber());
				initialvalues.put("h_quotstatusid",qdh.getHistory_QuotationStatusId());
				initialvalues.put("chat_message", qdh.getChat_message());
				initialvalues.put("offered_message", qdh.getOffered_price());
				initialvalues.put("chat_reqquant", qdh.getChat_RequiredQty());
				initialvalues.put("chat_responseuser_type",qdh.getChat_ResponseUserType());
				initialvalues.put("additional_info", qdh.getmAdditionalInfo());
				
				List<Milestones> mMilestones = qdh.getmMilestoneList();
				if(mMilestones == null || mMilestones.size() == 0){
					
				}else{
					int mTotalmilestones = 0;
					ContentValues milestones = new ContentValues();
					for(Milestones ms : mMilestones){
						milestones.put("quotaion_history_id", ms.getmQuotationHistoryId());
						milestones.put("name", ms.getmName());
						milestones.put("percent_breakup", ms.getmPercentBreakup());
						milestones.put("brief_description", ms.getmBriefDescription());
						
						long rowid = 0;
						rowid = insertData("milestones", milestones);
						if(rowid != 0){
							mTotalmilestones++;
						}
					}
					Log.e("Total inserted values in milestones table", ""+ mTotalmilestones);
				}
				long rowid = 0;
				rowid = insertData("dashboard_quotationhistory", initialvalues);
				if (rowid != 0) {
					mTotalValues++;
				}
				Log.e("Total inserted values in dashboard_quotationhistory", ""+ mTotalValues);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Exception", "Error");

		}
	}*/
	
	/*public void insertQuotationHistory(ArrayList<QuotationDashboardHistory> dashboardHistories) {
		SQLiteDatabase database = this.getWritableDatabase();
		try {
			openDataBase();
			database.execSQL("delete from dashboard_quotationhistory");
			database.execSQL("delete from milestones");
			database.close();
			Log.e("delete Success", "dashboard_quotationhistory Success");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("delete Error", "dashboard_quotationhistory Error");
		}
		long mTotalValues = 0;
		try {
			ContentValues initialvalues = new ContentValues();
			for (QuotationDashboardHistory qdh : dashboardHistories) {
				initialvalues.put("hbuyer_name", qdh.getHistory_Buyername());
				initialvalues.put("hbuyer_image", qdh.getHistory_Buyerimage());
				initialvalues.put("hman_name", qdh.getHistory_Manufacturename());
				initialvalues.put("hman_image",qdh.getHistory_Manufactureimage());
				initialvalues.put("hpro_name", qdh.getHistory_productname());
				initialvalues.put("hpro_image", qdh.getHistory_productimage());
				initialvalues.put("hsubject", qdh.getHistory_subject());
				initialvalues.put("h_createddate", qdh.getHistory_createddate());
				initialvalues.put("h_id", qdh.getHistory_id());
				initialvalues.put("his_externalpro",qdh.getHistory_isexternalproduct());
				initialvalues.put("hlst_manresponseid",qdh.getHistory_LastManufacturerResponseId());
				initialvalues.put("last_responsedate",qdh.getHistory_LastResponseDate());
				initialvalues.put("last_responsesequencenum",qdh.getHistory_LastResponseSequenceNumber());
				initialvalues.put("h_quotstatusid",qdh.getHistory_QuotationStatusId());
				initialvalues.put("chat_message", qdh.getChat_message());
				initialvalues.put("offered_message", qdh.getOffered_price());
				initialvalues.put("chat_reqquant", qdh.getChat_RequiredQty());
				initialvalues.put("chat_responseuser_type",qdh.getChat_ResponseUserType());
				initialvalues.put("additional_info", qdh.getmAdditionalInfo());
				initialvalues.put("history_quotation_response_id", qdh.getHistory_QuotationResponseId());
				initialvalues.put("last_manufacturer_response_id", qdh.getHistory_QuotationResponseId());
				initialvalues.put("chat_measurement", qdh.getChat_UnitsOfMeasurement());
				initialvalues.put("attachment", qdh.getmAttachment());
				
				List<Milestones> mMilestones = qdh.getmMilestoneList();
				if(mMilestones == null || mMilestones.size() == 0){
					
				}else{
					int mTotalmilestones = 0;
					ContentValues milestones = new ContentValues();
					for(Milestones ms : mMilestones){
						milestones.put("quotaion_history_id", ms.getmQuotationHistoryId());
						milestones.put("name", ms.getmName());
						milestones.put("percent_breakup", ms.getmPercentBreakup());
						milestones.put("brief_description", ms.getmBriefDescription());
						
						long rowid = 0;
						rowid = insertData("milestones", milestones);
						if(rowid != 0){
							mTotalmilestones++;
						}
					}
					Log.e("Total inserted values in milestones table", ""+ mTotalmilestones);
				}
				long rowid = 0;
				rowid = insertData("dashboard_quotationhistory", initialvalues);
				if (rowid != 0) {
					mTotalValues++;
				}
				Log.e("Total inserted values in dashboard_quotationhistory", ""+ mTotalValues);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Exception", "Error");

		}
	}*/
	
	/*public void insertQuotationHistory(List<QuotationDashboardHistory> dashboardHistories) {
		SQLiteDatabase database = this.getWritableDatabase();
		try {
			openDataBase();
			database.execSQL("delete from dashboard_quotationhistory");
			database.execSQL("delete from milestones");
			database.close();
			Log.e("delete Success", "dashboard_quotationhistory Success");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("delete Error", "dashboard_quotationhistory Error");
		}
		long mTotalValues = 0;
		try {
			ContentValues initialvalues = new ContentValues();
			for (QuotationDashboardHistory qdh : dashboardHistories) {
				initialvalues.put("hbuyer_name", qdh.getHistory_Buyername());
				initialvalues.put("hbuyer_image", qdh.getHistory_Buyerimage());
				initialvalues.put("hman_name", qdh.getHistory_Manufacturename());
				initialvalues.put("hman_image",qdh.getHistory_Manufactureimage());
				initialvalues.put("hpro_name", qdh.getHistory_productname());
				initialvalues.put("hpro_image", qdh.getHistory_productimage());
				initialvalues.put("hsubject", qdh.getHistory_subject());
				initialvalues.put("h_createddate", qdh.getHistory_createddate());
				initialvalues.put("h_id", qdh.getHistory_id());
				initialvalues.put("his_externalpro",qdh.getHistory_isexternalproduct());
				initialvalues.put("hlst_manresponseid",qdh.getHistory_LastManufacturerResponseId());
				initialvalues.put("last_responsedate",qdh.getChat_ResponseDate());
				initialvalues.put("last_responsesequencenum",qdh.getHistory_LastResponseSequenceNumber());
				initialvalues.put("h_quotstatusid",qdh.getHistory_QuotationStatusId());
				initialvalues.put("chat_message", qdh.getChat_message());
				initialvalues.put("offered_message", qdh.getOffered_price());
				initialvalues.put("chat_reqquant", qdh.getChat_RequiredQty());
				initialvalues.put("chat_responseuser_type",qdh.getChat_ResponseUserType());
				initialvalues.put("additional_info", qdh.getmAdditionalInfo());
				initialvalues.put("history_quotation_response_id", qdh.getHistory_QuotationResponseId());
				initialvalues.put("last_manufacturer_response_id", qdh.getHistory_QuotationResponseId());
				initialvalues.put("chat_measurement", qdh.getChat_UnitsOfMeasurement());
				initialvalues.put("attachment", qdh.getmAttachment());
				
				List<Milestones> mMilestones = qdh.getmMilestoneList();
				if(mMilestones == null || mMilestones.size() == 0){
					
				}else{
					int mTotalmilestones = 0;
					ContentValues milestones = new ContentValues();
					for(Milestones ms : mMilestones){
						milestones.put("quotaion_history_id", ms.getmQuotationHistoryId());
						milestones.put("name", ms.getmName());
						milestones.put("percent_breakup", ms.getmPercentBreakup());
						milestones.put("brief_description", ms.getmBriefDescription());
						
						long rowid = 0;
						rowid = insertData("milestones", milestones);
						if(rowid != 0){
							mTotalmilestones++;
						}
					}
					Log.e("Total inserted values in milestones table", ""+ mTotalmilestones);
				}
				long rowid = 0;
				rowid = insertData("dashboard_quotationhistory", initialvalues);
				if (rowid != 0) {
					mTotalValues++;
				}
				Log.e("Total inserted values in dashboard_quotationhistory", ""+ mTotalValues);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Exception", "Error");

		}
	}*/
	
	public void insertQuotationHistory(List<QuotationDashboardHistory> dashboardHistories) {
		SQLiteDatabase database = this.getWritableDatabase();
		try {
			openDataBase();
			database.execSQL("delete from dashboard_quotationhistory");
			database.execSQL("delete from milestones");
			database.close();
			Log.e("delete Success", "dashboard_quotationhistory Success");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("delete Error", "dashboard_quotationhistory Error");
		}
		long mTotalValues = 0;
		try {
			ContentValues initialvalues = new ContentValues();
			for (QuotationDashboardHistory qdh : dashboardHistories) {
				initialvalues.put("hbuyer_name", qdh.getHistory_Buyername());
				initialvalues.put("hbuyer_image", qdh.getHistory_Buyerimage());
				initialvalues.put("hman_name", qdh.getHistory_Manufacturename());
				initialvalues.put("hman_image",qdh.getHistory_Manufactureimage());
				initialvalues.put("hpro_name", qdh.getHistory_productname());
				initialvalues.put("hpro_image", qdh.getHistory_productimage());
				initialvalues.put("hsubject", qdh.getHistory_subject());
				initialvalues.put("h_createddate", qdh.getHistory_createddate());
				initialvalues.put("h_id", qdh.getHistory_id());
				initialvalues.put("his_externalpro",qdh.getHistory_isexternalproduct());
				initialvalues.put("hlst_manresponseid",qdh.getHistory_LastManufacturerResponseId());
				initialvalues.put("last_responsedate",qdh.getChat_ResponseDate());
				initialvalues.put("last_responsesequencenum",qdh.getHistory_LastResponseSequenceNumber());
				initialvalues.put("h_quotstatusid",qdh.getHistory_QuotationStatusId());
				initialvalues.put("chat_message", qdh.getChat_message());
				initialvalues.put("offered_price", qdh.getOffered_price());
				initialvalues.put("chat_reqquant", qdh.getChat_RequiredQty());
				initialvalues.put("chat_responseuser_type",qdh.getChat_ResponseUserType());
				initialvalues.put("additional_info", qdh.getmAdditionalInfo());
				initialvalues.put("history_quotation_response_id", qdh.getHistory_QuotationResponseId());
				initialvalues.put("last_manufacturer_response_id", qdh.getHistory_QuotationResponseId());
				initialvalues.put("chat_measurement", qdh.getChat_UnitsOfMeasurement());
				initialvalues.put("attachment", qdh.getmAttachment());
				initialvalues.put("deliverydate", qdh.getmShipping_DeliveryDate());
				initialvalues.put("dimensionlwh", qdh.getmDimensionLwh());
				initialvalues.put("dimensionweight", qdh.getmDimensionWeight());
				initialvalues.put("fromcity", qdh.getmShipping_FromCity());
				initialvalues.put("fromcountry", qdh.getmShipping_FromCountry());
				initialvalues.put("tocity", qdh.getmShipping_ToCity());
				initialvalues.put("tocountry", qdh.getmShipping_ToCountry());
				initialvalues.put("totalnumberOfcatons", qdh.getmTotalNumberOfCatons());
				initialvalues.put("totalweight", qdh.getmTotalWeight());
				initialvalues.put("inspectiontype", qdh.getmInspectionType());
				initialvalues.put("inspectortype", qdh.getmInspectorType());
				//initialvalues.put("inspectortype", qdh.getmInspectorType());
				//initialvalues.put("placeToinspect", qdh.getmPlaceToInspect());
				initialvalues.put("typeOfindustry", qdh.getmTypeOfIndustry());
				initialvalues.put("width", qdh.getmWidth());
				initialvalues.put("height", qdh.getmHeight());
				initialvalues.put("lenght", qdh.getmLength());
				initialvalues.put("weight", qdh.getmWeight());
				initialvalues.put("inspectiontypeid", qdh.getmInspectionTypeId());
				initialvalues.put("inspectortypeid", qdh.getmInspectorTypeId());
				initialvalues.put("industoryid", qdh.getmTypeOfIndustryId());
				initialvalues.put("city", qdh.getmCity());
				initialvalues.put("address", qdh.getmAddress());
				initialvalues.put("countryid", qdh.getmCountryId());
				initialvalues.put("countryName", qdh.getmCountryName());
				initialvalues.put("stateid", qdh.getmStateId());
				initialvalues.put("stateName", qdh.getmStateName());
				initialvalues.put("categoryid", qdh.getmCategoryId());
				initialvalues.put("categoryName", qdh.getmCategoryName());
				initialvalues.put("subCategoryId", qdh.getmSubCategoryId());
				initialvalues.put("subCategoryName", qdh.getmSubCategoryName());
				initialvalues.put("quantity", qdh.getmQuantity());
				initialvalues.put("companyName", qdh.getmCompanyName());
				initialvalues.put("shipping_pickup", qdh.getmShipping_pickup());
				
				
				List<Milestones> mMilestones = qdh.getmMilestoneList();
				if(mMilestones == null || mMilestones.size() == 0){
					
				}else{
					int mTotalmilestones = 0;
					ContentValues milestones = new ContentValues();
					for(Milestones ms : mMilestones){
						milestones.put("quotaion_history_id", qdh.getHistory_QuotationResponseId());
						milestones.put("name", ms.getmName());
						milestones.put("percent_breakup", ms.getmPercentBreakup());
						milestones.put("brief_description", ms.getmBriefDescription());
						
						long rowid = 0;
						rowid = insertData("milestones", milestones);
						if(rowid != 0){
							mTotalmilestones++;
						}
					}
					Log.e("Total inserted values in milestones table", ""+ mTotalmilestones);
				}
				long rowid = 0;
				rowid = insertData("dashboard_quotationhistory", initialvalues);
				if (rowid != 0) {
					mTotalValues++;
				}
				Log.e("Total inserted values in dashboard_quotationhistory", ""+ mTotalValues);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Exception", "Error");

		}
	}



	public ArrayList<QuotationDashboardHistory> getLatestChatMessage() {
		ArrayList<QuotationDashboardHistory> arrayList = new ArrayList<QuotationDashboardHistory>();

		Cursor cursor = getData("select max(last_responsedate) from dashboard_quotationhistory");
		if (cursor == null || cursor.getCount() == 0) {

		} else {
			cursor.moveToFirst();
			int count = cursor.getCount();
			for (int i = 0; i < count; i++) {
				QuotationDashboardHistory dashboardHistory = new QuotationDashboardHistory();
				dashboardHistory.setHistory_LastResponseDate(cursor.getString(0));
				arrayList.add(dashboardHistory);
				cursor.moveToNext();
			}
		}

		return arrayList;

	}
	
	

	public long insertSearchDetails(List<ProductList> mProductDetails) {
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			openDataBase();
			db.execSQL("DELETE FROM search_master");
			Log.e("Deleting search_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting search_master working", "Error");
		}
		try {
			for (int i = 0; i < mProductDetails.size(); i++) {
				ContentValues initialValues = new ContentValues();
				initialValues.put("product_id", mProductDetails.get(i).getmProduct_Id());
				initialValues.put("product_name", mProductDetails.get(i).getmProduct_Name());
				initialValues.put("product_image", mProductDetails.get(i).getmProduct_Image());
				initialValues.put("manufacturer_id", mProductDetails.get(i).getmManufacturer_Id());
				initialValues.put("manufacturer_name", mProductDetails.get(i).getmManufacturer_Name());
				initialValues.put("manufacturer_image", mProductDetails.get(i).getmManufacturer_Image());
				initialValues.put("product_price", mProductDetails.get(i).getmProductPrice());
				initialValues.put("product_quantity", mProductDetails.get(i).getmProductQuantity());
				initialValues.put("product_unitofmeasurement", mProductDetails.get(i).getmUnitOfMeasurement());
				initialValues.put("total_count", mProductDetails.get(i).getmProductTotalCount());
				
				long row = 0;
				row = insertData("search_master", initialValues);
				Log.e("insertion search_master Table",
						"insertion search_master working");
				if (row != 0) {
					mTotalInsertedValues++;
				}
			}
			
			Log.e("Inserted search_master", "Rows "+ mTotalInsertedValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
	}
	
	public List<ProductList> getSearchList() {
		Cursor cursor;
		List<ProductList> mSearchList = new ArrayList<ProductList>();
		cursor = getData("SELECT * FROM search_master");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ProductList mSearchDetails = new ProductList();

				mSearchDetails.setmProduct_Id(cursor.getString(0));
				mSearchDetails.setmProduct_Name(cursor.getString(1));
				mSearchDetails.setmProduct_Image(cursor.getString(2));
				mSearchDetails.setmManufacturer_Id(cursor.getString(3));
				mSearchDetails.setmManufacturer_Name(cursor.getString(4));
				mSearchDetails.setmManufacturer_Image(cursor.getString(5));
				mSearchDetails.setmProductPrice(cursor.getString(6));
				mSearchDetails.setmProductQuantity(cursor.getString(7));
				mSearchDetails.setmUnitOfMeasurement(cursor.getString(8));
				mSearchDetails.setmProductTotalCount(cursor.getString(9));
				mSearchList.add(mSearchDetails);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}
		return mSearchList;
	}
	
	/*public ArrayList<QuotationDashboardHistory> getAllDashboardQuotationHistory(String mResponseType) {
		ArrayList<QuotationDashboardHistory> list = new ArrayList<QuotationDashboardHistory>();
		Cursor cursor = null;
		if(mResponseType == null || mResponseType.length() == 0){
			cursor = getData("select * from dashboard_quotationhistory");
		}else{
			cursor = getData("select * from dashboard_quotationhistory where chat_responseuser_type='"+mResponseType+"'");
		}
		if (cursor == null || cursor.getCount() == 0) {

		} else {
			cursor.moveToFirst();
			int count = cursor.getCount();
			for (int i = 0; i < count; i++) {
				QuotationDashboardHistory dashboardHistory = new QuotationDashboardHistory();
				dashboardHistory.setHistory_Buyername(cursor.getString(0));
				dashboardHistory.setHistory_Buyerimage(cursor.getString(1));
				dashboardHistory.setHistory_Manufacturename(cursor.getString(2));
				dashboardHistory.setHistory_Manufactureimage(cursor.getString(3));
				dashboardHistory.setHistory_productname(cursor.getString(4));
				dashboardHistory.setHistory_productimage(cursor.getString(5));
				dashboardHistory.setHistory_subject(cursor.getString(6));
				dashboardHistory.setHistory_createddate(cursor.getString(7));
				dashboardHistory.setHistory_id(cursor.getString(8));
				dashboardHistory.setHistory_isexternalproduct(cursor.getString(9));
				dashboardHistory.setHistory_LastManufacturerResponseId(cursor.getString(10));
				dashboardHistory.setChat_ResponseDate(cursor.getString(11));
				dashboardHistory.setHistory_LastResponseSequenceNumber(cursor.getString(12));
				dashboardHistory.setHistory_QuotationStatusId(cursor.getString(13));
				dashboardHistory.setChat_message(cursor.getString(14));
				dashboardHistory.setOffered_price(cursor.getString(15));
				dashboardHistory.setChat_RequiredQty(cursor.getString(16));
				dashboardHistory.setChat_ResponseUserType(cursor.getString(17));
				dashboardHistory.setmAdditionalInfo(cursor.getString(20));
				
				list.add(dashboardHistory);
				cursor.moveToNext();
			}
		}

		return list;

	}*/
	
	/*public ArrayList<QuotationDashboardHistory> getAllDashboardQuotationHistory(String mResponseType) {
		ArrayList<QuotationDashboardHistory> list = new ArrayList<QuotationDashboardHistory>();
		Cursor cursor = null;
		if(mResponseType == null || mResponseType.length() == 0){
			cursor = getData("select * from dashboard_quotationhistory");
		}else{
			cursor = getData("select * from dashboard_quotationhistory where chat_responseuser_type='"+mResponseType+"'");
		}
		if (cursor == null || cursor.getCount() == 0) {

		} else {
			cursor.moveToFirst();
			int count = cursor.getCount();
			for (int i = 0; i < count; i++) {
				QuotationDashboardHistory dashboardHistory = new QuotationDashboardHistory();
				dashboardHistory.setHistory_Buyername(cursor.getString(0));
				dashboardHistory.setHistory_Buyerimage(cursor.getString(1));
				dashboardHistory.setHistory_Manufacturename(cursor.getString(2));
				dashboardHistory.setHistory_Manufactureimage(cursor.getString(3));
				dashboardHistory.setHistory_productname(cursor.getString(4));
				dashboardHistory.setHistory_productimage(cursor.getString(5));
				dashboardHistory.setHistory_subject(cursor.getString(6));
				dashboardHistory.setHistory_createddate(cursor.getString(7));
				dashboardHistory.setHistory_id(cursor.getString(8));
				dashboardHistory.setHistory_isexternalproduct(cursor.getString(9));
				dashboardHistory.setHistory_LastManufacturerResponseId(cursor.getString(10));
				dashboardHistory.setChat_ResponseDate(cursor.getString(11));
				dashboardHistory.setHistory_LastResponseSequenceNumber(cursor.getString(12));
				dashboardHistory.setHistory_QuotationStatusId(cursor.getString(13));
				dashboardHistory.setChat_message(cursor.getString(14));
				dashboardHistory.setOffered_price(cursor.getString(15));
				dashboardHistory.setChat_RequiredQty(cursor.getString(16));
				dashboardHistory.setChat_ResponseUserType(cursor.getString(17));
				dashboardHistory.setmAdditionalInfo(cursor.getString(20));
				dashboardHistory.setHistory_QuotationResponseId(cursor.getString(21));
				dashboardHistory.setHistory_QuotationResponseId(cursor.getString(22));
				dashboardHistory.setChat_UnitsOfMeasurement(cursor.getString(23));
				dashboardHistory.setmAttachment(cursor.getString(24));
				
				list.add(dashboardHistory);
				cursor.moveToNext();
			}
		}

		return list;

	}*/
	
	public ArrayList<QuotationDashboardHistory> getAllDashboardQuotationHistory(String mResponseType) {
		ArrayList<QuotationDashboardHistory> list = new ArrayList<QuotationDashboardHistory>();
		Cursor cursor = null;
		if(mResponseType == null || mResponseType.length() == 0){
			cursor = getData("select * from dashboard_quotationhistory");
		}else{
			cursor = getData("select * from dashboard_quotationhistory where chat_responseuser_type='"+mResponseType+"'");
		}
		if (cursor == null || cursor.getCount() == 0) {

		} else {
			cursor.moveToFirst();
			int count = cursor.getCount();
			for (int i = 0; i < count; i++) {
				QuotationDashboardHistory dashboardHistory = new QuotationDashboardHistory();
				dashboardHistory.setHistory_Buyername(cursor.getString(0));
				dashboardHistory.setHistory_Buyerimage(cursor.getString(1));
				dashboardHistory.setHistory_Manufacturename(cursor.getString(2));
				dashboardHistory.setHistory_Manufactureimage(cursor.getString(3));
				dashboardHistory.setHistory_productname(cursor.getString(4));
				dashboardHistory.setHistory_productimage(cursor.getString(5));
				dashboardHistory.setHistory_subject(cursor.getString(6));
				dashboardHistory.setHistory_createddate(cursor.getString(7));
				dashboardHistory.setHistory_id(cursor.getString(8));
				dashboardHistory.setHistory_isexternalproduct(cursor.getString(9));
				dashboardHistory.setHistory_LastManufacturerResponseId(cursor.getString(10));
				dashboardHistory.setChat_ResponseDate(cursor.getString(11));
				dashboardHistory.setHistory_LastResponseSequenceNumber(cursor.getString(12));
				dashboardHistory.setHistory_QuotationStatusId(cursor.getString(13));
				dashboardHistory.setChat_message(cursor.getString(14));
				dashboardHistory.setOffered_price(cursor.getString(15));
				dashboardHistory.setChat_RequiredQty(cursor.getString(16));
				dashboardHistory.setChat_ResponseUserType(cursor.getString(17));
				dashboardHistory.setmAdditionalInfo(cursor.getString(20));
				dashboardHistory.setHistory_QuotationResponseId(cursor.getString(21));
				dashboardHistory.setHistory_QuotationResponseId(cursor.getString(22));
				dashboardHistory.setChat_UnitsOfMeasurement(cursor.getString(23));
				dashboardHistory.setmAttachment(cursor.getString(24));
				dashboardHistory.setmShipping_DeliveryDate(cursor.getString(25));
				dashboardHistory.setmDimensionLwh(cursor.getString(26));
				dashboardHistory.setmDimensionWeight(cursor.getString(27));
				dashboardHistory.setmShipping_FromCity(cursor.getString(28));
				dashboardHistory.setmShipping_FromCountry(cursor.getString(29));
				dashboardHistory.setmShipping_ToCity(cursor.getString(30));
				dashboardHistory.setmShipping_ToCountry(cursor.getString(31));
				dashboardHistory.setmTotalNumberOfCatons(cursor.getString(32));
				dashboardHistory.setmTotalWeight(cursor.getString(33));
				dashboardHistory.setmInspectionType(cursor.getString(34));
				dashboardHistory.setmInspectorType(cursor.getString(35));
				//dashboardHistory.setmPlaceToInspect(cursor.getString(36));
				dashboardHistory.setmTypeOfIndustry(cursor.getString(37));
				dashboardHistory.setmWidth(cursor.getString(38));
				dashboardHistory.setmHeight(cursor.getString(39));
				dashboardHistory.setmLength(cursor.getString(40));
				dashboardHistory.setmWeight(cursor.getString(41));
				dashboardHistory.setmInspectionTypeId(cursor.getString(42));
				dashboardHistory.setmInspectorTypeId(cursor.getString(43));
				dashboardHistory.setmTypeOfIndustryId(cursor.getString(44));
				dashboardHistory.setmCity(cursor.getString(45));
				dashboardHistory.setmAddress(cursor.getString(46));
				dashboardHistory.setmCountryId(cursor.getString(47));
				dashboardHistory.setmCountryName(cursor.getString(48));
				dashboardHistory.setmStateId(cursor.getString(49));
				dashboardHistory.setmStateName(cursor.getString(50));
				dashboardHistory.setmCategoryId(cursor.getString(51));
				dashboardHistory.setmCategoryName(cursor.getString(52));
				dashboardHistory.setmSubCategoryId(cursor.getString(53));
				dashboardHistory.setmSubCategoryName(cursor.getString(54));
				dashboardHistory.setmQuantity(cursor.getString(55));
				dashboardHistory.setmCompanyName(cursor.getString(56));
				dashboardHistory.setmShipping_pickup(cursor.getString(57));
				list.add(dashboardHistory);
				cursor.moveToNext();
			}
		}

		return list;

	}
	
	public ArrayList<Milestones> getMilestonesByQuotationHistoryId(String QuoatationId){
        ArrayList<Milestones> mMilestones = null;
        Cursor cursor = null;
        cursor = getData("select * from milestones where quotaion_history_id='"+QuoatationId+"'");
        if(cursor == null || cursor.getCount() == 0){
        }else{
            mMilestones = new ArrayList<Milestones>();
            cursor.moveToFirst();
            int len = cursor.getCount();
            for(int i = 0; i<len; i++){
                Milestones ms = new Milestones();
                ms.setmQuotationHistoryId(cursor.getString(0));
                ms.setmName(cursor.getString(1));
                ms.setmBriefDescription(cursor.getString(2));
                ms.setmPercentBreakup(cursor.getString(3));
                mMilestones.add(ms);
                cursor.moveToNext();
            }
        }
        return mMilestones;
    }

	public long insertManufacturerAttributes(List<ManufacturerDetails> mManufacturerDetails,String mManufacturerId) {
		List<ProductSpecificationDetails> mManufacturerAttributes= new ArrayList<ProductSpecificationDetails>();
		SQLiteDatabase db = this.getWritableDatabase();
		int mTotalInsertedValues =0;
		try{
			openDataBase();
			Cursor cur = getData("select DISTINCT manufacturer_id from manufacturerAttributes_master");
			if(cur.getCount() > 9){
				cur.moveToFirst();
				String mfr_id = cur.getString(cur.getColumnIndex("manufacturer_id"));
				System.out.println("MFR ID "+mfr_id);
				db.execSQL("DELETE * FROM manufacturerAttributes_master WHERE manufacturer_id = '"+mfr_id+"'");
			}
			Log.e("Deleting manufacturerAttributes_master working", "Done");
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Deleting manufacturerAttributes_master working", "Error");
		}
		try {
			for (int i = 0; i < mManufacturerDetails.size(); i++) {
				ContentValues initialValues = new ContentValues();
				mManufacturerAttributes = mManufacturerDetails.get(i).getmManufactureAttributes();
				if (mManufacturerAttributes != null && mManufacturerAttributes.size() > 0) {
					for (ProductSpecificationDetails details : mManufacturerAttributes) {
						initialValues.put("manufacturer_id", mManufacturerId);
						initialValues.put("manufacturerattribute_name", details.getmProductAttribute_Name());
						initialValues.put("manufacturerattribute_value",details.getmProductAttribute_Value());
						long row = 0;
						row = insertData("manufacturerAttributes_master", initialValues);
						Log.e("insertion manufacturerAttributes_master Table","insertion manufacturerAttributes_master working");
						if (row != 0) {
							mTotalInsertedValues++;
						}
					}
				}
			}
			Log.e("Inserted manufacturerAttributes_master", "Rows "+ mTotalInsertedValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTotalInsertedValues;
		
	}

	public List<ProductSpecificationDetails> getManufacturerAttributes(String mManufacturerId) {
		Cursor cursor;
		List<ProductSpecificationDetails> mManufacturerAttributesList = new ArrayList<ProductSpecificationDetails>();
		cursor = getData("SELECT * FROM manufacturerAttributes_master where manufacturer_id='"+ mManufacturerId + "'");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int size = 0; size < cursor.getCount(); size++) {
				ProductSpecificationDetails mManufacturerAttributes = new ProductSpecificationDetails();
				mManufacturerAttributes.setmProduct_Id(cursor.getString(0));
				mManufacturerAttributes.setmProductAttribute_Name(cursor.getString(1));
				mManufacturerAttributes.setmProductAttribute_Value(cursor.getString(2));

				mManufacturerAttributesList.add(mManufacturerAttributes);
				cursor.moveToNext();
			}
			cursor.close();
			cursorGetData.close();
			myDataBase.close();
		}

		return mManufacturerAttributesList;
	}
	
	public void insertOrdersDetails(List<Orders> orderList){
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			openDataBase();
			db.execSQL("DELETE FROM orders_master");
			Log.e("Deleting orders_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting orders_master working", "Error");
		}
		try {
			for(Orders o : orderList){
				ContentValues initialValues = new ContentValues();
				initialValues.put("customer_id", ""+o.getmCustomerId());
				initialValues.put("channel_name", ""+o.getmChannelName());
				initialValues.put("channel_order_id", ""+o.getmChannelOrderId());
				initialValues.put("no_of_pending_orders", ""+o.getmNoOfPendindOrders());
				initialValues.put("order_date", ""+o.getmOrderDate());
				initialValues.put("order_number", ""+o.getmOrderNumber());
				initialValues.put("order_qty", ""+o.getmOrderQty());
				initialValues.put("status_id", ""+o.getmStatusId());
				initialValues.put("total_order_qty", ""+o.getmTotalOrderQty());
				initialValues.put("product_id", ""+o.getmProductId());
				initialValues.put("product_image", ""+o.getmProductImage());
				initialValues.put("product_name", ""+o.getmProductName());
				initialValues.put("product_weight", ""+o.getmProductWeight());
				initialValues.put("description", ""+o.getmDescription());
				initialValues.put("channel_id", ""+o.getmChannelId());
				initialValues.put("picture_url", ""+o.getmPictureURL());
				//initialValues.put("order", ""+o.getmOrder());
				initialValues.put("total_count",""+o.getmTotalCount());
				initialValues.put("warehouse_details",""+o.getmWarehouseDetails());
				long row = 0;
				row = insertData("orders_master", initialValues);
				Log.e("insertion orders_master Table",
						"insertion orders_master working");
				if (row != 0) {
					mTotalInsertedValues++;
				}
			}
			Log.e("Inserted orders_master", "Rows "+ mTotalInsertedValues);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Orders> getAllOrdersList(){
		ArrayList<Orders> ordersList = new ArrayList<Orders>();
		Cursor cursor = getData("select * from orders_master");
		
		if (cursor == null || cursor.getCount() == 0) {
		} else {
			cursor.moveToFirst();
			int count = cursor.getCount();
			for(int i=0; i<count; i++){
				Orders mOrders = new Orders();
				mOrders.setmCustomerId(cursor.getString(0));
				mOrders.setmChannelName(cursor.getString(1));
				mOrders.setmChannelOrderId(cursor.getString(2));
				mOrders.setmNoOfPendindOrders(cursor.getString(3));
				mOrders.setmOrderDate(cursor.getString(4));
				mOrders.setmOrderNumber(cursor.getString(5));
				mOrders.setmOrderQty(cursor.getString(6));
				mOrders.setmStatusId(cursor.getString(7));
				mOrders.setmTotalOrderQty(cursor.getString(8));
				mOrders.setmProductId(cursor.getString(9));
				mOrders.setmProductImage(cursor.getString(10));
				mOrders.setmProductName(cursor.getString(11));
				mOrders.setmProductWeight(cursor.getString(12));
				mOrders.setmDescription(cursor.getString(13));
				mOrders.setmChannelId(cursor.getString(14));
				mOrders.setmPictureURL(cursor.getString(15));
				mOrders.setmOrder(cursor.getString(16));
				mOrders.setmTotalCount(cursor.getString(17));
				mOrders.setmWarehouseDetails(cursor.getString(18));
				ordersList.add(mOrders);
				cursor.moveToNext();
			}
		}
		return ordersList;
	}
	
	public Orders getOrderDetailsByOrderNumber(String mOrderNumber){
		Orders mOrders = null;
		Cursor cursor = getData("select * from orders_master where order_number = '"+mOrderNumber+"'");
		if (cursor == null || cursor.getCount() == 0) {
		} else {
			cursor.moveToFirst();
			int count = cursor.getCount();
			for(int i=0; i<count; i++){
				mOrders = new Orders();
				mOrders.setmCustomerId(cursor.getString(0));
				mOrders.setmChannelName(cursor.getString(1));
				mOrders.setmChannelOrderId(cursor.getString(2));
				mOrders.setmNoOfPendindOrders(cursor.getString(3));
				mOrders.setmOrderDate(cursor.getString(4));
				mOrders.setmOrderNumber(cursor.getString(5));
				mOrders.setmOrderQty(cursor.getString(6));
				mOrders.setmStatusId(cursor.getString(7));
				mOrders.setmTotalOrderQty(cursor.getString(8));
				mOrders.setmProductId(cursor.getString(9));
				mOrders.setmProductImage(cursor.getString(10));
				mOrders.setmProductName(cursor.getString(11));
				mOrders.setmProductWeight(cursor.getString(12));
				mOrders.setmDescription(cursor.getString(13));
				mOrders.setmChannelId(cursor.getString(14));
				mOrders.setmPictureURL(cursor.getString(15));
				mOrders.setmOrder(cursor.getString(16));
				mOrders.setmTotalCount(cursor.getString(17));
				mOrders.setmWarehouseDetails(cursor.getString(18));
				cursor.moveToNext();
			}
		}
		return mOrders;
	}
	
	/*public void insertInventoryDetails(List<Inventory> inventoryList){
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			openDataBase();
			db.execSQL("DELETE FROM inventory_master");
			Log.e("Deleting inventory_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting inventory_master working", "Error");
		}
		try {
			for(Inventory i:inventoryList){
				ContentValues initialValues = new ContentValues();
				initialValues.put("customer_id", ""+i.getmCustomerId());
				initialValues.put("buyer", ""+i.getmBuyer());
				initialValues.put("manufacturer", ""+i.getmManufacturer());
				initialValues.put("offered_price", ""+i.getmOfferedPrice());
				initialValues.put("order_date", ""+i.getmOrderDate());
				initialValues.put("order_id", ""+i.getmOrderId());
				initialValues.put("order_number", ""+i.getmOrderNumber());
				initialValues.put("order_qty", ""+i.getmOrderQuantity());
				initialValues.put("order_status_id",""+i.getmOrderStatusId());
				initialValues.put("product_id", ""+i.getmProductId());
				initialValues.put("product_image", ""+i.getmProductImage());
				initialValues.put("product_name", ""+i.getmProductName());
				initialValues.put("product_weight", ""+i.getmProductWeight());
				initialValues.put("sub_total", ""+i.getmSubTotal());
				initialValues.put("total_count",""+i.getmTotalCount());
				initialValues.put("units_of_measurement",""+i.getmUnitsOfMeasurement());
				
				long row = 0;
				row = insertData("inventory_master", initialValues);
				Log.e("insertion inventory_master Table",
						"insertion inventory_master working");
				if (row != 0) {
					mTotalInsertedValues++;
				}
			}
			Log.e("Inserted inventory_master", "Rows "+ mTotalInsertedValues);
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
	/*public List<Inventory> getAllInventoryList(){
		ArrayList<Inventory> mInventoriesList = null;
		Cursor cursor = getData("select * from inventory_master");
		if (cursor == null || cursor.getCount() == 0) {
		} else {
			mInventoriesList = new ArrayList<Inventory>();
			cursor.moveToFirst();
			int count = cursor.getCount();
			for(int i=0; i<count; i++){
				Inventory mInventory = new Inventory();
				mInventory.setmCustomerId(cursor.getString(0));
				mInventory.setmBuyer(cursor.getString(1));
				mInventory.setmManufacturer(cursor.getString(2));
				mInventory.setmOfferedPrice(cursor.getString(3));
				mInventory.setmOrderDate(cursor.getString(4));
				mInventory.setmOrderId(cursor.getString(5));
				mInventory.setmOrderNumber(cursor.getString(6));
				mInventory.setmOrderQuantity(cursor.getString(7));
				mInventory.setmOrderStatusId(cursor.getString(8));
				mInventory.setmProductId(cursor.getString(9));
				mInventory.setmProductImage(cursor.getString(10));
				mInventory.setmProductName(cursor.getString(11));
				mInventory.setmProductWeight(cursor.getString(12));
				mInventory.setmSubTotal(cursor.getString(13));
				mInventory.setmTotalCount(cursor.getString(14));
				mInventory.setmUnitsOfMeasurement(cursor.getString(15));
				mInventoriesList.add(mInventory);
				cursor.moveToNext();
			}
		}
		return mInventoriesList;
	}*/
	
	/*public Inventory getInventoryDetailsByOrderId(String mOrderId){
		Inventory mInventory = null;
		
		Cursor cursor = getData("select * from inventory_master where order_number='"+mOrderId+"'");
		
		if (cursor == null || cursor.getCount() == 0) {
		} else {
			cursor.moveToFirst();
			int count = cursor.getCount();
			for(int i=0; i<count; i++){
				mInventory = new Inventory();
				mInventory.setmCustomerId(cursor.getString(0));
				mInventory.setmBuyer(cursor.getString(1));
				mInventory.setmManufacturer(cursor.getString(2));
				mInventory.setmOfferedPrice(cursor.getString(3));
				mInventory.setmOrderDate(cursor.getString(4));
				mInventory.setmOrderId(cursor.getString(5));
				mInventory.setmOrderNumber(cursor.getString(6));
				mInventory.setmOrderQuantity(cursor.getString(7));
				mInventory.setmOrderStatusId(cursor.getString(8));
				mInventory.setmProductId(cursor.getString(9));
				mInventory.setmProductImage(cursor.getString(10));
				mInventory.setmProductName(cursor.getString(11));
				mInventory.setmProductWeight(cursor.getString(12));
				mInventory.setmSubTotal(cursor.getString(13));
				mInventory.setmTotalCount(cursor.getString(14));
				mInventory.setmUnitsOfMeasurement(cursor.getString(15));
				cursor.moveToNext();
			}
		}
		return mInventory;
	}*/
	
	
	/*public void insertChannelDetails(List<Channel> channelList){
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			openDataBase();
			db.execSQL("DELETE FROM channel_master");
			Log.e("Deleting channel_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting channel_master working", "Error");
		}
		try {
			for(Channel chn : channelList){
				ContentValues initialValues = new ContentValues();
				initialValues.put("channel_id", ""+chn.getmChannelId());
				initialValues.put("channel_name", ""+chn.getmChannelName());
				initialValues.put("channel_order", ""+chn.getmChannelOrder());
				initialValues.put("description", ""+chn.getmDescription());
				initialValues.put("picture_url", ""+chn.getmPictureURL());
				
				long row = 0;
				row = insertData("channel_master", initialValues);
				Log.e("insertion channel_master Table","insertion channel_master working");
				if (row != 0) {
					mTotalInsertedValues++;
				}
			}
			Log.e("Inserted channel_master", "Rows "+ mTotalInsertedValues);
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
	/*public List<Channel> getAllChannels(String mChannelId){
		List<Channel> channelsList = null;
		
		Cursor cursor = null;
		if(mChannelId == null || mChannelId.length() == 0){
			cursor = getData("select * from channel_master");
		}else{
			cursor = getData("select * from channel_master where channel_id = '"+mChannelId+"'");
		}
		if (cursor == null || cursor.getCount() == 0) {
		} else {
			channelsList = new ArrayList<Channel>();
			cursor.moveToFirst();
			int count = cursor.getCount();
			for(int i=0; i<count; i++){
				Channel channel = new Channel();
				channel.setmChannelId(cursor.getString(0));
				channel.setmChannelName(cursor.getString(1));
				channel.setmChannelOrder(cursor.getString(2));
				channel.setmDescription(cursor.getString(3));
				channel.setmPictureURL(cursor.getString(4));
				channelsList.add(channel);
				cursor.moveToNext();
			}
		}
		return channelsList;
	}*/

	/*public void insertWarehouseDetails(List<Warehouse> warehouseList){
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			openDataBase();
			db.execSQL("DELETE FROM warehouse_master");
			Log.e("Deleting warehouse_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting warehouse_master working", "Error");
		}
		try {
			for(Warehouse w:warehouseList){
				ContentValues initialValues = new ContentValues();
				initialValues.put("warehouse_id", ""+w.getmWarehouseId());
				initialValues.put("warehouse_name", ""+w.getmWarehouseName());
				initialValues.put("isown_warehouse", ""+w.getmIsOwnWarehouse());
				initialValues.put("address", ""+w.getmAddress());
				initialValues.put("product", ""+w.getmProduct());
				initialValues.put("stock_quantity", ""+w.getmStockQuantity());
				initialValues.put("total_warehouse_inventory", ""+w.getmTotalWarehouseInventory());
				initialValues.put("warehouse_inventory_id", ""+w.getmWarehouseInventoryId());
								
				long row = 0;
				row = insertData("warehouse_master", initialValues);
				Log.e("insertion warehouse_master Table",
						"insertion warehouse_master working");
				if (row != 0) {
					mTotalInsertedValues++;
				}
			}
			Log.e("Inserted warehouse_master", "Rows "+ mTotalInsertedValues);
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
	/*public ArrayList<Warehouse> getAllWarehouse(){
		ArrayList<Warehouse> mWarehouseList = null;
		Cursor cursor = null;
		cursor = getData("select * from warehouse_master");
		if(cursor == null || cursor.getCount() == 0){
		}else{
			mWarehouseList = new ArrayList<Warehouse>();
			cursor.moveToFirst();
			int len = cursor.getCount();
			for(int i = 0; i<len; i++){
				Warehouse wh = new Warehouse();
				wh.setmWarehouseId(cursor.getString(0));
				wh.setmWarehouseName(cursor.getString(1));
				wh.setmIsOwnWarehouse(cursor.getString(2));
				wh.setmAddress(cursor.getString(3));
				wh.setmProduct(cursor.getString(4));
				wh.setmStockQuantity(cursor.getString(5));
				wh.setmTotalWarehouseInventory(cursor.getString(6));
				wh.setmWarehouseInventoryId(cursor.getString(7));
				mWarehouseList.add(wh);
				cursor.moveToNext();
			}
		}
		return mWarehouseList;
	}*/
	
	/*public Warehouse getWarehouseById(String mWarehouseId){
		Warehouse mWarehouse = null;
		Cursor cursor = null;
		cursor = getData("select * from warehouse_master where warehouse_id = '"+mWarehouseId+"'");
		if(cursor == null || cursor.getCount() == 0){
		}else{
			mWarehouse = new Warehouse();
			cursor.moveToFirst();
			int len = cursor.getCount();
			for(int i = 0; i<len; i++){
				Warehouse wh = new Warehouse();
				wh.setmWarehouseId(cursor.getString(0));
				wh.setmWarehouseName(cursor.getString(1));
				wh.setmIsOwnWarehouse(cursor.getString(2));
				wh.setmAddress(cursor.getString(3));
				wh.setmProduct(cursor.getString(4));
				wh.setmStockQuantity(cursor.getString(5));
				wh.setmTotalWarehouseInventory(cursor.getString(6));
				wh.setmWarehouseInventoryId(cursor.getString(7));
				cursor.moveToNext();
			}
		}
		return mWarehouse;
	}*/

	public void insertCountries(List<CountryDetails> mCountriesList) {
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		try {
			openDataBase();
			db.execSQL("DELETE FROM country_master");
			Log.e("Deleting country_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting country_master working", "Error");
		}
		try {
			for(CountryDetails country:mCountriesList){
				ContentValues initialValues = new ContentValues();
				initialValues.put("cuontry_id", ""+country.getmCountryId());
				initialValues.put("cuontry_name", ""+country.getmCountryName());
				initialValues.put("lastdownload_time", dateTime);
								
				long row = 0;
				row = insertData("country_master", initialValues);
				Log.e("insertion country_master Table","insertion country_master working");
				if (row != 0) {
					mTotalInsertedValues++;
				}
			}
			Log.e("Inserted country_master", "Rows "+ mTotalInsertedValues);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public List<CountryDetails> getAllCountries() {
		ArrayList<CountryDetails> mCountriesList = new ArrayList<CountryDetails>();
		Cursor cursor = null;
		cursor = getData("select * from country_master");
		if(cursor == null || cursor.getCount() == 0){
		}else{
			cursor.moveToFirst();
			int len = cursor.getCount();
			for(int i = 0; i<len; i++){
				CountryDetails country = new CountryDetails();
				country.setmCountryId(cursor.getString(0));
				country.setmCountryName(cursor.getString(1));
				country.setmLastDownloadTime(cursor.getString(2));
			
				mCountriesList.add(country);
				cursor.moveToNext();
			}
		}
		return mCountriesList;
	}

	
	public void insertStates(List<CountryDetails> mStatesList) {
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		try {
			openDataBase();
			db.execSQL("DELETE FROM state_master");
			Log.e("Deleting state_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting state_master working", "Error");
		}
		try {
			for(CountryDetails states:mStatesList){
				ContentValues initialValues = new ContentValues();
				initialValues.put("country_id", ""+states.getmCountryId());
				initialValues.put("state_id", ""+states.getmStateId());
				initialValues.put("state_name", ""+states.getmStateName());
				initialValues.put("lastdownload_time", dateTime);
								
				long row = 0;
				row = insertData("state_master", initialValues);
				Log.e("insertion state_master Table","insertion state_master working");
				if (row != 0) {
					mTotalInsertedValues++;
				}
			}
			Log.e("Inserted state_master", "Rows "+ mTotalInsertedValues);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<CountryDetails> getAllStates() {
		ArrayList<CountryDetails> mStatesList = new ArrayList<CountryDetails>();
		Cursor cursor = null;
		cursor = getData("select * from state_master");
		if(cursor == null || cursor.getCount() == 0){
		}else{
			cursor.moveToFirst();
			int len = cursor.getCount();
			for(int i = 0; i<len; i++){
				CountryDetails state = new CountryDetails();
				state.setmCountryId(cursor.getString(0));
				state.setmStateId(cursor.getString(1));
				state.setmStateName(cursor.getString(2));
				state.setmLastDownloadTime(cursor.getString(3));
			
				mStatesList.add(state);
				cursor.moveToNext();
			}
		}
		return mStatesList;
	}
	
	public List<CountryDetails> getStateWithId(String mCounterId) {
		ArrayList<CountryDetails> mStatesList = new ArrayList<CountryDetails>();
		Cursor cursor = null;
		cursor = getData("select * from state_master where country_id='"+mCounterId+"'");
		if(cursor == null || cursor.getCount() == 0){
		}else{
			cursor.moveToFirst();
			int len = cursor.getCount();
			for(int i = 0; i<len; i++){
				CountryDetails state = new CountryDetails();
				state.setmCountryId(cursor.getString(0));
				state.setmStateId(cursor.getString(1));
				state.setmStateName(cursor.getString(2));
				state.setmLastDownloadTime(cursor.getString(3));
			
				mStatesList.add(state);
				cursor.moveToNext();
			}
		}
		return mStatesList;
	}

	public void insertUnitOfMeasurementList(List<Lookups> mUnitOfMeasurement) {
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		try {
			openDataBase();
			db.execSQL("DELETE FROM unitofmeasurement_master");
			Log.e("Deleting unitofmeasurement_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting unitofmeasurement_master working", "Error");
		}
		try {
			for(Lookups unitofmeasurement:mUnitOfMeasurement){
				ContentValues initialValues = new ContentValues();
				initialValues.put("name", ""+unitofmeasurement.getmName());
				initialValues.put("id", ""+unitofmeasurement.getmId());
				initialValues.put("lastdownloadtime", dateTime);
				long row = 0;
				row = insertData("unitofmeasurement_master", initialValues);
				Log.e("insertion unitofmeasurement_master Table","insertion unitofmeasurement_master working");
				if (row != 0) {
					mTotalInsertedValues++;
				}
			}
			Log.e("Inserted unitofmeasurement_master", "Rows "+ mTotalInsertedValues);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Lookups> getUnitOfMeasurentList() {
		ArrayList<Lookups> mUnitOfMeasurementList = new ArrayList<Lookups>();
		Cursor cursor = null;
		cursor = getData("select * from unitofmeasurement_master");
		if(cursor == null || cursor.getCount() == 0){
		}else{
			cursor.moveToFirst();
			int len = cursor.getCount();
			for(int i = 0; i<len; i++){
				Lookups unitofmeasurement = new Lookups();
				unitofmeasurement.setmName(cursor.getString(0));
				unitofmeasurement.setmId(cursor.getString(1));
				unitofmeasurement.setmLastDownloadTime(cursor.getString(2));
			
				mUnitOfMeasurementList.add(unitofmeasurement);
				cursor.moveToNext();
			}
		}
		return mUnitOfMeasurementList;
	}
	
	public void insertQuotationLookupList(List<Lookups> mQuotationLookupList) {
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		try {
			openDataBase();
			db.execSQL("DELETE FROM quotationlookup_master");
			Log.e("Deleting quotationlookup_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting quotationlookup_master working", "Error");
		}
		try {
			for(Lookups quotationlookup:mQuotationLookupList){
				ContentValues initialValues = new ContentValues();
				initialValues.put("name", ""+quotationlookup.getmName());
				initialValues.put("id", ""+quotationlookup.getmId());
				initialValues.put("lastdownloadtime", dateTime);
				long row = 0;
				row = insertData("quotationlookup_master", initialValues);
				Log.e("insertion quotationlookup_master Table","insertion quotationlookup_master working");
				if (row != 0) {
					mTotalInsertedValues++;
				}
			}
			Log.e("Inserted quotationlookup_master", "Rows "+ mTotalInsertedValues);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Lookups> getQuotationLookupList() {
		ArrayList<Lookups> mQuotationLookupList = new ArrayList<Lookups>();
		Cursor cursor = null;
		cursor = getData("select * from quotationlookup_master");
		if(cursor == null || cursor.getCount() == 0){
		}else{
			cursor.moveToFirst();
			int len = cursor.getCount();
			for(int i = 0; i<len; i++){
				Lookups quotationlookup = new Lookups();
				quotationlookup.setmName(cursor.getString(0));
				quotationlookup.setmId(cursor.getString(1));
				quotationlookup.setmLastDownloadTime(cursor.getString(2));
			
				mQuotationLookupList.add(quotationlookup);
				cursor.moveToNext();
			}
		}
		return mQuotationLookupList;
	}
	
	public void insertMoreinfoSubjectList(List<Lookups> mMoreinfoSubjectList) {
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		try {
			openDataBase();
			db.execSQL("DELETE FROM moreinfosubject_master");
			Log.e("Deleting moreinfosubject_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting moreinfosubject_master working", "Error");
		}
		try {
			for(Lookups quotationlookup:mMoreinfoSubjectList){
				ContentValues initialValues = new ContentValues();
				initialValues.put("name", ""+quotationlookup.getmName());
				initialValues.put("id", ""+quotationlookup.getmId());
				initialValues.put("lastdownloadtime", dateTime);
				long row = 0;
				row = insertData("moreinfosubject_master", initialValues);
				Log.e("insertion moreinfosubject_master Table","insertion moreinfosubject_master working");
				if (row != 0) {
					mTotalInsertedValues++;
				}
			}
			Log.e("Inserted moreinfosubject_master", "Rows "+ mTotalInsertedValues);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Lookups> getMoreinfoSubjectList() {
		ArrayList<Lookups> mMoreinfoSubjectList = new ArrayList<Lookups>();
		Cursor cursor = null;
		cursor = getData("select * from moreinfosubject_master");
		if(cursor == null || cursor.getCount() == 0){
		}else{
			cursor.moveToFirst();
			int len = cursor.getCount();
			for(int i = 0; i<len; i++){
				Lookups subjectlookup = new Lookups();
				subjectlookup.setmName(cursor.getString(0));
				subjectlookup.setmId(cursor.getString(1));
				subjectlookup.setmLastDownloadTime(cursor.getString(2));
			
				mMoreinfoSubjectList.add(subjectlookup);
				cursor.moveToNext();
			}
		}
		return mMoreinfoSubjectList;
	}
	
	public void insertMoreinfoLookupList(List<Lookups> mMoreinfoSubjectList) {
		int mTotalInsertedValues = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		String dateStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		SimpleDateFormat timeStamp= new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
		String time=timeStamp.format(System.currentTimeMillis());
		String dateTime=dateStamp+" "+time;
		System.out.println(""+dateTime);
		try {
			openDataBase();
			db.execSQL("DELETE FROM moreinfolookup_master");
			Log.e("Deleting moreinfolookup_master working", "Done");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Deleting moreinfolookup_master working", "Error");
		}
		try {
			for(Lookups quotationlookup:mMoreinfoSubjectList){
				ContentValues initialValues = new ContentValues();
				initialValues.put("name", ""+quotationlookup.getmName());
				initialValues.put("id", ""+quotationlookup.getmId());
				initialValues.put("lastdownloadtime", dateTime);
				long row = 0;
				row = insertData("moreinfolookup_master", initialValues);
				Log.e("insertion moreinfolookup_master Table","insertion moreinfolookup_master working");
				if (row != 0) {
					mTotalInsertedValues++;
				}
			}
			Log.e("Inserted moreinfolookup_master", "Rows "+ mTotalInsertedValues);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Lookups> getMoreinfoLookupList() {
		ArrayList<Lookups> mMoreinfoLookupList = new ArrayList<Lookups>();
		Cursor cursor = null;
		cursor = getData("select * from moreinfolookup_master");
		if(cursor == null || cursor.getCount() == 0){
		}else{
			cursor.moveToFirst();
			int len = cursor.getCount();
			for(int i = 0; i<len; i++){
				Lookups moreinfolookup = new Lookups();
				moreinfolookup.setmName(cursor.getString(0));
				moreinfolookup.setmId(cursor.getString(1));
				moreinfolookup.setmLastDownloadTime(cursor.getString(2));
			
				mMoreinfoLookupList.add(moreinfolookup);
				cursor.moveToNext();
			}
		}
		return mMoreinfoLookupList;
	}

	

}
