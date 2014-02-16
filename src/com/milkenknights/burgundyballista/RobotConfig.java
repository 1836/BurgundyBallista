package com.milkenknights.burgundyballista;

import java.util.Hashtable;

public class RobotConfig {
	private static final Hashtable defaultConfig;
	
	static {
		defaultConfig = new Hashtable();
		defaultConfig.put("tLeftWheel","4");
		defaultConfig.put("tRightWheel","9");
		defaultConfig.put("compressorPressureSwitch","7");
		defaultConfig.put("compressorRelayChannel","1");
		defaultConfig.put("sDriveGearA","1");
		defaultConfig.put("sDriveGearB","2");
		defaultConfig.put("sCasterA","7");
		defaultConfig.put("sCasterB","8");
		defaultConfig.put("leftEncA","11");
		defaultConfig.put("leftEncB","12");
		defaultConfig.put("rightEncA","13");
		defaultConfig.put("rightEncB","14");
		defaultConfig.put("leftEncPulse","0.102");
		defaultConfig.put("rightEncPulse","0.287");
		defaultConfig.put("lightRelay","2");
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
