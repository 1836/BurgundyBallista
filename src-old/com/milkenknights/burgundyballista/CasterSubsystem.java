package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.Solenoid;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jake
 */
public class CasterSubsystem extends Subsystem {
	JStick xbox;
	Solenoid caster;
	
	public CasterSubsystem(RobotConfig config) {
		xbox = JStickMultiton.getJStick(1);
		caster = new Solenoid(5);
	}
	
	public void teleopPeriodic() {
		if (xbox.isReleased(JStick.XBOX_RB)) {
			caster.set(!caster.get());
		}
	}
}
