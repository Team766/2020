package com.team766.frc2019.commands;

import com.team766.frc2019.TestState;
import com.team766.frc2019.mechanisms.*;
import org.junit.Test;

public class LimeDriveTest {

    @Test
    public void subroutine() {
        TestState testState = new TestState();
        DriveI drive = new DriveIMock( testState );
        LimeLightI limeLight = new LimeLightMock( testState );
        LimeDrive limeDrive = new LimeDrive(drive, limeLight, () -> System.currentTimeMillis());
        limeDrive.subroutine();
    }
}