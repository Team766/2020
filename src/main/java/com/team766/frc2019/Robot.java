package com.team766.frc2019;

import com.team766.framework.AutonomousCommandUtils;
import com.team766.framework.Command;
import com.team766.frc2019.mechanisms.Arm;
import com.team766.frc2019.mechanisms.Drive;
import com.team766.frc2019.mechanisms.Wrist;
import com.team766.hal.MyRobot;
import com.team766.web.AutonomousSelector;
import com.team766.web.ConfigUI;
import com.team766.web.LogViewer;
import com.team766.web.WebServer;

public class Robot extends MyRobot {
	public static Drive drive;
	@Override
	public void robotInit() {
		drive = new Drive();
	}
}
