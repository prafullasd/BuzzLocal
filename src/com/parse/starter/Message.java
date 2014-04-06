package com.parse.starter;

import java.util.Calendar;
import java.util.Date;

import android.location.Location;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class Message {
	private ParseObject thisMessage;
	private ParseGeoPoint buzzLoc;
	private String buzz;
	private Date time;
	private Calendar calendar;
	private int upvotes;
	private int downvotes;
	public Message(String text,Location loc, Date date){
		this.buzzLoc = new ParseGeoPoint(loc.getLatitude(),loc.getLongitude());
		this.buzz = text;
		this.time = date;
		this.calendar = Calendar.getInstance();
		this.calendar.setTime(date);
		this.upvotes = 0;
		this.downvotes = 0;
		thisMessage = new ParseObject("Message");
		thisMessage.put("buzz", this.buzz);
		thisMessage.put("buzzLoc", this.buzzLoc);
		thisMessage.put("buzzTime", this.time);
		thisMessage.put("upvotes", this.upvotes);
		thisMessage.put("downvotes", this.downvotes);
	}
	
	public void sendMessage(){
		thisMessage.saveInBackground();
	}
	
	public String getBuzz(){
		return this.buzz;
	}
}