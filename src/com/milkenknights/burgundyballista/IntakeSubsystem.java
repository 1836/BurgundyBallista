package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeSubsystem extends Subsystem {

	Talon tIntake;
	Solenoid sIntake;
	JStick joystick;

	FourBarSubsystem fourbar;

	boolean solenoidOn;

	boolean makeItGoOut;

	public IntakeSubsystem(RobotConfig config, FourBarSubsystem fourbar) {
		tIntake = new Talon(config.getAsInt("tIntake"));
		sIntake = new Solenoid(config.getAsInt("sIntakeA"));
		joystick = JStickMultiton.getJStick(2);
		this.fourbar = fourbar;
		makeItGoOut = false;
	}

	public void teleopPeriodic() {
		if (joystick.isPressed(5)) {
			// Outtake
			tIntake.set(1);
			if (!makeItGoOut) {
				makeItGoOut = true;
				fourbar.outtakePosition();
			}
		}

		if (joystick.isPressed(4)) {
			tIntake.set(-1);
		}

		if (joystick.isReleased(4) || joystick.isReleased(5)) {
			tIntake.set(0);
			makeItGoOut = false;
			fourbar.loadPosition();
		}

		if (joystick.isPressed(3)) {
			sIntake.set(!solenoidOn);
			solenoidOn = !solenoidOn;
		}

		SmartDashboard.putBoolean("Intake up", !sIntake.get());

	}

	public void autonomousInit() {
		sIntake.set(false);
	}

	public void autonomousPeriodic(int step) {
		if (step == 1) {
			tIntake.set(1);

		} else if (step == 2) {
			tIntake.set(0);
			sIntake.set(true);
		}
	}

}
