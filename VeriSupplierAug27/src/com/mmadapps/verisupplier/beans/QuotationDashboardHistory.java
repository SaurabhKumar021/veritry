package com.mmadapps.verisupplier.beans;

import java.util.List;

public class QuotationDashboardHistory {

	// Buyer Object Parameters
	String history_Buyername;
	String history_Buyerimage;

	// Manufacture Object Parameters
	String history_Manufacturename;
	String history_Manufactureimage;

	// Product Object Parameters
	String history_productname;
	String history_productimage;

	// Outside Parameters
	String history_subject;
	String history_createddate;
	String history_id;
	String history_isexternalproduct;
	String history_LastManufacturerResponseId;
	String history_LastResponseDate;
	String history_LastResponseSequenceNumber;
	String history_QuotationStatusId;
	String history_QuotationResponseId;
	String historyLastManufacturerResponseId;
	String history_Quotation_Number;
	int history_Service_Grouptype;
	String history_Shipping_Address_id;
	String history_Billing_Address_id;

	// Shipping Parameters
	String mShipping_DeliveryDate;
	String mShipping_FromCity;
	String mShipping_FromCountry;
	String mShipping_ToCity;
	String mShipping_ToCountry;
	private String mShipping_pickup;

	// Common Parameters
	String mDimensionLwh;
	String mDimensionWeight;
	String mTotalNumberOfCatons;
	String mTotalWeight;

	// Inspection Paramentes
	private String mInspectionTypeId;
	private String mInspectorTypeId;
	//private String mPlaceToInspectId;
	private String mTypeOfIndustryId;
	private String mInspectionType;
	private String mInspectorType;
	private String mTypeOfIndustry;
	
	private String mStateId;
	private String mStateName;
	private String mCountryId;
	private String mCountryName;
	private String mCategoryId;
	private String mCategoryName;
	private String mSubCategoryId;
	private String mSubCategoryName;
	private String mQuantity;
	private String mCompanyName;
	
	private String mOrderId;
	private String mOrderNumber;
	
	public String getmOrderId() {
		return mOrderId;
	}

	public void setmOrderId(String mOrderId) {
		this.mOrderId = mOrderId;
	}

	public String getmOrderNumber() {
		return mOrderNumber;
	}

	public void setmOrderNumber(String mOrderNumber) {
		this.mOrderNumber = mOrderNumber;
	}

	public String getmShipping_pickup() {
		return mShipping_pickup;
	}

	public void setmShipping_pickup(String mShipping_pickup) {
		this.mShipping_pickup = mShipping_pickup;
	}

	public String getmCompanyName() {
		return mCompanyName;
	}

	public void setmCompanyName(String mCompanyName) {
		this.mCompanyName = mCompanyName;
	}

	public String getmQuantity() {
		return mQuantity;
	}

	public void setmQuantity(String mQuantity) {
		this.mQuantity = mQuantity;
	}

	private String mCity;
	private String mAddress;
	
	public String getmCategoryId() {
		return mCategoryId;
	}

	public void setmCategoryId(String mCategoryId) {
		this.mCategoryId = mCategoryId;
	}

	public String getmCategoryName() {
		return mCategoryName;
	}

	public void setmCategoryName(String mCategoryName) {
		this.mCategoryName = mCategoryName;
	}

	public String getmSubCategoryId() {
		return mSubCategoryId;
	}

	public void setmSubCategoryId(String mSubCategoryId) {
		this.mSubCategoryId = mSubCategoryId;
	}

	public String getmSubCategoryName() {
		return mSubCategoryName;
	}

	public void setmSubCategoryName(String mSubCategoryName) {
		this.mSubCategoryName = mSubCategoryName;
	}

	public String getmCity() {
		return mCity;
	}

	public void setmCity(String mCity) {
		this.mCity = mCity;
	}

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getmStateId() {
		return mStateId;
	}

	public void setmStateId(String mStateId) {
		this.mStateId = mStateId;
	}

	public String getmStateName() {
		return mStateName;
	}

	public void setmStateName(String mStateName) {
		this.mStateName = mStateName;
	}

	public String getmCountryId() {
		return mCountryId;
	}

	public void setmCountryId(String mCountryId) {
		this.mCountryId = mCountryId;
	}

	public String getmCountryName() {
		return mCountryName;
	}

	public void setmCountryName(String mCountryName) {
		this.mCountryName = mCountryName;
	}

	public String getmInspectionTypeId() {
		return mInspectionTypeId;
	}

	public void setmInspectionTypeId(String mInspectionTypeId) {
		this.mInspectionTypeId = mInspectionTypeId;
	}

	public String getmInspectorTypeId() {
		return mInspectorTypeId;
	}

	public void setmInspectorTypeId(String mInspectorTypeId) {
		this.mInspectorTypeId = mInspectorTypeId;
	}

	

	public String getmTypeOfIndustryId() {
		return mTypeOfIndustryId;
	}

	public void setmTypeOfIndustryId(String mTypeOfIndustryId) {
		this.mTypeOfIndustryId = mTypeOfIndustryId;
	}

	public String getmInspectionType() {
		return mInspectionType;
	}

	public void setmInspectionType(String mInspectionType) {
		this.mInspectionType = mInspectionType;
	}

	public String getmInspectorType() {
		return mInspectorType;
	}

	public void setmInspectorType(String mInspectorType) {
		this.mInspectorType = mInspectorType;
	}


	public String getmTypeOfIndustry() {
		return mTypeOfIndustry;
	}

	public void setmTypeOfIndustry(String mTypeOfIndustry) {
		this.mTypeOfIndustry = mTypeOfIndustry;
	}

	String mHeight;
	String mWidth;
	String mLength;
	String mWeight;

	public String getmHeight() {
		return mHeight;
	}

	public void setmHeight(String mHeight) {
		this.mHeight = mHeight;
	}

	public String getmWidth() {
		return mWidth;
	}

	public void setmWidth(String mWidth) {
		this.mWidth = mWidth;
	}

	public String getmLength() {
		return mLength;
	}

	public void setmLength(String mLength) {
		this.mLength = mLength;
	}

	public String getmWeight() {
		return mWeight;
	}

	public void setmWeight(String mWeight) {
		this.mWeight = mWeight;
	}

	public String getmShipping_DeliveryDate() {
		return mShipping_DeliveryDate;
	}

	public void setmShipping_DeliveryDate(String mShipping_DeliveryDate) {
		this.mShipping_DeliveryDate = mShipping_DeliveryDate;
	}

	public String getmShipping_FromCity() {
		return mShipping_FromCity;
	}

	public void setmShipping_FromCity(String mShipping_FromCity) {
		this.mShipping_FromCity = mShipping_FromCity;
	}

	public String getmShipping_FromCountry() {
		return mShipping_FromCountry;
	}

	public void setmShipping_FromCountry(String mShipping_FromCountry) {
		this.mShipping_FromCountry = mShipping_FromCountry;
	}

	public String getmShipping_ToCity() {
		return mShipping_ToCity;
	}

	public void setmShipping_ToCity(String mShipping_ToCity) {
		this.mShipping_ToCity = mShipping_ToCity;
	}

	public String getmShipping_ToCountry() {
		return mShipping_ToCountry;
	}

	public void setmShipping_ToCountry(String mShipping_ToCountry) {
		this.mShipping_ToCountry = mShipping_ToCountry;
	}

	public String getmDimensionLwh() {
		return mDimensionLwh;
	}

	public void setmDimensionLwh(String mDimensionLwh) {
		this.mDimensionLwh = mDimensionLwh;
	}

	public String getmDimensionWeight() {
		return mDimensionWeight;
	}

	public void setmDimensionWeight(String mDimensionWeight) {
		this.mDimensionWeight = mDimensionWeight;
	}

	public String getmTotalNumberOfCatons() {
		return mTotalNumberOfCatons;
	}

	public void setmTotalNumberOfCatons(String mTotalNumberOfCatons) {
		this.mTotalNumberOfCatons = mTotalNumberOfCatons;
	}

	public String getmTotalWeight() {
		return mTotalWeight;
	}

	public void setmTotalWeight(String mTotalWeight) {
		this.mTotalWeight = mTotalWeight;
	}

	public String getHistory_Quotation_Number() {
		return history_Quotation_Number;
	}

	public void setHistory_Quotation_Number(String history_Quotation_Number) {
		this.history_Quotation_Number = history_Quotation_Number;
	}

	public int getHistory_Service_Grouptype() {
		return history_Service_Grouptype;
	}

	public void setHistory_Service_Grouptype(int history_Service_Grouptype) {
		this.history_Service_Grouptype = history_Service_Grouptype;
	}

	public String getHistory_Shipping_Address_id() {
		return history_Shipping_Address_id;
	}

	public void setHistory_Shipping_Address_id(
			String history_Shipping_Address_id) {
		this.history_Shipping_Address_id = history_Shipping_Address_id;
	}

	public String getHistory_Billing_Address_id() {
		return history_Billing_Address_id;
	}

	public void setHistory_Billing_Address_id(String history_Billing_Address_id) {
		this.history_Billing_Address_id = history_Billing_Address_id;
	}

	// Chat Array Parameters
	String chat_message;
	String offered_price;
	String chat_RequiredQty;
	String chat_ResponseDate;
	String chat_ResponseUserType;
	String chat_UnitsOfMeasurement;
	String milestone_name;
	String milestone_percentage_brkup;
	String type;
	String mAdditionalInfo;
	List<Milestones> mMilestoneList;
	String mAttachment;

	public String getmAttachment() {
		return mAttachment;
	}

	public void setmAttachment(String mAttachment) {
		this.mAttachment = mAttachment;
	}

	public String getHistoryLastManufacturerResponseId() {
		return historyLastManufacturerResponseId;
	}

	public void setHistoryLastManufacturerResponseId(
			String historyLastManufacturerResponseId) {
		this.historyLastManufacturerResponseId = historyLastManufacturerResponseId;
	}

	public String getHistory_QuotationResponseId() {
		return history_QuotationResponseId;
	}

	public void setHistory_QuotationResponseId(
			String history_QuotationResponseId) {
		this.history_QuotationResponseId = history_QuotationResponseId;
	}

	public List<Milestones> getmMilestoneList() {
		return mMilestoneList;
	}

	public void setmMilestoneList(List<Milestones> mMilestoneList) {
		this.mMilestoneList = mMilestoneList;
	}

	public String getmAdditionalInfo() {
		return mAdditionalInfo;
	}

	public void setmAdditionalInfo(String mAdditionalInfo) {
		this.mAdditionalInfo = mAdditionalInfo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChat_message() {
		return chat_message;
	}

	public void setChat_message(String chat_message) {
		this.chat_message = chat_message;
	}

	public String getOffered_price() {
		return offered_price;
	}

	public void setOffered_price(String offered_price) {
		this.offered_price = offered_price;
	}

	public String getChat_RequiredQty() {
		return chat_RequiredQty;
	}

	public void setChat_RequiredQty(String chat_RequiredQty) {
		this.chat_RequiredQty = chat_RequiredQty;
	}

	public String getChat_ResponseDate() {
		return chat_ResponseDate;
	}

	public void setChat_ResponseDate(String chat_ResponseDate) {
		this.chat_ResponseDate = chat_ResponseDate;
	}

	public String getChat_ResponseUserType() {
		return chat_ResponseUserType;
	}

	public void setChat_ResponseUserType(String chat_ResponseUserType) {
		this.chat_ResponseUserType = chat_ResponseUserType;
	}

	public String getChat_UnitsOfMeasurement() {
		return chat_UnitsOfMeasurement;
	}

	public void setChat_UnitsOfMeasurement(String chat_UnitsOfMeasurement) {
		this.chat_UnitsOfMeasurement = chat_UnitsOfMeasurement;
	}

	public String getMilestone_name() {
		return milestone_name;
	}

	public void setMilestone_name(String milestone_name) {
		this.milestone_name = milestone_name;
	}

	public String getMilestone_percentage_brkup() {
		return milestone_percentage_brkup;
	}

	public void setMilestone_percentage_brkup(String milestone_percentage_brkup) {
		this.milestone_percentage_brkup = milestone_percentage_brkup;
	}

	public String getHistory_subject() {
		return history_subject;
	}

	public void setHistory_subject(String history_subject) {
		this.history_subject = history_subject;
	}

	public String getHistory_createddate() {
		return history_createddate;
	}

	public void setHistory_createddate(String history_createddate) {
		this.history_createddate = history_createddate;
	}

	public String getHistory_id() {
		return history_id;
	}

	public void setHistory_id(String history_id) {
		this.history_id = history_id;
	}

	public String getHistory_isexternalproduct() {
		return history_isexternalproduct;
	}

	public void setHistory_isexternalproduct(String history_isexternalproduct) {
		this.history_isexternalproduct = history_isexternalproduct;
	}

	public String getHistory_LastManufacturerResponseId() {
		return history_LastManufacturerResponseId;
	}

	public void setHistory_LastManufacturerResponseId(
			String history_LastManufacturerResponseId) {
		this.history_LastManufacturerResponseId = history_LastManufacturerResponseId;
	}

	public String getHistory_LastResponseDate() {
		return history_LastResponseDate;
	}

	public void setHistory_LastResponseDate(String history_LastResponseDate) {
		this.history_LastResponseDate = history_LastResponseDate;
	}

	public String getHistory_LastResponseSequenceNumber() {
		return history_LastResponseSequenceNumber;
	}

	public void setHistory_LastResponseSequenceNumber(
			String history_LastResponseSequenceNumber) {
		this.history_LastResponseSequenceNumber = history_LastResponseSequenceNumber;
	}

	public String getHistory_QuotationStatusId() {
		return history_QuotationStatusId;
	}

	public void setHistory_QuotationStatusId(String history_QuotationStatusId) {
		this.history_QuotationStatusId = history_QuotationStatusId;
	}

	public String getHistory_Buyername() {
		return history_Buyername;
	}

	public void setHistory_Buyername(String history_Buyername) {
		this.history_Buyername = history_Buyername;
	}

	public String getHistory_Buyerimage() {
		return history_Buyerimage;
	}

	public void setHistory_Buyerimage(String history_Buyerimage) {
		this.history_Buyerimage = history_Buyerimage;
	}

	public String getHistory_Manufacturename() {
		return history_Manufacturename;
	}

	public void setHistory_Manufacturename(String history_Manufacturename) {
		this.history_Manufacturename = history_Manufacturename;
	}

	public String getHistory_Manufactureimage() {
		return history_Manufactureimage;
	}

	public void setHistory_Manufactureimage(String history_Manufactureimage) {
		this.history_Manufactureimage = history_Manufactureimage;
	}

	public String getHistory_productname() {
		return history_productname;
	}

	public void setHistory_productname(String history_productname) {
		this.history_productname = history_productname;
	}

	public String getHistory_productimage() {
		return history_productimage;
	}

	public void setHistory_productimage(String history_productimage) {
		this.history_productimage = history_productimage;
	}

}
