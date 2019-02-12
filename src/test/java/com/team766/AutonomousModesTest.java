package com.team766;

import com.team766.framework.AutonomousCommandUtils;
import com.team766.frc2019.AutonomousModes;

import org.junit.Test;

public class AutonomousModesTest extends junit.framework.TestCase {
	
	/// Test to make sure that commands associated with autonomous modes can
	/// be constructed when requested.
	@Test
	public void testAutonomousModes() {
		for (AutonomousModes mode : AutonomousModes.values()) {
			AutonomousCommandUtils.getCommand(mode);
		}
	}

}
