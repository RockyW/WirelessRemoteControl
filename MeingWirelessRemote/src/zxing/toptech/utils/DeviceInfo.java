package zxing.toptech.utils;

public class DeviceInfo {

	private String devicename;
	private String deviceid;

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	@Override
	public String toString() {
		return "deviceID:" + deviceid + "  deviceName:" + devicename;
	}
}
