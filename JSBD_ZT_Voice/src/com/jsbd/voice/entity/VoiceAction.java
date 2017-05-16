/**
 * 
 */
package com.jsbd.voice.entity;


public class VoiceAction {
	/*call:请求拨打电话 。 (param1此时保存的是电话号码)
        sendsms：请求发送短信。(param1此时保存的是电话号码，param2此时保存的是短信内容)
        startspeechrecord：语音助理告知系统，这个时候自身开始识别功能录音。（若使用讯飞降噪模块硬件，这个时候可以通知模块切换到降噪功能）
        stopspeechrecord：语音助理告知系统，这个时候自身结束识别功能录音。
        startwakerecord：语音助理告知系统，这个时候自身开始唤醒功能录音。（若使用讯飞降噪模块硬件，这个时候可以通知模块切换到唤醒功能）
        stopwakerecord：语音助理告知系统，这个时候自身结束唤醒功能录音。  */
	private String action;//业务标签： 取值：【call|sendsms|startspeechrecord|stopspeechrecord|startwakerecord|stopwakerecord】
	
	private String param1;//参数1，根据action类型取值：action=call, 参数表示电话打给号码action=call,参数表示短信发送给号码
	private String param2;//参数2，根据action类型取值：action=sendsms,参数表示短信内容
	
	
	public VoiceAction(String strResult){
		
	}
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the param1
	 */
	public String getParam1() {
		if (this.param1 != null) {
			return param1;
		} else {
			return "";
		}
	}
	/**
	 * @param param1 the param1 to set
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	/**
	 * @return the param2
	 */
	public String getParam2() {
		if (this.param2!= null) {
			return param2;
		} else {
			return "";
		}
	}
	/**
	 * @param param2 the param2 to set
	 */
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	
	
}
