package com.synapse.gofer;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.Utils;

public class TipForTheDay extends AsyncTask {

	private  ProgressDialog progressBar = null;
	private Context context = null;

	public TipForTheDay(Context ctx)
	{
		progressBar = new ProgressDialog(ctx);
		progressBar.setCancelable(false);
		progressBar.setMessage("Please wait...");
		context = ctx;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressBar.show();
	}

	@Override
	protected String doInBackground(Object... arg0) {
		String responseJson = null;
		String url = Constants.HTTP_HOST+"viewtodaytip";
		HttpPostConnector conn=new HttpPostConnector(url, null);
		HttpClient client = new DefaultHttpClient();
		HttpGet httpRequest = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(httpRequest);
			if(response != null)
			{
				responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return responseJson;
	}

	@Override
	protected void onPostExecute(Object result)
	{
		super.onPostExecute(result);
		progressBar.dismiss();
		String response = (String) result;
		if(response != null && response.length() > 0)
		{
			try {
				JSONObject respoonseJson = new JSONObject(response);

				if(respoonseJson.has("status") && respoonseJson.getString("status").equalsIgnoreCase("success"))
				{
					if(respoonseJson.has("tip_data"))
					{
						JSONArray array = respoonseJson.getJSONArray("tip_data");

						JSONObject tipObj = array.getJSONObject(0);

						if(tipObj.has("Tip"))
						{

							SharedPreferences pref = context.getSharedPreferences("goffer_pref", Context.MODE_PRIVATE);
							Date d = new Date();
							pref.edit().putLong("tipDisplayDate", d.getTime()).commit();
							tipObj = tipObj.getJSONObject("Tip");
							Utils.displayOkAlert(context, tipObj.getString("message"));
						}
						else
							Utils.displayOkAlert(context, "No tip for the day");
					}
				}
				else
					Utils.displayOkAlert(context, "No tip for the day");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}