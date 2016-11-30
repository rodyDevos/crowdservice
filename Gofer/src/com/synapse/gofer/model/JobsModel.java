package com.synapse.gofer.model;

public class JobsModel {
	
    private String status = "fail";
	private String message = "";
	private String jobApplied = "";
	private String jobImage = "";
	private JobData[] jobData = null;
	private String[] checkBids = null;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getJobApplied() {
		return jobApplied;
	}
	public void setJobApplied(String jobApplied) {
		this.jobApplied = jobApplied;
	}
	public String getJobImage() {
		return jobImage;
	}
	public void setJobImage(String jobImage) {
		this.jobImage = jobImage;
	}
	public JobData[] getJobData() {
		return jobData;
	}
	public void setJobData(JobData[] jobData) {
		this.jobData = jobData;
	}
	public String[] getCheckBids() {
		return checkBids;
	}
	public void setCheckBids(String[] checkBids) {
		this.checkBids = checkBids;
	}
	
	
}
