package com.iflytek.platformadapter;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.iflytek.platformservice.PlatformHelp;
import com.jsbd.voice.dimens.Constant;
import com.jsbd.voice.service.PublicReceiver;

public class PlatformAdapterApplication extends Application{
	private PlatformAdapterClient platformClient;
	@Override
	public void onCreate(){
		super.onCreate();
		Log.d(Constant.DEBUG_TAG, "adapterapplication is onCreate");
	
		//�л�Ϊ����ģʽ
		//VoiceCommon.setXFMicModle(Constant.XFMIC_FUNC_AWAKE, this);
		
		//ע��绰״̬�㲥 com.haoke.bt.state
		 IntentFilter filter = new IntentFilter();  
         filter.addAction("com.haoke.bt.state");  
         registerReceiver(new PublicReceiver(), filter);  
         Log.d(Constant.DEBUG_TAG, "registerReceiver btstate");
		//���ԣ�����Ϊ��ͨ¼����ͬʱ��������
	/*	VoiceCommon.setMicGain(20, this);
		VoiceCommon.setXFMicModle(Constant.XFMIC_FUNC_RECORD, this);*/
		
		/**
		 * �������� ʵ�� PlatformClientListener �ӿڵĶ���
		 */
		platformClient=new PlatformAdapterClient(this);
		
		PlatformHelp.getInstance().setPlatformClient(platformClient);		
		//�������ڷ���
		startService(new Intent(this,com.jsbd.voice.service.VoiceService.class));
		//��������
		startService(new Intent(this,com.jsbd.voice.service.SerialService.class));

		
		/*if(Constant.projecId==Constant.PROJECT_ID.ZT_B11A_C){
			try{
				Log.d(Constant.DEBUG_TAG, "start speechserver 77");
				Intent intent = new Intent();
	        	intent.setComponent(new ComponentName("com.iflytek.cutefly.speechclient",
	                    "com.iflytek.autofly.SpeechClientService"));
	        	 ��������İ���������
//	            intent.putExtra("fromservice", "");
	        	startService(intent);
	        	Log.d(Constant.DEBUG_TAG, "start  speechserver 88");
			}catch(Exception e){
				Log.d(Constant.DEBUG_TAG, Log.getStackTraceString(e));
			}
		}*/
	}
	
	//��ȡ��ǰƽ̨ʵ�������ڵ���ƽ̨�������ӿ�
	public static PlatformAdapterClient getPlatformClientInstance(){
		return (PlatformAdapterClient) PlatformHelp.getInstance().getPlatformClient();
	}
	
	public static Handler getUiHandler() {
		return uiHandler;
	}

	public static void setUiHandler(Handler uiHandler) {
		PlatformAdapterApplication.uiHandler = uiHandler;
	}

	//��ǰҳ���handler�����ڸ���ui
	private static Handler uiHandler;

	
}
