package com.team766.frc2019.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import com.team766.framework.Subroutine;

public class LimeLight {
	private static NetworkTableInstance table = null;

    private static NetworkTableEntry getValue(String key) {
		if (table == null) {
			table = NetworkTableInstance.getDefault();
		}

		return table.getTable("limelight").getEntry(key);
    }
    
	public static boolean isTarget() {
		return getValue("tv").getDouble(0) == 1;
	}

	public static double tx() {
		return getValue("tx").getDouble(0.00);
	}
	
	public static double tshort() {
		return getValue("tshort").getDouble(0.00);
	}

	public static double tlong() {
		return getValue("tlong").getDouble(0.00);
	}
	
	public static double ty() {
		return getValue("ty").getDouble(0.00);
	}
	
	public static double ta() {
		return getValue("ta").getDouble(0.00);
    }
    
    public static double ts() {
        return getValue("ts").getDouble(0.00);
    }

    public static double distanceDumb(double angle) {
		setPipeline(1);
		return 4.8 / Math.tan(((ty() + angle) * Math.PI) / 180.0 );
	}
	
	public static double thor() {
		return getValue("thor").getDouble(0.0);
	}

	public static void setPipeline(int number) {
		getValue("pipeline").setNumber(number);
	}
}