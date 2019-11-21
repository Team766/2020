package com.team766.frc2019;

//import static org.junit.Assume.assumeTrue;

import com.team766.framework.Command;
import com.team766.frc2019.Robot;
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

	private static int Spool_Up = 1;
	private static int Spool_Down = 3;
	private static int Vacuum = 2;
	private static int VacuumOff = 1;
	
	private double fwd_power = 0;
	private double turn_power = 0;
	private double leftPower = 0;
	private double rightPower = 0;

	private int index = 0;

	private static double MIN_ROBOT_VELOCITY = 2000.0;
	private static double MAX_ROBOT_VELOCITY = 20000.0;
	private static double TURN_THRESHOLD = 0.05;

	private boolean addSmallButton = false;
	private boolean addBigButton = false;
	private boolean subBigButton = false;

	public static boolean driverControl = false;

	public double targetPosition = -1; 

	public double combinedPosition;

	public static boolean calibrate;

	public OI() {
		// This allows the computer to recognize and act on the inputs from the joysticks. 
		// We mainly use axes 0, 1, 2 which range from -1 to 1 for TeleOp
		m_joystick1 = RobotProvider.instance.getJoystick(1);
		m_joystick2 = RobotProvider.instance.getJoystick(2);
		m_boxop = RobotProvider.instance.getJoystick(3);		
	}
	
	public void run() {
		// write your function here
		Robot.drive.setDrivePower(-m_joystick1.getRawAxis(1), -m_joystick2.getRawAxis(1));
		if(m_joystick1.getRawButton(Spool_Up)){
			Robot.drive.setSpoolPower(.3);
		}
		if(m_joystick1.getRawButton(Spool_Down)){
			Robot.drive.setSpoolPower(-.3);
		}
		if(!m_joystick1.getRawButton(Spool_Up) && !m_joystick1.getRawButton(Spool_Down)){	
			Robot.drive.setSpoolPower(0);
		}
		if(m_joystick2.getRawButton(Vacuum)){
			Robot.drive.setVacuumPower(1);

		}
		if(m_joystick2.getRawButton(VacuumOff)){
			Robot.drive.setVacuumPower(0);

		}
	}
}

	
