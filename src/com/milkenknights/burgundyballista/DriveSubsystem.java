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
	JStick joystick;
	Drive drive;
	
	public DriveSubsystem(JStick xbox, int leftWheel, int rightWheel) {
		joystick = xbox;
		drive = new Drive(leftWheel, rightWheel);
	}
	
	public void update() {
		double leftAxisY = joystick.getAxis(JStick.XBOX_LSY);
		double rightAxisX = joystick.getAxis(JStick.XBOX_RSX);
		boolean trigDown = Math.abs(joystick.getAxis(JStick.XBOX_TRIG)) > 0.5;
		
		double drivePower = leftAxisY;
		double driveTurn = rightAxisX;
		
        drive.cheesyDrive(drivePower, driveTurn, trigDown);
		
	}
}
