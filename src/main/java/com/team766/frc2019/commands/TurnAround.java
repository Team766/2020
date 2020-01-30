package com.team766.frc2019.commands;

import java.util.ArrayList;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.frc2019.paths.PathBuilder;
import com.team766.frc2019.paths.PathBuilder.Waypoint;
// import com.team766.frc2019.mechanisms.LimeLightI;
//import com.team766.hal.RobotProvider;
// import com.team766.hal.RobotProvider;

public class TurnAround extends Subroutine {

    public TurnAround() {
        takeControl(Robot.drive);
    }

    protected void subroutine() {
        System.out.println("TurnAround STARTING");

        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
        waypoints.add(new Waypoint(0, 0, 0, 0));
        waypoints.add(new Waypoint(90, 0, 0, 0));
        waypoints.add(new Waypoint(130, -50, 0, 0));

        // System.out.println("Waypoints" + waypoints);

        // System.out.println(PathBuilder.GeneratePath(waypoints));
        ArrayList<Waypoint> path = new ArrayList<Waypoint>();
        path = PathBuilder.buildPath(waypoints);

        System.out.println("path built");

        for (int i = 0; i < path.size(); i++) {
            // System.out.println("Point " + i + ": " + path.get(i).getX() + " " + path.get(i).getY());
            // System.out.println("("  + path.get(i).getX() + ", " + path.get(i).getY() + ")");
            System.out.println("curvature: " + path.get(i).getCurvature());
        }
        

       // callSubroutine(new PreciseTurn((Robot.drive.getGyroAngle() + 180) % 360));
        
    }
}