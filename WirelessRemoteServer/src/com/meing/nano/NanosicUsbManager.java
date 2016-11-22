package com.meing.nano;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.HashMap;

public class NanosicUsbManager
{
    private static final String ACTION_USB_PERMISSION = "com.android.usbupgrade.USB_PERMISSION";
    public static final int USB_DONGLE_MODE_NORMAL = 1;
    public static final int USB_DONGLE_MODE_UPDATE = 2;
    private static NanosicUsbManager instance;
    private final int PRODUCT_ID = 6176;
    private final int VENDOR_ID = 28741;
    private final String TAG = "NanosicUsbManager";
    private final int TIMEOUT = 2000;
    private final int UPDATE_MODE_PRODUCT_ID = 9778;
    private final int UPDATE_MODE_VENDOR_ID = 30021;
    private boolean forceClaim = true;
    private UsbDeviceConnection mConnection;
    private Context mContext;
    public int mDongleMode = -1;
    private boolean mIsActived = false;
    private Lisenter mLisenter;
    private UsbEndpoint mUpdateEndpointRead;
    private UsbEndpoint mUpdateEndpointWrite;
    private UsbDevice mUsbDevice;
    private UsbManager mUsbManager;
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String str = intent.getAction();
            Log.d(TAG, "BroadcastReceiver " + str);
            if (ACTION_USB_PERMISSION.equals(str))
            {
                try
                {
                    if (((UsbDevice)intent.getParcelableExtra("device")).getVendorId() != mUsbDevice.getVendorId())
                        return;
                    if (!intent.getBooleanExtra("permission", false))
                    {
                        Log.d("NanosicUsbManager", "Permission for accesss to the usb denied!!  ");
                        //requestPermission(context);
                        return;
                    }
                }
                finally
                {
                }
                openDeviceAndInterface();
            }
            context.unregisterReceiver(mUsbReceiver);
        }
    };
    private UsbInterface mUsbUpdateInterface;

    private NanosicUsbManager(Context context, Lisenter paramLisenter)
    {
        mContext = context;
        mLisenter = paramLisenter;
        mUsbManager = ((UsbManager)mContext.getSystemService(Context.USB_SERVICE));
    }

    private boolean claimUpdateInterface()
    {
        if (mConnection == null) {
            Log.d(TAG,"claimUpdateInterface conn null");
            return false;
        }
        return mConnection.claimInterface(mUsbUpdateInterface, forceClaim);
    }

    public static NanosicUsbManager instance(Context context, Lisenter paramLisenter)
    {
        if (instance == null)
            instance = new NanosicUsbManager(context, paramLisenter);
        if (paramLisenter != null)
            instance.mLisenter = paramLisenter;
        return instance;
    }

    // find interface and assign end point
    private boolean findIntfAndEpt() {
        if (mUsbDevice == null) {
            return false;
        }

        // find the device interface
        Log.d(TAG,  "UsbInterface count: " +  mUsbDevice.getInterfaceCount());
        for (int j = 0; j < mUsbDevice.getInterfaceCount();j++){
            UsbInterface intf = mUsbDevice.getInterface(j);
            Log.d(TAG, j+" " + intf);
            Log.d(TAG, "====>>>> class:"+intf.getInterfaceClass()+" subclass:"+intf.getInterfaceSubclass()+" proto:"+intf.getInterfaceProtocol());
        }
        //for (int i = 0; i < mUsbDevice.getInterfaceCount();i++)
        {
            // 获取设备接口，一般都是一个接口，你可以打印getInterfaceCount()方法查看接
            // 口的个数，在这个接口上有两个端点，OUT 和 IN
            int i=0;
            if(mUsbDevice.getInterfaceCount()>4) {
                i = 4;
            }
            UsbInterface intf = mUsbDevice.getInterface(i);
            Log.d(TAG, i+" " + intf);
            Log.d(TAG, "====>>>> class:"+intf.getInterfaceClass()+" subclass:"+intf.getInterfaceSubclass()+" proto:"+intf.getInterfaceProtocol());
            if(intf.getEndpointCount()>1)
//            if (intf.getInterfaceClass() == 1 && intf.getInterfaceSubclass() == 1 &&
//                    intf.getInterfaceProtocol() == 0)
            {    //HID设备的相关信息
                mUsbUpdateInterface = intf;
                Log.d(TAG,"find usb interface:"+mUsbUpdateInterface);
                //break;
            }

        }
        if(mUsbUpdateInterface == null){
            return false;
        }

        //Log.d(TAG,  "Endpoint count: " +  mUsbUpdateInterface.getEndpointCount());

        if (mUsbUpdateInterface.getEndpoint(1) != null) {
            mUpdateEndpointWrite = mUsbUpdateInterface.getEndpoint(1);
            //Log.d(TAG,"mUpdateEndpointWrite inout:"+mUpdateEndpointWrite.getDirection()+" address:"+mUpdateEndpointWrite.getAddress());
        }
        if (mUsbUpdateInterface.getEndpoint(0) != null) {
            mUpdateEndpointRead = mUsbUpdateInterface.getEndpoint(0);
            //Log.d(TAG,"mUpdateEndpointRead inout:"+mUpdateEndpointRead.getDirection()+" address:"+mUpdateEndpointRead.getAddress());
        }

        if(mUpdateEndpointWrite == null || mUpdateEndpointRead == null){
            return false;
        }

       return true;
    }

    private boolean matchUpdateEndpoint(int updateMode) {
        if (mUsbManager == null) {
            return false;
        }
        Log.d(TAG, "====>>>> updateMode:" + updateMode + " mDongleMode:" +mDongleMode);
        if(mDongleMode ==updateMode ){
            Log.d(TAG, "device has match");
            return true;
        }
        mUpdateEndpointRead = null;
        mUpdateEndpointWrite = null;
        mUsbUpdateInterface = null;
        mDongleMode = -1;

        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
            Log.d(TAG, "====>>>> vend:" + device.getVendorId() + " product:" + device.getProductId() + " name:" + device.toString());
        }

        if(updateMode == USB_DONGLE_MODE_UPDATE){
            int count = 0;
            do{
                for (UsbDevice device : deviceList.values()) {
                    Log.d(TAG, count+" ====>>>> vend:" + device.getVendorId() + " product:" + device.getProductId() + " name:" + device.toString());
                }
                if (!deviceList.isEmpty()) {
                    for (UsbDevice device : deviceList.values()) {
                        if (device.getVendorId() == UPDATE_MODE_VENDOR_ID && device.getProductId() == UPDATE_MODE_PRODUCT_ID) {
                            Log.d(TAG,"update found device count:"+count);
                            mUsbDevice = device;
                            Log.d(TAG, "update find usb device:" + mUsbDevice);
                            // 找到Device接口并分配相应端点
                            if (findIntfAndEpt()) {
                                mDongleMode = updateMode;
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                deviceList = mUsbManager.getDeviceList();
                count ++;
            }while(count<5);
        }else if(updateMode == USB_DONGLE_MODE_NORMAL) {
            if (!deviceList.isEmpty()) {
                for (UsbDevice device : deviceList.values()) {
                    if (device.getVendorId() == VENDOR_ID && device.getProductId() == PRODUCT_ID) {
                        mUsbDevice = device;
                        Log.d(TAG, "normal find usb device:" + mUsbDevice);
                        // 找到Device接口并分配相应端点
                        if (findIntfAndEpt()) {
                            mDongleMode = updateMode;
                            return true;
                        } else {
                            return false;
                        }
                    }else if (device.getVendorId() == UPDATE_MODE_VENDOR_ID && device.getProductId() == UPDATE_MODE_PRODUCT_ID) {
                        mUsbDevice = device;
                        Log.d(TAG, "normal find usb device:" + mUsbDevice);
                        // 找到Device接口并分配相应端点
                        if (findIntfAndEpt()) {
                            mDongleMode = USB_DONGLE_MODE_UPDATE;
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }else{
            return false;
        }

        return false;
    }

    private boolean openDeviceAndInterface()
    {
        Log.d(TAG, "openDeviceAndInterface");
        mConnection = mUsbManager.openDevice(mUsbDevice);
        if (claimUpdateInterface()) {
            mIsActived = true;
            Log.d(TAG, "[Usb Dev]: " + mUsbDevice);
            Log.d(TAG, "[Usb Interface]: " + mUsbUpdateInterface);
            Log.d(TAG, "[Usb EndpointIn]: " + mUpdateEndpointRead);
            Log.d(TAG, "[Usb EndpointOut]: " + mUpdateEndpointWrite);
            if (mLisenter != null)
                mLisenter.onReady(true, mDongleMode);
        }else{
            Log.d(TAG, "failed to claim update interface");
            return false;
        }

        return true;
    }

    private void requestPermission(Context context)
    {
        Log.d(TAG, "requestPermission");
        IntentFilter localIntentFilter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(mUsbReceiver, localIntentFilter);
        PendingIntent localPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        mUsbManager.requestPermission(mUsbDevice, localPendingIntent);
    }

    public boolean changeToUpdateMode(Context context)
    {
        boolean bool = false;

        Log.d(TAG,"mNanosicUsbManager changeToUpdateMode");

        bool = matchUpdateEndpoint(USB_DONGLE_MODE_UPDATE);
        if(bool) {
            if(mUsbManager.hasPermission(mUsbDevice)) {
                if(!openDeviceAndInterface()){
                    return false;
                }
            }else{
                requestPermission(context);
            }
        }else{
            Log.d(TAG, "matchUpdateEndpoint failed");
        }
        return bool;
    }

    public void close()
    {
        if (mConnection == null)
            return;
        mConnection.close();
    }

    public boolean isActived()
    {
        return mIsActived;
    }

    public boolean open(Context context)
    {
        Log.d(TAG, "====>>>> open");

        boolean bool1 = matchUpdateEndpoint(USB_DONGLE_MODE_NORMAL);

        if(bool1){
            if(mUsbManager.hasPermission(mUsbDevice)) {
                if(!openDeviceAndInterface()){
                    return false;
                }
            }else{
                requestPermission(context);
            }
        }
        return bool1;
    }

    public void setListener(Lisenter paramLisenter)
    {
        mLisenter = paramLisenter;
    }

    public int transfer(UsbEndpoint paramUsbEndpoint, byte[] paramArrayOfByte, int paramInt)
    {
        return mConnection.bulkTransfer(paramUsbEndpoint, paramArrayOfByte, paramInt, TIMEOUT);
    }

    public int updateInterfaceRead(byte[] paramArrayOfByte)
    {
        if ((mUpdateEndpointRead == null) || (mConnection == null))
        {
            Log.d(TAG, "EndpointRead or Connection is null ");
            return -1;
        }
        int ret = mConnection.bulkTransfer(mUpdateEndpointRead, paramArrayOfByte, 32, TIMEOUT);
        //Log.d(TAG,"read:"+ Utils.bytes2HexStr(paramArrayOfByte));
        return ret;
    }

    public int updateInterfaceWrite(byte[] paramArrayOfByte, int len)
    {
        if (mUpdateEndpointWrite == null)
        {
            Log.d(TAG, "EndpointWrite is null ");
            return -1;
        }
        if (mConnection == null)
        {
            Log.d(TAG, "Connection is null ");
            return -1;
        }
        //Log.d(TAG,"write:"+ Utils.bytes2HexStr(paramArrayOfByte));
        return mConnection.bulkTransfer(mUpdateEndpointWrite, paramArrayOfByte, len, TIMEOUT);
    }

    public static abstract interface Lisenter
    {
        public abstract void onReady(boolean bUpdate, int updateMode);
    }
}
