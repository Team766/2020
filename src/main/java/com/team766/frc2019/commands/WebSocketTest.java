package com.team766.frc2019.commands;

import java.net.InetSocketAddress;
import com.team766.framework.Subroutine;
import com.team766.frc2019.paths.PathWebSocketServer;
import org.java_websocket.server.WebSocketServer;

public class WebSocketTest extends Subroutine {
    @Override
    protected void subroutine() {

        WebSocketServer pathWebsocketServer = new PathWebSocketServer(new InetSocketAddress("10.7.66.2", 5801));
        
        pathWebsocketServer.start();
        int i = 0;
        while(true) {
            if (i % 100 == 0){
                System.out.println("broadcasted");
                pathWebsocketServer.broadcast("hello");
            }
            yield();
        }  
    }
}