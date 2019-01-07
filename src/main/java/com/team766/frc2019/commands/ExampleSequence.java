package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;

public class ExampleSequence extends Subroutine {
	public ExampleSequence() {
		takeControl(Robot.arm);
	}
	
	protected void subroutine() {
		Robot.arm.setTargetUp();
		waitFor(() -> Robot.arm.atTarget());
		Robot.arm.setTargetDown();
		waitFor(() -> Robot.arm.atTarget());
	}
}