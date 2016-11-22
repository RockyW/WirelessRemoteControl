package zxing.toptech.utils;

public class HandleKeyUtil {
	public static final String PREFIX = "client";
	private String keyMessage;
	private SendDataPacket mSendDataPacket = null;
	boolean isSend = true;

	public HandleKeyUtil() {
		mSendDataPacket = new SendDataPacket();
	}

	public boolean sendKey(String keyName) {
		String temp = null;
		if (keyName != null && !(keyName.equals(""))) {
			temp = keyName;
		}
		this.keyMessage = temp;
		if (keyMessage != null) {
			new Thread() {

				public void run() {
					synchronized (this) {
						isSend = mSendDataPacket.send(HexToReverse
								.getHexByte(keyMessage));
					}
				}
			}.start();
		}
		return isSend;
	}
}
