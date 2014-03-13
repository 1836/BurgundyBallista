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
	
	//Vision vision;
	
	boolean startSideLeft = true;
	
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
                
		fourBarSubsystem.robotInit();
		
		compressor.start();
		
		SmartDashboard.getBoolean("Starting on left side?", startSideLeft);
    }

    /**
     * This function is called periodically during autonomous
     */
	public void autonomousInit() {
		startTime = Timer.getFPGATimestamp();
		
		fourBarSubsystem.autonomousInit();
		driveSubsystem.autonomousInit();
		intakeSubsystem.autonomousInit();
		/*
		shooterSubsystem.autonomousInit();

		int startSide;
		int hotSide;
		
		hotSide = 1; //vision.isHot();
		if (startSideLeft) {
			startSide = 1;
		}
		else {
			startSide = 2;
		}
		if (hotSide == 0 || hotSide == -1) {
			//Error or can't detect hot side
		}
		if (hotSide == startSide) {
			shootFirst = true;
		}
		else {
			shootFirst = false;
		}*/
		
	}
	
	boolean alreadyShot;
	public void autonomousPeriodic() {
		double currentTime = Timer.getFPGATimestamp() - startTime;

		fourBarSubsystem.autonomousPeriodic();
		driveSubsystem.driveGear.set(true);

		//driveSubsystem.autonomousPeriodic(false);
		if (currentTime <= 5) {
			driveSubsystem.drive(true);
		} else if (alreadyShot == false) {
			alreadyShot = true;
			driveSubsystem.drive(false);
			intakeSubsystem.autonomousPeriodic(1);
		}
                /*
		if (currentTime > 3) {
			intakeSubsystem.autonomousPeriodic(2);
		}
		
		
		if (currentTime <= 5 && shootFirst && autonomousBallShot == false) {
			shooterSubsystem.autonomousPeriodic();
			autonomousBallShot = true;
		}
		else if (currentTime > 5 && shootFirst) {
			driveSubsystem.autonomousPeriodic();
		}
		if (currentTime <= 5 && shootFirst == false) {
			driveSubsystem.autonomousPeriodic();
		}
		else if (currentTime > 5 && shootFirst == false && autonomousBallShot == false) {
			shooterSubsystem.autonomousPeriodic();
			autonomousBallShot = true;
		}
		*/
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
		JStickMultiton.updateAll();

		for (Enumeration e = subsystems.elements(); e.hasMoreElements();) {
			((Subsystem) e.nextElement()).teleopPeriodic();
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
