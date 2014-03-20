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
 * The subsystem that manages the robot's wheels and gear solenoids.
 * 
 * @author Daniel
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
	
	int driveMode = 0;
	public static final int NONE = 0;
	public static final int CHEESY = 1;
	public static final int TANK = 2;
	public static final int PIDSTRAIGHT = 3;
	public static final int FULLSPEED = 4;
	
	public DriveSubsystem(RobotConfig config) {
		xbox = JStickMultiton.getJStick(1);
		drive = new Drive(new Talon(10),
				new Talon(9));
		// this solenoid pair is TRUE if the robot is in high gear
		driveGear = new SolenoidPair(2,
				1, true, false, true);
		
		leftPID = new PIDSystem(config.getAsDouble("driveDistance"),
				config.getAsDouble("drivePIDkp"),
				config.getAsDouble("drivePIDki"),
				config.getAsDouble("drivePIDkd"), .001);
		rightPID = new PIDSystem(config.getAsDouble("driveDistance"),
				config.getAsDouble("drivePIDkp"),
				config.getAsDouble("drivePIDki"),
				config.getAsDouble("drivePIDkd"), .001);
		gyroPID = new PIDSystem(config.getAsDouble("gyroAngle"),
				config.getAsDouble("gyrokp"),
				config.getAsDouble("gyroki"),
				config.getAsDouble("gyrokd"), .001);
		
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
	
	public void setStraightPIDSetpoint(double setpoint) {
		leftPID.changeSetpoint(setpoint);
		rightPID.changeSetpoint(setpoint);
	}
	
	public void setDriveMode(int mode) {
		driveMode = mode;
	}
	
	public boolean pidOnTarget(double threshold) {
		return leftPID.onTarget(threshold) && rightPID.onTarget(threshold);
	}
	
	/**
	 * Updates wheels depending on driveMode (which should be set to the
	 * desired mode with setDriveMode().
	 * This method should be called during every loop no matter what.
	 */
	public void update() {
		if (driveMode == CHEESY) {
			double power = xbox.getAxis(JStick.XBOX_LSY);
			double turn = xbox.getAxis(JStick.XBOX_RSX);
			boolean trigDown
					= Math.abs(xbox.getAxis(JStick.XBOX_TRIG)) > 0.5;

			if (slowMode) {
				power = power * .5;
			}

			drive.cheesyDrive(power, -turn, trigDown);
			
		} else if (driveMode == TANK) {
			double left = xbox.getAxis(JStick.XBOX_LSY);
			double right = xbox.getAxis(JStick.XBOX_RSY);
			
			if (slowMode) {
				left = left * .5;
				right = right * .5;
			}
			
			drive.tankDrive(left, right);
			
		} else if (driveMode == PIDSTRAIGHT) {
			drive.tankDrive(leftPID.update(leftDriveEncoder.getDistance()),
					rightPID.update(rightDriveEncoder.getDistance()));
			
		} else if (driveMode == FULLSPEED) {
			drive.tankDrive(1,1);
			
		} else {
			drive.tankDrive(0,0);
		}
	}	
}
