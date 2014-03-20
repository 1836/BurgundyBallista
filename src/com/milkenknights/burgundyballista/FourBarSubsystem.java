/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Jake
 */
public class FourBarSubsystem extends Subsystem {

	Talon tFourBar;
	PIDSystem fourBarPIDUp;
	PIDSystem fourBarPIDDown;
	Encoder encoder;
	JStick joystick;

	double outtakePosition;

	int position;
	public static final int LOAD = 1;
	public static final int SHOOT = 2;
	public static final int OUTTAKE = 3;
	
	boolean goingUp;

	double positionDistance;

	public FourBarSubsystem(RobotConfig config) {
		tFourBar = new Talon(config.getAsInt("tFourBar"));
		encoder = new Encoder(config.getAsInt("fourBarEncA"),
			config.getAsInt("fourBarEncB"));
		fourBarPIDUp = new PIDSystem(config.getAsDouble("fourBarDistance"),
			config.getAsDouble("fourBarPIDkpUp"),
			config.getAsDouble("fourBarPIDkiUp"),
			config.getAsDouble("fourBarPIDkdUp"), 0);

		fourBarPIDDown = new PIDSystem(0,
			config.getAsDouble("fourBarPIDkpDown"),
			config.getAsDouble("fourBarPIDkiDown"),
			config.getAsDouble("fourBarPIDkdDown"), 0);

		joystick = JStickMultiton.getJStick(2);

		positionDistance = config.getAsDouble("fourBarDistance");

		outtakePosition = config.getAsDouble("fourBarDistanceDown");
		goingUp = false;
		
		encoder.start();
		encoder.reset();
		
		setPosition(LOAD);
	}

	public void teleopPeriodic() {
		if (joystick.isPressed(2)) {
			if (position == SHOOT) {
				setPosition(LOAD);
			} else {
				setPosition(SHOOT);
			}
		}
		//System.out.println("" + encoder.getDistance());
	}

	public void autonomousInit() {
	}

	public void autonomousPeriodic(boolean pos) {
	}

	public void setPosition(int pos) {
		position = pos;
		if (position == LOAD) {
			goingUp = false;
			fourBarPIDDown.changeSetpoint(0);
		} else if (position == SHOOT) {
			fourBarPIDUp.changeSetpoint(positionDistance);
			goingUp = true;
		} else if (position == OUTTAKE) {
			if (goingUp) {
				goingUp = false;
				fourBarPIDDown.changeSetpoint(outtakePosition);
			} else {
				goingUp = true;
				fourBarPIDUp.changeSetpoint(outtakePosition);
			}
		}
	}
		
	public void update() {
		if (goingUp) {
			double out = fourBarPIDUp.update(encoder.getDistance());
			tFourBar.set(out);
		} else {
			double out = fourBarPIDDown.update(encoder.getDistance());
			tFourBar.set(out);
		}
	}
}
