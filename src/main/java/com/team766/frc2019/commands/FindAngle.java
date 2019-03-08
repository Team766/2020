package com.team766.frc2019.commands;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;


public class FindAngle extends Subroutine {
	protected void subroutine() {
		System.out.println(Robot.limeLight.distanceDumb(9.01862));
	}

	public double distanceToTarget(int pipeline, double angleToTarget) {
		Robot.limeLight.setPipeline(pipeline);
		waitForSeconds(1.0);
		return (Robot.drive.hatchHeight - Robot.drive.mountingHeight) / Math.tan(Robot.drive.mountingAngle + angleToTarget);
	}
}