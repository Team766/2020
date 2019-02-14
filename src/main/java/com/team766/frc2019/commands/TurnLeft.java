package com.team766.frc2019.commands;

<<<<<<< HEAD
=======
import com.team766.hal.CANSpeedController.ControlMode;
>>>>>>> origin/master
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class TurnLeft extends Subroutine {
<<<<<<< HEAD
    public TurnLeft() {
        takeControl(Robot.drive);
    }
    protected void subroutine(){
        Robot.drive.setDrivePower(0.5, -0.5);
        waitForSeconds(0.7);

    Robot.drive.setDrivePower(0, 0);
   
=======

    double m_turnTime;

    public TurnLeft() {
        m_turnTime = 1.0;
        takeControl(Robot.drive);
    }

    public TurnLeft(double turnTime) {
        m_turnTime = turnTime;
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        Robot.drive.setDrive(0.25, 0.25, ControlMode.PercentOutput);
        //waitForSeconds(m_turnTime);
        waitForSeconds(1.0);

        Robot.drive.setDrive(0.0, 0.0, ControlMode.PercentOutput);
>>>>>>> origin/master
    }
}