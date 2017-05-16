package com.jsbd.voice.entity;

import com.jsbd.voice.service.SerialService;

public class CarSeatStatus {

	private int seatHeat;//座椅加热等级
	private int seatAir;//座椅通风等级
	private int seatLeftHeat;//左座椅加热等级
	private int seatRightHeat;//右座椅加热等级	
	private int seatLeftAir;//左座椅通风等级
	private int seatRightAir;//右座椅通风等级
	
	public CarSeatStatus() {
		// TODO Auto-generated constructor stub
	}
	/**
	 by suochao 2016年11月28日
	 */

	/**
	 * @return the seatHeatLevel
	 */
	public int getSeatHeat() {
		return seatHeat;
	}

	/**
	 * @param seatHeatLevel the seatHeatLevel to set
	 */
	public void setSeatHeat(int seatHeat) {
/*		BIT0~BIT3:左座椅（主驾驶位）加热/制冷
		0：关
		1：第一档
		2：第二档
		3：第三档
		BIT4～BIT7：右座椅（副驾驶位）加热/制冷
		0：关
		1：第一档
		2：第二档
		3：第三档*/
		this.seatHeat = seatHeat;
		this.seatLeftHeat = seatHeat&0x0F;
		this.seatRightHeat = seatHeat>>4;
	}

	/**
	 * @return the seatAirLevel
	 */
	public int getSeatAir() {
		return seatAir;
	}

	/**
	 * @param seatAirLevel the seatAirLevel to set
	 */
	public void setSeatAir(int seatAir) {
		/*座椅（主驾驶位）通风：
		BIT0~BIT3:左座椅通风
		0：关
		1：第一档
		2：第二档
		3：第三档
		BIT4～BIT7：右座椅（副驾驶位）通风
		0：关
		1：第一档
		2：第二档
		3：第三档*/
		this.seatAir = seatAir;
		this.seatLeftAir = seatAir&0x0F;
		this.seatRightAir = seatAir>>4;
	}
	
	/**
	 * @return the seatLeftHeat
	 */
	public int getSeatLeftHeat() {
		return seatLeftHeat;
	}

	/**
	 * @param seatLeftHeat the seatLeftHeat to set
	 */
	public void setSeatLeftHeat(int seatLeftHeat) {
		this.seatLeftHeat = seatLeftHeat;
	}

	/**
	 * @return the seatRightHeat
	 */
	public int getSeatRightHeat() {
		return seatRightHeat;
	}

	/**
	 * @param seatRightHeat the seatRightHeat to set
	 */
	public void setSeatRightHeat(int seatRightHeat) {
		this.seatRightHeat = seatRightHeat;
	}

	/**
	 * @return the seatLeftAir
	 */
	public int getSeatLeftAir() {
		return seatLeftAir;
	}

	/**
	 * @param seatLeftAir the seatLeftAir to set
	 */
	public void setSeatLeftAir(int seatLeftAir) {
		this.seatLeftAir = seatLeftAir;
	}

	/**
	 * @return the seatRightAir
	 */
	public int getSeatRightAir() {
		return seatRightAir;
	}

	/**
	 * @param seatRightAir the seatRightAir to set
	 */
	public void setSeatRightAir(int seatRightAir) {
		this.seatRightAir = seatRightAir;
	}

	//向mcu 请求查询
	public static void  queryStatus(){
		SerialService.getInstance().sendData(3, (byte)0x0D,(byte)0x00,(byte)0x2B);
	}
}
