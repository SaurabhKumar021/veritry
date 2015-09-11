package com.mmadapps.verisupplier.beans;

import java.util.List;

public class ProductVariantAttributes {

	private String mColorSquaresRgb;
	private String mId;
	private String mName;
	private List<ProductVariantOptions> mProductVriantOptionsList;

	public String getmColorSquaresRgb() {
		return mColorSquaresRgb;
	}

	public void setmColorSquaresRgb(String mColorSquaresRgb) {
		this.mColorSquaresRgb = mColorSquaresRgb;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public List<ProductVariantOptions> getmProductVriantOptionsList() {
		return mProductVriantOptionsList;
	}

	public void setmProductVriantOptionsList(List<ProductVariantOptions> mProductVriantOptionsList) {
		this.mProductVriantOptionsList = mProductVriantOptionsList;
	}

}
