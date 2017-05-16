package com.jsbd.voice.service;

import com.iflytek.platformadapter.VoiceCommon;
import com.jsbd.voice.dimens.Constant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PublicReceiver extends BroadcastReceiver {
	 private   int BT_CALL_STATE_FLAG = -1;//记录蓝牙电话状态，标记上一次
	public PublicReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent intent) {
	//	if(Constant.projecId != Constant.PROJECT_ID.ZT_B11B){return;}
		if(intent==null){
			return;
		}
		String action = intent.getAction();
		if(action.equals("com.haoke.bt.state")){
			//打出4，打过来5，通话6，挂断0，
			int callState =  intent.getExtras().getInt("call_state");
			Log.d(Constant.DEBUG_TAG, "com.haoke.bt.state newValue,lastValue:"+callState+","+BT_CALL_STATE_FLAG);
			if(callState != BT_CALL_STATE_FLAG){
			switch (callState) {
			case 4:
				setBtMic(context);
				break;
			case 5:
				setBtMic(context);
				break;
			case 6:
				
				break;
			case 0:
				VoiceCommon.setXFMicModle(Constant.XFMIC_FUNC_AWAKE, context);
				break;
			default:
				break;
				}
			BT_CALL_STATE_FLAG = callState;
			}

		}
	}
	
	private void setBtMic(Context context){
		if(context==null){return;}
		if(Constant.projecId == Constant.PROJECT_ID.ZT_B11B){
			
			//VoiceCommon.setMicGain(50,context);
		}
		VoiceCommon.setXFMicModle(Constant.XFMIC_FUNC_RECORD, context);
	}
	/**
	 by suochao 2017年1月20日
	 */
}
