
package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;


public class ShooterSubsystem extends Subsystem {
	Talon tWinch;
	JStick joystick;
	Solenoid sWinch;
	PIDSystem PID;

	boolean loaded;
	
	// Tells the auton or teleop periodic whether to run the PID update function
	boolean runPID;

	double pullBack;
	
	Encoder shooterEncoder;

	public ShooterSubsystem(RobotConfig config) {
		pullBack = config.getAsDouble("winchPullBack");
		
		tWinch = new Talon(config.getAsInt("tWinch"));
		joystick = JStickMultiton.getJStick(2);
		sWinch = new Solenoid(config.getAsInt("sWinch"));
		PID = new PIDSystem(pullBack, config.getAsDouble("shooterPIDkp"),
				config.getAsDouble("shooterPIDki"),
				config.getAsDouble("shooterPIDkd"));
		runPID = false;
		shooterEncoder = new Encoder(config.getAsInt("winchEncA"),
			   config.getAsInt("winchEncB"), true, EncodingType.k4X);
		
		shooterEncoder.reset();

	}

	public void teleopPeriodic() {
		if (joystick.isPressed(1)) {
			shoot();
			load();
		}
		
		if (runPID) {
			tWinch.set(PID.update(shooterEncoder.getDistance()));
		}
		
		

	}
	
	public void autonomousPeriodic(int step) {
		if (step == 1) {
			load();
		}
		else if (step == 2) {
			shoot();
			load();
		}
		if (runPID) {
			tWinch.set(PID.update(shooterEncoder.getDistance()));
		}
	}
	
	public void load() {
		if (!loaded) {
			runPID = true;
			sWinch.set(true);
			loaded = true;
		}
	}
	
	public void shoot() {
		if (loaded) {
			sWinch.set(false);
			runPID = false;
			shooterEncoder.reset();
			loaded = false;
		}
	}



}