package com.jsbd.voice.entity;

public class VoiceVehicleInfo extends VoiceEntity {

	private String rawText;//����תд������
	private String operation;	//ȡֵ����QUERY��
	private String name;// ��ѯ�ĳ������ƣ�ȡֵ����������|̥ѹ|������|�յ�|ˮ��|ɲ��|�ƶ�|���塿
	
	
	
	
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
