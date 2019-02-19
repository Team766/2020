package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class CalibrateElevator extends Subroutine {

    private int index = 0;
    private boolean m_isrunning = false;

    public CalibrateElevator() {
        takeControl(Robot.elevator);
    }

    protected void subroutine() {
        calibrateUpper();
        calibrateLower();
    }

    protected void calibrateUpper() {
            m_isrunning = true;
            Robot.elevator.setUpperPower(0.5);
            waitForSeconds(0.25);
            Robot.elevator.setUpperPower(0.0);
            System.out.println("Calibrate upper: " + Robot.elevator.getUpperMinLimitSwitch());
            int caliBreak = 0;
            while (Robot.elevator.getUpperMinLimitSwitch() && Robot.drive.isEnabled()) {
                Robot.elevator.setUpperPower(-0.3);
                if (index++ % 60 == 0 && Robot.drive.isEnabled()) {
                    System.out.println("Setting upper power to -0.3");
                }
                if (caliBreak++ > 36000) {
                    break;
                }
            }
            Robot.elevator.resetUpperEncoder();
            Robot.elevator.setUpperPosition(0.0);
            Robot.elevator.setUpperPower(0.0);
            Robot.elevator.resetUpperEncoder();
            System.out.println("Successfully reset upper encoder");
            m_isrunning = false;
    }

    protected void calibrateLower() {
        m_isrunning = true;
        Robot.elevator.setLowerPower(0.5);
        waitForSeconds(0.4);
        Robot.elevator.setLowerPower(0.0);
        System.out.println("Calibrate lower: " + Robot.elevator.getLowerMinLimitSwitch());
        while (Robot.elevator.getLowerMinLimitSwitch()) {
            Robot.elevator.setLowerPower(-0.1);
            if (index % 50 == 0 && Robot.drive.isEnabled()) {
                System.out.println("Setting lower power to -0.1");
            }
            index++;
        }
        Robot.elevator.resetLowerEncoder();        
        Robot.elevator.setLowerPosition(0.0);
        Robot.elevator.setLowerPower(0.0);
        Robot.elevator.resetLowerEncoder();
        System.out.println("Successfully reset lower encoder");
        m_isrunning = false;
    }

    public boolean isRunning() {
        return m_isrunning;
    }
    
}
