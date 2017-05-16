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
	public static boolean isDoMcuUpdateMsg = false;//是否处理过一次mcu升级退出语音的操作
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
		Log.e(Constant.DEBUG_TAG, "串口服务启动");
		//mTtsCallback = TtsService.getCallback();
		mSerialDataReceiver = new SerialDataReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTIONS.MSG_MCU_TO_AP);
		filter.addAction(ACTIONS.MSG_AP_TO_MCU);
		registerReceiver(mSerialDataReceiver, filter);
			//注册广播，电话语音拨号调用
		mVoiceDReceiver  = new VoiceDialReceiver();
		  IntentFilter filterd=new IntentFilter();  
        filterd.addAction(ACTIONS.SR_VOICEDIALING_ACTION);  
        //动态注册BroadcastReceiver  
        registerReceiver(mVoiceDReceiver, filterd);  
        //初始化，要先查询下，空调，座椅加热状态，相关信息，并保存下来
        this.QueryCarinfor();
        
        
	}

	//向mcu 查询车辆相关信息
	private void QueryCarinfor() {
		//查询空调状态
		CarAirStatus.queryStatus();
		//查询座椅加热
		CarSeatStatus.queryStatus();
	}

	//
	public static SerialService getInstance(){
		return mm;
	}
	
	public static int getExcResult(){
		return excResult;
	}
	
	//分检MCU命令操作类型
	public void onSerialData(byte[] data){
		//Log.e(Constant.DEBUG_TAG, "mcu data[3]"+data[3]);
		if(data[3] == 0x12){
			switch(data[5]){
			case 0x01://语音识别状态命令
				//Log.e(Constant.DEBUG_TAG, "语音识别状态");
				voiceResult(data[6], data[7]);
				break;
			case 0x07://停止语音
				break;
			case 0x08://语音按键
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
					int fanspeed  = data[7];//右边四位 风速 0-7 0:不显示 8:风速自动，左边四位 风向
					
					//int temperature =  data[12];
					int lefttemp =  data[8];//左温度
					int righttemp =  data[9];//右温度
					int intemp = data[12];//车内温度
					Log.e("srcanair", "mcu to voice airstatu:fanspeed,lefttemp,righttemp,intemp:"+Integer.toBinaryString(fanspeed)+","
							+lefttemp+","
							+righttemp+","+intemp);
					CarInfor.carAirStatus.setFan(fanspeed);
					CarInfor.carAirStatus.setAcModeFlagData(data[6]&0xFF);
					Log.d("vair", data[6]+";");
					if(Constant.projecId==Constant.PROJECT_ID.ZT_B11A_C){
						//该项目取的车内温度
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
				//座椅座椅加热：
	/*			BIT0~BIT3:左座椅（主驾驶位）加热/制冷
				0：关
				1：第一档
				2：第二档
				3：第三档
				BIT4～BIT7：右座椅（副驾驶位）加热/制冷
				0：关
				1：第一档
				2：第二档
				3：第三档
								
				*座椅（主驾驶位）通风：
				BIT0~BIT3:左座椅通风
				0：关
				1：第一档
				2：第二档
				3：第三档
				BIT4～BIT7：右座椅（副驾驶位）通风
				0：关
				1：第一档
				2：第二档
				3：第三档
				*/

				int seathot = data[7];//座椅加热
				int seatair = data[8];//座椅通风
				Log.e("srcanair", "mcu to voice seatstatu:seathot,seatair:"+Integer.toBinaryString(seathot)+","+Integer.toBinaryString(seatair));
				CarInfor.carSeatStatus.setSeatHeat(seathot);
				CarInfor.carSeatStatus.setSeatAir(seatair);
			}
		}
		
	/*	else if(data[3] == 0x0E){
			if(Constant.projecId==Constant.PROJECT_ID.ZT_B11B){
				if(!isDoMcuUpdateMsg){
					Log.d("vreceiver", "muc update");
					//mcu 升级时不能使用语音
					VoiceCommon.systemStateChange(PlatformCode.STATE_SPEECHOFF);
					isDoMcuUpdateMsg = true;
				}
			}
		}*/
		
	}
	
	//通知MCU开始语音识别
	public void voiceStart(){
		sendData(4, (byte)0x12, (byte)0x00, (byte)0x01, (byte)0x01);
	}
	
	//通知MCU语音识别结束
	public void voiceStop(){
		sendData(4, (byte)0x12, (byte)0x00, (byte)0x01, (byte)0x00);
	}
	
	//语音识别状态
	public void voiceResult(byte param1, byte param2){
		Log.e(Constant.DEBUG_TAG, "MCU 返回结果代码: "+ param1);
		excResult = param1;
		switch(param1){
		case 0x00://识别终止
			//voiceStop();
			break;
		case 0x01://识别启动
			break;
		case 0x02://TTS提示开始
			break;
		case 0x03://TTS提示结束
			break;
		case 0x04://录音中
			break;
		case 0x05://录音超时
			break;
		case 0x06://语音开始
			break;
		case 0x07://语音结束
			break;
		case 0x08://识别开始
			break;
		case 0x09://识别结束
			break;
		case 0x10://识别失败
//			synchronized (getApplicationContext()) {
//				getApplicationContext().notifyAll();
//			}
			break;
		case 0x11://执行成功
			Log.e(Constant.DEBUG_TAG, "MCU执行成功");
			//voiceStop();
//			mTtsCallback.speakStart("hello,执行成功");
//			synchronized (obj) {
//				obj.notifyAll();
//			}
			break;
		case 0x12://执行失败
			Log.e(Constant.DEBUG_TAG, "MCU执行失败");
			
//			mTtsCallback.speakStart("hello,执行失败");
//			synchronized (SerialService.obj) {
//				obj.notifyAll();
//			}
//			voiceStop();
			break;
		case 0x13://需要再次识别（需要再次交互）
//			callVoiceAssistant();
			break;
		case 0x14://不需执行
			Log.e(Constant.DEBUG_TAG, "MCU不需要执行");
//			mTtsCallback.speakStart("hello,不需要执行");
//			synchronized (obj) {
//				obj.notifyAll();
//			}
//			voiceStop();
			break;
		case 0x15://执行中
			break;
		case 0x16://执行
			break;
		case 0x17://确认
			break;
		case 0x18://等待再次识别（等待再次交互）
			break;
		case 0x19://立即识别（需要再次交互）
			break;
		}
	}
	
	//处理语音按键动作
	public void voiceKey(){
		//判断当前是否可以语音识别
		int cansr = SceneStatus.canSR(this);
		Log.d(Constant.DEBUG_TAG, "voiceKey to start voice... is can,"+cansr);
		if(cansr==Constant.SR_AWAKE){
		/*	if(Constant.projecId==Constant.PROJECT_ID.ZT_B11B){
				//保护唤醒
				if(!SceneStatus.VR_IS_STATUS){
					int res = VoiceCommon.systemStateChange(PlatformCode.STATE_SPEECHON);
					Log.d(Constant.DEBUG_TAG, "voiceKey protect start has start voice"+res);
				}
			}*/
			//可以唤醒
			 callVoiceAssistant();
		}
		//voiceStart();
	}
	
	
	

	// 打开语音助理识别界面
	public void callVoiceAssistant() {
		try {
			Log.e(Constant.DEBUG_TAG, "唤醒调用");
    		Intent intent = new Intent();
        	intent.setComponent(new ComponentName("com.iflytek.cutefly.speechclient",
                    "com.iflytek.autofly.SpeechClientService"));
        	 /*外界启动的包名，必填*/
            intent.putExtra("fromservice", "");
        	this.startService(intent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	//App向MCU发送消息，len为串口协议中的长度，arg部分不需要传入0xFF 0x66 长度及校验和
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
	
	//包括头及校验和的完整串口数据发送给MCU
	public void sendMcuData(byte[] data){
		Intent intent = new Intent("com.jsbd.serial.apptomcu");
		intent.putExtra("DataLen", data.length);
		intent.putExtra("Data", data);
		Log.e(Constant.DEBUG_TAG, "发送MCU数据");
		String datas = "McuData = ";
		for(int i = 0; i < data.length; i++){
			datas = datas+" "+data[i];
		}
		Log.e(Constant.DEBUG_TAG, datas);
		sendBroadcast(intent);
	}
	
	//计算串口数据的校验和
	public byte getCmdSum(byte[] data)
	{
		int bLen = data[2];
		byte bCheckSum = 0;
		for(int i=0; i<=bLen; i++)
		{
			bCheckSum += data[2+i];
		}

		bCheckSum = (byte) (~bCheckSum+1);
		Log.e("sss", "校验和 = "+bCheckSum);
		return bCheckSum;
	}
	
	//接收MCU发送的消息
	public class SerialDataReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(action.equals(ACTIONS.MSG_MCU_TO_AP)){
				byte[] data = intent.getByteArrayExtra("Data");
				//Log.e(Constant.DEBUG_TAG, "开始接受MCU返回执行结果:"+data.toString());
				onSerialData(data);
			}
		}
		
	}
	
	//接收语音拨号调用的消息
	public class VoiceDialReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(action.equals(ACTIONS.SR_VOICEDIALING_ACTION)){
				//唤醒语音
				voiceKey();
			}
		}
		
	}
}
