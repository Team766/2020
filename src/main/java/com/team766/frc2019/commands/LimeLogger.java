package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.RobotProvider;

public class LimeLogger extends Subroutine{
    public LimeLogger() {
        takeControl(Robot.drive);
    }
    final double speed = 0.16;
    final double stop = 1.0;

    protected void subroutine() {
        /*LimeLight.setPipeline(1);
        double distance = LimeLight.ta();
        center2();
        waitForSeconds(0.1);
        
        System.out.println("DONE, FINAL ANGLE: " + skew());
       */
        /*final double start = RobotProvider.instance.getClock().getTime();
        while (RobotProvider.instance.getClock().getTime() - start < 30.0) {
            System.out.println(skew());
            waitForSeconds(1.0);
        }*/
        center2();
        
        

        
    }
    public double cX() {
        waitForSeconds(0.1);
        LimeLight.setPipeline(1);
        return LimeLight.tx();
    }

    public double angleE() {
        double raw[];
        raw = new double[8];

        boolean discard = false;;

        int n = 0;
        int count = 0;
        double average = 0.0;

        for (int i = 0; i < 8; i++) {
            raw[i] = skew();
            n += (90 + raw[i] <= 2.5) ? 1 : 0;
        }
        
        if (n < 7) {
            discard = true;
        }

        for (int i = 0; i < 8; i++) {
            if (discard) {
                if (Math.abs(raw[i]) - 90 > 1) {
                    average += raw[i];
                    count++;
                }
            } else {
                average += raw[i];
                count++;
            }
        }
        return average / count;
        

    }
    
    public double a = 0.125;

    public double skew() {
        LimeLight.setPipeline(2);
        waitForSeconds(a);
        double l = LimeLight.ta();
        l = LimeLight.ta();
        yield();
        LimeLight.setPipeline(3);
        waitForSeconds(a);
        double r = LimeLight.ta();
        r = LimeLight.ta();
        return 90.0 * ((l > r) ? (-1.0 * r)/l : l/r);
    }

    public void center() {
        
    }

    public void center2() {
        LimeLight.setPipeline(1);
        double e = 0.0025;
        double x = LimeLight.tx();
        double a = LimeLight.ta();
        while (a < stop){
            a = LimeLight.ta();
            x = LimeLight.tx();
            if (x > 0) {
                Robot.drive.setDrivePower(-speed - (x * x * e), -speed);
            }
            if (x < 0) {
                Robot.drive.setDrivePower(-speed, -speed - (x * x * e));
            }
            yield();
        }
        Robot.drive.setDrivePower(0.0, 0.0);
    }

    public void center3() {
        LimeLight.setPipeline(1);
        double e = 0.0025;
        double x = LimeLight.tx();
        double a = LimeLight.ta();
        boolean f = LimeLight.isTarget();
        while (true) {
            while (a < stop){
                f = LimeLight.isTarget();
                a = LimeLight.ta();
                while(!f) {
                    Robot.drive.setDrivePower(-0.2, 0.0);
                    f = LimeLight.isTarget();
                    yield();
                }    
                while (f) {
                    x = LimeLight.tx();
                    f = LimeLight.isTarget();
                    if (x > 0) {
                        Robot.drive.setDrivePower(-speed - (x * x * e), -speed);
                    }
                    if (x < 0) {
                        Robot.drive.setDrivePower(-speed, -speed - (x * x * e));
                    }
                    yield();
                }
            }
        }
        /*waitForSeconds(1.0);
        double fin = Math.abs(skew());
        waitForSeconds(1.0);
        fin = 90 - fin;
        System.out.println(fin);
        Robot.drive.setDrivePower(0.2,-0.2);
        waitForSeconds(0.3);
        Robot.drive.setDrivePower(-0.2, -0.2);
        waitForSeconds(0.0014*fin*fin);
        Robot.drive.setDrivePower(0.0, 0.0);
        waitForSeconds(1.0);
        Robot.drive.setDrivePower(-0.2,0.0);
        while (!LimeLight.isTarget()) {
        }
        x = LimeLight.tx();
        while (Math.abs(x) > 0.5){
            x = LimeLight.tx();
        }
        waitForSeconds(0.2);
        Robot.drive.setDrivePower(0.0, 0.0);
        */
    }

    public void center4() {
        waitForSeconds(0.2);
        LimeLight.setPipeline(2);
        double l = LimeLight.ta();
        waitForSeconds(0.2);
        LimeLight.setPipeline(3);
        double r = LimeLight.ta();
        double target = (r > l) ? r : l;
        LimeLight.setPipeline((r > l) ? 3 : 2);
        double x = LimeLight.tx();
        double a = LimeLight.ta();
        double e = 0.0003;
        double t = 26 - (LimeLight.thor() / 2.0);
        while (a < 6.0){
            a = LimeLight.ta();
            x = LimeLight.tx();
            if (x > t) {
                Robot.drive.setDrivePower(-speed - (x * x * e), 0.0);
            }
            if (x < t) {
                //Robot.drive.setDrivePower(0.0, -speed - (x * x * e));
            }
            yield();
        }


    }


    public double distanceBlunt() {
        LimeLight.setPipeline(1);
        return 2.604918 - (0.3757189/0.2250643)*(1 - Math.pow(Math.E,(-0.2250643*LimeLight.ty())));
    }

    public void backup(double d) {
        LimeLight.setPipeline(1);
        double a = LimeLight.ta();
        while (a > d){
            a = LimeLight.ta();
            double e = 0.0003;
            double x = LimeLight.tx();   
            if (x > 0) {
                Robot.drive.setDrivePower(speed + (x * x * e), speed);
            }
            if (x < 0) {
                Robot.drive.setDrivePower(speed, speed + (x * x * e));
            }
            yield();
        }
    }
}