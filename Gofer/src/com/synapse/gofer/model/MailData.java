package com.synapse.gofer.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MailData implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String jobId = "";
	private String status = "";
	private Date date = null;
	
	private ArrayList<Mail> mails = new ArrayList<Mail>();
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
	public ArrayList<Mail> getMails() {
		return mails;
	}
	public void setMails(ArrayList<Mail> mails) {
		this.mails = mails;
	}
	
	public void addMail(Mail mail){
		mails.add(mail);
		date = mail.getDate();
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
