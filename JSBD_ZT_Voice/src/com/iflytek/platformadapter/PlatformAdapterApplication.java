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
	
		//切换为唤醒模式
		//VoiceCommon.setXFMicModle(Constant.XFMIC_FUNC_AWAKE, this);
		
		//注册电话状态广播 com.haoke.bt.state
		 IntentFilter filter = new IntentFilter();  
         filter.addAction("com.haoke.bt.state");  
         registerReceiver(new PublicReceiver(), filter);  
         Log.d(Constant.DEBUG_TAG, "registerReceiver btstate");
		//测试，设置为普通录音，同时设置增益
	/*	VoiceCommon.setMicGain(20, this);
		VoiceCommon.setXFMicModle(Constant.XFMIC_FUNC_RECORD, this);*/
		
		/**
		 * 给助理传递 实现 PlatformClientListener 接口的对象
		 */
		platformClient=new PlatformAdapterClient(this);
		
		PlatformHelp.getInstance().setPlatformClient(platformClient);		
		//启动串口服务
		startService(new Intent(this,com.jsbd.voice.service.VoiceService.class));
		//启动服务
		startService(new Intent(this,com.jsbd.voice.service.SerialService.class));

		
		/*if(Constant.projecId==Constant.PROJECT_ID.ZT_B11A_C){
			try{
				Log.d(Constant.DEBUG_TAG, "start speechserver 77");
				Intent intent = new Intent();
	        	intent.setComponent(new ComponentName("com.iflytek.cutefly.speechclient",
	                    "com.iflytek.autofly.SpeechClientService"));
	        	 外界启动的包名，必填
//	            intent.putExtra("fromservice", "");
	        	startService(intent);
	        	Log.d(Constant.DEBUG_TAG, "start  speechserver 88");
			}catch(Exception e){
				Log.d(Constant.DEBUG_TAG, Log.getStackTraceString(e));
			}
		}*/
	}
	
	//获取当前平台实例，用于调用平台适配器接口
	public static PlatformAdapterClient getPlatformClientInstance(){
		return (PlatformAdapterClient) PlatformHelp.getInstance().getPlatformClient();
	}
	
	public static Handler getUiHandler() {
		return uiHandler;
	}

	public static void setUiHandler(Handler uiHandler) {
		PlatformAdapterApplication.uiHandler = uiHandler;
	}

	//当前页面的handler，用于更新ui
	private static Handler uiHandler;

	
}
