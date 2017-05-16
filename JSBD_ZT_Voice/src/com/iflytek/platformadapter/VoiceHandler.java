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
 * by suochao ��������ʶ����
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
					Log.e(Constant.DEBUG_TAG, "������fmƵ�ʣ�" + iFreq);
					int iFreqH = iFreq / 100;
					int iFreqL = iFreq % 100;
					if (iFreq > 10800 || iFreq < 8750) {
						// ttsSpeak("Ƶ�ʳ�����Χ");
						flag = false;
						return VoiceCommon.returnSucceed(false, "Ƶ�ʳ�����Χ");
					} else {
						flag = true;
					}
					if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
						//�����imax6 ��Ҫ��һ�����������Ĺ㲥
						changeModel(SR_MODEL_TYPE.RADIO);
					}
					SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
							(byte) 0x01);
					SerialService.getInstance().sendData(8, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
							(byte) 0x05, (byte) 0x00, (byte) iFreqH, (byte) iFreqL);
					SerialService.getInstance().voiceStop();
				} else if (band.equals("am")) {
					Log.d(Constant.DEBUG_TAG, "oiooiououo��" + iFreq);
					try {
						iFreq = Integer.parseInt(sFreq);
					} catch (Exception e) {
						// ttsSpeak("Ƶ�ʳ�����Χ");
					}
					Log.d(Constant.DEBUG_TAG, "������amƵ�ʣ�" + iFreq);
					if (iFreq < 531 || iFreq > 1629) {
						// ttsSpeak("Ƶ�ʳ�����Χ");
						return VoiceCommon.returnSucceed(false, "Ƶ�ʳ�����Χ");
					}
					byte iFreqH = (byte) ((iFreq & 0xFF00) >> 8);
					byte iFreqL = (byte) (iFreq & 0x00FF);
					Log.d(Constant.DEBUG_TAG, "������amƵ�ʣ��߰�λ��" + iFreqH + ";" + "�Ͱ�λ��" + iFreqL);
					String tmp = String.format("%h   %h", iFreqH, iFreqL);
					Log.e(Constant.DEBUG_TAG, tmp);
					if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
						//�����imax6 ��Ҫ��һ�����������Ĺ㲥
						changeModel(SR_MODEL_TYPE.RADIO);
					}
					SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
							(byte) 0x01);
					SerialService.getInstance().sendData(8, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
							(byte) 0x05, (byte) 0x01, (byte) iFreqH, (byte) iFreqL);
					SerialService.getInstance().voiceStop();
				}else{
					//ָֻ��Ƶ�ʣ�û��ָ����Ƶ���ǵ���
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
				// ttsSpeak("Ƶ�ʳ�����Χ");
				SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
						(byte) 0x01);
			}
		} else {
			// ttsSpeak("Ƶ�ʳ�����Χ");
			if (band.equals("am")) {
				Log.d(Constant.DEBUG_TAG, "���� am");
		
				SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
						(byte) 0x01);
				if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
					//�����imax6 ��Ҫ��һ�����������Ĺ㲥
					changeModel(SR_MODEL_TYPE.RADIO);
				}

				SerialService.getInstance().sendData(8, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x05, (byte) 0x01, (byte) 0xff, (byte) 0xff);
				if(vRadio.getRawText().contains("��������")){
					SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
							(byte) 0x03);
					return null;
				}
			
			}
			if (band.equals("fm")) {
				Log.d(Constant.DEBUG_TAG, "����  fm");
		
				SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
						(byte) 0x01);
				if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
					//�����imax6 ��Ҫ��һ�����������Ĺ㲥
					changeModel(SR_MODEL_TYPE.RADIO);
				}
				SerialService.getInstance().sendData(8, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x05, (byte) 0x00, (byte) 0xff, (byte) 0xff);
				if(vRadio.getRawText().contains("��Ƶ����")){
					SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
							(byte) 0x03);
					return null;
				}
		
			}
		}

		Log.d(Constant.DEBUG_TAG, "����   " + sFreq + "       " + band + "   " + iFreq);
		return null;
	}

	private String setRadioByCode(String sFreq) {
		String memo = "";
		if (VoiceCommon.isNumeric(sFreq)) {
			int iFreq = (int) (Double.parseDouble(sFreq) * 100);
			if (iFreq > 10800 || iFreq < 8750) {
				//���ڵ�Ƶ��Χ��,���ж��Ƿ��ڵ�����Χ��
				iFreq = Integer.parseInt(sFreq);
				if (iFreq < 531 || iFreq > 1629) {
					//���ڵ�����Χ��
					memo = "Ƶ�ʳ�����Χ";
				}else{
					//�趨����
					byte iFreqH = (byte) ((iFreq & 0xFF00) >> 8);
					byte iFreqL = (byte) (iFreq & 0x00FF);
					Log.d(Constant.DEBUG_TAG, "radio am��iFreqH��" + iFreqH + ";" + "iFreqH��" + iFreqL);
					String tmp = String.format("%h   %h", iFreqH, iFreqL);
					Log.e(Constant.DEBUG_TAG, tmp);
					if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
						//�����imax6 ��Ҫ��һ�����������Ĺ㲥
						changeModel(SR_MODEL_TYPE.RADIO);
					}
					SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
							(byte) 0x01);
					SerialService.getInstance().sendData(8, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
							(byte) 0x05, (byte) 0x01, (byte) iFreqH, (byte) iFreqL);
				}
			}else{
				//�趨��Ƶ
				int iFreqH = iFreq / 100;
				int iFreqL = iFreq % 100;
				if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
					//�����imax6 ��Ҫ��һ�����������Ĺ㲥
					changeModel(SR_MODEL_TYPE.RADIO);
				}
				SerialService.getInstance().sendData(5, (byte) 0x012, (byte) 0x00, (byte) 0x09, (byte) 0x00,
						(byte) 0x01);
				SerialService.getInstance().sendData(8, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x05, (byte) 0x00, (byte) iFreqH, (byte) iFreqL);
			}
		}else{
			memo = "Ƶ�ʳ�����Χ";
		}
		return memo;
		//
	}

	// ý����ƣ�����Ƶ����ͼƬ����������
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
		if(voiceCmd.getName().equals("��ʾ����������Ϣ")){
			voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_PM);
			return VoiceCommon.returnSucceed(true, "");
		}else if(voiceCmd.getName().equals("�رտ���������Ϣ")){
			voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_PM_CLOSE);
			return VoiceCommon.returnSucceed(true, "");
		}
		// ��������
		if (voiceCmd.getCategory().equals("��������")) {
			if (voiceCmd.getName().equals("����+")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x01);

			} else if (voiceCmd.getName().equals("����-")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x02);
			} else if (voiceCmd.getName().equals("����")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x03);
			} else if (voiceCmd.getName().equals("������")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x06);
			} else if (voiceCmd.getName().equals("��������")) {// ��������Ϊ�������
				if(isNumericN(voiceCmd.getNamaValue())){
					int value = Integer.parseInt(voiceCmd.getNamaValue());
					SerialService.getInstance().sendData(6, (byte)0x12, (byte)0x00, (byte)0x09, (byte)0x01, (byte)0x00, (byte)value);
				}
			}
		}
		
	
		// ����ģʽ
		if (voiceCmd.getCategory().equals("����ģʽ")) {
			Log.d("dd", "ddddd");
			if (voiceCmd.getName().equals("����ѭ��")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_REPEAT_ONE_MODE);
			} else if (voiceCmd.getName().equals("˳�򲥷�")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_REPEAT_OFF_MODE);
			} else if (voiceCmd.getName().equals("�������")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_RANDOM_MODE);
			} else if (voiceCmd.getName().equals("�ļ���ѭ��")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_REPEAT_FILE_MODE);
			} else if (voiceCmd.getName().equals("ȫ�̲���")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_REPEAT_ALL_MODE);
			} else if (voiceCmd.getName().equals("��һ��")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PREVIOUS);
			} else if (voiceCmd.getName().equals("��һ��")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_NEXT);
			}

		}
		// ��Ŀ����
		if (voiceCmd.getCategory().equals("��Ŀ����")) {
			if (voiceCmd.getName().equals("��һ��")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PREVIOUS);
			} else if (voiceCmd.getName().equals("��һ��".toString())) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_NEXT);
			} else if (voiceCmd.getName().equals("��ͣ")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PAUSE);
			} else if (voiceCmd.getName().equals("����")) {
				if(Constant.projecId == Constant.PROJECT_ID.ZT_B11B){
					voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PLAY);
				}else{
					voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PLAY);
				}
			}

		}

		if (voiceCmd.getCategory().length() == 0) {
			if (voiceCmd.getName().equals("��һ��") || voiceCmd.getName().equals("��һ��")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PREVIOUS);
			} else if (voiceCmd.getName().equals("��һ��") || voiceCmd.getName().equals("��һ��")) {
				voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_NEXT);
			}
		}
		// ��Ļ����
		if (voiceCmd.getCategory().equals("��Ļ����")) {
			if (voiceCmd.getName().equals("����+")) {

			} else if (voiceCmd.getName().equals("����-")) {

			}

		}
		
		//�绰����
		if (voiceCmd.getCategory().equals("�绰����")) {
			if (voiceCmd.getName().equals("ͨ����¼")) {
				if (SceneStatus.getBtIsConnection(mContext)) {
					voiceHandlerSendBroadcast(ACTIONS.SR_BLUETOOTHPHONE_ACTION, Constant.SR_PHONE_HISTORYCALL);
				} else {
					return VoiceCommon.returnSucceed(false, "������������");
				}
			} 
		}
		// ɨ���̨
		if (voiceCmd.getCategory().equals("����������") || voiceCmd.getCategory().equals("������")) {
			if (voiceCmd.getName().equals("ɨ���̨")||voiceCmd.getName().equals("������̨")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x03);
			} else if (voiceCmd.getName().equals("��һƵ��")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x0A);
			} else if (voiceCmd.getName().equals("��һƵ��")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x01,
						(byte) 0x09);
			} else if (voiceCmd.getName().equals("��������")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x01);
			} else if (voiceCmd.getName().equals("��������")) {
				SerialService.getInstance().sendData(5, (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x02,
						(byte) 0x02);
			} else if (voiceCmd.getName().equals("�����̨")) {
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
		 * song��Ϊ�ս����Ѹ����㲥��Ϊ�յ�����½�����Դ ������Ϊ�������֣���sourceΪ��
		 */
		if (mVoiceMusic.getArtist().length() > 0) {
			// searchSongBroadcast(mVoiceMusic);
			return VoiceCommon.returnSucceed(false, "��֧�ְ����������Ÿ���");
		}
		if (mVoiceMusic.getSource().equals("u��") || mVoiceMusic.getSource().equalsIgnoreCase("usb")) {
/*			if (!SceneStatus.getUIsConnection(mContext)) {
				return VoiceCommon.returnSucceed(false, "�����U��");
			}*/
			changeModel(SR_MODEL_TYPE.USB);
			Log.d("PlatformAdapterClient", "in_u��");
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
				return VoiceCommon.returnSucceed(false, "��������iPod");
			}
			changeModel(SR_MODEL_TYPE.IPOD);
			return VoiceCommon.returnSucceed(true, "");
		} else if (mVoiceMusic.getSource().equals("��������") || mVoiceMusic.getSource().equals("����")) {
			if(Constant.projecId == Constant.PROJECT_ID.ZT_B11F_IMAX6){
				changeModel(SR_MODEL_TYPE.BLUTOOTH);
				return VoiceCommon.returnSucceed(true, "");
			}
			if (!SceneStatus.getBtIsConnection(mContext)) {
				return VoiceCommon.returnSucceed(false, "������������");
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
					return VoiceCommon.returnSucceed(false, "��������AUX");
				}
				changeModel(SR_MODEL_TYPE.AUX);
				return VoiceCommon.returnSucceed(true, "");
			} else {
				return VoiceCommon.returnSucceed(false, "û��AUX����");
			}
		}
		if (!mVoiceMusic.getSong().equals("")) {
			searchSongBroadcast(mVoiceMusic);
			return VoiceCommon.returnSucceed(true, "");
		}
		if (mVoiceMusic.getOperation().equals("PLAY")
				&& (mVoiceMusic.getSource().equals("") || mVoiceMusic.getSong().equals(""))) {
			// �����Զ�����
			Log.d("PlatformAdapterClient", "�����Զ����Ź㲥");
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
			return VoiceCommon.returnSucceed(false, "��Ǹ���޷�ִ�в���");
		}
		
		// ���ȴ��������ã�����usb,����,��Ƶ��ͼƬ����������̨���㲥����������������
		int handleSelfState = -1;
		handleSelfState = this.handleSelfMachine(voiceAppEnt);
		Log.d("hanadd", handleSelfState + "," + falseMemo);
		if (handleSelfState == 0) {
			return VoiceCommon.returnSucceed(false, falseMemo);
		} else if (handleSelfState == 1) {
			// �ɹ�ִ��ָ���
			return VoiceCommon.returnSucceed(true, "");
		} else {
			// δִ��ָ��ͣ��������氲װ��Ӧ��
			// return this.handleSelfMachine(voiceAppEnt);
			boolean isHas = false;
			List<PackageInfo> packages = mContext.getPackageManager()
					.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
			Log.d("hanadd", ""+packages.size());
			if (packages == null || packages.size() <= 0) {
				return VoiceCommon.returnSucceed(false, "û���ҵ�"+voiceAppEnt.getName());
			}
			try {
				for (int i = 0; i < packages.size(); i++) {
					PackageInfo packageInfo = packages.get(i);
					String appName = packageInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
					if (appName.equals(voiceAppEnt.getName())) {
						isHas  = true;
						if (voiceAppEnt.getOperation().equals("LAUNCH")) {
							// ��app
							Log.d(Constant.DEBUG_TAG, "app package name:" + packageInfo.packageName);
							Log.d(Constant.DEBUG_TAG, "app name:" + appName);
							try {
								Intent intent = mContext.getPackageManager()
										.getLaunchIntentForPackage(packageInfo.packageName);
								mContext.startActivity(intent);
								break;// ����ж����ͬ��Ӧ��������ִֻ��һ��
							} catch (Exception e) {
								e.printStackTrace();
								return VoiceCommon.returnSucceed(false, appName + "����ʧ��");
							}
						} else if (voiceAppEnt.getOperation().equals("EXIT")) {
							// �ر�app
							return VoiceCommon.returnSucceed(false, "��֧�ֹر�"+voiceAppEnt.getName());
						}
					}
				}
				if(!isHas){
					return VoiceCommon.returnSucceed(false, "û���ҵ�"+voiceAppEnt.getName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return VoiceCommon.returnSucceed(true, "");
	}

	// �����Դ�app ������usb,ipod,sd�����̣��������������֣��绰�����绰����ͨ����¼������,��Ƶ��ͼƬ����������̨���㲥����������������
	private int handleSelfMachine(VoiceApp voiceAppEnt) {
		int sendstate = -1;

		if (voiceAppEnt.getName().length() > 0l) {
			String appOp = voiceAppEnt.getOperation();
			if(!appOp.equals("LAUNCH")){
				
			}
			String appName = voiceAppEnt.getName();
			
			//�ȹ��˲������Ӧ��
			if(Constant.SR_NAME_APP_NO.contains(appName)){
				falseMemo = "��"+appName+"����";
				return sendstate = -1;
			}
			appName = appName.toLowerCase();
			Log.d("vtest", "appName:ddd"+appName);
			if (Constant.SR_NAME_APP_SELFMACHINE.contains(appName)) {
				Log.d("vtest", "appName:"+appName);
				if (appName.equals("u��") || appName.equalsIgnoreCase("usb") || appName.equals("��ý��")
						|| appName.equals("ý��") || appName.equals("����")|| appName.equals("��Ƶ")|| appName.equals("����")) {
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
								falseMemo = "�����U��";
								return sendstate;
							}
						}
						//	} else {
								//sendstate = 0;
								//falseMemo = "�����U��";
						//	}
					}else	if(appOp.equals("EXIT")){
						//�ر�����
						voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_MEDIA_PAUSE);	
						sendstate = 1;
					}
				} else if (appName.equals("��̨") || appName.equals("������") || appName.equals("�㲥")) {
					
					changeModel(SR_MODEL_TYPE.RADIO);
					sendstate = 1;
				} else if (appName.equals("����") || appName.equals("��ͼ")) {
					Log.d("vsource", "daohang");
						changeModel(SR_MODEL_TYPE.NAV);
						sendstate = 1;
				} else if (appName.equals("��Ƶ")) {
					if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
						openApp(ACTIONS.SR_OS_ACTION,Constant.SR_LAUNCHER_VIDEO);
						sendstate = 1;
					}else{
						voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_VIDEO_PLAY);
						sendstate = 1;
					}
				} else if (appName.equals("������")) {
					if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
						openApp(ACTIONS.SR_OS_ACTION,Constant.SR_LAUNCHER_VIDEO);
						sendstate = 1;
					}
				} else if (appName.equals("ͼƬ")||appName.equals("���")) {
					if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
						openApp(ACTIONS.SR_OS_ACTION,Constant.SR_LAUNCHER_IMAGE);
						sendstate = 1;
					}else{
					voiceHandlerSendBroadcast(ACTIONS.SR_MEDIA_ACTION, Constant.SR_IMAGE_PLAY);
					sendstate = 1;
					}
				} else if (appName.equals("sd��") || appName.equals("SD��")) {
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
							falseMemo = "��������AUX";
						}
					} else {
						sendstate = 0;
						falseMemo = "û��AUX����";
					}
				} else if (appName.equals("��������")) {
					if (SceneStatus.getBtIsConnection(mContext)) {
						changeModel(SR_MODEL_TYPE.BLUTOOTH);
						sendstate = 1;
					} else {
						sendstate = 0;
						falseMemo = "������������";
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
							falseMemo = "��������ipod";
						}
					}
					
				}else if(appName.equals("����")){
					if(Constant.projecId==Constant.PROJECT_ID.ZT_B11F_IMAX6){
						voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_OS_SET);
						sendstate = 1;
					}
				} else if (appName.equals("�绰��") || appName.equals("ͨѶ¼") || appName.equals("�绰��")|| appName.equals("�绰��")) {
					if (SceneStatus.getBtIsConnection(mContext)) {
						voiceHandlerSendBroadcast(ACTIONS.SR_BLUETOOTHPHONE_ACTION, Constant.SR_PHONE_CONTACTS);
						sendstate = 1;
					} else {
						sendstate = 0;
						falseMemo = "������������";
					}
				} else if (appName.equals("���Ű�") || appName.equals("������")) {
					if (SceneStatus.getBtIsConnection(mContext)) {
						voiceHandlerSendBroadcast(ACTIONS.SR_BLUETOOTHPHONE_ACTION, Constant.SR_PHONE_DIAL);
						sendstate = 1;
					} else {
						sendstate = 0;
						falseMemo = "������������";
					}
				} else if (appName.equals("�绰") || appName.equals("�����绰")) {
					if (SceneStatus.getBtIsConnection(mContext)) {
						voiceHandlerSendBroadcast(ACTIONS.SR_BLUETOOTHPHONE_ACTION, Constant.SR_PHONE_DIAL);
						sendstate = 1;
					} else {
						sendstate = 0;
						falseMemo = "������������";
					}
				} else if (appName.equals("ͨ����¼")) {
					if (SceneStatus.getBtIsConnection(mContext)) {
						voiceHandlerSendBroadcast(ACTIONS.SR_BLUETOOTHPHONE_ACTION, Constant.SR_PHONE_HISTORYCALL);
						sendstate = 1;
					} else {
						sendstate = 0;
						falseMemo = "������������";
					}
				} else if (appName.equals("����")) {
					voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_BT);
					sendstate = 1;
				} else if (appName.equals("������Ϣ")) {
					voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_CARINFOR);
					sendstate = 1;
				}  else if (appName.equals("̥ѹ")||appName.equals("̥ѹ����")) {
					voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_TIRE);
					sendstate = 1;
				} else if (appName.equals("Ӧ��")) {
					voiceHandlerSendBroadcast(ACTIONS.SR_OS_ACTION, Constant.SR_LAUNCHER_APP);
					sendstate = 1;
				} else if (appName.equals("�ֻ�����")||appName.equals("����")) {
						changeModel(SR_MODEL_TYPE.PHONE_LINK);
						sendstate = 1;
					/*
					 * if(Constant.projecId == Constant.PROJECT_ID.ZT_B11B ){
					 * try { Intent intent =
					 * mContext.getPackageManager().getLaunchIntentForPackage(
					 * "net.easyconn"); mContext.startActivity(intent); } catch
					 * (Exception e) { e.printStackTrace(); sendstate = 0;
					 * falseMemo = "��ʧ��"; }
					 * changeModel(SR_MODEL_TYPE.PHONE_LINK); }else
					 * if(Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C){
					 * changeModel(SR_MODEL_TYPE.PHONE_LINK); }
					 */

				} else if (appName.equals("����������Ϣ��ʾ")||appName.equals("pm��������Ϣ��ʾ")) {
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
		Log.d("�յ�", "in_handleAircontrol");
		Log.d(Constant.DEBUG_TAG, "handleAirControl:" + recognizerResult);
		VoiceAirControl mVoiceAirControl = new VoiceAirControl(recognizerResult);
		// List<Integer> tomcuControl = new ArrayList<Integer>(4);
		// ������Э���㷢
		int num1 = 0x14;
		int num2 = 0x01;
		// ���μ���
		if (mVoiceAirControl.getDevice().equals("����")) {
			if (Constant.projecId == PROJECT_ID.ZT_B11A_C) {
				//return VoiceCommon.returnSucceed(false, "���β�֧���������ƹ���");
			}
			if ((mVoiceAirControl.getMode().equals("����") || mVoiceAirControl.getMode().equals("�ͷ�"))) {
				if (Constant.projecId == PROJECT_ID.ZT_B11A_C) {
					//return VoiceCommon.returnSucceed(false, "���β�֧���������ƹ���");
				}
				num1 = 0x50;
				num2 = 0x01;
				int num3 = 0xFF;
				int leftSeatAirControl = 0xFF;// 1��2��3 ͨ��ȼ�
				int rightSeatAirControl = 0xFF;// 1��2��3 ͨ��ȼ�
				int leftSeatHeat = 0xFF;// 1��2��3 ���ȵȼ�
				int rightSeatHeat = 0xFF;// 1��2��3 ���ȵȼ�
				int num8 = 0xFF;
				int num9 = 0x02;// ��ʾ�������� 0x01��������
				int whitchSeat = 0x01;// 1�����Σ�2������
				int num11 = 0xFF;//

				if (mVoiceAirControl.getTarget().equals("����")) {
					if (mVoiceAirControl.getMode().equals("����")) {
						if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
							// ���μ���û�п���ֻ�е�λ���ã�����Ҫ�Ȼ�ȡ�µ�ǰ��λֵ
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
					} else if (mVoiceAirControl.getMode().equals("�ͷ�")) {
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
				} else if (mVoiceAirControl.getTarget().equals("����")) {
					if (mVoiceAirControl.getMode().equals("����")) {
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
					} else if (mVoiceAirControl.getMode().equals("�ͷ�")) {
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
					//���û�б������ݻ��Ǹ�����ͳһĬ��Ϊ����
					if (mVoiceAirControl.getMode().equals("����")) {
						if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
							// ���μ���û�п���ֻ�е�λ���ã�����Ҫ�Ȼ�ȡ�µ�ǰ��λֵ
							leftSeatHeat = CarInfor.carSeatStatus.getSeatLeftHeat();
							leftSeatHeat = (leftSeatHeat == 0) ? 1 : leftSeatHeat;
							if(Constant.projecId == PROJECT_ID.ZT_B11A_C||Constant.projecId == PROJECT_ID.ZT_B11F_IMAX6){
								//�����a-c ����������
								rightSeatHeat = CarInfor.carSeatStatus.getSeatRightHeat();
								rightSeatHeat = (rightSeatHeat == 0) ? 1 : rightSeatHeat;
							}
							if(isNumericN(mVoiceAirControl.getTemperatureLevel())){
								leftSeatHeat = Integer.parseInt(mVoiceAirControl.getTemperatureLevel());
								if(Constant.projecId == PROJECT_ID.ZT_B11A_C||Constant.projecId == PROJECT_ID.ZT_B11F_IMAX6){
									//�����a-c b11f ����������
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
								//�����a-c  b11f ���������ر�
								rightSeatHeat = leftSeatHeat;
							}
						}
					} else if (mVoiceAirControl.getMode().equals("�ͷ�")) {
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
		if ((mVoiceAirControl.getMode().equals("����") || mVoiceAirControl.getMode().equals("�ͷ�"))
				&& (mVoiceAirControl.getTarget().equals("����") || mVoiceAirControl.getTarget().equals("����"))) {
			if (Constant.projecId == PROJECT_ID.ZT_B11A_C) {
				//return VoiceCommon.returnSucceed(false, "���β�֧���������ƹ���");
			}
			num1 = 0x50;
			num2 = 0x01;
			int num3 = 0xFF;
			int leftSeatAirControl = 0xFF;// 1��2��3 ͨ��ȼ�
			int rightSeatAirControl = 0xFF;// 1��2��3 ͨ��ȼ�
			int leftSeatHeat = 0xFF;// 1��2��3 ���ȵȼ�
			int rightSeatHeat = 0xFF;// 1��2��3 ���ȵȼ�
			int num8 = 0xFF;
			int num9 = 0x02;// ��ʾ�������� 0x01��������
			int whitchSeat = 0x01;// 1�����Σ�2������
			int num11 = 0xFF;//

			if (mVoiceAirControl.getTarget().equals("����")) {
				if (mVoiceAirControl.getMode().equals("����")) {
					if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
						// ���μ���û�п���ֻ�е�λ���ã�����Ҫ�Ȼ�ȡ�µ�ǰ��λֵ
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
				} else if (mVoiceAirControl.getMode().equals("�ͷ�")) {
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
			} else if (mVoiceAirControl.getTarget().equals("����")) {
				if (mVoiceAirControl.getMode().equals("����")) {
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
				} else if (mVoiceAirControl.getMode().equals("�ͷ�")) {
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
		int voiceActiveControl = 1;// ��������״̬
		int acSwitchRequest = 0xFF;// ѹ������������1��AC OFF (ѹ�����ر�/����) 2��AC ON
									// (ѹ������/����)
		if (mVoiceAirControl.getMode().equals("����")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				acSwitchRequest = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				acSwitchRequest = 1;
			}
		}
		if (mVoiceAirControl.getMode().equals("����")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				acSwitchRequest = 1;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				acSwitchRequest = 2;
			}
		}

		int airCondition = 0xFF;// �յ�״̬ 1�أ�2��
		if(Constant.projecId==Constant.PROJECT_ID.ZT_B11A_C){
			//airCondition = 2;	
		}
		if (mVoiceAirControl.getDevice().equals("�յ�")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("OPEN")) {
				airCondition = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				airCondition = 1;
			}
		}



		int autoStatus = 0xFF;// �Զ�ģʽ 1�أ�2��
		if (mVoiceAirControl.getMode().equals("�Զ�")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				autoStatus = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				autoStatus = 1;
			}
		}

		int cycleStaus = 0xFF;// ����ѭ��״̬ 1���⣬2����
		if (mVoiceAirControl.getMode().equals("��ѭ��")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				cycleStaus = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				cycleStaus = 1;
			}
		}
		if (mVoiceAirControl.getMode().equals("��ѭ��")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				cycleStaus = 1;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				cycleStaus = 2;
			}
		}

		int frontDefrost = 0xFF;// ǰ��˪ 1�أ�2��
		if (mVoiceAirControl.getMode().equals("ǰ��˪")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				frontDefrost = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				frontDefrost = 1;
			}
		}
		if (mVoiceAirControl.getMode().equals("��˪")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				frontDefrost = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				frontDefrost = 1;
			}
		}
		int reardefrost = 0xFF;// ���˪ 1�أ�2��
		if (mVoiceAirControl.getMode().equals("���˪")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				reardefrost = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				reardefrost = 1;
			}
		}

		if (mVoiceAirControl.getMode().equals("һ����˪")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				frontDefrost = 2;
				reardefrost = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				frontDefrost = 1;
				reardefrost = 1;
			}
		}
		int acMax = 0xFF;// ������� 1�أ�2��
		if (mVoiceAirControl.getMode().equals("����")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				if (mVoiceAirControl.getFanSpeed().equals("���")) {
					acMax = 2;
				}
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				acMax = 1;
			}
		}

		int hotMax = 0xFF;// ������� 1�أ�2��
		if (mVoiceAirControl.getMode().equals("����")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				if (mVoiceAirControl.getFanSpeed().equals("���")) {
					hotMax = 2;
				}
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				hotMax = 1;
			}
		}

		int temperatureRequest = 0xFF;// �¶����� 1up, 2down���Ƿ����¶����Ӷ���
		if (mVoiceAirControl.getTemperature().equals("+")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				temperatureRequest = 1;
			}
		} else if (mVoiceAirControl.getTemperature().equals("-")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				temperatureRequest = 2;
			}
		}

		int windSpeedRequest = 0xFF;// �������� 1up, 2down
		if (mVoiceAirControl.getFanSpeed().equals("+")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				windSpeedRequest = 1;
			}
		} else if (mVoiceAirControl.getFanSpeed().equals("-")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				windSpeedRequest = 2;
			}
		}

		int hvacModel = 0xFF;// �յ�ģʽ 0:default 1:Face (����) 2:Face and Foot(�����ͽ�)
								// 3:Foot (����)4:Foot and Defrost(���ź�ǰ��˪) 5:mode
								// active 6: Defrost(ǰ��˪)
		if (mVoiceAirControl.getAirflowDirection().equals("��")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				hvacModel = 1;
			}
		} else if (mVoiceAirControl.getAirflowDirection().equals("���洵��")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				hvacModel = 2;
			}
		} else if (mVoiceAirControl.getAirflowDirection().equals("��")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				hvacModel = 3;
				if (mVoiceAirControl.getMode().equalsIgnoreCase("��˪")) {
					hvacModel = 4;
				}
			}
		} else if (mVoiceAirControl.getAirflowDirection().equals("���Ŵ���")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				hvacModel = 4;
			}
		}else if (mVoiceAirControl.getAirflowDirection().equals("��")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				hvacModel = 3;
			}
		} else if (mVoiceAirControl.getMode().equals("��ǰ��")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				frontDefrost = 2;
				hvacModel = 6;
			}
		}

		int dualStatus = 0xFF;// ˫���� 1�أ�2��
		if (mVoiceAirControl.getMode().equals("˫��")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				dualStatus = 1;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				dualStatus = 2;
			}
		}

		VoiceSub temperatureSub = mVoiceAirControl.getTemperatureSub();
		int dataTypeNum = 0xFF;// ��̩18-32 ����17-32 ȷ�����¶�ֵת����ʽ
		if (isNumericN(mVoiceAirControl.getTemperature())) {
			dataTypeNum = Integer.parseInt(mVoiceAirControl.getTemperature());
			dataTypeNum = dataTypeNum * 2;
		} else if (mVoiceAirControl.getTemperature().length() > 0) {
			if (mVoiceAirControl.getOperation().equals("SET")) {
				if (temperatureSub != null) {
					int offsetValue = Integer.parseInt(temperatureSub.getOffset());
					if (mVoiceAirControl.getTarget().equals("����")) {
						dataTypeNum = CarInfor.carAirStatus.getRightTemperature();
						Log.d("voicetemp", "wen du right is " + dataTypeNum);
					}
					if (mVoiceAirControl.getTarget().equals("����") || mVoiceAirControl.getTarget().equals("")) {
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

		int selfTestActive = 0xFF;// �Լ�״̬
		int fanSpeedLevel = 0xFF;// �����ȼ� 1-7
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
			} else if (mVoiceAirControl.getFanSpeed().equals("�߷�")) {
				if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
					fanSpeedLevel = 7;
				}
			} else if (mVoiceAirControl.getFanSpeed().equals("�ͷ�")) {
				if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
					fanSpeedLevel = 1;
				}
			} else if (mVoiceAirControl.getFanSpeed().equals("���")) {
				if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
					fanSpeedLevel = 7;
				}
			} else if (mVoiceAirControl.getFanSpeed().equals("��С")) {
				if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
					fanSpeedLevel = 1;
				}
			}

		}

		int driverSeat = 0xFF;// �����յ�����0: driver seat 1: passenger seat 2: back
								// seat
		if (mVoiceAirControl.getTarget().equals("����")) {
			if (Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C) {
				return VoiceCommon.returnSucceed(false, "�յ���֧�������������ƹ���");
			}
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				driverSeat = 0;
			}
		}
		if (mVoiceAirControl.getTarget().equals("����")) {
			if (Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C) {
				return VoiceCommon.returnSucceed(false, "�յ���֧�������������ƹ���");
			}
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				driverSeat = 1;
			}
		}
		if (mVoiceAirControl.getTarget().equals("��յ�")) {
		//	if (Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C||Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C) {
			//	return VoiceCommon.returnSucceed(false, "��յ���֧���������ƹ���");
		//	}
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				driverSeat = 2;
			}
			return VoiceCommon.returnSucceed(false, "��յ���֧���������ƹ���");
		}

		int tempeleElecAirCondition = 0xFF;// �綯�յ��¶ȵ�λ 15��
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
				// �¶ȵ�λ���ӻ����
				int offsetValue = Integer.parseInt(tLevel.getOffset());
				tempeleElecAirCondition = CarInfor.carAirStatus.getFanSpeed();// ȱ���¶ȵ�λ��ȡ
				if (offsetValue > 0) {
					if (temperatureSub.getDirect().equals("-")) {
						if (Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C) {
							return VoiceCommon.returnSucceed(false, "��֧���¶ȵ�λ���ټ�");
						}
						tempeleElecAirCondition -= offsetValue;
					} else if (temperatureSub.getDirect().equals("+")) {
						if (Constant.projecId == Constant.PROJECT_ID.ZT_B11A_C) {
							return VoiceCommon.returnSucceed(false, "��֧���¶ȵ�λ���Ӽ�");
						}
						tempeleElecAirCondition += offsetValue;
					}
				}
			}
		}
		int heating = 0xFF;// ���μ���
		int airing = 0xFF;// ����ͨ��
		int autoCyle = 0xFF;// �Զ�����ѭ�� 1 noactive, 2 active
		if (mVoiceAirControl.getMode().equals("����ѭ���Զ�")) {
			
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				autoCyle = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				autoCyle = 1;
			}
		}

		int anion = 0xFF;// �����ӹ��ܿ��� 1 noactive, 2 active
		if (mVoiceAirControl.getMode().equals("��������")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				anion = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				anion = 1;
			}
		}

		int pm25 = 0xFF;// PM2.5��ʾ���� 1 noactive, 2 active
		int frontHeat = 0xFF;// ǰ�絲���� 1 noactive, 2 active
		if (mVoiceAirControl.getMode().equals("ǰ�絲����")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				frontHeat = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				frontHeat = 1;
			}
		}
		int temperatureProvince = 0xFF;// �¶ȷ������ƿ��� 1 noactive, 2 active
		if (mVoiceAirControl.getMode().equals("�¶ȷ�������")) {
			if (mVoiceAirControl.getOperation().equalsIgnoreCase("SET")) {
				temperatureProvince = 2;
			} else if (mVoiceAirControl.getOperation().equalsIgnoreCase("CLOSE")) {
				temperatureProvince = 1;
			}
		}
		int data25 = 0xFF;

	/*	if(Constant.projecId==Constant.PROJECT_ID.ZT_B11A_C){
			//�����ǰ�յ��򿪣����ٷ��������ǰ�յ��ر�Ҫ�ȷ��Ϳ�
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
			//ֻ���¶��趨�ĵط�Ҫ���жϣ�����رտյ���Ҫ�ȴ򿪿յ�
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
		if (carControl.getName().equals("�촰")) {
			if(Constant.projecId != Constant.PROJECT_ID.ZT_B11B&&Constant.projecId != Constant.PROJECT_ID.ZT_B11A_C){
				return VoiceCommon.returnSucceed(false, "��֧�����������촰");
			}
			int num1 = 0x17;
			int num2 = 0x01;
			int airWindow = 0x00; // �촰״̬ 2����3��
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
			if (vInfoEnt.getName().equals("����")) {
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
