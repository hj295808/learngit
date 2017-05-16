package com.jsbd.jsbdvoice.service;
import com.jsbd.jsbdvoice.service.JSBDTtsListener;

interface JSBDTtsService{
	void registerTtsListener(String flag, JSBDTtsListener cb);
	void unregisterTtsListener(String flag, JSBDTtsListener cb);
	void startSpeak(String flag, String text);
	void pauseSpeak(String flag);
	void stopSpeak(String flag);
	void resumeSpeak(String flag);
}