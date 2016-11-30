package com.synapse.gofer.parser;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.model.ProfileJobData;

public class ProfileJobParser {
	ArrayList<ProfileJobData> list = null;
	String strUrl;

	public ProfileJobParser(String url) {
		// TODO Auto-generated constructor stub
		strUrl = url;
	}

	public ArrayList getParseData(ArrayList<NameValuePair> nameValueParams,
			String isProvierJob) {
		try {
			HttpPostConnector conn = new HttpPostConnector(strUrl,
					nameValueParams);
			String response = conn.getResponseData();
			Log.d("TAGG", "ProfileJob ::> " + response);
			if (response != null) {
				JSONObject jsonresponse;
				try {
					jsonresponse = new JSONObject(response);

					list = new ArrayList<ProfileJobData>(1);
					JobsModel model = new JobsModel();

					if (jsonresponse.getString("status").equalsIgnoreCase(
							"success")) {
						Log.d("Provier", isProvierJob);
						if (jsonresponse.has(isProvierJob)) {
							JSONArray jobArray = jsonresponse
									.getJSONArray(isProvierJob);
							int count = jobArray.length();
							for (int i = 0; i < count; i++) {
								ProfileJobData profileJobData = new ProfileJobData();
								JSONObject jsonObject = jobArray
										.getJSONObject(i);

								// if(jsonObject.has("0"))
								// { Log.d("Asahish7 " ,"test ");
								// JSONObject jobsJson =
								// jsonObject.getJSONObject(i);
								if (jsonObject.has("jobs")) {
									JSONObject jobsDataJson = jsonObject
											.getJSONObject("jobs");
									String value = null;
									String key = null;
									if (jobsDataJson.has("id"))
										profileJobData.setJobId(jobsDataJson
												.getString("id"));
									profileJobData.setStatus(jsonresponse
											.getString("status"));
									profileJobData.setMessage(jsonresponse
											.getString("message"));
									profileJobData.setJobId(key);

									if (jobsDataJson.has("name"))
										profileJobData.setJobName(jobsDataJson
												.getString("name"));
								}

								if (jsonObject.has("categories")) {
									JSONObject categoryJsonObj = jsonObject
											.getJSONObject("categories");
									if (categoryJsonObj.has("name"))
										profileJobData
												.setJobCategory(categoryJsonObj
														.getString("name"));
								}

								if (jsonObject.has("ratings")) {
									JSONObject feedJsonObj = jsonObject
											.getJSONObject("ratings");
									Float a;
									if (feedJsonObj.has("rate")) {
										if (!(feedJsonObj.getString("rate")
												.equals(""))
												&& !(feedJsonObj
														.getString("rate")
														.equals(" "))
												&& (feedJsonObj
														.getString("rate") != null)) {

											a = Float.valueOf(feedJsonObj
													.getString("rate"));

										} else {
											a = 0.0f;
										}

										int b = Math.round(a);
										int c = (int) b;

										profileJobData.setJobRating(Integer
												.toString(c));
									}

									if (feedJsonObj.has("feedback"))
										profileJobData
												.setJobFeedback(feedJsonObj
														.getString("feedback"));

								}
								list.add(profileJobData);
							}
						}
					}
				} catch (Exception e) {
					Log.e("ParserException", ">>>" + e.getMessage());
				}
			}
		} catch (Exception e) {
			Log.e("ParserException1", ">>>" + e.getMessage());
		}
		return list;
	}
}