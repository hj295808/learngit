package com.jsbd.voice.entity;

public class VoiceCarControl extends VoiceEntity {

	private String rawText;//语音转写的文字
	private String operation;//操作取值：【OPEN|CLOSE|STOP|START_RECORD|FINISH_RECORD|PHOTOGRAPH】
	private String name;//车身控制对象，取值【天窗|后备箱|近光灯|远光灯|雾灯|前雾灯|后雾灯|示廓灯|警示灯|车窗|DVR】
	
	
	
	public VoiceCarControl(String strResult) {
		super(strResult);
		try{
			this.rawText = this.getJsonObject().getString("rawText");
			this.name = this.getJsonObject().getString("name");
			this.operation = this.getJsonObject().getString("operation");
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
		return operation;
	}



	public void setOperation(String operation) {
		this.operation = operation;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

}
