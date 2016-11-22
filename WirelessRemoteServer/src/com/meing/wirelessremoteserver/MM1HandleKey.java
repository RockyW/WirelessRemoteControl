package com.meing.wirelessremoteserver;

import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.Context;
import android.util.Log;

public class MM1HandleKey implements HandleKey {

	private static MM1HandleKey mm1HandleKey;
	private Context mContext;
	private DatagramSocket socket;
	private ServiceKey serviceKey;
	private AbstractServiceLight serviceLight;

	private MM1HandleKey() {

	}

	private MM1HandleKey(Context context, ServiceKey serviceKey,
			AbstractServiceLight serviceLight) {
		this.mContext = context;
		this.serviceKey = serviceKey;
		this.serviceLight = serviceLight;
	}

	public static MM1HandleKey getInstance(Context context,
			ServiceKey serviceKey, AbstractServiceLight serviceLight) {
		if (mm1HandleKey == null) {
			synchronized (MM1HandleKey.class) {
				if (mm1HandleKey == null) {
					mm1HandleKey = new MM1HandleKey(context, serviceKey,
							serviceLight);
				}
			}
		}
		return mm1HandleKey;
	}

	@Override
	public void execute(InetAddress inetAddress,
			int port, String receive) {
		String key = receive.substring(receive.length() / 2, receive.length());
		Log.d("remote", "MM1HandleKey--->execute()--receive_key = " + key);
		if (key.startsWith("lgt")) {
			serviceLight.prepare(inetAddress, port);
			serviceLight.execute(key);
		} else {
			serviceKey.setKey(key);
			serviceKey.start();
		}
	}

}
