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


	public static boolean isAdded = false; // �Ƿ�������������  
    private static WindowManager wm;  
    private static WindowManager.LayoutParams params;  
    private static ImageView image_floatView;  
    private static AnimationDrawable animationDrawable;
    /** 
     * ����һ��С����������ʼλ��Ϊ��Ļ���Ҳ��м�λ�á� 
     *  
     * @param context 
     *            ����ΪӦ�ó����Context. 
     */  
    public static void createSmallWindow(Context context) {   
         //View view =  LayoutInflater.from(context).inflate(R.layout.float_window_small, this);  
           //View view =context.findViewById(R.id.small_window_layout);  
    	if(image_floatView!=null){return;}
    	image_floatView = new ImageView(context.getApplicationContext());  
    	//btn_floatView.setText("������");  
    	//image_floatView.setBackgroundResource(R.drawable.ic_launcher);
    	image_floatView.setImageResource(R.drawable.animatio_robot);  
         animationDrawable = (AnimationDrawable) image_floatView.getDrawable();  
         animationDrawable.start();  
         wm = (WindowManager) context.getApplicationContext()  
          .getSystemService(Context.WINDOW_SERVICE);  
         params = new WindowManager.LayoutParams();  
           
         // ����window type  
         params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;  
         /* 
          * �������Ϊparams.type = WindowManager.LayoutParams.TYPE_PHONE; 
          * ��ô���ȼ��ή��һЩ, ������֪ͨ�����ɼ� 
          */  
           
         params.format = PixelFormat.RGBA_8888; // ����ͼƬ��ʽ��Ч��Ϊ����͸��  
           
         // ����Window flag  
         params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL  
                               | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;  
         /* 
          * �����flags���Ե�Ч����ͬ���������� 
          * ���������ɴ������������κ��¼�,ͬʱ��Ӱ�������¼���Ӧ�� 
         wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL 
                                | LayoutParams.FLAG_NOT_FOCUSABLE 
                                | LayoutParams.FLAG_NOT_TOUCHABLE; 
          */  
           
         // �����������ĳ��ÿ�  
         params.width = 240;  
         params.height = 320;  
           
         // ������������Touch����  
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
                  // ����������λ��  
                  wm.updateViewLayout(image_floatView, params);  
                  break;  
              case MotionEvent.ACTION_UP:  
                  // �����ָ�뿪��Ļʱ��xDownInScreen��xInScreen��ȣ���yDownInScreen��yInScreen��ȣ�����Ϊ�����˵����¼���  
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
