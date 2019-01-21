package com.team766.frc2019.commands;

import com.team766.framework.Subroutine;

public class DriveSquare extends Subroutine {
    protected void subroutine(){
        //first side
        callSubroutine(new DriveStraight());
        
        //first corner
        callSubroutine(new TurnLeft());
        
        //second side
        callSubroutine(new DriveStraight());
        
        //second corner
        callSubroutine(new TurnLeft());
        
        //third side
        callSubroutine(new DriveStraight());
        
        //third corner
        callSubroutine(new TurnLeft());
        
        //fourth side
        callSubroutine(new DriveStraight());
        
        //fourth corner
        callSubroutine(new TurnLeft());
    }
}