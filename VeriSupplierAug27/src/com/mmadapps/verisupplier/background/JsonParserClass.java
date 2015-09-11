package com.mmadapps.verisupplier.background;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.mmadapps.verisupplier.beans.AlertGetMessages;
import com.mmadapps.verisupplier.beans.CategoryDetails;
import com.mmadapps.verisupplier.beans.Channel;
import com.mmadapps.verisupplier.beans.CountryDetails;
import com.mmadapps.verisupplier.beans.FeedbackDetails;
import com.mmadapps.verisupplier.beans.Inventory;
import com.mmadapps.verisupplier.beans.InventoryWarehouseList;
import com.mmadapps.verisupplier.beans.Lookups;
import com.mmadapps.verisupplier.beans.ManufacturerDetails;
import com.mmadapps.verisupplier.beans.ManufacturerList;
import com.mmadapps.verisupplier.beans.Milestones;
import com.mmadapps.verisupplier.beans.Order;
import com.mmadapps.verisupplier.beans.OrderDashboard;
import com.mmadapps.verisupplier.beans.Orders;
import com.mmadapps.verisupplier.beans.ProductDetails;
import com.mmadapps.verisupplier.beans.ProductImageDetails;
import com.mmadapps.verisupplier.beans.ProductList;
import com.mmadapps.verisupplier.beans.ProductSpecificationDetails;
import com.mmadapps.verisupplier.beans.ProductVariantAttributes;
import com.mmadapps.verisupplier.beans.ProductVariantOptions;
import com.mmadapps.verisupplier.beans.QuickOrderResult;
import com.mmadapps.verisupplier.beans.QuoatationDashboard;
import com.mmadapps.verisupplier.beans.QuotationDashboardHistory;
import com.mmadapps.verisupplier.beans.SearchProducts;
import com.mmadapps.verisupplier.beans.SearchResults;
import com.mmadapps.verisupplier.beans.ServiceDetails;
import com.mmadapps.verisupplier.beans.ServiceList;
import com.mmadapps.verisupplier.beans.UserDetails;
import com.mmadapps.verisupplier.beans.Warehouse;
import com.mmadapps.verisupplier.beans.WarehouseList;
import com.mmadapps.verisupplier.beans.WishlistDetails;

public class JsonParserClass {

	Context mContext;

	public List<CategoryDetails> parseCategoryDetails(String resultParam,
			Context applicationContext) {
		List<CategoryDetails> mCategoryDetails = new ArrayList<CategoryDetails>();
		try {
			JSONObject jObj = new JSONObject(resultParam);
			JSONObject jCatObject = jObj.getJSONObject("GetCategoriesResult");
			if (jCatObject == null || jCatObject.length() == 0) {
			} else {
				JSONArray jCategoryArray = jCatObject
						.getJSONArray("ResponseObject");
				if (jCategoryArray == null || jCategoryArray.length() == 0) {
				} else {
					for (int i = 0; i < jCategoryArray.length(); i++) {
						JSONObject catObject = jCategoryArray.getJSONObject(i);
						CategoryDetails mCategoryList = new CategoryDetails();
						mCategoryList.setmCategory_Id(getObjectvalue(catObject,
								"Id"));
						mCategoryList.setmCategory_Name(getObjectvalue(
								catObject, "Name"));
						mCategoryList.setmCategory_Image(getObjectvalue(
								catObject, "PictureUrl"));
						mCategoryList.setmCategory_ParentId(getObjectvalue(
								catObject, "ParentCategoryId"));
						JSONArray childrenArray = catObject
								.getJSONArray("Children");
						if (childrenArray == null
								|| childrenArray.length() == 0) {
						} else {
							for (int j = 0; j < childrenArray.length(); j++) {
								JSONObject childObject = childrenArray
										.getJSONObject(j);
								CategoryDetails mCategoryChildList = new CategoryDetails();
								mCategoryChildList
										.setmCategory_Id(getObjectvalue(
												childObject, "Id"));
								mCategoryChildList
										.setmCategory_Name(getObjectvalue(
												childObject, "Name"));
								mCategoryChildList
										.setmCategory_Image(getObjectvalue(
												childObject, "PictureUrl"));
								mCategoryChildList
										.setmCategory_ParentId(getObjectvalue(
												childObject, "ParentCategoryId"));

								mCategoryDetails.add(mCategoryChildList);
							}
						}
						mCategoryDetails.add(mCategoryList);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mCategoryDetails;
	}

	public List<ProductList> parseQualityPicks(String resultParam,
			Context applicationContext) {
		List<ProductList> mQualityPicksList = new ArrayList<ProductList>();
		try {
			JSONObject jObj = new JSONObject(resultParam);
			JSONObject productObject = jObj
					.getJSONObject("GetProductQualityPicksResult");
			if (productObject == null || productObject.length() == 0) {
			} else {
				JSONObject productObj = productObject
						.getJSONObject("ResponseObject");
				if (productObj == null || productObj.length() == 0) {
				} else {
					JSONArray qualityArray = productObj
							.getJSONArray("ProductDetails");
					if (qualityArray == null || qualityArray.length() == 0) {
					} else {
						for (int i = 0; i < qualityArray.length(); i++) {
							JSONObject prdObject = qualityArray
									.getJSONObject(i);
							ProductList mQualityPicks = new ProductList();
							mQualityPicks.setmProduct_Name(getObjectvalue(
									prdObject, "ProductName"));
							mQualityPicks.setmProduct_Id(getObjectvalue(
									prdObject, "ProductId"));
							mQualityPicks.setmCategory_Id(getObjectvalue(
									prdObject, "CategoryId"));
							mQualityPicks.setmCategory_Name(getObjectvalue(
									prdObject, "CategoryName"));
							mQualityPicks.setmProduct_Image(getObjectvalue(
									prdObject, "ProductPicture"));
							mQualityPicks
									.setmProduct_OfferPrice(getObjectvalue(
											prdObject, "OfferValue"));

							JSONObject manufacturerObject = prdObject
									.getJSONObject("Manufacture");
							if (manufacturerObject == null
									|| manufacturerObject.length() == 0) {
							} else {
								mQualityPicks
										.setmManufacturer_Name(getObjectvalue(
												manufacturerObject, "Name"));
								mQualityPicks
										.setmManufacturer_Id(getObjectvalue(
												manufacturerObject, "Id"));
							}
							mQualityPicksList.add(mQualityPicks);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mQualityPicksList;
	}

	public List<ProductList> parseVerifiedManufacturer(String resultParam,
			Context applicationContext) {
		List<ProductList> mVerifiedManufacturerList = new ArrayList<ProductList>();
		try {
			JSONObject jObj = new JSONObject(resultParam);
			JSONObject productObject = jObj
					.getJSONObject("GetVerifiedManufacturerResult");
			if (productObject == null || productObject.length() == 0) {
			} else {
				JSONArray manufacturerArray = productObject
						.getJSONArray("ResponseObject");
				if (manufacturerArray == null
						|| manufacturerArray.length() == 0) {
				} else {
					for (int i = 0; i < manufacturerArray.length(); i++) {
						JSONObject prdObject = manufacturerArray
								.getJSONObject(i);
						ProductList mVerifiedManufacturer = new ProductList();
						mVerifiedManufacturer
								.setmManufacturer_Name(getObjectvalue(
										prdObject, "ManufacturerName"));
						mVerifiedManufacturer
								.setmManufacturer_Id(getObjectvalue(prdObject,
										"ManufacturerId"));
						mVerifiedManufacturer
								.setmManufacturer_Image(getObjectvalue(
										prdObject, "ManufacturerPicture"));
						mVerifiedManufacturer
								.setmManufacturer_Likes(getObjectvalue(
										prdObject, "NoOfPeopleLiked"));
						mVerifiedManufacturer
								.setmManufacturer_Feedback(getObjectvalue(
										prdObject, "PositiveFeedback"));
						mVerifiedManufacturerList.add(mVerifiedManufacturer);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mVerifiedManufacturerList;
	}

	public List<ProductDetails> parseProductDetails(String resultOutparam,
			Context applicationContext) {
		List<ProductDetails> mProductDetails = new ArrayList<ProductDetails>();
		List<ProductSpecificationDetails> mSpecificationList = new ArrayList<ProductSpecificationDetails>();
		List<FeedbackDetails> mUserFeedbackList = new ArrayList<FeedbackDetails>();
		List<ProductImageDetails> mProductsImages = new ArrayList<ProductImageDetails>();
		try {
			JSONObject json = new JSONObject(resultOutparam);
			JSONObject listObj = json.getJSONObject("GetProductDetailResult");
			if (listObj == null || listObj.length() == 0) {
			} else {
				JSONObject resObject = listObj.getJSONObject("ResponseObject");
				if (resObject == null || resObject.length() == 0) {
				} else {
					ProductDetails mProductdetail = new ProductDetails();
					mProductdetail.setmProduct_Name(getObjectvalue(resObject,
							"ProductName"));
					mProductdetail.setmProduct_Id(getObjectvalue(resObject,
							"Id"));
					// mProductdetail.setmProduct_Image(getObjectvalue(resObject,
					// "ProductPicture"));
					// mProductdetail.setmProduct_description(getObjectvalue(resObject,
					// "ShortDescription"));
					JSONObject PriceModelObject = resObject
							.getJSONObject("PriceModel");
					if (PriceModelObject == null
							|| PriceModelObject.length() == 0) {
					} else {
						mProductdetail.setmProduct_price(getObjectvalue(
								PriceModelObject, "Price"));
						mProductdetail
								.setmProduct_UnitOfMeasurement(getObjectvalue(
										PriceModelObject, "UnitOfMeasurement"));
					}

					/*
					 * JSONObject MOQObject=resObject.getJSONObject("MOQ");
					 * if(MOQObject==null || MOQObject.length()==0){ }else{
					 * String moqValue=getObjectvalue(MOQObject,
					 * "Quantity");//MOQObject.getString("Quantity"); String
					 * moqUnit=getObjectvalue(MOQObject,
					 * "UnitOfMeasurement");//MOQObject
					 * .getString("UnitOfMeasurement");
					 * 
					 * mProductdetail.setmProduct_moq(moqValue+" "+moqUnit); }
					 */

					mProductdetail.setmProduct_moq(getObjectvalue(resObject,
							"MOQ"));

					JSONObject manufacturerObject = resObject
							.getJSONObject("ManufacturerLiteM");
					if (manufacturerObject == null
							|| manufacturerObject.length() == 0) {
					} else {
						mProductdetail.setmManufacturer_id(getObjectvalue(
								manufacturerObject, "Id"));
						mProductdetail.setmManufacturer_name(getObjectvalue(
								manufacturerObject, "Name"));
						mProductdetail.setmManufacturer_image(getObjectvalue(
								manufacturerObject, "Logo"));
						/*
						 * JSONObject
						 * logoObject=manufacturerObject.getJSONObject("Logo");
						 * if(logoObject==null || logoObject.length()==0){
						 * }else{
						 * mProductdetail.setmManufacturer_image(getObjectvalue
						 * (logoObject, "url")); }
						 */
					}
					// mProductdetail.setmManufacturer_likes(getObjectvalue(resObject,
					// "LikesCount"));
					// mProductdetail.setmManufacturer_feedback(getObjectvalue(resObject,
					// "FeedbackCount"));
					/*
					 * JSONObject
					 * shippingObject=resObject.getJSONObject("ShippingDetails"
					 * ); if(shippingObject==null ||
					 * shippingObject.length()==0){ }else{
					 * mProductdetail.setmShipping_cost
					 * (getObjectvalue(shippingObject, "ShippingCost"));
					 * mProductdetail
					 * .setmShipping_date(getObjectvalue(shippingObject,
					 * "ShippingDate"));
					 * mProductdetail.setmShipping_type(getObjectvalue
					 * (shippingObject, "ShippingType")); }
					 */
					JSONObject feedbackObject = resObject
							.getJSONObject("FeedBackDetails");
					if (feedbackObject == null || feedbackObject.length() == 0) {
					} else {
						JSONArray ratingArray = feedbackObject
								.getJSONArray("Ratings");
						if (ratingArray == null || ratingArray.length() == 0) {
						} else {
							String mRatingNew = "";
							for (int i = 0; i < ratingArray.length(); i++) {
								JSONObject ratingObject = ratingArray
										.getJSONObject(i);
								mProductdetail
										.setmProduct_averageRating(getObjectvalue(
												ratingObject, "AverageRating"));
								if (i == 0) {
									mRatingNew = mRatingNew
											+ getObjectvalue(ratingObject,
													"TotalRatingCount");
								} else {
									mRatingNew += ","
											+ getObjectvalue(ratingObject,
													"TotalRatingCount");
								}
							}
							mProductdetail.setmFeedback_stars(mRatingNew);
							mProductdetail
									.setmTotalFeedBackCount(getObjectvalue(
											feedbackObject, "TotalFeedBack"));
						}

						JSONArray userfeedbackArray = feedbackObject
								.getJSONArray("UserFeedBack");
						if (userfeedbackArray == null
								|| userfeedbackArray.length() == 0) {
						} else {
							for (int k = 0; k < userfeedbackArray.length(); k++) {
								JSONObject userfeedback = userfeedbackArray
										.getJSONObject(k);
								FeedbackDetails userfeedbackList = new FeedbackDetails();

								userfeedbackList
										.setmCreatedDate(getObjectvalue(
												userfeedback, "CreatedDate"));
								userfeedbackList
										.setmFeedBackText(getObjectvalue(
												userfeedback, "FeedBackText"));
								userfeedbackList
										.setmFeedbackUserName(getObjectvalue(
												userfeedback, "Username"));
								userfeedbackList
										.setmFeedbackRating(getObjectvalue(
												userfeedback, "Rating"));
								userfeedbackList
										.setmFeedbackLocation(getObjectvalue(
												userfeedback, "Location"));

								mUserFeedbackList.add(userfeedbackList);
							}
						}
					}
					mProductdetail.setmUserFeedbackList(mUserFeedbackList);
					JSONArray specificationArray = resObject
							.getJSONArray("ProductAttributes");
					if (specificationArray == null
							|| specificationArray.length() == 0) {
					} else {
						for (int j = 0; j < specificationArray.length(); j++) {
							JSONObject specificationObject = specificationArray
									.getJSONObject(j);
							ProductSpecificationDetails mSpecificationDetails = new ProductSpecificationDetails();

							mSpecificationDetails
									.setmProductAttribute_Name(getObjectvalue(
											specificationObject,
											"ProductAttributeName"));
							mSpecificationDetails
									.setmProductAttribute_Value(getObjectvalue(
											specificationObject,
											"ProductAttributeValue"));

							mSpecificationList.add(mSpecificationDetails);

						}
					}
					mProductdetail.setmSpecificationList(mSpecificationList);
					try {
						JSONArray ProductImageUrlListArray = resObject
								.getJSONArray("ProductImageUrlList");
						if (ProductImageUrlListArray == null
								|| ProductImageUrlListArray.length() == 0) {
						} else {
							for (int k = 0; k < ProductImageUrlListArray
									.length(); k++) {
								// JSONObject
								// ProductImageUrlListObject=ProductImageUrlListArray.getJSONObject(k);
								ProductImageDetails mProductImages = new ProductImageDetails();

								mProductImages
										.setmProductImage_url(ProductImageUrlListArray
												.getString(k));

								mProductsImages.add(mProductImages);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					mProductdetail.setmProductsImagesList(mProductsImages);
					mProductDetails.add(mProductdetail);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mProductDetails;
	}

	public List<ManufacturerDetails> parseManufacturerDetails(String result) {
		List<ManufacturerDetails> mDetails = new ArrayList<ManufacturerDetails>();
		List<ProductSpecificationDetails> mManufacturerAttributesList = new ArrayList<ProductSpecificationDetails>();
		try {
			JSONObject json = new JSONObject(result);
			JSONObject mfrObject = json
					.getJSONObject("GetManufacturerDetailResult");
			String isError = getObjectvalue(mfrObject, "IsError");
			if (isError.equalsIgnoreCase("false")) {
				JSONObject responseObj = mfrObject
						.getJSONObject("ResponseObject");
				ManufacturerDetails manufacturerDetails = new ManufacturerDetails();

				manufacturerDetails.setmAddressDetails(getObjectvalue(
						responseObj, "Address"));
				manufacturerDetails.setmManufactureID(getObjectvalue(
						responseObj, "Id"));
				manufacturerDetails.setmManufacturerLogo(getObjectvalue(
						responseObj, "Logo"));
				manufacturerDetails.setmEmailDetails(getObjectvalue(
						responseObj, "ManufacturerEmail"));
				manufacturerDetails.setmPhoneDetails(getObjectvalue(
						responseObj, "ManufacturerPhone"));
				manufacturerDetails.setmManufacturerName(getObjectvalue(
						responseObj, "Name"));
				manufacturerDetails.setmShortDescription(getObjectvalue(
						responseObj, "ShortDescription"));

				JSONArray ManufacturerAttributesArray = responseObj
						.getJSONArray("ManufacuturerAttributes");
				if (ManufacturerAttributesArray == null
						|| ManufacturerAttributesArray.length() == 0) {
				} else {
					for (int j = 0; j < ManufacturerAttributesArray.length(); j++) {
						ProductSpecificationDetails mManufacturerAttributes = new ProductSpecificationDetails();
						JSONObject manufacturerObject = ManufacturerAttributesArray
								.getJSONObject(j);
						mManufacturerAttributes
								.setmProductAttribute_Name(getObjectvalue(
										manufacturerObject, "Name"));
						mManufacturerAttributes
								.setmProductAttribute_Value(getObjectvalue(
										manufacturerObject, "Value"));

						mManufacturerAttributesList
								.add(mManufacturerAttributes);
					}
				}
				manufacturerDetails
						.setmManufactureAttributes(mManufacturerAttributesList);
				mDetails.add(manufacturerDetails);

				/*
				 * JSONArray mfrAttributes =
				 * responseObj.getJSONArray("ManufacuturerAttributes"); int
				 * attrLen = mfrAttributes.length(); JSONArray mProdArray =
				 * responseObj.getJSONArray("ProductDetailsList"); int prodLen =
				 * mProdArray.length(); String mAddress =
				 * getObjectvalue(responseObj, "Address"); String mMfrEmail =
				 * getObjectvalue(responseObj,"ManufacturerEmail"); String
				 * mMfrId = getObjectvalue(responseObj, "ManufacturerId");
				 * String mMfrName =
				 * getObjectvalue(responseObj,"ManufacturerName"); String
				 * mMfrPhone = getObjectvalue(responseObj,"ManufacturerPhone");
				 * String mMfrPic =
				 * getObjectvalue(responseObj,"ManufacturerPicture"); String
				 * mLikes = getObjectvalue(responseObj, "NoOfPeopleLiked");
				 * String mFeedbacks =
				 * getObjectvalue(responseObj,"PositiveFeedback"); String
				 * mShortDesc = getObjectvalue(responseObj,"ShortDescription");
				 * 
				 * String mYOEstablishedKey = "Established Year"; String
				 * mYOEstablishedValue = ""; String mBusLocationKey =
				 * "Business Location"; String mBusLocationValue = ""; String
				 * mNOStaffKey = "No. of Staffs"; String mNOStaffValue = ""; for
				 * (int i = 0; i < attrLen; i++) { JSONObject attrObj =
				 * mfrAttributes.getJSONObject(i); String key =
				 * getObjectvalue(attrObj, "Name"); if
				 * (key.equalsIgnoreCase(mYOEstablishedKey)) {
				 * mYOEstablishedValue = getObjectvalue(attrObj, "Value"); }
				 * else if (key.equalsIgnoreCase(mBusLocationKey)) {
				 * mBusLocationValue = getObjectvalue(attrObj, "Value"); } else
				 * if (key.equalsIgnoreCase(mNOStaffKey)) { mNOStaffValue =
				 * getObjectvalue(attrObj, "Value"); } }
				 * 
				 * 
				 * 
				 * List<ManufacturerProductDetails> mProductList = new
				 * ArrayList<ManufacturerProductDetails>(); for (int i = 0; i <
				 * prodLen; i++) { ManufacturerProductDetails pd = new
				 * ManufacturerProductDetails(); JSONObject pobj =
				 * mProdArray.getJSONObject(i); String mProductId =
				 * getObjectvalue(pobj, "ProductId"); String mPrice =
				 * getObjectvalue(pobj, "Price"); String mOfferValue =
				 * getObjectvalue(pobj, "OfferValue"); String mProductName =
				 * getObjectvalue(pobj, "ProductName"); String mProductImage =
				 * getObjectvalue(pobj,"ProductPicture");
				 * pd.setmManufacturerProduct_Id(mProductId);
				 * pd.setmManufacturerProduct_Image(mProductImage);
				 * pd.setmManufacturerProduct_Name(mProductName);
				 * pd.setmManufacturerProduct_offer(mOfferValue);
				 * pd.setmManufacturerProduct_manufName(mMfrName);
				 * mProductList.add(pd); } ManufacturerDetails md = new
				 * ManufacturerDetails(); md.setmAddressDetails(mAddress);
				 * md.setmManufactureID(mMfrId);
				 * md.setmManufacturerLogo(mMfrPic);
				 * md.setmManufacturerName(mMfrName);
				 * md.setmEmailDetails(mMfrEmail);
				 * md.setmPhoneDetails(mMfrPhone); md.setmLikes(mLikes);
				 * md.setmFeedback(mFeedbacks);
				 * md.setmShortDescription(mShortDesc);
				 * md.setmYearEstablished(mYOEstablishedValue);
				 * md.setmEmployeeCount(mNOStaffValue); //
				 * md.setmBusinessType(mBusLocationValue);
				 * md.setmTradeDetail(mBusLocationValue);
				 * md.setmManufactureAttributes(mManufacturerAttributesList);
				 * md.setmProductDetails(mProductList);
				 */

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDetails;
	}

	public List<ServiceDetails> parseValueAddedServicesDetails(
			String resultOutparam, Context context) {
		mContext = context;
		List<ServiceDetails> mDetails = new ArrayList<ServiceDetails>();

		try {
			JSONObject json = new JSONObject(resultOutparam);
			JSONArray listObj = json
					.getJSONArray("GetValueAddedServicesResult");
			if (listObj == null || listObj.length() == 0) {
			} else {
				for (int i = 0; i < listObj.length(); i++) {
					List<ServiceList> mServiceList = new ArrayList<ServiceList>();
					ServiceDetails mServiceDetails = new ServiceDetails();
					JSONObject serviceObject = listObj.getJSONObject(i);
					try {
						JSONObject contact_detail = serviceObject
								.getJSONObject("ContactDetails");
						if (contact_detail == null
								|| contact_detail.length() == 0) {
						} else {
							JSONArray contact_detailArray = contact_detail
									.getJSONArray("Address");
							String mAddressNew = "";
							for (int j = 0; j < contact_detailArray.length(); j++) {
								if (j == 0) {
									mAddressNew = mAddressNew
											+ contact_detailArray.getString(j);
								} else {
									mAddressNew += ":"
											+ contact_detailArray.getString(j);
								}
							}
							mServiceDetails.setmAddress(mAddressNew);

							JSONArray contact_personArray = contact_detail
									.getJSONArray("ContactPerson");
							String mContactPerson = "";
							for (int k = 0; k < contact_personArray.length(); k++) {
								if (k == 0) {
									mContactPerson = mContactPerson
											+ contact_personArray.getString(k);
								} else {
									mContactPerson += ","
											+ contact_personArray.getString(k);
								}
							}
							mServiceDetails.setmContactPerson(mContactPerson);

							JSONArray email_Array = contact_detail
									.getJSONArray("Email");
							String mEmail = "";
							for (int l = 0; l < email_Array.length(); l++) {
								if (l == 0) {
									mEmail = mEmail + email_Array.getString(l);
								} else {
									mEmail += "," + email_Array.getString(l);
								}
							}
							mServiceDetails.setmEmail(mEmail);

							JSONArray phonenumber_Array = contact_detail
									.getJSONArray("PhoneNumber");
							String mPhoneNumber = "";
							for (int m = 0; m < phonenumber_Array.length(); m++) {
								if (m == 0) {
									mPhoneNumber = mPhoneNumber
											+ phonenumber_Array.getString(m);
								} else {
									mPhoneNumber += ":"
											+ phonenumber_Array.getString(m);
								}
							}
							mServiceDetails.setmPhoneNumber(mPhoneNumber);

							JSONArray website_Array = contact_detail
									.getJSONArray("Website");
							String mWebSite = "";
							for (int n = 0; n < website_Array.length(); n++) {
								if (n == 0) {
									mWebSite = mWebSite
											+ website_Array.getString(n);
								} else {
									mWebSite += ","
											+ website_Array.getString(n);
								}
							}
							mServiceDetails.setmWebsite(mWebSite);

							mServiceDetails
									.setmServiceDescription(getObjectvalue(
											serviceObject, "Description"));
							mServiceDetails.setmServiceName(getObjectvalue(
									serviceObject, "Name"));
							mServiceDetails.setmServiceId(getObjectvalue(
									serviceObject, "Id"));

							JSONObject logo_detail = serviceObject
									.getJSONObject("Logo");
							if (logo_detail == null
									|| logo_detail.length() == 0) {
							} else {
								mServiceDetails
										.setmService_CompanyImageUrl(getObjectvalue(
												logo_detail, "url"));
							}

							mServiceDetails.setmShipmentType(getObjectvalue(
									serviceObject, "ShipmentType"));

							JSONArray servicer_Array = serviceObject
									.getJSONArray("Services");
							for (int j = 0; j < servicer_Array.length(); j++) {

								ServiceList mService = new ServiceList();
								JSONObject servicelistObj = servicer_Array
										.getJSONObject(j);

								JSONObject charge_detail = servicelistObj
										.getJSONObject("Charges");
								if (charge_detail == null
										|| charge_detail.length() == 0) {
								} else {
									mService.setmCurrency(getObjectvalue(
											charge_detail, "Currency"));
									mService.setmPrice(getObjectvalue(
											charge_detail, "Price"));
									mService.setmUnits(getObjectvalue(
											charge_detail, "Units"));
								}

								mService.setmDescription(getObjectvalue(
										servicelistObj, "Description"));
								mService.setmName(getObjectvalue(
										servicelistObj, "Name"));
								mService.setmId(getObjectvalue(servicelistObj,
										"Id"));

								mServiceList.add(mService);
							}
							mServiceDetails.setmServiceList(mServiceList);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					mDetails.add(mServiceDetails);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDetails;
	}

	// Shubham................Dashboard...........................................................................

	/*
	 * public ArrayList<OrderDashboard> ParseDashboardOrder(String
	 * mResuloutparam) { ArrayList<OrderDashboard> dashboards = null;
	 * 
	 * try { JSONObject jsonObject = new JSONObject(mResuloutparam); JSONObject
	 * jsonObject2 = jsonObject.getJSONObject("GetOrderResult");
	 * 
	 * String result = jsonObject2.getString("IsError");
	 * 
	 * if (result.equalsIgnoreCase("false")) { JSONArray jsonArray =
	 * jsonObject2.getJSONArray("ResponseObject"); dashboards = new
	 * ArrayList<OrderDashboard>();
	 * 
	 * for (int i = 0; i < jsonArray.length(); i++) { JSONObject orderObject =
	 * jsonArray.getJSONObject(i);
	 * 
	 * OrderDashboard orderDashboard = new OrderDashboard();
	 * orderDashboard.setmOrder_OfferPrice
	 * (getObjectvalue(orderObject,"OfferedPrice"));
	 * orderDashboard.setmOrder_Date(getObjectvalue(orderObject, "OrderDate"));
	 * orderDashboard.setmOrder_Id(getObjectvalue(orderObject, "OrderId"));
	 * orderDashboard.setmOrder_Number(getObjectvalue(orderObject,
	 * "OrderNumber"));
	 * orderDashboard.setmOrder_Quantity(getObjectvalue(orderObject,
	 * "OrderQuantity"));
	 * orderDashboard.setmOrder_StatusId(getObjectvalue(orderObject,
	 * "OrderStatusId"));
	 * orderDashboard.setmOrder_SubTotal(getObjectvalue(orderObject,
	 * "SubTotal"));
	 * orderDashboard.setmOrder_TotalCount(getObjectvalue(orderObject,
	 * "TotalCount"));
	 * orderDashboard.setmOrder_UnitOfMeasurement(getObjectvalue(orderObject,
	 * "UnitsOfMeasurement"));
	 * 
	 * JSONObject buyerObject = orderObject.getJSONObject("Buyer");
	 * if(buyerObject==null || buyerObject.length()==0){ }else{
	 * orderDashboard.setmOrder_Buyername(getObjectvalue(buyerObject, "Name"));
	 * }
	 * 
	 * JSONObject productObject = orderObject.getJSONObject("Product");
	 * if(productObject==null || productObject.length()==0){ }else{
	 * orderDashboard.setmOrder_Productname(getObjectvalue(productObject,
	 * "ProductName"));
	 * orderDashboard.setmOrder_ProductImage(getObjectvalue(productObject,
	 * "ProductImage"));
	 * orderDashboard.setmOrder_ProductId(getObjectvalue(productObject, "Id"));
	 * 
	 * }
	 * 
	 * JSONObject manufacturerObject =
	 * orderObject.getJSONObject("Manufacturer"); if(manufacturerObject==null ||
	 * manufacturerObject.length()==0){ }else{
	 * orderDashboard.setmOrder_Manufacturname
	 * (getObjectvalue(manufacturerObject, "Name"));
	 * orderDashboard.setmOrder_ManufacturerId
	 * (getObjectvalue(manufacturerObject, "Id"));
	 * orderDashboard.setmOrder_ManufacturerLogo
	 * (getObjectvalue(manufacturerObject, "Logo")); }
	 * 
	 * dashboards.add(orderDashboard); }
	 * 
	 * } else {
	 * 
	 * }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * return dashboards; }
	 */

	/*
	 * public ArrayList<QuoatationDashboard> ParseDashboardQuaotation(String
	 * ResultOutput) { ArrayList<QuoatationDashboard> quoatationDashboards = new
	 * ArrayList<QuoatationDashboard>(); ArrayList<Chatdata> list = new
	 * ArrayList<Chatdata>(); try { JSONObject jsonObject = new
	 * JSONObject(ResultOutput); JSONObject jsonObject2 =
	 * jsonObject.getJSONObject("GetQuotationsResult"); String IsError =
	 * jsonObject2.getString("IsError"); if (IsError.equalsIgnoreCase("false"))
	 * { JSONArray jsonArray = jsonObject2.getJSONArray("ResponseObject"); for
	 * (int i = 0; i < jsonArray.length(); i++) { JSONObject jsonObject3 =
	 * jsonArray.getJSONObject(i); QuoatationDashboard qudashboard1 = new
	 * QuoatationDashboard(); JSONObject jsonObject4 =
	 * jsonObject3.getJSONObject("Buyer"); JSONObject jsonObject5 =
	 * jsonObject3.getJSONObject("Manufacturer"); JSONObject jsonObject6 =
	 * jsonObject3.getJSONObject("Product");
	 * 
	 * qudashboard1.setQuotation_createddate(getObjectvalue(jsonObject3,
	 * "CreatedDate"));
	 * qudashboard1.setmQuotationStatusId(getObjectvalue(jsonObject3,
	 * "QuotationStatusId"));
	 * qudashboard1.setQuotation_Id(getObjectvalue(jsonObject3,"Id"));
	 * qudashboard1.setQuotation_isExternalProduct(getObjectvalue(jsonObject3,
	 * "IsExternalProduct"));
	 * qudashboard1.setQuotation_Subject(getObjectvalue(jsonObject3,
	 * "Subject")); qudashboard1.setQuotation_buyername(getObjectvalue(
	 * jsonObject4, "Name"));
	 * qudashboard1.setQuotation_manufacturename(getObjectvalue(jsonObject5,
	 * "Name"));
	 * qudashboard1.setQuotation_productname(getObjectvalue(jsonObject6,
	 * "ProductName"));
	 * qudashboard1.setmProduct_Image(getObjectvalue(jsonObject6,
	 * "ProductImage"));
	 * qudashboard1.setmQuotation_Number(getObjectvalue(jsonObject3,
	 * "QuotationNumber"));
	 * qudashboard1.setmQuotation_Datetime(getObjectvalue(jsonObject3,
	 * "CreatedDate"));
	 * 
	 * JSONArray jsonArray2 = jsonObject3.getJSONArray("ChatData");
	 * 
	 * for (int j = 0; j < jsonArray2.length(); j++) { Chatdata chatdata = new
	 * Chatdata(); JSONObject jsonObject7 = jsonArray2.getJSONObject(j);
	 * chatdata.setmLastresponsedate(getObjectvalue(jsonObject7,
	 * "ResponseDate"));
	 * chatdata.setmResponseUsertype(getObjectvalue(jsonObject7,
	 * "ResponseUserType")); list.add(chatdata); }
	 * qudashboard1.setChatdatas(list); quoatationDashboards.add(qudashboard1);
	 * } } else { } } catch (Exception e) {
	 * 
	 * } return quoatationDashboards; }
	 */

	/*
	 * public ArrayList<QuotationDashboardHistory> parseAllQuotationHistory(
	 * String mResult) { ArrayList<QuotationDashboardHistory> list = new
	 * ArrayList<QuotationDashboardHistory>(); try { JSONObject jsonObject = new
	 * JSONObject(mResult); JSONObject jsonObject2 =
	 * jsonObject.getJSONObject("GetQuotationHistoryResult");
	 * 
	 * String Iserror = jsonObject2.getString("IsError");
	 * 
	 * if (Iserror.equalsIgnoreCase("false")) { JSONObject jsonObject3 =
	 * jsonObject2.getJSONObject("ResponseObject"); JSONObject manufacture =
	 * jsonObject3.getJSONObject("Manufacturer"); JSONObject product =
	 * jsonObject3.getJSONObject("Product"); JSONObject jsonObject4 =
	 * jsonObject3.getJSONObject("Buyer");
	 * 
	 * String mHistory_createddate = getObjectvalue(jsonObject3,"CreatedDate");
	 * String mHistory_id = getObjectvalue(jsonObject3, "Id"); String
	 * mHistory_isexternalproduct =
	 * getObjectvalue(jsonObject3,"IsExternalProduct"); String
	 * mHistory_LastResponseDate =
	 * getObjectvalue(jsonObject3,"LastResponseDate"); String
	 * mHistory_LastResponseSequenceNumber = getObjectvalue(jsonObject3,
	 * "LastResponseSequenceNumber"); String mHistory_subject =
	 * getObjectvalue(jsonObject3, "Subject"); String mHistory_Buyername =
	 * getObjectvalue(jsonObject4, "Name"); String mHistory_Buyerimage =
	 * getObjectvalue(jsonObject4,"Image"); String mHistory_Manufacturename =
	 * getObjectvalue(manufacture,"Name"); String mHistory_productname =
	 * getObjectvalue(product,"ProductName"); String mHistory_productimage =
	 * getObjectvalue(product,"ProductImage");
	 * 
	 * JSONArray jsonArray = jsonObject3.getJSONArray("ChatData");
	 * 
	 * for (int i = 0; i < jsonArray.length(); i++) { QuotationDashboardHistory
	 * quotationDashboardHistory = new QuotationDashboardHistory();
	 * 
	 * JSONObject jsonObject5 = jsonArray.getJSONObject(i);
	 * 
	 * quotationDashboardHistory.setHistory_createddate(mHistory_createddate);
	 * quotationDashboardHistory.setHistory_id(mHistory_id);
	 * quotationDashboardHistory
	 * .setHistory_isexternalproduct(mHistory_isexternalproduct);
	 * quotationDashboardHistory
	 * .setHistory_LastResponseDate(mHistory_LastResponseDate);
	 * quotationDashboardHistory
	 * .setHistory_LastResponseSequenceNumber(mHistory_LastResponseSequenceNumber
	 * ); quotationDashboardHistory.setHistory_subject(mHistory_subject);
	 * quotationDashboardHistory.setHistory_Buyername(mHistory_Buyername);
	 * quotationDashboardHistory.setHistory_Buyerimage(mHistory_Buyerimage);
	 * quotationDashboardHistory
	 * .setHistory_Manufacturename(mHistory_Manufacturename);
	 * quotationDashboardHistory.setHistory_productname(mHistory_productname);
	 * quotationDashboardHistory.setHistory_productimage(mHistory_productimage);
	 * 
	 * quotationDashboardHistory.setChat_message(getObjectvalue(jsonObject5,
	 * "Message"));
	 * quotationDashboardHistory.setOffered_price(getObjectvalue(jsonObject5,
	 * "OfferedPrize"));
	 * quotationDashboardHistory.setChat_RequiredQty(getObjectvalue
	 * (jsonObject5,"RequiredQty"));
	 * quotationDashboardHistory.setChat_ResponseDate
	 * (getObjectvalue(jsonObject5,"ResponseDate"));
	 * quotationDashboardHistory.setChat_ResponseUserType
	 * (getObjectvalue(jsonObject5, "ResponseUserType"));
	 * 
	 * JSONObject jSpecsObj =jsonObject5.getJSONObject("Specs"); JSONArray
	 * jMileStonesArr = null; JSONArray jAdditionalInfoArr = null; try{
	 * jMileStonesArr = jSpecsObj.getJSONArray("MileStones"); jAdditionalInfoArr
	 * = jSpecsObj.getJSONArray("AdditionalInfo"); }catch(Exception e){
	 * e.printStackTrace(); } int additionalinfolength = 0;
	 * if(jAdditionalInfoArr != null){ jAdditionalInfoArr.length(); } int
	 * milestonelength = 0; if(jMileStonesArr != null){ milestonelength =
	 * jMileStonesArr.length(); } String mAdditionalInfo = ""; for(int k=0;
	 * k<additionalinfolength; k++){ String aInfo =
	 * jAdditionalInfoArr.getString(k); if(mAdditionalInfo.length() == 0){
	 * mAdditionalInfo +=aInfo; }else{ mAdditionalInfo+=","+aInfo; } }
	 * quotationDashboardHistory.setmAdditionalInfo(mAdditionalInfo);
	 * List<Milestones> mMilestoneList = null; if(milestonelength>0){
	 * mMilestoneList = new ArrayList<Milestones>(); } for(int j=0;
	 * j<milestonelength; j++){ Milestones ms = new Milestones(); JSONObject
	 * milestone = jMileStonesArr.getJSONObject(j); String mName =
	 * milestone.getString("Name"); String mBriefDescription =
	 * milestone.getString("BriefDescription"); String mPercentBreakup =
	 * milestone.getString("PercentBreakup");
	 * 
	 * ms.setmQuotationHistoryId(mHistory_id);
	 * ms.setmBriefDescription(mBriefDescription); ms.setmName(mName);
	 * ms.setmPercentBreakup(mPercentBreakup); mMilestoneList.add(ms); }
	 * quotationDashboardHistory.setmMilestoneList(mMilestoneList);
	 * list.add(quotationDashboardHistory); }
	 * 
	 * } else { Toast.makeText(mContext, "Parsing Error",
	 * Toast.LENGTH_LONG).show(); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); // Toast.makeText(mContext,
	 * "Parsing Catch Error", Toast.LENGTH_LONG).show(); }
	 * 
	 * return list;
	 * 
	 * }
	 */

	/*
	 * public ArrayList<QuotationDashboardHistory> parseAllQuotationHistory(
	 * String mResult) { ArrayList<QuotationDashboardHistory> list = new
	 * ArrayList<QuotationDashboardHistory>(); try { JSONObject jsonObject = new
	 * JSONObject(mResult); JSONObject jsonObject2 =
	 * jsonObject.getJSONObject("GetQuotationHistoryResult");
	 * 
	 * String Iserror = jsonObject2.getString("IsError");
	 * 
	 * if (Iserror.equalsIgnoreCase("false")) { JSONObject jsonObject3 =
	 * jsonObject2.getJSONObject("ResponseObject"); JSONObject manufacture =
	 * jsonObject3.getJSONObject("Manufacturer"); JSONObject product =
	 * jsonObject3.getJSONObject("Product"); JSONObject jsonObject4 =
	 * jsonObject3.getJSONObject("Buyer");
	 * 
	 * String mHistory_createddate = getObjectvalue(jsonObject3,"CreatedDate");
	 * String mHistory_id = getObjectvalue(jsonObject3, "Id"); String
	 * mHistory_isexternalproduct =
	 * getObjectvalue(jsonObject3,"IsExternalProduct"); String
	 * mHistory_LastResponseDate =
	 * getObjectvalue(jsonObject3,"LastResponseDate"); String
	 * mHistory_LastResponseSequenceNumber = getObjectvalue(jsonObject3,
	 * "LastResponseSequenceNumber"); String mHistory_lastManufacturerResponseId
	 * = getObjectvalue(jsonObject3, "LastManufacturerResponseId"); String
	 * mHistory_quotationStatusId = getObjectvalue(jsonObject3,
	 * "QuotationStatusId"); String mHistory_subject =
	 * getObjectvalue(jsonObject3, "Subject"); String mHistory_Buyername =
	 * getObjectvalue(jsonObject4, "Name"); String mHistory_Buyerimage =
	 * getObjectvalue(jsonObject4,"Image"); String mHistory_Manufacturename =
	 * getObjectvalue(manufacture,"Name"); String mHistory_productname =
	 * getObjectvalue(product,"ProductName"); String mHistory_productimage =
	 * getObjectvalue(product,"ProductImage"); JSONArray jsonArray =
	 * jsonObject3.getJSONArray("ChatData");
	 * 
	 * for (int i = 0; i < jsonArray.length(); i++) { QuotationDashboardHistory
	 * quotationDashboardHistory = new QuotationDashboardHistory();
	 * 
	 * JSONObject jsonObject5 = jsonArray.getJSONObject(i);
	 * 
	 * quotationDashboardHistory.setHistory_createddate(mHistory_createddate);
	 * quotationDashboardHistory.setHistory_id(mHistory_id);
	 * quotationDashboardHistory
	 * .setHistory_isexternalproduct(mHistory_isexternalproduct);
	 * quotationDashboardHistory
	 * .setHistory_LastResponseDate(mHistory_LastResponseDate);
	 * quotationDashboardHistory
	 * .setHistory_LastResponseSequenceNumber(mHistory_LastResponseSequenceNumber
	 * ); quotationDashboardHistory.setHistory_LastManufacturerResponseId(
	 * mHistory_lastManufacturerResponseId);
	 * quotationDashboardHistory.setHistory_subject(mHistory_subject);
	 * quotationDashboardHistory.setHistory_Buyername(mHistory_Buyername);
	 * quotationDashboardHistory.setHistory_Buyerimage(mHistory_Buyerimage);
	 * quotationDashboardHistory
	 * .setHistory_Manufacturename(mHistory_Manufacturename);
	 * quotationDashboardHistory.setHistory_productname(mHistory_productname);
	 * quotationDashboardHistory.setHistory_productimage(mHistory_productimage);
	 * quotationDashboardHistory
	 * .setHistory_QuotationStatusId(mHistory_quotationStatusId);
	 * 
	 * quotationDashboardHistory.setChat_message(getObjectvalue(jsonObject5,
	 * "Message"));
	 * quotationDashboardHistory.setOffered_price(getObjectvalue(jsonObject5,
	 * "OfferedPrize"));
	 * quotationDashboardHistory.setChat_RequiredQty(getObjectvalue
	 * (jsonObject5,"RequiredQty"));
	 * quotationDashboardHistory.setChat_ResponseDate
	 * (getObjectvalue(jsonObject5,"ResponseDate"));
	 * quotationDashboardHistory.setChat_ResponseUserType
	 * (getObjectvalue(jsonObject5, "ResponseUserType"));
	 * quotationDashboardHistory.setmAttachment(getObjectvalue(jsonObject5,
	 * "Attachments"));
	 * quotationDashboardHistory.setHistory_QuotationResponseId(
	 * getObjectvalue(jsonObject5, "QuotationResponseId"));
	 * quotationDashboardHistory
	 * .setChat_UnitsOfMeasurement(jsonObject5.getString("UnitsOfMeasurement"));
	 * 
	 * JSONObject jSpecsObj =jsonObject5.getJSONObject("Specs");
	 * if(jSpecsObj==null || jSpecsObj.length()==0){
	 * 
	 * }else{
	 * 
	 * }
	 * 
	 * JSONArray additioninfoArray=jSpecsObj.getJSONArray("AdditionalInfo");
	 * String mAdditionalInfo = ""; if(additioninfoArray==null ||
	 * additioninfoArray.length()==0){ }else{
	 * 
	 * for(int k=0; k<additioninfoArray.length(); k++){ String aInfo =
	 * additioninfoArray.getString(k); if(mAdditionalInfo.length() == 0){
	 * mAdditionalInfo +=aInfo; }else{ mAdditionalInfo+=","+aInfo; } } }
	 * 
	 * JSONArray jMileStonesArr = null; JSONArray jAdditionalInfoArr = null;
	 * try{ jMileStonesArr = jSpecsObj.getJSONArray("MileStones");
	 * jAdditionalInfoArr = jSpecsObj.getJSONArray("AdditionalInfo");
	 * }catch(Exception e){ e.printStackTrace(); } int additionalinfolength = 0;
	 * if(jAdditionalInfoArr != null){ jAdditionalInfoArr.length(); } int
	 * milestonelength = 0; if(jMileStonesArr != null){ milestonelength =
	 * jMileStonesArr.length(); } String mAdditionalInfo = ""; for(int k=0;
	 * k<additionalinfolength; k++){ String aInfo =
	 * jAdditionalInfoArr.getString(k); if(mAdditionalInfo.length() == 0){
	 * mAdditionalInfo +=aInfo; }else{ mAdditionalInfo+=","+aInfo; } }
	 * quotationDashboardHistory.setmAdditionalInfo(mAdditionalInfo);
	 * List<Milestones> mMilestoneList = null; if(milestonelength>0){
	 * mMilestoneList = new ArrayList<Milestones>(); } for(int j=0;
	 * j<milestonelength; j++){ Milestones ms = new Milestones(); JSONObject
	 * milestone = jMileStonesArr.getJSONObject(j); String mName =
	 * milestone.getString("Name"); String mBriefDescription =
	 * milestone.getString("BriefDescription"); String mPercentBreakup =
	 * milestone.getString("PercentBreakup");
	 * 
	 * ms.setmQuotationHistoryId(mHistory_id);
	 * ms.setmBriefDescription(mBriefDescription); ms.setmName(mName);
	 * ms.setmPercentBreakup(mPercentBreakup); mMilestoneList.add(ms); }
	 * quotationDashboardHistory.setmMilestoneList(mMilestoneList);
	 * list.add(quotationDashboardHistory); }
	 * 
	 * } else { Toast.makeText(mContext, "Parsing Error",
	 * Toast.LENGTH_LONG).show(); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); // Toast.makeText(mContext,
	 * "Parsing Catch Error", Toast.LENGTH_LONG).show(); }
	 * 
	 * return list;
	 * 
	 * }
	 */

	public ArrayList<QuotationDashboardHistory> parseAllQuotationHistory(
			String mResult) {
		ArrayList<QuotationDashboardHistory> list = new ArrayList<QuotationDashboardHistory>();
		try {
			JSONObject jsonObject = new JSONObject(mResult);
			JSONObject jsonObject2 = jsonObject
					.getJSONObject("GetQuotationHistoryResult");
			String Iserror = jsonObject2.getString("IsError");
			if (Iserror.equalsIgnoreCase("false")) {
				JSONObject jsonObject3 = jsonObject2
						.getJSONObject("ResponseObject");
				JSONObject manufacture = jsonObject3
						.getJSONObject("Manufacturer");
				JSONObject product = jsonObject3.getJSONObject("Product");
				JSONObject jsonObject4 = jsonObject3.getJSONObject("Buyer");

				String mHistory_createddate = getObjectvalue(jsonObject3,
						"CreatedDate");
				String mHistory_id = getObjectvalue(jsonObject3, "Id");
				String mHistory_isexternalproduct = getObjectvalue(jsonObject3,
						"IsExternalProduct");
				String mHistory_LastResponseDate = getObjectvalue(jsonObject3,
						"LastResponseDate");
				String mHistory_LastResponseSequenceNumber = getObjectvalue(
						jsonObject3, "LastResponseSequenceNumber");
				String mHistory_lastManufacturerResponseId = getObjectvalue(
						jsonObject3, "LastManufacturerResponseId");
				String mHistory_quotationStatusId = getObjectvalue(jsonObject3,
						"QuotationStatusId");
				String mHistory_subject = getObjectvalue(jsonObject3, "Subject");
				String mHistory_Quotation_Number = getObjectvalue(jsonObject3,
						"QuotationNumber");
				int mHistory_Service_Grouptype = Integer
						.parseInt(getObjectvalue(jsonObject3,
								"ServiceGroupType"));
				String mHistory_Shipping_Address_id = getObjectvalue(
						jsonObject3, "ShippingAddressId");
				String mHistory_Billing_Address_id = getObjectvalue(
						jsonObject3, "BillingAddressId");
				String mHistory_Buyername = getObjectvalue(jsonObject4, "Name");
				String mHistory_Buyerimage = getObjectvalue(jsonObject4,
						"Image");
				String mHistory_Manufacturename = getObjectvalue(manufacture,
						"Name");
				String mHistory_productname = getObjectvalue(product,
						"ProductName");
				String mHistory_productimage = getObjectvalue(product,
						"ProductImage");
				JSONArray jsonArray = jsonObject3.getJSONArray("ChatData");

				for (int i = 0; i < jsonArray.length(); i++) {
					QuotationDashboardHistory quotationDashboardHistory = new QuotationDashboardHistory();
					JSONObject jsonObject5 = jsonArray.getJSONObject(i);

					quotationDashboardHistory
							.setHistory_createddate(mHistory_createddate);
					quotationDashboardHistory.setHistory_id(mHistory_id);
					quotationDashboardHistory
							.setHistory_isexternalproduct(mHistory_isexternalproduct);
					quotationDashboardHistory
							.setHistory_LastResponseDate(mHistory_LastResponseDate);
					quotationDashboardHistory
							.setHistory_LastResponseSequenceNumber(mHistory_LastResponseSequenceNumber);
					quotationDashboardHistory
							.setHistory_LastManufacturerResponseId(mHistory_lastManufacturerResponseId);
					quotationDashboardHistory
							.setHistory_subject(mHistory_subject);
					quotationDashboardHistory
							.setHistory_Buyername(mHistory_Buyername);
					quotationDashboardHistory
							.setHistory_Buyerimage(mHistory_Buyerimage);
					quotationDashboardHistory
							.setHistory_Manufacturename(mHistory_Manufacturename);
					quotationDashboardHistory
							.setHistory_productname(mHistory_productname);
					quotationDashboardHistory
							.setHistory_productimage(mHistory_productimage);
					quotationDashboardHistory
							.setHistory_QuotationStatusId(mHistory_quotationStatusId);
					quotationDashboardHistory
							.setHistory_Quotation_Number(mHistory_Quotation_Number);
					quotationDashboardHistory
							.setHistory_Service_Grouptype(mHistory_Service_Grouptype);
					quotationDashboardHistory
							.setHistory_Shipping_Address_id(mHistory_Shipping_Address_id);
					quotationDashboardHistory
							.setHistory_Billing_Address_id(mHistory_Billing_Address_id);

					quotationDashboardHistory.setChat_message(getObjectvalue(
							jsonObject5, "Message"));
					quotationDashboardHistory.setOffered_price(getObjectvalue(
							jsonObject5, "OfferedPrize"));
					quotationDashboardHistory
							.setChat_RequiredQty(getObjectvalue(jsonObject5,
									"RequiredQty"));
					quotationDashboardHistory
							.setChat_ResponseDate(getObjectvalue(jsonObject5,
									"ResponseDate"));
					quotationDashboardHistory
							.setChat_ResponseUserType(getObjectvalue(
									jsonObject5, "ResponseUserType"));
					String responseusertype = getObjectvalue(jsonObject5,
							"ResponseUserType");
					quotationDashboardHistory.setmAttachment(getObjectvalue(
							jsonObject5, "Attachments"));
					quotationDashboardHistory
							.setHistory_QuotationResponseId(getObjectvalue(
									jsonObject5, "QuotationResponseId"));
					quotationDashboardHistory
							.setChat_UnitsOfMeasurement(jsonObject5
									.getString("UnitsOfMeasurement"));

					if (mHistory_Service_Grouptype == 1) {
						JSONObject SpecsObject = jsonObject5
								.getJSONObject("Specs");
						if (SpecsObject == null || SpecsObject.length() == 0) {
						} else {
							JSONArray additioninfoArray = SpecsObject
									.getJSONArray("AdditionalInfo");
							String mAdditionalInfo = "";
							if (additioninfoArray == null
									|| additioninfoArray.length() == 0) {
							} else {
								for (int k = 0; k < additioninfoArray.length(); k++) {
									String aInfo = additioninfoArray
											.getString(k);
									if (mAdditionalInfo.length() == 0) {
										mAdditionalInfo += aInfo;
									} else {
										mAdditionalInfo += "," + aInfo;
									}
								}
							}
							quotationDashboardHistory
									.setmAdditionalInfo(mAdditionalInfo);

							JSONArray jMileStonesArr = null;
							try {
								jMileStonesArr = SpecsObject
										.getJSONArray("MileStones");
							} catch (Exception e) {
								e.printStackTrace();
							}
							int milestonelength = 0;
							if (jMileStonesArr != null) {
								milestonelength = jMileStonesArr.length();
							}
							List<Milestones> mMilestoneList = null;
							if (milestonelength > 0) {
								mMilestoneList = new ArrayList<Milestones>();
							}
							for (int j = 0; j < milestonelength; j++) {
								Milestones ms = new Milestones();
								JSONObject milestone = jMileStonesArr
										.getJSONObject(j);
								String milestone_order = milestone
										.getString("MileStoneOrder");
								String mName = milestone.getString("Name");
								String mPercentBreakup = milestone
										.getString("PercentBreakup");
								String mDate = milestone.getString("NoOfWeeks");
								ms.setmQuotationHistoryId(mHistory_id);
								ms.setmName(mName);
								ms.setmPercentBreakup(mPercentBreakup);
								ms.setmBriefDescription(mDate);
								mMilestoneList.add(ms);
							}
							quotationDashboardHistory
									.setmMilestoneList(mMilestoneList);

						}
						list.add(quotationDashboardHistory);
					} else if (mHistory_Service_Grouptype == 2) {
						if (responseusertype.equalsIgnoreCase("2")) {
						} else {
							JSONObject shipping = jsonObject5
									.getJSONObject("ShippingAttributes");
							if (shipping == null || shipping.length() == 0) {
								quotationDashboardHistory
										.setmShipping_pickup(getObjectvalue(
												shipping, "PickUp"));
							} else {
								quotationDashboardHistory
										.setmShipping_DeliveryDate(getObjectvalue(
												shipping, "DeliveryDate"));
								quotationDashboardHistory
										.setmShipping_FromCity(getObjectvalue(
												shipping, "FromCity"));
								quotationDashboardHistory
										.setmShipping_FromCountry(getObjectvalue(
												shipping, "FromCountry"));
								quotationDashboardHistory
										.setmShipping_ToCity(getObjectvalue(
												shipping, "ToCity"));
								quotationDashboardHistory
										.setmShipping_ToCountry(getObjectvalue(
												shipping, "ToCountry"));
								quotationDashboardHistory
										.setmShipping_pickup(getObjectvalue(
												shipping, "PickUp"));

								JSONObject DimensionLwhObject = shipping
										.getJSONObject("DimensionLwh");
								if (DimensionLwhObject == null
										|| DimensionLwhObject.length() == 0) {
								} else {
									quotationDashboardHistory
											.setmWidth(getObjectvalue(
													DimensionLwhObject, "Width"));
									quotationDashboardHistory
											.setmHeight(getObjectvalue(
													DimensionLwhObject,
													"Height"));
									quotationDashboardHistory
											.setmLength(getObjectvalue(
													DimensionLwhObject,
													"Length"));
								}
								JSONObject DimensionWeightObject = shipping
										.getJSONObject("DimensionWeight");
								if (DimensionWeightObject == null
										|| DimensionWeightObject.length() == 0) {
								} else {
									quotationDashboardHistory
											.setmWeight(getObjectvalue(
													DimensionWeightObject,
													"Weight"));
								}
								quotationDashboardHistory
										.setmTotalNumberOfCatons(getObjectvalue(
												shipping, "TotalNumberOfCatons"));
								quotationDashboardHistory
										.setmTotalWeight(getObjectvalue(
												shipping, "TotalWeight"));
							}
						}
						list.add(quotationDashboardHistory);
					} else if (mHistory_Service_Grouptype == 3) {
						if (responseusertype.equalsIgnoreCase("2")) {
						} else {
							JSONObject inspection = jsonObject5
									.getJSONObject("InspectionAttirbutes");

							JSONObject InspectionTypeObject = inspection
									.getJSONObject("InspectionType");
							if (InspectionTypeObject == null
									|| InspectionTypeObject.length() == 0) {
							} else {
								quotationDashboardHistory
										.setmInspectionTypeId(getObjectvalue(
												InspectionTypeObject, "Id"));
								quotationDashboardHistory
										.setmInspectionType(getObjectvalue(
												InspectionTypeObject, "Value"));
							}

							JSONObject InspectorTypeObject = inspection
									.getJSONObject("InspectorType");
							if (InspectorTypeObject == null
									|| InspectorTypeObject.length() == 0) {
							} else {
								quotationDashboardHistory
										.setmInspectorTypeId(getObjectvalue(
												InspectorTypeObject, "Id"));
								quotationDashboardHistory
										.setmInspectorType(getObjectvalue(
												InspectorTypeObject, "Value"));
							}

							JSONObject TypeOfIndustryObject = inspection
									.getJSONObject("TypeOfIndustry");
							if (TypeOfIndustryObject == null
									|| TypeOfIndustryObject.length() == 0) {
							} else {
								quotationDashboardHistory
										.setmTypeOfIndustryId(getObjectvalue(
												TypeOfIndustryObject, "Id"));
								quotationDashboardHistory
										.setmTypeOfIndustry(getObjectvalue(
												TypeOfIndustryObject, "Value"));
							}

							JSONObject PlaceToInspectObject = inspection
									.getJSONObject("PlaceToInspect");
							if (PlaceToInspectObject == null
									|| PlaceToInspectObject.length() == 0) {

							} else {
								quotationDashboardHistory
										.setmCountryName(getObjectvalue(
												PlaceToInspectObject,
												"CountryName"));
								quotationDashboardHistory
										.setmCountryId(getObjectvalue(
												PlaceToInspectObject,
												"CountryId"));
								quotationDashboardHistory
										.setmStateId(getObjectvalue(
												PlaceToInspectObject, "StateId"));
								quotationDashboardHistory
										.setmStateName(getObjectvalue(
												PlaceToInspectObject,
												"StateName"));
								quotationDashboardHistory
										.setmAddress(getObjectvalue(
												PlaceToInspectObject,
												"Address1"));
								quotationDashboardHistory
										.setmCity(getObjectvalue(
												PlaceToInspectObject, "City"));
							}
						}
						list.add(quotationDashboardHistory);
					} else if (mHistory_Service_Grouptype == 4) {
						if (responseusertype.equalsIgnoreCase("2")) {
						} else {
							JSONObject custom = jsonObject5
									.getJSONObject("CustomAttributes");
							JSONObject CategoryObject = custom
									.getJSONObject("Category");
							if (CategoryObject == null
									|| CategoryObject.length() == 0) {
							} else {
								quotationDashboardHistory
										.setmCategoryId(getObjectvalue(
												CategoryObject, "Id"));
								quotationDashboardHistory
										.setmCategoryName(getObjectvalue(
												CategoryObject, "Value"));
							}

							JSONObject SubCategoryObject = custom
									.getJSONObject("SubCategory");
							if (SubCategoryObject == null
									|| SubCategoryObject.length() == 0) {
							} else {
								quotationDashboardHistory
										.setmSubCategoryId(getObjectvalue(
												SubCategoryObject, "Id"));
								quotationDashboardHistory
										.setmSubCategoryName(getObjectvalue(
												SubCategoryObject, "Value"));

							}

							JSONObject AddressObject = custom
									.getJSONObject("Address");
							if (AddressObject == null
									|| AddressObject.length() == 0) {
							} else {
								quotationDashboardHistory
										.setmAddress(getObjectvalue(
												AddressObject, "Address1"));
								quotationDashboardHistory
										.setmCity(getObjectvalue(AddressObject,
												"City"));
								quotationDashboardHistory
										.setmCountryId(getObjectvalue(
												AddressObject, "CountryId"));
								quotationDashboardHistory
										.setmCountryName(getObjectvalue(
												AddressObject, "CountryName"));
								quotationDashboardHistory
										.setmStateId(getObjectvalue(
												AddressObject, "StateId"));
								quotationDashboardHistory
										.setmStateName(getObjectvalue(
												AddressObject, "StateName"));
								quotationDashboardHistory
										.setmCompanyName(getObjectvalue(
												AddressObject, "Company"));
							}

							JSONObject QuantityObject = custom
									.getJSONObject("Quantity");
							if (QuantityObject == null
									|| QuantityObject.length() == 0) {
							} else {
								quotationDashboardHistory
										.setmQuantity(getObjectvalue(
												QuantityObject, "Quantity"));
								JSONObject UnitOfMeasurementObject = QuantityObject
										.getJSONObject("UnitOfMeasurement");
								if (UnitOfMeasurementObject == null
										|| UnitOfMeasurementObject.length() == 0) {
								} else {
									quotationDashboardHistory
											.setChat_UnitsOfMeasurement(getObjectvalue(
													UnitOfMeasurementObject,
													"Value"));
								}
							}
						}
						list.add(quotationDashboardHistory);
					} else if (mHistory_Service_Grouptype == 7) {
						if (responseusertype.equalsIgnoreCase("2")) {
						} else {
							JSONObject warehouse = jsonObject5
									.getJSONObject("WareHouseAttributes");
							JSONObject AreaRequirementObject = warehouse
									.getJSONObject("AreaRequirement");
							if (AreaRequirementObject == null
									|| AreaRequirementObject.length() == 0) {
							} else {
								quotationDashboardHistory
										.setmHeight(getObjectvalue(
												AreaRequirementObject, "Height"));
								quotationDashboardHistory
										.setmLength(getObjectvalue(
												AreaRequirementObject, "Length"));
								quotationDashboardHistory
										.setmWeight(getObjectvalue(
												AreaRequirementObject, "Weight"));
								quotationDashboardHistory
										.setmWidth(getObjectvalue(
												AreaRequirementObject, "Width"));
							}
							JSONObject WareHouseAddressObject = warehouse
									.getJSONObject("WareHouseAddress");
							if (WareHouseAddressObject == null
									|| WareHouseAddressObject.length() == 0) {
							} else {
								quotationDashboardHistory
										.setmCity(getObjectvalue(
												WareHouseAddressObject, "City"));
							}
						}
						list.add(quotationDashboardHistory);
					} else if (mHistory_Service_Grouptype == 6) {
					}
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String parseUpdateQuotationResult(String mResult) {
		String result = null;

		try {
			JSONObject jsonObject = new JSONObject(mResult);
			JSONObject jsonObject2 = jsonObject
					.getJSONObject("UpdateQuotationResult");
			result = jsonObject2.getString("ResponseObject");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;

	}

	public String[] parseAcceptQuotation(String resultOutparam) {
		String[] mResult = new String[3];
		try {
			JSONObject jObject = new JSONObject(resultOutparam);
			JSONObject resObj = jObject.getJSONObject("AcceptQuotationResult");
			JSONObject responceObject = resObj.getJSONObject("ResponseObject");

			mResult[0] = resObj.getString("IsError");
			mResult[1] = responceObject.getString("OrderId");
			mResult[2] = responceObject.getString("OrderNumber");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mResult;
	}

	public String parseProductReviewResult(String mResult) {
		String result = null;

		try {
			JSONObject jsonObject = new JSONObject(mResult);
			JSONObject jsonObject2 = jsonObject
					.getJSONObject("SaveProductReviewResult");
			result = jsonObject2.getString("ResponseObject");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;

	}

	public String parseManufacturerReviewResult(String mResult) {
		String result = null;

		try {
			JSONObject jsonObject = new JSONObject(mResult);
			JSONObject jsonObject2 = jsonObject
					.getJSONObject("SaveManufacturerReviewResult");
			result = jsonObject2.getString("ResponseObject");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;

	}

	public String[] parseQuotationResult(String resultOutparam, Context context) {
		String[] mResult = new String[2];
		try {
			JSONObject jObject = new JSONObject(resultOutparam);
			JSONObject resObj = jObject.getJSONObject("SaveQuotationResult");
			mResult[0] = resObj.getString("ResponseObject");
			mResult[1] = resObj.getString("IsError");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mResult;
	}

	public String[] parseMoreInfoResult(String resultOutparam, Context context) {
		String[] mResult = new String[2];
		try {
			JSONObject jObject = new JSONObject(resultOutparam);
			JSONObject resObj = jObject
					.getJSONObject("CreateAdditionalInfoResult");
			mResult[0] = resObj.getString("ResponseObject");
			mResult[1] = resObj.getString("IsError");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mResult;
	}

	/*
	 * public List<ProductList> parseSearchDetails(String resultOutparam,Context
	 * applicationContext) { List<ProductList> mProductDetails=new
	 * ArrayList<ProductList>(); try { JSONObject jObject = new
	 * JSONObject(resultOutparam); JSONObject listObject =
	 * jObject.getJSONObject("SearchResult"); JSONObject resObject =
	 * listObject.getJSONObject("ResponseObject"); if(resObject==null ||
	 * resObject.length()==0){ }else{ String
	 * total_count=getObjectvalue(resObject, "TotalCount"); JSONArray
	 * productArray=resObject.getJSONArray("Products"); if(productArray==null ||
	 * productArray.length()==0){ }else{ for (int i = 0; i <
	 * productArray.length(); i++) { JSONObject
	 * productObject=productArray.getJSONObject(i); ProductList mProduct=new
	 * ProductList();
	 * mProduct.setmProduct_DiscountAmount(getObjectvalue(productObject,
	 * "DiscountAmount"));
	 * mProduct.setmProduct_DiscountPercentage(getObjectvalue(productObject,
	 * "DiscountPercentage"));
	 * mProduct.setmProduct_Id(getObjectvalue(productObject, "ProductId"));
	 * mProduct.setmProduct_Name(getObjectvalue(productObject, "ProductName"));
	 * mProduct.setmProduct_Image(getObjectvalue(productObject,
	 * "ProductPicture"));
	 * mProduct.setmProduct_OfferPrice(getObjectvalue(productObject,
	 * "OfferPrice"));
	 * 
	 * JSONObject manufacturerObject=productObject.getJSONObject("Manufacture");
	 * if(manufacturerObject==null || manufacturerObject.length()==0){ }else{
	 * mProduct.setmManufacturer_Id(getObjectvalue(manufacturerObject, "Id"));
	 * mProduct.setmManufacturer_Name(getObjectvalue(manufacturerObject,
	 * "Name"));
	 * mProduct.setmManufacturer_Image(getObjectvalue(manufacturerObject,
	 * "Logo")); } mProduct.setmManufacturer_Likes(getObjectvalue(productObject,
	 * "LikesCount"));
	 * mProduct.setmManufacturer_Feedback(getObjectvalue(productObject,
	 * "FeedbackCount")); mProduct.setmProductTotalCount(total_count);
	 * mProductDetails.add(mProduct); } }
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); } return mProductDetails;
	 * }
	 */

	public List<ProductList> parseSearchDetails(String resultOutparam,
			Context applicationContext) {
		List<ProductList> mSearchProductDetails = new ArrayList<ProductList>();
		try {
			JSONObject jObject = new JSONObject(resultOutparam);
			JSONObject SearchResultObject = jObject
					.getJSONObject("SearchMResult");
			String IsError = getObjectvalue(SearchResultObject, "IsError");
			if (IsError.equalsIgnoreCase("false")) {
				JSONObject ResponseObject = SearchResultObject
						.getJSONObject("ResponseObject");
				if (ResponseObject == null || ResponseObject.length() == 0) {
				} else {
					String total_count = getObjectvalue(ResponseObject,
							"TotalCount");
					JSONArray SearchResponsePropertiesArray = ResponseObject
							.getJSONArray("Products");
					if (SearchResponsePropertiesArray == null
							|| SearchResponsePropertiesArray.length() == 0) {
					} else {
						for (int i = 0; i < SearchResponsePropertiesArray
								.length(); i++) {
							JSONObject SearchResponsePropertiesObject = SearchResponsePropertiesArray
									.getJSONObject(i);
							ProductList mSearchProduct = new ProductList();

							mSearchProduct
									.setmProduct_OfferPrice(getObjectvalue(
											SearchResponsePropertiesObject,
											"OfferPrice"));
							mSearchProduct.setmProductPrice(getObjectvalue(
									SearchResponsePropertiesObject, "Price"));
							mSearchProduct
									.setmProduct_Id(getObjectvalue(
											SearchResponsePropertiesObject,
											"ProductId"));
							mSearchProduct.setmProduct_Name(getObjectvalue(
									SearchResponsePropertiesObject,
									"ProductName"));
							mSearchProduct.setmProduct_Image(getObjectvalue(
									SearchResponsePropertiesObject,
									"ProductPicture"));
							mSearchProduct
									.setmProductQuantity(getObjectvalue(
											SearchResponsePropertiesObject,
											"Quantity"));
							mSearchProduct
									.setmProductShortDescription(getObjectvalue(
											SearchResponsePropertiesObject,
											"ShortDescription"));

							JSONObject ManufacturerObject = SearchResponsePropertiesObject
									.getJSONObject("Manufacture");
							if (ManufacturerObject == null
									|| ManufacturerObject.length() == 0) {
							} else {
								mSearchProduct
										.setmManufacturer_Id(getObjectvalue(
												ManufacturerObject, "Id"));
								mSearchProduct
										.setmManufacturer_Name(getObjectvalue(
												ManufacturerObject, "Name"));

								JSONObject Manufacture_Logo = ManufacturerObject
										.getJSONObject("Logo");
								if (Manufacture_Logo == null
										|| Manufacture_Logo.length() == 0) {

								} else {
									mSearchProduct
											.setmManufacturer_Image(getObjectvalue(
													Manufacture_Logo, "url"));
								}
							}

							/*
							 * JSONObject
							 * MOQObject=SearchResponsePropertiesObject
							 * .getJSONObject("MOQ"); if(MOQObject==null ||
							 * MOQObject.length()==0){ }else{
							 * mSearchProduct.setmProductPrice
							 * (getObjectvalue(MOQObject, "Price"));
							 * mSearchProduct
							 * .setmUnitOfMeasurement(getObjectvalue(MOQObject,
							 * "UnitOfMeasurement"));
							 * mSearchProduct.setmProductQuantity
							 * (getObjectvalue(MOQObject, "Quantity")); //}
							 * 
							 * JSONObject
							 * ManufacturerObject=SearchResponsePropertiesObject
							 * .getJSONObject("Manufacturer");
							 * if(ManufacturerObject==null ||
							 * ManufacturerObject.length()==0){ }else{
							 * mSearchProduct
							 * .setmManufacturer_Name(getObjectvalue
							 * (ManufacturerObject, "Name"));
							 * mSearchProduct.setmManufacturer_Id
							 * (getObjectvalue(ManufacturerObject, "Id"));
							 * mSearchProduct
							 * .setmManufacturer_Image(getObjectvalue
							 * (ManufacturerObject, "Logo")); }
							 * 
							 * JSONObject
							 * ProductObject=SearchResponsePropertiesObject
							 * .getJSONObject("Product"); if(ProductObject==null
							 * || ProductObject.length()==0){ }else{
							 * mSearchProduct
							 * .setmProduct_Name(getObjectvalue(ProductObject,
							 * "ProductName"));
							 * mSearchProduct.setmProduct_Id(getObjectvalue
							 * (ProductObject, "Id"));
							 * mSearchProduct.setmProduct_Image
							 * (getObjectvalue(ProductObject,
							 * "ProductPicture")); }
							 */
							mSearchProduct.setmProductTotalCount(total_count);
							mSearchProductDetails.add(mSearchProduct);
						}
					}
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mSearchProductDetails;
	}

	public String parseAttachImageUrl(String result) {
		String imageUrl = null;
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject jsonObject2 = jsonObject.getJSONObject("ResponseObject");
			imageUrl = jsonObject2.getString("url");
		} catch (Exception e) {

		}
		return imageUrl;
	}

	public String parseSaveOpenQuotation(String result) {
		String resultsavequotation = null;
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject jsonObject2 = jsonObject
					.getJSONObject("SaveOpenQuoteResult");
			resultsavequotation = jsonObject2.getString("ResponseObject");
		} catch (Exception e) {

		}
		return resultsavequotation;
	}

	public String[] parserAuthentication(String resultOutParam,
			Context applicationContext) {
		String[] mResult = new String[2];
		try {
			JSONObject jsonObject = new JSONObject(resultOutParam);
			JSONObject resultObject = jsonObject
					.getJSONObject("ValidateUserResult");
			mResult[0] = resultObject.getString("IsError");
			mResult[1] = resultObject.getString("ResponseObject");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return mResult;
	}

	public UserDetails parseUserAuthentication(String resultOutParam,
			Context applicationContext) {
		UserDetails mUserDetails = new UserDetails();
		try {
			JSONObject jsonObject = new JSONObject(resultOutParam);
			JSONObject userObject = jsonObject
					.getJSONObject("ValidateUserResult");
			String IsError = getObjectvalue(userObject, "IsError");
			if (IsError.equalsIgnoreCase("false")) {
				JSONObject responseObject = userObject
						.getJSONObject("ResponseObject");
				if (responseObject == null || responseObject.length() == 0) {
				} else {
					mUserDetails.setmUserEmail(getObjectvalue(responseObject,
							"Email"));
					mUserDetails
							.setmUserID(getObjectvalue(responseObject, "Id"));
					mUserDetails.setmUserMobile(getObjectvalue(responseObject,
							"MobileNumber"));
					mUserDetails.setmUserName(getObjectvalue(responseObject,
							"UserName"));
					mUserDetails.setmUserImage(getObjectvalue(responseObject,
							"Image"));
					mUserDetails.setmIsActive(getObjectvalue(responseObject,
							"IsActive"));
					mUserDetails.setmManufacturerId(getObjectvalue(
							responseObject, "ManufacturerId"));
					mUserDetails
							.setmName(getObjectvalue(responseObject, "Name"));

					JSONObject userTypeObj = responseObject
							.getJSONObject("UserType");
					mUserDetails.setmUserTypeId(getObjectvalue(userTypeObj,
							"Id"));
					mUserDetails.setmUserType(getObjectvalue(userTypeObj,
							"Name"));
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mUserDetails;

	}

	/*
	 * public UserDetails parseUserDetails(String resultOutput,Context
	 * applicationContext) { UserDetails mUserDetails=new UserDetails(); try {
	 * JSONObject jsonObject = new JSONObject(resultOutput); JSONObject
	 * userObject = jsonObject.getJSONObject("UserDetailsResult");
	 * if(userObject==null || userObject.length()==0){ }else{ JSONObject
	 * responseObject = userObject.getJSONObject("ResponseObject");
	 * if(responseObject==null || responseObject.length()==0){ }else{
	 * mUserDetails.setmUserEmail(getObjectvalue(responseObject, "Email"));
	 * mUserDetails.setmUserID(getObjectvalue(responseObject, "Id"));
	 * mUserDetails.setmUserMobile(getObjectvalue(responseObject,
	 * "MobileNumber"));
	 * mUserDetails.setmUserName(getObjectvalue(responseObject, "UserName"));
	 * mUserDetails.setmUserImage(getObjectvalue(responseObject, "Image"));
	 * mUserDetails.setmIsActive(getObjectvalue(responseObject, "IsActive")); }
	 * } } catch (Exception e) { e.printStackTrace(); } return mUserDetails; }
	 */

	public UserDetails parseUserDetails(String resultOutput,
			Context applicationContext) {
		UserDetails mUserDetails = new UserDetails();
		try {
			JSONObject jsonObject = new JSONObject(resultOutput);
			JSONObject userObject = jsonObject
					.getJSONObject("UserDetailsResult");
			if (userObject == null || userObject.length() == 0) {
			} else {
				JSONObject responseObject = userObject
						.getJSONObject("ResponseObject");
				if (responseObject == null || responseObject.length() == 0) {
				} else {
					mUserDetails.setmUserEmail(getObjectvalue(responseObject,
							"Email"));
					mUserDetails
							.setmUserID(getObjectvalue(responseObject, "Id"));
					mUserDetails.setmUserMobile(getObjectvalue(responseObject,
							"MobileNumber"));
					mUserDetails.setmUserName(getObjectvalue(responseObject,
							"UserName"));
					mUserDetails.setmUserImage(getObjectvalue(responseObject,
							"Image"));
					mUserDetails.setmIsActive(getObjectvalue(responseObject,
							"IsActive"));
					mUserDetails.setmManufacturerId(getObjectvalue(
							responseObject, "ManufacturerId"));
					mUserDetails
							.setmName(getObjectvalue(responseObject, "Name"));

					JSONObject userTypeObj = responseObject
							.getJSONObject("UserType");
					mUserDetails.setmUserTypeId(getObjectvalue(userTypeObj,
							"Id"));
					mUserDetails.setmUserType(getObjectvalue(userTypeObj,
							"Name"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mUserDetails;
	}

	public List<Lookups> pareseLookups(String result) {
		List<Lookups> mLookupsList = new ArrayList<Lookups>();
		try {
			JSONObject json = new JSONObject(result);
			JSONObject lookups = json.getJSONObject("GetLookupsResult");
			String isError = lookups.getString("IsError");
			if (isError.equalsIgnoreCase("false")) {
				JSONArray jarr = lookups.getJSONArray("ResponseObject");
				for (int i = 0; i < jarr.length(); i++) {
					JSONObject jsn = jarr.getJSONObject(i);
					Lookups lookup = new Lookups();
					String mId = getObjectvalue(jsn, "Id");
					String mName = getObjectvalue(jsn, "Name");
					lookup.setmId(mId);
					lookup.setmName(mName);
					mLookupsList.add(lookup);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mLookupsList;
	}

	public String parseregUserDetails(String result) {
		String resultreguser = null;
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject jsonObject2 = jsonObject
					.getJSONObject("IsAccountExistResult");
			resultreguser = jsonObject2.getString("ResponseObject");
		} catch (Exception e) {

		}
		return resultreguser;
	}

	public String[] parseemailotp(String result) {
		String[] resultsavequotation = new String[2];
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject jsonObjectsendformat = jsonObject
					.getJSONObject("SendEmailFormatResult");

			JSONObject jsonObjectresponse = jsonObjectsendformat
					.getJSONObject("ResponseObject");
			resultsavequotation[0] = jsonObjectresponse.getString("Message");
			resultsavequotation[1] = jsonObjectresponse.getString("Result");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return resultsavequotation;
	}

	public String parseregdetails(String result) {
		String resultreguser = null;
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject jsonObject2 = jsonObject
					.getJSONObject("UserRegistrationResult");
			resultreguser = jsonObject2.getString("ResponseObject");
		} catch (Exception e) {

		}
		return resultreguser;
	}

	// Order details parsing
	public QuickOrderResult parseQuickOrderResult(String result,
			String mJsonObjectName) {
		QuickOrderResult orderResult = null;
		try {
			JSONObject json = new JSONObject(result);
			JSONObject quickOrderResult = json.getJSONObject(mJsonObjectName);
			String isError = quickOrderResult.getString("IsError");
			if (isError.equals("false")) {
				orderResult = new QuickOrderResult();
				JSONArray responseArray = quickOrderResult
						.getJSONArray("ResponseObject");
				if (responseArray == null || responseArray.length() == 0) {
				} else {
					for (int i = 0; i < responseArray.length(); i++) {
						JSONObject responseObject = responseArray
								.getJSONObject(i);
						orderResult.setmBillingAddressId(getObjectvalue(
								responseObject, "BillingAddressId"));
						orderResult.setmQuotationId(getObjectvalue(
								responseObject, "Id"));
						orderResult.setmServiceGroupType(getObjectvalue(
								responseObject, "ServiceGroupType"));
						String ServiceGroupType = getObjectvalue(
								responseObject, "ServiceGroupType");
						orderResult.setmShippingAddressId(getObjectvalue(
								responseObject, "ShippingAddressId"));
						orderResult.setmTotalProductCost(getObjectvalue(
								responseObject, "SubTotal"));
						orderResult.setmSubject(getObjectvalue(responseObject,
								"Subject"));

						JSONObject buyerObject = responseObject
								.getJSONObject("Buyer");
						if (buyerObject == null || buyerObject.length() == 0) {
						} else {
							orderResult.setmAddress(getObjectvalue(buyerObject,
									"Address"));
							orderResult.setmAddress2(getObjectvalue(
									buyerObject, "Address2"));
							orderResult.setmCity(getObjectvalue(buyerObject,
									"City"));
							orderResult.setmCountry(getObjectvalue(buyerObject,
									"Country"));
							orderResult.setmEmail(getObjectvalue(buyerObject,
									"Email"));
							orderResult.setmFaxNumber(getObjectvalue(
									buyerObject, "FaxNumber"));
							orderResult.setmFirstName(getObjectvalue(
									buyerObject, "FirstName"));
							orderResult.setmLastName(getObjectvalue(
									buyerObject, "LastName"));
							orderResult.setmName(getObjectvalue(buyerObject,
									"Name"));
							orderResult.setmPhoneNumber(getObjectvalue(
									buyerObject, "PhoneNumber"));
							orderResult.setmState(getObjectvalue(buyerObject,
									"State"));
							orderResult.setmLastName(getObjectvalue(
									buyerObject, "UserName"));
							orderResult.setmZip(getObjectvalue(buyerObject,
									"Zip"));
						}

						JSONObject manufacturerObject = responseObject
								.getJSONObject("Manufacturer");
						if (manufacturerObject == null
								|| manufacturerObject.length() == 0) {
						} else {
							orderResult.setmManufacturerId(getObjectvalue(
									manufacturerObject, "Id"));
							orderResult.setmMAnufacturerName(getObjectvalue(
									manufacturerObject, "Name"));
							JSONObject logo = manufacturerObject
									.getJSONObject("Logo");
							if (logo == null || logo.length() == 0) {
							} else {
								orderResult
										.setmManufacturerImage(getObjectvalue(
												logo, "url"));
							}
							JSONObject contactDetails = manufacturerObject
									.getJSONObject("ContactDetails");
							if (contactDetails == null
									|| contactDetails.length() == 0) {
							} else {
								orderResult
										.setmManufacturerAddress(getObjectvalue(
												contactDetails, "Address"));
								orderResult
										.setmManufacturerEmail(getObjectvalue(
												contactDetails, "Email"));
								orderResult
										.setmManufacturerPhoneNumber(getObjectvalue(
												contactDetails, "PhoneNumber"));
							}
						}

						JSONObject productObject = responseObject
								.getJSONObject("Product");
						if (productObject == null
								|| productObject.length() == 0) {
						} else {
							orderResult.setmProductId(getObjectvalue(
									productObject, "Id"));
							orderResult.setmProductImage(getObjectvalue(
									productObject, "ProductImage"));
							orderResult.setmProductName(getObjectvalue(
									productObject, "ProductName"));
						}

						JSONArray chatArray = responseObject
								.getJSONArray("ChatData");
						if (chatArray == null || chatArray.length() == 0) {
						} else {
							for (int j = 0; j < chatArray.length(); j++) {
								JSONObject chatObject = chatArray
										.getJSONObject(j);
								orderResult.setmMessage(getObjectvalue(
										chatObject, "Message"));
								orderResult.setmOfferedPrice(getObjectvalue(
										chatObject, "OfferedPrize"));
								orderResult.setmOrderQuantity(getObjectvalue(
										chatObject, "RequiredQty"));
								orderResult
										.setmUnitsOfMeasurement(getObjectvalue(
												chatObject,
												"UnitsOfMeasurement"));

								if (ServiceGroupType.equalsIgnoreCase("1")) {

								} else if (ServiceGroupType
										.equalsIgnoreCase("2")) {
									JSONObject ShippingAttributesObject = chatObject
											.getJSONObject("ShippingAttributes");
									if (ShippingAttributesObject == null
											|| ShippingAttributesObject
													.length() == 0) {
									} else {
										orderResult
												.setmDeliveryDate(getObjectvalue(
														ShippingAttributesObject,
														"DeliveryDate"));
										orderResult
												.setmFromCity(getObjectvalue(
														ShippingAttributesObject,
														"FromCity"));
										orderResult.setmCountry(getObjectvalue(
												ShippingAttributesObject,
												"FromCountry"));
										orderResult.setmPickUp(getObjectvalue(
												ShippingAttributesObject,
												"PickUp"));
										orderResult.setmToCity(getObjectvalue(
												ShippingAttributesObject,
												"ToCity"));
										orderResult
												.setmToCountry(getObjectvalue(
														ShippingAttributesObject,
														"ToCountry"));
										orderResult
												.setmTotalNumberOfCatons(getObjectvalue(
														ShippingAttributesObject,
														"TotalNumberOfCatons"));
										orderResult
												.setmTotalWeight(getObjectvalue(
														ShippingAttributesObject,
														"TotalWeight"));

										JSONObject DimensionLwhObject = ShippingAttributesObject
												.getJSONObject("DimensionLwh");
										if (DimensionLwhObject == null
												|| DimensionLwhObject.length() == 0) {
										} else {
											orderResult
													.setmHeight(getObjectvalue(
															DimensionLwhObject,
															"Height"));
											orderResult
													.setmLength(getObjectvalue(
															DimensionLwhObject,
															"Length"));
											orderResult
													.setmWidth(getObjectvalue(
															DimensionLwhObject,
															"Width"));
										}

										JSONObject DimensionWeightObject = ShippingAttributesObject
												.getJSONObject("DimensionWeight");
										if (DimensionWeightObject == null
												|| DimensionWeightObject
														.length() == 0) {
										} else {
											orderResult
													.setmShippingWeight(getObjectvalue(
															DimensionWeightObject,
															"Weight"));
										}
									}
								} else if (ServiceGroupType
										.equalsIgnoreCase("3")) {
									JSONObject InspectionAttirbutesObject = chatObject
											.getJSONObject("InspectionAttirbutes");
									if (InspectionAttirbutesObject == null
											|| InspectionAttirbutesObject
													.length() == 0) {
									} else {
										JSONObject InspectionTypeObject = InspectionAttirbutesObject
												.getJSONObject("InspectionType");
										if (InspectionTypeObject == null
												|| InspectionTypeObject
														.length() == 0) {
										} else {
											orderResult
													.setmInspectionType(getObjectvalue(
															InspectionTypeObject,
															"Value"));
										}
										JSONObject InspectorTypeObject = InspectionAttirbutesObject
												.getJSONObject("InspectorType");
										if (InspectorTypeObject == null
												|| InspectorTypeObject.length() == 0) {
										} else {
											orderResult
													.setmInspectorType(getObjectvalue(
															InspectorTypeObject,
															"Value"));
										}
										JSONObject TypeOfIndustryObject = InspectionAttirbutesObject
												.getJSONObject("TypeOfIndustry");
										if (TypeOfIndustryObject == null
												|| TypeOfIndustryObject
														.length() == 0) {
										} else {
											orderResult
													.setmTypeOfIndustry(getObjectvalue(
															TypeOfIndustryObject,
															"Value"));

										}
										JSONObject PlaceToInspectObject = InspectionAttirbutesObject
												.getJSONObject("PlaceToInspect");
										if (PlaceToInspectObject == null
												|| PlaceToInspectObject
														.length() == 0) {
										} else {
											orderResult
													.setmInspectionAddress1(getObjectvalue(
															PlaceToInspectObject,
															"Address1"));
											orderResult
													.setmInspectionAddress2(getObjectvalue(
															PlaceToInspectObject,
															"Address2"));
											orderResult
													.setmInspectionCountryName(getObjectvalue(
															PlaceToInspectObject,
															"CountryName"));
											orderResult
													.setmInspectionStateName(getObjectvalue(
															PlaceToInspectObject,
															"StateName"));
										}
									}
								} else if (ServiceGroupType
										.equalsIgnoreCase("4")) {
									JSONObject CustomAttributesObject = chatObject
											.getJSONObject("CustomAttributes");
									if (CustomAttributesObject == null
											|| CustomAttributesObject.length() == 0) {
									} else {
										JSONObject AddressObject = CustomAttributesObject
												.getJSONObject("Address");
										if (AddressObject == null
												|| AddressObject.length() == 0) {
										} else {
											orderResult
													.setmCustomAddress(getObjectvalue(
															AddressObject,
															"Address1"));
											orderResult
													.setmCustomCity(getObjectvalue(
															AddressObject,
															"City"));
											orderResult
													.setmCustomCountryName(getObjectvalue(
															AddressObject,
															"CountryName"));
											orderResult
													.setmCustomStateName(getObjectvalue(
															AddressObject,
															"StateName"));
										}

										JSONObject CategoryObject = CustomAttributesObject
												.getJSONObject("Category");
										if (CategoryObject == null
												|| CategoryObject.length() == 0) {
										} else {
											orderResult
													.setmCustomCategory(getObjectvalue(
															CategoryObject,
															"Value"));
											orderResult
													.setmCustomSubCategory(getObjectvalue(
															CategoryObject,
															"Value"));
										}

										JSONObject QuantityObject = CustomAttributesObject
												.getJSONObject("Quantity");
										if (QuantityObject == null
												|| QuantityObject.length() == 0) {
										} else {
											orderResult
													.setmCustomQuantity(getObjectvalue(
															QuantityObject,
															"Quantity"));
										}
									}
								} else if (ServiceGroupType
										.equalsIgnoreCase("7")) {
									JSONObject WareHouseAttributesObject = chatObject
											.getJSONObject("WareHouseAttributes");
									if (WareHouseAttributesObject == null
											|| WareHouseAttributesObject
													.length() == 0) {
									} else {
										JSONObject AreaRequirementObject = WareHouseAttributesObject
												.getJSONObject("AreaRequirement");
										if (AreaRequirementObject == null
												|| AreaRequirementObject
														.length() == 0) {
										} else {
											orderResult
													.setmHeight(getObjectvalue(
															AreaRequirementObject,
															"Height"));
											orderResult
													.setmLength(getObjectvalue(
															AreaRequirementObject,
															"Length"));
											orderResult
													.setmWidth(getObjectvalue(
															AreaRequirementObject,
															"Width"));
											JSONObject UnitsOfMeasurement1Object = AreaRequirementObject
													.getJSONObject("UnitsOfMeasurement1");
											if (UnitsOfMeasurement1Object == null
													|| UnitsOfMeasurement1Object
															.length() == 0) {
											} else {
												orderResult
														.setmUnitsOfMeasurement(getObjectvalue(
																UnitsOfMeasurement1Object,
																"Value"));
											}
										}
									}

									JSONObject WareHouseAddressObject = WareHouseAttributesObject
											.getJSONObject("WareHouseAddress");
									if (WareHouseAddressObject == null
											|| WareHouseAddressObject.length() == 0) {
									} else {
										orderResult
												.setmWarehouserAddress(getObjectvalue(
														WareHouseAddressObject,
														"Address1"));
										orderResult
												.setmWarehouseAddress2(getObjectvalue(
														WareHouseAddressObject,
														"Address2"));
										orderResult
												.setmWarehouseCity(getObjectvalue(
														WareHouseAddressObject,
														"City"));
										orderResult
												.setmWarehouseCountry(getObjectvalue(
														WareHouseAddressObject,
														"CountryName"));
										orderResult
												.setmWarehouseState(getObjectvalue(
														WareHouseAddressObject,
														"StateName"));
									}

								} else if (ServiceGroupType
										.equalsIgnoreCase("8")) {

								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderResult;
	}

	// Order details parsing
	public QuickOrderResult parseOrderDetailsResult(String result) {
		QuickOrderResult orderResult = new QuickOrderResult();
		try {
			JSONObject json = new JSONObject(result);
			JSONObject quickOrderResult = json
					.getJSONObject("GetOrderDetailResult");
			String isError = quickOrderResult.getString("IsError");
			if (isError.equals("false")) {
				JSONObject responseObject = quickOrderResult
						.getJSONObject("ResponseObject");
				if (responseObject == null || responseObject.length() == 0) {
				} else {
					orderResult.setmBillingAddressId(getObjectvalue(
							responseObject, "BillingAddressId"));
					orderResult.setmQuotationId(getObjectvalue(responseObject,
							"OrderId"));
					orderResult.setmOrderNumber(getObjectvalue(responseObject,
							"OrderNumber"));
					orderResult.setmServiceGroupType(getObjectvalue(
							responseObject, "ServiceGroupType"));
					String ServiceGroupType = getObjectvalue(responseObject,
							"ServiceGroupType");
					orderResult.setmShippingAddressId(getObjectvalue(
							responseObject, "ShippingAddressId"));
					orderResult.setmTotalProductCost(getObjectvalue(
							responseObject, "SubTotal"));
					orderResult.setmSubject(getObjectvalue(responseObject,
							"Subject"));

					JSONObject buyerObject = responseObject
							.getJSONObject("Buyer");
					if (buyerObject == null || buyerObject.length() == 0) {
					} else {
						orderResult.setmAddress(getObjectvalue(buyerObject,
								"Address"));
						orderResult.setmAddress2(getObjectvalue(buyerObject,
								"Address2"));
						orderResult
								.setmCity(getObjectvalue(buyerObject, "City"));
						orderResult.setmCountry(getObjectvalue(buyerObject,
								"Country"));
						orderResult.setmEmail(getObjectvalue(buyerObject,
								"Email"));
						orderResult.setmFaxNumber(getObjectvalue(buyerObject,
								"FaxNumber"));
						orderResult.setmFirstName(getObjectvalue(buyerObject,
								"FirstName"));
						orderResult.setmLastName(getObjectvalue(buyerObject,
								"LastName"));
						orderResult
								.setmName(getObjectvalue(buyerObject, "Name"));
						orderResult.setmPhoneNumber(getObjectvalue(buyerObject,
								"PhoneNumber"));
						orderResult.setmState(getObjectvalue(buyerObject,
								"State"));
						orderResult.setmLastName(getObjectvalue(buyerObject,
								"UserName"));
						orderResult.setmZip(getObjectvalue(buyerObject, "Zip"));
					}

					JSONObject manufacturerObject = responseObject
							.getJSONObject("Manufacturer");
					if (manufacturerObject == null
							|| manufacturerObject.length() == 0) {
					} else {
						orderResult.setmManufacturerId(getObjectvalue(
								manufacturerObject, "Id"));
						orderResult.setmMAnufacturerName(getObjectvalue(
								manufacturerObject, "Name"));
						JSONObject logo = manufacturerObject
								.getJSONObject("Logo");
						if (logo == null || logo.length() == 0) {
						} else {
							orderResult.setmManufacturerImage(getObjectvalue(
									logo, "url"));
						}
						JSONObject contactDetails = manufacturerObject
								.getJSONObject("ContactDetails");
						if (contactDetails == null
								|| contactDetails.length() == 0) {
						} else {
							orderResult.setmManufacturerAddress(getObjectvalue(
									contactDetails, "Address"));
							orderResult.setmManufacturerEmail(getObjectvalue(
									contactDetails, "Email"));
							orderResult
									.setmManufacturerPhoneNumber(getObjectvalue(
											contactDetails, "PhoneNumber"));
						}
					}

					JSONObject productObject = responseObject
							.getJSONObject("Product");
					if (productObject == null || productObject.length() == 0) {
					} else {
						orderResult.setmProductId(getObjectvalue(productObject,
								"Id"));
						orderResult.setmProductImage(getObjectvalue(
								productObject, "ProductImage"));
						orderResult.setmProductName(getObjectvalue(
								productObject, "ProductName"));
					}

					JSONArray chatArray = responseObject
							.getJSONArray("ChatData");
					if (chatArray == null || chatArray.length() == 0) {
					} else {
						for (int j = 0; j < chatArray.length(); j++) {
							JSONObject chatObject = chatArray.getJSONObject(j);
							orderResult.setmMessage(getObjectvalue(chatObject,
									"Message"));
							orderResult.setmOfferedPrice(getObjectvalue(
									chatObject, "OfferedPrize"));
							orderResult.setmOrderQuantity(getObjectvalue(
									chatObject, "RequiredQty"));
							orderResult.setmUnitsOfMeasurement(getObjectvalue(
									chatObject, "UnitsOfMeasurement"));

							if (ServiceGroupType.equalsIgnoreCase("1")) {

							} else if (ServiceGroupType.equalsIgnoreCase("2")) {
								JSONObject ShippingAttributesObject = chatObject
										.getJSONObject("ShippingAttributes");
								if (ShippingAttributesObject == null
										|| ShippingAttributesObject.length() == 0) {
								} else {
									orderResult
											.setmDeliveryDate(getObjectvalue(
													ShippingAttributesObject,
													"DeliveryDate"));
									orderResult.setmFromCity(getObjectvalue(
											ShippingAttributesObject,
											"FromCity"));
									orderResult.setmCountry(getObjectvalue(
											ShippingAttributesObject,
											"FromCountry"));
									orderResult
											.setmPickUp(getObjectvalue(
													ShippingAttributesObject,
													"PickUp"));
									orderResult
											.setmToCity(getObjectvalue(
													ShippingAttributesObject,
													"ToCity"));
									orderResult.setmToCountry(getObjectvalue(
											ShippingAttributesObject,
											"ToCountry"));
									orderResult
											.setmTotalNumberOfCatons(getObjectvalue(
													ShippingAttributesObject,
													"TotalNumberOfCatons"));
									orderResult.setmTotalWeight(getObjectvalue(
											ShippingAttributesObject,
											"TotalWeight"));

									JSONObject DimensionLwhObject = ShippingAttributesObject
											.getJSONObject("DimensionLwh");
									if (DimensionLwhObject == null
											|| DimensionLwhObject.length() == 0) {
									} else {
										orderResult.setmHeight(getObjectvalue(
												DimensionLwhObject, "Height"));
										orderResult.setmLength(getObjectvalue(
												DimensionLwhObject, "Length"));
										orderResult.setmWidth(getObjectvalue(
												DimensionLwhObject, "Width"));
									}

									JSONObject DimensionWeightObject = ShippingAttributesObject
											.getJSONObject("DimensionWeight");
									if (DimensionWeightObject == null
											|| DimensionWeightObject.length() == 0) {
									} else {
										orderResult
												.setmShippingWeight(getObjectvalue(
														DimensionWeightObject,
														"Weight"));
									}
								}
							} else if (ServiceGroupType.equalsIgnoreCase("3")) {
								JSONObject InspectionAttirbutesObject = chatObject
										.getJSONObject("InspectionAttirbutes");
								if (InspectionAttirbutesObject == null
										|| InspectionAttirbutesObject.length() == 0) {
								} else {
									JSONObject InspectionTypeObject = InspectionAttirbutesObject
											.getJSONObject("InspectionType");
									if (InspectionTypeObject == null
											|| InspectionTypeObject.length() == 0) {
									} else {
										orderResult
												.setmInspectionType(getObjectvalue(
														InspectionTypeObject,
														"Value"));
									}
									JSONObject InspectorTypeObject = InspectionAttirbutesObject
											.getJSONObject("InspectorType");
									if (InspectorTypeObject == null
											|| InspectorTypeObject.length() == 0) {
									} else {
										orderResult
												.setmInspectorType(getObjectvalue(
														InspectorTypeObject,
														"Value"));
									}
									JSONObject TypeOfIndustryObject = InspectionAttirbutesObject
											.getJSONObject("TypeOfIndustry");
									if (TypeOfIndustryObject == null
											|| TypeOfIndustryObject.length() == 0) {
									} else {
										orderResult
												.setmTypeOfIndustry(getObjectvalue(
														TypeOfIndustryObject,
														"Value"));

									}
									JSONObject PlaceToInspectObject = InspectionAttirbutesObject
											.getJSONObject("PlaceToInspect");
									if (PlaceToInspectObject == null
											|| PlaceToInspectObject.length() == 0) {
									} else {
										orderResult
												.setmInspectionAddress1(getObjectvalue(
														PlaceToInspectObject,
														"Address1"));
										orderResult
												.setmInspectionAddress2(getObjectvalue(
														PlaceToInspectObject,
														"Address2"));
										orderResult
												.setmInspectionCountryName(getObjectvalue(
														PlaceToInspectObject,
														"CountryName"));
										orderResult
												.setmInspectionStateName(getObjectvalue(
														PlaceToInspectObject,
														"StateName"));
									}
								}
							} else if (ServiceGroupType.equalsIgnoreCase("4")) {
								JSONObject CustomAttributesObject = chatObject
										.getJSONObject("CustomAttributes");
								if (CustomAttributesObject == null
										|| CustomAttributesObject.length() == 0) {
								} else {
									JSONObject AddressObject = CustomAttributesObject
											.getJSONObject("Address");
									if (AddressObject == null
											|| AddressObject.length() == 0) {
									} else {
										orderResult
												.setmCustomAddress(getObjectvalue(
														AddressObject,
														"Address1"));
										orderResult
												.setmCustomCity(getObjectvalue(
														AddressObject, "City"));
										orderResult
												.setmCustomCountryName(getObjectvalue(
														AddressObject,
														"CountryName"));
										orderResult
												.setmCustomStateName(getObjectvalue(
														AddressObject,
														"StateName"));
									}

									JSONObject CategoryObject = CustomAttributesObject
											.getJSONObject("Category");
									if (CategoryObject == null
											|| CategoryObject.length() == 0) {
									} else {
										orderResult
												.setmCustomCategory(getObjectvalue(
														CategoryObject, "Value"));
										orderResult
												.setmCustomSubCategory(getObjectvalue(
														CategoryObject, "Value"));
									}

									JSONObject QuantityObject = CustomAttributesObject
											.getJSONObject("Quantity");
									if (QuantityObject == null
											|| QuantityObject.length() == 0) {
									} else {
										orderResult
												.setmCustomQuantity(getObjectvalue(
														QuantityObject,
														"Quantity"));
									}
								}
							} else if (ServiceGroupType.equalsIgnoreCase("7")) {
								JSONObject WareHouseAttributesObject = chatObject
										.getJSONObject("WareHouseAttributes");
								if (WareHouseAttributesObject == null
										|| WareHouseAttributesObject.length() == 0) {
								} else {
									JSONObject AreaRequirementObject = WareHouseAttributesObject
											.getJSONObject("AreaRequirement");
									if (AreaRequirementObject == null
											|| AreaRequirementObject.length() == 0) {
									} else {
										orderResult
												.setmHeight(getObjectvalue(
														AreaRequirementObject,
														"Height"));
										orderResult
												.setmLength(getObjectvalue(
														AreaRequirementObject,
														"Length"));
										orderResult
												.setmWidth(getObjectvalue(
														AreaRequirementObject,
														"Width"));
										JSONObject UnitsOfMeasurement1Object = AreaRequirementObject
												.getJSONObject("UnitsOfMeasurement1");
										if (UnitsOfMeasurement1Object == null
												|| UnitsOfMeasurement1Object
														.length() == 0) {
										} else {
											orderResult
													.setmUnitsOfMeasurement(getObjectvalue(
															UnitsOfMeasurement1Object,
															"Value"));
										}
									}
								}

								JSONObject WareHouseAddressObject = WareHouseAttributesObject
										.getJSONObject("WareHouseAddress");
								if (WareHouseAddressObject == null
										|| WareHouseAddressObject.length() == 0) {
								} else {
									orderResult
											.setmWarehouserAddress(getObjectvalue(
													WareHouseAddressObject,
													"Address1"));
									orderResult
											.setmWarehouseAddress2(getObjectvalue(
													WareHouseAddressObject,
													"Address2"));
									orderResult
											.setmWarehouseCity(getObjectvalue(
													WareHouseAddressObject,
													"City"));
									orderResult
											.setmWarehouseCountry(getObjectvalue(
													WareHouseAddressObject,
													"CountryName"));
									orderResult
											.setmWarehouseState(getObjectvalue(
													WareHouseAddressObject,
													"StateName"));
								}

							} else if (ServiceGroupType.equalsIgnoreCase("8")) {

							}
						}

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderResult;
	}

	// parsing saveorderResult
	public String[] parseSaveOrderResult(String result) {
		String[] mResult = new String[2];
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject resultObject = jsonObject
					.getJSONObject("SaveOrderResult");
			mResult[0] = resultObject.getString("IsError");
			if (mResult[0].equalsIgnoreCase("true")) {
				JSONObject error = resultObject
						.getJSONObject("ExceptionObject");
				mResult[1] = error.getString("ErrorMessage");
			} else {
				mResult[1] = resultObject.getString("ResponseObject");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return mResult;
	}

	public String[] parseUpdatePaymentDetail(String result) {
		String[] mResult = new String[2];
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject resultObject = jsonObject
					.getJSONObject("UpdatePaymentDetailResult");
			mResult[0] = resultObject.getString("IsError");
			if (mResult[0].equalsIgnoreCase("true")) {
				JSONObject error = resultObject
						.getJSONObject("ExceptionObject");
				mResult[1] = error.getString("ErrorMessage");
			} else {
				mResult[1] = resultObject.getString("ResponseObject");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return mResult;
	}

	public List<ProductList> parseNewArrival(String resultParam,
			Context applicationContext) {
		List<ProductList> mNewArrivalList = new ArrayList<ProductList>();
		try {
			JSONObject jObj = new JSONObject(resultParam);
			JSONObject productObject = jObj
					.getJSONObject("GetFeaturedProductsResult");
			if (productObject == null || productObject.length() == 0) {
			} else {
				JSONObject productObj = productObject
						.getJSONObject("ResponseObject");
				if (productObj == null || productObj.length() == 0) {
				} else {
					JSONArray productArray = productObj
							.getJSONArray("ProductDetails");
					if (productArray == null || productArray.length() == 0) {
					} else {
						for (int i = 0; i < productArray.length(); i++) {
							JSONObject prdObject = productArray
									.getJSONObject(i);
							ProductList mNewArrivals = new ProductList();
							mNewArrivals.setmProduct_Name(getObjectvalue(
									prdObject, "ProductName"));
							mNewArrivals.setmProduct_Id(getObjectvalue(
									prdObject, "ProductId"));
							mNewArrivals.setmCategory_Id(getObjectvalue(
									prdObject, "CategoryId"));
							mNewArrivals.setmCategory_Name(getObjectvalue(
									prdObject, "CategoryName"));
							mNewArrivals.setmProduct_Image(getObjectvalue(
									prdObject, "ProductPicture"));
							mNewArrivals.setmProduct_OfferPrice(getObjectvalue(
									prdObject, "OfferValue"));

							JSONObject manufacturerObject = prdObject
									.getJSONObject("Manufacture");
							if (manufacturerObject == null
									|| manufacturerObject.length() == 0) {
							} else {
								mNewArrivals
										.setmManufacturer_Name(getObjectvalue(
												manufacturerObject, "Name"));
								mNewArrivals
										.setmManufacturer_Id(getObjectvalue(
												manufacturerObject, "Id"));
							}
							mNewArrivalList.add(mNewArrivals);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mNewArrivalList;
	}

	public List<ProductList> parseFeaturedProducts(String resultParam,
			Context applicationContext) {
		List<ProductList> mFeaturedProductsList = new ArrayList<ProductList>();
		try {
			JSONObject jObj = new JSONObject(resultParam);
			JSONObject GetFeaturedProductsResultObject = jObj
					.getJSONObject("GetFeaturedProductsResult");
			if (GetFeaturedProductsResultObject == null
					|| GetFeaturedProductsResultObject.length() == 0) {
			} else {
				String IsError = GetFeaturedProductsResultObject
						.getString("IsError");
				if (IsError.equalsIgnoreCase("false")) {
					JSONArray ResponseArray = GetFeaturedProductsResultObject
							.getJSONArray("ResponseObject");
					if (ResponseArray == null || ResponseArray.length() == 0) {
					} else {
						for (int i = 0; i < ResponseArray.length(); i++) {
							JSONObject responseObject = ResponseArray
									.getJSONObject(i);
							ProductList mFeacturedProduct = new ProductList();

							mFeacturedProduct.setmProduct_Id(getObjectvalue(
									responseObject, "Id"));
							mFeacturedProduct.setmProduct_Name(getObjectvalue(
									responseObject, "ProductName"));
							mFeacturedProduct
									.setmProduct_DiscountAmount(getObjectvalue(
											responseObject, "DiscountAmount"));

							JSONArray productImageArray = responseObject
									.getJSONArray("ProductImageUrlList");
							if (productImageArray == null
									|| productImageArray.length() == 0) {
							} else {
								for (int j = 0; j < productImageArray.length(); j++) {
									mFeacturedProduct
											.setmProduct_Image(productImageArray
													.getString(j));
								}

							}

							JSONObject CategoryObject = responseObject
									.getJSONObject("Category");
							if (CategoryObject == null
									|| CategoryObject.length() == 0) {
							} else {
								mFeacturedProduct
										.setmCategory_Id(getObjectvalue(
												CategoryObject, "Id"));
								mFeacturedProduct
										.setmCategory_Name(getObjectvalue(
												CategoryObject, "Name"));
							}

							JSONObject ManufacturerLiteMObject = responseObject
									.getJSONObject("ManufacturerLiteM");
							if (ManufacturerLiteMObject == null
									|| ManufacturerLiteMObject.length() == 0) {
							} else {
								mFeacturedProduct
										.setmManufacturer_Id(getObjectvalue(
												ManufacturerLiteMObject, "Id"));
								mFeacturedProduct
										.setmManufacturer_Name(getObjectvalue(
												ManufacturerLiteMObject, "Name"));
							}

							JSONObject PriceModelObject = responseObject
									.getJSONObject("PriceModel");
							if (PriceModelObject == null
									|| PriceModelObject.length() == 0) {
							} else {
								mFeacturedProduct
										.setmProductPrice(getObjectvalue(
												PriceModelObject, "Price"));
								mFeacturedProduct
										.setmUnitOfMeasurement(getObjectvalue(
												PriceModelObject,
												"UnitOfMeasurement"));
							}

							mFeaturedProductsList.add(mFeacturedProduct);
						}
					}
				} else {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mFeaturedProductsList;
	}

	public ArrayList<QuoatationDashboard> ParseDashboardQuaotation(
			String resultOutparam) {
		ArrayList<QuoatationDashboard> mQuoatationDashboardsList = new ArrayList<QuoatationDashboard>();
		try {
			JSONObject jsonObject = new JSONObject(resultOutparam);
			JSONObject GetQuotationsResultObject = jsonObject
					.getJSONObject("GetQuotationsResult");
			String IsError = GetQuotationsResultObject.getString("IsError");
			if (IsError.equalsIgnoreCase("false")) {
				JSONObject ResponseObject = GetQuotationsResultObject
						.getJSONObject("ResponseObject");
				if (ResponseObject == null || ResponseObject.length() == 0) {
				} else {
					String mTotalCount = getObjectvalue(ResponseObject,
							"TotalCount");

					JSONArray QuotationLitePropertiesArray = ResponseObject
							.getJSONArray("QuotationLiteProperties");
					if (QuotationLitePropertiesArray == null
							|| QuotationLitePropertiesArray.length() == 0) {
					} else {
						for (int i = 0; i < QuotationLitePropertiesArray
								.length(); i++) {
							JSONObject QuotationLiteProperties = QuotationLitePropertiesArray
									.getJSONObject(i);
							QuoatationDashboard mQuotationDetails = new QuoatationDashboard();
							mQuotationDetails
									.setmQuotation_Datetime(getObjectvalue(
											QuotationLiteProperties,
											"CreatedDate"));
							mQuotationDetails.setQuotation_Id(getObjectvalue(
									QuotationLiteProperties, "Id"));
							mQuotationDetails
									.setmQuotation_Number(getObjectvalue(
											QuotationLiteProperties,
											"QuotationNumber"));
							mQuotationDetails
									.setmQuotationStatusId(getObjectvalue(
											QuotationLiteProperties,
											"QuotationStatusId"));
							JSONObject ProductObject = QuotationLiteProperties
									.getJSONObject("Product");
							if (ProductObject == null
									|| ProductObject.length() == 0) {
							} else {
								mQuotationDetails
										.setQuotation_productname(getObjectvalue(
												ProductObject, "ProductName"));
							}
							JSONObject ManufacturerObject = QuotationLiteProperties
									.getJSONObject("Manufacturer");
							if (ManufacturerObject == null
									|| ManufacturerObject.length() == 0) {
							} else {
								mQuotationDetails
										.setQuotation_manufacturename(getObjectvalue(
												ManufacturerObject, "Name"));
							}
							mQuotationDetails.setmTotalCount(mTotalCount);
							mQuoatationDashboardsList.add(mQuotationDetails);
						}
					}
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mQuoatationDashboardsList;
	}

	public List<Inventory> parseproductsForInventory(String resultOutparam) {
		Log.e("abcd", "pcalled");
		Log.e("abcd", resultOutparam);
		List<Inventory> mInventoryList = new ArrayList<Inventory>();
		try {
			JSONObject jObj = new JSONObject(resultOutparam);

			JSONObject jInventoryObj = jObj
					.getJSONObject("GetProductsForInventoryResult");

			JSONArray jResponseArray = jInventoryObj
					.getJSONArray("ResponseObject");

			for (int i = 0; i < jResponseArray.length(); i++) {
				Inventory inventoryPItem = new Inventory();
				JSONObject jResponseObj = jResponseArray.getJSONObject(i);

				// inventoryItem.setmProductId(jResponseObj.get("ProductId")
				// .toString());
				inventoryPItem.setmProductName(jResponseObj.get("ProductName")
						.toString());
				mInventoryList.add(inventoryPItem);
				Log.e("abcd", "ProductName");
			}
		} catch (Exception e) {
			Log.e("abcd", "er: " + e.getMessage());
		}
		return mInventoryList;

	}

	public List<OrderDashboard> ParseDashboardOrder(String resultOutparam) {
		List<OrderDashboard> DashboardOrderList = new ArrayList<OrderDashboard>();
		try {
			JSONObject jsonObject = new JSONObject(resultOutparam);
			JSONObject GetOrderResultObject = jsonObject
					.getJSONObject("GetOrderResult");
			String IsError = GetOrderResultObject.getString("IsError");
			if (IsError.equalsIgnoreCase("false")) {
				JSONArray ResponseArray = GetOrderResultObject
						.getJSONArray("ResponseObject");
				if (ResponseArray == null || ResponseArray.length() == 0) {
				} else {
					for (int i = 0; i < ResponseArray.length(); i++) {
						JSONObject ResponseObject = ResponseArray
								.getJSONObject(i);
						OrderDashboard mOrderList = new OrderDashboard();
						mOrderList.setmOrder_Id(getObjectvalue(ResponseObject,
								"OrderId"));
						mOrderList.setmOrder_Number(getObjectvalue(
								ResponseObject, "OrderNumber"));
						mOrderList.setmOrder_StatusId(getObjectvalue(
								ResponseObject, "OrderStatusId"));
						mOrderList.setmOrder_Date(getObjectvalue(
								ResponseObject, "OrderDate"));

						JSONObject ProductObject = ResponseObject
								.getJSONObject("Product");
						if (ProductObject == null
								|| ProductObject.length() == 0) {
						} else {
							mOrderList.setmOrder_Productname(getObjectvalue(
									ProductObject, "ProductName"));
						}

						JSONObject ManufacturerObject = ResponseObject
								.getJSONObject("Manufacturer");
						if (ManufacturerObject == null
								|| ManufacturerObject.length() == 0) {
						} else {
							mOrderList.setmOrder_Manufacturname(getObjectvalue(
									ManufacturerObject, "Name"));
						}

						DashboardOrderList.add(mOrderList);
					}
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DashboardOrderList;
	}

	// prasing orders list
	public List<Orders> parseOrderDetails(String mResultData) {
		List<Orders> mOrdersList = null;
		try {
			JSONObject json = new JSONObject(mResultData);
			JSONObject channelOrderObject = json
					.getJSONObject("GetChannelOrderResult");
			String isError = getObjectvalue(channelOrderObject, "IsError");
			if (isError != null && isError.equalsIgnoreCase("false")) {
				JSONArray responseArray = channelOrderObject
						.getJSONArray("ResponseObject");
				int len = 0;
				if (responseArray != null && responseArray.length() > 0) {
					mOrdersList = new ArrayList<Orders>();
					len = responseArray.length();
				}
				for (int i = 0; i < len; i++) {
					Orders order = new Orders();
					JSONObject orderObj = responseArray.getJSONObject(i);
					order.setmCustomerId(getObjectvalue(orderObj, "CustomerId"));
					order.setmOrder(getObjectvalue(orderObj, "Order"));
					order.setmTotalCount(getObjectvalue(orderObj, "TotalCount"));
					// Channel details
					JSONObject channelDetailsObj = orderObj
							.getJSONObject("ChannelDetails");
					order.setmChannelId(getObjectvalue(channelDetailsObj, "Id"));
					order.setmChannelName(getObjectvalue(channelDetailsObj,
							"ChannelName"));
					order.setmPictureURL(getObjectvalue(channelDetailsObj,
							"PictureURL"));
					// ChannelOrder details
					JSONObject channelOrderObj = channelDetailsObj
							.getJSONObject("ChannelOrder");
					order.setmChannelOrderId(getObjectvalue(channelOrderObj,
							"ChannelOrderId"));
					order.setmNoOfPendindOrders(getObjectvalue(channelOrderObj,
							"NoOfPendindOrders"));
					order.setmOrderDate(getObjectvalue(channelOrderObj,
							"OrderDate"));
					order.setmOrderNumber(getObjectvalue(channelOrderObj,
							"OrderNumber"));
					order.setmOrderQty(getObjectvalue(channelOrderObj,
							"OrderQty"));
					order.setmStatusId(getObjectvalue(channelOrderObj,
							"StatusId"));
					order.setmTotalOrderQty(getObjectvalue(channelOrderObj,
							"TotalOrderQty"));
					// Product details
					JSONObject productObj = channelOrderObj
							.getJSONObject("Product");
					order.setmProductId(getObjectvalue(productObj, "Id"));
					order.setmProductImage(getObjectvalue(productObj,
							"ProductImage"));
					order.setmProductName(getObjectvalue(productObj,
							"ProductName"));
					order.setmProductWeight(getObjectvalue(productObj,
							"ProductWeight"));
					mOrdersList.add(order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mOrdersList;
	}

	/*
	 * // parsing inventory list public List<Inventory> parseInventory(String
	 * mResultData){ List<Inventory> mInventoryList = null; try{ JSONObject json
	 * = new JSONObject(mResultData); JSONObject inventoryOrderObject =
	 * json.getJSONObject("GetInventoryOrderResult"); String isError =
	 * inventoryOrderObject.getString("IsError");
	 * if(isError.equalsIgnoreCase("false")){
	 * 
	 * JSONArray responseArray =
	 * inventoryOrderObject.getJSONArray("ResponseObject"); int len =0;
	 * if(responseArray != null && responseArray.length() > 0){ mInventoryList =
	 * new ArrayList<Inventory>(); len = responseArray.length(); } for(int i=0;
	 * i<len; i++){ Inventory mInventory = new Inventory(); JSONObject
	 * inventoryObj = responseArray.getJSONObject(i);
	 * mInventory.setmCustomerId(getObjectvalue(inventoryObj, "CustomerId"));
	 * //Order details JSONObject inventoryOrders =
	 * inventoryObj.getJSONObject("Order");
	 * mInventory.setmBuyer(getObjectvalue(inventoryOrders, "Buyer"));
	 * mInventory.setmManufacturer(getObjectvalue(inventoryOrders,
	 * "Manufacturer"));
	 * mInventory.setmOfferedPrice(getObjectvalue(inventoryOrders,
	 * "OfferedPrice"));
	 * mInventory.setmOrderDate(getObjectvalue(inventoryOrders,"OrderDate"));
	 * mInventory.setmOrderId(getObjectvalue(inventoryOrders,"OrderId"));
	 * mInventory
	 * .setmOrderNumber(getObjectvalue(inventoryOrders,"OrderNumber"));
	 * mInventory
	 * .setmOrderQuantity(getObjectvalue(inventoryOrders,"OrderQuantity"));
	 * mInventory
	 * .setmOrderStatusId(getObjectvalue(inventoryOrders,"OrderStatusId"));
	 * mInventory.setmSubTotal(getObjectvalue(inventoryOrders,"SubTotal"));
	 * mInventory.setmTotalCount(getObjectvalue(inventoryOrders,"TotalCount"));
	 * mInventory
	 * .setmUnitsOfMeasurement(getObjectvalue(inventoryOrders,"UnitsOfMeasurement"
	 * )); //Product details JSONObject productObj =
	 * inventoryOrders.getJSONObject("Product");
	 * mInventory.setmProductId(getObjectvalue(productObj, "Id"));
	 * mInventory.setmProductImage(getObjectvalue(productObj, "ProductImage"));
	 * mInventory.setmProductName(getObjectvalue(productObj, "ProductName"));
	 * mInventory.setmProductWeight(getObjectvalue(productObj,
	 * "ProductWeight"));
	 * 
	 * mInventoryList.add(mInventory); } } }catch(Exception e){
	 * e.printStackTrace(); } return mInventoryList; }
	 */

	/*
	 * //parsing channels detail public List<Channel> parseChannels(String
	 * mResultData){ List<Channel> mChannelList = null; try{ JSONObject json =
	 * new JSONObject(mResultData); JSONObject channelsObj =
	 * json.getJSONObject("GetChannelResult"); String isError =
	 * channelsObj.getString("IsError"); if(isError == null ||
	 * isError.equalsIgnoreCase("true")){
	 * 
	 * }else{ JSONArray respArray = channelsObj.getJSONArray("ResponseObject");
	 * int len = 0; if(respArray == null || respArray.length() ==0){ }else{
	 * mChannelList = new ArrayList<Channel>(); len = respArray.length(); } for
	 * (int i=0; i<len; i++){ JSONObject channel = respArray.getJSONObject(i);
	 * Channel chn = new Channel(); chn.setmChannelId(getObjectvalue(channel,
	 * "Id")); chn.setmChannelName(getObjectvalue(channel, "ChannelName"));
	 * chn.setmChannelOrder(getObjectvalue(channel, "ChannelOrder"));
	 * chn.setmDescription(getObjectvalue(channel, "Description"));
	 * chn.setmPictureURL(getObjectvalue(channel, "PictureURL"));
	 * mChannelList.add(chn); } } }catch(Exception e){ e.printStackTrace(); }
	 * return mChannelList; }
	 */

	/*
	 * public List<Warehouse> parseWarehouseDetails(String mResultData){
	 * List<Warehouse> mWarehouseList = null; try{ JSONObject json = new
	 * JSONObject(mResultData); JSONObject resultObj =
	 * json.getJSONObject("GetWarehouseResult"); String IsError =
	 * getObjectvalue(resultObj, "IsError"); if(IsError == null ||
	 * IsError.equalsIgnoreCase("true")){ }else{ JSONArray respArray =
	 * resultObj.getJSONArray("ResponseObject"); int len = 0; if(respArray ==
	 * null || respArray.length() == 0){ }else{ len = respArray.length();
	 * mWarehouseList = new ArrayList<Warehouse>(); for(int i=0; i<len; i++){
	 * JSONObject warehouse = respArray.getJSONObject(i); JSONObject whObj=
	 * warehouse.getJSONObject("WarehouseDetails");
	 * 
	 * Warehouse wh = new Warehouse(); wh.setmWarehouseId(getObjectvalue(whObj,
	 * "WarehouseId")); wh.setmWarehouseName(getObjectvalue(whObj, "Name"));
	 * wh.setmAddress(getObjectvalue(whObj, "Address"));
	 * wh.setmIsOwnWarehouse(getObjectvalue(whObj, "IsOwnWarehouse"));
	 * wh.setmProduct(getObjectvalue(whObj, "Product"));
	 * wh.setmStockQuantity(getObjectvalue(whObj, "StockQuantity"));
	 * wh.setmTotalWarehouseInventory(getObjectvalue(whObj,
	 * "TotalWarehouseInventory"));
	 * wh.setmWarehouseInventoryId(getObjectvalue(whObj,
	 * "WarehouseInventoryId")); mWarehouseList.add(wh); } } }
	 * 
	 * }catch(Exception e){ e.printStackTrace(); } return mWarehouseList; }
	 */
	/*
	 * public List<Warehouse> parseWarehouseList(String resultOutparam) {
	 * List<Warehouse> mWarehouseList = new ArrayList<Warehouse>();
	 * 
	 * try { JSONObject jObj = new JSONObject(resultOutparam);
	 * 
	 * JSONObject jWarehouseObj = jObj.getJSONObject("GetWarehouseResult");
	 * 
	 * JSONArray jResponseArray = jWarehouseObj .getJSONArray("ResponseObject");
	 * for (int i = 0; i < jResponseArray.length(); i++) { Warehouse
	 * WarehouseItem = new Warehouse(); JSONObject jResponseObj =
	 * jResponseArray.getJSONObject(i);
	 * 
	 * JSONObject jWareHouseObj = jResponseObj
	 * .getJSONObject("WarehouseDetails"); JSONObject jWareHouseAddressObj =
	 * jWareHouseObj .getJSONObject("Address");
	 * 
	 * Log.e("Warehouse name", jWareHouseObj.get("WarehouseId") .toString());
	 * Log.e("Warehouse name", jWareHouseObj.get("Name").toString());
	 * Log.e("Warehouse name", jWareHouseAddressObj.get("Address1")
	 * .toString()); Log.e("Warehouse name",
	 * jWareHouseAddressObj.get("PhoneNumber") .toString());
	 * Log.e("Warehouse name", jWareHouseAddressObj.get("Email") .toString());
	 * 
	 * Log.e("Warehouse name", "----------------------------------------");
	 * 
	 * WarehouseItem.setmWareHouseId(jWareHouseObj.get("WarehouseId")
	 * .toString()); WarehouseItem.setmWareHouseName(jWareHouseObj.get("Name")
	 * .toString());
	 * WarehouseItem.setmWareHouseAddress(jWareHouseAddressObj.get(
	 * "Address1").toString());
	 * WarehouseItem.setmWareHousePhoneNo(jWareHouseAddressObj.get(
	 * "PhoneNumber").toString());
	 * WarehouseItem.setmWareHouseEmail(jWareHouseAddressObj.get(
	 * "Email").toString());
	 * WarehouseItem.setmWareHousePictureURL(jWareHouseObj.get(
	 * "PictureUrl").toString()); mWarehouseList.add(WarehouseItem);
	 * 
	 * }
	 * 
	 * } catch (Exception e) {
	 * 
	 * } return mWarehouseList; }
	 */

	public String parseDeleteWareHouseResult(String result) {
		// TODO Auto-generated method stub
		try {
			JSONObject jObj = new JSONObject(result);

			JSONObject jdeleteWareHouseResult = jObj
					.getJSONObject("DeleteWareHouseResult");
			// Log.e("delete2",jdeleteWareHouseResult+ "");
			return (jdeleteWareHouseResult.getString("ResponseObject"));

		} catch (Exception e) {
			return "false";
		}
	}

	public List<AlertGetMessages> parseAlertGetMessages(String resultOutparam) {
		// TODO Auto-generated method stub
		List<AlertGetMessages> alertGetMessages = new ArrayList<AlertGetMessages>();
		try {
			JSONObject jObj = new JSONObject(resultOutparam);
			JSONObject jalertGetMessages = jObj
					.getJSONObject("GetMessagesResult");
			JSONArray jResponseArray = jalertGetMessages
					.getJSONArray("ResponseObject");
			Log.e("abc2", jResponseArray.length() + "");
			for (int i = 0; i < jResponseArray.length(); i++) {
				AlertGetMessages alertGetMessagesList = new AlertGetMessages();
				JSONObject jResponseObj = jResponseArray.getJSONObject(i);
				alertGetMessagesList.setmAlertMessages(jResponseObj.getString(
						"Messages").toString());

				alertGetMessages.add(alertGetMessagesList);
			}
		} catch (Exception e) {

			// return null;
		}
		return alertGetMessages;
	}

	public List<WarehouseList> parseWarehouseList(String resultOutparam) {
		List<WarehouseList> mWarehouseList = new ArrayList<WarehouseList>();

		try {
			JSONObject jObj = new JSONObject(resultOutparam);

			JSONObject jWarehouseObj = jObj
					.getJSONObject("GetWarehouseListResult");

			JSONArray jResponseArray = jWarehouseObj
					.getJSONArray("ResponseObject");
			for (int i = 0; i < jResponseArray.length(); i++) {
				WarehouseList WarehouseItem = new WarehouseList();
				JSONObject jResponseObj = jResponseArray.getJSONObject(i);

				JSONObject jWareHouseObj = jResponseObj
						.getJSONObject("WarehouseDetails");
				JSONObject jWareHouseAddressObj = jWareHouseObj
						.getJSONObject("Address");

				Log.e("Warehouse name", jWareHouseObj.get("WarehouseId")
						.toString());
				Log.e("Warehouse name", jWareHouseObj.get("Name").toString());
				Log.e("Warehouse name", jWareHouseAddressObj.get("Address1")
						.toString());
				Log.e("Warehouse name", jWareHouseAddressObj.get("PhoneNumber")
						.toString());
				Log.e("Warehouse name", jWareHouseAddressObj.get("Email")
						.toString());

				Log.e("Warehouse name",
						"----------------------------------------");

				WarehouseItem.setmWareHouseId(jWareHouseObj.get("WarehouseId")
						.toString());
				WarehouseItem.setmWareHouseName(jWareHouseObj.get("Name")
						.toString());
				WarehouseItem.setmWareHouseAddress(jWareHouseAddressObj.get(
						"Address1").toString());
				WarehouseItem.setmWareHousePhoneNo(jWareHouseAddressObj.get(
						"PhoneNumber").toString());
				WarehouseItem.setmWareHouseEmail(jWareHouseAddressObj.get(
						"Email").toString());
				WarehouseItem.setmWareHousePictureURL(jWareHouseObj.get(
						"PictureUrl").toString());
				mWarehouseList.add(WarehouseItem);
			}

		} catch (Exception e) {

		}
		return mWarehouseList;
	}

	public List<ManufacturerList> parseManufacturerList(String result,
			Context applicationContext) {
		List<ManufacturerList> mManufacturerList = new ArrayList<ManufacturerList>();
		try {
			JSONObject json = new JSONObject(result);
			JSONObject resultObj = json
					.getJSONObject("GetManufacturerListForServiceResult");
			String IsError = getObjectvalue(resultObj, "IsError");
			if (IsError.equalsIgnoreCase("false")) {
				JSONArray ResponseObjectArray = resultObj
						.getJSONArray("ResponseObject");
				if (ResponseObjectArray == null
						|| ResponseObjectArray.length() == 0) {
				} else {
					for (int i = 0; i < ResponseObjectArray.length(); i++) {
						JSONObject ResponseObject = ResponseObjectArray
								.getJSONObject(i);
						ManufacturerList mManufacturer = new ManufacturerList();

						mManufacturer.setmId(getObjectvalue(ResponseObject,
								"Id"));
						mManufacturer.setmLogo(getObjectvalue(ResponseObject,
								"Logo"));
						mManufacturer.setmName(getObjectvalue(ResponseObject,
								"Name"));
						mManufacturerList.add(mManufacturer);
					}
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mManufacturerList;
	}

	public List<ManufacturerList> parseManufacturerProductsList(String result,
			Context applicationContext) {
		List<ManufacturerList> mManufacturerProductList = new ArrayList<ManufacturerList>();
		try {
			JSONObject json = new JSONObject(result);
			JSONObject resultObj = json
					.getJSONObject("GetProductListOfManufacturerResult");
			String IsError = getObjectvalue(resultObj, "IsError");
			if (IsError.equalsIgnoreCase("false")) {
				JSONArray ResponseObjectArray = resultObj
						.getJSONArray("ResponseObject");
				if (ResponseObjectArray == null
						|| ResponseObjectArray.length() == 0) {
				} else {
					for (int i = 0; i < ResponseObjectArray.length(); i++) {
						JSONObject ResponseObject = ResponseObjectArray
								.getJSONObject(i);
						ManufacturerList mManufacturer = new ManufacturerList();

						mManufacturer.setmId(getObjectvalue(ResponseObject,
								"Id"));
						mManufacturer.setmLogo(getObjectvalue(ResponseObject,
								"ProductPicture"));
						mManufacturer.setmName(getObjectvalue(ResponseObject,
								"ProductName"));
						mManufacturerProductList.add(mManufacturer);
					}
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mManufacturerProductList;
	}

	public List<CountryDetails> parseCountries(String outputResult,
			Context applicationContext) {
		List<CountryDetails> mCountriesList = new ArrayList<CountryDetails>();
		try {
			JSONObject json = new JSONObject(outputResult);
			JSONObject resultObj = json.getJSONObject("GetCountriesResult");
			String IsError = getObjectvalue(resultObj, "IsError");
			if (IsError.equalsIgnoreCase("false")) {
				JSONArray ResponseObjectArray = resultObj
						.getJSONArray("ResponseObject");
				if (ResponseObjectArray == null
						|| ResponseObjectArray.length() == 0) {
				} else {
					for (int i = 0; i < ResponseObjectArray.length(); i++) {
						JSONObject ResponseObject = ResponseObjectArray
								.getJSONObject(i);
						CountryDetails mCountries = new CountryDetails();

						mCountries.setmCountryId(getObjectvalue(ResponseObject,
								"CountryId"));
						mCountries.setmCountryName(getObjectvalue(
								ResponseObject, "CountryName"));
						mCountriesList.add(mCountries);
					}
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mCountriesList;
	}

	public List<CountryDetails> parseStates(String outputResult,
			Context applicationContext) {
		List<CountryDetails> mStateList = new ArrayList<CountryDetails>();
		try {
			JSONObject json = new JSONObject(outputResult);
			JSONObject resultObj = json.getJSONObject("GetStatesResult");
			String IsError = getObjectvalue(resultObj, "IsError");
			if (IsError.equalsIgnoreCase("false")) {
				JSONArray ResponseObjectArray = resultObj
						.getJSONArray("ResponseObject");
				if (ResponseObjectArray == null
						|| ResponseObjectArray.length() == 0) {
				} else {
					for (int i = 0; i < ResponseObjectArray.length(); i++) {
						JSONObject ResponseObject = ResponseObjectArray
								.getJSONObject(i);
						CountryDetails mState = new CountryDetails();

						mState.setmCountryId(getObjectvalue(ResponseObject,
								"CountryId"));
						mState.setmStateId(getObjectvalue(ResponseObject,
								"StateId"));
						mState.setmStateName(getObjectvalue(ResponseObject,
								"StateName"));
						mStateList.add(mState);
					}
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mStateList;
	}

	public List<Channel> parseChannelList(String resultOutparam) {
		List<Channel> mChannelList = new ArrayList<Channel>();
		try {
			JSONObject jObj = new JSONObject(resultOutparam);

			JSONObject jChannelObj = jObj.getJSONObject("GetChannelResult");

			JSONArray jResponseArray = jChannelObj
					.getJSONArray("ResponseObject");
			for (int i = 0; i < jResponseArray.length(); i++) {
				Channel channelItem = new Channel();
				JSONObject jResponseObj = jResponseArray.getJSONObject(i);
				channelItem.setmChannelId(jResponseObj.get("Id").toString());
				channelItem.setmChannelName(jResponseObj.get("ChannelName")
						.toString());
				channelItem.setmAddress(jResponseObj.get("ChannelAddress")
						.toString());
				channelItem.setmPhoneNo(jResponseObj.get("ChannelPhoneNo")
						.toString());
				channelItem.setmEmail(jResponseObj.get("ChannelEmailId")
						.toString());
				channelItem.setmPictureURL(jResponseObj.get("PictureURL")
						.toString());
				channelItem.setIsActive(jResponseObj.getBoolean("IsActive"));

				Log.e("Channel name", jResponseObj.get("Id").toString());
				Log.e("Channel name", jResponseObj.get("ChannelName")
						.toString());
				Log.e("Channel name", jResponseObj.get("ChannelAddress")
						.toString());
				Log.e("Channel name", jResponseObj.get("ChannelPhoneNo")
						.toString());
				Log.e("Channel name", jResponseObj.get("ChannelEmailId")
						.toString());
				Log.e("Channel name", jResponseObj.get("PictureURL").toString());
				Log.e("Channel name", jResponseObj.get("IsActive").toString());
				Log.e("Channel name",
						"----------------------------------------");
				mChannelList.add(channelItem);

			}
		} catch (Exception e) {

		}
		return mChannelList;
	}

	public List<Inventory> parseInventoryList(String resultOutparam) {
		List<Inventory> mInventoryList = new ArrayList<Inventory>();
		try {
			JSONObject jObj = new JSONObject(resultOutparam);

			JSONObject jInventoryObj = jObj
					.getJSONObject("GetInventoryListByCustomerIdResult");

			JSONArray jResponseArray = jInventoryObj
					.getJSONArray("ResponseObject");
			for (int i = 0; i < jResponseArray.length(); i++) {
				Inventory inventoryItem = new Inventory();
				JSONObject jResponseObj = jResponseArray.getJSONObject(i);

				/*
				 * channelItem.setmChannelId(jResponseObj.get("Id").toString());
				 * channelItem
				 * .setmChannelName(jResponseObj.get("ChannelName").toString());
				 * channelItem
				 * .setmAddress(jResponseObj.get("ChannelAddress").toString());
				 * channelItem
				 * .setmPhoneNo(jResponseObj.get("ChannelPhoneNo").toString());
				 * channelItem
				 * .setmEmail(jResponseObj.get("ChannelEmailId").toString());
				 * channelItem
				 * .setmPictureURL(jResponseObj.get("PictureURL").toString());
				 * channelItem.setIsActive(jResponseObj.getBoolean("IsActive"));
				 */
				inventoryItem.setmProductId(jResponseObj.get("ProductId")
						.toString());
				inventoryItem.setmProductName(jResponseObj.get("ProductName")
						.toString());
				inventoryItem.setmManufactureName(jResponseObj.get(
						"ManufactureCompanyName").toString());
				inventoryItem.setmTotalStockQuantity(jResponseObj.get(
						"TotalStockQuantity").toString());
				inventoryItem.setmUnit(jResponseObj.get("Unit").toString());
				inventoryItem.setmWarehouseId(jResponseObj.get("WarehouseId")
						.toString());
				inventoryItem.setMlogo(jResponseObj.get("logo").toString());

				Log.e("Inventory name", jResponseObj.get("ProductId")
						.toString());
				Log.e("Inventory name", jResponseObj.get("ProductName")
						.toString());
				Log.e("Inventory name",
						jResponseObj.get("ManufactureCompanyName").toString());
				Log.e("Inventory name", jResponseObj.get("TotalStockQuantity")
						.toString());
				Log.e("Inventory name", jResponseObj.get("Unit").toString());
				Log.e("Inventory name", jResponseObj.get("WarehouseId")
						.toString());
				Log.e("Inventory name", jResponseObj.get("logo").toString());
				Log.e("Inventory name",
						"----------------------------------------");
				mInventoryList.add(inventoryItem);

			}
		} catch (Exception e) {

		}
		return mInventoryList;
	}

	public List<Order> parseOrderList(String resultOutparam) {
		List<Order> mOrderList = new ArrayList<Order>();

		try {
			JSONObject jObj = new JSONObject(resultOutparam);

			JSONObject jOrderObj = jObj
					.getJSONObject("GetOrderListByCidResult");

			JSONArray jResponseArray = jOrderObj.getJSONArray("ResponseObject");

			for (int i = 0; i < jResponseArray.length(); i++) {
				Order OrderItem = new Order();
				JSONObject jResponseObj = jResponseArray.getJSONObject(i);

				JSONObject ChannelOrder = jResponseObj
						.getJSONObject("ChannelOrder");

				JSONObject jOrderProductObj = ChannelOrder
						.getJSONObject("OrderProduct");

				Log.e("Warehouse name", jResponseObj.get("Id").toString());
				Log.e("Warehouse name", jResponseObj.get("ChannelName")
						.toString());

				Log.e("Warehouse name", jOrderProductObj.get("ProductName")
						.toString());
				Log.e("Warehouse name", ChannelOrder.get("OrderQty").toString());
				Log.e("Warehouse name", jResponseObj.get("PictureURL")
						.toString());
				Log.e("Warehouse name", ChannelOrder.get("StatusId").toString());
				Log.e("Warehouse name", ChannelOrder.get("OrderDate")
						.toString());

				Log.e("Warehouse name",
						"----------------------------------------");
				OrderItem.setmOrderId(jResponseObj.get("Id").toString());
				OrderItem.setmChannelName(jResponseObj.get("ChannelName")
						.toString());
				OrderItem.setmProductName(jOrderProductObj.get("ProductName")
						.toString());
				OrderItem.setmQuantity(ChannelOrder.get("OrderQty").toString());
				OrderItem.setmLogo(jResponseObj.get("PictureURL").toString());
				OrderItem.setmStatus(ChannelOrder.get("StatusId").toString());
				OrderItem.setmDate(ChannelOrder.get("OrderDate").toString());
				mOrderList.add(OrderItem);

			}
			Log.e("Warehouse name", "----------------------------------------"
					+ mOrderList.size());

		} catch (Exception e) {

		}
		return mOrderList;
	}

	public String parseCreateChannelResult(String result) {
		try {
			JSONObject jObj = new JSONObject(result);

			JSONObject jcreateChannelObj = jObj
					.getJSONObject("CreateChannelResult");
			return jcreateChannelObj.getString("ResponseObject");

		} catch (Exception e) {
			return "false";
		}

	}

	public String parseCreateWareHouseResult(String result) {
		try {
			JSONObject jObj = new JSONObject(result);

			JSONObject jcreateChannelObj = jObj
					.getJSONObject("CU_WarehouseResult");
			return (jcreateChannelObj.getString("IsError"));

		} catch (Exception e) {
			return "false";
		}
	}

	public List<InventoryWarehouseList> parseinventorywarehouseList(
			String resultOutparam) {
		List<InventoryWarehouseList> inventoryWarehouseList = new ArrayList<InventoryWarehouseList>();
		try {
			JSONObject jObj = new JSONObject(resultOutparam);

			JSONObject jinventorydetailsproductObj = jObj
					.getJSONObject("GetInventoryDetailsByProductIdResult");
			JSONArray jResponseArray = jinventorydetailsproductObj
					.getJSONArray("ResponseObject");
			Log.e("Warearray", jResponseArray.length() + "");
			for (int i = 0; i < jResponseArray.length(); i++) {
				InventoryWarehouseList inventoryWarehouseItem = new InventoryWarehouseList();
				JSONObject jResponseObj = jResponseArray.getJSONObject(i);
				inventoryWarehouseItem.setWarehousename(jResponseObj
						.getString("WareHouseName"));
				inventoryWarehouseItem.setAvailablequantity(jResponseObj
						.getString("StockQuantity"));
				inventoryWarehouseItem.setAvailableproductunit(jResponseObj
						.getString("Unit"));
				inventoryWarehouseList.add(inventoryWarehouseItem);
			}

		} catch (Exception e) {
			// return "false";
		}
		return inventoryWarehouseList;
	}

	public List<SearchResults> parseSearchResults(String mResult) {
		List<SearchResults> mSearchResultsList = new ArrayList<SearchResults>();
		try {
			SearchResults mSearchResults = new SearchResults();
			JSONObject mObject = new JSONObject(mResult);
			JSONObject mSearchMResult = mObject.getJSONObject("SearchMResult");
			JSONObject mResponseObject = mSearchMResult
					.getJSONObject("ResponseObject");
			String mServiceGroupType = mResponseObject
					.getString("ServiceGroupType");
			mSearchResults.setmServiceGroupType(mServiceGroupType);
			String msearch_q = mResponseObject.getString("search_q");
			mSearchResults.setMsearch_q(msearch_q);

			// products array
			List<SearchProducts> mSearchProductsList = null;
			JSONArray mProductsArray = mResponseObject.getJSONArray("Products");
			if (mProductsArray == null || mProductsArray.length() == 0) {

			} else {
				mSearchProductsList = new ArrayList<SearchProducts>();
				int prodLen = mProductsArray.length();
				for (int i = 0; i < prodLen; i++) {
					JSONObject mProduct = mProductsArray.getJSONObject(i);
					SearchProducts mProducts = new SearchProducts();
					mProducts.setmCategoryId(mProduct.getString("CategoryId"));
					mProducts.setmCategoryName(mProduct
							.getString("CategoryName"));
					mProducts.setmDiscountAmount(mProduct
							.getString("DiscountAmount"));
					mProducts.setmDiscountPercentage(mProduct
							.getString("DiscountPercentage"));
					mProducts.setmFeedbackCount(mProduct
							.getString("FeedbackCount"));
					mProducts.setmLikesCount(mProduct.getString("LikesCount"));
					mProducts.setmOfferPrice(mProduct.getString("OfferPrice"));
					mProducts.setmPrice(mProduct.getString("Price"));
					mProducts.setmProductAttributes(mProduct
							.getString("ProductAttributes"));
					mProducts.setmProductId(mProduct.getString("ProductId"));
					mProducts
							.setmProductName(mProduct.getString("ProductName"));
					mProducts.setmProductPicture(mProduct
							.getString("ProductPicture"));
					mProducts.setmProductPictureList(mProduct
							.getString("ProductPictureList"));
					mProducts.setmQuantity(mProduct.getString("Quantity"));
					mProducts.setmShippingDetails(mProduct
							.getString("ShippingDetails"));
					mProducts.setmShortDescription(mProduct
							.getString("ShortDescription"));
					mProducts.setmUnitOfMeasurement(mProduct
							.getString("UnitOfMeasurement"));

					JSONObject mManufacture = mProduct
							.getJSONObject("Manufacture");
					mProducts.setmMfrContactDetails(mManufacture
							.getString("ContactDetails"));
					mProducts.setmMfrDescription(mManufacture
							.getString("Description"));
					mProducts.setmMfrId(mManufacture.getString("Id"));
					mProducts.setmMfrManufacturerName(mManufacture
							.getString("ManufacturerName"));
					mProducts.setmMfrName(mManufacture.getString("Name"));

					JSONObject mLogo = mManufacture.getJSONObject("Logo");
					mProducts.setmMfrLogoUrl(mLogo.getString("url"));

					mSearchProductsList.add(mProducts);
				}
			}
			mSearchResults.setmSearchProductsList(mSearchProductsList);

			// attributesModel object
			JSONObject mAttributesModelArray = mResponseObject
					.getJSONObject("attributesModel");

			// ProductVariantAttributes array
			List<ProductVariantAttributes> mProductVariantAttributes = null;

			JSONArray mProductVariantAttributesArray = mAttributesModelArray
					.getJSONArray("ProductVariantAttributes");
			if (mProductVariantAttributesArray == null
					|| mProductVariantAttributesArray.length() == 0) {

			} else {
				int prodVariantlen = mProductVariantAttributesArray.length();
				mProductVariantAttributes = new ArrayList<ProductVariantAttributes>();
				for (int i = 0; i < prodVariantlen; i++) {
					JSONObject mProductAttribute = mProductVariantAttributesArray
							.getJSONObject(i);
					ProductVariantAttributes pAttributes = new ProductVariantAttributes();
					pAttributes.setmColorSquaresRgb(mProductAttribute
							.getString("ColorSquaresRgb"));
					pAttributes.setmId(mProductAttribute.getString("Id"));
					pAttributes.setmName(mProductAttribute.getString("Name"));

					// ProductVariantAttributesOptions Array
					List<ProductVariantOptions> mProductVriantOptionsList = null;
					JSONArray mProductAttOptions = mProductAttribute
							.getJSONArray("ProductVariantAttributesOptions");
					if (mProductAttOptions == null
							|| mProductAttOptions.length() == 0) {

					} else {
						mProductVriantOptionsList = new ArrayList<ProductVariantOptions>();
						int attOptionlen = mProductAttOptions.length();
						for (int j = 0; j < attOptionlen; j++) {
							JSONObject options = mProductAttOptions
									.getJSONObject(j);
							ProductVariantOptions attOptions = new ProductVariantOptions();
							attOptions.setmCheckedState(options
									.getString("CheckedState"));
							attOptions.setmCount(options.getString("Count"));
							attOptions.setmName(options.getString("Name"));
							mProductVriantOptionsList.add(attOptions);
						}
					}
					pAttributes
							.setmProductVriantOptionsList(mProductVriantOptionsList);
					mProductVariantAttributes.add(pAttributes);
				}
			}
			mSearchResults
					.setmProductVariantAttributes(mProductVariantAttributes);
			mSearchResultsList.add(mSearchResults);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mSearchResultsList;
	}

	public List<WishlistDetails> parseWishListDetails(String resultOutparam,
			Context applicationContext) {
		List<WishlistDetails> details = new ArrayList<WishlistDetails>();
		try {
			JSONObject jObj = new JSONObject(resultOutparam);

			JSONObject WishListObj = jObj.getJSONObject("GetWishListResult");
			JSONObject jResponseObj = WishListObj
					.getJSONObject("ResponseObject");

			JSONArray jProductWish = jResponseObj.getJSONArray("ProductWish");

			for (int i = 0; i < jProductWish.length(); i++) {
				WishlistDetails wishdetails = new WishlistDetails();
				JSONObject jobject = jProductWish.getJSONObject(i);

				wishdetails
						.setmCustomerId(getObjectvalue(jobject, "CustomerId"));
				wishdetails.setProduct_id(getObjectvalue(jobject, "ProductId"));
				wishdetails.setProduct_nam(getObjectvalue(jobject,
						"ProductName"));
				wishdetails.setmProductPrice(getObjectvalue(jobject,
						"ProductPrice"));
				wishdetails
						.setProduct_img(getObjectvalue(jobject, "PictureUrl"));
				wishdetails.setmServiceGroupType(getObjectvalue(jobject,
						"ServiceGroupId"));

				JSONObject ManufacturerObject = jobject
						.getJSONObject("Manufacturer");
				if (ManufacturerObject == null
						|| ManufacturerObject.length() == 0) {
				} else {
					wishdetails.setmManufactureId(getObjectvalue(
							ManufacturerObject, "Id"));
					wishdetails.setProduct_manufname(getObjectvalue(
							ManufacturerObject, "Name"));
				}

				details.add(wishdetails);
			}

		} catch (Exception e) {
		}
		return details;
	}

	public String parseCreateWishlist(String result) {
		String resultcreatewish = null;
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject jsonObject2 = jsonObject
					.getJSONObject("CreateUpdateWishlistResult");
			resultcreatewish = jsonObject2.getString("ResponseObject");
		} catch (Exception e) {

		}
		return resultcreatewish;
	}

	public String parseDeleteWishlist(String resultOutparam) {
		String resultcreatewish = null;
		try {
			JSONObject jsonObject = new JSONObject(resultOutparam);
			JSONObject jsonObject2 = jsonObject
					.getJSONObject("DeleteWishlistResult");
			resultcreatewish = jsonObject2.getString("ResponseObject");
		} catch (Exception e) {

		}
		return resultcreatewish;
	}

	public String parseForgotPassword(String resultOutparam) {
		String resultforgot = null;
		try {
			JSONObject jsonObject = new JSONObject(resultOutparam);
			JSONObject jsonObject2 = jsonObject
					.getJSONObject("GetforgotPasswordResult");
			resultforgot = jsonObject2.getString("ResponseObject");
		} catch (Exception e) {

		}
		return resultforgot;
	}

	private String getObjectvalue(JSONObject listObj, String keyOfObject) {
		try {
			try {
				String mKeyValue = listObj.getString(keyOfObject).toString();
				if (mKeyValue.equals("null")) {
				} else {
					return mKeyValue;
				}
			} catch (NullPointerException nullExp) {
			}
		} catch (JSONException e) {
		}
		return "";
	}

}
