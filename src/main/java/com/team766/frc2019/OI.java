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
	private static int INTAKE_RETRACT = 3;
	private static int INTAKE_IN = 4;
	private static int INTAKE_OUT = 5;
	private static int ELEVATOR_UP = 9;
	private static int ELEVATOR_DOWN = 10;
	private static int ELEVATOR_UP_SMALL = 7;
	private static int ELEVATOR_DOWN_SMALL = 8;
	private static int ELEVATOR_LVL3 = 11;
	private static int ELEVATOR_LVL2 = 12;
	private static int ELEVATOR_LVL1 = 13;
	
	private double fwd_power = 0;
	private double turn_power = 0;
	private double leftPower = 0;
	private double rightPower = 0;

	private int index = 0;

	private static double MIN_ROBOT_VELOCITY = 2000.0;
	private static double MAX_ROBOT_VELOCITY = 20000.0;
	private static double TURN_THRESHOLD = 0.05;

	public double combinedPosition;

	public OI() {
		m_joystick1 = RobotProvider.instance.getJoystick(0);
		m_joystick2 = RobotProvider.instance.getJoystick(1);
		m_boxop = RobotProvider.instance.getJoystick(2);
		new ExtendGripper().start();		
		takeControl(Robot.drive);
		m_calibrate = new CalibrateElevator();
	}
	
	public void run() {
		combinedPosition = Robot.elevator.getLowerHeight() + Robot.elevator.getUpperHeight();
		//output encoder ticks
		/*
		System.out.println("LowerMaxLimSwitch: " + Robot.elevator.getLowerMaxLimitSwitch());
		System.out.println("UpperMinLimSwitch: " + Robot.elevator.getUpperMinLimitSwitch());
		System.out.println("UpperMaxLimSwitch: " + Robot.elevator.getUpperMaxLimitSwitch());
		*/
		if (Math.abs(m_joystick1.getRawAxis(1)) < 0.05 ) {
			fwd_power = 0;
		} else {
			fwd_power = -(0.05*(Math.abs(m_joystick1.getRawAxis(1))/m_joystick1.getRawAxis(1)) + Math.pow(m_joystick1.getRawAxis(1), 3));
		}
		if (Math.abs(m_joystick2.getRawAxis(0)) < 0.05 ) {
			turn_power = 0;
		} else {
			turn_power = 0.05*(Math.abs(m_joystick2.getRawAxis(0))/m_joystick2.getRawAxis(0)) + Math.pow(m_joystick2.getRawAxis(0), 3);
			//turn_power = (1.1 - fwd_power) * turn_power;
			turn_power = 0.5 * turn_power;
			if (fwd_power > 0.5) {
				turn_power = 0.8 * turn_power;
			}


		}
		double normalizer = Math.max(Math.abs(fwd_power),Math.abs(turn_power))/(Math.abs(fwd_power) + Math.abs(turn_power)); // divides both motor powers by the larger one to keep the ratio and keep power at or below 1
		double leftPower = (fwd_power + turn_power);
		double rightPower = (fwd_power - turn_power);
		//System.out.println("forward power: " + fwd_power + " turn power: " + turn_power);
	//	System.out.println("lp: " + leftPower + " rp: " + rightPower + " " + (turn_power*(1.1-fwd_power)+0.5));
		Robot.drive.setDrive(leftPower, rightPower, ControlMode.PercentOutput);

//		if (index++ % 2000 == 0 && Robot.drive.isEnabled()) {
//			System.out.println("LowerMinLimSwitch: " + Robot.elevator.getLowerMinLimitSwitch());
//			System.out.println("LowerHeight: " + Robot.elevator.getLowerHeight() + " UpperHeight: " + Robot.elevator.getUpperHeight());/
//		}
		// cheezy - right stick fwd/back - left stick lft/rgt
		//double fwd_power = Math.pow(-(1.0)*m_joystick1.getRawAxis(1), 1);
		//double turn_power = Math.pow((0.45)*m_joystick2.getRawAxis(0), 1);
		/*
		if (m_joystick2.getRawAxis(0) > 0.001) {
			turn_power =  Math.pow((0.8)*m_joystick2.getRawAxis(0), 2);
		} else if (m_joystick2.getRawAxis(0) <= 0.001 && m_joystick2.getRawAxis(0) >= -0.001) {
			turn_power = 0.0;
		} else {
			turn_power =  -Math.pow((0.8)*m_joystick2.getRawAxis(0), 2);
		}

		if (m_joystick1.getRawAxis(1) >= 0) {
			fwd_power = -Math.pow(-(1.0)*m_joystick1.getRawAxis(1), 1);
		} else if (m_joystick1.getRawAxis(0) <= 0.001 && m_joystick1.getRawAxis(0) >= -0.001) {
			fwd_power = 0.0;
		} else {
			fwd_power = Math.pow((1.0)*m_joystick1.getRawAxis(1), 1);
		}



		while (m_joystick2.getRawButton(1)) {
			turn_power *= 0.5;
		}
		*/
		

		//if (m_joystick2.getRawAxis(0) >= 0) {
			//turn_power = Math.pow((1.0)*m_joystick2.getRawAxis(0), 0.5);
			//System.out.println("joystick: " + m_joystick2.getRawAxis(0));
			//turn_power = .7*(Math.atan( m_joystick2.getRawAxis(0)*1.2 - 1.2 ) + (Math.PI/2) - 0.69473);
			//System.out.println("turn power: " + turn_power);
		//} else {
			//turn_power = -Math.pow((1.0)*m_joystick2.getRawAxis(0), 0.5);
			//System.out.println("joystick: " + m_joystick2.getRawAxis(0));
			//turn_power = .7*(( Math.atan( m_joystick2.getRawAxis(0)*1.2 + 1.2 ) - 0.87605));
			//System.out.println("turn power: " + turn_power);
		//}
		//System.out.println(m_joystick2.getRawAxis(0));
		/*double leftPower = fwd_power*MAX_ROBOT_VELOCITY;
		double rightPower = fwd_power*MAX_ROBOT_VELOCITY;
		double normalizer = Math.max(Math.abs(fwd_power),Math.abs(turn_power))/(Math.abs(fwd_power) + Math.abs(turn_power)); // divides both motor powers by the larger one to keep the ratio and keep power at or below 1
		if (Math.abs(turn_power)>TURN_THRESHOLD) {
			rightPower = Math.min((fwd_power - turn_power) * MAX_ROBOT_VELOCITY * normalizer, MIN_ROBOT_VELOCITY);
			leftPower = Math.min((fwd_power + turn_power) * MAX_ROBOT_VELOCITY * normalizer, MIN_ROBOT_VELOCITY);
			//rightPower = (fwd_power - turn_power) * normalizer;
			//leftPower = (fwd_power + turn_power) * normalizer;
		}

		Robot.drive.setDrive(leftPower, rightPower, ControlMode.Velocity);
		System.out.println(fwd_power + "  " + turn_power + "  " + leftPower + "  " + rightPower);
		// Robot.drive.setDrivePower(leftPower, rightPower);*/



		// SMALL ELEVATOR FORCED MANUAL MOVEMENT
		
		if(m_boxop.getRawButton(ELEVATOR_UP_SMALL) ) {
			/*Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setUpperPower(0.5);
			System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
			*/
			if(!m_calibrate.isRunning() && Robot.drive.isEnabled()) {
				m_calibrate.start();
			}
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setUpperPower(-0.5);
			//Robot.elevator.setLowerPower(-0.5);
			System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
		} else {
			Robot.elevator.upperNeutral();
		}
		

		// BIG ELEVATOR FORCED MANUAL MOVEMENT
		/*if(m_boxop.getRawButton(ELEVATOR_UP_SMALL) ) {
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setLowerPower(0.5);
			System.out.println("UPPER POWER IS 0.75");
			System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setLowerPower(-0.5);
			System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
		} else {
			Robot.elevator.setLowerPower(0.0);
			//System.out.println("Upper stopped moving");
			//Robot.elevator.upperNeutral();
		}
		*/

		// Lower Elevator basic movement w/o limits
		/*
		if(m_boxop.getRawButton(ELEVATOR_UP)) {
			Robot.elevator.setLowerPower(0.75);
			System.out.println("Lower going up");
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN)) {
			Robot.elevator.setLowerPower(-0.75);
			System.out.println("Lower going down");
		} else {
			Robot.elevator.setLowerPower(0.0);
		}
		*/
		

		// Upper elevator basic movement w/o limits
		/*
		if(m_boxop.getRawButton(ELEVATOR_UP_SMALL)) {
			Robot.elevator.setUpperPower(0.5);
			System.out.println("Upper going up");
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.setUpperPower(-0.5);
			System.out.println("Upper going down");
		} else {
			Robot.elevator.setUpperPower(0.0);
		}
		*/
		
		/*if(m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.setUpperPower(-0.5);
			System.out.println("Upper going down");
		} else {
			Robot.elevator.setUpperPower(0.0);
		}
		*/

		// 
		/*if(m_boxop.getRawButton(ELEVATOR_UP_SMALL) ) {
			Robot.elevator.addToPosition(10000);
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.addToPosition(-10000);
		}
		*/

		// COMBINED ELEVATOR MOVEMENT ALGORITHM
		if (m_boxop.getRawButton(ELEVATOR_UP) ) {
			Robot.elevator.elevatorUp(); 
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN)) {
			Robot.elevator.elevatorDown();
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

		//System.out.println("==> Going to level: " + tgtLvl );
		switch( tgtLvl ){
			case 1:
				/*if(!m_calibrate.isRunning() && Robot.drive.isEnabled()) {
					m_calibrate.start();
				}
				*/
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
