package com.team766.frc2019;

//import static org.junit.Assume.assumeTrue;

import com.team766.framework.Command;
import com.team766.frc2019.Robot;
import com.team766.frc2019.commands.CalibrateElevator;
import com.team766.frc2019.commands.ExtendGripper;
import com.team766.frc2019.commands.RetractGripper;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI extends Command {
	private JoystickReader m_joystick1;
	private JoystickReader m_joystick2;
	private JoystickReader m_boxop;
	private CalibrateElevator m_calibrate;

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

	private int index = 0;

	private static double MIN_ROBOT_VELOCITY = 2000.0;
	private static double MAX_ROBOT_VELOCITY = 20000.0;
	private static double TURN_THRESHOLD = 0.05;

	public double combinedPosition;

	public static boolean calibrate;

	public OI() {
		m_joystick1 = RobotProvider.instance.getJoystick(0);
		m_joystick2 = RobotProvider.instance.getJoystick(1);
		m_boxop = RobotProvider.instance.getJoystick(2);
		new ExtendGripper().start();		
		takeControl(Robot.drive);
		m_calibrate = new CalibrateElevator();
	}
	
	public void run() {

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
		Robot.drive.setDrive(leftPower, rightPower, ControlMode.PercentOutput);

		if (index++ % 50 == 0 && Robot.drive.isEnabled()) {
			System.out.println("lower height: " + Robot.elevator.getLowerHeight());

		}
		index++;

		// SMALL ELEVATOR FORCED MANUAL MOVEMENT
		
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
			/*
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setUpperPower(-0.5);
			//Robot.elevator.setLowerPower(-0.5);
			System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
		} else {
			Robot.elevator.upperNeutral();
		}
		*/
		

		// BIG ELEVATOR FORCED MANUAL MOVEMENT
		if(m_boxop.getRawButton(LOWER_UP) ) {
			Robot.elevator.lowerStopTargeting = true;
			Robot.elevator.setLowerPower(0.5);
			System.out.println("LOWER POWER IS 0.5");
			System.out.println("Lower Height: " + Robot.elevator.getLowerHeight());
		} else if (m_boxop.getRawButton(LOWER_DOWN)) {
			Robot.elevator.lowerStopTargeting = true;
			Robot.elevator.setLowerPower(-0.5);
			System.out.println("Lower Height: " + Robot.elevator.getLowerHeight());
		} else {
			Robot.elevator.lowerNeutral();
		}

		//SMALL ELEVATOR FORCED MOVEMENT 
		if(m_boxop.getRawButton(UPPER_UP) ) {
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setUpperPower(0.5);
			System.out.println("UPPER POWER IS 0.5");
			System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
		} else if (m_boxop.getRawButton(UPPER_DOWN)) {
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setUpperPower(-0.5);
			System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
		} else {
			Robot.elevator.upperNeutral();
		}
		

		// Upper elevator basic movement w/o limits
		/*

		/*if(m_boxop.getRawButton(ELEVATOR_UP_SMALL) ) {
			Robot.elevator.addToPosition(10000);
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.addToPosition(-10000);
		}
		*/

		if (m_boxop.getRawButton(CALI_SWITCH)) {
			calibrate = true;
			//System.out.println("Calibrated");
		} else {
			calibrate = false;
			//System.out.println("No longer calibrating");
		}

		// COMBINED ELEVATOR MOVEMENT ALGORITHM
		if (m_boxop.getRawButton(ELEVATOR_UP) ) {
			Robot.elevator.elevatorUp(); 
			if (calibrate) {
				Robot.elevator.resetUpperEncoder();
				Robot.elevator.resetLowerEncoder();
				System.out.println("Calibrated");
			}
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN)) {
			Robot.elevator.elevatorDown();
			if (calibrate) {
				Robot.elevator.resetUpperEncoder();
				Robot.elevator.resetLowerEncoder();
				System.out.println("Calibrated");
			}
		} else {
			Robot.elevator.elevatorNeutral();
		}
	
		// INTAKE FORWARD AND BACK
		if (m_boxop.getRawButton(INTAKE_ACTUATE)) {
			Robot.flowerActuator.extend();
		} else if (m_boxop.getRawButton(INTAKE_RETRACT)) {
			Robot.flowerActuator.retract();
		}

		// GRIPPER ACTIONS
		if(m_boxop.getRawButton(INTAKE_IN) ) {
			// user clicked on the intake in button
			System.out.println(">>> INTAKE_OPEN pressed");
			new ExtendGripper().start();

		}

		if(m_boxop.getRawButton(INTAKE_OUT) ) {
			// user clicked on the intake out button                                                                                                                                            ggbhhhhhhhhhhmmbb  
			new RetractGripper().start();
		}

		// ELEVATOR LEVELS
		int tgtLvl = m_boxop.getRawButton(ELEVATOR_LVL1) ? 1 : 
					m_boxop.getRawButton(ELEVATOR_LVL2) ? 2 :
					m_boxop.getRawButton(ELEVATOR_LVL3) ? 3 : -1;
		switch( tgtLvl ){
			case 1:
				Robot.elevator.setCombinedPosition(Robot.elevator.LVL1);
				break;
			case 2:
				Robot.elevator.setCombinedPosition(Robot.elevator.LVL2);
				break;
			case 3:
				Robot.elevator.setCombinedPosition(Robot.elevator.LVL3);
				break;
		}
		
		if (!Robot.drive.isEnabled()) {
			Robot.drive.nukeRobot();
			m_calibrate.stop();
			return;
		}
	}
}

	
