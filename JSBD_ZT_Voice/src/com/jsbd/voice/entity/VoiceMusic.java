package com.jsbd.voice.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jsbd.voice.dimens.Constant;

import android.util.Log;

public class VoiceMusic extends VoiceEntity {


	private String rawText;
	private String operation;
	private String song;
	private String artist;
	private String album;
	private String category;
	private String source;
	
	public VoiceMusic(String strResult) {
			super(strResult);
		try {
			this.rawText = this.getJsonObject().optString("rawText", "");
			this.operation = this.getJsonObject().optString("operation","");
			this.song = this.getJsonObject().optString("song","");
			this.artist = this.getJsonObject().optString("artist","");
			this.album = this.getJsonObject().optString("album","");
			this.category = this.getJsonObject().optString("category","");
			this.source = this.getJsonObject().optString("source","");
			
//			this.setJsonObject(new JSONObject(strResult));
//			this.setRawText(this.getJsonObject().getString("rawText"));
//			this.setOperation(this.getJsonObject().getString("operation"));
//			this.setSong(this.getJsonObject().getString("song"));
//			this.setArtist(this.getJsonObject().getString("artist"));
//			this.setAlbum(this.getJsonObject().getString("album"));
//			this.setCategory(this.getJsonObject().getString("category"));
//			this.setSource(this.getJsonObject().getString("source"));
			this.getMusicString();
		} catch (Exception e) {
			//Log.d(Constant.DEBUG_TAG, "VoiceMusic ");
			e.printStackTrace();
		}
		
	}
	
	public String getMusicString(){
		String musicStr = "";
		musicStr = "rawText:"+this.rawText+",operation:"+this.operation+",song:"+this.song+",artist:"+this.artist+",album:"+this.album+",category:"+this.category+",source:"+this.source;
		//Log.d(Constant.DEBUG_TAG, musicStr);
		 return musicStr;
	}
	
	public String getRawText() {
		return rawText;
	}
	public void setRawText(String rawText) {
		this.rawText = rawText;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getSong() {
		return song;
	}
	public void setSong(String song) {
		this.song = song;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
}
