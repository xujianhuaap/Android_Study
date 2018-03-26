LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := MediaPlayerJNi
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \
	-lz \
	-lm \

LOCAL_SRC_FILES := \
	/home/xujianhua/Documents/codes/MyApplication/app/src/main/jni/com_example_xujianhua_myapplication_MediaPlayer.c \

LOCAL_C_INCLUDES += /home/xujianhua/Documents/codes/MyApplication/app/src/debug/jni
LOCAL_C_INCLUDES += /home/xujianhua/Documents/codes/MyApplication/app/src/main/jni

include $(BUILD_SHARED_LIBRARY)
