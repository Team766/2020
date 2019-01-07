package com.team766.frc2019;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class SimConfig {
    public static final String SIM_CONFIG = String.join("\n",
        "logFilePath,sim_robot_logs",
        "drive.leftMotor,6",
        "drive.rightMotor,4",
        "drive.leftEncoder,0,1",
        "drive.rightEncoder,2,3",
        "leftJoy,0",
        "rightJoy,1",
        "buttonJoy,2",
        "gripperLeft,0",
        "gripperRight,1",
        "gripperMotorA,10",
        "gripperMotorB,11",
        "arm.motor,8",
        "wrist.motor,9",
        "climber,10",
        "arm.encoder,4,5",
        "wrist.encoder,6,7",
        "rightShifter,1",
        "leftShifter,2",
        "drive.gyro,12",
        "",
        "arm.wristLockout.minPosition,0",
        "arm.wristLockout.maxPosition,0",
        "arm.wristLockout.minCommand,0",
        "arm.wristLockout.maxCommand,0",
        "wrist.armLockout.minPosition,0",
        "wrist.armLockout.maxPosition,0",
        "wrist.armLockout.minCommand,0",
        "wrist.armLockout.maxCommand,0",
        "arm.pGain,0",
        "arm.iGain,0",
        "arm.dGain,0",
        "arm.threshold,0",
        "wrist.pGain,0",
        "wrist.iGain,0",
        "wrist.dGain,0",
        "wrist.threshold,0",
        "arm.upPosition,0",
        "arm.middlePosition,0");
    
    public static String getSimConfigFile() {
        try {
            File f = File.createTempFile("sim_config", ".txt");
            f.deleteOnExit();
            try (PrintWriter out = new PrintWriter(f)) {
                out.println(SIM_CONFIG);
            }
            return f.getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
		}
    }
}