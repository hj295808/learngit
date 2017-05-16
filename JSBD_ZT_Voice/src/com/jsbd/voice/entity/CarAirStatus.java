package com.jsbd.voice.entity;


import com.jsbd.voice.dimens.CarInfor;
import com.jsbd.voice.service.SerialService;

public class CarAirStatus {

	private int temperature;
	private int leftTemperature;//左温度
	private int rightTemperature;//右温度
	private int fan;//风
	private int fanSpeed;//风速
	private int fanModel;//风向
	private int acModeFlagData;
	
	//private int 
	/**
	 * @return the fanSpeed
	 */
	public int getFanSpeed() {
		return fanSpeed;
	}


	/**
	 * @return the fanModel
	 */
	public int getFanModel() {
		return fanModel;
	}


	
	public CarAirStatus() {
		// TODO Auto-generated constructor stub
	}
	/**
	 by suochao 2016年11月28日
	 */


	/**
	 * @return the temperature
	 */
	public int getTemperature() {
		return temperature;
	}


	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}


	/**
	 * @return the fanSpeed
	 */
	public int getFan() {
		return fan;
	}


	/**
	 * @param fanSpeed the fanSpeed to set
	 */
	public void setFan(int fan) {
		this.fan = fan;//右边四位 风速 0-7 0:不显示 8:风速自动，左边四位 风向
		this.fanSpeed = fan&0x0F;
		this.fanModel = fan>>4;
	}
	
	/**
	 * @return the leftTemperature
	 */
	public int getLeftTemperature() {
		return leftTemperature;
	}


	/**
	 * @param leftTemperature the leftTemperature to set
	 */
	public void setLeftTemperature(int leftTemperature) {
		this.leftTemperature = leftTemperature;
	}


	/**
	 * @return the rightTemperature
	 */
	public int getRightTemperature() {
		return rightTemperature;
	}


	/**
	 * @param rightTemperature the rightTemperature to set
	 */
	public void setRightTemperature(int rightTemperature) {
		this.rightTemperature = rightTemperature;
	}


	//向mcu 请求查询
	public static void  queryStatus(){
		SerialService.getInstance().sendData(3, (byte)0x06,(byte)0x00,(byte)0x56);
	}


	public void setAcModeFlagData(int acModeFlagData) {
		this.acModeFlagData = acModeFlagData;
	}
	

	public int getAcModeFlagData() {
		// TODO Auto-generated method stub
		return this.acModeFlagData;
	}
	//空调是否开启
	public boolean isAirConditionON() {
		return ((CarInfor.carAirStatus.getAcModeFlagData()&128) ==0);
	}

	public static void OpenAir(){
		int nnvalue = 0xFF;
		SerialService.getInstance().sendData(32, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x11, (byte) 0x14,
				(byte) 0x01, (byte) 1, (byte) nnvalue, (byte) 0x02,(byte) nnvalue,
				(byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue,
				(byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue,
				(byte) 50, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue,
				(byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue,
				(byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue);
	}
}
