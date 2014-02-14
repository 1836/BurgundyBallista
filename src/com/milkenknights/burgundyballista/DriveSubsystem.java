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
public class DriveSubsystem {
	JStick xbox;
	Drive drive;
	SolenoidPair driveGear;
	boolean normalDriveGear;
	boolean slowMode;
	
	public DriveSubsystem(int leftWheel, int rightWheel) {
		xbox = JStickMultiton.getJStick(1);
		drive = new Drive(leftWheel, rightWheel);
		driveGear = new SolenoidPair(1, 2, true, false, true);
	}
	
	public void update() {
		if (xbox.isReleased(JStick.XBOX_LB)) {
			driveGear.toggle();
			normalDriveGear = driveGear.get();
		}
		
		if (xbox.isReleased(JStick.XBOX_Y)) {
			slowMode =! slowMode;
			
			if (slowMode) {
				driveGear.set(false);
			}
			else {
				driveGear.set(normalDriveGear);
			}
		}
		
		double power = xbox.getAxis(JStick.XBOX_LSY);
		double turn = xbox.getAxis(JStick.XBOX_RSX);
		boolean trigDown = Math.abs(xbox.getAxis(JStick.XBOX_TRIG)) > 0.5;

		if (slowMode) {
			power = power * .5;
		}
		
        drive.cheesyDrive(power, turn, trigDown);
	}
}
