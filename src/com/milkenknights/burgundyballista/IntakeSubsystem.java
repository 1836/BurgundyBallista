
package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class IntakeSubsystem extends Subsystem{
	Talon tIntake;
	SolenoidPair sIntake;
	JStick joystick;
	
	boolean solenoidOn;

	
	public IntakeSubsystem(RobotConfig config) {
                tIntake = new Talon(config.getAsInt("tIntake"));
                sIntake = new SolenoidPair(config.getAsInt("sIntakeA"), 
                    config.getAsInt("sIntakeB"), true, true, false);
                joystick = JStickMultiton.getJStick(2);
        }
	
	public void teleopPeriodic() {
		if (joystick.isPressed(4)) {
			tIntake.set(1);
		}
                
		if (joystick.isPressed(5)) {
			tIntake.set(-1);
		}
                
		if (joystick.isReleased(4) || joystick.isReleased(5)) {
			tIntake.set(0);
		}
                
		if (joystick.isPressed(3)) {
			sIntake.set(!solenoidOn);
			solenoidOn =! solenoidOn;
		}
		
		SmartDashboard.putBoolean("Intake up", !sIntake.get());
     
	}
	
	public void autonomousInit() {
		sIntake.set(false);
	}
	
	public void autonomousPeriodic(int step) {
		if (step == 1) {
			tIntake.set(1);
		}
		else if (step == 2) {
			tIntake.set(0);
			sIntake.set(true);
		}
	}
	
}
