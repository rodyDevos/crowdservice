package com.synapse.gofer.parser;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.UserDetail;

public class UserDataPostParser {

	UserDetail data = null;
	String strUrl;

	public UserDataPostParser(String url) {
		// TODO Auto-generated constructor stub
		strUrl = url;
	}

	public UserDetail getParseData(ArrayList<NameValuePair> nameValueParams) {
		try {
			HttpPostConnector conn = new HttpPostConnector(strUrl,
					nameValueParams);
			String response = conn.getResponseData();
			if (response != null) {
				Log.d("TAGG", "UserProf- > " + response);
				JSONObject jsonresponse;
				try {
					data = new UserDetail();
					jsonresponse = new JSONObject(response);
					if (jsonresponse.has("id"))
						data.setId(jsonresponse.getString("id"));
					if (jsonresponse.has("username"))
						data.setUsername(jsonresponse.getString("username"));
					if (jsonresponse.has("email"))
						data.setEmail(jsonresponse.getString("email"));
					if (jsonresponse.has("first_name"))
						data.setFirst_name(jsonresponse.getString("first_name"));
					if (jsonresponse.has("provider_job_count"))
						data.setCJCount(jsonresponse
								.getString("provider_job_count"));
					if (jsonresponse.has("customer_job_count"))
						data.setSJCount(jsonresponse
								.getString("customer_job_count"));
					if (jsonresponse.has("karma_count"))
						data.setKarmaCount(jsonresponse
								.getString("karma_count"));

					if (jsonresponse.has("last_name"))
						data.setLast_name(jsonresponse.getString("last_name"));
					if (jsonresponse.has("billing_add1"))
						data.setAddress1(jsonresponse.getString("billing_add1"));
					if (jsonresponse.has("address2"))
						data.setAddress2(jsonresponse.getString("address2"));
					if (jsonresponse.has("address3"))
						data.setAddress3(jsonresponse.getString("address3"));
					if (jsonresponse.has("address4"))
						data.setAddress4(jsonresponse.getString("address4"));
					if (jsonresponse.has("address5"))
						data.setAddress5(jsonresponse.getString("address5"));
					if (jsonresponse.has("aprox_address"))
						data.setAprox_address1(jsonresponse
								.getString("aprox_address"));
					if (jsonresponse.has("aprox_address2"))
						data.setAprox_address2(jsonresponse
								.getString("aprox_address2"));
					if (jsonresponse.has("aprox_address3"))
						data.setAprox_address3(jsonresponse
								.getString("aprox_address3"));
					if (jsonresponse.has("aprox_address4"))
						data.setAprox_address4(jsonresponse
								.getString("aprox_address4"));
					if (jsonresponse.has("aprox_address5"))
						data.setAprox_address5(jsonresponse
								.getString("aprox_address5"));
					if (jsonresponse.has("image_big"))
						data.setImage_big(jsonresponse.getString("image_big"));
					if (jsonresponse.has("favorite_quote"))
						data.setFavorite_quote(jsonresponse
								.getString("favorite_quote"));
					if (jsonresponse.has("avg_rating"))
						data.setAvg_rating(jsonresponse.getString("avg_rating"));
					if (jsonresponse.has("about"))
						data.setAbout(jsonresponse.getString("about"));
					if (jsonresponse.has("cardInfo"))
						data.setCreditCard(jsonresponse.getString("cardInfo"));
					if (jsonresponse.has("BankInfo"))
						data.setBankDetail(jsonresponse.getString("BankInfo"));
					if (jsonresponse.has("Paypal_id"))
						data.setPaypal(jsonresponse.getString("Paypal_id"));
					if (jsonresponse.has("security_shield")) {
						Log.d("Security Response",
								jsonresponse.getString("security_shield"));
						data.setSecurity(jsonresponse
								.getString("security_shield"));

					}
					if (jsonresponse.has("couriercategory"))
						data.setcouriercategory(jsonresponse
								.getString("couriercategory"));
					if (jsonresponse.has("homecategory"))
						data.sethomecategory(jsonresponse
								.getString("homecategory"));

				} catch (Exception ex) {
					// TODO: handle exception
					Log.e("PostDataParserException", ">>>" + ex.getMessage());
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("ParserException", ">>>" + e.getMessage());
		}
		return data;
	}
}
