package com.meing.wirelessremoteserver;

import java.io.OutputStream;

import android.util.Log;

public class RootShellCmd {
	private static RootShellCmd rootShellCmd;
	private OutputStream os;

	public static RootShellCmd getInstance() {
		if (rootShellCmd == null) {
			synchronized (RootShellCmd.class) {
				if (rootShellCmd == null) {
					rootShellCmd = new RootShellCmd();
				}
			}
		}
		return rootShellCmd;
	}

	/**
	 * 执行shell指令
	 * 
	 * @param cmd
	 *            指令
	 */
	public final void exec(String cmd) {
		Log.d("remote", "--------------- cmd = "+cmd);
		try {
			if (os == null) {
				os = Runtime.getRuntime().exec("su").getOutputStream();
			}
			os.write(cmd.getBytes());
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 后台模拟全局按键
	 * 
	 * @param keyCode
	 *            键值
	 */
	public final void simulateKey(int keyCode) {
		exec("input keyevent " + keyCode);
	}
}
