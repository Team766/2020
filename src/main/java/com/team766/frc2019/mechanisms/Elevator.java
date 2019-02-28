package com.team766.frc2019.mechanisms;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team766.framework.Mechanism;
import com.team766.frc2019.Robot;
import com.team766.hal.CANSpeedController;
import com.team766.hal.DigitalInputReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.CANSpeedController.ControlMode;



public class Elevator extends Mechanism {

    private CANSpeedController m_lowerElevatorMotor;
    private CANSpeedController m_upperElevatorMotor;
    private DigitalInputReader lowerElevatorMinLimitSwitch;
    private DigitalInputReader lowerElevatorMaxLimitSwitch;
    private DigitalInputReader upperElevatorMinLimitSwitch;
    private DigitalInputReader upperElevatorMaxLimitSwitch;
    public static double DIST_PER_PULSE = Robot.drive.DIST_PER_PULSE;
    public static double targetDestination;

    // Changed minimums to 0, added very close min lower height to make the lower elevator have a better cushion 
    // BE CAREFUL! since the encoders go whack when the elevator gets to the top, it will probably forget where bottom is and ignore mins.
    // Even if the min is 500,000 the encoders could still end up off by ~500,000 and slam into bottom. Not sure if it is mechanical problem.
    public static int MIN_LOWER_HEIGHT = 0;
    public static int VERY_CLOSE_MIN_LOWER_HEIGHT = 100000;
	public static int NEAR_MIN_LOWER_HEIGHT = 400000;
    private static int NEAR_MAX_LOWER_HEIGHT = 1800000;
    private static int MAX_LOWER_HEIGHT = 2130000;

	public static int MIN_UPPER_HEIGHT = 0;
    public static int NEAR_MIN_UPPER_HEIGHT = 200000;
    private static int NEAR_MAX_UPPER_HEIGHT = 880000;
    private static int MAX_UPPER_HEIGHT = 920000;

    private static int MID_HEIGHT_BIG = 1000000;
	private static int MAX_HEIGHT_BIG = 1930000;
	private static int MID_HEIGHT_SMALL = 500000;
    private static int MAX_HEIGHT_SMALL = 900000;
    public static int MAX_COMBINED_HEIGHT = MAX_LOWER_HEIGHT + MAX_UPPER_HEIGHT;

    public static int LVL1 = 100000;
    public static int LVL2 = 1480000;
    public static int LVL3 = 3000000;
    
    private boolean sendCombinedRunning = false;
    public boolean combinedStopTargeting = false;
    public boolean upperStopTargeting = false;
    public boolean hovering = true;
    private int index = 0;

    public Elevator() {
        m_lowerElevatorMotor = RobotProvider.instance.getTalonCANMotor("elevator.elevatorMotor");
        m_upperElevatorMotor = RobotProvider.instance.getTalonCANMotor("elevator.actuatorMotor");
        lowerElevatorMinLimitSwitch = RobotProvider.instance.getDigitalInput("elevator.lowerMinLimitSwitch");
        lowerElevatorMaxLimitSwitch = RobotProvider.instance.getDigitalInput("elevator.lowerMaxLimitSwitch");
        upperElevatorMinLimitSwitch = RobotProvider.instance.getDigitalInput("elevator.upperMinLimitSwitch");
        upperElevatorMaxLimitSwitch = RobotProvider.instance.getDigitalInput("elevator.upperMaxLimitSwitch");
        m_lowerElevatorMotor.configFactoryDefault();
        m_upperElevatorMotor.configFactoryDefault();
        m_upperElevatorMotor.setInverted(true);
        m_lowerElevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_upperElevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_lowerElevatorMotor.setSensorPhase(true);
        m_upperElevatorMotor.setSensorPhase(false);
        m_lowerElevatorMotor.configNominalOutputForward(0.0);
        m_lowerElevatorMotor.configNominalOutputReverse(0.0);
        m_lowerElevatorMotor.configPeakOutputForward(1.0);
        m_lowerElevatorMotor.configPeakOutputReverse(-1.0);
        m_upperElevatorMotor.configNominalOutputForward(0.0);
        m_upperElevatorMotor.configNominalOutputReverse(0.0);
        m_upperElevatorMotor.configPeakOutputForward(1.0);
        m_upperElevatorMotor.configPeakOutputReverse(-1.0);
        m_lowerElevatorMotor.config_kP(0, 0.01, 0);
        m_lowerElevatorMotor.config_kI(0, 0.0, 0);
        m_lowerElevatorMotor.config_kD(0, 0.01, 0);
        m_lowerElevatorMotor.setFeedForward(0.0);
        m_upperElevatorMotor.config_kP(0, 0.01, 0);
        m_upperElevatorMotor.config_kI(0, 0.0, 0);
        m_upperElevatorMotor.config_kD(0, 0.01, 0);
        m_upperElevatorMotor.setFeedForward(0.0);
        m_lowerElevatorMotor.setNeutralMode(NeutralMode.Brake);
        m_upperElevatorMotor.setNeutralMode(NeutralMode.Brake);
        m_upperElevatorMotor.setPosition(0);
        m_lowerElevatorMotor.setPosition(0);
    }

    // Prevents lower elevator from lowering due to gravity
    public void hover() {
        sendLowerToDestination(Math.max(getLowerHeight(),0.0));
    }

    // Get limit switches   
    public boolean getLowerMinLimitSwitch() {
        return lowerElevatorMinLimitSwitch.get();
    }

    public boolean getLowerMaxLimitSwitch() {
        return lowerElevatorMaxLimitSwitch.get();
    }

    public boolean getUpperMinLimitSwitch() {
        return upperElevatorMinLimitSwitch.get();
    }

    public boolean getUpperMaxLimitSwitch() {
        return upperElevatorMaxLimitSwitch.get();
    }

    // Reset encoders    
    public void resetUpperEncoder() {
        m_upperElevatorMotor.setPosition(0);
    }

    public void resetLowerEncoder() {
        m_lowerElevatorMotor.setPosition(0);
    }

    // Sets elevator motors' power
    public void setLowerPower(double elevatorPower) {
        //System.out.println("*** setting lower power to " + elevatorPower + " ***");
        m_lowerElevatorMotor.set(ControlMode.PercentOutput, elevatorPower);
    }

    public void setUpperPower(double actuatorPower) {
        //System.out.println("*** setting upper power to " + actuatorPower + " ***");
        m_upperElevatorMotor.set(ControlMode.PercentOutput, actuatorPower);
    }

    // Moves motors until encoders read the set destination
    public void sendLowerToDestination(double destination) {
        targetDestination += destination - getLowerHeight();
        hovering = false;
        m_lowerElevatorMotor.set(ControlMode.Position, destination);
    }
    
    public void sendUpperToDestination(double destination) {
        targetDestination += destination - getUpperHeight();
        m_upperElevatorMotor.set(ControlMode.Position, destination);
    }

    // Revised sendCombined. (UNTESTED) Uses near slowing for each elevator seperately, so upper doesnt slow if lower is nearing.
    // Since it has been altered so drastically, be careful when testing this! It could be completely messed up, but I have examined it closely.
    public void sendCombinedToDestination(double destination) {
        if (sendCombinedRunning) {
           return;
        }
        sendCombinedRunning = true;
        double upperTarget = 95*destination/308;
        double lowerTarget = 213*destination/308;
        // Checks if elevator can actually reach the destination OR if elevator is being overrided by the manual combined buttons
        if (destination >= 0 && destination < (double)(MAX_COMBINED_HEIGHT) && !combinedStopTargeting) {
            System.out.println("Sending elevator to: " + destination + " upperTarget: " + upperTarget + " lowerTarget: " + lowerTarget);
            hovering = false;
            
            // Checks if the lower elevator has to GO DOWN and get close to the bottom (basically, if lower might crash into bottom)
            if (getLowerHeight() > lowerTarget && lowerTarget < NEAR_MIN_LOWER_HEIGHT) {
                System.out.println("lowerTarget close to bottom, activating near precautions");
                // Instead of using send to destination, lower elevator will use the down algorithm and slow when nearing
                while((getLowerHeight() > lowerTarget) && getLowerMinLimitSwitch()) {
                    if (getLowerHeight() > NEAR_MIN_LOWER_HEIGHT) {
                        setLowerPower(-1.0);
                    } else if (getLowerHeight() > VERY_CLOSE_MIN_LOWER_HEIGHT) {
                        setLowerPower(-0.4);
                    } else {
                        setLowerPower(-0.2);
                    }
                }
                // Once lower exits while loop (reaches lowerTarget), hover at current position (should be lowerTarget)
                hovering = true;
            } else {
                sendLowerToDestination(lowerTarget);
            }

            // Checks if the upper elevator has to GO DOWN and get close to the minimum
            if (getUpperHeight() > upperTarget && upperTarget < NEAR_MIN_UPPER_HEIGHT) {
                System.out.println("upperTarget close to bottom, activating near precautions");
                // Instead of using send to destination, upper elevator will use the down algorithm and slow when nearing
                while((getUpperHeight() > upperTarget) && getUpperMinLimitSwitch()) {
                    if (getUpperHeight() > NEAR_MIN_UPPER_HEIGHT) {
                        setUpperPower(-1.0);
                    } else {
                        setUpperPower(-0.4);
                    }
                }
                // Once upper exits while loop (reaches upperTarget), set power to 0.0 (brake)
                setUpperPower(0.0);
            } else {
                sendUpperToDestination(upperTarget);
            }

        } else {
            //  Only prints cannot reach if it actually can't reach. Before, it printed "cannot reach" even if it actually could and was just interrupted by manual buttons
            if (!combinedStopTargeting) {
                System.out.println("Cannot reach target destination");
            }
        }
        sendCombinedRunning = false;
    }

    // Adds to DESTINATION, not current position. If robot is not moving to a LVL already, simply adds to current position.
    public void addToDestination(double add) {
        // In testing, addToDestination may run multiple times with a short, quick button press
        System.out.println("Adding " + add);
        // If this print appears multiple times very quickly, we may need to find a way to make it not run again until button is RELEASED and then PRESSED again.
        if (combinedStopTargeting == true) {
            targetDestination = getLowerHeight() + getUpperHeight();
            combinedStopTargeting = false;
        } else if ((targetDestination + add) <= MAX_COMBINED_HEIGHT) {
            hovering = false;
            targetDestination += add;
            sendCombinedToDestination(targetDestination);
        } else if ((targetDestination + add) > MAX_COMBINED_HEIGHT) {
            System.out.println("Canceling addToDestination; would have sent above max height");
        }
    }

    // Returns encoder ticks of elevators
    public double getUpperHeight() {
       return m_upperElevatorMotor.getSensorPosition();
    }

    public double getLowerHeight() {
       return m_lowerElevatorMotor.getSensorPosition();
    }


    // Less complicated methods for setting elevators to destination. Allows us to control power, but is less reliable than sendToDestination
    public void lowerMoveTo(double position, double power) {
        while (getLowerHeight() != position) {
            if (getLowerHeight() > position) {
                setLowerPower(-power);
            } else {
                setLowerPower(power);
            }
        }
    }

    public void upperMoveTo(double position, double power) {
        while (getUpperHeight() != position) {
            if (getUpperHeight() > position) {
                setUpperPower(-power);
            } else {
                setUpperPower(power);
            }
        }
    }

    // Moves lower up, then upper starts moving up when lower is approaching max. Both elevators slow when nearing endpoints.
    public void elevatorUp() {
        combinedStopTargeting = true;
        if (index++ % 2000 == 0 && Robot.drive.isEnabled()) {
            System.out.println("LH: " + getLowerHeight() + " UH: " + getUpperHeight());
        }
        if (getLowerHeight() > NEAR_MAX_LOWER_HEIGHT) {
            if (getLowerHeight() >= MAX_LOWER_HEIGHT) {
                hovering = true;
            } else {
                hovering = false;
                setLowerPower(0.6);
            }
            // Upper movement is nested in lower so that upper moves while lower is nearing (to save time)
            if (getUpperHeight() >= MAX_UPPER_HEIGHT) {
                setUpperPower(0.0);
            } else if (getUpperHeight() > NEAR_MAX_UPPER_HEIGHT) {
                //System.out.println("UPPER NEARING DESTINATION");
                setUpperPower(0.6);
            } else {
                setUpperPower(1.0);
            }
        } else {
            hovering = false;
            setLowerPower(1.0);
        }
    }

    public void elevatorDown() {
        combinedStopTargeting = true;
        if (index++ % 2000 == 0 && Robot.drive.isEnabled()) {
            System.out.println("LH: " + getLowerHeight() + " UH: " + getUpperHeight());
        }
        if (getUpperHeight() <= NEAR_MIN_UPPER_HEIGHT) {
            if (getUpperHeight() <= MIN_UPPER_HEIGHT) {
                setUpperPower(0.0);
            } else {
                setUpperPower(-0.4);
            }
            if (getLowerHeight() <= MIN_LOWER_HEIGHT || !getLowerMinLimitSwitch()) {
                hovering = true;
            } else if (getLowerHeight() < NEAR_MIN_LOWER_HEIGHT) {
                //System.out.println("Nearing Bottom");
                hovering = false;
                setLowerPower(-0.3);
            }  else {
                hovering = false;
                setLowerPower(-0.9);
            }
        } else {
            setUpperPower(-1.0);
        }
    }

    // Only runs if not setting combined position. Resolves conflict between manual combined and presets
    public void elevatorNeutral() {
        if (combinedStopTargeting == true) {
            System.out.println("Setting elevator to neutral");
            setUpperPower(0.0);
            hovering = true;
            targetDestination = getLowerHeight() + getUpperHeight();
            combinedStopTargeting = false;
        }
    }

    // Only runs if not setting upper position. This resolves the conflict between manual upper and presets
    public void upperNeutral() {
        if (upperStopTargeting == true) {
            System.out.println("Setting upper to neutral");
            setUpperPower(0.0);
            targetDestination = getUpperHeight() + getLowerHeight();
            upperStopTargeting = false;
        }
    }

     // Replacement for LVL1, same function as elevatorDown method, but moves both elevators at the same time 
     public void bothElevatorsDown() {
        if(getUpperHeight() <= MIN_UPPER_HEIGHT) {
            setUpperPower(0.0); 
        } else if (getUpperHeight() < NEAR_MIN_UPPER_HEIGHT) {
            setUpperPower(-0.3);
        } else {
            setUpperPower(-1.0);
        }
        if (getLowerHeight() <= MIN_LOWER_HEIGHT) {
            hovering = true;
        } else if (getLowerHeight() < VERY_CLOSE_MIN_LOWER_HEIGHT) {
            hovering = false;
            setLowerPower(-0.1);
        } else if (getLowerHeight() < NEAR_MIN_LOWER_HEIGHT) {
            hovering = false;
            setLowerPower(-0.4);
        } else {
            hovering = false;
            setLowerPower(-1.0);
        }
    }


    /**
    * Move the elevator motor a set number of inches.
    **/
    public void moveLowerDistance(double distance) {
        m_lowerElevatorMotor.set(ControlMode.Position, distance / DIST_PER_PULSE);
    }

    /**
    * Move the gripper motor a set number of inches.
    **/
    public void moveUpperDistance(double distance) {
        m_upperElevatorMotor.set(ControlMode.Position, distance / DIST_PER_PULSE);
    }
}

