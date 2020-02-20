package com.team766.frc2020.paths;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;

import com.team766.frc2020.paths.Waypoint;

/**
 * Build paths from a few waypoints to use with path following
 */
public class PathBuilder {
    // values are measured in inches and seconds
    private static final double spacing = 6;
    private static final double maxVelocity = 1000;//360
    private static final double maxAcceleration = 500;

    /**
     * makes a smooth path given a few waypoints to follow
     * @param waypoints start and end waypoints as well as other positions to go to
     * @return final path
     */
    public static ArrayList<Waypoint> buildPath(ArrayList<Waypoint> waypoints) {
        ArrayList<Waypoint> newWaypoints = waypoints;
        newWaypoints = interpolateWaypoints(newWaypoints, spacing);
        newWaypoints = smoother(newWaypoints, .6, .4, 0.001);
        newWaypoints = calculateDistanceBetweenWaypoints(newWaypoints);
        newWaypoints = calculateCurvature(newWaypoints);
        newWaypoints = calculateMaximumVelocities(newWaypoints, 5);
        newWaypoints = calculateTargetVelocities(newWaypoints);
        newWaypoints = addEndBuffer(newWaypoints, 5);

        return(newWaypoints);
    }

    /**
     * adds points in between sequential input points with set spacing
     * used before path smoothing to have enough points to smooth
     * @param waypoints waypoints to interpolate
     * @param spacing spacing between interpolated points
     * @return interpolated waypoints
     */
    public static ArrayList<Waypoint> interpolateWaypoints(ArrayList<Waypoint> waypoints, double spacing) {
        ArrayList<Waypoint> newPoints = new ArrayList<Waypoint>();
        PathFollower pathFollower = new PathFollower(waypoints);

        for (int i = 0; i < waypoints.size() - 1; i++) {
            // compute vector between current point and next point
            Vector vector = new Vector(waypoints.get(i + 1).getX() - waypoints.get(i).getX(), waypoints.get(i + 1).getY() - waypoints.get(i).getY());

            int numberOfPointsThatFit = (int) Math.ceil(vector.magnitude() / spacing);

            // compute vector to add to current point
            vector = vector.normalize().scale(spacing);

            // add interpolated points into return ArrayList
            for (int j = 0; j < numberOfPointsThatFit; j++) {
                newPoints.add(new Waypoint(waypoints.get(i).getX() + vector.getX() * j, waypoints.get(i).getY() + vector.getY() * j));

            }
        } 
        newPoints.add(waypoints.get(waypoints.size() - 1));

        return newPoints;
    }

    /**
     * Smooths a path of points (order of points determines path)
     * Algorithm reference https://www.youtube.com/watch?v=v0-OUApP_5Q
     * https://www.youtube.com/watch?v=ffLPf8kI6qg
     * @param path
     * @param weightData
     * @param weightSmooth
     * @param tolerance when to stop optimizing (lower value converges slower)
     */
    public static ArrayList<Waypoint> smoother(ArrayList<Waypoint> inputPath, double weightData, double weightSmooth, double tolerance) {
        // copy path into 2d array
        double[][] path = new double[inputPath.size()][2];
        for (int i = 0; i < inputPath.size(); i++) {
            path[i][0] = inputPath.get(i).getX();
            path[i][1] = inputPath.get(i).getY();
        }

        // copy path into newPath
        double[][] newPath = new double[path.length][];

        for(int i = 0; i < path.length; i++) {
            newPath[i] = path[i].clone();
        }

        // gradient descent
        double change = tolerance;
        while(change >= tolerance) {
            change = 0.0;
            for(int i = 1; i < path.length - 1; i++) {
                for(int j = 0; j < path[i].length; j++) {
                    double aux = newPath[i][j];
                    newPath[i][j] += weightData * (path[i][j] - newPath[i][j]);
                    newPath[i][j] += weightSmooth * (newPath[i-1][j] + newPath[i+1][j] - (2.0 * newPath[i][j]));
                    change += Math.abs(aux - newPath[i][j]);
                }
            }
        }

        // convert 2d array back to ArrayList<Waypoint>
        ArrayList<Waypoint> outputPath = new ArrayList<Waypoint>();

        for (int i = 0; i < newPath.length; i++) {
            outputPath.add(new Waypoint(newPath[i][0], newPath[i][1]));
         }

        return outputPath;
    }

    /**
     * adds distances to totalDistanceFromFirstWaypoint variable
     * to each waypoint except the last one
     * @param inputPath
     * @return
     */
    public static ArrayList<Waypoint> calculateDistanceBetweenWaypoints(ArrayList<Waypoint> inputPath) {
        // copy input array into output array
        ArrayList<Waypoint> outputPath = new ArrayList<Waypoint>();
        Iterator<Waypoint> iterator = inputPath.iterator();
        while(iterator.hasNext()){
            outputPath.add((Waypoint)(iterator.next()).clone());
        }

        // acutal distance calculations
        outputPath.get(0).setTotalDistanceFromFirstWaypoint(0);
        for (int i = 1; i < inputPath.size(); i++) {
        // distance at point i = distance at point (i − 1) + distance_formula(point i, point (i − 1))
            outputPath.get(i).setTotalDistanceFromFirstWaypoint(
                outputPath.get(i - 1).getTotalDistanceFromFirstWaypoint() +
                Waypoint.calculateDistanceBetweenTwoWaypoints(outputPath.get(i), outputPath.get(i-1))
            );
        }

        return outputPath;
    }

    /**
     * calculates curvature at each point based on circumradius of the
     * 2 points next to the point
     */
    public static ArrayList<Waypoint> calculateCurvature(ArrayList<Waypoint> inputPath) {
        // copy input array into output array
        ArrayList<Waypoint> outputPath = new ArrayList<Waypoint>();
        Iterator<Waypoint> iterator = inputPath.iterator();
        while(iterator.hasNext()){
            outputPath.add((Waypoint)(iterator.next()).clone());
        }

        // curvature calculations
        outputPath.get(0).setCurvature(0);
        outputPath.get(outputPath.size() - 1).setCurvature(0);
        for (int i = 1; i < inputPath.size() - 1; i++) {
            outputPath.get(i).setCurvature(
                1 / calculateCircumradius(outputPath.get(i - 1), outputPath.get(i), outputPath.get(i + 1))
            );
        }

        return outputPath;
    }

    /**
     * used to calculate circumradius for determining maximum velocity during turns
     * @param pointOne
     * @param pointTwo
     * @param pointThree
     * @return
     */
    public static double calculateCircumradius(Waypoint pointA, Waypoint pointB, Waypoint pointC) {
        double lengthAB = Waypoint.calculateDistanceBetweenTwoWaypoints(pointA, pointB);
        double lengthBC = Waypoint.calculateDistanceBetweenTwoWaypoints(pointB, pointC);
        double lengthAC = Waypoint.calculateDistanceBetweenTwoWaypoints(pointA, pointC);
        return (lengthAB * lengthBC * lengthAC) / (4 * heronsFormula(lengthAB, lengthBC, lengthAC));
    }

    public static double heronsFormula(double lengthA, double lengthB, double lengthC) {
        double semiPerimeter = (lengthA + lengthB + lengthC) / 2;
        return Math.sqrt(semiPerimeter * (semiPerimeter - lengthA) * (semiPerimeter - lengthB) * (semiPerimeter - lengthC));
    }
    
    /**
     * calculates maximum velocity the robot can go based on the curvature of
     * the points and some constants
     * @param inputPath
     * @param turnVelocityConstant value between 1 - 5 (lower value is slower turn velocity)
     */
    public static ArrayList<Waypoint> calculateMaximumVelocities(ArrayList<Waypoint> inputPath, double turnVelocityConstant) {
        // copy input array into output array
        ArrayList<Waypoint> outputPath = new ArrayList<Waypoint>();
        Iterator<Waypoint> iterator = inputPath.iterator();
        while(iterator.hasNext()){
            outputPath.add((Waypoint)(iterator.next()).clone());
        }

        // max velocity calculations
        for (int i = 0; i < inputPath.size(); i++) {
            outputPath.get(i).setVelocity(Math.min(maxVelocity, turnVelocityConstant / outputPath.get(i).getCurvature()));
        }

        return outputPath;
    }

    /**
     * Calculates target velocities based on curvature values in inputPath
     */
    public static ArrayList<Waypoint> calculateTargetVelocities(ArrayList<Waypoint> inputPath) {
        // copy input array into output array
        ArrayList<Waypoint> outputPath = new ArrayList<Waypoint>();
        Iterator<Waypoint> iterator = inputPath.iterator();
        while(iterator.hasNext()){
            outputPath.add((Waypoint)(iterator.next()).clone());
        }

        outputPath.get(outputPath.size() - 1).setVelocity(0);
        // target velocity calculations
        for (int i = inputPath.size() - 2; i >= 0; i--) {
            outputPath.get(i).setVelocity(
                Math.min(
                outputPath.get(i).getVelocity(),
                    Math.sqrt(
                        Math.pow(
                            outputPath.get(i + 1).getVelocity(), 2) +
                            2 * maxAcceleration * Waypoint.calculateDistanceBetweenTwoWaypoints(outputPath.get(i), outputPath.get(i + 1)
                        )
                    )
                )
            );
        }
        return outputPath;
    }

    /**
     * add points to end to make robot actually reach last goal waypoint from path
     * @param buffer num extra waypoints to add
     * @return input path plus #buffer collinear points
     */
    public static ArrayList<Waypoint> addEndBuffer(ArrayList<Waypoint> inputPath, int buffer) {
        double deltaX = inputPath.get(inputPath.size()-1).getX() - inputPath.get(inputPath.size()-2).getX();
        double deltaY = inputPath.get(inputPath.size()-1).getY() - inputPath.get(inputPath.size()-2).getY();
        for (int i = 0; i < buffer; i++) {
            inputPath.add(new Waypoint(inputPath.get(inputPath.size()-1).getX() + i*deltaX, inputPath.get(inputPath.size()-1).getY() + i*deltaY));
         }
        return inputPath;
    }
}
