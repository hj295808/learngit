#include <XfMicJNI.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/ioctl.h>
#include <android/log.h>

#define LOG_TAG "suoc_jni"
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

int SetXfmicMode(int value) {
	int fd = -1;
	int ret = 0;
	fd = open("/dev/xfmic", O_RDONLY);
	if (value == 1)
		ret = ioctl(fd, ENTER_PASSBY_MODE, 0);
	else if (value == 2)
		ret = ioctl(fd, ENTER_NR_MODE, 0);
	else if (value == 3)
		ret = ioctl(fd, ENTER_MUTE_MODE, 0);
	else if (value == 4)
		ret = ioctl(fd, ENTER_UNMUTE_MODE, 0);
	else if (value == 5)
		ret = ioctl(fd, ENTER_WAKEUP_MODE, 0);
	else {
		ALOGE("SetXfmicMode invalid value");
	}
	close(fd);
	return 0;
}

/*
 {1,ENTER_PASSBY_MODE}
 {2,ENTER_NR_MODE}
 {3,ENTER_MUTE_MODE}
 {4,ENTER_UNMUTE_MODE}
 {5,ENTER_WAKEUP_MODE}
 */
int GetXfmicMode(void) {
	int fd = -1;
	int ret = -1;
	fd = open("/dev/xfmic", O_RDONLY);
	if (-1 == fd) {
		ALOGE("open /dev/xfmic error!");
	} else {
		if (ioctl(fd, GET_MODE_STATE, &ret) < 0) {
			ALOGE("GetXfmicMode error!");
		}
		ALOGE("GetXfmicMode GET_MODE_STATE=%d,ret=%d\r\n", GET_MODE_STATE, ret);
		close(fd);
	}
	return ret;
}

JNIEXPORT void JNICALL Java_com_jsbd_voice_jni_XfMicJNI_SetXfmicMode(
		JNIEnv * env, jobject obj, jint value) {
	SetXfmicMode(value);
}

/*
 * Class:     com_jsbd_voice_jni_XfMicJNI
 * Method:    GetXfmicMode
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_jsbd_voice_jni_XfMicJNI_GetXfmicMode(
		JNIEnv * env, jobject obj) {
  return	GetXfmicMode();
}
