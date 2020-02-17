package com.team766.frc2019.commands;

import java.net.InetSocketAddress;
import com.team766.framework.Subroutine;
import com.team766.frc2019.paths.PathWebSocketServer;
import org.java_websocket.server.WebSocketServer;

import com.team766.frc2019.Robot;

public class WebSocketTest extends Subroutine {
    @Override
    protected void subroutine() {
        System.out.println("websockettest STARTING");

        WebSocketServer pathWebsocketServer = new PathWebSocketServer(new InetSocketAddress("10.7.66.2", 5801));
        
        pathWebsocketServer.start();

        int i = 0;
        while(true) {
            if (i % 100 == 0){
                System.out.println("broadcasted");
            }
            pathWebsocketServer.broadcast("{\"position\": { \"x\": " + Robot.drive.getXPosition() + ", \"y\": " + Robot.drive.getYPosition() + "}}" );
            yield();
        }  
    }
}