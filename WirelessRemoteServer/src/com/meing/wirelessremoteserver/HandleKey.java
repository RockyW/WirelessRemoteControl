package com.meing.wirelessremoteserver;

import java.net.DatagramSocket;
import java.net.InetAddress;

public interface HandleKey {
	public void execute(InetAddress clientAddress, int port, String receive);
}
