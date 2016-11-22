package com.meing.wirelessremoteserver;

import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.Context;
import android.util.Log;

public class MY2HandleKey implements HandleKey {

	private static MY2HandleKey mm1HandleKey;
	private Context mContext;
	private DatagramSocket socket;
	private ServiceKey serviceKey;
	private AbstractServiceLight serviceLight;

	private MY2HandleKey() {

	}

	private MY2HandleKey(Context context, ServiceKey serviceKey,
			AbstractServiceLight serviceLight) {
		this.mContext = context;
		this.serviceKey = serviceKey;
		this.serviceLight = serviceLight;
	}

	public static MY2HandleKey getInstance(Context context,
			ServiceKey serviceKey, AbstractServiceLight serviceLight) {
		if (mm1HandleKey == null) {
			synchronized (MY2HandleKey.class) {
				if (mm1HandleKey == null) {
					mm1HandleKey = new MY2HandleKey(context, serviceKey,
							serviceLight);
				}
			}
		}
		return mm1HandleKey;
	}

	@Override
	public void execute(InetAddress clientAddress,
			int port, String receive) {
		String key = receive.substring(receive.length() / 2, receive.length());
		Log.d("remote", "MY2HandleKey--->execute()--receive_key = " + key);
		if (key.startsWith("lgt")) {
			serviceLight.execute(key);
		} else {
			serviceKey.setKey(key);
			serviceKey.start();
		}
	}

}
