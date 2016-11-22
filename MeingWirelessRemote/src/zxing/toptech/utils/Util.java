package zxing.toptech.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Util {

	/**
	 * regular expression
	 */
	private static final String PATTERN = "^[A-Z|0-9]{4}\\d[A-Z]\\d{10}$";

	/**
	 * this method is used to judge the correctness of the input
	 * 
	 * @param String
	 * @return boolean
	 */
	public static boolean regexMatch(String input) {
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

	public static String[] regexSplit(String pattern, String content) {
		Pattern pt = Pattern.compile(pattern);
		return pt.split(content);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static String getLocalIpAddress() {
		String ipaddress = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(inetAddress
									.getHostAddress())) {
						ipaddress = inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			return "没有获取到IP";
		}

		return ipaddress;
	}

	public static String getWIFILocalIpAdress(WifiManager wifiManager) {
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = formatIpAddress(ipAddress);
		return ip;
	}

	private static String formatIpAddress(int ipAdress) {
		return (ipAdress & 0xFF) + "." + ((ipAdress >> 8) & 0xFF) + "."
				+ ((ipAdress >> 16) & 0xFF) + "." + (ipAdress >> 24 & 0xFF);
	}

	/***
	 * byte array 转化为 int
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);

		return result;
	}

	/**
	 * 从bytes 格式转化成int格式
	 * 
	 * @param b
	 * @param offset
	 * @return
	 */
	public static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}

	/**
	 * 系统提供的数组拷贝方法arraycopy
	 * */
	public static byte[] sysCopy(List<byte[]> srcArrays) {
		int len = 0;
		for (byte[] srcArray : srcArrays) {
			len += srcArray.length;
		}
		byte[] destArray = new byte[len];
		int destLen = 0;
		for (byte[] srcArray : srcArrays) {
			System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
			destLen += srcArray.length;
		}

		return destArray;
	}
}
