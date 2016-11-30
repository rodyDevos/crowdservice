package com.synapse.gofer.parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.util.Constants;

public class JobAcceptParser {

	String mUrl;

	public JobAcceptParser(String url) {
		mUrl = url;
	}

	public HashMap<String, String> getParseData(
			ArrayList<NameValuePair> nameValueParams) {
		String status = "fail";
		String message = "";
		try {
			HttpPostConnector conn = new HttpPostConnector(mUrl,
					nameValueParams);
			String response = conn.getResponseData();
			if (response != null) {
				JSONObject obj = new JSONObject(response);

				status = obj.getString("status");
				message = obj.getString("message");
				Log.e("VIPII",
						"JobAcceptParser >> "
								+ Integer.parseInt(obj.getString("is_customer")));
				Constants.userType = Integer.parseInt(obj
						.getString("is_customer"));
			}
		} catch (Exception e) {
			Log.e("ParserException", ">>>" + e.getMessage());
		}

		HashMap<String, String> ret = new HashMap<String, String>();
		ret.put("status", status);
		ret.put("message", message);

		return ret;
	}
}
