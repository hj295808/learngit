package com.jsbd.voice.dimens;

public class Constant {
public static final String DEBUG_TAG = "PlatformAdapterClient";
public static  final  String  DEBUG_VoiceApplication = "VoiceApplication";
//public static boolean 
//����tts�����Tag
public static final String CALL_TTS_TAG = "com.jsbd.jsbdvoice.srttsspeak";
//ϵͳ����
public static final int SR_OS_SET =70;
//�������ӽ���
public static final int SR_OS_BLUETOOTH =81;
//�绰���棨�򿪵绰��ʶ��
//public static final int SR_OS_PHONE =80;


//���ܶ��ƽ̨��ͬһ��������������������ò�ͬƽ̨����������
public static final PROJECT_ID projecId = PROJECT_ID.ZT_A02A;
public enum PROJECT_ID{
	ZT_B11B,
	ZT_A02A,
	ZT_B11A,
	ZT_B11F,
	ZT_B11A_C,
	ZT_B11F_IMAX6,
}

 public static int XFMIC_FUNC_RECORD = 1;//��ͨ¼������
 public static int XFMIC_FUNC_DENOSE = 2;//����ģʽ
 public static int XFMIC_FUNC_AWAKE = 5;//����
 public static int XFMIC_FUNC_CALL_ECHO_CANCEL = -99;//ͨ����������,Ԥ������
 
//ý�����
public static final int SR_MUSIC_SEARCH =10;
public static final int SR_MUSIC_MUSIC_PLAY =11;
public static final int SR_IMAGE_PLAY =20;
public static final int SR_VIDEO_PLAY =30;
public static final int SR_MEDIA_PAUSE =40;
public static final int SR_MEDIA_PLAY =41;
public static final int SR_MEDIA_NEXT =42;
public static final int SR_MEDIA_PREVIOUS =43;
public static final int SR_RANDOM_MODE =50;
public static final int SR_REPEAT_OFF_MODE =51;
public static final int SR_REPEAT_ALL_MODE =52;
public static final int SR_REPEAT_ONE_MODE =53;
public static final int SR_REPEAT_FILE_MODE =54;

//�����绰
public static final int SR_PHONE_CONTACTS = 60;
public static final int SR_PHONE_HISTORYCALL = 61;
public static final int SR_PHONE_DIAL = 62;
public static final int SR_PHONE_CALL = 63;

//�򿪽���
public static final int SR_LAUNCHER_BT = 73;//��������
public static final int SR_LAUNCHER_CARINFOR = 72;//������Ϣ
public static final int SR_LAUNCHER_TIRE = 75;//̥ѹ����
public static final int SR_LAUNCHER_APP = 74;//Ӧ�ý���
public static final int SR_LAUNCHER_MEDIA = 71;//ý�����
public static final int SR_LAUNCHER_SEATHOT = 76;//���μ���
public static final int SR_LAUNCHER_SEATFAN = 77;//����ͨ��
public static final int SR_LAUNCHER_PM = 79;//pm2.5 ��ʾ
public static final int SR_LAUNCHER_PM_CLOSE = 80;//pm2.5 �ر�

public static final int SR_LAUNCHER_MUSIC = 14;//���ֽ���
public static final int SR_LAUNCHER_VIDEO = 20;//��Ƶ����
public static final int SR_LAUNCHER_IMAGE = 21;//ͼƬ����
public static final int SR_LAUNCHER_RADIO = 13;//����������
public static final int SR_LAUNCHER_AUX = 10;//AUX����
public static final int SR_LAUNCHER_IPOD = 12;//IPod ����
public static final int SR_LAUNCHER_BTMUSIC = 11;//�������ֽ���
public static final int SR_LAUNCHER_NAV = 22;//����
public static final int SR_LAUNCHER_PHONELINK = 23;//�ֻ�����


//���ش�����
public static final int SR_RES_FAILURE =0;
public static final int SR_RES_SUCCEED =1;
public static final int SR_RES_TAG_SUCCEED = 111;
public static final String SR_RES_STR_NOSONG = "δ�ҵ���˵�ĸ���";
public static final int SR_RES_NOSONG =112;
public static final String SR_RES_STR_NOTINMEDIA = "��ǰ���ڶ�ý��ģʽ";
public static final int SR_RES_NOTINMEDIA =113;
public static final String SR_RES_STR_NOTFINDMEDIADEVICE = "û���ҵ�ý���豸";
public static final int SR_RES_NOTFINDMEDIADEVICE =114;
public static final String SR_RES_STR_NOTFINDMUSICFILE= "û���ҵ������ļ�";
public static final int SR_RES_NOTFINDMUSICFILE =115;
public static final String SR_RES_STR_NOTFINDIMGFILE = "û���ҵ�ͼƬ�ļ�";
public static final int SR_RES_NOTFINDIMGFILE=116;
public static final String SR_RES_STR_NOTFINDVIDEOFILE = "û���ҵ���Ƶ�ļ�";
public static final int SR_RES_NOTFINDVIDEOFILE =117;
public static final String SR_RES_STR_NOTPLAYING = "��ǰý�岻�ڲ���״̬";
public static final int SR_RES_NOTPLAYING =118;
public static final String SR_RES_STR_NOTREADYDEVICE = "�豸δ׼����";
public static final int SR_RES_NOTREADYDEVICE=119;
public static final String SR_RES_STR_NONSUPPORT = "��ǰģʽ��֧�ָò���";
public static final int SR_RES_NONSUPPORT=120;

//������ʶ����
public static final int SR_STATUS_OFF = 0;
//����ʶ����
public static final int SR_STATUS_IN = 1;

//֪ͨ�����ʱ�Ƿ���Ա���������
public static final String SR_CAN_AWAKE_ACTION = "com.jsbdtek.awakesr.action";
//����ʶ���Ƿ������������
public static final String SR_AWAKESR_STR = "awakeSr";
//����ʶ��ǰ���ܻ���
public static final int SR_SLEEP =0;
//����ʶ��ǰ���Ա�����
public static final int SR_AWAKE = 1;
//�����绰���ش�����
public static final String SR_RES_STR_NOCONNECTBLUETOOTH = "�����ֻ�������������";
public static final int SR_RES_NOCONNECTBLUETOOTH =220;

//�㲥����������
public static final String SR_BROADCAST_EXTRA_COMMANDCODE ="commandCode";
public static final String SR_BROADCAST_EXTRA_SONGNAME ="songName";
public static final String SR_BROADCAST_EXTRA_RESULTCODE ="resultCode";
public static final String SR_BROADCAST_EXTRA_RESULRDESCRIBE ="resultDescribe";


//�Զ��峵���ڲ�������ΪӦ�õĳ���
public static final String SR_NAME_APP_SELFMACHINE = "������|���|����|��Ƶ|ͼƬ|������|�㲥|��̨|����|��ͼ|����|��ý��|ý��|Ӧ��|����|������Ϣ|̥ѹ|̥ѹ����|��Ӱ|usb|sd|����|ipod|��������|�����绰|�绰|ͨ����¼|�绰��|���Ű�|������|�绰��|�绰��|ͨѶ¼|u��|sd��|����|aux|AUX|Aux|auxin|Ipod|IPOD|iPod|�ֻ�����|����|��Ƶ|����������Ϣ��ʾ|pm��������Ϣ��ʾ";
//���˳���Ӧ��
public static final String SR_NAME_APP_NO = "�����";
//ģʽ����
public enum SR_MODEL_TYPE{
	NULL,
	USB,//usb����sdģʽ
	RADIO,//������
	NAV,//����
	BLUTOOTH,//����
	IPOD,//ipod
	
	SD,
	AUX,
	PHONE_LINK,USB2,USB3
	
}
//�Զ���ʻ�
public static final String[] SR_CUSTOM_APP_DICT ={"��Ƶ","������Ƶ","ͼƬ","���ͼƬ","�鿴ͼƬ","����ͼƬ","��������","�򿪵���","�򿪵�ͼ"};

//ContentProvider
public static final String SCENE_STATUS_CONTENTPROVIDER = "content://com.haoke.status.provider/word/status";

//ҵ�����ͣ�focus��ö��
public enum FocusType{
	NULL,
	MUSIC,
	RADIO,
	CMD,
	APP,
	CARCONTROL,
	VEHICLEINFO,
	AIRCONTROL,
	MESSAGE,
	STOCK,//��Ʊ��ѯ
	WEATHER,//������ѯ
	FLIGHT,//��Ʊ��ѯ
	TRAIN,//��Ʊ��ѯ
	NEWS,//����
	PM25//��������	
}

//�����յ�����ö��ָ��
public enum AirControlEnum {
NONE,//������0
ON,//1�յ���
OFF,//2�յ���
LEFT_DOWN,//3���¶�-
LEFT_UP,//4���¶�+
RIGHT_DOWN,//5���¶�-
RIGHT_UP,//6���¶�+
SPEED_DOWN,//7����-
SPEED_UP,//8����+
LEFT_SEAT_HEAT,//9�����μ���
LEFT_SEAT_COOL,//10����������
RIGHT_SEAT_HEAT,//11�����μ���
RIGHT_SEAT_COOL,//12����������
SETTING,//13�趨
DUAL_ON,//14DUAL ��
DUAL_OFF,//15DUAL ��
IONS_AIR_ON,//16������ ��
IONS_AIR_OFF,//17������ ��
FRONT_DEFROST_ON,//18ǰ�����˪ ��
FRONT_DEFROST_OFF,//19ǰ�����˪ ��
FRONT_DEFROST_STRONG_ON,//20ǰ����ǿ����˪ ��
FRONT_DEFROST_STRONG_OFF,//21ǰ����ǿ����˪ ��
REAR_DEFROST_ON,//22�󵲷��˪ ��
REAR_DEFROST_OFF,//23�󵲷��˪ ��
REAR_DEFROST_STRONG_ON,//24�󵲷�ǿ����˪ ��
REAR_DEFROST_STRONG_OFF,//25�󵲷�ǿ����˪ ��
AUTO_ON,//26 AUTO ��
AUTO_OFF,//27 AUTO ��
COOL_AIR_ON,//28���� ��
COOL_AIR_OFF,//29���� ��
COOL_MAX_AIR_ON,//30������� ��
COOL_MAX_AIR_OFF,//31������� ��
HEAT_AIR_ON,//32���� ��
HEAT_AIR_OFF,//33���� ��
HEAT_MAX_AIR_ON,//34������� ��
HEAT_MAX_AIR_OFF,//35������� ��
CYCLE_INSIDE,//36��ѭ��
CYCLE_OUTSIDE,//37��ѭ��
DRY_AIR_ON,//38��ʪ��
DRY_AIR_OFF,//39��ʪ��
UNDER_AIR,//40�����ͷ�
FLAT_AIR,//41ƽ���ͷ�
FLATANDUNDER_AIR,//42ƽ���������ͷ�
UPWARD_AIR,//43�����ͷ�
UPWARDBYUNDER_AIR,//44�����������ͷ�
UPWARDANDFLAT_AIR,//45������ƽ���ͷ�
UPANDUNDERANDFLAT_AIR,//46������������ƽ���ͷ�
AUTO_WIND_AIR,//47�Զ�����ģʽ
TEMPE_UP,//48�¶ȼ�xxx��(byte10)
TEMPE_DOWN,//49�¶ȼ���xxx��(byte10)
TEMPE_SET,//50�¶��趨(byte10)
SPEED_SET,//51�����趨(byte10)
SPEED_UP_VALUE,//52��������xx
SPEED_DOWN_VALUE,//53���ټ�Сxx
SPEED_MAX,//54�������
SPEED_MIN,//55������С
SEAT_LEFT_VENTILATION,//56������ͨ��
SEAT_RIGHT_VENTILATION,//57������ͨ��	
CYCLE_AUTO_ON,//58ѭ���Զ���
CYCLE_AUTO_OFF,//59ѭ���Զ���
AIR_SELFTEST_ON,//60�յ��Լ쿪
AIR_SELFTEST_OFF,//61�յ��Լ��
WIND_FONT_HEAT_ON,//62ǰ������ȿ�
WIND_FONT_HEAT_OFF,//63ǰ������ȹ�
AIR_BACK_TEMPE_SET,//64���ſյ��¶ȵ��ڣ������� byte10��
AIR_BACK_SPEED_SET,//65���ſյ����ٵ��ڣ������� byte10��
TEMPE_LEFT_SET,//66���¶��趨(byte10)
TEMPE_RIGHT_SET,//67���¶��趨(byte10
}
}
