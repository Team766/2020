package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.controllers.PIDController;
import com.team766.hal.RobotProvider;
import com.team766.frc2019.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Outtake extends Mechanism {

    private CANSpeedController m_talon1;
    private CANSpeedController m_talon2;
    private PIDController m_velocityController1;
    private PIDController m_velocityController2;
    private final double P = 0.01;
    private final double I = 0.00;
    private final double D = 0.00;
    private final double THRESHOLD = 0.25;
    private final double idlePower = 0;

    // SmartDashboard.putNumber("Outtake bla",  bla);

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

    /**
     * This sets and maintains the given velocity to shoot balls
     * @param targetVelocity this is the goal velocity for the shooter to reach. 
     * This must be calculated based on distance to target and then be passed in as a parameter.
     * 
     */
    public void shootingSpeed(double targetVelocity) {
        m_velocityController1 = new PIDController(P, I, D, THRESHOLD, RobotProvider.getTimeProvider());
        m_velocityController2 = new PIDController(P, I, D, THRESHOLD, RobotProvider.getTimeProvider());
        m_velocityController1.setSetpoint(targetVelocity);
        m_velocityController2.setSetpoint(targetVelocity);
        // while (CONDITION  (some button says I still need to be shooting--done by drivers)) {
        m_velocityController1.calculate(m_talon1.getSensorVelocity(), true);
        m_velocityController2.calculate(m_talon2.getSensorVelocity(), true); // because clamp is true, clipped will bound value for power [0,1]

        double power1 = m_velocityController1.getOutput();
        double power2 = m_velocityController2.getOutput();

        setPower(power1, power2);
        // }

        setPower(idlePower, idlePower);
    }

    /**
     * 
     * @param distance inches away from target
     * @return power to send to motors
     */
    public double calculateVelocity(double distance) {
        double k = 0.1; // constantMuliplier TODO: make a real calculation for this stuff
        double output = distance * k;
        return output;
    }

}