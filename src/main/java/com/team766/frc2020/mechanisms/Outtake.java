package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.frc2020.mechanisms.LightSensor;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController;
import com.team766.controllers.PIDController;
import com.team766.hal.RobotProvider;
import com.team766.frc2020.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Outtake extends Mechanism {

    private CANSpeedController m_talon1;
    private CANSpeedController m_talon2;
    private PIDController m_velocityController;
    private double kShoot = 0.1;

    public Outtake() {
        m_talon1 = RobotProvider.instance.getTalonCANMotor("outtake.leftTalon");
        m_talon2 = RobotProvider.instance.getTalonCANMotor("outtake.rightTalon");
        m_talon1.setInverted(true);
    }

    public void setOuttakePower(double outtakePower) {
        m_talon1.set(outtakePower);
        m_talon2.set(outtakePower);
    }

    public void setOuttakePower(double outtakePowerLeft, double outtakePowerRight) {
        m_talon1.set(outtakePowerLeft);
        m_talon2.set(outtakePowerRight);
    }

    public void stopOuttake() {
        m_talon1.stopMotor();
        m_talon2.stopMotor();
    }

    public void testOuttake() {
        double power = 0.5;  
        SmartDashboard.putNumber("outtake power", power);
        m_talon1.set(power);
        m_talon2.set(power);
    }

    /**
     * set the outtake power dynamically based on distance away
     * @param distance away from target in inches
     */
    public void setOuttakePowerDistance(double distance) {
        // uses the Talon PID and not our own
        m_talon1.set(kShoot * distance);
        m_talon2.set(kShoot * distance);
    }

    public void continuousDistanceShoot() {
        while(Robot.lightSensor.getTopLightSensorState()){
            //double distance = limelight.getDistanceToTarget();
            m_talon1.set(kShoot * distance);
            m_talon2.set(kShoot * distance);
        }
    }

}