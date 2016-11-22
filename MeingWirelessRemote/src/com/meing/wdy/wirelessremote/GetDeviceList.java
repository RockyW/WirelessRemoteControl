package com.meing.wdy.wirelessremote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.meing.wdy.wirelessremote.MainActivity.UpdateUIHandler;

import zxing.toptech.utils.Constants;
import zxing.toptech.utils.DeviceInfo;
import zxing.toptech.utils.HandleKeyUtil;
import zxing.toptech.utils.HexToReverse;
import zxing.toptech.utils.Util;
import android.app.Dialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GetDeviceList {

	private final static String TAG = "GetDeviceList";
	private Dialog dialog;
	private Context mContext;
	private CallBackInterface mCallBack;

	private ListView bind_device_list;
	private ArrayList<DeviceInfo> lists;
	private String ip;
	private DeviceAdapter adapter;
	private Animation loadinganimation;
	private ImageView loadingview;
	private Button cancel;
	private View bind_device_view;
	private HandleKeyUtil mHandleKeyUtil = null;
	private UpdateUIHandler mHandler;

	class UpdateUIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case Constants.SEND_FAILURE:
				Toast.makeText(mContext, R.string.check_network_connection,
						Toast.LENGTH_LONG).show();
				break;
			case 10:
				Log.v(TAG, "listClient.size:" + String.valueOf(lists.size()));
				Log.v(TAG, "list devicenameinhandler"
						+ lists.get(0).getDevicename());
				loadinganimation.cancel();
				loadingview.setAnimation(null);
				loadingview.setVisibility(View.GONE);
				// emarket_list.setAdapter(adapter);
				bind_device_list.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
				break;
			}
		}
	}

	public GetDeviceList(Context context, CallBackInterface callBack) {
		this.mContext = context;
		this.mCallBack = callBack;
		mHandleKeyUtil = new HandleKeyUtil();
		mHandler = new UpdateUIHandler();
	}

	public void show() {
		dialog = new Dialog(mContext, R.style.dialog);

		bind_device_view = LayoutInflater.from(mContext).inflate(
				R.layout.search, null);
		bind_device_list = (ListView) bind_device_view
				.findViewById(R.id.search_list);
		loadinganimation = AnimationUtils.loadAnimation(mContext,
				R.anim.loading_animation);
		loadingview = (ImageView) bind_device_view
				.findViewById(R.id.imageViewLoading);
		loadingview.startAnimation(loadinganimation);

		cancel = (Button) bind_device_view.findViewById(R.id.search_cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constants.isget = false;
				dialog.dismiss();

			}

		});
		lists = new ArrayList<DeviceInfo>();
		ip = "255.255.255.255";
		adapter = new DeviceAdapter(mContext, lists);
		bind_device_list.setAdapter(adapter);

		// 点击条目即选择连接的目标设备。此时将设备的信息包括ip,id,name保存到本地，此后操作中将会被用到
		bind_device_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DeviceInfo deviceInfo = lists.get(arg2);
				mCallBack.setSerial(deviceInfo.getDevicename());
				Constants.isget = false;
				dialog.dismiss();
			}

		});

		int w = Util.dip2px(mContext, 270);
		int h = Util.dip2px(mContext, 270);

		dialog.setContentView(bind_device_view,
				new ViewGroup.LayoutParams(w, h));
		loadingview.startAnimation(loadinganimation);
		String localip = "";
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			localip = Util.getWIFILocalIpAdress(wifiManager);
		} else {
			localip = Util.getLocalIpAddress();
		}

		Log.v(TAG, localip + "--->" + ip);
		new Thread(new Runnable() {
			@Override
			public void run() {

				DatagramSocket dSocket = null;
				InetAddress local = null;
				byte[] msg = HexToReverse.getHexByte(Constants.SEARCH_DEVICE);
				byte[] buf = new byte[64];
				try {
					local = InetAddress.getByName(Constants.HOST_ADDR);
					dSocket = new DatagramSocket();
					DatagramPacket dPacket = new DatagramPacket(msg, 0,
							msg.length, local, Constants.SERVER_PORT);
					dSocket.send(dPacket);

					Constants.isget = true;
					while (Constants.isget) {
						boolean noExist = true;
						DatagramPacket myPacket = new DatagramPacket(buf,
								buf.length);
						dSocket.receive(myPacket);
						byte[] data = myPacket.getData();
						String hex = HexToReverse.printHexString(data);
						String deviceName = HexToReverse.HextoString(hex);
						if (deviceName != null && deviceName.length() > 0) {
							for (int i = 0; i < lists.size(); i++) {
								if (lists.get(i).getDevicename()
										.equals(deviceName)) {
									noExist = false;
									// 如果获取的设备id在list中存在，那么返回。
									break;
								}
							}
							if (noExist) {
								DeviceInfo device = new DeviceInfo();
								// 如果这是一个在list中不存在的新的条目，则将其添加到list中。
								device.setDevicename(deviceName);
								lists.add(device);
								Log.v(TAG, "list devicenameingetmessagenoExist"
										+ lists.get(0).getDevicename());
							}

							mHandler.sendEmptyMessage(10);
						}

					}

				} catch (Exception e) {

					e.printStackTrace();

				} finally {
					dSocket.close();
				}
			}
		}).start();
		dialog.show();
	}
}
