/**
 * 
 */
package com.jsbd.voice.entity;

import java.security.PublicKey;

import org.json.JSONObject;

import android.Manifest.permission;

/**
 * @author suochao
 *
 */
public class VoiceAirControl extends VoiceEntity {

	private String rawText;//����תд������
	private String operation;//�������ͣ���SET|OPEN|CLOSE|CANCEL��
	private String device;//�����豸���ƣ����յ�|���������豸��
	private String mode;//����ģʽ���ƣ�ȡֵ��������|����|��ʪ|��˪|�Զ�|�ͷ�|ʡ��|�ڷ�|����|����ɨ|����|����|��˯|�縨��|����|ǿ��|�|����|�紵��|�����|��Ӧ ����|����|��ǰ��|�����ʹ���|��ѭ��|��ѭ��|�Զ�ѭ����
	private String temperature;//�¶ȵ��ڣ�ȡֵ����+|-|���֡������忴digital˵��
	private String temperatureLevel;//�¶ȵȼ�
	private String fanSpeed;//���ٵ��ڣ�ȡֵ���߷�|�ͷ�|+|-|���|��С|�з�|�Զ��硿
	private String airflowDirection;//������ڣ�ȡֵ������ɨ������|����ɨ������|ɨ��ֹͣ|��|��|��|��|ͷ|��|����|��|��|��|���洵��|ɨ��|���¾���|���Ҿ��С�
	private String name;//��ʾ��Ҫȡ���Ĳ������ƣ�Ŀǰȡֵ�С���ʱ��
	private String target;//��Ҫ���Ƶľ���Ŀ�꡾����|����|��յ���
	
	//���ڽ���Temperature��json���ݼ��ڲ���
	private JSONObject mJsonObject=null;
	private VoiceSub temperatureSub =null;
	private VoiceSub fanSpeedSub = null;
	private VoiceSub temperatureLevelSub  = null;
	

	
	public VoiceAirControl(String strResult) {
		super(strResult);
		try{
			
			this.rawText = this.getJsonObject().optString("rawText", "");//getString("rawText");
			this.operation = this.getJsonObject().optString("operation", "");//getString("operation");
			this.device = this.getJsonObject().optString("device", "");//getString("device");
			this.mode = this.getJsonObject().optString("mode", "");//getString("mode");
			this.temperature = this.getJsonObject().optString("temperature", "");//getString("temperature");
			this.temperatureLevel = this.getJsonObject().optString("temperatureLevel", "");//getString("temperatureLevel");
			this.fanSpeed = this.getJsonObject().optString("fan_speed", "");//getString("fanSpeed");
			this.airflowDirection = this.getJsonObject().optString("airflow_direction", "");//getString("airflowDirection");
			this.name = this.getJsonObject().optString("name", "");//getString("name");
			this.target = this.getJsonObject().optString("target", "");//getString("target");
			//�ж�temperature�Ƿ�Ϊjson����
			if (temperature.startsWith("{")) {
				mJsonObject=new JSONObject(temperature);
				temperatureSub = new VoiceSub(mJsonObject);
			}
			//�ж�fanSpeed�Ƿ�Ϊjson����
			if (fanSpeed.startsWith("{")) {
				fanSpeedSub = new VoiceSub(new JSONObject(fanSpeed));
			}
			//�ж�temperatureLevel�Ƿ�Ϊjson����
			if (temperatureLevel.startsWith("{")) {
				setTemperatureLevelSub(new VoiceSub(new JSONObject(temperatureLevel)));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//�ڲ������Temperature
	public class VoiceSub{
		private String direct;
		private String offset;
		private String ref;
		private String type;
		
		public VoiceSub(JSONObject JSONObject){
			this.direct = JSONObject.optString("direct", "");
			this.offset = JSONObject.optString("offset", "");
			this.ref = JSONObject.optString("ref", "");
			this.type = JSONObject.optString("type", "");
		}

		public String getDirect() {
			return direct;
		}

		public void setDirect(String direct) {
			this.direct = direct;
		}

		public String getOffset() {
			return offset;
		}

		public void setOffset(String offset) {
			this.offset = offset;
		}

		public String getRef() {
			return ref;
		}

		public void setRef(String ref) {
			this.ref = ref;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		
		
	}
	

	
	public String getVoiceAirControlString(){
		String str = "";
		str = "rawText:"+this.rawText+",operation:"+this.operation+",device:"+this.device+",mode:"+this.mode+",temperature:"+this.temperature+",fanSpeed:"+this.fanSpeed+",airflowDirection:"+this.airflowDirection
				+",name:"+this.name+",target:"+this.target;
		//Log.d(Constant.DEBUG_TAG, musicStr);
		 return str;
	}

	public JSONObject getmJsonObject() {
		return mJsonObject;
	}

	public void setmJsonObject(JSONObject mJsonObject) {
		this.mJsonObject = mJsonObject;
	}

	public VoiceSub getTemperatureSub() {
		return temperatureSub;
	}

	public void setTemperature(VoiceSub temperatureSub) {
		this.temperatureSub = temperatureSub;
	}

	/**
	 * @return the rawText
	 */
	public String getRawText() {
		return rawText;
	}

	/**
	 * @param rawText the rawText to set
	 */
	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
			return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the device
	 */
	public String getDevice() {
			return device;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
			return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the temperature
	 */
	public String getTemperature() {
//		if (this.temperature != null) {
//			if (temperature.equals("")) {
//				return "0";
//			}
//			return temperature;
//		} else {
//			return "0";
//		}
		return temperature;
	}

	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the temperatureLevel
	 */
	public String getTemperatureLevel() {
		return temperatureLevel;
	}

	/**
	 * @param temperatureLevel the temperatureLevel to set
	 */
	public void setTemperatureLevel(String temperatureLevel) {
		this.temperatureLevel = temperatureLevel;
	}

	/**
	 * @return the fanSpeed
	 */
	public String getFanSpeed() {
		return fanSpeed;
	}

	/**
	 * @param fanSpeed the fanSpeed to set
	 */
	public void setFanSpeed(String fanSpeed) {
		this.fanSpeed = fanSpeed;
	}

	/**
	 * @return the airflowDirection
	 */
	public String getAirflowDirection() {
		return airflowDirection;
	}

	/**
	 * @param airflowDirection the airflowDirection to set
	 */
	public void setAirflowDirection(String airflowDirection) {
		this.airflowDirection = airflowDirection;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @return the fanSpeedSub
	 */
	public VoiceSub getFanSpeedSub() {
		return fanSpeedSub;
	}

	/**
	 * @param fanSpeedSub the fanSpeedSub to set
	 */
	public void setFanSpeedSub(VoiceSub fanSpeedSub) {
		this.fanSpeedSub = fanSpeedSub;
	}

	/**
	 * @return the temperatureLevelSub
	 */
	public VoiceSub getTemperatureLevelSub() {
		return temperatureLevelSub;
	}

	/**
	 * @param temperatureLevelSub the temperatureLevelSub to set
	 */
	public void setTemperatureLevelSub(VoiceSub temperatureLevelSub) {
		this.temperatureLevelSub = temperatureLevelSub;
	}

}
