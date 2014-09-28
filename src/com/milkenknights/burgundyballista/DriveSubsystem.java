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
	//JStick xbox;
	JStick atkl;
	JStick atkr;
	
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
	boolean reverseMode;
	boolean runPID;
	boolean runGyro;
	
	double tankLeftSpeed;
	double tankRightSpeed;
	
	ByteServer byteServer;
	
	int driveMode = 0;
	public static final int NONE = 0;
	public static final int CHEESY = 1;
	public static final int TANK = 2;
	public static final int PIDSTRAIGHT = 3;
	public static final int FULLSPEED = 4;
	public static final int REESHVISION = 5;
	
	public DriveSubsystem(RobotConfig config) {
		//xbox = JStickMultiton.getJStick(1);
		atkl = JStickMultiton.getJStick(1);
		atkr = JStickMultiton.getJStick(2);

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
		
		/*
		leftDriveEncoder = new Encoder(config.getAsInt("leftEncA"),
				config.getAsInt("leftEncB"),
				true, CounterBase.EncodingType.k4X);
		rightDriveEncoder = new Encoder(config.getAsInt("rightEncA"),
				config.getAsInt("rightEncB"),
				true, CounterBase.EncodingType.k4X);
		*/
		
		gyro = new Gyro(config.getAsInt("gyro"));
		
		gyro.reset();
		
		// reeshvision
		byteServer = new ByteServer(1180);
	}
	
	public void teleopInit() {
		setDriveMode(TANK);
		reverseMode = false;
	}
	
	public void teleopPeriodic() {
		//if (xbox.isReleased(JStick.XBOX_LB)) {
		if (atkr.isReleased(1)) {
			driveGear.toggle();
			normalDriveGear = driveGear.get();
		}
		
		/*
		if (xbox.isReleased(JStick.XBOX_Y)) {
			slowMode =! slowMode;
			
			if (slowMode) {
				driveGear.set(false);
			} else {
				driveGear.set(normalDriveGear);
			}
		}
		
		if (xbox.isReleased(JStick.XBOX_X)) {
			reverseMode = !reverseMode;
		}
		*/
		
		// REVERSED?? CHANGE IT HERE BY PLACING NEGATIVES
		setTankSpeed(atkl.getAxis(2), atkr.getAxis(2));
		
		if (atkl.getAxis(2) != atkl.getSlowedAxis(2)) {
			SmartDashboard.putNumber("L slowed", 0);
		} else {
			SmartDashboard.putNumber("L slowed", 1);
		}
		
		SmartDashboard.putBoolean("Drive gear high:", driveGear.get());
	}
	
	public void setLeftSpeed(double speed) {
		tankLeftSpeed = speed;
	}

	public void setRightSpeed(double speed) {
		tankRightSpeed = speed;
	}

	public void setTankSpeed(double left, double right) {
		tankLeftSpeed = left;
		tankRightSpeed = right;
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
		SmartDashboard.putNumber("drivemode", driveMode);
		if (driveMode == CHEESY) {
			/*
			double power = xbox.getAxis(JStick.XBOX_LSY);
			double turn = xbox.getAxis(JStick.XBOX_RSX);
			boolean trigDown
					= Math.abs(xbox.getAxis(JStick.XBOX_TRIG)) > 0.5;
			
			if (reverseMode) {
				power = -power;
				turn = -turn;
			}
			
			if (slowMode) {
				power = power * .5;
			}
			
			SmartDashboard.putNumber("power", power);
			SmartDashboard.putNumber("turn", turn);
			SmartDashboard.putBoolean("td",trigDown);

			drive.cheesyDrive(power, -turn, trigDown);
			*/
		} else if (driveMode == TANK) {
			drive.tankDrive(tankLeftSpeed, tankRightSpeed);
		} else if (driveMode == PIDSTRAIGHT) {
			drive.tankDrive(leftPID.update(leftDriveEncoder.getDistance()),
					rightPID.update(rightDriveEncoder.getDistance()));
			
		} else if (driveMode == FULLSPEED) {
			drive.tankDrive(1,1);
		} else if (driveMode == REESHVISION) {
			int b = byteServer.getCurrentByte();
			int lbyte = b >> 4;
			int rbyte = b & 15;
			double reesh_l, reesh_r;
			if (lbyte > 7) {
				reesh_l = ((double)(8 - lbyte)) / 7.0;
			} else {
				reesh_l = ((double)lbyte)/7.0;
			}
			
			if (rbyte > 7) {
				reesh_r = ((double)(8 - rbyte)) / 7.0;
			} else {
				reesh_r = ((double)rbyte)/7.0;
			}
			System.out.println(""+reesh_l+", "+reesh_r+", "+lbyte+", "+rbyte);
			drive.tankDrive(reesh_l, reesh_l);
		} else {
			drive.tankDrive(0,0);
		}
		
		SmartDashboard.putNumber("left", drive.getLeft());
		SmartDashboard.putNumber("right", drive.getRight());
	}	
}