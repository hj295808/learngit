package com.iflytek.platformadapter;


/*
 * ������������ִ�еķ����ӿ�
 */
public interface VoiceServiceFunctionInterface {
	//public void turnOnRadio();
	/*���µķ���ֵΪString �Ƿ�֧�ִ���˹��ܣ�Ϊjsonobject��ʽ��{"status":"","message":""}����
	status ����״̬(success|fail) �� ��Ϊ��success|fail 
	message ��������£���Ҫ�������ʾ�� �� �� statusΪfail������£���Ҫ����������ʾ�Ͳ�������ʾ 
	���磺�ܹ����� 
	{"status":"success"} ���ܹ�����Ĺ��� {"status":"fail","message":"��Ǹ������Ϊ������***"}*/
	public String handleRadio(String recognizerResult);//����������
	public String handleCmd(String recognizerResult);//��������
	public String handleMusic(String recognizerResult);//��������
	public String handleBluetoothPhone(String recognizerResult);//
	public String handleApp(String recognizerResult);//����app�򿪹ر�
	public String handleAirControl(String recognizerResult);//����յ�
	public String handleMessage(String recognizerResult);//�������
	public String handleDoAction(String recognizerResult);//�绰
	public String handleCarControl(String recognizerResult);//����������
	public String handleVehicleInfo(String recognizerResult);//������Ϣ
}
