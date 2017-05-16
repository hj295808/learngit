package com.iflytek.platformadapter;


/*
 * 定义解析语义后执行的方法接口
 */
public interface VoiceServiceFunctionInterface {
	//public void turnOnRadio();
	/*以下的返回值为String 是否支持处理此功能，为jsonobject格式（{"status":"","message":""}）。
	status 处理状态(success|fail) 是 仅为：success|fail 
	message 错误情况下，需要助理的提示。 否 当 status为fail的情况下，需要语音助理显示和播报的提示 
	例如：能够处理。 
	{"status":"success"} 不能够处理的功能 {"status":"fail","message":"抱歉，不能为您处理***"}*/
	public String handleRadio(String recognizerResult);//处理收音机
	public String handleCmd(String recognizerResult);//处理命令
	public String handleMusic(String recognizerResult);//处理音乐
	public String handleBluetoothPhone(String recognizerResult);//
	public String handleApp(String recognizerResult);//处理app打开关闭
	public String handleAirControl(String recognizerResult);//处理空调
	public String handleMessage(String recognizerResult);//处理短信
	public String handleDoAction(String recognizerResult);//电话
	public String handleCarControl(String recognizerResult);//处理车辆控制
	public String handleVehicleInfo(String recognizerResult);//车辆信息
}
