package com.meing.wdy.wirelessremote;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.meing.wdy.wirelessremote.MainActivity.UpdateUIHandler;

import zxing.toptech.utils.Constants;
import zxing.toptech.utils.DeviceInfo;
import zxing.toptech.utils.HandleKeyUtil;
import zxing.toptech.utils.HexToReverse;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class LightActivity extends Activity implements OnClickListener,
		OnLongClickListener, OnTouchListener {

	private FrameLayout frameLayout;
	private LinearLayout hueLayout;
	private LinearLayout lightLayout;
	private LinearLayout modelLayout;
	private ImageView lightOn;
	private ImageView lightOff;
	private LinearLayout modelOne;
	private LinearLayout modelTwo;
	private LinearLayout modelThree;
	private LinearLayout modelFour;
	private LinearLayout modelFive;
	private ImageView hueWarm;
	private ImageView hueCold;
	private ImageView lightUp;
	private ImageView lightDown;
	private ImageView lightModel;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private TextView tvState;

	Vibrator vibrator = null;
	private SharedPreferences sp;
	private String connectionValue = null;
	private Handler mHandler = null;
	private HandleKeyUtil mHandleKeyUtil = null;

	private static final int CUSTOM_ONE = 1;
	private static final int CUSTOM_TWO = 2;
	private static final int CUSTOM_THREE = 3;
	private static final String SUCCESS = "1";
	private static final String FAILURE = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.light_layout);

		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		sp = getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
		connectionValue = sp.getString(Constants.CONNECTION_KEY_SHARED, "");
		mHandler = new UpdateUIHandler();
		mHandleKeyUtil = new HandleKeyUtil();
		initView();
	}

	class UpdateUIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int what = msg.what;
			switch (what) {
			case Constants.SEND_FAILURE:
				Toast.makeText(getApplicationContext(),
						R.string.check_network_connection, Toast.LENGTH_LONG)
						.show();
				break;
			case Constants.UPDATE_UI:
				String temp = (String) msg.obj;
				if (Constants.KEY_LIGHT_OFF.equals(temp)) {
					setTipText(getResources().getString(R.string.tip_off));
				} else if (Constants.KEY_MODEL_ONE.equals(temp)) {
					setTipText(getResources().getString(R.string.tip_mode1));
				} else if (Constants.KEY_MODEL_TWO.equals(temp)) {
					setTipText(getResources().getString(R.string.tip_mode2));
				} else if (Constants.KEY_MODEL_THREE.equals(temp)
						|| Constants.KEY_SET_MODEL_ONE.equals(temp)) {
					setTipText(getResources().getString(R.string.tip_mode3));
				} else if (Constants.KEY_MODEL_FOUR.equals(temp)
						|| Constants.KEY_SET_MODEL_TWO.equals(temp)) {
					setTipText(getResources().getString(R.string.tip_mode4));
				} else if (Constants.KEY_MODEL_FIVE.equals(temp)
						|| Constants.KEY_SET_MODEL_THREE.equals(temp)) {
					setTipText(getResources().getString(R.string.tip_mode5));
				} else {
					setTipText(getResources().getString(R.string.tip_mode6));
				}
				break;
			default:
				break;
			}
		}
	}

	private void initView() {
		frameLayout = (FrameLayout) findViewById(R.id.model_layout);
		ViewTreeObserver keyTreeObserver = frameLayout.getViewTreeObserver();
		keyTreeObserver.addOnGlobalLayoutListener(keyTreeObserverListener);

		tvState = (TextView) findViewById(R.id.tv_state);

		hueLayout = (LinearLayout) findViewById(R.id.hue_layout);
		lightLayout = (LinearLayout) findViewById(R.id.light_layout);
		modelLayout = (LinearLayout) findViewById(R.id.light_model_layout);

		lightOn = (ImageView) findViewById(R.id.label_light_on);
		lightOn.setOnClickListener(this);

		lightOff = (ImageView) findViewById(R.id.label_light_off);
		lightOff.setOnClickListener(this);

		modelOne = (LinearLayout) findViewById(R.id.light_model_one);
		modelOne.setOnClickListener(this);
		modelOne.setOnTouchListener(this);

		modelTwo = (LinearLayout) findViewById(R.id.light_model_two);
		modelTwo.setOnClickListener(this);
		modelTwo.setOnTouchListener(this);

		modelThree = (LinearLayout) findViewById(R.id.light_model_three);
		modelThree.setOnClickListener(this);
		modelThree.setOnTouchListener(this);
		modelThree.setOnLongClickListener(this);

		modelFour = (LinearLayout) findViewById(R.id.light_model_four);
		modelFour.setOnClickListener(this);
		modelFour.setOnTouchListener(this);
		modelFour.setOnLongClickListener(this);

		modelFive = (LinearLayout) findViewById(R.id.light_model_five);
		modelFive.setOnClickListener(this);
		modelFive.setOnTouchListener(this);
		modelFive.setOnLongClickListener(this);

		hueWarm = (ImageView) findViewById(R.id.label_hue_warm);
		hueWarm.setOnClickListener(this);
		hueWarm.setOnTouchListener(this);

		hueCold = (ImageView) findViewById(R.id.label_hue_cold);
		hueCold.setOnClickListener(this);
		hueCold.setOnTouchListener(this);

		lightUp = (ImageView) findViewById(R.id.label_light_up);
		lightUp.setOnClickListener(this);
		lightUp.setOnTouchListener(this);

		lightDown = (ImageView) findViewById(R.id.label_light_down);
		lightDown.setOnClickListener(this);
		lightDown.setOnTouchListener(this);

		lightModel = (ImageView) findViewById(R.id.label_model);
		lightModel.setOnClickListener(this);

		textView1 = (TextView) findViewById(R.id.label_model_three_title);
		textView2 = (TextView) findViewById(R.id.label_model_four_title);
		textView3 = (TextView) findViewById(R.id.label_model_five_title);

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
			frameParams.width = (int) (vircleHeight * 4 / 3);
			frameLayout.setLayoutParams(frameParams);
		}

	};

	public void setTipText(String tip) {
		tvState.setText(tip);
	}

	@Override
	public void onClick(View arg0) {
		String keyName = "";
		vibrator.vibrate(new long[] { 0, 100 }, -1);
		int id = arg0.getId();
		switch (id) {
		case R.id.label_light_on:
			keyName = Constants.KEY_LIGHT_ON;
			setTipText(getResources().getString(R.string.tip_mode6));
			break;
		case R.id.label_light_off:
			keyName = Constants.KEY_LIGHT_OFF;
			setTipText(getResources().getString(R.string.tip_mode6));
			break;
		case R.id.light_model_one:
			keyName = Constants.KEY_MODEL_ONE;
			setTipText(getResources().getString(R.string.tip_mode1));
			break;
		case R.id.light_model_two:
			keyName = Constants.KEY_MODEL_TWO;
			setTipText(getResources().getString(R.string.tip_mode2));
			break;
		case R.id.light_model_three:
			keyName = Constants.KEY_MODEL_THREE;
			setTipText(getResources().getString(R.string.tip_mode3));
			break;
		case R.id.light_model_four:
			keyName = Constants.KEY_MODEL_FOUR;
			setTipText(getResources().getString(R.string.tip_mode4));
			break;
		case R.id.light_model_five:
			keyName = Constants.KEY_MODEL_FIVE;
			setTipText(getResources().getString(R.string.tip_mode5));
			break;
		case R.id.label_hue_warm:
			keyName = Constants.KEY_HUE_WARM;
			setTipText(getResources().getString(R.string.tip_mode6));
			break;
		case R.id.label_hue_cold:
			keyName = Constants.KEY_HUE_COLD;
			setTipText(getResources().getString(R.string.tip_mode6));
			break;
		case R.id.label_light_up:
			keyName = Constants.KEY_LIGHT_UP;
			setTipText(getResources().getString(R.string.tip_mode6));
			break;
		case R.id.label_light_down:
			keyName = Constants.KEY_LIGHT_DOWN;
			setTipText(getResources().getString(R.string.tip_mode6));
			break;
		case R.id.label_model:
			keyName = Constants.KEY_LIGHT_MODEL;
			setTipText(getResources().getString(R.string.tip_mode6));
			break;
		default:
			keyName = "";
			break;
		}
		sendKey(keyName);
	}

	public void sendKey(String keyName) {
		if (keyName != null && !(keyName.equals(""))) {
			final String temp = keyName;
			final String keyMsg = connectionValue + temp;

			new Thread(new Runnable() {
				@Override
				public void run() {

					DatagramSocket dSocket = null;
					InetAddress local = null;
					byte[] msg = HexToReverse.getHexByte(keyMsg);
					byte[] buf = new byte[32];
					try {
						local = InetAddress.getByName(Constants.HOST_ADDR);
						dSocket = new DatagramSocket();
						DatagramPacket dPacket = new DatagramPacket(msg, 0,
								msg.length, local, Constants.SERVER_PORT);
						dSocket.send(dPacket);

						// receive
						DatagramPacket myPacket = new DatagramPacket(buf,
								buf.length);
						dSocket.receive(myPacket);
						byte[] data = myPacket.getData();
						String hex = HexToReverse.printHexString(data);
						String receive = HexToReverse.HextoString(hex);
						String[] receives = receive.split(",");
						// receives[0]:判断命令时候执行成功
						// receives[1]:模式
						// receives[2]:功率
						for (int i = 0; i < receives.length; i++) {
							System.out.println("receives[" + i + "] = "
									+ receives[i]);
						}
						if (receives[0] != null && SUCCESS.equals(receives[0])) {
							Message message = mHandler.obtainMessage();
							message.what = Constants.UPDATE_UI;
							message.obj = temp;
							mHandler.sendMessage(message);
						}

					} catch (Exception e) {
						mHandler.sendEmptyMessageDelayed(
								Constants.SEND_FAILURE, 200);
					} finally {
						dSocket.close();
					}
				}
			}).start();
		}
	}

	@Override
	public boolean onLongClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.light_model_three:
			showCustomDialog(Constants.KEY_SET_MODEL_ONE, textView1.getText()
					.toString());
			break;
		case R.id.light_model_four:
			showCustomDialog(Constants.KEY_SET_MODEL_TWO, textView2.getText()
					.toString());
			break;
		case R.id.light_model_five:
			showCustomDialog(Constants.KEY_SET_MODEL_THREE, textView3.getText()
					.toString());
			break;
		}
		return false;
	}

	public void showCustomDialog(final String key, String msg) {
		final EditText edit = new EditText(this);
		edit.setText(msg);
		edit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) }); // Max
																				// input
																				// length
																				// 6
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(getResources().getString(R.string.dialog_tip))
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(edit)
				.setPositiveButton(getResources().getString(R.string.qr_ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								sendKey(key);
								if (key.equals(Constants.KEY_SET_MODEL_ONE)) {
									textView1.setText(edit.getText());
								} else if (key
										.equals(Constants.KEY_SET_MODEL_TWO)) {
									textView2.setText(edit.getText());
								} else if (key
										.equals(Constants.KEY_SET_MODEL_THREE)) {
									textView3.setText(edit.getText());
								}
							}
						})
				.setNegativeButton(getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});
		builder.setCancelable(true);
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true); // 点击其他地方，退出Dialog
		dialog.show();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {

		int id = arg0.getId();
		switch (arg1.getAction()) {
		case MotionEvent.ACTION_DOWN:
			switch (id) {
			case R.id.light_model_one:
				modelLayout
						.setBackgroundResource(R.drawable.contextual_model_one);
				break;
			case R.id.light_model_two:
				modelLayout
						.setBackgroundResource(R.drawable.contextual_model_two);
				break;
			case R.id.light_model_three:
				modelLayout
						.setBackgroundResource(R.drawable.contextual_model_three);
				break;
			case R.id.light_model_four:
				modelLayout
						.setBackgroundResource(R.drawable.contextual_model_four);
				break;
			case R.id.light_model_five:
				modelLayout
						.setBackgroundResource(R.drawable.contextual_model_five);
				break;
			case R.id.label_hue_warm:
				hueLayout.setBackgroundResource(R.drawable.hue_warm_bg);
				break;
			case R.id.label_hue_cold:
				hueLayout.setBackgroundResource(R.drawable.hue_cold_bg);
				break;
			case R.id.label_light_up:
				lightLayout.setBackgroundResource(R.drawable.light_up);
				break;
			case R.id.label_light_down:
				lightLayout.setBackgroundResource(R.drawable.light_down);
				break;
			}
			break;
		case MotionEvent.ACTION_UP:
			switch (id) {
			case R.id.light_model_one:
			case R.id.light_model_two:
			case R.id.light_model_three:
			case R.id.light_model_four:
			case R.id.light_model_five:
				modelLayout
						.setBackgroundResource(R.drawable.contextual_model_normal);
				break;
			case R.id.label_hue_warm:
			case R.id.label_hue_cold:
				hueLayout.setBackgroundResource(R.drawable.hue_normal_bg);
				break;
			case R.id.label_light_up:
			case R.id.label_light_down:
				lightLayout.setBackgroundResource(R.drawable.light_normal);
				break;
			}
			break;

		}
		switch (id) {
		case R.id.light_model_one:
			break;
		case R.id.light_model_two:
			break;
		case R.id.light_model_three:
			break;
		case R.id.light_model_four:
			break;
		case R.id.light_model_five:
			break;
		case R.id.label_hue_warm:
			break;
		case R.id.label_hue_cold:
			break;
		case R.id.label_light_up:
			break;
		case R.id.label_light_down:
			break;
		}

		return false;
	}

}
