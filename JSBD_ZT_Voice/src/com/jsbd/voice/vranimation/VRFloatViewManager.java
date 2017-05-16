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
	


	public static boolean isAdded = false; // �Ƿ�������������  
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
    	//btn_floatView.setText("������");  
    	//image_floatView.setBackgroundResource(R.drawable.ic_launcher);
    	image_floatView.setImageResource(R.drawable.animatio_robot);  
         animationDrawable = (AnimationDrawable) image_floatView.getDrawable();  
         animationDrawable.start();  
         wm = (WindowManager) this.mContext.getApplicationContext()  
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
         //params.gravity =  Gravity.RIGHT | Gravity.BOTTOM;
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
          //��¼��ǰ��ָλ������Ļ�ϵĺ�����ֵ 
          private float xInScreen;  
          // ��¼��ǰ��ָλ������Ļ�ϵ�������ֵ 
          private float yInScreen;  
          //��¼��ָ����ʱ����Ļ�ϵĺ������ֵ 
          private float xDownInScreen;  
          // ��¼��ָ����ʱ����Ļ�ϵ��������ֵ 
          private float yDownInScreen;  
          //��¼��ָ����ʱ��С��������View�ϵĺ������ֵ 
          private float xInView;  
          //��¼��ָ����ʱ��С��������View�ϵ��������ֵ 
          private float yInView;  
   	      /** 
           * ����С����������Ļ�е�λ�á� 
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
                  // ����������λ��  
                 // wm.updateViewLayout(image_floatView, params);  
            	/*  if(xInScreen==event.getRawX()&&  yInScreen == event.getRawY() ){
            		  break;
            	  }
            	   xInScreen = event.getRawX();  
                   yInScreen = event.getRawY() ;//- getStatusBarHeight();  
                
                   Log.d("vrfloat","move getX()"+event.getX()+";getY():"+event.getY()+";getRawX()"+event.getX()+";getRawY():"+event.getY());*/
                  break;  
              case MotionEvent.ACTION_UP:  
                  // �����ָ�뿪��Ļʱ��xDownInScreen��xInScreen��ȣ���yDownInScreen��yInScreen��ȣ�����Ϊ�����˵����¼���  
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
