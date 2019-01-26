package com.team766.frc2019.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;


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

    public static double distanceDumb() {
		setPipeline(1);
		return 4.8 / Math.tan(((ty() + 9.3625) * Math.PI) / 180.0 );
	}
	
	public static double thor() {
		return getValue("thor").getDouble(0.0);
	}

	public static double skew() {
		setPipeline(2);
		double l = ta();
		setPipeline(3);
		double r = ta();
		return 90.0 * ((l > r) ? r/l : (-1.0 * l)/r);
	}

	public static double ra() {
		setPipeline(3);
		double x = ta();
		return x;
	}

	public static double la() {
		setPipeline(2);
		double x = ta();
		return x;
	}
	public static void setPipeline(int number) {
		getValue("pipeline").setNumber(number);
	}
}