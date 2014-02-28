// PID System, just provide it with your tuning variables and setpoint
// and it will provide a function that takes your current position and
// outputs a thing.

package com.milkenknights.burgundyballista;

public class PIDSystem {

	private double setpoint;
	private double kp;
	private double ki;
	private double kd;
	
	private double sumOfError;
	private double lastError;

	public PIDSystem(double setpoint, double kp, double ki, double kd) {
		this.setpoint = setpoint;
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
		this.sumOfError = 0;
		this.lastError = 0;
	}

	private double PFunction(double error) {
		double pValue = kp * error;
		return pValue;
	}

	private double IFunction(double error) {
		sumOfError += error;
		double iValue = ki * sumOfError;
		return iValue;
	}

	private double DFunction(double error) {
		double changeInError = error - lastError;
		lastError = error;
		double dValue = kd * changeInError;
		return dValue;
	}

	public double update (double position) {
		double error = setpoint - position;
		double output = PFunction(error) + IFunction(error) + DFunction(error);
		return output;
	}
}
