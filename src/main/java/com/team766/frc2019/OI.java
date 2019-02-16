package com.team766.frc2019;

import com.team766.framework.Command;
import com.team766.frc2019.Robot;
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
	private static int MIN_HEIGHT = 200;
	private static int NEAR_MIN_HEIGHT = 200000;
	private static int MAX_HEIGHT = 2130000;
	private static int NEAR_MAX_HEIGHT = 1930000;
	private static int NEAR_MAX_ACTUATOR_HEIGHT = 900000;
	private static int MAX_HEIGHT_ACTUATOR = 950000;
	private static int MID_HEIGHT_BIG = 0;
	private static int MAX_HEIGHT_BIG = 0;
	private static int MID_HEIGHT_SMALL = 0;
	private static int MAX_HEIGHT_SMALL = 0;





	private static double MIN_ROBOT_VELOCITY = 2000.0;
	private static double MAX_ROBOT_VELOCITY = 20000.0;
	private static double TURN_THRESHOLD = 0.05;
	


	public OI() {
		m_joystick1 = RobotProvider.instance.getJoystick(0);
		m_joystick2 = RobotProvider.instance.getJoystick(1);
		m_boxop = RobotProvider.instance.getJoystick(2);
		new ExtendGripper().start();		
        takeControl(Robot.drive);
	}
	
	public void run() {
		// cheezy - right stick fwd/back - left stick lft/rgt
		double fwd_power = Math.pow(-m_joystick1.getRawAxis(1), 3);
		double turn_power =  Math.pow(m_joystick2.getRawAxis(0), 3);
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

		/// Ryan added this code
		double leftPower = fwd_power + turn_power;
		double rightPower = fwd_power - turn_power;
		Robot.drive.setDrivePower(leftPower, rightPower, ControlMode.PercentOutput);
		/// End of Ryan's code


		//GRIPPER ACTIONS
		if(m_boxop.getRawButton(INTAKE_IN) ) {
			// user clicked on the intake in button
			System.out.println(">>> INTAKE_OPEN pressed");
			new ExtendGripper().start();

		}
		if(m_boxop.getRawButton(INTAKE_OUT) ) {
			// user clicked on the intake out button
			System.out.println(">>> INTAKE_CLOSE pressed");
			new RetractGripper().start();
		}

		//SMALL ELEVATOR MOVEMENT
		if(m_boxop.getRawButton(ELEVATOR_UP_SMALL) ) {
			Robot.elevator.setActuatorPower(-1);
			System.out.println("Actuator Height - " + Robot.elevator.getActuatorHeight());
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.setActuatorPower(1);
			System.out.println("Actuator Height - " + Robot.elevator.getActuatorHeight());
			//if (Robot.elevator.getActuatorHeight() < MIN_HEIGHT) {
			//	Robot.elevator.resetActuatorEncoder();
			//}
		} else {
			Robot.elevator.setActuatorPower(0.0);	
		}

		//BIG ELEVATOR MOVEMENT
		if (m_boxop.getRawButton(ELEVATOR_UP) ) {
			//takeControl(Robot.elevator);
			System.out.println("Elevator Height - " + Robot.elevator.getElevatorHeight());
			if (Robot.elevator.getElevatorHeight() >= MAX_HEIGHT) {
				if (Robot.elevator.getActuatorHeight() > MAX_HEIGHT_ACTUATOR) {
					Robot.elevator.setActuatorPower(0.0);
				} else if (Robot.elevator.getActuatorHeight() > NEAR_MAX_ACTUATOR_HEIGHT) {
					Robot.elevator.setActuatorPower(0.2);
				} else {
					Robot.elevator.setActuatorPower(0.75);
				}
				Robot.elevator.setElevatorPower(0.0);
			} else if (Robot.elevator.getElevatorHeight() > NEAR_MAX_HEIGHT) {
				Robot.elevator.setElevatorPower(0.2);
			} else {
				Robot.elevator.setElevatorPower(0.75);
			}
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN)) {
			//takeControl(Robot.elevator);
			System.out.println("Elevator Height - " + Robot.elevator.getElevatorHeight());
			if (Robot.elevator.getElevatorHeight() < MIN_HEIGHT) {
				Robot.elevator.setElevatorPower(0.0);
			} else if (Robot.elevator.getElevatorHeight() < NEAR_MIN_HEIGHT) {
				Robot.elevator.setElevatorPower(-0.1);
			} else {
				Robot.elevator.setElevatorPower(-0.75);
			}
		} else {
			Robot.elevator.setElevatorPower(0.0);
		}

		//INTAKE FORWARD AND BACK
		if (m_boxop.getRawButton(INTAKE_ACTUATE)) {
			Robot.flowerActuator.extend();
		} else if (m_boxop.getRawButton(INTAKE_RETRACT)) {
			Robot.flowerActuator.retract();
		}

		//ELEVATOR LEVELS
		if (m_boxop.getRawButton(ELEVATOR_LVL1)) {
			Robot.elevator.setElevatorHeight(0, 0.25);
			Robot.elevator.setActuatorHeight(0, 0.25);

		}
		if (m_boxop.getRawButton(ELEVATOR_LVL2)) {
			Robot.elevator.setElevatorHeight(MID_HEIGHT_BIG, 0.25);
			Robot.elevator.setActuatorHeight(MID_HEIGHT_SMALL, 0.25);
		}
		if (m_boxop.getRawButton(ELEVATOR_LVL3)) {
			Robot.elevator.setElevatorHeight(MAX_HEIGHT_BIG, 0.25);
			Robot.elevator.setActuatorHeight(MAX_HEIGHT_SMALL, 0.25);

		}
	
	}
}
