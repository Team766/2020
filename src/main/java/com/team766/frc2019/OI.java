package com.team766.frc2019;

import com.team766.framework.Command;
import com.team766.frc2019.Robot;
import com.team766.frc2019.commands.ExtendGripper;
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

	private static int WRIST_BACK = 7;
	private static int WRIST_VERTICAL = 10;
	private static int WRIST_DOWN = 9;
	private static int MANUAL_ARM_UP = 2;
	private static int MANUAL_ARM_DOWN = 4;
	private static int MANUAL_WRIST_UP = 1;
	private static int MANUAL_WRIST_DOWN = 3;

	private static double MAX_ROBOT_VELOCITY = 20000.0;
	private static double TURN_THRESHHOLD = 0.05;


	public OI() {
		m_joystick1 = RobotProvider.instance.getJoystick(0);
		m_joystick2 = RobotProvider.instance.getJoystick(1);
		m_boxop = RobotProvider.instance.getJoystick(2);		
        takeControl(Robot.drive);
	}
	
	public void run() {
		// cheezy - right stick fwd/back - left stick lft/rgt
		double fwd_power = -m_joystick1.getRawAxis(1);
		double turn_power =  m_joystick2.getRawAxis(0);
		fwd_power = fwd_power*fwd_power*fwd_power;
		turn_power = turn_power * turn_power  * turn_power;
		double leftPower = fwd_power*MAX_ROBOT_VELOCITY;
		double rightPower = fwd_power*MAX_ROBOT_VELOCITY;
		double normalizer = Math.max(Math.abs(fwd_power),Math.abs(turn_power))/(Math.abs(fwd_power) + Math.abs(turn_power));
		if (Math.abs(turn_power)>TURN_THRESHHOLD) {
			rightPower = (fwd_power - turn_power)*MAX_ROBOT_VELOCITY;
			leftPower = (fwd_power + turn_power)*MAX_ROBOT_VELOCITY;
		}

		Robot.drive.setDrive(leftPower, rightPower, ControlMode.Velocity);
		System.out.println(fwd_power + "  " + turn_power + "  " + leftPower + "  " + rightPower);
		// Robot.drive.setDrivePower(leftPower, rightPower);

//		if(m_boxop.getRawButton(WRIST_BACK) ) {
			// user clicked on the wrist back button
//			System.out.println(">>> WRIST_BACK pressed");
//			new ExtendGripper().start();
//		}
//		if(m_boxop.getRawButton(WRIST_VERTICAL) ) {
//			Robot.elevator.setActuatorPower(0.5);
//		} else if (m_boxop.getRawButton(WRIST_DOWN)) {
//			Robot.elevator.setActuatorPower(-0.5);
//		} else {
//			Robot.elevator.setActuatorPower(0.0);
//		}
//		if(m_boxop.getRawButton(MANUAL_ARM_UP) ) {
//			Robot.elevator.setElevatorPower(0.5);
//		} else if (m_boxop.getRawButton(MANUAL_ARM_DOWN)) {
//			Robot.elevator.setElevatorPower(-0.5);
//		} else {
//			Robot.elevator.setElevatorPower(0.0);
//		}
	}
}
