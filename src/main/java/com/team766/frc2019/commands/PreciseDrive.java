package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class PreciseDrive extends Subroutine {

    double m_driveTime;

    public PreciseDrive() {
        m_driveTime = 4.0;
        takeControl(Robot.drive);
    }

    public PreciseDrive(double driveTime) {
        m_driveTime = driveTime;
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        int i = 0;
        int numUpdates = 10;
        double targetAngle = Robot.drive.getGyroAngle() % 360;
        double error = 0;
        int P = 1, I = 1, D = 1;
        int integral = 0, previous_error = 0;
        double derivative = 0, PIDAngle = 0;
        System.out.println("(Going Straight) Current Angle: " + Robot.drive.getGyroAngle() + " Target Angle: " + targetAngle + " PID Angle: " + PIDAngle);
        while (i < (m_driveTime * numUpdates)) {
            if (!(Robot.drive.getGyroAngle() >= targetAngle - 1 && Robot.drive.getGyroAngle() <= targetAngle + 1)) {
                error = Robot.drive.calculateError(Robot.drive.getGyroAngle(), targetAngle);
                integral += error*.02;
                derivative = (error - previous_error) / .02;
                PIDAngle = P * error + I * integral + D * derivative;
                callSubroutine(new PreciseTurn(PIDAngle));
            }
            System.out.println("(Going Straight) Current Angle: " + Robot.drive.getGyroAngle() + " Target Angle: " + targetAngle + " PID Angle: " + PIDAngle);
            i++;
            Robot.drive.setDrivePower(-1, -1);
            waitForSeconds(1.0 / numUpdates);
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }
}