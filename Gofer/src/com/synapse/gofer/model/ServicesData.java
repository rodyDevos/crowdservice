package com.synapse.gofer.model;

public class ServicesData {
	
    private String status = "fail";
	private String message = "";
	private String jobid = "";
	private String jobname = "";
	
	
	
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
	
	
}
