
package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class ShooterSubsystem extends Subsystem {
	Talon tWinch;
	JStick joystick;
	DoubleSolenoid sWinch;
	//PIDSystem PID;
	
	int state = 0;
	public static final int WINCH_INITIAL = 0;
	public static final int WINCH_PULLING = 1;
	public static final int WINCH_PULLED = 2;
	public static final int WINCH_SHOOTING = 3;
	public static final int WINCH_STOPPED = 4;
	
	// when we shoot, we should wait a certain duration before being certain
	// that we have gone back into the initial state.
	double lastShootTime;
	static final double SHOOT_DELAY = 0.5;
        
	DigitalInput limitswitch;
	
	//Encoder shooterEncoder;

	public ShooterSubsystem(RobotConfig config) {
		tWinch = new Talon(config.getAsInt("tWinch"));
		joystick = JStickMultiton.getJStick(2);
		sWinch = new DoubleSolenoid(config.getAsInt("sWinchA"), config.getAsInt("sWinchB"));
		limitswitch = new DigitalInput(config.getAsInt("lsShooter"));
	}
	
	public int getState() {
		return state;
	}
		
	public void teleopPeriodic() {
		// in teleop, the winch should always be pulled back
		if (getState() == WINCH_INITIAL) {
			pullBack();
		}
		if (joystick.isPressed(1) && state == WINCH_PULLED) {
			shoot();
		}
		
		if (joystick.isReleased(11)) {
			pullBack();
		}
	}
	
	public void pullBack() {
		state = WINCH_PULLING;
	}
	
	/**
	 * This method will disengage the winch solenoid. This can be called at any
	 * time, but should only be called if the winch is fully pulled back.
	 * shooterSubsystem.getState() should be equal to WINCH_PULLED.
	 */
	public void shoot() {
		if (state != WINCH_PULLED) {
			System.out.println("Warning! The robot is shooting before the"
					+ "winch has been pulled back.");
		}
		
		if (joystick.isReleased(8)) {
			stopWinch();
		}
		
		state = WINCH_SHOOTING;
		sWinch.set(DoubleSolenoid.Value.kForward);
		lastShootTime = Timer.getFPGATimestamp();
	}
	
	public void stopWinch() {
		state = WINCH_STOPPED;
	}
	
	public void update() {
		if (state == WINCH_PULLING) {
			if (!limitswitch.get()) {
				state = WINCH_PULLED;
				tWinch.set(0);
			} else {
				tWinch.set(1);
			}
		} else if (state == WINCH_SHOOTING &&
				(Timer.getFPGATimestamp() - lastShootTime) > SHOOT_DELAY) {
			state = WINCH_INITIAL;
			sWinch.set(DoubleSolenoid.Value.kReverse);
		} else if (state == WINCH_STOPPED) {
			tWinch.set(0);
		}
		System.out.println("limit switch "+(limitswitch.get() ? "on" : "off"));
	}
	
	public void test() {
		boolean lim = limitswitch.get();
		
		System.out.println("lim "+(lim?"on":"off"));
		SmartDashboard.putBoolean("lim", lim);
	}
}