package com.team766.frc2019.commands;
import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.hal.JoystickReader;

public class TestBoxop extends Subroutine {
	private static int INTAKE_ACTUATE = 2;
	//private static int INTAKE_ACTUATE = 7;
	private static int INTAKE_RETRACT = 3;
	//private static int INTAKE_RETRACT = 8;
	private static int INTAKE_OPEN = 4;
	//private static int INTAKE_OPEN = 5;	
	private static int INTAKE_CLOSE = 5;
	//private static int INTAKE_CLOSE = 6;
	private static int ELEVATOR_UP = 9;
	//private static int ELEVATOR_UP = 1;
	private static int ELEVATOR_DOWN = 10;
	//private static int ELEVATOR_DOWN = 2;
	private static int ELEVATOR_UP_SMALL = 7;
	//private static int ELEVATOR_UP_SMALL = 3;
	private static int ELEVATOR_DOWN_SMALL = 8;
	//private static int ELEVATOR_DOWN_SMALL = 4;
	private static int ELEVATOR_LVL3 = 11;
	private static int ELEVATOR_LVL2 = 12;
    private static int ELEVATOR_LVL1 = 13;
    
    private JoystickReader m_joystick1;
	private JoystickReader m_joystick2;
    private JoystickReader m_boxop;
    
    public TestBoxop() {
        
//        takeControl(Robot.flowerActuator);
    }

    @Override
    protected void subroutine() {
        if(m_boxop.getRawButton(INTAKE_ACTUATE) ) {
			System.out.println("Intake actuate - " + INTAKE_ACTUATE);
		} else if(m_boxop.getRawButton(INTAKE_RETRACT) ) {
			System.out.println("Intake retract - " + INTAKE_RETRACT);
		} else if(m_boxop.getRawButton(INTAKE_OPEN) ) {
			System.out.println("Intake open - " + INTAKE_OPEN);
		} else if(m_boxop.getRawButton(INTAKE_CLOSE) ) {
			System.out.println("Intake close - " + INTAKE_CLOSE);
		} else if(m_boxop.getRawButton(ELEVATOR_UP) ) {
			System.out.println("Elevator up - " + ELEVATOR_UP);
		} else if(m_boxop.getRawButton(ELEVATOR_DOWN) ) {
			System.out.println("Elevator down - " + ELEVATOR_DOWN);
		} else if(m_boxop.getRawButton(ELEVATOR_UP_SMALL )) {
			System.out.println("Elevator up small - " + ELEVATOR_DOWN_SMALL);
		} else if(m_boxop.getRawButton(ELEVATOR_DOWN_SMALL) ) {
			System.out.println("Elevator down small - " + ELEVATOR_DOWN_SMALL);
		} else if(m_boxop.getRawButton(ELEVATOR_LVL3) ) {
			System.out.println("Elevator level 3 - " + INTAKE_ACTUATE);
		} else if(m_boxop.getRawButton(ELEVATOR_LVL2) ) {
			System.out.println("Elevator level 2 - " + ELEVATOR_LVL2);
		} else if(m_boxop.getRawButton(ELEVATOR_LVL1) ) {
			System.out.println("Elevator level 1 - " + ELEVATOR_LVL1);
        }
    }
}
