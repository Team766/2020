package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.controllers.PIDController;
import com.team766.hal.RobotProvider;
import com.team766.frc2019.Robot;


public class Outtake extends Mechanism {

    private CANSpeedController m_talon1;
    private CANSpeedController m_talon2;
    private PIDController m_velocityController;

    public Outtake() {
        m_talon1 = RobotProvider.instance.getTalonCANMotor("outtake.talon1");
        m_talon2 = RobotProvider.instance.getTalonCANMotor("outtake.talon2");
    }

    public void setPower(double outtakePower) {
        m_talon1.set(outtakePower);
        m_talon2.set(outtakePower);
    }

    public void setPower(double outtakePowerLeft, double outtakePowerRight) {
        m_talon1.set(outtakePowerLeft);
        m_talon2.set(outtakePowerRight);
    }

    public void stop() {
        m_talon1.stopMotor();
        m_talon2.stopMotor();
    }

    public void shootingSpeed(double targetVelocity) {
        //m_velocityController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
        //m_velocityController.calculate(Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_targetAngle), false);

    }

}