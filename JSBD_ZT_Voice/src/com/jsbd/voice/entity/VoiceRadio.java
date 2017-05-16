package com.jsbd.voice.entity;

import android.util.Log;

import com.jsbd.voice.dimens.Constant;

public class VoiceRadio extends VoiceEntity {

	private String rawText;//语音转写的文字
	private String name;//电台名称
	private String code;//电台频点
	private String waveband;//waveband取值（fm,am）
	private String category;//电台类型
	private String location;//地区
	
	
	public VoiceRadio(String strResult) {
		super(strResult);
		try{
		/* Log.d(Constant.DEBUG_TAG, "VoiceRadio:"+ this.getJsonObject().toString());
			 Log.d(Constant.DEBUG_TAG, "this.rawText"+ this.getJsonObject().optString("rawText"));
			 Log.d(Constant.DEBUG_TAG, "this.name"+ this.getJsonObject().optString("name"));
			 Log.d(Constant.DEBUG_TAG, "this.code"+ this.getJsonObject().optString("code"));
			 Log.d(Constant.DEBUG_TAG, "this.waveband"+ this.getJsonObject().optString("waveband"));
			 Log.d(Constant.DEBUG_TAG, "this.category"+ this.getJsonObject().optString("category"));
			 Log.d(Constant.DEBUG_TAG, "this.location"+ this.getJsonObject().optString("location"));*/
			this.rawText = this.getJsonObject().optString("rawText", "");
			this.name = this.getJsonObject().optString("name", "");
			this.code = this.getJsonObject().optString("code", "");
			this.waveband = this.getJsonObject().optString("waveband", "");
			this.category = this.getJsonObject().optString("category", "");
			this.location = this.getJsonObject().optString("location", "");
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public String getRawText() {
		return rawText;
	}


	public void setRawText(String rawText) {
		this.rawText = rawText;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getWaveband() {
		return waveband;
	}


	public void setWaveband(String waveband) {
		this.waveband = waveband;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}

}
