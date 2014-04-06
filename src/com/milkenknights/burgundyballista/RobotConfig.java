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
		defaultConfig.put("sCaster","9");
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
		defaultConfig.put("sWinchA","4");
		defaultConfig.put("sWinchB","5");
		defaultConfig.put("sIntakeA","3");
		//defaultConfig.put("sIntakeB","7");
		defaultConfig.put("winchPullBack", "1");
		defaultConfig.put("shooterPIDkp", ".1");
		defaultConfig.put("shooterPIDki", "0");
		defaultConfig.put("shooterPIDkd", "0");
		defaultConfig.put("driveDistance", "1");
		defaultConfig.put("drivePIDkp", ".1");
		defaultConfig.put("drivePIDki", "0.01");
		defaultConfig.put("drivePIDkd", "0.001");
		defaultConfig.put("gyroAngle", "1");
		defaultConfig.put("gyrokp", ".1");
		defaultConfig.put("gyroki", "0.01");
		defaultConfig.put("gyrokd", "0.001");
		defaultConfig.put("gyro", "1");
		defaultConfig.put("fourBarDistanceA", "90");
		defaultConfig.put("fourBarDistanceB", "200");
		defaultConfig.put("fourBarPIDkpUp", "0.0125");
		defaultConfig.put("fourBarPIDkiUp", "0.0");
		defaultConfig.put("fourBarPIDkdUp", "0.002");
		defaultConfig.put("fourBarPIDkpDown", "0.009");
		defaultConfig.put("fourBarPIDkiDown", "0.0");
		defaultConfig.put("fourBarPIDkdDown", "0.0001");
		defaultConfig.put("fourBarDistanceDown", "30");
		defaultConfig.put("lsShooter", "2");
	}

	final public String get(Object k) {
		Object v = customGet(k);
		
		return (v != null ? v : defaultConfig.get(k)).toString();
	}
	
	public double getAsDouble(Object k) {
		return Double.parseDouble(get(k));
	}

	public int getAsInt(Object k) {
		return Integer.parseInt(get(k));
	}
	
	// This method should be overridden by an extending class
	protected Object customGet(Object k) {
		return null;
	}
}
