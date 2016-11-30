package com.synapse.gofer.parser;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.BiddingDetail;
import com.synapse.gofer.model.Category;
import com.synapse.gofer.model.JobBean;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.JobProfile;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.model.ProfileData;
import com.synapse.gofer.model.UserDetail;


public class ProfilePostParser 
{

	ProfileData pdata=null;
	String strUrl;

	public ProfilePostParser(String url) 
	{
		//TODO Auto-generated constructor stub
		strUrl=url;
	}

	public  ProfileData getParseData(ArrayList<NameValuePair> nameValueParams)
	{		
		try
		{
			HttpPostConnector conn=new HttpPostConnector(strUrl, nameValueParams);
			String response=conn.getResponseData();			
			if(response!=null)
			{
				JSONObject jsonresponse;				
				try
				{
					jsonresponse=new JSONObject(response);
					pdata=new ProfileData();					
					if(jsonresponse.getString("status").equalsIgnoreCase("success"))
					{						
						pdata.setMessage(jsonresponse.getString("message"));
						pdata.setImage_big(jsonresponse.getString("image_big"));
						pdata.setJobprofile1(jsonresponse.getString("jobprofile1"));
						pdata.setJobprofile2(jsonresponse.getString("jobprofile2"));
						pdata.setJobprofile3(jsonresponse.getString("jobprofile3"));
						pdata.setJobprofile4(jsonresponse.getString("jobprofile4"));
						pdata.setJobprofile5(jsonresponse.getString("jobprofile5"));
						pdata.setJobprofile6(jsonresponse.getString("jobprofile6"));
						pdata.setJobprofile7(jsonresponse.getString("jobprofile7"));
						pdata.setJobprofile8(jsonresponse.getString("jobprofile8"));
						pdata.setJobprofile9(jsonresponse.getString("jobprofile9"));
						pdata.setJobprofile10(jsonresponse.getString("jobprofile10"));
												
						if(jsonresponse.has("Data_job"))
						{
							JSONArray jobArray = jsonresponse.getJSONArray("Data_job");							
							JSONObject jsonObject = (JSONObject) jobArray.get(0);
														
							if(jsonObject.has("0"))
							{
								JSONObject biddingJson = jsonObject.getJSONObject("0");
								BiddingDetail bDetail = new BiddingDetail();
								bDetail.setCount(biddingJson.getString("count"));								
								pdata.setBiddetail(bDetail);
							}				
							if(jsonObject.has("Jobprofile"))
							{
								JobProfile profile = new JobProfile();
								
								JSONObject jobJsonObject = jsonObject.getJSONObject("Jobprofile");
								
								profile.setFavorite_quote(jobJsonObject.getString("favorite_quote"));
								profile.setHome_link(jobJsonObject.getString("home_link"));
								profile.setId(jobJsonObject.getString("id"));
								profile.setImage(jobJsonObject.getString("image"));
								profile.setJobprofile1(jobJsonObject.getString("jobprofile1"));
								profile.setJobprofile2(jobJsonObject.getString("jobprofile2"));
								profile.setJobprofile3(jobJsonObject.getString("jobprofile3"));
								profile.setJobprofile4(jobJsonObject.getString("jobprofile4"));
								profile.setJobprofile5(jobJsonObject.getString("jobprofile5"));
								profile.setJobprofile6(jobJsonObject.getString("jobprofile6"));
								profile.setJobprofile7(jobJsonObject.getString("jobprofile7"));
								profile.setJobprofile8(jobJsonObject.getString("jobprofile8"));
								profile.setJobprofile9(jobJsonObject.getString("jobprofile9"));
								profile.setJobprofile10(jobJsonObject.getString("jobprofile10"));
								if(jobJsonObject.has("user_description"))
									profile.setUser_description(jobJsonObject.getString("user_description"));
								if(jobJsonObject.has("approx_location"))
									profile.setApprox_location(jobJsonObject.getString("approx_location"));
								if(jobJsonObject.has("preferred_services"))
									profile.setPreferred_services(jobJsonObject.getString("preferred_services"));
								
								pdata.setJobprofile(profile);
							}							
							if(jsonObject.has("User"))
							{
								UserDetail user = new UserDetail();								
								JSONObject userJsonObject = jsonObject.getJSONObject("User");
								user.setId(userJsonObject.getString("id"));
								user.setUsername(userJsonObject.getString("username"));
								user.setFirst_name(userJsonObject.getString("first_name"));
								user.setLast_name(userJsonObject.getString("last_name"));
								user.setImage_big(userJsonObject.getString("image"));
								user.setAvg_rating(userJsonObject.getString("avg_rating"));
								user.setKpoint(userJsonObject.getString("kpoint"));
								user.setSecurity(userJsonObject.getString("security"));								
								pdata.setUserdetail(user);
							}
							
							
						}
						
					}
					
				}catch (Exception ex) {
					pdata = null;
					Log.e("PostDataParserException",">>>"+ex.getMessage());
				}	
			}

		}catch (Exception e) {
			pdata = null;
			Log.e("ParserException",">>>"+e.getMessage());
		}		
		return pdata;
	}
}
