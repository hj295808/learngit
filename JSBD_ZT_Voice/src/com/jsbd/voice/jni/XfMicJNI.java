package com.jsbd.voice.jni;


public class XfMicJNI {

	public XfMicJNI() {
		// TODO Auto-generated constructor stub
	}

	static{
		System.loadLibrary("xfmic");
	}
	
	public static native void SetXfmicMode(int value);
	public static native int GetXfmicMode(); 
}
