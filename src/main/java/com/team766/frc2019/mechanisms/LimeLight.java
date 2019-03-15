package com.team766.frc2019.mechanisms;

import com.team766.frc2019.Robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimeLight implements LimeLightI {
	private static NetworkTableInstance table = null;

    private NetworkTableEntry getValue(String key) {
		if (table == null) {
			table = NetworkTableInstance.getDefault();
		}

		return table.getTable("limelight").getEntry(key);
	}
	
	//if everything goes wrong for some unexplainable reason define the 
	//tv tx and ta and all that before hand like the documentation online does
    
	@Override
	public boolean isTarget() {
		return getValue("tv").getDouble(0) == 1;
	}

	@Override
	public double tx() {
		return getValue("tx").getDouble(0.00);
	}
	
	@Override
	public double tshort() {
		return getValue("tshort").getDouble(0.00);
	}

	@Override
	public double tlong() {
		return getValue("tlong").getDouble(0.00);
	}
	
	@Override
	public double ty() {
		return getValue("ty").getDouble(0.00);
	}
	
	@Override
	public double ta() {
		return getValue("ta").getDouble(0.00);
    }
    
    @Override
	public double ts() {
        return getValue("ts").getDouble(0.00);
    }

    @Override
	public double distanceDumb(double angle) {
		setPipeline(1);
		return 4.8 / Math.tan(((ty() + angle) * Math.PI) / 180.0 );
	}

	public static double thor() {
		return Robot.limeLight.getValue("thor").getDouble(0.0);
	}

	public static void setPipeline(int number) {
		Robot.limeLight.getValue("pipeline").setNumber(number);
	}

	/**
	* Returns the distance to the target based on the LimeLight's reading.
	* @param pipeline
	* just set this to 1 lol idk what it does
	* @param angleToTarget
	* the angle between the camera and target, just pass in ty()
	*/
	@Override
	public double distanceToTarget(int pipeline, double angleToTarget) {
		Robot.limeLight.setPipeline(pipeline);
		//should probably wait here but idk how to
		return (Robot.drive.hatchHeight - Robot.drive.mountingHeight) / Math.tan(Robot.drive.mountingAngle + angleToTarget);
	}
}