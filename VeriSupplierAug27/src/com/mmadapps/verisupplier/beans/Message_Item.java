package com.mmadapps.verisupplier.beans;

public class Message_Item {
	
	int user_image;
	String person_name;
	String company_name;
	String subject;
	String date;
	String time;
	int reply_status;
	int star_status;
	int msg_status;
	private String text;
    private int type;
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Message_Item(int user_image, String person_name,
			String company_name, String subject, String date, String time,
			int reply_status, int star_status, int msg_status,String text,int type) {
		super();
		this.user_image = user_image;
		this.person_name = person_name;
		this.company_name = company_name;
		this.subject = subject;
		this.date = date;
		this.time = time;
		this.reply_status = reply_status;
		this.star_status = star_status;
		this.msg_status = msg_status;
		this.text = text;
		this.type = type;
	}
	public int getUser_image() {
		return user_image;
	}
	public void setUser_image(int user_image) {
		this.user_image = user_image;
	}
	public String getPerson_name() {
		return person_name;
	}
	public void setPerson_name(String person_name) {
		this.person_name = person_name;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getReply_status() {
		return reply_status;
	}
	public void setReply_status(int reply_status) {
		this.reply_status = reply_status;
	}
	public int getStar_status() {
		return star_status;
	}
	public void setStar_status(int star_status) {
		this.star_status = star_status;
	}
	public int getMsg_status() {
		return msg_status;
	}
	public void setMsg_status(int msg_status) {
		this.msg_status = msg_status;
	}
	
	
	

}
