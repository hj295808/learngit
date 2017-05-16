package com.jsbd.voice.entity;

import com.jsbd.voice.dimens.Constant;

import android.util.Log;

public class VoiceApp extends VoiceEntity {
	/**
	 by suochao 2016��7��26��
	 */
	

	private String rawText;//����תд������
	private String operation;//ȡֵ����LAUNCH|EXIT��
	private String name;//Ӧ������
	
	
	
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
			//Ĭ�ϴ�Ӧ�ã�����˵ �������������ֶη��ص���"",����Ĭ�ϴ�
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
