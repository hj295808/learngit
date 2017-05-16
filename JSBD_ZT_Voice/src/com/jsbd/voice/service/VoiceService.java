package com.jsbd.voice.service;

import com.iflytek.platform.type.PlatformCode;
import com.iflytek.platformadapter.PlatformAdapterApplication;
import com.iflytek.platformadapter.PlatformAdapterClient;
import com.iflytek.platformadapter.VoiceCommon;
import com.iflytek.platformadapter.VoiceServiceFunctionInterface;
import com.iflytek.platformservice.PlatformService;
import com.jsbd.jsbdvoice.service.JSBDTtsListener;
import com.jsbd.jsbdvoice.service.JSBDTtsService;
import com.jsbd.voice.dimens.ACTIONS;
import com.jsbd.voice.dimens.Constant;
import com.jsbd.voice.dimens.SceneStatus;
import com.jsbd.voice.vranimation.VRFloatViewManager;
import com.jsbd.voice.vranimation.VRWindowManager;

import android.R.string;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class VoiceService extends Service  {

	private PlatformAdapterClient pAdapterClient = null;
	private BroadcastReceiver mBroadcastReceiver=null;
	//����contentprovider
	private MyObserver myObserver = null;
	//�����Ƿ���Ի��ѵı�ǣ���¼��һ���Ƿ���Ի��ѵ�״̬��Ĭ��ֵ�ǿ��Ի��ѵ�
	private int srCanTag = Constant.SR_AWAKE;
	private Context mContext;
	
	
	//ע����� app���ؽ���㲥
	private void regBroadcastReceiver() {
		// TODO Auto-generated method stub
		unRegBroadcastReceiver();
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(ACTIONS.SR_RESULR_ACTION);
		mBroadcastReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String resultaction = intent.getAction();
				if(resultaction.equals(ACTIONS.SR_RESULR_ACTION)){
					//String value = intent.getStringExtra("resultCode").toString();
					Log.d("PlatformAdapterClient", "result======"+intent.toString());
					Log.d("PlatformAdapterClient", "result>>>>>>"+intent.getExtras());
					Log.d("PlatformAdapterClient", "resultCode======"+intent.getIntExtra("resultCode", 77));
					Log.d("PlatformAdapterClient", "resultDesc>>>>>>"+intent.getIntExtra("resultDescribe",-99));
					onVoiceResult(context,intent);
				}
			}};
			
			this.registerReceiver(mBroadcastReceiver, mIntentFilter);

	}
	
	private void unRegBroadcastReceiver() {
		// TODO Auto-generated method stub
		if (mBroadcastReceiver != null)
			this.unregisterReceiver(mBroadcastReceiver);
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("PlatformAdapterClient", "onCreate====regBroadcastReceiver()");
		mContext = this;
		//��ʼ��״̬��������ʶ��״̬
		updateContentProviderData(Constant.SR_STATUS_OFF);
		//����contentprovider���ݱ仯
		myObserver = new MyObserver(new Handler());
		mContext.getContentResolver().registerContentObserver(Uri.parse(Constant.SCENE_STATUS_CONTENTPROVIDER),
				true, myObserver);  
		regBroadcastReceiver();	

		boolean bind = bindTtsService();
		//
		//VoiceCommon.showVRFloatView(this);
	}



	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unRegBroadcastReceiver();
		if (connection != null) {
			try {
				releaseService();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}



	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}
	



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(Constant.DEBUG_TAG, "onStartCommand ");
		if(Constant.projecId==Constant.PROJECT_ID.ZT_B11B){
		
			if (intent != null) {		
				boolean isShow = true;
				boolean perfisShow = true;
				boolean intentisShow = intent.getBooleanExtra("isShow", true);
			/*	//�����������Ƿ���ʾ
				SharedPreferences  sPf = getSharedPreferences("sr_config",  Context.MODE_MULTI_PROCESS+Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		*/
				Context otherAppsContext = null;
				try {
					otherAppsContext = createPackageContext("com.suochao.mytest", Context.CONTEXT_IGNORE_SECURITY);
				} catch (NameNotFoundException e) {
					Log.d(Constant.DEBUG_TAG,Log.getStackTraceString(e));
				}
				SharedPreferences sPf = otherAppsContext.getSharedPreferences("sr_config", Context.MODE_MULTI_PROCESS+Context.MODE_WORLD_READABLE);
			
				if(sPf!=null){
					perfisShow =	sPf.getBoolean("isShow", true);//Ĭ������ʾ
				}
				Log.d(Constant.DEBUG_TAG, "perf  isShow :"+perfisShow);
				isShow  = perfisShow&&intentisShow;
				if(isShow)
				Log.d(Constant.DEBUG_TAG, "intent not null isShow :" + isShow);
				if (isShow) {
					// �����ǰ�ڵ����Ͳ���ʾ
					int SRISRUN = SceneStatus.canSR(mContext);
					if (SRISRUN == Constant.SR_AWAKE) {
						// VRFloatViewManager.getInstance(this).showVRFloat();
						VRWindowManager.createSmallWindow(mContext);
					}
				} else {
					// VRFloatViewManager.getInstance(this).hiddenVRFloat();
					VRWindowManager.removeSmallWindow(mContext);
				}
			} else {
				Log.d(Constant.DEBUG_TAG, "intent  null isShow :");
				// VRFloatViewManager.getInstance(this).showVRFloat();
			}
			
		}
		return super.onStartCommand(intent, flags, startId);
	}



	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


	//��������ʶ��״̬
	public void updateContentProviderData(int srStatus) {
		Uri uri = Uri.parse(Constant.SCENE_STATUS_CONTENTPROVIDER);
		ContentValues values = new ContentValues();
		values.put("value",srStatus);
		Log.e(Constant.DEBUG_TAG, "start update provider......");
		try {
			int dasdf = mContext.getContentResolver().update(uri, values, null, new String[] { "5" });
			Log.e(Constant.DEBUG_TAG, "updaterows:" + dasdf);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//����ʶ����������Ϣ
	public void onVoiceResult(Context context, Intent intent){
		if( intent.getAction() != null){
			String resultAction = intent.getAction();
			if(resultAction.equals(ACTIONS.SR_RESULR_ACTION)){
				Log.d(Constant.DEBUG_VoiceApplication, "start to recevice resultcode");
				//�����ص�����ʶ�����룬Ȼ�󲥱���112-119�����������
				int resultCode = intent.getIntExtra(Constant.SR_BROADCAST_EXTRA_RESULTCODE, 10);		
				Log.d(Constant.DEBUG_VoiceApplication, "resultcode:"+resultCode);		
				if(resultCode == Constant.SR_RES_SUCCEED){
					//������سɹ�
				}
				if(resultCode == Constant.SR_RES_FAILURE){
					//�������ʧ��
					int resultDescriptionCode = intent.getIntExtra(Constant.SR_BROADCAST_EXTRA_RESULRDESCRIBE, 10);		
					Log.d(Constant.DEBUG_VoiceApplication, "errresultcode"+resultDescriptionCode);
					switch (resultDescriptionCode){
					case Constant.SR_RES_NOSONG:
						ttsStartSpeak(Constant.SR_RES_STR_NOSONG);
						break;
					case Constant.SR_RES_NOTINMEDIA:
						ttsStartSpeak(Constant.SR_RES_STR_NOTINMEDIA);
						break;
					case Constant.SR_RES_NOTFINDMEDIADEVICE:
						ttsStartSpeak(Constant.SR_RES_STR_NOTFINDMEDIADEVICE);
						break;
					case Constant.SR_RES_NOTFINDMUSICFILE:
						ttsStartSpeak(Constant.SR_RES_STR_NOTFINDMUSICFILE);
						break;
					case Constant.SR_RES_NOTFINDIMGFILE:
						ttsStartSpeak(Constant.SR_RES_STR_NOTFINDIMGFILE);
						break;					
					case Constant.SR_RES_NOTFINDVIDEOFILE:
						ttsStartSpeak(Constant.SR_RES_STR_NOTFINDVIDEOFILE);
						break;
					case Constant.SR_RES_NOTPLAYING:
						ttsStartSpeak(Constant.SR_RES_STR_NOTPLAYING);
						break;
					case Constant.SR_RES_NOTREADYDEVICE:
						ttsStartSpeak(Constant.SR_RES_STR_NOTREADYDEVICE);
						break;	
					case Constant.SR_RES_NONSUPPORT:
						ttsStartSpeak(Constant.SR_RES_STR_NONSUPPORT);
					}
				}
				
				
			}
			
			//������������
			if(resultAction.equals(ACTIONS.SR_VOICEDIALING_ACTION)){
				//��������ʶ�����
				Intent opentIntent = new Intent();
				opentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				opentIntent.setComponent(new ComponentName("com.iflytek.autofly",
						"com.iflytek.autofly.SpeechActivity"));
				startActivity(opentIntent);
			}
		}
	
	}
	
	private class MyObserver extends ContentObserver {// ����
		public MyObserver(Handler handler) {
			super(handler);
		}
		// ��ContentProvier���ݷ����ı䣬�򴥷��ú���
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			//Log.e(Constant.DEBUG_TAG, "ContentProvier has changed");
			// ֪ͨ�Ƿ������������ʶ��
			Intent intenta = new Intent(ACTIONS.SR_RUN_ACTION);
		
			int SRISRUN = SceneStatus.canSR(mContext);
			Log.d(Constant.DEBUG_TAG, "is can start isself,voice,last srCanTag:"+selfChange+","+SRISRUN+","+srCanTag);
			intenta.putExtra(Constant.SR_AWAKESR_STR, SRISRUN);
			if (srCanTag != SRISRUN) {
				srCanTag = SRISRUN;
				int callbackValue = -1;
				if(srCanTag == 1){
					//�ָ���������
					callbackValue = PlatformCode.STATE_SPEECHON;
					VoiceCommon.showVRFloatView(mContext);
				}else if(srCanTag == 0){
					//������������
					callbackValue = PlatformCode.STATE_SPEECHOFF;
					VoiceCommon.hiddenVRFloatView(mContext);
				}
				Log.d(Constant.DEBUG_TAG, "notice voiceassistant can awake:"+callbackValue);
				if (PlatformService.platformCallback != null) {
					try {
						Log.d(Constant.DEBUG_TAG, "nn notice voiceassistant do:"+callbackValue);
						//PlatformService.platformCallback.systemStateChange(callbackValue);
						VoiceCommon.systemStateChange(callbackValue);
					} catch (Exception e) {
						Log.d(Constant.DEBUG_TAG, "PlatformService.platformCallback err:" +e.toString());
						e.printStackTrace();
					}
				}else{
					Log.d(Constant.DEBUG_TAG, "SrAwakeReceive callback is null");
				}
			}
		}
	}
	
	
	private static VoiceService mvs = null;
	/**TTSServer Start*/
	//��������
	//private String ttsTextStr = "";
	private TtsServiceConnection connection;
	private JSBDTtsService mJSBDTtsService;
	private JSBDTtsListener mJSBDTtsListener = new JSBDTtsListener.Stub() {
			@Override
			public void onTtsPlayState(int action) throws RemoteException {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(action);
			}
		};
		
	private final int TTS_SPEAK = 100;	
	private final int TTS_SPEAK_DELAY = 2000;//ms
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Log.e(Constant.DEBUG_TAG, "jsbdvoicetts speak complate ");
				// result.setText("�������");
				break;
			case 1:
				Log.e(Constant.DEBUG_TAG, "jsbdvoicetts speak start");
				// result.setText("������ʼ");

				break;
			case 2:
				Log.e(Constant.DEBUG_TAG, "jsbdvoicetts cut");
				// result.setText("�����ж�");
				break;
			case 3:
				Log.e(Constant.DEBUG_TAG, "jsbdvoicetts err");
				// result.setText("��������");
				break;
			case TTS_SPEAK:
				if(msg.obj!=null){

					ttsSpeak((String)msg.obj);
				}
				break;
			}
		}
	};
	
	public static VoiceService getInstance(){
		return mvs;
	}
	

	private boolean bindTtsService() {
		Log.d("ttsbind", "0000");
		connection = new TtsServiceConnection();
		Log.d("ttsbind", "11111");
		Intent intent = new Intent("com.jsbd.jsbdvoice.service.JSBDTtsService");
		//intent.setClassName("com.jsbd.voice.service", VoiceService.class.getName());
		boolean ret = getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
		Log.d("ttsbind", "2222 ret "+ret);
		return ret;
	}



	private void releaseService() {
		unbindService(connection);
		connection = null;
	}



	public void ttsStartSpeak(String sptext) {
		if (sptext.length() <= 0 || mJSBDTtsService == null) {
			return;
		}
		handler.removeMessages(TTS_SPEAK);
		Message msg = new Message();
		msg.what = TTS_SPEAK;
		msg.obj =  sptext;
		handler.sendMessageDelayed(msg, TTS_SPEAK_DELAY);
		
	}
	
	public void ttsSpeak(String sptext){
		try {
			// Log.e(Constant.DEBUG_TAG, "voicestatus:"+new
			// SceneStatus(mContext).getSrStatus());
			Log.e(Constant.DEBUG_TAG, "voice speak result :" + sptext);
			mJSBDTtsService.startSpeak(Constant.CALL_TTS_TAG, sptext);
			Log.e(Constant.DEBUG_TAG, "voice has speaked result " + sptext);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Log.e(Constant.DEBUG_TAG, "voice speak err");
			e.printStackTrace();
			// istopTag = false;
			// updateContentProviderData(0x00);//��ǰ��������״̬
		}
	}
	
	//�����
	 public class TtsServiceConnection implements ServiceConnection{

			@Override
			public void onServiceConnected(ComponentName name, IBinder boundService) {
				mJSBDTtsService =(JSBDTtsService)JSBDTtsService.Stub.asInterface((IBinder)boundService);
				if(mJSBDTtsService!=null){
					Log.d("ttsbind", "mJSBDTtsService isnot null");
				}else{
					Log.d("ttsbind", "mJSBDTtsService is null");
				}
				try {
					mJSBDTtsService.registerTtsListener(Constant.CALL_TTS_TAG, mJSBDTtsListener);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				mJSBDTtsService = null;
			}
		}
		/**TTSServer end*/
}
