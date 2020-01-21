package com.team766.frc2019.commands;

import java.util.ArrayList;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.frc2019.paths.PathBuilder;
import com.team766.frc2019.paths.PathBuilder.Waypoint;
import com.team766.frc2019.mechanisms.LimeLightI;
//import com.team766.hal.RobotProvider;
import com.team766.hal.RobotProvider;

public class TurnAround extends Subroutine {

    public TurnAround() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("TurnAround STARTING");

        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(116, 209, 0, 0));
        sWaypoints.add(new Waypoint(105, 225, 0, 30));
        sWaypoints.add(new Waypoint(5, 2250, 0, 30));
        sWaypoints.add(new Waypoint(101, 235, 0, 60));

        System.out.println("Waypoints" + sWaypoints);

        System.out.println(PathBuilder.GeneratePath(sWaypoints));

       // callSubroutine(new PreciseTurn((Robot.drive.getGyroAngle() + 180) % 360));
    }
}