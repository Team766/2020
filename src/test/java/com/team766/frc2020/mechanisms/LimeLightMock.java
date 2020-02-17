package com.team766.frc2020.mechanisms;

import com.team766.frc2020.TestState;

public class LimeLightMock implements LimeLightI {

    final TestState testState;

    public LimeLightMock(TestState testState) {
        this.testState = testState;
    }

    @Override
    public boolean isTarget() {
        return false;
    }

    @Override
    public double tx() {
        testState.tick();
        return testState.tx;
    }

    @Override
    public double tshort() {
        return 0;
    }

    @Override
    public double tlong() {
        return 0;
    }

    @Override
    public double ty() {
        testState.tick();
        return testState.ty;
    }

    @Override
    public double ta() {
        return 0;
    }

    @Override
    public double ts() {
        return 0;
    }

    @Override
    public double distanceDumb(double angle) {
        return 0;
    }

    @Override
    public double distanceToTarget(int pipeline, double angleToTarget) {
        return 0;
    }
}
