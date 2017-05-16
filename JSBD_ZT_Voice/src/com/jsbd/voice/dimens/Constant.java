package com.jsbd.voice.dimens;

public class Constant {
public static final String DEBUG_TAG = "PlatformAdapterClient";
public static  final  String  DEBUG_VoiceApplication = "VoiceApplication";
//public static boolean 
//调用tts传入的Tag
public static final String CALL_TTS_TAG = "com.jsbd.jsbdvoice.srttsspeak";
//系统设置
public static final int SR_OS_SET =70;
//蓝牙连接界面
public static final int SR_OS_BLUETOOTH =81;
//电话界面（打开电话的识别）
//public static final int SR_OS_PHONE =80;


//可能多个平台用同一个适配器，这个用来配置不同平台的特殊需求
public static final PROJECT_ID projecId = PROJECT_ID.ZT_A02A;
public enum PROJECT_ID{
	ZT_B11B,
	ZT_A02A,
	ZT_B11A,
	ZT_B11F,
	ZT_B11A_C,
	ZT_B11F_IMAX6,
}

 public static int XFMIC_FUNC_RECORD = 1;//普通录音功能
 public static int XFMIC_FUNC_DENOSE = 2;//降噪模式
 public static int XFMIC_FUNC_AWAKE = 5;//唤醒
 public static int XFMIC_FUNC_CALL_ECHO_CANCEL = -99;//通话回声消除,预留功能
 
//媒体控制
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

//蓝牙电话
public static final int SR_PHONE_CONTACTS = 60;
public static final int SR_PHONE_HISTORYCALL = 61;
public static final int SR_PHONE_DIAL = 62;
public static final int SR_PHONE_CALL = 63;

//打开界面
public static final int SR_LAUNCHER_BT = 73;//蓝牙界面
public static final int SR_LAUNCHER_CARINFOR = 72;//车辆信息
public static final int SR_LAUNCHER_TIRE = 75;//胎压界面
public static final int SR_LAUNCHER_APP = 74;//应用界面
public static final int SR_LAUNCHER_MEDIA = 71;//媒体界面
public static final int SR_LAUNCHER_SEATHOT = 76;//座椅加热
public static final int SR_LAUNCHER_SEATFAN = 77;//座椅通风
public static final int SR_LAUNCHER_PM = 79;//pm2.5 显示
public static final int SR_LAUNCHER_PM_CLOSE = 80;//pm2.5 关闭

public static final int SR_LAUNCHER_MUSIC = 14;//音乐界面
public static final int SR_LAUNCHER_VIDEO = 20;//视频界面
public static final int SR_LAUNCHER_IMAGE = 21;//图片界面
public static final int SR_LAUNCHER_RADIO = 13;//收音机界面
public static final int SR_LAUNCHER_AUX = 10;//AUX界面
public static final int SR_LAUNCHER_IPOD = 12;//IPod 界面
public static final int SR_LAUNCHER_BTMUSIC = 11;//蓝牙音乐界面
public static final int SR_LAUNCHER_NAV = 22;//导航
public static final int SR_LAUNCHER_PHONELINK = 23;//手机互联


//返回处理码
public static final int SR_RES_FAILURE =0;
public static final int SR_RES_SUCCEED =1;
public static final int SR_RES_TAG_SUCCEED = 111;
public static final String SR_RES_STR_NOSONG = "未找到您说的歌曲";
public static final int SR_RES_NOSONG =112;
public static final String SR_RES_STR_NOTINMEDIA = "当前不在多媒体模式";
public static final int SR_RES_NOTINMEDIA =113;
public static final String SR_RES_STR_NOTFINDMEDIADEVICE = "没有找到媒体设备";
public static final int SR_RES_NOTFINDMEDIADEVICE =114;
public static final String SR_RES_STR_NOTFINDMUSICFILE= "没有找到音乐文件";
public static final int SR_RES_NOTFINDMUSICFILE =115;
public static final String SR_RES_STR_NOTFINDIMGFILE = "没有找到图片文件";
public static final int SR_RES_NOTFINDIMGFILE=116;
public static final String SR_RES_STR_NOTFINDVIDEOFILE = "没有找到视频文件";
public static final int SR_RES_NOTFINDVIDEOFILE =117;
public static final String SR_RES_STR_NOTPLAYING = "当前媒体不在播放状态";
public static final int SR_RES_NOTPLAYING =118;
public static final String SR_RES_STR_NOTREADYDEVICE = "设备未准备好";
public static final int SR_RES_NOTREADYDEVICE=119;
public static final String SR_RES_STR_NONSUPPORT = "当前模式不支持该操作";
public static final int SR_RES_NONSUPPORT=120;

//非语音识别中
public static final int SR_STATUS_OFF = 0;
//语音识别中
public static final int SR_STATUS_IN = 1;

//通知助理此时是否可以被语音唤醒
public static final String SR_CAN_AWAKE_ACTION = "com.jsbdtek.awakesr.action";
//语音识别是否可以语音唤醒
public static final String SR_AWAKESR_STR = "awakeSr";
//语音识别当前不能唤醒
public static final int SR_SLEEP =0;
//语音识别当前可以被唤醒
public static final int SR_AWAKE = 1;
//蓝牙电话返回处理码
public static final String SR_RES_STR_NOCONNECTBLUETOOTH = "请与手机蓝牙建立连接";
public static final int SR_RES_NOCONNECTBLUETOOTH =220;

//广播附带参数名
public static final String SR_BROADCAST_EXTRA_COMMANDCODE ="commandCode";
public static final String SR_BROADCAST_EXTRA_SONGNAME ="songName";
public static final String SR_BROADCAST_EXTRA_RESULTCODE ="resultCode";
public static final String SR_BROADCAST_EXTRA_RESULRDESCRIBE ="resultDescribe";


//自定义车机内部可能作为应用的常量
public static final String SR_NAME_APP_SELFMACHINE = "播放器|相册|设置|视频|图片|收音机|广播|电台|导航|地图|音乐|多媒体|媒体|应用|蓝牙|车辆信息|胎压|胎压界面|电影|usb|sd|优盘|ipod|蓝牙音乐|蓝牙电话|电话|通话记录|电话本|拨号板|拨号盘|电话簿|电话薄|通讯录|u盘|sd卡|蓝牙|aux|AUX|Aux|auxin|Ipod|IPOD|iPod|手机互联|亿连|音频|空气质量信息显示|pm二点五信息显示";
//过滤车机应用
public static final String SR_NAME_APP_NO = "浏览器";
//模式类型
public enum SR_MODEL_TYPE{
	NULL,
	USB,//usb或者sd模式
	RADIO,//收音机
	NAV,//导航
	BLUTOOTH,//蓝牙
	IPOD,//ipod
	
	SD,
	AUX,
	PHONE_LINK,USB2,USB3
	
}
//自定义词汇
public static final String[] SR_CUSTOM_APP_DICT ={"视频","播放视频","图片","浏览图片","查看图片","播放图片","打开收音机","打开导航","打开地图"};

//ContentProvider
public static final String SCENE_STATUS_CONTENTPROVIDER = "content://com.haoke.status.provider/word/status";

//业务类型（focus）枚举
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
	STOCK,//股票查询
	WEATHER,//天气查询
	FLIGHT,//机票查询
	TRAIN,//火车票查询
	NEWS,//新闻
	PM25//控制质量	
}

//语音空调控制枚举指令
public enum AirControlEnum {
NONE,//不设置0
ON,//1空调开
OFF,//2空调关
LEFT_DOWN,//3左温度-
LEFT_UP,//4左温度+
RIGHT_DOWN,//5右温度-
RIGHT_UP,//6右温度+
SPEED_DOWN,//7风速-
SPEED_UP,//8风速+
LEFT_SEAT_HEAT,//9左座椅加热
LEFT_SEAT_COOL,//10左座椅制冷
RIGHT_SEAT_HEAT,//11右座椅加热
RIGHT_SEAT_COOL,//12右座椅制冷
SETTING,//13设定
DUAL_ON,//14DUAL 开
DUAL_OFF,//15DUAL 关
IONS_AIR_ON,//16负离子 开
IONS_AIR_OFF,//17负离子 关
FRONT_DEFROST_ON,//18前挡风除霜 开
FRONT_DEFROST_OFF,//19前挡风除霜 关
FRONT_DEFROST_STRONG_ON,//20前挡风强力除霜 开
FRONT_DEFROST_STRONG_OFF,//21前挡风强力除霜 关
REAR_DEFROST_ON,//22后挡风除霜 开
REAR_DEFROST_OFF,//23后挡风除霜 关
REAR_DEFROST_STRONG_ON,//24后挡风强力除霜 开
REAR_DEFROST_STRONG_OFF,//25后挡风强力除霜 关
AUTO_ON,//26 AUTO 开
AUTO_OFF,//27 AUTO 关
COOL_AIR_ON,//28制冷 开
COOL_AIR_OFF,//29制冷 关
COOL_MAX_AIR_ON,//30最大制冷 开
COOL_MAX_AIR_OFF,//31最大制冷 关
HEAT_AIR_ON,//32制热 开
HEAT_AIR_OFF,//33制热 关
HEAT_MAX_AIR_ON,//34最大制热 开
HEAT_MAX_AIR_OFF,//35最大制热 关
CYCLE_INSIDE,//36内循环
CYCLE_OUTSIDE,//37外循环
DRY_AIR_ON,//38除湿开
DRY_AIR_OFF,//39除湿关
UNDER_AIR,//40向下送风
FLAT_AIR,//41平行送风
FLATANDUNDER_AIR,//42平行与向下送风
UPWARD_AIR,//43向上送风
UPWARDBYUNDER_AIR,//44向上与向下送风
UPWARDANDFLAT_AIR,//45向上与平行送风
UPANDUNDERANDFLAT_AIR,//46向上与向下与平行送风
AUTO_WIND_AIR,//47自动吹风模式
TEMPE_UP,//48温度加xxx度(byte10)
TEMPE_DOWN,//49温度减少xxx度(byte10)
TEMPE_SET,//50温度设定(byte10)
SPEED_SET,//51风速设定(byte10)
SPEED_UP_VALUE,//52风速增加xx
SPEED_DOWN_VALUE,//53风速减小xx
SPEED_MAX,//54风速最大
SPEED_MIN,//55风速最小
SEAT_LEFT_VENTILATION,//56左座椅通风
SEAT_RIGHT_VENTILATION,//57右座椅通风	
CYCLE_AUTO_ON,//58循环自动开
CYCLE_AUTO_OFF,//59循环自动关
AIR_SELFTEST_ON,//60空调自检开
AIR_SELFTEST_OFF,//61空调自检关
WIND_FONT_HEAT_ON,//62前挡风加热开
WIND_FONT_HEAT_OFF,//63前挡风加热关
AIR_BACK_TEMPE_SET,//64后排空调温度调节（带参数 byte10）
AIR_BACK_SPEED_SET,//65后排空调风速调节（带参数 byte10）
TEMPE_LEFT_SET,//66左温度设定(byte10)
TEMPE_RIGHT_SET,//67右温度设定(byte10
}
}
