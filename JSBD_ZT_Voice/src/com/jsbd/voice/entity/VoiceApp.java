package com.jsbd.voice.entity;

import com.jsbd.voice.dimens.Constant;

import android.util.Log;

public class VoiceApp extends VoiceEntity {
	/**
	 by suochao 2016年7月26日
	 */
	

	private String rawText;//语音转写的文字
	private String operation;//取值：【LAUNCH|EXIT】
	private String name;//应用名称
	
	
	
	public VoiceApp(String strResult) {
		super(strResult);
		try{
			this.rawText = this.getJsonObject().optString("rawText", "");
			this.name = this.getJsonObject().optString("name","");
			this.operation = this.getJsonObject().optString("operation","");
			Log.d(Constant.DEBUG_TAG, "operation:"+operation);
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



	public String getOperation() {
		if(this.operation!=null){
			//默认打开应用，比如说 “计算器”该字段返回的是"",所以默认打开
			if(this.operation.equals("")){
				return "LAUNCH";
			}
			return operation;
		}else{
			return "";
		}
	}



	public void setOperation(String operation) {
		this.operation = operation;
	}



	public String getName() {
		if (this.name != null) {
			return name;
		} else {
			return "";
		}
	}



	public void setName(String name) {
		this.name = name;
	}

}
