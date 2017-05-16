package com.jsbd.voice.vranimation;

import com.iflytek.platformadapter.PlatformAdapterApplication;
import com.iflytek.platformadapter.R;
import com.jsbd.voice.service.SerialService;

import android.R.color;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

public class VRFloatViewManager {

	private static VRFloatViewManager vrFloatViewManager = null;
	private Context mContext = null;
	private VRFloatView vrFloatView = null;
	private final String  Tag = "vrfloat";
	


	public static boolean isAdded = false; // 是否已增加悬浮窗  
    private static WindowManager wm;  
    private static WindowManager.LayoutParams params;  
    private static ImageView image_floatView;  
    private static AnimationDrawable animationDrawable;
	public VRFloatViewManager(Context context) {
		this.mContext = context;	
		//initVRFloatView();
	}
	
	public static VRFloatViewManager getInstance(Context context){
		if(vrFloatViewManager==null){
			vrFloatViewManager = new VRFloatViewManager(context);
		
		}
		return vrFloatViewManager;
	}
	
	public void initVRFloatView(){
		if(image_floatView!=null){return;}
    	image_floatView = new ImageView(this.mContext.getApplicationContext());  
    	//btn_floatView.setText("悬浮窗");  
    	//image_floatView.setBackgroundResource(R.drawable.ic_launcher);
    	image_floatView.setImageResource(R.drawable.animatio_robot);  
         animationDrawable = (AnimationDrawable) image_floatView.getDrawable();  
         animationDrawable.start();  
         wm = (WindowManager) this.mContext.getApplicationContext()  
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
         //params.gravity =  Gravity.RIGHT | Gravity.BOTTOM;
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
          //记录当前手指位置在屏幕上的横坐标值 
          private float xInScreen;  
          // 记录当前手指位置在屏幕上的纵坐标值 
          private float yInScreen;  
          //记录手指按下时在屏幕上的横坐标的值 
          private float xDownInScreen;  
          // 记录手指按下时在屏幕上的纵坐标的值 
          private float yDownInScreen;  
          //记录手指按下时在小悬浮窗的View上的横坐标的值 
          private float xInView;  
          //记录手指按下时在小悬浮窗的View上的纵坐标的值 
          private float yInView;  
   	      /** 
           * 更新小悬浮窗在屏幕中的位置。 
          */  
          private void updateViewPosition() {  
        	 /* Log.d("vrfloat","update params.x:"+params.x+";params.y:"+params.y);
          	params.x = (int) (xInScreen - xInView);  
          	params.y = (int) (yInScreen - yInView);  */
          	wm.updateViewLayout(image_floatView, params);  
          }  
            
          public boolean onTouch(View v, MotionEvent event) {  
              switch(event.getAction()) {  
              case MotionEvent.ACTION_DOWN:  
                  lastX = (int) event.getRawX();  
                  lastY = (int) event.getRawY();  
                  paramX = params.x;  
                  paramY = params.y;  
                  Log.d("vrfloat","lastX:"+lastX+";lastY:"+lastY+";paramX:"+paramX+";paramY:"+paramY);
            	/*    xInView = event.getX();  
                    yInView = event.getY();  
                    xDownInScreen = event.getRawX();  
                    yDownInScreen = event.getRawY(); //- getStatusBarHeight();  
                    xInScreen = event.getRawX();  
                    yInScreen = event.getRawY() ;//- getStatusBarHeight();  
             	   Log.d("vrfloat","getX()"+event.getX()+";getY():"+event.getY()+";getRawX()"+event.getX()+";getRawY():"+event.getY());*/
                  break;  
              case MotionEvent.ACTION_MOVE:  
                  int dx = (int) event.getRawX() - lastX;  
                  int dy = (int) event.getRawY() - lastY;  
                  params.x = paramX + dx;  
                  params.y = paramY + dy;  
                  updateViewPosition();
                 // Log.d("vrfloat","params.x:"+params.x+";params.y:"+params.y+";dx:"+dx+";dy:"+dy);
                  // 更新悬浮窗位置  
                 // wm.updateViewLayout(image_floatView, params);  
            	/*  if(xInScreen==event.getRawX()&&  yInScreen == event.getRawY() ){
            		  break;
            	  }
            	   xInScreen = event.getRawX();  
                   yInScreen = event.getRawY() ;//- getStatusBarHeight();  
                
                   Log.d("vrfloat","move getX()"+event.getX()+";getY():"+event.getY()+";getRawX()"+event.getX()+";getRawY():"+event.getY());*/
                  break;  
              case MotionEvent.ACTION_UP:  
                  // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。  
                 if ((int) event.getRawX() == lastX &&  (int) event.getRawY() == lastY) {
            	 // if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {  
                     // openBigWindow();
                	  SerialService.getInstance().callVoiceAssistant();
                	  Log.d("vrfloat", "on click ");
                	  
                  }  
                  break;  
              }  
              return true;  
          }  
      });  
	}
	
	public void showVRFloat(){
		if(!isAdded){
			Log.d(Tag, "is not add ,to add  ");
			if(image_floatView==null){

				this.initVRFloatView();
				
			}else{
				
			}
			if(wm!=null){
	         wm.addView(image_floatView, params);
	         wm.updateViewLayout(image_floatView, params);  
	         isAdded = true;
			}
			Log.d(Tag, "is added  ,isadded= "+isAdded);
		}else{
			
		}
	}
	
	public void hiddenVRFloat(){
		
		if(isAdded){
			Log.d(Tag, "is added , to remove ");
			if(wm!=null){
				wm.removeView(image_floatView);
				isAdded = false;
				Log.d(Tag, "is remove , isAdded ="+isAdded);
			}
		}
	}
	
	

}
