package com.jsbd.voice.vranimation;

import com.iflytek.platformadapter.R;
import com.jsbd.voice.service.SerialService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class FloatViewActivity extends Activity {

	public FloatViewActivity() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//VRWindowManager.createSmallWindow(this);
		 Intent getIntent =  getIntent();
		 boolean isShow = false;
		 if(getIntent!=null){
			 isShow = getIntent.getBooleanExtra("isShow", false);
		 }
		 Log.d("vrfloat", "isshow "+isShow);
		 if(isShow){
			 showWindow(this);
		 }else{
			 hiddenWindow(this);
		 }
		 
		this.finish();
	}


	public static boolean isAdded = false; // 是否已增加悬浮窗  
    private static WindowManager wm;  
    private static WindowManager.LayoutParams params;  
    private static ImageView image_floatView;  
    private static AnimationDrawable animationDrawable;
    /** 
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。 
     *  
     * @param context 
     *            必须为应用程序的Context. 
     */  
    public static void createSmallWindow(Context context) {   
         //View view =  LayoutInflater.from(context).inflate(R.layout.float_window_small, this);  
           //View view =context.findViewById(R.id.small_window_layout);  
    	if(image_floatView!=null){return;}
    	image_floatView = new ImageView(context.getApplicationContext());  
    	//btn_floatView.setText("悬浮窗");  
    	//image_floatView.setBackgroundResource(R.drawable.ic_launcher);
    	image_floatView.setImageResource(R.drawable.animatio_robot);  
         animationDrawable = (AnimationDrawable) image_floatView.getDrawable();  
         animationDrawable.start();  
         wm = (WindowManager) context.getApplicationContext()  
          .getSystemService(Context.WINDOW_SERVICE);  
         params = new WindowManager.LayoutParams();  
           
         // 设置window type  
         params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;  
         /* 
          * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 
          * 那么优先级会降低一些, 即拉下通知栏不可见 
          */  
           
         params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明  
           
         // 设置Window flag  
         params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL  
                               | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;  
         /* 
          * 下面的flags属性的效果形同“锁定”。 
          * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。 
         wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL 
                                | LayoutParams.FLAG_NOT_FOCUSABLE 
                                | LayoutParams.FLAG_NOT_TOUCHABLE; 
          */  
           
         // 设置悬浮窗的长得宽  
         params.width = 240;  
         params.height = 320;  
           
         // 设置悬浮窗的Touch监听  
         image_floatView.setOnTouchListener(new OnTouchListener() {  
          int lastX, lastY;  
          int paramX, paramY;  
            
          public boolean onTouch(View v, MotionEvent event) {  
              switch(event.getAction()) {  
              case MotionEvent.ACTION_DOWN:  
                  lastX = (int) event.getRawX();  
                  lastY = (int) event.getRawY();  
                  paramX = params.x;  
                  paramY = params.y;  
                  break;  
              case MotionEvent.ACTION_MOVE:  
                  int dx = (int) event.getRawX() - lastX;  
                  int dy = (int) event.getRawY() - lastY;  
                  params.x = paramX + dx;  
                  params.y = paramY + dy;  
                  // 更新悬浮窗位置  
                  wm.updateViewLayout(image_floatView, params);  
                  break;  
              case MotionEvent.ACTION_UP:  
                  // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。  
                  if ((int) event.getRawX() == lastX &&  (int) event.getRawY() == lastY) {  
                     // openBigWindow();
                	  SerialService.getInstance().callVoiceAssistant();
                	  Log.d("vrfloat", "on click ");
                	  
                  }  
                  break;  
              }  
              return true;  
          }  
      });  
           
         wm.addView(image_floatView, params);  
         wm.updateViewLayout(image_floatView, params);
    }  
    
    private static void showTost() {
    	 
	}


	public static void showWindow(Context context) {  
		if(!isAdded){
	    	if(wm!=null&&image_floatView!=null){
	    		//wm.addView(image_floatView);
	    		wm.addView(image_floatView, params);
	    	}else{
	    		createSmallWindow(context);
	    	}

    		isAdded = true;
		}else{
			
		}
    }
    
    public static void hiddenWindow(Context context) {  
    	if(wm!=null&&image_floatView!=null){
    		wm.removeView(image_floatView);
    		isAdded = false;
    	}
    	
    }
}
