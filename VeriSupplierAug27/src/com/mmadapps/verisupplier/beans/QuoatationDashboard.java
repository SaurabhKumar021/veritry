package com.mmadapps.verisupplier.beans;

import java.util.ArrayList;

public class QuoatationDashboard {

	String Quotation_buyername;
	String Quotation_manufacturename;
	String Quotation_productname;
	String Quotation_createddate;
	String Quotation_Id;
	String Quotation_isExternalProduct;
	String Quotation_LastManufacturerResponseId;
	String Quotation_LastResponseSequenceNumber;
	String QuotationNumber;
	String Quotation_Subject;
	String mQuotation_Number;
	String mQuotation_Datetime;
	private int type;
	ArrayList<Chatdata> chatdatas;
	private String mProduct_Image;
	private String mQuotationStatusId;
	
	private String mTotalCount;

	public String getmTotalCount() {
		return mTotalCount;
	}

	public void setmTotalCount(String mTotalCount) {
		this.mTotalCount = mTotalCount;
	}

	public String getmQuotationStatusId() {
		return mQuotationStatusId;
	}

	public void setmQuotationStatusId(String mQuotationStatusId) {
		this.mQuotationStatusId = mQuotationStatusId;
	}

	public String getmQuotation_Number() {
		return mQuotation_Number;
	}

	public void setmQuotation_Number(String mQuotation_Number) {
		this.mQuotation_Number = mQuotation_Number;
	}

	public String getmQuotation_Datetime() {
		return mQuotation_Datetime;
	}

	public void setmQuotation_Datetime(String mQuotation_Datetime) {
		this.mQuotation_Datetime = mQuotation_Datetime;
	}

	public String getmProduct_Image() {
		return mProduct_Image;
	}

	public void setmProduct_Image(String mProduct_Image) {
		this.mProduct_Image = mProduct_Image;
	}

	public ArrayList<Chatdata> getChatdatas() {
		return chatdatas;
	}

	public void setChatdatas(ArrayList<Chatdata> chatdatas) {
		this.chatdatas = chatdatas;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getQuotation_buyername() {
		return Quotation_buyername;
	}

	public void setQuotation_buyername(String quotation_buyername) {
		Quotation_buyername = quotation_buyername;
	}

	public String getQuotation_manufacturename() {
		return Quotation_manufacturename;
	}

	public void setQuotation_manufacturename(String quotation_manufacturename) {
		Quotation_manufacturename = quotation_manufacturename;
	}

	public String getQuotation_productname() {
		return Quotation_productname;
	}

	public void setQuotation_productname(String quotation_productname) {
		Quotation_productname = quotation_productname;
	}

	public String getQuotation_createddate() {
		return Quotation_createddate;
	}

	public void setQuotation_createddate(String quotation_createddate) {
		Quotation_createddate = quotation_createddate;
	}

	public String getQuotation_Id() {
		return Quotation_Id;
	}

	public void setQuotation_Id(String quotation_Id) {
		Quotation_Id = quotation_Id;
	}

	public String getQuotation_isExternalProduct() {
		return Quotation_isExternalProduct;
	}

	public void setQuotation_isExternalProduct(
			String quotation_isExternalProduct) {
		Quotation_isExternalProduct = quotation_isExternalProduct;
	}

	public String getQuotation_LastManufacturerResponseId() {
		return Quotation_LastManufacturerResponseId;
	}

	public void setQuotation_LastManufacturerResponseId(
			String quotation_LastManufacturerResponseId) {
		Quotation_LastManufacturerResponseId = quotation_LastManufacturerResponseId;
	}

	public String getQuotation_LastResponseSequenceNumber() {
		return Quotation_LastResponseSequenceNumber;
	}

	public void setQuotation_LastResponseSequenceNumber(
			String quotation_LastResponseSequenceNumber) {
		Quotation_LastResponseSequenceNumber = quotation_LastResponseSequenceNumber;
	}

	public String getQuotationNumber() {
		return QuotationNumber;
	}

	public void setQuotationNumber(String quotationNumber) {
		QuotationNumber = quotationNumber;
	}

	public String getQuotation_Subject() {
		return Quotation_Subject;
	}

	public void setQuotation_Subject(String quotation_Subject) {
		Quotation_Subject = quotation_Subject;
	}

}
