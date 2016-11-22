package zxing.toptech.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

public class SendDataPacket {
	// ����Ĳ���Ϊ�ֽ����飬���汣�����16���ƣ���16�������
	public boolean send(byte[] msg) {

		DatagramSocket dSocket = null;

		InetAddress local = null;
		try {

			local = InetAddress.getByName(Constants.HOST_ADDR); // ��������
			// local = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {

			e.printStackTrace();

		}

		try {

			dSocket = new DatagramSocket();

		} catch (SocketException e) {

			e.printStackTrace();

		}

		int msg_len = msg == null ? 0 : msg.length;
		Log.i("remote", "------------------>"+local);
		DatagramPacket dPacket = new DatagramPacket(msg, msg_len, local,
				Constants.SERVER_PORT);

		try {

			dSocket.send(dPacket);

		} catch (IOException e) {
			return false;
		} finally {
			dSocket.close();
		}

		return true;
	}
}
