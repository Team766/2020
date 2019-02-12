package com.team766.frc2019.commands;
import com.team766.framework.Subroutine;


public class FindAngle extends Subroutine {
	public double compute(double h1, double h2, double distance, int pipeline) {
		LimeLight.setPipeline(pipeline);
		waitForSeconds(1.0);
		return (Math.atan((h2 - h1) / distance) - LimeLight.ty());
	}
	protected void subroutine() {
		System.out.println(LimeLight.distanceDumb(9.01862));
	}
}