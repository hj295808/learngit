package com.jsbd.voice.entity;

public class VoiceCarControl extends VoiceEntity {

	private String rawText;//����תд������
	private String operation;//����ȡֵ����OPEN|CLOSE|STOP|START_RECORD|FINISH_RECORD|PHOTOGRAPH��
	private String name;//������ƶ���ȡֵ���촰|����|�����|Զ���|���|ǰ���|�����|ʾ����|��ʾ��|����|DVR��
	
	
	
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
