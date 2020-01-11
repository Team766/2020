package com.team766.frc2019;

import com.team766.framework.AutonomousCommandUtils;
import com.team766.framework.Command;
import com.team766.frc2019.mechanisms.Drive;
import com.team766.frc2019.mechanisms.Elevator;
import com.team766.frc2019.mechanisms.FlowerActuator;
import com.team766.frc2019.mechanisms.FlowerGripper;
import com.team766.frc2019.mechanisms.LimeLight;
// import com.team766.frc2019.mechanisms.CoprocessorCommunicator;
import com.team766.hal.MyRobot;
import com.team766.hal.mock.Gyro;
import com.team766.web.AutonomousSelector;
import com.team766.web.ConfigUI;
import com.team766.web.LogViewer;
import com.team766.web.WebServer;
public class Robot extends MyRobot {
	// Declare mechanisms here
	public static Drive drive;
	public static FlowerGripper flowerGripper;
	public static FlowerActuator flowerActuator;
	public static Elevator elevator;
	public static LimeLight limeLight;
	// public static CoprocessorCommunicator coprocessorCommunicator;

	public static OI m_oi;
	
	private WebServer m_webServer;
	private AutonomousSelector m_autonSelector;
	private Command m_autonomous;
	
	@Override
	public void robotInit() {
		// Initialize mechanisms here
		drive = new Drive();
		flowerGripper = new FlowerGripper();
		elevator = new Elevator();
		flowerActuator = new FlowerActuator();
		limeLight = new LimeLight();
		// coprocessorCommunicator = new CoprocessorCommunicator("10.7.66.2");
		// try {
		// 	coprocessorCommunicator.get();
		// } catch(Exception error) {

		// }
	
		//auton picker
		m_webServer = new WebServer();
		m_webServer.addHandler("/config", new ConfigUI());
		m_webServer.addHandler("/logs", new LogViewer());
		m_autonSelector = new AutonomousSelector(AutonomousModes.class);
		m_webServer.addHandler("/values", m_autonSelector);
		m_webServer.start();
	}
	
	@Override
	//initializes auton
	public void autonomousInit() {
		if (m_autonomous != null) {
			m_autonomous.stop();
		}
		if (m_oi != null) {
			m_oi.stop();
		}
		
		m_autonomous = AutonomousCommandUtils.getCommand(m_autonSelector.getSelectedAutonMode(AutonomousModes.class));
		m_autonomous.start();
	}
	
	@Override
	//initializes teleop
	public void teleopInit() {
		if (m_autonomous != null) {
			m_autonomous.stop();
		}
		
		if (m_oi == null) {
			m_oi = new OI();
		}
		m_oi.start();
	}
}
