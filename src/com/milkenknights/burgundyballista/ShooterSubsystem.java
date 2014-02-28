
package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;


public class ShooterSubsystem extends Subsystem {
	Talon tWinch;
	JStick joystick;
	Solenoid sWinch;
	
	PIDSystem PID;

	boolean loaded;

	double pullBack;

	public ShooterSubsystem(RobotConfig config) {
		pullBack = config.getAsDouble("winchPullBack");
		
		tWinch = new Talon(config.getAsInt("tWinch"));
		joystick = JStickMultiton.getJStick(2);
		sWinch = new Solenoid(config.getAsInt("sWinch"));
		PID = new PIDSystem(pullBack, config.getAsDouble("shooterPIDkp"),
				config.getAsDouble("shooterPIDki"),
				config.getAsDouble("shooterPIDkd"));

	}

	public void teleopPeriodic() {
		if (joystick.isPressed(3)) {
			load();
		}
		
		if (joystick.isPressed(1)) {
			shoot();
		}
		
		

	}
	
	public void autonomousPeriodic(int step) {
		if (step == 1) {
			load();
		}
		else if (step == 2) {
			shoot();
		}
	}
	
	public void load() {
		if (loaded == false) {
			//PID code here
			sWinch.set(true);
			loaded = true;
		}
	}
	
	public void shoot() {
		if (loaded) {
			sWinch.set(false);
			loaded = false;
		}
	}



}