package com.jsbd.voice.entity;

import com.jsbd.voice.service.SerialService;

public class CarSeatStatus {

	private int seatHeat;//���μ��ȵȼ�
	private int seatAir;//����ͨ��ȼ�
	private int seatLeftHeat;//�����μ��ȵȼ�
	private int seatRightHeat;//�����μ��ȵȼ�	
	private int seatLeftAir;//������ͨ��ȼ�
	private int seatRightAir;//������ͨ��ȼ�
	
	public CarSeatStatus() {
		// TODO Auto-generated constructor stub
	}
	/**
	 by suochao 2016��11��28��
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
/*		BIT0~BIT3:�����Σ�����ʻλ������/����
		0����
		1����һ��
		2���ڶ���
		3��������
		BIT4��BIT7�������Σ�����ʻλ������/����
		0����
		1����һ��
		2���ڶ���
		3��������*/
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
		/*���Σ�����ʻλ��ͨ�磺
		BIT0~BIT3:������ͨ��
		0����
		1����һ��
		2���ڶ���
		3��������
		BIT4��BIT7�������Σ�����ʻλ��ͨ��
		0����
		1����һ��
		2���ڶ���
		3��������*/
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

	//��mcu �����ѯ
	public static void  queryStatus(){
		SerialService.getInstance().sendData(3, (byte)0x0D,(byte)0x00,(byte)0x2B);
	}
}
