package com.meing.wirelessremoteserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.meing.nano.NanosicHandle;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MM1ServiceLight extends AbstractServiceLight {

	private Context mContext;
	private int lightEnable = -1;
	private InetAddress inetAddress;
	private int port;
	private TvManager tvManager;
	private final static int LENGTH = 14;
	private byte[] msg;

//	enum CMD_TYPE {
//		EN_SET_LIGHT_ENABLE, EN_SET_LIGHT_DISABLE, EN_SET_FILM_MODE, EN_SET_NIGHT_MODE, EN_SET_USER_MODE1, EN_SET_USER_MODE2, EN_SET_USER_MODE3, EN_SET_CURRENT_AS_USER_MODE1, EN_SET_CURRENT_AS_USER_MODE2, EN_SET_CURRENT_AS_USER_MODE3, EN_SET_INC_WARM, EN_SET_INC_COLD, EN_SET_SCENE_MODE, EN_SET_INC_BRIGHTNESS, EN_SET_DEC_BRIGHTNESS
//	}

	public MM1ServiceLight(Context context) {
		this.mContext = context;
		tvManager = TvManager.getInstance();
	}

	Runnable ServiceLightThread = new Runnable() {

		@Override
		public void run() {
			DatagramSocket dSocket = null;

			try {
				dSocket = new DatagramSocket();
				int msg_len = msg == null ? 0 : msg.length;
				DatagramPacket dPacket = new DatagramPacket(msg, 0, msg_len,
						inetAddress, port);
				dSocket.send(dPacket);
			} catch (Exception e) {

				e.printStackTrace();

			} finally {
				dSocket.close();
			}

		}

	};

	// 启动线程ServiceKeyThread
	public void start() {
		// TODO Auto-generated method stub
		new Thread(ServiceLightThread).start();
	}

	@Override
	public void prepare(InetAddress inetAddress, int port) {
		this.inetAddress = inetAddress;
		this.port = port;
	}

	public void execute(String key) {
		int[] data = new int[LENGTH];
		int cmd = getCmd(key);
		boolean flag = false;
		if (tvManager == null) {
			tvManager = TvManager.getInstance();
		}
		if (cmd != -1) {
			try {
				flag = tvManager.LightCmd(true, 1, LENGTH, data);
			} catch (TvCommonException e) {
				flag = false;
			}

		}
		LightMsg lightMsg = new LightMsg();
		lightMsg.setMsg(data);
		StringBuilder builder = new StringBuilder();
		builder.append(flag);
		builder.append(",");
		// builder.append(lightMsg.toString());
		// builder.append(",");
		String str = builder.toString();
		Log.d("remote", "light state msg = "+str);
		msg = Utils.getHexByte(str);
		this.start();
	}

	private int getCmd(String key) {
		int cmd = -1;
		if (Constants.KEY_LIGHT_ON.equals(key)) {
			cmd = 1;
		} else if (Constants.KEY_LIGHT_OFF.equals(key)) {
			cmd = 2;
		} else if (Constants.KEY_MODEL_ONE.equals(key)) {
			cmd = 3;
		} else if (Constants.KEY_MODEL_TWO.equals(key)) {
			cmd = 4;
		} else if (Constants.KEY_MODEL_THREE.equals(key)) {
			cmd = 5;
		} else if (Constants.KEY_MODEL_FOUR.equals(key)) {
			cmd = 6;
		} else if (Constants.KEY_MODEL_FIVE.equals(key)) {
			cmd = 7;
		} else if (Constants.KEY_SET_MODEL_ONE.equals(key)) {
			cmd = 8;
		} else if (Constants.KEY_SET_MODEL_TWO.equals(key)) {
			cmd = 9;
		} else if (Constants.KEY_SET_MODEL_THREE.equals(key)) {
			cmd = 10;
		} else if (Constants.KEY_HUE_WARM.equals(key)) {
			cmd = 11;
		} else if (Constants.KEY_HUE_COLD.equals(key)) {
			cmd = 12;
		} else if (Constants.KEY_LIGHT_MODEL.equals(key)) {
			cmd = 13;
		} else if (Constants.KEY_LIGHT_UP.equals(key)) {
			cmd = 14;
		} else if (Constants.KEY_LIGHT_DOWN.equals(key)) {
			cmd = 15;
		}
		return cmd;
	}

	public void rePly(byte[] msg) {
		DatagramSocket dSocket = null;

		try {
			dSocket = new DatagramSocket();
			int msg_len = msg == null ? 0 : msg.length;
			DatagramPacket dPacket = new DatagramPacket(msg, 0, msg_len,
					inetAddress, port);
			dSocket.send(dPacket);
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			dSocket.close();
		}
	}

}
