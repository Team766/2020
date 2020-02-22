package com.team766.frc2020.paths;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;

public class PathWebSocketServer extends WebSocketServer {
    ArrayList<Waypoint> path;
	public PathWebSocketServer(InetSocketAddress address, ArrayList<Waypoint> path) {
        super(address);
        this.path = path;
    }
    
    public PathWebSocketServer(InetSocketAddress address) {
        super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// conn.send("Welcome to the server!"); //This method sends a message to the new client
		// broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
		System.out.println("new connection to " + conn.getRemoteSocketAddress());
        broadcastPath(path);
    }

    public void broadcastPath(ArrayList<Waypoint> path) {
        String pathString = "";
        pathString += "{\"path\": [";
        
        for (int i = 0; i < path.size(); i++) {
            pathString += "{\"x\": " + path.get(i).getX() + ", \"y\": " + path.get(i).getY() + "},";
        }
        pathString = pathString.substring(0, pathString.length() - 1) + "]}";

        System.out.println("path string " + pathString);
        broadcast(pathString);
    }

    public void broadcastPosition(double xPosition, double yPosition) {
        broadcast("{\"position\": { \"x\": " + xPosition + ", \"y\": " + yPosition + "}}" );
    }

    public void broadcastHeading(double heading) {
        broadcast("{\"heading\": " + heading + "}" );
    }

    @Override
    public void broadcast(String text) {
        super.broadcast(text);
    }

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		System.out.println("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
	}
	
	@Override
	public void onStart() {
		System.out.println("server started successfully");
    }
}