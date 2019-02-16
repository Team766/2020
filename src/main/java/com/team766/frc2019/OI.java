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
	private static int INTAKE_OPEN = 4;
	private static int INTAKE_CLOSE = 5;
	private static int ELEVATOR_UP = 9;
	private static int ELEVATOR_DOWN = 10;

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
		Robot.drive.setDrive(leftPower, rightPower, ControlMode.PercentOutput);
		/// End of Ryan's code

		if(m_boxop.getRawButton(INTAKE_OPEN) ) {
			// user clicked on the intake open button
			System.out.println(">>> INTAKE_OPEN pressed");
			new RetractGripper().start();
		}
		if(m_boxop.getRawButton(INTAKE_CLOSE) ) {
			// user clicked on the intake close button
			System.out.println(">>> INTAKE_CLOSE pressed");
			new ExtendGripper().start();
		}
		if(m_boxop.getRawButton(ELEVATOR_UP) ) {
			Robot.elevator.setElevatorPower(0.5);
			Robot.elevator.setActuatorPower(0.5);
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN)) {
			Robot.elevator.setElevatorPower(-0.5);
			Robot.elevator.setActuatorPower(-0.5);
		} else {
			Robot.elevator.setElevatorPower(0.0);
			Robot.elevator.setActuatorPower(0.0);
		}
	}
}
