package com.meing.wirelessremoteserver;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

public class ServiceKey {

	private String ReceivedKey;
	private Context mContext;
	Instrumentation inst = new Instrumentation();

	public ServiceKey(Context context) {
		this.mContext = context;
	}

	public void setKey(String receivedFromClient) {
		this.ReceivedKey = receivedFromClient;
	}

	Runnable ServiceKeyThread = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 获取实际按键值
			int keycode = getKey(ReceivedKey);
			// 发送按键值
			if (keycode > 0) {
				inst.sendKeyDownUpSync(keycode);
			}
		}

	};

	// 启动线程ServiceKeyThread
	public void start() {
		// TODO Auto-generated method stub
		new Thread(ServiceKeyThread).start();
	}

	// 把接收到的按键信息转化成实际的按键值
	protected int getKey(String ReceivedKey) {
		// TODO Auto-generated method stub
		int keycode = -1;
		if (Constants.KEY_POWER.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_POWER;
		} else if (Constants.KEY_MUTE.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_VOLUME_MUTE;
		} else if (Constants.KEY_SETTING.equalsIgnoreCase(ReceivedKey)) {
			keycode = 264;
		} else if (Constants.KEY_3DSETTING.equalsIgnoreCase(ReceivedKey)) {
			keycode = 292;
		} else if (Constants.KEY_UP.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_DPAD_UP;
		} else if (Constants.KEY_DOWN.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_DPAD_DOWN;
		} else if (Constants.KEY_LEFT.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_DPAD_LEFT;
		} else if (Constants.KEY_RIGHT.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_DPAD_RIGHT;
		} else if (Constants.KEY_OK.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_ENTER;
		} else if (Constants.KEY_BACK.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_BACK;
		} else if (Constants.KEY_MENU.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_MENU;
		} else if (Constants.KEY_HOME.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_HOME;
		} else if (Constants.KEY_VOICE_UP.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_VOLUME_UP;
		} else if (Constants.KEY_VOICE_DOWN.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_VOLUME_DOWN;
		} else if (Constants.KEY_NUM0.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_0;
		} else if (Constants.KEY_NUM1.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_1;
		} else if (Constants.KEY_NUM2.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_2;
		} else if (Constants.KEY_NUM3.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_3;
		} else if (Constants.KEY_NUM4.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_4;
		} else if (Constants.KEY_NUM5.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_5;
		} else if (Constants.KEY_NUM6.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_6;
		} else if (Constants.KEY_NUM7.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_7;
		} else if (Constants.KEY_NUM8.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_8;
		} else if (Constants.KEY_NUM9.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_9;
		} else if (Constants.KEY_LOWER_A.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_A;
		} else if (Constants.KEY_LOWER_B.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_B;
		} else if (Constants.KEY_LOWER_C.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_C;
		} else if (Constants.KEY_LOWER_D.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_D;
		} else if (Constants.KEY_LOWER_E.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_E;
		} else if (Constants.KEY_LOWER_F.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_F;
		} else if (Constants.KEY_LOWER_G.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_G;
		} else if (Constants.KEY_LOWER_H.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_H;
		} else if (Constants.KEY_LOWER_I.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_I;
		} else if (Constants.KEY_LOWER_J.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_J;
		} else if (Constants.KEY_LOWER_K.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_K;
		} else if (Constants.KEY_LOWER_L.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_L;
		} else if (Constants.KEY_LOWER_M.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_M;
		} else if (Constants.KEY_LOWER_N.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_N;
		} else if (Constants.KEY_LOWER_O.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_O;
		} else if (Constants.KEY_LOWER_P.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_P;
		} else if (Constants.KEY_LOWER_Q.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_Q;
		} else if (Constants.KEY_LOWER_R.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_R;
		} else if (Constants.KEY_LOWER_S.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_S;
		} else if (Constants.KEY_LOWER_T.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_T;
		} else if (Constants.KEY_LOWER_U.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_U;
		} else if (Constants.KEY_LOWER_V.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_V;
		} else if (Constants.KEY_LOWER_W.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_W;
		} else if (Constants.KEY_LOWER_X.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_X;
		} else if (Constants.KEY_LOWER_Y.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_Y;
		} else if (Constants.KEY_LOWER_Z.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_Z;
		} else if (Constants.KEY_UPPER_A.equalsIgnoreCase(ReceivedKey)) {
			// InputConnection connection = getCurrentInputConnection();
			// Log.d("remote", "input upper A");
			// int keyChar = 'A';
			// String result = String.valueOf((char) keyChar);
			// getCurrentInputConnection().commitText(result, result.length());
		}
		// else if (Constants.KEY_UPPER_B.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_C.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_D.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_E.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_F.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_G.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_H.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_I.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_J.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_K.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_L.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_M.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_N.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_O.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_P.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_Q.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_R.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_S.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_T.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_U.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_V.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_W.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_X.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_Y.equalsIgnoreCase(ReceivedKey)) {
		//
		// } else if (Constants.KEY_UPPER_Z.equalsIgnoreCase(ReceivedKey)) {
		//
		// }
		else if (Constants.KEY_PLUS.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_PLUS;
		} else if (Constants.KEY_MINUS.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_MINUS;
		} else if (Constants.KEY_STAR.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_STAR;
		} else if (Constants.KEY_SLASH.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_SLASH;
		} else if (Constants.KEY_EQUALS.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_EQUALS;
		} else if (Constants.KEY_AT.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_AT;
		} else if (Constants.KEY_POUND.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_POUND;
		} else if (Constants.KEY_APOSTROPHE.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_APOSTROPHE;
		} else if (Constants.KEY_BACKSLASH.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_BACKSLASH;
		} else if (Constants.KEY_COMMA.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_COMMA;
		} else if (Constants.KEY_PERIOD.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_PERIOD;
		} else if (Constants.KEY_LEFT_BRACKET.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_LEFT_BRACKET;
		} else if (Constants.KEY_RIGHT_BRACKET.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_RIGHT_BRACKET;
		} else if (Constants.KEY_SEMICOLON.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_SEMICOLON;
		} else if (Constants.KEY_GRAVE.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_GRAVE;
		} else if (Constants.KEY_SPACE.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_SPACE;
		} else if (Constants.KEY_CAPS_LOCK.equalsIgnoreCase(ReceivedKey)) {
			// keycode = KeyEvent.KEYCODE_CAPS_LOCK;
			execCmd("2");
			// rootShellCmd.simulateKey(KeyEvent.KEYCODE_UA);
			// keycode = KeyEvent.KEYCODE_UA;
		} else if (Constants.KEY_ENTER.equalsIgnoreCase(ReceivedKey)) {
			// keycode = KeyEvent.KEYCODE_ENTER;
			// execCmd("106");
			// rootShellCmd.simulateKey(KeyEvent.KEYCODE_VOLUME_UP);
		} else if (Constants.KEY_DELETE.equalsIgnoreCase(ReceivedKey)) {
			keycode = KeyEvent.KEYCODE_DEL;
		}
		return keycode;
	}

	public void execCmd(String cmd) {
		Log.d("remote", "------------------------- cmd = " + cmd);
		execShell("su");
		execShell("chmod 666 /dev/input/event0");
		execShell("sendevent /dev/input/event0 1 " + cmd + " 1");
		execShell("sendevent /dev/input/event0 0 0 0");
		execShell("sendevent /dev/input/event0 1 " + cmd + " 0");
		execShell("sendevent /dev/input/event0 0 0 0");
	}

	public void execShell(String shell) {
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec(shell);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
