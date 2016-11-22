LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_CERTIFICATE := platform
LOCAL_PACKAGE_NAME := WirelessRemoteServer
LOCAL_JAVA_LIBRARIES := com.mstar.android\
						services

include $(BUILD_PACKAGE)

