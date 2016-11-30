package com.synapse.gofer.model;

public class ProfileJobData {

	private String status = "fail";
	private String message = "";
	private String jobid = "";
	private String jobname = "";
	private String jobcategory = "";
	private String jobrating = "";
	private String jobfeedback = "";

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

	public String getJobId() {
		return jobid;
	}

	public void setJobId(String jobid) {
		this.jobid = jobid;
	}

	public String getJobName() {
		return jobname;
	}

	public void setJobName(String jobname) {
		this.jobname = jobname;
	}

	public String getJobCategory() {
		return jobcategory;
	}

	public void setJobCategory(String jobcategory) {
		this.jobcategory = jobcategory;
	}

	public String getJobRating() {
		return jobrating;
	}

	public void setJobRating(String jobrating) {
		this.jobrating = jobrating;
	}

	public String getJobFeedback() {
		return jobfeedback;
	}

	public void setJobFeedback(String jobfeedback) {
		this.jobfeedback = jobfeedback;
	}
}
