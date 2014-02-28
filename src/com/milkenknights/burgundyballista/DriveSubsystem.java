/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Jake
 */
public class DriveSubsystem extends Subsystem {
	JStick xbox;
	Drive drive;
	SolenoidPair driveGear;
	
	PIDSystem leftPID;
	PIDSystem rightPID;
	
	Encoder leftDriveEncoder;
	Encoder rightDriveEncoder;
	
	boolean normalDriveGear;
	boolean slowMode;
	
	
	public DriveSubsystem(RobotConfig config) {
		xbox = JStickMultiton.getJStick(1);
		drive = new Drive(new Talon(config.getAsInt("tLeftWheel")),
				new Talon(config.getAsInt("tRightWheel")));
		// this solenoid pair is TRUE if the robot is in high gear
		driveGear = new SolenoidPair(config.getAsInt("sDriveGearHigh"),
				config.getAsInt("sDriveGearLow"), true, false, true);
		
		leftPID = new PIDSystem(config.getAsDouble("driveDistance"),
				config.getAsDouble("drivePIDkp"),
				config.getAsDouble("drivePIDki"),
				config.getAsDouble("drivePIDkd"));
		rightPID = new PIDSystem(config.getAsDouble("driveDistance"),
				config.getAsDouble("drivePIDkp"),
				config.getAsDouble("drivePIDki"),
				config.getAsDouble("drivePIDkd"));
		
		leftDriveEncoder = new Encoder(config.getAsInt("leftEncA"),
				config.getAsInt("leftEncB"),
				true, CounterBase.EncodingType.k4X);
		rightDriveEncoder = new Encoder(config.getAsInt("rightEncA"),
				config.getAsInt("rightEncB"),
				true, CounterBase.EncodingType.k4X);
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
		
		double power = xbox.getAxis(JStick.XBOX_LSY);
		double turn = xbox.getAxis(JStick.XBOX_RSX);
		boolean trigDown = Math.abs(xbox.getAxis(JStick.XBOX_TRIG)) > 0.5;

		if (slowMode) {
			power = power * .5;
		}
		
        drive.cheesyDrive(power, turn, trigDown);
	}
	
	public void autonomousPeriodic() {
			drive.tankDrive(leftPID.update(leftDriveEncoder.getDistance()),
			rightPID.update(rightDriveEncoder.getDistance()));
	}
	
}
