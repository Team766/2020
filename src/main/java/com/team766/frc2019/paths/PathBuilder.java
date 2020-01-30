package com.team766.frc2019.paths;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Math;

// import com.team766.lib.util.control.Path;
// import com.team766.lib.util.control.PathSegment;
// import com.team766.lib.util.math.RigidTransform2d;
// import com.team766.lib.util.math.Rotation2d;
// import com.team766.lib.util.math.Translation2d;

/**
 * Build paths from a few weaypoints to use with path following
 */
public class PathBuilder {
    private static final double spacing = 6;
    private static final double maxSpeed = 10;
    // private static final double kEpsilon = 1E-9;
    // private static final double kReallyBigNumber = 1E9;

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

        for (int i = 0; i < waypoints.size() - 1; i++) {
            // compute vector between current point and next point
            Vector vector = new Vector(waypoints.get(i + 1).x - waypoints.get(i).x, waypoints.get(i + 1).y - waypoints.get(i).y);

            int numberOfPointsThatFit = (int) Math.ceil(vector.magnitude() / spacing);

            // compute vector to add to current point
            vector = vector.normalize().scale(spacing);

            // add interpolated points into return ArrayList
            for (int j = 0; j < numberOfPointsThatFit; j++) {
                newPoints.add(new Waypoint(waypoints.get(i).x + vector.getX() * j, waypoints.get(i).y + vector.getY() * j));
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
                calculateDistanceBetweenTwoWaypoints(outputPath.get(i), outputPath.get(i-1))
            );
        }

        return outputPath;
    }

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
        double lengthAB = calculateDistanceBetweenTwoWaypoints(pointA, pointB);
        double lengthBC = calculateDistanceBetweenTwoWaypoints(pointB, pointC);
        double lengthAC = calculateDistanceBetweenTwoWaypoints(pointA, pointC);
        return (lengthAB * lengthBC * lengthAC) / (4 * heronsFormula(lengthAB, lengthBC, lengthAC));
    }

    public static double calculateDistanceBetweenTwoWaypoints(Waypoint pointA, Waypoint pointB) {
        return Math.sqrt(Math.pow(pointA.getX() - pointB.getX(), 2) + Math.pow(pointA.getY() - pointB.getY(), 2));
    }

    public static double heronsFormula(double lengthA, double lengthB, double lengthC) {
        double semiPerimeter = (lengthA + lengthB + lengthC) / 2;
        return Math.sqrt(semiPerimeter * (semiPerimeter - lengthA) * (semiPerimeter - lengthB) * (semiPerimeter - lengthC));
    }

    

    public static ArrayList<Waypoint> calculateSpeeds(ArrayList<Waypoint> inputPath, double turnSpeedConstant) {
        // copy input array into output array
        ArrayList<Waypoint> outputPath = new ArrayList<Waypoint>();
        Iterator<Waypoint> iterator = inputPath.iterator();
        while(iterator.hasNext()){
            outputPath.add((Waypoint)(iterator.next()).clone());
        }

        // curvature calculations
        for (int i = 1; i < inputPath.size(); i++) {
            outputPath.get(i).setSpeed(Math.min(maxSpeed, turnSpeedConstant / outputPath.get(i).getCurvature()));
        }

        return outputPath;
    }

    // you can delete me, I am deprecated and no longer needed to calculateSpeed
    public static double[] generateVelocities(ArrayList<Waypoint> waypts) {
        // this will generate output power level for the robot (N.B.: Power!=Velocity!)
        double[] velocities = new double[waypts.size()];  
        double k1;
        double k2;
        double b;
        double a;
        double r;
        double power;
        for (int i=1; i<waypts.size()-1; i++){
            // power inversely related with curvature
            k1 = 0.5*(Math.pow(waypts.get(i-1).x, 2) + Math.pow(waypts.get(i-1).y, 2)-(Math.pow(waypts.get(i).x, 2) + Math.pow(waypts.get(i).y, 2)))/(waypts.get(i-1).x - waypts.get(i).x + 0.0001);
            k2 = (waypts.get(i-1).y - waypts.get(i).y)/(waypts.get(i-1).x - waypts.get(i).x);
            b = 0.5*(Math.pow(waypts.get(i).x, 2) - 2*waypts.get(i).x*k1 + Math.pow(waypts.get(i).y, 2) - Math.pow(waypts.get(i+1).x, 2) + 2*waypts.get(i+1).x*k1 - Math.pow(waypts.get(i+1).y, 2))/(waypts.get(i+1).x*k2 - waypts.get(i+1).y + waypts.get(i).y - waypts.get(i).x*k2);
            a = k1 - k2*b;
            r = Math.sqrt(Math.pow(waypts.get(i-1).x - a, 2) + Math.pow(waypts.get(i-1).y - b, 2));

            power = 1/(1 + Math.exp(-1*r)); // needs to be scaled correctly
            velocities[i] = power;
        } 
        return velocities;
    }

    // private static Waypoint getPoint(ArrayList<Waypoint> w, int i) {
    //     if (i > w.size())
    //         return w.get(w.size() - 1);
    //     return w.get(i);
    // }

    public static class Vector {
        private double x;
        private double y;
        Vector(double x, double y) {
            this.setX(x);
            this.setY(y);
        }
        
        public double magnitude() {
            return(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
        }

        public Vector normalize() {
            return(new Vector(x / magnitude(), y / magnitude()));
        }

        public  Vector scale(double scalar) {
            return(new Vector(x * scalar, y * scalar));
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
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
        // Translation2d position;
        private double x;
        private double y;
        private double curvature;
        // double radius;
        // double speed;

        private double speed;

        // used for calculating velocity and curvature
        // must be entered manually
        private double totalDistanceFromFirstWaypoint;
        // String marker;

        // public Waypoint(Waypoint other) {
        //     this(other.position.x(), other.position.y(), other.radius, other.speed, other.marker);
        // }

        // public Waypoint(double x, double y, double r, double s) {
        //     // position = new Translation2d(x, y);
        //     this.setX(x);
        //     this.setY(y);
        //     radius = r;
        //     speed = s;
        // }

        public Waypoint() {};

        public Waypoint(double x, double y) {
            this.x = x;
            this.y = y;
        };

        // public Waypoint(Translation2d pos, double r, double s) {
        //     position = pos;
        //     radius = r;
        //     speed = s;
        // }

        // public Waypoint(double x, double y, double r, double s, String m) {
        //     // position = new Translation2d(x, y);
        //     this.x = x;
        //     this.y = y;
        //     radius = r;
        //     speed = s;
        //     // marker = m;
        // }

        public Waypoint clone(){
            Waypoint waypoint = new Waypoint();

            waypoint.setX(this.getX());
            waypoint.setY(this.getY());
            waypoint.setTotalDistanceFromFirstWaypoint(this.getTotalDistanceFromFirstWaypoint());
            return waypoint;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getTotalDistanceFromFirstWaypoint() {
            return this.totalDistanceFromFirstWaypoint;
        }

        public void setTotalDistanceFromFirstWaypoint(double totalDistanceFromFirstWaypoint) {
            this.totalDistanceFromFirstWaypoint = totalDistanceFromFirstWaypoint;
        }

        public double getCurvature() {
            return this.curvature;
        }

        public void setCurvature(double curvature) {
            this.curvature = curvature;
        }

        public double getSpeed() {
            return this.speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }

//     /**
//      * An Arc object is formed by two Lines that share a common Waypoint. Contains a center position, radius, and speed.
//      */
//     static class Arc {
//         Line a;
//         Line b;
//         Translation2d center;
//         double radius;
//         double speed;

//         public Arc(Waypoint a, Waypoint b, Waypoint c) {
//             this(new Line(a, b), new Line(b, c));
//         }

//         public Arc(Line a, Line b) {
//             this.a = a;
//             this.b = b;
//             this.speed = (a.speed + b.speed) / 2;
//             this.center = intersect(a, b);
//             this.radius = new Translation2d(center, a.end).norm();
//         }

//         private void addToPath(Path p) {
//             a.addToPath(p, speed);
//             if (radius > kEpsilon && radius < kReallyBigNumber) {
//                 p.addSegment(new PathSegment(a.end.x(), a.end.y(), b.start.x(), b.start.y(), center.x(), center.y(),
//                         speed, p.getLastMotionState(), b.speed));
//             }
//         }

//         private static Translation2d intersect(Line l1, Line l2) {
//             final RigidTransform2d lineA = new RigidTransform2d(l1.end, new Rotation2d(l1.slope, true).normal());
//             final RigidTransform2d lineB = new RigidTransform2d(l2.start, new Rotation2d(l2.slope, true).normal());
//             return lineA.intersection(lineB);
//         }
//     }

//     /**
//      * A Line object is formed by two Waypoints. Contains a start and end position, slope, and speed.
//      */
//     static class Line {
//         Waypoint a;
//         Waypoint b;
//         Translation2d start;
//         Translation2d end;
//         Translation2d slope;
//         double speed;

//         public Line(Waypoint a, Waypoint b) {
//             this.a = a;
//             this.b = b;
//             slope = new Translation2d(a.position, b.position);
//             speed = b.speed;
//             start = a.position.translateBy(slope.scale(a.radius / slope.norm()));
//             end = b.position.translateBy(slope.scale(-b.radius / slope.norm()));
//         }

//         private void addToPath(Path p, double endSpeed) {
//             double pathLength = new Translation2d(end, start).norm();
//             if (pathLength > kEpsilon) {
//                 if (b.marker != null) {
//                     p.addSegment(new PathSegment(start.x(), start.y(), end.x(), end.y(), b.speed,
//                             p.getLastMotionState(), endSpeed, b.marker));
//                 } else {
//                     p.addSegment(new PathSegment(start.x(), start.y(), end.x(), end.y(), b.speed,
//                             p.getLastMotionState(), endSpeed));
//                 }
//             }

//         }
//     }
}
