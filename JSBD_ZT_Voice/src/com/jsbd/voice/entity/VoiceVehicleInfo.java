package com.jsbd.voice.entity;

public class VoiceVehicleInfo extends VoiceEntity {

	private String rawText;//语音转写的文字
	private String operation;	//取值：【QUERY】
	private String name;// 查询的车身名称，取值包括【油量|胎压|发动机|空调|水温|刹车|制动|整体】
	
	
	
	
	public VoiceVehicleInfo(String strResult) {
		super(strResult);
		try{
			this.rawText = this.getJsonObject().optString("rawText","");
			this.name = this.getJsonObject().optString("name","");
			this.operation = this.getJsonObject().optString("operation","");
		}catch(Exception e){
			e.printStackTrace();
		}
	}




	/**
	 * @return the rawText
	 */
	public String getRawText() {
		return rawText;
	}




	/**
	 * @param rawText the rawText to set
	 */
	public void setRawText(String rawText) {
		this.rawText = rawText;
	}




	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}




	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}




	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}




	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
