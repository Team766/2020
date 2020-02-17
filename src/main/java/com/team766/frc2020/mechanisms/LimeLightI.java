package com.team766.frc2020.mechanisms;

import com.team766.frc2020.Robot;

public interface LimeLightI {
    boolean isTarget();

    double tx();

    double tshort();

    double tlong();

    double ty();

    double ta();

    double ts();

    double distanceDumb(double angle);

    double distanceToTarget(int pipeline, double angleToTarget);
}
