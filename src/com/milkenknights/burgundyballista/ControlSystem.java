package com.milkenknights.burgundyballista;

/**
 * An abstract class for control systems. Implementations should access this
 * class and and the teleopPeriodic method, and control the subsystems based on
 * joystick inputs.
 * 
 * Getting Joystick instances should be handled by the implementing class.
 * 
 * @author Daniel
 */
public abstract class ControlSystem {
	protected CasterSubsystem casterSub;
	protected DriveSubsystem driveSub;
	protected FourBarSubsystem fourBarSub;
	protected IntakeSubsystem intakeSub;
	protected ShooterSubsystem shooterSub;
	
	protected ControlSystem(CasterSubsystem sCaster,
			             DriveSubsystem sDrive,
						 FourBarSubsystem sFourBar,
						 IntakeSubsystem sIntake,
						 ShooterSubsystem sShooter) {
		casterSub = sCaster;
		driveSub = sDrive;
		fourBarSub = sFourBar;
		intakeSub = sIntake;
		shooterSub = sShooter;
	}
	public abstract void teleopPeriodic();
}
