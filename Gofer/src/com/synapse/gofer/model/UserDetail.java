package com.synapse.gofer.model;

import java.io.Serializable;

import android.util.Log;

public class UserDetail implements Serializable {

	private String id = "";
	private String ratingbar = "";
	private String username = "";
	private String email = "";
	private String first_name = "";
	private String last_name = "";
	private String about = "";
	private String address1 = "";
	private String address2 = "";
	private String address3 = "";
	private String address4 = "";
	private String address5 = "";

	private String aprox_address1 = "";
	private String aprox_address2 = "";
	private String aprox_address3 = "";
	private String aprox_address4 = "";
	private String aprox_address5 = "";

	private String image_big = "";
	private String favorite_quote = "";

	private String avg_rating = "";
	private String kpoint = "";
	private String security = "";

	private String userType = "";
	private String latitude = "";
	private String longitude = "";
	private String location = "";

	private String servicejobcount = "";
	private String customerjobcount = "";
	private String temp_karma_count = "";
	private String bankdetails = "";
	private String creditcard = "";
	private String paypal = "";

	private String couriercategory = "";
	private String homecategory = "";
	private boolean isLogin = false;
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreditCard() {
		return creditcard;
	}

	public void setCreditCard(String creditcard) {
		this.creditcard = creditcard;
	}

	public String getBankDetail() {
		return bankdetails;
	}

	public void setBankDetail(String bankdetails) {
		this.bankdetails = bankdetails;
	}

	public String getPaypal() {
		return paypal;
	}

	public void setPaypal(String paypal) {
		this.paypal = paypal;
	}

	public String getSJCount() {
		return servicejobcount;
	}

	public void setSJCount(String servicejobcount) {
		this.servicejobcount = servicejobcount;
	}

	public String getCJCount() {
		return customerjobcount;
	}

	public void setKarmaCount(String temp_karma_count) {
		this.temp_karma_count = temp_karma_count;
	}

	public String getKarmaCount() {
		return temp_karma_count;
	}

	public void setCJCount(String customerjobcount) {

		this.customerjobcount = customerjobcount;
	}

	public String getAbout() {
		Log.d("about2", "" + about);
		return about;
	}

	public void setAbout(String about) {
		Log.d("about", "" + about);
		this.about = about;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getRatingBar() {
		return ratingbar;
	}

	public void setRatingBar(String ratingbar) {

		this.ratingbar = ratingbar;
		Log.d("ratirati", "rati" + ratingbar);
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public String getAddress5() {
		return address5;
	}

	public void setAddress5(String address5) {
		this.address5 = address5;
	}

	public String getAprox_address1() {
		return aprox_address1;
	}

	public void setAprox_address1(String aprox_address1) {
		this.aprox_address1 = aprox_address1;
	}

	public String getAprox_address2() {
		return aprox_address2;
	}

	public void setAprox_address2(String aprox_address2) {
		this.aprox_address2 = aprox_address2;
	}

	public String getAprox_address3() {
		return aprox_address3;
	}

	public void setAprox_address3(String aprox_address3) {
		this.aprox_address3 = aprox_address3;
	}

	public String getAprox_address4() {
		return aprox_address4;
	}

	public void setAprox_address4(String aprox_address4) {
		this.aprox_address4 = aprox_address4;
	}

	public String getAprox_address5() {
		return aprox_address5;
	}

	public void setAprox_address5(String aprox_address5) {
		this.aprox_address5 = aprox_address5;
	}

	public String getImage_big() {
		return image_big;
	}

	public void setImage_big(String image_big) {
		this.image_big = image_big;
	}

	public String getFavorite_quote() {
		return favorite_quote;
	}

	public void setFavorite_quote(String favorite_quote) {
		this.favorite_quote = favorite_quote;
	}

	public String getAvg_rating() {
		return avg_rating;
	}

	public void setAvg_rating(String avg_rating) {
		this.avg_rating = avg_rating;
	}

	public String getKpoint() {
		return kpoint;
	}

	public void setKpoint(String kpoint) {
		this.kpoint = kpoint;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getcouriercategory() {
		return couriercategory;
	}

	public void setcouriercategory(String couriercategory) {
		this.couriercategory = couriercategory;
	}

	public String gethomecategory() {
		return homecategory;
	}

	public void sethomecategory(String homecategory) {
		this.homecategory = homecategory;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

}
