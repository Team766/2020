package com.team766.frc2019;

//import static org.junit.Assume.assumeTrue;

import com.team766.framework.Command;
import com.team766.frc2019.Robot;
import com.team766.frc2019.commands.PreciseTurn;
import com.team766.frc2019.mechanisms.CoprocessorCommunicator;
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
	private PreciseTurn m_preciseTurn;

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
		CoprocessorCommunicator coprocessorCommunicator = new CoprocessorCommunicator("10.7.66.2");
		try {
			coprocessorCommunicator.get();
		} catch(Exception error) {

		}
		
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

		
		if (m_joystick1.getRawButton(6)) {
			if (!PreciseTurn.turning) {
				m_preciseTurn = new PreciseTurn((Robot.drive.getGyroAngle() - 180)% 360);
				m_preciseTurn.start();
			} else {
				return;
			}
		}
	}
}

	
