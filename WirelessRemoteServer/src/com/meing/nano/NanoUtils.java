package com.meing.nano;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NanoUtils
{
    public static boolean isEmpty(String s)
    {
        if (s == null) return  true;
        if (s.equals("")) return true;
        if (s.length() == 0) return  true;
        return  false;
    }

    public static byte accCheckByteValue(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
        int i = 0;
        for (int j = paramInt1; ; j++)
        {
            if (j >= paramInt1 + paramInt2)
                return (byte)(i & 0xFF);
            i += (0xFF & paramArrayOfByte[j]);
        }
    }

    public static int accCheckIntValue(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
        int i = 0;
        for (int j = paramInt1; ; j++)
        {
            if (j >= paramInt1 + paramInt2)
                return i;
            i += (0xFF & paramArrayOfByte[(j + paramInt1)]);
        }
    }

    public static String bytes2HexStr(byte[] paramArrayOfByte)
    {
        if (paramArrayOfByte == null)
            return "";
        StringBuffer localStringBuffer = new StringBuffer();
        for (int i = 0; ; i++)
        {
            if (i >= paramArrayOfByte.length)
                return localStringBuffer.toString();
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Byte.valueOf(paramArrayOfByte[i]);
            localStringBuffer.append(String.format("%02x ", arrayOfObject));
        }
    }

    public static int bytes2Int(byte[] paramArrayOfByte)
    {
        return 0xFF & paramArrayOfByte[3] | (0xFF & paramArrayOfByte[2]) << 8 | (0xFF & paramArrayOfByte[1]) << 16 | (0xFF & paramArrayOfByte[0]) << 24;
    }

    public static int bytes2Int(byte[] paramArrayOfByte, int paramInt)
    {
        return 0xFF & paramArrayOfByte[(paramInt + 0)] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24;
    }

    public static String bytes2Str(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
        if (paramArrayOfByte == null)
            return "";
        StringBuilder localStringBuilder = new StringBuilder(paramArrayOfByte.length);
        for (int i = paramInt1; ; i++)
        {
            if (i >= paramInt1 + paramInt2)
                return localStringBuilder.toString();
            if (paramArrayOfByte[i] > 0)
                localStringBuilder.append((char)paramArrayOfByte[i]);
        }
    }

    public static final byte[] int2Bytes(int paramInt)
    {
        byte[] arrayOfByte = new byte[4];
        arrayOfByte[0] = ((byte)(paramInt >> 24));
        arrayOfByte[1] = ((byte)(paramInt >> 16));
        arrayOfByte[2] = ((byte)(paramInt >> 8));
        arrayOfByte[3] = ((byte)paramInt);
        return arrayOfByte;
    }
}