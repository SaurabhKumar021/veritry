package com.mmadapps.verisupplier.beans;

import java.util.List;

public class ManufacturerDetails {
	
	private String mManufacturerName;
	private String mYearEstablished;
	private String mEmployeeCount;
	private String mBusinessType;
	private String mManufacturerLogo;
	
	//private List<ProductSpecificationDetails> mTradeDetails;
	//private List<AdditionalDescription> mAddressDetails;
	//private List<AdditionalDescription> mEmailDetails;
	
	private String mTradeDetail;
	private String mAddressDetails;
	private String mEmailDetails;
	private String mPhoneDetails;
	private String mFaxDetails;
	private String mWebsiteDetails;
	private String mManufactureID;
	private String mIsActive;
	private String mFeedback;
	private String mLikes;
	private List<ProductSpecificationDetails> mManufactureAttributes;
	private String mShortDescription;
	
	public String getmShortDescription() {
		return mShortDescription;
	}
	public void setmShortDescription(String mShortDescription) {
		this.mShortDescription = mShortDescription;
	}
	public List<ProductSpecificationDetails> getmManufactureAttributes() {
		return mManufactureAttributes;
	}
	public void setmManufactureAttributes(
			List<ProductSpecificationDetails> mManufactureAttributes) {
		this.mManufactureAttributes = mManufactureAttributes;
	}
	public String getmLikes() {
		return mLikes;
	}
	public void setmLikes(String mLikes) {
		this.mLikes = mLikes;
	}
	public String getmFeedback() {
		return mFeedback;
	}
	public void setmFeedback(String mFeedback) {
		this.mFeedback = mFeedback;
	}
	public String getmIsActive() {
		return mIsActive;
	}
	public void setmIsActive(String mIsActive) {
		this.mIsActive = mIsActive;
	}
	public String getmManufactureID() {
		return mManufactureID;
	}
	public void setmManufactureID(String mManufactureID) {
		this.mManufactureID = mManufactureID;
	}
	private List<ManufacturerProductDetails> mProductDetails;
	
	
	public String getmManufacturerLogo() {
		return mManufacturerLogo;
	}
	public void setmManufacturerLogo(String mManufacturerLogo) {
		this.mManufacturerLogo = mManufacturerLogo;
	}
	public List<ManufacturerProductDetails> getmProductDetails() {
		return mProductDetails;
	}
	public void setmProductDetails(List<ManufacturerProductDetails> mProductDetails) {
		this.mProductDetails = mProductDetails;
	}
	public String getmAddressDetails() {
		return mAddressDetails;
	}
	public void setmAddressDetails(String mAddressDetails) {
		this.mAddressDetails = mAddressDetails;
	}
	public String getmEmailDetails() {
		return mEmailDetails;
	}
	public void setmEmailDetails(String mEmailDetails) {
		this.mEmailDetails = mEmailDetails;
	}
	public String getmPhoneDetails() {
		return mPhoneDetails;
	}
	public void setmPhoneDetails(String mPhoneDetails) {
		this.mPhoneDetails = mPhoneDetails;
	}
	public String getmFaxDetails() {
		return mFaxDetails;
	}
	public void setmFaxDetails(String mFaxDetails) {
		this.mFaxDetails = mFaxDetails;
	}
	public String getmWebsiteDetails() {
		return mWebsiteDetails;
	}
	public void setmWebsiteDetails(String mWebsiteDetails) {
		this.mWebsiteDetails = mWebsiteDetails;
	}
	public String getmTradeDetail() {
		return mTradeDetail;
	}
	public void setmTradeDetail(String mTradeDetail) {
		this.mTradeDetail = mTradeDetail;
	}
	/*public List<AdditionalDescription> getmAddressDetails() {
		return mAddressDetails;
	}
	public void setmAddressDetails(List<AdditionalDescription> mAddressDetails) {
		this.mAddressDetails = mAddressDetails;
	}
	public List<AdditionalDescription> getmEmailDetails() {
		return mEmailDetails;
	}
	public void setmEmailDetails(List<AdditionalDescription> mEmailDetails) {
		this.mEmailDetails = mEmailDetails;
	}*/
	
	public String getmManufacturerName() {
		return mManufacturerName;
	}
	public void setmManufacturerName(String mManufacturerName) {
		this.mManufacturerName = mManufacturerName;
	}
	public String getmYearEstablished() {
		return mYearEstablished;
	}
	public void setmYearEstablished(String mYearEstablished) {
		this.mYearEstablished = mYearEstablished;
	}
	public String getmEmployeeCount() {
		return mEmployeeCount;
	}
	public void setmEmployeeCount(String mEmployeeCount) {
		this.mEmployeeCount = mEmployeeCount;
	}
	public String getmBusinessType() {
		return mBusinessType;
	}
	public void setmBusinessType(String mBusinessType) {
		this.mBusinessType = mBusinessType;
	}

}
