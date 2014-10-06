package com.milkenknights.burgundyballista;

/**
 * This control system uses three ATK3 controllers: two for driving and one for
 * AUX.
 * @author Daniel
 */
public class TripleATKControl extends ControlSystem {
	JStick atkr, atkl, atka;
	public TripleATKControl(CasterSubsystem sCaster,
			DriveSubsystem sDrive,
			FourBarSubsystem sFourBar,
			IntakeSubsystem sIntake,
			ShooterSubsystem sShooter) {
		super(sCaster, sDrive, sFourBar, sIntake, sShooter);
		atkl = new JStick(1);
		atkr = new JStick(2);
		atka = new JStick(3);
	}
	
	public void teleopPeriodic() {
		// GEAR TOGGLE
		// controlled by right ATK trigger
		if (atkr.isReleased(1)) {
			driveSub.toggleGear();
		}
		
		// TANK DRIVE
		// controlled by left and right ATK y axes
		driveSub.setTankSpeed(atkl.getAxis(2), atkr.getAxis(2));
		
		// CASTER TOGGLE
		// controlled by left ATK trigger
		if (atkl.isReleased(1)) {
			casterSub.toggleCaster();
		}
		
		// FOUR BAR CONTROL
		// toggle between SHOOT and LOAD positions with aux ATK 2
		if (atka.isReleased(2)) {
			if (fourBarSub.getPosition() == fourBarSub.SHOOT) {
				fourBarSub.setPosition(fourBarSub.LOAD);
			} else {
				fourBarSub.setPosition(fourBarSub.SHOOT);
			}
		}
		
		// INTAKE CONTROL
		// outtake mode: aux ATK button 5 held down moves fourbar and sets
		// intake wheels to outtake mode
		if (atka.isPressed(5)) {
			intakeSub.setWheelsState(intakeSub.WHEELS_OUTTAKE);
			fourBarSub.setPosition(FourBarSubsystem.OUTTAKE);
		}
		
		// intake mode, which happens while aux ATK button 4 is held down
		if (atka.isPressed(4)) {
			intakeSub.setWheelsState(intakeSub.WHEELS_INTAKE);
		}
		
		// reset when button 4 or 5 is released
		if (atka.isReleased(4) || atka.isReleased(5)) {
			intakeSub.setWheelsState(intakeSub.WHEELS_STOPPED);
			fourBarSub.setPosition(fourBarSub.LOAD);
		}
		
		// button 3 toggles intake position
		if (atka.isReleased(3)) {
			intakeSub.toggleIntakePosition();
		}
		
		// SHOOTER CONTROL (currently commented out because we're not using the
		// shooter)
		/*
		// in teleop. the winch should always be pulled back
		if (shooterSub.getState() == shooterSub.WINCH_INITIAL) {
			shooterSub.pullBack();
		}
		
		// aux ATK trigger shoots the ball
		if (atka.isPressed(1) && shooterSub.state == shooterSub.WINCH_PULLED) {
			shooterSub.shoot();
		}
		
		// aux ATK button 11 pulls back the winch
		if (atka.isReleased(11)) {
			shooterSub.pullBack();
		}
		*/
	}
}
