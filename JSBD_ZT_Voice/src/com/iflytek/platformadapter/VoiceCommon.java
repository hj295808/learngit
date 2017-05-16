package com.iflytek.platformadapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;











import com.iflytek.platform.type.PlatformCode;
import com.iflytek.platformservice.PlatformService;
import com.jsbd.voice.dimens.Constant;
import com.jsbd.voice.dimens.SceneStatus;
import com.jsbd.voice.jni.XfMicJNI;
import com.jsbd.voice.service.SerialService;
import com.jsbd.voice.service.VoiceService;
import com.jsbd.voice.vranimation.FloatViewActivity;
import com.jsbd.voice.vranimation.VRFloatViewManager;
import com.jsbd.voice.vranimation.VRWindowManager;

public class VoiceCommon {
	//语音处理后要返回处理状态给助理，如果失败则让助理播报msg内容
	public static String returnSucceed(boolean isSucceed,String msg){
		JSONObject resultJson = new JSONObject();
		try {
			if (isSucceed) {
				resultJson.put("status", "success");
			} else {
				resultJson.put("status", "fail");
				resultJson.put("message", msg==null?"":msg);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultJson.toString();
	}
	
	public static boolean isNumeric(String str){    
		Pattern pattern = Pattern.compile("[0-9]+.?[0-9]");    
		Matcher isNum = pattern.matcher(str);   
		if( !isNum.matches()){   
			return false;
		}
		return true;
	}

	public static int getIntValue(String offset) {
		if(offset==null||offset.length()==0){
			return -1;
		}
		if(!isNumeric(offset)){
			return -1;
		}else{
			try{
				return Integer.parseInt(offset);
			}catch(Exception e){
				e.printStackTrace();
				return -1;
			}
		}
		
	}

	//1 普通录音功能，2 降噪模式 ，5 唤醒 
	public static void setXFMicModle(int value,Context mcontext){
		if(mcontext==null){return;}
		//if(value==5||value==1){return;}
/*		if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
			try{
				//Imax6.setXFMicModle(value, mcontext);		
				  XfMicJNI.SetXfmicMode(value);
			}catch(Exception e){
				Log.d(Constant.DEBUG_TAG, Log.getStackTraceString(e));
			}
			//Imax6.setXFMicModle(value, mcontext);		
			Log.d(Constant.DEBUG_TAG, "Imax6jni setXFMicModle value:"+value);
		}else{*/
		Log.d(Constant.DEBUG_TAG, Constant.projecId.toString()+ "jni setXFMicModle  value:" + value);
		if (Constant.projecId == Constant.PROJECT_ID.ZT_B11B) {
			if (value == 2 || value == 5) {
				// setMicGain(40,mcontext);
			}
		}
		try {
			XfMicJNI.SetXfmicMode(value);
		} catch (Exception e) {
			Log.d(Constant.DEBUG_TAG, Log.getStackTraceString(e));
		}
		//}
		getXfMicMode(mcontext);
	}
	
	public static int getXfMicMode(Context mcontext){
		int getmodle = -9999;
		try{
			//getmodle = Imax6.getXfMicMode(mcontext);
			getmodle =  XfMicJNI.GetXfmicMode();
			Log.d(Constant.DEBUG_TAG,  Constant.projecId.toString()+"jni getXfMicMode value:"+getmodle);
		}catch(Exception e){
			Log.d(Constant.DEBUG_TAG, Log.getStackTraceString(e));
		}
		return getmodle;
	/*	if(mcontext==null){return getmodle;}
		if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
			try{
				//getmodle = Imax6.getXfMicMode(mcontext);
				getmodle =  XfMicJNI.GetXfmicMode();
			}catch(Exception e){
				Log.d(Constant.DEBUG_TAG, Log.getStackTraceString(e));
			}
			//getmodle = Imax6.getXfMicMode(mcontext);
				Log.d(Constant.DEBUG_TAG, "Imax6jni getXfMicMode value:"+getmodle);
				
			return getmodle;
		}else{
			if(HkInterface.getInstance(mcontext)!=null){
				try{
				   getmodle = HkInterface.getInstance(mcontext).getXfMicMode();
				   Log.d(Constant.DEBUG_TAG, Constant.projecId.toString()+" getXFMicModle value:"+getmodle);
				 return getmodle;
				}catch (Exception e){
					Log.d(Constant.DEBUG_TAG, "getXFMicModle err ");
					e.printStackTrace();
					return getmodle;
				}
			}else{
				Log.d(Constant.DEBUG_TAG, "hkintereface is null getXFMicModle");
				return getmodle;
			}
		}*/
		
	}

	public static void setMicGain(int value, Context context) {
		if(context==null){return;}
		try{
			//HkInterface.getInstance(context).setMicGain(value);
			Log.d(Constant.DEBUG_TAG, Constant.projecId.toString()+" setMicGain value:"+value);
		}catch(Exception e){
			Log.d(Constant.DEBUG_TAG, "setMicGain err");
			e.printStackTrace();
		}
	}

	public static String getNumb(String numb) {
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(numb);   
		return m.replaceAll("").trim();
	}
	
	//4:禁用，5:回复
	public static int systemStateChange(int value){
		int res = -99;
		try {
			if(value == PlatformCode.STATE_SPEECHON){
				SceneStatus.VR_IS_STATUS = true;
			}
			res = PlatformService.platformCallback.systemStateChange(value);
			if(res == PlatformCode.SUCCESS){
				 if(value == PlatformCode.STATE_SPEECHOFF){
					SceneStatus.VR_IS_STATUS = false;
				}
			}
			Log.d(Constant.DEBUG_TAG,"systemStateChange vrisstatus:"+SceneStatus.VR_IS_STATUS+",value:"+value+";res"+res);
		} catch (Exception e) {
			Log.d(Constant.DEBUG_TAG, Log.getStackTraceString(e));
		}
		Log.d(Constant.DEBUG_TAG,"systemStateChange :"+value);
		return res;
	}
	
	public static void showVRFloatView(Context mContext){
		if(Constant.projecId == Constant.PROJECT_ID.ZT_B11B){
			//FloatViewActivity.showWindow(mContext);
			//VRFloatViewManager.getInstance(mContext).showVRFloat();
			
			Intent intent = new Intent();
			if(mContext!=null){
				//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(mContext, VoiceService.class);
				intent.putExtra("isShow", true);
				mContext.startService(intent);
			}
			//VRWindowManager.createSmallWindow(mContext);
			//VRFloatViewManager.getInstance(mContext).show();
			//VRWindowManager.createSmallWindow(mContext);
			//}
		}
	}
	
	public static void hiddenVRFloatView(Context mContext){
		if(Constant.projecId == Constant.PROJECT_ID.ZT_B11B){
			//VRFloatViewManager.getInstance(mContext).hiddenVRFloatView();
			Intent intent = new Intent();
			if(mContext!=null){
				//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(mContext, VoiceService.class);
				intent.putExtra("isShow", false);
				mContext.startService(intent);
			}
		}
	}
}
