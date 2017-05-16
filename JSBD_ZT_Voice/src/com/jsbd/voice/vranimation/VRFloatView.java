package com.jsbd.voice.vranimation;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

public class VRFloatView extends ImageView {

	public VRFloatView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public VRFloatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public VRFloatView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	private float mTouchX;
	private float mTouchY;
	private float x;
	private float y;
	private float mStartX;
	private float mStartY;
	private OnClickListener mClickListener;

	private WindowManager windowManager = (WindowManager) getContext()
			.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	// ��windowManagerParams����Ϊ��ȡ��ȫ�ֱ��������Ա����������ڵ�����
	private WindowManager.LayoutParams windowManagerParams = null;



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//��ȡ��״̬���ĸ߶�
		Rect frame =  new  Rect();  
		getWindowVisibleDisplayFrame(frame);
		int  statusBarHeight = frame.top - 48; 
		System.out.println("statusBarHeight:"+statusBarHeight);
		// ��ȡ�����Ļ�����꣬������Ļ���Ͻ�Ϊԭ��
		x = event.getRawX();
		y = event.getRawY() - statusBarHeight; // statusBarHeight��ϵͳ״̬���ĸ߶�
		Log.i("tag", "currX" + x + "====currY" + y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // ������ָ�������¶���
			// ��ȡ���View�����꣬���Դ�View���Ͻ�Ϊԭ��
			mTouchX = event.getX();
			mTouchY = event.getY();
			mStartX = x;
			mStartY = y;
			Log.i("tag", "startX" + mTouchX + "====startY"
					+ mTouchY);
			break;

		case MotionEvent.ACTION_MOVE: // ������ָ�����ƶ�����
			updateViewPosition();
			break;

		case MotionEvent.ACTION_UP: // ������ָ�����뿪����
			updateViewPosition();
			mTouchX = mTouchY = 0;
			if ((x - mStartX) < 5 && (y - mStartY) < 5) {
				if(mClickListener!=null) {
					mClickListener.onClick(this);
				}
			}
			break;
		}
		return true;
	}
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.mClickListener = l;
	}
	
	private void updateViewPosition() {
		// ���¸�������λ�ò���
		windowManagerParams.x = (int) (x - mTouchX);
		windowManagerParams.y = (int) (y - mTouchY);
		windowManager.updateViewLayout(this, windowManagerParams); // ˢ����ʾ
	}

	public static void showView(){
		
	}
}
