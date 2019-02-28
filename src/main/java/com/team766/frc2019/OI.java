package com.team766.frc2019;

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

		/* 
		*  THE LOWER ELEVATOR MAINTAINS MOMENTUM AFTER LETTING GO OF THE ELEVATOR DOWN BUTTON,
		*  BUT THE hover(); METHOD MAKES IT MOVE BACK UP TO WHERE IT WAS WHEN THE BUTTON WAS RELEASED.
		*  THIS MAKES IT VERY DIFFICULT TO ACCURATELY LOWER THE LOWER ELEVATOR WITH QUICK, SUCCESSIVE TAPS ON THE BUTTON.
		*  TO RESOLVE THIS ISSUE WHILE STILL MAKING SURE THE LOWER ELEVATOR NEVER DRIFTS DOWN FROM ITS WEIGHT DUE TO GRAVITY,
		*  WE CAN TRY MAKING IT CONSTANTLY UPDATE ITS DESTINATION TO ITS CURRENT POSITION IF IT IS NOT TARGETTING ANYTHING.
		*  
		*  Unfortunately, if we plug in a command to constantly do this, it will interfere with the sendToDestination methods
		*  and the manual elevator buttons, so we have to make any action interrupt this process. I'm adding a hovering boolean
		*  to set true whenever a button is pressed.
		*
		*/

		// Since run() is constantly refreshing, hover will always be updating to set position at the new current position.
		// This should resolve the issue with it drifting downwards and then going back up to compensate.
		if (Robot.elevator.hovering) {
			Robot.elevator.hover();
		}

		/* 
		*  Print limit switch values
		*  System.out.println("LowerMinLimSwitch: " + Robot.elevator.getLowerMinLimitSwitch());
		*  System.out.println("LowerMaxLimSwitch: " + Robot.elevator.getLowerMaxLimitSwitch());
		*  System.out.println("UpperMinLimSwitch: " + Robot.elevator.getUpperMinLimitSwitch());
		*  System.out.println("UpperMaxLimSwitch: " + Robot.elevator.getUpperMaxLimitSwitch());
		*/

		// Prints every 2000 ticks so it doesnt spam the log
		if (index++ % 2000 == 0 && Robot.drive.isEnabled()) {
			System.out.println("LowerHeight: " + Robot.elevator.getLowerHeight() + " UpperHeight: " + Robot.elevator.getUpperHeight());
		}


		// cheezy - right stick fwd/back - left stick lft/rgt
		double fwd_power = Math.pow(-(1.1)*m_joystick1.getRawAxis(1), 1);
		double turn_power = Math.pow((0.5)*m_joystick2.getRawAxis(0), 1);
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
		double leftPower = (fwd_power + turn_power); //* (10000);
		double rightPower = (fwd_power - turn_power);//* (10000);
		//System.out.println("forward power: " + fwd_power + "turn power: " + turn_power);
		Robot.drive.setDrive(leftPower, rightPower, ControlMode.PercentOutput);
		/// End of Ryan's code


		// SMALL ELEVATOR FORCED MANUAL MOVEMENT
		if(m_boxop.getRawButton(ELEVATOR_UP_SMALL) ) {
			/*Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setUpperPower(0.5);
			System.out.println("Upper Height: " + Robot.elevator.getUpperHeight()); */
			if(!m_calibrate.isRunning() && Robot.drive.isEnabled()) {
				m_calibrate.start();
			}
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setUpperPower(-0.5);
			/*Robot.elevator.hovering = false;
			Robot.elevator.setLowerPower(-0.5); */
			System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
		} else {
			Robot.elevator.upperNeutral();
		}
		

		// BIG ELEVATOR FORCED MANUAL MOVEMENT (doesnt really work. we would need to code a Robot.elevator.lowerNeutral(), which we dont have)
		/*if(m_boxop.getRawButton(ELEVATOR_UP_SMALL) ) {
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setLowerPower(0.6);
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setLowerPower(-0.6);
		} else {
			Robot.elevator.setLowerPower(0.0);
			Robot.elevator.upperNeutral();
		}
		*/

		// Replace the manual forced buttons with the addToPosition function (We should try this out sometime!)
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
		if (m_boxop.getRawButton(ELEVATOR_LVL1)) {
			/*if(!m_calibrate.isRunning() && Robot.drive.isEnabled()) {
				m_calibrate.start();
			}*/
			Robot.elevator.sendCombinedToDestination(Robot.elevator.LVL1);
			//Robot.elevator.bothElevatorsDown();
		}

		if (m_boxop.getRawButton(ELEVATOR_LVL2)) {
			Robot.elevator.sendCombinedToDestination(Robot.elevator.LVL2);
		}

		if (m_boxop.getRawButton(ELEVATOR_LVL3)) {
			Robot.elevator.sendCombinedToDestination(Robot.elevator.LVL3);
		}

		/*
		// Yarden's code for LVL buttons

		int tgtLvl = m_boxop.getRawButton(ELEVATOR_LVL1) ? 1 : 
					m_boxop.getRawButton(ELEVATOR_LVL2) ? 2 :
					m_boxop.getRawButton(ELEVATOR_LVL3) ? 3 : -1;

		//System.out.println("==> Going to level: " + tgtLvl );
		switch( tgtLvl ){
			case 1:
//				if(!m_calibrate.isRunning() && Robot.drive.isEnabled()) {
//					m_calibrate.start();
//				}
				
				Robot.elevator.setCombinedPosition(Robot.elevator.LVL1);
				break;
			case 2:
				Robot.elevator.setCombinedPosition(Robot.elevator.LVL2);
				break;
			case 3:
				Robot.elevator.setCombinedPosition(Robot.elevator.LVL3);
				break;
		}
		
		*/
		// When robot is disabled, NUKE ROBOT! and disable calibrate subroutine.
		if (!Robot.drive.isEnabled()) {
			Robot.drive.nukeRobot();
			m_calibrate.stop();
			return;
		}
	}
}
