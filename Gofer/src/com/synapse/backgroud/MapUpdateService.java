package com.synapse.backgroud;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.BiddingDetail;
import com.synapse.gofer.model.Category;
import com.synapse.gofer.model.JobBean;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.util.Constants;

public class MapUpdateService extends Service {

	private MapUpdateListner updateListner = null;
	private Timer updateTimer = null;
	public static MapUpdateService self  = null;

	@Override
	public void onCreate() {
		super.onCreate();
		self = this;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		updateTimer = new Timer();
		updateTimer.schedule(new Updater(), 10000, Constants.mapRefreshRate);
		self = this;
		return Service.START_NOT_STICKY;
	}

	public void changeRefreshRate(long millisec)
	{
		if(updateTimer != null)
		{
			updateTimer.cancel();
			updateTimer.purge();
			updateTimer = null;
		}

		updateTimer = new Timer();
		updateTimer.schedule(new Updater(), millisec, millisec);
	}
	
	public void changeRefreshRate(long millisec,long delay)
	{
		if(updateTimer != null)
		{
			updateTimer.cancel();
			updateTimer.purge();
			updateTimer = null;
		}
                                                                                                                  
		updateTimer = new Timer();
		updateTimer.schedule(new Updater(), delay, millisec);
	}
	
	public void changeMapCondition(int condition, long millisec, long delay)
	{
		if(updateTimer != null)
		{
			updateTimer.cancel();
			updateTimer.purge();
			updateTimer = null;
		}
                                                                                                                  
		updateTimer = new Timer();
		updateTimer.schedule(new Updater(condition), delay, millisec);
	}
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public void setOnUpdateListener(MapUpdateListner listner)
	{
		updateListner = listner;
	}


	private class Updater1 extends TimerTask
	{
		@Override
		public void run() {
			if(updateListner != null)
				updateListner.updateStart();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("latitude", Constants.lat));
			nameValuePairs.add(new BasicNameValuePair("longitude", Constants.lon));
			nameValuePairs.add(new BasicNameValuePair("location", Constants.locationAdd));
			nameValuePairs.add(new BasicNameValuePair("job_distance",""+Constants.jobRadius));
			
			HttpPostConnector conn=new HttpPostConnector(Constants.HTTP_HOST+"viewMapService", nameValuePairs);
			String response=conn.getResponseData();			
			if(response != null && response.length() > 0)
			{
				try {
					JSONObject responseObject = new JSONObject(response);

					if(responseObject.has( "status") && responseObject.getString( "status").equalsIgnoreCase("success"))
					{
						String loc = "";
						if(responseObject.has("location"))
							loc = responseObject.getString("location");

						if(responseObject.has("Data_job") && responseObject.get("Data_job") instanceof JSONArray)
						{
							Constants.jobsArray = new ArrayList<JobData>();

							JSONArray jobArray = responseObject.getJSONArray("Data_job");
							int count = jobArray.length();
							Log.d("Total Job : ",""+count);

							for (int i = 0; i < count; i++) 
							{
								JobData model = new JobData();
								JSONObject jsonObject = jobArray.getJSONObject(i);

								if(jsonObject.has("Job"))
								{
									JobBean job = new JobBean();

									JSONObject jobJsonObject = jsonObject.getJSONObject("Job");
									job.setId(jobJsonObject.getString("id"));
									job.setName(jobJsonObject.getString("name"));
									job.setCategoryId(jobJsonObject.getString("category_id"));
									job.setUserId(jobJsonObject.getString("user_id"));
									job.setAmount(jobJsonObject.getString("amount"));
									if(jobJsonObject.has("start_latitude"))
										job.setStartLatitude(jobJsonObject.getString("start_latitude"));
									if(jobJsonObject.has("start_longitude"))
										job.setStartlongitude(jobJsonObject.getString("start_longitude"));
									job.setStartDate(jobJsonObject.getString("start_date"));
									job.setEndDate(jobJsonObject.getString("end_date"));
									if(jobJsonObject.has("from_location"))
										job.setFromLocation(jobJsonObject.getString("from_location"));
									if(jobJsonObject.has("to_location"))
										job.setToLocation(jobJsonObject.getString("to_location"));
									job.setDescription(jobJsonObject.getString("description"));
									job.setDate(jobJsonObject.getString("date"));
									job.setStatus(jobJsonObject.getString("status"));
									if(jobJsonObject.has("image"))
										job.setImage(jobJsonObject.getString("image"));

									model.setJob(job);
								}

								if(jsonObject.has("Bid"))
								{
									BiddingDetail bDetail = new BiddingDetail();
									JSONObject biddingJson = jsonObject.getJSONObject("Bid");
									if(jsonObject.has("job_id"))
										bDetail.setJobPostedById(biddingJson.getString("job_id"));
									if(biddingJson.has("link_status"))
										bDetail.setLinkStatus(biddingJson.getString("link_status"));

									model.setBidDetail(bDetail);
								}


								if(jsonObject.has("Category"))
								{
									Category category = new Category("", "");

									JSONObject catJsonObject = jsonObject.getJSONObject("Category");

									category.setId(catJsonObject.getString("id"));
									category.setName(catJsonObject.getString("name"));

									model.setCategory(category);
								}

								Constants.jobsArray.add(model);
							}
						}

						if(responseObject.has("Data_users") && responseObject.get("Data_users") instanceof JSONArray)
						{
							JSONArray userArray = responseObject.getJSONArray("Data_users");
							int count = userArray.length();
							Constants.customerArray = new ArrayList<UserDetail>();
							Constants.courierArray = new ArrayList<UserDetail>();
							for (int i = 0; i < count; i++) 
							{
								JSONObject object = userArray.getJSONObject(i);
								if(object.has("User"))
									object = object.getJSONObject("User");
								if(object != null)
								{
									UserDetail user = new UserDetail();

									user.setId(object.getString("id"));
									user.setUsername(object.getString("username"));
									user.setAvg_rating(object.getString("avg_rating"));
									user.setUserType(object.getString("user_type"));
									user.setLatitude(object.getString("latitude"));  
									user.setLongitude(object.getString("longitude"));


									if(object.has("location"))
										user.setLocation(object.getString("location"));

									if(user.getUserType().equalsIgnoreCase("2"))
										Constants.customerArray.add(user); 
									else
										Constants.courierArray.add(user);
								}
							}

						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			if(updateListner != null)
				updateListner.updateEnd();
		}
	}
	private class Updater extends TimerTask
	{
		int mapCondition;
		
		public Updater(){
			this.mapCondition = Constants.MAP_MODE_MYJOBS;
		}
		public Updater(int condition){
			this.mapCondition = condition;
		}
		
		@Override
		public void run() {
			if(updateListner != null)
				updateListner.updateStart();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("latitude", Constants.lat));
			nameValuePairs.add(new BasicNameValuePair("longitude", Constants.lon));
			nameValuePairs.add(new BasicNameValuePair("location", Constants.locationAdd));
//			nameValuePairs.add(new BasicNameValuePair("distance",""+Constants.jobRadius));

			nameValuePairs.add(new BasicNameValuePair("distance","10000000"));
			
			switch (mapCondition) {
				case Constants.MAP_MODE_EVERYONE:
					nameValuePairs.add(new BasicNameValuePair("condition","all"));
					break;
				case Constants.MAP_MODE_MYJOBS:
					nameValuePairs.add(new BasicNameValuePair("condition","related"));
					break;
				case Constants.MAP_MODE_OPENJOBS:
					nameValuePairs.add(new BasicNameValuePair("condition","open"));
					break;
				default:
					break;
			}
			
			
			Log.d("Map Update Params=", nameValuePairs.toString());
			
			if(Constants.customerArray == null)
				Constants.customerArray = new ArrayList<UserDetail>();
			else
				Constants.customerArray.clear();
			
			if(Constants.courierArray == null)
				Constants.courierArray = new ArrayList<UserDetail>();
			else
				Constants.courierArray.clear();
			
			HttpPostConnector conn=new HttpPostConnector(Constants.API_UPDATE_MAPMARKER, nameValuePairs);
			String response=conn.getResponseData();		
			//Log.d("Map Update Response=", response);
			
			if(response != null && response.length() > 0)
			{
				try {
					
					JSONArray responseArray = new JSONArray(response);
					
					int count = responseArray.length();
					Log.d("Total Users : ",""+count);
					
					if(count > 0){
						
						for(int i = 0; i < count; i++){
							
							JSONObject object = responseArray.getJSONObject(i).getJSONObject("User");
							
							UserDetail user = new UserDetail();
							
							user.setId(object.getString("id"));
							user.setUsername(object.getString("username"));
							user.setKarmaCount(object.getString("karma_count"));
							
							user.setUserType(object.getString("user_type"));
							
							if(object.has("location"))
								user.setLocation(object.getString("location"));
							if(object.has("latitude"))
								user.setLatitude(object.getString("latitude"));
							if(object.has("longitude"))
								user.setLongitude(object.getString("longitude"));
							
							if(mapCondition == Constants.MAP_MODE_EVERYONE){
								Constants.courierArray.add(user);
							}else if(mapCondition == Constants.MAP_MODE_MYJOBS){
								
								if(user.getUserType().equalsIgnoreCase("2"))
									Constants.customerArray.add(user); 
								else
									Constants.courierArray.add(user);
								
							}else if(mapCondition == Constants.MAP_MODE_OPENJOBS){
								Constants.customerArray.add(user);
							}							
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			if(updateListner != null)
				updateListner.updateEnd();
		}
	}


	public void onDestroy() {
		if(updateTimer != null)
		{
			updateTimer.cancel();
			updateTimer.purge();
			updateTimer = null;
		}
		self = null;
	}

}