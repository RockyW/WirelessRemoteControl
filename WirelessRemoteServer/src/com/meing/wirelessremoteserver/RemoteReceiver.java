package com.meing.wirelessremoteserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RemoteReceiver extends BroadcastReceiver {

	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(ACTION)) {
			intent = new Intent(context, RemoteService.class);
			
			context.startService(new Intent(context, RemoteService.class));// 启动遥控器服务
			Toast.makeText(context, "RemoteService service has started!",
					Toast.LENGTH_LONG).show();
		}
	}

}
