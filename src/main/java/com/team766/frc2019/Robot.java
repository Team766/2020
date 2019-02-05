package com.team766.frc2019;

import com.team766.framework.AutonomousCommandUtils;
import com.team766.framework.Command;
import com.team766.frc2019.mechanisms.Drive;
import com.team766.frc2019.mechanisms.FlowerActuator;
import com.team766.frc2019.mechanisms.FlowerGripper;
import com.team766.hal.MyRobot;
import com.team766.web.AutonomousSelector;
import com.team766.web.ConfigUI;
import com.team766.web.LogViewer;
import com.team766.web.WebServer;
import com.team766.frc2019.mechanisms.Drive;
public class Robot extends MyRobot {
	// Declare mechanisms here
	public static Drive drive;
		public static FlowerGripper flowerGripper;
		public static FlowerActuator flowerActuator;


	private OI m_oi;
	
	private WebServer m_webServer;
	private AutonomousSelector m_autonSelector;
	private Command m_autonomous;
	
	@Override
	public void robotInit() {
		// Initialize mechanisms here
		drive = new Drive();
		flowerGripper = new FlowerGripper();
//		flowerActuator = new FlowerActuator();
		
		m_webServer = new WebServer();
		m_webServer.addHandler("/config", new ConfigUI());
		m_webServer.addHandler("/logs", new LogViewer());
		m_autonSelector = new AutonomousSelector(AutonomousModes.class);
		m_webServer.addHandler("/values", m_autonSelector);
		m_webServer.start();
	}
	
	@Override
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
