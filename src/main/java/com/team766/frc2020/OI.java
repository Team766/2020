package com.team766.frc2020;

//import static org.junit.Assume.assumeTrue;

import com.team766.framework.Command;
import com.team766.frc2020.Robot;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;
import com.team766.frc2020.commands.LimeScore;

import com.team766.frc2020.mechanisms.*;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI extends Command {
	private JoystickReader m_joystick1;
	private JoystickReader m_joystick2;
	private JoystickReader m_boxop;
	int index = 0;
	boolean startIndexing = false;

	// Variables for arcade drive
	private double fwd_power = 0;
	private double turn_power = 0;

	private LimeScore m_limeScore;

	public static boolean driverControl = false;

	public OI() {
		m_joystick1 = RobotProvider.instance.getJoystick(1);
		m_joystick2 = RobotProvider.instance.getJoystick(2);
		m_boxop = RobotProvider.instance.getJoystick(3);		
	}
	
	public void run() {

		if (Math.abs(m_joystick1.getRawAxis(1)) < 0.13 ) {
			fwd_power = 0;
		} else {
			fwd_power = -(0.05 + Math.pow(m_joystick1.getRawAxis(1), 3));
		}
		if (Math.abs(m_joystick2.getRawAxis(0)) < 0.13 ) {
			turn_power = 0;
		} else {
			turn_power = (1.3 + Math.pow(m_joystick2.getRawAxis(0), 3));
			turn_power = 0.20 * turn_power;
			if (Math.abs(fwd_power) > 0.5) {
				turn_power = 0.6 * turn_power;
			}
		}

		double normalizer = Math.max(Math.abs(fwd_power),Math.abs(turn_power))/(Math.abs(fwd_power) + Math.abs(turn_power)); // divides both motor powers by the larger one to keep the ratio and keep power at or below 1
		double leftPower = (fwd_power + turn_power);
		double rightPower = (fwd_power - turn_power);
		
		// SmartDashboard.putNumber("Forward Power", fwd_power);
		// SmartDashboard.putNumber("Turn Power", turn_power);
		// SmartDashboard.putNumber("Left Power", leftPower);
		// SmartDashboard.putNumber("Right Power", rightPower);
		
		Robot.drive.setDrive(leftPower, rightPower);

		if (!Robot.drive.isEnabled()) {
			Robot.drive.nukeRobot();
		}

		Robot.drive.setArcadeDrive(-m_joystick1.getRawAxis(1), Math.pow(m_joystick2.getRawAxis(0), 3));
		


		//intake mode
		if(m_joystick1.getRawButton(2)){
			if(!Robot.waterwheel.intakeMode) {
				Robot.waterwheel.setIntakeMode(true);
			}
			//Robot.intake.setIntakeState(true);
			Robot.intake.setIntakePower(0.5);
			Robot.wagon.setWagonPower(0.7);
			if(Robot.lightSensor.getBottomLightSensorState()){
				Robot.waterwheel.setWheelPosition(Robot.waterwheel.getWheelPosition() + 840);
			}
		} else {
			Robot.waterwheel.setIntakeMode(false);
			//Robot.intake.setIntakeState(false);
			Robot.intake.setIntakePower(0);
			Robot.wagon.setWagonPower(0);
		}

		//outtake mode
		if(m_boxop.getRawButton(3)) {
			if (startIndexing) {
				Robot.waterwheel.setPusherState(true);
				index++;
			}
			if (index == 50) {
				Robot.waterwheel.setPusherState(false);
			}
			if (index > 200) {
				index = 0;
				//System.out.println("outtaking truly finished");
				Robot.waterwheel.outtaking = false;
				startIndexing = false;
			}
			Robot.waterwheel.setOuttakeMode(true);
			Robot.outtake.setOuttakePower(m_boxop.getRawAxis(3)/10);
			if (index++ % 10 == 0) {
				System.out.println(m_boxop.getRawAxis(3)/10);
			}
			//System.out.println("waterwheel.outtaking: " + waterwheel.outtaking);
			if (Robot.waterwheel.getWheelPosition() > (Robot.waterwheel.initialWheelPosition + 700)) {
				//System.out.println("outtaking finished");
				startIndexing = true;
			} 
			if (!Robot.waterwheel.outtaking) {
				//System.out.println("got here: " + waterwheel.outtaking);
				Robot.waterwheel.outtakeOneBall();
			}
			//System.out.println("pushed");
			//waterwheel.setPusherState(false);
		} else {
			Robot.outtake.setOuttakePower(0.0);
		}

		//turn one tick
		if (m_boxop.getRawButton(9)) {
			Robot.waterwheel.setWheelPosition(Robot.waterwheel.getWheelPosition() + 840);
		}
		
		//pusher out
		if (m_boxop.getRawButton(10)) {
			System.out.println("setting pushed state");
			Robot.waterwheel.pusherOutAndIn();
		}
			//Robot.waterwheel.setPusherState(false);

		if (m_joystick1.getRawButton(1)) {
			Robot.waterwheel.setWheelPosition(Robot.waterwheel.getWheelPosition() + 840);
		}

		if (m_joystick2.getRawButton(1)) {
			Robot.waterwheel.pusherOutAndIn();
		} 
		
		if (m_joystick2.getRawButton(3)) {
			Robot.outtake.setOuttakePowerDistance(3.0);
			if (!Robot.lightSensor.getTopLightSensorState()) {
				System.out.println("turning by one sector");
				Robot.waterwheel.setWheelPosition(Robot.waterwheel.getWheelPosition() + 840);
			}
			Robot.waterwheel.pusherOutAndIn();
		} else {
			Robot.outtake.stopOuttake();
			Robot.waterwheel.setPusherState(false);
			// get rid of all the other button else conditions so they don't conflict and do weird things
		}

		// if (!m_joystick2.getRawButton(3)) {
		// 	outtakeAllBalls();
		// }
		// if (m_joystick2.getRawButton(4)) {
		// 	m_limeScore = new LimeScore();
		// 	m_limeScore.start();
		//  } 
		// if (m_joystick2.getRawButton(5)) {
		// 	m_limeScore = new LimeScore();
		// 	m_limeScore.stop();
		//  }
		// if (!Robot.drive.isEnabled()) {
		// 	Robot.drive.nukeRobot();
		// 	return;
		if (m_joystick2.getRawButton(4)) {
			Robot.waterwheel.setInitialWaterwheelPosition();
			Robot.outtake.setOuttakePowerDistance(3.0);
		}
		
		//climber
		if (m_boxop.getRawButton(2)) {
			Robot.climber.setClimberUp(true);
		} else {
			Robot.climber.setClimberUp(false);
		}
	}

	// public void outtakeAllBalls() {
	// 	// when limelight works, get the distance here
	// 	for (int i = 0; i < 5; i++) {
	// 		// Robot.waterwheel.turnDegrees(840);
	// 		// Robot.waterwheel.setWheelPosition(840 * i);
	// 		// Robot.outtake.continuousDistanceShoot();
	// 		// Robot.outtake.setOuttakePowerDistance(2.0);
	// 		// Robot.waterwheel.pusherOutAndIn();
	// 	}
	// 	// Robot.outtake.setOuttakePower(0);
	// 	}
	// }
}
