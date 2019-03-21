package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.OI;
import com.team766.frc2019.Robot;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;

public class AutonRocketTeleop extends Subroutine {

    private JoystickReader m_joystick1;
	private JoystickReader m_joystick2;
    private JoystickReader m_boxop;
    
    private CalibrateElevator m_calibrate = new CalibrateElevator();

	private static int INTAKE_ACTUATE = 2;
	private static int INTAKE_RETRACT = 1;
	private static int INTAKE_IN = 3;
	private static int INTAKE_OUT = 4;
	private static int ELEVATOR_UP = 10;
	private static int ELEVATOR_DOWN = 9;
	private static int UPPER_UP = 21;
	private static int UPPER_DOWN = 20;
	private static int LOWER_UP = 19;
	private static int LOWER_DOWN = 18;
	private static int ADD_SMALL = 6;
	private static int SUBTRACT_SMALL = 5;
	private static int ADD_BIG = 8;
	private static int SUBTRACT_BIG = 7;
	private static int CALI_SWITCH = 17;
	private static int CALI_BUTTON = 16;
	private static int ELEVATOR_LVL3 = 13;
	private static int ELEVATOR_LVL2 = 14;
	private static int ELEVATOR_LVL1 = 15;
	
	private double fwd_power = 0;
	private double turn_power = 0;
	private double leftPower = 0;
	private double rightPower = 0;

    public AutonRocketTeleop() {
        m_joystick1 = RobotProvider.instance.getJoystick(1);
		m_joystick2 = RobotProvider.instance.getJoystick(2);
		m_boxop = RobotProvider.instance.getJoystick(3);
    }

    protected void subroutine() {

        Robot.drive.resetGyro();
        callSubroutine(new RetractGripper());
        callSubroutine(new PreciseDrive(0, 2, .35, 0));
        waitForSeconds(1.0);
        callSubroutine(new PreciseTurn(45));
        waitForSeconds(0.5);
        callSubroutine(new PreciseDrive(45, 12, .5, 0));
        
        while (Robot.drive.isAutonomous() == true) {
            // System.out.println("joystick1: " + m_joystick1.getRawAxis(0) + "joystick2: " + m_joystick2.getRawAxis(1));
            // System.out.println("iteration");
            // System.out.println("joystick1: " + m_joystick1 + "joystick2: " + m_joystick2);

            if (Math.abs(m_joystick1.getRawAxis(1)) < 0.05 ) {
                fwd_power = 0;
            } else {
                fwd_power = -(0.05*(Math.abs(m_joystick1.getRawAxis(1))/m_joystick1.getRawAxis(1)) + Math.pow(m_joystick1.getRawAxis(1), 3));
            }
            if (Math.abs(m_joystick2.getRawAxis(0)) < 0.05 ) {
                turn_power = 0;
            } else {
                turn_power = 0.05*(Math.abs(m_joystick2.getRawAxis(0))/m_joystick2.getRawAxis(0)) + Math.pow(m_joystick2.getRawAxis(0), 3);
                turn_power = 0.5 * turn_power;
                if (fwd_power > 0.5) {
                    turn_power = 0.8 * turn_power;
                }
    
    
            }
            double normalizer = Math.max(Math.abs(fwd_power),Math.abs(turn_power))/(Math.abs(fwd_power) + Math.abs(turn_power)); // divides both motor powers by the larger one to keep the ratio and keep power at or below 1
            double leftPower = (fwd_power + turn_power);
            double rightPower = (fwd_power - turn_power);
            //System.out.println("forward power: " + fwd_power + " turn power: " + turn_power);
            Robot.drive.setDrive(leftPower, rightPower);

            if(m_boxop.getRawButton(CALI_BUTTON) ) {
                //CURRENTLY CALIBRATES
                /*Robot.elevator.upperStopTargeting = true;
                Robot.elevator.setUpperPower(0.5);
                System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
                */
                if(!m_calibrate.isRunning() && Robot.drive.isEnabled()) {
                    m_calibrate.start();
                }
            }

            if (m_boxop.getRawButton(INTAKE_ACTUATE)) {
                Robot.flowerActuator.extend();
            } else if (m_boxop.getRawButton(INTAKE_RETRACT)) {
                Robot.flowerActuator.retract();
            }
    
            // GRIPPER ACTIONS
            if(m_boxop.getRawButton(INTAKE_IN) ) {
                // user clicked on the intake in button
                //System.out.println(">>> INTAKE_OPEN pressed");
                new RetractGripper().start();
            }
    
            if(m_boxop.getRawButton(INTAKE_OUT) ) {
                // user clicked on the intake out button  
                new ExtendGripper().start();  
            }
            yield();
        }
    }
}