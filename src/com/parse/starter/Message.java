package com.parse.starter;

import android.location.Location;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class Message {
	ParseObject thisMessage;
	ParseGeoPoint buzzLoc;
	String buzz;
	public Message(String text,Location loc){
		this.buzzLoc = new ParseGeoPoint(loc.getLatitude(),loc.getLongitude());
		this.buzz = text;
		thisMessage = new ParseObject("Message");
		thisMessage.put("buzz", this.buzz);
		thisMessage.put("buzzLoc", this.buzzLoc);
	}
	
	public void sendMessage(){
		thisMessage.saveInBackground();
	}
}