package com.synapse.gofer.parser;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.BiddingDetail;
import com.synapse.gofer.model.CourierModel;
import com.synapse.gofer.model.UserDetail;

public class CourierListParser {

	ArrayList<CourierModel> list = null;
	String strUrl;

	public CourierListParser(String url) {
		// TODO Auto-generated constructor stub
		strUrl = url;
	}

	public ArrayList<CourierModel> getParseData(
			ArrayList<NameValuePair> nameValueParams) {
		try {
			HttpPostConnector conn = new HttpPostConnector(strUrl,
					nameValueParams);
			String response = conn.getResponseData();
			Log.e("VIPIII", "response List >> " + response);
			if (response != null) {
				JSONObject jsonresponse;
				try {
					jsonresponse = new JSONObject(response);

					if (jsonresponse.getString("status").equalsIgnoreCase(
							"success")) {

						JSONArray jsonArray = jsonresponse
								.getJSONArray("Data_user");
						int count = jsonArray.length();
						list = new ArrayList<CourierModel>(count);

						for (int i = 0; i < count; i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							CourierModel model = new CourierModel();
							if (jsonObject.has("Bid")) {
								JSONObject obj = jsonObject
										.getJSONObject("Bid");

								BiddingDetail bid = new BiddingDetail();

								bid.setId(obj.getString("id"));
								bid.setJobId(obj.getString("job_id"));
								bid.setLinkStatus(obj.getString("link_status"));
								bid.setAmount(obj.getString("amount"));
								bid.setDate(obj.getString("date"));

								model.setBid(bid);
							}

							if (jsonObject.has("User")) {
								JSONObject objUser = jsonObject
										.getJSONObject("User");
								UserDetail user = new UserDetail();

								user.setId(objUser.getString("id"));
								user.setFirst_name(objUser
										.getString("first_name"));
								user.setLast_name(objUser
										.getString("last_name"));
								user.setUsername(objUser.getString("username"));

								if (objUser.has("avg_rating"))
									user.setAvg_rating(objUser
											.getString("avg_rating"));
								model.setUser(user);
							}
							model.setStatus("success");
							list.add(model);
						}
					} else {
						list = new ArrayList<CourierModel>(1);
						CourierModel model = new CourierModel();
						model.setStatus("fail");
						model.setMessage(jsonresponse.getString("message"));
					}

				} catch (Exception ex) {
					// TODO: handle exception
					Log.e("PostDataParserException", ">>>" + ex.getMessage());
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("ParserException", ">>>" + e.getMessage());
		}
		return list;
	}
}
