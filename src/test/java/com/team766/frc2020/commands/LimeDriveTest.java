package com.team766.frc2020.commands;

import com.team766.frc2020.TestState;
import com.team766.frc2020.mechanisms.*;
import org.junit.Test;

public class LimeDriveTest {

    @Test
    public void subroutine() {
        TestState testState = new TestState();
        DriveI drive = new DriveIMock( testState );
        LimeLightI limeLight = new LimeLightMock( testState );
        LimePickup limeDrive = new LimePickup(drive, limeLight, () -> System.currentTimeMillis());
        limeDrive.subroutine();
    }
}