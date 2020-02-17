package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.controllers.PIDController;
import com.team766.hal.RobotProvider;
import com.team766.frc2019.Robot;
import java.lang.Math;


public class Outtake extends Mechanism {

    private CANSpeedController m_talon1;
    private CANSpeedController m_talon2;
    private PIDController m_velocityController;
    public double height_meters = 1.69; //vertical distance from shooter to center of top ports

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

    public double[] distanceNeeded(double velocity, double angle){
        double[] distances = new double[2];
        double acc = -9.80665;
        double InitialVy = velocity * Math.sin(angle);
        double[] timesToHeight = {((-InitialVy + Math.sqrt(Math.pow(InitialVy, 2) + 2 * acc * height_meters))/acc), ((-InitialVy - Math.sqrt(Math.pow(InitialVy, 2) + 2 * acc * height_meters))/acc)};
        distances[0] = timesToHeight[0] * velocity * Math.cos(angle);
        distances[1] = timesToHeight[1] * velocity * Math.cos(angle);
        return distances;
    }

    public double[] distanceNeeded45Degrees(double velocity){
        return distanceNeeded(velocity, Math.PI / 4);
    }
}