package com.synapse.gofer.parser;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.Card;
import com.synapse.gofer.model.ResultData;

public class CardPostParser 
{
	
	Card resultData =  null;
	String strUrl;
	
	public CardPostParser(String url) 
	{
		strUrl = url;
		resultData =  null;
	}
	
	public Card getParseData(ArrayList<NameValuePair> params)
	{		
		try
		{
			HttpPostConnector conn=new HttpPostConnector(strUrl,params);
			String response=conn.getResponseData();
			if(response!=null)
			{
				JSONObject jsonresponse;					
				try
				{					
					jsonresponse  = new JSONObject(response);
					
					if(jsonresponse.has("status"))
					{
						if(jsonresponse.getString("status").equalsIgnoreCase("success"))
						{
							resultData = new Card();
							resultData.setStatus("success");
							resultData.setMessage(jsonresponse.getString("message"));
							if(jsonresponse.has("card_data"))
							{
								jsonresponse = jsonresponse.getJSONArray("card_data").getJSONObject(0);
								jsonresponse = jsonresponse.getJSONObject("User");
								
								resultData.setCardCvvNumber(jsonresponse.getString("cardCvvNumber"));
								resultData.setCardNumber(jsonresponse.getString("cardNumber"));
								resultData.setDcExpMonth(jsonresponse.getString("dcExpMonth"));
								resultData.setDcExpYear(jsonresponse.getString("dcExpYear"));
							}
							
						}
					}
										
					
					
				}catch (Exception ex){
					Log.e("CardPostParser",">>>"+ ex.getMessage().toString());
				}	
			}
			
		}catch (Exception e) {
			Log.e("CardPostParser",">>>"+e.getMessage());
		}		
		return resultData;
	}
}
