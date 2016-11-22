package com.meing.nano;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UpdateCMDManager
{
    public static final int ERROR_CODE_FAILURE = -1;
    public static final int ERROR_CODE_FILE_OPEN_FAILURE = -2;
    public static final int ERROR_CODE_ROM_CHECK_FAILURE = -3;
    public static final int ERROR_CODE_SUCCESS = 0;
    public static final int ERROR_CODE_USB_TRANSFER = -10;
    public static final int ERROR_RESPONSE_CHECK_FAILURE = -4;
    public static final int RESPONSE_CRC_CHECK_ERROR = 1;
    public static final int RESPONSE_FAILURE = 2;
    public static final int RESPONSE_SUCCESS = 3;
    private static UpdateCMDManager instance;
    final boolean DBUG = true;
    private final String TAG = "UpdateCMD  nanoupdate";
    public static UpdateCMDManager instance()
    {
        if (instance == null)
            instance = new UpdateCMDManager();
        return instance;
    }

    public int checkCMDResponse(byte[] paramArrayOfByte)
    {
        if ((paramArrayOfByte[0] != 35) && (paramArrayOfByte[2] != 85))
        {
            Log.d(TAG, "Invalid response data!!");
            return ERROR_RESPONSE_CHECK_FAILURE;
        }
        if (NanoUtils.accCheckByteValue(paramArrayOfByte, 0, 31) != paramArrayOfByte[31])
        {
            Log.d(TAG, "ERROR_RESPONSE_CHECK_FAILURE: crc");
            return ERROR_RESPONSE_CHECK_FAILURE;
        }
//        if ((paramArrayOfByte[0] != 35) && (paramArrayOfByte[2] == 85))
//        {
//            Log.d(TAG, "ERROR_RESPONSE_CHECK_FAILURE: data");
//            return ERROR_RESPONSE_CHECK_FAILURE;
//        }
        return paramArrayOfByte[4];
    }

    public byte[] getCmdSetLightEnable(boolean bEnable)
    {
        byte[] arrayOfByte = new byte[16];
        arrayOfByte[0] = 0x4E;
        arrayOfByte[1] = (byte) 0x9D;
        arrayOfByte[2] = 0x55;
        arrayOfByte[3] = 0x01;
        arrayOfByte[4] = (byte)(bEnable ? 0x01:0x00);
        arrayOfByte[15] = NanoUtils.accCheckByteValue(arrayOfByte, 0, 15);
        return arrayOfByte;
    }

    public byte[] getCmdSetLightBrightness(byte val)
    {
        byte[] arrayOfByte = new byte[16];
        arrayOfByte[0] = 0x4E;
        arrayOfByte[1] = (byte) 0x9D;
        arrayOfByte[2] = 0x55;
        arrayOfByte[3] = 0x02;
        arrayOfByte[4] = val;
        arrayOfByte[15] = NanoUtils.accCheckByteValue(arrayOfByte, 0, 15);
        return arrayOfByte;
    }

    public byte[] getCmdReadLightVal()
    {
        byte[] arrayOfByte = new byte[16];
        arrayOfByte[0] = 0x4E;
        arrayOfByte[1] = (byte) 0x9D;
        arrayOfByte[2] = 0x55;
        arrayOfByte[3] = 0x03;
        arrayOfByte[15] = NanoUtils.accCheckByteValue(arrayOfByte, 0, 15);
        return arrayOfByte;
    }
}
