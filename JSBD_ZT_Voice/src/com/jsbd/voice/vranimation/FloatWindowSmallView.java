package com.jsbd.voice.vranimation;

import java.lang.reflect.Field;

import com.iflytek.platformadapter.R;
import com.jsbd.voice.service.SerialService;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatWindowSmallView extends LinearLayout {

	private static final String tag = "Floatwind";
    private  ImageView image_floatView;  
    private  AnimationDrawable animationDrawable;
	/** 
     * ��¼С�������Ŀ�� 
     */  
    public static int viewWidth;  
  
    /** 
     * ��¼С�������ĸ߶� 
     */  
    public static int viewHeight;  
  
    /** 
     * ��¼ϵͳ״̬���ĸ߶� 
     */  
     private static int statusBarHeight;  
  
    /** 
     * ���ڸ���С��������λ�� 
     */  
    private WindowManager windowManager;  
  
    /** 
     * С�������Ĳ��� 
     */  
    private WindowManager.LayoutParams mParams;  
  
    /** 
     * ��¼��ǰ��ָλ������Ļ�ϵĺ�����ֵ 
     */  
    private float xInScreen;  
  
    /** 
     * ��¼��ǰ��ָλ������Ļ�ϵ�������ֵ 
     */  
    private float yInScreen;  
  
    /** 
     * ��¼��ָ����ʱ����Ļ�ϵĺ������ֵ 
     */  
    private float xDownInScreen;  
  
    /** 
     * ��¼��ָ����ʱ����Ļ�ϵ��������ֵ 
     */  
    private float yDownInScreen;  
  
    /** 
     * ��¼��ָ����ʱ��С��������View�ϵĺ������ֵ 
     */  
    private float xInView;  
  
    /** 
     * ��¼��ָ����ʱ��С��������View�ϵ��������ֵ 
     */  
    private float yInView;  
    
    private int screenWidth;
    private int screenHeight;
  
    
    
    public FloatWindowSmallView(Context context) {  
        super(context);  
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);  
        View view = findViewById(R.id.small_window_layout);  
    	image_floatView = new ImageView(context);  
        image_floatView.setImageResource(R.drawable.animatio_robot);  
        animationDrawable = (AnimationDrawable) image_floatView.getDrawable();  
        animationDrawable.start();  
        this.removeAllViews();
        this.addView(image_floatView);
        /*viewWidth = view.getLayoutParams().width;  
        viewHeight = view.getLayoutParams().height;*/  
        viewWidth = 140;  
        viewHeight = 160;  
        Log.d("vrfloat", "width,height:"+viewWidth+","+viewHeight);
        DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);  
        screenWidth = metric.widthPixels;     // ��Ļ��ȣ����أ�  
        screenHeight = metric.heightPixels;   // ��Ļ�߶ȣ����أ�  
        
       // TextView percentView = (TextView) findViewById(R.id.percent);  
       // percentView.setText(MyWindowManager.getUsedPercentValue(context));  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            // ��ָ����ʱ��¼��Ҫ����,�������ֵ����Ҫ��ȥ״̬���߶�  
            xInView = event.getX();  
            yInView = event.getY();  
            xDownInScreen = event.getRawX();  
            yDownInScreen = event.getRawY() - getStatusBarHeight();  
            xInScreen = event.getRawX();  
            yInScreen = event.getRawY() - getStatusBarHeight();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            xInScreen = event.getRawX();  
            yInScreen = event.getRawY() - getStatusBarHeight();  
            // ��ָ�ƶ���ʱ�����С��������λ��  
            updateViewPosition();  
            break;  
        case MotionEvent.ACTION_UP:  
            // �����ָ�뿪��Ļʱ��xDownInScreen��xInScreen��ȣ���yDownInScreen��yInScreen��ȣ�����Ϊ�����˵����¼���  
            if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {  
            	  SerialService.getInstance().callVoiceAssistant();
            }  
            
            //�����߿���
            absorbIn();
            Log.d(tag, "width:"+screenWidth+";height:"+screenHeight+";eventx:"+xInScreen);
            break;  
        default:  
            break;  
        }  
        return true;  
    }  
  
    //
    private void absorbIn() {
    	if(xInScreen<=screenWidth/2){
    		//�����ĻС����
    		xInScreen = 0;
    	}else if(xInScreen>screenWidth/2){
    		//�����ĻС����
    		xInScreen = screenWidth;
    	}
    	updateViewPosition();
	}

	/** 
     * ��С�������Ĳ������룬���ڸ���С��������λ�á� 
     *  
     * @param params 
     *            С�������Ĳ��� 
     */  
    public void setParams(WindowManager.LayoutParams params) {  
        mParams = params;  
    }  
  
    /** 
     * ����С����������Ļ�е�λ�á� 
     */  
    private void updateViewPosition() {  
        mParams.x = (int) (xInScreen - xInView);  
        mParams.y = (int) (yInScreen - yInView);  
        windowManager.updateViewLayout(this, mParams);  
    }  
  
    /** 
     * �򿪴���������ͬʱ�ر�С�������� 
     */  
    private void openBigWindow() {  
       // MyWindowManager.createBigWindow(getContext());  
     //   MyWindowManager.removeSmallWindow(getContext());  
    }  
  
    /** 
     * ���ڻ�ȡ״̬���ĸ߶ȡ� 
     *  
     * @return ����״̬���߶ȵ�����ֵ�� 
     */  
   private int getStatusBarHeight() {  
        if (statusBarHeight == 0) {  
            try {  
                Class<?> c = Class.forName("com.android.internal.R$dimen");  
                Object o = c.newInstance();  
                Field field = c.getField("status_bar_height");  
                int x = (Integer) field.get(o);  
                statusBarHeight = getResources().getDimensionPixelSize(x);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return statusBarHeight;  
    }  
}
