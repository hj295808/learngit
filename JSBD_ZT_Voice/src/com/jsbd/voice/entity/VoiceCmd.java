package com.jsbd.voice.entity;

public class VoiceCmd extends VoiceEntity {

	private String rawText;//语音转写的文字
	private String category;// 取值：【地图设置|播放模式|音量控制|曲目控制】等
	private String name;//取值：【视图切换|2D视图|3D视图|地图放大|地图缩小|导航路线|取消导航|收藏当前点 顺序循环|单曲循环|随机播放 音量+|音量-|静音|打开音量 暂停|播放|上一首|下一首|重播】等
	private String namaValue;//音量具体值
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
