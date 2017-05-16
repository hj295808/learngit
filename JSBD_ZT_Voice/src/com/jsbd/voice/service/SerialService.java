package com.jsbd.voice.service;

import com.iflytek.platform.type.PlatformCode;
import com.iflytek.platformadapter.VoiceCommon;
import com.jsbd.voice.dimens.ACTIONS;
import com.jsbd.voice.dimens.CarInfor;
import com.jsbd.voice.dimens.Constant;
import com.jsbd.voice.dimens.SceneStatus;
import com.jsbd.voice.entity.CarAirStatus;
import com.jsbd.voice.entity.CarSeatStatus;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class SerialService extends Service{

	//TtsCallback mTtsCallback;
	SerialDataReceiver mSerialDataReceiver = null;
	VoiceDialReceiver mVoiceDReceiver = null;
	static int excResult = 0;
	public static boolean isDoMcuUpdateMsg = false;//�Ƿ����һ��mcu�����˳������Ĳ���
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String obj = "obj";
	static SerialService mm = null;
	public void onCreate(){
		super.onCreate();
		mm = this;
		Log.e(Constant.DEBUG_TAG, "���ڷ�������");
		//mTtsCallback = TtsService.getCallback();
		mSerialDataReceiver = new SerialDataReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTIONS.MSG_MCU_TO_AP);
		filter.addAction(ACTIONS.MSG_AP_TO_MCU);
		registerReceiver(mSerialDataReceiver, filter);
			//ע��㲥���绰�������ŵ���
		mVoiceDReceiver  = new VoiceDialReceiver();
		  IntentFilter filterd=new IntentFilter();  
        filterd.addAction(ACTIONS.SR_VOICEDIALING_ACTION);  
        //��̬ע��BroadcastReceiver  
        registerReceiver(mVoiceDReceiver, filterd);  
        //��ʼ����Ҫ�Ȳ�ѯ�£��յ������μ���״̬�������Ϣ������������
        this.QueryCarinfor();
        
        
	}

	//��mcu ��ѯ���������Ϣ
	private void QueryCarinfor() {
		//��ѯ�յ�״̬
		CarAirStatus.queryStatus();
		//��ѯ���μ���
		CarSeatStatus.queryStatus();
	}

	//
	public static SerialService getInstance(){
		return mm;
	}
	
	public static int getExcResult(){
		return excResult;
	}
	
	//�ּ�MCU�����������
	public void onSerialData(byte[] data){
		//Log.e(Constant.DEBUG_TAG, "mcu data[3]"+data[3]);
		if(data[3] == 0x12){
			switch(data[5]){
			case 0x01://����ʶ��״̬����
				//Log.e(Constant.DEBUG_TAG, "����ʶ��״̬");
				voiceResult(data[6], data[7]);
				break;
			case 0x07://ֹͣ����
				break;
			case 0x08://��������
				String dd = "";
/*				for(int i=0;i<data.length;i++){
					dd+=data[i]+",";
				}*/
				
				Log.e(Constant.DEBUG_TAG, "voice key....:"+dd);
				QueryCarinfor();
				voiceKey();
				break;
			}
		}else if(data[3] == 0x06){
			if(data[5] == 0x56){
				if(CarInfor.carAirStatus!=null){
				
					//CarInfor.carAirStatus.setFanSpeed(fanSpeed);
					//CarInfor.carAirStatus.getTemperature(fanSpeed);
					int fanspeed  = data[7];//�ұ���λ ���� 0-7 0:����ʾ 8:�����Զ��������λ ����
					
					//int temperature =  data[12];
					int lefttemp =  data[8];//���¶�
					int righttemp =  data[9];//���¶�
					int intemp = data[12];//�����¶�
					Log.e("srcanair", "mcu to voice airstatu:fanspeed,lefttemp,righttemp,intemp:"+Integer.toBinaryString(fanspeed)+","
							+lefttemp+","
							+righttemp+","+intemp);
					CarInfor.carAirStatus.setFan(fanspeed);
					CarInfor.carAirStatus.setAcModeFlagData(data[6]&0xFF);
					Log.d("vair", data[6]+";");
					if(Constant.projecId==Constant.PROJECT_ID.ZT_B11A_C){
						//����Ŀȡ�ĳ����¶�
						CarInfor.carAirStatus.setLeftTemperature(intemp);
						CarInfor.carAirStatus.setRightTemperature(intemp);
						
			    	}else 	if(Constant.projecId==Constant.PROJECT_ID.ZT_B11B){
						CarInfor.carAirStatus.setLeftTemperature(lefttemp);
						CarInfor.carAirStatus.setRightTemperature(righttemp);
						
			    	}
				}
			}
		}else if(data[3] == 0x0D){
			if(data[5] == 0x2B){
				//�������μ��ȣ�
	/*			BIT0~BIT3:�����Σ�����ʻλ������/����
				0����
				1����һ��
				2���ڶ���
				3��������
				BIT4��BIT7�������Σ�����ʻλ������/����
				0����
				1����һ��
				2���ڶ���
				3��������
								
				*���Σ�����ʻλ��ͨ�磺
				BIT0~BIT3:������ͨ��
				0����
				1����һ��
				2���ڶ���
				3��������
				BIT4��BIT7�������Σ�����ʻλ��ͨ��
				0����
				1����һ��
				2���ڶ���
				3��������
				*/

				int seathot = data[7];//���μ���
				int seatair = data[8];//����ͨ��
				Log.e("srcanair", "mcu to voice seatstatu:seathot,seatair:"+Integer.toBinaryString(seathot)+","+Integer.toBinaryString(seatair));
				CarInfor.carSeatStatus.setSeatHeat(seathot);
				CarInfor.carSeatStatus.setSeatAir(seatair);
			}
		}
		
	/*	else if(data[3] == 0x0E){
			if(Constant.projecId==Constant.PROJECT_ID.ZT_B11B){
				if(!isDoMcuUpdateMsg){
					Log.d("vreceiver", "muc update");
					//mcu ����ʱ����ʹ������
					VoiceCommon.systemStateChange(PlatformCode.STATE_SPEECHOFF);
					isDoMcuUpdateMsg = true;
				}
			}
		}*/
		
	}
	
	//֪ͨMCU��ʼ����ʶ��
	public void voiceStart(){
		sendData(4, (byte)0x12, (byte)0x00, (byte)0x01, (byte)0x01);
	}
	
	//֪ͨMCU����ʶ�����
	public void voiceStop(){
		sendData(4, (byte)0x12, (byte)0x00, (byte)0x01, (byte)0x00);
	}
	
	//����ʶ��״̬
	public void voiceResult(byte param1, byte param2){
		Log.e(Constant.DEBUG_TAG, "MCU ���ؽ������: "+ param1);
		excResult = param1;
		switch(param1){
		case 0x00://ʶ����ֹ
			//voiceStop();
			break;
		case 0x01://ʶ������
			break;
		case 0x02://TTS��ʾ��ʼ
			break;
		case 0x03://TTS��ʾ����
			break;
		case 0x04://¼����
			break;
		case 0x05://¼����ʱ
			break;
		case 0x06://������ʼ
			break;
		case 0x07://��������
			break;
		case 0x08://ʶ��ʼ
			break;
		case 0x09://ʶ�����
			break;
		case 0x10://ʶ��ʧ��
//			synchronized (getApplicationContext()) {
//				getApplicationContext().notifyAll();
//			}
			break;
		case 0x11://ִ�гɹ�
			Log.e(Constant.DEBUG_TAG, "MCUִ�гɹ�");
			//voiceStop();
//			mTtsCallback.speakStart("hello,ִ�гɹ�");
//			synchronized (obj) {
//				obj.notifyAll();
//			}
			break;
		case 0x12://ִ��ʧ��
			Log.e(Constant.DEBUG_TAG, "MCUִ��ʧ��");
			
//			mTtsCallback.speakStart("hello,ִ��ʧ��");
//			synchronized (SerialService.obj) {
//				obj.notifyAll();
//			}
//			voiceStop();
			break;
		case 0x13://��Ҫ�ٴ�ʶ����Ҫ�ٴν�����
//			callVoiceAssistant();
			break;
		case 0x14://����ִ��
			Log.e(Constant.DEBUG_TAG, "MCU����Ҫִ��");
//			mTtsCallback.speakStart("hello,����Ҫִ��");
//			synchronized (obj) {
//				obj.notifyAll();
//			}
//			voiceStop();
			break;
		case 0x15://ִ����
			break;
		case 0x16://ִ��
			break;
		case 0x17://ȷ��
			break;
		case 0x18://�ȴ��ٴ�ʶ�𣨵ȴ��ٴν�����
			break;
		case 0x19://����ʶ����Ҫ�ٴν�����
			break;
		}
	}
	
	//����������������
	public void voiceKey(){
		//�жϵ�ǰ�Ƿ��������ʶ��
		int cansr = SceneStatus.canSR(this);
		Log.d(Constant.DEBUG_TAG, "voiceKey to start voice... is can,"+cansr);
		if(cansr==Constant.SR_AWAKE){
		/*	if(Constant.projecId==Constant.PROJECT_ID.ZT_B11B){
				//��������
				if(!SceneStatus.VR_IS_STATUS){
					int res = VoiceCommon.systemStateChange(PlatformCode.STATE_SPEECHON);
					Log.d(Constant.DEBUG_TAG, "voiceKey protect start has start voice"+res);
				}
			}*/
			//���Ի���
			 callVoiceAssistant();
		}
		//voiceStart();
	}
	
	
	

	// ����������ʶ�����
	public void callVoiceAssistant() {
		try {
			Log.e(Constant.DEBUG_TAG, "���ѵ���");
    		Intent intent = new Intent();
        	intent.setComponent(new ComponentName("com.iflytek.cutefly.speechclient",
                    "com.iflytek.autofly.SpeechClientService"));
        	 /*��������İ���������*/
            intent.putExtra("fromservice", "");
        	this.startService(intent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	//App��MCU������Ϣ��lenΪ����Э���еĳ��ȣ�arg���ֲ���Ҫ����0xFF 0x66 ���ȼ�У���
	public void sendData(int len, byte... arg){
		byte[] data = new byte[len+4];
		data[0] = (byte)0xff;
		data[1] = 0x66;
		data[2] = (byte)len;
		for(int i = 0; i < len; i++){
			data[i+3] = arg[i];
		}
		data[len+3] = getCmdSum(data);
		sendMcuData(data);
	}
	
	//����ͷ��У��͵������������ݷ��͸�MCU
	public void sendMcuData(byte[] data){
		Intent intent = new Intent("com.jsbd.serial.apptomcu");
		intent.putExtra("DataLen", data.length);
		intent.putExtra("Data", data);
		Log.e(Constant.DEBUG_TAG, "����MCU����");
		String datas = "McuData = ";
		for(int i = 0; i < data.length; i++){
			datas = datas+" "+data[i];
		}
		Log.e(Constant.DEBUG_TAG, datas);
		sendBroadcast(intent);
	}
	
	//���㴮�����ݵ�У���
	public byte getCmdSum(byte[] data)
	{
		int bLen = data[2];
		byte bCheckSum = 0;
		for(int i=0; i<=bLen; i++)
		{
			bCheckSum += data[2+i];
		}

		bCheckSum = (byte) (~bCheckSum+1);
		Log.e("sss", "У��� = "+bCheckSum);
		return bCheckSum;
	}
	
	//����MCU���͵���Ϣ
	public class SerialDataReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(action.equals(ACTIONS.MSG_MCU_TO_AP)){
				byte[] data = intent.getByteArrayExtra("Data");
				//Log.e(Constant.DEBUG_TAG, "��ʼ����MCU����ִ�н��:"+data.toString());
				onSerialData(data);
			}
		}
		
	}
	
	//�����������ŵ��õ���Ϣ
	public class VoiceDialReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(action.equals(ACTIONS.SR_VOICEDIALING_ACTION)){
				//��������
				voiceKey();
			}
		}
		
	}
}
