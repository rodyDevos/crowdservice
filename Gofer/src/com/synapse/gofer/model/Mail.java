package com.synapse.gofer.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.synapse.gofer.util.Constants;

public class Mail implements Serializable {
	private String jobId = "";
	private String status = "";
	private String jobName = "";
	private String toUserId = "";
	private String toUserName = "";
	private String fromUserId = "";
	private String fromUserName = "";
		private String message = "";
	private String photoUrl = "";
	private Date date = null;
	private String questionId = "";
	private String jobCreator = ""; 
	private String parentId = "";
	private ArrayList<Mail> answers = new ArrayList<Mail>();
		
	public Mail(){
		
	}
	public Mail(JSONObject jObj){
		parseJSONData(jObj);
	}
	
	private void parseJSONData(JSONObject jObj){
		try {
			JSONObject jPushData = jObj.getJSONObject("Pushnotification");
			JSONObject jJobData = jObj.getJSONObject("job");
			
			this.status = jPushData.getString("status");
			this.jobId = jPushData.getString("job_id");
			this.jobName = jJobData.getString("name");
			this.jobCreator = jJobData.getString("user_id");
			this.message = jPushData.getString("message");
			this.setDate(Constants.serverFormate.parse(jPushData.getString("date")) );
			this.setQuestionId(jPushData.getString("question_id"));
			this.setParentId(jPushData.getString("parent_id"));
			
			if(Integer.parseInt(status) != 3){
				JSONObject jFromUser = jObj.getJSONObject("from_user");
				JSONObject jToUser = jObj.getJSONObject("to_user");
				
				this.setFromUserName(jFromUser.getString("user_name"));
				this.setFromUserId(jFromUser.getString("id"));
				this.setToUserName(jToUser.getString("user_name"));
				this.setToUserId(jToUser.getString("id"));
				
				this.setPhotoUrl(jFromUser.getString("photoUrl"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getJobCreator() {
		return jobCreator;
	}

	public void setJobCreator(String jobCreator) {
		this.jobCreator = jobCreator;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public ArrayList<Mail> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<Mail> answers) {
		if(answers != null)
			this.answers = answers;
		else
			this.answers = new ArrayList<Mail>();
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	
	
}
