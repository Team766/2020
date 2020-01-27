package com.team766.frc2019.paths;

import java.util.ArrayList;

import com.team766.lib.util.control.Path;
import com.team766.lib.util.control.PathSegment;
import com.team766.lib.util.math.RigidTransform2d;
import com.team766.lib.util.math.Rotation2d;
import com.team766.lib.util.math.Translation2d;

public class PathBuilder {
    private static final double spacing = 6;
    private static final double kEpsilon = 1E-9;
    private static final double kReallyBigNumber = 1E9;

    /**
     * makes a smooth path given a few waypoints to follow
     * @param waypoints start and end waypoints as well as other positions to go to
     * @return final path
     */
    public static ArrayList<Waypoint> buildPath(ArrayList<Waypoint> waypoints) {
        ArrayList<Waypoint> newWaypoints = waypoints;
        newWaypoints = interpolateWaypoints(newWaypoints, spacing);
        newWaypoints = smoother(newWaypoints, .2, .8, 0.001);
        return(newWaypoints);
    }

    /**
     * adds points in between input points with set spacing
     * used before path smoothing to have enough points to smooth
     * @param waypoints waypoints to interpolate
     * @param spacing spacing between interpolated points
     * @return interpolated waypoints
     */
    public static ArrayList<Waypoint> interpolateWaypoints(ArrayList<Waypoint> waypoints, double spacing) {     //how's your day going?
        ArrayList<Waypoint> newPoints = new ArrayList<Waypoint>();
        for (int i = 0; i < waypoints.size() - 1; i++) {
            Vector vector = new Vector(waypoints.get(i + 1).x - waypoints.get(i).x, waypoints.get(i + 1).y - waypoints.get(i).y);
            int numberOfPointsThatFit = (int) Math.ceil(vector.magnitude() / spacing);
            vector = vector.normalize().scale(spacing);
            for (int j = 0; j < numberOfPointsThatFit; j++) {
                newPoints.add(new Waypoint(waypoints.get(i).x + vector.getX() * j, waypoints.get(i).y + vector.getY() * j, 0, 0));
            }
        } 
            newPoints.add(waypoints.get(waypoints.size() - 1));
        return newPoints;
    }

    /**
     * Smooths a path of points
     * Algorithm reference https://www.youtube.com/watch?v=v0-OUApP_5Q
     * https://www.youtube.com/watch?v=ffLPf8kI6qg
     * @param path
     * @param weight_data
     * @param weight_smooth
     * @param tolerance when to stop optimizing (lower value converges slower)
     */
    public static ArrayList<Waypoint> smoother(ArrayList<Waypoint> path, double weightData, double weightSmooth, double tolerance) {
        double weight_data = weightData;
        double weight_smooth = weightSmooth;
        double[][] pathMatrix = new double[path.size()][2];
        for (int i = 0; i < path.size(); i++) {
            pathMatrix[i][0] = path.get(i).getX();
            pathMatrix[i][1] = path.get(i).getY();

        }

        double[][] newPathMatrix = pathMatrix;

        for(int i = 0; i < pathMatrix.length; i++) {
            newPathMatrix[i] = pathMatrix[i].clone();
        }


        double change = tolerance;
        while(change >= tolerance) {
            change = 0.0;
            for(int i = 1; i < pathMatrix.length - 1; i++) {
                for(int j = 0; j < pathMatrix[i].length; j++) {
                    double aux = newPathMatrix[i][j];
                    newPathMatrix[i][j] += weight_data * (pathMatrix[i][j] - newPathMatrix[i][j]);
                    newPathMatrix[i][j] += weight_smooth * (newPathMatrix[i-1][j] + newPathMatrix[i+1][j] - (2.0 * newPathMatrix[i][j]));
                    change += Math.abs(aux - newPathMatrix[i][j]);
                }
            }
        }

        ArrayList<Waypoint> newPath = new ArrayList<Waypoint>();

        for (int i = 0; i < newPathMatrix.length; i++) {
            newPath.add(new Waypoint(newPathMatrix[i][0], newPathMatrix[i][1], 0, 0));
        }

        return newPath;
    }
    // public static ArrayList<Waypoint> smoother(ArrayList<Waypoint> path, double weight_data, double weight_smooth, double tolerance) {

		//copy array
        // ArrayList<Waypoint> newPath = new ArrayList<Waypoint>();
        // newPath = path;

        // double change = tolerance;

		// while(change >= tolerance) {
		// 	change = 0.0;
		// 	for(int i = 1; i < path.size()-1; i++) {
        //         System.out.println("i: " + i);
        //         double auxX = newPath.get(i).getX();
        //         double changedPathX = newPath.get(i).getX();
        //         changedPathX += weight_data * (path.get(i).getX() - newPath.get(i).getX()) + weight_smooth * (newPath.get(i-1).getX() + newPath.get(i+1).getX() - (2.0 * newPath.get(i).getX()));
        //         //System.out.println("changed path x: " + changedPathX);
        //         newPath.get(i).setX(changedPathX);
        //         change += Math.abs(auxX - changedPathX);
        //         System.out.println("changeX: " + change);	
                
        //         double auxY = newPath.get(i).getY();
        //         double changedPathY = newPath.get(i).getX();
        //         changedPathY += weight_data * (path.get(i).getY() - newPath.get(i).getY()) + weight_smooth * (newPath.get(i-1).getY() + newPath.get(i+1).getY() - (2.0 * newPath.get(i).getY()));
        //         //System.out.println("changed path y: " + changedPathX);
        //         newPath.get(i).setY(changedPathY);
        //         change += Math.abs(auxY - changedPathY);
        //         System.out.println("changeY: " + change);
        //     }
        // }

		// return newPath;

	// }
    

    private static Waypoint getPoint(ArrayList<Waypoint> w, int i) {
        if (i > w.size())
            return w.get(w.size() - 1);
        return w.get(i);
    }

    public static class Vector {
        static double x;
        static double y;
        Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public static double magnitude() {
            return(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
        }

        public static Vector normalize() {
            return(new Vector(x / magnitude(), y / magnitude()));
        }

        public static Vector scale(double scalar) {
            return(new Vector(x * scalar, y * scalar));
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    /**
     * A waypoint along a path. Contains a position, radius (for creating curved
     * paths), and speed. The information from these waypoints is used by the
     * PathBuilder class to generate Paths. Waypoints also contain an optional
     * marker that is used by the WaitForPathMarkerAction.
     *
     * @see GeneratePath
     */
    public static class Waypoint {
        Translation2d position;
        double x;
        double y;
        double radius;
        double speed;
        String marker;

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public void setX(double newX) {
            x = newX;

        }

        public void setY(double newY) {
            y = newY;
        }

        public Waypoint(Waypoint other) {
            this(other.position.x(), other.position.y(), other.radius, other.speed, other.marker);
        }

        public Waypoint(double x, double y, double r, double s) {
            // position = new Translation2d(x, y);
            this.x = x;
            this.y = y;
            radius = r;
            speed = s;
        }

        // public Waypoint(Translation2d pos, double r, double s) {
        //     position = pos;
        //     radius = r;
        //     speed = s;
        // }

        public Waypoint(double x, double y, double r, double s, String m) {
            // position = new Translation2d(x, y);
            this.x = x;
            this.y = y;
            radius = r;
            speed = s;
            marker = m;
        }
    }

    /**
     * An Arc object is formed by two Lines that share a common Waypoint. Contains a center position, radius, and speed.
     */
    static class Arc {
        Line a;
        Line b;
        Translation2d center;
        double radius;
        double speed;

        public Arc(Waypoint a, Waypoint b, Waypoint c) {
            this(new Line(a, b), new Line(b, c));
        }

        public Arc(Line a, Line b) {
            this.a = a;
            this.b = b;
            this.speed = (a.speed + b.speed) / 2;
            this.center = intersect(a, b);
            this.radius = new Translation2d(center, a.end).norm();
        }

        private void addToPath(Path p) {
            a.addToPath(p, speed);
            if (radius > kEpsilon && radius < kReallyBigNumber) {
                p.addSegment(new PathSegment(a.end.x(), a.end.y(), b.start.x(), b.start.y(), center.x(), center.y(),
                        speed, p.getLastMotionState(), b.speed));
            }
        }

        private static Translation2d intersect(Line l1, Line l2) {
            final RigidTransform2d lineA = new RigidTransform2d(l1.end, new Rotation2d(l1.slope, true).normal());
            final RigidTransform2d lineB = new RigidTransform2d(l2.start, new Rotation2d(l2.slope, true).normal());
            return lineA.intersection(lineB);
        }
    }

    /**
     * A Line object is formed by two Waypoints. Contains a start and end position, slope, and speed.
     */
    static class Line {
        Waypoint a;
        Waypoint b;
        Translation2d start;
        Translation2d end;
        Translation2d slope;
        double speed;

        public Line(Waypoint a, Waypoint b) {
            this.a = a;
            this.b = b;
            slope = new Translation2d(a.position, b.position);
            speed = b.speed;
            start = a.position.translateBy(slope.scale(a.radius / slope.norm()));
            end = b.position.translateBy(slope.scale(-b.radius / slope.norm()));
        }

        private void addToPath(Path p, double endSpeed) {
            double pathLength = new Translation2d(end, start).norm();
            if (pathLength > kEpsilon) {
                if (b.marker != null) {
                    p.addSegment(new PathSegment(start.x(), start.y(), end.x(), end.y(), b.speed,
                            p.getLastMotionState(), endSpeed, b.marker));
                } else {
                    p.addSegment(new PathSegment(start.x(), start.y(), end.x(), end.y(), b.speed,
                            p.getLastMotionState(), endSpeed));
                }
            }

        }
    }
}
