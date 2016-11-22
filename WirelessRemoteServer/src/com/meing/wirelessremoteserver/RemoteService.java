package com.meing.wirelessremoteserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemProperties;
import android.util.Log;

public class RemoteService extends Service {

	private Boolean startrunflag = true;
	private int recivebuffersize = 32;
	private String serialNum = null;

	private ServiceKey serviceKey;
	private AbstractServiceLight serviceLight;
	private HandleKey handleKey;

	// ActivityManager activityManager = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		serviceKey = new ServiceKey(this);
		String modual = getModual();
		Log.d("remote", "modual = " + modual);
		if (modual.equals(Constants.MODUAL_MM1)) {
			serviceLight = new MM1ServiceLight(this);
			handleKey = MM1HandleKey.getInstance(RemoteService.this,
					serviceKey, serviceLight);
		} else if (modual.equals(Constants.MODUAL_MY2)
				|| modual.equals(Constants.MODUAL_MY2S)) {
			serviceLight = new MY2ServiceLight(this);
			handleKey = MY2HandleKey.getInstance(RemoteService.this,
					serviceKey, serviceLight);
		}
		serialNum = getSerialNum();
		PendingIntent pintent = PendingIntent.getActivity(this, 0, new Intent(
				this, MyMainActivity.class), 0);
		Notification.Builder build = new Notification.Builder(this);
		build.setContentTitle("wireless remote");
		build.setSmallIcon(R.drawable.ic_launcher);
		build.setWhen(System.currentTimeMillis());
		build.setContentIntent(pintent);
		Notification notification = build.build();

		// activityManager = (ActivityManager) this
		// .getSystemService(Context.ACTIVITY_SERVICE);

		/**
		 * Make this service run in the foreground, supplying the ongoing
		 * notification to be shown to the user while in this state
		 * 把当前service设为前台服务，当退出activity时，service依然在运行状态
		 */
		startForeground(0x2016, notification);

		super.onCreate();
	}

	private String getSerialNum() {
		String serialNum = "";
		try {
			serialNum = TvManager.getInstance().getEnvironment("mypid");
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serialNum;
	}

	private String getModual() {
		return SystemProperties.get("ro.product.Module");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		startrunflag = true;
		ClientThread clientThread = new ClientThread();
		new Thread(clientThread).start();
		return super.onStartCommand(intent, flags, startId);
	}

	class ClientThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			byte[] dataBuffer = new byte[recivebuffersize];
			// 把socket对象获取到的数据保存到缓存区中
			DatagramPacket inPacket = new DatagramPacket(dataBuffer,
					dataBuffer.length);

			DatagramSocket serviceSocket = null;
			if (serviceSocket == null) {
				try {
					// 创建数据包socket对象
					serviceSocket = new DatagramSocket(Constants.SERVER_PORT);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (serviceSocket != null) {
				while (startrunflag) {

					try {
						// 从socket对象中获取客户端发送过来的数据包，保存到DatagramPacket对象中
						serviceSocket.receive(inPacket);
						// 得到数据包中的数据
						byte[] byteData = inPacket.getData();
						// 把字节码数组转换成对应的16进制字符串
						String hex = Utils.printHexString(byteData);
						// 把16进制的字符串转化成标准字符串，比如0x636c69656e747269676874转化成成clientright
						String receivedFromClient = Utils.HextoString(hex);
						// 把获取到的字符串传到ServiceKey对象中
						Log.d("remote", "service---serialNum = " + serialNum);
						Log.d("remote", "service---receive_key = "
								+ receivedFromClient);
						// 从服务器返回给客户端数据
						InetAddress clientAddress = inPacket.getAddress(); // 获得客户端的IP地址
						Log.d("remote", "service---clientAddress = "
								+ clientAddress);
						int clientPort = inPacket.getPort(); // 获得客户端的端口号
						Log.d("remote", "service---clientPort = " + clientPort);
						if (receivedFromClient
								.contains(Constants.SEARCH_DEVICE)) {
							rePly(clientAddress, clientPort,
									Utils.getHexByte(serialNum));
						} else if (receivedFromClient.contains(serialNum)) {
							String modual = getModual();
							if (modual.equals(Constants.MODUAL_MM1)) {
								handleKey = MM1HandleKey.getInstance(
										RemoteService.this, serviceKey,
										serviceLight);
							} else if (modual.equals(Constants.MODUAL_MY2)
									|| modual.equals(Constants.MODUAL_MY2S)) {
								handleKey = MY2HandleKey.getInstance(
										RemoteService.this, serviceKey,
										serviceLight);
							}
							handleKey.execute(clientAddress,
									clientPort, receivedFromClient);
						}

					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		startrunflag = false;
		super.onDestroy();
	}

	public void rePly(InetAddress address, int port, byte[] msg) {
		// TODO Auto-generated method stub

		DatagramSocket dSocket = null;

		try {
			dSocket = new DatagramSocket();
			int msg_len = msg == null ? 0 : msg.length;
			DatagramPacket dPacket = new DatagramPacket(msg, 0, msg_len, address, port);
			dSocket.send(dPacket);
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			dSocket.close();
		}
	}
}
