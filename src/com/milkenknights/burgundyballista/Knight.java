/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.milkenknights.burgundyballista;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;
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
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		config = new ConfigFile("robot-config.txt");
		config.loadFile();
		
		compressor = new Compressor(config.getAsInt("compressorPressureSwitch"),
				config.getAsInt("compressorRelayChannel"));
		
		subsystems = new Vector(10);
		
		subsystems.addElement(new DriveSubsystem(config));
		subsystems.addElement(new CasterSubsystem(config));
		
		// since no more subsystems will be added, we can free the remaining
		// memory
		subsystems.trimToSize();
		
		compressor.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

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
