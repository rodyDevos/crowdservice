package com.synapse.gofer.parser;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.synapse.gofer.http.HttpPostConnector;

public class FundPostParser {

	String data = null;
	String strUrl;

	public FundPostParser(String url) {
		// TODO Auto-generated constructor stub
		strUrl = url;
	}

	public String getParseData(ArrayList<NameValuePair> nameValueParams) {
		try {
			HttpPostConnector conn = new HttpPostConnector(strUrl,
					nameValueParams);
			String response = conn.getResponseData();
			if (response != null) {
				Log.e("VIPIII", "res > " + response);
				JSONObject jsonresponse;
				try {
					jsonresponse = new JSONObject(response);
					if (jsonresponse.getString("status").equals("success"))
						data = jsonresponse.getString("message");
					else
						data = "";
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
