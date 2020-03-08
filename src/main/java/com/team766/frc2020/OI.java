package com.team766.frc2020;

//import static org.junit.Assume.assumeTrue;

import com.team766.framework.Command;
import com.team766.frc2020.Robot;
import com.team766.frc2020.mechanisms.WaterWheel;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;

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
	WaterWheel waterWheel = new WaterWheel();
	int index = 0;

	// Variables for arcade drive
	private double fwd_power = 0;
	private double turn_power = 0;

	public static boolean driverControl = false;

	public OI() {
		m_joystick1 = RobotProvider.instance.getJoystick(1);
		m_joystick2 = RobotProvider.instance.getJoystick(2);
		m_boxop = RobotProvider.instance.getJoystick(3);		
	}
	
	public void run() {

		// if (Math.abs(m_joystick1.getRawAxis(1)) < 0.13 ) {
		// 	fwd_power = 0;
		// } else {
		// 	fwd_power = -(0.05 + Math.pow(m_joystick1.getRawAxis(1), 3));
		// }
		// if (Math.abs(m_joystick2.getRawAxis(0)) < 0.13 ) {
		// 	turn_power = 0;
		// } else {
		// 	turn_power = (1.3 + Math.pow(m_joystick2.getRawAxis(0), 3));
		// 	turn_power = 0.20 * turn_power;
		// 	if (Math.abs(fwd_power) > 0.5) {
		// 		turn_power = 0.6 * turn_power;
		// 	}
		// }

		// double normalizer = Math.max(Math.abs(fwd_power),Math.abs(turn_power))/(Math.abs(fwd_power) + Math.abs(turn_power)); // divides both motor powers by the larger one to keep the ratio and keep power at or below 1
		// double leftPower = (fwd_power + turn_power);
		// double rightPower = (fwd_power - turn_power);
		
		// // //SmartDashboard.putNumber("Forward Power", fwd_power);
		// // //SmartDashboard.putNumber("Turn Power", turn_power);
		// // //SmartDashboard.putNumber("Left Power", leftPower);
		// // //SmartDashboard.putNumber("Right Power", rightPower);
		
		// Robot.drive.setDrive(leftPower, rightPower);

		Robot.drive.setArcadeDrive(-m_joystick1.getRawAxis(1), Math.pow(m_joystick2.getRawAxis(0), 3));
		
		//climber
		if (m_boxop.getRawButton(1)) {
            Robot.climber.setClimberUpState(false);
            Robot.climber.setClimberDownState(true);
		} else if (m_boxop.getRawButton(2)) {
            Robot.climber.setClimberUpState(true);
            Robot.climber.setClimberDownState(false);
		}

		//intake up and down
		if (m_boxop.getRawButton(6)) {
			Robot.intake.setIntakeState(true);
		} else if (m_boxop.getRawButton(7)) {
			Robot.intake.setIntakeState(false);
		}

	

		//intake mode
		if(m_joystick1.getRawButton(2)){
			if(!waterWheel.intakeMode) {
				waterWheel.setIntakeMode(true);
			}
			//Robot.intake.setIntakeState(true);
			Robot.intake.setIntakePower(0.5);
			Robot.wagon.setWagonPower(0.7);
			if(Robot.lightSensor.getBottomLightSensorState()){
				waterWheel.setWheelPosition(Robot.waterwheel.getWheelPosition() + 840);
			}
		} else {
			waterWheel.setIntakeMode(false);
			//Robot.intake.setIntakeState(false);
			Robot.intake.setIntakePower(0);
			Robot.wagon.setWagonPower(0);
		}

		//outtake mode
		if(m_boxop.getRawButton(3)) {
			waterWheel.setOuttakeMode(true);
			Robot.outtake.setOuttakePower(m_boxop.getRawAxis(3)/10);
			if (index++ % 10 == 0) {
				System.out.println(m_boxop.getRawAxis(3)/10);
			}
			waterWheel.setWheelPosition(Robot.waterwheel.getWheelPosition() + 840);
			waterWheel.setPusherState(true);
			//System.out.println("pushed");
			waterWheel.setPusherState(false);
		} else {
			Robot.outtake.setOuttakePower(0.0);
		}

		//turn one tick
		if (m_boxop.getRawButton(9)) {
			waterWheel.setWheelPosition(Robot.waterwheel.getWheelPosition() + 840);
		}
		
		//pusher out
		if (m_boxop.getRawButton(10)) {
			System.out.println("setting pushed state");
			waterWheel.pusherOutAndIn();
		}
			//waterWheel.setPusherState(false);
		
		// if (m_boxop.getRawButton(1)) {
		// 	Robot.intake.setIntakeState(false);
		// } else if (m_boxop.getRawButton(2)) {
		// 	Robot.intake.setIntakeState(true);
		// }

		if (m_joystick1.getRawButton(1)) {
			waterWheel.setWheelPosition(Robot.waterwheel.getWheelPosition() + 840);
		}


		// if (m_boxop.getRawButton(18)) {
		// 	Robot.spinner.setSpinnerPower(0);
		// } else if (m_boxop.getRawButton(19)) {
		// 	Robot.spinner.setSpinnerPower(0.5);
		// }

		// if (m_boxop.getRawButton(20)) {
		// 	Robot.spinner.setSpinnerState(false);
		// } else if (m_boxop.getRawButton(21)) {
		// 	Robot.spinner.setSpinnerState(true);
		// }

		// if (m_boxop.getRawButton(22)) {
		// 	Robot.wagon.setWagonPower(0.5);
		// } else if (m_boxop.getRawButton(23)) {
		// 	Robot.wagon.setWagonPower(0);
		// }

		if (m_joystick2.getRawButton(1)) {
			Robot.waterwheel.setPusherState(true);
		} else {
			Robot.waterwheel.setPusherState(false);
		}

		// if (m_joystick1.getRawButton(2)) {
		// 	Robot.waterwheel.setWheelPower(-0.1);
		// } else if (m_joystick2.getRawButton(2)) {
		// 	Robot.waterwheel.setWheelPower(0.1);
		// } else {
		// 	Robot.waterwheel.setWheelPower(0);
		// }
		
		if (!Robot.drive.isEnabled()) {
			Robot.drive.nukeRobot();
			return;
		}
		

		if(m_boxop.getRawButton(24)) {
			outtakeAllBalls();
		}
	}

	public void outtakeAllBalls() {
		// when limelight works, get the distance here
		for (int i = 0; i < 5; i++) {
			Robot.waterwheel.turnDegrees(840);
			// Robot.waterwheel.setWheelPosition(840 * i);
			// Robot.outtake.continuousDistanceShoot();
			Robot.outtake.setOuttakePowerDistance(2.0);
			Robot.waterwheel.pusherOutAndIn();
		}
		Robot.outtake.setOuttakePower(0);
		}
	}
	
