/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.milkenknights.burgundyballista;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Enumeration;
import java.util.Vector;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Knight extends IterativeRobot {
	ConfigFile config;
	
	Vector subsystems;
	
	Compressor compressor;
	DriveSubsystem driveSubsystem;
	CasterSubsystem casterSubsystem;
	ShooterSubsystem shooterSubsystem;
	IntakeSubsystem intakeSubsystem;
	FourBarSubsystem fourBarSubsystem;
	
	Vision vision;
	
	boolean startSideLeft = true;
	// this should be equal to 1,2,3,4
	int autonMode = 1;
	
	double startTime;
	boolean shootFirst;
	boolean autonomousBallShot;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		config = new ConfigFile("robot-config.txt");
		config.loadFile();
		
		compressor = new Compressor(config.getAsInt("compressorPressureSwitch"),
				config.getAsInt("compressorRelayChannel"));
		driveSubsystem = new DriveSubsystem(config);
		shooterSubsystem = new ShooterSubsystem(config);
		fourBarSubsystem = new FourBarSubsystem(config);
		intakeSubsystem = new IntakeSubsystem(config, fourBarSubsystem);
		casterSubsystem = new CasterSubsystem(config);
		
		//vision = new Vision();
		
		subsystems = new Vector(10);
		
		subsystems.addElement(driveSubsystem);
		subsystems.addElement(casterSubsystem);
		subsystems.addElement(shooterSubsystem);
		subsystems.addElement(intakeSubsystem);
		subsystems.addElement(fourBarSubsystem);
		
		// since no more subsystems will be added, we can free the remaining
		// memory
		subsystems.trimToSize();
                		
		compressor.start();
    }

	boolean alreadyShot;
	double moveForwardTime;
	
    /**
     * This function is called periodically during autonomous
     */
	public void autonomousInit() {
		/*
		startSideLeft
				= SmartDashboard.getBoolean("Starting on left side?", false);
		
		moveForwardTime = SmartDashboard.getNumber("moveForwardTime", 1);

		startTime = Timer.getFPGATimestamp();
		*/
		/*
		int hotSide = vision.isHot();
		System.out.println("HOTSIDE RETURNED "+hotSide);

		if (hotSide == -1 || hotSide == 0) {
			// stuff to do if camera malfunctions
		} else if ((hotSide == 1) == startSideLeft) {
			// if we are on the side of the hot goal, start the autonomous
			// procedure immediately
		} else {
			// if we are on the opposite side of the hot goal, wait 5
			// seconds before starting the autonomous procedure
			startTime += 5;
		}
		*/
		/*
		autonMode = (int) SmartDashboard.getNumber("autonMode",4);
		
		if (autonMode == 1) {
			// Auton mode 1: simply move 10 feet forward
			driveSubsystem.setStraightPIDSetpoint(10 * 12);
			driveSubsystem.setDriveMode(DriveSubsystem.PIDSTRAIGHT);
		} else if (autonMode == 2) {
			// Auton mode 2: Two ball auton
			alreadyShot = false;
			// two ball auton
			intakeSubsystem.setIntakePosition(IntakeSubsystem.INTAKE_DOWN);
			fourBarSubsystem.setPosition(FourBarSubsystem.SHOOT);
			shooterSubsystem.pullBack();
		} else if (autonMode == 3) {
			// one ball auton high without encoders
			shooterSubsystem.pullBack();
		} else if (autonMode == 4) {
			// Auton mode 4: One ball auton without encoders
			driveSubsystem.setDriveMode(DriveSubsystem.FULLSPEED);
		} else if (autonMode ==5) {
			// just go forward for 2 seconds
			driveSubsystem.setDriveMode(DriveSubsystem.FULLSPEED);
		}
		*/
		
		driveSubsystem.setDriveMode(DriveSubsystem.REESHVISION);
	}
	
	public void autonomousPeriodic() {
		/*
		double currentTime = Timer.getFPGATimestamp() - startTime;

		
		if (autonMode == 1) {
			// emergency auton-- move 10 feet forward
		} else if (autonMode == 2) {
			if (currentTime > 0) {				
				if (shooterSubsystem.getState() == ShooterSubsystem.WINCH_PULLED
						&& !alreadyShot) {
					shooterSubsystem.shoot();
					shooterSubsystem.pullBack();
					
				} else if (shooterSubsystem.getState() ==
						ShooterSubsystem.WINCH_INITIAL || alreadyShot) {
					shooterSubsystem.pullBack();
					alreadyShot = true;
					driveSubsystem.setStraightPIDSetpoint(-3);
					driveSubsystem.setDriveMode(DriveSubsystem.PIDSTRAIGHT);
					
					intakeSubsystem.setWheelsState(IntakeSubsystem.WHEELS_INTAKE);
					fourBarSubsystem.setPosition(FourBarSubsystem.LOAD);
					
					if (driveSubsystem.pidOnTarget(.6)) {
						fourBarSubsystem.setPosition(FourBarSubsystem.SHOOT);
						if (shooterSubsystem.getState() ==
								ShooterSubsystem.WINCH_PULLED) {
							shooterSubsystem.shoot();
						} else if (shooterSubsystem.getState() ==
								ShooterSubsystem.WINCH_INITIAL) {
							driveSubsystem.setStraightPIDSetpoint(10 * 12);
						}
					}
				}
			}
			
		} else if (autonMode == 3) {
			// one ball auton high without encoders
			if (currentTime > moveForwardTime) {
				driveSubsystem.setDriveMode(DriveSubsystem.NONE);
				if (shooterSubsystem.getState() ==
						ShooterSubsystem.WINCH_PULLED) {
					shooterSubsystem.shoot();
				} else if (shooterSubsystem.getState() ==
						ShooterSubsystem.WINCH_INITIAL) {
				}
			} else if (currentTime > 0) {
				driveSubsystem.setDriveMode(DriveSubsystem.FULLSPEED);
			}
			
		} else if (autonMode == 4) {
			// one ball auton low without encoders
			if (currentTime > 3) {
				intakeSubsystem.setWheelsState(IntakeSubsystem.WHEELS_STOPPED);
			} else if (currentTime > 1) {
				driveSubsystem.setDriveMode(DriveSubsystem.NONE);
				intakeSubsystem.setWheelsState(IntakeSubsystem.WHEELS_OUTTAKE);
				intakeSubsystem.setIntakePosition(IntakeSubsystem.INTAKE_DOWN);
			}
		} else if (autonMode == 5) {
			if (currentTime > 2) {
				driveSubsystem.setDriveMode(DriveSubsystem.NONE);
			}
		}
		*/
		
		for (Enumeration e = subsystems.elements(); e.hasMoreElements();) {
			((Subsystem) e.nextElement()).update();
		}
    }

	public void teleopInit() {
		for (Enumeration e = subsystems.elements(); e.hasMoreElements();) {
			((Subsystem) e.nextElement()).teleopInit();
		}
	}
	
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
		JStickMultiton.updateAll();

		int i = 1;
		for (Enumeration e = subsystems.elements(); e.hasMoreElements();) {
			Subsystem s = (Subsystem) e.nextElement();
			//System.out.println("Subsystem "+i+" running");
			double fullTime = 0;
			double startTime = Timer.getFPGATimestamp();
			
			s.teleopPeriodic();
			
			//System.out.println("teleop: "+(Timer.getFPGATimestamp() - startTime));
			fullTime = Timer.getFPGATimestamp() - startTime;
			
			s.update();
			
			//System.out.println("update: "+(Timer.getFPGATimestamp() - (startTime + fullTime)));
			fullTime += Timer.getFPGATimestamp() - (startTime + fullTime);
			//System.out.println("total "+i+": "+fullTime);
		}
		
                
        // Feed the Watchdog. Makes the motors not fail every 100ms
        Watchdog.getInstance().feed();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
		shooterSubsystem.test();
    }
    
}
