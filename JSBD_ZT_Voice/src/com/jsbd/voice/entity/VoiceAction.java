/**
 * 
 */
package com.jsbd.voice.entity;


public class VoiceAction {
	/*call:���󲦴�绰 �� (param1��ʱ������ǵ绰����)
        sendsms�������Ͷ��š�(param1��ʱ������ǵ绰���룬param2��ʱ������Ƕ�������)
        startspeechrecord�����������֪ϵͳ�����ʱ������ʼʶ����¼��������ʹ��Ѷ�ɽ���ģ��Ӳ�������ʱ�����֪ͨģ���л������빦�ܣ�
        stopspeechrecord�����������֪ϵͳ�����ʱ���������ʶ����¼����
        startwakerecord�����������֪ϵͳ�����ʱ������ʼ���ѹ���¼��������ʹ��Ѷ�ɽ���ģ��Ӳ�������ʱ�����֪ͨģ���л������ѹ��ܣ�
        stopwakerecord�����������֪ϵͳ�����ʱ������������ѹ���¼����  */
	private String action;//ҵ���ǩ�� ȡֵ����call|sendsms|startspeechrecord|stopspeechrecord|startwakerecord|stopwakerecord��
	
	private String param1;//����1������action����ȡֵ��action=call, ������ʾ�绰�������action=call,������ʾ���ŷ��͸�����
	private String param2;//����2������action����ȡֵ��action=sendsms,������ʾ��������
	
	
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
