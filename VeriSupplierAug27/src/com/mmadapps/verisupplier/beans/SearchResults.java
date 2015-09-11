package com.mmadapps.verisupplier.beans;

import java.util.List;

public class SearchResults {

	private String mServiceGroupType;
	private String msearch_q;
	private List<SearchProducts> mSearchProductsList;
	private List<ProductVariantAttributes> mProductVariantAttributes;

	public String getmServiceGroupType() {
		return mServiceGroupType;
	}

	public void setmServiceGroupType(String mServiceGroupType) {
		this.mServiceGroupType = mServiceGroupType;
	}

	public String getMsearch_q() {
		return msearch_q;
	}

	public void setMsearch_q(String msearch_q) {
		this.msearch_q = msearch_q;
	}

	public List<SearchProducts> getmSearchProductsList() {
		return mSearchProductsList;
	}

	public void setmSearchProductsList(List<SearchProducts> mSearchProductsList) {
		this.mSearchProductsList = mSearchProductsList;
	}

	public List<ProductVariantAttributes> getmProductVariantAttributes() {
		return mProductVariantAttributes;
	}

	public void setmProductVariantAttributes(
			List<ProductVariantAttributes> mProductVariantAttributes) {
		this.mProductVariantAttributes = mProductVariantAttributes;
	}

}
