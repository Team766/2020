package com.team766.frc2020.commands;

import java.net.InetSocketAddress;
import com.team766.framework.Subroutine;
import com.team766.frc2020.paths.PathWebSocketServer;
import org.java_websocket.server.WebSocketServer;

import com.team766.frc2020.Robot;

public class WebSocketTest extends Subroutine {
    @Override
    protected void subroutine() {
        System.out.println("websockettest STARTING");

        WebSocketServer pathWebSocketServer = new PathWebSocketServer(new InetSocketAddress("10.7.66.2", 5801));
        
        pathWebSocketServer.start();

        int i = 0;



        while(true) {
            if (i % 100 == 0){
                System.out.println("broadcasted " + i);
                System.out.println("heading " + Robot.drive.getGyroAngle());
            }
            i++;
            pathWebSocketServer.broadcast("{\"position\": { \"x\": " + Robot.drive.getXPosition() + ", \"y\": " + Robot.drive.getYPosition() + "}}" );
            pathWebSocketServer.broadcast("{\"heading\": " + Robot.drive.getGyroAngle() + "}" );
            yield();
        }  
    }
}