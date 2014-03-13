/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Jake
 */
public class FourBarSubsystem extends Subsystem {

    Talon tFourBar;
    PIDSystem fourBarPIDUp;
    PIDSystem fourBarPIDDown;
    Encoder encoder;
    JStick joystick;

    double outtakePosition;

    boolean position;
    boolean goingUp;

    double positionDistance;

    public FourBarSubsystem(RobotConfig config) {
        tFourBar = new Talon(config.getAsInt("tFourBar"));
        encoder = new Encoder(config.getAsInt("fourBarEncA"),
                config.getAsInt("fourBarEncB"));
        fourBarPIDUp = new PIDSystem(config.getAsDouble("fourBarDistance"),
                config.getAsDouble("fourBarPIDkpUp"),
                config.getAsDouble("fourBarPIDkiUp"),
                config.getAsDouble("fourBarPIDkdUp"), 0);

        fourBarPIDDown = new PIDSystem(0,
                config.getAsDouble("fourBarPIDkpDown"),
                config.getAsDouble("fourBarPIDkiDown"),
                config.getAsDouble("fourBarPIDkdDown"), 0);

        joystick = JStickMultiton.getJStick(2);

        positionDistance = config.getAsDouble("fourBarDistance");

        outtakePosition = config.getAsDouble("fourBarDistanceDown");
        goingUp = false;
    }

    public void robotInit() {
        encoder.start();
        encoder.reset();
        loadPosition();
    }

    public void pidLoop() {
        if (goingUp) {
            double out = fourBarPIDUp.update(encoder.getDistance());
            tFourBar.set(out);
        } else {
            double out = fourBarPIDDown.update(encoder.getDistance());
            tFourBar.set(out);
        }
    }

    public void teleopPeriodic() {
        if (joystick.isPressed(2)) {
            position = !position;
            changePosition(position);
        }
        pidLoop();
    //System.out.println("" + encoder.getDistance());
    }

    public void autonomousInit() {
        outtakePosition();
    }
    public void autonomousPeriodic(boolean pos) {
        pidLoop();
    }

    public void changePosition(boolean a) {
        if (a) {
            shootPosition();
        } else {
            loadPosition();
        }

    }

    public void loadPosition() {
        goingUp = false;
        fourBarPIDDown.changeSetpoint(0);
    }

    public void shootPosition() {
        fourBarPIDUp.changeSetpoint(positionDistance);
        goingUp = true;

    }

    public void outtakePosition() {
        System.out.println("OK");
        if (goingUp) {
            goingUp = false;
            fourBarPIDDown.changeSetpoint(outtakePosition);
        } else {
            goingUp = true;
            fourBarPIDUp.changeSetpoint(outtakePosition);
        }
    }
}
