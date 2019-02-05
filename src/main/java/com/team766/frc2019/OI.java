package com.team766.frc2019;

import com.team766.framework.Command;
//import com.team766.frc2019.commands.ExtendGripper;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI extends Command {
	private JoystickReader m_joystick1;
	private JoystickReader m_joystick2;
	private JoystickReader m_boxop;

	//private static int WRIST_BACK = 7;
	//private static int WRIST_VERTICAL = 10;
	//private static int WRIST_DOWN = 9;
	private static int EXTEND_ACTUATOR = 3;
	private static int RETRACT_ACTUATOR = 5;
	private static int EXTEND_FLOWER = 2;
	private static int RETRACT_FLOWER = 4;
	
	public OI() {
		m_joystick1 = RobotProvider.instance.getJoystick(1);
		m_joystick2 = RobotProvider.instance.getJoystick(2);
		m_boxop = RobotProvider.instance.getJoystick(3);		
	}
	
	public void run() {
		double leftValue = Math.pow(m_joystick1.getRawAxis(0), 3)*-1;
		double rightValue = Math.pow(m_joystick2.getRawAxis(1), 3)*-1;

		double targetPower = Math.max(Math.abs(leftValue), Math.abs(rightValue));

		double forwardPower = rightValue;
		
		double leftTurnPower = leftValue;
		double rightTurnPower = rightValue*-1;

		double leftRawPower = forwardPower + leftTurnPower;
		double rightRawPower = forwardPower + rightTurnPower;

		double normalizingFactor = Math.max(leftRawPower, rightRawPower);
		double leftPower = leftRawPower / normalizingFactor;
		double rightPower = rightRawPower / normalizingFactor;

		rightPower *= targetPower;
		leftPower *= targetPower;

		Robot.drive.setDrivePower(leftPower, rightPower);



		
/*		if(m_boxop.getRawButton(WRIST_BACK) ) {
			// user clicked on the wrist back button
			System.out.println(">>> WRIST_BACK pressed");
			new ExtendGripper().start();
			System.out.println("I am coding shmoding");
		} */

		if(m_boxop.getRawButton(EXTEND_FLOWER)) {
			Robot.flowerGripper.extend();
		}

		if(m_boxop.getRawButton(RETRACT_FLOWER)) {
			Robot.flowerGripper.retract();
		}

		if(m_boxop.getRawButton(EXTEND_ACTUATOR)) {
			Robot.flowerActuator.extend();
		}

		if(m_boxop.getRawButton(RETRACT_ACTUATOR)) {
			Robot.flowerActuator.retract();
		}
	}
}