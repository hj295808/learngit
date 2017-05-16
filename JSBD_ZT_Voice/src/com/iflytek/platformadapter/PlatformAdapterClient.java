package com.iflytek.platformadapter;

import java.nio.channels.Channel;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.autofly.audioservice.util.AudioServiceCons;
import com.iflytek.platform.PlatformClientListener;
import com.iflytek.platform.type.PlatformCode;
import com.iflytek.platformservice.PlatformService;
import com.jsbd.voice.dimens.ACTIONS;
import com.jsbd.voice.dimens.Constant;
import com.jsbd.voice.dimens.SceneStatus;
import com.jsbd.voice.entity.VoiceAirControl;
import com.jsbd.voice.entity.VoiceEntity;
import com.jsbd.voice.entity.VoiceMusic;
import com.jsbd.voice.service.SerialService;
import com.jsbd.voice.service.VoiceService;
import com.jsbd.voice.vranimation.VRFloatViewManager;
import com.jsbd.voice.vranimation.VRWindowManager;

public class PlatformAdapterClient implements PlatformClientListener,
		LocationListener, OnSharedPreferenceChangeListener {

	private static String tag = "PlatformAdapterClient";
	private Context mContext;
	private AudioManager audioManager = null;
	private int currentMicType = -1;

	public static final int SEARCH_MUSIC = 0;
	public static final int SEARCH_RADIO = 1;

	private VoiceServiceFunctionInterface voiceFunction;
	protected LocationManager locationManager;
	private long startTime = 0;

	public PlatformAdapterClient(Context context) {
		// super(context);
		this.mContext = context;
		// 注册接收语音是否可以唤醒的广播，其它应用告知适配器，然后适配器再回调给助理

		mContext.registerReceiver(new SrAwakeReceive(), new IntentFilter(
				Constant.SR_CAN_AWAKE_ACTION));
		audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		voiceFunction = new VoiceHandler(this.mContext);//

		locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 1, this);
		
	
		//sPf.registerOnSharedPreferenceChangeListener(this);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SEARCH_MUSIC:
				String paramsStr = (String) msg.obj;
				String jsonResult = onGetMusics(paramsStr);
				if (PlatformService.platformCallback == null) {
					Log.e(tag, "PlatformService.platformCallback == null");
					return;
				}
				try {
					PlatformService.platformCallback.onSearchPlayListResult(
							SEARCH_MUSIC, jsonResult);
				} catch (RemoteException e) {
					Log.e(tag,
							"platformCallback get music error:"
									+ e.getMessage());
				}

				break;
			case SEARCH_RADIO:

				break;

			}
		}
	};

	public void onAbandonAudioFocus() {
		/* 模拟代码，实际需要实现 */
		// 这里使用的 android AudioFocus的音频协调机制
		audioManager.abandonAudioFocus(afChangeListener);
		// Log.d("xsl", "in_onAbandAudioFocus");
		/** 失去音频焦点 */
		sendAuidoCtrol(false);
		// 更改语音识别焦点状态
		// SceneStatus.setSceneStatusValueWithIndex(mContext, 5, 0);
		// int msg = SceneStatus.getStatusWithIndex(mContext, 5);
		// Log.d("xsl", "onAbandon_status:"+msg);
	}

	public String onDoAction(String actionJson) {
		/* 模拟代码，实际需要实现 */
		JSONObject resultJson = new JSONObject();
		if (actionJson == null) {
			try {
				resultJson.put("status", "fail");
				resultJson.put("message", "抱歉，没有可处理的操作");
			} catch (JSONException e) {

			}
			return resultJson.toString();
		} else {
			try {
				JSONObject action = new JSONObject(actionJson);

				if ("call".equals(action.getString("action"))) {
					if (action.getString("param1") != null) {
						Log.d(tag, "call number =" + action.getString("param1"));
						// Intent i = new Intent();
						// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// i.putExtra("NLPContent", action.getString("param1"));
						// i.setComponent(new ComponentName(
						// "com.iflytek.platformadapterdemo",
						// "com.iflytek.platformadapterdemo.MainActivity"));
						// mContext.startActivity(i);

						// 发送打电话广播
						// 先查询蓝牙状态
						if (!SceneStatus.getBtIsConnection(mContext)) {
							// VoiceService.getInstance().ttsStartSpeak("请先连接蓝牙");
							return VoiceCommon.returnSucceed(false, "请先连接蓝牙");
						}
						try {
							String numb = action.getString("param1").toString();
							numb = VoiceCommon.getNumb(numb);
							Intent mIntent = new Intent();
							mIntent.setAction(ACTIONS.SR_BLUETOOTHPHONE_ACTION);
							mIntent.putExtra("commandCode",
									Constant.SR_PHONE_CALL).putExtra(
									"callNumber", numb);
							mContext.sendBroadcast(mIntent);
							Log.d(tag, numb);
						} catch (Exception e) {
							return VoiceCommon.returnSucceed(false, "拨号失败");
						}

					}
					resultJson.put("status", "success");
					return resultJson.toString();
				} else if ("sendsms".equals(action.getString("action"))) {
					sendSMS(action.getString("param1"),
							action.getString("param2"));
					resultJson.put("status", "success");
					return resultJson.toString();
				} else if ("startspeechrecord".equals(action
						.getString("action"))) {
					Log.i(tag, "Action_StartSpeechRecord ");
					changeDinoseMode(0);
					resultJson.put("status", "success");
					startTime = System.currentTimeMillis();
					return resultJson.toString();
				} else if ("stopspeechrecord"
						.equals(action.getString("action"))) {
					Log.i(tag, "Action_StopSpeechRecord ");
					resultJson.put("status", "success");
					return resultJson.toString();
				} else if ("startwakerecord".equals(action.getString("action"))) {
					Log.i(tag, "Action_StartWakeRecord ");
					changeDinoseMode(1);
					resultJson.put("status", "success");
					return resultJson.toString();
				} else if ("stopwakerecord".equals(action.getString("action"))) {
					Log.i(tag, "Action_StopWakeRecord ");
					resultJson.put("status", "success");
					return resultJson.toString();
				}
			} catch (JSONException e) {
				Log.e(tag, "Fail to do action:" + e.getMessage());
			}

		}
		try {
			resultJson.put("status", "fail");
			resultJson.put("message", "抱歉，无法处理此操作");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultJson.toString();
	}

	public String onGetLocation() {
		// 获取当前位置 这是只是模拟了一个位置 。实际的位置 需要客户实现
		// 语音助理 的：今天的天气、到上海的航班、附近的美食、附近的酒店，是依赖这个位置信息的
		// 117.143269,31.834399
		/*
		 * String location =
		 * "{'name':'科大讯飞信息科技股份有限公司','address':'黄山路616','city':'合肥市','longitude':'117.143269','latitude':'31.834399'}"
		 * ; return location;
		 */
		String locationStr = "";
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			double latitude = location.getLatitude(); // 经度
			double longitude = location.getLongitude(); // 纬度
			double altitude = location.getAltitude(); // 海拔
			locationStr = "{'name':'','address':'','city':'','longitude':'"
					+ longitude + "','latitude':'" + latitude + "'}";
			Log.d(Constant.DEBUG_TAG, "onGetLocation()=====" + locationStr
					+ ";altitude:" + altitude);
		} else {
			Log.d(Constant.DEBUG_TAG, "onGetLocation()===== is null");
		}
		return locationStr;
	}

	public int onGetState(int arg0) {
		/* 伪代码，实际需要客户实现 */
		// 实际状态 需要客户读取提供
		if (arg0 == PlatformCode.STATE_BLUETOOTH_PHONE) {
			// 返回蓝牙电话状态
			if (!SceneStatus.getBtIsConnection(mContext)) {
				return PlatformCode.STATE_NO;
			}
			return PlatformCode.STATE_OK;
		} else if (arg0 == PlatformCode.STATE_SENDSMS) {
			// 返回短信功能是否可用
			return PlatformCode.STATE_NO;
		} else {
			// 不存在此种状态请求
			return PlatformCode.FAILED;
		}
	}

	public String onNLPResult(String strResult) {
		long endTime = System.currentTimeMillis();
		// Toast.makeText(mContext, "VR_TIME:"+(endTime-startTime), 1).show();
		Log.d("VR_TIME", "result:" + (endTime - startTime));
		/* 伪代码，实际需要客户实现 */
		// arg0 的json格式，请参见 讯飞汽车语音助理平台适配器开发册_Android文档。。。
		Log.d("voiceresult", strResult);
		VoiceEntity voiceEntity = new VoiceEntity(strResult);

		Log.d("PlatformAdapterClient", "focus is " + voiceEntity.getFocus());
		Intent i = new Intent();
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setComponent(new ComponentName("com.iflytek.platformadapter",
				"com.iflytek.platformadapter.MainActivity"));
		i.putExtra("NLPContent", strResult);
		// mContext.startActivity(i);

		if (voiceEntity.getFocus().equals("app")) {
			return voiceFunction.handleApp(strResult);
		}
		if (voiceEntity.getFocus().equals("airControl")) {
			return voiceFunction.handleAirControl(strResult);
		}
		if (voiceEntity.getFocus().equals("cmd")) {
			return voiceFunction.handleCmd(strResult);
		}
		if (voiceEntity.getFocus().equals("radio")) {
			return voiceFunction.handleRadio(strResult);
		}
		if (voiceEntity.getFocus().equals("music")) {
			// VoiceMusic voiceMusic = new VoiceMusic(strResult);
			// Log.d("PlatformAdapterClient", "music is "+
			// voiceMusic.getJsonObject().toString());
			// Log.d(Constant.DEBUG_TAG, voiceMusic.getMusicString());
			// i.putExtra("NLPContent", voiceMusic.getJsonObject().toString());
			// mContext.startActivity(i);
			Log.d("xsl", "handleMusic");
			return voiceFunction.handleMusic(strResult);

			// JSONObject resultJson = new JSONObject();
			//
			// try {
			// resultJson.put("message", "success");
			// } catch (JSONException e1) {
			// Log.d(tag, "json error");
			// }
			//
			// return resultJson.toString();
		}
		if (voiceEntity.getFocus().equals("vehicleInfo")) {
			return voiceFunction.handleVehicleInfo(strResult);
		}
		if (voiceEntity.getFocus().equals("carControl")) {
			return voiceFunction.handleCarControl(strResult);
		} else {
			return VoiceCommon.returnSucceed(true, "");
			// return "";
		}

	}

	public int onRequestAudioFocus(int streamType, int nDuration) {
		/* 伪代码，实际需要客户实现 */
		// 这里使用的 android AudioFocus的音频协调机制
		int audioFocusResult = audioManager.requestAudioFocus(afChangeListener,
				streamType, nDuration);
		if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			/** 获得音频焦点 通知导航 */
			sendAuidoCtrol(true);
		}
		Log.d("xsl", "in_onRequestAudioFocus");
		// 更改语音识别焦点状态
		// SceneStatus.setSceneStatusValueWithIndex(mContext, 5, 1);

		// int msg = SceneStatus.getStatusWithIndex(mContext, 5);
		// Log.d("xsl", "onRwquest_status:"+msg);

		return audioFocusResult;
	}

	public void onServiceUnbind() {
		// 助理因为异常，导致和平台适配器服务断开，这里可以做重置处理
		Log.e(tag, " onServiceUnbind ");
	}

	OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			AudioFocusChange(focusChange);
		}
	};

	/**
	 * 客户主动回调的方法
	 */
	public void AudioFocusChange(int focusChange) {
		if (PlatformService.platformCallback == null) {
			Log.e(tag, "PlatformService.platformCallback == null");
			return;
		}
		try {
			PlatformService.platformCallback.audioFocusChange(focusChange);
		} catch (RemoteException e) {
			Log.e(tag,
					"platformCallback audioFocusChange error:" + e.getMessage());
		}
	}

	private void uploadCustomData() {

		uploadCustomData(PlatformCode.UPLOADTYPE_APP,
				Constant.SR_CUSTOM_APP_DICT);
	}

	/**
	 * 客户主动回调的方法
	 */
	public void uploadCustomData(int type, String[] data) {
		if (PlatformService.platformCallback == null) {
			Log.e(tag,
					"uploadCustomData, PlatformService.platformCallback == null");
			return;
		}
		try {
			PlatformService.platformCallback.uploadCustomData(type, data);
		} catch (RemoteException e) {
			Log.e(tag,
					"uploadCustomData,platformCallback uploadCustomData error:"
							+ e.getMessage());
		}
	}

	/** 音频协调方案 **/
	private void sendAuidoCtrol(boolean isStart) {

		Intent intent = new Intent();
		if (isStart) {
			Log.d(tag, "sendAuidoCtrol voice start ==");
			intent.setAction(ACTIONS.SR_OPERATION_ACTION);
			intent.putExtra("operation", 1);
			updateContentProviderVoiceData(0x01);
			SerialService.getInstance().voiceStart();
			// 退出动画
			VoiceCommon.hiddenVRFloatView(mContext);
		} else {
			Log.d(tag, "sendAuidoCtrol voice end ==");
			intent.setAction(ACTIONS.SR_OPERATION_ACTION);
			intent.putExtra("operation", 0);
			updateContentProviderVoiceData(0x00);
			SerialService.getInstance().voiceStop();
			// 显示动画
			VoiceCommon.showVRFloatView(mContext);
		}
		mContext.sendBroadcast(intent);
	}

	private void sendSMS(String phoneNum, String message) {
		// 初始化发短信SmsManager类
		SmsManager smsManager = SmsManager.getDefault();
		// 如果短信内容长度超过200则分为若干条发
		if (message.length() > 200) {
			ArrayList<String> msgs = smsManager.divideMessage(message);
			for (String msg : msgs) {
				smsManager.sendTextMessage(phoneNum, null, msg, null, null);
			}
		} else {
			smsManager.sendTextMessage(phoneNum, null, message, null, null);
		}
	}

	/***
	 * 测试代码 需要按照自己平台的要求 切换驱动 降噪模块的工作模式
	 * 
	 * @param type
	 */
	private void changeDinoseMode(int type) {
		// TODO Auto-generated method stub
		/* 伪代码，实际需要客户实现 */
		// 若客户使用了 讯飞的降噪模块 这里来切换 降噪模块的不同工作模式
		Log.d(Constant.DEBUG_TAG, "changeDinoseMode type: " + type);
		com.iflytek.autofly.audioservice.util.AudioManager mAudioManager = com.iflytek.autofly.audioservice.util.AudioManager
				.registerListener(mContext, null);
		if (type == 0) {
			// 降噪模式
			if (currentMicType != 0) {
				// 切换为降噪功能

				VoiceCommon.setXFMicModle(Constant.XFMIC_FUNC_DENOSE, mContext);
				// mAudioManager.setMicMode(AudioServiceCons.MicMode.NOISE_REDUCTION);
				Log.i("info", "降噪模式"+type);
			}
			currentMicType = 0;

		} else {
			// 唤醒模式
			if (currentMicType != 1) {
				// 切换为唤醒功能
				VoiceCommon.setXFMicModle(Constant.XFMIC_FUNC_AWAKE, mContext);
				// mAudioManager.setMicMode(AudioServiceCons.MicMode.WAKEUP);
			}
			currentMicType = 1;
			Log.i("info", "唤醒模式"+type);
		}
	}

	/***
	 * 测试代码 需要按照自己平台的要求，在电话状态切换时调用PlatformService.platformCallback
	 * .phoneCallStateChange方法 在电话状态切换时调用以告知语音助理
	 * 
	 * @param type
	 */
	public void phoneStateChange(int state, String inComingNumber) {
		if (PlatformService.platformCallback == null) {
			Log.e(tag, "PlatformService.platformCallback == null");
			return;
		}
		JSONObject actionJson = new JSONObject();

		try {
			actionJson.put("state", state);
			actionJson.put("number", inComingNumber);
			PlatformService.platformCallback.phoneCallStateChange(actionJson
					.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int changePhoneState(int state) {
		// TODO Auto-generated method stub
		/* 伪代码，实际需要客户实现 */
		// 客户开发时应注意此处应为语音助理通知平台接听或者挂断电话，需要执行相应的电话操作
		Log.d(tag, "change phone state:" + state);
		if (PlatformAdapterApplication.getUiHandler() != null) {
			Message msg = new Message();
			msg.what = state;
			PlatformAdapterApplication.getUiHandler().sendMessage(msg);
			return PlatformCode.SUCCESS;
		}
		return PlatformCode.FAILED;
	}

	public String onGetCarNumbersInfo() {
		// 获取车辆信息，暂时只是一个測試數據 。
		// 语音助理 的：违章查询业务依赖此信息
		// 包含三個信息：carNumber车牌号，carCode车架号，carDriveNo发动机号
		String carInfo = "{'carNumber':'粤YM5610','carCode':'116238','carDriveNo':'123446'}";
		return carInfo;
	}

	/*
	 * {"song":"忘情水"}<br/> {"artist":"刘德华"}<br/> {"category":"摇滚歌曲"}<br/>
	 * {"source":"网络"}<br/>
	 */
	public String onGetMusics(String params) {
		try {
			JSONObject action = new JSONObject(params);
			String songStr = action.optString("song");
			String artistStr = action.optString("artist");
			String category = action.optString("category");
			String source = action.optString("source");
			String album = action.optString("album");
			return "{\"focus\":\"music\",\"status\":\"success\",\"result\":[{\"song\":\"忘情水\",\"artist\":\"刘德华\"},"
					+ "{\"song\":\"恭喜发财\",\"artist\":\"刘德华\"},"
					+ "{\"song\":\"billie jean\",\"artist\":\"迈克杰克逊\",\"category\":\"摇滚\"},"
					+ "{\"song\":\"beat it\",\"artist\":\"迈克杰克逊\",\"category\":\"摇滚\"},"
					+ "{\"song\":\"we are the world\",\"artist\":\"迈克杰克逊\",\"category\":\"摇滚\"},"
					+ "{\"song\":\"beat it\",\"artist\":\"迈克杰克逊\",\"category\":\"摇滚\"},"
					+ "{\"song\":\"双节棍\",\"artist\":\"周杰伦\",\"album\":\"Jay\"},"
					+ "{\"song\":\"青花瓷\",\"artist\":\"周杰伦\"},"
					+ "{\"song\":\"斗牛\",\"artist\":\"周杰伦\"},"
					+ "{\"song\":\"七里香\",\"artist\":\"周杰伦\"}" + "]}";
			// return
			// "{\"focus\":\"music\",\"status\":\"fail\",\"message\":\"获取音乐失败\"}";

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"focus\":\"music\",\"status\":\"fail\",\"message\":\"获取音乐失败\"}";
	}

	@Override
	public boolean onSearchPlayList(String arg0) {
		Log.d(tag, "onSearchPlayList:" + arg0);
		try {
			JSONObject action = new JSONObject(arg0);
			String focus = action.optString("focus");
			if ("music".equals(focus)) {
				// 不要进行耗时操作，有需要的话请使用handler。
				/*
				 * Message message = new Message(); message.what = SEARCH_MUSIC;
				 * message.obj = arg0; handler.sendMessage(message);
				 */
				if (voiceFunction != null) {
					((VoiceHandler) voiceFunction).voiceHandlerSendBroadcast(
							ACTIONS.SR_MEDIA_ACTION,
							Constant.SR_MUSIC_MUSIC_PLAY);
				}
				return true;
			} else if ("radio".equals(focus)) {
				// 不要进行耗时操作，有需要的话请使用handler。
				/*
				 * Message message = new Message(); message.what = SEARCH_RADIO;
				 * message.obj = arg0; handler.sendMessage(message);
				 */
				if (voiceFunction != null) {
					((VoiceHandler) voiceFunction)
							.changeModel(Constant.SR_MODEL_TYPE.RADIO);
				}
				return true;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}

	// 接收助理是否可以唤醒的广播
	class SrAwakeReceive extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			/*
			 * if(intent.getAction().equals(Constant.SR_CAN_AWAKE_ACTION)){ int
			 * srState = intent.getIntExtra(Constant.SR_AWAKESR_STR, -1); int
			 * callbackValue = -1; Log.d(tag, "SrAwakeReceive:" + srState);
			 * if(srState == 1){ //恢复语音唤醒 callbackValue =
			 * PlatformCode.STATE_SPEECHON; }else if(srState == 0){ //禁用语音唤醒
			 * callbackValue = PlatformCode.STATE_SPEECHOFF; } if
			 * (PlatformService.platformCallback != null) { Log.d(tag,
			 * "SrAwakeReceive callbackValue:" + callbackValue); try {
			 * PlatformService
			 * .platformCallback.systemStateChange(callbackValue); } catch
			 * (RemoteException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }else{ Log.d(tag,
			 * "SrAwakeReceive callback is null"); } }
			 */
		}

	}

	// 更新语音识别状态
	public void updateContentProviderVoiceData(int srStatus) {
		Uri uri = Uri.parse(Constant.SCENE_STATUS_CONTENTPROVIDER);
		ContentValues values = new ContentValues();
		values.put("value", srStatus);
		Log.e(Constant.DEBUG_TAG, "start to update provider.....");
		try {
			int dasdf = mContext.getContentResolver().update(uri, values, null,
					new String[] { "5" });
			Log.e(Constant.DEBUG_TAG, "updaterows:" + dasdf);
		} catch (Exception e) {
			Log.e(Constant.DEBUG_TAG, "updaterows:" + e);
			e.printStackTrace();
		}

	}

	private String longitude = "";// 经度
	private String latitude = "";// 纬度

	// 位置start 1018
	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			this.longitude = String.valueOf(location.getLongitude());
			this.latitude = String.valueOf(location.getLatitude());
			Log.d("suochaolocation", "onLocationChanged():" + "Latitude:"
					+ this.latitude + ", Longitude:" + this.longitude);
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d("suochaolocation", "onProviderDisabled()");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("suochaolocation", "onProviderEnabled()");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("suochaolocation", "onStatusChanged()");
	}

	// 位置end 1018

	//监听设置参数变化
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.d(Constant.DEBUG_TAG, "onSharedPreferenceChanged:"+key);
		if(key!=null||key.equals("isShow")){return;}
		if(sharedPreferences!=null){
			boolean isShow =	sharedPreferences.getBoolean(key, true);
			if(isShow){
				VoiceCommon.showVRFloatView(mContext);
			}else{
				VoiceCommon.hiddenVRFloatView(mContext);
			}
		}
	}

}
