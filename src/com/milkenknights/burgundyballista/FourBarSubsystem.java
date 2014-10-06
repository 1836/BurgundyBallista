/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Jake
 */
public class FourBarSubsystem extends Subsystem {

	Talon tFourBar;
	PIDSystem fourBarPIDUp;
	PIDSystem fourBarPIDDown;
	Encoder encoder;
	JStick atka;

	double outtakePosition;

	int position;
	public static final int LOAD = 1;
	public static final int SHOOT = 2;
	public static final int OUTTAKE = 3;
	
	boolean goingUp;

	double positionDistance;

	public FourBarSubsystem(RobotConfig config) {
		tFourBar = new Talon(config.getAsInt("tFourBar"));
		
		positionDistance = config.getAsDouble("fourBarDistanceA");
				
		encoder = new Encoder(config.getAsInt("fourBarEncA"),
			config.getAsInt("fourBarEncB"));
		
		fourBarPIDUp = new PIDSystem(positionDistance,
			config.getAsDouble("fourBarPIDkpUp"),
			config.getAsDouble("fourBarPIDkiUp"),
			config.getAsDouble("fourBarPIDkdUp"), 0);

		fourBarPIDDown = new PIDSystem(0,
			config.getAsDouble("fourBarPIDkpDown"),
			config.getAsDouble("fourBarPIDkiDown"),
			config.getAsDouble("fourBarPIDkdDown"), 0);

		atka = JStickMultiton.getJStick(3);


		outtakePosition = config.getAsDouble("fourBarDistanceDown");
		goingUp = false;
		
		encoder.start();
		encoder.reset();
		
		setPosition(LOAD);
	}
	
	public void teleopInit() {
		setPosition(LOAD);
	}

	public void teleopPeriodic() {
		if (atka.isPressed(2)) {
			if (position == SHOOT) {
				setPosition(LOAD);
			} else {
				setPosition(SHOOT);
			}
		}
		//System.out.println("" + encoder.getDistance());
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int pos) {
		if (pos == LOAD) {
			goingUp = false;
			fourBarPIDDown.changeSetpoint(0);
		} else if (pos == SHOOT) {
			fourBarPIDUp.changeSetpoint(positionDistance);
			goingUp = true;
		} else if (pos == OUTTAKE && position != OUTTAKE) {
			if (goingUp) {
				goingUp = false;
				fourBarPIDDown.changeSetpoint(outtakePosition);
			} else {
				goingUp = true;
				fourBarPIDUp.changeSetpoint(outtakePosition);
			}
		}
		
		position = pos;
	}
		
	public void update() {
		if (goingUp) {
			double out = fourBarPIDUp.update(encoder.getDistance());
			tFourBar.set(out);
		} else {
			double out = fourBarPIDDown.update(encoder.getDistance());
			tFourBar.set(out);
		}
		
		SmartDashboard.putNumber("fourbar encoder", encoder.getDistance());
	}
	
	public void test() {
		tFourBar.set(atka.getAxis(2));
		SmartDashboard.putNumber("fourbar encoder", encoder.getDistance());
	}
}
