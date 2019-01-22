package com.team766.frc2019;

import com.team766.framework.Command;
import com.team766.frc2019.commands.ExtendGripper;
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

	private static int WRIST_BACK = 7;
	private static int WRIST_VERTICAL = 10;
	private static int WRIST_DOWN = 9;
	private static int MANUAL_ARM_UP = 2;
	private static int MANUAL_ARM_DOWN = 4;
	private static int MANUAL_WRIST_UP = 1;
	private static int MANUAL_WRIST_DOWN = 3;


	public OI() {
		m_joystick1 = RobotProvider.instance.getJoystick(1);
		m_joystick2 = RobotProvider.instance.getJoystick(2);
		m_boxop = RobotProvider.instance.getJoystick(3);		
	}
	
	public void run() {
		 double leftPower = m_joystick1.getRawAxis(2);
		 double rightPower = m_joystick2.getRawAxis(2);

		// Robot.drive.setDrivePower(leftPower, rightPower);

		if(m_boxop.getRawButton(WRIST_BACK) ) {
			// user clicked on the wrist back button
			System.out.println(">>> WRIST_BACK pressed");
			new ExtendGripper().start();
		}
	}
}
