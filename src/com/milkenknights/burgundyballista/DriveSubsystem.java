/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.milkenknights.burgundyballista;

/**
 *
 * @author Jake
 */
public class DriveSubsystem extends Subsystem {
	JStick xbox;
	Drive drive;
	SolenoidPair driveGear;
	boolean normalDriveGear;
	boolean slowMode;
	public DriveSubsystem(RobotConfig config) {
		xbox = JStickMultiton.getJStick(1);
		drive = new Drive(config.getAsInt("tLeftWheel"),
				config.getAsInt("tRightWheel"));
		// this solenoid pair is TRUE if the robot is in high gear
		driveGear = new SolenoidPair(config.getAsInt("sDriveGearHigh"),
				config.getAsInt("sDriveGearLow"), true, false, true);
	}
	
	public void teleopPeriodic() {
		if (xbox.isReleased(JStick.XBOX_LB)) {
			driveGear.toggle();
			normalDriveGear = driveGear.get();
		}
		
		if (xbox.isReleased(JStick.XBOX_Y)) {
			slowMode =! slowMode;
			
			if (slowMode) {
				driveGear.set(false);
			} else {
				driveGear.set(normalDriveGear);
			}
		}
		
		xbox.update();
		
		double power = xbox.getAxis(JStick.XBOX_LSY);
		double turn = xbox.getAxis(JStick.XBOX_RSX);
		boolean trigDown = Math.abs(xbox.getAxis(JStick.XBOX_TRIG)) > 0.5;

		if (slowMode) {
			power = power * .5;
		}
		
        drive.cheesyDrive(power, turn, trigDown);
	}
}
