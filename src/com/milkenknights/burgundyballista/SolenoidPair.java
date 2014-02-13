package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.Solenoid;

public class SolenoidPair {
	protected final Solenoid sa;
	protected final Solenoid sb;
	
	private boolean state;
	
	public boolean get() {
		return state;
	}
	
	public void set(boolean on) {
		state = on;
	}
	
	public final void toggle() {
		state = !state;
		set(state);
	}

	public SolenoidPair(int a, int b, boolean initial) {
		sa = new Solenoid(a);
		sb = new Solenoid(b);
		set(initial);
	}
}
