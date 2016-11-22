package com.meing.wirelessremoteserver;

public class LightMsg {

	private String lightState;
	private int lightModel;
	private int lightPower;

	public LightMsg() {
		// TODO Auto-generated constructor stub
	}

	public String getLightState() {
		return lightState;
	}

	public void setLightState(String lightState) {
		this.lightState = lightState;
	}

	public int getLightModel() {
		return lightModel;
	}

	public void setLightModel(int lightModel) {
		this.lightModel = lightModel;
	}

	public int getLightPower() {
		return lightPower;
	}

	public void setLightPower(int lightPower) {
		this.lightPower = lightPower;
	}

	public void setMsg(int[] data) {
		if (data != null) {
			int state = data[0];
			if (state == 1) {
				this.lightState = "on";
			} else {
				this.lightState = "off";
			}
			this.lightModel = data[1];
			this.lightPower = toPower(data[2], data[3]);
		}
	}

	// get power by data[2] and data[3]
	private int toPower(int i, int j) {
		return i + j;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.lightState + "," + this.lightModel + "," + this.lightPower;
	}

}
