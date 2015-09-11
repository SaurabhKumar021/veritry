package com.mmadapps.verisupplier.beans;

import java.util.List;

public class ProductDetails {
	
	private String mProduct_Id;
	private String mProduct_Name;
	private String mProduct_Image;
	private String mProduct_description;
	private String mProduct_averageRating;
	private String mProduct_price;
	private String mProduct_moq;
	private String mManufacturer_name;
	private String mManufacturer_id;
	private String mManufacturer_image;
	private String mManufacturer_likes;
	private String mManufacturer_feedback;
	private String mProduct_quantity;
	private String mShipping_date;
	private String mShipping_cost;
	private String mShipping_type;
	private String mFeedback_stars;
	private String mTotalFeedBackCount;
	private String mProduct_UnitOfMeasurement;
	private List<ProductSpecificationDetails> mSpecificationList;
	private List<FeedbackDetails> mUserFeedbackList;
	private List<ProductImageDetails> mProductsImagesList;
	
	public List<ProductImageDetails> getmProductsImagesList() {
		return mProductsImagesList;
	}
	public void setmProductsImagesList(List<ProductImageDetails> mProductsImagesList) {
		this.mProductsImagesList = mProductsImagesList;
	}
	public String getmProduct_UnitOfMeasurement() {
		return mProduct_UnitOfMeasurement;
	}
	public void setmProduct_UnitOfMeasurement(String mProduct_UnitOfMeasurement) {
		this.mProduct_UnitOfMeasurement = mProduct_UnitOfMeasurement;
	}
	public List<FeedbackDetails> getmUserFeedbackList() {
		return mUserFeedbackList;
	}
	public void setmUserFeedbackList(List<FeedbackDetails> mUserFeedbackList) {
		this.mUserFeedbackList = mUserFeedbackList;
	}
	public String getmTotalFeedBackCount() {
		return mTotalFeedBackCount;
	}
	public void setmTotalFeedBackCount(String mTotalFeedBackCount) {
		this.mTotalFeedBackCount = mTotalFeedBackCount;
	}
	
	
	public List<ProductSpecificationDetails> getmSpecificationList() {
		return mSpecificationList;
	}
	public void setmSpecificationList(
			List<ProductSpecificationDetails> mSpecificationList) {
		this.mSpecificationList = mSpecificationList;
	}
	public String getmProduct_Id() {
		return mProduct_Id;
	}
	public void setmProduct_Id(String mProduct_Id) {
		this.mProduct_Id = mProduct_Id;
	}
	public String getmProduct_Name() {
		return mProduct_Name;
	}
	public void setmProduct_Name(String mProduct_Name) {
		this.mProduct_Name = mProduct_Name;
	}
	public String getmProduct_Image() {
		return mProduct_Image;
	}
	public void setmProduct_Image(String mProduct_Image) {
		this.mProduct_Image = mProduct_Image;
	}
	public String getmProduct_description() {
		return mProduct_description;
	}
	public void setmProduct_description(String mProduct_description) {
		this.mProduct_description = mProduct_description;
	}
	public String getmProduct_averageRating() {
		return mProduct_averageRating;
	}
	public void setmProduct_averageRating(String mProduct_averageRating) {
		this.mProduct_averageRating = mProduct_averageRating;
	}
	public String getmProduct_price() {
		return mProduct_price;
	}
	public void setmProduct_price(String mProduct_price) {
		this.mProduct_price = mProduct_price;
	}
	public String getmProduct_moq() {
		return mProduct_moq;
	}
	public void setmProduct_moq(String mProduct_moq) {
		this.mProduct_moq = mProduct_moq;
	}
	public String getmManufacturer_name() {
		return mManufacturer_name;
	}
	public void setmManufacturer_name(String mManufacturer_name) {
		this.mManufacturer_name = mManufacturer_name;
	}
	public String getmManufacturer_id() {
		return mManufacturer_id;
	}
	public void setmManufacturer_id(String mManufacturer_id) {
		this.mManufacturer_id = mManufacturer_id;
	}
	public String getmManufacturer_image() {
		return mManufacturer_image;
	}
	public void setmManufacturer_image(String mManufacturer_image) {
		this.mManufacturer_image = mManufacturer_image;
	}
	public String getmManufacturer_likes() {
		return mManufacturer_likes;
	}
	public void setmManufacturer_likes(String mManufacturer_likes) {
		this.mManufacturer_likes = mManufacturer_likes;
	}
	public String getmManufacturer_feedback() {
		return mManufacturer_feedback;
	}
	public void setmManufacturer_feedback(String mManufacturer_feedback) {
		this.mManufacturer_feedback = mManufacturer_feedback;
	}
	public String getmProduct_quantity() {
		return mProduct_quantity;
	}
	public void setmProduct_quantity(String mProduct_quantity) {
		this.mProduct_quantity = mProduct_quantity;
	}
	public String getmShipping_date() {
		return mShipping_date;
	}
	public void setmShipping_date(String mShipping_date) {
		this.mShipping_date = mShipping_date;
	}
	public String getmShipping_cost() {
		return mShipping_cost;
	}
	public void setmShipping_cost(String mShipping_cost) {
		this.mShipping_cost = mShipping_cost;
	}
	public String getmShipping_type() {
		return mShipping_type;
	}
	public void setmShipping_type(String mShipping_type) {
		this.mShipping_type = mShipping_type;
	}
	public String getmFeedback_stars() {
		return mFeedback_stars;
	}
	public void setmFeedback_stars(String mFeedback_stars) {
		this.mFeedback_stars = mFeedback_stars;
	}
	
	
	private String mManufacturerEmail;
	private String mManufacturerAddress;
	private String mProductSubTotal;

	public String getmManufacturerEmail() {
		return mManufacturerEmail;
	}
	public void setmManufacturerEmail(String mManufacturerEmail) {
		this.mManufacturerEmail = mManufacturerEmail;
	}
	public String getmManufacturerAddress() {
		return mManufacturerAddress;
	}
	public void setmManufacturerAddress(String mManufacturerAddress) {
		this.mManufacturerAddress = mManufacturerAddress;
	}
	public String getmProductSubTotal() {
		return mProductSubTotal;
	}
	public void setmProductSubTotal(String mProductSubTotal) {
		this.mProductSubTotal = mProductSubTotal;
	}

}
