package com.mmadapps.verisupplier.beans;

public class Channel {

	private String mChannelId;
	private String mChannelName;
	private String mAddress;
	private String mPhoneNo;
	private String mEmail;
	private String mPictureURL;
	private Boolean isActive=false;
	
	public String getmChannelId() {
		return mChannelId;
	}
	public void setmChannelId(String mChannelId) {
		this.mChannelId = mChannelId;
	}
	public String getmChannelName() {
		return mChannelName;
	}
	public void setmChannelName(String mChannelName) {
		this.mChannelName = mChannelName;
	}
	public String getmAddress() {
		return mAddress;
	}
	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}
	public String getmPhoneNo() {
		return mPhoneNo;
	}
	public void setmPhoneNo(String mPhoneNo) {
		this.mPhoneNo = mPhoneNo;
	}
	public String getmEmail() {
		return mEmail;
	}
	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}
	public String getmPictureURL() {
		return mPictureURL;
	}
	public void setmPictureURL(String mPictureURL) {
		this.mPictureURL = mPictureURL;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	

}
