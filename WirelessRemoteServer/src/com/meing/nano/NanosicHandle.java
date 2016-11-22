package com.meing.nano;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class NanosicHandle {
	private final String TAG = "NanosicHandle";
	private NanosicUsbManager mNanosicUsbManager;
    private UpdateCMDManager mUpdateCMDManager;
    private Context mContext;
    private Handler mHandler;
    private final int USB_CMD_LEN = 32;
    private final int USB_CMD_LIGHT_LEN = 16;
    public final static int MSG_NANO_CONNECT_OK = 1000;
    public final static int MSG_NANO_CONNECT_FAIL = 1001;
    private static NanosicHandle instance;
    
    
    NanosicUsbManager.Lisenter mLisenter = new NanosicUsbManager.Lisenter()
    {
        public void onReady(boolean bReady, int updateMode)
        {
            Log.d(TAG, "onReady: " + bReady + " mode=" + updateMode);
            mHandler.sendEmptyMessage(bReady?MSG_NANO_CONNECT_OK:MSG_NANO_CONNECT_FAIL);
        }
    };
    
    private NanosicHandle(Context context, Handler handler)
    {
        mContext = context;
        mHandler = handler;
    }
    
    public static NanosicHandle instance(Context context, Handler handler)
    {
        if (instance == null)
            instance = new NanosicHandle(context, handler);
        return instance;
    }

    public boolean open(Context context) {
    	boolean ret = true;
    	mContext  = context;
    	mUpdateCMDManager = UpdateCMDManager.instance();
        mNanosicUsbManager = NanosicUsbManager.instance(mContext, mLisenter);
        
        if (!mNanosicUsbManager.open(mContext)){
        	ret = false;
        }
        Log.d("light", "open(.):ret="+ret);
        
        return ret;
    }
    
    public int getLightBritness() {
        byte[] arrayOfByte = mUpdateCMDManager.getCmdReadLightVal();
        int writeBytes = mNanosicUsbManager.updateInterfaceWrite(arrayOfByte, USB_CMD_LIGHT_LEN);
        if (writeBytes != USB_CMD_LIGHT_LEN) {
            Log.d(TAG, "getLightBritness error ");
            return -1;
        }
        byte[] resultOfByte = new byte[USB_CMD_LEN];
        int readBytes = mNanosicUsbManager.updateInterfaceRead(resultOfByte);
        Log.d(TAG, "getLightBritness: " + NanoUtils.bytes2HexStr(resultOfByte));
        if (readBytes != USB_CMD_LEN) {
            Log.d(TAG, "getLightBritness read " + readBytes);
            return -1;
        }
        return resultOfByte[5];
    }
    
    public int getLightEnable() {
        byte[] arrayOfByte = mUpdateCMDManager.getCmdReadLightVal();
        int writeBytes = mNanosicUsbManager.updateInterfaceWrite(arrayOfByte, USB_CMD_LIGHT_LEN);
        if (writeBytes != USB_CMD_LIGHT_LEN) {
            Log.d(TAG, "getLightBritness error ");
            return -1;
        }
        byte[] resultOfByte = new byte[USB_CMD_LEN];
        int readBytes = mNanosicUsbManager.updateInterfaceRead(resultOfByte);
        Log.d(TAG, "getLightBritness: " + NanoUtils.bytes2HexStr(resultOfByte));
        if (readBytes != USB_CMD_LEN) {
            Log.d(TAG, "getLightBritness read " + readBytes);
            return -1;
        }
        return resultOfByte[6];
    }

    public boolean setLightEnable(boolean enable) {
        byte[] arrayOfByte = mUpdateCMDManager.getCmdSetLightEnable(enable);
        Log.d(TAG, "lightEnable to write: " + NanoUtils.bytes2HexStr(arrayOfByte));
        int writeBytes = mNanosicUsbManager.updateInterfaceWrite(arrayOfByte, USB_CMD_LIGHT_LEN);
        if (writeBytes != USB_CMD_LIGHT_LEN) {
            Log.d(TAG, "lightEnable error ");
            return false;
        }
        threadSleep(100);
        byte[] resultOfByte = new byte[USB_CMD_LEN];
        int readBytes = mNanosicUsbManager.updateInterfaceRead(resultOfByte);
        Log.d(TAG, "lightEnable: " + NanoUtils.bytes2HexStr(resultOfByte));
        if (readBytes != USB_CMD_LEN) {
            Log.d(TAG, "lightEnable read " + readBytes);
            return false;
        }
        
        return true;
    }

    public boolean setLihghBritness(int brightness) {
        if(brightness < 0){
        	brightness = 0;
        }else if(brightness > 100){
        	brightness = 100;
        }
        byte[] arrayOfByte = mUpdateCMDManager.getCmdSetLightBrightness((byte)brightness);
        int writeBytes = mNanosicUsbManager.updateInterfaceWrite(arrayOfByte, USB_CMD_LIGHT_LEN);
        if (writeBytes != USB_CMD_LIGHT_LEN) {
            Log.d(TAG, "lightBritness error ");
            return false;
        }
        threadSleep(100);
        byte[] resultOfByte = new byte[USB_CMD_LEN];
        int readBytes = mNanosicUsbManager.updateInterfaceRead(resultOfByte);
        Log.d(TAG, "lightBritness: " + NanoUtils.bytes2HexStr(resultOfByte));
        if (readBytes != USB_CMD_LEN) {
            Log.d(TAG, "lightBritness read " + readBytes);
            return false;
        }
        
        return true;
    }
    
    private void threadSleep(int millsec)
    {
        try
        {
            Thread.sleep(millsec);
        }
        catch (InterruptedException localInterruptedException)
        {
            localInterruptedException.printStackTrace();
        }
    }
}
