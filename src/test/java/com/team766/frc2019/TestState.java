package com.team766.frc2019;


public class TestState {
    public double ty = -5;
    public double tx = 0;

    public void tick() {

        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
        }
        ty += 0.5;
        tx -= 1;
    }
}

