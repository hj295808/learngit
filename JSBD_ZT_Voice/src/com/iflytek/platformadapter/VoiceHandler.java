package com.iflytek.platformadapter;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;





import com.jsbd.voice.dimens.ACTIONS;
import com.jsbd.voice.dimens.CarInfor;
import com.jsbd.voice.dimens.Constant;
import com.jsbd.voice.dimens.Constant.PROJECT_ID;
import com.jsbd.voice.dimens.SceneStatus;
import com.jsbd.voice.dimens.Constant.SR_MODEL_TYPE;
import com.jsbd.voice.entity.CarAirStatus;
import com.jsbd.voice.entity.VoiceAirControl;
import com.jsbd.voice.entity.VoiceAirControl.VoiceSub;
import com.jsbd.voice.entity.VoiceApp;
import com.jsbd.voice.entity.VoiceCarControl;
import com.jsbd.voice.entity.VoiceCmd;
import com.jsbd.voice.entity.VoiceEntity;
import com.jsbd.voice.entity.VoiceMusic;
import com.jsbd.voice.entity.VoiceRadio;
import com.jsbd.voice.entity.VoiceVehicleInfo;
import com.jsbd.voice.service.SerialService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * by suochao 用来处理识别结果
 */
public class VoiceHandler implements VoiceServiceFunctionInterface {

	private Context mContext;
	private SerialService serialService;

	public VoiceHandler(Context context) {
		this.mContext = context;
		serialService = SerialService.getInstance();
	}

	@Override
	public String handleRadio(String recognizerResult) {
		VoiceRadio vRadio = new VoiceRadio(recognizerResult);
		if (serialService == null) {
			serialService = SerialService.getInstance();
		}
		String band = vRadio.getWaveband();//
		String sFreq = vRadio.getCode();//
		Log.d(Constant.DEBUG_TAG, "radioent,band,sFreq" + vRadio.getJsonObject().toString() + "," + vRadio.getWaveband()
				+ "," + vRadio.getCode());
		int iFreq = 0;
		boolean flag = false;
		if (sFreq != null && sFreq.length() > 0) {
			if (VoiceCommon.isNumeric(sFreq)) {
				Log.d(Constant.DEBUG_TAG, "isNumeric," + band);
				if (band == null || band.equals("fm")) {
					iFreq = (int) (Double.parseDouble(sFreq) * 100);
					Log.e(Constant.DEBUG_TAG, "收音机fm频率：" + iFreq);
					int iFreqH = iFreq / 100;
					int iFreqL = iFreq % 100;
					if (iFreq > 10800 || iFreq < 8750) {
						// ttsSpeak("频率超出范围");
						flag = false;
						return VoiceCommon.returnSucceed(false, "频率超出范围");
					} else {
						flag = true;
					}
					if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
						//如果是imax6 则要发一个打开收音机的广播
						changeModel(SR_MODEL_TYPE.RADIO);
					}
					SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
							(byte) 0x01);
					SerialService.getInstance().sendData(8, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
							(byte) 0x05, (byte) 0x00, (byte) iFreqH, (byte) iFreqL);
					SerialService.getInstance().voiceStop();
				} else if (band.equals("am")) {
					Log.d(Constant.DEBUG_TAG, "oiooiououo：" + iFreq);
					try {
						iFreq = Integer.parseInt(sFreq);
					} catch (Exception e) {
						// ttsSpeak("频率超出范围");
					}
					Log.d(Constant.DEBUG_TAG, "收音机am频率：" + iFreq);
					if (iFreq < 531 || iFreq > 1629) {
						// ttsSpeak("频率超出范围");
						return VoiceCommon.returnSucceed(false, "频率超出范围");
					}
					byte iFreqH = (byte) ((iFreq & 0xFF00) >> 8);
					byte iFreqL = (byte) (iFreq & 0x00FF);
					Log.d(Constant.DEBUG_TAG, "收音机am频率，高八位：" + iFreqH + ";" + "低八位：" + iFreqL);
					String tmp = String.format("%h   %h", iFreqH, iFreqL);
					Log.e(Constant.DEBUG_TAG, tmp);
					if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
						//如果是imax6 则要发一个打开收音机的广播
						changeModel(SR_MODEL_TYPE.RADIO);
					}
					SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
							(byte) 0x01);
					SerialService.getInstance().sendData(8, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
							(byte) 0x05, (byte) 0x01, (byte) iFreqH, (byte) iFreqL);
					SerialService.getInstance().voiceStop();
				}else{
					//只指定频率，没有指定调频还是调幅
					//double a = 9.5;
					String memo = 	setRadioByCode(sFreq);
					if(memo.equals("")){
						return VoiceCommon.returnSucceed(true, memo);
					}else{
						return VoiceCommon.returnSucceed(false, memo);
					}
				//	
				}
			} else {
				// ttsSpeak("频率超出范围");
				SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
						(byte) 0x01);
			}
		} else {
			// ttsSpeak("频率超出范围");
			if (band.equals("am")) {
				Log.d(Constant.DEBUG_TAG, "收音 am");
		
				SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
						(byte) 0x01);
				if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
					//如果是imax6 则要发一个打开收音机的广播
					changeModel(SR_MODEL_TYPE.RADIO);
				}

				SerialService.getInstance().sendData(8, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x05, (byte) 0x01, (byte) 0xff, (byte) 0xff);
				if(vRadio.getRawText().contains("调幅搜索")){
					SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
							(byte) 0x03);
					return null;
				}
			
			}
			if (band.equals("fm")) {
				Log.d(Constant.DEBUG_TAG, "收音  fm");
		
				SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
						(byte) 0x01);
				if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
					//如果是imax6 则要发一个打开收音机的广播
					changeModel(SR_MODEL_TYPE.RADIO);
				}
				SerialService.getInstance().sendData(8, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x05, (byte) 0x00, (byte) 0xff, (byte) 0xff);
				if(vRadio.getRawText().contains("调频搜索")){
					SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
							(byte) 0x03);
					return null;
				}
		
			}
		}

		Log.d(Constant.DEBUG_TAG, "收音   " + sFreq + "       " + band + "   " + iFreq);
		return null;
	}

	private String setRadioByCode(String sFreq) {
		String memo = "";
		if (VoiceCommon.isNumeric(sFreq)) {
			int iFreq = (int) (Double.parseDouble(sFreq) * 100);
			if (iFreq > 10800 || iFreq < 8750) {
				//不在调频范围内,在判断是否在调幅范围内
				iFreq = Integer.parseInt(sFreq);
				if (iFreq < 531 || iFreq > 1629) {
					//不在调幅范围内
					memo = "频率超出范围";
				}else{
					//设定调幅
					byte iFreqH = (byte) ((iFreq & 0xFF00) >> 8);
					byte iFreqL = (byte) (iFreq & 0x00FF);
					Log.d(Constant.DEBUG_TAG, "radio am，iFreqH：" + iFreqH + ";" + "iFreqH：" + iFreqL);
					String tmp = String.format("%h   %h", iFreqH, iFreqL);
					Log.e(Constant.DEBUG_TAG, tmp);
					if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
						//如果是imax6 则要发一个打开收音机的广播
						changeModel(SR_MODEL_TYPE.RADIO);
					}
					SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
							(byte) 0x01);
					SerialService.getInstance().sendData(8, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
							(byte) 0x05, (byte) 0x01, (byte) iFreqH, (byte) iFreqL);
				}
			}else{
				//设定调频
				int iFreqH = iFreq / 100;
				int iFreqL = iFreq % 100;
				if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
					//如果是imax6 则要发一个打开收音机的广播
					changeModel(SR_MODEL_TYPE.RADIO);
				}
				SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
						(byte) 0x01);
				SerialService.getInstance().sendData(8, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x05, (byte) 0x00, (byte) iFreqH, (byte) iFreqL);
			}
		}else{
			memo = "频率超出范围";
		}
		return memo;
		//
	}

	// 媒体控制，打开视频，打开图片，播放音乐
	public void voiceHandlerSendBroadcast(String action, int constant) {
		Log.d(Constant.DEBUG_TAG, "voiceHandlerSendBroadcast:" + action + "," + constant);
		try{
			Intent mIntent = new Intent();
			mIntent.setAction(action);
			mIntent.putExtra("commandCode", constant);
			SerialService.getInstance().sendBroadcast(mIntent);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public String handleCmd(String recognizerResult) {
		VoiceCmd voiceCmd = new VoiceCmd(recognizerResult);
		if(voiceCmd.getName().equals("显示空气质量信息")){
			voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_PM);
			return VoiceCommon.returnSucceed(true, "");
		}else if(voiceCmd.getName().equals("关闭空气质量信息")){
			voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_PM_CLOSE);
			return VoiceCommon.returnSucceed(true, "");
		}
		// 音量控制
		if (voiceCmd.getCategory().equals("音量控制")) {
			if (voiceCmd.getName().equals("音量+")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x01);

			} else if (voiceCmd.getName().equals("音量-")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x02);
			} else if (voiceCmd.getName().equals("静音")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x03);
			} else if (voiceCmd.getName().equals("打开音量")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x06);
			} else if (voiceCmd.getName().equals("音量调节")) {// 音量设置为多少语句
				if(isNumericN(voiceCmd.getNamaValue())){
					int value = Integer.parseInt(voiceCmd.getNamaValue());
					SerialService.getInstance().sendData(6, (byte)0x12, (byte)0x00, (byte)0x09, (byte)0x01, (byte)0x00, (byte)value);
				}
			}
		}
		
	
		// 播放模式
		if (voiceCmd.getCategory().equals("播放模式")) {
			Log.d("dd", "ddddd");
			if (voiceCmd.getName().equals("单曲循环")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_REPEAT_ONE_MODE);
			} else if (voiceCmd.getName().equals("顺序播放")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_REPEAT_OFF_MODE);
			} else if (voiceCmd.getName().equals("随机播放")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_RANDOM_MODE);
			} else if (voiceCmd.getName().equals("文件夹循环")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_REPEAT_FILE_MODE);
			} else if (voiceCmd.getName().equals("全盘播放")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_REPEAT_ALL_MODE);
			} else if (voiceCmd.getName().equals("上一张")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PREVIOUS);
			} else if (voiceCmd.getName().equals("下一张")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_NEXT);
			}

		}
		// 曲目控制
		if (voiceCmd.getCategory().equals("曲目控制")) {
			if (voiceCmd.getName().equals("上一首")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PREVIOUS);
			} else if (voiceCmd.getName().equals("下一首".toString())) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_NEXT);
			} else if (voiceCmd.getName().equals("暂停")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PAUSE);
			} else if (voiceCmd.getName().equals("播放")) {
				if(Constant.projecId == Constant.PROJECT_ID.ZT_B11B){
					voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PLAY);
				}else{
					voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PLAY);
				}
			}

		}

		if (voiceCmd.getCategory().length() == 0) {
			if (voiceCmd.getName().equals("上一首") || voiceCmd.getName().equals("上一个")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PREVIOUS);
			} else if (voiceCmd.getName().equals("下一首") || voiceCmd.getName().equals("下一个")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_NEXT);
			}
		}
		// 屏幕控制
		if (voiceCmd.getCategory().equals("屏幕控制")) {
			if (voiceCmd.getName().equals("亮度+")) {

			} else if (voiceCmd.getName().equals("亮度-")) {

			}

		}
		
		//电话控制
		if (voiceCmd.getCategory().equals("电话控制")) {
			if (voiceCmd.getName().equals("通话记录")) {
				if (SceneStatus.getBtIsConnection(mContext)) {
					voiceHandlerSendBroadcast(ACTIONS.SR_BLUETOOTHPHONE_ACTION, Constant.SR_PHONE_HISTORYCALL);
				} else {
					return VoiceCommon.returnSucceed(false, "请先连接蓝牙");
				}
			} 
		}
		// 扫描电台
		if (voiceCmd.getCategory().equals("收音机控制") || voiceCmd.getCategory().equals("收音机")) {
			if (voiceCmd.getName().equals("扫描电台")||voiceCmd.getName().equals("搜索电台")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x03);
			} else if (voiceCmd.getName().equals("下一频道")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x0A);
			} else if (voiceCmd.getName().equals("上一频道")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x09);
			} else if (voiceCmd.getName().equals("向上搜索")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x01);
			} else if (voiceCmd.getName().equals("向下搜索")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x02);
			} else if (voiceCmd.getName().equals("浏览电台")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x04);
			}
		}
		return null;
	}

	private void searchSongBroadcast(VoiceMusic voiceMusic) {
		String songname = voiceMusic.getSong();
		Intent mIntent = new Intent();
		mIntent.setAction(ACTIONS.SR_MEDIA_ACTION);
		mIntent.putExtra("commandCode", Constant.SR_MUSIC_SEARCH).putExtra("songName", songname);
		SerialService.getInstance().sendBroadcast(mIntent);
	}

	@Override
	public String handleMusic(String recognizerResult) {
		// TODO Auto-generated method stub
		Log.d("PlatformAdapterClient", "handler music:" + recognizerResult);
		VoiceMusic mVoiceMusic = new VoiceMusic(recognizerResult);
		/*
		 * song不为空进行搜歌曲广播，为空的情况下进行切源 若命令为播放音乐，则source为空
		 */
		if (mVoiceMusic.getArtist().length() > 0) {
			// searchSongBroadcast(mVoiceMusic);
			return VoiceCommon.returnSucceed(false, "不支持按歌手名播放歌曲");
		}
		if (mVoiceMusic.getSource().equals("u盘") || mVoiceMusic.getSource().equalsIgnoreCase("usb")) {
/*			if (!SceneStatus.getUIsConnection(mContext)) {
				return VoiceCommon.returnSucceed(false, "请插入U盘");
			}*/
			changeModel(SR_MODEL_TYPE.USB);
			Log.d("PlatformAdapterClient", "in_u盘");
			return VoiceCommon.returnSucceed(true, "");
		} else if (mVoiceMusic.getSource().equalsIgnoreCase("sd")) {
			changeModel(SR_MODEL_TYPE.SD);
			return VoiceCommon.returnSucceed(true, "");
		} else if (mVoiceMusic.getSource().equalsIgnoreCase("iPod")) {
			if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
				changeModel(SR_MODEL_TYPE.IPOD);
				return VoiceCommon.returnSucceed(true, "");
			}
			if (!SceneStatus.getIpodIsConnection(mContext)) {
				return VoiceCommon.returnSucceed(false, "请先连接iPod");
			}
			changeModel(SR_MODEL_TYPE.IPOD);
			return VoiceCommon.returnSucceed(true, "");
		} else if (mVoiceMusic.getSource().equals("蓝牙音乐") || mVoiceMusic.getSource().equals("蓝牙")) {
			if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
				changeModel(SR_MODEL_TYPE.BLUTOOTH);
				return VoiceCommon.returnSucceed(true, "");
			}
			if (!SceneStatus.getBtIsConnection(mContext)) {
				return VoiceCommon.returnSucceed(false, "请先连接蓝牙");
			}
			changeModel(SR_MODEL_TYPE.BLUTOOTH);
			return VoiceCommon.returnSucceed(true, "");
		} else if (mVoiceMusic.getSource().equalsIgnoreCase("aux")) {
			if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
				changeModel(SR_MODEL_TYPE.AUX);
				return VoiceCommon.returnSucceed(true, "");
			}
			if (Constant.projecId == Constant.PROJECT_ID.ZT_B11B) {
				if (!SceneStatus.getAUXIsConnection(mContext)) {
					return VoiceCommon.returnSucceed(false, "请先连接AUX");
				}
				changeModel(SR_MODEL_TYPE.AUX);
				return VoiceCommon.returnSucceed(true, "");
			} else {
				return VoiceCommon.returnSucceed(false, "没有AUX功能");
			}
		}
		if (!mVoiceMusic.getSong().equals("")) {
			searchSongBroadcast(mVoiceMusic);
			return VoiceCommon.returnSucceed(true, "");
		}
		if (mVoiceMusic.getOperation().equals("PLAY")
				&& (mVoiceMusic.getSource().equals("") || mVoiceMusic.getSong().equals(""))) {
			// 音乐自动播放
			Log.d("PlatformAdapterClient", "音乐自动播放广播");
			if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
				openApp(ACTIONS.SR_OS_ACTION,Constant.SR_LAUNCHER_MUSIC);
			}else{
				Intent mIntent = new Intent();
				mIntent.setAction(ACTIONS.SR_MEDIA_ACTION);
				mIntent.putExtra("commandCode", Constant.SR_MUSIC_MUSIC_PLAY);
				SerialService.getInstance().sendBroadcast(mIntent);
			}
			return VoiceCommon.returnSucceed(true, "");
		}
		return null;
	}

	@Override
	public String handleBluetoothPhone(String recognizerResult) {
		// TODO Auto-generated method stub
		return null;
	}

	private String falseMemo = "";

	@Override
	public String handleApp(String recognizerResult) {
		falseMemo = "";
		Log.d(Constant.DEBUG_TAG, "handleApp:" + recognizerResult);
		VoiceApp voiceAppEnt = new VoiceApp(recognizerResult);
		if (mContext == null || voiceAppEnt.getOperation() == "") {
			return VoiceCommon.returnSucceed(false, "抱歉，无法执行操作");
		}
		
		// 优先处理车机内置，比如usb,音乐,视频，图片，歌曲，电台，广播，收音机，收音机
		int handleSelfState = -1;
		handleSelfState = this.handleSelfMachine(voiceAppEnt);
		Log.d("hanadd", handleSelfState + "," + falseMemo);
		if (handleSelfState == 0) {
			return VoiceCommon.returnSucceed(false, falseMemo);
		} else if (handleSelfState == 1) {
			// 成功执行指令发送
			return VoiceCommon.returnSucceed(true, "");
		} else {
			// 未执行指令发送，交给下面安装的应用
			// return this.handleSelfMachine(voiceAppEnt);
			boolean isHas = false;
			List<PackageInfo> packages = mContext.getPackageManager()
					.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
			Log.d("hanadd", ""+packages.size());
			if (packages == null || packages.size() <= 0) {
				return VoiceCommon.returnSucceed(false, "没有找到"+voiceAppEnt.getName());
			}
			try {
				for (int i = 0; i < packages.size(); i++) {
					PackageInfo packageInfo = packages.get(i);
					String appName = packageInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
					if (appName.equals(voiceAppEnt.getName())) {
						isHas  = true;
						if (voiceAppEnt.getOperation().equals("LAUNCH")) {
							// 打开app
							Log.d(Constant.DEBUG_TAG, "app package name:" + packageInfo.packageName);
							Log.d(Constant.DEBUG_TAG, "app name:" + appName);
							try {
								Intent intent = mContext.getPackageManager()
										.getLaunchIntentForPackage(packageInfo.packageName);
								mContext.startActivity(intent);
								break;// 如果有多个相同的应用名称则只执行一次
							} catch (Exception e) {
								e.printStackTrace();
								return VoiceCommon.returnSucceed(false, appName + "，打开失败");
							}
						} else if (voiceAppEnt.getOperation().equals("EXIT")) {
							// 关闭app
							return VoiceCommon.returnSucceed(false, "不支持关闭"+voiceAppEnt.getName());
						}
					}
				}
				if(!isHas){
					return VoiceCommon.returnSucceed(false, "没有找到"+voiceAppEnt.getName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return VoiceCommon.returnSucceed(true, "");
	}

	// 处理自带app ，比如usb,ipod,sd，优盘，蓝牙，蓝牙音乐，电话本，电话簿，通话记录，音乐,视频，图片，歌曲，电台，广播，收音机，收音机
	private int handleSelfMachine(VoiceApp voiceAppEnt) {
		int sendstate = -1;

		if (voiceAppEnt.getName().length() > 0l) {
			String appOp = voiceAppEnt.getOperation();
			if(!appOp.equals("LAUNCH")){
				
			}
			String appName = voiceAppEnt.getName();
			
			//先过滤不处理的应用
			if(Constant.SR_NAME_APP_NO.contains(appName)){
				falseMemo = "无"+appName+"功能";
				return sendstate = -1;
			}
			appName = appName.toLowerCase();
			Log.d("vtest", "appName:ddd"+appName);
			if (Constant.SR_NAME_APP_SELFMACHINE.contains(appName)) {
				Log.d("vtest", "appName:"+appName);
				if (appName.equals("u盘") || appName.equalsIgnoreCase("usb") || appName.equals("多媒体")
						|| appName.equals("媒体") || appName.equals("音乐")|| appName.equals("音频")|| appName.equals("优盘")) {
					Log.d("PlatformAdapterClient", "in_selfmachine_usb");
					if(appOp.equals("LAUNCH")){
							//if (SceneStatus.getUIsConnection(mContext)) {

						if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
							openApp(ACTIONS.SR_OS_ACTION,Constant.SR_LAUNCHER_MUSIC);
							sendstate = 1;
						}else if(Constant.projecId!=Constant.PROJECT_ID.ZT_B11F){
							voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_MUSIC);
							sendstate = 1;
								/*voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PLAY);
								sendstate = 1;*/
							}else {
							if(SceneStatus.getUIsConnection(mContext)){
								changeModel(SR_MODEL_TYPE.USB);
							}else if(SceneStatus.getU2IsConnection(mContext)){
								changeModel(SR_MODEL_TYPE.USB2);
							}else if(SceneStatus.getU3IsConnection(mContext)){
								changeModel(SR_MODEL_TYPE.USB3);
							}else{
								sendstate = 0;
								falseMemo = "请插入U盘";
								return sendstate;
							}
						}
						//	} else {
								//sendstate = 0;
								//falseMemo = "请插入U盘";
						//	}
					}else	if(appOp.equals("EXIT")){
						//关闭音乐
						voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PAUSE);	
						sendstate = 1;
					}
				} else if (appName.equals("电台") || appName.equals("收音机") || appName.equals("广播")) {
					
					changeModel(SR_MODEL_TYPE.RADIO);
					sendstate = 1;
				} else if (appName.equals("导航") || appName.equals("地图")) {
					Log.d("vsource", "daohang");
						changeModel(SR_MODEL_TYPE.NAV);
						sendstate = 1;
				} else if (appName.equals("视频")) {
					if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
						openApp(ACTIONS.SR_OS_ACTION,Constant.SR_LAUNCHER_VIDEO);
						sendstate = 1;
					}else{
						voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_VIDEO_PLAY);
						sendstate = 1;
					}
				} else if (appName.equals("播放器")) {
					if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
						openApp(ACTIONS.SR_OS_ACTION,Constant.SR_LAUNCHER_VIDEO);
						sendstate = 1;
					}
				} else if (appName.equals("图片")||appName.equals("相册")) {
					if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
						openApp(ACTIONS.SR_OS_ACTION,Constant.SR_LAUNCHER_IMAGE);
						sendstate = 1;
					}else{
					voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_IMAGE_PLAY);
					sendstate = 1;
					}
				} else if (appName.equals("sd卡") || appName.equals("SD卡")) {
					changeModel(SR_MODEL_TYPE.SD);
					sendstate = 1;
				} else if (appName.equalsIgnoreCase("aux")||appName.equalsIgnoreCase("auxin")) {
					if(Constant.projecId == PROJECT_ID.ZT_B11F_IMAX6){
						changeModel(SR_MODEL_TYPE.AUX);
						sendstate = 1;
					}else if (Constant.projecId == PROJECT_ID.ZT_B11B) {
					
						if (SceneStatus.getAUXIsConnection(mContext)) {	
							changeModel(SR_MODEL_TYPE.AUX);
							sendstate = 1;
						} else {
							sendstate = 0;
							falseMemo = "请先连接AUX";
						}
					} else {
						sendstate = 0;
						falseMemo = "没有AUX功能";
					}
				} else if (appName.equals("蓝牙音乐")) {
					if (SceneStatus.getBtIsConnection(mContext)) {
						changeModel(SR_MODEL_TYPE.BLUTOOTH);
						sendstate = 1;
					} else {
						sendstate = 0;
						falseMemo = "请先连接蓝牙";
					}
				} else if (appName.equalsIgnoreCase("ipod")) {
					Log.d("PlatformAdapterClient", "in_ipod");
					if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
						changeModel(SR_MODEL_TYPE.IPOD);
						sendstate = 1;
					} else{
						if (SceneStatus.getIpodIsConnection(mContext)) {
								changeModel(SR_MODEL_TYPE.IPOD);
								sendstate = 1;
						}else{
							sendstate = 0;
							falseMemo = "请先连接ipod";
						}
					}
					
				}else if(appName.equals("设置")){
					if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
						voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_OS_SET);
						sendstate = 1;
					}
				} else if (appName.equals("电话本") || appName.equals("通讯录") || appName.equals("电话薄")|| appName.equals("电话簿")) {
					if (SceneStatus.getBtIsConnection(mContext)) {
						voiceHandlerSendBroadcast(ACTIONS.SR_BLUETOOTHPHONE_ACTION, Constant.SR_PHONE_CONTACTS);
						sendstate = 1;
					} else {
						sendstate = 0;
						falseMemo = "请先连接蓝牙";
					}
				} else if (appName.equals("拨号板") || appName.equals("拨号盘")) {
					if (SceneStatus.getBtIsConnection(mContext)) {
						voiceHandlerSendBroadcast(ACTIONS.SR_BLUETOOTHPHONE_ACTION, Constant.SR_PHONE_DIAL);
						sendstate = 1;
					} else {
						sendstate = 0;
						falseMemo = "请先连接蓝牙";
					}
				} else if (appName.equals("电话") || appName.equals("蓝牙电话")) {
					if (SceneStatus.getBtIsConnection(mContext)) {
						voiceHandlerSendBroadcast(ACTIONS.SR_BLUETOOTHPHONE_ACTION, Constant.SR_PHONE_DIAL);
						sendstate = 1;
					} else {
						sendstate = 0;
						falseMemo = "请先连接蓝牙";
					}
				} else if (appName.equals("通话记录")) {
					if (SceneStatus.getBtIsConnection(mContext)) {
						voiceHandlerSendBroadcast(ACTIONS.SR_BLUETOOTHPHONE_ACTION, Constant.SR_PHONE_HISTORYCALL);
						sendstate = 1;
					} else {
						sendstate = 0;
						falseMemo = "请先连接蓝牙";
					}
				} else if (appName.equals("蓝牙")) {
					voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_BT);
					sendstate = 1;
				} else if (appName.equals("车辆信息")) {
					voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_CARINFOR);
					sendstate = 1;
				}  else if (appName.equals("胎压")||appName.equals("胎压界面")) {
					voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_TIRE);
					sendstate = 1;
				} else if (appName.equals("应用")) {
					voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_APP);
					sendstate = 1;
				} else if (appName.equals("手机互联")||appName.equals("亿连")) {
						changeModel(SR_MODEL_TYPE.PHONE_LINK);
						sendstate = 1;
					/*
					 * if(Constant.projecId == Constant.PROJECT_ID.ZT_B11B ){
					 * try { Intent intent =
					 * mContext.getPackageManager().getLaunchIntentForPackage(
					 * "net.easyconn"); mContext.startActivity(intent); } catch
					 * (Exception e) { e.printStackTrace(); sendstate = 0;
					 * falseMemo = "打开失败"; }
					 * changeModel(SR_MODEL_TYPE.PHONE_LINK); }else
					 * if(Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C){
					 * changeModel(SR_MODEL_TYPE.PHONE_LINK); }
					 */

				} else if (appName.equals("空气质量信息显示")||appName.equals("pm二点五信息显示")) {
					if(voiceAppEnt.getOperation().equalsIgnoreCase("LAUNCH")){
						voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_PM);
						sendstate = 1;
					}else if(voiceAppEnt.getOperation().equalsIgnoreCase("EXIT")){
						voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_PM_CLOSE);
						sendstate = 1;
					}
				}
				
			}
		}

		return sendstate;
	}

	private void openApp(String srOsAction, int srLauncher) {
		voiceHandlerSendBroadcast(srOsAction,srLauncher);
	}

	public boolean isNumericN(String str) {
		if (str == null || str.length() <= 0) {
			return false;
		}
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String handleAirControl(String recognizerResult) {
		Log.d("空调", "in_handleAircontrol");
		Log.d(Constant.DEBUG_TAG, "handleAirControl:" + recognizerResult);
		VoiceAirControl mVoiceAirControl = new VoiceAirControl(recognizerResult);
		// List<Integer> tomcuControl = new ArrayList<Integer>(4);
		// 改用新协议裸发
		int num1 = 0x14;
		int num2 = 0x01;
		// 座椅加热
		if (mVoiceAirControl.getDevice().equals("座椅")) {
			if (Constant.projecId == PROJECT_ID.ZT_B11A_C) {
				//return VoiceCommon.returnSucceed(false, "座椅不支持语音控制功能");
			}
			if ((mVoiceAirControl.getMode().equals("制热") || mVoiceAirControl.getMode().equals("送风"))) {
				if (Constant.projecId == PROJECT_ID.ZT_B11A_C) {
					//return VoiceCommon.returnSucceed(false, "座椅不支持语音控制功能");
				}
				num1 = 0x50;
				num2 = 0x01;
				int num3 = 0xFF;
				int leftSeatAirControl = 0xFF;// 1，2，3 通风等级
				int rightSeatAirControl = 0xFF;// 1，2，3 通风等级
				int leftSeatHeat = 0xFF;// 1，2，3 加热等级
				int rightSeatHeat = 0xFF;// 1，2，3 加热等级
				int num8 = 0xFF;
				int num9 = 0x02;// 表示语音控制 0x01按键控制
				int whitchSeat = 0x01;// 1左座椅，2右座椅
				int num11 = 0xFF;//

				if (mVoiceAirControl.getTarget().equals("主驾")) {
					if (mVoiceAirControl.getMode().equals("制热")) {
						if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
							// 座椅加热没有开，只有档位设置，这里要先获取下当前档位值
							leftSeatHeat = CarInfor.carSeatStatus.getSeatLeftHeat();
							leftSeatHeat = (leftSeatHeat == 0) ? 1 : leftSeatHeat;
							if(isNumericN(mVoiceAirControl.getTemperatureLevel())){
								leftSeatHeat = Integer.parseInt(mVoiceAirControl.getTemperatureLevel());
							}
							if(Constant.projecId == PROJECT_ID.ZT_B11B){
								rightSeatAirControl = 0;
								leftSeatAirControl = 0;
							}
							
						} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
							leftSeatHeat = 0;
						}
					} else if (mVoiceAirControl.getMode().equals("送风")) {
						if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
							leftSeatAirControl = CarInfor.carSeatStatus.getSeatLeftAir();
							leftSeatAirControl = (leftSeatAirControl == 0) ? 1 : leftSeatAirControl;
							if (isNumericN(mVoiceAirControl.getFanSpeed())) {
								leftSeatAirControl = Integer.parseInt(mVoiceAirControl.getFanSpeed());
							}
							if(Constant.projecId == PROJECT_ID.ZT_B11B){
								rightSeatHeat = 0;
								leftSeatHeat = 0;
							}
						} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
							leftSeatAirControl = 0;
						}
					}
				} else if (mVoiceAirControl.getTarget().equals("副驾")) {
					if (mVoiceAirControl.getMode().equals("制热")) {
						if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
							rightSeatHeat = CarInfor.carSeatStatus.getSeatRightHeat();
							rightSeatHeat = (rightSeatHeat == 0) ? 1 : rightSeatHeat;
							if(isNumericN(mVoiceAirControl.getTemperatureLevel())){
								rightSeatHeat = Integer.parseInt(mVoiceAirControl.getTemperatureLevel());
							}
							if(Constant.projecId == PROJECT_ID.ZT_B11B){
								rightSeatAirControl = 0;
								leftSeatAirControl = 0;
							}
						} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
							rightSeatHeat = 0;
						}
					} else if (mVoiceAirControl.getMode().equals("送风")) {
						if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
							rightSeatAirControl = CarInfor.carSeatStatus.getSeatRightAir();
							rightSeatAirControl = (rightSeatAirControl == 0) ? 1 : rightSeatAirControl;
							if (isNumericN(mVoiceAirControl.getFanSpeed())) {
								rightSeatAirControl = Integer.parseInt(mVoiceAirControl.getFanSpeed());
							}
							if(Constant.projecId == PROJECT_ID.ZT_B11B){
								rightSeatHeat = 0;
								leftSeatHeat = 0;
							}
						} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
							rightSeatAirControl = 0;
						}
					}
				}else{
					//如果没有表明主驾还是副驾则统一默认为主驾
					if (mVoiceAirControl.getMode().equals("制热")) {
						if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
							// 座椅加热没有开，只有档位设置，这里要先获取下当前档位值
							leftSeatHeat = CarInfor.carSeatStatus.getSeatLeftHeat();
							leftSeatHeat = (leftSeatHeat == 0) ? 1 : leftSeatHeat;
							if(Constant.projecId == PROJECT_ID.ZT_B11A_C||Constant.projecId == PROJECT_ID.ZT_B11F_IMAX6){
								//如果是a-c 则两个都打开
								rightSeatHeat = CarInfor.carSeatStatus.getSeatRightHeat();
								rightSeatHeat = (rightSeatHeat == 0) ? 1 : rightSeatHeat;
							}
							if(isNumericN(mVoiceAirControl.getTemperatureLevel())){
								leftSeatHeat = Integer.parseInt(mVoiceAirControl.getTemperatureLevel());
								if(Constant.projecId == PROJECT_ID.ZT_B11A_C||Constant.projecId == PROJECT_ID.ZT_B11F_IMAX6){
									//如果是a-c b11f 则两个都打开
									rightSeatHeat = leftSeatHeat;
								}
							}
							if(Constant.projecId == PROJECT_ID.ZT_B11B){
								rightSeatAirControl = 0;
								leftSeatAirControl= 0;
							}
						} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
							leftSeatHeat = 0;
							if(Constant.projecId == PROJECT_ID.ZT_B11A_C||Constant.projecId == PROJECT_ID.ZT_B11F_IMAX6){
								//如果是a-c  b11f 则两个都关闭
								rightSeatHeat = leftSeatHeat;
							}
						}
					} else if (mVoiceAirControl.getMode().equals("送风")) {
						if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
							leftSeatAirControl = CarInfor.carSeatStatus.getSeatLeftAir();
							leftSeatAirControl = (leftSeatAirControl == 0) ? 1 : leftSeatAirControl;
							if (isNumericN(mVoiceAirControl.getFanSpeed())) {
								leftSeatAirControl = Integer.parseInt(mVoiceAirControl.getFanSpeed());
							}
							if(Constant.projecId == PROJECT_ID.ZT_B11B){
								rightSeatHeat = 0;
								leftSeatHeat = 0;
							}
						} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
							leftSeatAirControl = 0;
						}
					}
				}
				SerialService.getInstance().sendData(15, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x11, (byte) num1,
						(byte) num2, (byte) num3, (byte) leftSeatAirControl, (byte) rightSeatAirControl,
						(byte) leftSeatHeat, (byte) rightSeatHeat, (byte) num8, (byte) num9, (byte) whitchSeat,
						(byte) num11);
				Log.d("vsendmcu",
						"seat: num1:" + num1 + ";num2:" + num2 + ";num3:" + num3 + ";leftSeatAirControl:"
								+ leftSeatAirControl + ";rightSeatAirControl:" + rightSeatAirControl + ";leftSeatHeat:"
								+ leftSeatHeat + ";rightSeatHeat:" + rightSeatHeat + ";num8:" + num8 + ";num9:" + num9
								+ ";whitchSeat:" + whitchSeat + ";num11:" + num11);
				return VoiceCommon.returnSucceed(true, "");
			}
		}
		if ((mVoiceAirControl.getMode().equals("制热") || mVoiceAirControl.getMode().equals("送风"))
				&& (mVoiceAirControl.getTarget().equals("主驾") || mVoiceAirControl.getTarget().equals("副驾"))) {
			if (Constant.projecId == PROJECT_ID.ZT_B11A_C) {
				//return VoiceCommon.returnSucceed(false, "座椅不支持语音控制功能");
			}
			num1 = 0x50;
			num2 = 0x01;
			int num3 = 0xFF;
			int leftSeatAirControl = 0xFF;// 1，2，3 通风等级
			int rightSeatAirControl = 0xFF;// 1，2，3 通风等级
			int leftSeatHeat = 0xFF;// 1，2，3 加热等级
			int rightSeatHeat = 0xFF;// 1，2，3 加热等级
			int num8 = 0xFF;
			int num9 = 0x02;// 表示语音控制 0x01按键控制
			int whitchSeat = 0x01;// 1左座椅，2右座椅
			int num11 = 0xFF;//

			if (mVoiceAirControl.getTarget().equals("主驾")) {
				if (mVoiceAirControl.getMode().equals("制热")) {
					if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
						// 座椅加热没有开，只有档位设置，这里要先获取下当前档位值
						leftSeatHeat = CarInfor.carSeatStatus.getSeatLeftHeat();
						leftSeatHeat = (leftSeatHeat == 0) ? 1 : leftSeatHeat;
						if(isNumericN(mVoiceAirControl.getTemperatureLevel())){
							leftSeatHeat = Integer.parseInt(mVoiceAirControl.getTemperatureLevel());
						}
						if(Constant.projecId == PROJECT_ID.ZT_B11B){
							rightSeatAirControl = 0;
							leftSeatAirControl = 0;
						}
					} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
						leftSeatHeat = 0;
					}
				} else if (mVoiceAirControl.getMode().equals("送风")) {
					if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
						leftSeatAirControl = CarInfor.carSeatStatus.getSeatLeftAir();
						leftSeatAirControl = (leftSeatAirControl == 0) ? 1 : leftSeatAirControl;
						if (isNumericN(mVoiceAirControl.getFanSpeed())) {
							leftSeatAirControl = Integer.parseInt(mVoiceAirControl.getFanSpeed());
						}
						if(Constant.projecId == PROJECT_ID.ZT_B11B){
							rightSeatHeat = 0;
							leftSeatHeat = 0;
						}
					} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
						leftSeatAirControl = 0;
					}
				}
			} else if (mVoiceAirControl.getTarget().equals("副驾")) {
				if (mVoiceAirControl.getMode().equals("制热")) {
					if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
						rightSeatHeat = CarInfor.carSeatStatus.getSeatRightHeat();
						rightSeatHeat = (rightSeatHeat == 0) ? 1 : rightSeatHeat;
						if(isNumericN(mVoiceAirControl.getTemperatureLevel())){
							rightSeatHeat = Integer.parseInt(mVoiceAirControl.getTemperatureLevel());
						}
						if(Constant.projecId == PROJECT_ID.ZT_B11B){
							rightSeatAirControl = 0;
							leftSeatAirControl = 0;
						}
					} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
						rightSeatHeat = 0;
					}
				} else if (mVoiceAirControl.getMode().equals("送风")) {
					if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
						rightSeatAirControl = CarInfor.carSeatStatus.getSeatRightAir();
						rightSeatAirControl = (rightSeatAirControl == 0) ? 1 : rightSeatAirControl;
						if (isNumericN(mVoiceAirControl.getFanSpeed())) {
							rightSeatAirControl = Integer.parseInt(mVoiceAirControl.getFanSpeed());
						}
						if(Constant.projecId == PROJECT_ID.ZT_B11B){
							rightSeatHeat = 0;
							leftSeatHeat = 0;
						}
					} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
						rightSeatAirControl = 0;
					}
				}
			}
			SerialService.getInstance().sendData(15, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x11, (byte) num1,
					(byte) num2, (byte) num3, (byte) leftSeatAirControl, (byte) rightSeatAirControl,
					(byte) leftSeatHeat, (byte) rightSeatHeat, (byte) num8, (byte) num9, (byte) whitchSeat,
					(byte) num11);
			Log.d("vsendmcu",
					"seat: num1:" + num1 + ";num2:" + num2 + ";num3:" + num3 + ";leftSeatAirControl:"
							+ leftSeatAirControl + ";rightSeatAirControl:" + rightSeatAirControl + ";leftSeatHeat:"
							+ leftSeatHeat + ";rightSeatHeat:" + rightSeatHeat + ";num8:" + num8 + ";num9:" + num9
							+ ";whitchSeat:" + whitchSeat + ";num11:" + num11);
			return VoiceCommon.returnSucceed(true, "");
		}
		int voiceActiveControl = 1;// 语音激活状态
		int acSwitchRequest = 0xFF;// 压缩机开关请求1：AC OFF (压缩机关闭/制热) 2：AC ON
									// (压缩机打开/制冷)
		if (mVoiceAirControl.getMode().equals("制冷")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				acSwitchRequest = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				acSwitchRequest = 1;
			}
		}
		if (mVoiceAirControl.getMode().equals("制热")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				acSwitchRequest = 1;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				acSwitchRequest = 2;
			}
		}

		int airCondition = 0xFF;// 空调状态 1关，2开
		if(Constant.projecId==Constant.PROJECT_ID.ZT_B11A_C){
			//airCondition = 2;	
		}
		if (mVoiceAirControl.getDevice().equals("空调")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("OPEN")) {
				airCondition = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				airCondition = 1;
			}
		}



		int autoStatus = 0xFF;// 自动模式 1关，2开
		if (mVoiceAirControl.getMode().equals("自动")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				autoStatus = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				autoStatus = 1;
			}
		}

		int cycleStaus = 0xFF;// 内外循环状态 1，外，2，内
		if (mVoiceAirControl.getMode().equals("内循环")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				cycleStaus = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				cycleStaus = 1;
			}
		}
		if (mVoiceAirControl.getMode().equals("外循环")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				cycleStaus = 1;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				cycleStaus = 2;
			}
		}

		int frontDefrost = 0xFF;// 前除霜 1关，2开
		if (mVoiceAirControl.getMode().equals("前除霜")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				frontDefrost = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				frontDefrost = 1;
			}
		}
		if (mVoiceAirControl.getMode().equals("除霜")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				frontDefrost = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				frontDefrost = 1;
			}
		}
		int reardefrost = 0xFF;// 后除霜 1关，2开
		if (mVoiceAirControl.getMode().equals("后除霜")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				reardefrost = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				reardefrost = 1;
			}
		}

		if (mVoiceAirControl.getMode().equals("一键除霜")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				frontDefrost = 2;
				reardefrost = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				frontDefrost = 1;
				reardefrost = 1;
			}
		}
		int acMax = 0xFF;// 最大制冷 1关，2开
		if (mVoiceAirControl.getMode().equals("制冷")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				if (mVoiceAirControl.getFanSpeed().equals("最大")) {
					acMax = 2;
				}
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				acMax = 1;
			}
		}

		int hotMax = 0xFF;// 最大制热 1关，2开
		if (mVoiceAirControl.getMode().equals("制热")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				if (mVoiceAirControl.getFanSpeed().equals("最大")) {
					hotMax = 2;
				}
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				hotMax = 1;
			}
		}

		int temperatureRequest = 0xFF;// 温度增量 1up, 2down，是否有温度增加多少
		if (mVoiceAirControl.getTemperature().equals("+")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				temperatureRequest = 1;
			}
		} else if (mVoiceAirControl.getTemperature().equals("-")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				temperatureRequest = 2;
			}
		}

		int windSpeedRequest = 0xFF;// 风速增量 1up, 2down
		if (mVoiceAirControl.getFanSpeed().equals("+")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				windSpeedRequest = 1;
			}
		} else if (mVoiceAirControl.getFanSpeed().equals("-")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				windSpeedRequest = 2;
			}
		}

		int hvacModel = 0xFF;// 空调模式 0:default 1:Face (吹脸) 2:Face and Foot(吹脸和脚)
								// 3:Foot (吹脚)4:Foot and Defrost(吹脚和前除霜) 5:mode
								// active 6: Defrost(前除霜)
		if (mVoiceAirControl.getAirflowDirection().equals("面")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				hvacModel = 1;
			}
		} else if (mVoiceAirControl.getAirflowDirection().equals("吹面吹脚")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				hvacModel = 2;
			}
		} else if (mVoiceAirControl.getAirflowDirection().equals("脚")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				hvacModel = 3;
				if (mVoiceAirControl.getMode().equalsIgnoreCase("除霜")) {
					hvacModel = 4;
				}
			}
		} else if (mVoiceAirControl.getAirflowDirection().equals("吹脚吹窗")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				hvacModel = 4;
			}
		}else if (mVoiceAirControl.getAirflowDirection().equals("脚")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				hvacModel = 3;
			}
		} else if (mVoiceAirControl.getMode().equals("吹前窗")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				frontDefrost = 2;
				hvacModel = 6;
			}
		}

		int dualStatus = 0xFF;// 双联动 1关，2开
		if (mVoiceAirControl.getMode().equals("双区")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				dualStatus = 1;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				dualStatus = 2;
			}
		}

		VoiceSub temperatureSub = mVoiceAirControl.getTemperatureSub();
		int dataTypeNum = 0xFF;// 众泰18-32 吉利17-32 确定下温度值转换方式
		if (isNumericN(mVoiceAirControl.getTemperature())) {
			dataTypeNum = Integer.parseInt(mVoiceAirControl.getTemperature());
			dataTypeNum = dataTypeNum * 2;
		} else if (mVoiceAirControl.getTemperature().length() > 0) {
			if (mVoiceAirControl.getOperation().equals("SET")) {
				if (temperatureSub != null) {
					int offsetValue = Integer.parseInt(temperatureSub.getOffset());
					if (mVoiceAirControl.getTarget().equals("副驾")) {
						dataTypeNum = CarInfor.carAirStatus.getRightTemperature();
						Log.d("voicetemp", "wen du right is " + dataTypeNum);
					}
					if (mVoiceAirControl.getTarget().equals("主驾") || mVoiceAirControl.getTarget().equals("")) {
						dataTypeNum = CarInfor.carAirStatus.getLeftTemperature();
						Log.d("voicetemp", "wen du left is " + dataTypeNum);
					}
					if (offsetValue > 0) {
						offsetValue = offsetValue * 2;
						if (temperatureSub.getDirect().equals("-")) {
							dataTypeNum -= offsetValue;
							// dataTypeNum = CarInfor.
						} else if (temperatureSub.getDirect().equals("+")) {
							dataTypeNum += offsetValue;
						}
					}
				}
			}
			Log.d("voicetemp", "wen du is " + dataTypeNum);
		}

		int selfTestActive = 0xFF;// 自检状态
		int fanSpeedLevel = 0xFF;// 风量等级 1-7
		if (isNumericN(mVoiceAirControl.getFanSpeed())) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				fanSpeedLevel = Integer.parseInt(mVoiceAirControl.getFanSpeed());
				if (fanSpeedLevel >= 7) {
					fanSpeedLevel = 7;
				}
			}
		} else {
			VoiceSub fansub = mVoiceAirControl.getFanSpeedSub();
			if (fansub != null) {
				int offsetValue = Integer.parseInt(fansub.getOffset());
				fanSpeedLevel = CarInfor.carAirStatus.getFanSpeed();
				if (offsetValue > 0) {
					if (temperatureSub.getDirect().equals("-")) {
						fanSpeedLevel -= offsetValue;
					} else if (temperatureSub.getDirect().equals("+")) {
						fanSpeedLevel += offsetValue;
					}
				}
			} else if (mVoiceAirControl.getFanSpeed().equals("高风")) {
				if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
					fanSpeedLevel = 7;
				}
			} else if (mVoiceAirControl.getFanSpeed().equals("低风")) {
				if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
					fanSpeedLevel = 1;
				}
			} else if (mVoiceAirControl.getFanSpeed().equals("最大")) {
				if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
					fanSpeedLevel = 7;
				}
			} else if (mVoiceAirControl.getFanSpeed().equals("最小")) {
				if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
					fanSpeedLevel = 1;
				}
			}

		}

		int driverSeat = 0xFF;// 多区空调区域0: driver seat 1: passenger seat 2: back
								// seat
		if (mVoiceAirControl.getTarget().equals("主驾")) {
			if (Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C) {
				return VoiceCommon.returnSucceed(false, "空调不支持语音分区控制功能");
			}
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				driverSeat = 0;
			}
		}
		if (mVoiceAirControl.getTarget().equals("副驾")) {
			if (Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C) {
				return VoiceCommon.returnSucceed(false, "空调不支持语音分区控制功能");
			}
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				driverSeat = 1;
			}
		}
		if (mVoiceAirControl.getTarget().equals("后空调")) {
		//	if (Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C||Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C) {
			//	return VoiceCommon.returnSucceed(false, "后空调不支持语音控制功能");
		//	}
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				driverSeat = 2;
			}
			return VoiceCommon.returnSucceed(false, "后空调不支持语音控制功能");
		}

		int tempeleElecAirCondition = 0xFF;// 电动空调温度档位 15个
		if (isNumericN(mVoiceAirControl.getTemperatureLevel())) {
			Log.d("vsend", "level " + mVoiceAirControl.getTemperatureLevel());
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				tempeleElecAirCondition = Integer.parseInt(mVoiceAirControl.getTemperatureLevel());
				if (tempeleElecAirCondition >= 15) {
					tempeleElecAirCondition = 15;
				}
			}
		} else {
			VoiceSub tLevel = mVoiceAirControl.getTemperatureLevelSub();
			if (tLevel != null) {
				// 温度档位增加或减少
				int offsetValue = Integer.parseInt(tLevel.getOffset());
				tempeleElecAirCondition = CarInfor.carAirStatus.getFanSpeed();// 缺少温度档位获取
				if (offsetValue > 0) {
					if (temperatureSub.getDirect().equals("-")) {
						if (Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C) {
							return VoiceCommon.returnSucceed(false, "不支持温度档位减少几");
						}
						tempeleElecAirCondition -= offsetValue;
					} else if (temperatureSub.getDirect().equals("+")) {
						if (Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C) {
							return VoiceCommon.returnSucceed(false, "不支持温度档位增加几");
						}
						tempeleElecAirCondition += offsetValue;
					}
				}
			}
		}
		int heating = 0xFF;// 座椅加热
		int airing = 0xFF;// 座椅通风
		int autoCyle = 0xFF;// 自动内外循环 1 noactive, 2 active
		if (mVoiceAirControl.getMode().equals("内外循环自动")) {
			
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				autoCyle = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				autoCyle = 1;
			}
		}

		int anion = 0xFF;// 负离子功能开关 1 noactive, 2 active
		if (mVoiceAirControl.getMode().equals("空气净化")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				anion = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				anion = 1;
			}
		}

		int pm25 = 0xFF;// PM2.5显示开关 1 noactive, 2 active
		int frontHeat = 0xFF;// 前风挡加热 1 noactive, 2 active
		if (mVoiceAirControl.getMode().equals("前风挡制热")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				frontHeat = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				frontHeat = 1;
			}
		}
		int temperatureProvince = 0xFF;// 温度分区控制开关 1 noactive, 2 active
		if (mVoiceAirControl.getMode().equals("温度分区控制")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				temperatureProvince = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				temperatureProvince = 1;
			}
		}
		int data25 = 0xFF;

	/*	if(Constant.projecId==Constant.PROJECT_ID.ZT_B11A_C){
			//如果当前空调打开，则不再发，如果当前空调关闭要先发送开
			int nvalue = 0xFF;
			SerialService.getInstance().sendData(32, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x11, (byte) num1,
					(byte) num2, (byte) voiceActiveControl, (byte) nvalue, (byte) 2,(byte) nvalue,
					(byte) nvalue, (byte) nvalue, (byte) nvalue, (byte) nvalue, (byte) nvalue,
					(byte) nvalue, (byte) nvalue, (byte) nvalue, (byte) nvalue,
					(byte) nvalue, (byte) nvalue, (byte) nvalue, (byte) nvalue,
					(byte) nvalue, (byte) heating, (byte) nvalue, (byte) nvalue, (byte) nvalue,
					(byte) nvalue, (byte) nvalue, (byte) nvalue, (byte) data25);
			Log.d("vsendmcu","no value ==");
		}

		hand.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				int nnvalue = 0xFF;
				SerialService.getInstance().sendData(32, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x11, (byte) 0x14,
						(byte) 0x01, (byte) 1, (byte) nnvalue, (byte) nnvalue,(byte) nnvalue,
						(byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue,
						(byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue,
						(byte) 50, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue,
						(byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue,
						(byte) nnvalue, (byte) nnvalue, (byte) nnvalue, (byte) nnvalue);
				Log.d("vsendmcu","set air delay ==");
			}
		},91);*/
		
		
		Log.d("vsendmcu",
				"air: num1:" + num1 + ";num2:" + num2 + ";voiceActiveControl:" + voiceActiveControl
				+ ";acSwitchRequest:" + acSwitchRequest + ";airCondition:" + airCondition + ";autoStatus:"
				+ autoStatus + ";cycleStaus:" + cycleStaus + ";frontDefrost:" + frontDefrost + ";reardefrost:"
				+ reardefrost + ";acMax:" + acMax + ";hotMax:" + hotMax + ";temperatureRequest:"
				+ temperatureRequest + ";windSpeedRequest;" + windSpeedRequest + ";hvacModel;" + hvacModel
				+ ";dualStatus;" + dualStatus + ";dataTypeNum;" + dataTypeNum + ";selfTestActive;"
				+ selfTestActive + ";fanSpeedLevel;" + fanSpeedLevel + ";driverSeat;" + driverSeat
				+ ";tempeleElecAirCondition;" + tempeleElecAirCondition + ";heating;" + heating + ";airing;"
				+ airing + ";autoCyle;" + autoCyle + ";anion;" + anion + ";pm25;" + pm25 + ";frontHeat;"
				+ frontHeat + ";temperatureProvince;" + temperatureProvince + ";data25;" + data25);
		if(Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C||Constant.projecId == Constant.PROJECT_ID.ZT_B11F||
				Constant.projecId == Constant.PROJECT_ID.ZT_B11B||Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6||Constant.projecId == Constant.PROJECT_ID.ZT_B11A){
			//只有温度设定的地方要先判断，如果关闭空调，要先打开空调
			if(dataTypeNum != 0xFF){
				if(!CarInfor.carAirStatus.isAirConditionON()){
					CarAirStatus.OpenAir();
					sendAirControlDelay((byte) num1,
							(byte) num2, (byte) voiceActiveControl, (byte) acSwitchRequest, (byte) airCondition,(byte) autoStatus,
							(byte) cycleStaus, (byte) frontDefrost, (byte) reardefrost, (byte) acMax, (byte) hotMax,
							(byte) temperatureRequest, (byte) windSpeedRequest, (byte) hvacModel, (byte) dualStatus,
							(byte) dataTypeNum, (byte) selfTestActive, (byte) fanSpeedLevel, (byte) driverSeat,
							(byte) tempeleElecAirCondition, (byte) heating, (byte) airing, (byte) autoCyle, (byte) anion,
							(byte) pm25, (byte) frontHeat, (byte) temperatureProvince, (byte) data25);

					return VoiceCommon.returnSucceed(true, "");
				}
			}
		}
			SerialService.getInstance().sendData(32, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x11, (byte) num1,
					(byte) num2, (byte) voiceActiveControl, (byte) acSwitchRequest, (byte) airCondition,(byte) autoStatus,
					(byte) cycleStaus, (byte) frontDefrost, (byte) reardefrost, (byte) acMax, (byte) hotMax,
					(byte) temperatureRequest, (byte) windSpeedRequest, (byte) hvacModel, (byte) dualStatus,
					(byte) dataTypeNum, (byte) selfTestActive, (byte) fanSpeedLevel, (byte) driverSeat,
					(byte) tempeleElecAirCondition, (byte) heating, (byte) airing, (byte) autoCyle, (byte) anion,
					(byte) pm25, (byte) frontHeat, (byte) temperatureProvince, (byte) data25);
			SerialService.getInstance().voiceStop();	

		return VoiceCommon.returnSucceed(true, "");

	}
	private void sendAirControlDelay(
			final byte num1, final byte num2, final byte voiceActiveControl,
			final byte acSwitchRequest, final byte airCondition, final byte autoStatus,
			final byte cycleStaus, final byte frontDefrost, final byte reardefrost, final byte acMax,
			final byte hotMax, final byte temperatureRequest, final byte windSpeedRequest,
			final byte hvacModel, final byte dualStatus, final byte dataTypeNum,
			final byte selfTestActive, final byte fanSpeedLevel, final byte driverSeat,
			final byte tempeleElecAirCondition, final byte heating, final byte airing,
			final byte autoCyle, final byte anion, final byte pm25, final byte frontHeat,
			final byte temperatureProvince, final byte data25) {
		if(handler!=null){
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					SerialService.getInstance().sendData(32, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x11, (byte) num1,
							(byte) num2, (byte) voiceActiveControl, (byte) acSwitchRequest, (byte) airCondition,(byte) autoStatus,
							(byte) cycleStaus, (byte) frontDefrost, (byte) reardefrost, (byte) acMax, (byte) hotMax,
							(byte) temperatureRequest, (byte) windSpeedRequest, (byte) hvacModel, (byte) dualStatus,
							(byte) dataTypeNum, (byte) selfTestActive, (byte) fanSpeedLevel, (byte) driverSeat,
							(byte) tempeleElecAirCondition, (byte) heating, (byte) airing, (byte) autoCyle, (byte) anion,
							(byte) pm25, (byte) frontHeat, (byte) temperatureProvince, (byte) data25);

					Log.d("vsendmcu","has to delay ");
					SerialService.getInstance().voiceStop();	
				}
			}, 100);
		}
		
	}

	Handler hand = new Handler();
	private int getIntValue(String offset, int defaultValue) {
		int va = 0;
		try {
			va = Integer.parseInt(offset);
		} catch (Exception e) {
			e.printStackTrace();
			va = defaultValue;
		}
		return va;
	}

	@Override
	public String handleMessage(String recognizerResult) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String handleDoAction(String recognizerResult) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String handleCarControl(String recognizerResult) {
		
		VoiceCarControl carControl=new VoiceCarControl(recognizerResult); 
		if (carControl.getName().equals("天窗")) {
			if(Constant.projecId != Constant.PROJECT_ID.ZT_B11B&&Constant.projecId != Constant.PROJECT_ID.ZT_B11A_C){
				return VoiceCommon.returnSucceed(false, "不支持语音控制天窗");
			}
			int num1 = 0x17;
			int num2 = 0x01;
			int airWindow = 0x00; // 天窗状态 2开，3关
			if (carControl.getOperation().equalsIgnoreCase("OPEN")) {
				airWindow = 2;
			} else if (carControl.getOperation().equalsIgnoreCase("CLOSE")) {
				airWindow = 3;
			}
			Log.i("vsendmcu", "num1:" + num1 + ";num2:" + num2 + ";airWindow:"
					+ airWindow);
			SerialService.getInstance().sendData(7, (byte) 0x12, (byte) 0x00,
					(byte) 0x09, (byte) 0x11, (byte) num1, (byte) num2,
					(byte) airWindow);

		}
		return VoiceCommon.returnSucceed(true, "");
	}

	@Override
	public String handleVehicleInfo(String recognizerResult) {
		VoiceVehicleInfo vInfoEnt = new VoiceVehicleInfo(recognizerResult);
		if (vInfoEnt != null) {
			if (vInfoEnt.getName().equals("整体")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_CARINFOR);
			}
		}
		return VoiceCommon.returnSucceed(true, "");
	}

	public boolean changeModel(Constant.SR_MODEL_TYPE mType) {
		if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
			int value  = -1;
			switch(mType){
			case USB:
				value = Constant.SR_LAUNCHER_MUSIC;
			break;
		case BLUTOOTH:
			value = Constant.SR_LAUNCHER_BTMUSIC;
			break;
		case IPOD:
			value = Constant.SR_LAUNCHER_IPOD;
			break;
		case NAV:
			value = Constant.SR_LAUNCHER_NAV;
			break;
		case RADIO:
			value = Constant.SR_LAUNCHER_RADIO;
			break;
		case SD:
			value = Constant.SR_LAUNCHER_MUSIC;
			break;
		case AUX:
			value = Constant.SR_LAUNCHER_AUX;
		break;
		case PHONE_LINK:
			value = Constant.SR_LAUNCHER_PHONELINK;
			break;
		default:
			break;
			}
			openApp(ACTIONS.SR_OS_ACTION,value);
			
		}else{
		switch (mType) {
		case USB:
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x03);
			break;		
			case USB2:
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x17);
				Log.d("suochaov", "usb2 open");
			break;
			case USB3:
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x18);
			break;
		case BLUTOOTH:
			SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x0B);
			break;
		case IPOD:
			SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x07);
			break;
		case NAV:
			SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x0A);
			break;
		case RADIO:
			SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x01);
			break;
		case SD:
			SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x04);
			break;
		case AUX:
			SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x05);
			break;
		case PHONE_LINK:
			SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x10);
			break;
		default:
			break;
		}

		SerialService.getInstance().voiceStop();
		}
		return true;
	}

	private final int CMD_PLAY = 100;
	private final int CMD_PLAY_DELAY = 1000;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case CMD_PLAY:
				break;
			}
		}
	};
}
