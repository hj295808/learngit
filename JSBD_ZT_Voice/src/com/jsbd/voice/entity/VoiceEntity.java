package com.jsbd.voice.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class VoiceEntity {
	// 业务类型
	private String focus;
	private JSONObject jsonObject;
	
	public VoiceEntity(String strResult){
		try {
			JSONObject jsResult = new JSONObject(strResult);
			this.setJsonObject(jsResult);
			this.setFocus(this.jsonObject.getString("focus"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public String getFocus() {
		return focus;
	}

	public void setFocus(String focus) {
		if (focus != null) {
			this.focus = focus;
		} else {
			this.focus = "";
		}
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

}
