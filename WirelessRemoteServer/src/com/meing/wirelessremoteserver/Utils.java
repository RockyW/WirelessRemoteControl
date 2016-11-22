package com.meing.wirelessremoteserver;

import android.util.Log;

/**
 * 
 * 工具类
 */
public class Utils {

	/*
	 * 给定16进制字符串转化成实际字符串 如：6f6b ---> ok
	 */
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		Log.i("remote", "toHexString()"+s+"--->"+str);
		return str;
	}

	// 十六进制编码转化成为字符串
	public static String HextoString(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	// 把字节码数组转换成对应的16进制字符串
	public static String printHexString(byte[] b) {
		// System.out.print(hint);
		String hexString = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString = hexString + hex;
			// System.out.print(hex.toUpperCase() + " ");
		}
		// System.out.println("");
		return hexString;

	}

	/*
	 * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" –> byte[]{0x2B, 0×44, 0xEF,
	 * 0xD9}
	 */
	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < tmp.length / 2; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/*
	 * 合并单个字节，比如9和e，合并成9e
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}
	
	public static byte[] getHexByte(String beforeReversed) {
		String st = toHexString(beforeReversed);
		Log.i("remote", "getHexByte()"+beforeReversed+"--->"+st);
		byte[] finalBytes = HexString2Bytes(st);
		return finalBytes;
	}

}
