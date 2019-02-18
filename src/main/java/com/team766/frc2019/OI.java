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
		double fwd_power = Math.pow(-(1.2)*m_joystick1.getRawAxis(1), 1);
		double turn_power = Math.pow((0.7)*m_joystick2.getRawAxis(0), 1);
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

		/// Ryan added this code
		double leftPower = (fwd_power + turn_power) * (10000);
		double rightPower = (fwd_power - turn_power) * (10000);
		Robot.drive.setDrive(leftPower, rightPower, ControlMode.Velocity);
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
			Robot.elevator.setActuatorPower(0.75);
			Robot.elevator.stopTargeting = true;
			System.out.println("Actuator Height - " + Robot.elevator.getActuatorHeight());
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.setActuatorPower(-0.75);
			Robot.elevator.stopTargeting = true;
			System.out.println("Actuator Height - " + Robot.elevator.getActuatorHeight());
		} else {
			Robot.elevator.setActuatorPower(0.0);
			System.out.println("Actuator stopped moving");
			//Robot.elevator.actuatorNeutral();
		}

		//BIG ELEVATOR MOVEMENT
		if (m_boxop.getRawButton(ELEVATOR_UP) ) {
			Robot.elevator.elevatorUp(); 
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN)) {
			Robot.elevator.elevatorDown();
		} else {
			Robot.elevator.elevatorNeutral();
		}

		//INTAKE FORWARD AND BACK
		if (m_boxop.getRawButton(INTAKE_ACTUATE)) {
			Robot.flowerActuator.extend();
		} else if (m_boxop.getRawButton(INTAKE_RETRACT)) {
			Robot.flowerActuator.retract();
		}

		//ELEVATOR LEVELS
		if (m_boxop.getRawButton(ELEVATOR_LVL1)) {
			Robot.elevator.setCombinedPosition(Robot.elevator.LVL1);
		}
		if (m_boxop.getRawButton(ELEVATOR_LVL2)) {
			Robot.elevator.setCombinedPosition(Robot.elevator.LVL2);
		}
		if (m_boxop.getRawButton(ELEVATOR_LVL3)) {
			Robot.elevator.setCombinedPosition(Robot.elevator.LVL3);
		}
	
	}
}
