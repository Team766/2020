package com.team766.frc2019;

//import static org.junit.Assume.assumeTrue;

import com.team766.framework.Command;
import com.team766.frc2019.Robot;
import com.team766.frc2019.commands.CalibrateElevator;
import com.team766.frc2019.commands.LimePickup;
import com.team766.frc2019.commands.LimeScore;
import com.team766.frc2019.commands.PreciseTurn;
import com.team766.frc2019.commands.ExtendGripper;
import com.team766.frc2019.commands.RetractGripper;
import com.team766.frc2019.commands.Rocket;
import com.team766.frc2019.commands.TurnInertia;
import com.team766.frc2019.mechanisms.LimeLightI;
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
	private CalibrateElevator m_calibrate = new CalibrateElevator();
	private LimePickup m_limePickup = new LimePickup(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider());
	private LimeScore m_limeScore = new LimeScore(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider());
	private PreciseTurn m_preciseTurn;
	private Rocket m_rocket = new Rocket(Robot.drive, Robot.limeLight, RobotProvider.getTimeProvider());


	private static int INTAKE_ACTUATE = 2;
	private static int INTAKE_RETRACT = 1;
	private static int INTAKE_IN = 3;
	private static int INTAKE_OUT = 4;
	private static int ELEVATOR_UP = 10;
	private static int ELEVATOR_DOWN = 9;
	private static int UPPER_UP = 21;
	private static int UPPER_DOWN = 20;
	private static int LOWER_UP = 19;
	private static int LOWER_DOWN = 18;
	private static int ADD_SMALL = 6;
	private static int SUBTRACT_SMALL = 5;
	private static int ADD_BIG = 8;
	private static int SUBTRACT_BIG = 7;
	private static int CALI_SWITCH = 17;
	private static int CALI_BUTTON = 16;
	private static int ELEVATOR_LVL3 = 13;
	private static int ELEVATOR_LVL2 = 14;
	private static int ELEVATOR_LVL1 = 15;
	
	private double fwd_power = 0;
	private double turn_power = 0;
	private double leftPower = 0;
	private double rightPower = 0;

	private int index = 0;

	private static double MIN_ROBOT_VELOCITY = 2000.0;
	private static double MAX_ROBOT_VELOCITY = 20000.0;
	private static double TURN_THRESHOLD = 0.05;

	private boolean addSmallButton = false;
	private boolean subSmallButton = false;
	private boolean addBigButton = false;
	private boolean subBigButton = false;

	public static boolean driverControl = false;

	public double targetPosition = -1; 

	public double combinedPosition;

	public static boolean calibrate;

	public OI() {
		m_joystick1 = RobotProvider.instance.getJoystick(1);
		m_joystick2 = RobotProvider.instance.getJoystick(2);
		m_boxop = RobotProvider.instance.getJoystick(3);		
	}
	
	public void run() {

		//System.out.println("joystick1: " + m_joystick1 + "joystick2: " + m_joystick2);

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
		
		SmartDashboard.putNumber("Forward Power", fwd_power);
		SmartDashboard.putNumber("Turn Power", turn_power);
		SmartDashboard.putNumber("Left Power", leftPower);
		SmartDashboard.putNumber("Right Power", rightPower);
		
		if (index++ > 200) {
			index = 0;
			//System.out.println(" leftPower: " + leftPower + " rightPower: " + rightPower);
		}
		//System.out.println("forward power: " + fwd_power + " turn power: " + turn_power);
		Robot.drive.setDrive(leftPower, rightPower);

		if (index++ % 50 == 0 && Robot.drive.isEnabled()) {
			//System.out.println("lower height: " + Robot.elevator.getLowerHeight());

		}
		index++;

		// SMALL ELEVATOR FORCED MANUAL MOVEMENT
		
		if(m_boxop.getRawButton(CALI_BUTTON) ) {
			//CURRENTLY CALIBRATES
			/*Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setUpperPower(0.5);
			System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
			*/
			if(!m_calibrate.isRunning() && Robot.drive.isEnabled()) {
				Robot.elevator.setCombinedPosition( -1);
				m_calibrate.start();
			}
		}
			/*
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN_SMALL)) {
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setUpperPower(-0.5);
			//Robot.elevator.setLowerPower(-0.5);
			System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
		} else {
			Robot.elevator.upperNeutral();
		}
		*/

		if (m_boxop.getRawButton(23) || m_joystick1.getRawButton(1)) {
			m_limePickup.start();
		}

		if (m_joystick1.getRawButton(3)) {
			m_rocket.start();
		}

		if (m_boxop.getRawButton(22) || m_joystick1.getRawButton(2)) {
			m_limeScore.start();
		}

		
		if (m_joystick1.getRawButton(6)) {
			if (!PreciseTurn.turning) {
				m_preciseTurn = new PreciseTurn((Robot.drive.getGyroAngle() - 180)% 360);
				m_preciseTurn.start();
			} else {
				return;
			}
		}
/*
		if (m_joystick1.getRawButton(5)) {
			if (!PreciseTurn.turning) {
				m_preciseTurn = new PreciseTurn((Robot.drive.getGyroAngle() + 180)% 360);
				m_preciseTurn.start();
			}
		}
*/

		// BIG ELEVATOR FORCED MANUAL MOVEMENT
		if(m_boxop.getRawButton(LOWER_UP) ) {
			Robot.elevator.setCombinedPosition( -1);
			Robot.elevator.lowerStopTargeting = true;
			Robot.elevator.setLowerPower(0.5);
		//	System.out.println("Lower Height: " + Robot.elevator.getLowerHeight());
			if (calibrate) {
				Robot.elevator.resetLowerEncoder();
			//	System.out.println("Calibrated");
			}
			//System.out.println("LOWER POWER IS 0.5");
			//System.out.println("Lower Height: " + Robot.elevator.getLowerHeight());
		} else if (m_boxop.getRawButton(LOWER_DOWN)) {
			Robot.elevator.setCombinedPosition( -1);
			Robot.elevator.lowerStopTargeting = true;
			Robot.elevator.setLowerPower(-0.5);
			//System.out.println("Lower Height: " + Robot.elevator.getLowerHeight());
			if (calibrate) {
				Robot.elevator.resetLowerEncoder();
			//	System.out.println("Calibrated");
			}
			//System.out.println("Lower Height: " + Robot.elevator.getLowerHeight());

		} else {
			Robot.elevator.lowerNeutral();
		}

		//SMALL ELEVATOR FORCED MOVEMENT 
		if(m_boxop.getRawButton(UPPER_UP) ) {
			Robot.elevator.setCombinedPosition( -1);
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setUpperPower(0.9);
			//System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
		//	System.out.println("UPPER POWER IS 0.5");
		//	System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
			if (calibrate) {
				Robot.elevator.resetUpperEncoder();
			//	System.out.println("Calibrated");
			}
		} else if (m_boxop.getRawButton(UPPER_DOWN)) {
			Robot.elevator.setCombinedPosition( -1);
			Robot.elevator.upperStopTargeting = true;
			Robot.elevator.setUpperPower(-0.9);
			//System.out.println("Upper Height: " + Robot.elevator.getUpperHeight());
			if (calibrate) {
				Robot.elevator.resetUpperEncoder();
				//System.out.println("Calibrated");
			}
		} else {
			Robot.elevator.upperNeutral();
		}
		
		if (m_boxop.getRawButton(ADD_SMALL)) {
			if (!addSmallButton) {
				addSmallButton = true;
				Robot.elevator.setCombinedPosition( Robot.elevator.getTargetPosition() + 100000);
			}
		} else {
			addSmallButton = false;
		}

		if (m_boxop.getRawButton(SUBTRACT_SMALL)) {
			if (!subSmallButton) {
				subSmallButton = true;
				Robot.elevator.setCombinedPosition( Robot.elevator.getTargetPosition() - 30000);
			}
		} else {
			subSmallButton = false;
		}

		if (m_boxop.getRawButton(ADD_BIG)) {
			if (!addBigButton) {
				addBigButton = true;
				Robot.elevator.setCombinedPosition( Robot.elevator.getTargetPosition() + 400000);
			}
		} else {
			addBigButton = false;
		}

		if (m_boxop.getRawButton(SUBTRACT_BIG)) {
			if (!subBigButton) {
				subBigButton = true;
				Robot.elevator.setCombinedPosition( Robot.elevator.getTargetPosition() - 400000);
			}
		} else {
			subBigButton = false;
		}


		if (m_boxop.getRawButton(CALI_SWITCH)) {
			calibrate = true;
			//System.out.println("Calibrated");
		} else {
			calibrate = false;
			//System.out.println("No longer calibrating");
		}

		// COMBINED ELEVATOR MOVEMENT ALGORITHM
		if (m_boxop.getRawButton(ELEVATOR_UP) ) {
			Robot.elevator.setCombinedPosition(-1);
			Robot.elevator.elevatorUp(); 
		} else if (m_boxop.getRawButton(ELEVATOR_DOWN)) {
			Robot.elevator.setCombinedPosition(-1);
			Robot.elevator.elevatorDown();
			if (calibrate) {
				Robot.elevator.resetUpperEncoder();
				Robot.elevator.resetLowerEncoder();
			//	System.out.println("Calibrated");
			}
		} else {
			Robot.elevator.elevatorNeutral();
		}
	
		// INTAKE FORWARD AND BACK
		if (m_boxop.getRawButton(INTAKE_ACTUATE)) {
			System.out.println("actuate");
			Robot.flowerActuator.extend();
		} else if (m_boxop.getRawButton(INTAKE_RETRACT)) {
			System.out.println("retract");
			Robot.flowerActuator.retract();
		}

		// GRIPPER ACTIONS
		if(m_boxop.getRawButton(INTAKE_IN) || m_joystick2.getRawButton(2)) {
			// user clicked on the intake in button
		//	System.out.println(">>> INTAKE_OPEN pressed");
			new RetractGripper().start();
		}

		if(m_boxop.getRawButton(INTAKE_OUT) || m_joystick2.getRawButton(1)) {
			// user clicked on the intake out button  
			new ExtendGripper().start();  
		}

		// ELEVATOR LEVELS
		int tgtLvl = m_boxop.getRawButton(ELEVATOR_LVL1) ? 1 : 
					m_boxop.getRawButton(ELEVATOR_LVL2) ? 2 :
					m_boxop.getRawButton(ELEVATOR_LVL3) ? 3 : -1;
		switch( tgtLvl ){
			case 1:
			Robot.elevator.setCombinedPosition(Robot.elevator.LVL1);
				break;
			case 2:
			Robot.elevator.setCombinedPosition(Robot.elevator.LVL2);
				break;
			case 3:
			Robot.elevator.setCombinedPosition(Robot.elevator.LVL3);
				break;
		}
		
		if (!Robot.drive.isEnabled()) {
			Robot.drive.nukeRobot();
			m_calibrate.stop();
			return;
		}

		if (index % 30 == 0) {
			//System.out.println("Target Position: " + targetPosition);
		}
		Robot.elevator.movePosition(); 
	}
}

	
