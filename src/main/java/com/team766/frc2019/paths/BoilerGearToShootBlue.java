// package com.team766.frc2019.Autonomous;

// import com.team766.frc2019.Autonomous.GeneratePath.Waypoint;
// import com.team766.lib.util.control.Path;
// import com.team766.lib.util.math.RigidTransform2d;
// import com.team766.lib.util.math.Rotation2d;
// import com.team766.lib.util.math.Translation2d;

// import java.util.ArrayList;

// /**
//  * Path from the blue boiler side peg to the blue boiler.
//  * 
//  * Used in BoilerGearThenShootModeBlue
//  * 
//  * @see BoilerGearThenShootModeBlue
//  * @see PathContainer
//  */
// public class BoilerGearToShootBlue implements PathContainer {

//     @Override
//     public Path buildPath() {
//         ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
//         sWaypoints.add(new Waypoint(116, 209, 0, 0));
//         sWaypoints.add(new Waypoint(101, 235, 0, 60));

//         System.out.println("Waypoints" + sWaypoints);

//         return GeneratePath.GeneratePath(sWaypoints);
//     }

//     @Override
//     public RigidTransform2d getStartPose() {
//         return new RigidTransform2d(new Translation2d(120, 215), Rotation2d.fromDegrees(120.0));
//     }

//     @Override
//     public boolean isReversed() {
//         return false;
//     }
//     // WAYPOINT_DATA:
//     // [{"position":{"x":120,"y":215},"speed":0,"radius":0,"comment":""},{"position":{"x":90,"y":245},"speed":120,"radius":0,"comment":""}]
//     // IS_REVERSED: true
//     // FILE_NAME: GearToShootBlue
// }