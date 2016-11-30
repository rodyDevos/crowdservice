package com.synapse.gofer.parser;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.Category;

public class CategoryPostParser {

	ArrayList<Category> list = null;
	String strUrl;

	public CategoryPostParser(String url) {
		// TODO Auto-generated constructor stub
		strUrl = url;
	}

	public ArrayList getParseData(ArrayList<NameValuePair> nameValueParams) {
		try {
			Log.e("TAGG", "URL Cat > " + strUrl);
			HttpPostConnector conn = new HttpPostConnector(strUrl,
					nameValueParams);
			String response = conn.getResponseData();
			Log.e("TAGG", "response Cat > " + response);
			if (response != null) {
				JSONObject jsonresponse;
				try {
					list = new ArrayList<Category>();
					jsonresponse = new JSONObject(response);
					JSONArray arrCategory = jsonresponse
							.getJSONArray("Data_category");
					if (arrCategory != null && arrCategory.length() > 0) {
						for (int i = 0; i < arrCategory.length(); i++) {
							JSONObject objData = arrCategory.getJSONObject(i);
							JSONObject objCat;
							/*
							 * if(objData.has("homecategories"))
							 * objCat=objData.getJSONObject("homecategories");
							 * else
							 */
							objCat = objData.getJSONObject("categories");
							Category category = new Category("", "");
							category.setId(objCat.getString("id"));
							Log.e("VIP", "ID >" + objCat.getString("id"));
							category.setName(objCat.getString("name"));
							Log.e("VIP", "NAME >" + objCat.getString("name"));

							list.add(category);
						}
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
