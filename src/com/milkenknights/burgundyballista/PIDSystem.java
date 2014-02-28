/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.milkenknights.burgundyballista;

/**
 * Handles any PID someone needs to do.
 * @author Nathan Hakkakzadeh
 */
public class PIDSystem {
	
	private double setpoint;
	private double kp;
	private double ki;
	private double kd;
	
	private double sumOfErrors;
	
	/*
	* Initializes a PID system you want done. Give it a setpoint and tuning
	* variables and it will provide functions anybody can use to do PID.
	*/
	public PIDSystem(double setpoint, double kp, double ki, double kd) {
		this.setpoint = setpoint;
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
	}
	
	private double PFunction(double position, double error) {
		double pValue = kp * error;
		return pValue;
	}
	
	private double IFunction(double position, double error) {
		
	}	
}
