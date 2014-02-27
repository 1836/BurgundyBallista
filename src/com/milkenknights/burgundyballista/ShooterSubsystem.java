/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;


/**
 *
 * @author Jake
 */
public class ShooterSubsystem extends Subsystem {
	Talon tWinch;
	JStick joystick;
	Solenoid sWinch;
	
	boolean loaded;
	
	int pullBack;
	
	public ShooterSubsystem(RobotConfig config) {
		tWinch = new Talon(config.getAsInt("tWinch"));
		joystick = JStickMultiton.getJStick(2);
		sWinch = new Solenoid(config.getAsInt("sWinch"));
		
		pullBack = config.getAsInt("winchPullBack");
	}
	
	public void teleopPeriodic() {
		if (joystick.isReleased(3) && loaded == false) {
			
		}
		
	}
	
	
	
}
