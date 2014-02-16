package com.milkenknights.burgundyballista;

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
	SolenoidPair caster;
	
	public CasterSubsystem(RobotConfig config) {
		xbox = JStickMultiton.getJStick(1);
		caster = new SolenoidPair(config.getAsInt("sCasterA"),
				config.getAsInt("sCasterB"), true, true, false);
	}
	
	public void teleopPeriodic() {
		if (xbox.isReleased(JStick.XBOX_RB)) {
			caster.toggle();
		}
	}
}
