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
            int caliBreak = 0;
            while (Robot.elevator.getUpperMinLimitSwitch() && Robot.drive.isEnabled()) {
                Robot.elevator.setUpperPower(-0.4);
                if (index++ % 60 == 0 && Robot.drive.isEnabled()) {
                    System.out.println("Upper moving down to calibrate: " + Robot.elevator.getUpperMinLimitSwitch());
                }
                if (caliBreak++ >= 36000) {
                    System.out.println("Upper elevator timed out; breaking");
                    Robot.elevator.setUpperPower(0.0);
                    break;
                }
            }
            Robot.elevator.resetUpperEncoder();
            Robot.elevator.setUpperPower(0.0);
            if (!Robot.elevator.getUpperMinLimitSwitch()) {
                System.out.println("Successfully reset upper encoder");
            } else {
                System.out.println("Timed out upper elevator");
            }
            m_isrunning = false;
    }

    protected void calibrateLower() {
        m_isrunning = true;
        Robot.elevator.setLowerPower(0.3);
        waitForSeconds(0.2);
        Robot.elevator.setLowerPower(0.0);
        int caliBreak = 0;
        while (Robot.elevator.getLowerMinLimitSwitch() && Robot.drive.isEnabled()) {
            Robot.elevator.setLowerPower(-0.2);
            if (index++ % 60 == 0 && Robot.drive.isEnabled()) {
                System.out.println("Lower moving down to calibrate: " + Robot.elevator.getLowerMinLimitSwitch());
            }
            if (caliBreak++ >= 36000) {
                System.out.println("Lower elevator timed out; breaking");
                Robot.elevator.setLowerPower(0.0);
                break;
            }
        }
        Robot.elevator.resetLowerEncoder();    
        Robot.elevator.setLowerPower(0.0);
        Robot.elevator.setLowerPosition(0.0);
        if (!Robot.elevator.getLowerMinLimitSwitch()) {  
            System.out.println("Succesfully reset lower encoder");    
        } else {
            System.out.println("Timed out lower elevator");
        }
        m_isrunning = false;
    }

    public boolean isRunning() {
        return m_isrunning;
    }
}
