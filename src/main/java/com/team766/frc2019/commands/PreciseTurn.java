package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController.ControlMode;
import com.team766.controllers.PIDController;
import com.team766.hal.RobotProvider;

public class PreciseTurn extends Subroutine {

    double m_turnAngle;
    PIDController m_turnController;

    public PreciseTurn(double turnAngle) {
        m_turnAngle = turnAngle;
        m_turnController = new PIDController(Robot.drive.P, Robot.drive.I, Robot.drive.D, Robot.drive.THRESHOLD, RobotProvider.getTimeProvider());
        //takeControl(Robot.drive);
    }

    protected void subroutine() {
        double power = 0;
        double index = 0;
        System.out.println("hey im gonna turn");
        m_turnController.setSetpoint(0.0);
        m_turnController.calculate(Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_turnAngle), true);
        while(!(Robot.drive.isTurnDone(m_turnController))) {
            m_turnController.calculate(Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_turnAngle), true);
            power = m_turnController.getOutput();
            if (Math.abs(power) < Robot.drive.MIN_TURN_SPEED) {
                if (power < 0) {
                    power = -Robot.drive.MIN_TURN_SPEED;
                } else {
                    power = Robot.drive.MIN_TURN_SPEED;
                }
            }
            Robot.drive.setDrive(-power / 1.75, power / 1.75);
            if (index++ % 10 == 0) {
                System.out.println("Current Angle : " + Robot.drive.getGyroAngle() + " Target Angle: " + m_turnAngle + " Diff: " + Robot.drive.AngleDifference(Robot.drive.getGyroAngle(), m_turnAngle) + " POut: " + m_turnController.getOutput());
            }
            if (!Robot.drive.isEnabled()){
                Robot.drive.nukeRobot();
                yield();
                return;
            }
        }
        Robot.drive.setDrive(0.0, 0.0);
        Robot.drive.resetEncoders();
        yield();
        return;
    }
}
