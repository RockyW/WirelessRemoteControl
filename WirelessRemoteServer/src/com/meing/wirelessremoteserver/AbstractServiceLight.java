package com.meing.wirelessremoteserver;

import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class AbstractServiceLight {

	public abstract void execute(String cmd);

	public void prepare(InetAddress inetAddress, int port) {

	}

}
