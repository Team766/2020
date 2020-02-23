
package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.framework.AutonomousCommandUtils;
import com.team766.framework.Command;
import com.team766.frc2020.mechanisms.Drive;
import com.team766.hal.MyRobot;
import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.wpilibj.AnalogInput;
public class Ultrasonic extends Mechanism{
    private static double kValueToInches = 0.125; // factor to convert sensor values to a distance in inches
    private AnalogInput m_ultrasonic;
    
    public Ultrasonic(int kUltrasonicPort){
      m_ultrasonic = new AnalogInput(kUltrasonicPort);
    }

    public double getDistance(){
        double currentDistance = m_ultrasonic.getValue() * kValueToInches;
        return currentDistance;
    }
    
    public boolean isBallPresent(){
      if (getDistance() < 3.0) {
        return true;
      }
      return false;
    }
}