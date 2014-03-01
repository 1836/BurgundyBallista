/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	PIDSystem gyroPID;
	
	Encoder leftDriveEncoder;
	Encoder rightDriveEncoder;
	
	Gyro gyro;
	
	boolean normalDriveGear;
	boolean slowMode;
	boolean runPID;
	boolean runGyro;
	
	
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
		gyroPID = new PIDSystem(config.getAsDouble("gyroAngle"),
				config.getAsDouble("gyrokp"),
				config.getAsDouble("gyroki"),
				config.getAsDouble("gyrokd"));
		
		leftDriveEncoder = new Encoder(config.getAsInt("leftEncA"),
				config.getAsInt("leftEncB"),
				true, CounterBase.EncodingType.k4X);
		rightDriveEncoder = new Encoder(config.getAsInt("rightEncA"),
				config.getAsInt("rightEncB"),
				true, CounterBase.EncodingType.k4X);
		
		gyro = new Gyro(config.getAsInt("gyro"));
		
		gyro.reset();
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
		
		SmartDashboard.putBoolean("Drive gear high:", driveGear.get());
	}
	
	public void autonomousInit() {
		gyro.reset();
	}
	
	public void autonomousPeriodic(boolean rungyro) {
		if (rungyro) {
			runGyro = true;
		}
		else {
			runPID = true;
		}
		
		if (runGyro) {
			drive.tankDrive(gyroPID.update(gyro.getAngle()), 
					-gyroPID.update(gyro.getAngle()));
			if (gyroPID.update(gyro.getAngle()) == 0) {
				runGyro = false;
			}
		}
		else if (runPID) {
			drive.tankDrive(leftPID.update(leftDriveEncoder.getDistance()), 
					rightPID.update(rightDriveEncoder.getDistance()));
			if (leftPID.update(leftDriveEncoder.getDistance()) == 0 &&
					rightPID.update(rightDriveEncoder.getDistance()) == 0) {
				runPID = false;
			}
		}
		
	}
	
}
