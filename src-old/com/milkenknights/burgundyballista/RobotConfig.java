package com.milkenknights.burgundyballista;

import java.util.Hashtable;

public class RobotConfig {
	private static final Hashtable defaultConfig;
	
	static {
		defaultConfig = new Hashtable();
		defaultConfig.put("tLeftWheel","10");
		defaultConfig.put("tRightWheel","9");
		defaultConfig.put("compressorPressureSwitch","5");
		defaultConfig.put("compressorRelayChannel","2");
		defaultConfig.put("sDriveGearHigh","2");
		defaultConfig.put("sDriveGearLow","1");
		defaultConfig.put("sCaster","5");
		defaultConfig.put("leftEncA","11");
		defaultConfig.put("leftEncB","12");
		defaultConfig.put("rightEncA","13");
		defaultConfig.put("rightEncB","14");
		defaultConfig.put("fourBarEncA","6");
		defaultConfig.put("fourBarEncB","7");
		defaultConfig.put("winchEncA","9");
		defaultConfig.put("winchEncB","10");
		defaultConfig.put("leftEncPulse","0.102");
		defaultConfig.put("rightEncPulse","0.287");
		defaultConfig.put("lightRelay","1");
		defaultConfig.put("tWinch","8");
		defaultConfig.put("tIntake","6");
		defaultConfig.put("tFourBar","5");
		defaultConfig.put("sGrabber","3");
		defaultConfig.put("sWinch","4");
		defaultConfig.put("sIntakeA","6");
		defaultConfig.put("sIntakeB","7");
	}

	final public String get(Object k) {
		String v = customGet(k);
		
		return (v != null ? v : defaultConfig.get(k)).toString();
	}
	
	public double getAsDouble(Object k) {
		return Double.parseDouble(get(k));
	}

	public int getAsInt(Object k) {
		return Integer.parseInt(get(k));
	}
	
	// This method should be overridden by an extending class
	public String customGet(Object k) {
		return null;
	}
}
