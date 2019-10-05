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

	}
}

	
