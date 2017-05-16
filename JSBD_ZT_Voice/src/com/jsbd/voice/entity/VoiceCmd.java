package com.jsbd.voice.entity;

public class VoiceCmd extends VoiceEntity {

	private String rawText;//����תд������
	private String category;// ȡֵ������ͼ����|����ģʽ|��������|��Ŀ���ơ���
	private String name;//ȡֵ������ͼ�л�|2D��ͼ|3D��ͼ|��ͼ�Ŵ�|��ͼ��С|����·��|ȡ������|�ղص�ǰ�� ˳��ѭ��|����ѭ��|������� ����+|����-|����|������ ��ͣ|����|��һ��|��һ��|�ز�����
	private String namaValue;//��������ֵ
	private String code;
	
	public VoiceCmd(String strResult) {
		super(strResult);
		try{
			this.rawText = this.getJsonObject().optString("rawText", "");//getString("rawText");
			this.name = this.getJsonObject().optString("name", "");//getString("name");
			this.namaValue = this.getJsonObject().optString("nameValue", "");
			this.category = this.getJsonObject().optString("category", "");//getString("category");
			this.code = this.getJsonObject().optString("code", "");//getString("code");		
			
			}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the namaValue
	 */
	public String getNamaValue() {
		return namaValue;
	}

	/**
	 * @param namaValue the namaValue to set
	 */
	public void setNamaValue(String namaValue) {
		this.namaValue = namaValue;
	}

}
