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
public class CasterSubsystem {
	JStick xbox;
	SolenoidPair caster;
	public CasterSubsystem(int a, int b) {
		xbox = JStickMultiton.getJStick(1);
		caster = new SolenoidPair(a, b, true, true, false);
	}
	
	public void update() {
		if (xbox.isReleased(JStick.XBOX_RB)) {
			caster.toggle();
		}
	}
}
