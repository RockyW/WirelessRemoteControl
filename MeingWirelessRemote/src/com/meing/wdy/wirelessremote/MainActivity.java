package com.meing.wdy.wirelessremote;

import zxing.toptech.utils.Constants;
import zxing.toptech.utils.HandleKeyUtil;
import zxing.toptech.utils.SendDataPacket;
import zxing.toptech.utils.Util;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity implements OnClickListener,
		OnTouchListener {

	private FrameLayout frameLayout = null;
	private FrameLayout frameLayoutVoice = null;
	private LinearLayout upDownlayout = null;
	private LinearLayout leftRightLayout = null;
	private RelativeLayout voiceLayout = null;
	private ImageView ivDirBg = null;

	private ImageView ivPower = null;
	private ImageView ivMatch = null;
	private ImageView ivMute = null;
	private ImageView ivSetting = null;
	private ImageView ivKeyBoard = null;
	private ImageView iv3D = null;
	private ImageView ivDirUp = null;
	private ImageView ivDirDown = null;
	private ImageView ivDirLeft = null;
	private ImageView ivDirRight = null;
	private ImageView ivOk = null;
	private ImageView ivBack = null;
	private ImageView ivMenu = null;
	private ImageView ivHome = null;
	private ImageView ivVoiceUp = null;
	private ImageView ivVoiceDown = null;

	Vibrator vibrator = null;
	private Handler mHandler = null;
	private HandleKeyUtil mHandleKeyUtil = null;
	private SharedPreferences sp;
	private String connectionValue = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		mHandler = new UpdateUIHandler();
		mHandleKeyUtil = new HandleKeyUtil();
		initView();

		sp = getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
		connectionValue = sp.getString(Constants.CONNECTION_KEY_SHARED, "");
	}

	private void initView() {
		// TODO Auto-generated method stub
		frameLayoutVoice = (FrameLayout) findViewById(R.id.framelayout_voice);
		frameLayout = (FrameLayout) findViewById(R.id.center_layout);
		ViewTreeObserver keyTreeObserver = frameLayout.getViewTreeObserver();
		keyTreeObserver.addOnGlobalLayoutListener(keyTreeObserverListener);

		upDownlayout = (LinearLayout) findViewById(R.id.up_down_layout);
		leftRightLayout = (LinearLayout) findViewById(R.id.left_right_layout);
		voiceLayout = (RelativeLayout) findViewById(R.id.voice_layout);
		ivDirBg = (ImageView) findViewById(R.id.label_dir_bg);

		ivPower = (ImageView) findViewById(R.id.label_power);
		ivPower.setOnClickListener(this);

		ivMatch = (ImageView) findViewById(R.id.label_match);
		ivMatch.setOnClickListener(this);

		ivKeyBoard = (ImageView) findViewById(R.id.label_keyboard);
		ivKeyBoard.setOnClickListener(this);

		ivMute = (ImageView) findViewById(R.id.label_mute);
		ivMute.setOnClickListener(this);

		ivSetting = (ImageView) findViewById(R.id.label_setting);
		ivSetting.setOnClickListener(this);

		iv3D = (ImageView) findViewById(R.id.label_3d);
		iv3D.setOnClickListener(this);
		
		ivHome = (ImageView) findViewById(R.id.label_home);
		ivHome.setOnClickListener(this);

		ivDirUp = (ImageView) findViewById(R.id.label_up);
		ivDirUp.setOnClickListener(this);
		ivDirUp.setOnTouchListener(this);

		ivDirDown = (ImageView) findViewById(R.id.label_down);
		ivDirDown.setOnClickListener(this);
		ivDirDown.setOnTouchListener(this);

		ivDirLeft = (ImageView) findViewById(R.id.label_left);
		ivDirLeft.setOnClickListener(this);
		ivDirLeft.setOnTouchListener(this);

		ivDirRight = (ImageView) findViewById(R.id.label_right);
		ivDirRight.setOnClickListener(this);
		ivDirRight.setOnTouchListener(this);

		ivOk = (ImageView) findViewById(R.id.label_ok);
		ivOk.setOnClickListener(this);
		ivOk.setOnTouchListener(this);

		ivBack = (ImageView) findViewById(R.id.label_back);
		ivBack.setOnClickListener(this);

		ivMenu = (ImageView) findViewById(R.id.label_menu);
		ivMenu.setOnClickListener(this);

		ivVoiceUp = (ImageView) findViewById(R.id.label_voice_up);
		ivVoiceUp.setOnClickListener(this);
		ivVoiceUp.setOnTouchListener(this);

		ivVoiceDown = (ImageView) findViewById(R.id.label_voice_down);
		ivVoiceDown.setOnClickListener(this);
		ivVoiceDown.setOnTouchListener(this);
	}

	// 动态设置中间方向按钮的layout，这样就不会采用布局中的dp来设置，影响自适应多屏
	OnGlobalLayoutListener keyTreeObserverListener = new OnGlobalLayoutListener() {

		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			int vircleHeight = frameLayout.getHeight();
			int vircleWeight = frameLayout.getWidth();
			LayoutParams frameParams = (LayoutParams) frameLayout
					.getLayoutParams();
			frameParams.width = vircleHeight;
			frameLayout.setLayoutParams(frameParams);
		}

	};

	class UpdateUIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == Constants.SEND_FAILURE)
				Toast.makeText(getApplicationContext(),
						R.string.check_network_connection, Toast.LENGTH_LONG)
						.show();
		}
	}

	@Override
	public void onClick(View view) {
		String keyName = "";
		vibrator.vibrate(new long[] { 0, 100 }, -1);
		int viewid = view.getId();
		switch (viewid) {
		case R.id.label_power:
			keyName = Constants.KEY_POWER;
			break;
		case R.id.label_match:
			Intent intent = new Intent(this, MatchActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.label_keyboard:
			Intent intent1 = new Intent(this, KeyBoardActivity.class);
			intent1.putExtra(Constants.STORE_CONNECTION_KEY, connectionValue);
			startActivity(intent1);
			finish();
			break;
		case R.id.label_mute:
			keyName = Constants.KEY_MUTE;
			break;
		case R.id.label_setting:
			keyName = Constants.KEY_SETTING;
			break;
		case R.id.label_3d:
			keyName = Constants.KEY_3DSETTING;
			break;
		case R.id.label_up:
			keyName = Constants.KEY_UP;
			break;
		case R.id.label_down:
			keyName = Constants.KEY_DOWN;
			break;
		case R.id.label_home:
			keyName = Constants.KEY_HOME;
			break;
		case R.id.label_left:
			keyName = Constants.KEY_LEFT;
			break;
		case R.id.label_right:
			keyName = Constants.KEY_RIGHT;
			break;
		case R.id.label_ok:
			keyName = Constants.KEY_OK;
			break;
		case R.id.label_back:
			keyName = Constants.KEY_BACK;
			break;
		case R.id.label_menu:
			keyName = Constants.KEY_MENU;
			break;
		case R.id.label_voice_up:
			keyName = Constants.KEY_VOICE_UP;
			break;
		case R.id.label_voice_down:
			keyName = Constants.KEY_VOICE_DOWN;
			break;
		default:
			keyName = "";
			break;
		}
		if (keyName != null && !(keyName.equals(""))) {
			keyName = connectionValue+keyName;
		}
		Log.d("remote", "keyName = " + keyName);
		boolean isSend = mHandleKeyUtil.sendKey(keyName);
		if (!isSend) {
			mHandler.sendEmptyMessageDelayed(Constants.SEND_FAILURE, 200);
		}

	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		int viewId = view.getId();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			switch (viewId) {
			case R.id.label_up:
				ivDirBg.setImageResource(R.drawable.icon_dir_up);
				break;
			case R.id.label_down:
				ivDirBg.setImageResource(R.drawable.icon_dir_down);
				break;
			case R.id.label_left:
				ivDirBg.setImageResource(R.drawable.icon_dir_left);
				break;
			case R.id.label_right:
				ivDirBg.setImageResource(R.drawable.icon_dir_right);
				break;
			case R.id.label_ok:
				ivDirBg.setImageResource(R.drawable.icon_dir_ok);
				break;
			case R.id.label_voice_up:
				voiceLayout.setBackgroundResource(R.drawable.voice_up_bg);
				break;
			case R.id.label_voice_down:
				voiceLayout.setBackgroundResource(R.drawable.voice_down_bg);
				break;
			default:
				break;
			}
			break;
		case MotionEvent.ACTION_UP:
			switch (viewId) {
			case R.id.label_up:
			case R.id.label_down:
			case R.id.label_left:
			case R.id.label_right:
			case R.id.label_ok:
				ivDirBg.setImageResource(R.drawable.icon_dir_normal);
				break;
			case R.id.label_voice_up:
			case R.id.label_voice_down:
				voiceLayout.setBackgroundResource(R.drawable.voice_normal_bg);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		ExitApp();
	}

	private long exitTime = 0;

	// 按两次返回键退出程序
	public void ExitApp() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(this,
					getResources().getString(R.string.exit_apllication),
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			this.finish();
		}

	}

}
