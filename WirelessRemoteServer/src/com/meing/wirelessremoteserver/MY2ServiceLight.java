package com.meing.wirelessremoteserver;

import com.meing.nano.NanosicHandle;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MY2ServiceLight extends AbstractServiceLight {

	private Context mContext;
	private int lightEnable = -1;
	private Boolean isNanoConnected = false;
	private NanosicHandle nanosicHandle = null;
	private NanoHandler nanoHandler = null;
	private int currentLightValue = 0;

	public MY2ServiceLight(Context context) {
		this.mContext = context;
		nanoHandler = new NanoHandler();
		nanosicHandle = NanosicHandle.instance(mContext, nanoHandler);
		nanosicHandle.open(mContext);
	}

	// Runnable ServiceLightThread = new Runnable() {
	//
	// @Override
	// public void run() {
	//
	// }
	//
	// };
	//
	// // 启动线程ServiceKeyThread
	// public void start() {
	// // TODO Auto-generated method stub
	// new Thread(ServiceLightThread).start();
	// }
	
	public void execute(String cmd) {
		Log.d("remote", "MY2ServiceLight--->execute("+cmd+")");
		if (Constants.KEY_LIGHT_ON.equalsIgnoreCase(cmd)||Constants.KEY_LIGHT_POWER.equalsIgnoreCase(cmd)) {
			// 开关灯
			cmdPower();
		} else if (Constants.KEY_LIGHT_UP.equalsIgnoreCase(cmd)) {
			// 增加灯的亮度
			cmdLightUp();
		} else if (Constants.KEY_LIGHT_DOWN.equalsIgnoreCase(cmd)) {
			// 减小灯的亮度
			cmdLightDown();
		}
	}

	private void cmdPower() {
		if (isNanoConnected) {
			if (lightEnable == Constants.LIGHT_OPEN) {
				lightEnable = Constants.LIGHT_CLOSE;
				nanosicHandle.setLightEnable(false);
			} else {
				lightEnable = Constants.LIGHT_OPEN;
				nanosicHandle.setLightEnable(true);
				Log.d("remote", "KEY_LIGHT_POWER--------currentLightValues = "
						+ currentLightValue);
			}
		}
	}

	private void cmdLightUp() {
		if (isNanoConnected && (lightEnable == Constants.LIGHT_OPEN)) {
			currentLightValue = nanosicHandle.getLightBritness();
			Log.d("remote", "KEY_LIGHT_UP-----1---currentLightValues = "
					+ currentLightValue);
			if (currentLightValue >= Constants.LIGHT_ONE
					&& currentLightValue < Constants.LIGHT_TWO) {
				currentLightValue = Constants.LIGHT_TWO;
				nanosicHandle.setLihghBritness(currentLightValue);
			} else if (currentLightValue >= Constants.LIGHT_TWO
					&& currentLightValue < Constants.LIGHT_THREE) {
				currentLightValue = Constants.LIGHT_THREE;
				nanosicHandle.setLihghBritness(currentLightValue);
			} else if (currentLightValue >= Constants.LIGHT_THREE
					&& currentLightValue < Constants.LIGHT_FOUR) {
				currentLightValue = Constants.LIGHT_FOUR;
				nanosicHandle.setLihghBritness(currentLightValue);
			} else if (currentLightValue >= Constants.LIGHT_FOUR
					&& currentLightValue < Constants.LIGHT_FIVE) {
				currentLightValue = Constants.LIGHT_FIVE;
				nanosicHandle.setLihghBritness(currentLightValue);
			}
			Log.d("remote", "KEY_LIGHT_UP-----2---currentLightValues = "
					+ currentLightValue);
		}

	}

	private void cmdLightDown() {
		if (isNanoConnected && (lightEnable == Constants.LIGHT_OPEN)) {
			currentLightValue = nanosicHandle.getLightBritness();
			Log.d("remote", "KEY_LIGHT_DOWN-----1---currentLightValues = "
					+ currentLightValue);
			if (currentLightValue > Constants.LIGHT_FOUR) {
				currentLightValue = Constants.LIGHT_FOUR;
				nanosicHandle.setLihghBritness(currentLightValue);
			} else if (currentLightValue > Constants.LIGHT_THREE) {
				currentLightValue = Constants.LIGHT_THREE;
				nanosicHandle.setLihghBritness(currentLightValue);
			} else if (currentLightValue > Constants.LIGHT_TWO) {
				currentLightValue = Constants.LIGHT_TWO;
				nanosicHandle.setLihghBritness(currentLightValue);
			} else if (currentLightValue > Constants.LIGHT_ONE) {
				currentLightValue = Constants.LIGHT_ONE;
				nanosicHandle.setLihghBritness(currentLightValue);
			}
		}
	}

	class NanoHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case Constants.MSG_NANO_CONNECT_OK:
				Log.d("remote", "MY2ServiceLight--->NanoHandler: 灯打开成功！！！");
				isNanoConnected = true;
				lightEnable = nanosicHandle.getLightEnable();
				currentLightValue = nanosicHandle.getLightBritness();
				break;
			case Constants.MSG_NANO_CONNECT_FAIL:
				Log.d("remote", "MY2ServiceLight--->NanoHandler: 灯打开失败！！！");
				isNanoConnected = false;
				break;
			}
		}

	}
}
