package com.team766.frc2019.commands;

import java.util.ArrayList;
import java.net.InetSocketAddress;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.java_websocket.server.WebSocketServer;

import com.team766.framework.Subroutine;
import com.team766.frc2019.Robot;
import com.team766.frc2019.paths.PathBuilder;
import com.team766.frc2019.paths.PathFollower;
import com.team766.frc2019.paths.Waypoint;
import com.team766.frc2019.paths.PathWebSocketServer;
import com.team766.hal.RobotProvider;
import com.team766.controllers.PIDController;
import com.team766.frc2019.mechanisms.Drive;
import com.team766.frc2019.commands.PreciseTurn;

// import com.team766.frc2019.mechanisms.LimeLightI;
// import com.team766.hal.RobotProvider;

public class WebSocketTest extends Subroutine {
    @Override
    protected void subroutine() {
        System.out.println("TurnAround STARTING");

        WebSocketServer pathWebsocketServer = new PathWebSocketServer(new InetSocketAddress("10.7.66.2", 5801));
        
        pathWebsocketServer.start();
        
        while(true) {
            System.out.println("broadcasted");
            pathWebsocketServer.broadcast("hello");
            yield();
        }  
    }
}