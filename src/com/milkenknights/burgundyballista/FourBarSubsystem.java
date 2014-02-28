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
public class FourBarSubsystem extends Subsystem{
	Talon tFourBar;
	PIDSystem fourBarPID;
	Encoder encoder;
	JStick joystick;
	
	boolean runPID;
	boolean position;
	
	double positionDistance;
	public FourBarSubsystem (RobotConfig config) {
		tFourBar = new Talon(config.getAsInt("tFourBar"));
		encoder = new Encoder(config.getAsInt("fourBarEncA"),
				config.getAsInt("fourBarEncB"));
		fourBarPID = new PIDSystem(config.getAsDouble("fourBarDistance"),
				config.getAsDouble("fourBarPIDkp"),
				config.getAsDouble("fourBarPIDki"),
				config.getAsDouble("fourBarPIDkd"));
		
		joystick = JStickMultiton.getJStick(2);
		
		positionDistance = config.getAsDouble("fourBarDistance");
	}
	
	public void teleopPeriodic() {
		if (joystick.isPressed(2)) {
			position =! position;
			changePosition(position);
		}
	}
	
	public void autonomousPeriodic(boolean pos) {
		changePosition(pos);
		if (runPID) {
			tFourBar.set(fourBarPID.update(encoder.getDistance()));
			if (fourBarPID.update(encoder.getDistance()) == 0) {
				runPID = false;
			}
		}
	}
	
	public void changePosition(boolean a) {
		if (a) {
			shootPosition();
		}
		else {
			loadPosition();
		}
		if (runPID) {
			tFourBar.set(fourBarPID.update(encoder.getDistance()));
			if (fourBarPID.update(encoder.getDistance()) == 0) {
				runPID = false;
			}
		}
	}
	
	public void loadPosition() {
		fourBarPID.changeSetpoint(0);
		runPID = true;
	}
	
	public void shootPosition() {
		fourBarPID.changeSetpoint(positionDistance);
		runPID = true;
	}
}
