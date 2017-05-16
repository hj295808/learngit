package com.jsbd.voice.dimens;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SceneStatus {
	//记录下语音当前的打开状态，录音的关闭，打开
	public static boolean VR_IS_STATUS = true;

	public static int canSR(Context context) {
		Cursor cursor = context.getContentResolver().query(
				Uri.parse(Constant.SCENE_STATUS_CONTENTPROVIDER), null, null,
				null, null);
		if (cursor == null) {
			return Constant.SR_AWAKE;
		}
		int indexa = 0;
		while (cursor.moveToNext()) {
			try {
				int statusa = cursor.getInt(2);
				switch (indexa) {
				case 0:// 车辆行驶状态 vehicleStatus 倒车和非倒车（1和0）
					if (statusa == 1) {
						 Log.e(Constant.DEBUG_TAG, " status name >>"+cursor.getString(1));
						 cursor.close();
						return Constant.SR_SLEEP;
					}
				case 1:// 通话状态 callStatus 通话和未通话（1和0）
					if (statusa == 1) {
						Log.e(Constant.DEBUG_TAG, "status name >>"+cursor.getString(1));
						cursor.close();
						return Constant.SR_SLEEP;
					}
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:// T-BOX tBOXStatus 来电响铃、接通、被挂断、连接中（0、1、2、3）;
					if (statusa == 0||statusa ==1||statusa == 3) {
						Log.e(Constant.DEBUG_TAG, "status name >>"+cursor.getString(1));
						cursor.close();
						return Constant.SR_SLEEP;
					}
				case 7:// T-BOX E-Call 不在E-Call、在E-Call中（0、1）
					if (statusa == 1) {
						Log.e(Constant.DEBUG_TAG, "status name >>"+cursor.getString(1));
						cursor.close();
						return Constant.SR_SLEEP;
					}
				case 8:
					break;
				case 10://power状态 0 power off,1 power on
					if (statusa == 0) {
						Log.e(Constant.DEBUG_TAG, "status name >>"+cursor.getString(1));
						return Constant.SR_SLEEP;
					}
					break;
				}
			} catch (Exception e) {
				//e.printStackTrace();
				 Log.e(Constant.DEBUG_TAG, "read ContentProvier err");
					if(cursor!=null){
						cursor.close();
					}
				return Constant.SR_AWAKE;
			} 
			indexa++;
		}
		if(cursor!=null){
			cursor.close();
		}
		return Constant.SR_AWAKE;
	}
	
	/*public SceneStatus(Context context){
		String[] valcolum = null;
		Cursor cursor = context.getContentResolver().query(Uri.parse(Constant.SCENE_STATUS_CONTENTPROVIDER), null,
				null, null, null);
		if (cursor != null) {
			Log.e(Constant.DEBUG_TAG, String.valueOf(cursor.getCount()));
			Log.e(Constant.DEBUG_TAG, String.valueOf(cursor.getColumnCount()));
			valcolum = cursor.getColumnNames();
			for(int i=0;i<valcolum.length;i++){
				Log.d("valcolum", valcolum[i]);
			}
			int indexa = 0;
			while (cursor.moveToNext()) {
				try {
					Log.d("id:status:value",
							cursor.getInt(0) + ":" + cursor.getString(1) + ":"
									+ String.valueOf(cursor.getInt(2)));
					int statusa = cursor.getInt(2);
					switch (indexa) {
					case 0:// 车辆行驶状态 vehicleStatus 倒车和非倒车（1和0）
						this.setVehicleStatus(cursor.getInt(2));
						Log.e(Constant.DEBUG_TAG,
								"status name >>" + cursor.getString(1) + ":"
										+ this.vehicleStatus);
						break;
					case 1:// 通话状态 callStatus 通话和未通话（1和0）
						this.setCallStatus(cursor.getInt(2));
						Log.e(Constant.DEBUG_TAG,"status name >>" + cursor.getString(1) + ":"+ this.callStatus);
						break;
					case 2:
						this.setDayOrNight(cursor.getInt(2));
						Log.e(Constant.DEBUG_TAG,"status name >>" + cursor.getString(1) + ":"+ this.dayOrNight);
						break;
					case 3:
						this.setBluetoothConnStatus(cursor.getInt(2));
						Log.e(Constant.DEBUG_TAG,"status name >>" + cursor.getString(1) + ":"+ this.bluetoothConnStatus);
						break;
					case 4:
						this.setMediaStatus(cursor.getInt(2));
						Log.e(Constant.DEBUG_TAG,"status name >>" + cursor.getString(1) + ":"+ this.mediaStatus);
						break;
					case 5:
						this.setSrStatus(cursor.getInt(2));
						Log.e(Constant.DEBUG_TAG,"status name >>" + cursor.getString(1) + ":"+ this.srStatus);
						break;
					case 6:// T-BOX tBOXStatus 来电、电话接通、电话连接中（0、1、3）;
						this.settBOXStatus(cursor.getInt(2));
						Log.e(Constant.DEBUG_TAG,"status name >>" + cursor.getString(1) + ":"+ this.tBOXStatus);
						break;
					case 7:// T-BOX E-Call 不在E-Call、在E-Call中（0、1）
						this.setECall(cursor.getInt(2));
						Log.e(Constant.DEBUG_TAG,"status name >>" + cursor.getString(1) + ":"+ this.ECall);
						break;
					case 8:
						this.setDeviceStatus1(cursor.getInt(2));
						Log.e(Constant.DEBUG_TAG,"status name >>" + cursor.getString(1) + ":"+ this.deviceStatus1);
						break;
					case 9:
						this.setDeviceStatus2(cursor.getInt(2));
						Log.e(Constant.DEBUG_TAG,"status name >>" + cursor.getString(1) + ":"+ this.deviceStatus2);
						break;
					}
				} catch (Exception e) {
					// e.printStackTrace();
					Log.e(Constant.DEBUG_TAG, "read ContentProvier err");
					cursor.close();
				}
				indexa++;
			}
			cursor.close();
		}
	}*/
	

	
	public int getVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(int vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public int getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(int callStatus) {
		this.callStatus = callStatus;
	}

	public int getDayOrNight() {
		return dayOrNight;
	}

	public void setDayOrNight(int dayOrNight) {
		this.dayOrNight = dayOrNight;
	}

	public int getBluetoothConnStatus() {
		return bluetoothConnStatus;
	}

	public void setBluetoothConnStatus(int bluetoothConnStatus) {
		this.bluetoothConnStatus = bluetoothConnStatus;
	}

	public int getMediaStatus() {
		return mediaStatus;
	}

	public void setMediaStatus(int mediaStatus) {
		this.mediaStatus = mediaStatus;
	}

	public int getSrStatus() {
		return srStatus;
	}

	public void setSrStatus(int srStatus) {
		this.srStatus = srStatus;
		
	}

	public int gettBOXStatus() {
		return tBOXStatus;
	}

	public void settBOXStatus(int tBOXStatus) {
		this.tBOXStatus = tBOXStatus;
	}

	public int getECall() {
		return ECall;
	}

	public void setECall(int eCall) {
		ECall = eCall;
	}

	public int getDeviceStatus1() {
		return deviceStatus1;
	}

	public void setDeviceStatus1(int deviceStatus1) {
		this.deviceStatus1 = deviceStatus1;
	}

	public int getDeviceStatus2() {
		return deviceStatus2;
	}

	public void setDeviceStatus2(int deviceStatus2) {
		this.deviceStatus2 = deviceStatus2;
	}

	private int vehicleStatus;
	private int callStatus;
	private int dayOrNight;
	private int bluetoothConnStatus;
	private int mediaStatus;
	private int srStatus;
	private int tBOXStatus;
	private int ECall;
	private int deviceStatus1;
	private int deviceStatus2;
	
	public static int getStatusWithIndex(Context context, int statusIndex) {
		if(context==null){return -1;}
		String[] whereStr = {String.valueOf(statusIndex)};
		Cursor cursor = context.getContentResolver().query(
				Uri.parse(Constant.SCENE_STATUS_CONTENTPROVIDER), null, null,
				whereStr, null);
		if(cursor==null){
			return -1;
		}
		try{
			if(cursor.moveToNext()){
				int val = cursor.getInt(2);
				String valname = cursor.getString(1);
				Log.e(Constant.DEBUG_TAG, "getStatusWithIndex >> "+valname+":"+val);
				if(cursor!=null){
					cursor.close();
				}
				return  val;
			}
		}catch(Exception e){
			Log.e(Constant.DEBUG_TAG, Log.getStackTraceString(e));
			if(cursor!=null){
				cursor.close();
			}
		}
		return -1;
	}
	
	public static void setSceneStatusValueWithIndex(Context context,int statusIndex,int statusvalue){
		String[] whereStr =  {String.valueOf(statusIndex)}; 
		ContentValues mContentValues = new ContentValues();
		mContentValues.put("value", statusvalue);
		context.getContentResolver().update(Uri.parse(Constant.SCENE_STATUS_CONTENTPROVIDER)
				                                      , mContentValues
				                                      , null
				                                      , whereStr);//selectionArgs
		
		
	   
	}
	
	
	private static String toBinaryArray(int value){
		if(value<0){
			return null;
		}
		Log.e(Constant.DEBUG_TAG, "设备状态表中的值 >> "+value);
		String binaryStr = Integer.toBinaryString(value);
		Log.e(Constant.DEBUG_TAG, "设备状态转换后二进制的值 >>"+binaryStr+"长度："+binaryStr.length());
		for(int i=binaryStr.length();i<8;i++){
			binaryStr = "0"+binaryStr;
		}
		Log.e(Constant.DEBUG_TAG, "设备状态转换后二进制的值 >>"+binaryStr);
		return binaryStr;
	}

	public static String getDeviceStatus1(Context context) {
		return toBinaryArray(getStatusWithIndex(context, 8));
	}
	
	public static String getDeviceStatus2(Context context){
		return toBinaryArray(getStatusWithIndex(context, 9));
	}
	
	//蓝牙 连接状态
	public static boolean getBtIsConnection(Context context){
		return (getStatusWithIndex(context,3)==1);
	}
	
	//ipod 连接状态
	public static boolean getIpodIsConnection(Context context){
		String  devicesStatu =SceneStatus.getDeviceStatus1(context);
		return (devicesStatu.charAt(5)=='1');
	}
	
	//AUX 连接状态
	public static boolean getAUXIsConnection(Context context){
		String  devicesStatu =SceneStatus.getDeviceStatus2(context);
		return (devicesStatu.charAt(3)=='1');
	}
	
	
	//U盘1 连接状态
	public static boolean getUIsConnection(Context context){
		String  devicesStatu =SceneStatus.getDeviceStatus1(context);
		return (devicesStatu.charAt(7)=='1');
	}
	
	//U盘2 连接状态
	public static boolean getU2IsConnection(Context context){
		String  devicesStatu =SceneStatus.getDeviceStatus1(context);
		return (devicesStatu.charAt(1)=='1');
	}
	
	//U盘3 连接状态
	public static boolean getU3IsConnection(Context context){
		String  devicesStatu =SceneStatus.getDeviceStatus1(context);
		return (devicesStatu.charAt(2)=='1');
	}
}
