package com.synapse.gofer.model;

import java.io.Serializable;

public class BiddingDetail implements Serializable {
	private String count = "";
	private String linkStatus = "";
	private String id = "";
	private String jobId = "";
	private String date = "";
	private String amount = "";
	private String distance = "";
	private String jobPostedById = "";

	public String getJobPostedById() {
		return jobPostedById;
	}

	public void setJobPostedById(String jobPostedById) {
		this.jobPostedById = jobPostedById;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(String linkStatus) {
		this.linkStatus = linkStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
