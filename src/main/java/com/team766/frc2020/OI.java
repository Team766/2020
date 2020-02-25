package com.team766.frc2020;

//import static org.junit.Assume.assumeTrue;

import com.team766.framework.Command;
import com.team766.frc2020.Robot;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;

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

	private double fwd_power = 0;
	private double turn_power = 0;

	public static boolean driverControl = false;

	private double totalForward = (Robot.drive.leftSensorBasePosition + Robot.drive.rightSensorBasePosition) / 2;
	private double totalTheta = Robot.drive.getGyroAngle();
	private double oldTotalForward = 0;
	private double oldTotalTheta = 0;

	public OI() {
		m_joystick1 = RobotProvider.instance.getJoystick(1);
		m_joystick2 = RobotProvider.instance.getJoystick(2);
		m_boxop = RobotProvider.instance.getJoystick(3);		
	}
	
	public void run() {

		if (Math.abs(m_joystick1.getRawAxis(1)) < 0.13 ) {
			fwd_power = 0;
		} else {
			fwd_power = -(0.05*(Math.abs(m_joystick1.getRawAxis(1))/m_joystick1.getRawAxis(1)) + Math.pow(m_joystick1.getRawAxis(1), 3));
		}
		if (Math.abs(m_joystick2.getRawAxis(0)) < 0.13 ) {
			turn_power = 0;
		} else {
			turn_power = (Math.abs(m_joystick2.getRawAxis(0))*1.3/m_joystick2.getRawAxis(0)) + Math.pow(m_joystick2.getRawAxis(0), 3);
			turn_power = 0.20 * turn_power;
			if (Math.abs(fwd_power) > 0.5) {
				turn_power = 0.6 * turn_power;
			}
		}

		double normalizer = Math.max(Math.abs(fwd_power),Math.abs(turn_power))/(Math.abs(fwd_power) + Math.abs(turn_power)); // divides both motor powers by the larger one to keep the ratio and keep power at or below 1
		double leftPower = (fwd_power + turn_power);
		double rightPower = (fwd_power - turn_power);
		
		//SmartDashboard.putNumber("Forward Power", fwd_power);
		//SmartDashboard.putNumber("Turn Power", turn_power);
		//SmartDashboard.putNumber("Left Power", leftPower);
		//SmartDashboard.putNumber("Right Power", rightPower);
		
		Robot.drive.setDrive(leftPower, rightPower);
		
		// if (m_boxop.getRawButton(5)) {
        //     Robot.climber.setClimberUpState(false);
        //     Robot.climber.setClimberDownState(true);
		// } else if (m_boxop.getRawButton(6)) {
        //     Robot.climber.setClimberUpState(true);
        //     Robot.climber.setClimberDownState(false);
		// }

		// if (m_boxop.getRawButton(7)) {
		// 	Robot.climber.setShifterPower(0);
		// } else if (m_boxop.getRawButton(8)) {
		// 	Robot.climber.setShifterPower(0.5);
		// }

		// if (m_boxop.getRawButton(9)) {
		// 	Robot.climber.setWinchPower(0);
		// } else if (m_boxop.getRawButton(10)) {
		// 	Robot.climber.setWinchPower(0.5);
		// }

		// if (m_boxop.getRawButton(3)) {
		// 	Robot.intake.setIntakePower(0);
		// } else if (m_boxop.getRawButton(4)) {
		// 	Robot.intake.setIntakePower(0.5);
		// }
		
		// if (m_boxop.getRawButton(1)) {
		// 	Robot.intake.setIntakeState(false);
		// } else if (m_boxop.getRawButton(2)) {
		// 	Robot.intake.setIntakeState(true);
		// }

		// if (m_joystick1.getRawButton(1)) {
		// 	Robot.outtake.setOuttakePower(0.5);
		// } else {
		// 	Robot.outtake.setOuttakePower(0);
		// }

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

		// if (m_joystick2.getRawButton(1)) {
		// 	Robot.waterwheel.setPusherState(true);
		// } else {
		// 	Robot.waterwheel.setPusherState(false);
		// }

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

		oldTotalForward = totalForward;
		oldTotalTheta = totalTheta;

		totalForward = ((Robot.drive.leftEncoderDistance() + Robot.drive.rightEncoderDistance()) * Robot.drive.DIST_PER_PULSE) / 2;
		totalTheta = Robot.drive.getGyroAngle();

		double deltaForward = totalForward - oldTotalForward;
		double deltaTheta = totalTheta - oldTotalTheta;

		Robot.pathWebSocketServer.broadcastDeltas(deltaForward, deltaTheta);
	}
}

	
