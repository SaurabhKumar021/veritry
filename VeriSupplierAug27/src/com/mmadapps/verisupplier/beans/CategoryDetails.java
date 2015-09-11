package com.mmadapps.verisupplier.beans;

public class CategoryDetails {
	
	private String mCategory_Id;
	private String mCategory_Name;
	private String mCategory_Image;
	private String mCategory_ParentId;
	private String mLastDownloadDateTime;
	
	public String getmLastDownloadDateTime() {
		return mLastDownloadDateTime;
	}
	public void setmLastDownloadDateTime(String mLastDownloadDateTime) {
		this.mLastDownloadDateTime = mLastDownloadDateTime;
	}
	public String getmCategory_Id() {
		return mCategory_Id;
	}
	public void setmCategory_Id(String mCategory_Id) {
		this.mCategory_Id = mCategory_Id;
	}
	public String getmCategory_Name() {
		return mCategory_Name;
	}
	public void setmCategory_Name(String mCategory_Name) {
		this.mCategory_Name = mCategory_Name;
	}
	public String getmCategory_Image() {
		return mCategory_Image;
	}
	public void setmCategory_Image(String mCategory_Image) {
		this.mCategory_Image = mCategory_Image;
	}
	public String getmCategory_ParentId() {
		return mCategory_ParentId;
	}
	public void setmCategory_ParentId(String mCategory_ParentId) {
		this.mCategory_ParentId = mCategory_ParentId;
	}

}
