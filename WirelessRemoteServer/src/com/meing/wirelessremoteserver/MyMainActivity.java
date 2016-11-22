package com.meing.wirelessremoteserver;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MyMainActivity extends Activity {
	private static final String TAG = "MyMainActivity";

	private CheckBox serviceSwitch;
	private Intent intent;
	private boolean flag = false;
	private Button backstageButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		intent = new Intent(MyMainActivity.this, RemoteService.class);
		serviceSwitch = (CheckBox) findViewById(R.id.service_switch_id);
		backstageButton = (Button) findViewById(R.id.backstage_button);

		boolean isRunnning = isServiceRunning();
		if (isRunnning) {
			serviceSwitch.setChecked(true);
			serviceSwitch.setText(getResources().getString(R.string.service_running));
			backstageButton.setEnabled(true);
		}

		serviceSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startService(intent);
					saveState(true, R.string.service_running);
				} else {
					MyMainActivity.this.stopService(intent);
					saveState(false, R.string.service_closed);
				}

			}

		});
		backstageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Runtime.getRuntime().exec("input keyevent " + KeyEvent.KEYCODE_HOME);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Log.e(TAG, "the program input keyevent dose not execute, please check out!");
							}
						}

					}).start();
				}
			}
		});
		super.onCreate(savedInstanceState);
	}

	private void saveState(boolean isTrue, int isd) {
		flag = isTrue;
		backstageButton.setEnabled(isTrue);
		serviceSwitch.setText(getResources().getString(isd));
	}

	/**
	 * @return true if stack has RemoteService 当前栈中是否有RemoteService
	 */
	private boolean isServiceRunning() {
		ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningService = am.getRunningServices(20);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().equals("com.example.wirelessremoteserver.RemoteService")) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		saveState(false, R.string.service_closed);
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// user can implement custom code
		return super.onKeyDown(keyCode, event);
	}

}
