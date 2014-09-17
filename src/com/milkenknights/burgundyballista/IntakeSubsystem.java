package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeSubsystem extends Subsystem {

	Talon tIntake;
	Solenoid sIntake;
	JStick atka;

	FourBarSubsystem fourbar;
	
	public static final int WHEELS_STOPPED = 0;
	public static final int WHEELS_INTAKE = 1;
	public static final int WHEELS_OUTTAKE = 2;
	
	public static final boolean INTAKE_UP = false;
	public static final boolean INTAKE_DOWN = true;
	
	
	public IntakeSubsystem(RobotConfig config, FourBarSubsystem fourbar) {
		tIntake = new Talon(config.getAsInt("tIntake"));
		sIntake = new Solenoid(config.getAsInt("sIntakeA"));
		atka = JStickMultiton.getJStick(2);
		this.fourbar = fourbar;
	}
	
	public void teleopInit() {
		setWheelsState(WHEELS_STOPPED);
	}

	public void teleopPeriodic() {
		if (atka.isPressed(5)) {
			// Outtake
			setWheelsState(WHEELS_OUTTAKE);
			fourbar.setPosition(FourBarSubsystem.OUTTAKE);
		}

		if (atka.isPressed(4)) {
			setWheelsState(WHEELS_INTAKE);
		}

		if (atka.isReleased(4) || atka.isReleased(5)) {
			setWheelsState(WHEELS_STOPPED);
			fourbar.setPosition(FourBarSubsystem.LOAD);
		}

		if (atka.isPressed(3)) {
			toggleIntakePosition();
		}

		SmartDashboard.putBoolean("Intake up", !sIntake.get());

	}
	
	public void setWheelsState(int s) {
		SmartDashboard.putNumber("intake wheels state", s);
		if (s == WHEELS_STOPPED) {
			tIntake.set(0);
		} else if (s == WHEELS_INTAKE) {
			tIntake.set(-1);
		} else if (s == WHEELS_OUTTAKE) {
			tIntake.set(1);
		}
		
	}
	
	public void setIntakePosition(boolean p) {
		sIntake.set(p);
	}
	
	public void toggleIntakePosition() {
		sIntake.set(!sIntake.get());
	}

}
