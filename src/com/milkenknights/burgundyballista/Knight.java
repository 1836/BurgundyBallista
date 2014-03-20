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
		
		compressor = new Compressor(5, 2);
		driveSubsystem = new DriveSubsystem(config);
		shooterSubsystem = new ShooterSubsystem(config);
		fourBarSubsystem = new FourBarSubsystem(config);
		intakeSubsystem = new IntakeSubsystem(config, fourBarSubsystem);
		casterSubsystem = new CasterSubsystem(config);
		
		vision = new Vision();
		
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
	
    /**
     * This function is called periodically during autonomous
     */
	public void autonomousInit() {
		startSideLeft
				= SmartDashboard.getBoolean("Starting on left side?", false);

		startTime = Timer.getFPGATimestamp();
		
		int hotSide = vision.isHot();
		System.out.println("HOTSIDE RETURNED "+hotSide);

		if (hotSide == -1) {
			// stuff to do if camera malfunctions
		} else if ((hotSide == 1) == startSideLeft) {
			// if we are on the side of the hot goal, start the autonomous
			// procedure immediately
		} else {
			// if we are on the opposite side of the hot goal, wait 5
			// seconds before starting the autonomous procedure
			startTime -= 5;
		}
		
		autonMode = (int) SmartDashboard.getNumber("autonMode",1);
		
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
			shooterSubsystem.pullBack();
		}
	}
	
	public void autonomousPeriodic() {
		double currentTime = Timer.getFPGATimestamp() - startTime;

		// emergency auton-- move 10 feet forward
		if (autonMode == 1) {
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
			// one ball auton high
			if (currentTime > 0) {
				if (shooterSubsystem.getState() ==
						ShooterSubsystem.WINCH_PULLED) {
					shooterSubsystem.shoot();
				} else if (shooterSubsystem.getState() ==
						ShooterSubsystem.WINCH_INITIAL) {
					// this means the shooter has finished shooting, so we
					// should start moving forward
					driveSubsystem.setStraightPIDSetpoint(10 * 12);
					driveSubsystem.setDriveMode(DriveSubsystem.PIDSTRAIGHT);
				}
			}
		}
		
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

		for (Enumeration e = subsystems.elements(); e.hasMoreElements();) {
			((Subsystem) e.nextElement()).teleopPeriodic();
			((Subsystem) e.nextElement()).update();
		}
		
                
        // Feed the Watchdog. Makes the motors not fail every 100ms
        Watchdog.getInstance().feed();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
