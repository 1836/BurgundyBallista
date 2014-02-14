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
	
	public DriveSubsystem(int leftWheel, int rightWheel) {
		xbox = JStickMultiton.getJStick(1);
		drive = new Drive(leftWheel, rightWheel);
	}
	
	public void update() {
		double leftAxisY = xbox.getAxis(JStick.XBOX_LSY);
		double rightAxisX = xbox.getAxis(JStick.XBOX_RSX);
		boolean trigDown = Math.abs(xbox.getAxis(JStick.XBOX_TRIG)) > 0.5;
		
		double drivePower = leftAxisY;
		double driveTurn = rightAxisX;
		
        drive.cheesyDrive(drivePower, driveTurn, trigDown);
		
	}
}
